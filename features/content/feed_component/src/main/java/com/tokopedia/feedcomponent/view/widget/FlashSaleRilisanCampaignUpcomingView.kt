package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.widget.listener.FeedCampaignListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class FlashSaleRilisanCampaignUpcomingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val fstTimer: TimerUnifySingle
    private val postImageLayout: ConstraintLayout
    private val fstSaleProductTitle: Typography
    private val fstReminderBtn: UnifyButton
    private var mListener: FeedCampaignListener? = null
    var isReminderSet = false
    private var mPostionInFeed: Int = 0
    private var mFeedXCard: FeedXCard? = null

    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
        LayoutInflater.from(context).inflate(R.layout.item_flash_sale_rs_upcoming_card, this, true)

        fstTimer = findViewById(R.id.fst_timer)
        postImageLayout = findViewById(R.id.ribbon_image_parent)
        fstSaleProductTitle = findViewById(R.id.flash_sale_title)
        fstReminderBtn = findViewById(R.id.fst_reminder_btn)

    }
    fun setData(
        feedXCard: FeedXCard,
        positionInFeed: Int
    ) {
        mPostionInFeed = positionInFeed
        mFeedXCard = feedXCard
        setRibbonBackground(feedXCard.ribbonImageURL)
        setUpReminderButtonListener()
        fstSaleProductTitle.text = feedXCard.campaign.shortName
    }

    fun  setListener(listener : FeedCampaignListener){
        this.mListener = listener
    }

    fun setReminderBtnState(reminderStatus: FeedASGCUpcomingReminderStatus, positionInFeed: Int) {
        fstReminderBtn.apply {
            if (reminderStatus == FeedASGCUpcomingReminderStatus.On(
                    mFeedXCard?.campaign?.campaignId ?: 0
                )
            ) {
                text = context.getString(R.string.btn_asgc_flash_remind_btn_text_disabled)
                buttonType = UnifyButton.Type.ALTERNATE
            } else {
                text = context.getString(R.string.btn_asgc_flash_remind_btn_text)
                buttonType = UnifyButton.Type.MAIN

            }
        }
    }


    fun setupTimer(startTime: String, onFinished: () -> Unit) {
        val targetCalendar = TimeConverter.convertToCalendar(startTime)
        targetCalendar?.let { target ->
            if (target.timeInMillis > Calendar.getInstance().timeInMillis)
                fstTimer.apply {
                    targetDate = target
                    onFinish = onFinished
                    show()
                }
        }?: fstTimer.hide()
    }

    private fun setUpReminderButtonListener(){
        fstReminderBtn.setOnClickListener {
            mListener?.onReminderBtnClick(isReminderSet,mPostionInFeed )
        }
    }

    private fun setRibbonBackground(backgroundUrl: String) {
        Glide.with(context).load(backgroundUrl).into(object : CustomTarget<Drawable?>() {
            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                postImageLayout.background = resource
            }
        })
    }
}
