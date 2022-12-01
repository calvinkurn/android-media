package com.tokopedia.merchantvoucher.common.gql.data

import com.tokopedia.abstraction.common.network.exception.MessageErrorException

class MessageTitleErrorException(val errorMessageTitle:String?,message:String?) : MessageErrorException(message)