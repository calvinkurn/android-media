package com.tokopedia.groupchat.room.view.viewmodel

import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author : Steven 24/05/19
 */
@Parcelize
class DynamicButton(var buttonId : String = "",
                    var imageUrl: String = "",
                    var linkUrl: String = "",
                    var contentType: String = "",
                    var contentText: String = "",
                    var contentButtonText: String = "",
                    var contentLinkUrl: String = "",
                    var contentImageUrl: String = "",
                    var hasNotification: Boolean = false,
                    var tooltip: String = "",
                    var tooltipDuration: Int = 0,
                    var priority: Int = 0
) : BaseDynamicButton() {

    override fun type(typeFactory: DynamicButtonTypeFactory): Int {
        return typeFactory.type(this)
    }
}

