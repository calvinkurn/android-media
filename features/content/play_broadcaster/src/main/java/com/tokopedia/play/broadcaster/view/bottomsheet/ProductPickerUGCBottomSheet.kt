package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.ugc.ProductPickerUGCAnalytic
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayUgcProductPickerBinding
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.BaseProductSetupBottomSheet
import com.tokopedia.play.broadcaster.type.PriceUnknown
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 29/08/22
 */
class ProductPickerUGCBottomSheet @Inject constructor(
    private val dialogCustomizer: PlayBroadcastDialogCustomizer,
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: ProductPickerUGCAnalytic,
) : BaseProductSetupBottomSheet() {

    private val offsetToaster by lazy { context?.resources?.getDimensionPixelOffset(R.dimen.play_dp_50) ?: 0 }

    private var _binding: BottomSheetPlayUgcProductPickerBinding? = null
    private val binding: BottomSheetPlayUgcProductPickerBinding
        get() = _binding!!

    private val parentViewModel by activityViewModels<PlayBroadcastViewModel> {
        parentViewModelFactoryCreator.create(requireActivity())
    }

    private val productTagListener = object : ProductTagParentFragment.Listener {
        override fun onCloseProductTag() {
            closeBottomSheet()
        }

        override fun onFinishProductTag(products: List<SelectedProductUiModel>) {
            viewModel.submitAction(
                ProductSetupAction.SetProducts(
                    products.map {
                        ProductUiModel(
                            id = it.id,
                            name = it.name,
                            imageUrl = it.cover,
                            stock = 1,
                            price = PriceUnknown,
                        )
                    }
                )
            )

            viewModel.submitAction(ProductSetupAction.SaveProducts)
        }

        override fun onMaxSelectedProductReached() {
            toaster.showToaster(
                message = getString(R.string.play_bro_max_selected_product_reached).format(viewModel.maxProduct),
                actionLabel = getString(R.string.play_ok),
                actionListener = { toaster.dismissToaster() },
                bottomMargin = offsetToaster,
            )
        }
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    private val productTagDataSource = object : ProductTagParentFragment.DataSource {
        override fun getInitialSelectedProduct(): List<SelectedProductUiModel> {
            return viewModel.selectedProducts.map {
                SelectedProductUiModel.createOnlyId(it.id)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                ProductTagParentFragment.findFragment(childFragmentManager)
                    ?.onBackPressed()
            }

            override fun cancel() {
                closeBottomSheet()
            }
        }.apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    private var mListener: Listener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductTagParentFragment -> {
                childFragment.setListener(productTagListener)
                childFragment.setDataSource(productTagDataSource)
                childFragment.setAnalytic(analytic)
            }
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun showNow(fragmentManager: FragmentManager) {
        showNow(fragmentManager, TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * SHEET_HEIGHT_PERCENT).toInt() + bottomSheetWrapper.paddingTop
        }

        try {
            bottomSheetWrapper.setPadding(0, 0, 0, 0)
        } catch (ignored: UninitializedPropertyAccessException) { }

        setCloseClickListener {
            closeBottomSheet()
        }

        val selectedAccount = parentViewModel.uiState.value.selectedContentAccount

        val productPicker = ProductTagParentFragment.getFragment(
            fragmentManager = childFragmentManager,
            classLoader = requireActivity().classLoader,
            argumentBuilder = ContentProductTagArgument.Builder()
                .setAuthorType(selectedAccount.type)
                .setProductTagSource(ProductTagSource.GlobalSearch.tag)
                .setAuthorId(selectedAccount.id)
                .setShopBadge(selectedAccount.badge)
                .setMultipleSelectionProduct(true, viewModel.maxProduct)
                .setIsShowActionBarDivider(false)
        )

        childFragmentManager.beginTransaction()
            .replace(binding.containerContent.id, productPicker, ProductTagParentFragment.TAG)
            .commit()
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayUgcProductPickerBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        clearContentPadding = true
        showHeader = false
        setChild(binding.root)
    }

    private fun closeBottomSheet() {
        if (viewModel.uiState.value.saveState.isLoading) return
        mListener?.onCancelled(this)
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                getProductPickerUGCFragment()
                    ?.setLoading(it.saveState.isLoading)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when (it) {
                    PlayBroProductChooserEvent.SaveProductSuccess -> {
                        mListener?.onFinished(this@ProductPickerUGCBottomSheet)
                    }
                    is PlayBroProductChooserEvent.ShowError -> {
                        toaster.showError(
                            err = it.error,
                            customErrMessage = it.error.message,
                            bottomMargin = offsetToaster,
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getProductPickerUGCFragment(): ProductTagParentFragment? {
        return ProductTagParentFragment.findFragment(childFragmentManager)
    }

    companion object {
        private const val TAG = "PlayUGCProductPickerBottomSheet"
        private const val SHEET_HEIGHT_PERCENT = 0.9f //a bit higher than the other bottomsheet to cater for no header height

        fun get(fragmentManager: FragmentManager): ProductPickerUGCBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? ProductPickerUGCBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,

        ): ProductPickerUGCBottomSheet {
            val existing = get(fragmentManager)
            if (existing != null) return existing

            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductPickerUGCBottomSheet::class.java.name
            ) as ProductPickerUGCBottomSheet
        }
    }

    interface Listener {
        fun onCancelled(bottomSheet: ProductPickerUGCBottomSheet)
        fun onFinished(bottomSheet: ProductPickerUGCBottomSheet)
    }
}