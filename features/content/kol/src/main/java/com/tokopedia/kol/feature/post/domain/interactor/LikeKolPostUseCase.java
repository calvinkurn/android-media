package com.tokopedia.kol.feature.post.domain.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolPostUseCase extends UseCase<Boolean> {

    public static final String PARAM_ID = "PARAM_ID";
    public static final String PARAM_ACTION = "PARAM_ACTION";

    public static final int ACTION_LIKE = 1;
    public static final int ACTION_UNLIKE = 0;

//    FeedRepository feedRepository;

    public LikeKolPostUseCase() {
//        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return null;
//        return feedRepository.likeUnlikeKolPost(requestParams);
    }


    public static RequestParams getParam(int id, int action) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putInt(PARAM_ACTION, action);
        return params;
    }
}
