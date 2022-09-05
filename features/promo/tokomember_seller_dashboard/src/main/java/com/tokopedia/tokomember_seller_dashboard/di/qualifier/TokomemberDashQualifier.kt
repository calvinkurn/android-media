package com.tokopedia.tokomember_seller_dashboard.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoroutineMainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoroutineBackgroundDispatcher