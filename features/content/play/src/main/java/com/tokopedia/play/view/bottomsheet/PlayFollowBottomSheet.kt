package com.tokopedia.play.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.databinding.PlayFollowBottomSheetBinding
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameAction
import com.tokopedia.play.view.uimodel.action.DismissFollowPopUp
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.FailedFollow
import com.tokopedia.play.view.uimodel.event.ShowInfoEvent
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModelFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/10/22
 */
class PlayFollowBottomSheet @Inject constructor(
    private val analytic: PlayNewAnalytic,
    factory: PlayViewModelFactory.Creator,
) : BottomSheetUnify() {

    private var _binding: PlayFollowBottomSheetBinding? = null
    private val binding: PlayFollowBottomSheetBinding
        get() = _binding!!

    private val channelId: String
        get() = requireActivity().intent?.getStringExtra(PLAY_KEY_CHANNEL_ID).orEmpty()

    private val viewModel by activityViewModels<PlayViewModel> {
        factory.create(this, channelId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSheet()
    }

    private fun setupSheet() {
        _binding = PlayFollowBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        clearContentPadding = true
        showHeader = false
        isFullpage = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent()
        observeState()
        renderPartner()
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is ShowInfoEvent -> {
                        analytic.impressToasterPopUp(
                            viewModel.channelId,
                            viewModel.channelType.value,
                            isSuccess = true
                        )
                        dismiss()
                    }
                    is FailedFollow -> {
                        analytic.impressToasterPopUp(
                            viewModel.channelId,
                            viewModel.channelType.value,
                            isSuccess = false
                        )
                        dismiss()
                    }
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { state ->
                renderFollowButton(state.value.partner)
            }
        }
    }

    private fun renderPartner() {
        analytic.impressFollowPopUp(
            viewModel.channelId,
            viewModel.channelType.value,
            viewModel.partnerType,
            viewModel.partnerId.toString()
        )

        binding.followHeader.closeListener = View.OnClickListener {
            analytic.clickDismissFollowPopUp(
                viewModel.channelId,
                viewModel.channelType.value,
                viewModel.partnerType,
                viewModel.partnerId.toString()
            )
            dismiss()
        }

        binding.followHeader.title = getString(R.string.play_follow_popup_header_title)

        val partnerInfo = viewModel.latestCompleteChannelData.partnerInfo

        binding.ivBadge.showWithCondition(partnerInfo.badgeUrl.isNotBlank())
        binding.ivIcon.showWithCondition(partnerInfo.iconUrl.isNotBlank())
        binding.ivBadge.loadIcon(partnerInfo.badgeUrl)
        binding.ivIcon.loadIcon(partnerInfo.iconUrl)
        binding.tvPartnerName.text = partnerInfo.name

        binding.tvFollowDesc.text =
            getString(R.string.play_follow)

        binding.clFollowContainer.setOnClickListener {
            analytic.clickCreatorPopUp(
                viewModel.channelId,
                viewModel.channelType.value,
                viewModel.partnerType,
                viewModel.partnerId.toString()
            )
            viewModel.submitAction(ClickPartnerNameAction(partnerInfo.appLink))
        }

        binding.btnFollow.setOnClickListener {
            analytic.clickFollowCreatorPopUp(
                viewModel.channelId,
                viewModel.channelType.value,
                viewModel.partnerType,
                viewModel.partnerId.toString()
            )
            viewModel.submitAction(PlayViewerNewAction.FollowInteractive)
        }
    }

    private fun renderFollowButton(state: PlayPartnerInfo) {
        binding.btnFollow.isLoading = state.isLoadingFollow
    }

    fun show(fragmentManager: FragmentManager) {
        if (isAdded || isVisible) return
        show(fragmentManager, TAG)
    }

    override fun dismiss() {
        super.dismiss()

        viewModel.submitAction(DismissFollowPopUp)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val TAG = "PlayFollowBottomSheet"
        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayFollowBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayFollowBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayFollowBottomSheet::class.java.name
            ) as PlayFollowBottomSheet
        }
    }
}
