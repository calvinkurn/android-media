package com.tokopedia.user.session.di

import android.content.Context
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import dagger.BindsInstance
import dagger.Component

@Component(modules = [SessionModule::class], )
interface UserSessionComponent {

    fun inject(worker: DataStoreMigrationWorker)

    @Component.Builder
    interface Builder {
        @BindsInstance fun context(context: Context): Builder
        fun build(): UserSessionComponent
    }

}