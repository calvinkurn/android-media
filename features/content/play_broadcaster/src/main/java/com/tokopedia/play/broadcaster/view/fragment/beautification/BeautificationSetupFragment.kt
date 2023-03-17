package com.tokopedia.play.broadcaster.view.fragment.beautification

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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalyticStateHolder
import com.tokopedia.play.broadcaster.databinding.FragmentBeautificationSetupBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.bridge.BeautificationUiBridge
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.view.adapter.BeautificationPagerAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.unifycomponents.RangeSliderUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifyprinciples.R as unifyR
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 01, 2023
 */
class BeautificationSetupFragment @Inject constructor(
    private val viewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val beautificationUiBridge: BeautificationUiBridge,
    private val beautificationAnalytic: PlayBroadcastBeautificationAnalytic,
    private val beautificationAnalyticStateHolder: PlayBroadcastBeautificationAnalyticStateHolder,
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = TAG

    private var _binding: FragmentBeautificationSetupBinding? = null
    private val binding: FragmentBeautificationSetupBinding
        get() = _binding!!

    private var _bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private val bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
        get() = _bottomSheetBehavior!!

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when(newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    beautificationAnalytic.openScreenBeautificationBottomSheet()
                }
                BottomSheetBehavior.STATE_HIDDEN -> {
                    beautificationUiBridge.eventBus.emit(BeautificationUiBridge.Event.BeautificationBottomSheetDismissed)
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
        requireContext().resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)
    }

    private val pagerAdapter by lazyThreadSafetyNone {
        BeautificationPagerAdapter(
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
        _binding = FragmentBeautificationSetupBinding.inflate(inflater, container, false)
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

        binding.sliderBeautification.apply {
            showSeparator(true)
            rangeSliderStepSize = SLIDER_STEP_SIZE
            setShowTickMark(true)
        }

        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = pagerAdapter
        }

        TabsUnifyMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                BeautificationTabFragment.Companion.Type.FaceFilter.value -> tab.setCustomText(getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_face_tab))
                BeautificationTabFragment.Companion.Type.Preset.value -> tab.setCustomText(getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_makeup_tab))
                else -> {}
            }
        }

        binding.tabLayout.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(selectedTabIdx != tab?.position) {
                    selectedTabIdx = tab?.position ?: -1

                    beautificationAnalytic.clickBeautificationTab(
                        account = viewModel.selectedAccount,
                        page = beautificationAnalyticStateHolder.pageSource.mapToAnalytic(),
                        tab = when(selectedTabIdx) {
                            BeautificationTabFragment.Companion.Type.FaceFilter.value -> {
                                PlayBroadcastBeautificationAnalytic.Tab.FaceShaping
                            }
                            BeautificationTabFragment.Companion.Type.Preset.value -> {
                                PlayBroadcastBeautificationAnalytic.Tab.Makeup
                            }
                            else -> {
                                PlayBroadcastBeautificationAnalytic.Tab.Unknown
                            }
                        }
                    )

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
                    BeautificationTabFragment.Companion.Type.FaceFilter.value -> {
                        beautificationAnalytic.clickSliderBeautyFilter(
                            viewModel.selectedAccount,
                            beautificationAnalyticStateHolder.pageSource.mapToAnalytic(),
                            PlayBroadcastBeautificationAnalytic.Tab.FaceShaping,
                            viewModel.selectedFaceFilter?.id.orEmpty(),
                        )
                        viewModel.submitAction(PlayBroadcastAction.ChangeFaceFilterValue(p0.first))
                    }
                    BeautificationTabFragment.Companion.Type.Preset.value -> {
                        viewModel.submitAction(PlayBroadcastAction.ChangePresetValue(p0.first))
                        beautificationAnalytic.clickSliderBeautyFilter(
                            viewModel.selectedAccount,
                            beautificationAnalyticStateHolder.pageSource.mapToAnalytic(),
                            PlayBroadcastBeautificationAnalytic.Tab.Makeup,
                            viewModel.selectedPreset?.id.orEmpty(),
                        )
                    }
                    else -> {}
                }
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(bottomSheetBehaviorCallback)

        binding.tvBottomSheetAction.setOnClickListener {
            beautificationAnalytic.clickBeautyFilterReset(viewModel.selectedAccount, beautificationAnalyticStateHolder.pageSource.mapToAnalytic())

            requireContext().getDialog(
                actionType = DialogUnify.HORIZONTAL_ACTION,
                title = getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_reset_filter_confirmation_title),
                desc = getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_reset_filter_confirmation_description),
                primaryCta = getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_reset_filter_confirm),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    viewModel.submitAction(PlayBroadcastAction.ResetBeautification)
                },
                secondaryCta = getString(com.tokopedia.play.broadcaster.R.string.play_broadcaster_reset_filter_cancel),
                secondaryListener = { dialog ->
                    dialog.dismiss()
                }
            ).show()
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderSlider(it.prevValue?.beautificationConfig, it.value.beautificationConfig)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            beautificationUiBridge.eventBus.subscribe().collect { event ->
                when(event) {
                    is BeautificationUiBridge.Event.BeautificationBottomSheetDismissed -> {
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
        if(prev?.selectedFaceFilter?.id == curr.selectedFaceFilter?.id && prev?.selectedPreset?.id == curr.selectedPreset?.id) return

        setupSlider()
    }

    private fun setupSlider() {
        if (_bottomSheetBehavior?.state == BottomSheetBehavior.STATE_HIDDEN) return

        when (selectedTabIdx) {
            BeautificationTabFragment.Companion.Type.FaceFilter.value -> {
                viewModel.selectedFaceFilter?.let { faceFilter ->
                    if (faceFilter.isRemoveEffect) {
                        hideSlider()
                        return@let
                    }

                    binding.sliderBeautification.apply {
                        updateStartValue(faceFilter.minValueForSlider)
                        updateEndValue(faceFilter.maxValueForSlider)
                        showTickMarkOnRangeValues(true, faceFilter.defaultValueForSlider, faceFilter.defaultValueForSlider)
                        setInitialValue(faceFilter.valueForSlider)
                    }

                    showSlider()
                } ?: kotlin.run {
                    hideSlider()
                }
            }
            BeautificationTabFragment.Companion.Type.Preset.value -> {
                viewModel.selectedPreset?.let { preset ->
                    if (preset.isRemoveEffect) {
                        hideSlider()
                        return@let
                    }

                    binding.sliderBeautification.apply {
                        updateStartValue(preset.minValueForSlider)
                        updateEndValue(preset.maxValueForSlider)
                        showTickMarkOnRangeValues(true, preset.defaultValueForSlider, preset.defaultValueForSlider)
                        setInitialValue(preset.valueForSlider)
                    }

                    showSlider()
                } ?: kotlin.run {
                    hideSlider()
                }
            }
            else -> {}
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

        beautificationUiBridge.eventBus.emit(BeautificationUiBridge.Event.BeautificationBottomSheetDismissed)
    }

    fun showFaceSetupBottomSheet(pageSource: PageSource) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        beautificationAnalyticStateHolder.pageSource = pageSource
        beautificationUiBridge.eventBus.emit(BeautificationUiBridge.Event.BeautificationBottomSheetShown(binding.bottomSheet.height))
        setupSlider()
    }

    enum class PageSource {
        Unknown, Preparation, Live;

        fun mapToAnalytic(): PlayBroadcastBeautificationAnalytic.Page {
            return when(this) {
                Unknown -> PlayBroadcastBeautificationAnalytic.Page.Unknown
                Preparation -> PlayBroadcastBeautificationAnalytic.Page.Preparation
                Live -> PlayBroadcastBeautificationAnalytic.Page.Live
            }
        }
    }

    companion object {
        private const val SLIDER_STEP_SIZE = 10

        const val TAG = "BeautificationSetupFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): BeautificationSetupFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? BeautificationSetupFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                BeautificationSetupFragment::class.java.name,
            ) as BeautificationSetupFragment
        }
    }
}
