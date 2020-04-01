package com.tokopedia.kol.feature.comment.view.subscriber;

import com.tokopedia.kol.R;
import com.tokopedia.kolcommon.util.GraphqlErrorHandler;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;

import rx.Subscriber;

/**
 * @author by nisie on 11/10/17.
 */

public class DeleteKolCommentSubscriber extends Subscriber<Boolean> {

    private final KolComment.View view;
    private final int adapterPosition;

    public DeleteKolCommentSubscriber(KolComment.View view, int adapterPosition) {
        this.view = view;
        this.adapterPosition = adapterPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissProgressDialog();
        view.onErrorDeleteComment(GraphqlErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(Boolean deleteKolCommentDomain) {
        view.dismissProgressDialog();
        if (deleteKolCommentDomain) {
            view.onSuccessDeleteComment(adapterPosition);
        } else {
            view.onErrorDeleteComment(
                    view.getContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown)
            );
        }
    }
}
