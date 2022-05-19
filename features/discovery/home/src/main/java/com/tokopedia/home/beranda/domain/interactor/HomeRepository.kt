package com.tokopedia.home.beranda.domain.interactor

import android.os.Bundle

interface HomeRepository<T> {
    suspend fun getRemoteData(bundle: Bundle = Bundle()): T
    suspend fun getCachedData(bundle: Bundle = Bundle()): T
}