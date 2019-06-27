package com.tokopedia.design.widget.ExpandableView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.tokopedia.design.R
import com.tokopedia.design.utils.InterpolatorUtils
import com.tokopedia.design.widget.ExpandableView.ExpandableLayout.Companion.DEFAULT_DURATION
import com.tokopedia.design.widget.ExpandableView.ExpandableLayout.Companion.DEFAULT_EXPANDED
import com.tokopedia.design.widget.ExpandableView.ExpandableLayout.Companion.VERTICAL

class ExpandableRelativeLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), ExpandableLayout {

    private var duration = DEFAULT_DURATION
    private var isExpanded = DEFAULT_EXPANDED
    private var interpolator: TimeInterpolator = LinearInterpolator()
    var orientation = VERTICAL
        set(@ExpandableLayout.Orientation value) {
            field = value
        }
    private var defaultChildIndex = Int.MAX_VALUE
    private var defaultChildPosition = Int.MIN_VALUE
    var closePosition = 0

    private var savedState: ExpandableSavedState? = null
    private var layoutSize = 0
    private var isArranged = false
    private var isCalculatedSize = false
    private var isAnimating = false
    private val childPositionList = mutableListOf<Int>()
    private var listener: ExpandableLayoutListener? = null

    init {
        initializeView(context, attrs, defStyleAttr)
    }

