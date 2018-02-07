package com.tokopedia.core.manage.general.districtrecommendation.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.manage.general.districtrecommendation.data.repository.DistrictRecommendationRepository;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.AddressResponse;

import javax.inject.Inject;

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

    @Inject
    public GetDistrictRequestUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     DistrictRecommendationRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<AddressResponse> createObservable(RequestParams requestParams) {
        return repository.getAddresses(requestParams.getParamsAllValueInString());
    }

}
