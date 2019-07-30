package com.tokopedia.explore.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** override this class for unit test by change all the val to Dispatchers.Uncofined **/
data class CoroutineThread (
        val MAIN: CoroutineDispatcher = Dispatchers.Main,
        val IO: CoroutineDispatcher = Dispatchers.IO
)