package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.play.widget.databinding.ViewPlayWidgetTotalViewsBinding

/**
 * Created by meyta.taliti on 01/09/23.
 */
class PlayTotalWatchBadgeView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewPlayWidgetTotalViewsBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setTotalWatch(formattedNumber: String) {
        binding.tvTotalViews.text = formattedNumber
    }
}
