package com.tokopedia.kol.feature.comment.domain.interactor;

import com.tokopedia.kol.feature.comment.data.source.KolCommentSource;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 11/10/17.
 */

public class DeleteKolCommentUseCase extends UseCase<Boolean> {

    public static final String PARAM_ID = "idComment";

    private final KolCommentSource kolCommentSource;

    @Inject
    public DeleteKolCommentUseCase(KolCommentSource kolCommentSource) {
        this.kolCommentSource = kolCommentSource;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return kolCommentSource.deleteKolComment(requestParams);
    }


    public static RequestParams getParam(long commentId) {
        RequestParams params = RequestParams.create();
        params.putLong(PARAM_ID, commentId);
        return params;
    }
}
