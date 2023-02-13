package com.tokopedia.kol.feature.comment.domain.interactor;

import com.tokopedia.kol.feature.comment.data.source.KolCommentSource;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/31/17.
 */

public class GetKolCommentsUseCase extends UseCase<KolComments> {

    public static final String PARAM_ID = "idPost";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";

    public static final int DEFAULT_LIMIT = 10;
    private static final String FIRST_CURSOR = "";
    private final KolCommentSource kolCommentSource;

    @Inject
    public GetKolCommentsUseCase(KolCommentSource kolCommentSource) {
        this.kolCommentSource = kolCommentSource;
    }

    @Override
    public Observable<KolComments> createObservable(RequestParams requestParams) {
        return kolCommentSource.getComments(requestParams);
    }

    public static RequestParams getFirstTimeParam(long postId) {
        RequestParams params = RequestParams.create();
        params.putLong(PARAM_ID, postId);
        params.putString(PARAM_CURSOR, FIRST_CURSOR);
        params.putInt(PARAM_LIMIT, DEFAULT_LIMIT);
        return params;
    }

    public static RequestParams getParam(long postId, String cursor) {
        RequestParams params = RequestParams.create();
        params.putLong(PARAM_ID, postId);
        params.putString(PARAM_CURSOR, cursor);
        params.putInt(PARAM_LIMIT, DEFAULT_LIMIT);
        return params;
    }
}
