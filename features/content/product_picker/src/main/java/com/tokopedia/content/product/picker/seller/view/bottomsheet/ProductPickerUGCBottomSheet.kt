package com.tokopedia.content.product.picker.seller.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductChooserEvent
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.model.PriceUnknown
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.content.product.picker.databinding.BottomSheetUgcProductPickerBinding
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 29/08/22
 */
class ProductPickerUGCBottomSheet @Inject constructor(
    private val dialogCustomizer: ContentDialogCustomizer,
    private val analytic: ContentProductTagAnalytic,
) : BaseProductSetupBottomSheet() {

    private val offsetToaster by lazy { context?.resources?.getDimensionPixelOffset(R.dimen.content_product_picker_50_dp) ?: 0 }

    private var _binding: BottomSheetUgcProductPickerBinding? = null
    private val binding: BottomSheetUgcProductPickerBinding
        get() = _binding!!

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
                            hasCommission = false,
                            commissionFmt = "",
                            commission = 0L,
                            extraCommission = false,
                            pinStatus = PinProductUiModel.Empty,
                            number = "",
                        )
                    }
                )
            )

            viewModel.submitAction(ProductSetupAction.SaveProducts)
        }

        override fun onMaxSelectedProductReached() {
            toaster.showToaster(
                message = getString(R.string.max_selected_product_reached).format(viewModel.maxProduct),
                actionLabel = getString(R.string.content_product_picker_ok),
                actionListener = { toaster.dismissToaster() },
                bottomMargin = offsetToaster
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

    private var mDataSource: DataSource? = null

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

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
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

        val selectedAccount = mDataSource?.getSelectedAccount()

        val productPicker = ProductTagParentFragment.getFragment(
            fragmentManager = childFragmentManager,
            classLoader = requireActivity().classLoader,
            argumentBuilder = ContentProductTagArgument.Builder()
                .setAuthorType(selectedAccount?.type.orEmpty())
                .setProductTagSource(ProductTagSource.GlobalSearch.tag)
                .setAuthorId(selectedAccount?.id.orEmpty())
                .setShopBadge(mDataSource?.getShopBadgeIfAny().orEmpty())
                .setMultipleSelectionProduct(true, viewModel.maxProduct)
                .setIsShowActionBarDivider(false)
        )

        childFragmentManager.beginTransaction()
            .replace(binding.containerContent.id, productPicker, ProductTagParentFragment.TAG)
            .commit()
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetUgcProductPickerBinding.inflate(
            LayoutInflater.from(requireContext())
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
                    ProductChooserEvent.SaveProductSuccess -> {
                        mListener?.onFinished(this@ProductPickerUGCBottomSheet)
                    }
                    is ProductChooserEvent.ShowError -> {
                        toaster.showError(
                            err = it.error,
                            customErrMessage = it.error.message,
                            bottomMargin = offsetToaster
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
        private const val SHEET_HEIGHT_PERCENT = 0.9f // a bit higher than the other bottomsheet to cater for no header height

        fun get(fragmentManager: FragmentManager): ProductPickerUGCBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? ProductPickerUGCBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader

        ): ProductPickerUGCBottomSheet {
            val existing = get(fragmentManager)
            if (existing != null) return existing

            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductPickerUGCBottomSheet::class.java.name
            ) as ProductPickerUGCBottomSheet
        }
    }

    interface DataSource {
        fun getSelectedAccount(): ContentAccountUiModel
        fun getShopBadgeIfAny(): String
    }

    interface Listener {
        fun onCancelled(bottomSheet: ProductPickerUGCBottomSheet)
        fun onFinished(bottomSheet: ProductPickerUGCBottomSheet)
    }
}
