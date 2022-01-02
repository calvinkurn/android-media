package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.repository.HomeRevampRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitRepository
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class HomeBusinessUnitUseCase @Inject constructor(
    private val homeBusinessUnitRepository: HomeBusinessUnitRepository
) : HomeRevampRepository {

    fun getBuUnitTab(position: Int) {

    }

    override fun getHomeDataFlow(): Flow<HomeDynamicChannelModel?> {
        return flowOf()
    }

    override fun updateHomeData(): Flow<Result<Any>> {
        return flowOf()
    }

    override suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>> {
        return listOf()
    }

    override fun deleteHomeData() {

    }
}