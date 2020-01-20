package com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.di

import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.AddressListContract
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.AddressListPresenter
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