package com.tokopedia.stories.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.ui.adapter.ContentTaggedProductBottomSheetAdapter
import com.tokopedia.content.common.ui.viewholder.ContentTaggedProductBottomSheetViewHolder
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.FragmentStoriesProductBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.model.ProductBottomSheetUiState
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * @author by astidhiyaa on 25/07/23
 */
class StoriesProductBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : BottomSheetUnify(), ContentTaggedProductBottomSheetViewHolder.Listener {

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

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
    }

    private fun setupView() {
        binding.rvStoriesProduct.adapter = productAdapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderProducts(prevState?.productSheet, state.productSheet)
            }
        }
    }

    private fun renderProducts(prevState: ProductBottomSheetUiState?, state: ProductBottomSheetUiState) {
        if (prevState == state) return

        binding.storiesProductSheetLoader.showWithCondition(state.resultState is ResultState.Loading)
        binding.rvStoriesProduct.shouldShowWithAction(state.resultState is ResultState.Success) {
            productAdapter.setItemsAndAnimateChanges(state.products)
        }
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

    private fun handleProductAction(type: StoriesProductAction, product: ContentTaggedProductUiModel) {
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
