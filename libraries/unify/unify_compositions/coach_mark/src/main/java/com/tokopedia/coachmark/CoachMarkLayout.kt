/*
 * Copyright 2019 Tokopedia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Hendy
 * Edited by Meyta
 */

package com.tokopedia.coachmark

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.coachmark.util.ViewHelper

class CoachMarkLayout : FrameLayout {

    private var layoutRes: Int = 0
    private var isCancelable: Boolean = false
    private var circleBackgroundDrawableRes: Int = 0
    private var spacing: Int = 0
    private var arrowMargin: Int = 0
    private var arrowWidth: Int = 0

    // View
    private var viewGroup: ViewGroup? = null
    private var bitmap: Bitmap? = null
    private var lastTutorialView: View? = null
    private var viewPaint: Paint? = null
    private var arrowPaint: Paint? = null

    // listener
    private var showCaseListener: CoachMarkListener? = null

    internal var coachMarkContentPosition: CoachMarkContentPosition? = null

    private var highlightLocX: Int = 0
    private var highlightLocY: Int = 0

    // determined if this is last chain
    private var isStart: Boolean = false
    private var isLast: Boolean = false

    // path for arrow
    private var path: Path? = null
    private var textViewTitle: TextView? = null
    private var textViewDesc: TextView? = null
    private var prevButton: TextView? = null
    private var nextButton: TextView? = null
    private var viewGroupIndicator: ViewGroup? = null

