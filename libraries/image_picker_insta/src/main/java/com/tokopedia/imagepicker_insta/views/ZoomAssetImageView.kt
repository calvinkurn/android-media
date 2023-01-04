package com.tokopedia.imagepicker_insta.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.ZoomInfo
import timber.log.Timber
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**
 * Source: https://github.com/k1slay/ZoomImageView
 * */

class ZoomAssetImageView : androidx.appcompat.widget.AppCompatImageView {

    private val textPaint = Paint()
    private val zoomMatrix = Matrix()
    private val baseMatrix = Matrix()
    private val preEventImgRect = RectF()
    private val matrixValues = FloatArray(9)
    private val zoomInterpolator = AccelerateDecelerateInterpolator()
    private var logText = ""
    private var handlingDismiss = false
    private var touchSlop: Float = 0F
    private var oldScale = MIN_SCALE
    private var panAnimator: ValueAnimator? = null
    private var zoomAnimator: ValueAnimator? = null
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null
    private var viewWidth = right - left - paddingLeft - paddingRight
    private var viewHeight = bottom - top - paddingTop - paddingBottom
    private lateinit var scroller: OverScroller
    private lateinit var tapDetector: GestureDetector
    private lateinit var scaleDetector: ScaleGestureDetector
    var debugInfoVisible = false
    var onDismiss: () -> Unit = {}
    var onDrawableLoaded: () -> Unit = {}
    var dismissProgressListener: (progress: Float) -> Unit = {}

