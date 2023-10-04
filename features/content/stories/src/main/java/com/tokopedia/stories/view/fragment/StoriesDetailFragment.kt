package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atcvariant.view.viewmodel.AtcVariantSharedViewModel
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler.ImageLoaderStateListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
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
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType
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
import com.tokopedia.stories.view.viewmodel.state.isAnyShown
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.model.ShareModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesDetailFragment @Inject constructor(
    private val analyticFactory: StoriesAnalytics.Factory,
    private val router: Router
) : TkpdBaseV4Fragment(), StoriesThreeDotsBottomSheet.Listener, StoriesProductBottomSheet.Listener {

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

    private var variantSheet: AtcVariantBottomSheet? = null

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

    private val isEligiblePage: Boolean
        get() = groupId == viewModel.mGroup.groupId

    private val sharingComponent by lazyThreadSafetyNone {
        StoriesSharingComponent(rootView = requireView())
    }

    override fun getScreenName(): String {
        return TAG_FRAGMENT_STORIES_DETAIL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is StoriesThreeDotsBottomSheet -> fragment.setListener(this)
                is StoriesProductBottomSheet -> fragment.setListener(this)
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
        resumeStories()
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun setupUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroupHeader(prevState?.storiesMainData, state.storiesMainData)
                renderStoriesDetail(
                    prevState?.storiesMainData?.groupItems?.get(prevState.storiesMainData.selectedGroupPosition.orZero())?.detail,
                    state.storiesMainData.groupItems[state.storiesMainData.selectedGroupPosition].detail
                )
                observeBottomSheetStatus(prevState?.bottomSheetStatus, state.bottomSheetStatus)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesEvent.collect { event ->
                if (!isEligiblePage) return@collect
                when (event) {
                    is StoriesUiEvent.EmptyDetailPage -> {
                        setNoContent(true)
                        showPageLoading(false)
                    }

                    is StoriesUiEvent.ErrorDetailPage -> {
                        if (event.throwable.isNetworkError) {
                            setNoInternet(true)
                            binding.layoutStoriesNoInet.btnStoriesNoInetRetry.setOnClickListener { run { event.onClick() } }
                        } else {
                            setFailed(true)
                            binding.layoutStoriesFailed.btnStoriesFailedLoad.setOnClickListener { run { event.onClick() } }
                        }
                        showPageLoading(false)
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
                            override fun onDismissEvent(isFromClick: Boolean, view: StoriesSharingComponent) {
                                viewModelAction(StoriesUiAction.DismissSheet(BottomSheetType.Sharing))
                                if (isFromClick) analytic?.onCloseShareSheet(viewModel.storyId)
                            }

                            override fun onShareChannel(shareModel: ShareModel) {
                                analytic?.onClickShareOptions(viewModel.storyId, shareModel.channel.orEmpty())
                            }

                            override fun onShowSharing(view: StoriesSharingComponent) {
                                analytic?.onImpressShareSheet(viewModel.storyId)
                            }
                        })
                        analytic?.onClickShareIcon(viewModel.storyId)
                        sharingComponent.show(childFragmentManager, event.metadata, viewModel.userId, viewModel.storyId)
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
                    else -> return@collect
                }
            }
        }
    }

    private fun renderStoriesGroupHeader(
        prevState: StoriesUiModel?,
        state: StoriesUiModel
    ) {
        if (prevState?.groupHeader == state.groupHeader ||
            groupId != state.selectedGroupId
        ) {
            return
        }

        mAdapter.clearAllItems()
        mAdapter.setItems(state.groupHeader)
        mAdapter.notifyItemRangeInserted(mAdapter.itemCount, state.groupHeader.size)
        binding.rvStoriesCategory.scrollToPosition(state.selectedGroupPosition)
        binding.layoutDetailLoading.categoriesLoader.hide()
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetail?,
        state: StoriesDetail
    ) {
        if (prevState == state ||
            state == StoriesDetail() ||
            state.selectedGroupId != groupId ||
            state.selectedDetailPosition < 0 ||
            state.detailItems.isEmpty()
        ) {
            return
        }

        setNoInternet(false)
        setFailed(false)
        setNoContent(state.detailItems.isEmpty())

        val prevItem = prevState?.detailItems?.getOrNull(prevState.selectedDetailPosition)
        val currentItem = state.detailItems.getOrNull(state.selectedDetailPosition) ?: return

        storiesDetailsTimer(state)

        if (currentItem.isContentLoaded) return

        renderAuthor(currentItem)
        renderNudge(prevItem, currentItem)
        renderMedia(currentItem.content, currentItem.status)

        showPageLoading(false)
        binding.vStoriesKebabIcon.showWithCondition(currentItem.menus.isNotEmpty())
    }

    private fun renderMedia(content: StoriesItemContent, status: StoryStatus) {
        if (status == StoryStatus.Active &&
            content.type == StoriesItemContentType.Image
        ) {
            binding.layoutStoriesContent.ivStoriesDetailContent.loadImage(
                content.data,
                object : ImageLoaderStateListener {
                    override fun successLoad() {
                        contentIsLoaded()
                        analytic?.sendImpressionStoriesContent(viewModel.storyId)
                    }

                    override fun failedLoad() {
                    }
                }
            )
            binding.layoutStoriesContent.root.show()
            binding.layoutNoContent.root.hide()
        } else {
            binding.layoutStoriesContent.root.hide()
            binding.layoutNoContent.root.show()
        }
    }

    private fun observeBottomSheetStatus(
        prevState: Map<BottomSheetType, Boolean>?,
        state: Map<BottomSheetType, Boolean>
    ) {
        if (prevState == state) return
        if (state.isAnyShown.orFalse()) pauseStories() else resumeStories()
    }

    private fun storiesDetailsTimer(state: StoriesDetail) {
        with(binding.cvStoriesDetailTimer) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesDetailTimer(
                        currentPosition = state.selectedDetailPosition,
                        itemCount = state.detailItems.size,
                        data = state.detailItems[state.selectedDetailPosition]
                    ) { if (isEligiblePage) viewModelAction(NextDetail) }
                }
            }
        }
    }

    private fun buildEventLabel(): String = "${mParentPage.args.entryPoint} - ${viewModel.storyId} - ${mParentPage.args.authorId} - asgc - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker}"

    private fun renderAuthor(state: StoriesDetailItem) {
        with(binding.vStoriesPartner) {
            tvPartnerName.text = state.author.name
            ivIcon.setImageUrl(state.author.thumbnailUrl)
            btnFollow.gone()
            if (state.author is StoryAuthor.Shop) {
                ivBadge.setImageUrl(state.author.badgeUrl)
            }
            root.setOnClickListener {
                analytic?.sendClickShopNameEvent(buildEventLabel())
                viewModelAction(StoriesUiAction.Navigate(state.author.appLink))
            }
        }
    }

    private fun setupStoriesView() = with(binding) {
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
                StoriesProductNudge(state.productCount) {
                    viewModelAction(StoriesUiAction.OpenProduct)
                }
            }
        }

        if (prevState?.id == state.id) return
        showSwipeProductJob?.cancel()
        showSwipeProductJob = viewLifecycleOwner.lifecycleScope.launch {
            if (state.isProductAvailable) {
                binding.flStoriesProduct.hide()
                delay(DELAY_SWIPE_PRODUCT_BADGE_SHOW)
                TransitionManager.beginDelayedTransition(
                    binding.root,
                    Fade(Fade.IN)
                        .addTarget(binding.flStoriesProduct)
                )
                binding.flStoriesProduct.show()
            } else {
                binding.flStoriesProduct.hide()
            }
        }
    }

    private fun pauseStories() {
        viewModelAction(PauseStories)
    }

    private fun resumeStories() {
        viewModelAction(ResumeStories)
    }

    private fun contentIsLoaded() {
        viewModelAction(ContentIsLoaded)
    }

    private fun showStoriesComponent(isShow: Boolean) {
        binding.storiesComponent.showWithCondition(isShow)
        binding.flStoriesNext.showWithCondition(isShow)
        binding.flStoriesProduct.showWithCondition(isShow)
        binding.clSideIcons.showWithCondition(isShow)
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun showPageLoading(isShowLoading: Boolean) = with(binding) {
        rvStoriesCategory.showWithCondition(!isShowLoading)
        layoutStoriesContent.container.showWithCondition(!isShowLoading)
        layoutDetailLoading.container.showWithCondition(isShowLoading)
        binding.clSideIcons.showWithCondition(!isShowLoading)
    }

    private fun trackClickGroup(position: Int, data: StoriesGroupHeader) {
        analytic?.sendClickStoryCircleEvent(
            currentCircle = data.groupName,
            promotions = listOf(
                StoriesEEModel(
                    creativeName = "",
                    creativeSlot = position.plus(1).toString(),
                    itemId = "${data.groupId} - ${data.groupName} - ${mParentPage.args.authorId}",
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
                shopId = mParentPage.args.authorId,
                dismissAfterTransaction = false,
                trackerCdListName = viewModel.storyId
            )
        )
        showImmediately(childFragmentManager, VARIANT_BOTTOM_SHEET_TAG) {
            variantSheet = AtcVariantBottomSheet()
            variantSheet?.setOnDismissListener { }
            variantSheet?.setShowListener {
                variantSheet?.bottomSheetClose?.setOnClickListener {
                    variantSheet?.dismiss()
                    viewModelAction(StoriesUiAction.DismissSheet(BottomSheetType.GVBS))
                }
                variantSheet?.setOnDismissListener {
                    viewModelAction(StoriesUiAction.DismissSheet(BottomSheetType.GVBS))
                }
            }
            variantSheet ?: AtcVariantBottomSheet()
        }
    }

    private fun setNoInternet(isShow: Boolean) = with(binding.layoutStoriesNoInet) {
        root.showWithCondition(isShow)
        icCloseLoading.setOnClickListener { activity?.finish() }
    }

    private fun setFailed(isShow: Boolean) = with(binding.layoutStoriesFailed) {
        root.showWithCondition(isShow)
        icCloseLoading.setOnClickListener { activity?.finish() }
    }

    private fun setNoContent(isShow: Boolean) = with(binding.layoutNoContent) {
        root.showWithCondition(isShow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRemoveStory(view: StoriesThreeDotsBottomSheet) {
        analytic?.sendClickRemoveStoryEvent(buildEventLabel())
    }

    override fun onProductActionClicked(
        action: StoriesProductAction,
        product: ContentTaggedProductUiModel,
        position: Int,
        view: StoriesProductBottomSheet
    ) {
        val eventLabel = "${viewModel.storyId} - ${mParentPage.args.authorId} - asgc - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker} - ${product.id}"
        if (action == StoriesProductAction.Atc) analytic?.sendClickAtcButtonEvent(eventLabel, listOf(product), position, viewModel.mDetail.author.name) else analytic?.sendClickBuyButtonEvent(eventLabel, listOf(product), position, viewModel.mDetail.author.name)
    }

    override fun onClickedProduct(
        product: ContentTaggedProductUiModel,
        position: Int,
        view: StoriesProductBottomSheet
    ) {
        val eventLabel = "${viewModel.storyId} - ${mParentPage.args.authorId} - asgc - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker} - ${product.id}"
        analytic?.sendClickProductCardEvent(eventLabel, "/stories-room - ${viewModel.storyId} - product card", listOf(product), position)
    }

    override fun onImpressedProduct(
        product: Map<ContentTaggedProductUiModel, Int>,
        view: StoriesProductBottomSheet
    ) {
        val eventLabel = "${viewModel.storyId} - ${mParentPage.args.authorId} - asgc - ${viewModel.mDetail.content.type.value} - ${viewModel.mGroup.groupName} - ${viewModel.mDetail.meta.templateTracker} - ${product.keys.firstOrNull()?.id.orEmpty()}"
        analytic?.sendViewProductCardEvent(eventLabel, product)
    }

    private fun setupAnalytic() {
        if (analytic == null && mParentPage.args != StoriesArgsModel()) {
            analytic = analyticFactory.create(mParentPage.args)
        }
    }

    companion object {
        private const val DELAY_SWIPE_PRODUCT_BADGE_SHOW = 2000L

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
