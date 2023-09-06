package com.tokopedia.stories.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.ui.adapter.ContentTaggedProductBottomSheetAdapter
import com.tokopedia.content.common.ui.viewholder.ContentTaggedProductBottomSheetViewHolder
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.FragmentStoriesProductBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.model.ProductBottomSheetUiState
import com.tokopedia.stories.view.model.StoriesCampaignUiModel
import com.tokopedia.stories.view.model.isNotAvailable
import com.tokopedia.stories.view.model.isOngoing
import com.tokopedia.stories.view.utils.showToaster
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.utils.date.DateUtil
import kotlinx.coroutines.flow.collectLatest
import java.util.Date
import javax.inject.Inject

/**
 * @author by astidhiyaa on 25/07/23
 */
class StoriesProductBottomSheet @Inject constructor(
) : BottomSheetUnify(), ContentTaggedProductBottomSheetViewHolder.Listener {

    private val viewModel by activityViewModels<StoriesViewModel> { (requireParentFragment() as StoriesDetailFragment).viewModelProvider }

    private var _binding: FragmentStoriesProductBinding? = null
    private val binding: FragmentStoriesProductBinding get() = _binding!!

    private val productAdapter by lazyThreadSafetyNone {
        ContentTaggedProductBottomSheetAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoriesProductBinding.inflate(inflater, container, false)

        setChild(binding.root)
        setTitle(getString(R.string.stories_product_bottomsheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiState()
        observeUiEvent()
    }

    private fun setupView() {
        binding.rvStoriesProduct.adapter = productAdapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderProducts(prevState?.productSheet, state.productSheet)
                renderCampaign(prevState?.productSheet, state.productSheet)
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is StoriesUiEvent.ShowErrorEvent -> {
                        requireView().showToaster(
                            message = event.message.message.orEmpty(),
                            type = Toaster.TYPE_ERROR,
                            bottomHeight = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_48)
                                .orZero()
                        )
                    }

                    is StoriesUiEvent.ShowInfoEvent -> {
                        val message = getString(event.message)
                        requireView().showToaster(
                            message = message,
                            bottomHeight = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_48)
                                .orZero()
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    private fun renderProducts(
        prevState: ProductBottomSheetUiState?,
        state: ProductBottomSheetUiState
    ) {
        if (prevState == state) return

        binding.storiesProductSheetLoader.showWithCondition(state.resultState is ResultState.Loading)
        binding.rvStoriesProduct.shouldShowWithAction(state.resultState is ResultState.Success) {
            productAdapter.setItemsAndAnimateChanges(state.products)
        }
    }

    private fun renderCampaign(
        prevState: ProductBottomSheetUiState?,
        state: ProductBottomSheetUiState
    ) {
        if (prevState == state) return
        val campaign = state.campaign
        binding.vStoriesCampaign.root.showWithCondition(!campaign.isNotAvailable)

        binding.vStoriesCampaign.tvHeaderTitle.text = when (campaign) {
            is StoriesCampaignUiModel.Ongoing -> campaign.title
            is StoriesCampaignUiModel.Upcoming -> campaign.title
            else -> ""
        }

        binding.vStoriesCampaign.sectionTimer.timerVariant = if (campaign.isOngoing) {
            TimerUnifySingle.VARIANT_MAIN
        } else TimerUnifySingle.VARIANT_INFORMATIVE

        val targetTime = when (campaign) {
            is StoriesCampaignUiModel.Ongoing -> campaign.endTime
            is StoriesCampaignUiModel.Upcoming -> campaign.startTime
            else -> null
        } ?: return

        val dt = DateUtil.getCurrentCalendar().apply {
            time = Date(targetTime.time)
        }

        binding.vStoriesCampaign.sectionTimer.targetDate = dt
    }

    override fun onResume() {
        super.onResume()
        viewModel.submitAction(StoriesUiAction.FetchProduct)
    }

    override fun onProductCardClicked(product: ContentTaggedProductUiModel, itemPosition: Int) {
        viewModel.submitAction(StoriesUiAction.Navigate(product.appLink))
    }

    override fun onAddToCartProductButtonClicked(
        product: ContentTaggedProductUiModel,
        itemPosition: Int
    ) {
        handleProductAction(StoriesProductAction.ATC, product)
    }

    override fun onBuyProductButtonClicked(
        product: ContentTaggedProductUiModel,
        itemPosition: Int
    ) {
        handleProductAction(StoriesProductAction.Buy, product)
    }

    private fun handleProductAction(
        type: StoriesProductAction,
        product: ContentTaggedProductUiModel
    ) {
        if (product.showGlobalVariant) {
            viewModel.submitAction(StoriesUiAction.ShowVariantSheet(product))
        } else {
            viewModel.submitAction(StoriesUiAction.ProductAction(type, product))
        }
    }

    fun show(fg: FragmentManager) {
        if (isAdded) return
        super.show(fg, StoriesThreeDotsBottomSheet.TAG)
    }

    override fun dismiss() {
        if (!isAdded) return
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Product))
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Product))
        super.onCancel(dialog)
    }

    companion object {
        const val TAG = "StoriesProductBottomSheet"

        fun get(fragmentManager: FragmentManager): StoriesProductBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? StoriesProductBottomSheet
        }

        fun getOrCreateFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesProductBottomSheet {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesProductBottomSheet::class.java.name
            ) as StoriesProductBottomSheet
        }
    }
}
