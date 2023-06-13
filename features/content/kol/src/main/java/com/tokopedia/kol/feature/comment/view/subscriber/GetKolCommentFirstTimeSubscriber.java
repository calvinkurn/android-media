package com.tokopedia.kol.feature.comment.view.subscriber;

import com.tokopedia.kolcommon.util.GraphqlErrorException;
import com.tokopedia.kolcommon.util.GraphqlErrorHandler;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;

/**
 * @author by nisie on 11/1/17.
 */

public class GetKolCommentFirstTimeSubscriber extends rx.Subscriber<KolComments> {


    private final KolComment.View viewListener;

    public GetKolCommentFirstTimeSubscriber(KolComment.View view) {
        this.viewListener = view;
    }

    @Override
    public void onCompleted() {


    }

    @Override
    public void onError(Throwable e) {
        viewListener.removeLoading();
        if (e instanceof GraphqlErrorException) {
            viewListener.onServerErrorGetCommentsFirstTime(
                    GraphqlErrorHandler.getErrorMessage(viewListener.getContext(), e)
            );
        } else {
            viewListener.onErrorGetCommentsFirstTime(
                    GraphqlErrorHandler.getErrorMessage(viewListener.getContext(), e)
            );
        }
    }

    @Override
    public void onNext(KolComments kolComments) {
        viewListener.removeLoading();
        viewListener.updateCursor(kolComments.getLastCursor());
        viewListener.onSuccessGetCommentsFirstTime(kolComments);
    }
}
