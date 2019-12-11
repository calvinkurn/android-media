package com.tokopedia.digital.productV2.presentation.fragment

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
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownBottomSheet
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownBottomSheet.Companion.SHOW_KEYBOARD_DELAY
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.digital.R
import com.tokopedia.digital.productV2.di.DigitalProductComponent
import com.tokopedia.digital.productV2.model.DigitalProductData
import com.tokopedia.digital.productV2.model.DigitalProductInput
import com.tokopedia.digital.productV2.model.DigitalProductItemData
import com.tokopedia.digital.productV2.model.DigitalProductOperatorCluster
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapter
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapterFactory
import com.tokopedia.digital.productV2.presentation.adapter.viewholder.OnInputListener
import com.tokopedia.digital.productV2.presentation.model.DigitalProductSelectDropdownData
import com.tokopedia.digital.productV2.presentation.viewmodel.DigitalProductViewModel
import com.tokopedia.digital.productV2.presentation.viewmodel.SharedDigitalProductViewModel
import com.tokopedia.digital.productV2.widget.DigitalProductSelectDropdownBottomSheet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_product.*
import javax.inject.Inject

class DigitalProductFragment: BaseTopupBillsFragment(), OnInputListener, DigitalProductAdapter.LoaderListener {

    //    @Inject
//    lateinit var trackingUtil: DigitalHomeTrackingUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: DigitalProductViewModel
    @Inject
    lateinit var sharedViewModel: SharedDigitalProductViewModel

