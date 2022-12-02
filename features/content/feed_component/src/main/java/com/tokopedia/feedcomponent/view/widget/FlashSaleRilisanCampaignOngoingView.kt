package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.widget.listener.FeedCampaignListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import kotlin.math.roundToInt

/**
 * @author by shruti on 01/09/22
 */

class FlashSaleRilisanCampaignOngoingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val fstTimer: TimerUnifySingle
    private val postImageLayout: ConstraintLayout
    private val fstSaleProductTitle: Typography
    private val stockText: Typography
    private val stockProgressBar: ProgressBarUnify
    private var mListener: FeedCampaignListener? = null
    private var mPostionInFeed: Int = 0
    private var mFeedXCard: FeedXCard? = null
    private var mFeedXProduct: FeedXProduct? = null

    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
        LayoutInflater.from(context)
                .inflate(R.layout.item_flash_sale_rs_ongoing_card, this, true)

        fstTimer = findViewById(R.id.fst_ongoing_timer)
        postImageLayout = findViewById(R.id.ribbon_image_parent)
        fstSaleProductTitle = findViewById(R.id.flash_sale_ongoing_title)
        stockText = findViewById(R.id.stock_text)
        stockProgressBar = findViewById(R.id.ongoing_progress_bar)
    }

    fun setData(
        feedXCard: FeedXCard,
        positionInFeed: Int,
        media: FeedXMedia
    ) {
        mPostionInFeed = positionInFeed
        mFeedXCard = feedXCard
        mFeedXProduct = media.tagProducts.firstOrNull()
        setStockBarProgressAndText()
        setRibbonBackground(feedXCard.ribbonImageURL)
        fstSaleProductTitle.text = feedXCard.campaign.shortName
    }

    fun  setListener(listener : FeedCampaignListener){
        this.mListener = listener
    }

    private fun setStockBarProgressAndText() {
        val value = (((mFeedXProduct?.stockSoldPercentage ?: 0.75f) * 100) / 100).roundToInt()
        stockProgressBar.setValue(value, true)
        stockText.text = mFeedXProduct?.stockWording
        setGradientColorForProgressBar()
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
    private fun setGradientColorForProgressBar(){
        val progressBarColor: IntArray = intArrayOf(
            MethodChecker.getColor(context, com.tokopedia.feedcomponent.R.color.feed_dms_asgc_progress_0_color),
            MethodChecker.getColor(context, com.tokopedia.feedcomponent.R.color.feed_dms_asgc_progress_100_color)
        )
        stockProgressBar.progressBarColor = progressBarColor

    }


    private fun setRibbonBackground(backgroundUrl: String) {
        Glide.with(context).load(backgroundUrl).into(object : CustomTarget<Drawable?>() {
            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                postImageLayout.background = resource
            }
        })
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        mListener = null
    }
}
