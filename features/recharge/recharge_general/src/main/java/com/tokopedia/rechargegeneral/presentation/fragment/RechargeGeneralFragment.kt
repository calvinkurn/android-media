package com.tokopedia.rechargegeneral.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.analytics.PromotionMultiCheckout
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.ACTION_GENERAL_MYBILLS
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.PREFERENCE_MULTICHECKOUT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.SHOW_COACH_MARK_MULTICHECKOUT_KEY
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.WHITE_COLOR
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.bottomsheet.TopupBillsMenuBottomSheets
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ENQUIRY_PARAM_OPERATOR_ID
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget.Companion.SHOW_KEYBOARD_DELAY
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.common.presentation.bottomsheet.DigitalDppoConsentBottomSheet
import com.tokopedia.common_digital.common.presentation.model.DigitalAtcTrackingModel
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.databinding.FragmentRechargeGeneralBinding
import com.tokopedia.rechargegeneral.databinding.ViewRechargeGeneralProductInputInfoBottomSheetBinding
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.model.RechargeGeneralDynamicField
import com.tokopedia.rechargegeneral.model.RechargeGeneralDynamicInput
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import com.tokopedia.rechargegeneral.model.mapper.RechargeGeneralMapper
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity.Companion.RECHARGE_PRODUCT_EXTRA
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapter
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapterFactory
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.OnInputListener
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectData
import com.tokopedia.rechargegeneral.presentation.viewmodel.RechargeGeneralViewModel
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import com.tokopedia.rechargegeneral.util.RechargeGeneralAnalytics
import com.tokopedia.rechargegeneral.util.RechargeGeneralGqlQuery
import com.tokopedia.rechargegeneral.widget.RechargeGeneralCheckoutBottomSheet
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.resources.common.R as resourcescommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class RechargeGeneralFragment :
    BaseTopupBillsFragment(),
    OnInputListener,
    RechargeGeneralAdapter.LoaderListener,
    RechargeGeneralCheckoutBottomSheet.CheckoutListener,
    TopupBillsMenuBottomSheets.MenuListener {

    private var binding by autoClearedNullable<FragmentRechargeGeneralBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }

    lateinit var viewModel: RechargeGeneralViewModel
    lateinit var sharedViewModel: SharedRechargeGeneralViewModel

    @Inject
    lateinit var rechargeGeneralAnalytics: RechargeGeneralAnalytics

    @Inject
    lateinit var mapper: RechargeGeneralMapper

    private var saveInstanceManager: SaveInstanceCacheManager? = null

    lateinit var adapter: RechargeGeneralAdapter

    private var favoriteNumbers: List<TopupBillsSearchNumberDataModel> = emptyList()
    private var inputData: HashMap<String, String> = hashMapOf()
    private var inputDataKeys = mutableListOf<String>()

    var rechargeProductFromSlice: String = ""

    private var coachmark: CoachMark2? = null
    private var loyaltyStatus: String = ""
    private var isAlreadyTrackImpressionMultiButton: Boolean = false
    private var multiCheckoutButton: RechargeGeneralDynamicField? = null

    private var operatorId: Int = 0
        set(value) {
            field = value
            // Get operator name for tracking
            operatorName = getOperatorData(value)?.attributes?.name ?: ""
        }
    override var productId: Int = 0
        set(value) {
            field = value
            updateProductData()
        }
    private var needProductDataUpdate = false
    private var isPromo: Boolean = false
    private var operatorCluster: String = ""
    private var hasInputData = false

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

    private var isEnableButtonEnquiry : MutableState<Boolean> = mutableStateOf(false)
    private var titleButtonEnquiry: MutableState<String> = mutableStateOf(context?.resources?.getString(R.string.enquiry_label) ?: "")
    private var actionButtonEnquiry: MutableState<(() -> Unit)> = mutableStateOf({})
    private var isLoadingButtonEnquiry: MutableState<Boolean> = mutableStateOf(false)
    private var visibilityButtonEnquiry: MutableState<Boolean> = mutableStateOf(false)
    private var variantButtonEnquiry: MutableState<ButtonVariant> = mutableStateOf(ButtonVariant.FILLED)
    private var viewEnquiry: View? = null

    private var isEnableButtonSecondary : MutableState<Boolean> = mutableStateOf(false)
    private var titleButtonSecondary: MutableState<String> = mutableStateOf(context?.resources?.getString(R.string.enquiry_label) ?: "")
    private var actionButtonSecondary: MutableState<(() -> Unit)> = mutableStateOf({})
    private var isLoadingButtonSecondary: MutableState<Boolean> = mutableStateOf(false)
    private var visibilityButtonSecondary: MutableState<Boolean> = mutableStateOf(false)
    private var variantButtonSecondary: MutableState<ButtonVariant> = mutableStateOf(ButtonVariant.FILLED)
    private var viewSecondary: View? = null

    override fun onUpdateMultiCheckout() {
        //do nothing
    }

    override fun onTrackMultiCheckoutAtc(atc: DigitalAtcTrackingModel) {
        multiCheckoutButton?.let { multiCheckoutButton ->
            commonMultiCheckoutAnalytics.onClickMultiCheckout(
                categoryName,
                operatorName,
                atc.channelId,
                userSession.userId,
                multiCheckoutButtonPromotion(
                    multiCheckoutButton.items
                )
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRechargeGeneralBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.let {
            viewModel = viewModelFragmentProvider.get(RechargeGeneralViewModel::class.java)
            sharedViewModel = viewModelFragmentProvider.get(SharedRechargeGeneralViewModel::class.java)

            saveInstanceManager = SaveInstanceCacheManager(it, savedInstanceState)
            val savedEnquiryData: TopupBillsEnquiry? = saveInstanceManager!!.get(EXTRA_PARAM_ENQUIRY_DATA, TopupBillsEnquiry::class.java)
            if (savedEnquiryData != null) {
                enquiryData = savedEnquiryData
                productId = enquiryData?.attributes?.productId?.toIntSafely().toZeroIfNull()
                price = enquiryData?.attributes?.price?.toIntSafely().toZeroIfNull()
            }

            adapter = RechargeGeneralAdapter(it, RechargeGeneralAdapterFactory(this), this)
        }

        arguments?.let {
            categoryId = it.getInt(EXTRA_PARAM_CATEGORY_ID, 0)
            menuId = it.getInt(EXTRA_PARAM_MENU_ID, 0)
            operatorId = it.getInt(EXTRA_PARAM_OPERATOR_ID, 0)
            productId = it.getInt(EXTRA_PARAM_PRODUCT_ID, 0)
            hasInputData = operatorId > 0
            rechargeProductFromSlice = it.getString(RECHARGE_PRODUCT_EXTRA, "")
        }
    }

    override fun onPause() {
        super.onPause()
        if (::checkoutBottomSheet.isInitialized) checkoutBottomSheet.dismiss()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.operatorCluster.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        binding?.loadingView?.hide()
                        renderInitialData()
                    }
                    is Fail -> {
                        hideLoading()
                        showGetListError(it.throwable)
                    }
                }
            }
        )

        viewModel.productList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        setupInputAndProduct(it.data)
                        updateFavoriteNumberInputField()
                        if (needProductDataUpdate) updateProductData()
                        hideLoading()
                    }
                    is Fail -> {
                        val previousOperatorId = operatorId
                        resetInputData()
                        // Get default operator & compare previous operator;
                        // If they're different fetch product data
                        val defaultOperatorId = getDefaultOperatorId()
                        if (previousOperatorId != defaultOperatorId) {
                            operatorId = defaultOperatorId
                            val defaultOperatorName = getOperatorData(operatorId)?.attributes?.name
                                ?: ""
                            binding?.operatorSelect?.setInputText(defaultOperatorName, false)
                            getProductList(menuId, operatorId.toString())
                        } else {
                            hideLoading()
                        }

                        view?.let { v ->
                            var throwable = it.throwable
                            if (throwable.message.isNullOrEmpty()) {
                                throwable = MessageErrorException(getString(R.string.selection_null_product_error))
                            }
                            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                                requireContext(),
                                throwable,
                                ErrorHandler.Builder()
                                    .className(this::class.java.simpleName)
                                    .build()
                            )
                            Toaster.build(
                                v,
                                errorMessage.orEmpty(),
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(resourcescommonR.string.general_label_ok)
                            ).show()
                        }
                    }
                }
            }
        )

        sharedViewModel.recommendationItem.observe(
            viewLifecycleOwner,
            Observer {
                val operatorClusters = viewModel.operatorCluster.value
                if (operatorClusters is Success) {
                    rechargeGeneralAnalytics.eventClickRecentIcon(it, categoryName, it.position)
                    operatorId = it.operatorId.toIntOrZero()
                    productId = it.productId.toIntOrZero()
                    inputData[PARAM_CLIENT_NUMBER] = it.clientNumber
                    renderInitialData()
                    // Enquire & navigate to checkout
                    enquire()
                }
            }
        )

        observe(viewModel.dppoConsent) {
            when (it) {
                is Success -> {
                    activity?.invalidateOptionsMenu()
                }
                is Fail -> {}
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            categoryId = savedInstanceState.getInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
            menuId = savedInstanceState.getInt(EXTRA_PARAM_MENU_ID, menuId)
            operatorId = savedInstanceState.getInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
            productId = savedInstanceState.getInt(EXTRA_PARAM_PRODUCT_ID)
            inputData = (savedInstanceState.getSerializable(EXTRA_PARAM_INPUT_DATA) as? HashMap<String, String>)
                ?: inputData
            inputDataKeys = savedInstanceState.getStringArrayList(EXTRA_PARAM_INPUT_DATA_KEYS)?.toMutableList()
                ?: inputDataKeys
        }

        if (rechargeProductFromSlice.isNotEmpty()) {
            rechargeGeneralAnalytics.onClickSliceRecharge(userSession.userId, rechargeProductFromSlice)
            rechargeGeneralAnalytics.onOpenPageFromSlice()
        }

        context?.let {
            coachmark = CoachMark2(it)
        }

        binding?.run {
            rvDigitalProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvDigitalProduct.adapter = adapter
            while (rvDigitalProduct.itemDecorationCount > 0) rvDigitalProduct.removeItemDecorationAt(0)
            rvDigitalProduct.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    // Add offset to all items except the last one
                    if (parent.getChildAdapterPosition(view) < adapter.dataSize - 1) {
                        context?.resources?.getDimension(ITEM_DECORATOR_SIZE)?.toInt()?.let { dimen -> outRect.bottom = dimen }
                    }
                }
            })
            isEnableButtonEnquiry.value = false
            isEnableButtonSecondary.value = false
            binding?.rechargeGeneralEnquiryButtonCompose?.isEnabled = false
            binding?.rechargeGeneralSecondaryButtonCompose?.isEnabled = false

            rechargeGeneralEnquiryButtonCompose.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    viewEnquiry = LocalView.current
                    if (visibilityButtonEnquiry.value) {
                        NestButton(
                            text = titleButtonEnquiry.value,
                            onClick = actionButtonEnquiry.value,
                            variant = variantButtonEnquiry.value,
                            size = ButtonSize.MEDIUM,
                            isEnabled = isEnableButtonEnquiry.value,
                            isClickable = isEnableButtonEnquiry.value,
                            isLoading = isLoadingButtonEnquiry.value,
                        )
                    }
                }
            }

            rechargeGeneralSecondaryButtonCompose.apply {
                hide()
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    viewSecondary = LocalView.current
                    if (visibilityButtonSecondary.value) {
                        NestButton(
                            text = titleButtonSecondary.value,
                            onClick = actionButtonSecondary.value,
                            variant = variantButtonSecondary.value,
                            size = ButtonSize.MEDIUM,
                            isEnabled = isEnableButtonSecondary.value,
                            isClickable = isEnableButtonSecondary.value,
                            isLoading = isLoadingButtonSecondary.value,
                        )
                    }
                }
            }

            rechargeGeneralSwipeRefreshLayout.setOnRefreshListener {
                rechargeGeneralSwipeRefreshLayout.isRefreshing = true
                loadData()
            }

            loadData()
        }

        viewModel.getDppoConsent(categoryId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DIGITAL_SEARCH_NUMBER -> {
                    val favNumber = data?.getParcelableExtra<TopupBillsSearchNumberDataModel>(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER)
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
                    enquiryData?.let {
                        renderCheckoutView(it)
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Render enquiry data
            enquiryData?.let {
                renderCheckoutView(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
        outState.putInt(EXTRA_PARAM_MENU_ID, menuId)
        outState.putInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
        outState.putInt(EXTRA_PARAM_PRODUCT_ID, productId)
        outState.putInt(EXTRA_PARAM_PRODUCT_ID, productId)
        if (inputDataKeys.isNotEmpty()) {
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
                    operatorCluster = getClusterOfOperatorId(operatorClusters, operatorId)?.name
                        ?: ""
                    renderOperatorCluster(operatorClusters)

                    val operatorGroup = groups.firstOrNull { it.name == operatorCluster }
                    operatorGroup?.run {
                        val isOperatorHidden = operatorClusters.style == OPERATOR_TYPE_HIDDEN
                        renderOperatorList(operatorGroup, isOperatorHidden, operatorClusters.text)

                        if (operatorGroup.operators.size > 1) getProductList(menuId, operatorId.toString())
                    }
                }
            }
        }
    }

    private fun renderOperatorCluster(cluster: RechargeGeneralOperatorCluster) {
        cluster.operatorGroups?.let { groups ->
            binding?.run {
                if (groups.size == 1) {
                    operatorClusterSelect.hide()
                } else if (groups.size > 1) {
                    operatorClusterSelect.show()
                    operatorClusterSelect.setLabel(getString(R.string.operator_cluster_select_label))
                    operatorClusterSelect.setHint("")
                    operatorClusterSelect.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                        override fun onFinishInput(input: String) {
                            if (operatorCluster != input) {
                                resetInputData()
                                operatorCluster = input
                                // Remove selected operator
                                operatorSelect.setInputText("", false)
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
                            showOperatorSelectDropdown(operatorClusterSelect, dropdownData, cluster.text)
                        }

                        override fun onTextChangeInput() {
                            // do nothing
                        }
                    }
                    operatorClusterSelect.show()

                    // Set cluster name
                    if (operatorCluster.isNotEmpty()) operatorClusterSelect.setInputText(operatorCluster, false)
                }
            }
        }
        if (cluster.operatorGroups == null) showGetListError(MessageErrorException(getString(R.string.selection_null_product_error)))
    }

    private fun renderOperatorList(operatorGroup: RechargeGeneralOperatorCluster.CatalogOperatorGroup, isHidden: Boolean, label: String) {
        binding?.run {
            if (operatorGroup.operators.isNotEmpty()) {
                operatorSelect.show()
                operatorSelect.setLabel(label)
                operatorSelect.setHint("")
                operatorSelect.actionListener = object : TopupBillsInputFieldWidget.ActionListener {
                    override fun onFinishInput(input: String) {
                        operatorGroup.operators.find { it.attributes.name == input }?.let {
                            if (operatorId != it.id.toIntSafely()) {
                                // Save operator id for enquiry
                                resetInputData()
                                operatorId = it.id.toIntSafely()
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
                        showOperatorSelectDropdown(operatorSelect, dropdownData)
                    }

                    override fun onTextChangeInput() {
                        // do nothing
                    }
                }

                if (operatorGroup.operators.size == 1) {
                    if (isHidden) operatorSelect.hide()
                    // Get product data based on operator id
                    operatorId = operatorGroup.operators.firstOrNull()?.id.toIntSafely()
                    adapter.showLoading()
                    getProductList(menuId, operatorId.toString())
                }
            }

            // Set operator name
            if (operatorId > 0) {
                operatorGroup.operators.find { it.id == operatorId.toString() }?.attributes?.name?.let { name ->
                    operatorSelect.setInputText(name, false)
                }
            }
        }
    }

    private fun setupInputAndProduct(productData: RechargeGeneralDynamicInput) {
        val dataList: MutableList<Visitable<RechargeGeneralAdapterFactory>> = mutableListOf()

        if (productData.enquiryFields.isNotEmpty()) {
            val enquiryFields = productData.enquiryFields.toMutableList()
            multiCheckoutButton = productData.enquiryFields.find { it.style ==  INPUT_TYPE_MULTIBUTTONS }
            val enquiryInfo = productData.enquiryFields.find { it.style == INPUT_TYPE_ENQUIRY_INFO }
            if (multiCheckoutButton != null) {
                multiCheckoutButton?.let { multiCheckoutButton ->
                    enquiryFields.remove(multiCheckoutButton)
                    setMultiCheckoutButton(multiCheckoutButton)
                    if (!isAlreadyTrackImpressionMultiButton) {
                        isAlreadyTrackImpressionMultiButton = true
                        commonMultiCheckoutAnalytics.onImpressMultiCheckoutButtons(
                            categoryName,
                            multiCheckoutButtonCheckButtonType(multiCheckoutButton.items),
                            userSession.userId
                        )
                    }
                }
            } else if (enquiryInfo != null) {
                if (!isAlreadyTrackImpressionMultiButton) {
                    isAlreadyTrackImpressionMultiButton = true
                    commonMultiCheckoutAnalytics.onImpressMultiCheckoutButtons(
                        categoryName,
                        Int.ZERO,
                        userSession.userId
                    )
                }
                enquiryFields.remove(enquiryInfo)
                // Set enquiry button label
                setEnquiryButtonLabel(enquiryInfo.text)
            }

            inputDataKeys.clear()
            enquiryFields.map {
                // processing product
                if (it.name == RechargeGeneralViewModel.PARAM_PRODUCT) {
                    // Show product field if there is > 1 product
                    val rechargeGeneralProductItemData = mapper.mapInputToProductItemData(it)
                    if (productData.isShowingProduct) {
                        val productSelectData = it
                        productSelectData?.apply {
                            if (productId == 0) {
                                productId = rechargeGeneralProductItemData.dataCollections.firstOrNull()?.products?.firstOrNull()?.id.toIntOrZero()
                            }
                            productId = getIdFromProduct(rechargeGeneralProductItemData.dataCollections, productId.toString()).toIntSafely()
                            rechargeGeneralProductItemData.selectedProductId = productId.toString()
                            dataList.add(rechargeGeneralProductItemData)
                        }
                    } else {
                        val product = it.dataCollections.firstOrNull()?.products?.firstOrNull()
                        product?.let { catalogProduct ->
                            with(catalogProduct.attributes) {
                                val slashedPrice = if (promo != null) price else ""
                                productId = catalogProduct.id.toIntSafely()
                            }
                        }
                    }
                } else {
                    // processing enquiry fields
                    if (productData.needEnquiry) {
                        val enquiryField = mapper.mapDynamicInputToProductData(it)
                        if (inputData.containsKey(it.name)) {
                            enquiryField.value = inputData[it.name]!!
                        }
                        dataList.add(enquiryField)
                        inputDataKeys.add(it.name)
                    } else {
                        // do nothing
                    }
                }
            }
        }

        toggleEnquiryButton()
        if (dataList.isNotEmpty()) adapter.renderList(dataList)
    }

    // Reset product id & input data
    private fun resetInputData() {
        operatorId = 0
        productId = 0
        inputData = hashMapOf()
        // Reset product info ticker
        binding?.tickerRechargeGeneralProductInfo?.hide()
        toggleEnquiryButton()
    }

    private fun showOperatorSelectDropdown(
        field: TopupBillsInputFieldWidget,
        data: List<TopupBillsInputDropdownData>,
        title: String = ""
    ) {
        context?.let { context ->
            val dropdownBottomSheet = BottomSheetUnify()
            dropdownBottomSheet.setTitle(title)
            dropdownBottomSheet.setFullPage(true)
            dropdownBottomSheet.clearAction()
            dropdownBottomSheet.setCloseClickListener {
                dropdownBottomSheet.dismiss()
            }

            val dropdownView = TopupBillsInputDropdownWidget(
                context,
                listener = object : TopupBillsInputDropdownWidget.OnClickListener {
                    override fun onItemClicked(item: TopupBillsInputDropdownData) {
                        dropdownBottomSheet.dismiss()
                        field.setInputText(item.label)
                    }
                },
                selected = field.getInputText()
            )
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

    private fun showProductSelectDropdown(
        field: TopupBillsInputFieldWidget,
        data: List<RechargeGeneralProductSelectData>,
        title: String = ""
    ) {
       rechargeGeneralAnalytics.eventClickProductListDropdown(categoryName, operatorName)
        context?.let { context ->
            val dropdownBottomSheet = BottomSheetUnify()
            dropdownBottomSheet.setTitle(title)
            dropdownBottomSheet.isFullpage = false
            dropdownBottomSheet.clearContentPadding = true
            dropdownBottomSheet.clearAction()
            dropdownBottomSheet.setCloseClickListener {
                dropdownBottomSheet.dismiss()
            }

            val dropdownView = RechargeGeneralProductSelectBottomSheet(
                context,
                listener = object : RechargeGeneralProductSelectBottomSheet.OnClickListener {
                    override fun onItemClicked(item: RechargeGeneralProductSelectData) {
                        dropdownBottomSheet.dismiss()

                        // Show label & store id for enquiry
                        field.setInputText(item.title, false)
                        productId = item.id.toIntSafely()
                        rechargeGeneralAnalytics.eventClickProductCard(categoryName, operatorName, item.title.toLowerCase())
                        toggleEnquiryButton()
                    }
                }
            )
            dropdownView.dropdownData = data
            dropdownBottomSheet.setChild(dropdownView)
            fragmentManager?.run { dropdownBottomSheet.show(this, "Product select dropdown bottom sheet") }
        }
    }

    private fun showFavoriteNumbersPage(
        favoriteNumbers: List<TopupBillsSearchNumberDataModel>,
        enquiryData: RechargeGeneralProductInput
    ) {
        if (favoriteNumbers.isNotEmpty()) {
            val clientNumberType = when (enquiryData.style) {
                TopupBillsInputFieldWidget.INPUT_NUMERIC -> ClientNumberType.TYPE_INPUT_NUMERIC.value
                TopupBillsInputFieldWidget.INPUT_ALPHANUMERIC -> ClientNumberType.TYPE_INPUT_ALPHANUMERIC.value
                TopupBillsInputFieldWidget.INPUT_TELCO -> ClientNumberType.TYPE_INPUT_TEL.value
                else -> ClientNumberType.TYPE_INPUT_NUMERIC.value
            }

            context?.run {
                startActivityForResult(
                    TopupBillsSearchNumberActivity.getCallingIntent(
                        this,
                        clientNumberType,
                        inputData[PARAM_CLIENT_NUMBER] ?: "",
                        favoriteNumbers
                    ),
                    REQUEST_CODE_DIGITAL_SEARCH_NUMBER
                )
            }
        }
    }

    override fun onInfoClick(text: String) {
        showInfoBottomSheets(text)
    }

    private fun showInfoBottomSheets(helpText: String) {
        val infoTextView = ViewRechargeGeneralProductInputInfoBottomSheetBinding.inflate(LayoutInflater.from(context))
        infoTextView.infoText.text = helpText

        val moreInfoBottomSheet = BottomSheetUnify()
        moreInfoBottomSheet.setTitle("Info")
        moreInfoBottomSheet.setChild(infoTextView.root)
        moreInfoBottomSheet.clearAction()
        moreInfoBottomSheet.setCloseClickListener {
            moreInfoBottomSheet.dismiss()
        }
        fragmentManager?.run { moreInfoBottomSheet.show(this, "E-gold more info bottom sheet") }
    }

    private fun setupAutoFillData(data: TopupBillsRecommendation) {
        with(data) {
            this@RechargeGeneralFragment.operatorId = operatorId.toIntSafely()
            this@RechargeGeneralFragment.productId = productId.toIntSafely()
            if (clientNumber.isNotEmpty()) {
                inputData[PARAM_CLIENT_NUMBER] = clientNumber
            }
        }
        renderInitialData()
    }

    private fun renderTickers(tickers: List<TopupBillsTicker>) {
        binding?.run {
            if (tickers.isNotEmpty()) {
                val messages = mutableListOf<TickerData>()
                for (item in tickers) {
                    var description: String = item.content
                    if (item.actionText.isNotEmpty() && item.actionLink.isNotEmpty()) {
                        description += " [${item.actionText}]{${item.actionLink}}"
                    }
                    messages.add(
                        TickerData(
                            item.name,
                            description,
                            when (item.type) {
                                TopupBillsTicker.TYPE_WARNING -> Ticker.TYPE_WARNING
                                TopupBillsTicker.TYPE_INFO -> Ticker.TYPE_INFORMATION
                                TopupBillsTicker.TYPE_SUCCESS -> Ticker.TYPE_ANNOUNCEMENT
                                TopupBillsTicker.TYPE_ERROR -> Ticker.TYPE_ERROR
                                else -> Ticker.TYPE_INFORMATION
                            }
                        )
                    )
                }

                if (messages.size == 1) {
                    with(messages.first()) {
                        rechargeGeneralTicker.tickerTitle = title
                        rechargeGeneralTicker.setHtmlDescription(description)
                        rechargeGeneralTicker.tickerType = type
                    }
                    rechargeGeneralTicker.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            if (linkUrl.startsWith(PREFIX_LINK)) {
                                RouteManager.route(context, linkUrl.toString())
                            } else {
                                RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                            }
                        }

                        override fun onDismiss() {
                        }
                    })
                } else {
                    context?.let { context ->
                        val tickerAdapter = TickerPagerAdapter(context, messages)
                        tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                                if (linkUrl.startsWith(PREFIX_LINK)) {
                                    RouteManager.route(context, linkUrl.toString())
                                } else {
                                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                                }
                            }
                        })
                        rechargeGeneralTicker.addPagerView(tickerAdapter, messages)
                    }
                }

                rechargeGeneralTicker.visibility = View.VISIBLE
            } else {
                rechargeGeneralTicker.visibility = View.GONE
            }
        }
    }

    private fun renderFooter(data: TopupBillsMenuDetail) {
        val promos = data.promos
        val recommendations = data.recommendations

        val listProductTab = mutableListOf<TopupBillsTabItem>()
        var recentTransactionFragment: RechargeGeneralRecentTransactionFragment? = null
        var promoListFragment: RechargeGeneralPromoListFragment? = null
        val showTitle = !(recommendations.isNotEmpty() && promos.isNotEmpty())
        if (recommendations.isNotEmpty()) {
            recentTransactionFragment = RechargeGeneralRecentTransactionFragment.newInstance(recommendations, showTitle)
            listProductTab.add(TopupBillsTabItem(recentTransactionFragment, RECENT_TRANSACTION_LABEL))
        }
        if (promos.isNotEmpty()) {
            promoListFragment = RechargeGeneralPromoListFragment.newInstance(promos, showTitle)
            listProductTab.add(TopupBillsTabItem(promoListFragment, PROMO_LIST_LABEL))
        }

        binding?.run {
            if (listProductTab.isNotEmpty()) {
                val pagerAdapter = TopupBillsProductTabAdapter(this@RechargeGeneralFragment, listProductTab)
                productViewPager.adapter = pagerAdapter
                productViewPager.offscreenPageLimit = listProductTab.size
                tabLayout.customTabMode = TabLayout.MODE_FIXED
                tabLayout.customTabGravity = TabLayout.GRAVITY_FILL

                if (listProductTab.size > 1) {
                    tabLayout.getUnifyTabLayout().removeAllTabs()
                    for (item in listProductTab) {
                        tabLayout.addNewTab(item.title)
                    }
                    tabLayout.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {
                            productViewPager.setCurrentItem(tab.position, true)
                            val promoListIndex = listProductTab.indexOfFirst { it.title == getString(R.string.promo_tab_title) }
                            if (tab.position == promoListIndex) {
                                rechargeGeneralAnalytics.eventClickPromoTab(categoryName, operatorName)
                            }
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {
                        }
                    })
                    productViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            tabLayout.getUnifyTabLayout().getTabAt(position)?.let {
                                it.select()
                            }
                            super.onPageSelected(position)
                            val myFragment = childFragmentManager.findFragmentByTag("f$position")
                            myFragment?.view?.let { updatePagerHeightForChild(it, productViewPager) }
                        }

                        private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
                            view.post {
                                if (view != null) {
                                    val wMeasureSpec =
                                        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                                    view.measure(wMeasureSpec, hMeasureSpec)

                                    if (pager.layoutParams.height != view.measuredHeight) {
                                        pager.layoutParams = (pager.layoutParams)
                                            .also { lp ->
                                                lp.height = view.measuredHeight
                                            }
                                    }
                                }
                            }
                        }
                    })
                    tabLayout.show()
                } else {
                    tabLayout.hide()
                }
                productViewPager.show()
            } else {
                hideFooterView()
            }
        }
    }

    private fun renderClientNumber(favNumber: TopupBillsSearchNumberDataModel) {
        with(favNumber) {
            operatorId.toIntOrNull()?.let { oprId -> this@RechargeGeneralFragment.operatorId = oprId }
            if (clientNumber.isNotEmpty()) inputData[PARAM_CLIENT_NUMBER] = clientNumber
            if (productId.isNotEmpty()) this@RechargeGeneralFragment.productId = productId.toIntOrZero()

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
        binding?.loadingView?.hide()
        hideInputView()
        hideFooterView()
        adapter.showGetListError(e)
    }

    private fun hideInputView() {
        binding?.run {
            operatorClusterSelect.hide()
            operatorSelect.hide()
            rechargeGeneralEnquiryButtonCompose.hide()
            visibilityButtonEnquiry.value = false
            rechargeGeneralSecondaryButtonCompose.hide()
            visibilityButtonSecondary.value = false
        }
    }

    private fun hideFooterView() {
        binding?.run {
            tabLayout.hide()
            productViewPager.hide()
        }
    }

    override fun loadData() {
        binding?.run {
            rechargeGeneralEnquiryButtonCompose.show()
            visibilityButtonEnquiry.value = true
            loadingView.show()
        }
        getMenuDetail(menuId)
        getFavoriteNumbers(categoryId)
        getCatalogPluginData(operatorId, categoryId)
        getOperatorCluster(menuId)
    }

    private fun getOperatorCluster(menuId: Int) {
        binding?.run {
            viewModel.getOperatorCluster(
                RechargeGeneralGqlQuery.catalogOperatorSelectGroup,
                viewModel.createOperatorClusterParams(menuId),
                rechargeGeneralSwipeRefreshLayout.isRefreshing,
                getString(R.string.selection_null_product_error)
            )
        }
    }

    private fun getProductList(menuId: Int, operator: String) {
        binding?.run {
            viewModel.getProductList(
                com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery.rechargeCatalogDynamicProductInput,
                viewModel.createProductListParams(menuId, operator),
                rechargeGeneralSwipeRefreshLayout.isRefreshing,
                getString(R.string.selection_null_product_error)
            )
        }
    }

    override fun onFinishInput(label: String, input: String, position: Int, isManual: Boolean) {
        if (label.isNotEmpty() && input.isNotEmpty() && isManual) {
            rechargeGeneralAnalytics.eventInputManualNumber(categoryName, operatorName, position + 1)
        }

        updateInputData(label, input)
    }

    override fun onTextChangeInput() {
        isEnableButtonEnquiry.value = false
        binding?.rechargeGeneralEnquiryButtonCompose?.isEnabled = false
    }

    override fun onCustomInputClick(
        field: TopupBillsInputFieldWidget,
        enquiryData: RechargeGeneralProductInput?,
        productData: List<RechargeGeneralProductSelectData>?,
        position: Int
    ) {
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
        binding?.run {
            isEnableButtonEnquiry.value = validateEnquiry()
            isEnableButtonSecondary.value = validateEnquiry()
            binding?.rechargeGeneralEnquiryButtonCompose?.isEnabled = validateEnquiry()
            binding?.rechargeGeneralSecondaryButtonCompose?.isEnabled = validateEnquiry()
            if (enquiryLabel.isNotEmpty()) titleButtonEnquiry.value = enquiryLabel
        }
    }

    private fun closeCoachmarkTrack() {
        commonMultiCheckoutAnalytics.onCloseMultiCheckoutCoachmark(
            categoryName, loyaltyStatus
        )
    }

    private fun setMultiCheckoutButton(multiCheckoutButtons: RechargeGeneralDynamicField) {
        context?.let { context ->
            val localCacheHandler = LocalCacheHandler(context, PREFERENCE_MULTICHECKOUT)
            val coachMarkList = arrayListOf<CoachMark2Item>()

            binding?.apply {
                if (multiCheckoutButtons.items.size > Int.ONE) {
                    processMultipleButton(multiCheckoutButtons, coachMarkList)
                } else if (multiCheckoutButtons.items.size == Int.ONE) {
                    processOneButton(multiCheckoutButtons, coachMarkList)
                } else {
                    actionButtonEnquiry.value = {
                        enquire()
                    }
                }
            }

            updateCoachMark(localCacheHandler, coachMarkList)
        }
    }

    private fun processMultipleButton(multiCheckoutButtons: RechargeGeneralDynamicField, coachMarkList: ArrayList<CoachMark2Item>) {
        val (leftButton, rightButton) = multiCheckoutButtonSeparator(
            multiCheckoutButtons.items
        )
        processLeftButton(leftButton, coachMarkList)
        processRightButton(rightButton, coachMarkList)
    }

    private fun processRightButton(rightButton: RechargeGeneralDynamicField.Item,
                                   coachMarkList: ArrayList<CoachMark2Item>) {
        if (rightButton.text.isNotEmpty() && rightButton.color.isNotEmpty() && rightButton.style.isNotEmpty()) {
            binding?.rechargeGeneralEnquiryButtonCompose?.show()
            visibilityButtonEnquiry.value = true
            titleButtonEnquiry.value = rightButton.text
            variantButtonEnquiry.value =
                variantButton(rightButton.color)
            actionButtonEnquiry.value = {
                chooseListenerAction(rightButton.style)
            }

            viewEnquiry?.let { viewEnquiry ->
                if (rightButton.coachmark.isNotEmpty()) {
                    coachMarkList.add(
                        CoachMark2Item(
                            viewEnquiry, "", rightButton.coachmark
                        )
                    )
                }
            }
        }
    }

    private fun processLeftButton(leftButton: RechargeGeneralDynamicField.Item,
                                  coachMarkList: ArrayList<CoachMark2Item>) {
        if (leftButton.text.isNotEmpty() && leftButton.color.isNotEmpty() && leftButton.style.isNotEmpty()) {
            binding?.rechargeGeneralSecondaryButtonCompose?.show()
            visibilityButtonSecondary.value = true
            titleButtonSecondary.value = leftButton.text
            variantButtonSecondary.value =
                variantButton(leftButton.color)
            actionButtonSecondary.value = {
                chooseListenerAction(leftButton.style)
            }

            viewSecondary?.let { viewSecondary ->
                if (leftButton.coachmark.isNotEmpty()) {
                    coachMarkList.add(
                        CoachMark2Item(
                            viewSecondary, "", leftButton.coachmark
                        )
                    )
                }
            }
        } else {
            binding?.rechargeGeneralSecondaryButtonCompose?.show()
            visibilityButtonSecondary.value = false
        }
    }

    private fun processOneButton(multiCheckoutButtons: RechargeGeneralDynamicField,
                                 coachMarkList: ArrayList<CoachMark2Item>) {
        val button = multiCheckoutButtons.items.first()
        titleButtonEnquiry.value = button.text
        variantButtonEnquiry.value = variantButton(button.color)
        actionButtonEnquiry.value = {
            chooseListenerAction(button.style)
        }

        viewEnquiry?.let { viewEnquiry ->
            if (button.coachmark.isNotEmpty()) {
                coachMarkList.add(
                    CoachMark2Item(
                        viewEnquiry, "", button.coachmark
                    )
                )
            }
        }
    }
    private fun updateCoachMark(localCacheHandler: LocalCacheHandler,
                                coachMarkList: ArrayList<CoachMark2Item>) {
        coachmark?.let { coachmark ->

            val isCoachMarkClosed =
                localCacheHandler.getBoolean(SHOW_COACH_MARK_MULTICHECKOUT_KEY, false)
            if (!isCoachMarkClosed && !coachmark.isShowing) {
                coachmark.showCoachMark(coachMarkList)
                coachmark.setOnDismissListener {
                    closeCoachmarkTrack()
                    localCacheHandler.putBoolean(SHOW_COACH_MARK_MULTICHECKOUT_KEY, true)
                    localCacheHandler.applyEditor()
                }
            }
        }
    }

    private fun multiCheckoutButtonSeparator(multiCheckoutButtons: List<RechargeGeneralDynamicField.Item>): Pair<RechargeGeneralDynamicField.Item, RechargeGeneralDynamicField.Item> {
        val leftButton = multiCheckoutButtons.first()
        val rightButton = multiCheckoutButtons[Int.ONE]
        return Pair(leftButton, rightButton)
    }

    private fun multiCheckoutButtonCheckButtonType(multiCheckoutButtons: List<RechargeGeneralDynamicField.Item>): Int {
        val button = multiCheckoutButtons.first()
        val buttonType = if(button.style == ACTION_GENERAL_MYBILLS) Int.ONE else 2
        return buttonType
    }

    private fun multiCheckoutButtonPromotion(multiCheckoutButtons: List<RechargeGeneralDynamicField.Item>): PromotionMultiCheckout {
        val buttonType = multiCheckoutButtonCheckButtonType(multiCheckoutButtons)
        val multiCheckoutButtonText = multiCheckoutButtons.first{
            it.style == ACTION_GENERAL_MYBILLS
        }.text

        return PromotionMultiCheckout(
           multiCheckoutButtonText, buttonType
        )
    }
    private fun chooseListenerAction(type: String) {
        if (type.equals(ACTION_GENERAL_MYBILLS)) {
            addToCartViewModel.setAtcMultiCheckoutParam()
            enquire()
        } else {
            enquire()
        }
    }
    private fun variantButton(color: String): ButtonVariant {
        return if (color.equals(WHITE_COLOR)) {
            ButtonVariant.GHOST
        } else {
            ButtonVariant.FILLED
        }
    }

    private fun setEnquiryButtonLabel(label: String) {
        if (label.isNotEmpty()) {
            enquiryLabel = label
            titleButtonEnquiry.value = enquiryLabel
            actionButtonEnquiry.value = {
                enquire()
            }
        }
    }

    private fun validateEnquiry(): Boolean {
        return operatorId > 0 && productId > 0 &&
            (
                inputDataKeys.isEmpty() ||
                    inputData.keys.toList().sorted() == inputDataKeys.sorted()
                )
    }

    private fun enquire() {
        if (validateEnquiry()) {
            // If it's express checkout & not add sbm, open checkout bottomsheet; if not navigate to old checkout
            if (!isExpressCheckout) {
                processCheckout()
            } else {
                if (!userSession.isLoggedIn) {
                    navigateToLoginPage()
                } else {
                    getEnquiry(operatorId.toString(), productId.toString(), inputData)
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
        categoryName = data.catalog.label
        loyaltyStatus = data.userPerso.loyaltyStatus
        super.processMenuDetail(data)
        with(data.catalog) {
            (activity as? BaseSimpleActivity)?.updateTitle(
                label
            )
            rechargeAnalytics.eventOpenScreen(
                    userSession.userId,
                    categoryName,
                    categoryId.toString()
            )

        }
        renderTickers(data.tickers)
        // Set recommendation data if available
        if (data.recommendations.isNotEmpty() && !hasInputData) {
            setupAutoFillData(data.recommendations[0])
        }

        renderFooter(data)

    }

    override fun onLoadingMenuDetail(showLoading: Boolean) {
        // do nothing
    }

    override fun onLoadingAtc(showLoading: Boolean) {
        isLoadingButtonEnquiry.value = showLoading
        isLoadingButtonSecondary.value = showLoading
    }

    override fun processFavoriteNumbers(data: List<TopupBillsSearchNumberDataModel>) {
        favoriteNumbers = data
        updateFavoriteNumberInputField()
    }

    override fun showErrorMessage(error: Throwable) {
        view?.let { v ->
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            Toaster.build(
                v,
                errorMessage.orEmpty()
                    ?: "",
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    override fun redirectErrorUnVerifiedNumber(error: ErrorAtc) {
        /*no op*/
    }

    private fun updateFavoriteNumberInputField() {
        if (favoriteNumbers.isNotEmpty()) {
            if (adapter.data.isNotEmpty()) {
                val clientNumberInput: RechargeGeneralProductInput? = adapter.data.find { it is RechargeGeneralProductInput && it.name == PARAM_CLIENT_NUMBER } as? RechargeGeneralProductInput
                clientNumberInput?.apply {
                    isFavoriteNumber = true
                    adapter.notifyItemChanged(adapter.data.indexOf(clientNumberInput))
                }
            }
        }
    }

    override fun onEnquiryError(error: Throwable) {
        view?.let { v ->
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            Toaster.build(
                v,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(resourcescommonR.string.general_label_ok)
            ).show()
        }
    }

    override fun onMenuDetailError(error: Throwable) {
    }

    override fun onCatalogPluginDataError(error: Throwable) {
    }

    override fun onFavoriteNumbersError(error: Throwable) {
    }

    override fun onCheckVoucherError(error: Throwable) {
        view?.let { v ->
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            Toaster.build(
                v,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(resourcescommonR.string.general_label_ok)
            ).show()
        }
    }

    override fun onExpressCheckoutError(error: Throwable) {
        view?.let { v ->
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            Toaster.build(
                v,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(resourcescommonR.string.general_label_ok)
            ).show()
        }
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

    override fun initAddToCartViewModel() {
        addToCartViewModel = viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java)
    }

    override fun onPromoTickerNull(data: PromoData) {
        pendingPromoData = data
    }

    override fun onClickCheckout(data: TopupBillsEnquiry) {
        rechargeGeneralAnalytics.eventClickCheckBills(categoryName, operatorName, productName)
        rechargeGeneralAnalytics.eventClickBuy(categoryName, operatorName, false, data)

        processCheckout()
    }

    override fun onClickMultiCheckout(data: TopupBillsEnquiry) {
        rechargeGeneralAnalytics.eventClickCheckBills(categoryName, operatorName, productName)
        rechargeGeneralAnalytics.eventClickBuy(categoryName, operatorName, false, data)

        addToCartViewModel.setAtcMultiCheckoutParam()
        processCheckout()
    }

    override fun onCloseCoachMark() {
        closeCoachmarkTrack()
    }

    private fun processCheckout() {
        // Setup checkout pass data
        if (validateEnquiry()) {
            if (productId > 0) {
                var checkoutPassDataBuilder = getDefaultCheckoutPassDataBuilder()
                    .categoryId(categoryId.toString())
                    .isPromo(if (isPromo) "1" else "0")
                    .operatorId(operatorId.toString())
                    .productId(productId.toString())
                    .utmCampaign(categoryId.toString())

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
        return cluster.operatorGroups?.firstOrNull()?.operators?.firstOrNull()?.id.toIntSafely()
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
                if (operator.id == operatorId.toString()) return group
            }
        }
        return null
    }

    private fun getOperatorDataOfOperatorId(cluster: RechargeGeneralOperatorCluster, operatorId: Int): CatalogOperator? {
        cluster.operatorGroups?.forEach { group ->
            group.operators.forEach { operator ->
                if (operator.id == operatorId.toString()) return operator
            }
        }
        return null
    }

    private fun updateProductData() {
        if (viewModel.productList.value is Success) {
            needProductDataUpdate = false
            val products = (viewModel.productList.value as Success).data.enquiryFields.firstOrNull {
                it.name == RechargeGeneralViewModel.PARAM_PRODUCT
            }?.dataCollections?.firstOrNull()?.products
            products?.let {
                val product = products.firstOrNull { it.id == productId.toString() }
                product?.run {
                    productName = attributes.desc
                    this@RechargeGeneralFragment.price = attributes.pricePlain.toIntSafely()
                    isPromo = attributes.promo != null

                    // Show product info ticker
                    val description = attributes.detailCompact
                    if (description.isNotEmpty()) {
                        binding?.tickerRechargeGeneralProductInfo?.let {
                            it.show()
                            it.setHtmlDescription(description)
                            it.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    context?.let {
                                        RouteManager.route(it, attributes.detailUrl)
                                    }
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
                        }
                    } else {
                        binding?.tickerRechargeGeneralProductInfo?.hide()
                    }
                }
            }
        } else {
            needProductDataUpdate = true
        }
    }

    fun onBackPressed() {
        rechargeGeneralAnalytics.eventClickBackButton(categoryName, operatorName)

    }

    private fun hideLoading() {
        binding?.run {
            rechargeGeneralSwipeRefreshLayout.isEnabled = true
            rechargeGeneralSwipeRefreshLayout.isRefreshing = false
            adapter.hideLoading()
        }
    }

    override fun processSeamlessFavoriteNumbers(
        data: TopupBillsSeamlessFavNumber,
        shouldRefreshInputNumber: Boolean
    ) {
        // do nothing
    }

    override fun onSeamlessFavoriteNumbersError(error: Throwable) {
        // do nothing
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(RechargeGeneralComponent::class.java).inject(this)
    }

    private fun getIdFromProduct(data: List<CatalogProductInput.DataCollection>, id: String): String {
        data.forEach { collection ->
            collection.products.forEach { product ->
                if (product.id == id) return product.id
            }
        }

        return data.firstOrNull()?.products?.firstOrNull()?.id ?: ""
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        try {
            val dppoConsentData = viewModel.dppoConsent.value
            inflater.inflate(R.menu.menu, menu)
            if (dppoConsentData is Success && dppoConsentData.data.description.isNotEmpty()) {
                menu.showConsentIcon()
                menu.setupConsentIcon(dppoConsentData.data.description)
                menu.setupKebabIcon()
            } else {
                menu.hideConsentIcon()
                menu.setupKebabIcon()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun Menu.hideConsentIcon() {
        findItem(R.id.action_dppo_consent).isVisible = false
    }

    private fun Menu.showConsentIcon() {
        findItem(R.id.action_dppo_consent).isVisible = true
    }

    private fun Menu.setupConsentIcon(description: String) {
        if (description.isNotEmpty()) {
            context?.let { ctx ->
                val iconUnify = getIconUnifyDrawable(
                    ctx,
                    IconUnify.INFORMATION,
                    ContextCompat.getColor(ctx, unifyprinciplesR.color.Unify_NN900)
                )
                iconUnify?.toBitmap()?.let {
                    getItem(0).setOnMenuItemClickListener {
                        val bottomSheet = DigitalDppoConsentBottomSheet(description)
                        bottomSheet.show(childFragmentManager)
                        true
                    }
                    getItem(0).icon = BitmapDrawable(
                        ctx.resources,
                        Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true)
                    )
                }
            }
        }
    }

    private fun Menu.setupKebabIcon() {
        context?.let { ctx ->
            val iconUnify = getIconUnifyDrawable(
                ctx,
                IconUnify.MENU_KEBAB_VERTICAL,
                ContextCompat.getColor(ctx, unifyprinciplesR.color.Unify_NN900)
            )
            iconUnify?.toBitmap()?.let {
                getItem(1).setOnMenuItemClickListener {
                    showBottomMenus()
                    true
                }
                getItem(1).icon = BitmapDrawable(
                    ctx.resources,
                    Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true)
                )
            }
        }
    }

    private fun showBottomMenus() {
        val generalMenuBottomSheet = TopupBillsMenuBottomSheets.newInstance()
        generalMenuBottomSheet.listener = this
        generalMenuBottomSheet.setShowListener {
            generalMenuBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        generalMenuBottomSheet.show(childFragmentManager, TAG_GENERAL_MENU_BOTTOM_SHEET)
    }

    override fun onOrderListClicked() {
        RouteManager.route(context, ApplinkConst.DIGITAL_ORDER)
    }

    override fun onPromoClicked() {
        RouteManager.route(context, ApplinkConst.PROMO_LIST)
    }

    override fun onHelpClicked() {
        RouteManager.route(context, ApplinkConst.CONTACT_US_NATIVE)
    }

    companion object {
        private const val PREFIX_LINK = "tokopedia"
        const val EXTRA_PARAM_MENU_ID = "EXTRA_PARAM_MENU_ID"
        const val EXTRA_PARAM_CATEGORY_ID = "EXTRA_PARAM_CATEGORY_ID"
        const val EXTRA_PARAM_OPERATOR_ID = "EXTRA_PARAM_OPERATOR_ID"
        const val EXTRA_PARAM_PRODUCT_ID = "EXTRA_PARAM_PRODUCT_ID"
        const val EXTRA_PARAM_INPUT_DATA = "EXTRA_PARAM_INPUT_DATA"
        const val EXTRA_PARAM_INPUT_DATA_KEYS = "EXTRA_PARAM_INPUT_DATA_KEYS"
        const val EXTRA_PARAM_ENQUIRY_DATA = "EXTRA_PARAM_ENQUIRY_DATA"

        const val OPERATOR_TYPE_VISIBLE = "select_dropdown"
        const val OPERATOR_TYPE_HIDDEN = "hidden"

        const val INPUT_TYPE_FAVORITE_NUMBER = "input_favorite"
        const val INPUT_TYPE_ENQUIRY_INFO = "enquiry"
        const val INPUT_TYPE_MULTIBUTTONS = "multibuttons"

        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_ZONE_ID = "zone_id"

        const val PROMO_LIST_LABEL = "Promo"
        const val RECENT_TRANSACTION_LABEL = "Transaksi Terakhir"

        const val REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 77

        private const val TAG_GENERAL_MENU_BOTTOM_SHEET = "GENERAL_MENU_BOTTOM_SHEET"
        private const val TAG_GENERAL_CONSENT_BOTTOM_SHEET = "GENERAL_CONSENT_BOTTOM_SHEET"
        private const val TOOLBAR_ICON_SIZE = 64

        val ITEM_DECORATOR_SIZE = unifyprinciplesR.dimen.spacing_lvl3

        fun newInstance(
            categoryId: Int,
            menuId: Int,
            operatorId: Int = 0,
            productId: Int = 0,
            rechargeProductFromSlice: String = "",
        ): RechargeGeneralFragment {
            val fragment = RechargeGeneralFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
            bundle.putInt(EXTRA_PARAM_MENU_ID, menuId)
            bundle.putInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
            bundle.putInt(EXTRA_PARAM_PRODUCT_ID, productId)
            bundle.putString(RECHARGE_PRODUCT_EXTRA, rechargeProductFromSlice)
            fragment.arguments = bundle
            return fragment
        }
    }
}
