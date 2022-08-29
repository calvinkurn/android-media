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
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class FlashSaleCampaignUpcomingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val fstTimer : TimerUnifySingle
    private val postImageLayout : ConstraintLayout
    private val fstSaleProductTitle : Typography
    private val fstReminderBtn : UnifyButton
    private var mListener : Listener? = null
    var isReminderSet = false


    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_flash_sale_card, this, true)

        fstTimer = findViewById(R.id.fst_timer)
        postImageLayout = findViewById(R.id.ribbon_image_parent)
        fstSaleProductTitle = findViewById(R.id.flash_sale_title)
        fstReminderBtn = findViewById(R.id.fst_reminder_btn)

    }
    fun setData(
        feedXCard: FeedXCard,
        positionInFeed: Int
    ) {
        //TODO Replace with Backend Values , these are for testing
        val imageUrl = "https://images.tokopedia.net/img/feeds/ribbon-overlay-rs.png"
        setRibbonBackground(imageUrl)
        setUpReminderButtonListener()
//        setBackground(feedXCard.ribbonImageURL)
    }

    fun setListener(listener : Listener){
        this.mListener = listener
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
        fstTimer.onFinish = { mListener?.onTimerFinish() }
    }
    private fun setUpReminderButtonListener(){
        fstReminderBtn.apply {
            setOnClickListener {

                mListener?.onReminderBtnClick(isReminderSet)

                if (isReminderSet) {
                    isReminderSet = false
                    text = context.getString(R.string.btn_asgc_flash_remind_btn_text)
                    buttonType = UnifyButton.Type.MAIN
                } else {
                    isReminderSet = true
                    text = context.getString(R.string.btn_asgc_flash_remind_btn_text_disabled)
                    buttonType = UnifyButton.Type.ALTERNATE
                }

            }
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
    interface Listener {
        fun onTimerFinish()
        fun onReminderBtnClick(isReminderSet: Boolean)
    }

}