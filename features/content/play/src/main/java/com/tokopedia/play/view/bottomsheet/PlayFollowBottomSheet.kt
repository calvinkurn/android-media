package com.tokopedia.play.view.bottomsheet

import android.content.res.Configuration
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.databinding.PlayFollowBottomSheetBinding
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameAction
import com.tokopedia.play.view.uimodel.action.DismissFollowPopUp
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.FailedFollow
import com.tokopedia.play.view.uimodel.event.ShowInfoEvent
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/10/22
 */
@Suppress("LateinitUsage")
class PlayFollowBottomSheet @Inject constructor(private val analytic: PlayNewAnalytic) : BottomSheetUnify() {

    private var _binding: PlayFollowBottomSheetBinding? = null
    private val binding: PlayFollowBottomSheetBinding
        get() = _binding!!

    private lateinit var playViewModel: PlayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSheet()
    }

    private fun setupSheet () {
        _binding = PlayFollowBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        clearContentPadding = true
        showHeader = false
        isFullpage = false

        val grandParentFragment = ((requireParentFragment() as? PlayUserInteractionFragment)?.parentFragment) as PlayFragment
        playViewModel = ViewModelProvider(
            grandParentFragment, grandParentFragment.viewModelProviderFactory
        ).get(PlayViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeEvent()
        observeState()
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiEvent.collect { event ->
                when (event) {
                    is ShowInfoEvent -> {
                        analytic.impressToasterPopUp(playViewModel.channelId, playViewModel.channelType.value, isSuccess = true)
                        dismiss()
                    }
                    is FailedFollow -> {
                        analytic.impressToasterPopUp(playViewModel.channelId, playViewModel.channelType.value, isSuccess = false)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest  { state ->
                renderFollowButton(state.value.partner)
            }
        }
    }

    private fun setupView() {
        analytic.impressFollowPopUp(playViewModel.channelId, playViewModel.channelType.value, playViewModel.partnerType, playViewModel.partnerId.toString())

        binding.followHeader.closeListener = View.OnClickListener {
            analytic.clickDismissFollowPopUp(
                playViewModel.channelId,
                playViewModel.channelType.value,
                playViewModel.partnerType,
                playViewModel.partnerId.toString()
            )
            dismiss()
        }

        binding.followHeader.title = getString(R.string.play_follow_popup_header_title)

        val partnerInfo = playViewModel.latestCompleteChannelData.partnerInfo

        binding.ivBadge.showWithCondition(partnerInfo.badgeUrl.isNotBlank())
        binding.ivIcon.showWithCondition(partnerInfo.iconUrl.isNotBlank())
        binding.ivBadge.loadIcon(partnerInfo.badgeUrl)
        binding.ivIcon.loadIcon(partnerInfo.iconUrl)
        binding.tvPartnerName.text = partnerInfo.name

        binding.tvFollowDesc.text = playViewModel.latestCompleteChannelData.channelDetail.popupConfig.text

        binding.clFollowContainer.setOnClickListener {
            analytic.clickCreatorPopUp(playViewModel.channelId, playViewModel.channelType.value, playViewModel.partnerType, playViewModel.partnerId.toString())
            playViewModel.submitAction(ClickPartnerNameAction(partnerInfo.appLink))
        }

        binding.btnFollow.setOnClickListener {
            analytic.clickFollowCreatorPopUp(playViewModel.channelId, playViewModel.channelType.value, playViewModel.partnerType, playViewModel.partnerId.toString())
            playViewModel.submitAction(PlayViewerNewAction.FollowInteractive)
        }
    }

    private fun renderFollowButton(state: PlayPartnerInfo) {
        binding.btnFollow.isLoading = state.isLoadingFollow
    }

    fun show(fragmentManager: FragmentManager){
        if(isAdded || isVisible) return
        show(fragmentManager, TAG)
    }

    override fun dismiss() {
        if(!isVisible) return
        super.dismiss()

        playViewModel.submitAction(DismissFollowPopUp)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        playViewModel.submitAction(DismissFollowPopUp)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dismiss()
    }

    companion object {
        private const val TAG = "PlayFollowBottomSheet"
        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayFollowBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayFollowBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(classLoader, PlayFollowBottomSheet::class.java.name) as PlayFollowBottomSheet
        }
    }
}
