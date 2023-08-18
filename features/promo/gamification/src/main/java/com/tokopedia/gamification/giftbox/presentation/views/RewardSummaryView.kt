package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmGiftTapTap
import com.tokopedia.gamification.giftbox.presentation.adapter.RewardSummaryAdapter
import com.tokopedia.gamification.giftbox.presentation.entities.RewardSummaryItem
import com.tokopedia.gamification.giftbox.presentation.helpers.RewardItemDecoration
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.DEFAULT
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.EXIT
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.PLAY_WITH_POINTS
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtonType.Companion.REDIRECT
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtons.Companion.GREEN
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtons.Companion.ORANGE
import com.tokopedia.gamification.giftbox.presentation.views.RewardButtons.Companion.OUTLINE
import com.tokopedia.gamification.taptap.data.entiity.RewardButton
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.image.ImageUtils

class RewardSummaryView : FrameLayout {

    lateinit var tvSummary: AppCompatTextView
    lateinit var viewFlipper: ViewFlipper
    lateinit var rvRewards: RecyclerView
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView
    lateinit var tvMessage: AppCompatTextView
    lateinit var imageBox: AppCompatImageView
    private var llButton: LinearLayout? = null

    lateinit var rvAdapter: RewardSummaryAdapter
    lateinit var decoration: RewardItemDecoration
    val dataList = arrayListOf<RewardSummaryItem>()
    val buttonsMap = HashMap<String, Typography>()

    val CONTAINER_REWARD = 0
    val CONTAINER_EMPTY = 1

    var imageBoxUrl: String? = null

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
        imageBox = findViewById(R.id.imageBox)
        llButton = findViewById(R.id.llButton)

        rvAdapter = RewardSummaryAdapter(dataList)

        rvRewards.layoutManager = LinearLayoutManager(context)
        val smallPadding = rvRewards.context.resources.getDimension(R.dimen.gami_reward_item_padding_small).toInt()
        val medPadding = rvRewards.context.resources.getDimension(R.dimen.gami_reward_item_padding_med).toInt()
        val largePadding = rvRewards.context.resources.getDimension(R.dimen.gami_reward_item_padding_large).toInt()
        decoration = RewardItemDecoration(smallPadding, medPadding, largePadding)
        rvRewards.addItemDecoration(decoration)

        viewFlipper.alpha = 0f
        llButton?.alpha = 0f
    }

    fun playRewardItemAnimation() {
        val rewardItemAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.gf_box_rewards_list_item_anim)
        rvRewards.layoutAnimation = rewardItemAnimation
        rvRewards.scheduleLayoutAnimation()
        rvRewards.adapter = rvAdapter
    }

    fun setButtons(rewardButtons: List<RewardButton>?) {
        val userSession = UserSession(context)
        if (!rewardButtons.isNullOrEmpty()) {
            rewardButtons.forEachIndexed { index, rb ->
                if (rb.backgroundColor != null) {
                    val button = getRewardButton(rb.backgroundColor)

                    if (button != null) {
                        llButton?.addView(button)

                        button.text = rb.text
                        button.setOnClickListener {
                            if (rb.type == RewardButtonType.EXIT) {
                                (it.context as? AppCompatActivity)?.finish()

                                if (dataList.isEmpty()) {
                                    GtmGiftTapTap.clickExitNoReward(userSession.userId)
                                } else {
                                    GtmGiftTapTap.clickExitButtonReward(userSession.userId)
                                }
                            } else {
                                GtmGiftTapTap.clickCheckRewards(userSession.userId)
                            }
                            RouteManager.route(it.context, rb.applink)
                        }

                        if (index == 0) {
                            (button.layoutParams as LinearLayout.LayoutParams).rightMargin = context.resources.getDimensionPixelSize(R.dimen.gami_dp_12)
                        }

                        if (rb.type != null) {
                            buttonsMap[rb.type] = button
                        }
                    }
                }
            }
        }
    }

    fun setRewardData(rewardSummaryItemList: List<RewardSummaryItem>) {
        if (!imageBoxUrl.isNullOrEmpty()) {
            ImageUtils.loadImageWithoutPlaceholderAndError(imageBox, imageBoxUrl!!)
        }

        val userSession = UserSession(context)

        if (rewardSummaryItemList.isEmpty()) {
            showEmpty()
            GtmGiftTapTap.viewNoRewardsPage(userSession.userId)
        } else {
            setRewardSummaryData(rewardSummaryItemList)
            GtmGiftTapTap.viewRewardSummary(userSession.userId)
        }
    }

    private fun showEmpty() {
        viewFlipper.displayedChild = CONTAINER_EMPTY
        buttonsMap.filter { it.key != RewardButtonType.EXIT }.forEach { it.value.gone() }

        viewFlipper.animate().alpha(1f).setDuration(300L).start()
        llButton?.animate()?.alpha(1f)?.setDuration(300L)?.start()
    }

    private fun setRewardSummaryData(rewardSummaryItemList: List<RewardSummaryItem>) {
        viewFlipper.displayedChild = CONTAINER_REWARD
        buttonsMap.forEach { it.value.visible() }

        viewFlipper.alpha = 1f
        llButton?.alpha = 1f

        val list = mutableListOf<RewardSummaryItem>()
        list.addAll(rewardSummaryItemList)

        val filteredItems = rewardSummaryItemList.filter { it.benfit.isBigPrize }
        list.removeAll(filteredItems)
        list.addAll(0, filteredItems)

        if (filteredItems.isNotEmpty()) {
            decoration.indexTillBigPrize = filteredItems.size - 1
        }

        rvAdapter.dataList.clear()
        rvAdapter.dataList.addAll(list)
        rvAdapter.notifyDataSetChanged()
    }

    fun getRewardButton(@RewardButtons rewardButtonType: String): Typography? {
        val typography = Typography(context)
        val heightLarge = context.resources.getDimensionPixelSize(R.dimen.gami_dp_43)
        val heightSmall = context.resources.getDimensionPixelSize(R.dimen.gami_dp_40)

        when (rewardButtonType) {
            ORANGE -> {
                val lp = LinearLayout.LayoutParams(0, heightLarge)
                lp.gravity = Gravity.CENTER
                lp.weight = 1f

                typography.layoutParams = lp
                typography.background = ContextCompat.getDrawable(context, R.drawable.gf_bg_orange_3d)
                typography.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            }
            GREEN -> {
                val lp = LinearLayout.LayoutParams(0, heightLarge)
                lp.gravity = Gravity.CENTER
                lp.weight = 1f

                typography.layoutParams = lp
                typography.background = ContextCompat.getDrawable(context, R.drawable.gf_bg_green_3d)
                typography.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            }
            OUTLINE -> {
                val lp = LinearLayout.LayoutParams(0, heightSmall)
                lp.gravity = Gravity.CENTER
                lp.weight = 1f

                typography.layoutParams = lp
                typography.background = ContextCompat.getDrawable(context, R.drawable.gami_grey_bg)
            }
            else -> {
                return null
            }
        }

        typography.gravity = Gravity.CENTER
        typography.setType(Typography.HEADING_5)
        typography.setWeight(Typography.BOLD)
        return typography
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

@Retention(AnnotationRetention.SOURCE)
@StringDef(ORANGE, OUTLINE, GREEN)
annotation class RewardButtons {
    companion object {
        const val ORANGE = "orange"
        const val OUTLINE = "outline"
        const val GREEN = "green"
    }
}
