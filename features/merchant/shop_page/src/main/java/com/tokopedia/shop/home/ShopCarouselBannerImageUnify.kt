package com.tokopedia.shop.home

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.ViewGroup

import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import java.io.File
import java.lang.Exception
import java.net.URI
import java.util.*

class ShopCarouselBannerImageUnify : AppCompatImageView {

    var type: Int = TYPE_RECT
    var cornerRadius: Int = 8
    var heightRatio: Float? = null
    var urlSrc: String = ""
        set(value) {
            field = value

            if (urlSrc.isNotEmpty()) {
                setImageUrl(value)
            }
        }

    var onUrlLoaded: ((isSuccess: Boolean) -> Any)? = null
    var isGif: Boolean? = null

    var customLoadingAVD: AnimatedVectorDrawableCompat? = null
    var disableShimmeringPlaceholder: Boolean = false
    private var shimmeringPlaceholder: AnimatedVectorDrawableCompat? = null
    private var placeholder: Int = 0

    private lateinit var rectF: RectF
    private val path = Path()
    private var paint = Paint()
    private var isMeasured = false
    private var isPlaceholderLoaded = false
    private var isImageLoaded = false
    private var isLoadError = false
    private var hasImageUrl = false
    private var prevScaleType = ScaleType.FIT_XY
    private val errorDrawable = LayerDrawable(arrayOf(AppCompatResources.getDrawable(context, com.tokopedia.unifycomponents.R.drawable.imagestate_error)))
    private var shimmerDrawable: AnimatedVectorDrawableCompat? = null
    private var prevWidth = 0

