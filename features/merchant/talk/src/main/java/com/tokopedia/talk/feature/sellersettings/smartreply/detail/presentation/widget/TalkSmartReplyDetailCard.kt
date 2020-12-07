package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_smart_reply_detail_card.view.*

class TalkSmartReplyDetailCard : BaseCustomView {

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
        View.inflate(context, R.layout.widget_smart_reply_detail_card, this)
    }

    fun setData(sellerName: String, sellerThumbnail: String) {
        talkSmartReplyDetailSellerName.text = sellerName
        talkSmartReplyDetailSellerThumbnail.loadImage(sellerThumbnail)
    }
}