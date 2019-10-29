package com.tokopedia.common.travel.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.common.travel.data.PhoneCodeRepository
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode

import javax.inject.Inject

/**
 * @author by jessica on 2019-09-10
 */

class GetPhoneCodeByIdUseCase @Inject constructor(val phoneCodeRepository: PhoneCodeRepository) {
    fun execute(paramId: String): CountryPhoneCode {
        val countries = phoneCodeRepository.getCountryById(paramId)
        var countryPhoneCode = CountryPhoneCode()
        if (countries.isNotEmpty()) {
            countryPhoneCode.countryName = countries.first().countryName
            countryPhoneCode.countryId = countries.first().countryId
            countryPhoneCode.countryPhoneCode = countries.first().phoneCode.toString()
        }
        return countryPhoneCode
    }
}