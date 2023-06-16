package com.tokopedia.chatbot.chatbot2.view.uimodel.invoice

/**
 * Created by Hendri on 28/03/18.
 */

class AttachInvoiceSingleUiModel {
    var typeString: String = ""
    var type: Long = 0
    var code: String = ""
    var createdTime: String = ""
    var description: String = ""
    var url: String = ""
    var id: Long = 0
    var imageUrl: String = ""
    var status: String = ""
    var statusId: Long = 0
    var title: String = ""
    var amount: String = ""
    var color: String = ""
    var isSearchAllButton: Boolean = false
        private set
    constructor(
        typeString: String,
        type: Long,
        code: String,
        createdTime: String,
        description: String,
        url: String,
        id: Long,
        imageUrl: String,
        status: String,
        statusId: Long,
        title: String,
        amount: String,
        color: String
    ) {
        this.typeString = typeString
        this.type = type
        this.code = code
        this.createdTime = createdTime
        this.description = description
        this.url = url
        this.id = id
        this.imageUrl = imageUrl
        this.status = status
        this.statusId = statusId
        this.title = title
        this.amount = amount
        this.color = color
        isSearchAllButton = false
    }

    constructor(isSearchAllButton: Boolean) {
        this.isSearchAllButton = isSearchAllButton
    }
}
