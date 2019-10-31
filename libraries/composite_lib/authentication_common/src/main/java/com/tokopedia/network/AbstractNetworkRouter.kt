package com.tokopedia.network

import com.tokopedia.network.data.model.AbstractFingerprintModel

interface AbstractNetworkRouter {
    fun getFingerprintModel(): AbstractFingerprintModel
}