package com.tokopedia.tokopoints.view.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.tokopoints.R
import com.tokopedia.unifycomponents.setBodyText
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion
import kotlin.math.abs

class MembershipProgressBar : RelativeLayout {
    val progressBarContainer = FrameLayout(context)
    val progressDrawable = GradientDrawable()
    val trackDrawable = GradientDrawable()
    val progressBar = LinearLayout(context)
    val trackBar = View(context)
    val progressBarIndicatorWrapper: FrameLayout
    val progressBarWrapper: LinearLayout
    val rightIndicatorTextView: TextView
    var progressBarColorType = COLOR_GREEN
        set(value) {
            field = value

            when(value) {
                COLOR_GREEN -> {
                    progressDrawable.colors = intArrayOf(
                        ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.progressbarunify_type_green),
                        ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.progressbarunify_type_green)
                    )
                }
            }
        }

    var progressBarColor: IntArray = intArrayOf(
        ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.progressbarunify_type_green),
        ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.progressbarunify_type_green)
    )
        set(value) {
            field = value

            progressDrawable.colors = progressBarColor
        }
    private var value = 0
    var progressBarHeight = SIZE_SMALL
        set(value) {
            field = value

            progressBarContainer.layoutParams =
                LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, progressBarHeight.toPx())
        }

    var progressBarIndicator: Array<ProgressBarIndicatorItemUnify>? = null
        set(value) {
            field = value
            initIndicator(value)
        }

    var progressBarBottomDescriptive: (Pair<String, String>)? = null
        set(value) {
            field = value
            progressBarIndicatorWrapper.setPadding(0, 4.toPx(), 0, 0)
            val leftTv = TextView(context)
            val rightTv = TextView(context)
            leftTv.apply {
                text = value?.first
                setBodyText(3, true)
                setTextColor(ContextCompat.getColor(context,
                    com.tokopedia.unifycomponents.R.color.progressbarunify_indicator_text_color
                ))
            }
            rightTv.apply {
                text = value?.second
                setBodyText(3, true)
                setTextColor(ContextCompat.getColor(context,
                    com.tokopedia.unifycomponents.R.color.progressbarunify_indicator_text_color
                ))
            }

            val indicatorArr = arrayOf(
                ProgressBarIndicatorItemUnify(0, null, ProgressBarIndicatorItemUnify.ALIGN_LEFT, leftTv),
                ProgressBarIndicatorItemUnify(100, null, ProgressBarIndicatorItemUnify.ALIGN_RIGHT, rightTv)
            )

            initIndicator(indicatorArr)

            onFinishIndicator = {
                indicatorArr[0].itemIndicatorRef.visibility = View.GONE
                indicatorArr[1].itemIndicatorRef.visibility = View.GONE
            }
        }

    var onFinishIndicator: (() -> Unit)? = null
    var onValueChangeListener: ((prev: Int, current: Int) -> Unit)? = null

    var rightIndicatorText: CharSequence = ""
        set(value) {
            field = value

            if (value.isNotEmpty()) {
                rightIndicatorTextView.visibility = View.VISIBLE
                rightIndicatorTextView.post {
                    if (!hasProgressIcon && progressBarHeight.toPx() < rightIndicatorTextView.measuredHeight) {
                        progressBarWrapper.layoutParams.height = rightIndicatorTextView.measuredHeight
                        progressBarWrapper.requestLayout()
                        progressBarIndicatorWrapper.setPadding(0, 2.toPx(), 0, 0)
                    } else {
                        progressBarIndicatorWrapper.setPadding(0, 8.toPx(), 0, 0)
                    }
                }
            } else {
                rightIndicatorTextView.visibility = View.GONE
                progressBarIndicatorWrapper.setPadding(0, 8.toPx(), 0, 0)
            }

            rightIndicatorTextView.text = value

            measureIndicator()
        }

    val layout = View.inflate(context, R.layout.tp_progressbar_membership, this)

    private var hasProgressIcon = false

    private var progressIcon: ImageView = ImageView(context)

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initWithAttr(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initWithAttr(context, attributeSet)
    }

    init {
        progressBarWrapper = layout.findViewById(R.id.progress_bar_wrapper)
        progressBarIndicatorWrapper = layout.findViewById(R.id.progress_bar_indicator_wrapper)
        rightIndicatorTextView = layout.findViewById(R.id.right_indicator_text)
        rightIndicatorTextView.setBodyText(3, true)
        rightIndicatorTextView.setTextColor(ContextCompat.getColor(context,
            com.tokopedia.unifycomponents.R.color.progressbarunify_indicator_text_color
        ))
        val lp = LinearLayout.LayoutParams(0.toPx(), LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.CENTER_VERTICAL
        progressBar.apply {
            layoutParams = lp
        }

        progressDrawable.apply {
            shape = GradientDrawable.RECTANGLE
            colors = progressBarColor
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
        }

        trackDrawable.apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.progressbarunify_track_color))
        }

        val indicatorBar = View(context)
        indicatorBar.post {
            progressDrawable.cornerRadius = indicatorBar.measuredHeight.toPx().toFloat()
            indicatorBar.background = progressDrawable
        }

        trackBar.post {
            trackDrawable.cornerRadius = trackBar.measuredHeight.toPx().toFloat()
            trackBar.background = trackDrawable
        }

        progressBarWrapper.post {
            trackBar.layoutParams =
                FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    progressBarHeight.toPx()
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }

            indicatorBar.layoutParams = LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                progressBarHeight.toPx()
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
        }

        progressBar.addView(indicatorBar)

        progressBarContainer.addView(trackBar)
        progressBarContainer.addView(progressBar)
        progressBarWrapper.addView(progressBarContainer)
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.UnifyProgressBar)
        val valAttr =
            attributeArray.getInteger(R.styleable.UnifyProgressBar_unify_progress_bar_value, 0)
        val type = attributeArray.getInteger(R.styleable.UnifyProgressBar_unify_progress_bar_color_type, COLOR_GREEN)
        val typeColor = ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.progressbarunify_type_green)

        val fromColor = attributeArray.getColor(
            R.styleable.UnifyProgressBar_unify_progress_bar_from_color,
            typeColor
        )
        val toColor = attributeArray.getColor(
            R.styleable.UnifyProgressBar_unify_progress_bar_to_color,
            typeColor
        )

        progressBarHeight =
            attributeArray.getInt(R.styleable.UnifyProgressBar_unify_progress_bar_height, SIZE_SMALL)

        progressBarColor = intArrayOf(fromColor, toColor)
        progressBarContainer.layoutParams =
            LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, progressBarHeight.toPx())

        if (valAttr != 0) {
            setValue(valAttr)
        }
    }

    private fun initIndicator(value: Array<ProgressBarIndicatorItemUnify>?) {
        progressBarWrapper.post {
            val lp = ConstraintLayout.LayoutParams(
                progressBarWrapper.measuredWidth,
                LayoutParams.WRAP_CONTENT
            )
            lp.leftToLeft = layout.id
            lp.topToBottom = progressBarWrapper.id

            progressBarIndicatorWrapper.layoutParams = lp

            progressBarIndicatorWrapper.visibility = View.GONE

            progressBarIndicatorWrapper.post {
                value?.forEachIndexed { index, progressBarIndicatorItemUnify ->
                    val indicatorItemWrapper = RelativeLayout(context)
                    progressBarIndicatorItemUnify.itemRef = indicatorItemWrapper
                    val indicatorDots = ImageView(context).apply {
                        id = R.id.indicator_dots_id
                    }
                    progressBarIndicatorItemUnify.itemIndicatorRef = indicatorDots

                    val indicatorContent = LinearLayout(context)
                    indicatorContent.apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                        ).apply {
                            addRule(BELOW, indicatorDots.id)
                        }
                    }

                    indicatorItemWrapper.addView(indicatorContent)

                    val lp = LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                    )
                    indicatorItemWrapper.layoutParams = lp
                    indicatorItemWrapper.translationX =
                        (progressBarIndicatorWrapper.measuredWidth.toFloat() * progressBarIndicatorItemUnify.indicatorPosition / 100)

                    if (progressBarIndicatorItemUnify.indicatorText != null) {
                        val indicatorText = TextView(context)

                        indicatorText.apply {
                            text = progressBarIndicatorItemUnify.indicatorText
                            gravity = Gravity.CENTER_HORIZONTAL
                            setBodyText(3)
                            setTextColor(ContextCompat.getColor(context,
                                com.tokopedia.unifycomponents.R.color.progressbarunify_indicator_text_color
                            ))
                        }

                        indicatorContent.addView(indicatorText)
                    }

                    if (progressBarIndicatorItemUnify.indicatorCustomView != null) {
                        indicatorContent.addView(progressBarIndicatorItemUnify.indicatorCustomView)
                    }

                    indicatorItemWrapper.addView(indicatorDots)

                    indicatorItemWrapper.post {
                        when (progressBarIndicatorItemUnify.indicatorAlign) {
                            ProgressBarIndicatorItemUnify.ALIGN_LEFT -> {
                                indicatorItemWrapper.translationX =
                                    indicatorItemWrapper.translationX - 6.toPx()
                                if (indicatorItemWrapper.translationX < 0.toPx()) indicatorItemWrapper.translationX =
                                    0.toPx().toFloat()
                            }
                            ProgressBarIndicatorItemUnify.ALIGN_CENTER -> {
                                indicatorItemWrapper.translationX =
                                    indicatorItemWrapper.translationX + (indicatorItemWrapper.measuredWidth.toFloat() / -2) - 3.toPx()
                            }
                            ProgressBarIndicatorItemUnify.ALIGN_RIGHT -> {
                                indicatorItemWrapper.translationX =
                                    indicatorItemWrapper.translationX + (-indicatorItemWrapper.measuredWidth.toFloat())
                            }
                        }

                        indicatorDots.apply {
                            setBackgroundResource(com.tokopedia.unifycomponents.R.drawable.progress_bar_indicator_dot)
                            layoutParams = LayoutParams(6.toPx(), 6.toPx()).apply {
                                when (progressBarIndicatorItemUnify.indicatorAlign) {
                                    ProgressBarIndicatorItemUnify.ALIGN_LEFT -> addRule(ALIGN_LEFT)
                                    ProgressBarIndicatorItemUnify.ALIGN_CENTER -> addRule(
                                        CENTER_HORIZONTAL
                                    )
                                    ProgressBarIndicatorItemUnify.ALIGN_RIGHT -> {
                                        setMargins(
                                            indicatorItemWrapper.measuredWidth - 6.toPx(),
                                            0,
                                            0,
                                            0
                                        )
                                    }
                                }
                            }
                        }
                    }

                    progressBarIndicatorWrapper.addView(indicatorItemWrapper)
                }

                onFinishIndicator?.invoke()
            }
        }
    }

    private fun measureIndicator() {
        progressBarContainer.addOnLayoutChangeListener(object: OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                leftWas: Int,
                topWas: Int,
                rightWas: Int,
                bottomWas: Int
            ) {
                val widthWas = rightWas - leftWas
                if (v?.width != widthWas) {

                }
            }
        })

        progressBarWrapper.post {
            val lp = ConstraintLayout.LayoutParams(
                progressBarWrapper.width,
                LayoutParams.WRAP_CONTENT
            )
            lp.leftToLeft = layout.id
            lp.topToBottom = progressBarWrapper.id

            progressBarIndicatorWrapper.layoutParams = lp

            progressBarIndicatorWrapper.post {
                progressBarIndicator?.forEachIndexed { index, progressBarIndicatorItemUnify ->
                    val itemView = progressBarIndicatorWrapper.getChildAt(index)
                    itemView.translationX =
                        (progressBarIndicatorWrapper.measuredWidth.toFloat() * progressBarIndicatorItemUnify.indicatorPosition / 100)

                    when (progressBarIndicatorItemUnify.indicatorAlign) {
                        ProgressBarIndicatorItemUnify.ALIGN_LEFT -> {
                            itemView.translationX =
                                itemView.translationX - 6.toPx()
                            if (itemView.translationX < 0.toPx()) itemView.translationX =
                                0.toPx().toFloat()
                        }
                        ProgressBarIndicatorItemUnify.ALIGN_CENTER -> {
                            itemView.translationX =
                                itemView.translationX + (itemView.measuredWidth.toFloat() / -2) - 3.toPx()
                        }
                        ProgressBarIndicatorItemUnify.ALIGN_RIGHT -> {
                            itemView.translationX =
                                itemView.translationX + (-itemView.measuredWidth.toFloat())
                        }
                    }
                }
            }
        }
    }

    fun setValue(value: Int, isSmooth: Boolean = true) {
        onValueChangeListener?.invoke(this.value, value)
        when {
            value < 0 -> this.value = 0
            value > 100 -> this.value = 100
            else -> this.value = value
        }

        progressBarWrapper.post {
            var endWidth = progressBarWrapper.measuredWidth * this.value / 100

            if (endWidth < 5.toPx() && this.value != 0) {
                endWidth = 5.toPx()
            }

            if (isSmooth) {
                progressBar.post {
                    val anim = ValueAnimator.ofInt(progressBar.measuredWidth, endWidth)

                    anim.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                        override fun onAnimationUpdate(p0: ValueAnimator?) {
                            val animVal: Int = (p0?.animatedValue as Int)
                            val progressBarLayoutParams = progressBar.layoutParams
                            progressBarLayoutParams.width = animVal
                            progressBar.layoutParams = progressBarLayoutParams

                            // check whether target width more than progress icon width / 2,
                            // if no then force to 5dp (same width as minimum progress bar width) not 0f, to give an impression that the bar is moving,
                            // if yes then proceed to normal flow
                            if (animVal > progressIcon.measuredWidth / 2) {
                                // check whether progressIcon is overflowing the parent,
                                // if no then force it to stay inside
                                if (progressBarWrapper.measuredWidth - animVal > (progressIcon.measuredWidth / 2)) {
                                    progressIcon.translationX =
                                        animVal.toFloat() - (progressIcon.measuredWidth / 2)
                                } else {
                                    progressIcon.translationX =
                                        progressBarWrapper.measuredWidth - progressIcon.measuredWidth.toFloat()
                                }
                            } else {
                                progressIcon.translationX = 5f
                                if (animVal == 0) {
                                    progressIcon.translationX = 0f
                                }
                            }
                        }
                    })

                    anim.interpolator = UnifyMotion.EASE_IN_OUT
                    anim.duration = UnifyMotion.T3
                    anim.start()
                }
            } else {
                progressBar.layoutParams =
                    FrameLayout.LayoutParams(endWidth, LayoutParams.MATCH_PARENT)

                // same logic as smooth, changing the animation width value with final target width
                if (endWidth > progressIcon.measuredWidth / 2) {
                    if (progressBarWrapper.measuredWidth - endWidth > (progressIcon.measuredWidth / 2)) {
                        progressIcon.translationX =
                            endWidth.toFloat() - (progressIcon.measuredWidth / 2)
                    } else {
                        progressIcon.translationX =
                            progressBarWrapper.measuredWidth - progressIcon.measuredWidth.toFloat()
                    }
                } else {
                    progressIcon.translationX = 5f

                }
            }
        }
    }

    fun setProgressIcon(icon: Drawable?, offsetY: Float = 0f, width: Int? = null, height: Int? = null) {
        hasProgressIcon = true
        var mWidth = width ?: 0
        var mHeight = height ?: 0

        if (width == null) {
            mWidth = when (progressBarHeight) {
                SIZE_SMALL -> 16.toPx()
                SIZE_MEDIUM -> 24.toPx()
                SIZE_LARGE -> 28.toPx()
                else -> 16.toPx()
            }
        }

        if (height == null) {
            mHeight = when (progressBarHeight) {
                SIZE_SMALL -> 16.toPx()
                SIZE_MEDIUM -> 24.toPx()
                SIZE_LARGE -> 28.toPx()
                else -> 16.toPx()
            }
        }

        if (progressIcon.parent != null) {
            (progressIcon.parent as ViewGroup).removeView(progressIcon)
        }
        val mImageViewLayoutParams =  FrameLayout.LayoutParams(mWidth, mHeight)
        mImageViewLayoutParams.gravity = Gravity.BOTTOM
        progressIcon.layoutParams = mImageViewLayoutParams
        progressIcon.setImageDrawable(icon)
        if (offsetY < 0f) {
            progressIcon.translationY = offsetY
        }
        progressBarContainer.addView(progressIcon)

        progressBarContainer.post {
            // check if offsetY is negative
            if (offsetY < 0f) {
                progressBarContainer.layoutParams =
                    LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mHeight + abs(offsetY.toInt()))
                progressBar.translationY = (mHeight + abs(offsetY.toInt()) - progressBarHeight.toPx()) / 2f
                trackBar.translationY = (mHeight + abs(offsetY.toInt()) - progressBarHeight.toPx()) / 2f
                rightIndicatorTextView.translationY = (mHeight + abs(offsetY.toInt()) - progressBarHeight.toPx()) / 2f
            } else {
                // offsetY is positive.
                // check if offsetY is more than icon height minus parent height, will add more needed height to
                // the parent container if yes
                if (offsetY > mHeight - progressBarHeight.toPx()) {
                    progressBarContainer.layoutParams =
                        LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mHeight + abs(offsetY - (mHeight - progressBarHeight.toPx())).toInt())

                    progressBar.translationY = -offsetY / 2
                    trackBar.translationY = -offsetY / 2
                    rightIndicatorTextView.translationY = -offsetY / 2
                } else {
                    progressBarContainer.layoutParams =
                        LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mHeight)

                    progressBar.translationY = (mHeight - progressBarHeight.toPx()) / 2f - offsetY
                    trackBar.translationY = (mHeight - progressBarHeight.toPx()) / 2f - offsetY
                    rightIndicatorTextView.translationY = (mHeight - progressBarHeight.toPx()) / 2f - offsetY
                }
            }
        }
    }

    fun getValue(): Int = value

    companion object {
        const val COLOR_GREEN = 0
        const val COLOR_ORANGE = 1
        const val COLOR_RED = 2
        const val SIZE_SMALL = 4
        const val SIZE_MEDIUM = 6
        const val SIZE_LARGE = 8
    }
}

class ProgressBarIndicatorItemUnify(
    var indicatorPosition: Int,
    var indicatorText: CharSequence?,
    var indicatorAlign: Int = ALIGN_CENTER,
    var indicatorCustomView: View? = null
) {
    lateinit var itemRef: RelativeLayout
    lateinit var itemIndicatorRef: ImageView

    companion object {
        const val ALIGN_LEFT = 0
        const val ALIGN_CENTER = 1
        const val ALIGN_RIGHT = 2
    }
}