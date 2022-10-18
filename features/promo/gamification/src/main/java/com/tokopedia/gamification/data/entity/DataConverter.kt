package com.tokopedia.gamification.data.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    @TypeConverter
    fun fromCrackBenefitEntityList(countryLang: List<CrackBenefitEntity>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<CrackBenefitEntity?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCrackBenefitEntityList(countryLangString: String?): List<CrackBenefitEntity>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<CrackBenefitEntity?>?>() {}.type
        return gson.fromJson(countryLangString, type)
    }
}