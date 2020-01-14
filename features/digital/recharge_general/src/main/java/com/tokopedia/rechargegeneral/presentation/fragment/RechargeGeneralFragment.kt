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
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget.Companion.SHOW_KEYBOARD_DELAY
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.onTabSelected
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
import com.tokopedia.rechargegeneral.util.RechargeGeneralAnalytics
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

    @Inject
    lateinit var rechargeGeneralAnalytics: RechargeGeneralAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: RechargeGeneralViewModel
    @Inject
    lateinit var sharedViewModel: SharedRechargeGeneralViewModel
    private var saveInstanceManager: SaveInstanceCacheManager? = null

    lateinit var adapter: RechargeGeneralAdapter

    private var inputData: HashMap<String, String> = hashMapOf()
    private var inputDataSize: Int = 0

    private var menuId: Int = 0
    private var categoryId: Int = 0
    private var operatorId: Int = 0
    private var productId: String = ""
    private var operatorCluster: String = ""
    private var favoriteNumbers: List<TopupBillsFavNumberItem> = listOf()

    private var enquiryLabel = ""
    private lateinit var enquiryData: TopupBillsEnquiry

    private lateinit var checkoutBottomSheet: BottomSheetUnify

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

            saveInstanceManager = SaveInstanceCacheManager(it, savedInstanceState)
            val savedEnquiryData: TopupBillsEnquiry? = saveInstanceManager!!.get(EXTRA_PARAM_ENQUIRY_DATA, TopupBillsEnquiry::class.java)
            if (savedEnquiryData != null) enquiryData = savedEnquiryData

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
                    loading_view.hide()
                    renderInitialData(it.data)
//                    trackSearchResultCategories(it.data)

                    // For enquiry testing
