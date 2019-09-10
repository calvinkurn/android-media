package com.tokopedia.common.travel.domain

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.tokopedia.common.travel.data.PhoneCodeRepository
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode

import javax.inject.Inject

/**
 * @author by jessica on 2019-09-10
 */

class GetPhoneCodeByIdUseCase @Inject constructor(val phoneCodeRepository: PhoneCodeRepository) {
    fun execute(paramId: String): MutableLiveData<CountryPhoneCode> {
        return Transformations.map(phoneCodeRepository.getCountryById(paramId)) {
            var countryPhoneCode = CountryPhoneCode()
            if (it.isNotEmpty()) {
                countryPhoneCode.countryName = it.first().countryName
                countryPhoneCode.countryId = it.first().countryId
                countryPhoneCode.countryPhoneCode = it.first().phoneCode.toString()
            }
            countryPhoneCode
        } as MutableLiveData<CountryPhoneCode>
    }
}