package com.tokopedia.seller.menu.common.exception

import com.tokopedia.network.exception.MessageErrorException

class UserShopInfoException(val throwable: Throwable, val errorType: String) :
    MessageErrorException(throwable.message)