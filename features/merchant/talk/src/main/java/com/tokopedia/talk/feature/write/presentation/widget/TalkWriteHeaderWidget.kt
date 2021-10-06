package com.tokopedia.talk.feature.write.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.media.loader.loadImage
import com.tokopedia.talk.databinding.WidgetTalkWriteHeaderBinding
import com.tokopedia.unifycomponents.BaseCustomView

class TalkWriteHeaderWidget : BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = WidgetTalkWriteHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    fun bind(productName: String, productImage: String) {
        binding.talkWriteProductImage.loadImage(productImage)
        binding.talkWriteProductName.text = productName
    }
}