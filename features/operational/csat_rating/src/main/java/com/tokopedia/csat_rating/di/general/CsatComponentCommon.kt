package com.tokopedia.csat_rating.di.general

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import dagger.Component

@CsatCommonScope
@Component(modules = [CsatModuleCommon::class], dependencies = [BaseAppComponent::class])
interface CsatComponentCommon {
    @get:ApplicationContext
    val context: Context
    fun provideCoroutineDispatchers(): CoroutineDispatchers
}
