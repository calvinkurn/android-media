package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography


class TalkSmartReplyDetailCard : BaseCustomView {

    private var talkSmartReplyDetailSellerName: Typography? = null
    private var talkSmartReplyDetailSellerThumbnail: ImageUnify? = null
    private var talkSmartReplyDetailPreview: Typography? = null

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
        View.inflate(context, R.layout.widget_talk_smart_reply_detail_card, this)
        bindViews()
    }

    private fun bindViews() {
        talkSmartReplyDetailSellerName = findViewById(R.id.talkSmartReplyDetailSellerName)
        talkSmartReplyDetailSellerThumbnail = findViewById(R.id.talkSmartReplyDetailSellerThumbnail)
        talkSmartReplyDetailPreview = findViewById(R.id.talkSmartReplyDetailPreview)
    }

    fun setData(sellerName: String, sellerThumbnail: String) {
        talkSmartReplyDetailSellerName?.text = sellerName
        talkSmartReplyDetailSellerThumbnail?.loadImage(sellerThumbnail)
    }

    fun setSmartReply(smartReply: String) {
        talkSmartReplyDetailPreview?.text = smartReply
    }
}