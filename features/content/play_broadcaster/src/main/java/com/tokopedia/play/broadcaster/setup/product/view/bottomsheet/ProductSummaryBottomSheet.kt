package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductSummaryBinding
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
class ProductSummaryBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : BaseProductSetupBottomSheet() {

    private var _binding: BottomSheetPlayBroProductSummaryBinding? = null
    private val binding: BottomSheetPlayBroProductSummaryBinding
        get() = _binding!!

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
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.85f).toInt()
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