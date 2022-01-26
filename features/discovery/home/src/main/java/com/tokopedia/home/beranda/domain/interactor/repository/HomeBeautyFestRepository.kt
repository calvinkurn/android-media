package com.tokopedia.home.beranda.domain.interactor.repository

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment

class HomeBeautyFestRepository {
    fun getBeautyFest(data: HomeDynamicChannelModel): Int {
        //beauty fest event will qualify if contains "isChannelBeautyFest":true
        return try {
            if (data.isBeautyFest)
                HomeRevampFragment.BEAUTY_FEST_TRUE
            else
                HomeRevampFragment.BEAUTY_FEST_FALSE
        } catch (e: Exception) {
            HomeRevampFragment.BEAUTY_FEST_NOT_SET
        }
    }
}