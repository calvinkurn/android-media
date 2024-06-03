package com.tokopedia.feedplus.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.content.common.util.CompositeTouchDelegate
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.doOnLayout
import com.tokopedia.content.common.util.reduceDragSensitivity
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta
import com.tokopedia.creation.common.analytics.ContentCreationAnalytics
import com.tokopedia.creation.common.consts.ContentCreationConsts
import com.tokopedia.creation.common.presentation.bottomsheet.ContentCreationBottomSheet
import com.tokopedia.creation.common.presentation.model.ContentCreationEntryPointSource
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.analytics.FeedAnalytics
import com.tokopedia.feedplus.analytics.FeedNavigationAnalytics
import com.tokopedia.feedplus.analytics.FeedTooltipAnalytics
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedInjector
import com.tokopedia.feedplus.presentation.activityresultcontract.OpenCreateShortsContract
import com.tokopedia.feedplus.presentation.activityresultcontract.RouteContract
import com.tokopedia.feedplus.presentation.adapter.FeedPagerAdapter
import com.tokopedia.feedplus.presentation.customview.UploadInfoView
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedTabModel
import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.onboarding.ImmersiveFeedOnboarding
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModelStoreOwner
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModelStoreProvider
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.lifecycle.whenLifecycle
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.hitRect
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.creation.common.R as creationcommonR

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedBaseFragment :
    TkpdBaseV4Fragment(),
    ContentCreationBottomSheet.Listener,
    FragmentListener,
    AppLogInterface,
    IAdsLog {

    private var _binding: FragmentFeedBaseBinding? = null
    private val binding get() = _binding!!

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModelAssistedFactory: FeedMainViewModel.Factory

    @Inject
    lateinit var contentCreationAnalytics: ContentCreationAnalytics

    @Inject
    lateinit var tooltipAnalytics: FeedTooltipAnalytics

    private val feedMainViewModel: FeedMainViewModel by viewModels {
        FeedMainViewModel.provideFactory(viewModelAssistedFactory, activeTabSource)
    }

    private val activeTabSource: ActiveTabSource
        get() {
            return ActiveTabSource(
                tabName = arguments?.getString(ApplinkConstInternalContent.UF_EXTRA_FEED_TAB_NAME),
                index = 0
            )
        }

    @Inject
    lateinit var playShortsUploadAnalytic: PlayShortsUploadAnalytic

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var feedNavigationAnalytics: FeedNavigationAnalytics

    @Inject
    lateinit var creationUploader: CreationUploader

    private var mCoachMarkJob: Job? = null

    private val toasterBottomMargin by lazy {
        requireContext().resources.getDimensionPixelOffset(R.dimen.feed_toaster_bottom_margin)
    }

    private val tabExtraTopOffset24 by lazy {
        requireContext().resources.getDimensionPixelOffset(R.dimen.feed_space_24)
    }

    private val tabExtraTopOffset16 by lazy {
        requireContext().resources.getDimensionPixelOffset(R.dimen.feed_space_16)
    }

    private val adapter by lazy {
        FeedPagerAdapter(
            childFragmentManager,
            lifecycle,
            appLinkExtras = arguments ?: Bundle.EMPTY,
            entryPoint = getEntryPoint().orEmpty()
        )
    }

    private val isFromPushNotif: Boolean
        get() = activity?.intent?.getStringExtra(EXTRAS_UTM_MEDIUM)
            ?.contains(PARAM_PUSH_NOTIFICATION, true)
            ?: false

    private var mOnboarding: ImmersiveFeedOnboarding? = null

    private var isJustLoggedIn: Boolean
        get() = arguments?.getBoolean(
            ApplinkConstInternalContent.UF_EXTRA_FEED_IS_JUST_LOGGED_IN,
            false
        ) ?: false
        set(value) {
            val arguments = getOrCreateArguments()
            arguments.putBoolean(
                ApplinkConstInternalContent.UF_EXTRA_FEED_IS_JUST_LOGGED_IN,
                value
            )
        }

    private val viewModelStoreProvider by activityViewModels<FeedPostViewModelStoreProvider>()

    private val openCreateShorts =
        registerForActivityResult(OpenCreateShortsContract()) { isCreatingNewShorts ->
            if (!isCreatingNewShorts) return@registerForActivityResult
            feedMainViewModel.setActiveTab(TAB_TYPE_FOLLOWING)
        }

    private val openAppLink = registerForActivityResult(RouteContract()) {}

    private val swipeFollowingLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // this doesn't work, bcs the viewmodel doen't survive
            if (userSession.isLoggedIn) {
                feedMainViewModel.setActiveTab(TAB_TYPE_FOLLOWING)
            } else {
                feedMainViewModel.setActiveTab(TAB_TYPE_FOR_YOU)
            }
        }
    private val openBrowseLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // this also doesn't work, if the previous `browseAppLink` is empty :(
            if (userSession.isLoggedIn) {
                val metaModel = feedMainViewModel.metaData.value
                RouteManager.route(requireContext(), metaModel.browseApplink)
            }
        }

    private val containerFeedTouchDelegate by viewLifecycleBound(
        creator = {
            CompositeTouchDelegate(binding.containerFeedTopNav.layoutFeedTopTab.containerFeedTopTab)
        },
        onLifecycle = whenLifecycle {
            onDestroy { it.removeAll() }
        }
    )

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is ContentCreationBottomSheet -> {
                    fragment.widgetSource = ContentCreationEntryPointSource.Feed
                    fragment.listener = this
                    fragment.shouldShowPerformanceAction = false
                    fragment.analytics = contentCreationAnalytics
                }

                is FeedFragment -> {
                    fragment.setDataSource(object : FeedFragment.DataSource {
                        override fun getViewModelStoreOwner(type: String): ViewModelStoreOwner {
                            return FeedPostViewModelStoreOwner(viewModelStoreProvider, type)
                        }
                    })
                }
            }
        }
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> onResumeInternal()
                    Lifecycle.Event.ON_PAUSE -> onPauseInternal()
                    else -> {}
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedMainViewModel.fetchFeedTabs()

        setupView()
        setupInsets()

        observeFeedTabData()

        observeEvent()
        observeTooltip()
        observeUpload()
    }

    override fun onStart() {
        super.onStart()
        binding.containerFeedTopNav.vMenuCenter.requestApplyInsetsWhenAttached()
        binding.loaderFeedTopNav.root.requestApplyInsetsWhenAttached()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onScrollToTop() {
        if (!isAdded) return
        feedMainViewModel.scrollCurrentTabToTop()
    }

    override fun isLightThemeStatusBar(): Boolean {
        return true
    }

    override fun isForceDarkModeNavigationBar(): Boolean {
        return true
    }

    private fun inject() {
        FeedInjector.get(requireActivity()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun getPageName(): String {
        return PageName.FEED
    }

    override fun getAdsPageName(): String {
        return PageName.FEED
    }

    override fun isEnterFromWhitelisted(): Boolean {
        return true
    }

    override fun onCreationNextClicked(data: ContentCreationItemModel) {
        when (data.type) {
            ContentCreationTypeEnum.LIVE -> {
                feedNavigationAnalytics.eventClickCreateLive()

                openAppLink.launch(ApplinkConst.PLAY_BROADCASTER)
            }

            ContentCreationTypeEnum.POST -> {
                feedNavigationAnalytics.eventClickCreatePost()

                val intent = ContentCreationConsts.getPostIntent(
                    context = context,
                    asABuyer = data.authorType.asBuyer,
                    title = getString(creationcommonR.string.content_creation_post_as_label),
                    sourcePage = ""
                )
                startActivity(intent)
                TrackerProvider.attachTracker(FeedTrackerImagePickerInsta(userSession.shopId))
            }

            ContentCreationTypeEnum.SHORT -> {
                feedNavigationAnalytics.eventClickCreateVideo()

                openCreateShorts.launch()
            }

            ContentCreationTypeEnum.STORY -> {
                openAppLink.launch(data.applink)
            }

            else -> {}
        }
    }

    private fun showSwipeOnboarding() {
        binding.viewVerticalSwipeOnboarding.showAnimated()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupView() {
        binding.containerFeedTopNav.layoutFeedTopTab.containerFeedTopTab.touchDelegate = containerFeedTouchDelegate

        binding.vpFeedTabItemsContainer.adapter = adapter
        binding.vpFeedTabItemsContainer.reduceDragSensitivity(3)
        binding.vpFeedTabItemsContainer.registerOnPageChangeCallback(object :
                OnPageChangeCallback() {

                var shouldSendSwipeTracker = false

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    handleTabTransition(position)
                    if (!userSession.isLoggedIn &&
                        activeTabSource.tabName == null // not coming from appLink
                    ) {
                        if (position == TAB_SECOND_INDEX) {
                            swipeFollowingLoginResult.launch(
                                RouteManager.getIntent(context, ApplinkConst.LOGIN)
                            )
                        }
                    }

                    if (shouldSendSwipeTracker) {
                        if (THRESHOLD_OFFSET_HALF > positionOffset) {
                            feedNavigationAnalytics.eventSwipeFollowingTab()
                        } else {
                            feedNavigationAnalytics.eventSwipeForYouTab()
                        }
                        shouldSendSwipeTracker = false
                    }
                }

                override fun onPageSelected(position: Int) {
                    feedMainViewModel.setActiveTab(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    shouldSendSwipeTracker = state == ViewPager2.SCROLL_STATE_DRAGGING
                }
            })

        binding.viewVerticalSwipeOnboarding.setText(
            getString(R.string.feed_check_next_content)
        )

        binding.containerFeedTopNav.btnFeedBrowse.doOnLayout {
            val centerX = it.width / 2

            binding.containerFeedTopNav.searchTooltip.setAnchorXLocation(centerX)
        }
    }

    private fun setupInsets() {
        binding.containerFeedTopNav.vMenuCenter.doOnApplyWindowInsets { _, insets, _, margin ->
            val topInsetsMargin =
                (insets.systemWindowInsetTop + tabExtraTopOffset24).coerceAtLeast(margin.top)

            binding.containerFeedTopNav.vMenuCenter.apply {
                setMargin(
                    marginLeft,
                    topInsetsMargin,
                    marginRight,
                    marginBottom
                )
            }
        }

        binding.loaderFeedTopNav.root.doOnApplyWindowInsets { v, insets, _, margin ->

            val topInsetsMargin =
                (insets.systemWindowInsetTop + tabExtraTopOffset16).coerceAtLeast(margin.top)

            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            marginLayoutParams.updateMargins(top = topInsetsMargin)

            v.parent.requestLayout()
        }
    }

    private fun checkResume(): Boolean {
        return userVisibleHint && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    private fun onResumeInternal() {
        if (!checkResume()) return

        feedMainViewModel.resumePage()

        val meta = feedMainViewModel.metaData.value
        showOnboarding(meta)

        feedMainViewModel.updateUserInfo()
        feedMainViewModel.fetchFeedMetaData()

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun onPauseInternal() {
        feedMainViewModel.pausePage()

        mOnboarding?.dismiss()
        mOnboarding = null

        Toaster.toasterCustomBottomHeight = 0

        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun observeFeedTabData() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedMainViewModel.feedTabs
                .filterNotNull()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .withCache()
                .collectLatest {
                    val prevValue = it.prevValue
                    val currValue = it.value

                    when (currValue) {
                        NetworkResult.Loading -> {
                            hideErrorView()
                            showLoading()
                        }

                        is NetworkResult.Success -> {
                            hideErrorView()
                            hideLoading()

                            if (
                                currValue.data.data.indexOf(feedMainViewModel.selectedTab) == binding.vpFeedTabItemsContainer.currentItem ||
                                currValue.data.data == (prevValue as? NetworkResult.Success)?.data?.data
                            ) {
                                return@collectLatest
                            }

                            initTabsView(currValue.data)
                            handleActiveTab(currValue.data)
                        }

                        is NetworkResult.Fail -> {
                            showErrorView(currValue.error)
                        }

                        NetworkResult.Unknown -> {
                            // ignore
                        }
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            feedMainViewModel.metaData
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .collectLatest { initMetaView(it) }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                feedMainViewModel.uiEvent.collect { event ->
                    if (event == null) return@collect

                    when (event) {
                        FeedMainEvent.HasJustLoggedIn -> {
                            showJustLoggedInToaster()
                        }

                        FeedMainEvent.ShowSwipeOnboarding -> {
                            showSwipeOnboarding()
                        }

                        else -> {}
                    }

                    feedMainViewModel.consumeEvent(event)
                }
            }
        }
    }

    private fun observeTooltip() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                feedMainViewModel.tooltipEvent.collect { event ->
                    if (event == null) return@collect

                    when (event) {
                        is FeedTooltipEvent.ShowTooltip -> {
                            if (binding.containerFeedTopNav.btnFeedBrowse.isVisible && !mOnboarding?.hasCoachMark.orFalse()) {
                                binding.containerFeedTopNav.searchTooltip.setTooltipMessage(event.category)
                                binding.containerFeedTopNav.searchTooltip.show()
                                feedMainViewModel.setHasShownTooltip()

                                tooltipAnalytics.impressSearchTooltip(feedMainViewModel.currentTooltipCategory)
                            }
                        }
                        is FeedTooltipEvent.DismissTooltip -> {
                            binding.containerFeedTopNav.searchTooltip.hide()
                        }
                    }

                    feedMainViewModel.consumeEvent(event)
                }
            }
        }
    }

    private fun observeUpload() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                creationUploader
                    .observe()
                    .collect { uploadResult ->
                        when (uploadResult) {
                            is CreationUploadResult.Empty -> {
                                binding.containerFeedTopNav.uploadView.hide()
                            }

                            is CreationUploadResult.Upload -> {
                                binding.containerFeedTopNav.uploadView.show()
                                binding.containerFeedTopNav.uploadView.setUploadProgress(uploadResult.progress)
                                binding.containerFeedTopNav.uploadView.setThumbnail(uploadResult.data.notificationCover)
                            }

                            is CreationUploadResult.OtherProcess -> {
                                binding.containerFeedTopNav.uploadView.show()
                                binding.containerFeedTopNav.uploadView.setOtherProgress(uploadResult.progress)
                                binding.containerFeedTopNav.uploadView.setThumbnail(uploadResult.data.notificationCover)
                            }

                            is CreationUploadResult.Success -> {
                                binding.containerFeedTopNav.uploadView.hide()

                                when (val uploadData = uploadResult.data) {
                                    is CreationUploadData.Post -> {
                                        showNormalToaster(
                                            getString(R.string.feed_upload_content_success),
                                            duration = Toaster.LENGTH_LONG,
                                            actionText = getString(R.string.feed_upload_shorts_see_video),
                                            actionListener = {
                                                val intent = RouteManager.getIntent(
                                                    context,
                                                    ApplinkConst.FEED_RELEVANT_POST,
                                                    uploadData.activityId
                                                )

                                                router.route(requireActivity(), intent)
                                            }
                                        )
                                    }

                                    is CreationUploadData.Shorts -> {
                                        showNormalToaster(
                                            getString(R.string.feed_upload_content_success),
                                            duration = Toaster.LENGTH_LONG,
                                            actionText = getString(R.string.feed_upload_shorts_see_video),
                                            actionListener = {
                                                playShortsUploadAnalytic.clickRedirectToChannelRoom(
                                                    uploadResult.data.authorId,
                                                    uploadResult.data.authorType,
                                                    uploadResult.data.creationId
                                                )
                                                router.route(
                                                    requireContext(),
                                                    ApplinkConst.PLAY_DETAIL,
                                                    uploadResult.data.creationId
                                                )
                                            }
                                        )
                                    }

                                    is CreationUploadData.Stories -> {
                                        showNormalToaster(
                                            getString(R.string.feed_upload_story_success),
                                            duration = Toaster.LENGTH_LONG,
                                            actionText = getString(R.string.feed_upload_shorts_see_video),
                                            actionListener = {
                                                router.route(
                                                    requireContext(),
                                                    uploadData.applink
                                                )
                                            }
                                        )
                                    }

                                    else -> {}
                                }
                            }

                            is CreationUploadResult.Failed -> {
                                binding.containerFeedTopNav.uploadView.show()
                                binding.containerFeedTopNav.uploadView.setFailed()
                                binding.containerFeedTopNav.uploadView.setThumbnail(uploadResult.data.notificationCover)
                                binding.containerFeedTopNav.uploadView.setListener(object : UploadInfoView.Listener {
                                    override fun onRetryClicked(view: UploadInfoView) {
                                        launch {
                                            creationUploader.retry(uploadResult.data.notificationIdAfterUpload)
                                        }
                                    }

                                    override fun onCloseWhenFailedClicked(view: UploadInfoView) {
                                        launch {
                                            creationUploader.removeFailedContentFromQueue(uploadResult.data)
                                        }
                                    }
                                })
                            }

                            else -> {}
                        }
                    }
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isAdded) return
        if (!isVisibleToUser) {
            onPauseInternal()
        } else {
            onResumeInternal()
        }
    }

    private fun initMetaView(meta: MetaModel) {
        showOnboarding(meta)

        binding.containerFeedTopNav.feedUserProfileImage.show()
        if (userSession.isLoggedIn) {
            if (meta.showMyProfile && meta.profilePhotoUrl.isNotEmpty()) {
                binding.containerFeedTopNav.feedUserProfileImage.setImageUrl(meta.profilePhotoUrl)
            } else {
                binding.containerFeedTopNav.feedUserProfileImage.hide()
            }
        }

        binding.containerFeedTopNav.btnFeedCreatePost.setOnClickListener {
            feedNavigationAnalytics.eventClickCreationButton()
            onCreatePostClicked()
        }

        binding.containerFeedTopNav.btnFeedLive.setOnClickListener {
            feedNavigationAnalytics.eventClickLiveButton()
            openAppLink.launch(meta.liveApplink)
        }

        binding.containerFeedTopNav.btnFeedBrowse.setOnClickListener {
            feedNavigationAnalytics.sendClickBrowseIconEvent()
            goToFeedBrowse(meta.browseApplink)
        }

        binding.containerFeedTopNav.searchTooltip.setOnClickTooltip {
            binding.containerFeedTopNav.searchTooltip.hide()
            tooltipAnalytics.clickSearchTooltip(feedMainViewModel.currentTooltipCategory)

            goToFeedBrowse(meta.browseApplink)
        }

        binding.containerFeedTopNav.feedUserProfileImage.setOnClickListener {
            feedNavigationAnalytics.eventClickProfileButton()
            if (feedMainViewModel.isLoggedIn) {
                openAppLink.launch(meta.profileApplink)
            } else {
                openAppLink.launch(ApplinkConst.LOGIN)
            }
        }

        binding.containerFeedTopNav.btnFeedCreatePost.showWithCondition(meta.isCreationActive && userSession.isLoggedIn)
        binding.containerFeedTopNav.btnFeedLive.showWithCondition(meta.showLive)
        binding.containerFeedTopNav.labelFeedLive.showWithCondition(meta.showLive)
        binding.containerFeedTopNav.btnFeedBrowse.showWithCondition(meta.showBrowse)
    }

    private fun initTabsView(tab: FeedTabModel) {
        val data = tab.data
        adapter.setTabsList(data)

        var firstTabData: FeedDataModel? = null
        var secondTabData: FeedDataModel? = null

        if (data.isNotEmpty()) {
            firstTabData = data[TAB_FIRST_INDEX]
            if (data.size > TAB_SECOND_INDEX && data[TAB_SECOND_INDEX].isActive) {
                secondTabData = data[TAB_SECOND_INDEX]
            }
        }

        if (firstTabData != null) {
            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedFirstTab.text = firstTabData.title
            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedFirstTab.setOnClickListener {
                feedNavigationAnalytics.eventClickForYouTab()
                binding.vpFeedTabItemsContainer.setCurrentItem(TAB_FIRST_INDEX, true)
            }

            if (firstTabData.hasNewContent && userSession.isLoggedIn) {
                binding.containerFeedTopNav.layoutFeedTopTab.vFirstTabRedDot.show()
            } else {
                binding.containerFeedTopNav.layoutFeedTopTab.vFirstTabRedDot.hide()
            }

            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedFirstTab.show()

            val rect = binding.containerFeedTopNav.layoutFeedTopTab.tyFeedFirstTab.hitRect
            rect.top = binding.containerFeedTopNav.root.top
            rect.bottom += binding.containerFeedTopNav.root.bottom
            rect.left -= binding.containerFeedTopNav.layoutFeedTopTab.containerFeedTopTab.paddingStart
            rect.right += binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab.marginStart / 2
            containerFeedTouchDelegate.addDelegate(
                binding.containerFeedTopNav.layoutFeedTopTab.tyFeedFirstTab,
                TouchDelegate(rect, binding.containerFeedTopNav.layoutFeedTopTab.tyFeedFirstTab)
            )
        } else {
            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedFirstTab.hide()
        }

        if (secondTabData != null) {
            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab.text = secondTabData.title
            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab.setOnClickListener {
                feedNavigationAnalytics.eventClickFollowingTab()
                binding.vpFeedTabItemsContainer.setCurrentItem(TAB_SECOND_INDEX, true)
            }

            if (secondTabData.hasNewContent && userSession.isLoggedIn) {
                binding.containerFeedTopNav.layoutFeedTopTab.vSecondTabRedDot.show()
            } else {
                binding.containerFeedTopNav.layoutFeedTopTab.vSecondTabRedDot.hide()
            }

            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab.show()

            val rect = binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab.hitRect
            rect.top = binding.containerFeedTopNav.root.top
            rect.bottom += binding.containerFeedTopNav.root.bottom
            rect.right += binding.containerFeedTopNav.layoutFeedTopTab.containerFeedTopTab.paddingEnd
            rect.left -= binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab.marginStart / 2
            containerFeedTouchDelegate.addDelegate(
                binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab,
                TouchDelegate(rect, binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab)
            )
        } else {
            binding.containerFeedTopNav.layoutFeedTopTab.tyFeedSecondTab.hide()
        }

        if (isJustLoggedIn && userSession.isLoggedIn) {
            showJustLoggedInToaster()
        }
        isJustLoggedIn = false
    }

    private fun setupActiveTab(tab: FeedTabModel) {
        val source = tab.activeTabSource
        when {
            source.tabName != null -> {
                feedMainViewModel.setActiveTab(source.tabName)
            }

            source.index > -1 && source.index < tab.data.size -> {
                selectActiveTab(source.index)
            }

            else -> selectActiveTab(0)
        }
    }

    private fun selectActiveTab(position: Int) {
        feedMainViewModel.setActiveTab(position)
    }

    private fun showOnboarding(meta: MetaModel) {
        mCoachMarkJob?.cancel()
        mCoachMarkJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(ONBOARDING_SHOW_DELAY)
            if (mOnboarding?.isShowing() == true) return@launch

            mOnboarding = ImmersiveFeedOnboarding.Builder(requireContext())
                .setCreateContentView(
                    if (meta.isCreationActive &&
                        !feedMainViewModel.hasShownCreateContent() &&
                        feedMainViewModel.isShortEntryPointShowed
                    ) {
                        binding.containerFeedTopNav.btnFeedCreatePost
                    } else {
                        null
                    }
                )
                .setProfileEntryPointView(
                    if (meta.showMyProfile && !feedMainViewModel.hasShownProfileEntryPoint()) {
                        binding.containerFeedTopNav.feedUserProfileImage
                    } else {
                        null
                    }
                )
                .setBrowseIconView(
                    if (meta.showBrowse && userSession.isLoggedIn && !feedMainViewModel.hasShownBrowseEntryPoint()) {
                        binding.containerFeedTopNav.btnFeedBrowse
                    } else {
                        null
                    }
                )
                .setListener(object : ImmersiveFeedOnboarding.Listener {
                    override fun onStarted() {}

                    override fun onCompleteCreateContentOnboarding() {
                        feedMainViewModel.setHasShownCreateContent()
                    }

                    override fun onCompleteProfileEntryPointOnboarding() {
                        feedMainViewModel.setHasShownProfileEntryPoint()
                    }

                    override fun onCompleteBrowseEntryPointOnboarding() {
                        feedMainViewModel.setHasShownBrowseEntryPoint()
                    }

                    override fun onFinished(isForcedDismiss: Boolean) {
                        if (!isForcedDismiss) feedMainViewModel.setReadyToShowOnboarding()
                    }
                }).build()

            mOnboarding?.show()
        }
    }

    private fun onCreatePostClicked() {
        ContentCreationBottomSheet
            .getOrCreateFragment(childFragmentManager, requireActivity().classLoader)
            .show(childFragmentManager)
    }

    private fun goToFeedBrowse(appLink: String) {
        if (!userSession.isLoggedIn) {
            openBrowseLoginResult.launch(
                RouteManager.getIntent(requireContext(), ApplinkConst.LOGIN)
            )
        } else {
            openAppLink.launch(appLink)
        }
    }

    private fun showJustLoggedInToaster() {
        showNormalToaster(
            getString(
                R.string.feed_report_login_success_toaster_text,
                feedMainViewModel.displayName
            )
        )
    }

    private fun showNormalToaster(
        text: String,
        duration: Int = Toaster.LENGTH_SHORT,
        actionText: String = "",
        actionListener: View.OnClickListener = View.OnClickListener {}
    ) {
        Toaster.toasterCustomBottomHeight = toasterBottomMargin

        Toaster.build(
            binding.root,
            text,
            duration,
            Toaster.TYPE_NORMAL,
            actionText,
            actionListener
        ).show()
    }

    private fun getOrCreateArguments(): Bundle {
        return this.arguments ?: Bundle().apply {
            this@FeedBaseFragment.arguments = this
        }
    }

    private fun getEntryPoint() = if (isFromPushNotif) {
        FeedAnalytics.ENTRY_POINT_PUSH_NOTIF
    } else if (activity?.intent?.getStringExtra(ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT) != null) {
        activity?.intent?.getStringExtra(ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT)
    } else {
        FeedAnalytics.ENTRY_POINT_SHARE_LINK
    }

    /**
     * to select tab position,
     * please use:
     * - selectActiveTab(position); or
     * - viewModel.setActiveTab(position);
     * - viewModel.setActiveTab(type);
     */
    private fun handleActiveTab(tab: FeedTabModel) {
        val selectedTab = feedMainViewModel.selectedTab

        if (selectedTab == null) {
            setupActiveTab(tab)
        } else {
            // keep active tab updated whenever sending tracker
            feedNavigationAnalytics.setActiveTab(selectedTab)
            binding.vpFeedTabItemsContainer.setCurrentItem(tab.data.indexOf(selectedTab), true)
        }
    }

    private fun handleTabTransition(position: Int) {
        if (position == TAB_FIRST_INDEX) {
            binding.containerFeedTopNav.layoutFeedTopTab.containerFeedTopTab.transitionToStart()
        } else {
            binding.containerFeedTopNav.layoutFeedTopTab.containerFeedTopTab.transitionToEnd()
        }
    }

    private fun showLoading() {
        binding.containerFeedTopNav.root.invisible()
        binding.loaderFeedTopNav.root.show()
    }

    private fun hideLoading() {
        binding.containerFeedTopNav.root.visible()
        binding.loaderFeedTopNav.root.hide()
    }

    private fun showErrorView(error: Throwable) {
        binding.feedError.visible()
        binding.feedError.setIcon(IconUnify.RELOAD)
        binding.feedError.setTitle(getString(R.string.feed_failed_to_load_content))
        binding.feedError.setDescription("")
        binding.feedError.setButton(getString(R.string.feed_label_error_fetch_button)) {
            feedMainViewModel.fetchFeedTabs()
            feedMainViewModel.fetchFeedMetaData()
        }
    }

    private fun hideErrorView() {
        binding.feedError.hide()
    }

    companion object {
        const val TAB_FIRST_INDEX = 0
        const val TAB_SECOND_INDEX = 1

        const val TAB_TYPE_FOR_YOU = "foryou"
        const val TAB_TYPE_FOLLOWING = "following"
        const val TAB_TYPE_CDP = "cdp"

        private const val EXTRAS_UTM_MEDIUM = "utm_medium"
        private const val PARAM_PUSH_NOTIFICATION = "push"

        private const val THRESHOLD_OFFSET_HALF = 0.5f

        private const val ONBOARDING_SHOW_DELAY = 500L
    }
}
