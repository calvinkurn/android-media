package com.tokopedia.play_common.view.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ViewInteractiveFinishBinding
import com.tokopedia.play_common.view.RoundedFrameLayout
import com.tokopedia.play_common.view.loadImage
import kotlin.math.max
import kotlin.math.min

/**
 * Created by kenny.hadisaputra on 11/04/22
 */
class InteractiveFinishView : RoundedFrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val binding = ViewInteractiveFinishBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private val mWidth = resources.getDimensionPixelSize(R.dimen.interactive_finish_width)

    init {
        setCornerRadius(
            resources.getDimensionPixelSize(R.dimen.interactive_widget_radius).toFloat()
        )

        minimumHeight = resources.getDimensionPixelSize(R.dimen.interactive_finish_min_height)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY),
            heightMeasureSpec
        )
    }

    fun setDescription(desc: String) {
        binding.tvDesc.text = desc
    }
}