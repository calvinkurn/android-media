package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.doOnLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.EventListener
import com.google.android.exoplayer2.video.VideoListener
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atcvariant.view.viewmodel.AtcVariantSharedViewModel
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.report_content.bottomsheet.ContentReportBottomSheet
import com.tokopedia.content.common.report_content.bottomsheet.ContentSubmitReportBottomSheet
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.util.ContentDateConverter
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play_common.view.ImageLoaderStateListener
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.showImmediately
import com.tokopedia.stories.analytics.StoriesAnalytics
import com.tokopedia.stories.analytics.StoriesEEModel
import com.tokopedia.stories.bottomsheet.StoriesProductBottomSheet
import com.tokopedia.stories.bottomsheet.StoriesThreeDotsBottomSheet
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.animation.StoriesProductNudge
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.components.player.StoriesExoPlayer
import com.tokopedia.stories.view.custom.StoriesErrorView
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.BUFFERING
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.RESUME
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Image
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Unknown
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Video
import com.tokopedia.stories.view.model.StoriesDetailItem.StoryStatus
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID
import com.tokopedia.stories.view.utils.StoriesSharingComponent
import com.tokopedia.stories.view.utils.TAG_FRAGMENT_STORIES_DETAIL
import com.tokopedia.stories.view.utils.TouchEventStories
import com.tokopedia.stories.view.utils.isNetworkError
import com.tokopedia.stories.view.utils.onTouchEventStories
import com.tokopedia.stories.view.utils.showToaster
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ContentIsLoaded
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.NextDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PreviousDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ResumeStories
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType
import com.tokopedia.stories.view.viewmodel.state.StoryReportStatusInfo
import com.tokopedia.stories.view.viewmodel.state.TimerStatusInfo
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.stories.R as storiesR

