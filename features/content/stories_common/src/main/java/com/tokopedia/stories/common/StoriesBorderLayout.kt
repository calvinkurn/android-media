package com.tokopedia.stories.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.stories.common.databinding.LayoutStoriesBorderBinding

/**
 * Created by kenny.hadisaputra on 11/08/23
 */
class StoriesBorderLayout : FrameLayout {

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

    private val binding = LayoutStoriesBorderBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    init {
        setWillNotDraw(false)
    }

    private val circleChildPath = Path()

    private val childMargin = 4.dpToPx(resources.displayMetrics)

    private var mStoriesStatus = StoriesStatus.HasUnseenStories

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        if (child is StoriesBorderView) {
            return super.drawChild(canvas, child, drawingTime)
        }

        canvas.save()
        canvas.clipChildPath()
        val drawChild = super.drawChild(canvas, child, drawingTime)
        canvas.restore()

        return drawChild
    }

    fun startAnimation() {
        binding.border.startAnimation()
    }

    private fun setupChildPath(width: Int, height: Int) {
        circleChildPath.reset()
        circleChildPath.addCircle(
            width / 2f,
            height / 2f,
            width / 2f - getChildMargin(),
            Path.Direction.CW
        )
    }

    private fun Canvas.clipChildPath() {
        setupChildPath(width, height)
        clipPath(circleChildPath)
    }

    private fun getChildMargin(): Int {
        return when (mStoriesStatus) {
            StoriesStatus.NoStories -> 0
            else -> childMargin
        }
    }
}
