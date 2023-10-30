package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetRewardDetailBinding
import com.tokopedia.sellerhomecommon.presentation.model.RewardDetailUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.RewardDetailAdapter

class RewardDetailBottomSheet: BaseBottomSheet<ShcBottomSheetRewardDetailBinding>() {

    private val rewardDetail by lazy {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(REWARD_DETAIL_CACHE_ID_KEY)
            )
        }

        cacheManager?.get<RewardDetailUiModel>(REWARD_DETAIL_KEY, RewardDetailUiModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    override fun setupView() {
        setupHeader()
        setupRecyclerView()
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved || isVisible) {
            return
        }

        show(fm, TAG)
    }

    private fun initBottomSheet() {
        showKnob = true
        isFullpage = true
        showCloseIcon = false
        showHeader = false
    }

    private fun setupHeader() {
        rewardDetail?.let {
            binding?.tvRewardDetailHeaderTitle?.text = it.rewardTitle
            binding?.tvRewardDetailHeaderDescription?.text = it.rewardSubtitle
            binding?.ivRewardDetailHeader?.loadImage(it.rewardImage)
        }
    }

    private fun setupRecyclerView() {
        rewardDetail?.benefitList?.let {
            binding?.rvRewardDetailBenefit?.run {
                adapter = RewardDetailAdapter(it)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }
    }

    companion object {
        private const val TAG = "RewardDetailBottomSheet"

        private const val REWARD_DETAIL_KEY = "reward_detail"
        private const val REWARD_DETAIL_CACHE_ID_KEY = "reward_detail_cache"

        fun createInstance(context: Context,
                           rewardDetail: RewardDetailUiModel
        ): RewardDetailBottomSheet {
            SaveInstanceCacheManager(context, true).apply {
                put(
                    REWARD_DETAIL_KEY,
                    rewardDetail
                )
            }.let { cacheManager ->
                return RewardDetailBottomSheet().apply {
                    arguments = Bundle().also {
                        it.putString(REWARD_DETAIL_CACHE_ID_KEY, cacheManager.id)
                    }
                }
            }

        }
    }

}
