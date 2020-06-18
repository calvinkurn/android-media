package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ViewFlipper
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.adapter.RewardSummaryAdapter
import com.tokopedia.gamification.giftbox.presentation.entities.RewardSummaryItem
import com.tokopedia.gamification.giftbox.presentation.entities.SimpleReward
import com.tokopedia.gamification.giftbox.presentation.helpers.RewardItemDecoration
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.DEFAULT
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.EXIT
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.PLAY_WITH_POINTS
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.REDIRECT
import com.tokopedia.gamification.taptap.data.entiity.RewardButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.gami_gift_result.view.*

class RewardSummaryView : FrameLayout {

    lateinit var tvSummary: AppCompatTextView
    lateinit var viewFlipper: ViewFlipper
    lateinit var rvRewards: RecyclerView
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView
    lateinit var tvMessage: AppCompatTextView
    lateinit var btnFirst: Typography
    lateinit var btnSecond: Typography
    lateinit var fmParent: FrameLayout

    lateinit var rvAdapter: RewardSummaryAdapter
    val dataList = arrayListOf<RewardSummaryItem>()

    val CONTAINER_REWARD = 0
    val CONTAINER_EMPTY = 1

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
        LayoutInflater.from(context).inflate(com.tokopedia.gamification.R.layout.view_reward_summary, this, true)
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

        viewFlipper.alpha = 0f
        llButton.alpha = 0f
    }

    fun playRewardItemAnimation() {
        val rewardItemAnimation = AnimationUtils.loadLayoutAnimation(context, com.tokopedia.gamification.R.anim.gf_box_rewards_list_item_anim)
        rvRewards.layoutAnimation = rewardItemAnimation
        rvRewards.scheduleLayoutAnimation()
        rvRewards.adapter = rvAdapter
    }

    fun setButtons(rewardButtons: List<RewardButton>?) {
        if (!rewardButtons.isNullOrEmpty()) {
            rewardButtons.forEach { rb ->
                if (rb.type == RewardButtonType.EXIT) {
                    btnSecond.text = rb.text
                    btnSecond.setOnClickListener {
                        (it.context as? AppCompatActivity)?.finish()
                        RouteManager.route(it.context, rb.applink)
                    }
                } else if (rb.type == RewardButtonType.REDIRECT) {
                    btnFirst.text = rb.text
                    btnFirst.setOnClickListener {
                        RouteManager.route(it.context, rb.applink)
                    }
                }
            }
        }
    }

    fun setRewardData(rewardSummaryItemList: List<RewardSummaryItem>) {
        if (rewardSummaryItemList.isEmpty()) {
            showEmpty()
        } else {
            setRewardSummaryData()
        }
    }

    private fun showEmpty() {
        viewFlipper.displayedChild = CONTAINER_EMPTY
        btnFirst.visibility = View.GONE

        viewFlipper.animate().alpha(1f).setDuration(300L).start()
        llButton.animate().alpha(1f).setDuration(300L).start()
    }

    private fun setRewardSummaryData() {
        viewFlipper.displayedChild = CONTAINER_REWARD
        btnFirst.visibility = View.VISIBLE

        viewFlipper.alpha = 1f
        llButton.alpha = 1f

        val items = arrayListOf<RewardSummaryItem>()
        items.apply {
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
            add(RewardSummaryItem(null, SimpleReward("", "Lorem ipsum")))
        }
        rvAdapter.dataList.addAll(items)
        rvAdapter.notifyDataSetChanged()
    }

}

@Retention(AnnotationRetention.SOURCE)
@StringDef(REDIRECT, EXIT, PLAY_WITH_POINTS, DEFAULT)
annotation class RewardButtonType {
    companion object {
        const val REDIRECT = "redirect"
        const val EXIT = "exit"
        const val PLAY_WITH_POINTS = "playwithpoints"
        const val DEFAULT = "default"
    }
}