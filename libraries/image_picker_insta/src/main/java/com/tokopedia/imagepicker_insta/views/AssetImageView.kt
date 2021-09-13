package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.Asset
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

        Glide.with(this)
            .load(asset.contentUri)
            .fitCenter()
            .into(this)
    }

    fun loadUriThumbnail(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(this)
    }

    fun loadAssetThumbnail(asset: Asset) {
        loadUriThumbnail(asset.contentUri)
    }

    fun removeAsset() {
        setImageDrawable(null)
    }
}