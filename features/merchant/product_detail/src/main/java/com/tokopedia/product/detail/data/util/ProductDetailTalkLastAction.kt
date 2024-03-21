package com.tokopedia.product.detail.data.util

sealed class ProductDetailTalkLastAction
class ProductDetailTalkGoToReplyDiscussion(val questionId: String) : ProductDetailTalkLastAction()
object ProductDetailTalkGoToWriteDiscussion : ProductDetailTalkLastAction()