//                    sharedViewModel.recommendationItem.selectedId = TopupBillsRecommendation(operatorId = 18, productId = 291, clientNumber = "102111106111")
                }
                is Fail -> {
                    showGetListError(it.throwable)
                }
            }
        })

        viewModel.productList.observe(this, Observer {
            when(it) {
                is Success -> {
                    adapter.hideLoading()
                    setupInputAndProduct(it.data)
                }
                is Fail -> {
                    showGetListError(it.throwable)
                }
            }
        })

        sharedViewModel.recommendationItem.observe(this, Observer {
            if (viewModel.operatorCluster.value is Success) {
                rechargeGeneralAnalytics.eventClickRecentIcon(it, it.position)
                operatorId = it.operatorId
                productId = it.productId.toString()
                inputData[PARAM_CLIENT_NUMBER] = it.clientNumber
                renderInitialData((viewModel.operatorCluster.value as Success).data)
            }
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

        loadData()

        // Render enquiry data
        if (::enquiryData.isInitialized) renderCheckoutView(enquiryData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_DIGITAL_SEARCH_NUMBER) {
                val favNumber = data?.getParcelableExtra<TopupBillsFavNumberItem>(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER)
                favNumber?.let {
                    rechargeGeneralAnalytics.eventInputFavoriteNumber(categoryId, operatorId)
                    renderClientNumber(favNumber.clientNumber)
                }
            } else if (requestCode == REQUEST_CODE_LOGIN) {
                enquire()
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
        if (::enquiryData.isInitialized) {
            saveInstanceManager?.apply {
                onSave(outState)
                put(EXTRA_PARAM_ENQUIRY_DATA, enquiryData)
            }
        }
    }

    private fun renderInitialData(cluster: RechargeGeneralOperatorCluster) {
        if (operatorId == 0) operatorId = getFirstOperatorId(cluster)
        if (operatorId > 0) {
            renderOperatorCluster(cluster)

            operatorCluster = getClusterOfOperatorId(cluster, operatorId)?.name ?: ""
            val operatorGroup = cluster.operatorGroups.first { it.name == operatorCluster }
            renderOperatorList(operatorGroup, cluster.text)
            if (operatorGroup.operators.size > 1) getProductList(menuId, operatorId)
        }
    }

    private fun renderOperatorCluster(cluster: RechargeGeneralOperatorCluster) {
        if (cluster.operatorGroups.size == 1) {
            operator_cluster_select.hide()
        } else if (cluster.operatorGroups.size > 1) {
            operator_cluster_select.show()
            // TODO: Get operator cluster label from backend
            operator_cluster_select.setLabel("Pilih Operator Cluster")
//            operator_cluster_select.setLabel(cluster.text)
            operator_cluster_select.setHint("")
            operator_cluster_select.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    if (operatorCluster != input) {
                        operatorCluster = input
                        resetInputData()
                        rechargeGeneralAnalytics.eventChooseOperatorCluster(categoryId, operatorCluster)

                        cluster.operatorGroups.find { it.name == input }?.let {
                            renderOperatorList(it, cluster.text)
                        }
                    }
                }

                override fun onCustomInputClick() {
                    rechargeGeneralAnalytics.eventClickOperatorClusterDropdown(categoryId)

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
            // Get product data based on operator id
            operatorId = operatorGroup.operators.first().id
            adapter.showLoading()
            getProductList(menuId, operatorId)
        } else if (operatorGroup.operators.size > 1) {
            operator_select.show()
            operator_select.setLabel(label)
            operator_select.setHint("")
            operator_select.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    operatorGroup.operators.find { it.attributes.name == input }?.let {
                        if (operatorId != it.id) {
                            // Save operator id for enquiry
                            operatorId = it.id
                            resetInputData()
                            rechargeGeneralAnalytics.eventChooseOperator(categoryId, operatorId)

                            adapter.showLoading()
                            getProductList(menuId, it.id)
                        }
                    }
                }

                override fun onCustomInputClick() {
                    rechargeGeneralAnalytics.eventClickOperatorListDropdown(categoryId)

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

    private fun setupInputAndProduct(productData: RechargeGeneralProductData) {
        val dataList: MutableList<Visitable<RechargeGeneralAdapterFactory>> = mutableListOf()
        if (productData.enquiryFields.isNotEmpty()) {
            val enquiryFields = productData.enquiryFields.toMutableList()
            val enquiryInfo = productData.enquiryFields.find { it.style == INPUT_TYPE_ENQUIRY_INFO }
            if (enquiryInfo != null) {
                enquiryFields.remove(enquiryInfo)
                // Set enquiry button label
                setEnquiryButtonLabel(enquiryInfo.text)
            }
            if (productData.needEnquiry) {
                // Set favorite number if available
                val itr = enquiryFields.listIterator()
                while (itr.hasNext()) {
                    val item = itr.next()
                    if (item.name == PARAM_CLIENT_NUMBER && favoriteNumbers.isNotEmpty()) {
                        item.style = INPUT_TYPE_FAVORITE_NUMBER
                    }
                    if (inputData.containsKey(item.name)) {
                        item.value = inputData[item.name]!!
                    }
                    itr.set(item)
                }
                dataList.addAll(enquiryFields)

                inputDataSize = enquiryFields.size
            }
        }

        // Show product field if there is > 1 product
        if (productData.isShowingProduct) {
            val productSelectData = productData.product
            if (productId.isNotEmpty()) productSelectData.selectedId = productId
            dataList.add(productSelectData)
        } else {
            productId = productData.product.dataCollections.getOrNull(0)?.products?.getOrNull(0)?.id ?: ""
        }

        if (dataList.isNotEmpty()) adapter.renderList(dataList)
//        trackSearchResultCategories(it.data)
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

            val dropdownView = TopupBillsInputDropdownWidget(context, listener = object: TopupBillsInputDropdownWidget.OnClickListener{
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
        rechargeGeneralAnalytics.eventClickProductListDropdown(categoryId, operatorId)
        context?.let { context ->
            val dropdownBottomSheet = BottomSheetUnify()
            dropdownBottomSheet.setTitle(title)
            dropdownBottomSheet.setFullPage(true)
            dropdownBottomSheet.clearAction()
            dropdownBottomSheet.setCloseClickListener {
                dropdownBottomSheet.dismiss()
            }

            val dropdownView = RechargeGeneralProductSelectBottomSheet(context,
                    listener = object : RechargeGeneralProductSelectBottomSheet.OnClickListener{
                override fun onItemClicked(item: RechargeGeneralProductSelectData) {
                    dropdownBottomSheet.dismiss()

                    // Show label & store id for enquiry
                    field.setInputText(item.title, false)
                    productId = item.id
                    rechargeGeneralAnalytics.eventClickProductCard(categoryId, operatorId, productId)
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
            context?.run {
                startActivityForResult(
                        TopupBillsSearchNumberActivity.getCallingIntent(this,
                                ClientNumberType.TYPE_INPUT_NUMERIC,
                                inputData[PARAM_CLIENT_NUMBER] ?: "",
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
        moreInfoBottomSheet.setChild(infoTextView)
        moreInfoBottomSheet.clearAction()
        moreInfoBottomSheet.setCloseClickListener {
            moreInfoBottomSheet.dismiss()
        }
        fragmentManager?.run { moreInfoBottomSheet.show(this,"E-gold more info bottom sheet") }
    }

    private fun setupAutoFillData(data: TopupBillsRecommendation) {
        if (viewModel.operatorCluster.value is Success
                && (viewModel.operatorCluster.value as Success).data.operatorGroups.isNotEmpty()) {
            operatorId = data.operatorId
            productId = data.productId.toString()

            renderInitialData((viewModel.operatorCluster.value as Success).data)
        }
    }

    private fun renderFooter(data: TopupBillsMenuDetail) {
        val promos = data.promos
        val recommendations = data.recommendations

        val listProductTab = mutableListOf<TopupBillsTabItem>()
        var recentTransactionFragment: RechargeGeneralRecentTransactionFragment? = null
        var promoListFragment: RechargeGeneralPromoListFragment? = null
        if (recommendations.isNotEmpty()) {
            recentTransactionFragment = RechargeGeneralRecentTransactionFragment.newInstance(recommendations)
            listProductTab.add(TopupBillsTabItem(recentTransactionFragment, getString(R.string.recent_transaction_tab_title)))
        }
        if (promos.isNotEmpty()) {
            promoListFragment = RechargeGeneralPromoListFragment.newInstance(promos)
            listProductTab.add(TopupBillsTabItem(promoListFragment, getString(R.string.promo_tab_title)))
        }

        if (listProductTab.isNotEmpty()) {
            val pagerAdapter = TopupBillsProductTabAdapter(listProductTab, childFragmentManager)
            product_view_pager.adapter = pagerAdapter
            product_view_pager.offscreenPageLimit = listProductTab.size

            if (listProductTab.size > 1) {
                tab_layout.setupWithViewPager(product_view_pager)
                tab_layout.onTabSelected {
                    if (it.text == getString(R.string.promo_tab_title)) {
                        rechargeGeneralAnalytics.eventClickPromoTab(categoryId, operatorId)
                    }
                }
                tab_layout.show()
                product_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(p0: Int) {

                    }

                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                    }

                    override fun onPageSelected(pos: Int) {

                    }
                })

                // Hide widget title
                recentTransactionFragment?.run { toggleTitle(false) }
                promoListFragment?.run { toggleTitle(false) }
            }
            product_view_pager.show()
        } else {
            hideFooterView()
        }
    }

    private fun renderClientNumber(number: String) {
        inputData[PARAM_CLIENT_NUMBER] = number
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
        loading_view.hide()
        hideInputView()
        hideFooterView()
        adapter.showGetListError(e)
    }

    private fun hideInputView() {
        operator_cluster_select.hide()
        operator_select.hide()
        enquiry_button.hide()
    }

    private fun hideFooterView() {
        tab_layout.hide()
        separator.hide()
        product_view_pager.hide()
    }

    override fun loadData() {
        enquiry_button.show()
        loading_view.show()

        getMenuDetail(menuId)
        getFavoriteNumbers(categoryId)
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

    override fun onCustomInputClick(field: TopupBillsInputFieldWidget, position: Int, data: List<RechargeGeneralProductSelectData>?) {
        // If there is data open product select bottom sheet, else open favorite number activity
        if (data != null) {
            showProductSelectDropdown(field, data, "Pilih Produk")
            // TODO: Change bottom sheet title to dynamic
        } else {
            showFavoriteNumbersPage()
        }
    }

    private fun updateInputData(label: String, input: String, position: Int) {
        if (label.isNotEmpty() && input.isNotEmpty()) {
            rechargeGeneralAnalytics.eventInputManualNumber(categoryId, operatorId, position)
            inputData[label] = input
        } else {
            inputData.remove(label)
        }
        toggleEnquiryButton()
    }

    private fun toggleEnquiryButton() {
        enquiry_button.isEnabled = validateEnquiry()
        // TODO: Temporary fix - set enquiry label after setEnabled() on UnifyButton
        if (enquiryLabel.isNotEmpty()) enquiry_button.text = enquiryLabel
    }

    private fun setEnquiryButtonLabel(label: String) {
        if (label.isNotEmpty()) {
            enquiryLabel = label
            enquiry_button.text = enquiryLabel
        }
    }

    private fun validateEnquiry(): Boolean {
        return operatorId > 0 && productId.isNotEmpty() && inputData.size == inputDataSize
    }

    private fun enquire() {
        if (validateEnquiry()) {
            if (!userSession.isLoggedIn) {
                navigateToLoginPage()
            } else {
                getEnquiry(operatorId.toString(), productId, inputData)
            }
        }
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        enquiryData = data.enquiry
        renderCheckoutView(enquiryData)
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        setupAutoFillData(data.recommendations[0])
        renderFooter(data)
    }

    override fun processFavoriteNumbers(data: TopupBillsFavNumber) {
        favoriteNumbers = data.favNumberList
        if (favoriteNumbers.isNotEmpty()) {
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

    private fun renderCheckoutView(data: TopupBillsEnquiry) {
        context?.let { context ->
            checkoutBottomSheet = BottomSheetUnify()
            checkoutBottomSheet.setTitle(getString(R.string.checkout_view_title))
            checkoutBottomSheet.setCloseClickListener {
                rechargeGeneralAnalytics.eventCloseInquiry(categoryId, operatorId)
                checkoutBottomSheet.dismiss()
            }
            checkoutBottomSheet.setFullPage(true)
            checkoutBottomSheet.clearAction()

            val checkoutView = RechargeGeneralCheckoutBottomSheet(context)
            checkoutView.listener = this
            checkoutView.setPayload(data)
            checkoutBottomSheet.setChild(checkoutView)

            fragmentManager?.let { fm ->
                checkoutBottomSheet.show(fm, "checkout view bottom sheet")
            }
        }
    }

    override fun onClickCheckout(data: TopupBillsEnquiry) {
        if (::checkoutBottomSheet.isInitialized) checkoutBottomSheet.dismiss()
        // TODO: Change instant checkout constant when it is available
        rechargeGeneralAnalytics.eventClickBuy(categoryId, operatorId, false, data)
        processCheckout()
    }

    private fun processCheckout() {
        // Setup checkout pass data
        if (validateEnquiry()) {
            var checkoutPassDataBuilder = DigitalCheckoutPassData.Builder()
                    .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                    .categoryId(categoryId.toString())
                    .instantCheckout("0")
                    // TODO: Check promo
//                    .isPromo(if (selectedProduct.attributes.promo != null) "1" else "0")
                    .isPromo("1")
                    .operatorId(operatorId.toString())
                    .productId(productId)
                    .utmCampaign(categoryId.toString())
                    .utmContent(GlobalConfig.VERSION_NAME)
                    .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                    .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                    .voucherCodeCopied("")
            if (inputData.containsKey(PARAM_CLIENT_NUMBER)) {
                checkoutPassDataBuilder = checkoutPassDataBuilder.clientNumber(inputData[PARAM_CLIENT_NUMBER]!!)
            }
            if (inputData.containsKey(PARAM_ZONE_ID)) {
                checkoutPassDataBuilder = checkoutPassDataBuilder.zoneId(inputData[PARAM_ZONE_ID]!!)
            }
            val otherInputs = inputData.filter { it.key != PARAM_CLIENT_NUMBER && it.key != PARAM_ZONE_ID }
            if (otherInputs.isNotEmpty()) {
                checkoutPassDataBuilder = checkoutPassDataBuilder.fields(HashMap(otherInputs))
            }
            checkoutPassData = checkoutPassDataBuilder.build()

            processToCart()
        }
    }

    private fun getFirstOperatorId(cluster: RechargeGeneralOperatorCluster): Int {
        return cluster.operatorGroups.getOrNull(0)?.operators?.getOrNull(0)?.id ?: 0
    }

    private fun getClusterOfOperatorId(cluster: RechargeGeneralOperatorCluster, operatorId: Int): RechargeGeneralOperatorCluster.CatalogOperatorGroup? {
        cluster.operatorGroups.forEach { group ->
            group.operators.forEach { operator ->
                if (operator.id == operatorId) return group
            }
        }
        return null
    }

    fun onBackPressed() {
        rechargeGeneralAnalytics.eventClickBackButton(categoryId, operatorId)
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
        const val EXTRA_PARAM_ENQUIRY_DATA = "EXTRA_PARAM_ENQUIRY_DATA"

        const val INPUT_TYPE_NUMERIC = "input_numeric"
        const val INPUT_TYPE_ALPHANUMERIC = "input_alphanumeric"
        const val INPUT_TYPE_FAVORITE_NUMBER = "input_favorite"
        const val INPUT_TYPE_ENQUIRY_INFO = "enquiry"

        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_ZONE_ID = "zone_id"

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