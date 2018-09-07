package com.tokopedia.gm.statistic.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.domain.GMStatRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatGetPopularProductUseCase extends UseCase<GetPopularProduct> {
    public static final String START_DATE = "sdt";
    public static final String END_DATE = "edt";

    private GMStatRepository gmStatRepository;

    @Inject
    public GMStatGetPopularProductUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatRepository gmStatRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.gmStatRepository = gmStatRepository;
    }

    public static RequestParams createRequestParam(long startDate, long endDate) {
        RequestParams params = RequestParams.create();
        params.putLong(START_DATE, startDate);
        params.putLong(END_DATE, endDate);
        return params;
    }

    @Override
    public Observable<GetPopularProduct> createObservable(RequestParams requestParams) {
        final long startDate = requestParams.getLong(START_DATE, -1);
        final long endDate = requestParams.getLong(END_DATE, -1);
        return gmStatRepository.getPopularProduct(startDate, endDate);
    }
}
