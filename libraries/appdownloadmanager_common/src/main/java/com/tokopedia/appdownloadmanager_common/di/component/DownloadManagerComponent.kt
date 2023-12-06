package com.tokopedia.appdownloadmanager_common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.appdownloadmanager_common.di.module.DownloadManagerModule
import com.tokopedia.appdownloadmanager_common.di.module.DownloadManagerViewModelModule
import com.tokopedia.appdownloadmanager_common.di.scope.DownloadManagerScope
import com.tokopedia.appdownloadmanager_common.presentation.bottomsheet.AppDownloadingBottomSheet
import com.tokopedia.appdownloadmanager_common.presentation.dialog.AppUpdateVersionDialog
import dagger.Component

@DownloadManagerScope
@Component(
    modules = [DownloadManagerModule::class, DownloadManagerViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface DownloadManagerComponent {
    fun inject(appDownloadingBottomSheet: AppDownloadingBottomSheet)
    fun inject(appUpdateVersionDialog: AppUpdateVersionDialog)
}
