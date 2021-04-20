package com.tokopedia.talk.feature.write.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteHeaderModel
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_talk_write_header.view.*

class TalkWriteHeaderWidget : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_talk_write_header, this)
    }

    fun bind(talkWriteHeaderModel: TalkWriteHeaderModel) {
        this.talkWriteProductImage.loadImage(talkWriteHeaderModel.productImage)
        this.talkWriteProductName.text = talkWriteHeaderModel.productName
    }
}