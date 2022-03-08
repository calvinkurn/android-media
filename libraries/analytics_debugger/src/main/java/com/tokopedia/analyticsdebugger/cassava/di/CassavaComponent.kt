package com.tokopedia.analyticsdebugger.cassava.di

import android.content.Context
import com.tokopedia.analyticsdebugger.cassava.ui.debugger.AnalyticsDebuggerFragment
import com.tokopedia.analyticsdebugger.cassava.ui.MainValidatorActivity
import com.tokopedia.analyticsdebugger.cassava.ui.list.ValidatorListFragment
import com.tokopedia.analyticsdebugger.cassava.ui.main.MainValidatorFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * @author by furqan on 07/04/2021
 */
@Singleton
@Component(
        modules = [CassavaModule::class, CassavaViewModelModule::class],
)
interface CassavaComponent {

    fun inject(mainValidatorActivity: MainValidatorActivity)
    fun inject(mainValidatorActivity: MainValidatorFragment)
    fun inject(validatorListFragment: ValidatorListFragment)
    fun inject(debuggerFragment: AnalyticsDebuggerFragment)

    @Component.Builder
    interface Builder {
        fun build(): CassavaComponent
        @BindsInstance
        fun context(context: Context): Builder
    }
}