package com.tokopedia.logisticaddaddress.di.addnewaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.MapPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.MapSearchLocationBottomSheetPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 2019-05-09.
 */

@Module
class AddNewAddressModule {

    @Provides
    @AddNewAddressScope
    fun provideMapPresenter(
            @ApplicationContext context: Context): MapPresenter {
        return MapPresenter(context)
    }

    @Provides
    @AddNewAddressScope
    fun provideMapSearchLocationBottomSheetPresenter(autocompleteGeocodeUseCase: AutocompleteGeocodeUseCase)
            : MapSearchLocationBottomSheetPresenter {
        return MapSearchLocationBottomSheetPresenter(autocompleteGeocodeUseCase)
    }
}