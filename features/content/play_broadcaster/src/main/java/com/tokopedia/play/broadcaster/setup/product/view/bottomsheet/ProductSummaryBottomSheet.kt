package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductSummaryBinding
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductSummaryListViewComponent
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
class ProductSummaryBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dialogCustomizer: PlayBroadcastDialogCustomizer,
) : BottomSheetUnify(), ProductSummaryListViewComponent.Listener {

    private lateinit var viewModel: PlayBroProductSetupViewModel

    private var _binding: BottomSheetPlayBroProductSummaryBinding? = null
    private val binding: BottomSheetPlayBroProductSummaryBinding
        get() = _binding!!

    private val productSummaryListView by viewComponent {
        ProductSummaryListViewComponent(binding.rvProductSummaries, this)
    }

    override fun onProductDeleteClicked(product: ProductUiModel) {
        TODO("Not yet implemented")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment(), viewModelFactory)
            .get(PlayBroProductSetupViewModel::class.java)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()

        productSummaryListView.setProductList(listOf())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroProductSummaryBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        clearContentPadding = true
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.85f).toInt()
        }
        /** TODO: change this later */
        setTitle("Produk terpilih (5/30)")
        setAction(getString(R.string.play_bro_product_add_more)) {
            /** TODO: handle logic here later */
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
            }
        }
    }

    companion object {
        private const val TAG = "ProductSummaryBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ProductSummaryBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductSummaryBottomSheet
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    ProductSummaryBottomSheet::class.java.name
                ) as ProductSummaryBottomSheet
            }
        }
    }
}