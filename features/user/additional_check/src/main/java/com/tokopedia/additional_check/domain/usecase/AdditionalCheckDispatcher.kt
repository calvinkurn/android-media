package com.tokopedia.additional_check.domain.usecase

/**
 * Created by Yoris Prayogo on 26/05/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class AdditionalCheckDispatcher @Inject constructor() {
    open val Main: CoroutineContext = Dispatchers.Main
    open val IO: CoroutineContext = Dispatchers.IO
}