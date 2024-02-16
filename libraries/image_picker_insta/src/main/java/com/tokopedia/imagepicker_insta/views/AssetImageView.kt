package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toPx

open class AssetImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var hasRoundCorner = false
    var clipPath = Path()
    var clipRectF = RectF()
    var radius = 4.toPx().toFloat()


    init {
        readDataFromAttrs(attrs)
    }

    private fun readDataFromAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AssetImageView, 0, 0)
            try {
                hasRoundCorner = typedArray.getBoolean(R.styleable.AssetImageView_image_picker_insta_has_round_corner, false)
            } finally {
                typedArray.recycle();
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (hasRoundCorner) {
            clipRectF.right = measuredWidth.toFloat()
            clipRectF.bottom = measuredHeight.toFloat()
            clipPath.addRoundRect(clipRectF, radius, radius, Path.Direction.CW)
            canvas?.clipPath(clipPath)
        }
        super.onDraw(canvas)

    }

    fun loadAsset(asset: Asset) {
        this.loadImage(asset.contentUri) {
            fitCenter()
        }
    }

    fun loadUriThumbnail(uri: Uri, height: Int) {

//        if (height < 1) {
            loadGlideThumbnail(uri)
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            try {
//                Glide.with(this)
//                    .load(context.contentResolver.loadThumbnail(uri, Size(height, height), null))
//                    .into(this)
//            } catch (th: Throwable) {
//                Timber.e(th)
//                loadGlideThumbnail(uri)
//            }
//
//        } else {
//            loadGlideThumbnail(uri)
//        }
    }

    fun loadGlideThumbnail(uri: Uri) {
        this.loadImage(uri) {
            thumbnailSize(0.33f)
            centerCrop()
        }
    }

    fun loadAssetThumbnail(asset: Asset, height: Int) {
        loadUriThumbnail(asset.contentUri, height)
    }

    fun removeAsset() {
        setImageDrawable(null)
    }
}
