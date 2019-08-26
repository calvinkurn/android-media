package com.tokopedia.vouchergame.detail.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam.EXTRA_PARAM_TELCO
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.detail.data.VoucherGameEnquiryFields
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import com.tokopedia.vouchergame.detail.di.VoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameProductDecorator
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder
import com.tokopedia.vouchergame.detail.view.viewmodel.VoucherGameDetailViewModel
import com.tokopedia.vouchergame.detail.widget.VoucherGameBottomSheets
import com.tokopedia.vouchergame.detail.widget.VoucherGameInputFieldWidget
import kotlinx.android.synthetic.main.fragment_voucher_game_detail.*
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by resakemal on 16/08/19.
 */
class VoucherGameDetailFragment: BaseTopupBillsFragment<Visitable<*>,
        VoucherGameDetailAdapterFactory>(),
        BaseEmptyViewHolder.Callback,
        VoucherGameProductViewHolder.OnClickListener,
        TopupBillsCheckoutWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var voucherGameViewModel: VoucherGameDetailViewModel

    lateinit var selectedProduct: VoucherGameProduct

    lateinit var voucherGameExtraParam: VoucherGameExtraParam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            voucherGameViewModel = viewModelProvider.get(VoucherGameDetailViewModel::class.java)
        }

        arguments?.let {
            voucherGameExtraParam = it.getParcelable(EXTRA_PARAM_TELCO)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        voucherGameViewModel.voucherGameProducts.observe(this, Observer {
            it.run {
                when(it) {
                    is Success -> {
                        renderEnquiryFields(it.data)
                        renderProducts(it.data)

                        checkAutoSelectProduct()
                    }
                    is Fail -> {
                        showGetListError(it.throwable)
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_PARAM_TELCO, voucherGameExtraParam)
    }

    private fun initView() {
        recycler_view.addItemDecoration(VoucherGameProductDecorator(ITEM_DECORATOR_SIZE, resources))

        checkout_view.setVisibilityLayout(false)
        checkout_view.setListener(this)
    }

    private fun renderEnquiryFields(data: VoucherGameDetailData) {
        // Hide input fields if there is no fields
        if (!data.needEnquiry) input_field_container.visibility = View.GONE
        else {
            val fields = data.enquiryFields

            // Show first input field (guaranteed to have an input field)
            val firstField = fields[0]
            input_field_1.setLabel(firstField.name)
            input_field_1.setHint(firstField.name)

            // Hide second field if there is only one field, setup second field otherwise
            if (fields.size == 1) {
                input_field_2.visibility = View.GONE
            } else if (fields.size == 2) {
                val secondField = fields[1]
                input_field_2.setLabel(secondField.name)
                input_field_2.setHint(secondField.name)
            }
        }

        // Enquire when all required input fields are filled
        if (input_field_1.visibility == View.VISIBLE) {
            input_field_1.setListener(object : VoucherGameInputFieldWidget.ActionListener {
                override fun onEditorActionDone() {
                    val input1 = input_field_1.getInputText()
                    val input2 = input_field_2.getInputText()

                    // If first input field is filled, check if second input field is required (and is filled)
                    if (input1.isNotEmpty()) {
                        if (input_field_2.visibility == View.VISIBLE && input2.isNotEmpty()) {
                            enquireFields(data.enquiryFields, input1, input2)
                        } else {
                            enquireFields(data.enquiryFields, input1)
                        }
                    }
                }
            })
        }
        if (input_field_2.visibility == View.VISIBLE) {
            input_field_2.setListener(object : VoucherGameInputFieldWidget.ActionListener {
                override fun onEditorActionDone() {
                    val input1 = input_field_1.getInputText()
                    val input2 = input_field_2.getInputText()

                    // Both input fields are required, check if both are filled
                    if (input1.isNotEmpty() && input2.isNotEmpty()) {
                        enquireFields(data.enquiryFields, input1, input2)
                    }
                }
            })
        }
    }

    private fun enquireFields(enquiryData: List<VoucherGameEnquiryFields>,
                              input1: String,
                              input2: String? = null) {

        // Verify fields
        var isValid: Boolean
        isValid = verifyField(enquiryData[0].validations, input1)
        if (isValid && input2 != null) {
            isValid = verifyField(enquiryData[1].validations, input2)
        }

        if (isValid) {
            // Reset error label
            setInputFieldsError(false)

            // TODO: Add enquiry api call
        } else {
            // Set error message
            setInputFieldsError(true)
        }
    }

    private fun verifyField(fieldValidation: List<VoucherGameEnquiryFields.Validation>,
                            input: String): Boolean {
        for (validation in fieldValidation) {
            if (!Pattern.matches(validation.rule, input)) {
                return false
            }
        }
        return true
    }

    private fun setInputFieldsError(value: Boolean) {
        context?.run {
            if (value) {
                input_field_label.text = getString(R.string.input_field_error_message)
                input_field_label.setTextColor(ContextCompat.getColor(this, R.color.red_600))
            } else {
                input_field_label.text = getString(R.string.input_field_label_message)
                input_field_label.setTextColor(ContextCompat.getColor(this, R.color.black_70))
            }
        }
    }

    private fun renderProducts(data: VoucherGameDetailData) {
        product_name.text = data.product.text

        val dataCollection = data.product.dataCollections
        if (dataCollection.isEmpty()) showEmpty()
        else {
            val listData = mutableListOf<Visitable<*>>()
            for (productList in dataCollection) {
                // Create new instance to prevent adding copy of products
                // to adapter data (set products to empty list)
                val categoryItem = VoucherGameProductData.DataCollection(productList.name, listOf())
                listData.add(categoryItem)

                if (productList.products.isNotEmpty())  {
                    listData.addAll(productList.products)
                }
            }
            renderList(listData)
        }
    }

    private fun setupProductInfo() {
        // TODO: Add menu detail data
//        product_image.setOnClickListener { showProductInfo() }
//        help_label.setOnClickListener { showProductInfo() }
//        info_icon.setOnClickListener { showProductInfo() }
    }

    private fun showProductInfo(imageUrl: String, title: String, desc: String) {
        activity?.let {
            val voucherGameBottomSheets = VoucherGameBottomSheets()
            voucherGameBottomSheets.imageUrl = imageUrl
            voucherGameBottomSheets.title = title
            voucherGameBottomSheets.description = desc
            voucherGameBottomSheets.show(it.supportFragmentManager, TAG_VOUCHER_GAME_INFO)
        }
    }

    override fun getAdapterTypeFactory(): VoucherGameDetailAdapterFactory {
        return VoucherGameDetailAdapterFactory(this, this)
    }

    override fun getScreenName(): String = getString(R.string.app_label)

    override fun initInjector() {
        getComponent(VoucherGameDetailComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
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
        selectProduct(product, position)
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
        if (::selectedProduct.isInitialized) {
            // If product is already selected, do nothing
            if (product == selectedProduct) return

            val selectedIndex = adapter.data.indexOf(selectedProduct)
            (adapter.data[selectedIndex] as VoucherGameProduct).selected = false
            adapter.notifyItemChanged(selectedIndex)
        }
        (adapter.data[position] as VoucherGameProduct).selected = true
        adapter.notifyItemChanged(position)

        // Update selected product
        selectedProduct = product

        showCheckoutView()
    }

    private fun showCheckoutView() {
        // TODO: Check enquiry result before processing to cart

        checkout_view.setVisibilityLayout(true)
        checkout_view.setTotalPrice(selectedProduct.attributes.promo?.newPrice ?: selectedProduct.attributes.price)
    }

    override fun onClickNextBuyButton() {
        processCheckout()
    }

    private fun processCheckout() {
        // Setup checkout pass data
        if (::voucherGameExtraParam.isInitialized && ::selectedProduct.isInitialized) {
            checkoutPassData = DigitalCheckoutPassData.Builder()
                    .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                    .categoryId(voucherGameExtraParam.categoryId)
//                .clientNumber(telcoClientNumberWidget.getInputNumber())
                    .instantCheckout("0")
                    .isPromo(if (selectedProduct.attributes.promo != null) "1" else "0")
                    .operatorId(voucherGameExtraParam.operatorId)
                    .productId(selectedProduct.id)
                    .utmCampaign(voucherGameExtraParam.categoryId)
                    .utmContent(GlobalConfig.VERSION_NAME)
                    .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                    .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                    .voucherCodeCopied("")
                    .build()
        }

        processToCart()
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(p0: Int): Int {
                return when (adapter.getItemViewType(p0)) {
                    VoucherGameProductViewHolder.LAYOUT -> 1
                    else -> 2
                }
            }
        }
        return layoutManager
    }

    override fun onEmptyContentItemTextClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmptyButtonClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        const val ITEM_DECORATOR_SIZE = 8

        const val TAG_VOUCHER_GAME_INFO = "voucherGameInfo"

        fun newInstance(voucherGameExtraParam: VoucherGameExtraParam): Fragment {
            val fragment = VoucherGameDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_TELCO, voucherGameExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}