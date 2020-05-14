package com.tokopedia.withdraw.domain.exception

class SubmitWithdrawalException(val errorMessage: String) : Throwable(errorMessage)