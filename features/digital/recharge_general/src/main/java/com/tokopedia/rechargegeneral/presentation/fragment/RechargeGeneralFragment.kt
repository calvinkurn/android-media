package com.tokopedia.rechargegeneral.presentation.fragment

import android.content.Context
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
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownBottomSheet
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownBottomSheet.Companion.SHOW_KEYBOARD_DELAY
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.model.RechargeGeneralData
import com.tokopedia.rechargegeneral.model.RechargeGeneralInput
import com.tokopedia.rechargegeneral.model.RechargeGeneralItemData
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapter
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapterFactory
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.OnInputListener
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectDropdownData
import com.tokopedia.rechargegeneral.presentation.viewmodel.RechargeGeneralViewModel
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import com.tokopedia.rechargegeneral.widget.DigitalProductSelectDropdownBottomSheet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_product.*
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

    private var inputData: Array<Map<String, String>?>? = null
    private var isLoadingRecent = false
        set(value) {
            field = value
            if (value) loading_view.show() else loading_view.hide()
        }

    private var menuId: Int = 0
    private var categoryId: String = ""
    private var operatorId: Int? = null
    private var productId: String? = null
    private var operatorCluster: String = ""

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
            categoryId = it.getString(EXTRA_PARAM_CATEGORY_ID, "")
            menuId = it.getInt(EXTRA_PARAM_MENU_ID)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.operatorCluster.observe(this, Observer {
            when(it) {
                is Success -> {
//                    (activity as BaseSimpleActivity).updateTitle("")

                    // Set default operator id
                    if (operatorId != null) operatorId = getFirstOperatorId(it.data)

                    renderOperators(it.data)
                    if (isLoadingRecent) renderRecentTransactionOperator(it.data)
//                    trackSearchResultCategories(it.data)

                    // For enquiry testing
//                    sharedViewModel.recommendationItem.value = TopupBillsRecommendation(operatorId = 18, productId = 291, clientNumber = "102111106111")
                }
                is Fail -> {
                    isLoadingRecent = false
                    showGetListError(it.throwable)
                }
            }
        })

        viewModel.productList.observe(this, Observer {
            when(it) {
                is Success -> {
                    adapter.hideLoading()
                    renderInputAndProduct(it.data)
                    if (isLoadingRecent) renderRecentTransactionProduct(it.data)
                }
                is Fail -> {
                    isLoadingRecent = false
                    showGetListError(it.throwable)
                }
            }
        })

        sharedViewModel.recommendationItem.observe(this, Observer {
            if (viewModel.operatorCluster.value is Success) {
                operatorId = it.operatorId
                renderRecentTransactionOperator((viewModel.operatorCluster.value as Success).data)
            }
        })

        sharedViewModel.promoItem.observe(this, Observer {

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun renderOperators(cluster: RechargeGeneralOperatorCluster) {
        if (operatorId == null) operatorId = getFirstOperatorId(cluster)
        val id = operatorId
        if (id != null) {
            operatorCluster = getClusterNameOfOperatorId(cluster, id) ?: ""
            renderOperatorCluster(cluster)
            renderOperatorList(cluster.operatorGroups.first { it.name == operatorCluster })
            getProductList(menuId, id)
        }
    }

    private fun renderOperatorCluster(cluster: RechargeGeneralOperatorCluster) {
        if (cluster.operatorGroups.size == 1) {
            operator_cluster_select.hide()
        } else if (cluster.operatorGroups.size > 1) {
            operator_cluster_select.setLabel(cluster.text)
            operator_cluster_select.setHint("")
            operator_cluster_select.setActionListener(object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    if (operatorCluster != input) {
                        operatorCluster = input
                        cluster.operatorGroups.find { it.name == input }?.let {
                            renderOperatorList(it)
                        }
                    }
                }

                override fun onCustomInputClick() {
                    val dropdownData = cluster.operatorGroups.map { TopupBillsInputDropdownData(it.name) }
                    showOperatorSelectDropdown(operator_cluster_select, dropdownData, cluster.text)
                }
            })
            operator_cluster_select.show()

            // Set cluster name
            if (operatorCluster.isNotEmpty()) operator_cluster_select.setInputText(operatorCluster, false)
        }
    }

    private fun renderOperatorList(operatorGroup: RechargeGeneralOperatorCluster.CatalogOperatorGroup) {
        resetInputData()

        if (operatorGroup.operators.size == 1) {
            operator_select.hide()
            adapter.showLoading()
        } else if (operatorGroup.operators.size > 1) {
            operator_select.setLabel(operatorGroup.name)
            operator_select.setHint("")
            operator_select.setActionListener(object : TopupBillsInputFieldWidget.ActionListener {
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
            })
            operator_select.show()

            // Set operator name
            operatorId?.let { id ->
                operatorGroup.operators.find { it.id == id }?.attributes?.name?.let { name ->
                    operator_select.setInputText(name, false)
                }
            }
        }
    }

    private fun renderInputAndProduct(productData: RechargeGeneralProductData) {
        resetInputData()

        val dataList: MutableList<Visitable<RechargeGeneralAdapterFactory>> = mutableListOf()
        if (productData.needEnquiry) {
            val enquiryFields = productData.enquiryFields.filter { it.style != "enquiry" }
            dataList.addAll(enquiryFields)
        }

        // Show product field if there is > 1 product
        if (productData.isShowingProduct) {
            dataList.add(productData.product)
        } else {
            productId = productData.product.dataCollections[0].products[0].id
        }

        adapter.renderList(dataList)
//        trackSearchResultCategories(it.data)

        val inputDataSize = productData.enquiryFields.size
        inputData = arrayOfNulls(inputDataSize)
    }

    // Reset product id & input data
    private fun resetInputData() {
        productId = null
        inputData = null
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
                }
            })
            dropdownView.dropdownData = data
            dropdownBottomSheet.setChild(dropdownView)
            fragmentManager?.run { dropdownBottomSheet.show(this,"Product select dropdown bottom sheet") }
        }
    }

    override fun onCustomInputClick(field: TopupBillsInputFieldWidget, data: List<RechargeGeneralProductSelectData>, position: Int) {
        showProductSelectDropdown(field, data, "Pilih Produk")
        // TODO: Change bottom sheet title to dynamic
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

    private fun renderRecentTransactionOperator(cluster: RechargeGeneralOperatorCluster) {
        isLoadingRecent = true

        operatorId?.let { id ->
            operator_cluster_select.setInputText(getClusterNameOfOperatorId(cluster, id) ?: "", false)

            val operatorName = getOperatorFromCluster(cluster, id)?.attributes?.name
            operatorName?.let { name ->
                if (name == operator_select.getInputText() && viewModel.productList.value is Success) {
                    renderRecentTransactionProduct((viewModel.productList.value as Success).data)
                } else {
                    operator_select.setInputText(name, false)
                    getProductList(menuId, id)
                }
            }
        }
    }

    private fun renderRecentTransactionProduct(products: RechargeGeneralProductData) {
        val recommendationData = sharedViewModel.recommendationItem.value
        recommendationData?.productId?.let { id ->
            //            productId = id.toString()
            // TODO: Remove temporary enquiry params
            productId = "291"

            // Get product object
            if (products.isShowingProduct) {
                products.product.dataCollections.forEach loop@{ dataCollection ->
                    val product = dataCollection.products.find { product -> product.id == id.toString() }
                    if (product != null) {
                        // Update product select recycler view item; product select is always the last item
                        val productSelectData = adapter.data.last()
                        if (productSelectData is RechargeGeneralProductItemData) {
                            productSelectData.value = product.attributes.desc
                            adapter.data[adapter.data.lastIndex] = productSelectData
                            adapter.notifyItemChanged(adapter.data.lastIndex)
                        }
                        return@loop
                    }
                }
            }
        }
        recommendationData?.clientNumber?.let { input ->
            adapter.data.forEachIndexed { index, productInput ->
                if (productInput is RechargeGeneralProductInput) {
//                    productInput.value = input
                    // TODO: Remove temporary enquiry params
                    productInput.value = "102111106111"
                    adapter.notifyItemChanged(index)
                }
            }
        }
        isLoadingRecent = false
    }

    private fun showGetListError(e: Throwable) {
        operator_cluster_select.hide()
        operator_select.hide()
        adapter.showGetListError(e)
    }

    override fun loadData() {
        getMenuDetail(menuId)
        getOperatorCluster(menuId)
    }

    private fun getOperatorCluster(menuId: Int) {
        viewModel.getOperatorCluster(viewModel.createParams(menuId))
    }

    private fun getProductList(menuId: Int, operator: Int) {
        viewModel.getProductList(viewModel.createParams(menuId, operator))
    }

    override fun onFinishInput(label: String, input: String, position: Int) {
        updateInputData(label, input, position)
    }

    private fun updateInputData(label: String, input: String, position: Int) {
        inputData?.apply {
            this[position] = if (input.isEmpty()) null else mapOf(label to input)
            toggleEnquiryButton()
        }
    }

    private fun toggleEnquiryButton() {
        enquiry_button.isEnabled = validateEnquiry()
    }

    private fun validateEnquiry(): Boolean {
        return operatorId != null
                && inputData != null
                && (inputData!!.isEmpty() || inputData!!.all { it != null })
    }

    private fun enquire() {
        if (validateEnquiry()) {
            getEnquiry(operatorId!!.toString(), productId!!, inputData!!.filterNotNull().toTypedArray())
            // TODO: Remove temporary enquiry params
//            val enquiryData = Gson().fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_enquiry_data), TopupBillsEnquiryData::class.java)
//            renderCheckoutView(enquiryData)
        }
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        renderCheckoutView(data)
    }

    private fun renderCheckoutView(data: TopupBillsEnquiryData) {
        context?.let { context ->
            val checkoutBottomSheet = BottomSheetUnify()
            checkoutBottomSheet.setTitle("Checkout")
            checkoutBottomSheet.setCloseClickListener { checkoutBottomSheet.dismiss() }
            checkoutBottomSheet.setFullPage(true)
            checkoutBottomSheet.clearAction()

            val checkoutView = RechargeGeneralCheckoutBottomSheet(context)
            checkoutView.setPayload(data.enquiry)
            checkoutBottomSheet.setChild(checkoutView)

            fragmentManager?.let { fm ->
                checkoutBottomSheet.show(fm, "checkout view bottom sheet")
            }
        }
    }

    override fun onClickCheckout() {
        // TODO: Process checkout
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        renderFooter(data)
    }

    override fun showEnquiryError(t: Throwable) {
//        showGetListError(t)
    }

    override fun showMenuDetailError(t: Throwable) {
//        showGetListError(t)
    }

    private fun getFirstOperatorId(cluster: RechargeGeneralOperatorCluster): Int? {
        return cluster.operatorGroups.getOrNull(0)?.operators?.getOrNull(0)?.id
    }

    private fun getClusterNameOfOperatorId(cluster: RechargeGeneralOperatorCluster, operatorId: Int): String? {
        cluster.operatorGroups.forEach { group ->
            group.operators.forEach { operator ->
                if (operator.id == operatorId) return group.name
            }
        }
        return null
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

        val ITEM_DECORATOR_SIZE = com.tokopedia.design.R.dimen.dp_8

        fun newInstance(categoryId: String, menuId: Int): RechargeGeneralFragment {
            val fragment = RechargeGeneralFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PARAM_CATEGORY_ID, categoryId)
            bundle.putInt(EXTRA_PARAM_MENU_ID, menuId)
            fragment.arguments = bundle
            return fragment
        }
    }
}