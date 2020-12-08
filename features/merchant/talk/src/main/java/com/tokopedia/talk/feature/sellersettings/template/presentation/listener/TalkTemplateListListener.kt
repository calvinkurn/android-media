package com.tokopedia.talk.feature.sellersettings.template.presentation.listener

interface TalkTemplateListListener {
    fun onEditClicked(template: String)
    fun onItemMove(originalIndex: Int, moveTo: Int)
    fun onItemRemoved(index: Int)
}