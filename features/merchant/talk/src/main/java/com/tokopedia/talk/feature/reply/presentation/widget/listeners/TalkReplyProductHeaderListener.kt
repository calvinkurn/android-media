package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface TalkReplyProductHeaderListener {
    fun onProductClicked()
    fun onProductCardClicked(productName: String, position: Int)
    fun onProductCardImpressed(productName: String, position: Int)
}