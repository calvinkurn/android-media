package com.tokopedia.home.beranda.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable

interface HomeRevampRepository {
    fun getHomeDataFlow(): Flow<HomeDynamicChannelModel?>
    fun updateHomeData(): Flow<Result<Any>>
    suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>>
    fun deleteHomeData()
}

