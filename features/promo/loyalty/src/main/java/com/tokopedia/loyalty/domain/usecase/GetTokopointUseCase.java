package com.tokopedia.loyalty.domain.usecase;

import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by meta on 17/07/18.
 */
public class GetTokopointUseCase extends UseCase<TokoPointDrawerData> {

    public static final String KEY_PARAM = "gql";

    private final ITokoPointRepository repository;

    public GetTokopointUseCase(ITokoPointRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<TokoPointDrawerData> createObservable(RequestParams requestParams) {
        return repository.getPointDrawer(requestParams.getString(KEY_PARAM, ""));
    }
}
