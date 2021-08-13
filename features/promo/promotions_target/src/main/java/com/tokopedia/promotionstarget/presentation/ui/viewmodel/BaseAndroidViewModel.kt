package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(private val baseDispatcher: CoroutineDispatcher, app:Application): AndroidViewModel(app), CoroutineScope {
    @VisibleForTesting
    val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + masterJob
}