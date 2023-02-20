package com.tokopedia.contactus.inboxtickets.view.inbox.uimodel

object UiObjectMapper {
    fun mapToSelectionFilterObject(data: List<String>): ArrayList<InboxFilterSelection> {
        val options = arrayListOf<InboxFilterSelection>()
        data.forEachIndexed { index, callOption ->
            options.add(InboxFilterSelection(index, callOption, isSelected = index == 0))
        }
        return options
    }
}
