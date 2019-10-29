package com.tokopedia.home.account.di.module

import com.tokopedia.home.account.di.scope.PushNotifCheckerScope
import com.tokopedia.home.account.domain.GetPushNotifCheckerStatusUseCase
import com.tokopedia.home.account.presentation.PushNotifCheckerContract
import com.tokopedia.home.account.presentation.presenter.PushNotifCheckerPresenter
import dagger.Module
import dagger.Provides

@Module
class PushNotifCheckerModule {
    @Provides
    @PushNotifCheckerScope
    fun providePushNotifCheckerPresenter(getPushNotifCheckerStatusUseCase: GetPushNotifCheckerStatusUseCase): PushNotifCheckerContract.Presenter  {
        return PushNotifCheckerPresenter(getPushNotifCheckerStatusUseCase)
    }
}