package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.imagepicker_insta.models.Asset

class AssetImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun loadAsset(asset: Asset){
        val path = asset.assetPath
        Glide.with(this)
            .load(path)
            .into(this)
    }

    fun removeAsset(){
        setImageDrawable(null)
    }
}