    var mediaScaleTypeContract: MediaScaleTypeContract? = null
    var zoomInfo: ZoomInfo? = null
    var asset: Asset? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
        initTextPaint()
        scaleType = ScaleType.MATRIX
        scaleDetector = ScaleGestureDetector(context, scaleListener)
        scroller = OverScroller(context, DecelerateInterpolator())
        tapDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                return false
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                onClickListener?.onClick(this@ZoomAssetImageView)
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                onLongClickListener?.onLongClick(this@ZoomAssetImageView)
            }

            override fun onScroll(
                e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float
            ): Boolean {
                if (scaleDetector.isInProgress) return false
                val xAbs = distanceX.absoluteValue
                val yAbs = distanceY.absoluteValue
                panImage(distanceX, distanceY)
                return (xAbs > touchSlop || yAbs > touchSlop)
            }

            override fun onFling(
                e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float
            ): Boolean {
                if (currentZoom <= MIN_SCALE) return false
                val maxX = (preEventImgRect.width() - viewWidth).toInt()
                val maxY = (preEventImgRect.height() - viewHeight).toInt()
                flingRunnable.lastX = -preEventImgRect.left
                flingRunnable.lastY = -preEventImgRect.top
                scroller.fling(
                    flingRunnable.lastX.toInt(), flingRunnable.lastY.toInt(), -velocityX.toInt(),
                    -velocityY.toInt(), 0, maxX, 0, maxY
                )
                ViewCompat.postOnAnimation(this@ZoomAssetImageView, flingRunnable)
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                removeCallbacks(flingRunnable)
                scroller.forceFinished(true)
                displayRect?.let {
                    preEventImgRect.set(it)
                }
                panAnimator?.removeAllUpdateListeners()
                panAnimator?.cancel()
                return true
            }
        })

    }

    private val flingRunnable = object : Runnable {
        var lastX = 0F
        var lastY = 0F
        override fun run() {
            if (!scroller.isFinished && scroller.computeScrollOffset()) {
                val curX = scroller.currX.toFloat()
                val curY = scroller.currY.toFloat()
                panImage((curX - lastX), (curY - lastY))
                lastX = curX
                lastY = curY
                ViewCompat.postOnAnimation(this@ZoomAssetImageView, this)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val disallowIntercept =
            currentScale > MIN_SCALE || scaleDetector.isInProgress || handlingDismiss
        if (event?.action == MotionEvent.ACTION_UP) {
            if (handlingDismiss) {
                if (currentTransY.absoluteValue > dismissThreshold) {
                    onDismiss.invoke()
                } else {
                    animatePan(0F, currentTransY, 0F, 0F, dismissProgress)
                }
            }
        }
        parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
        return tapDetector.onTouchEvent(event) || return scaleDetector.onTouchEvent(event) || return true
    }

    private fun setZoom(scale: Float, x: Float, y: Float, absolute:Boolean = false) {
        if(absolute){
            zoomMatrix.setScale(scale, scale, x, y)
        }else{
            zoomMatrix.postScale(scale, scale, x, y)
        }
        setBounds()
        updateMatrix(drawMatrix)
        updateZoomInfo()
    }

    private fun updateMatrix(drawMatrix: Matrix) {
        logText = "tX: $currentTransX tY: $currentTransY"
        logText += " Scale: $currentScale"
        imageMatrix = drawMatrix
    }

    private fun setScale(scale: Float, x: Float, y: Float) {
        setZoom(scale, x, y)
    }

    private fun updateZoomInfo() {
        if (drawable != null) {
            zoomInfo?.scale = currentScale
            zoomInfo?.panX = currentTransX
            zoomInfo?.panY = currentTransY
            if (displayRect != null) {
                zoomInfo?.rectF = RectF()
                zoomInfo?.rectF?.set(displayRect!!)
            }
            zoomInfo?.matrix = Matrix()
            zoomInfo?.matrix?.set(imageMatrix)
            zoomInfo?.bmpHeight = drawable.intrinsicHeight
            zoomInfo?.bmpWidth = drawable.intrinsicWidth
        }
        Timber.d("update zoom: uri = ${asset?.contentUri}," +
                "scale=${zoomInfo?.scale}," +
                "matrix=${zoomInfo?.matrix}," +
                "bmpW=${zoomInfo?.bmpWidth}," +
                "bmpH=${zoomInfo?.bmpHeight}," +
                "left=${zoomInfo?.rectF?.left}," +
                "top=${zoomInfo?.rectF?.top}")
    }

    private fun setScaleAbsolute(scale: Float, x: Float, y: Float) {
        val zoom = when {
            scale > MAX_SCALE -> MAX_SCALE
            scale < MIN_SCALE -> MIN_SCALE
            else -> scale
        }

        cancelAnimation()
        animateZoom(oldScale, zoom, x, y)
    }

    private inline val drawableWidth: Int
        get() = drawable?.intrinsicWidth ?: 0

    private inline val drawableHeight: Int
        get() = drawable?.intrinsicHeight ?: 0

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (drawable != null) {
            onDrawableLoaded.invoke()
            resetZoom()
            zoomMatrix.set(imageMatrix)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        viewWidth = right - left - paddingLeft - paddingRight
        viewHeight = bottom - top - paddingTop - paddingBottom
        if (changed) resetZoom()
    }

    fun resetZoom(skipMatrix:Boolean = false) {
        val tempSrc = RectF(0F, 0F, drawableWidth.toFloat(), drawableHeight.toFloat())
        val tempDst = RectF(0F, 0F, viewWidth.toFloat(), viewHeight.toFloat())

        baseMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER)
        updateMinZoom(drawable)

        if (zoomInfo?.matrix != null && !skipMatrix) {
            imageMatrix.set(zoomInfo!!.matrix)
        } else {
            val scale = getInitialScale()
            zoomMatrix.setScale(scale,scale,viewWidth / 2f, viewHeight / 2f)
            setBounds()
            updateMatrix(drawMatrix)
            updateZoomInfo()
        }
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (detector.scaleFactor.isNaN() || detector.scaleFactor.isInfinite())
                return false
            if (currentScale >= MAX_SCALE && detector.scaleFactor > 1F) return true
            if (currentScale <= MIN_SCALE && detector.scaleFactor < 1F) return true

            oldScale = currentScale
            setScale(scaleDetector.scaleFactor, detector.focusX, detector.focusY)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            super.onScaleEnd(detector)
            oldScale = currentScale
            var needsReset = false
            var newScale = MIN_SCALE
            if (currentScale < MIN_SCALE) {
                newScale = MIN_SCALE
                needsReset = true
            }
            if (needsReset) setScaleAbsolute(newScale, detector.focusX, detector.focusY)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (debugInfoVisible) {
            canvas.drawText(logText, 10F, height - 10F, textPaint)
            val drawableBound = displayRect?.let {
                "Drawable: $it"
            } ?: ""
            canvas.drawText(drawableBound, 10F, 40F, textPaint)
        }
    }

    private fun initTextPaint() {
        textPaint.color = Color.RED
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 40F
    }

    private fun animateZoom(startZoom: Float, endZoom: Float, x: Float, y: Float) {

        zoomAnimator = ValueAnimator.ofFloat(startZoom, endZoom).apply {
            duration = VALUE_ANIMATOR_DURATION
            addUpdateListener {
                val scale = (it.animatedValue as Float) / currentScale
                setZoom(scale, x, y)
            }
            interpolator = zoomInterpolator
            start()
        }
    }

    fun loadAsset(asset: Asset, zoomInfo: ZoomInfo) {

        if (!isValidGlideContext()) {
            return
        }

        this.zoomInfo = zoomInfo
        this.asset = asset

        Glide.with(this)
            .load(asset.contentUri)
            .fitCenter()
            .apply(RequestOptions().override(width, height))
            .into(this)
    }

    private fun animatePan(
        startX: Float, startY: Float, endX: Float, endY: Float, dismissProgress: Float? = null
    ) {
        panAnimator = ValueAnimator.ofFloat(startX, startY, endX, endY).apply {
            duration = VALUE_ANIMATOR_DURATION
            addUpdateListener {
                val newX = (startX - endX) * it.animatedFraction
                val newY = (startY - endY) * it.animatedFraction
                panImage(startX - newX, startY - newY, setAbsolute = true)
                dismissProgress?.let { progress ->
                    if (1.0F - it.animatedFraction < progress) {
                        dismissProgressListener.invoke(1.0F - it.animatedFraction)
                    }
                }
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    handlingDismiss = false
                }

                override fun onAnimationCancel(p0: Animator) {
                    panImage(0F, 0F, setAbsolute = true)
                    handlingDismiss = false
                }

                override fun onAnimationRepeat(p0: Animator) {

                }
            })
            interpolator = zoomInterpolator
            start()
        }
    }

    private fun cancelAnimation() {
        zoomAnimator?.removeAllUpdateListeners()
        zoomAnimator?.cancel()
    }

    private fun panImage(x: Float, y: Float, setAbsolute: Boolean = false) {
        if (setAbsolute)
            zoomMatrix.setTranslate(x, y)
        else
            zoomMatrix.postTranslate(-x, -y)
        setBounds()
        updateMatrix(drawMatrix)
        updateZoomInfo()
    }

    private fun setBounds() {
        val rect = displayRect ?: return
        val height = rect.height()
        val width = rect.width()
        val viewHeight: Int = this.viewHeight
        var deltaX = 0f
        var deltaY = 0f
        when {
            height <= viewHeight -> {
                if (!handlingDismiss)
                    deltaY = (viewHeight - height) / 2 - rect.top
            }
            rect.top > 0 -> {
                deltaY = -rect.top
            }
            rect.bottom < viewHeight -> {
                deltaY = viewHeight - rect.bottom
            }
        }
        val viewWidth: Int = this.viewWidth
        when {
            width <= viewWidth -> {
                deltaX = (viewWidth - width) / 2 - rect.left
            }
            rect.left > 0 -> {
                deltaX = -rect.left
            }
            rect.right < viewWidth -> {
                deltaX = viewWidth - rect.right
            }
        }
        zoomMatrix.postTranslate(deltaX, deltaY)
    }

    private inline val dismissThreshold: Float
        get() = viewHeight / 3F

    private inline val currentScale: Float
        get() {
            zoomMatrix.getValues(matrixValues)
            return matrixValues[Matrix.MSCALE_X]
        }

    private inline val currentTransX: Float
        get() {
            zoomMatrix.getValues(matrixValues)
            return matrixValues[Matrix.MTRANS_X]
        }

    private inline val currentTransY: Float
        get() {
            zoomMatrix.getValues(matrixValues)
            return matrixValues[Matrix.MTRANS_Y]
        }

    private inline val dismissProgress: Float
        get() = currentTransY.absoluteValue / dismissThreshold

    private val displayRect: RectF? = RectF()
        get() {
            drawable?.let { d ->
                field?.set(
                    0f, 0f, d.intrinsicWidth.toFloat(), d.intrinsicHeight.toFloat()
                )
                drawMatrix.mapRect(field)
                return field
            }
            return null
        }

    private val drawMatrix: Matrix = Matrix()
        get() {
            field.set(baseMatrix)
            field.postConcat(zoomMatrix)
            return field
        }

    var currentZoom: Float
        get() = currentScale
        set(value) {
            oldScale = currentScale
            setScaleAbsolute(value, viewWidth / 2F, viewHeight / 2F)
        }

    companion object {
        var MAX_SCALE = 5F
        var MIN_SCALE = 1F
        private const val VALUE_ANIMATOR_DURATION = 10L
        const val portraitAR = 4 / 5f
        const val landscapeAR = 16 / 9f
    }

    override fun setOnClickListener(l: OnClickListener?) {
        this.onClickListener = l
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        this.onLongClickListener = l
    }

    fun getCenterCropZoom(): Float {
        val bmp = (drawable as? BitmapDrawable)?.bitmap
        if (bmp != null) {

            val dwidth = bmp.width
            val dheight = bmp.height

            val vwidth = width - paddingLeft - paddingRight;
            val vheight = height - paddingTop - paddingBottom;

            var scale: Float

            if (dwidth * vheight > vwidth * dheight) {
                //Landscape
                scale = (vheight / dheight.toFloat())
                scale = max(scale, MIN_SCALE)
            } else {
                //Portrait
                scale = (vwidth / dwidth.toFloat())
                scale = max(scale, MIN_SCALE)
            }

            return scale
        }
        return MIN_SCALE
    }

    fun setScaleToCenterCrop() {
        if(drawable!=null) {
            currentZoom = getCenterCropZoom()
        }
    }
    fun setScaleToCenterInside() {
        if(drawable!=null) {
            currentZoom = MIN_SCALE
        }
    }

    fun updateMinZoom(resource: Drawable?) {
        val bmp = (resource as? BitmapDrawable)?.bitmap
        if (bmp != null) {
            val ar = bmp.width / bmp.height.toFloat()

            val targetAR = if (ar > 1) {
                max(landscapeAR * 1 / ar, landscapeAR)
            } else {
                max(portraitAR * 1 / ar, ar)
            }
            val maxZoom = MAX_SCALE
            val finalMinZoom = max(1f, targetAR)
            MIN_SCALE = (min(maxZoom, finalMinZoom))
        }
    }

    private fun getInitialScale(): Float {
        if(drawable == null) return MIN_SCALE
        if (mediaScaleTypeContract?.getCurrentMediaScaleType() == MediaScaleType.MEDIA_CENTER_CROP) {
            return getCenterCropZoom()
        } else if (mediaScaleTypeContract?.getCurrentMediaScaleType() == MediaScaleType.MEDIA_CENTER_INSIDE) {
            return MIN_SCALE
        }
        return MIN_SCALE
    }

    fun removeAsset() {
        setImageDrawable(null)
    }

    private fun isValidGlideContext():Boolean{
        if(context is Activity) {
            val activity = context as Activity
            return !(activity.isFinishing || activity.isDestroyed)
        }
        return false
    }
}
