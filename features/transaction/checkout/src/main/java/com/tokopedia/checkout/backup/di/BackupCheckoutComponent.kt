package com.tokopedia.checkout.backup.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.backup.view.BackupCheckoutFragment
import dagger.Component

@ActivityScope
@Component(modules = [BackupCheckoutModule::class, BackupCheckoutViewModelModule::class], dependencies = [BaseAppComponent::class])
interface BackupCheckoutComponent {

    fun inject(backupCheckoutFragment: BackupCheckoutFragment)
}
