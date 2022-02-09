package com.tokopedia.topchat.chattemplate.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

class GetTemplateResultModel {
    var isSuccess = false
    var isEnabled = false
    var listTemplate: ArrayList<Visitable<*>> = arrayListOf()
}