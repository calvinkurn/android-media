package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atcvariant.view.viewmodel.AtcVariantSharedViewModel
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.showImmediately
import com.tokopedia.stories.bottomsheet.StoriesProductBottomSheet
import com.tokopedia.stories.bottomsheet.StoriesThreeDotsBottomSheet
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.animation.StoriesProductNotch
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.model.isAnyShown
import com.tokopedia.stories.view.utils.SHOP_ID
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID
import com.tokopedia.stories.view.utils.StoriesSharingComponent
import com.tokopedia.stories.view.utils.TouchEventStories
import com.tokopedia.stories.view.utils.isNetworkError
import com.tokopedia.stories.view.utils.onTouchEventStories
import com.tokopedia.stories.view.utils.showToaster
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ContentIsLoaded
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.NextDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PreviousDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ResumeStories
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesDetailFragment @Inject constructor(
    private val router: Router,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesDetailBinding? = null
    private val binding: FragmentStoriesDetailBinding
        get() = _binding!!

    val viewModelProvider by lazyThreadSafetyNone {
        (requireParentFragment() as StoriesGroupFragment).viewModelProvider
    }

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelProvider }

    private val mAdapter: StoriesGroupAdapter by lazyThreadSafetyNone {
        StoriesGroupAdapter(object : StoriesGroupAdapter.Listener {
            override fun onClickGroup(position: Int) {
                viewModelAction(StoriesUiAction.SetGroup(position, false))
            }
        })
    }

    private val mLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private lateinit var variantSheet : AtcVariantBottomSheet

    private val atcVariantViewModel by lazyThreadSafetyNone {
        ViewModelProvider(requireActivity())[AtcVariantSharedViewModel::class.java]
    }

    private val groupId: String
        get() = arguments?.getString(STORIES_GROUP_ID).orEmpty()

    private val shopId: String
        get() = arguments?.getString(SHOP_ID).orEmpty()

    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is StoriesThreeDotsBottomSheet -> {}
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
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun setupUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroupHeader(prevState?.storiesGroup, state.storiesGroup)
                renderStoriesDetail(prevState?.storiesDetail, state.storiesDetail)
                observeBottomSheetStatus(prevState?.bottomSheetStatus, state.bottomSheetStatus)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    StoriesUiEvent.OpenKebab -> {
                        if (groupId != viewModel.mGroupId) return@collectLatest
                        StoriesThreeDotsBottomSheet
                            .getOrCreateFragment(
                                childFragmentManager,
                                requireActivity().classLoader
                            )
                            .show(childFragmentManager)
                    }
                    StoriesUiEvent.OpenProduct -> {
                        if (groupId != viewModel.mGroupId) return@collectLatest

                        StoriesProductBottomSheet.getOrCreateFragment(
                            childFragmentManager,
                            requireActivity().classLoader
                        )
                            .show(childFragmentManager)
                    }
                    is StoriesUiEvent.Login -> {
                        goTo(ApplinkConst.LOGIN)
                    }
                    is StoriesUiEvent.NavigateEvent -> goTo(event.appLink)
                    is StoriesUiEvent.ShowVariantSheet -> openVariantBottomSheet(event.product)
                    is StoriesUiEvent.TapSharing -> {
                        if (groupId != viewModel.mGroupId) return@collectLatest
                        val sheet = StoriesSharingComponent(rootView = requireView())
                        sheet.setListener(object : StoriesSharingComponent.Listener {
                            override fun onDismissEvent(view: StoriesSharingComponent) {
                                viewModelAction(StoriesUiAction.DismissSheet(BottomSheetType.Sharing))
                            }
                        })
                        sheet.show(childFragmentManager, event.metadata, viewModel.userId, viewModel.storyId)
                    }
                    is StoriesUiEvent.ShowErrorEvent -> {
                        if (viewModel.isAnyBottomSheetShown) return@collectLatest
                        requireView().showToaster(
                            message = event.message.message.orEmpty(),
                            type = Toaster.TYPE_ERROR
                        )
                    }
                    is StoriesUiEvent.ShowInfoEvent -> {
                        if (viewModel.isAnyBottomSheetShown) return@collectLatest
                        val message = getString(event.message)
                        requireView().showToaster(message = message,)
                    }
                    is StoriesUiEvent.ErrorDetailPage -> {
                        if (viewModel.mGroupId != groupId) return@collectLatest
                        if (event.throwable.isNetworkError) {
                            // TODO handle error network here
                            showToast("error detail network ${event.throwable}")
                        }
                        else {
                            // TODO handle error fetch here
                            showToast("error detail content ${event.throwable}")
                        }
                        showPageLoading(false)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderStoriesGroupHeader(
        prevState: StoriesGroupUiModel?,
        state: StoriesGroupUiModel,
    ) {
        if (prevState?.groupHeader == state.groupHeader ||
            groupId != state.selectedGroupId
        ) return

        mAdapter.setItems(state.groupHeader)
        mAdapter.notifyItemRangeInserted(mAdapter.itemCount, state.groupHeader.size)
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetailUiModel?,
        state: StoriesDetailUiModel
    ) {
        if (prevState == state ||
            state == StoriesDetailUiModel() ||
            state.detailItems.isEmpty() ||
            state.selectedGroupId != groupId || state.selectedDetailPosition < 0 || state.selectedDetailPositionCached < 0
        ) return

        val currentItem = state.detailItems[state.selectedDetailPosition]

        if (state.detailItems.isEmpty()) {
            // TODO handle error empty data state here
            Toast.makeText(
                requireContext(),
                "Don't worry this is debug: ask BE team why data stories $groupId is empty :)"
                , Toast.LENGTH_LONG
            ).show()
            return
        }

        storiesDetailsTimer(state)
        renderAuthor(currentItem)
        renderNotch(currentItem)

        val currContent = state.detailItems.getOrNull(state.selectedDetailPosition)
        if (currContent?.isSameContent == true || currContent == null) return

        showPageLoading(false)

        binding.ivStoriesDetailContent.apply {
            setImageUrl(currContent.imageContent)
            onUrlLoaded = {
                showStoriesComponent(true)
                contentIsLoaded()
            }
        }
        binding.vStoriesKebabIcon.showWithCondition(currContent.menus.isNotEmpty())
    }

    private fun observeBottomSheetStatus(
        prevState: Map<BottomSheetType, Boolean>?,
        state: Map<BottomSheetType, Boolean>,
    ) {
        if (prevState == state) return
        if (state.isAnyShown.orFalse()) pauseStories() else resumeStories()
    }

    private fun storiesDetailsTimer(state: StoriesDetailUiModel) {
        with(binding.cvStoriesDetailTimer) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesDetailTimer(
                        currentPosition = state.selectedDetailPosition,
                        itemCount = state.detailItems.size,
                        data = state.detailItems[state.selectedDetailPosition],
                    ) {
                        /** TODO
                         * all state already updated using the new groupId
                         * but the oldest data that we left (ui action swipe) still there
                         * need other way to stop the oldest data if the position is invalid
                         * it also causing broken timer experience when (ui action swipe)
                         * invalid -> groupId != viewModel.mGroupId
                         **/
                        if (groupId != viewModel.mGroupId) return@StoriesDetailTimer
                        viewModelAction(NextDetail)
                    }
                }
            }
        }
    }

    private fun renderAuthor(state: StoriesDetailItemUiModel) = with(binding.vStoriesPartner) {
        tvPartnerName.text = state.author.name
        ivIcon.setImageUrl(state.author.thumbnailUrl)
        btnFollow.gone()
        if (state.author is StoryAuthor.Shop) ivBadge.setImageUrl(state.author.badgeUrl)
    }


    private fun setupStoriesView() = with(binding) {
        showPageLoading(true)

        icClose.setOnClickListener { activity?.finish() }

        rvStoriesCategory.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        flStoriesPrev.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> {
                    flStoriesNext.hide()
                    flStoriesProduct.hide()
                    showStoriesComponent(false)
                    pauseStories()
                }

                TouchEventStories.RESUME -> {
                    flStoriesNext.show()
                    flStoriesProduct.show()
                    showStoriesComponent(true)
                    resumeStories()
                }

                TouchEventStories.NEXT_PREV -> viewModelAction(PreviousDetail)
                else -> {}
            }
        }
        flStoriesNext.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> {
                    flStoriesPrev.hide()
                    flStoriesProduct.hide()
                    showStoriesComponent(false)
                    pauseStories()
                }

                TouchEventStories.RESUME -> {
                    flStoriesPrev.show()
                    flStoriesProduct.show()
                    showStoriesComponent(true)
                    resumeStories()
                }

                TouchEventStories.NEXT_PREV -> viewModelAction(NextDetail)
                else -> {}
            }
        }
        vStoriesKebabIcon.setOnClickListener {
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
                    if (groupId != viewModel.mGroupId) return@onTouchEventStories
                    viewModelAction(StoriesUiAction.OpenProduct)
                }
                else -> {}
            }

        }
    }

    private fun renderNotch(state: StoriesDetailItemUiModel) {
        binding.vStoriesProductIcon.root.showWithCondition(viewModel.isProductAvailable)
        binding.vStoriesProductIcon.tvPlayProductCount.text = state.productCount
        with(binding.notchStoriesProduct) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesProductNotch(state.productCount) {
                        viewModelAction(StoriesUiAction.OpenProduct)
                    }
                }
                showWithCondition(viewModel.isProductAvailable)
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
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun showPageLoading(isShowLoading: Boolean) = with(binding){
        layoutTimer.llTimer.showWithCondition(isShowLoading)
        layoutDeclarative.loaderDecorativeWhite.showWithCondition(isShowLoading)
        ivStoriesDetailContent.showWithCondition(!isShowLoading)
    }

    private fun goTo(appLink: String) {
        router.route(requireContext(), appLink)
    }

    private fun openVariantBottomSheet(product: ContentTaggedProductUiModel) {
        atcVariantViewModel.setAtcBottomSheetParams(
            ProductVariantBottomSheetParams(
                pageSource = VariantPageSource.STORIES_PAGESOURCE.source,
                productId = product.id,
                shopId = shopId, //is shop id mandatory from applink?
                dismissAfterTransaction = false,
                trackerCdListName = viewModel.storyId,
            )
        )
        showImmediately(childFragmentManager, VARIANT_BOTTOM_SHEET_TAG) {
            variantSheet = AtcVariantBottomSheet()
            variantSheet.setOnDismissListener { }
            variantSheet.bottomSheetClose.setOnClickListener {
                viewModelAction(StoriesUiAction.DismissSheet(BottomSheetType.GVBS))
            }
            variantSheet
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "StoriesDetailFragment"
        private const val VARIANT_BOTTOM_SHEET_TAG = "atc variant bottom sheet"
        const val STORY_GROUP_ID = "StoriesGroupId"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesDetailFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesDetailFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesDetailFragment::class.java.name
            ) as StoriesDetailFragment
        }
    }
}
