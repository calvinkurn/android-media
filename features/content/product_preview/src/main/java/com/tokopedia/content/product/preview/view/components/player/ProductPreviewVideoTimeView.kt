package com.tokopedia.content.product.preview.view.components.player

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.content.product.preview.databinding.ViewProductPreviewTimeBinding
import com.tokopedia.content.product.preview.utils.millisToFormattedVideoDuration
import java.util.concurrent.TimeUnit
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductPreviewVideoTimeView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewProductPreviewTimeBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    init {
        gravity = Gravity.CENTER
    }

    fun setCurrentPosition(position: Long) {
        binding.tvCurrentPos.text = position.millisToFormattedVideoDuration(context)
    }

    fun setTotalDuration(duration: Long) {
        binding.tvTotalDuration.text = duration.millisToFormattedVideoDuration(context)
    }

}
