package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * @author by astidhiyaa on 26/01/22
 */
class PlayWidgetCardLargeBannerView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val background: AppCompatImageView

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.item_play_widget_card_banner_large, this)
        background = view.findViewById(R.id.play_widget_banner)
    }

    fun setData(data: PlayWidgetBannerUiModel) {
        background.loadImage(data.imageUrl)

        this.setOnClickListener {
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
            item: PlayWidgetBannerUiModel
        )
    }
}