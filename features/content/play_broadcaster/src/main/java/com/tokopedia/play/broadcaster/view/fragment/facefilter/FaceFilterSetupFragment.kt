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
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.databinding.FragmentFaceFilterSetupBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.BeautificationConfigUiModel
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

    override fun getScreenName(): String = TAG

    private var _binding: FragmentFaceFilterSetupBinding? = null
    private val binding: FragmentFaceFilterSetupBinding
        get() = _binding!!

    private var _bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private val bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
        get() = _bottomSheetBehavior!!

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when(newState) {
                BottomSheetBehavior.STATE_HIDDEN -> {
                    viewModel.submitAction(PlayBroadcastAction.FaceFilterBottomSheetDismissed)
                }
                else -> {}
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

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

    private var selectedTabIdx: Int = 0

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
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetBehaviorCallback)

        _binding = null
        _bottomSheetBehavior = null
    }

    private fun setupView() {
        hideFaceSetupBottomSheet()

        binding.sliderBeautification.rangeSliderValueFrom = SLIDER_MIN_VALUE
        binding.sliderBeautification.rangeSliderValueTo = SLIDER_MAX_VALUE
        binding.sliderBeautification.rangeSliderStepSize = SLIDER_STEP_SIZE

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = pagerAdapter

        TabsUnifyMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                PlayBroMakeupTabFragment.Companion.Type.FaceFilter.value -> tab.setCustomText(getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_face_tab))
                PlayBroMakeupTabFragment.Companion.Type.Preset.value -> tab.setCustomText(getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_makeup_tab))
                else -> {}
            }
        }

        binding.tabLayout.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(selectedTabIdx != tab?.position) {
                    selectedTabIdx = tab?.position ?: -1

                    setupSlider()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
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
        binding.sliderBeautification.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
            override fun onSliderMove(p0: Pair<Int, Int>) {

                when(selectedTabIdx) {
                    PlayBroMakeupTabFragment.Companion.Type.FaceFilter.value -> {
                        viewModel.submitAction(PlayBroadcastAction.ChangeFaceFilterValue(p0.first))
                    }
                    PlayBroMakeupTabFragment.Companion.Type.Preset.value -> {
                        viewModel.submitAction(PlayBroadcastAction.ChangePresetValue(p0.first))
                    }
                    else -> {}
                }
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(bottomSheetBehaviorCallback)
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderSlider(it.prevValue?.beautificationConfig, it.value.beautificationConfig)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is PlayBroadcastEvent.FaceFilterBottomSheetDismissed -> {
                        hideFaceSetupBottomSheet()
                        hideSlider()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderSlider(
        prev: BeautificationConfigUiModel?,
        curr: BeautificationConfigUiModel,
    ) {
        if(prev == curr) return

        setupSlider()
    }

    private fun setupSlider() {
        val selectedFilter = when(selectedTabIdx) {
            PlayBroMakeupTabFragment.Companion.Type.FaceFilter.value -> viewModel.selectedFaceFilter
            PlayBroMakeupTabFragment.Companion.Type.Preset.value -> viewModel.selectedPreset
            else -> null
        }

        selectedFilter?.let {
            binding.sliderBeautification.updateValue((selectedFilter.value * PERCENTAGE_MULTIPLIER).toInt(), null)
            showSlider()
        }
    }

    private fun showSlider() {
        if(binding.sliderBeautification.visibility == View.VISIBLE) return

        val sliderHeight = binding.sliderBeautification.height
        val fullPageHeight = binding.root.height
        val bottomSheetHeight = binding.bottomSheet.height
        val targetY = (fullPageHeight - sliderHeight - bottomSheetHeight - offset16).toFloat()

        binding.sliderBeautification.y = targetY
        binding.sliderBeautification.show()
    }

    private fun hideSlider() {
        binding.sliderBeautification.visibility = View.INVISIBLE
    }

    private fun hideFaceSetupBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun showFaceSetupBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        viewModel.submitAction(PlayBroadcastAction.FaceFilterBottomSheetShown(binding.bottomSheet.height))
        setupSlider()
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
