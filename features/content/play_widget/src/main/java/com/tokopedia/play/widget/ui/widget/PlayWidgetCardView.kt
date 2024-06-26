package com.tokopedia.play.widget.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardChannelBinding
import com.tokopedia.play.widget.ui.custom.PlayLiveBadgeView
import com.tokopedia.play.widget.ui.custom.PlayTotalWatchBadgeView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.view.RoundedFrameLayout
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by meyta.taliti on 18/08/23.
 */
class PlayWidgetCardView : RoundedFrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = ViewPlayWidgetCardChannelBinding.inflate(LayoutInflater.from(context), this)

    private var mListener: Listener? = null

    private val dp8 = context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl3)
    private val dp4 = context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl2)

    init {
        setBackgroundColor(Color.TRANSPARENT)
        setCornerRadius(dp8.toFloat())
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setData(data: PlayWidgetChannelUiModel) {
        binding.viewPlayWidgetThumbnail.scaleType = ImageView.ScaleType.CENTER
        binding.viewPlayWidgetThumbnail.loadImage(data.video.coverUrl) {
            listener(
                onSuccess = { _, _ -> binding.viewPlayWidgetThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP }
            )
        }

        if (data.channelType == PlayWidgetChannelType.Live) {
            addLiveBadgeView()
        } else {
            removeLiveBadgeView()
        }

        addTotalWatchView(data.totalView.totalViewFmt)
        setupLayout()

        if (mListener == null) return
        binding.root.setOnClickListener {
            mListener?.onCardClicked(this@PlayWidgetCardView, data)
        }
    }

    private val totalWatchView: PlayTotalWatchBadgeView by lazy { PlayTotalWatchBadgeView(binding.root.context) }
    private val liveBadgeView: PlayLiveBadgeView by lazy { PlayLiveBadgeView(binding.root.context) }

    private fun addTotalWatchView(formattedNumber: String) {
        with(binding.viewPlayWidgetChildContainer) {
            totalWatchView.setTotalWatch(formattedNumber)
            val index = indexOfChild(totalWatchView)
            totalWatchView.setTotalWatch(formattedNumber)
            if (index == -1) {
                addView(totalWatchView)
            }
        }
    }

    private fun addLiveBadgeView() {
        with(binding.viewPlayWidgetChildContainer) {
            val index = indexOfChild(liveBadgeView)
            if (index == -1) {
                addView(liveBadgeView)
            }
        }
    }

    private fun removeLiveBadgeView() {
        with(binding.viewPlayWidgetChildContainer) {
            val index = indexOfChild(liveBadgeView)
            if (index > -1) {
                removeViewAt(index)
            }
        }
    }

    private fun setupLayout() {
        with(binding.viewPlayWidgetChildContainer) {
            val liveBadgeIndex = indexOfChild(liveBadgeView)
            val totalWatchIndex = indexOfChild(totalWatchView)

            if (liveBadgeIndex != -1 && totalWatchIndex != -1) {
                totalWatchView.apply {
                    layoutParams = getDefaultLayoutParams().apply {
                        leftMargin = dp4
                    }
                }
            }
        }
    }

    private fun getDefaultLayoutParams() =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

    interface Listener {

        fun onCardClicked(
            view: PlayWidgetCardView,
            item: PlayWidgetChannelUiModel
        )
    }
}
