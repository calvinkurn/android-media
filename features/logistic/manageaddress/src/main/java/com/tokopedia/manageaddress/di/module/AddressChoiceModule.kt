package com.tokopedia.manageaddress.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.ui.addresschoice.AddressListContract
import com.tokopedia.manageaddress.ui.addresschoice.AddressListPresenter
import dagger.Binds
import dagger.Module

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@Module
abstract class AddressChoiceModule {

    @Binds
    @ActivityScope
    abstract fun provideAddressListPresenter(presenter: AddressListPresenter): AddressListContract.Presenter

}