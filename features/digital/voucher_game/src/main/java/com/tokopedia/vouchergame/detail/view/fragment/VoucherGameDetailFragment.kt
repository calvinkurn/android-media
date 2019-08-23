package com.tokopedia.vouchergame.detail.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.di.VoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameProductDecorator
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder
import com.tokopedia.vouchergame.detail.view.viewmodel.VoucherGameDetailViewModel
import com.tokopedia.vouchergame.detail.widget.VoucherGameBottomSheets
import com.tokopedia.vouchergame.detail.widget.VoucherGameInputFieldWidget
import kotlinx.android.synthetic.main.fragment_voucher_game_detail.*
import kotlinx.android.synthetic.main.view_voucher_game_input_field.view.*
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

    var menuId: Int = 0
    var platformId: Int = 0
    var operator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            voucherGameViewModel = viewModelProvider.get(VoucherGameDetailViewModel::class.java)
        }

        arguments?.let {
            menuId = it.getInt(EXTRA_MENU_ID, 0)
            platformId = it.getInt(EXTRA_PLATFORM_ID, 0)
            operator = it.getString(EXTRA_OPERATOR_ID, "")
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
                    }
                    is Fail -> {
                        showGetListError(it.throwable)
                    }
                }
            }
        })
//        checkoutPassData = DigitalCheckoutPassData.Builder()
//                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
//                .categoryId(it.product.attributes.categoryId.toString())
//                .clientNumber(telcoClientNumberWidget.getInputNumber())
//                .instantCheckout("0")
//                .isPromo(if (it.product.attributes.productPromo != null) "1" else "0")
//                .operatorId(it.product.attributes.operatorId.toString())
//                .productId(it.product.id)
//                .utmCampaign(it.product.attributes.categoryId.toString())
//                .utmContent(GlobalConfig.VERSION_NAME)
//                .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
//                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
//                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
//                .voucherCodeCopied("")
//                .build()
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
        outState.putInt(EXTRA_MENU_ID, menuId)
        outState.putInt(EXTRA_PLATFORM_ID, platformId)
        outState.putString(EXTRA_OPERATOR_ID, operator)
    }

    private fun initView() {
        recycler_view.addItemDecoration(VoucherGameProductDecorator(ITEM_DECORATOR_SIZE, resources))

        // Enquire when all required input fields are filled
        input_field_1.setListener(object : VoucherGameInputFieldWidget.ActionListener{
            override fun onEditorActionDone() {
                val input1 = input_field_1.getInputText()
                val input2 = input_field_2.getInputText()

                // If first input field is filled, check if second input field is required (and is filled)
                if (input1.isNotEmpty()) {
                    if (input_field_2.visibility == View.VISIBLE && input2.isNotEmpty()) {
                        enquireFields(input1, input2)
                    } else {
                        enquireFields(input1)
                    }
                }
            }
        })
        input_field_2.setListener(object : VoucherGameInputFieldWidget.ActionListener{
            override fun onEditorActionDone() {
                val input1 = input_field_1.getInputText()
                val input2 = input_field_2.getInputText()

                // Both input fields are required, check if both are filled
                if (input1.isNotEmpty() && input2.isNotEmpty()) {
                    enquireFields(input1, input2)
                }
            }
        })

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
    }

    private fun renderProducts(data: VoucherGameDetailData) {
        val dataCollection = data.product.dataCollections
        if (dataCollection.isEmpty()) showEmpty()
        else {
            val listData = mutableListOf<Visitable<*>>()
            for (productList in dataCollection) {
                listData.add(productList)
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

    private fun enquireFields(input1: String, input2: String = "") {

    }

    override fun getAdapterTypeFactory(): VoucherGameDetailAdapterFactory {
        return VoucherGameDetailAdapterFactory(this, this)
    }

    override fun getScreenName(): String = "Voucher Game"

    override fun initInjector() {
        getComponent(VoucherGameDetailComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        voucherGameViewModel.getVoucherGameProducts(GraphqlHelper.loadRawString(resources,
                R.raw.query_voucher_game_product_detail),
                voucherGameViewModel.createParams(menuId, platformId, operator))
    }

    override fun onItemClicked(item: Visitable<*>) {
        // no op
    }

    override fun onItemClicked(product: VoucherGameProduct, position: Int) {
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

        checkout_view.setVisibilityLayout(true)
        checkout_view.setTotalPrice(product.attributes.promo?.newPrice ?: product.attributes.price)
    }

    override fun onClickNextBuyButton() {
        // TODO: Check enquiry result before processing to cart
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

        const val EXTRA_MENU_ID = "EXTRA_MENU_ID"
        const val EXTRA_PLATFORM_ID = "EXTRA_PLATFORM_ID"
        const val EXTRA_OPERATOR_ID = "EXTRA_OPERATOR_ID"

        const val ITEM_DECORATOR_SIZE = 8

        const val TAG_VOUCHER_GAME_INFO = "voucherGameInfo"

        fun createInstance(menuId: Int, platformId: Int, operator: String): VoucherGameDetailFragment {
            return VoucherGameDetailFragment().also {
                it.arguments = Bundle().apply {
                    putInt(EXTRA_MENU_ID, menuId)
                    putInt(EXTRA_PLATFORM_ID, platformId)
                    putString(EXTRA_OPERATOR_ID, operator)
                }
            }
        }
    }
}