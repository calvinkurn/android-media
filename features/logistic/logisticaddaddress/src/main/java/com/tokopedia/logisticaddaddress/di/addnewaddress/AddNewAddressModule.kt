package com.tokopedia.logisticaddaddress.di.addnewaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.logisticaddaddress.data.AddressRepository
import com.tokopedia.logisticaddaddress.domain.mapper.*
import com.tokopedia.logisticaddaddress.domain.usecase.*
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation.DistrictRecommendationBottomSheetPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapPresenter
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 2019-05-09.
 */

@Module
class AddNewAddressModule {

    @Provides
    @AddNewAddressScope
    fun providePinpointMapPresenter(
            getDistrictUseCase: GetDistrictUseCase,
            getDistrictMapper: GetDistrictMapper,
            autofillUseCase: AutofillUseCase,
            districtBoundaryUseCase: DistrictBoundaryUseCase,
            districtBoundaryMapper: DistrictBoundaryMapper): PinpointMapPresenter {
        return PinpointMapPresenter(getDistrictUseCase, getDistrictMapper, autofillUseCase,
                districtBoundaryUseCase, districtBoundaryMapper)
    }

    @Provides
    @AddNewAddressScope
    fun provideAddEditAddressPresenter(
            @ApplicationContext context: Context,
            addAddressUseCase: AddAddressUseCase,
            addAddressMapper: AddAddressMapper): AddEditAddressPresenter {
        return AddEditAddressPresenter(context, addAddressUseCase, addAddressMapper)
    }

    @Provides
    @AddNewAddressScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @Provides
    @AddNewAddressScope
    fun provideDistrictRecommendationPresenter(
            districtRecommendationUseCase: DistrictRecommendationUseCase,
            districtRecommendationMapper: DistrictRecommendationMapper): DistrictRecommendationBottomSheetPresenter {
        return DistrictRecommendationBottomSheetPresenter(districtRecommendationUseCase, districtRecommendationMapper)
    }


}