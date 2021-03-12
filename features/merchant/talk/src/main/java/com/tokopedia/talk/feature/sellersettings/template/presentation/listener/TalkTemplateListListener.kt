package com.tokopedia.talk.feature.sellersettings.template.presentation.listener

import com.tokopedia.talk.feature.sellersettings.template.presentation.adapter.TalkTemplateListViewHolder

interface TalkTemplateListListener {
    fun onEditClicked(template: String, index: Int)
    fun onItemMove(originalIndex: Int, moveTo: Int)
    fun onDrag(viewHolder: TalkTemplateListViewHolder)
}