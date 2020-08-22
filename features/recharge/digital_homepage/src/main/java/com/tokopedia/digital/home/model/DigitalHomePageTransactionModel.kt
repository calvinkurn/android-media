package com.tokopedia.digital.home.model

import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageAdapterFactory

class DigitalHomePageTransactionModel : DigitalHomePageItemModel() {

    override fun visitableId(): Int {
        return TRANSACTION_SECTION_ID
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is DigitalHomePageTransactionModel
    }

    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val TRANSACTION_SECTION_ID = -8
    }

}