    private val reloadIcon = getIconUnifyDrawable(
            context,
            IconUnify.RELOAD,
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
    )
    private val reloadDrawable = LayerDrawable(
            arrayOf(
                    AppCompatResources.getDrawable(
                            context,
                            com.tokopedia.unifycomponents.R.drawable.ic_imagestate_reload
                    ), reloadIcon
            )
    )
    private val defaultPlaceholderDrawable = LayerDrawable(arrayOf(AppCompatResources.getDrawable(context, com.tokopedia.unifycomponents.R.drawable.imagestate_placeholder)))
    private val defaultBackgroundDrawable =
            ColorDrawable(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N75))

    var isRetryable = false

    var initialWidth: Int? = null
        set(value) {
            field = value

            if (value != null) {
                val h = heightRatio
                if (h != null) {
                    this.layoutParams.height = (value * h.toInt())

                    this.requestLayout()
                }
            }
        }

    constructor(context: Context) : super(context) {
        initPlaceholder()
    }

    constructor(context: Context, placeholder: Int) : super(context) {
        this.placeholder = placeholder
        initPlaceholder()
    }

    constructor(
            context: Context,
            customLoadingAvd: AnimatedVectorDrawableCompat?
    ) : super(context) {
        this.customLoadingAVD = customLoadingAvd
        initPlaceholder()
    }

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
        scaleType = ScaleType.FIT_XY

        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attributeSet, com.tokopedia.unifycomponents.R.styleable.UnifyImage)
        type = attributeArray.getInt(com.tokopedia.unifycomponents.R.styleable.UnifyImage_unify_image_type, TYPE_RECT)
        cornerRadius = attributeArray.getInt(com.tokopedia.unifycomponents.R.styleable.UnifyImage_unify_image_corner_radius, 8)
        placeholder =
                attributeArray.getResourceId(com.tokopedia.unifycomponents.R.styleable.UnifyImage_unify_image_placeholder, 0)
        var attrCustomLoadingAvd =
                attributeArray.getResourceId(com.tokopedia.unifycomponents.R.styleable.UnifyImage_unify_image_custom_loading_avd, 0)

        if (attrCustomLoadingAvd != 0) {
            customLoadingAVD = AnimatedVectorDrawableCompat.create(context, attrCustomLoadingAvd)
        }

        disableShimmeringPlaceholder = attributeArray.getBoolean(com.tokopedia.unifycomponents.R.styleable.UnifyImage_unify_image_disable_shimmering_placeholder, false)

        initPlaceholder()
        urlSrc = attributeArray.getString(com.tokopedia.unifycomponents.R.styleable.UnifyImage_unify_image_url_src) ?: ""

        attributeArray.recycle()
    }

    private fun initPlaceholder() {
        if (placeholder == 0 && drawable == null) {
            setImageDrawable(defaultPlaceholderDrawable)
            background = defaultBackgroundDrawable
        } else {
            setBackgroundResource(placeholder)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        resetPath()

        // reload dimension is 32dp for small image (< 256dp)
        var reloadPaddingH = measuredWidth / 2 - 16.toPx()
        var reloadIconPaddingH = measuredWidth / 2 - 12.toPx()
        var reloadPaddingV = measuredHeight / 2 - 16.toPx()
        var reloadIconPaddingV = measuredHeight / 2 - 12.toPx()

        if (measuredWidth.toDp() > 256 && measuredHeight.toDp() > 256) {
            // reload dimension is 48dp for large image (< 256dp)
            reloadPaddingH = measuredWidth / 2 - 24.toPx()
            reloadIconPaddingH = measuredWidth / 2 - 16.toPx()
            reloadPaddingV = measuredHeight / 2 - 24.toPx()
            reloadIconPaddingV = measuredHeight / 2 - 16.toPx()

            errorDrawable.setLayerInset(0,
                    measuredWidth / 2 - 128.toPx(),
                    measuredHeight / 2 - 128.toPx(),
                    measuredWidth / 2 - 128.toPx(),
                    measuredHeight / 2 - 128.toPx()
            )

            defaultPlaceholderDrawable.setLayerInset(0,
                    measuredWidth / 2 - 128.toPx(),
                    measuredHeight / 2 - 128.toPx(),
                    measuredWidth / 2 - 128.toPx(),
                    measuredHeight / 2 - 128.toPx()
            )
        }

        reloadDrawable.setLayerInset(0, reloadPaddingH, reloadPaddingV, reloadPaddingH, reloadPaddingV)
        reloadDrawable.setLayerInset(1, reloadIconPaddingH, reloadIconPaddingV, reloadIconPaddingH, reloadIconPaddingV)
    }

    override fun draw(canvas: Canvas) {
        val save = when {
            /**
             * saveLayer without flag was added in API 21
             */
            Build.VERSION.SDK_INT >= 21 -> canvas.saveLayer(
                    RectF(0f, 0f, width.toFloat(), height.toFloat()),
                    null
            )
            /**
             * saveLayer with flag is deprecated in API 26
             */
            else -> canvas.saveLayer(
                    RectF(0f, 0f, width.toFloat(), height.toFloat()),
                    null,
                    Canvas.ALL_SAVE_FLAG
            )
        }
        if (Build.VERSION.SDK_INT > 27) canvas.clipPath(path)
        super.draw(canvas)
        if (Build.VERSION.SDK_INT < 28) canvas.drawPath(path, paint)
        canvas.restoreToCount(save)
    }

    private fun resetPath() {
        path.reset()
        when (type) {
            TYPE_RECT -> path.addRoundRect(
                    rectF,
                    cornerRadius.toPx().toFloat(),
                    cornerRadius.toPx().toFloat(),
                    Path.Direction.CW
            )
            TYPE_CIRCLE -> path.addRoundRect(
                    rectF,
                    measuredHeight.toFloat(),
                    measuredHeight.toFloat(),
                    Path.Direction.CW
            )
            else -> path.addRoundRect(rectF, 0f, 0f, Path.Direction.CW)
        }

        path.close()
    }

    fun setGif(drawable: Int) {
        if(!context.isValidGlideContext()) return
        Glide.with(context).asGif().load(drawable).into(this)
    }

    fun setImageUrl(url: String, heightRatio: Float? = null, placeholderHeight: Int? = null, isSkipCache: Boolean = false) {
        if(!context.isValidGlideContext()) return
        this.post {
            heightRatio?.let {
                configHeightRatio(it)
            }

            startShimmer()
            hasImageUrl = true

            val uri: URI? = try {
                URI(url)
            } catch (e: Exception) {
                null
            }

            val ext: String = try {
                File(uri?.path).extension.toLowerCase(Locale.ENGLISH)
            } catch (e: Exception) {
                ""
            }

            if (placeholderHeight != null) {
                this.layoutParams.height = placeholderHeight
            }

            when (isGif) {
                true -> {
                    loadGif(url, placeholderHeight)
                }
                false -> {
                    loadImage(url, placeholderHeight, isSkipCache)
                }
                else -> {
                    if (ext.isNotEmpty() && (ext == "gif" || ext == "gifv")) {
                        loadGif(url, placeholderHeight)
                    } else {
                        loadImage(url, placeholderHeight, isSkipCache)
                    }
                }
            }
        }
    }

    private fun applyLoopingAnimatedVectorDrawable() {
        shimmeringPlaceholder?.registerAnimationCallback(object :
                Animatable2Compat.AnimationCallback() {
            val mainHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                mainHandler.post {
                    shimmeringPlaceholder!!.start()
                }
            }
        })
        shimmeringPlaceholder?.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (null != heightRatio) {
            var width = measuredWidth
            var height = measuredHeight
            when {
                width > 0 -> height = (width * heightRatio.orZero()).toInt()
                height > 0 -> width = (height / heightRatio.orZero()).toInt()
                else -> return
            }
            setMeasuredDimension(width, height)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (isLoadError || (!hasImageUrl && placeholder == 0)) {
            if (measuredWidth.toDp() <= 256 || measuredHeight.toDp() <= 256) {
                if (!isRetryable) {
                    prevScaleType = scaleType
                    scaleType = ScaleType.FIT_CENTER
                }
            }
        }

        if (drawable != null && drawable != defaultPlaceholderDrawable && drawable != errorDrawable && drawable != reloadDrawable) {
            this.background = null
        }

        if (measuredWidth != prevWidth && heightRatio != null) {
            configHeightRatio(heightRatio!!)
            prevWidth = measuredWidth
        }
    }

    private fun onError() {
        onUrlLoaded?.invoke(false)
        this@ShopCarouselBannerImageUnify.isImageLoaded = true
        this@ShopCarouselBannerImageUnify.isLoadError = true
        this.background = defaultBackgroundDrawable
    }

    private fun loadGif(url: String, placeholderHeight: Int?) {
        if(!context.isValidGlideContext()) return
        val mRequestBuilder: RequestBuilder<GifDrawable> = Glide.with(context).asGif().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

        if (heightRatio == null) mRequestBuilder.dontTransform()

        mRequestBuilder.load(url).error(com.tokopedia.unifycomponents.R.drawable.ic_broken_image)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            isFirstResource: Boolean
                    ): Boolean {
                        clearShimmerAnimation()

                        onError()
                        return false
                    }

                    override fun onResourceReady(
                            resource: GifDrawable?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                    ): Boolean {
                        clearShimmerAnimation()

                        onUrlLoaded?.invoke(true)
                        this@ShopCarouselBannerImageUnify.isImageLoaded = true

                        if (resource != null) {
                            renderSource(
                                    resource.intrinsicHeight,
                                    resource.intrinsicHeight,
                                    placeholderHeight
                            )
                        }
                        return false
                    }
                })
                .into(this)
    }

    private fun loadImage(url: String, placeholderHeight: Int?, isSkipCache: Boolean) {
        if(!context.isValidGlideContext()) return
        val mRequestBuilder: RequestBuilder<Bitmap> = Glide.with(context).asBitmap().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

        if (heightRatio == null) mRequestBuilder.dontTransform()

        mRequestBuilder.load(url).error(if (isRetryable) reloadDrawable else errorDrawable)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                    ): Boolean {
                        clearShimmerAnimation()

                        onError()
                        return false
                    }

                    override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                    ): Boolean {
                        clearShimmerAnimation()

                        onUrlLoaded?.invoke(true)
                        this@ShopCarouselBannerImageUnify.isImageLoaded = true

                        if (resource != null) {
                            renderSource(resource.width, resource.height, placeholderHeight)
                        }
                        return false
                    }
                })
                .skipMemoryCache(isSkipCache)
                .diskCacheStrategy(if(isSkipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.AUTOMATIC)
                .into(this)

    }

    private fun renderSource(w: Int, h: Int, placeholderHeight: Int?) {
        val hRatio: Float =
                (h.toDouble() / w.toDouble()).toFloat()
        if (this@ShopCarouselBannerImageUnify.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || placeholderHeight != null) {
            this@ShopCarouselBannerImageUnify.post {
                this@ShopCarouselBannerImageUnify.layoutParams.height =
                        (this@ShopCarouselBannerImageUnify.measuredWidth * hRatio).toInt()
                this@ShopCarouselBannerImageUnify.requestLayout()
            }
        }
    }

    private fun clearShimmerAnimation() {
        background = null
        shimmeringPlaceholder?.stop()
        shimmeringPlaceholder?.clearAnimationCallbacks()
    }

    override fun onDetachedFromWindow() {
        clearShimmerAnimation()

        super.onDetachedFromWindow()
    }

    private fun configHeightRatio(h: Float) {
        heightRatio = h
        layoutParams.height = (measuredWidth * h).toInt()
        requestLayout()
        isMeasured = true
    }

    private fun startShimmer() {
        shimmerDrawable = shimmerDrawable ?: AnimatedVectorDrawableCompat.create(
                context,
                com.tokopedia.unifycomponents.R.drawable.unify_loader_shimmer
        )

        shimmeringPlaceholder = customLoadingAVD ?: shimmerDrawable

        if (customLoadingAVD != null || !disableShimmeringPlaceholder) {
            background = shimmeringPlaceholder
            applyLoopingAnimatedVectorDrawable()
        } else {
            background = defaultBackgroundDrawable
        }

    }

    companion object {
        const val TYPE_RECT = 0
        const val TYPE_CIRCLE = 1
    }
}