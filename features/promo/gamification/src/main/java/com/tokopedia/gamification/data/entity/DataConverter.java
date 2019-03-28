package com.tokopedia.gamification.data.entity;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String fromCrackBenefitEntityList(List<CrackBenefitEntity> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CrackBenefitEntity>>() {}.getType();
        String json = gson.toJson(countryLang, type);
        return json;
    }

    @TypeConverter
    public List<CrackBenefitEntity> toCrackBenefitEntityList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CrackBenefitEntity>>() {}.getType();
        List<CrackBenefitEntity> countryLangList = gson.fromJson(countryLangString, type);
        return countryLangList;
    }
 }