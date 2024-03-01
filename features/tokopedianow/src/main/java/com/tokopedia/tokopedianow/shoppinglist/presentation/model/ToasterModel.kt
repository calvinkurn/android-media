package com.tokopedia.tokopedianow.shoppinglist.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL

data class ToasterModel(
    val text: String,
    val duration: Int = LENGTH_SHORT,
    val type: Int = TYPE_NORMAL,
    val actionText: String = String.EMPTY,
    val productId: String = String.EMPTY,
    val event: Event
) {
    enum class Event {
        ADD_WISHLIST,
        DELETE_WISHLIST
    }
}
