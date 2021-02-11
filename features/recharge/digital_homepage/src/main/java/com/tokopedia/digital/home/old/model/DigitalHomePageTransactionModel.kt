package com.tokopedia.digital.home.old.model

import com.tokopedia.digital.home.old.presentation.adapter.DigitalHomePageTypeFactory

class DigitalHomePageTransactionModel : DigitalHomePageItemModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }

}
