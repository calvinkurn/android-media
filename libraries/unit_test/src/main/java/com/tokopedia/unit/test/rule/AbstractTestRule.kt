package com.tokopedia.unit.test.rule

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.test.TestDispatcher

interface AbstractTestRule {
    val dispatchers: CoroutineDispatchers
    val coroutineDispatcher: TestDispatcher
}
