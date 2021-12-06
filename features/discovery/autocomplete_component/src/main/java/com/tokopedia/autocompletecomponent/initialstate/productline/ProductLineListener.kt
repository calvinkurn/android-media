package com.tokopedia.autocompletecomponent.initialstate.productline

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch

interface ProductLineListener {

    fun onProductLineClicked(item: BaseItemInitialStateSearch)
}