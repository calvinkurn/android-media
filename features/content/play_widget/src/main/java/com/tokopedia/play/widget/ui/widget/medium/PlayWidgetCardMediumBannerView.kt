package com.tokopedia.play.widget.ui.widget.medium

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
class PlayWidgetCardMediumBannerView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val background: AppCompatImageView
    private val card: CardView

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_medium_banner, this)
        background = view.findViewById(R.id.play_widget_banner)
        card = view.findViewById(R.id.play_card_banner_medium)
    }

    fun setData(data: PlayWidgetBannerUiModel) {
        background.scaleType = ImageView.ScaleType.CENTER
        background.loadImage(data.imageUrl) {
            listener(
                onSuccess = { _, _ -> background.scaleType = ImageView.ScaleType.CENTER_CROP }
            )
        }

        card.setOnClickListener {
            mListener?.onBannerClicked(it, data)
            RouteManager.route(it.context, data.appLink)
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {

        fun onBannerClicked(
            view: View,
            item: PlayWidgetBannerUiModel,
        )
    }
}
