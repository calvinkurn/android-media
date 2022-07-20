package com.tokopedia.play.widget.ui.widget.small

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
class PlayWidgetCardSmallBannerView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val ivBanner: ImageView
    private val tvFallback: Typography

    private var mListener: Listener? = null

    private val bannerListener: ImageHandler.ImageLoaderStateListener

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_small_banner, this)
        ivBanner = view.findViewById(R.id.iv_banner)
        tvFallback = view.findViewById(R.id.tv_fallback)

        bannerListener = object : ImageHandler.ImageLoaderStateListener {

            override fun successLoad() {
                ivBanner.visibility = View.VISIBLE
                tvFallback.visibility = View.GONE
            }

            override fun failedLoad() {
                ivBanner.visibility = View.GONE
                tvFallback.visibility = View.VISIBLE
            }
        }
    }

    fun setData(data: PlayWidgetBannerUiModel) {
        ivBanner.loadImage(data.imageUrl, bannerListener)

        setOnClickListener {
            mListener?.onBannerClicked(this)
            RouteManager.route(it.context, data.appLink)
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onBannerClicked(view: PlayWidgetCardSmallBannerView)
    }
}