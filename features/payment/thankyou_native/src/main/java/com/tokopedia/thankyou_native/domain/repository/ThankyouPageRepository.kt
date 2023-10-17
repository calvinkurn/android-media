package com.tokopedia.thankyou_native.domain.repository

import android.os.Bundle

interface ThankyouPageRepository<T> {
    suspend fun getRemoteData(bundle: Bundle = Bundle()): T
}
