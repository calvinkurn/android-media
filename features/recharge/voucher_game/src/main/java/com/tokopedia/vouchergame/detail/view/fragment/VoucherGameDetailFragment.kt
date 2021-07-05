package com.tokopedia.vouchergame.detail.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.product.CatalogOperatorAttributes
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.common.topupbills.utils.AnalyticUtils
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget.Companion.SHOW_KEYBOARD_DELAY
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.common.constant.DigitalExtraParam.EXTRA_PARAM_VOUCHER_GAME
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.VoucherGameAnalytics
import com.tokopedia.vouchergame.common.util.VoucherGameGqlQuery
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import com.tokopedia.vouchergame.detail.di.VoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapter
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameProductDecorator
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder
import com.tokopedia.vouchergame.detail.view.viewmodel.VoucherGameDetailViewModel
import com.tokopedia.vouchergame.detail.widget.OperatorInfoWidget
import com.tokopedia.vouchergame.detail.widget.ProductDetailWidget
import com.tokopedia.vouchergame.detail.widget.VoucherGameEnquiryResultWidget
import kotlinx.android.synthetic.main.fragment_voucher_game_detail.*
import kotlinx.android.synthetic.main.fragment_voucher_game_detail.view.*
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

    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    lateinit var adapter: VoucherGameDetailAdapter

    private var selectedProduct: VoucherGameProduct? = null
        set(value) {
            field = value
            if (value != null) {
                productId = value.id.toIntOrNull() ?: 0
                productName = value.attributes.desc
                price = value.attributes.pricePlain.toIntOrNull() ?: 0
                checkVoucherWithDelay()
            }
        }

    lateinit var voucherGameExtraParam: VoucherGameExtraParam
    lateinit var voucherGameOperatorData: CatalogOperatorAttributes

    @Inject
    lateinit var voucherGameAnalytics: VoucherGameAnalytics

    lateinit var enquiryData: List<CatalogProductInput>
    var inputData: MutableMap<String, String> = mutableMapOf()
    private var inputFieldCount = 0
    var isEnquired = false
        set(value) {
            field = value
            setInputFieldsError(!value)
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
            // Initalize variables of base topup bills fragment
            menuId = voucherGameExtraParam.menuId.toIntOrNull() ?: 0
            categoryId = voucherGameExtraParam.categoryId.toIntOrNull() ?: 0
            productId = voucherGameExtraParam.productId.toIntOrNull() ?: 0

            voucherGameOperatorData =
                    it.getParcelable(EXTRA_PARAM_OPERATOR_DATA) ?: CatalogOperatorAttributes()
            it.getString(EXTRA_INPUT_FIELD_1)?.let { input -> inputData[EXTRA_INPUT_FIELD_1] = input }
            it.getString(EXTRA_INPUT_FIELD_2)?.let { input -> inputData[EXTRA_INPUT_FIELD_2] = input }
        }

        operatorName = voucherGameOperatorData.name
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        voucherGameViewModel.voucherGameProducts.observe(viewLifecycleOwner, Observer {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Scroll view to top
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CART_DIGITAL && !isEnquired) {
            vg_detail_scroll_view.smoothScrollTo(0, vg_detail_scroll_view.top)
        }
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
        selectedProduct?.run { voucherGameExtraParam.productId = id }
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
        checkout_view.listener = this
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        toggleEnquiryLoadingBar(false)
        isEnquired = true
        renderEnquiryResult(data.enquiry.attributes.mainInfoList)
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        super.processMenuDetail(data)
        if (data.catalog.label.isNotEmpty()) {
            categoryName = data.catalog.label
            voucherGameAnalytics.categoryName = categoryName
            (activity as? BaseSimpleActivity)?.updateTitle(data.catalog.label)
        }
    }

    override fun processFavoriteNumbers(data: TopupBillsFavNumber) {

    }

    override fun onEnquiryError(error: Throwable) {
        toggleEnquiryLoadingBar(false)
        isEnquired = false
        view?.let {
            Toaster.build(it, ErrorHandler.getErrorMessage(context, error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.resources.common.R.string.general_label_ok),
                    View.OnClickListener { enquireFields() }).show()
        }
    }

    override fun onMenuDetailError(error: Throwable) {

    }

    override fun onLoadingMenuDetail(showLoading: Boolean) {
        //do nothing
    }

    override fun onLoadingAtc(showLoading: Boolean) {
        checkout_view.onBuyButtonLoading(showLoading)
    }

    override fun onCatalogPluginDataError(error: Throwable) {

    }

    override fun onFavoriteNumbersError(error: Throwable) {

    }

    override fun onCheckVoucherError(error: Throwable) {
        view?.let { v ->
            Toaster.build(v, ErrorHandler.getErrorMessage(requireContext(), error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
        }
    }

    override fun onExpressCheckoutError(error: Throwable) {
        view?.let { v ->
            Toaster.build(v, ErrorHandler.getErrorMessage(requireContext(), error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
        }
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
            // Chcek if input count is valid
            if (enquiryData.size in INPUT_COUNT_MIN..INPUT_COUNT_MAX) inputFieldCount = enquiryData.size

            if (hasFirstInput()) {
                // Show first input field
                setupEnquiryField(input_field_1, enquiryData[0])
                // Hide second field if there is only one field, setup second field otherwise
                if (hasSecondInput()) {
                    setupEnquiryField(input_field_2, enquiryData[1])
                }
                input_field_container.visibility = View.VISIBLE
            }
        }
    }

    private fun setupEnquiryField(field: TopupBillsInputFieldWidget, data: CatalogProductInput) {
        field.visibility = View.VISIBLE
        field.setLabel(data.text)
        field.setHint(data.placeholder)
        field.setInputType(data.style)

        var dropdownData: List<TopupBillsInputDropdownData> = listOf()
        if (data.style == INPUT_DROPDOWN_PARAM) {
            field.isCustomInput = true
            dropdownData = data.dataCollections.map { item -> TopupBillsInputDropdownData(item.value) }
        }

        // Enquire if all required fields are filled
        field.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
            override fun onFinishInput(input: String) {
                onReceiveInput(input)
            }

            override fun onCustomInputClick() {
                if (field.isCustomInput && dropdownData.isNotEmpty()) { showInputDropdown(field, dropdownData) }
            }

            override fun onTextChangeInput() {
                //do nothing
            }
        }
    }

    private fun checkAutoFillInput() {
        if (::enquiryData.isInitialized && hasInputs() && inputData.isNotEmpty()) {
            inputData[EXTRA_INPUT_FIELD_1]?.let { input ->
                if (hasFirstInput()) input_field_1.setInputText(input)
            }
            inputData[EXTRA_INPUT_FIELD_2]?.let { input ->
                if (hasSecondInput()) input_field_2.setInputText(input)
            }
            if (inputData.size == inputFieldCount) enquireFields()
        }
    }

    private fun onReceiveInput(input: String) {
        if (input.isNotEmpty()) {
            voucherGameAnalytics.eventInputNumber()
            enquireFields()
        }
    }

    private fun enquireFields() {
        if (::enquiryData.isInitialized) {
            val input1 = input_field_1.getInputText()
            val input2 = input_field_2.getInputText()
            var isValid = false

            if (hasFirstInput()) {
                isValid = verifyField(enquiryData[0].validations, input1)
            }
            if (hasSecondInput()) {
                // Add case when user is still filling the fields (only 1/2 fields are filled)
                if (input1.isEmpty() xor input2.isEmpty()) {
                    isEnquired = false
                    setInputFieldsError(false)
                    return
                }

                if (isValid) {
                    isValid = verifyField(enquiryData[1].validations, input2)
                }
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

    private fun showInputDropdown(field: TopupBillsInputFieldWidget, data: List<TopupBillsInputDropdownData>) {
        context?.let { context ->
            val dropdownBottomSheet = BottomSheetUnify()
            dropdownBottomSheet.setFullPage(true)
            dropdownBottomSheet.clearAction()
            dropdownBottomSheet.setCloseClickListener {
                dropdownBottomSheet.dismiss()
            }

            val dropdownView = TopupBillsInputDropdownWidget(context, listener = object : TopupBillsInputDropdownWidget.OnClickListener{
                override fun onItemClicked(item: TopupBillsInputDropdownData) {
                    field.setInputText(item.label)
                }
            }, selected = field.getInputText())
            dropdownView.setData(data)
            dropdownBottomSheet.setChild(dropdownView)

            fragmentManager?.run { dropdownBottomSheet.show(this,"Enquiry input field dropdown bottom sheet") }
            // Open keyboard with delay so it opens when bottom sheet is fully visible
            Handler().postDelayed({
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }, SHOW_KEYBOARD_DELAY)
        }
    }

    private fun toggleEnquiryLoadingBar(state: Boolean) {
        enquiry_loading_bar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun verifyField(fieldValidation: List<CatalogProductInput.Validation>,
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
                input_field_label.setTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_R600))
            } else {
                input_field_label.visibility = View.GONE
            }
        }
    }

    private fun renderProducts(data: VoucherGameDetailData) {
        val dataCollection = data.product.dataCollections as List<VoucherGameProductData.DataCollection>
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
            val productInfoBottomSheet = BottomSheetUnify()
            productInfoBottomSheet.setCloseClickListener {
                productInfoBottomSheet.dismiss()
            }

            val productInfoWidget = OperatorInfoWidget(it)
            productInfoWidget.title = title
            productInfoWidget.description = desc
            productInfoWidget.imageUrl = imageUrl
            productInfoBottomSheet.setChild(productInfoWidget)

            fragmentManager?.run {
                productInfoBottomSheet.show(this, "Voucher template product info")
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(VoucherGameDetailComponent::class.java).inject(this)
    }

    override fun loadData() {
        voucherGameExtraParam.menuId.toIntOrNull()?.let {
            getMenuDetail(it)
            voucherGameViewModel.getVoucherGameProducts(VoucherGameGqlQuery.voucherGameProducts,
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
            val productDetailBottomSheet = BottomSheetUnify()
            with(product.attributes) {
                productDetailBottomSheet.setTitle(desc)
                productDetailBottomSheet.setCloseClickListener {
                    productDetailBottomSheet.dismiss()
                }

                val productDetailWidget = ProductDetailWidget(it)
                productDetailWidget.description = detail
                productDetailWidget.url = detailUrl
                productDetailWidget.urlLabel = detailUrlText
                productDetailBottomSheet.setChild(productDetailWidget)
            }

            fragmentManager?.run {
                productDetailBottomSheet.show(this, "Voucher template product detail")
            }
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
        if (selectedProduct != null && product == selectedProduct) return

        adapter.setSelectedProduct(position)
        // Update selected product
        selectedProduct = product

        showCheckoutView()
    }

    private fun showCheckoutView() {
        checkout_view.setVisibilityLayout(true)
        selectedProduct?.attributes?.run {
            checkout_view.setTotalPrice(promo?.newPrice ?: price)
        }
        // Try to enquire if currently not enquired
        enquireFields()
    }

    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return checkout_view
    }

    override fun initAddToCartViewModel() {
        addToCartViewModel = viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java)
    }

    override fun onClickNextBuyButton() {
        processCheckoutData()
    }

    private fun processCheckoutData() {
        // Setup checkout pass data
        if (::voucherGameExtraParam.isInitialized) {
            selectedProduct?.run {
                var checkoutPassDataBuilder = getDefaultCheckoutPassDataBuilder()
                        .categoryId(voucherGameExtraParam.categoryId)
                        .isPromo(if (attributes.promo != null) "1" else "0")
                        .operatorId(voucherGameExtraParam.operatorId)
                        .productId(id)
                        .utmCampaign(voucherGameExtraParam.categoryId)

                if (hasFirstInput()) {
                    checkoutPassDataBuilder = checkoutPassDataBuilder.clientNumber(input_field_1.getInputText())
                }
                if (hasSecondInput()) {
                    checkoutPassDataBuilder = checkoutPassDataBuilder.zoneId(input_field_2.getInputText())
                }
                checkoutPassData = checkoutPassDataBuilder.build()
            }
        }

        val inputs = inputData
        inputs[TopupBillsViewModel.ENQUIRY_PARAM_OPERATOR_ID] = voucherGameExtraParam.operatorId
        inputFields = inputs

        processTransaction()
    }

    private fun getAllProductsList(data: VoucherGameDetailData): List<VoucherGameProduct> {
        val productList = mutableListOf<VoucherGameProduct>()
        for (dataCollection in data.product.dataCollections) {
            productList.addAll(dataCollection.products)
        }
        return productList
    }

    private fun hasInputs(): Boolean {
        return inputFieldCount > 0
    }

    private fun hasFirstInput(): Boolean {
        return hasNthInput(1)
    }

    private fun hasSecondInput(): Boolean {
        return hasNthInput(2)
    }

    private fun hasNthInput(n: Int): Boolean {
        return inputFieldCount >= n
    }

    companion object {
        val ITEM_DECORATOR_SIZE = R.dimen.voucher_game_dp_6
        const val INFO_TOUCH_AREA_SIZE_PX = 20

        const val FULL_SCREEN_SPAN_SIZE = 1
        const val PRODUCT_ITEM_SPAN_SIZE = 2

        const val INPUT_COUNT_MIN = 0
        const val INPUT_COUNT_MAX = 2

        const val EXTRA_PARAM_OPERATOR_DATA = "EXTRA_PARAM_OPERATOR_DATA"
        const val EXTRA_INPUT_FIELD_1 = "EXTRA_INPUT_FIELD_1"
        const val EXTRA_INPUT_FIELD_2 = "EXTRA_INPUT_FIELD_2"
        const val TAG_VOUCHER_GAME_INFO = "voucherGameInfo"

        const val INPUT_DROPDOWN_PARAM = "select_dropdown"

        fun newInstance(voucherGameExtraParam: VoucherGameExtraParam,
                        voucherGameOperatorAttributes: CatalogOperatorAttributes): Fragment {
            val fragment = VoucherGameDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_VOUCHER_GAME, voucherGameExtraParam)
            bundle.putParcelable(EXTRA_PARAM_OPERATOR_DATA, voucherGameOperatorAttributes)
            fragment.arguments = bundle
            return fragment
        }
    }
}