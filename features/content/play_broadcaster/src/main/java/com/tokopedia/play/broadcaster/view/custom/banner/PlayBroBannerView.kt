package com.tokopedia.play.broadcaster.view.custom.banner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroBannerBinding

/**
 * Created By : Jonathan Darwin on November 02, 2022
 */
class PlayBroBannerView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

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
}
