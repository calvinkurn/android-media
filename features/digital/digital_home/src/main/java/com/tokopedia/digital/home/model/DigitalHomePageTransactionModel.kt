package com.tokopedia.digital.home.model

import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory

class DigitalHomePageTransactionModel : DigitalHomePageItemModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }

}
