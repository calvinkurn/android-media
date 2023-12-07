package com.tokopedia.shareexperience.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shareexperience.databinding.ShareexperienceShareLinkBinding

class ShareExLinkView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ShareexperienceShareLinkBinding? = null

    init {
        initViews()
    }

    private fun initViews() {
        binding = ShareexperienceShareLinkBinding.inflate(LayoutInflater.from(context), this)
        binding?.shareexTvTitleLink?.text = "Samsung Official Store"
        binding?.shareexTvCommisionLink?.text = "Komisi hingga 10%/barang terjual"
        binding?.shareexTvLink?.text = "tokopedia.link"
        binding?.shareexIvImageThumbnailLink?.loadImage("https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png")
        binding?.shareexLabelLink?.text = "Komisi Ekstra"
        binding?.shareexTvDate?.text = "Hingga 31 Des 2022"
    }
}
