package com.tokopedia.core.manage.general.districtrecommendation.domain.usecase;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.manage.general.districtrecommendation.data.repository.DistrictRecommendationRepository;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.AddressResponse;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class GetDistrictRequestUseCase extends UseCase<AddressResponse> {

    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_UT = "ut";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_PAGE = "page";

    private final DistrictRecommendationRepository repository;

    public GetDistrictRequestUseCase(DistrictRecommendationRepository repository) {
        super(new JobExecutor(), new UIThread());
        this.repository = repository;
    }

    @Override
    public Observable<AddressResponse> createObservable(RequestParams requestParams) {
        return repository.getAddresses(requestParams.getParamsAllValueInString());
    }

}
