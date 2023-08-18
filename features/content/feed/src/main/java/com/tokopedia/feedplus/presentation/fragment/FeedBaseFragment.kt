package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.reduceDragSensitivity
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.analytics.FeedAnalytics
import com.tokopedia.feedplus.analytics.FeedNavigationAnalytics
import com.tokopedia.feedplus.databinding.FragmentFeedBaseBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.activityresultcontract.OpenCreateShortsContract
import com.tokopedia.feedplus.presentation.activityresultcontract.RouteContract
import com.tokopedia.feedplus.presentation.adapter.FeedPagerAdapter
import com.tokopedia.feedplus.presentation.adapter.bottomsheet.FeedContentCreationTypeBottomSheet
import com.tokopedia.feedplus.presentation.customview.UploadInfoView
import com.tokopedia.feedplus.presentation.model.*
import com.tokopedia.feedplus.presentation.onboarding.ImmersiveFeedOnboarding
import com.tokopedia.feedplus.presentation.receiver.FeedMultipleSourceUploadReceiver
import com.tokopedia.feedplus.presentation.receiver.UploadStatus
import com.tokopedia.feedplus.presentation.receiver.UploadType
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalytic
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration
import com.tokopedia.content.common.R as contentCommonR

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedBaseFragment :
    BaseDaggerFragment(),
    FeedContentCreationTypeBottomSheet.Listener,
    FragmentListener {

    private var _binding: FragmentFeedBaseBinding? = null
    private val binding get() = _binding!!

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject lateinit var viewModelAssistedFactory: FeedMainViewModel.Factory
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
    lateinit var uploadReceiverFactory: FeedMultipleSourceUploadReceiver.Factory

    @Inject
    lateinit var playShortsUploadAnalytic: PlayShortsUploadAnalytic

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var feedNavigationAnalytics: FeedNavigationAnalytics

    private var mCoachMarkJob: Job? = null

    private val toasterBottomMargin by lazy {
        resources.getDimensionPixelOffset(R.dimen.feed_toaster_bottom_margin)
    }

    private val tabExtraTopOffset by lazy {
        resources.getDimensionPixelOffset(R.dimen.feed_space_24)
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

    private val openCreateShorts =
        registerForActivityResult(OpenCreateShortsContract()) { isCreatingNewShorts ->
            if (!isCreatingNewShorts) return@registerForActivityResult
            feedMainViewModel.setActiveTab(TAB_TYPE_FOLLOWING)
        }

    private val openAppLink = registerForActivityResult(RouteContract()) {}

    private val onNonLoginGoToFollowingTab =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (userSession.isLoggedIn) {
                showNormalToaster(
                    text = getString(
                        R.string.feed_report_login_success_toaster_text,
                        userSession.name
                    ),
                    duration = Toaster.LENGTH_LONG
                )

                feedMainViewModel.setActiveTab(TAB_TYPE_FOLLOWING)
            } else {
                feedMainViewModel.setActiveTab(TAB_TYPE_FOR_YOU)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is FeedContentCreationTypeBottomSheet -> {
                    fragment.setListener(this)
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
        observeCreateContentBottomSheetData()

        observeEvent()

        observeUpload()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onScrollToTop() {
        feedMainViewModel.scrollCurrentTabToTop()
    }

    override fun isLightThemeStatusBar(): Boolean {
        return true
    }

    override fun isForceDarkModeNavigationBar(): Boolean {
        return true
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun onCreationItemClick(creationTypeItem: ContentCreationTypeItem) {
        when (creationTypeItem.type) {
            CreateContentType.CREATE_LIVE -> {
                feedNavigationAnalytics.eventClickCreateLive()

                openAppLink.launch(ApplinkConst.PLAY_BROADCASTER)
            }
            CreateContentType.CREATE_POST -> {
                feedNavigationAnalytics.eventClickCreatePost()

                val intent = RouteManager.getIntent(context, ApplinkConst.IMAGE_PICKER_V2).apply {
                    putExtra(
                        BundleData.IS_CREATE_POST_AS_BUYER,
                        creationTypeItem.creatorType.asBuyer
                    )
                    putExtra(
                        BundleData.APPLINK_AFTER_CAMERA_CAPTURE,
                        ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2
                    )
                    putExtra(
                        BundleData.MAX_MULTI_SELECT_ALLOWED,
                        BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED
                    )
                    putExtra(
                        BundleData.TITLE,
                        getString(contentCommonR.string.feed_post_sebagai)
                    )
                    putExtra(
                        BundleData.APPLINK_FOR_GALLERY_PROCEED,
                        ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2
                    )
                }
                startActivity(intent)
                TrackerProvider.attachTracker(FeedTrackerImagePickerInsta(userSession.shopId))
            }

            CreateContentType.CREATE_SHORT_VIDEO -> {
                feedNavigationAnalytics.eventClickCreateVideo()

                openCreateShorts.launch()
            }
            else -> {}
        }
    }

    @OptIn(ExperimentalTime::class)
    fun showSwipeOnboarding() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(COACHMARK_START_DELAY_IN_SEC.toDuration(DurationUnit.SECONDS))
            binding.viewVerticalSwipeOnboarding.showAnimated()
        }
    }

    private fun setupView() {
        binding.vpFeedTabItemsContainer.adapter = adapter

        if (isJustLoggedIn) {
            showJustLoggedInToaster()
            feedMainViewModel.setActiveTab(TAB_TYPE_FOLLOWING)
        }
        isJustLoggedIn = false

        binding.vpFeedTabItemsContainer.reduceDragSensitivity(3)
        binding.vpFeedTabItemsContainer.registerOnPageChangeCallback(object :
                OnPageChangeCallback() {

                var shouldSendSwipeTracker = false

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (!userSession.isLoggedIn &&
                        activeTabSource.tabName == null // not coming from appLink
                    ) {
                        val activeTab = feedMainViewModel.activeTab ?: return
                        if (activeTab.isFollowingTab) {
                            onNonLoginGoToFollowingTab.launch(
                                RouteManager.getIntent(
                                    context,
                                    ApplinkConst.LOGIN
                                )
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
                }

                override fun onPageScrollStateChanged(state: Int) {
                    shouldSendSwipeTracker = state == ViewPager2.SCROLL_STATE_DRAGGING
                }
            })

        binding.viewVerticalSwipeOnboarding.setText(
            getString(R.string.feed_check_next_content)
        )
    }

    private fun setupInsets() {
        binding.vMenuCenter.doOnApplyWindowInsets { _, insets, _, margin ->

            val topInsetsMargin = insets.systemWindowInsetTop + tabExtraTopOffset

            if (topInsetsMargin > margin.top) {
                getAllMotionScene().forEach {
                    it.setMargin(binding.vMenuCenter.id, ConstraintSet.TOP, topInsetsMargin)
                }
            } else {
                getAllMotionScene().forEach {
                    it.setMargin(binding.vMenuCenter.id, ConstraintSet.TOP, margin.top)
                }
            }
        }
    }

    private fun checkResume(): Boolean {
        return userVisibleHint && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    private fun onResumeInternal() {
        if (!checkResume()) return

        feedMainViewModel.resumePage()

        val meta = feedMainViewModel.metaData.value
        if (meta is Success) showOnboarding(meta.data)

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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                feedMainViewModel.feedTabs.collectLatest {
                    when (it) {
                        is Success -> initTabsView(it.data)
                        is Fail -> Toast.makeText(
                            requireContext(),
                            it.throwable.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                feedMainViewModel.metaData.collectLatest {
                    when (it) {
                        is Success -> initMetaView(it.data)
                        is Fail -> Toast.makeText(
                            requireContext(),
                            it.throwable.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> {}
                    }
                }
            }
        }
    }

    private fun observeCreateContentBottomSheetData() {
        feedMainViewModel.feedCreateContentBottomSheetData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                }
                is Fail -> Toast.makeText(
                    requireContext(),
                    it.throwable.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                        is FeedMainEvent.SelectTab -> {
                            handleActiveTab(event.data, event.position)
                        }
                        else -> {}
                    }

                    feedMainViewModel.consumeEvent(event)
                }
            }
        }
    }

    private fun observeUpload() {
        // we don't use repeatOnLifecycle here as we want to listen to upload receivers even when the page is not fully resumed
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            val uploadReceiver = uploadReceiverFactory.create(this@FeedBaseFragment)
            uploadReceiver
                .observe()
                .collect { info ->
                    when (val status = info.status) {
                        is UploadStatus.Progress -> {
                            binding.uploadView.show()
                            binding.uploadView.setProgress(status.progress)
                            binding.uploadView.setThumbnail(status.thumbnailUrl)
                        }
                        is UploadStatus.Finished -> {
                            binding.uploadView.hide()

                            if (info.type == UploadType.Shorts) {
                                showNormalToaster(
                                    getString(R.string.feed_upload_content_success),
                                    duration = Toaster.LENGTH_LONG,
                                    actionText = getString(R.string.feed_upload_shorts_see_video),
                                    actionListener = {
                                        playShortsUploadAnalytic.clickRedirectToChannelRoom(
                                            status.authorId,
                                            status.authorType,
                                            status.contentId
                                        )
                                        router.route(
                                            requireContext(),
                                            ApplinkConst.PLAY_DETAIL,
                                            status.contentId
                                        )
                                    }
                                )
                            } else {
                                showNormalToaster(
                                    getString(R.string.feed_upload_content_success),
                                    duration = Toaster.LENGTH_LONG
                                )
                            }
                        }
                        is UploadStatus.Failed -> {
                            binding.uploadView.setFailed()
                            binding.uploadView.setListener(object : UploadInfoView.Listener {
                                override fun onRetryClicked(view: UploadInfoView) {
                                    status.onRetry()
                                }

                                override fun onCloseWhenFailedClicked(view: UploadInfoView) {
                                    launch {
                                        uploadReceiver.releaseCurrent()
                                        binding.uploadView.hide()
                                    }

                                    if (info.type == UploadType.Post) {
                                        feedMainViewModel.deletePostCache()
                                    }
                                }
                            })
                        }
                    }
                }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) {
            onPauseInternal()
        } else {
            onResumeInternal()
        }
    }

    private fun initMetaView(meta: MetaModel) {
        showOnboarding(meta)

        binding.feedUserProfileImage.show()
        if (userSession.isLoggedIn) {
            if (meta.showMyProfile && meta.profilePhotoUrl.isNotEmpty()) {
                binding.feedUserProfileImage.setImageUrl(meta.profilePhotoUrl)
            } else {
                binding.feedUserProfileImage.hide()
            }
        }

        binding.btnFeedCreatePost.setOnClickListener {
            feedNavigationAnalytics.eventClickCreationButton()
            onCreatePostClicked()
        }

        binding.btnFeedLive.setOnClickListener {
            feedNavigationAnalytics.eventClickLiveButton()
            openAppLink.launch(meta.liveApplink)
        }

        binding.feedUserProfileImage.setOnClickListener {
            feedNavigationAnalytics.eventClickProfileButton()
            if (feedMainViewModel.isLoggedIn) {
                openAppLink.launch(meta.profileApplink)
            } else {
                openAppLink.launch(ApplinkConst.LOGIN)
            }
        }

        if (meta.isCreationActive && userSession.isLoggedIn) {
            binding.btnFeedCreatePost.show()
        } else {
            binding.btnFeedCreatePost.hide()
        }

        if (meta.showLive) {
            binding.btnFeedLive.show()
            binding.labelFeedLive.show()
        } else {
            binding.btnFeedLive.hide()
            binding.labelFeedLive.hide()
        }
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
            binding.tyFeedFirstTab.text = firstTabData.title
            binding.tyFeedFirstTab.setOnClickListener {
                feedNavigationAnalytics.eventClickForYouTab()
                selectActiveTab(TAB_FIRST_INDEX)
            }
            binding.tyFeedFirstTab.show()
        } else {
            binding.tyFeedFirstTab.hide()
        }

        if (secondTabData != null) {
            binding.tyFeedSecondTab.text = secondTabData.title
            binding.tyFeedSecondTab.setOnClickListener {
                feedNavigationAnalytics.eventClickFollowingTab()
                selectActiveTab(TAB_SECOND_INDEX)
            }
            binding.tyFeedSecondTab.show()
        } else {
            binding.tyFeedSecondTab.hide()
        }

        setupActiveTab(tab)
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
                        binding.btnFeedCreatePost
                    } else {
                        null
                    }
                )
                .setProfileEntryPointView(
                    if (meta.showMyProfile && !feedMainViewModel.hasShownProfileEntryPoint()) {
                        binding.feedUserProfileImage
                    } else {
                        null
                    }
                )
                .setListener(object : ImmersiveFeedOnboarding.Listener {
                    override fun onStarted() {
                    }

                    override fun onCompleteCreateContentOnboarding() {
                        feedMainViewModel.setHasShownCreateContent()
                    }

                    override fun onCompleteProfileEntryPointOnboarding() {
                        feedMainViewModel.setHasShownProfileEntryPoint()
                    }

                    override fun onFinished(isForcedDismiss: Boolean) {
                        if (!isForcedDismiss) feedMainViewModel.setReadyToShowOnboarding()
                    }
                }).build()

            mOnboarding?.show()
        }
    }

    private fun onCreatePostClicked() {
        activity?.let {
            val creationBottomSheet = FeedContentCreationTypeBottomSheet
                .getFragment(childFragmentManager, it.classLoader)

            val feedCreateBottomSheetDataResult =
                feedMainViewModel.feedCreateContentBottomSheetData.value
            if (feedCreateBottomSheetDataResult is Success) {
                val list = feedCreateBottomSheetDataResult.data
                if (list.isNotEmpty()) {
                    creationBottomSheet.setData(feedCreateBottomSheetDataResult.data)
                    creationBottomSheet.show(childFragmentManager)
                }
            }
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
    private fun handleActiveTab(dataModel: FeedDataModel, position: Int) {
        // keep active tab updated whenever sending tracker
        feedNavigationAnalytics.setActiveTab(dataModel)
        binding.vpFeedTabItemsContainer.setCurrentItem(position, true)
        if (position == TAB_FIRST_INDEX) {
            binding.root.transitionToStart()
        } else {
            binding.root.transitionToEnd()
        }
    }

    private fun getAllMotionScene(): List<ConstraintSet> {
        return listOf(
            binding.root.getConstraintSet(R.id.for_you_state),
            binding.root.getConstraintSet(R.id.following_state),
        )
    }

    companion object {
        const val TAB_FIRST_INDEX = 0
        const val TAB_SECOND_INDEX = 1

        const val TAB_TYPE_FOR_YOU = "foryou"
        const val TAB_TYPE_FOLLOWING = "following"
        const val CDP = "cdp"

        private const val EXTRAS_UTM_MEDIUM = "utm_medium"
        private const val PARAM_PUSH_NOTIFICATION = "push"

        private const val THRESHOLD_OFFSET_HALF = 0.5f

        private const val COACHMARK_START_DELAY_IN_SEC = 3

        private const val ONBOARDING_SHOW_DELAY = 500L
    }
}
