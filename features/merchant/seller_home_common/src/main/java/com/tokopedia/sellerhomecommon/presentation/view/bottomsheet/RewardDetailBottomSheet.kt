package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetRewardDetailBinding
import com.tokopedia.sellerhomecommon.presentation.model.RewardDetailUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.RewardDetailAdapter

class RewardDetailBottomSheet : BaseBottomSheet<ShcBottomSheetRewardDetailBinding>() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetRewardDetailBinding.inflate(inflater, container, false).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() {
        setTitle(context?.getString(R.string.shc_milestone_reward_detail_title).orEmpty())
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
        showHeader = true
        isDragable = true
        isHideable = true
    }

    private fun setupHeader() {
        rewardDetail?.let {
            binding?.tvRewardDetailHeaderTitle?.text = it.rewardTitle
            binding?.tvRewardDetailHeaderDescription?.text = it.rewardSubtitle
            binding?.ivRewardDetailHeader?.loadImage(it.rewardImage)
            binding?.ivRewardDetailIllustration?.loadImage(TokopediaImageUrl.SELLER_HOME_REWARD_DETAIL)
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

        fun createInstance(
            context: Context,
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
