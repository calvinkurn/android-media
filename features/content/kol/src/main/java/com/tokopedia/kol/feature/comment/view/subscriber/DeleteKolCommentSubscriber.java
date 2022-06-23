package com.tokopedia.kol.feature.comment.view.subscriber;

import com.tokopedia.kolcommon.util.GraphqlErrorHandler;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;

import java.lang.ref.WeakReference;

import rx.Subscriber;

/**
 * @author by nisie on 11/10/17.
 */

public class DeleteKolCommentSubscriber extends Subscriber<Boolean> {

    private final WeakReference<KolComment.View> view;
    private final int adapterPosition;

    public DeleteKolCommentSubscriber(KolComment.View view, int adapterPosition) {
        this.view = new WeakReference<KolComment.View>(view);
        this.adapterPosition = adapterPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        KolComment.View view = this.view.get();
        if (view == null) return;

        view.dismissProgressDialog();
        view.onErrorDeleteComment(GraphqlErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(Boolean deleteKolCommentDomain) {
        KolComment.View view = this.view.get();
        if (view == null) return;

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
