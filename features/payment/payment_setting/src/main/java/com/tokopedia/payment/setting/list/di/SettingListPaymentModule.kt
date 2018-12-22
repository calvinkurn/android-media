package com.tokopedia.payment.setting.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.payment.setting.detail.di.DetailCreditCardScope
import com.tokopedia.payment.setting.list.view.presenter.SettingListPaymentPresenter
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@Module
class SettingListPaymentModule {

    @DetailCreditCardScope
    @Provides
    fun providePresenter(@ApplicationContext context: Context) : SettingListPaymentPresenter {
        return SettingListPaymentPresenter(UserSession(context))
    }
}
