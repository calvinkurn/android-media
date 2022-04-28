package com.tokopedia.loginregister.inactive_phone_number.domain

import com.tokopedia.loginregister.inactive_phone_number.data.model.RegisterCheckModel

interface InactivePhoneNumberUseCaseContract {
    suspend fun getData(phoneNumber: String): RegisterCheckModel
}