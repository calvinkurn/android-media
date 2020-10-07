package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetCardItemType
import com.tokopedia.play.widget.ui.type.PlayWidgetCardType
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage

/**
 * Created by jegul on 06/10/20
 */
class PlayWidgetCardSmallView : ConstraintLayout {

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
        val view = View.inflate(context, R.layout.view_play_widget_card_small, this)
        flBorder = view.findViewById(R.id.fl_border)
        ivCover = view.findViewById(R.id.iv_cover)
        ivDiscount = view.findViewById(R.id.iv_discount)
        clTotalView = view.findViewById(R.id.cl_total_view)
        tvTotalView = view.findViewById(R.id.tv_total_view)
        tvTitle = view.findViewById(R.id.tv_title)
        tvUpcoming = view.findViewById(R.id.tv_upcoming)
        ivLiveBadge = view.findViewById(R.id.iv_live_badge)
    }

    fun setModel(model: PlayWidgetCardUiModel) {
        ivCover.loadImage(model.card.video.coverUrl)

        handleType(model.card.cardType)
        handlePromo(model.card.cardType, model.card.hasPromo)
        handleTotalView(model.card.cardType, model.card.totalViewVisible, model.card.totalView)

        tvTitle.text = "Kuliner Lokal Lezatnya Total Lezatnya"
        tvUpcoming.text = "10 Jan - 17.00"

        flBorder.setBackgroundResource(
                if (model.card.isLive) R.drawable.bg_play_widget_small_live_border
                else R.drawable.bg_play_widget_small_default_border
        )
    }

    private fun handleType(type: PlayWidgetCardItemType) {
        when (type) {
            PlayWidgetCardItemType.Live -> {
                tvUpcoming.gone()
                ivLiveBadge.visible()
            }
            PlayWidgetCardItemType.Vod -> {
                tvUpcoming.gone()
                ivLiveBadge.gone()
            }
            PlayWidgetCardItemType.Upcoming -> {
                tvUpcoming.visible()
                ivLiveBadge.gone()
                ivDiscount.gone()
            }
        }
    }

    private fun handlePromo(type: PlayWidgetCardItemType, hasPromo: Boolean) {
        if (type == PlayWidgetCardItemType.Upcoming || type == PlayWidgetCardItemType.Unknown) ivDiscount.gone()
        else if (hasPromo) ivDiscount.visible()
        else ivDiscount.gone()
    }

    private fun handleTotalView(type: PlayWidgetCardItemType, isVisible: Boolean, totalViewString: String) {
        if (type == PlayWidgetCardItemType.Upcoming || type == PlayWidgetCardItemType.Unknown) clTotalView.gone()
        else if (isVisible) {
            clTotalView.visible()
            tvTotalView.text = totalViewString
        }
        else clTotalView.gone()
    }
}