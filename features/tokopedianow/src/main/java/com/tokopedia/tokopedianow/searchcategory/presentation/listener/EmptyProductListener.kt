package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.filter.common.data.Option

interface EmptyProductListener {

    fun onFindInTokopediaClick()

    fun goToTokopediaNowHome()

    fun onRemoveFilterClick(option: Option)
}