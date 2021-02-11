package com.tokopedia.promotionstarget.presentation.ui

import kotlinx.coroutines.sync.Mutex

object Locks {
    val notificationMutex = Mutex()
}