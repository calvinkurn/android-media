package com.tokopedia.topchat.chattemplate.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactory

/**
 * Created by stevenfredian on 11/29/17.
 */
class TemplateChatUiModel : Visitable<TemplateChatTypeFactory> {
    var message: String? = null
    var isIcon = false
    var size = 0

    constructor() {
        isIcon = false
    }

    constructor(message: String?) {
        this.message = message
    }

    constructor(hasMessage: Boolean) {
        isIcon = !hasMessage
    }

    constructor(hasMessage: Boolean, size: Int) {
        isIcon = !hasMessage
        this.size = size
    }

    fun size(): Int {
        return size
    }

    override fun type(typeFactory: TemplateChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    val ellipsizedMessage: String?
        get() = if (message!!.length > 15) {
            message!!.substring(0, 16) + "..."
        } else message
}