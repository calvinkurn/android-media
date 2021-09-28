package com.tokopedia.talk.feature.write.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.media.loader.loadImage
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.WidgetTalkWriteHeaderBinding
import com.tokopedia.unifycomponents.BaseCustomView

class TalkWriteHeaderWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private val binding = WidgetTalkWriteHeaderBinding.bind(this)

    private fun init() {
        View.inflate(context, R.layout.widget_talk_write_header, this)
    }

    fun bind(productName: String, productImage: String) {
        binding.talkWriteProductImage.loadImage(productImage)
        binding.talkWriteProductName.text = productName
    }
}