    private fun initializeView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ExpandableRelativeLayout, defStyleAttr, 0)
        duration = typedArray.getInteger(R.styleable.ExpandableRelativeLayout_el_duration, ExpandableLayout.DEFAULT_DURATION)
        isExpanded = typedArray.getBoolean(R.styleable.ExpandableRelativeLayout_el_expanded, ExpandableLayout.DEFAULT_EXPANDED)
        orientation = typedArray.getInteger(R.styleable.ExpandableRelativeLayout_el_orientation, ExpandableLayout.VERTICAL)
        defaultChildIndex = typedArray.getInteger(R.styleable.ExpandableRelativeLayout_el_defaultChildIndex,
                Int.MAX_VALUE)
        defaultChildPosition = typedArray.getInteger(R.styleable.ExpandableRelativeLayout_el_defaultPosition,
                Int.MIN_VALUE)
        val interpolatorType = typedArray.getInteger(R.styleable.ExpandableRelativeLayout_el_interpolator,
                InterpolatorUtils.LINEAR_INTERPOLATOR)
        interpolator = InterpolatorUtils.createInterpolator(interpolatorType)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!isCalculatedSize) {
            // calculate this layout size
            childPositionList.clear()
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val params = view.layoutParams as RelativeLayout.LayoutParams

                val childSize = if (isVertical())
                    view.measuredHeight
                else
                    view.measuredWidth
                val childMargin = if (isVertical())
                    params.topMargin + params.bottomMargin
                else
                    params.leftMargin + params.rightMargin
                var sumSize = 0
                if (0 < i) {
                    sumSize = childPositionList[i - 1]
                }
                childPositionList.add(sumSize + childSize + childMargin)
            }
            layoutSize = childPositionList[childPositionList.size - 1]

            if (0 < layoutSize) {
                isCalculatedSize = true
            }
        }

        if (isArranged) {
            return
        }

        if (isExpanded) {
            setLayoutSize(layoutSize)
        } else {
            setLayoutSize(closePosition)
        }
        val childNumbers = childPositionList.size
        if (childNumbers > defaultChildIndex && childNumbers > 0) {
            moveChild(defaultChildIndex, 0, null)
        }
        if (defaultChildPosition > 0 && layoutSize >= defaultChildPosition && layoutSize > 0) {
            move(defaultChildPosition, 0, null)
        }
        isArranged = true

        savedState?.run {
            setLayoutSize(size)
        }
    }

    private fun setLayoutSize(size: Int) {
        if (isVertical()) {
            layoutParams.height = size
        } else {
            layoutParams.width = size
        }
    }

    private fun isVertical() = (orientation == ExpandableLayout.VERTICAL)

    /**
     * Moves to bottom(VERTICAL) or right(HORIZONTAL) of child view
     *
     * @param index        index child view index
     * @param duration
     * @param interpolator
     */
    fun moveChild(index: Int, duration: Long, interpolator: TimeInterpolator?) {
        if (isAnimating) {
            return
        }
        createExpandAnimator(getCurrentPosition(), childPositionList[index],
                duration, interpolator).start()
    }

    /**
     * Moves to position
     *
     * @param position
     * @param duration
     * @param interpolator
     */
    fun move(position: Int, duration: Long = this.duration.toLong(),
             interpolator: TimeInterpolator? = this.interpolator) {
        if (isAnimating) {
            return
        }
        if (0 > position || layoutSize < position) {
            return
        }
        createExpandAnimator(getCurrentPosition(), position,
                duration, interpolator).start()
    }

    /**
     * Gets the current position.
     *
     * @return
     */
    fun getCurrentPosition() = if (isVertical()) measuredHeight else measuredWidth

    override fun onSaveInstanceState(): Parcelable {
        val parcelable = super.onSaveInstanceState()
        return ExpandableSavedState(parcelable).apply { size = getCurrentPosition() }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is ExpandableSavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        savedState = state
    }

    override fun toggle() {
        if (closePosition < getCurrentPosition()) {
            collapse()
        } else {
            expand()
        }
    }

    override fun toggle(duration: Long, interpolator: TimeInterpolator?) {}

    override fun expand() {
        if (isAnimating) {
            return
        }
        createExpandAnimator(getCurrentPosition(), layoutSize,
                duration.toLong(), interpolator).start()
    }

    override fun expand(duration: Long, interpolator: TimeInterpolator?) {}

    override fun collapse() {
        if (isAnimating) {
            return
        }
        createExpandAnimator(getCurrentPosition(), closePosition,
                duration.toLong(), interpolator).start()
    }

    override fun collapse(duration: Long, interpolator: TimeInterpolator?) {}

    override fun initLayout(isMaintain: Boolean) {
        closePosition = 0
        layoutSize = 0
        isArranged = isMaintain
        isCalculatedSize = false
        savedState = null

        super.requestLayout()
    }

    override fun setListener(listener: ExpandableLayoutListener) {
        this.listener = listener
    }

    override fun setDuration(duration: Int) {
        if (duration < 0) {
            throw IllegalArgumentException("Animators cannot have negative duration: $duration")
        }
        this.duration = duration
    }

    override fun setExpanded(expanded: Boolean) {
        isExpanded = expanded
        isArranged = false
        requestLayout()
    }

    override fun isExpanded() = isExpanded

    override fun setInterpolator(interpolator: TimeInterpolator) {
        this.interpolator = interpolator
    }

    /**
     * Gets the width from left of layout if orientation is horizontal.
     * Gets the height from top of layout if orientation is vertical.
     *
     * @param index index of child view
     * @return position from top or left
     */
    fun getChildPosition(index: Int): Int {
        if (0 > index || childPositionList.size <= index) {
            throw IllegalArgumentException("There aren't the view having this index.")
        }
        return childPositionList[index]
    }

    /**
     * Creates value animator.
     * Expand the layout if {@param to} is bigger than {@param from}.
     * Collapse the layout if {@param from} is bigger than {@param to}.
     *
     * @param from
     * @param to
     * @param duration
     * @param interpolator
     * @return
     */
    private fun createExpandAnimator(
            from: Int, to: Int, duration: Long, interpolator: TimeInterpolator?): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(from, to)
        valueAnimator.duration = duration
        valueAnimator.interpolator = interpolator
        valueAnimator.addUpdateListener { animator ->
            if (isVertical()) {
                layoutParams.height = animator.animatedValue as Int
            } else {
                layoutParams.width = animator.animatedValue as Int
            }
            requestLayout()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                isAnimating = true
                if (listener == null) {
                    return
                }
                listener?.onAnimationStart()

                if (layoutSize == to) {
                    listener?.onPreOpen()
                    return
                }
                if (closePosition == to) {
                    listener?.onPreClose()
                }
            }

            override fun onAnimationEnd(animator: Animator) {
                isAnimating = false
                val currentSize = if (isVertical())
                    layoutParams.height
                else
                    layoutParams.width
                isExpanded = currentSize > closePosition

                if (listener == null) {
                    return
                }
                listener?.onAnimationEnd()

                if (currentSize == layoutSize) {
                    listener?.onOpened()
                    return
                }
                if (currentSize == closePosition) {
                    listener?.onClosed()
                }
            }
        })
        return valueAnimator
    }

}