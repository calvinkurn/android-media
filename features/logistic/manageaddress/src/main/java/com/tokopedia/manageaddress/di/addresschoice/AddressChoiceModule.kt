package com.tokopedia.checkout.subfeature.address_choice.view.di

import com.tokopedia.manageaddress.ui.addresschoice.AddressListContract
import com.tokopedia.manageaddress.ui.addresschoice.AddressListPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@Module
class AddressChoiceModule {

    @Provides
    @AddressChoiceScope
    fun provideAddressListPresenter(presenter: AddressListPresenter): AddressListContract.Presenter {
        return presenter
    }

}