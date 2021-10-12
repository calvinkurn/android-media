package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.toPx

class ToggleViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var maskImageView:ToggleImageView
    lateinit var assetImageView: AssetImageView
    lateinit var toggleCountView: ToggleCountLayout

    var clipPath = Path()
    var clipRectF = RectF()

    fun getLayout() = R.layout.imagepicker_insta_toggle_view_group
    val cornerRadius = 4.toPx()

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.let {
            clipPath.reset()
            clipRectF.top = 0f
            clipRectF.left = 0f
            clipRectF.right = canvas.width.toFloat()
            clipRectF.bottom = canvas.height.toFloat()
            clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW)
            canvas.clipPath(clipPath)
        }

        super.dispatchDraw(canvas)
    }

    fun initViews(){
        maskImageView = findViewById(R.id.image_mask)
        toggleCountView = findViewById(R.id.toggle_count_layout)
        assetImageView = findViewById(R.id.src_image)

        maskImageView.scaleType = ImageView.ScaleType.FIT_XY
        maskImageView.onDrawableId = R.drawable.imagepicker_insta_ic_grey_mask
    }

    fun loadAsset(asset: Asset){
        assetImageView.loadAsset(asset)
    }
    fun loadAssetThumbnail(asset: Asset, height:Int){
        assetImageView.loadAssetThumbnail(asset,height)
    }

    fun setChecked(isChecked:Boolean){
        toggleCountView.toggle(isChecked)
        maskImageView.toggle(isChecked)
    }

}