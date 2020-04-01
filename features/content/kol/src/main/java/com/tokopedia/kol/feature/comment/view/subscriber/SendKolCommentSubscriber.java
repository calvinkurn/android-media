package com.tokopedia.kol.feature.comment.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.kol.R;
import com.tokopedia.kolcommon.util.GraphqlErrorHandler;
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class SendKolCommentSubscriber extends Subscriber<SendKolCommentDomain> {
    private final KolComment.View viewListener;

    public SendKolCommentSubscriber(KolComment.View view) {
        this.viewListener = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissProgressDialog();
        viewListener.onErrorSendComment(
                GraphqlErrorHandler.getErrorMessage(viewListener.getContext(), e)
        );
    }

    @Override
    public void onNext(SendKolCommentDomain sendKolCommentDomain) {
        viewListener.dismissProgressDialog();
        if (sendKolCommentDomain != null && !TextUtils.isEmpty(sendKolCommentDomain.getComment()))
            viewListener.onSuccessSendComment(sendKolCommentDomain);
        else {
            viewListener.onErrorSendComment(
                    viewListener.getContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown)
            );
        }
    }
}
