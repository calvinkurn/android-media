package com.tokopedia.kol.feature.post.domain.usecase;

import com.tokopedia.kol.feature.post.data.source.LikeKolPostSourceCloud;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolPostUseCase extends UseCase<Boolean> {

    public static final int ACTION_LIKE = 1;
    public static final int ACTION_UNLIKE = 0;

    private static final String PARAM_ID = "idPost";
    private static final String PARAM_ACTION = "action";

    private LikeKolPostSourceCloud likeKolPostSourceCloud;

    @Inject
    public LikeKolPostUseCase(LikeKolPostSourceCloud likeKolPostSourceCloud) {
        this.likeKolPostSourceCloud = likeKolPostSourceCloud;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return likeKolPostSourceCloud.likeKolPost(requestParams);
    }

    public static RequestParams getParam(int postId, int action) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, postId);
        params.putInt(PARAM_ACTION, action);
        return params;
    }
}
