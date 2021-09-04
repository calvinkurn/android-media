package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.imagepicker_insta.models.Asset

class AssetImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun loadAsset(asset: Asset){
        Glide.with(this)
            .load(asset.contentUri)
            .fitCenter()
            .into(this)
    }

    fun loadUriThumbnail(uri: Uri){
        Glide.with(this)
            .load(uri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(this)
    }

    fun loadAssetThumbnail(asset: Asset){
        loadUriThumbnail(asset.contentUri)
    }

    fun removeAsset(){
        setImageDrawable(null)
    }
}