package com.tokopedia.loginregister.invalid_phone_number.domain

import com.tokopedia.loginregister.invalid_phone_number.data.model.RegisterCheckModel

interface InvalidPhoneNumberUseCaseContract {
    suspend fun getData(phoneNumber: String): RegisterCheckModel
}