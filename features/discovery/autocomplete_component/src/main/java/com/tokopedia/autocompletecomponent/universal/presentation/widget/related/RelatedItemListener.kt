package com.tokopedia.autocompletecomponent.universal.presentation.widget.related

interface RelatedItemListener {
    fun onRelatedItemClick(data: RelatedItemDataView)
    fun onRelatedItemImpressed(data: RelatedItemDataView)
}