class StoriesDetailFragment @Inject constructor(
    private val analyticFactory: StoriesAnalytics.Factory,
    private val router: Router
) : TkpdBaseV4Fragment(),
    StoriesThreeDotsBottomSheet.Listener,
    StoriesProductBottomSheet.Listener,
    ContentReportBottomSheet.Listener,
    ContentSubmitReportBottomSheet.Listener,
    VideoListener {

    private val mParentPage: StoriesGroupFragment
        get() = (requireParentFragment() as StoriesGroupFragment)

    private var _binding: FragmentStoriesDetailBinding? = null
    private val binding: FragmentStoriesDetailBinding
        get() = _binding!!

    val viewModelProvider by lazyThreadSafetyNone { mParentPage.viewModelProvider }
    private val viewModel by activityViewModels<StoriesViewModel> { viewModelProvider }

    private val mAdapter: StoriesGroupAdapter by lazyThreadSafetyNone {
        StoriesGroupAdapter(object : StoriesGroupAdapter.Listener {
            override fun onClickGroup(position: Int, data: StoriesGroupHeader) {
                binding.rvStoriesCategory.scrollToPosition(position)
                trackClickGroup(position, data)
                viewModelAction(StoriesUiAction.SelectGroup(position, false))
            }

            override fun onGroupImpressed(data: StoriesGroupHeader) {
                viewModelAction(StoriesUiAction.CollectImpressedGroup(data))
            }
        })
    }

    private val mLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private val atcVariantViewModel by lazyThreadSafetyNone {
        ViewModelProvider(requireActivity())[AtcVariantSharedViewModel::class.java]
    }

    private var showSwipeProductJob: Job? = null

    private val groupId: String
        get() = arguments?.getString(STORIES_GROUP_ID).orEmpty()

    private var analytic: StoriesAnalytics? = null

    private val activityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    private val reportStoryLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (viewModel.userSession.isLoggedIn) {
                openReportBottomSheet()
            }
        }

    private val isEligiblePage: Boolean
        get() = groupId == viewModel.mGroup.groupId

    private val sharingComponent by lazyThreadSafetyNone {
        StoriesSharingComponent(rootView = requireView())
    }

    private var _videoPlayer: StoriesExoPlayer? = null
    private val videoPlayer: StoriesExoPlayer
        get() = _videoPlayer!!

    private var _dialog: DialogUnify? = null
    private val dialog: DialogUnify
        get() = _dialog!!

    private var currentPlayingVideoUrl: String = ""
    private var mCoachMark: CoachMark2? = null
    private var isPageActive: Boolean = false

    override fun getScreenName(): String {
        return TAG_FRAGMENT_STORIES_DETAIL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is StoriesThreeDotsBottomSheet -> fragment.setListener(this)

                is StoriesProductBottomSheet -> fragment.setListener(this)

                is ContentReportBottomSheet -> fragment.setListener(this)

                is ContentSubmitReportBottomSheet -> fragment.setListener(this)

                is AtcVariantBottomSheet -> {
                    fragment.setCloseClickListener {
                        fragment.dismiss()
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStoriesView()
        setupObserver()
        setupAnalytic()
    }

    override fun onResume() {
        super.onResume()
        isPageActive = true
        resumeStories(forceResume = true)
    }

    override fun onPause() {
        super.onPause()
        isPageActive = false
        pauseStories()
    }

    override fun onRenderedFirstFrame() {
        super.onRenderedFirstFrame()
        contentIsLoaded()
    }

    override fun onCloseButtonClicked() {
        ContentReportBottomSheet.get(childFragmentManager)?.dismiss()
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Report))
    }

    override fun onItemReportClick(item: PlayUserReportReasoningUiModel.Reasoning) {
        ContentReportBottomSheet.get(childFragmentManager)?.dismiss()
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Report))

        analytic?.sendClickReportReason(
            storiesId = viewModel.mDetail.id,
            contentType = viewModel.mDetail.content.type,
            storyType = viewModel.mDetail.storyType,
            reportReason = item.title
        )

        ContentSubmitReportBottomSheet.getOrCreate(
            childFragmentManager,
            requireActivity().classLoader
        ).apply {
            setData(item)
        }.show(childFragmentManager, ContentSubmitReportBottomSheet.TAG)

        viewModel.submitAction(StoriesUiAction.SelectReportReason(item))
    }

    override fun onFooterClicked() {
        router.route(
            context,
            getString(contentcommonR.string.content_user_report_footer_weblink)
        )
    }

    override fun onBackButtonListener() {
        (childFragmentManager.findFragmentByTag(ContentSubmitReportBottomSheet.TAG) as? ContentSubmitReportBottomSheet?)?.dismiss()
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.SubmitReport))
    }

    override fun onSubmitReport(desc: String) {
        showDialog(
            title = getString(storiesR.string.dialog_report_story_title),
            description = getString(contentcommonR.string.play_user_report_verification_dialog_desc),
            primaryCTAText = getString(contentcommonR.string.play_user_report_verification_dialog_btn_ok),
            secondaryCTAText = getString(storiesR.string.dialog_report_story_cancel),
            primaryAction = {
                viewModel.submitReport(desc, _videoPlayer?.exoPlayer?.currentPosition.orZero())
                (childFragmentManager.findFragmentByTag(ContentSubmitReportBottomSheet.TAG) as? ContentSubmitReportBottomSheet?)?.dismiss()
            }
        )
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun setupUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesState.withCache().collectLatest { (prevState, currState) ->
                renderStoriesGroupHeader(prevState?.storiesMainData, currState.storiesMainData, currState.canShowGroup)
                handleReportState(prevState?.reportState, currState.reportState)

                if (prevState?.storiesMainData != null && prevState.storiesMainData != StoriesUiModel()) {
                    val prev = prevState.storiesMainData.groupItems
                        .getOrNull(prevState.storiesMainData.selectedGroupPosition)?.detail
                    val curr = currState.storiesMainData.groupItems
                        .getOrNull(currState.storiesMainData.selectedGroupPosition)?.detail
                    renderStoriesDetail(prev, curr)
                    renderTimer(prevState.timerStatus, currState.timerStatus)

                    curr?.let {
                        it.detailItems.getOrNull(it.selectedDetailPosition)?.let { item ->
                            handleVideoPlayState(item, currState.timerStatus, it.selectedGroupId)
                        }
                    }
                }
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesEvent.collect { event ->
                if (!isEligiblePage) return@collect
                when (event) {
                    is StoriesUiEvent.EmptyDetailPage -> {
                        setErrorType(StoriesErrorView.Type.EmptyCategory)
                    }

                    is StoriesUiEvent.ErrorDetailPage -> {
                        setErrorType(if (event.throwable.isNetworkError) StoriesErrorView.Type.NoInternet else StoriesErrorView.Type.FailedLoad) { event.onClick() }
                    }

                    StoriesUiEvent.OpenKebab -> {
                        StoriesThreeDotsBottomSheet
                            .getOrCreateFragment(
                                childFragmentManager,
                                requireActivity().classLoader
                            ).show(childFragmentManager)
                    }

                    StoriesUiEvent.OpenProduct -> openProductBottomSheet()
                    is StoriesUiEvent.Login -> {
                        val intent = router.getIntent(requireContext(), ApplinkConst.LOGIN)
                        router.route(activityResult, intent)
                    }

                    is StoriesUiEvent.NavigateEvent -> goTo(event.appLink)
                    is StoriesUiEvent.ShowVariantSheet -> openVariantBottomSheet(event.product)
                    is StoriesUiEvent.TapSharing -> {
                        sharingComponent.setListener(object : StoriesSharingComponent.Listener {
                            override fun onDismissEvent(
                                isFromClick: Boolean,
                                view: StoriesSharingComponent
                            ) {
                                viewModelAction(StoriesUiAction.DismissSheet(BottomSheetType.Sharing))
                                if (isFromClick) analytic?.onCloseShareSheet(viewModel.storyId)
                            }

                            override fun onShareChannel(shareModel: ShareModel) {
                                analytic?.onClickShareOptions(
                                    viewModel.storyId,
                                    shareModel.channel.orEmpty()
                                )
                            }

                            override fun onShowSharing(view: StoriesSharingComponent) {
                                analytic?.onImpressShareSheet(viewModel.storyId)
                            }
                        })
                        analytic?.onClickShareIcon(viewModel.storyId)
                        sharingComponent.show(
                            childFragmentManager,
                            event.metadata,
                            viewModel.userId,
                            viewModel.storyId
                        )
                    }

                    is StoriesUiEvent.ShowErrorEvent -> {
                        if (viewModel.isAnyBottomSheetShown) return@collect
                        requireView().showToaster(
                            message = event.message.message.orEmpty(),
                            type = Toaster.TYPE_ERROR
                        )
                    }

                    is StoriesUiEvent.ShowInfoEvent -> {
                        if (viewModel.isAnyBottomSheetShown) return@collect
                        val message = getString(event.message)
                        requireView().showToaster(message = message)
                    }

                    is StoriesUiEvent.ShowStoriesTimeCoachmark -> {
                        showStoriesDurationCoachmark()
                    }

                    else -> return@collect
                }
            }
        }
    }

    private fun handleReportState(prevState: StoryReportStatusInfo?, state: StoryReportStatusInfo) {
        if (prevState?.state == state.state) return
        if (state.state != StoryReportStatusInfo.ReportState.Submitted) return
        if (state.report.submitStatus == null) return

        when (state.report.submitStatus) {
            is Success -> requireView().showToaster(message = getString(storiesR.string.story_reported_successfully_message))
            is Fail -> requireView().showToaster(
                message = ErrorHandler.getErrorMessage(
                    context,
                    state.report.submitStatus.throwable
                ),
                type = Toaster.TYPE_ERROR
            )
        }
        viewModel.submitAction(StoriesUiAction.ResetReportState)
    }

    private fun renderStoriesGroupHeader(
        prevState: StoriesUiModel?,
        state: StoriesUiModel,
        canShowGroup: Boolean
    ) {
        if (prevState?.groupHeader == state.groupHeader ||
            groupId != state.selectedGroupId
        ) {
            return
        }

        if (canShowGroup) {
            mAdapter.clearAllItems()
            mAdapter.setItems(state.groupHeader)
            mAdapter.notifyItemRangeInserted(mAdapter.itemCount, state.groupHeader.size)
            binding.rvStoriesCategory.scrollToPosition(state.selectedGroupPosition)
            binding.tvTitle.text = ""
        } else {
            mAdapter.clearAllItems()
            mAdapter.notifyItemRangeRemoved(0, state.groupHeader.size)
            val selectedGroup = state.groupItems[state.selectedGroupPosition]
            binding.tvTitle.text = selectedGroup.detail.detailItems[selectedGroup.detail.selectedDetailPosition].categoryName
        }

        binding.layoutDetailLoading.categoriesLoader.hide()
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetail?,
        state: StoriesDetail?
    ) {
        if (prevState == state ||
            state == StoriesDetail() ||
            state == null ||
            state.selectedGroupId != groupId ||
            state.selectedDetailPosition < 0 ||
            state.detailItems.isEmpty()
        ) {
            return
        }

        val prevItem = prevState?.detailItems?.getOrNull(prevState.selectedDetailPosition)
        val currentItem = state.detailItems.getOrNull(state.selectedDetailPosition) ?: return

        if (currentItem.isContentLoaded) return

        renderAuthor(currentItem)
        renderNudge(prevItem, currentItem)
        renderMedia(currentItem.content, currentItem.status, state.selectedGroupId)

        showPageLoading(false)
        binding.vStoriesKebabIcon.showWithCondition(currentItem.menus.isNotEmpty())
        binding.vStoriesShareIcon.showWithCondition(currentItem.share.isShareable)
    }

    private fun renderMedia(
        content: StoriesItemContent,
        status: StoryStatus,
        selectedGroupId: String
    ) {
        when (content.type) {
            Image -> {
                renderStoryBasedOnStatus(status) {
                    binding.layoutStoriesContent.ivStoriesDetailContent.loadImage(
                        content.data,
                        object : ImageLoaderStateListener {
                            override fun successLoad() {
                                contentIsLoaded()
                                analytic?.sendImpressionStoriesContent(viewModel.storyId)
                                hideError()
                            }

                            override fun failedLoad() {
                                setErrorType(StoriesErrorView.Type.NoContent)
                                contentIsLoaded()
                            }
                        }
                    )
                    showImageContent()
                }
            }

            Video -> {
                renderStoryBasedOnStatus(status) {
                    showVideoContent()
                    showVideoLoading()
                    renderVideoMedia(content, selectedGroupId)
                    hideError()
                }
            }

            Unknown -> {
                setErrorType(StoriesErrorView.Type.EmptyCategory)
                contentIsLoaded()
            }
        }
    }

    private fun renderTimer(prevTimer: TimerStatusInfo?, timerState: TimerStatusInfo) {
        if (prevTimer == timerState) return

        showStoriesActionView(timerState.event == RESUME)

        with(binding.cvStoriesDetailTimer) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesDetailTimer(
                        timerInfo = timerState
                    ) {
                        if (isEligiblePage) {
                            mCoachMark?.dismissCoachMark()
                            viewModelAction(NextDetail)
                        }
                    }
                }
            }
        }
    }

    private fun buildEventLabel(): String =
        "${mParentPage.args.entryPoint} - ${viewModel.storyId} - ${viewModel.validAuthorId} - ${viewModel.mDetail.storyType} - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker}"

    private fun renderAuthor(state: StoriesDetailItem) {
        with(binding.vStoriesPartner) {
            tvPartnerName.text = state.author.name
            ivIcon.setImageUrl(state.author.thumbnailUrl)
            btnFollow.gone()

            when (state.category) {
                StoriesDetailItem.StoryCategory.Manual -> {
                    val creationTimestamp =
                        ContentDateConverter.getDiffTime(state.publishedAt) { dateTime ->
                            when {
                                dateTime.day > THIRTY -> dateTime.yearMonth
                                dateTime.day in ONE..THIRTY -> "${dateTime.day} ${ContentDateConverter.DAY}"
                                dateTime.hour in ONE..TWENTY_THREE -> "${dateTime.hour} ${ContentDateConverter.HOUR}"
                                dateTime.minute in ONE..FIFTY_NINE -> "${dateTime.minute} ${ContentDateConverter.MINUTE_CONCISE}"
                                else -> ContentDateConverter.BELOW_1_MINUTE_CONCISE
                            }
                        }

                    tvStoriesTimestamp.text = getString(
                        storiesR.string.story_creation_timestamp,
                        creationTimestamp
                    )
                    tvStoriesTimestamp.show()
                }

                StoriesDetailItem.StoryCategory.ASGC -> {
                    tvStoriesTimestamp.hide()
                }
            }

            if (state.author is StoryAuthor.Shop) {
                ivBadge.setImageUrl(state.author.badgeUrl)
            }
            root.setOnClickListener {
                analytic?.sendClickShopNameEvent(buildEventLabel())
                viewModelAction(StoriesUiAction.Navigate(state.author.appLink))
            }
            root.show()
        }
    }

    private fun setupStoriesView() = with(binding) {
        showStoriesActionView(false)
        showPageLoading(true)

        icClose.setOnClickListener { activity?.finish() }

        binding.icClose.hide()
        icClose.setOnClickListener {
            activity?.finish()
        }

        rvStoriesCategory.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        flStoriesPrev.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> {
                    showStoriesComponent(false)
                    pauseStories()
                }

                TouchEventStories.RESUME -> {
                    showStoriesComponent(true)
                    resumeStories()
                }

                TouchEventStories.NEXT_PREV -> {
                    trackTapPreviousDetail()
                    mCoachMark?.dismissCoachMark()
                    viewModelAction(PreviousDetail)
                }

                else -> {}
            }
        }
        flStoriesNext.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> {
                    showStoriesComponent(false)
                    pauseStories()
                }

                TouchEventStories.RESUME -> {
                    showStoriesComponent(true)
                    resumeStories()
                }

                TouchEventStories.NEXT_PREV -> {
                    trackTapNextDetail()
                    mCoachMark?.dismissCoachMark()
                    viewModelAction(NextDetail)
                }

                else -> {}
            }
        }
        vStoriesKebabIcon.setOnClickListener {
            analytic?.sendClickThreeDotsEvent(buildEventLabel())
            viewModelAction(StoriesUiAction.OpenKebabMenu)
        }
        vStoriesShareIcon.setOnClickListener {
            viewModelAction(StoriesUiAction.TapSharing)
        }
        vStoriesProductIcon.root.setOnClickListener {
            viewModelAction(StoriesUiAction.OpenProduct)
        }
        flStoriesProduct.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.SWIPE_UP -> {
                    if (!isEligiblePage || !viewModel.isProductAvailable) return@onTouchEventStories
                    viewModelAction(StoriesUiAction.OpenProduct)
                }

                else -> {}
            }
        }
    }

    private fun renderNudge(prevState: StoriesDetailItem?, state: StoriesDetailItem) {
        binding.vStoriesProductIcon.root.showWithCondition(state.isProductAvailable)
        binding.vStoriesProductIcon.tvPlayProductCount.text = state.productCount

        with(binding.nudgeStoriesProduct) {
            setContent {
                StoriesProductNudge(state.productCount, state.isProductAvailable) {
                    viewModelAction(StoriesUiAction.OpenProduct)
                }
            }
        }

        if (prevState?.id == state.id) return
        showSwipeProductJob?.cancel()
        showSwipeProductJob = viewLifecycleOwner.lifecycleScope.launch {
            if (state.isProductAvailable) {
                binding.flStoriesProduct.hide()
                binding.nudgeStoriesProduct.hide()

                delay(DELAY_SWIPE_PRODUCT_BADGE_SHOW)
                TransitionManager.beginDelayedTransition(
                    binding.root,
                    Fade(Fade.IN)
                        .addTarget(binding.flStoriesProduct)
                )
                binding.flStoriesProduct.show()
                binding.nudgeStoriesProduct.show()
            } else {
                binding.flStoriesProduct.hide()
                binding.nudgeStoriesProduct.hide()
            }
        }
    }

    private fun pauseStories() {
        viewModelAction(PauseStories)
    }

    private fun resumeStories(forceResume: Boolean = false) {
        viewModelAction(ResumeStories(forceResume))
    }

    private fun contentIsLoaded() {
        viewModelAction(ContentIsLoaded)
    }

    private fun showStoriesComponent(isShow: Boolean) {
        showSwipeProductJob?.cancel()
        binding.storiesComponent.showWithCondition(isShow)
        binding.clSideIcons.showWithCondition(isShow)
        binding.vStoriesPartner.root.showWithCondition(isShow)

        binding.tvTitle.showWithCondition(isShow && !viewModel.storiesState.value.canShowGroup)
    }

    private fun showStoriesActionView(isShow: Boolean) {
        binding.flStoriesNext.showWithCondition(isShow)
        binding.flStoriesPrev.showWithCondition(isShow)
        binding.flStoriesProduct.showWithCondition(isShow)
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun showPageLoading(isShowLoading: Boolean) = with(binding) {
        cvStoriesDetailTimer.showWithCondition(isShowLoading)
        rvStoriesCategory.showWithCondition(!isShowLoading)
        layoutStoriesContent.container.showWithCondition(!isShowLoading)
        layoutDetailLoading.container.showWithCondition(isShowLoading)
        clSideIcons.showWithCondition(!isShowLoading)
    }

    private fun trackClickGroup(position: Int, data: StoriesGroupHeader) {
        analytic?.sendClickStoryCircleEvent(
            currentCircle = data.groupName,
            promotions = listOf(
                StoriesEEModel(
                    creativeName = "",
                    creativeSlot = position.plus(1).toString(),
                    itemId = "${data.groupId} - ${data.groupName} - ${viewModel.validAuthorId}",
                    itemName = "/ - stories"
                )
            )
        )
    }

    private fun trackTapPreviousDetail() {
        analytic?.sendClickTapPreviousContentEvent(
            storiesId = viewModel.mDetail.id,
            creatorType = "asgc",
            contentType = viewModel.mDetail.content.type.value,
            currentCircle = viewModel.mGroup.groupName
        )
    }

    private fun trackTapNextDetail() {
        analytic?.sendClickTapNextContentEvent(
            storiesId = viewModel.mDetail.id,
            creatorType = "asgc",
            contentType = viewModel.mDetail.content.type.value,
            currentCircle = viewModel.mGroup.groupName
        )
    }

    private fun goTo(appLink: String) {
        router.route(requireContext(), appLink)
    }

    private fun openProductBottomSheet() {
        analytic?.sendClickShoppingBagEvent(buildEventLabel())
        StoriesProductBottomSheet.getOrCreateFragment(
            childFragmentManager,
            requireActivity().classLoader
        ).show(childFragmentManager)
    }

    private fun openVariantBottomSheet(product: ContentTaggedProductUiModel) {
        atcVariantViewModel.setAtcBottomSheetParams(
            ProductVariantBottomSheetParams(
                pageSource = VariantPageSource.STORIES_PAGESOURCE.source,
                productId = product.id,
                shopId = viewModel.validAuthorId,
                dismissAfterTransaction = false,
                trackerCdListName = viewModel.storyId
            )
        )
        showImmediately(childFragmentManager, VARIANT_BOTTOM_SHEET_TAG) {
            AtcVariantBottomSheet()
        }
    }

    private fun setErrorType(errorType: StoriesErrorView.Type, onClick: () -> Unit = {}) {
        with(binding.vStoriesError) {
            show()
            type = errorType
            setAction { onClick() }
            setCloseAction { activity?.finish() }
            translationZ =
                if (errorType == StoriesErrorView.Type.NoContent || errorType == StoriesErrorView.Type.EmptyCategory) 0f else 1f
        }

        binding.vStoriesPartner.root.hide()
    }

    private fun hideError() = binding.vStoriesError.gone()

    override fun onDestroyView() {
        _videoPlayer?.destroy()
        _videoPlayer = null

        _dialog?.dismiss()
        _dialog = null

        mCoachMark = null

        _binding = null
        super.onDestroyView()
    }

    override fun onRemoveStory(view: StoriesThreeDotsBottomSheet) {
        analytic?.sendClickRemoveStoryEvent(buildEventLabel())
    }

    override fun onSeePerformance(view: StoriesThreeDotsBottomSheet) {
        analytic?.clickPerformance(
            storiesId = viewModel.mDetail.id,
            contentType = viewModel.mDetail.content.type,
            storyType = viewModel.mDetail.storyType,
        )
        router.route(requireContext(), viewModel.mDetail.performanceLink)
    }

    override fun onReportStoryClicked(view: StoriesThreeDotsBottomSheet) {
        view.dismiss()

        if (!viewModel.userSession.isLoggedIn) {
            reportStoryLoginResult.launch(router.getIntent(context, ApplinkConst.LOGIN))
        } else {
            openReportBottomSheet()
        }
    }

    override fun onProductActionClicked(
        action: StoriesProductAction,
        product: ContentTaggedProductUiModel,
        position: Int,
        view: StoriesProductBottomSheet
    ) {
        val eventLabel =
            "${viewModel.storyId} - ${viewModel.validAuthorId} - asgc - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker} - ${product.id}"
        if (action == StoriesProductAction.Atc) {
            analytic?.sendClickAtcButtonEvent(
                eventLabel,
                listOf(product),
                position,
                viewModel.mDetail.author.name
            )
        } else {
            analytic?.sendClickBuyButtonEvent(
                eventLabel,
                listOf(product),
                position,
                viewModel.mDetail.author.name
            )
        }
    }

    override fun onClickedProduct(
        product: ContentTaggedProductUiModel,
        position: Int,
        view: StoriesProductBottomSheet
    ) {
        val eventLabel =
            "${viewModel.storyId} - ${viewModel.validAuthorId} - asgc - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker} - ${product.id}"
        analytic?.sendClickProductCardEvent(
            eventLabel,
            "/stories-room - ${viewModel.storyId} - product card",
            listOf(product),
            position
        )
    }

    override fun onImpressedProduct(
        product: Map<ContentTaggedProductUiModel, Int>,
        view: StoriesProductBottomSheet
    ) {
        val eventLabel =
            "${viewModel.storyId} - ${viewModel.validAuthorId} - asgc - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker} - ${product.keys.firstOrNull()?.id.orEmpty()}"
        analytic?.sendViewProductCardEvent(eventLabel, product)
    }

    private fun setupAnalytic() {
        if (analytic == null && mParentPage.args != StoriesArgsModel()) {
            analytic = analyticFactory.create(mParentPage.args)
        }
    }

    private fun showImageContent() {
        binding.layoutStoriesContent.ivStoriesDetailContent.show()
        binding.layoutStoriesContent.containerVideoStoriesContent.hide()
    }

    private fun showVideoContent() {
        binding.layoutStoriesContent.ivStoriesDetailContent.hide()
        binding.layoutStoriesContent.containerVideoStoriesContent.show()
    }

    private fun renderVideoMedia(content: StoriesItemContent, selectedGroupId: String) {
        context?.let {
            if (_videoPlayer == null) {
                _videoPlayer = StoriesExoPlayer(it)
            }

            videoPlayer.pause()
            videoPlayer.setVideoListener(this)
            videoPlayer.setEventListener(object : EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    val isPlaying = playWhenReady && playbackState == Player.STATE_READY
                    val isBuffering = playbackState == Player.STATE_BUFFERING

                    when {
                        isPlaying -> {
                            val videoDuration = videoPlayer.exoPlayer.duration.toInt()
                            if (content.duration != videoDuration) {
                                viewModel.submitAction(
                                    StoriesUiAction.UpdateStoryDuration(
                                        videoDuration
                                    )
                                )
                            }
                            hideVideoLoading()
                            contentIsLoaded()
                        }

                        isBuffering -> {
                            viewModel.submitAction(StoriesUiAction.VideoBuffering)
                        }
                    }
                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    setErrorType(StoriesErrorView.Type.NoContent)
                }
            })

            binding.layoutStoriesContent.playerStoriesDetailContent.player = videoPlayer.exoPlayer

            if (currentPlayingVideoUrl != content.data) {
                videoPlayer.start(content.data, selectedGroupId, isAutoPlay = isPageActive)
                currentPlayingVideoUrl = content.data
            } else if (!videoPlayer.exoPlayer.isPlaying && isPageActive) {
                videoPlayer.resume(shouldReset = true, selectedGroupId)
            }
        }
    }

    private fun showVideoLoading() {
        binding.layoutStoriesContent.loaderStoriesDetailContent.show()
        binding.layoutStoriesContent.playerStoriesDetailContent.hide()
    }

    private fun hideVideoLoading() {
        binding.layoutStoriesContent.loaderStoriesDetailContent.hide()
        binding.layoutStoriesContent.playerStoriesDetailContent.show()
    }

    private fun renderStoryBasedOnStatus(
        status: StoryStatus,
        onActive: () -> Unit
    ) {
        when (status) {
            StoryStatus.Active -> {
                onActive()
            }

            StoryStatus.Unknown -> {
                setErrorType(StoriesErrorView.Type.NoContent)
                contentIsLoaded()
            }
        }
    }

    private fun handleVideoPlayState(state: StoriesDetailItem, timerState: TimerStatusInfo, selectedGroupId: String) {
        if (_videoPlayer == null) return

        when {
            (state.event == RESUME || state.event == BUFFERING) && state.content.type == Video && timerState.event != PAUSE -> {
                if (!isPageActive) return
                videoPlayer.resume(activeGroupId = selectedGroupId)
            }
            state.event == PAUSE || timerState.event == PAUSE -> {
                videoPlayer.pause()
            }
        }
    }

    private fun openReportBottomSheet() {
        ContentReportBottomSheet.getOrCreate(
            childFragmentManager,
            requireActivity().classLoader
        ).apply {
            updateList(viewModel.userReportReasonList)
        }.show(childFragmentManager, ContentReportBottomSheet.TAG)

        analytic?.sendViewReportReasonList(
            storiesId = viewModel.mDetail.id,
            contentType = viewModel.mDetail.content.type,
            storyType = viewModel.mDetail.storyType
        )

        viewModel.submitAction(StoriesUiAction.OpenReport)
    }

    private fun showDialog(
        title: String,
        description: String,
        primaryCTAText: String,
        secondaryCTAText: String,
        primaryAction: () -> Unit,
        secondaryAction: () -> Unit = {}
    ) {
        activity?.let {
            if (_dialog == null) {
                _dialog =
                    DialogUnify(context = it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            }
            if (dialog.isShowing) return

            dialog.apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCTAText)
                setPrimaryCTAClickListener {
                    primaryAction()
                    dismiss()
                }
                setSecondaryCTAText(secondaryCTAText)
                setSecondaryCTAClickListener {
                    secondaryAction()
                    dismiss()
                }
            }.show()
        }
    }

    private fun showStoriesDurationCoachmark() {
        context?.let {
            if (mCoachMark == null) {
                mCoachMark = CoachMark2(it).apply {
                    onDismissListener = {
                        viewModel.submitAction(StoriesUiAction.HasSeenDurationCoachMark)
                    }
                }
            }

            if (mCoachMark?.isShowing != true) {
                binding.vStoriesPartner.tvStoriesTimestamp.doOnLayout { storiesTimestamp ->
                    mCoachMark?.showCoachMark(
                        arrayListOf(
                            CoachMark2Item(
                                storiesTimestamp,
                                getString(storiesR.string.story_manual_duration_coachmark_title),
                                getString(storiesR.string.story_manual_duration_coachmark_subtitle),
                                CoachMark2.POSITION_TOP
                            )
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val DELAY_SWIPE_PRODUCT_BADGE_SHOW = 2000L

        private const val THIRTY = 30
        private const val TWENTY_THREE = 23
        private const val FIFTY_NINE = 59
        private const val ONE = 1

        private const val VARIANT_BOTTOM_SHEET_TAG = "atc variant bottom sheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesDetailFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG_FRAGMENT_STORIES_DETAIL) as? StoriesDetailFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesDetailFragment::class.java.name
            ) as StoriesDetailFragment
        }
    }
}
