package com.tokopedia.play.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.play.R
import com.tokopedia.play.databinding.PlayFollowBottomSheetBinding
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameAction
import com.tokopedia.play.view.uimodel.action.DismissFollowPopUp
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.databinding.BottomSheetHeaderBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/10/22
 */
class PlayFollowBottomSheet @Inject constructor() : BottomSheetUnify() {

    private var _binding: PlayFollowBottomSheetBinding? = null
    private val binding: PlayFollowBottomSheetBinding
        get() = _binding!!

    private var _headerBinding: BottomSheetHeaderBinding? = null
    private val headerBinding: BottomSheetHeaderBinding
        get() = _headerBinding!!

    private lateinit var playViewModel: PlayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSheet()
    }

    private fun setupSheet () {
        _binding = PlayFollowBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        _headerBinding = BottomSheetHeaderBinding.bind(binding.root)
        setChild(binding.root)

        clearContentPadding = true
        showHeader = false
        isFullpage = false

        if(requireParentFragment() is PlayUserInteractionFragment){
            val grandParentActivity = ((requireParentFragment() as PlayUserInteractionFragment).parentFragment) as PlayFragment

            playViewModel = ViewModelProvider(
                grandParentActivity, grandParentActivity.viewModelProviderFactory
            ).get(PlayViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    fun setupView() {
        headerBinding.ivSheetClose.setOnClickListener {
            dismiss()
        }

        headerBinding.tvSheetTitle.text = getString(R.string.play_follow_popup_header_title)

        val partnerInfo = playViewModel.latestCompleteChannelData.partnerInfo

        binding.ivBadge.showWithCondition(partnerInfo.badgeUrl.isNotBlank())
        binding.ivIcon.showWithCondition(partnerInfo.iconUrl.isNotBlank())
        binding.ivBadge.loadIcon(partnerInfo.badgeUrl)
        binding.ivIcon.loadIcon(partnerInfo.iconUrl)
        binding.tvPartnerName.text = partnerInfo.name

        binding.tvFollowDesc.text = playViewModel.latestCompleteChannelData.channelDetail.popupConfig.text

        binding.clFollowContainer.setOnClickListener {
            playViewModel.submitAction(ClickPartnerNameAction(partnerInfo.appLink))
        }

        binding.btnFollow.setOnClickListener {
            playViewModel.submitAction(PlayViewerNewAction.Follow)
        }
    }

    fun show(fragmentManager: FragmentManager){
        if(isAdded || isVisible) return
        show(fragmentManager, TAG)
    }

    override fun dismiss() {
        super.dismiss()

        playViewModel.submitAction(DismissFollowPopUp)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        _headerBinding = null
    }

    companion object {
        private const val TAG = "PlayFollowBottomSheet"
        fun getOrCreate(
            fragmentManager: FragmentManager,
        ): PlayFollowBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayFollowBottomSheet
            return oldInstance ?: PlayFollowBottomSheet()
        }
    }
}
