package com.tokopedia.play.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.play.databinding.PlayFollowBottomSheetBinding
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/10/22
 */
class PlayFollowBottomSheet @Inject constructor() : BottomSheetUnify() {

    private var _binding: PlayFollowBottomSheetBinding? = null
    private val binding: PlayFollowBottomSheetBinding
        get() = _binding!!

    private lateinit var playViewModel: PlayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = PlayFollowBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
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


    private fun setupView() {
        binding.ivBadge.showWithCondition(playViewModel.latestCompleteChannelData.partnerInfo.badgeUrl.isNotBlank())
        binding.ivIcon.showWithCondition(playViewModel.latestCompleteChannelData.partnerInfo.iconUrl.isNotBlank())
        binding.ivBadge.loadIcon(playViewModel.latestCompleteChannelData.partnerInfo.badgeUrl)
        binding.ivIcon.loadIcon(playViewModel.latestCompleteChannelData.partnerInfo.iconUrl)
        binding.tvPartnerName.text = playViewModel.latestCompleteChannelData.partnerInfo.name

        binding.clFollowContainer.setOnClickListener {
            playViewModel.latestCompleteChannelData.partnerInfo.appLink
        }
        binding.clFollowContainer.setOnClickListener {
            playViewModel.submitAction((PlayViewerNewAction.Follow))
        }
    }

    companion object {
        const val TAG = "PlayFollowBottomSheet"
        fun getOrCreate(
            fragmentManager: FragmentManager,
        ): PlayFollowBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayFollowBottomSheet
            return oldInstance ?: PlayFollowBottomSheet()
        }
    }
}