    lateinit var adapter: DigitalProductAdapter

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_product, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalProductViewModel::class.java)
            sharedViewModel = viewModelProvider.get(SharedDigitalProductViewModel::class.java)

            adapter = DigitalProductAdapter(it, DigitalProductAdapterFactory(this), this)
        }

        arguments?.let {
            categoryId = it.getString(EXTRA_PARAM_CATEGORY_ID, "")
            menuId = it.getInt(EXTRA_PARAM_MENU_ID)
        }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.operatorCluster.observe(this, Observer {
            when(it) {
                is Success -> {
//                    (activity as BaseSimpleActivity).updateTitle("")
                    renderOperatorCluster(it.data)
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
            // Check if operator cluster data is already available; if not wait until data is received
            if (viewModel.operatorCluster.value != null && viewModel.operatorCluster.value is Success) {
                val operators = (viewModel.operatorCluster.value as Success).data
                renderRecentTransactionOperator(operators)
            } else {
                isLoadingRecent = true
            }
        })

        sharedViewModel.promoItem.observe(this, Observer {

        })
    }

    private fun renderOperatorCluster(cluster: DigitalProductOperatorCluster) {
        if (cluster.operatorGroups.size == 1) {
            operator_cluster_select.hide()
            renderOperatorList(cluster.operatorGroups[0])
        } else if (cluster.operatorGroups.size > 1) {
            operator_cluster_select.setLabel(cluster.text)
            operator_cluster_select.setHint("")
            operator_cluster_select.setActionListener(object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    cluster.operatorGroups.find { it.name == input }?.let {
                        renderOperatorList(it)
                    }
                }

                override fun onCustomInputClick() {
                    val dropdownData = cluster.operatorGroups.map { TopupBillsInputDropdownData(it.name) }
                    showOperatorSelectDropdown(operator_cluster_select, dropdownData, cluster.text)
                }
            })

            // Setup dropdown bottom sheet data
            operator_cluster_select.show()
        }
    }

    private fun renderOperatorList(operatorGroup: DigitalProductOperatorCluster.CatalogOperatorGroup) {
        // Reset operator id
        operatorId = null

        if (operatorGroup.operators.size == 1) {
            operator_select.hide()
            adapter.showLoading()
            getProductList(menuId, operatorGroup.operators[0].id)
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

            // Setup dropdown bottom sheet data
            operator_select.show()
        }
    }

    private fun renderInputAndProduct(productData: DigitalProductData) {
        // Reset input data & product id
        inputData = null
        productId = null

        val dataList: MutableList<Visitable<DigitalProductAdapterFactory>> = mutableListOf()
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
//                    trackSearchResultCategories(it.data)

        val inputDataSize = productData.enquiryFields.size
        inputData = arrayOfNulls(inputDataSize)
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
                                           data: List<DigitalProductSelectDropdownData>,
                                           title: String = "") {
        context?.let { context ->
            val dropdownBottomSheet = BottomSheetUnify()
            dropdownBottomSheet.setTitle(title)
            dropdownBottomSheet.setFullPage(true)
            dropdownBottomSheet.clearAction()
            dropdownBottomSheet.setCloseClickListener {
                dropdownBottomSheet.dismiss()
            }

            val dropdownView = DigitalProductSelectDropdownBottomSheet(context, listener = object: DigitalProductSelectDropdownBottomSheet.OnClickListener{
                override fun onItemClicked(item: DigitalProductSelectDropdownData) {
                    dropdownBottomSheet.dismiss()

                    // Show label & store id for enquiry
                    field.setInputText(item.title)
                    productId = item.id
                }
            })
            dropdownView.dropdownData = data
            dropdownBottomSheet.setChild(dropdownView)
            fragmentManager?.run { dropdownBottomSheet.show(this,"Product select dropdown bottom sheet") }
        }
    }

    override fun onCustomInputClick(field: TopupBillsInputFieldWidget, data: List<DigitalProductSelectDropdownData>, position: Int) {
        showProductSelectDropdown(field, data, "Pilih Produk")
        // TODO: Change bottom sheet title to dynamic
    }

    private fun renderFooter(data: TopupBillsMenuDetail) {
        val promos = data.promos
        val recommendations = data.recommendations

        val listProductTab = mutableListOf<TopupBillsTabItem>()
        if (recommendations.isNotEmpty()) {
            listProductTab.add(TopupBillsTabItem(DigitalProductRecentTransactionFragment.newInstance(recommendations), getString(R.string.recent_transaction_tab_title)))
        }
        if (promos.isNotEmpty()) {
            listProductTab.add(TopupBillsTabItem(DigitalProductPromoListFragment.newInstance(promos), getString(R.string.promo_tab_title)))
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

    private fun renderRecentTransactionOperator(operatorCluster: DigitalProductOperatorCluster) {
        isLoadingRecent = true

        val recentOperatorId = sharedViewModel.recommendationItem.value?.operatorId
        if (recentOperatorId != null) {
            operatorCluster.operatorGroups.forEach loop@{ operatorGroup ->
                val operator = operatorGroup.operators.find { opr -> opr.id == recentOperatorId }
                if (operator != null) {
                    // Set selected operator cluster & operator if not already selected
                    if (operatorCluster.operatorGroups.size > 1 && operator_cluster_select.getInputText() != operatorGroup.name) {
                        operator_cluster_select.setInputText(operatorGroup.name)
                    }
                    // This will trigger getProductList
                    if (operatorGroup.operators.size > 1 && operator_select.getInputText() != operator.attributes.name) {
                        operator_select.setInputText(operator.attributes.name)
                    }

                    // Check if product list data is already available; if not wait until data is received
                    if (operatorId == recentOperatorId
                            && viewModel.productList.value != null
                            && viewModel.productList.value is Success) {
                        val products = (viewModel.productList.value as Success).data
                        renderRecentTransactionProduct(products)
                    } else {
                        isLoadingRecent = true
                        operatorId = recentOperatorId
                    }
                    return@loop
                }
            }
        }
    }

    private fun renderRecentTransactionProduct(products: DigitalProductData) {
        isLoadingRecent = true

        val recommendationData = sharedViewModel.recommendationItem.value
        recommendationData?.productId?.let { id ->
            productId = id.toString()

            // Get product object
            if (products.isShowingProduct) {
                products.product.dataCollections.forEach loop@{ dataCollection ->
                    val product = dataCollection.products.find { product -> product.id == id.toString() }
                    if (product != null) {
                        // Update product select recycler view item; product select is always the last item
                        val productSelectData = adapter.data.last()
                        if (productSelectData is DigitalProductItemData) {
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
                if (productInput is DigitalProductInput) {
                    productInput.value = input
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
        viewModel.getOperatorCluster(GraphqlHelper.loadRawString(resources, R.raw.query_catalog_operator_select_group),
                viewModel.createParams(menuId))
    }

    private fun getProductList(menuId: Int, operator: Int) {
        viewModel.getProductList(GraphqlHelper.loadRawString(resources, com.tokopedia.common.topupbills.R.raw.query_catalog_product_input),
                viewModel.createParams(menuId, operator))
    }

    override fun onFinishInput(label: String, input: String, position: Int) {
        updateInputData(label, input, position)
    }

    private fun updateInputData(label: String, input: String, position: Int) {
        inputData?.apply {
            this[position] = if (input.isEmpty()) null else mapOf(label to input)
            enquiry_button.isEnabled = validateEnquiry()
        }
    }

    private fun enquire() {
        if (validateEnquiry()) {
            getEnquiry(operatorId!!.toString(), productId!!, inputData!!.filterNotNull().toTypedArray())
        }
    }

    private fun validateEnquiry(): Boolean {
        return operatorId != null
                && inputData != null
                && (inputData!!.isEmpty() || inputData!!.all { it != null })
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        val test = "test"
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        renderFooter(data)
    }

    override fun showEnquiryError(t: Throwable) {
        showGetListError(t)
    }

    override fun showMenuDetailError(t: Throwable) {
//        showGetListError(t)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(DigitalProductComponent::class.java).inject(this)
    }

    companion object {
        const val EXTRA_PARAM_MENU_ID = "EXTRA_PARAM_MENU_ID"
        const val EXTRA_PARAM_CATEGORY_ID = "EXTRA_PARAM_CATEGORY_ID"

        val ITEM_DECORATOR_SIZE = com.tokopedia.design.R.dimen.dp_8

        fun newInstance(categoryId: String, menuId: Int): DigitalProductFragment {
            val fragment = DigitalProductFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PARAM_CATEGORY_ID, categoryId)
            bundle.putInt(EXTRA_PARAM_MENU_ID, menuId)
            fragment.arguments = bundle
            return fragment
        }
    }
}