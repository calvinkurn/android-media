package com.tokopedia.logisticinsurance.di;

import com.tokopedia.logisticdata.data.apiservice.InsuranceApi;
import com.tokopedia.logisticdata.data.module.LogisticNetworkModule;
import com.tokopedia.logisticdata.data.module.qualifier.InsuranceTnCScope;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticInsuranceApiQualifier;
import com.tokopedia.logisticdata.data.repository.InsuranceTnCDataStore;
import com.tokopedia.logisticdata.data.repository.InsuranceTnCRepository;
import com.tokopedia.logisticinsurance.domain.InsuranceTnCUseCase;
import com.tokopedia.logisticinsurance.view.InsuranceTnCContract;
import com.tokopedia.logisticinsurance.view.InsuranceTnCPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

@Module(includes = LogisticNetworkModule.class)
public class InsuranceTnCModule {

    public InsuranceTnCModule() {
    }

    @Provides
    @InsuranceTnCScope
    InsuranceTnCDataStore provideInsuranceTnCDataStore(
            @LogisticInsuranceApiQualifier InsuranceApi insuranceApi) {
        return new InsuranceTnCDataStore(insuranceApi);
    }

    @Provides
    @InsuranceTnCScope
    InsuranceTnCRepository provideInsuranceTnCRepository(InsuranceTnCDataStore insuranceTnCDataStore) {
        return new InsuranceTnCRepository(insuranceTnCDataStore);
    }

    @Provides
    @InsuranceTnCScope
    InsuranceTnCUseCase provideGetDistrictRequestUseCase(
            InsuranceTnCRepository insuranceTnCRepository
    ) {
        return new InsuranceTnCUseCase(insuranceTnCRepository);
    }

    @Provides
    @InsuranceTnCScope
    InsuranceTnCContract.Presenter provideInsuranceTnCPresenter(
            InsuranceTnCUseCase getDistrictRequestUseCase) {
        return new InsuranceTnCPresenter(getDistrictRequestUseCase);
    }

}
