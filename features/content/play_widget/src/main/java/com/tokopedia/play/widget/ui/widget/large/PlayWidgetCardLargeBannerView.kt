package com.tokopedia.play.widget.ui.widget.large

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.getWidthAndHeight

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetCardLargeBannerView : FrameLayout {

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
        val view = View.inflate(context, R.layout.view_play_widget_card_large_banner, this)
        background = view.findViewById(R.id.play_widget_banner)
        card = view.findViewById(R.id.play_card_banner_large)
    }

    fun setData(data: PlayWidgetBannerUiModel) {
        background.loadImage(data.imageUrl)

        card.setOnClickListener {
            mListener?.onBannerClicked(it, data)
            RouteManager.route(it.context, data.appLink)
        }

        layoutParams = ViewGroup.LayoutParams(
            data.gridType.getWidthAndHeight(context).first,
            data.gridType.getWidthAndHeight(context).second
        )
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
