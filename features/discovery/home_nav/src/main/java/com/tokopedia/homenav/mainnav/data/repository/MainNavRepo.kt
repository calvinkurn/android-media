package com.tokopedia.homenav.mainnav.data.repository

import com.tokopedia.homenav.mainnav.data.pojo.MainNavPojo
import kotlinx.coroutines.flow.Flow

interface MainNavRepo {
    fun getMainNavData(): Flow<MainNavPojo?>
}