package com.tokopedia.kol.feature.comment.domain.interactor;

import com.tokopedia.kol.feature.comment.data.source.KolCommentSource;
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class SendKolCommentUseCase extends UseCase<SendKolCommentDomain> {

    public static final String PARAM_ID = "idPost";
    public static final String PARAM_COMMENT = "comment";

    private final KolCommentSource kolCommentSource;

    @Inject
    public SendKolCommentUseCase(KolCommentSource kolCommentSource) {
        this.kolCommentSource = kolCommentSource;
    }

    @Override
    public Observable<SendKolCommentDomain> createObservable(RequestParams requestParams) {
        return kolCommentSource.sendComment(requestParams);
    }

    public static RequestParams getParam(long id, String comment) {
        RequestParams params = RequestParams.create();
        params.putLong(PARAM_ID, id);
        params.putString(PARAM_COMMENT, comment);
        return params;
    }
}
