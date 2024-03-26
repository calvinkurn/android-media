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
import com.tokopedia.play.widget.ui.model.PlayWidgetRatio
import com.tokopedia.play.widget.ui.model.getWidthAndHeight

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetCardLargeBannerView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(attrs)
    }

    private val background: AppCompatImageView
    private val card: CardView

    private var mListener: Listener? = null

    private var mRatio = PlayWidgetRatio.Unknown

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_large_banner, this)
        background = view.findViewById(R.id.play_widget_banner)
        card = view.findViewById(R.id.play_card_banner_large)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return

        val attributeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PlayWidgetCardLargeView
        )
        val rawDimensionRatio = attributeArray.getString(R.styleable.PlayWidgetCardLargeView_dimensionRatio).orEmpty()
        mRatio = PlayWidgetRatio.parse(rawDimensionRatio)

        attributeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (newWidthMeasureSpec, newHeightMeasureSpec) = mRatio.getMeasureSpec(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
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
