package com.tokopedia.logisticaddaddress.di.addnewaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.logisticaddaddress.domain.mapper.AutofillMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutofillUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.MapPresenter
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
            @ApplicationContext context: Context,
            getDistrictUseCase: GetDistrictUseCase,
            getDistrictMapper: GetDistrictMapper,
            autofillUseCase: AutofillUseCase,
            autofillMapper: AutofillMapper): MapPresenter {
        return MapPresenter(context, getDistrictUseCase, getDistrictMapper, autofillUseCase, autofillMapper)
    }

    /*@Provides
    @AddNewAddressScope
    fun provideAutoCompleteGeocodeBottomSheetPresenter(autocompleteGeocodeUseCase: AutocompleteGeocodeUseCase,
                                                       autoCompleteGeocodeMapper: AutocompleteGeocodeMapper)
            : AutocompleteBottomSheetPresenter {
        return AutocompleteBottomSheetPresenter(autocompleteGeocodeUseCase, autoCompleteGeocodeMapper)
    }

    @Provides
    @AddNewAddressScope
    fun provideGetDistrictBottomSheetPresenter(getDistrictUseCase: GetDistrictUseCase,
                                               getDistrictMapper: GetDistrictMapper,
                                               autofillUseCase: AutofillUseCase,
                                               autofillMapper: AutofillMapper) : GetDistrictBottomSheetPresenter {
        return GetDistrictBottomSheetPresenter(getDistrictUseCase, getDistrictMapper, autofillUseCase, autofillMapper)
    }*/
}