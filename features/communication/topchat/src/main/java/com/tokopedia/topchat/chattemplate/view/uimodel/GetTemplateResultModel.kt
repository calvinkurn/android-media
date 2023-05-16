package com.tokopedia.topchat.chattemplate.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class GetTemplateResultModel(
    var isEnabled: Boolean = false,
    var listTemplate: ArrayList<Visitable<*>> = arrayListOf()
)
