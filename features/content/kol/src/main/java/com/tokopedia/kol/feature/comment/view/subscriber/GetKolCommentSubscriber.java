package com.tokopedia.kol.feature.comment.view.subscriber;

import com.tokopedia.kolcommon.util.GraphqlErrorHandler;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class GetKolCommentSubscriber extends Subscriber<KolComments> {

    private final KolComment.View viewListener;

    public GetKolCommentSubscriber(KolComment.View view) {
        this.viewListener = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.removeLoading();
        viewListener.onErrorLoadMoreComment(
                GraphqlErrorHandler.getErrorMessage(viewListener.getContext(), e)
        );
    }

    @Override
    public void onNext(KolComments kolComments) {
        viewListener.updateCursor(kolComments.getLastCursor());
        viewListener.onSuccessGetComments(kolComments);
    }
}
