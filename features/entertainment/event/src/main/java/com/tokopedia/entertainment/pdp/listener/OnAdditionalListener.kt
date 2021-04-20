package com.tokopedia.entertainment.pdp.listener

import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData

interface OnAdditionalListener {
    fun onClickAdditional(additonal : EventCheckoutAdditionalData)
}