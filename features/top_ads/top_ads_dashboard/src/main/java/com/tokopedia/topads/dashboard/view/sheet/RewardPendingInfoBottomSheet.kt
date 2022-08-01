package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.common.data.util.Utils.attributedText
import com.tokopedia.topads.common.data.util.Utils.fastLazy
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.TopadsRewardPendingInfoBottomSheetBinding
import com.tokopedia.topads.dashboard.view.adapter.viewholder.attributedString
import com.tokopedia.unifycomponents.BottomSheetUnify

class RewardPendingInfoBottomSheet : BottomSheetUnify() {

    private val binding: TopadsRewardPendingInfoBottomSheetBinding by fastLazy {
        TopadsRewardPendingInfoBottomSheetBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListeners()
    }

    private fun initListeners() {
        binding.btnOkay.setOnClickListener {
            dismiss()
        }
    }

    private fun initView() = with(binding) {
        txtItuDescription.attributedText(R.string.topads_credit_itu_reward_description)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        setUpChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpChildView() {
        setChild(binding.root)
        context?.resources?.getString(R.string.topads_credit_reward_pending)?.let { setTitle(it) }
        isFullpage = false
        showCloseIcon = true
    }
}