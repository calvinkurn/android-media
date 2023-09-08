package com.tokopedia.play.widget.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardChannelBinding
import com.tokopedia.play.widget.ui.custom.PlayLiveBadgeView
import com.tokopedia.play.widget.ui.custom.PlayTotalWatchBadgeView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by meyta.taliti on 18/08/23.
 */
class PlayWidgetCardView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = ViewPlayWidgetCardChannelBinding.inflate(LayoutInflater.from(context), this, true)

    private var mListener: Listener? = null

    private val dp4 = context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl2)

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setData(data: PlayWidgetChannelUiModel) {
        binding.viewPlayWidgetThumbnail.setImageUrl(data.video.coverUrl)

        if (data.channelType == PlayWidgetChannelType.Live) {
            addLiveBadgeView()
        } else {
            removeLiveBadgeView()
        }

        addTotalWatchView(data.totalView.totalViewFmt)
        setupLayout()

        binding.root.setOnClickListener {
            mListener?.onCardClicked(this@PlayWidgetCardView, data)
        }
    }

    private val totalWatchView: PlayTotalWatchBadgeView by lazy { PlayTotalWatchBadgeView(binding.root.context) }
    private val liveBadgeView: PlayLiveBadgeView by lazy { PlayLiveBadgeView(binding.root.context) }

    private fun addTotalWatchView(formattedNumber: String) {
        with(binding.viewPlayWidgetChildContainer) {
            val index = indexOfChild(totalWatchView)
            if (index == -1) {
                totalWatchView.setTotalWatch(formattedNumber)
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
