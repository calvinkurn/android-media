package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.filter.common.data.Option

interface EmptyProductListener {

    fun onGoToGlobalSearch()

    fun onChangeKeywordButtonClick()

    fun onRemoveFilterClick(option: Option)
}