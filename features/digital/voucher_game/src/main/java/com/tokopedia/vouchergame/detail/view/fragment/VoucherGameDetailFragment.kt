package com.tokopedia.vouchergame.detail.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.utils.AnalyticUtils
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalExtraParam.EXTRA_PARAM_VOUCHER_GAME
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.VoucherGameAnalytics
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.detail.data.VoucherGameEnquiryFields
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import com.tokopedia.vouchergame.detail.di.VoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapter
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameProductDecorator
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder
import com.tokopedia.vouchergame.detail.view.viewmodel.VoucherGameDetailViewModel
import com.tokopedia.vouchergame.detail.widget.OperatorInfoBottomSheets
import com.tokopedia.vouchergame.detail.widget.ProductDetailBottomSheets
import com.tokopedia.vouchergame.detail.widget.VoucherGameEnquiryResultWidget
import com.tokopedia.vouchergame.detail.widget.VoucherGameInputFieldWidget
import com.tokopedia.vouchergame.list.view.model.VoucherGameOperatorAttributes
import kotlinx.android.synthetic.main.fragment_voucher_game_detail.*
import kotlinx.android.synthetic.main.fragment_voucher_game_detail.btn_info_icon
import kotlinx.android.synthetic.main.fragment_voucher_game_detail.view.*
import kotlinx.android.synthetic.main.view_voucher_game_input_field.view.*
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by resakemal on 16/08/19.
 */
