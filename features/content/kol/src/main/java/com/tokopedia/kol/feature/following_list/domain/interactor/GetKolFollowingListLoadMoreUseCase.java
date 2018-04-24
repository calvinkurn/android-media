package com.tokopedia.kol.feature.following_list.domain.interactor;

import com.tokopedia.kol.feature.following_list.data.source.KolFollowingListSource;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingResultDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetKolFollowingListLoadMoreUseCase extends UseCase<KolFollowingResultDomain> {

    public static final String PARAM_ID = "id";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";


    public static final int DEFAULT_LIMIT = 10;
    private KolFollowingListSource kolFollowingListSource;

    public GetKolFollowingListLoadMoreUseCase(KolFollowingListSource kolFollowingListSource) {
        this.kolFollowingListSource = kolFollowingListSource;
    }

    @Override
    public Observable<KolFollowingResultDomain> createObservable(RequestParams requestParams) {
        return kolFollowingListSource.getFollowingList(requestParams);
    }

    public static RequestParams getParam(int id, String cursor) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putString(PARAM_CURSOR, cursor);
        params.putInt(PARAM_LIMIT, DEFAULT_LIMIT);
        return params;
    }
}
