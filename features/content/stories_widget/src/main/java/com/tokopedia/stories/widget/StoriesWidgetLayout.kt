package com.tokopedia.stories.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import com.tokopedia.stories.widget.databinding.LayoutStoriesBorderBinding
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import kotlin.properties.Delegates

/**
 * Created by kenny.hadisaputra on 11/08/23
 */
class StoriesWidgetLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = LayoutStoriesBorderBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        setWillNotDraw(false)
        initAttrs(attrs)

        super.setOnClickListener {
            if (mState.status == StoriesStatus.NoStories) return@setOnClickListener
            mListener?.onClickedWhenHasStories(this, mState)
        }
    }

    private var mState by Delegates.observable(StoriesWidgetState.Default) { _, _, newProp ->
        getChildBorderView()?.setStoriesStatus(newProp.status)

        isClickable = newProp.status != StoriesStatus.NoStories
    }

    private var mListener: Listener? = null

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.StoriesWidgetLayout)
        val seenBorderWidth = attributeArray.getDimensionPixelSize(R.styleable.StoriesWidgetLayout_seen_stories_border_width, -1)
        val unseenBorderWidth = attributeArray.getDimensionPixelSize(R.styleable.StoriesWidgetLayout_unseen_stories_border_width, -1)

        getChildBorderView()?.let {
            it.setBorderConfig { config ->
                config.copy(
                    seenStoriesWidth = if (seenBorderWidth != -1) StoriesBorderView.BorderValue.Fixed(seenBorderWidth) else config.seenStoriesWidth,
                    unseenStoriesWidth = if (unseenBorderWidth != -1) StoriesBorderView.BorderValue.Fixed(unseenBorderWidth) else config.unseenStoriesWidth
                )
            }
        }

        attributeArray.recycle()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        error("You are not allowed to call setOnClickListener on StoriesWidgetLayout, use StoriesWidgetLayout.Listener instead")
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return mState.status != StoriesStatus.NoStories
    }

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        return super.drawChild(canvas, child, drawingTime)
    }

    fun setState(onUpdate: (StoriesWidgetState) -> StoriesWidgetState) {
        mState = onUpdate(mState)
        invalidate()
    }

    fun startAnimation() {
        binding.border.startAnimation()
    }

    fun endAnimation() {
        binding.border.endAnimation()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun getChildBorderView(): StoriesBorderView? {
        return children.firstOrNull { it is StoriesBorderView } as? StoriesBorderView
    }

    interface Listener {
        fun onClickedWhenHasStories(view: StoriesWidgetLayout, state: StoriesWidgetState)
    }
}