class VoucherGameDetailFragment: BaseTopupBillsFragment(),
        BaseListAdapter.OnAdapterInteractionListener<Visitable<*>>,
        VoucherGameDetailAdapter.LoaderListener,
        VoucherGameProductViewHolder.OnClickListener,
        TopupBillsCheckoutWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var voucherGameViewModel: VoucherGameDetailViewModel

    lateinit var adapter: VoucherGameDetailAdapter

    lateinit var selectedProduct: VoucherGameProduct

    lateinit var voucherGameExtraParam: VoucherGameExtraParam
    lateinit var voucherGameOperatorData: VoucherGameOperatorAttributes

    @Inject
    lateinit var voucherGameAnalytics: VoucherGameAnalytics

    lateinit var enquiryData: List<VoucherGameEnquiryFields>
    var inputData: MutableMap<String, String> = mutableMapOf()
    var isEnquired = false
        set(value) {
            field = value
            setInputFieldsError(!value)
            toggleCheckoutButton()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            voucherGameViewModel = viewModelProvider.get(VoucherGameDetailViewModel::class.java)

            //Setup adapter
            adapter = VoucherGameDetailAdapter(it, resources,
                    VoucherGameDetailAdapterFactory(this),
                    this)
        }

        arguments?.let {
            voucherGameExtraParam = it.getParcelable(EXTRA_PARAM_VOUCHER_GAME) ?: VoucherGameExtraParam()
            voucherGameOperatorData =
                    it.getParcelable(EXTRA_PARAM_OPERATOR_DATA) ?: VoucherGameOperatorAttributes()
            it.getString(EXTRA_INPUT_FIELD_1)?.let { input -> inputData[EXTRA_INPUT_FIELD_1] = input }
            it.getString(EXTRA_INPUT_FIELD_2)?.let { input -> inputData[EXTRA_INPUT_FIELD_2] = input }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        voucherGameViewModel.voucherGameProducts.observe(this, Observer {
            it.run {
                input_field_container_shimmering.visibility = View.GONE
                when(it) {
                    is Success -> {
                        adapter.hideLoading()

                        setupEnquiryFields(it.data)
                        checkAutoFillInput()

                        renderProducts(it.data)
                        checkAutoSelectProduct()
                    }
                    is Fail -> {
                        adapter.showGetListError(it.throwable)
                    }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_voucher_game_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        adapter.showLoading()
        loadData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (input_field_1.getInputText().isNotEmpty()) outState.putString(EXTRA_INPUT_FIELD_1, input_field_1.getInputText())
        if (input_field_2.getInputText().isNotEmpty()) outState.putString(EXTRA_INPUT_FIELD_2, input_field_2.getInputText())
        if (::selectedProduct.isInitialized) voucherGameExtraParam.productId = selectedProduct.id
        outState.putParcelable(EXTRA_PARAM_VOUCHER_GAME, voucherGameExtraParam)
        outState.putParcelable(EXTRA_PARAM_OPERATOR_DATA, voucherGameOperatorData)
    }

    private fun initView() {
        setupOperatorDetail()

        // Show input fields shimmering layout
        input_field_container.visibility = View.GONE
        input_field_container_shimmering.visibility = View.VISIBLE

        recycler_view.adapter = adapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(p0: Int): Int {
                return when (adapter.getItemViewType(p0)) {
                    VoucherGameProductViewHolder.LAYOUT -> FULL_SCREEN_SPAN_SIZE
                    else -> PRODUCT_ITEM_SPAN_SIZE
                }
            }
        }
        recycler_view.layoutManager = layoutManager
        while (recycler_view.itemDecorationCount > 0) recycler_view.removeItemDecorationAt(0)
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val productData = voucherGameViewModel.voucherGameProducts.value
                    if (productData is Success) {
                        val visibleIndexes = AnalyticUtils.getVisibleItemIndexesOfType(recycler_view, VoucherGameProductViewHolder.LAYOUT)
                        voucherGameAnalytics.impressionProductCard(
                                getAllProductsList(productData.data).subList(visibleIndexes.first, visibleIndexes.second + 1), voucherGameOperatorData.name)
                    }
                }
            }
        })

        checkout_view.setVisibilityLayout(false)
        checkout_view.setListener(this)
        toggleCheckoutButton()
    }

    override fun processEnquiry(data: TelcoEnquiryData) {
        toggleEnquiryLoadingBar(false)
        isEnquired = true
        renderEnquiryResult(data.enquiry.attributes.mainInfoList)
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        if (data.catalog.label.isNotEmpty()) {
            voucherGameAnalytics.categoryName = data.catalog.label
            (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        }
    }

    override fun showError(t: Throwable) {
        toggleEnquiryLoadingBar(false)
        isEnquired = false
        NetworkErrorHelper.createSnackbarRedWithAction(
                activity, ErrorHandler.getErrorMessage(context, t)) { enquireFields() }.showRetrySnackbar()
    }

    private fun setupEnquiryFields(data: VoucherGameDetailData) {
        // Hide input fields if there is no fields
        if (!data.needEnquiry || data.enquiryFields.isEmpty()) {
            inputFieldCount = 0
            isEnquired = true
            input_field_container.visibility = View.GONE
        }
        else {
            enquiryData = data.enquiryFields
            inputFieldCount = enquiryData.size

            // Show first input field (guaranteed to have an input field)
            val firstField = enquiryData[0]
            input_field_1.setLabel(firstField.text)
            input_field_1.setHint(firstField.placeholder)

            input_field_1.ac_input.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    voucherGameAnalytics.eventInputNumber()
                }
                false
            }

            // Hide second field if there is only one field, setup second field otherwise
            when (inputFieldCount) {
                1 -> input_field_2.visibility = View.GONE
                2 -> {
                    val secondField = enquiryData[1]
                    input_field_2.setLabel(secondField.text)
                    input_field_2.setHint(secondField.placeholder)
                }
            }
            input_field_container.visibility = View.VISIBLE

            // Enquire if all required fields are filled
            input_field_1.setListener(object : VoucherGameInputFieldWidget.ActionListener {
                override fun onFinishInput() {
                    enquireFields()
                }
            })
            if (inputFieldCount == 2) {
                input_field_2.setListener(object : VoucherGameInputFieldWidget.ActionListener {
                    override fun onFinishInput() {
                        enquireFields()
                    }
                })
            }
        }
    }

    private fun checkAutoFillInput() {
        if (inputFieldCount in 1..2 && ::enquiryData.isInitialized && inputData.isNotEmpty()) {
            inputData[EXTRA_INPUT_FIELD_1]?.let { input -> input_field_1.setInputText(input) }
            inputData[EXTRA_INPUT_FIELD_2]?.let { input -> input_field_2.setInputText(input) }
            if (inputData.size == inputFieldCount) enquireFields()
        }
    }

    private fun enquireFields() {
        if (inputFieldCount in 1..2 && ::enquiryData.isInitialized) {
            val input1 = input_field_1.getInputText()
            val input2 = input_field_2.getInputText()

            // Add case when user is still filling the fields (only 1/2 fields are filled)
            if (inputFieldCount == 2 && (input1.isEmpty() xor input2.isEmpty())) {
                isEnquired = false
                setInputFieldsError(false)
                return
            }

            // Verify fields
            var isValid: Boolean
            isValid = verifyField(enquiryData[0].validations, input1)
            if (isValid && inputFieldCount == 2) {
                isValid = verifyField(enquiryData[1].validations, input2)
            }

            if (isValid) {
            // Enquiry query is not ready, temporarily validate enquiry
                isEnquired = true
//                toggleEnquiryLoadingBar(true)
//                val clientNumber = if (input2.isNotEmpty()) "${input1}_${input2}" else input1
//                getEnquiry(clientNumber, voucherGameExtraParam.operatorId)
            } else {
                isEnquired = false
            }
        }
    }

    private fun toggleEnquiryLoadingBar(state: Boolean) {
        enquiry_loading_bar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun verifyField(fieldValidation: List<VoucherGameEnquiryFields.Validation>,
                            input: String): Boolean {
        if (input.isEmpty()) return false
        for (validation in fieldValidation) {
            if (validation.rule.isNotEmpty() && !Pattern.matches(validation.rule, input)) {
                return false
            }
        }
        return true
    }

    private fun setInputFieldsError(value: Boolean) {
        context?.run {
            if (value) {
                input_field_label.text = getString(R.string.vg_input_field_error_message)
                input_field_label.setTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.red_600))
            } else {
                input_field_label.visibility = View.GONE
            }
        }
    }

    private fun renderProducts(data: VoucherGameDetailData) {
        val dataCollection = data.product.dataCollections
        if (dataCollection.isEmpty()) adapter.showEmpty()
        else {
            val listData = mutableListOf<Visitable<*>>()
            val decorator = VoucherGameProductDecorator(resources.getDimensionPixelOffset(ITEM_DECORATOR_SIZE))
            val trackingList = mutableListOf<VoucherGameProduct>()

            var hasMoreDetails = false
            for (productList in dataCollection) {
                // Create new instance to prevent adding copy of products
                // to adapter data (set products to empty list)
                if (productList.name.isNotEmpty()) {
                    val categoryItem = VoucherGameProductData.DataCollection(productList.name, listOf())
                    listData.add(categoryItem)
                }

                if (productList.products.isNotEmpty())  {
                    listData.addAll(productList.products)
                    trackingList.addAll(productList.products)
                    if (!hasMoreDetails && productList.products.filter { it.attributes.detail.isNotEmpty() }.isNotEmpty()) {
                        hasMoreDetails = true
                    }
                }
            }
            adapter.hasMoreDetails = hasMoreDetails
            recycler_view.addItemDecoration(decorator)

            adapter.renderList(listData)
//            recycler_view.post {
//                val productData = voucherGameViewModel.voucherGameProducts.value
//                if (productData is Success) {
//                    val visibleIndexes = AnalyticUtils.getVisibleItemIndexesOfType(recycler_view, VoucherGameProductViewHolder.LAYOUT)
//                    voucherGameAnalytics.impressionProductCard(
//                            getAllProductsList(productData.data).subList(visibleIndexes.first, visibleIndexes.second + 1))
//                }
//            }
        }
    }

    private fun renderEnquiryResult(results: List<TopupBillsEnquiryMainInfo>) {
        input_field_label.visibility = View.INVISIBLE

        context?.run {
            for (result in results) {
                val resultView = VoucherGameEnquiryResultWidget(this)
                resultView.setEnquiryResult(result.label, result.value)
                enquiry_result_container.addView(resultView)
            }
        }
    }

    private fun setupOperatorDetail() {
        // Enlarge info button touch area with TouchDelegate
        operator_detail_container.post {
            val delegateArea = Rect()
            btn_info_icon.getHitRect(delegateArea)

            delegateArea.top -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.left -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.bottom += INFO_TOUCH_AREA_SIZE_PX
            delegateArea.right += INFO_TOUCH_AREA_SIZE_PX

            operator_detail_container.apply { touchDelegate = TouchDelegate(delegateArea, btn_info_icon) }
        }

        if (::voucherGameOperatorData.isInitialized) {
            voucherGameOperatorData.run {
                product_name.text = name
                ImageHandler.LoadImage(product_image, imageUrl)

                product_image.setOnClickListener { showProductInfo(name, description) }
                btn_info_icon.setOnClickListener {
                    voucherGameAnalytics.eventClickInfoButton()
                    showProductInfo(name, description)
                }
                help_label.text = voucherGameOperatorData.helpCta
                help_label.setOnClickListener { showProductInfo(desc = helpText, imageUrl = helpImage) }
            }
        }
    }

    private fun showProductInfo(title: String = "", desc: String, imageUrl: String = "") {
        activity?.let {
            val operatorInfoBottomSheets = OperatorInfoBottomSheets()
            operatorInfoBottomSheets.title = title
            operatorInfoBottomSheets.description = desc
            operatorInfoBottomSheets.imageUrl = imageUrl
            operatorInfoBottomSheets.show(it.supportFragmentManager, TAG_VOUCHER_GAME_INFO)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(VoucherGameDetailComponent::class.java).inject(this)
    }

    override fun loadData() {
        voucherGameExtraParam.menuId.toIntOrNull()?.let {
            voucherGameViewModel.getVoucherGameProducts(GraphqlHelper.loadRawString(resources,
                    R.raw.query_voucher_game_product_detail),
                    voucherGameViewModel.createParams(it, voucherGameExtraParam.operatorId))
        }
    }

    override fun onItemClicked(item: Visitable<*>) {
        // no op
    }

    override fun onItemClicked(product: VoucherGameProduct, position: Int) {
        if (::voucherGameOperatorData.isInitialized) {
            voucherGameAnalytics.eventClickProductCard(product, voucherGameOperatorData.name)
        }
        selectProduct(product, position)
    }

    override fun onDetailClicked(product: VoucherGameProduct) {
        activity?.let {
            val productDetailBottomSheets = ProductDetailBottomSheets()
            with(product.attributes) {
                productDetailBottomSheets.title = desc
                productDetailBottomSheets.description = detail
                productDetailBottomSheets.urlLabel = detailUrlText
                productDetailBottomSheets.url = detailUrl
            }
            productDetailBottomSheets.show(it.supportFragmentManager, TAG_VOUCHER_GAME_INFO)
        }
    }

    private fun checkAutoSelectProduct() {
        if (voucherGameExtraParam.productId.isNotEmpty()) {
            // Find product with the corresponding product id then select it
            for (i in adapter.data.indices) {
                if (adapter.getItemViewType(i) == VoucherGameProductViewHolder.LAYOUT) {
                    val product = adapter.data[i] as VoucherGameProduct
                    if (product.id == voucherGameExtraParam.productId) {
                        selectProduct(product, i)

                        // Show error to notify the user to fill in the fields
                        setInputFieldsError(true)
                    }
                }
            }
        }
    }

    private fun selectProduct(product: VoucherGameProduct, position: Int) {
        // Show selected item in list
        if (::selectedProduct.isInitialized && product == selectedProduct) return

        adapter.setSelectedProduct(position)
        // Update selected product
        selectedProduct = product

        showCheckoutView()
    }

    private fun showCheckoutView() {
        checkout_view.setVisibilityLayout(true)
        checkout_view.setTotalPrice(selectedProduct.attributes.promo?.newPrice ?: selectedProduct.attributes.price)
        // Try to enquire if currently not enquired
        enquireFields()
    }

    private fun toggleCheckoutButton() {
        checkout_view.setBuyButtonState(isEnquired)
    }

    override fun onClickNextBuyButton() {
        if (isEnquired) {
            if (::voucherGameOperatorData.isInitialized) {
                voucherGameAnalytics.eventClickBuy(voucherGameExtraParam.categoryId,
                        voucherGameOperatorData.name, product = selectedProduct)
            }
            processCheckout()
        }
    }

    private fun processCheckout() {
        // Setup checkout pass data
        if (::voucherGameExtraParam.isInitialized && ::selectedProduct.isInitialized) {
            var checkoutPassDataBuilder = DigitalCheckoutPassData.Builder()
                    .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                    .categoryId(voucherGameExtraParam.categoryId)
                    .instantCheckout("0")
                    .isPromo(if (selectedProduct.attributes.promo != null) "1" else "0")
                    .operatorId(voucherGameExtraParam.operatorId)
                    .productId(selectedProduct.id)
                    .utmCampaign(voucherGameExtraParam.categoryId)
                    .utmContent(GlobalConfig.VERSION_NAME)
                    .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                    .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                    .voucherCodeCopied("")
            if (inputFieldCount in 1..2) {
                checkoutPassDataBuilder = checkoutPassDataBuilder.clientNumber(input_field_1.getInputText())
            }
            if (inputFieldCount == 2) {
                checkoutPassDataBuilder = checkoutPassDataBuilder.zoneId(input_field_2.getInputText())
            }
            checkoutPassData = checkoutPassDataBuilder.build()

            processToCart()
        }
    }

    private fun getAllProductsList(data: VoucherGameDetailData): List<VoucherGameProduct> {
        val productList = mutableListOf<VoucherGameProduct>()
        for (dataCollection in data.product.dataCollections) {
            productList.addAll(dataCollection.products)
        }
        return productList
    }

    companion object {

        var inputFieldCount = 0

        val ITEM_DECORATOR_SIZE = com.tokopedia.design.R.dimen.dp_6
        const val INFO_TOUCH_AREA_SIZE_PX = 20

        const val FULL_SCREEN_SPAN_SIZE = 1
        const val PRODUCT_ITEM_SPAN_SIZE = 2

        const val EXTRA_PARAM_OPERATOR_DATA = "EXTRA_PARAM_OPERATOR_DATA"
        const val EXTRA_INPUT_FIELD_1 = "EXTRA_INPUT_FIELD_1"
        const val EXTRA_INPUT_FIELD_2 = "EXTRA_INPUT_FIELD_2"
        const val TAG_VOUCHER_GAME_INFO = "voucherGameInfo"

        fun newInstance(voucherGameExtraParam: VoucherGameExtraParam,
                        voucherGameOperatorAttributes: VoucherGameOperatorAttributes): Fragment {
            val fragment = VoucherGameDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_VOUCHER_GAME, voucherGameExtraParam)
            bundle.putParcelable(EXTRA_PARAM_OPERATOR_DATA, voucherGameOperatorAttributes)
            fragment.arguments = bundle
            return fragment
        }
    }
}