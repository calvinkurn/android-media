package com.tokopedia.otp.verification

import com.tokopedia.otp.verification.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ade Fulki on 02/06/20.
 */

class DispatcherProviderTest: DispatcherProvider {
    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
}