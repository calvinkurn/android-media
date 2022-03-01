package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBeautyFestRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import javax.inject.Inject

class HomeBeautyFestUseCase @Inject constructor(private val homeBeautyFestRepository: HomeBeautyFestRepository) {
    fun getBeautyFest(data: HomeDynamicChannelModel): Int {
        //beauty fest event will qualify if contains "isChannelBeautyFest":true
        return homeBeautyFestRepository.getBeautyFest(data)
    }
}