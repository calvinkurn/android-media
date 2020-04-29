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
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ENQUIRY_PARAM_OPERATOR_ID
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget.Companion.SHOW_KEYBOARD_DELAY
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.common.topupbills.widget.TopupBillsWidgetInterface
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
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
import com.tokopedia.rechargegeneral.presentation.viewmodel.RechargeGeneralViewModel.Companion.NULL_PRODUCT_ERROR
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import com.tokopedia.rechargegeneral.util.RechargeGeneralAnalytics
import com.tokopedia.rechargegeneral.widget.RechargeGeneralCheckoutBottomSheet
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_recharge_general.*
import kotlinx.android.synthetic.main.view_recharge_general_product_input_info_bottom_sheet.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RechargeGeneralFragment: BaseTopupBillsFragment(),
        OnInputListener,
        RechargeGeneralAdapter.LoaderListener,
        RechargeGeneralCheckoutBottomSheet.CheckoutListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: RechargeGeneralViewModel
    lateinit var sharedViewModel: SharedRechargeGeneralViewModel
    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics
    @Inject
    lateinit var rechargeGeneralAnalytics: RechargeGeneralAnalytics
    private var saveInstanceManager: SaveInstanceCacheManager? = null

    lateinit var adapter: RechargeGeneralAdapter

    private lateinit var favoriteNumbers: List<TopupBillsFavNumberItem>
    private var inputData: HashMap<String, String> = hashMapOf()
    private lateinit var inputDataKeys: List<String>

    private var operatorId: Int = 0
    set(value) {
        field = value
        // Get operator name for tracking
        operatorName = getOperatorData(value)?.attributes?.name?.toLowerCase(Locale.getDefault()) ?: ""
    }
    private var selectedProduct: RechargeGeneralProductSelectData? = null
        set(value) {
            field = value
            value?.run {
                productId = id.toIntOrNull() ?: 0
                productName = title
                this@RechargeGeneralFragment.price = price.toIntOrNull() ?: 0

                // Show product info ticker
                if (description.isNotEmpty()) {
                    ticker_recharge_general_product_info.show()
                    ticker_recharge_general_product_info.setHtmlDescription(description)
                } else {
                    ticker_recharge_general_product_info.hide()
                }
            }
        }
    private var operatorCluster: String = ""
    private var hasInputData = false
    private var hasFavoriteNumbers = false

    var pendingPromoData: PromoData? = null
    override var promoTicker: TickerPromoStackingCheckoutView? = null
        set(value) {
            field = value
            value?.run {
                if (isExpressCheckout) {
                    this.show()
                    pendingPromoData?.let { promoData ->
                        setupPromoTicker(promoData)
                        pendingPromoData = null
                    }
                } else {
                    this.hide()
                }
            }
        }

    private var enquiryLabel = ""
    private var enquiryData: TopupBillsEnquiry? = null

    private lateinit var checkoutBottomSheet: BottomSheetUnify

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recharge_general, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(RechargeGeneralViewModel::class.java)
            sharedViewModel = viewModelProvider.get(SharedRechargeGeneralViewModel::class.java)

            saveInstanceManager = SaveInstanceCacheManager(it, savedInstanceState)
            val savedEnquiryData: TopupBillsEnquiry? = saveInstanceManager!!.get(EXTRA_PARAM_ENQUIRY_DATA, TopupBillsEnquiry::class.java)
            if (savedEnquiryData != null) {
                enquiryData = savedEnquiryData
                productId = enquiryData?.attributes?.productId?.toIntOrNull() ?: 0
                price = enquiryData?.attributes?.price?.toIntOrNull() ?: 0
            }

            adapter = RechargeGeneralAdapter(it, RechargeGeneralAdapterFactory(this), this)
        }

        arguments?.let {
            categoryId = it.getInt(EXTRA_PARAM_CATEGORY_ID, 0)
            menuId = it.getInt(EXTRA_PARAM_MENU_ID, 0)
            operatorId = it.getInt(EXTRA_PARAM_OPERATOR_ID, 0)
            selectedProduct = it.getParcelable(EXTRA_PARAM_PRODUCT)
            hasInputData = operatorId > 0
        }
    }

    override fun onPause() {
        super.onPause()
        if (::checkoutBottomSheet.isInitialized) checkoutBottomSheet.dismiss()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.operatorCluster.observe(this, Observer {
            when(it) {
                is Success -> {
                    loading_view.hide()
                    renderInitialData()
                }
                is Fail -> {
                    var throwable = it.throwable
                    if (throwable.message == NULL_PRODUCT_ERROR)
                        throwable = MessageErrorException(getString(R.string.selection_null_product_error))
                    showGetListError(throwable)
                }
            }
        })

        viewModel.productList.observe(this, Observer {
            when(it) {
                is Success -> {
                    setupInputAndProduct(it.data)
                    if (hasFavoriteNumbers) {
                        updateFavoriteNumberInputField()
                    }
                    adapter.hideLoading()
                }
                is Fail -> {
                    val previousOperatorId = operatorId
                    resetInputData()
                    // Get default operator & compare previous operator;
                    // If they're different fetch product data
                    val defaultOperatorId = getDefaultOperatorId()
                    if (previousOperatorId != defaultOperatorId) {
                        operatorId = defaultOperatorId
                        val defaultOperatorName = getOperatorData(operatorId)?.attributes?.name ?: ""
                        operator_select.setInputText(defaultOperatorName, false)
                        getProductList(menuId, operatorId)
                    } else {
                        adapter.hideLoading()
                    }
                    NetworkErrorHelper.showRedSnackbar(activity, getString(R.string.selection_null_product_error))
                }
            }
        })

        sharedViewModel.recommendationItem.observe(this, Observer {
            val operatorClusters = viewModel.operatorCluster.value
            if (operatorClusters is Success) {
                rechargeGeneralAnalytics.eventClickRecentIcon(it, categoryName, it.position)
                operatorId = it.operatorId
                selectedProduct = RechargeGeneralProductSelectData(it.productId.toString(), it.title, it.description)
                inputData[PARAM_CLIENT_NUMBER] = it.clientNumber
                renderInitialData()
                // Enquire & navigate to checkout
                enquire()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            categoryId = savedInstanceState.getInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
            menuId = savedInstanceState.getInt(EXTRA_PARAM_MENU_ID, menuId)
            operatorId = savedInstanceState.getInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
            selectedProduct = savedInstanceState.getParcelable(EXTRA_PARAM_PRODUCT)
            inputData = (savedInstanceState.getSerializable(EXTRA_PARAM_INPUT_DATA) as? HashMap<String, String>) ?: inputData
            inputDataKeys = savedInstanceState.getStringArrayList(EXTRA_PARAM_INPUT_DATA_KEYS)?.toList() ?: inputDataKeys
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
        recharge_general_enquiry_button.isEnabled = false
        recharge_general_enquiry_button.setOnClickListener {
            enquire()
        }

        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DIGITAL_SEARCH_NUMBER -> {
                    val favNumber = data?.getParcelableExtra<TopupBillsFavNumberItem>(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER)
                    favNumber?.run {
                        hasInputData = true
                        rechargeGeneralAnalytics.eventInputFavoriteNumber(categoryName, operatorName)
                        renderClientNumber(this)
                    }
                }
                REQUEST_CODE_LOGIN -> {
                    enquire()
                }
                REQUEST_CODE_PROMO_LIST, REQUEST_CODE_PROMO_DETAIL -> {
                    // Render enquiry data
                    enquiryData?.let{
                        renderCheckoutView(it)
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Render enquiry data
            enquiryData?.let{
                renderCheckoutView(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
        outState.putInt(EXTRA_PARAM_MENU_ID, menuId)
        outState.putInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
        outState.putParcelable(EXTRA_PARAM_PRODUCT, selectedProduct)
        outState.putSerializable(EXTRA_PARAM_INPUT_DATA, inputData)
        if (::inputDataKeys.isInitialized) {
            outState.putStringArrayList(EXTRA_PARAM_INPUT_DATA_KEYS, ArrayList(inputDataKeys))
        }
        enquiryData?.let { data ->
            saveInstanceManager?.apply {
                onSave(outState)
                put(EXTRA_PARAM_ENQUIRY_DATA, data)
            }
        }
    }

    private fun renderInitialData() {
        if (viewModel.operatorCluster.value is Success) {
            val operatorClusters = (viewModel.operatorCluster.value as Success).data
            operatorClusters.operatorGroups?.let { groups ->
                if (operatorId == 0) operatorId = getFirstOperatorId(operatorClusters)
                if (operatorId > 0) {
                    operatorCluster = getClusterOfOperatorId(operatorClusters, operatorId)?.name ?: ""
                    renderOperatorCluster(operatorClusters)

                    val operatorGroup = groups.first { it.name == operatorCluster }
                    val isOperatorHidden = operatorClusters.style == OPERATOR_TYPE_HIDDEN
                    renderOperatorList(operatorGroup, isOperatorHidden, operatorClusters.text)

                    if (operatorGroup.operators.size > 1) getProductList(menuId, operatorId)
                }
            }
        }
    }

    private fun renderOperatorCluster(cluster: RechargeGeneralOperatorCluster) {
        cluster.operatorGroups?.let { groups ->
            if (groups.size == 1) {
                operator_cluster_select.hide()
            } else if (groups.size > 1) {
                operator_cluster_select.show()
                operator_cluster_select.setLabel(getString(R.string.operator_cluster_select_label))
                operator_cluster_select.setHint("")
                operator_cluster_select.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                    override fun onFinishInput(input: String) {
                        if (operatorCluster != input) {
                            resetInputData()
                            operatorCluster = input
                            // Remove selected operator
                            operator_select.setInputText("", false)
                            rechargeGeneralAnalytics.eventChooseOperatorCluster(categoryName, operatorCluster)

                            val isOperatorHidden = cluster.style == OPERATOR_TYPE_HIDDEN
                            groups.find { it.name == input }?.let {
                                renderOperatorList(it, isOperatorHidden, cluster.text)
                            }
                        }
                    }

                    override fun onCustomInputClick() {
                        rechargeGeneralAnalytics.eventClickOperatorClusterDropdown(categoryName)

                        val dropdownData = groups.map { TopupBillsInputDropdownData(it.name) }
                        showOperatorSelectDropdown(operator_cluster_select, dropdownData, cluster.text)
                    }
                }
                operator_cluster_select.show()

                // Set cluster name
                if (operatorCluster.isNotEmpty()) operator_cluster_select.setInputText(operatorCluster, false)
            }
        }
        if (cluster.operatorGroups == null) showGetListError(MessageErrorException(getString(R.string.selection_null_product_error)))
    }

    private fun renderOperatorList(operatorGroup: RechargeGeneralOperatorCluster.CatalogOperatorGroup, isHidden: Boolean, label: String) {
         if (operatorGroup.operators.isNotEmpty()) {
            operator_select.show()
            operator_select.setLabel(label)
            operator_select.setHint("")
            operator_select.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    operatorGroup.operators.find { it.attributes.name == input }?.let {
                        if (operatorId != it.id) {
                            // Save operator id for enquiry
                            resetInputData()
                            operatorId = it.id
                            rechargeGeneralAnalytics.eventChooseOperator(categoryName, operatorName)

                            adapter.showLoading()
                            getProductList(menuId, it.id)
                        }
                    }
                }

                override fun onCustomInputClick() {
                    rechargeGeneralAnalytics.eventClickOperatorListDropdown(categoryName)

                    val dropdownData = operatorGroup.operators.map {
                        TopupBillsInputDropdownData(it.attributes.name, it.attributes.imageUrl)
                    }
                    showOperatorSelectDropdown(operator_select, dropdownData)
                }
            }

             if (operatorGroup.operators.size == 1) {
                 if (isHidden) operator_select.hide()
                 // Get product data based on operator id
                 operatorId = operatorGroup.operators.firstOrNull()?.id ?: 0
                 adapter.showLoading()
                 getProductList(menuId, operatorId)
             }
        }

        // Set operator name
        if (operatorId > 0) {
            operatorGroup.operators.find { it.id == operatorId }?.attributes?.name?.let { name ->
                operator_select.setInputText(name, false)
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
                    if (inputData.containsKey(item.name)) {
                        item.value = inputData[item.name]!!
                    }
                    itr.set(item)
                }
                dataList.addAll(enquiryFields)

                inputDataKeys = enquiryFields.map { it.name }
            }
        }

        // Show product field if there is > 1 product
        if (productData.isShowingProduct) {
            val productSelectData = productData.product
            productSelectData?.apply {
                selectedProduct?.run { selectedId = id }
                dataList.add(this)
            }
        } else {
            val product = productData.product?.dataCollections?.getOrNull(0)?.products?.getOrNull(0)
            product?.let {
                with (it.attributes) {
                    val slashedPrice = if (promo != null) price else ""
                    selectedProduct = RechargeGeneralProductSelectData(
                            it.id,
                            desc,
                            detailCompact,
                            promo?.newPrice ?: price,
                            slashedPrice,
                            isPromo = promo != null)
                }
            }
        }

        if (dataList.isNotEmpty()) adapter.renderList(dataList)
    }

    // Reset product id & input data
    private fun resetInputData() {
        operatorId = 0
        selectedProduct = null
        inputData = hashMapOf()
        // Reset product info ticker
        ticker_recharge_general_product_info.hide()
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
        rechargeGeneralAnalytics.eventClickProductListDropdown(categoryName, operatorName)
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
                    selectedProduct = item
                    rechargeGeneralAnalytics.eventClickProductCard(categoryName, operatorName, item.title.toLowerCase())
                    toggleEnquiryButton()
                }
            })
            dropdownView.dropdownData = data
            dropdownBottomSheet.setChild(dropdownView)
            fragmentManager?.run { dropdownBottomSheet.show(this,"Product select dropdown bottom sheet") }
        }
    }

    private fun showFavoriteNumbersPage(favoriteNumbers: List<TopupBillsFavNumberItem>,
                                        enquiryData: RechargeGeneralProductInput) {
        if (favoriteNumbers.isNotEmpty()) {
            val clientNumberType = when (enquiryData.style) {
                TopupBillsInputFieldWidget.INPUT_NUMERIC -> ClientNumberType.TYPE_INPUT_NUMERIC
                TopupBillsInputFieldWidget.INPUT_ALPHANUMERIC -> ClientNumberType.TYPE_INPUT_ALPHANUMERIC
                TopupBillsInputFieldWidget.INPUT_TELCO -> ClientNumberType.TYPE_INPUT_TEL
                else -> ClientNumberType.TYPE_INPUT_NUMERIC
            }

            context?.run {
                startActivityForResult(
                        TopupBillsSearchNumberActivity.getCallingIntent(this,
                                clientNumberType,
                                inputData[PARAM_CLIENT_NUMBER] ?: "",
                                favoriteNumbers), REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
            }
        }
    }

    override fun onInfoClick(text: String) {
        showInfoBottomSheets(text)
    }

    private fun showInfoBottomSheets(helpText: String) {
        val infoTextView = View.inflate(context,R.layout.view_recharge_general_product_input_info_bottom_sheet, null)
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
        with (data) {
            this@RechargeGeneralFragment.operatorId = operatorId
            selectedProduct = RechargeGeneralProductSelectData(productId.toString(), title)
            if (clientNumber.isNotEmpty()) {
                inputData[PARAM_CLIENT_NUMBER] = clientNumber
            }
        }
        renderInitialData()
    }

    private fun renderTickers(tickers: List<TopupBillsTicker>) {
        if (tickers.isNotEmpty()) {
            val messages = mutableListOf<TickerData>()
            for (item in tickers) {
                var description: String = item.content
                if (item.actionText.isNotEmpty() && item.actionLink.isNotEmpty()) {
                    description += " [${item.actionText}]{${item.actionLink}}"
                }
                messages.add(TickerData(item.name, description,
                        when (item.type) {
                            TopupBillsTicker.TYPE_WARNING -> Ticker.TYPE_WARNING
                            TopupBillsTicker.TYPE_INFO -> Ticker.TYPE_INFORMATION
                            TopupBillsTicker.TYPE_SUCCESS -> Ticker.TYPE_ANNOUNCEMENT
                            TopupBillsTicker.TYPE_ERROR -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }))
            }

            if (messages.size == 1) {
                with (messages.first()) {
                    recharge_general_ticker.tickerTitle = title
                    recharge_general_ticker.setHtmlDescription(description)
                    recharge_general_ticker.tickerType = type
                }
                recharge_general_ticker.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
                    }

                    override fun onDismiss() {

                    }
                })
            } else {
                context?.let { context ->
                    val tickerAdapter = TickerPagerAdapter(context, messages)
                    tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                            RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
                        }
                    })
                    recharge_general_ticker.addPagerView(tickerAdapter, messages)
                }
            }

            recharge_general_ticker.visibility = View.VISIBLE
        } else {
            recharge_general_ticker.visibility = View.GONE
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
                        rechargeGeneralAnalytics.eventClickPromoTab(categoryName, operatorName)
                    }
                }
                tab_layout.show()

                // Hide widget title
                (product_view_pager.getChildAt(0) as TopupBillsWidgetInterface).toggleTitle(false)
                (product_view_pager.getChildAt(1) as TopupBillsWidgetInterface).toggleTitle(false)
            }
            product_view_pager.show()
        } else {
            hideFooterView()
        }
    }

    private fun renderClientNumber(favNumber: TopupBillsFavNumberItem) {
        with (favNumber) {
            operatorId.toIntOrNull()?.let { oprId -> this@RechargeGeneralFragment.operatorId = oprId }
            if (clientNumber.isNotEmpty()) inputData[PARAM_CLIENT_NUMBER] = clientNumber
            if (productId.isNotEmpty()) selectedProduct = RechargeGeneralProductSelectData(productId)

            if (adapter.data.isNotEmpty()) {
                val clientNumberInput: RechargeGeneralProductInput? = adapter.data.find { it is RechargeGeneralProductInput && it.name == PARAM_CLIENT_NUMBER } as? RechargeGeneralProductInput
                clientNumberInput?.apply {
                    if (clientNumber.isNotEmpty()) value = clientNumber
                    isFavoriteNumber = true
                }
                adapter.notifyItemChanged(adapter.data.indexOf(clientNumberInput))
            }
        }
        renderInitialData()
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
        recharge_general_enquiry_button.hide()
    }

    private fun hideFooterView() {
        tab_layout.hide()
        separator.hide()
        product_view_pager.hide()
    }

    override fun loadData() {
        recharge_general_enquiry_button.show()
        loading_view.show()

        getMenuDetail(menuId)
        getFavoriteNumbers(categoryId)
        getCatalogPluginData(operatorId, categoryId)
        getOperatorCluster(menuId)
    }

    private fun getOperatorCluster(menuId: Int) {
        viewModel.getOperatorCluster(
                GraphqlHelper.loadRawString(resources, R.raw.query_catalog_operator_select_group),
                viewModel.createOperatorClusterParams(menuId)
        )
    }

    private fun getProductList(menuId: Int, operator: Int) {
        viewModel.getProductList(
                GraphqlHelper.loadRawString(resources, com.tokopedia.common.topupbills.R.raw.query_catalog_product_input),
                viewModel.createProductListParams(menuId, operator)
        )
    }

    override fun onFinishInput(label: String, input: String, position: Int, isManual: Boolean) {
        if (label.isNotEmpty() && input.isNotEmpty() && isManual) {
            rechargeGeneralAnalytics.eventInputManualNumber(categoryName, operatorName, position + 1)
        }
        updateInputData(label, input)
    }

    override fun onCustomInputClick(field: TopupBillsInputFieldWidget,
                                    enquiryData: RechargeGeneralProductInput?,
                                    productData: List<RechargeGeneralProductSelectData>?) {
        // If there is data open product select bottom sheet, else open favorite number activity
        if (productData != null) {
            showProductSelectDropdown(field, productData, getString(R.string.product_select_label))
        } else if (enquiryData != null) {
            showFavoriteNumbersPage(favoriteNumbers, enquiryData)
        }
    }

    private fun updateInputData(label: String, input: String) {
        if (label.isNotEmpty() && input.isNotEmpty()) {
            inputData[label] = input
        } else {
            inputData.remove(label)
        }
        toggleEnquiryButton()
    }

    private fun toggleEnquiryButton() {
        recharge_general_enquiry_button.isEnabled = validateEnquiry()
        if (enquiryLabel.isNotEmpty()) recharge_general_enquiry_button.text = enquiryLabel
    }

    private fun setEnquiryButtonLabel(label: String) {
        if (label.isNotEmpty()) {
            enquiryLabel = label
            recharge_general_enquiry_button.text = enquiryLabel
        }
    }

    private fun validateEnquiry(): Boolean {
        return operatorId > 0 && selectedProduct != null
                && ::inputDataKeys.isInitialized
                && inputData.keys.toList().sorted() == inputDataKeys.sorted()
    }

    private fun enquire() {
        if (validateEnquiry()) {
            // If it's express checkout, open checkout bottomsheet; if not navigate to old checkout
            if (!isExpressCheckout) {
                processCheckout()
            } else {
                if (!userSession.isLoggedIn) {
                    navigateToLoginPage()
                } else {
                    selectedProduct?.run { getEnquiry(operatorId.toString(), id, inputData) }
                }
            }
        }
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        enquiryData = data.enquiry
        price = data.enquiry.attributes.pricePlain
        renderCheckoutView(data.enquiry)
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        super.processMenuDetail(data)
        with (data.catalog) {
            (activity as? BaseSimpleActivity)?.updateTitle(label)
            rechargeAnalytics.eventOpenScreen(userSession.isLoggedIn, categoryName, categoryId.toString())
        }
        renderTickers(data.tickers)
        // Set recommendation data if available
        if (data.recommendations.isNotEmpty() && !hasInputData) {
            setupAutoFillData(data.recommendations[0])
        }
        renderFooter(data)
    }

    override fun processFavoriteNumbers(data: TopupBillsFavNumber) {
        favoriteNumbers = data.favNumberList
        updateFavoriteNumberInputField()
    }

    private fun updateFavoriteNumberInputField() {
        if (favoriteNumbers.isNotEmpty()) {
            if (adapter.data.isNotEmpty()) {
                val clientNumberInput: RechargeGeneralProductInput? = adapter.data.find { it is RechargeGeneralProductInput && it.name == PARAM_CLIENT_NUMBER } as? RechargeGeneralProductInput
                clientNumberInput?.apply { isFavoriteNumber = true }
                adapter.notifyItemChanged(adapter.data.indexOf(clientNumberInput))
            } else { // Store favorite number state
                hasFavoriteNumbers = true
            }
        }
    }

    override fun onEnquiryError(error: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, error.message)
    }

    override fun onMenuDetailError(error: Throwable) {
        showGetListError(error)
    }

    override fun onCatalogPluginDataError(error: Throwable) {

    }

    override fun onFavoriteNumbersError(error: Throwable) {

    }

    override fun onCheckVoucherError(error: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, error.message)
    }

    override fun onExpressCheckoutError(error: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, error.message)
    }

    private fun renderCheckoutView(data: TopupBillsEnquiry) {
        context?.let { context ->
            checkoutBottomSheet = BottomSheetUnify()
            checkoutBottomSheet.setTitle(getString(R.string.checkout_view_title))
            checkoutBottomSheet.setCloseClickListener {
                rechargeGeneralAnalytics.eventCloseInquiry(categoryName, operatorName)
                promoTicker = null
                checkoutBottomSheet.dismiss()
            }
            checkoutBottomSheet.setFullPage(true)
            checkoutBottomSheet.clearAction()

            val checkoutView = RechargeGeneralCheckoutBottomSheet(context, listener = this)
            checkoutView.setPayload(data)
            checkoutBottomSheet.setChild(checkoutView)

            promoTicker = checkoutView.getPromoTicker()
            promoTicker?.actionListener = getPromoListener()
            checkVoucher()

            fragmentManager?.let { fm ->
                checkoutBottomSheet.show(fm, "checkout view bottom sheet")
            }
        }
    }

    // Exception because checkout view is located in bottom sheet
    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return null
    }

    override fun onPromoTickerNull(data: PromoData) {
        pendingPromoData = data
    }

    override fun onClickCheckout(data: TopupBillsEnquiry) {
        rechargeGeneralAnalytics.eventClickCheckBills(categoryName, operatorName, selectedProduct?.title ?: "")
        rechargeGeneralAnalytics.eventClickBuy(categoryName, operatorName, false, data)

        processCheckout()
    }

    private fun processCheckout() {
        // Setup checkout pass data
        if (validateEnquiry()) {
            selectedProduct?.run {
                var checkoutPassDataBuilder = DigitalCheckoutPassData.Builder()
                        .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                        .categoryId(categoryId.toString())
                        .instantCheckout("0")
                        .isPromo(if (isPromo) "1" else "0")
                        .operatorId(operatorId.toString())
                        .productId(id)
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
            }
        }

        // Put operatorId in input list
        val inputs = inputData.toMutableMap()
        inputs[ENQUIRY_PARAM_OPERATOR_ID] = operatorId.toString()
        inputFields = inputs

        processTransaction()
    }

    private fun getDefaultOperatorId(): Int {
        val operatorCluster = viewModel.operatorCluster.value
        if (operatorCluster is Success) {
            return getFirstOperatorId(operatorCluster.data)
        }
        return 0
    }

    private fun getFirstOperatorId(cluster: RechargeGeneralOperatorCluster): Int {
        return cluster.operatorGroups?.getOrNull(0)?.operators?.getOrNull(0)?.id ?: 0
    }

    private fun getOperatorData(operatorId: Int): CatalogOperator? {
        val operatorCluster = viewModel.operatorCluster.value
        if (operatorCluster is Success) {
            return getOperatorDataOfOperatorId(operatorCluster.data, operatorId)
        }
        return null
    }

    private fun getClusterOfOperatorId(cluster: RechargeGeneralOperatorCluster, operatorId: Int): RechargeGeneralOperatorCluster.CatalogOperatorGroup? {
        cluster.operatorGroups?.forEach { group ->
            group.operators.forEach { operator ->
                if (operator.id == operatorId) return group
            }
        }
        return null
    }

    private fun getOperatorDataOfOperatorId(cluster: RechargeGeneralOperatorCluster, operatorId: Int): CatalogOperator? {
        cluster.operatorGroups?.forEach { group ->
            group.operators.forEach { operator ->
                if (operator.id == operatorId) return operator
            }
        }
        return null
    }

    fun onBackPressed() {
        rechargeGeneralAnalytics.eventClickBackButton(categoryName, operatorName)
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
        const val EXTRA_PARAM_PRODUCT = "EXTRA_PARAM_PRODUCT_ID"
        const val EXTRA_PARAM_INPUT_DATA = "EXTRA_PARAM_INPUT_DATA"
        const val EXTRA_PARAM_INPUT_DATA_KEYS = "EXTRA_PARAM_INPUT_DATA_KEYS"
        const val EXTRA_PARAM_ENQUIRY_DATA = "EXTRA_PARAM_ENQUIRY_DATA"

        const val OPERATOR_TYPE_VISIBLE = "select_dropdown"
        const val OPERATOR_TYPE_HIDDEN = "hidden"

        const val INPUT_TYPE_FAVORITE_NUMBER = "input_favorite"
        const val INPUT_TYPE_ENQUIRY_INFO = "enquiry"

        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_ZONE_ID = "zone_id"

        const val REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 77

        val ITEM_DECORATOR_SIZE = com.tokopedia.design.R.dimen.dp_8

        fun newInstance(categoryId: Int,
                        menuId: Int,
                        operatorId: Int = 0,
                        selectedProduct: String = ""): RechargeGeneralFragment {
            val fragment = RechargeGeneralFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
            bundle.putInt(EXTRA_PARAM_MENU_ID, menuId)
            bundle.putInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
            bundle.putString(EXTRA_PARAM_PRODUCT, selectedProduct)
            fragment.arguments = bundle
            return fragment
        }
    }
}