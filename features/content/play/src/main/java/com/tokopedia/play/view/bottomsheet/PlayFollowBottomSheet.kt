package com.tokopedia.play.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.play.R
import com.tokopedia.play.databinding.PlayFollowBottomSheetBinding
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameAction
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.databinding.BottomSheetHeaderBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
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

        setupView()
    }

    private fun setupView () {
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

        bindView()
        observeState()
    }

    private fun observeState () {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                if(prevState?.followPopUp != state.followPopUp) setupView(description = state.followPopUp.popupConfig.text, partnerInfo = state.partner)
            }
        }
    }

    private fun bindView (){
        headerBinding.ivSheetClose.setOnClickListener {
            dismiss()
        }

        headerBinding.tvSheetTitle.text = getString(R.string.play_follow_popup_header_title)
    }

    fun setupView(partnerInfo: PlayPartnerInfo, description: String) {
        binding.ivBadge.showWithCondition(partnerInfo.badgeUrl.isNotBlank())
        binding.ivIcon.showWithCondition(partnerInfo.iconUrl.isNotBlank())
        binding.ivBadge.loadIcon(partnerInfo.badgeUrl)
        binding.ivIcon.loadIcon(partnerInfo.iconUrl)
        binding.tvPartnerName.text = partnerInfo.name
        binding.tvFollowDesc.text = description

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
