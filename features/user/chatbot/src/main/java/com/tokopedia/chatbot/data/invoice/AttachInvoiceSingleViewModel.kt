package com.tokopedia.chatbot.data.invoice

/**
 * Created by Hendri on 28/03/18.
 */

class AttachInvoiceSingleViewModel {
    var typeString: String = ""
    var type: Int = 0
    var code: String = ""
    var createdTime: String = ""
    var description: String = ""
    var url: String = ""
    var id: Long = 0
    var imageUrl: String = ""
    var status: String = ""
    var statusId: Int = 0
    var title: String = ""
    var amount: String = ""
    var isSearchAllButton: Boolean = false
        private set
    constructor(typeString: String, type: Int, code: String, createdTime: String,
                description: String, url: String, id: Long, imageUrl: String,
                status: String, statusId: Int, title: String, amount: String) {
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
        isSearchAllButton = false
    }

    constructor(isSearchAllButton: Boolean) {
        this.isSearchAllButton = isSearchAllButton
    }

}