    constructor(context: Context, builder: CoachMarkBuilder?) : super(context) {
        init(context, builder)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, null)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context,
                attrs: AttributeSet,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, null)
    }

    private fun init(context: Context, builder: CoachMarkBuilder?) {
        visibility = View.GONE

        if (isInEditMode) {
            return
        }

        initFrame()
        applyAttrs(context, builder)

        // setContentView
        initContent(context)

        isClickable = this.isCancelable
        isFocusable = this.isCancelable

        if (this.isCancelable) {
            this.setOnClickListener { v -> onNextClicked() }
        }
    }

    private fun onNextClicked() {
        if (showCaseListener != null) {
            if (this.isLast) {
                this@CoachMarkLayout.showCaseListener!!.onComplete()
            } else {
                this@CoachMarkLayout.showCaseListener!!.onNext()
            }
        }
    }

    fun setShowCaseListener(showCaseListener: CoachMarkListener) {
        this.showCaseListener = showCaseListener
    }

    fun showTutorial(view: View?,
                     title: String?,
                     text: String,
                     currentTutorIndex: Int,
                     tutorsListSize: Int,
                     coachMarkContentPosition: CoachMarkContentPosition,
                     tintBackgroundColor: Int,
                     customTarget: IntArray?, radius: Int) {

        this.isStart = currentTutorIndex == 0

        this.isLast = currentTutorIndex == tutorsListSize - 1
        this.coachMarkContentPosition = coachMarkContentPosition

        if (this.bitmap != null) {
            this.bitmap!!.recycle()
        }
        if (this.lastTutorialView != null) {
            this.lastTutorialView!!.isDrawingCacheEnabled = false
        }

        if (TextUtils.isEmpty(title)) {
            textViewTitle!!.visibility = View.GONE
        } else {
            textViewTitle!!.text = fromHtml(title)
            textViewTitle!!.visibility = View.VISIBLE
        }

        textViewDesc!!.text = fromHtml(text)

        if (prevButton != null) {
            if (isStart || isLast) {
                prevButton!!.isEnabled = false
            } else {
                prevButton!!.isEnabled = true
            }
        }

        if (nextButton != null) {
            if (isLast) {
                nextButton!!.background = ContextCompat.getDrawable(context, R.drawable.bg_coachmark_done)
            } else if (currentTutorIndex < tutorsListSize - 1) { // has next
                nextButton!!.isEnabled = true
                nextButton!!.background = ContextCompat.getDrawable(context, R.drawable.bg_coachmark_right)
            }
        }

        makeCircleIndicator(!isStart || !isLast, currentTutorIndex, tutorsListSize)

        if (view == null) {
            this.lastTutorialView = null
            this.bitmap = null
            this.highlightLocX = 0
            this.highlightLocY = 0
            moveViewToCenter()
        } else {
            this.lastTutorialView = view
            if (view.willNotCacheDrawing()) {
                view.setWillNotCacheDrawing(false)
            }
            view.isDrawingCacheEnabled = true
            view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
            if (tintBackgroundColor == 0) {
                this.bitmap = view.drawingCache
            } else {
                val bitmapTemp = view.drawingCache

                val bigBitmap = Bitmap.createBitmap(view.measuredWidth,
                        view.measuredHeight, Bitmap.Config.ARGB_8888)
                val bigCanvas = Canvas(bigBitmap)
                bigCanvas.drawColor(tintBackgroundColor)
                val paint = Paint()
                bigCanvas.drawBitmap(bitmapTemp, 0f, 0f, paint)

                this.bitmap = bigBitmap
            }

            //set custom target to view
            if (customTarget != null) {
                if (customTarget.size == 2) {
                    this.bitmap = ViewHelper.getCroppedBitmap(bitmap!!, customTarget, radius)
                } else if (customTarget.size == 4) {
                    this.bitmap = ViewHelper.getCroppedBitmap(bitmap!!, customTarget)
                }

                this.highlightLocX = customTarget[0] - radius
                this.highlightLocY = customTarget[1] - radius
            } else { // use view location as target
                val location = IntArray(2)
                view.getLocationInWindow(location)

                this.highlightLocX = location[0]
                this.highlightLocY = location[1] - ViewHelper.getStatusBarHeight(context)
            }

            this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (this@CoachMarkLayout.bitmap != null) {
                        moveViewBasedHighlight(this@CoachMarkLayout.highlightLocX,
                                this@CoachMarkLayout.highlightLocY,
                                this@CoachMarkLayout.highlightLocX + this@CoachMarkLayout.bitmap!!.width,
                                this@CoachMarkLayout.highlightLocY + this@CoachMarkLayout.bitmap!!.height)

                        if (Build.VERSION.SDK_INT < 16) {
                            this@CoachMarkLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        } else {
                            this@CoachMarkLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                        invalidate()
                    }
                }
            })
        }

        this.visibility = View.VISIBLE
    }

    fun hideTutorial() {
        this.visibility = View.INVISIBLE
    }

    private fun makeCircleIndicator(hasMoreOneCircle: Boolean,
                                    currentTutorIndex: Int,
                                    tutorsListSize: Int) {
        if (this.viewGroupIndicator != null) {
            if (hasMoreOneCircle) { // has more than 1 circle
                // already has circle indicator
                if (this.viewGroupIndicator!!.childCount == tutorsListSize) {
                    for (i in 0..tutorsListSize) {
                        val viewCircle = this.viewGroupIndicator!!.getChildAt(i)
                        viewCircle.isSelected = i == currentTutorIndex
                    }
                } else { //reinitialize, the size is different
                    this.viewGroupIndicator!!.removeAllViews()
                    val inflater = LayoutInflater.from(context)
                    for (i in 0..tutorsListSize) {
                        val viewCircle = inflater.inflate(R.layout.layout_indicator,
                                viewGroupIndicator,
                                false)
                        viewCircle.setBackgroundResource(this.circleBackgroundDrawableRes)
                        if (i == currentTutorIndex) {
                            viewCircle.isSelected = true
                        }
                        this.viewGroupIndicator!!.addView(viewCircle)
                    }
                }
            } else {
                this.viewGroupIndicator!!.removeAllViews()
            }
        }
    }

    fun closeTutorial() {
        visibility = View.GONE
        if (this.bitmap != null) {
            this.bitmap!!.recycle()
            this.bitmap = null
        }
        if (lastTutorialView != null) {
            this.lastTutorialView!!.isDrawingCacheEnabled = false
            this.lastTutorialView = null
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycleResources()
    }

    public override fun onDraw(canvas: Canvas) {
        if (bitmap == null || bitmap!!.isRecycled) {
            return
        }
        super.onDraw(canvas)
        canvas.drawBitmap(this.bitmap!!, this.highlightLocX.toFloat(), this.highlightLocY.toFloat(), viewPaint)

        if (path != null && this.viewGroup!!.visibility == View.VISIBLE) {
            canvas.drawPath(path!!, arrowPaint!!)
        }
    }

    private fun initFrame() {
        setWillNotDraw(false)

        viewPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        viewPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arrowPaint!!.color = Color.WHITE
        arrowPaint!!.style = Paint.Style.FILL

        setBackgroundColor(ContextCompat.getColor(context, R.color.shadow))
    }

    private fun applyAttrs(context: Context, builder: CoachMarkBuilder?) {
        // set Default value before check builder (might be null)
        this.layoutRes = R.layout.layout_coachmark

        this.spacing = resources.getDimension(R.dimen.spacing_lvl4).toInt()
        this.arrowMargin = this.spacing / 3
        this.arrowWidth = (1.5 * this.spacing).toInt()

        this.circleBackgroundDrawableRes = R.drawable.indicator_default

        if (builder == null) {
            return
        }

        this.layoutRes = if (builder.layoutRes != 0)
            builder.layoutRes
        else
            this.layoutRes

        this.circleBackgroundDrawableRes = if (builder.circleIndicatorBackgroundDrawableRes != 0)
            builder.circleIndicatorBackgroundDrawableRes
        else
            this.circleBackgroundDrawableRes

        this.isCancelable = true
    }

    private fun initContent(context: Context) {
        this.viewGroup = LayoutInflater.from(context).inflate(this.layoutRes, this, false) as ViewGroup

        val viewGroupTutorContent = viewGroup!!.findViewById<View>(R.id.view_group_tutor_content)
        textViewTitle = viewGroupTutorContent.findViewById(R.id.text_title)
        textViewTitle = viewGroupTutorContent.findViewById(R.id.text_title)
        textViewDesc = viewGroupTutorContent.findViewById(R.id.text_description)

        prevButton = viewGroupTutorContent.findViewById(R.id.text_previous)
        nextButton = viewGroupTutorContent.findViewById(R.id.text_next)

        viewGroupIndicator = viewGroupTutorContent.findViewById(R.id.view_group_indicator)

        if (prevButton != null) {
            prevButton!!.setOnClickListener { v ->
                if (showCaseListener != null) {
                    if (this@CoachMarkLayout.isStart) {
                        this@CoachMarkLayout.showCaseListener!!.onComplete()
                    } else {
                        this@CoachMarkLayout.showCaseListener!!.onPrevious()
                    }
                }
            }
        }
        if (nextButton != null) {
            nextButton!!.setOnClickListener { v -> onNextClicked() }
        }

        this.addView(viewGroup)
    }

    private fun moveViewBasedHighlight(highlightXstart: Int,
                                       highlightYstart: Int,
                                       highlightXend: Int,
                                       highlightYend: Int) {
        if (coachMarkContentPosition === CoachMarkContentPosition.UNDEFINED) {
            val widthCenter = this.width / 2
            val heightCenter = this.height / 2
            if (highlightYend <= heightCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.BOTTOM
            } else if (highlightYstart >= heightCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.TOP
            } else if (highlightXend <= widthCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.RIGHT
            } else if (highlightXstart >= widthCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.LEFT
            } else { // not fit anywhere
                // if bottom is bigger, put to bottom, else put it on top
                if (this.height - highlightYend > highlightYstart) {
                    coachMarkContentPosition = CoachMarkContentPosition.BOTTOM
                } else {
                    coachMarkContentPosition = CoachMarkContentPosition.TOP
                }
            }
        }

        val layoutParams: FrameLayout.LayoutParams
        when (coachMarkContentPosition) {
            CoachMarkContentPosition.RIGHT -> {
                val expectedWidth = width - highlightXend - 2 * this.spacing

                viewGroup!!.measure(View.MeasureSpec.makeMeasureSpec(expectedWidth, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                val viewGroupHeight = viewGroup!!.measuredHeight

                layoutParams = FrameLayout.LayoutParams(
                        expectedWidth,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.END)
                layoutParams.rightMargin = this.spacing
                layoutParams.leftMargin = this.spacing
                layoutParams.bottomMargin = 0

                // calculate diff top height between object and the content;
                val hightLightHeight = highlightYend - highlightYstart
                val diffHeight = hightLightHeight - viewGroupHeight

                // check top margin. top should not out of window
                val expectedTopMargin = highlightYstart + diffHeight / 2
                checkMarginTopBottom(expectedTopMargin, layoutParams, viewGroupHeight)

                setLayoutViewGroup(layoutParams)

                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterY = (highlightYend + highlightYstart) / 2
                    val recalcArrowWidth = getRecalculateArrowWidth(highLightCenterY, height)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        path = Path()
                        path!!.moveTo((highlightXend + this.arrowMargin).toFloat(), highLightCenterY.toFloat())
                        path!!.lineTo((highlightXend + this.spacing).toFloat(),
                                (highLightCenterY - arrowWidth / 2).toFloat())
                        path!!.lineTo((highlightXend + this.spacing).toFloat(),
                                (highLightCenterY + arrowWidth / 2).toFloat())
                        path!!.close()
                    }
                }
            }
            CoachMarkContentPosition.LEFT -> {
                val expectedWidth = highlightXstart - 2 * this.spacing

                viewGroup!!.measure(View.MeasureSpec.makeMeasureSpec(expectedWidth, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                val viewGroupHeight = viewGroup!!.measuredHeight

                layoutParams = FrameLayout.LayoutParams(
                        expectedWidth,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.START)
                layoutParams.leftMargin = this.spacing
                layoutParams.rightMargin = this.spacing
                layoutParams.bottomMargin = 0

                // calculate diff top height between object and the content;
                val hightLightHeight = highlightYend - highlightYstart
                val diffHeight = hightLightHeight - viewGroupHeight

                // check top margin. top should not out of window
                val expectedTopMargin = highlightYstart + diffHeight / 2
                checkMarginTopBottom(expectedTopMargin, layoutParams, viewGroupHeight)

                setLayoutViewGroup(layoutParams)

                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterY = (highlightYend + highlightYstart) / 2
                    val recalcArrowWidth = getRecalculateArrowWidth(highLightCenterY, height)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        path = Path()
                        path!!.moveTo((highlightXstart - this.arrowMargin).toFloat(), highLightCenterY.toFloat())
                        path!!.lineTo((highlightXstart - this.spacing).toFloat(),
                                (highLightCenterY - arrowWidth / 2).toFloat())
                        path!!.lineTo((highlightXstart - this.spacing).toFloat(),
                                (highLightCenterY + arrowWidth / 2).toFloat())
                        path!!.close()
                    }
                }
            }
            CoachMarkContentPosition.BOTTOM -> {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.TOP)
                layoutParams.topMargin = highlightYend + this.spacing
                layoutParams.leftMargin = this.spacing
                layoutParams.rightMargin = this.spacing
                layoutParams.bottomMargin = 0

                setLayoutViewGroup(layoutParams)

                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterX = (highlightXend + highlightXstart) / 2
                    val recalcArrowWidth = getRecalculateArrowWidth(highLightCenterX, width)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        path = Path()
                        path!!.moveTo(highLightCenterX.toFloat(), (highlightYend + this.arrowMargin).toFloat())
                        path!!.lineTo((highLightCenterX - recalcArrowWidth / 2).toFloat(),
                                (highlightYend + this.spacing).toFloat())
                        path!!.lineTo((highLightCenterX + recalcArrowWidth / 2).toFloat(),
                                (highlightYend + this.spacing).toFloat())
                        path!!.close()
                    }
                }
            }
            CoachMarkContentPosition.TOP -> {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM)
                layoutParams.bottomMargin = height - highlightYstart + this.spacing
                layoutParams.topMargin = 0
                layoutParams.leftMargin = this.spacing
                layoutParams.rightMargin = this.spacing

                setLayoutViewGroup(layoutParams)

                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterX = (highlightXend + highlightXstart) / 2
                    val recalcArrowWidth = getRecalculateArrowWidth(highLightCenterX, width)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        path = Path()
                        path!!.moveTo(highLightCenterX.toFloat(), (highlightYstart - this.arrowMargin).toFloat())
                        path!!.lineTo((highLightCenterX - recalcArrowWidth / 2).toFloat(),
                                (highlightYstart - this.spacing).toFloat())
                        path!!.lineTo((highLightCenterX + recalcArrowWidth / 2).toFloat(),
                                (highlightYstart - this.spacing).toFloat())
                        path!!.close()
                    }
                }
            }
            CoachMarkContentPosition.UNDEFINED -> moveViewToCenter()
        }
    }

    private fun setLayoutViewGroup(params: FrameLayout.LayoutParams) {
        this.viewGroup!!.visibility = View.INVISIBLE

        this.viewGroup!!.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                this@CoachMarkLayout.viewGroup!!.visibility = View.VISIBLE
                this@CoachMarkLayout.viewGroup!!.removeOnLayoutChangeListener(this)
            }
        })
        this.viewGroup!!.layoutParams = params
        invalidate()
    }

    private fun getRecalculateArrowWidth(highlightCenter: Int, maxWidthOrHeight: Int): Int {
        var recalcArrowWidth = arrowWidth
        val safeArrowWidth = this.spacing + arrowWidth / 2
        if (highlightCenter < safeArrowWidth || highlightCenter > maxWidthOrHeight - safeArrowWidth) {
            recalcArrowWidth = 0
        }
        return recalcArrowWidth
    }

    private fun moveViewToCenter() {
        coachMarkContentPosition = CoachMarkContentPosition.UNDEFINED

        val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER)
        layoutParams.rightMargin = this.spacing
        layoutParams.leftMargin = this.spacing
        layoutParams.bottomMargin = this.spacing
        layoutParams.topMargin = this.spacing

        setLayoutViewGroup(layoutParams)
        this.path = null
    }

    private fun checkMarginTopBottom(expectedTopMargin: Int,
                                     layoutParams: FrameLayout.LayoutParams,
                                     viewHeight: Int) {
        if (expectedTopMargin < this.spacing) {
            layoutParams.topMargin = this.spacing
        } else {
            // check bottom margin. bottom should not out of window
            val prevActualHeight = expectedTopMargin + viewHeight + this.spacing
            if (prevActualHeight > height) {
                val diff = prevActualHeight - height
                layoutParams.topMargin = expectedTopMargin - diff
            } else {
                layoutParams.topMargin = expectedTopMargin
            }
        }
    }

    private fun recycleResources() {
        if (this.bitmap != null) {
            this.bitmap!!.recycle()
        }
        this.bitmap = null
        if (this.lastTutorialView != null) {
            this.lastTutorialView!!.isDrawingCacheEnabled = false
        }
        this.lastTutorialView = null
        this.viewPaint = null
    }

    private fun fromHtml(html: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }
}
