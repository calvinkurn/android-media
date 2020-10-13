package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage

/**
 * Created by jegul on 06/10/20
 */
class PlayWidgetCardChannelSmallView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val flBorder: FrameLayout
    private val ivCover: ImageView
    private val ivDiscount: ImageView
    private val clTotalView: ConstraintLayout
    private val tvTotalView: TextView
    private val tvTitle: TextView
    private val tvUpcoming: TextView
    private val ivLiveBadge: ImageView

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_channel_small, this)
        flBorder = view.findViewById(R.id.fl_border)
        ivCover = view.findViewById(R.id.iv_cover)
        ivDiscount = view.findViewById(R.id.iv_discount)
        clTotalView = view.findViewById(R.id.cl_total_view)
        tvTotalView = view.findViewById(R.id.tv_total_view)
        tvTitle = view.findViewById(R.id.tv_title)
        tvUpcoming = view.findViewById(R.id.tv_upcoming)
        ivLiveBadge = view.findViewById(R.id.iv_live_badge)
    }

    fun setModel(model: PlayWidgetSmallChannelUiModel) {
        ivCover.loadImage(model.video.coverUrl)

        handleType(model.channelType)
        handlePromo(model.channelType, model.hasPromo)
        handleTotalView(model.channelType, model.totalViewVisible, model.totalView)

        tvTitle.text = model.title
        tvUpcoming.text = "10 Jan - 17.00"

        setOnClickListener { RouteManager.route(context, model.appLink) }

        if (model.video.isLive) {
            flBorder.setBackgroundResource(R.drawable.bg_play_widget_small_live_border)
            ivLiveBadge.visible()
        } else {
            flBorder.setBackgroundResource(R.drawable.bg_play_widget_small_default_border)
            ivLiveBadge.invisible()
        }
    }

    private fun handleType(type: PlayWidgetChannelType) {
        when (type) {
            PlayWidgetChannelType.Live -> {
                tvUpcoming.gone()
            }
            PlayWidgetChannelType.Vod -> {
                tvUpcoming.gone()
            }
            PlayWidgetChannelType.Upcoming -> {
                tvUpcoming.visible()
                ivDiscount.gone()
            }
        }
    }

    private fun handlePromo(type: PlayWidgetChannelType, hasPromo: Boolean) {
        if (type == PlayWidgetChannelType.Upcoming || type == PlayWidgetChannelType.Unknown) ivDiscount.gone()
        else if (hasPromo) ivDiscount.visible()
        else ivDiscount.gone()
    }

    private fun handleTotalView(type: PlayWidgetChannelType, isVisible: Boolean, totalViewString: String) {
        if (type == PlayWidgetChannelType.Upcoming || type == PlayWidgetChannelType.Unknown) clTotalView.gone()
        else if (isVisible) {
            clTotalView.visible()
            tvTotalView.text = totalViewString
        }
        else clTotalView.gone()
    }
}