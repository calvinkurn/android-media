package com.tokopedia.play.broadcaster.view.custom.banner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroBannerBinding
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created By : Jonathan Darwin on November 02, 2022
 */
class PlayBroBannerView(
    context: Context,
    attrs: AttributeSet?
) : CardUnify2(context, attrs) {

    private val binding = ViewPlayBroBannerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var title: String = ""
        set(value) {
            field = value
            binding.tvTitle.text = value
        }

    var description: String = ""
        set(value) {
            field = value
            binding.tvDescription.text = value
        }

    var bannerIcon: Int = IconUnify.VIDEO
        set(value) {
            field = value
            binding.icBanner.setImage(value)
        }

    var onVisibilityChanged: ((currentVisibility: Int) -> Unit)? = null

    override fun setOnClickListener(l: OnClickListener?) {
        binding.root.setOnClickListener(l)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        onVisibilityChanged?.invoke(visibility)
    }
}
