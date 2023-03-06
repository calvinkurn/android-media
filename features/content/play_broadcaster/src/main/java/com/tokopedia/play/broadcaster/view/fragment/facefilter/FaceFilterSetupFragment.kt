package com.tokopedia.play.broadcaster.view.fragment.facefilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.databinding.FragmentFaceFilterSetupBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.FaceFilterUiModel
import com.tokopedia.play.broadcaster.view.adapter.FaceFilterPagerAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.unifycomponents.RangeSliderUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
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

    private var _bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private val bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
        get() = _bottomSheetBehavior!!

    private val viewModel: PlayBroadcastViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            requireActivity()
        )
    }

    private val offset16 by lazyThreadSafetyNone {
        requireContext().resources.getDimensionPixelOffset(R.dimen.spacing_lvl4)
    }

    private val pagerAdapter by lazyThreadSafetyNone {
        FaceFilterPagerAdapter(
            childFragmentManager,
            requireContext().classLoader,
            lifecycle
        )
    }

    val isBottomSheetShown: Boolean
        get() = _bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceFilterSetupBinding.inflate(inflater, container, false)
        _bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupInsets()
        setupListener()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomSheetBehavior = null
    }

    private fun setupView() {
        hideFaceSetupBottomSheet()

        binding.sliderFaceFilter.rangeSliderValueFrom = SLIDER_MIN_VALUE
        binding.sliderFaceFilter.rangeSliderValueTo = SLIDER_MAX_VALUE
        binding.sliderFaceFilter.rangeSliderStepSize = SLIDER_STEP_SIZE

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = pagerAdapter

        TabsUnifyMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.setCustomText(getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_face_tab))
                1 -> tab.setCustomText(getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_makeup_tab))
                else -> {}
            }
        }
    }

    private fun setupInsets() {
        binding.viewPager.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setupListener() {
        binding.sliderFaceFilter.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
            override fun onSliderMove(p0: Pair<Int, Int>) {
                viewModel.submitAction(PlayBroadcastAction.ChangeFaceFilterValue(p0.first))
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        val bottomSheetHeight = bottomSheet.height
                        viewModel.submitAction(PlayBroadcastAction.FaceFilterBottomSheetShown(bottomSheetHeight))

                        viewModel.selectedFaceFilter?.let {
                            setupFaceFilterSlider(it)
                        }
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        viewModel.submitAction(PlayBroadcastAction.FaceFilterBottomSheetDismissed)
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
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
                    is PlayBroadcastEvent.FaceFilterBottomSheetDismissed -> {
                        hideFaceSetupBottomSheet()
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
        if(prev == curr) return

        val prevSelectedFaceFilter = prev?.firstOrNull { it.isSelected }
        val selectedFaceFilter = curr.firstOrNull { it.isSelected } ?: return

        if(prevSelectedFaceFilter == selectedFaceFilter) return

        setupFaceFilterSlider(selectedFaceFilter)
    }

    private fun setupFaceFilterSlider(selectedFaceFilter: FaceFilterUiModel) {
        binding.sliderFaceFilter.updateValue((selectedFaceFilter.value * PERCENTAGE_MULTIPLIER).toInt(), null)
        showSliderFaceFilter()
    }

    private fun showSliderFaceFilter() {
        if(binding.sliderFaceFilter.visibility == View.VISIBLE) return

        val sliderHeight = binding.sliderFaceFilter.height
        val fullPageHeight = binding.root.height
        val bottomSheetHeight = binding.bottomSheet.height
        val targetY = (fullPageHeight - sliderHeight - bottomSheetHeight - offset16).toFloat()

        binding.sliderFaceFilter.y = targetY
        binding.sliderFaceFilter.show()
    }

    private fun hideSliderFaceFilter() {
        binding.sliderFaceFilter.visibility = View.INVISIBLE
    }

    private fun hideFaceSetupBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun showFaceSetupBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    companion object {

        private const val SLIDER_MIN_VALUE = 0
        private const val SLIDER_MAX_VALUE = 100
        private const val SLIDER_STEP_SIZE = 10
        private const val PERCENTAGE_MULTIPLIER = 100

        const val TAG = "FaceFilterSetupFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): FaceFilterSetupFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FaceFilterSetupFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FaceFilterSetupFragment::class.java.name,
            ) as FaceFilterSetupFragment
        }
    }
}
