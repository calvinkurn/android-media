package com.tokopedia.digital.home.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory

abstract class DigitalHomePageItemModel(
    var isLoaded: Boolean = false,
    var isSuccess: Boolean = false,
    var isEmpty: Boolean = false,
    var isLoadFromCloud: Boolean = true
) : Visitable<DigitalHomePageTypeFactory> {
    abstract override fun type(typeFactory: DigitalHomePageTypeFactory): Int
}