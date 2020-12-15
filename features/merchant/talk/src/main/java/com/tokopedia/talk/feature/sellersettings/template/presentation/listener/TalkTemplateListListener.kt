package com.tokopedia.talk.feature.sellersettings.template.presentation.listener

interface TalkTemplateListListener {
    fun onEditClicked(template: String, index: Int)
    fun onItemMove(originalIndex: Int, moveTo: Int)
}