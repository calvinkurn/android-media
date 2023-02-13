package com.tokopedia.mvc.presentation.intro.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.databinding.SmvcIntroCard2Binding
import com.tokopedia.unifycomponents.BaseCustomView

class VoucherTypeCardView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var binding: SmvcIntroCard2Binding? = null

    init {
        init()
    }

    private fun init() {
        binding = SmvcIntroCard2Binding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setData(mainTitle: String, subtitle: String, imageUrl: String) {
        binding?.title?.text = mainTitle
        binding?.subtitle?.text = subtitle
        if (imageUrl.isNotEmpty()) {
            binding?.imageView?.loadImage(imageUrl)
        }
    }
}
