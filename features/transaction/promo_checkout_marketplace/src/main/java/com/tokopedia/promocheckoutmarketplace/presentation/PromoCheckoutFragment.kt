package com.tokopedia.promocheckoutmarketplace.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.data.EXTRA_IS_USE
import com.tokopedia.promocheckout.common.data.EXTRA_KUPON_CODE
import com.tokopedia.promocheckout.common.data.ONE_CLICK_SHIPMENT
import com.tokopedia.promocheckout.common.data.PAGE_TRACKING
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.promocheckoutmarketplace.data.response.SectionTab
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleFragmentBinding
import com.tokopedia.promocheckoutmarketplace.di.DaggerPromoCheckoutMarketplaceComponent
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoSuggestionAdapter
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.compoundview.ToolbarPromoCheckout
import com.tokopedia.promocheckoutmarketplace.presentation.compoundview.ToolbarPromoCheckoutListener
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutSuggestionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.promocheckoutmarketplace.presentation.viewmodel.*
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.*
import java.net.UnknownHostException
import javax.inject.Inject

class PromoCheckoutFragment : BaseListFragment<Visitable<*>, PromoCheckoutAdapterTypeFactory>(),
        PromoCheckoutActionListener, PromoCheckoutSuggestionListener, ToolbarPromoCheckoutListener {

    private var viewBinding by autoClearedNullable<PromoCheckoutMarketplaceModuleFragmentBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var itemDecorator: PromoCheckoutDecoration

    @Inject
    lateinit var analytics: PromoCheckoutAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var promoCheckoutMarketplaceHanselHelper: PromoCheckoutMarketplaceHanselHelper? = null
    private var promoCheckoutSuggestionBottomSheet: BottomSheetBehavior<FrameLayout>? = null
    private var showBottomsheetJob: Job? = null
    private var clearSelectionActionFlagJob: Job? = null
    private var keyboardHeight = 0
    private var isPromoCheckoutSuggestionBottomSheetShown = false
    private var hasTriedToGetPromoSuggestionData = false
    private var isPromoMvcLockCourierFlow = false

    private lateinit var promoCoachMark: CoachMark2
    private lateinit var coachMarkRecyclerListener: RecyclerView.OnScrollListener
    private var promoWithCoachMarkIndex: Int = -1

    private lateinit var localCacheHandler: LocalCacheHandler

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PromoCheckoutViewModel::class.java)
    }

    // Use single recycler view to prevent memory leak & OOM caused by nested recyclerview
    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: PromoCheckoutAdapter

    private var toolbar: ToolbarPromoCheckout? = null

    companion object {
        const val REQUEST_CODE_PHONE_VERIFICATION = 9999
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0
        const val KEYBOARD_HEIGHT_THRESHOLD = 100
        const val DELAY_SHOW_BOTTOMSHEET_IN_MILIS = 250L
        const val DELAY_DEFAULT_IN_MILIS = 500L

        private const val PREFERENCES_NAME = "promo_coachmark_preferences"

        private const val KEY_PROMO_CHECKOUT_COACHMARK_IS_SHOWED = "KEY_PROMO_CHECKOUT_COACHMARK_IS_SHOWED"
        private const val DESTINATION_BACK = "back"
        private const val DESTINATION_REFRESH = "refresh"

        fun createInstance(pageSource: Int,
                           promoRequest: PromoRequest,
                           validateUsePromoRequest: ValidateUsePromoRequest,
                           bboPromoCodes: ArrayList<String>,
                           promoMvcLockCourierFlow: Boolean = false,
                           chosenAddress: ChosenAddress?): PromoCheckoutFragment {
            return PromoCheckoutFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARGS_PAGE_SOURCE, pageSource)
                    putParcelable(ARGS_PROMO_REQUEST, promoRequest)
                    putParcelable(ARGS_VALIDATE_USE_REQUEST, validateUsePromoRequest)
                    putStringArrayList(ARGS_BBO_PROMO_CODES, bboPromoCodes)
                    putBoolean(ARGS_PROMO_MVC_LOCK_COURIER_FLOW, promoMvcLockCourierFlow)
                    // Add chosen address for trade in indomaret case, will be null for other case
                    putParcelable(ARGS_CHOSEN_ADDRESS, chosenAddress)
                }
            }
        }
    }

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerPromoCheckoutMarketplaceComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        context?.let {
            localCacheHandler = LocalCacheHandler(it, PREFERENCES_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = PromoCheckoutMarketplaceModuleFragmentBinding.inflate(inflater, container, false)
        val view = viewBinding?.root
        recyclerView = getRecyclerView(view)
        recyclerView?.addItemDecoration(itemDecorator)
        (recyclerView?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        addViewTreeGlobalLayoutListener(view)
        return view
    }

    private fun addViewTreeGlobalLayoutListener(view: CoordinatorLayout?) {
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff = view.rootView?.height?.minus(view.height) ?: 0
            val displayMetrics = DisplayMetrics()
            val windowManager = view.context?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val heightDiffInDp = heightDiff.pxToDp(displayMetrics)
            if (heightDiffInDp > KEYBOARD_HEIGHT_THRESHOLD) {
                keyboardHeight = heightDiff
                if (!isPromoCheckoutSuggestionBottomSheetShown) {
                    isPromoCheckoutSuggestionBottomSheetShown = true
                    if (!hasTriedToGetPromoSuggestionData) {
                        getOrShowPromoSuggestionData()
                    }
                }
            } else {
                keyboardHeight = 0
                isPromoCheckoutSuggestionBottomSheetShown = false
                hidePromoCheckoutSuggestionBottomsheet()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()

        // Initialize hansel helper
        initializeHanselHelper()

        // UI Initialization
        initializeToolbar(view)
        initializeFragmentUiModel()
        initializeClickListener()
        initializePromoCheckoutSuggestionBottomsheet()
        initializeSwipeRefreshLayout()
        initializeRecyclerViewScrollListener()
        initializeFlagIsPromoMvcLockCourierFlow()

        // Observe visitable data changes
        observeFragmentUiModel()
        observePromoRecommendationUiModel()
        observePromoTabUiModel()
        observePromoInputUiModel()
        observePromoListUiModel()
        observeErrorStateUiModel()
        observeEmptyStateUiModel()
        observeVisitableChangeUiModel()
        observeVisitableListChangeUiModel()

        // Observe network call result
        observeGetCouponRecommendationResult()
        observeApplyPromoResult()
        observeClearPromoResult()
        observeGetPromoSuggestionResult()
    }

    private fun setBackground() {
        activity?.let {
            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        }
    }

    private fun initializeHanselHelper() {
        promoCheckoutMarketplaceHanselHelper = PromoCheckoutMarketplaceHanselHelper(viewModel, analytics)
    }

    private fun getHanselHelper(): PromoCheckoutMarketplaceHanselHelper? {
        return promoCheckoutMarketplaceHanselHelper
    }

    private fun initializeFlagIsPromoMvcLockCourierFlow() {
        isPromoMvcLockCourierFlow = arguments?.getBoolean(ARGS_PROMO_MVC_LOCK_COURIER_FLOW, false)
                ?: false
    }

    private fun setPromoMvcLockCourierFlow(isPromoMvcLockCourierFlow: Boolean) {
        this.isPromoMvcLockCourierFlow = isPromoMvcLockCourierFlow
    }

    private fun setResultIsPromoMvcLockCourierFlow(intent: Intent) {
        intent.putExtra(ARGS_PROMO_MVC_LOCK_COURIER_FLOW, isPromoMvcLockCourierFlow)
    }

    private fun setResultErrorPromo(intent: Intent) {
        intent.putExtra(ARGS_PROMO_ERROR, ARGS_FINISH_ERROR)
    }

    override fun getRecyclerViewResourceId() = R.id.promo_checkout_marketplace_module_recycler_view

    private fun initializeSwipeRefreshLayout() {
        activity?.let {
            viewBinding?.swipeRefreshLayout?.setColorSchemeColors(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        }
        viewBinding?.swipeRefreshLayout?.setOnRefreshListener {
            reloadData()
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(-1) && viewBinding?.tabsPromoHeader?.tabsPromo?.visibility != View.VISIBLE) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
                renderStickyPromoHeader(recyclerView)
            }
        })
    }

    private fun initializeFragmentUiModel() {
        val defaultErrorMessage = context?.resources?.getString(R.string.label_error_global_promo_checkout) ?: ""
        viewModel.initFragmentUiModel(arguments?.getInt(ARGS_PAGE_SOURCE, 0) ?: 0, defaultErrorMessage)
    }

    private fun initializeClickListener() {
        initializeClickButtonApplyPromo()
        initializeClickButtonApplyNoPromo()
        initializeClickBottomSheet()
    }

    private fun initializeClickBottomSheet() {
        viewBinding?.promoSuggestionBottomSheet?.buttonClose?.let { bottomsheetCloseButton ->
            bottomsheetCloseButton.setOnClickListener {
                analytics.eventDismissLastSeen(viewModel.getPageSource())
                hidePromoCheckoutSuggestionBottomsheet()
            }
        }

        viewBinding?.promoSuggestionBottomSheet?.bottomSheetTitle?.setOnClickListener { }
    }

    private fun initializeClickButtonApplyNoPromo() {
        viewBinding?.buttonApplyNoPromo?.let { buttonApplyNoPromo ->
            buttonApplyNoPromo.setOnClickListener {
                setButtonLoading(buttonApplyNoPromo, true)
                val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST)
                        ?: ValidateUsePromoRequest()
                val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                viewModel.clearPromo(validateUsePromoRequest, bboPromoCodes ?: ArrayList())
                analytics.eventClickBeliTanpaPromo(viewModel.getPageSource())
            }
        }
    }

    private fun initializeClickButtonApplyPromo() {
        viewBinding?.buttonApplyPromo?.let { buttonApplyPromo ->
            buttonApplyPromo.setOnClickListener {
                setButtonLoading(buttonApplyPromo, true)
                val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST)
                        ?: ValidateUsePromoRequest()
                val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                viewModel.applyPromo(validateUsePromoRequest, bboPromoCodes ?: ArrayList())
            }
        }
    }

    private fun initializePromoCheckoutSuggestionBottomsheet() {
        if (promoCheckoutSuggestionBottomSheet == null) {
            viewBinding?.promoSuggestionBottomSheet?.bottomSheetPromoSuggestion?.let {
                promoCheckoutSuggestionBottomSheet = BottomSheetBehavior.from(it)
                promoCheckoutSuggestionBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onDestroy() {
        showBottomsheetJob?.cancel()
        clearSelectionActionFlagJob?.cancel()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PHONE_VERIFICATION && resultCode == Activity.RESULT_OK) {
            reloadData()
        }
    }

    private fun setButtonLoading(button: UnifyButton, isLoading: Boolean) {
        if (isLoading) {
            button.isLoading = true
            button.isClickable = false
        } else {
            button.isLoading = false
            button.isClickable = true
        }
    }

    private fun renderStickyPromoHeader(recyclerView: RecyclerView) {
        if (adapter.data.isNotEmpty()) {
            val promoTabUiModel: PromoTabUiModel = viewModel.promoTabUiModel.value ?: return
            val topItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (topItemPosition == RecyclerView.NO_POSITION) return
            val topVisibleUiModel = adapter.data[topItemPosition]

            val isShow: Boolean = (topVisibleUiModel !is PromoInputUiModel &&
                    topVisibleUiModel !is PromoRecommendationUiModel &&
                    topVisibleUiModel !is PromoEligibilityHeaderUiModel) ||
                    (topVisibleUiModel is PromoEligibilityHeaderUiModel && !topVisibleUiModel.uiState.isEnabled)

            var tab: SectionTab? = null
            if (topVisibleUiModel is PromoEligibilityHeaderUiModel && !topVisibleUiModel.uiState.isEnabled) {
                tab = promoTabUiModel.uiData.tabs.firstOrNull {
                    it.id == topVisibleUiModel.uiData.tabId
                }
            } else if (topVisibleUiModel is PromoListHeaderUiModel) {
                tab = promoTabUiModel.uiData.tabs.firstOrNull {
                    it.id == topVisibleUiModel.uiData.tabId
                }
            } else if (topVisibleUiModel is PromoListItemUiModel) {
                tab = promoTabUiModel.uiData.tabs.firstOrNull {
                    it.id == topVisibleUiModel.uiData.tabId
                }
            }

            if (tab != null) {
                val tabPosition = promoTabUiModel.uiData.tabs.indexOf(tab)
                if (promoTabUiModel.uiState.selectedTabPosition != tabPosition && !promoTabUiModel.uiState.isSelectionAction) {
                    promoTabUiModel.uiState.selectedTabPosition = tabPosition
                    selectTab(promoTabUiModel)
                }
            }

            if (isShow) {
                viewBinding?.tabsPromoHeader?.root?.show()
                viewBinding?.tabsPromoHeader?.tabsPromo?.show()
                viewBinding?.tabsPromoHeader?.tabsPromo?.customTabMode = TabLayout.MODE_SCROLLABLE
                setToolbarShadowVisibility(false)
            } else {
                viewBinding?.tabsPromoHeader?.root?.gone()
                viewBinding?.tabsPromoHeader?.tabsPromo?.gone()
            }
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (show) {
            viewBinding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
        } else {
            viewBinding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
        }
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(viewLifecycleOwner, {
            renderFragmentState(it)
        })
    }

    private fun addOrModify(it: Visitable<*>) {
        if (adapter.data.contains(it)) {
            adapter.modifyData(adapter.data.indexOf(it))
        } else {
            adapter.addVisitable(it)
        }
    }

    private fun observeErrorStateUiModel() {
        viewModel.promoErrorStateUiModel.observe(viewLifecycleOwner, {
            addOrModify(it)
        })
    }

    private fun observeEmptyStateUiModel() {
        viewModel.promoEmptyStateUiModel.observe(viewLifecycleOwner, {
            addOrModify(it)
        })
    }

    private fun observePromoRecommendationUiModel() {
        viewModel.promoRecommendationUiModel.observe(viewLifecycleOwner, {
            if (!it.uiState.isInitialization) {
                addOrModify(it)
            }
        })
    }

    private fun observePromoTabUiModel() {
        viewModel.promoTabUiModel.observe(viewLifecycleOwner, {
            if (it.uiState.isInitialization) {
                if (viewBinding?.tabsPromoHeader?.tabsPromo?.getUnifyTabLayout()?.getTabAt(0) == null) {
                    viewBinding?.tabsPromoHeader?.tabsPromo?.customTabMode = TabLayout.MODE_SCROLLABLE
                    viewBinding?.tabsPromoHeader?.tabsPromo?.customTabGravity = TabLayout.GRAVITY_FILL
                    viewModel.promoTabUiModel.value?.uiData?.tabs?.forEach {
                        viewBinding?.tabsPromoHeader?.tabsPromo?.addNewTab(it.title)
                    }
                }
                viewBinding?.tabsPromoHeader?.tabsPromo?.getUnifyTabLayout()?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        val currentTabUiModel = viewModel.promoTabUiModel.value
                        currentTabUiModel?.let {
                            val tabPosition = tab?.position ?: 0
                            if (it.uiState.selectedTabPosition != tabPosition || it.uiState.isSelectionAction) {
                                it.uiState.selectedTabPosition = tabPosition
                                it.uiState.isSelectionAction = true
                                viewModel.changeSelectedTab(currentTabUiModel)
                                scrollToTabIndex(currentTabUiModel)
                            }

                            analytics.eventClickTabPromoCategory(viewModel.getPageSource(), currentTabUiModel.uiData.tabs[currentTabUiModel.uiState.selectedTabPosition].title)
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        /* No-op */
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        /* No-op */
                    }
                })
            } else {
                addOrModify(it)
                clearSelectionActionFlagJob?.cancel()
                clearSelectionActionFlagJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    delay(DELAY_DEFAULT_IN_MILIS)
                    it.uiState.isSelectionAction = false
                }
            }
        })
    }

    private fun observePromoInputUiModel() {
        viewModel.promoInputUiModel.observe(viewLifecycleOwner, {
            addOrModify(it)
        })
    }

    private fun observePromoListUiModel() {
        viewModel.promoListUiModel.observe(viewLifecycleOwner, {
            adapter.addVisitableList(it)
            renderPromoCoachMark()
        })
    }

    private fun observeVisitableChangeUiModel() {
        viewModel.tmpUiModel.observe(viewLifecycleOwner, {
            when (it) {
                is Update -> {
                    adapter.modifyData(adapter.data.indexOf(it.data))
                }
                is Delete -> {
                    adapter.removeData(it.data)
                }
            }
        })
    }

    private fun observeVisitableListChangeUiModel() {
        viewModel.tmpListUiModel.observe(viewLifecycleOwner, {
            when (it) {
                is Insert -> {
                    it.data.forEach { mapData ->
                        adapter.addVisitableList((adapter.data.indexOf(mapData.key) + 1), mapData.value)
                    }
                }
            }
        })
    }

    private fun observeGetCouponRecommendationResult() {
        viewModel.getPromoListResponseAction.observe(viewLifecycleOwner, {
            when (it.state) {
                GetPromoListResponseAction.ACTION_CLEAR_DATA -> {
                    clearAllData()
                }
                GetPromoListResponseAction.ACTION_SHOW_TOAST_ERROR -> {
                    it.exception?.let { throwable ->
                        showToastMessage(throwable)
                    }
                }
            }
        })
    }

    private fun observeApplyPromoResult() {
        viewModel.applyPromoResponseAction.observe(viewLifecycleOwner, {
            when (it.state) {
                ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CALLER_PAGE -> {
                    val intent = Intent()
                    if (it.data != null) {
                        intent.putExtra(ARGS_VALIDATE_USE_DATA_RESULT, it.data)
                        if (it.data?.promoUiModel?.additionalInfoUiModel?.promoSpIds?.isNotEmpty() == true) {
                            setPromoMvcLockCourierFlow(true)
                        }
                        val appliedMvcCartStrings = ArrayList<String>()
                        it?.data?.promoUiModel?.voucherOrderUiModels?.forEach { promoCheckoutVoucherOrdersItemUiModel ->
                            if (promoCheckoutVoucherOrdersItemUiModel.uniqueId.isNotBlank()) {
                                appliedMvcCartStrings.add(promoCheckoutVoucherOrdersItemUiModel.uniqueId)
                            }
                        }
                        intent.putStringArrayListExtra(ARGS_APPLIED_MVC_CART_STRINGS, appliedMvcCartStrings)
                    }
                    if (it.lastValidateUseRequest != null) {
                        intent.putExtra(ARGS_LAST_VALIDATE_USE_REQUEST, it.lastValidateUseRequest)
                    }
                    setResultIsPromoMvcLockCourierFlow(intent)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
                ApplyPromoResponseAction.ACTION_SHOW_TOAST_AND_RELOAD_PROMO -> {
                    viewBinding?.buttonApplyPromo?.let { button ->
                        setButtonLoading(button, false)
                    }
                    it.exception?.let { throwable ->
                        showToastMessage(throwable)
                    }
                    reloadData()
                }
                ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR -> {
                    viewBinding?.buttonApplyPromo?.let { button ->
                        setButtonLoading(button, false)
                    }
                    it.exception?.let { throwable ->
                        showToastMessage(throwable)
                        analytics.eventClickPakaiPromoFailed(viewModel.getPageSource(), getErrorMessage(throwable))
                    }
                }
            }
        })
    }

    private fun observeClearPromoResult() {
        viewModel.clearPromoResponse.observe(viewLifecycleOwner, {
            when (it.state) {
                ClearPromoResponseAction.ACTION_STATE_SUCCESS -> {
                    val intent = Intent()
                    if (it.data != null) {
                        intent.putExtra(ARGS_CLEAR_PROMO_RESULT, it.data)
                    }
                    if (it.lastValidateUseRequest != null) {
                        intent.putExtra(ARGS_LAST_VALIDATE_USE_REQUEST, it.lastValidateUseRequest)
                    }
                    setResultIsPromoMvcLockCourierFlow(intent)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
                ClearPromoResponseAction.ACTION_STATE_ERROR -> it.exception?.let { throwable ->
                    viewBinding?.buttonApplyNoPromo?.let { buttonApplyNoPromo ->
                        setButtonLoading(buttonApplyNoPromo, false)
                    }
                    showToastMessage(throwable)
                }
            }
        })
    }

    private fun observeGetPromoSuggestionResult() {
        viewModel.getPromoSuggestionResponse.observe(viewLifecycleOwner, {
            when (it.state) {
                GetPromoSuggestionAction.ACTION_SHOW -> {
                    it.data?.let { data ->
                        showPromoCheckoutSuggestionBottomSheet(data)
                    }
                }
                GetPromoSuggestionAction.ACTION_RELEASE_LOCK_FLAG -> {
                    hasTriedToGetPromoSuggestionData = false
                }
            }
        })
    }

    private fun showPromoCheckoutSuggestionBottomSheet(data: PromoSuggestionUiModel) {
        activity?.let {
            snapToPromoInput()
            showBottomsheetJob?.cancel()
            showBottomsheetJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                delay(DELAY_SHOW_BOTTOMSHEET_IN_MILIS)
                initializePromoSuggestionRecyclerView(data.uiData.promoSuggestionItemUiModelList)

                // Determine available space height for bottomsheet if soft keyboard open
                val promoInputHeight = viewModel.promoInputUiModel.value?.uiState?.viewHeight ?: 0
                val promoInputMargin = resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8).dpToPx()
                val availableSpaceHeight = getDeviceHeight(it) - keyboardHeight - promoInputHeight - promoInputMargin

                // Determine total space, in pixel, needed to show all promo last seen item
                val headerPromoSuggestionHeight = resources.getDimensionPixelSize(R.dimen.dp_56)
                val itemPromoSuggestionHeight = resources.getDimensionPixelSize(R.dimen.dp_68)
                val totalSpaceNeededForPromoSuggestionItems = (data.uiData.promoSuggestionItemUiModelList.size * itemPromoSuggestionHeight) + headerPromoSuggestionHeight

                // If available space is not sufficient to show all promo item, then set max available height for the bottomsheet
                val isAvailableSpaceSufficient = availableSpaceHeight - totalSpaceNeededForPromoSuggestionItems >= 0
                if (!isAvailableSpaceSufficient) {
                    promoCheckoutSuggestionBottomSheet?.peekHeight = availableSpaceHeight.toInt()
                    viewBinding?.promoSuggestionBottomSheet?.rvPromoSuggestion?.layoutParams?.height = (availableSpaceHeight - (viewBinding?.promoSuggestionBottomSheet?.buttonClose?.height
                            ?: 0)).toInt()
                } else {
                    promoCheckoutSuggestionBottomSheet?.peekHeight = totalSpaceNeededForPromoSuggestionItems
                    viewBinding?.promoSuggestionBottomSheet?.rvPromoSuggestion?.layoutParams?.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                }

                promoCheckoutSuggestionBottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED

                analytics.eventShowLastSeenPopUp(viewModel.getPageSource(), userSession.userId)
                hasTriedToGetPromoSuggestionData = false
            }
        }
    }

    private fun hidePromoCheckoutSuggestionBottomsheet() {
        promoCheckoutSuggestionBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializePromoSuggestionRecyclerView(dataList: List<PromoSuggestionItemUiModel>) {
        viewBinding?.promoSuggestionBottomSheet?.rvPromoSuggestion?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PromoSuggestionAdapter(this@PromoCheckoutFragment)
            (adapter as PromoSuggestionAdapter).data = ArrayList(dataList)
            (adapter as PromoSuggestionAdapter).notifyDataSetChanged()
        }
    }

    private fun snapToPromoInput() {
        recyclerView?.layoutManager?.let { layoutManager ->
            val linearSmoothScroller = object : LinearSmoothScroller(recyclerView?.context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            viewModel.promoInputUiModel.value?.let { promoInputUiModel ->
                linearSmoothScroller.targetPosition = adapter.data.indexOf(promoInputUiModel)
                layoutManager.startSmoothScroll(linearSmoothScroller)
                adapter.notifyItemChanged(adapter.data.indexOf(viewModel.promoInputUiModel.value))
            }
        }
    }

    override fun onClickItem(model: PromoSuggestionItemUiModel) {
        viewModel.setPromoInputFromLastApply(model.uiData.promoCode)
        hidePromoCheckoutSuggestionBottomsheet()
    }

    private fun renderFragmentState(fragmentUiModel: FragmentUiModel) {
        if (fragmentUiModel.uiState.isLoading) {
            showLoading()
        } else {
            hideLoading()
            viewBinding?.swipeRefreshLayout?.isRefreshing = false
        }

        if (!fragmentUiModel.uiState.hasFailedToLoad) {
            renderLoadPromoSuccess(fragmentUiModel)
        } else {
            renderLoadPromoFailed(fragmentUiModel)
        }
    }

    private fun renderLoadPromoSuccess(fragmentUiModel: FragmentUiModel) {
        viewBinding?.let {
            if (fragmentUiModel.uiState.hasAnyPromoSelected) {
                renderHasAnyPromoSelected(fragmentUiModel)
            } else {
                renderHasNoPromoSelected(fragmentUiModel)
            }
            it.layoutGlobalError.gone()
            it.layoutMainContainer.show()
        }
    }

    private fun renderLoadPromoFailed(fragmentUiModel: FragmentUiModel) {
        viewBinding?.let {
            toolbar?.disableResetButton()
            toolbar?.hideResetButton()
            fragmentUiModel.uiData.exception?.let { throwable ->
                it.layoutGlobalError.setType(getGlobalErrorType(throwable))
                if (throwable is AkamaiErrorException) {
                    showToastMessage(throwable)
                }
            }
            it.layoutGlobalError.setActionClickListener { _ ->
                analytics.eventClickCobaLagi(viewModel.getPageSource())
                it.layoutGlobalError.gone()
                reloadData()
            }
            it.layoutGlobalError.show()
            it.layoutMainContainer.gone()
            it.containerActionBottom.gone()
            analytics.eventViewErrorPopup(viewModel.getPageSource())
        }
    }

    private fun renderHasNoPromoSelected(fragmentUiModel: FragmentUiModel) {
        viewBinding?.let {
            toolbar?.disableResetButton()
            toolbar?.showResetButton()
            if (fragmentUiModel.uiState.hasPreAppliedPromo) {
                it.labelTotalPromoInfo.gone()
                it.labelTotalPromoAmount.gone()
                it.buttonApplyPromo.gone()
                it.buttonApplyNoPromo.show()
                it.containerActionBottom.show()
            } else {
                it.containerActionBottom.gone()
            }
        }
    }

    private fun renderHasAnyPromoSelected(fragmentUiModel: FragmentUiModel) {
        viewBinding?.let {
            toolbar?.enableResetButton()
            toolbar?.showResetButton()
            it.labelTotalPromoInfo.show()
            it.labelTotalPromoAmount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.uiData.totalBenefit, false).removeDecimalSuffix()
            it.labelTotalPromoAmount.show()
            var applyPromoText = ""
            activity?.let { activity ->
                applyPromoText = String.format(activity.resources.getString(R.string.promo_checkout_label_button_apply_promo), fragmentUiModel.uiData.usedPromoCount)
            }
            it.buttonApplyPromo.text = applyPromoText
            it.buttonApplyPromo.show()
            it.buttonApplyNoPromo.gone()
            it.containerActionBottom.show()
        }
    }

    private fun reloadData() {
        viewModel.resetPromoInput()
        toolbar?.disableResetButton()
        toolbar?.hideResetButton()
        viewBinding?.containerActionBottom?.gone()
        adapter.clearAllElements()
        viewBinding?.layoutMainContainer?.show()
        loadData(0)
    }

    private fun getGlobalErrorType(e: Throwable): Int {
        return if (e is UnknownHostException) {
            NO_CONNECTION
        } else {
            SERVER_ERROR
        }
    }

    private fun initializeToolbar(view: View) {
        activity?.let {
            viewBinding?.toolbarPromoCheckout?.removeAllViews()
            toolbar = getToolbarPromoCheckout()
            toolbar?.let {
                viewBinding?.toolbarPromoCheckout?.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(viewBinding?.toolbarPromoCheckout)
            }

            setToolbarShadowVisibility(false)
        }
    }

    private fun getToolbarPromoCheckout(): ToolbarPromoCheckout? {
        activity?.let {
            return ToolbarPromoCheckout(it).apply {
                listener = this@PromoCheckoutFragment
            }
        }

        return null
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PromoCheckoutAdapterTypeFactory> {
        adapter = PromoCheckoutAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getAdapterTypeFactory(): PromoCheckoutAdapterTypeFactory {
        return PromoCheckoutAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        /* No-op */
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun loadData(page: Int) {
        showLoading()
        val promoRequest = arguments?.getParcelable(ARGS_PROMO_REQUEST) ?: PromoRequest()
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(ARGS_CHOSEN_ADDRESS)
        viewModel.getPromoList(promoRequest, "", chosenAddress)
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    private fun showToastMessage(message: String) {
        view?.let {
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showToastMessage(throwable: Throwable) {
        showToastMessage(getErrorMessage(throwable))
    }

    private fun getErrorMessage(throwable: Throwable): String {
        var errorMessage = throwable.message
        if (throwable !is PromoErrorException && throwable !is AkamaiErrorException) errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        if (errorMessage.isNullOrBlank()) {
            errorMessage = getString(R.string.label_error_global_promo_checkout)
        }
        return errorMessage
    }

    private fun showSavePromoDialog() {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_title_promo_dialog_backpressed))
                setDescription(getString(R.string.label_description_promo_dialog_backpressed))
                setPrimaryCTAText(getString(R.string.label_primary_cta_promo_dialog_backpressed))
                setSecondaryCTAText(getString(R.string.label_secondary_cta_promo_dialog_backpressed))
                setPrimaryCTAClickListener {
                    analytics.eventClickSimpanPromoBaru(viewModel.getPageSource())
                    if (viewModel.isHasAnySelectedPromoItem()) {
                        val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST)
                                ?: ValidateUsePromoRequest()
                        val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                        viewModel.applyPromo(validateUsePromoRequest, bboPromoCodes ?: ArrayList())
                    } else {
                        val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST)
                                ?: ValidateUsePromoRequest()
                        val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                        viewModel.clearPromo(validateUsePromoRequest, bboPromoCodes ?: ArrayList())
                    }
                }
                setSecondaryCTAClickListener {
                    analytics.eventClickKeluarHalaman(viewModel.getPageSource())
                    dismiss()
                    val intent = Intent()
                    setPromoMvcLockCourierFlow(false)
                    setResultIsPromoMvcLockCourierFlow(intent)
                    it.setResult(Activity.RESULT_OK, intent)
                    it.finish()
                }
            }.show()

            analytics.eventViewPopupSavePromo(viewModel.getPageSource())
        }
    }

    private fun renderPromoCoachMark() {
        promoWithCoachMarkIndex = adapter.list.indexOfFirst { item ->
            if (item is PromoListItemUiModel) {
                item.uiData.coachMark.isShown
            } else {
                false
            }
        }

        if (promoWithCoachMarkIndex != -1 && localCacheHandler.getBoolean(KEY_PROMO_CHECKOUT_COACHMARK_IS_SHOWED, false) != true) {
            // initiate the scroll listener to dismiss coachmark if scrolled
            if (!::coachMarkRecyclerListener.isInitialized) {
                coachMarkRecyclerListener = object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        if (promoWithCoachMarkIndex != -1 && layoutManager.findFirstVisibleItemPosition() == promoWithCoachMarkIndex &&
                                ::promoCoachMark.isInitialized && promoCoachMark.isShowing) {
                            promoCoachMark.dismissCoachMark()
                            recyclerView.removeOnScrollListener(coachMarkRecyclerListener)
                        } else if (promoWithCoachMarkIndex != -1 && layoutManager.findLastVisibleItemPosition() == promoWithCoachMarkIndex &&
                                ::promoCoachMark.isInitialized && promoCoachMark.isShowing) {
                            promoCoachMark.dismissCoachMark()
                            recyclerView.removeOnScrollListener(coachMarkRecyclerListener)
                        }
                    }
                }
            }

            val scrollPosition = if (adapter.list.size > promoWithCoachMarkIndex) promoWithCoachMarkIndex + 1 else promoWithCoachMarkIndex
            recyclerView?.smoothScrollToPosition(scrollPosition)
            Handler().postDelayed({
                val holder = recyclerView?.findViewHolderForAdapterPosition(promoWithCoachMarkIndex)
                val coachMarkData = adapter.list[promoWithCoachMarkIndex] as PromoListItemUiModel
                holder?.let {
                    val coachMarkItem = arrayListOf(
                            CoachMark2Item(
                                    holder.itemView.findViewById(R.id.container_constraint_promo_checkout),
                                    coachMarkData.uiData.coachMark.title,
                                    coachMarkData.uiData.coachMark.content,
                                    CoachMark2.POSITION_BOTTOM
                            )
                    )

                    context?.let {
                        promoCoachMark = CoachMark2(it)
                        promoCoachMark.showCoachMark(coachMarkItem)
                        recyclerView?.addOnScrollListener(coachMarkRecyclerListener)
                        localCacheHandler.apply {
                            putBoolean(KEY_PROMO_CHECKOUT_COACHMARK_IS_SHOWED, true)
                            applyEditor()
                        }
                    }
                }
            }, 300)
        }
    }

    override fun onBackPressed() {
        if (viewModel.fragmentUiModel.value != null) {
            if (viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false && viewModel.hasDifferentPreAppliedState()) {
                showSavePromoDialog()
            } else {
                if (promoCheckoutSuggestionBottomSheet != null && promoCheckoutSuggestionBottomSheet?.state != BottomSheetBehavior.STATE_HIDDEN) {
                    hidePromoCheckoutSuggestionBottomsheet()
                } else {
                    val intent = Intent()
                    setPromoMvcLockCourierFlow(false)
                    setResultIsPromoMvcLockCourierFlow(intent)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
            }
        } else {
            val intent = Intent()
            setPromoMvcLockCourierFlow(false)
            setResultIsPromoMvcLockCourierFlow(intent)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    override fun onClickResetPromo() {
        viewModel.resetPromo()
    }

    override fun onClickApplyRecommendedPromo() {
        viewModel.applyRecommendedPromo()
    }

    override fun onClickPromoManualInputTextField() {
        if (!hasTriedToGetPromoSuggestionData) {
            getOrShowPromoSuggestionData()
        }
    }

    private fun getOrShowPromoSuggestionData() {
        hasTriedToGetPromoSuggestionData = true
        analytics.eventClickInputField(viewModel.getPageSource(), userSession.userId)
        view?.let {
            if (promoCheckoutSuggestionBottomSheet?.state == BottomSheetBehavior.STATE_HIDDEN) {
                viewModel.getPromoSuggestion()
            } else {
                hasTriedToGetPromoSuggestionData = false
            }
        }
    }

    override fun onClickApplyManualInputPromo(promoCode: String, isFromSuggestion: Boolean) {
        viewModel.updatePromoInputStateBeforeApplyPromo(promoCode, isFromSuggestion)
        val promoRequest = arguments?.getParcelable(ARGS_PROMO_REQUEST) ?: PromoRequest()
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(ARGS_CHOSEN_ADDRESS)
        viewModel.getPromoList(promoRequest, promoCode, chosenAddress)
    }

    override fun onCLickClearManualInputPromo() {
        analytics.eventClickRemovePromoCode(viewModel.getPageSource())
    }

    override fun onClickPromoListItem(element: PromoListItemUiModel, position: Int) {
        viewModel.updatePromoListAfterClickPromoItem(element)

        // dismiss coachmark if user click promo with coachmark
        val adapterItems = adapter.list
        if (promoWithCoachMarkIndex < adapterItems.size && promoWithCoachMarkIndex == position) {
            val data = adapterItems[promoWithCoachMarkIndex]
            if (data is PromoListItemUiModel && data.id == element.id && ::promoCoachMark.isInitialized && promoCoachMark.isShowing) {
                promoCoachMark.dismissCoachMark()
            }
        }
    }

    override fun onClickPromoItemDetail(element: PromoListItemUiModel) {
        analytics.eventClickLihatDetailKupon(viewModel.getPageSource(), element.uiData.promoCode)
        if (!element.uiState.isParentEnabled) {
            analytics.eventClickLihatDetailOnIneligibleCoupon(viewModel.getPageSource(), element.uiData.promoCode, element.uiData.errorMessage)
        }
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            val promoCodeLink = element.uiData.couponAppLink + element.uiData.promoCode
            putExtra(EXTRA_KUPON_CODE, promoCodeLink)
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
        }
        startActivity(intent)
    }

    override fun onClickEmptyStateButton(element: PromoEmptyStateUiModel) {
        if (element.uiData.emptyStateStatus == STATUS_PHONE_NOT_VERIFIED) {
            analytics.eventClickButtonVerifikasiNomorHp(viewModel.getPageSource())
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
            startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION)
        } else {
            reloadData()
        }
    }

    override fun onClickErrorStateButton(destination: String) {
        if (destination.equals(DESTINATION_BACK, true)) {
            val intent = Intent()
            setResultErrorPromo(intent)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else if (destination.equals(DESTINATION_REFRESH, true)) {
            reloadData()
        }
    }

    override fun onTabSelected(element: PromoTabUiModel) {
        selectTab(element)
        analytics.eventClickTabPromoCategory(viewModel.getPageSource(), element.uiData.tabs[element.uiState.selectedTabPosition].title)
    }

    private fun selectTab(element: PromoTabUiModel) {
        viewBinding?.tabsPromoHeader?.tabsPromo?.getUnifyTabLayout()?.getTabAt(element.uiState.selectedTabPosition)?.select()
    }

    override fun onShowPromoItem(element: PromoListItemUiModel, position: Int) {
        if (element.uiState.isParentEnabled) {
            analytics.eventImpressionEligiblePromoSection(viewModel.getPageSource(), position, element)
        } else {
            analytics.eventImpressionIneligiblePromoSection(viewModel.getPageSource(), position, element)
        }

        if (element.uiData.shippingOptions.isNotBlank()) {
            analytics.eventImpressionLockToShippingPromoSection(viewModel.getPageSource(), position, element)
        }

        if (element.uiData.paymentOptions.isNotBlank()) {
            analytics.eventImpressionLockToPaymentPromoSection(viewModel.getPageSource(), position, element)
        }

        if (element.uiState.isHighlighted) {
            analytics.eventImpressionHighlightedPromoSection(viewModel.getPageSource(), position, element)
        }
    }

    override fun onShowPromoRecommendation(element: PromoRecommendationUiModel) {
        var totalPotentialBenefit = 0
        viewModel.promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && element.uiData.promoCodes.contains(it.uiData.promoCode)) {
                totalPotentialBenefit += it.uiData.benefitAmount
            }
        }
        analytics.eventImpressionRecommendationPromoSection(viewModel.getPageSource(), element.uiData.promoCodes.size, totalPotentialBenefit)
    }

    private fun scrollToTabIndex(element: PromoTabUiModel) {
        var tmpIndex = RecyclerView.NO_POSITION
        loop@ for ((index, visitable) in adapter.data.withIndex()) {
            if (visitable is PromoEligibilityHeaderUiModel && !visitable.uiState.isEnabled) {
                if (visitable.uiData.tabId == element.uiData.tabs[element.uiState.selectedTabPosition].id) {
                    tmpIndex = index
                    break@loop
                }
            } else if (visitable is PromoListHeaderUiModel && visitable.uiState.isEnabled) {
                if (visitable.uiData.tabId == element.uiData.tabs[element.uiState.selectedTabPosition].id) {
                    tmpIndex = index
                    break@loop
                }
            }
        }

        if (tmpIndex != RecyclerView.NO_POSITION) {
            val tabHeight = context?.resources?.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_48)
                    ?: 0

            val layoutManager: RecyclerView.LayoutManager? = recyclerView?.layoutManager
            if (layoutManager != null) {
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(tmpIndex, tabHeight)
            }
        }
    }
}