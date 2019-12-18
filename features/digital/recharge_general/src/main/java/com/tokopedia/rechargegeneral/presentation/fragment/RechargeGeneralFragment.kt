package com.tokopedia.rechargegeneral.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownBottomSheet
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownBottomSheet.Companion.SHOW_KEYBOARD_DELAY
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductData
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapter
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapterFactory
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.OnInputListener
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectData
import com.tokopedia.rechargegeneral.presentation.viewmodel.RechargeGeneralViewModel
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import com.tokopedia.rechargegeneral.widget.RechargeGeneralCheckoutBottomSheet
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_product.*
import kotlinx.android.synthetic.main.view_digital_product_input_info_bottom_sheet.view.*
import javax.inject.Inject

class RechargeGeneralFragment: BaseTopupBillsFragment(),
        OnInputListener,
        RechargeGeneralAdapter.LoaderListener,
        RechargeGeneralCheckoutBottomSheet.CheckoutListener {

    //    @Inject
//    lateinit var trackingUtil: DigitalHomeTrackingUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: RechargeGeneralViewModel
    @Inject
    lateinit var sharedViewModel: SharedRechargeGeneralViewModel

    lateinit var adapter: RechargeGeneralAdapter
    private var isLoading = false
        set(value) {
            field = value
            if (value) loading_view.show() else loading_view.hide()
        }

    private var inputData: HashMap<String, String> = hashMapOf()
    private var inputDataSize: Int = 0

    private var menuId: Int = 0
    private var categoryId: Int = 0
    private var operatorId: Int = 0
    private var productId: String = ""
    private var operatorCluster: String = ""

    private var favoriteNumbers: List<TopupBillsFavNumberItem> = listOf()
    private var clientNumber: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_product, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(RechargeGeneralViewModel::class.java)
            sharedViewModel = viewModelProvider.get(SharedRechargeGeneralViewModel::class.java)

            // Setup viewmodel queries
            viewModel.operatorClusterQuery = GraphqlHelper.loadRawString(resources, R.raw.query_catalog_operator_select_group)
            viewModel.productListQuery = GraphqlHelper.loadRawString(resources, com.tokopedia.common.topupbills.R.raw.query_catalog_product_input)

            adapter = RechargeGeneralAdapter(it, RechargeGeneralAdapterFactory(this), this)
        }

        arguments?.let {
            categoryId = it.getInt(EXTRA_PARAM_CATEGORY_ID, 0)
            menuId = it.getInt(EXTRA_PARAM_MENU_ID, 0)
            operatorId = it.getInt(EXTRA_PARAM_OPERATOR_ID, 0)
            productId = it.getString(EXTRA_PARAM_PRODUCT_ID, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.operatorCluster.observe(this, Observer {
            when(it) {
                is Success -> {
                    renderOperators(it.data)
//                    trackSearchResultCategories(it.data)

                    // For enquiry testing
//                    sharedViewModel.recommendationItem.selectedId = TopupBillsRecommendation(operatorId = 18, productId = 291, clientNumber = "102111106111")
                }
                is Fail -> {
                    isLoading = false
                    showGetListError(it.throwable)
                }
            }
        })

        viewModel.productList.observe(this, Observer {
            when(it) {
                is Success -> {
                    adapter.hideLoading()
                    renderInputAndProduct(it.data)
                }
                is Fail -> {
                    isLoading = false
                    showGetListError(it.throwable)
                }
            }
        })

        sharedViewModel.recommendationItem.observe(this, Observer {
            if (viewModel.operatorCluster.value is Success) {
                operatorId = it.operatorId
                // TODO: Remove temporary enquiry params
                productId = it.productId.toString()
//                clientNumber = it.clientNumber
//                productId = "291"
                clientNumber = "102111106111"
                renderOperators((viewModel.operatorCluster.value as Success).data)
            }
        })

        sharedViewModel.promoItem.observe(this, Observer {

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            categoryId = savedInstanceState.getInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
            menuId = savedInstanceState.getInt(EXTRA_PARAM_MENU_ID, menuId)
            operatorId = savedInstanceState.getInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
            productId = savedInstanceState.getString(EXTRA_PARAM_PRODUCT_ID, productId)
            inputData = savedInstanceState.getSerializable(EXTRA_PARAM_INPUT_DATA) as HashMap<String, String>
        }

        rv_digital_product.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_digital_product.adapter = adapter
        while (rv_digital_product.itemDecorationCount > 0) rv_digital_product.removeItemDecorationAt(0)
        rv_digital_product.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                // Add offset to all items except the last one
                if (parent.getChildAdapterPosition(view) < adapter.dataSize - 1) {
                    context?.resources?.getDimension(ITEM_DECORATOR_SIZE)?.toInt()?.let { dimen -> outRect.bottom = dimen }
                }
            }
        })
        enquiry_button.isEnabled = false
        enquiry_button.setOnClickListener {
            enquire()
        }
        loading_view.hide()

        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_DIGITAL_SEARCH_NUMBER) {
                val favNumber = data?.getStringExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER)
                favNumber?.let { renderClientNumber(favNumber) }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
        outState.putInt(EXTRA_PARAM_MENU_ID, menuId)
        outState.putInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
        outState.putString(EXTRA_PARAM_PRODUCT_ID, productId)
        outState.putSerializable(EXTRA_PARAM_INPUT_DATA, inputData)
    }

    private fun renderOperators(cluster: RechargeGeneralOperatorCluster) {
        isLoading = true
        if (operatorId == 0) operatorId = getFirstOperatorId(cluster)
        if (operatorId > 0) {
            operatorCluster = getClusterNameOfOperatorId(cluster, operatorId)
            renderOperatorCluster(cluster)
            renderOperatorList(cluster.operatorGroups.first { it.name == operatorCluster }, cluster.text)
            getProductList(menuId, operatorId)
        }
    }

    private fun renderOperatorCluster(cluster: RechargeGeneralOperatorCluster) {
        if (cluster.operatorGroups.size == 1) {
            operator_cluster_select.hide()
        } else if (cluster.operatorGroups.size > 1) {
            operator_cluster_select.setLabel(cluster.text)
            operator_cluster_select.setHint("")
            operator_cluster_select.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    if (operatorCluster != input) {
                        operatorCluster = input
                        cluster.operatorGroups.find { it.name == input }?.let {
                            renderOperatorList(it, cluster.text)
                        }
                    }
                }

                override fun onCustomInputClick() {
                    val dropdownData = cluster.operatorGroups.map { TopupBillsInputDropdownData(it.name) }
                    showOperatorSelectDropdown(operator_cluster_select, dropdownData, cluster.text)
                }
            }
            operator_cluster_select.show()

            // Set cluster name
            if (operatorCluster.isNotEmpty()) operator_cluster_select.setInputText(operatorCluster, false)
        }
    }

    private fun renderOperatorList(operatorGroup: RechargeGeneralOperatorCluster.CatalogOperatorGroup, label: String) {
        if (operatorGroup.operators.size == 1) {
            operator_select.hide()
            adapter.showLoading()
        } else if (operatorGroup.operators.size > 1) {
            operator_select.setLabel(label)
            operator_select.setHint("")
            operator_select.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    operatorGroup.operators.find { it.attributes.name == input }?.let {
                        if (operatorId != it.id) {
                            // Save operator id for enquiry
                            operatorId = it.id

                            adapter.showLoading()
                            getProductList(menuId, it.id)
                        }
                    }
                }

                override fun onCustomInputClick() {
                    val dropdownData = operatorGroup.operators.map { TopupBillsInputDropdownData(it.attributes.name) }
                    showOperatorSelectDropdown(operator_select, dropdownData)
                }
            }
            operator_select.show()

            // Set operator name
            if (operatorId > 0) {
                operatorGroup.operators.find { it.id == operatorId }?.attributes?.name?.let { name ->
                    operator_select.setInputText(name, false)
                }
            }
        }
    }

    private fun renderInputAndProduct(productData: RechargeGeneralProductData) {
        resetInputData()

        val dataList: MutableList<Visitable<RechargeGeneralAdapterFactory>> = mutableListOf()
        if (productData.needEnquiry) {
            val enquiryFields = productData.enquiryFields.filter { it.style != INPUT_TYPE_ENQUIRY_INFO }.toMutableList()
            // Set favorite number if available
            val itr = enquiryFields.listIterator()
            while (itr.hasNext()) {
                val item = itr.next()
                if (item.name == PARAM_CLIENT_NUMBER) {
                    if (favoriteNumbers.isNotEmpty()) {
                        item.style = INPUT_TYPE_FAVORITE_NUMBER
                    }
                    if (clientNumber.isNotEmpty()) {
                        item.value = clientNumber
                        clientNumber = ""
                    }

                }
                if (inputData.containsKey(item.name)) {
                    item.value = inputData[item.name]!!
                }
                itr.set(item)
            }
            dataList.addAll(enquiryFields)
        }

        // Show product field if there is > 1 product
        if (productData.isShowingProduct) {
            val productSelectData = productData.product
            if (productId.isNotEmpty()) productSelectData.selectedId = productId
            dataList.add(productSelectData)
        } else {
            productId = productData.product.dataCollections[0].products[0].id
        }

        adapter.renderList(dataList)
//        trackSearchResultCategories(it.data)

        inputDataSize = productData.enquiryFields.size

        isLoading = false
    }

    // Reset product id & input data
    private fun resetInputData() {
        inputData = hashMapOf()
        toggleEnquiryButton()
    }

    private fun showOperatorSelectDropdown(field: TopupBillsInputFieldWidget,
                                           data: List<TopupBillsInputDropdownData>,
                                           title: String = "") {
        context?.let { context ->
            val dropdownBottomSheet = BottomSheetUnify()
            dropdownBottomSheet.setTitle(title)
            dropdownBottomSheet.setFullPage(true)
            dropdownBottomSheet.clearAction()
            dropdownBottomSheet.setCloseClickListener {
                dropdownBottomSheet.dismiss()
            }

            val dropdownView = TopupBillsInputDropdownBottomSheet(context, listener = object: TopupBillsInputDropdownBottomSheet.OnClickListener{
                override fun onItemClicked(item: TopupBillsInputDropdownData) {
                    dropdownBottomSheet.dismiss()
                    field.setInputText(item.label)
                }
            }, selected = field.getInputText())
            dropdownView.setData(data)
            dropdownBottomSheet.setChild(dropdownView)

            fragmentManager?.run {
                dropdownBottomSheet.show(this, "Enquiry input field dropdown bottom sheet")
                // Open keyboard with delay so it opens when bottom sheet is fully visible
                Handler().postDelayed({
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
                }, SHOW_KEYBOARD_DELAY)
            }
        }
    }

    private fun showProductSelectDropdown(field: TopupBillsInputFieldWidget,
                                          data: List<RechargeGeneralProductSelectData>,
                                          title: String = "") {
        context?.let { context ->
            val dropdownBottomSheet = BottomSheetUnify()
            dropdownBottomSheet.setTitle(title)
            dropdownBottomSheet.setFullPage(true)
            dropdownBottomSheet.clearAction()
            dropdownBottomSheet.setCloseClickListener {
                dropdownBottomSheet.dismiss()
            }

            val dropdownView = RechargeGeneralProductSelectBottomSheet(context, listener = object : RechargeGeneralProductSelectBottomSheet.OnClickListener{
                override fun onItemClicked(item: RechargeGeneralProductSelectData) {
                    dropdownBottomSheet.dismiss()

                    // Show label & store id for enquiry
                    field.setInputText(item.title, false)
                    productId = item.id
                    toggleEnquiryButton()
                }
            })
            dropdownView.dropdownData = data
            dropdownBottomSheet.setChild(dropdownView)
            fragmentManager?.run { dropdownBottomSheet.show(this,"Product select dropdown bottom sheet") }
        }
    }

    private fun showFavoriteNumbersPage() {
        if (favoriteNumbers.isNotEmpty()) {
            activity?.run {
                startActivityForResult(
                        TopupBillsSearchNumberActivity.newInstance(this,
                                ClientNumberType.TYPE_INPUT_NUMERIC,
                                clientNumber,
                                favoriteNumbers), REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
            }
        }
    }

    override fun onInfoClick(text: String) {
        showInfoBottomSheets(text)
    }

    private fun showInfoBottomSheets(helpText: String) {
        val infoTextView = View.inflate(context,R.layout.view_digital_product_input_info_bottom_sheet, null)
        with (infoTextView) {
            info_text.text = helpText
        }

        val moreInfoBottomSheet = BottomSheetUnify()
        moreInfoBottomSheet.setTitle("Info")
        moreInfoBottomSheet.setFullPage(false)
        moreInfoBottomSheet.setChild(infoTextView)
        moreInfoBottomSheet.clearAction()
        moreInfoBottomSheet.setCloseClickListener {
            moreInfoBottomSheet.dismiss()
        }
        fragmentManager?.run { moreInfoBottomSheet.show(this,"E-gold more info bottom sheet") }
    }

    private fun renderFooter(data: TopupBillsMenuDetail) {
        val promos = data.promos
        val recommendations = data.recommendations

        val listProductTab = mutableListOf<TopupBillsTabItem>()
        if (recommendations.isNotEmpty()) {
            listProductTab.add(TopupBillsTabItem(RechargeGeneralRecentTransactionFragment.newInstance(recommendations), getString(R.string.recent_transaction_tab_title)))
        }
        if (promos.isNotEmpty()) {
            listProductTab.add(TopupBillsTabItem(RechargeGeneralPromoListFragment.newInstance(promos), getString(R.string.promo_tab_title)))
        }

        if (listProductTab.isNotEmpty()) {
            val pagerAdapter = TopupBillsProductTabAdapter(listProductTab, childFragmentManager)
            product_view_pager.adapter = pagerAdapter
            product_view_pager.offscreenPageLimit = listProductTab.size

            if (listProductTab.size > 1) {
                tab_layout.setupWithViewPager(product_view_pager)
                tab_layout.show()
                product_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(p0: Int) {

                    }

                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                    }

                    override fun onPageSelected(pos: Int) {
//                topupAnalytics.eventClickTelcoPrepaidCategory(listProductTab[pos].title)
                    }
                })
            }
            product_view_pager.show()
        } else {
            tab_layout.hide()
            product_view_pager.hide()
        }
    }

    private fun renderClientNumber(number: String) {
        clientNumber = number
        if (adapter.data.isNotEmpty()) {
            adapter.data.forEachIndexed { index, productInput ->
                if (productInput is RechargeGeneralProductInput && productInput.name == PARAM_CLIENT_NUMBER) {
                    productInput.value = number
                    productInput.style = INPUT_TYPE_FAVORITE_NUMBER
                    adapter.notifyItemChanged(index)
                    return@forEachIndexed
                }
            }
        }
    }

    private fun showGetListError(e: Throwable) {
        operator_cluster_select.hide()
        operator_select.hide()
        enquiry_button.hide()
        adapter.showGetListError(e)
    }

    override fun loadData() {
        getMenuDetail(menuId)
        getFavoriteNumbers(categoryId)
        getOperatorCluster(menuId)
        enquiry_button.show()
    }

    private fun getOperatorCluster(menuId: Int) {
        viewModel.getOperatorCluster(viewModel.createParams(menuId))
    }

    private fun getProductList(menuId: Int, operator: Int) {
        viewModel.getProductList(viewModel.createParams(menuId, operator))
    }

    override fun onFinishInput(label: String, input: String) {
        updateInputData(label, input)
    }

    override fun onCustomInputClick(field: TopupBillsInputFieldWidget, position: Int, data: List<RechargeGeneralProductSelectData>?) {
        // If there is data open product select bottom sheet, else open favorite number activity
        if (data != null) {
            showProductSelectDropdown(field, data, "Pilih Produk")
            // TODO: Change bottom sheet title to dynamic
        } else {
            showFavoriteNumbersPage()
        }
    }

    private fun updateInputData(label: String, input: String) {
        inputData[label] = input
        toggleEnquiryButton()
    }

    private fun toggleEnquiryButton() {
        enquiry_button.isEnabled = validateEnquiry()
    }

    private fun validateEnquiry(): Boolean {
        return operatorId > 0 && productId.isNotEmpty() && inputData.size == inputDataSize
    }

    private fun enquire() {
        if (validateEnquiry()) {
            getEnquiry(operatorId.toString(), productId, inputData)
            // TODO: Remove temporary enquiry params
//            val enquiryData = Gson().fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_enquiry_data), TopupBillsEnquiryData::class.java)
        }
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        renderCheckoutView(data)
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        renderFooter(data)
    }

    override fun processFavoriteNumbers(data: TopupBillsFavNumber) {
        favoriteNumbers = data.favNumberList
        if (favoriteNumbers.isNotEmpty() && clientNumber.isEmpty()) {
            renderClientNumber(favoriteNumbers[0].clientNumber)
        }
    }

    override fun showEnquiryError(t: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, t.message)
    }

    override fun showMenuDetailError(t: Throwable) {
        showGetListError(t)
    }

    override fun showFavoriteNumbersError(t: Throwable) {
//        showGetListError(t)
    }

    private fun renderCheckoutView(data: TopupBillsEnquiryData) {
        context?.let { context ->
            val checkoutBottomSheet = BottomSheetUnify()
            checkoutBottomSheet.setTitle("Checkout")
            checkoutBottomSheet.setCloseClickListener { checkoutBottomSheet.dismiss() }
            checkoutBottomSheet.setFullPage(true)
            checkoutBottomSheet.clearAction()

            val checkoutView = RechargeGeneralCheckoutBottomSheet(context)
            checkoutView.listener = this
            checkoutView.setPayload(data.enquiry)
            checkoutBottomSheet.setChild(checkoutView)

            fragmentManager?.let { fm ->
                checkoutBottomSheet.show(fm, "checkout view bottom sheet")
            }
        }
    }

    override fun onClickCheckout() {
        processCheckout()
    }

    private fun processCheckout() {
        // Setup checkout pass data
        if (categoryId > 0
                && operatorId > 0
                && productId.isNotEmpty()) {
            checkoutPassData = DigitalCheckoutPassData.Builder()
                    .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                    .categoryId(categoryId.toString())
                    .instantCheckout("0")
                    // TODO: Check promo
//                    .isPromo(if (selectedProduct.attributes.promo != null) "1" else "0")
                    .isPromo("1")
                    .operatorId(operatorId.toString())
                    .productId(productId.toString())
                    .utmCampaign(categoryId.toString())
                    .utmContent(GlobalConfig.VERSION_NAME)
                    .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                    .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                    .voucherCodeCopied("")
                    .build()

            processToCart()
        }
    }

    private fun getFirstOperatorId(cluster: RechargeGeneralOperatorCluster): Int {
        return cluster.operatorGroups.getOrNull(0)?.operators?.getOrNull(0)?.id ?: 0
    }

    private fun getClusterNameOfOperatorId(cluster: RechargeGeneralOperatorCluster, operatorId: Int): String {
        cluster.operatorGroups.forEach { group ->
            group.operators.forEach { operator ->
                if (operator.id == operatorId) return group.name
            }
        }
        return ""
    }

    private fun getOperatorFromCluster(cluster: RechargeGeneralOperatorCluster, operatorId: Int): CatalogOperator? {
        cluster.operatorGroups.forEach { group ->
            group.operators.forEach { operator ->
                if (operator.id == operatorId) return operator
            }
        }
        return null
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(RechargeGeneralComponent::class.java).inject(this)
    }

    companion object {
        const val EXTRA_PARAM_MENU_ID = "EXTRA_PARAM_MENU_ID"
        const val EXTRA_PARAM_CATEGORY_ID = "EXTRA_PARAM_CATEGORY_ID"
        const val EXTRA_PARAM_OPERATOR_ID = "EXTRA_PARAM_OPERATOR_ID"
        const val EXTRA_PARAM_PRODUCT_ID = "EXTRA_PARAM_PRODUCT_ID"
        const val EXTRA_PARAM_CLIENT_NUMBER = "EXTRA_PARAM_CLIENT_NUMBER"
        const val EXTRA_PARAM_INPUT_DATA = "EXTRA_PARAM_INPUT_DATA"

        const val INPUT_TYPE_NUMERIC = "input_numeric"
        const val INPUT_TYPE_ALPHANUMERIC = "input_alphanumeric"
        const val INPUT_TYPE_FAVORITE_NUMBER = "input_favorite"
        const val INPUT_TYPE_ENQUIRY_INFO = "enquiry"

        const val PARAM_CLIENT_NUMBER = "client_number"

        const val REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 77

        val ITEM_DECORATOR_SIZE = com.tokopedia.design.R.dimen.dp_8

        fun newInstance(categoryId: Int,
                        menuId: Int,
                        operatorId: Int = 0,
                        productId: String = ""): RechargeGeneralFragment {
            val fragment = RechargeGeneralFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
            bundle.putInt(EXTRA_PARAM_MENU_ID, menuId)
            bundle.putInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
            bundle.putString(EXTRA_PARAM_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}