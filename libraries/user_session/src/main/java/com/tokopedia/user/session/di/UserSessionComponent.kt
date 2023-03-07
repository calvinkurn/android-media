package com.tokopedia.user.session.di

import android.content.Context
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SessionModule::class])
interface UserSessionComponent {

    fun inject(worker: DataStoreMigrationWorker)

    @Component.Builder
    interface Builder {
        @BindsInstance fun context(context: Context): Builder
        fun sessionModule(module: SessionModule): Builder
        fun build(): UserSessionComponent
    }

}