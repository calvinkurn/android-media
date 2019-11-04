package com.tokopedia.v2.home.base

import androidx.lifecycle.LiveData
import com.tokopedia.v2.home.model.pojo.HomeData
import com.tokopedia.v2.home.model.vo.Resource

interface HomeRepository {
    suspend fun getHomeDataWithCache(): LiveData<Resource<HomeData>>
}