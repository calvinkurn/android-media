package com.tokopedia.mvc.presentation.intro.customviews

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.mvc.databinding.SmvcIntroCard1Binding
import com.tokopedia.unifycomponents.BaseCustomView

class IntroVoucherCardView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var binding: SmvcIntroCard1Binding? = null

    init {
        init()
    }

    private fun init() {
        binding = SmvcIntroCard1Binding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setTextForHtml(mainTitle: String, subtitle: Spanned) {
        binding?.title?.text = mainTitle
        binding?.subtitle?.text = subtitle
    }

    fun setData(mainTitle: String, subtitle: String, imageUrl: String = "") {
        binding?.title?.text = mainTitle
        binding?.subtitle?.text = subtitle
        if (imageUrl.isNotEmpty()) {
            binding?.imageView?.loadRemoteImageDrawable(imageUrl, imageUrl)
        }
    }
}
