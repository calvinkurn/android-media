package com.tokopedia.play.broadcaster.view.fragment.facefilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.databinding.FragmentFaceFilterSetupBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.FaceFilterUiModel
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroFaceFilterSetupBottomSheet
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.unifycomponents.RangeSliderUnify
import com.tokopedia.unifyprinciples.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 01, 2023
 */
class FaceFilterSetupFragment @Inject constructor(
    private val viewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String {
        return "FaceFilterSetupFragment"
    }

    private var _binding: FragmentFaceFilterSetupBinding? = null
    private val binding: FragmentFaceFilterSetupBinding
        get() = _binding!!

    private val viewModel: PlayBroadcastViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            requireActivity()
        )
    }

    private val offset16 by lazyThreadSafetyNone {
        requireContext().resources.getDimensionPixelOffset(R.dimen.spacing_lvl4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openFaceFilterBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceFilterSetupBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.sliderFaceFilter.rangeSliderValueFrom = 0
        binding.sliderFaceFilter.rangeSliderValueTo = 100
        binding.sliderFaceFilter.rangeSliderStepSize = 10
    }

    private fun setupListener() {
        binding.sliderFaceFilter.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
            override fun onSliderMove(p0: Pair<Int, Int>) {
                viewModel.submitAction(PlayBroadcastAction.ChangeFaceFilterValue(p0.first))
            }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderSlider(it.prevValue?.faceFilter, it.value.faceFilter)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is PlayBroadcastEvent.FaceFilterBottomSheetShown -> {
                        val fullPageHeight = binding.root.height
                        val bottomSheetHeight = event.bottomSheetHeight

                        showSliderFaceFilter(bottomSheetHeight, fullPageHeight)
                    }
                    is PlayBroadcastEvent.FaceFilterBottomSheetDismissed -> {
                        hideSliderFaceFilter()
                    }
                }
            }
        }
    }

    private fun renderSlider(
        prev: List<FaceFilterUiModel>?,
        curr: List<FaceFilterUiModel>,
    ) {
        if(prev == curr || binding.sliderFaceFilter.visibility != View.VISIBLE) return

        val prevSelectedFaceFilter = prev?.firstOrNull { it.isSelected }
        val selectedFaceFilter = curr.firstOrNull { it.isSelected } ?: return

        if(prevSelectedFaceFilter == selectedFaceFilter) return

        binding.sliderFaceFilter.updateValue((selectedFaceFilter.value * 100).toInt(), null)
    }

    private fun showSliderFaceFilter(bottomSheetHeight: Int, fullPageHeight: Int) {
        binding.sliderFaceFilter.doOnApplyWindowInsets { v, insets, _, margin ->
            val systemWindowInsetBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            val targetY = fullPageHeight - v.height - bottomSheetHeight.toFloat() - systemWindowInsetBottom - offset16

            binding.sliderFaceFilter.y = targetY
            binding.sliderFaceFilter.show()
        }
    }

    private fun hideSliderFaceFilter() {
        binding.sliderFaceFilter.visibility = View.INVISIBLE
    }

    private fun openFaceFilterBottomSheet() {
        PlayBroFaceFilterSetupBottomSheet.getFragment(
            childFragmentManager,
            requireContext().classLoader,
        ).show(childFragmentManager)
    }
}
