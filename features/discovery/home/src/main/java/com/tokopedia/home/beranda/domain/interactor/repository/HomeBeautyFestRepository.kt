package com.tokopedia.home.beranda.domain.interactor.repository

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment

class HomeBeautyFestRepository {
    fun getBeautyFest(data: HomeDynamicChannelModel): Int {
        //beauty fest event will qualify if contains "isChannelBeautyFest":true
        return try {
            if (data.isCache) HomeRevampFragment.BEAUTY_FEST_NOT_SET
            else data.isBeautyFest
        } catch (e: Exception) {
            HomeRevampFragment.BEAUTY_FEST_NOT_SET
        }
    }
}