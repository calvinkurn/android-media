package com.tokopedia.promocheckoutmarketplace.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.data.EXTRA_IS_USE
import com.tokopedia.promocheckout.common.data.EXTRA_KUPON_CODE
import com.tokopedia.promocheckout.common.data.ONE_CLICK_SHIPMENT
import com.tokopedia.promocheckout.common.data.PAGE_TRACKING
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.promocheckoutmarketplace.di.DaggerPromoCheckoutMarketplaceComponent
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoLastSeenAdapter
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.compoundview.ToolbarPromoCheckout
import com.tokopedia.promocheckoutmarketplace.presentation.compoundview.ToolbarPromoCheckoutListener
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutLastSeenListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.promocheckoutmarketplace.presentation.viewmodel.*
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddress
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import java.net.UnknownHostException
import javax.inject.Inject

class PromoCheckoutFragment : BaseListFragment<Visitable<*>, PromoCheckoutAdapterTypeFactory>(),
        PromoCheckoutActionListener, PromoCheckoutLastSeenListener, ToolbarPromoCheckoutListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var itemDecorator: PromoCheckoutDecoration

    @Inject
    lateinit var analytics: PromoCheckoutAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var promoCheckoutMarketplaceHanselHelper: PromoCheckoutMarketplaceHanselHelper? = null
    private var promoCheckoutLastSeenBottomsheet: BottomSheetBehavior<FrameLayout>? = null
    private var showBottomsheetJob: Job? = null
    private var keyboardHeight = 0
    private var isPromoCheckoutlastSeenBottomsheetShown = false
    private var hasTriedToGetLastSeenData = false
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

    // Main Section
    private var toolbar: ToolbarPromoCheckout? = null
    private val appBarLayout by lazy { view?.findViewById<AppBarLayout>(R.id.app_bar_layout) }
    private val swipeRefreshLayout by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val layoutMainContainer by lazy { view?.findViewById<FrameLayout>(R.id.layout_main_container) }
    private val containerActionBottom by lazy { view?.findViewById<ConstraintLayout>(R.id.container_action_bottom) }
    private val layoutGlobalError by lazy { view?.findViewById<GlobalError>(R.id.layout_global_error) }

    // Sticky header section
    private val headerPromoSection by lazy { view?.findViewById<LinearLayout>(R.id.header_promo_section) }
    private val sectionImagePromoListHeader by lazy { view?.findViewById<ImageView>(R.id.section_image_promo_list_header) }
    private val sectionLabelPromoListHeaderTitle by lazy { view?.findViewById<Typography>(R.id.section_label_promo_list_header_title) }
    private val sectionLabelPromoListHeaderSubTitle by lazy { view?.findViewById<Typography>(R.id.section_label_promo_list_header_sub_title) }
    private val sectionImageChevron by lazy { view?.findViewById<ImageView>(R.id.section_image_chevron) }

    // Footer section
    private val labelTotalPromoInfo by lazy { view?.findViewById<Typography>(R.id.label_total_promo_info) }
    private val labelTotalPromoAmount by lazy { view?.findViewById<Typography>(R.id.label_total_promo_amount) }
    private val buttonApplyPromo by lazy { view?.findViewById<UnifyButton>(R.id.button_apply_promo) }
    private val buttonApplyNoPromo by lazy { view?.findViewById<UnifyButton>(R.id.button_apply_no_promo) }

    // Bottomsheet promo last seen section
    private val bottomsheetPromoLastSeenContainer by lazy { view?.findViewById<FrameLayout>(R.id.bottom_sheet_promo_last_seen) }
    private val bottomsheetCloseButton by lazy { view?.findViewById<ImageView>(R.id.bottom_sheet_close) }
    private val bottomSheetTitle by lazy { view?.findViewById<Typography>(R.id.bottom_sheet_title) }
    private val rvPromoLastSeen by lazy { view?.findViewById<RecyclerView>(R.id.rv_promo_last_seen) }

    companion object {
        const val REQUEST_CODE_PHONE_VERIFICATION = 9999
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0
        const val KEYBOARD_HEIGHT_THRESHOLD = 100
        const val DELAY_SHOW_BOTTOMSHEET_IN_MILIS = 250L

        private const val PREFERENCES_NAME = "promo_coachmark_preferences"

        private const val KEY_PROMO_CHECKOUT_COACHMARK_IS_SHOWED = "KEY_PROMO_CHECKOUT_COACHMARK_IS_SHOWED"

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
        val view = inflater.inflate(R.layout.promo_checkout_marketplace_module_fragment, container, false)
        recyclerView = getRecyclerView(view)
        recyclerView?.addItemDecoration(itemDecorator)
        (recyclerView?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff = view.rootView?.height?.minus(view.height) ?: 0
            val displayMetrics = DisplayMetrics()
            val windowManager = view.context?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val heightDiffInDp = heightDiff.pxToDp(displayMetrics)
            if (heightDiffInDp > KEYBOARD_HEIGHT_THRESHOLD) {
                keyboardHeight = heightDiff
                if (!isPromoCheckoutlastSeenBottomsheetShown) {
                    isPromoCheckoutlastSeenBottomsheetShown = true
                    if (!hasTriedToGetLastSeenData) {
                        getOrShowLastSeenData()
                    }
                }
            } else {
                keyboardHeight = 0
                isPromoCheckoutlastSeenBottomsheetShown = false
                hidePromoCheckoutLastSeenBottomsheet()
            }
        }

        return view
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
        initializePromoCheckoutLastSeenBottomsheet()
        initializeSwipeRefreshLayout()
        initializeRecyclerViewScrollListener()
        initializeFlagIsPromoMvcLockCourierFlow()

        // Observe visitable data changes
        observeFragmentUiModel()
        observePromoRecommendationUiModel()
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
        observeGetPromoLastSeenResult()
    }

    private fun setBackground() {
        activity?.let {
            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N50))
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
            swipeRefreshLayout?.setColorSchemeColors(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        }
        swipeRefreshLayout?.setOnRefreshListener {
            reloadData()
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(-1)) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
                renderStickyPromoHeader(recyclerView)
            }
        })
    }

    private fun initializeFragmentUiModel() {
        viewModel.initFragmentUiModel(arguments?.getInt(ARGS_PAGE_SOURCE, 0) ?: 0)
    }

    private fun initializeClickListener() {
        initializeClickButtonApplyPromo()
        initializeClickButtonApplyNoPromo()
        initializeClickBottomsheet()
    }

    private fun initializeClickBottomsheet() {
        bottomsheetCloseButton?.let { bottomsheetCloseButton ->
            bottomsheetCloseButton.setOnClickListener {
                analytics.eventDismissLastSeen(viewModel.getPageSource())
                hidePromoCheckoutLastSeenBottomsheet()
            }
        }

        bottomSheetTitle?.setOnClickListener { }
    }

    private fun initializeClickButtonApplyNoPromo() {
        buttonApplyNoPromo?.let { buttonApplyNoPromo ->
            buttonApplyNoPromo.setOnClickListener {
                setButtonLoading(buttonApplyNoPromo, true)
                val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST)
                        ?: ValidateUsePromoRequest()
                val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                viewModel.clearPromo(GraphqlHelper.loadRawString(it.resources, R.raw.clear_promo), validateUsePromoRequest, bboPromoCodes
                        ?: ArrayList())
                analytics.eventClickBeliTanpaPromo(viewModel.getPageSource())
            }
        }
    }

    private fun initializeClickButtonApplyPromo() {
        buttonApplyPromo?.let { buttonApplyPromo ->
            buttonApplyPromo.setOnClickListener {
                setButtonLoading(buttonApplyPromo, true)
                val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST)
                        ?: ValidateUsePromoRequest()
                val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                viewModel.applyPromo(GraphqlHelper.loadRawString(it.resources, com.tokopedia.purchase_platform.common.R.raw.mutation_validate_use_promo_revamp), validateUsePromoRequest, bboPromoCodes
                        ?: ArrayList())
            }
        }
    }

    private fun initializePromoCheckoutLastSeenBottomsheet() {
        if (promoCheckoutLastSeenBottomsheet == null) {
            bottomsheetPromoLastSeenContainer?.let {
                promoCheckoutLastSeenBottomsheet = BottomSheetBehavior.from(it)
                promoCheckoutLastSeenBottomsheet?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onDestroy() {
        showBottomsheetJob?.cancel()
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
            var lastHeaderUiModel: PromoListHeaderUiModel? = null
            val topItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (topItemPosition == RecyclerView.NO_POSITION) return
            val lastData = adapter.data[topItemPosition]
            if (lastData == lastHeaderUiModel) return

            val isShow: Boolean
            if (lastData is PromoListHeaderUiModel && lastData.uiState.isEnabled && !lastData.uiState.isCollapsed) {
                lastHeaderUiModel = lastData
                isShow = true
            } else if (lastHeaderUiModel != null && lastData is PromoListItemUiModel &&
                    lastData.uiData.parentIdentifierId == lastHeaderUiModel.uiData.identifierId &&
                    lastData.uiState.isParentEnabled) {
                isShow = true
            } else if (lastData is PromoListItemUiModel && lastData.uiState.isParentEnabled) {
                if (lastHeaderUiModel != null && lastData.uiData.parentIdentifierId == lastHeaderUiModel.uiData.identifierId) {
                    isShow = true
                } else {
                    var foundHeader = false
                    adapter.data.forEach {
                        if (it is PromoListHeaderUiModel && it.uiData.identifierId == lastData.uiData.parentIdentifierId) {
                            lastHeaderUiModel = it
                            foundHeader = true
                            return@forEach
                        }
                    }
                    isShow = foundHeader
                }
            } else {
                isShow = false
            }

            // View logic here should be same as view logic on #PromoListHeaderEnabledViewHolder
            if (lastHeaderUiModel != null) {
                sectionImagePromoListHeader?.let { sectionImagePromoListHeader ->
                    if (lastHeaderUiModel?.uiData?.iconUrl?.isNotBlank() == true) {
                        ImageHandler.loadImageRounded2(context, sectionImagePromoListHeader, lastHeaderUiModel?.uiData?.iconUrl)
                        sectionImagePromoListHeader.show()
                    } else {
                        sectionImagePromoListHeader.gone()
                    }
                    setImageFilterNormal(sectionImagePromoListHeader)
                }

                sectionLabelPromoListHeaderTitle?.text = lastHeaderUiModel?.uiData?.title
                activity?.let {
                    if (lastHeaderUiModel?.uiState?.hasSelectedPromoItem == true) {
                        sectionLabelPromoListHeaderSubTitle?.text = it.getString(R.string.label_subtitle_promo_selected)
                        sectionLabelPromoListHeaderSubTitle?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_T500))
                    } else {
                        sectionLabelPromoListHeaderSubTitle?.text = it.getString(R.string.label_subtitle_only_one_promo)
                        sectionLabelPromoListHeaderSubTitle?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
                    }
                }

                if (lastHeaderUiModel?.uiState?.isCollapsed == false) {
                    sectionImageChevron?.rotation = 180f
                } else {
                    sectionImageChevron?.rotation = 0f
                }

                sectionLabelPromoListHeaderSubTitle?.show()
                sectionImageChevron?.show()
                headerPromoSection?.setOnClickListener {
                    if (lastHeaderUiModel != null) {
                        onClickPromoListHeader(lastHeaderUiModel!!)
                    }
                }
            }

            if (isShow) {
                headerPromoSection?.show()
                setToolbarShadowVisibility(false)
            } else {
                headerPromoSection?.gone()
            }
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (show) {
                appBarLayout?.elevation = HAS_ELEVATION.toFloat()
            } else {
                appBarLayout?.elevation = NO_ELEVATION.toFloat()
            }
        }
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(viewLifecycleOwner, Observer {
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
        viewModel.promoErrorStateUiModel.observe(viewLifecycleOwner, Observer {
            addOrModify(it)
        })
    }

    private fun observeEmptyStateUiModel() {
        viewModel.promoEmptyStateUiModel.observe(viewLifecycleOwner, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoRecommendationUiModel() {
        viewModel.promoRecommendationUiModel.observe(viewLifecycleOwner, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoInputUiModel() {
        viewModel.promoInputUiModel.observe(viewLifecycleOwner, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoListUiModel() {
        viewModel.promoListUiModel.observe(viewLifecycleOwner, Observer {
            adapter.addVisitableList(it)
            renderPromoCoachMark()
        })
    }

    private fun observeVisitableChangeUiModel() {
        viewModel.tmpUiModel.observe(viewLifecycleOwner, Observer {
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
        viewModel.tmpListUiModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Insert -> {
                    it.data.forEach {
                        adapter.addVisitableList((adapter.data.indexOf(it.key) + 1), it.value)
                    }
                }
            }
        })
    }

    private fun observeGetCouponRecommendationResult() {
        viewModel.getPromoListResponseAction.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                GetPromoListResponseAction.ACTION_CLEAR_DATA -> {
                    clearAllData()
                }
                GetPromoListResponseAction.ACTION_SHOW_TOAST_ERROR -> {
                    it.exception?.let {
                        showToastMessage(it)
                    }
                }
            }
        })
    }

    private fun observeApplyPromoResult() {
        viewModel.applyPromoResponseAction.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CALLER_PAGE -> {
                    val intent = Intent()
                    if (it.data != null) {
                        intent.putExtra(ARGS_VALIDATE_USE_DATA_RESULT, it.data)
                        if (it.data?.promoUiModel?.additionalInfoUiModel?.promoSpIds?.isNotEmpty() == true) {
                            setPromoMvcLockCourierFlow(true)
                        }
                        val appliedMvcCartStrings = ArrayList<String>()
                        it?.data?.promoUiModel?.voucherOrderUiModels?.forEach {
                            if (it?.uniqueId?.isNotBlank() == true) {
                                appliedMvcCartStrings.add(it.uniqueId)
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
                    buttonApplyPromo?.let {
                        setButtonLoading(it, false)
                    }
                    it.exception?.let {
                        showToastMessage(it)
                    }
                    reloadData()
                }
                ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR -> {
                    buttonApplyPromo?.let {
                        setButtonLoading(it, false)
                    }
                    it.exception?.let {
                        showToastMessage(it)
                        analytics.eventClickPakaiPromoFailed(viewModel.getPageSource(), getErrorMessage(it))
                    }
                }
            }
        })
    }

    private fun observeClearPromoResult() {
        viewModel.clearPromoResponse.observe(viewLifecycleOwner, Observer {
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
                ClearPromoResponseAction.ACTION_STATE_ERROR -> it.exception?.let {
                    showToastMessage(it)
                }
            }
        })
    }

    private fun observeGetPromoLastSeenResult() {
        viewModel.getPromoLastSeenResponse.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                GetPromoLastSeenAction.ACTION_SHOW -> {
                    it.data?.let {
                        showPromoCheckoutLastSeenBottomsheet(it)
                    }
                }
                GetPromoLastSeenAction.ACTION_RELEASE_LOCK_FLAG -> {
                    hasTriedToGetLastSeenData = false
                }
            }
        })
    }

    private fun showPromoCheckoutLastSeenBottomsheet(data: PromoLastSeenUiModel) {
        activity?.let {
            snapToPromoInput()
            showBottomsheetJob?.cancel()
            showBottomsheetJob = GlobalScope.launch(Dispatchers.Main) {
                delay(DELAY_SHOW_BOTTOMSHEET_IN_MILIS)
                initializePromoLastSeenRecyclerView(data.uiData.promoLastSeenItemUiModelList)

                // Determine available space height for bottomsheet if soft keyboard open
                val promoInputHeight = viewModel.promoInputUiModel.value?.uiState?.viewHeight ?: 0
                val promoInputMargin = resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8).dpToPx()
                val availableSpaceHeight = getDeviceHeight(it) - keyboardHeight - promoInputHeight - promoInputMargin

                // Determine total space, in pixel, needed to show all promo last seen item
                val headerPromoLastSeenHeight = resources.getDimensionPixelSize(R.dimen.dp_56)
                val itemPromoLastSeenHeight = resources.getDimensionPixelSize(R.dimen.dp_52)
                val totalSpaceNeededForPromoLastSeenItems = (data.uiData.promoLastSeenItemUiModelList.size * itemPromoLastSeenHeight) + headerPromoLastSeenHeight

                // If available space is not sufficient to show all promo item, then set max available height for the bottomsheet
                val isAvailableSpaceSufficient = availableSpaceHeight - totalSpaceNeededForPromoLastSeenItems >= 0
                if (!isAvailableSpaceSufficient) {
                    promoCheckoutLastSeenBottomsheet?.peekHeight = availableSpaceHeight.toInt()
                    rvPromoLastSeen?.layoutParams?.height = (availableSpaceHeight - (bottomsheetCloseButton?.height
                            ?: 0)).toInt()
                } else {
                    promoCheckoutLastSeenBottomsheet?.peekHeight = totalSpaceNeededForPromoLastSeenItems
                    rvPromoLastSeen?.layoutParams?.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                }

                promoCheckoutLastSeenBottomsheet?.state = BottomSheetBehavior.STATE_COLLAPSED

                analytics.eventShowLastSeenPopUp(viewModel.getPageSource(), userSession.userId)
                hasTriedToGetLastSeenData = false
            }
        }
    }

    private fun hidePromoCheckoutLastSeenBottomsheet() {
        promoCheckoutLastSeenBottomsheet?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun initializePromoLastSeenRecyclerView(dataList: List<PromoLastSeenItemUiModel>) {
        rvPromoLastSeen?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PromoLastSeenAdapter(this@PromoCheckoutFragment)
            (adapter as PromoLastSeenAdapter).data = ArrayList(dataList)
            (adapter as PromoLastSeenAdapter).notifyDataSetChanged()
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

    override fun onClickItem(model: PromoLastSeenItemUiModel) {
        viewModel.setPromoInputFromLastApply(model.uiData.promoCode)
        hidePromoCheckoutLastSeenBottomsheet()
    }

    private fun renderFragmentState(fragmentUiModel: FragmentUiModel) {
        if (fragmentUiModel.uiState.isLoading) {
            showLoading()
        } else {
            hideLoading()
            swipeRefreshLayout?.isRefreshing = false
        }

        if (!fragmentUiModel.uiState.hasFailedToLoad) {
            if (fragmentUiModel.uiState.hasAnyPromoSelected) {
                toolbar?.enableResetButton()
                toolbar?.showResetButton()
                activity?.let {
                    labelTotalPromoInfo?.show()
                    labelTotalPromoAmount?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.uiData.totalBenefit, false).removeDecimalSuffix()
                    labelTotalPromoAmount?.show()
                    buttonApplyPromo?.text = String.format(it.resources.getString(R.string.promo_checkout_label_button_apply_promo), fragmentUiModel.uiData.usedPromoCount)
                    buttonApplyPromo?.show()
                    buttonApplyNoPromo?.gone()
                    containerActionBottom?.show()
                }
            } else {
                toolbar?.disableResetButton()
                toolbar?.showResetButton()
                if (fragmentUiModel.uiState.hasPreAppliedPromo) {
                    labelTotalPromoInfo?.gone()
                    labelTotalPromoAmount?.gone()
                    buttonApplyPromo?.gone()
                    buttonApplyNoPromo?.show()
                    containerActionBottom?.show()
                } else {
                    containerActionBottom?.gone()
                }
            }
            layoutGlobalError?.gone()
            layoutMainContainer?.show()
        } else {
            toolbar?.disableResetButton()
            toolbar?.hideResetButton()
            fragmentUiModel.uiData.exception?.let {
                layoutGlobalError?.setType(getGlobalErrorType(it))
                if (it is AkamaiErrorException) {
                    showToastMessage(it)
                }
            }
            layoutGlobalError?.setActionClickListener { view ->
                analytics.eventClickCobaLagi(viewModel.getPageSource())
                layoutGlobalError?.gone()
                reloadData()
            }
            layoutGlobalError?.show()
            layoutMainContainer?.gone()
            containerActionBottom?.gone()
            analytics.eventViewErrorPopup(viewModel.getPageSource())
        }
    }

    private fun reloadData() {
        viewModel.resetPromoInput()
        toolbar?.disableResetButton()
        toolbar?.hideResetButton()
        containerActionBottom?.gone()
        adapter.clearAllElements()
        layoutMainContainer?.show()
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
            val appbar = view.findViewById<Toolbar>(R.id.toolbar)
            appbar.removeAllViews()
            toolbar = getToolbarPromoCheckout()
            toolbar?.let {
                appbar.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(appbar)
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

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun loadData(page: Int) {
        activity?.let {
            showLoading()
            val promoRequest = arguments?.getParcelable(ARGS_PROMO_REQUEST) ?: PromoRequest()
            val mutation = GraphqlHelper.loadRawString(it.resources, R.raw.get_coupon_list_recommendation)
            val chosenAddress: ChosenAddress? = arguments?.getParcelable(ARGS_CHOSEN_ADDRESS)
            viewModel.getPromoList(mutation, promoRequest, "", chosenAddress)
        }
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
                        viewModel.applyPromo(GraphqlHelper.loadRawString(it.resources, com.tokopedia.purchase_platform.common.R.raw.mutation_validate_use_promo_revamp), validateUsePromoRequest, bboPromoCodes
                                ?: ArrayList())
                    } else {
                        val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST)
                                ?: ValidateUsePromoRequest()
                        val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                        viewModel.clearPromo(GraphqlHelper.loadRawString(it.resources, R.raw.clear_promo), validateUsePromoRequest, bboPromoCodes
                                ?: ArrayList())
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
                if (promoCheckoutLastSeenBottomsheet != null && promoCheckoutLastSeenBottomsheet?.state != BottomSheetBehavior.STATE_HIDDEN) {
                    hidePromoCheckoutLastSeenBottomsheet()
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
        if (!hasTriedToGetLastSeenData) {
            getOrShowLastSeenData()
        }
    }

    private fun getOrShowLastSeenData() {
        hasTriedToGetLastSeenData = true
        analytics.eventClickInputField(viewModel.getPageSource(), userSession.userId)
        view?.let {
            if (promoCheckoutLastSeenBottomsheet?.state == BottomSheetBehavior.STATE_HIDDEN) {
                val query = GraphqlHelper.loadRawString(it.resources, R.raw.promo_suggestion_query)
                viewModel.getPromoLastSeen(query)
            } else {
                hasTriedToGetLastSeenData = false
            }
        }
    }

    override fun onClickApplyManualInputPromo(promoCode: String, isFromLastSeen: Boolean) {
        activity?.let {
            viewModel.updatePromoInputStateBeforeApplyPromo(promoCode, isFromLastSeen)
            val promoRequest = arguments?.getParcelable(ARGS_PROMO_REQUEST) ?: PromoRequest()
            val mutation = GraphqlHelper.loadRawString(it.resources, R.raw.get_coupon_list_recommendation)
            val chosenAddress: ChosenAddress? = arguments?.getParcelable(ARGS_CHOSEN_ADDRESS)
            viewModel.getPromoList(mutation, promoRequest, promoCode, chosenAddress)
        }
    }

    override fun onCLickClearManualInputPromo() {
        analytics.eventClickRemovePromoCode(viewModel.getPageSource())
    }

    override fun onClickPromoListHeader(element: PromoListHeaderUiModel) {
        viewModel.updatePromoListAfterClickPromoHeader(element)
    }

    override fun onClickPromoListItem(element: PromoListItemUiModel, position: Int) {
        viewModel.updatePromoListAfterClickPromoItem(element)
        recyclerView?.let { renderStickyPromoHeader(it) }

        // dismiss coachmark if user click promo with coachmark
        if (promoWithCoachMarkIndex != -1 && adapter.list[promoWithCoachMarkIndex] is PromoListItemUiModel &&
                promoWithCoachMarkIndex == position &&
                (adapter.list[promoWithCoachMarkIndex] as PromoListItemUiModel).id == element.id &&
                ::promoCoachMark.isInitialized && promoCoachMark.isShowing) {
            promoCoachMark.dismissCoachMark()
        }
    }

    override fun onClickPromoItemDetail(element: PromoListItemUiModel) {
        analytics.eventClickLihatDetailKupon(viewModel.getPageSource(), element.uiData.promoCode)
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            val promoCodeLink = element.uiData.couponAppLink + element.uiData.promoCode
            putExtra(EXTRA_KUPON_CODE, promoCodeLink)
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
        }
        startActivity(intent)
    }

    override fun onClickPromoEligibilityHeader(element: PromoEligibilityHeaderUiModel) {
        viewModel.updateIneligiblePromoList(element)
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

    override fun onClickErrorStateButton() {
        val intent = Intent()
        setResultErrorPromo(intent)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }
}