package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * Created by jegul on 21/09/20
 */
class PlayMetricBubble : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val ivIcon: ImageView
    private val tvMetricDetail: TextView

    init {
        val view = View.inflate(context, R.layout.view_play_metric_bubble, this)
        ivIcon = view.findViewById(R.id.iv_icon)
        tvMetricDetail = view.findViewById(R.id.tv_metric_detail)
    }

    private val imageListener = object : ImageHandler.ImageLoaderStateListener {
        override fun successLoad() {
            ivIcon.visible()
        }

        override fun failedLoad() {
            ivIcon.gone()
        }
    }

    fun setMetric(metric: PlayMetricUiModel) {
        if (metric.iconUrl.isEmpty()) ivIcon.gone()
        else ivIcon.loadImage(metric.iconUrl, imageListener)

        tvMetricDetail.text = metric.spannedSentence
    }
}