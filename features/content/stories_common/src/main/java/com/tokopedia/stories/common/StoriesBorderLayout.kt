package com.tokopedia.stories.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.stories.common.databinding.LayoutStoriesBorderBinding
import kotlin.properties.Delegates

/**
 * Created by kenny.hadisaputra on 11/08/23
 */
class StoriesBorderLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = LayoutStoriesBorderBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    init {
        setWillNotDraw(false)
        initAttrs(attrs)

        super.setOnClickListener {
            if (mState.status == StoriesStatus.NoStories) return@setOnClickListener
            RouteManager.route(context, mState.appLink)
        }
    }

    private val circleChildPath = Path()

    private val childMargin = 4.dpToPx(resources.displayMetrics)

    private var mState by Delegates.observable(StoriesAvatarState.Default) { _, _, newProp ->
        getChildBorderView()?.setStoriesStatus(newProp.status)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.StoriesBorderLayout)
        val seenBorderWidth = attributeArray.getDimensionPixelSize(R.styleable.StoriesBorderLayout_seen_stories_border_width, -1)
        val unseenBorderWidth = attributeArray.getDimensionPixelSize(R.styleable.StoriesBorderLayout_unseen_stories_border_width, -1)

        getChildBorderView()?.let {
            it.setBorderConfig { config ->
                config.copy(
                    seenStoriesWidth = if (seenBorderWidth != -1) seenBorderWidth else config.seenStoriesWidth,
                    unseenStoriesWidth = if (unseenBorderWidth != -1) unseenBorderWidth else config.unseenStoriesWidth,
                )
            }
        }

        attributeArray.recycle()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        error("You are not allowed to call setOnClickListener on StoriesBorderLayout")
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return mState.status != StoriesStatus.NoStories
    }

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
//        if (child is StoriesBorderView) {
//            return super.drawChild(canvas, child, drawingTime)
//        }
//
//        canvas.save()
//        canvas.clipChildPath()
//        val drawChild = super.drawChild(canvas, child, drawingTime)
//        canvas.restore()
//
//        return drawChild
        return super.drawChild(canvas, child, drawingTime)
    }

    fun setState(onUpdate: (StoriesAvatarState) -> StoriesAvatarState) {
        mState = onUpdate(mState)
        invalidate()
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
        return when (mState.status) {
            StoriesStatus.NoStories -> 0
            else -> childMargin
        }
    }

    private fun getChildBorderView(): StoriesBorderView? {
        return children.firstOrNull { it is StoriesBorderView } as? StoriesBorderView
    }
}
