package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ViewFlipper
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.adapter.RewardSummaryAdapter
import com.tokopedia.gamification.giftbox.presentation.entities.RewardSummaryItem
import com.tokopedia.gamification.giftbox.presentation.helpers.RewardItemDecoration
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class RewardSummaryView : FrameLayout {

    lateinit var tvSummary: AppCompatTextView
    lateinit var viewFlipper: ViewFlipper
    lateinit var rvRewards: RecyclerView
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView
    lateinit var tvMessage: AppCompatTextView
    lateinit var btnFirst: Typography
    lateinit var btnSecond: UnifyButton
    lateinit var fmParent: FrameLayout

    lateinit var rvAdapter: RewardSummaryAdapter
    val dataList = arrayListOf<RewardSummaryItem>()

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_reward_summary, this, true)
        tvSummary = findViewById(R.id.tvSummary)
        viewFlipper = findViewById(R.id.viewFlipper)
        rvRewards = findViewById(R.id.rvRewards)
        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
        tvMessage = findViewById(R.id.tvMessage)
        btnFirst = findViewById(R.id.btnFirst)
        btnSecond = findViewById(R.id.btnSecond)
        fmParent = findViewById(R.id.fmParent)

        rvAdapter = RewardSummaryAdapter(dataList)

        rvRewards.layoutManager = LinearLayoutManager(context)
        rvRewards.addItemDecoration(RewardItemDecoration(rvRewards.dpToPx(8).toInt(), rvRewards.dpToPx(12).toInt()))
    }

    fun playRewardItemAnimation() {
        val rewardItemAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.gf_box_rewards_list_item_anim)
        rvRewards.layoutAnimation = rewardItemAnimation
        rvRewards.scheduleLayoutAnimation()
        rvRewards.adapter = rvAdapter
    }

}