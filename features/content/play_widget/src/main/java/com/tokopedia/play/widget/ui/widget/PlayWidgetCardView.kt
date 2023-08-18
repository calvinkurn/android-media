package com.tokopedia.play.widget.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardChannelBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play_common.view.RoundedConstraintLayout

/**
 * Created by meyta.taliti on 18/08/23.
 */
class PlayWidgetCardView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = ViewPlayWidgetCardChannelBinding.inflate(LayoutInflater.from(context), this, true)

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setData(data: PlayWidgetChannelUiModel) {
        binding.viewPlayWidgetThumbnail.setImageUrl(data.video.coverUrl)
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = data.totalView.totalViewFmt

        binding.root.setOnClickListener {
            mListener?.onCardClicked(this@PlayWidgetCardView, data)
        }
    }

    interface Listener {

        fun onCardClicked(
            view: PlayWidgetCardView,
            item: PlayWidgetChannelUiModel
        )
    }
}
