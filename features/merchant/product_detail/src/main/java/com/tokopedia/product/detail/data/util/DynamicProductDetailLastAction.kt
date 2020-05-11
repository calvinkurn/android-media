package com.tokopedia.product.detail.data.util

sealed class DynamicProductDetailLastAction
class DynamicProductDetailGoToReplyDiscussion(val questionId: String) : DynamicProductDetailLastAction()
object DynamicProductDetailGoToWriteDiscussion : DynamicProductDetailLastAction()