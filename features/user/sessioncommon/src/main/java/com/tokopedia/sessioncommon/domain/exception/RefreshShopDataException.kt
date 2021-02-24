package com.tokopedia.sessioncommon.domain.exception

import com.tokopedia.network.exception.MessageErrorException

class RefreshShopDataException(errorMessage: String = DEFAULT_ERROR_MESSAGE): MessageErrorException(errorMessage) {
    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Failed to get shop basic data"
    }
}