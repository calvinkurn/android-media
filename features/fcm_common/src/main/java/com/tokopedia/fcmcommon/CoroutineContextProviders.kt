package com.tokopedia.fcmcommon

import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CoroutineContextProviders @Inject constructor(
        val Main: CoroutineContext,
        val IO: CoroutineContext
)