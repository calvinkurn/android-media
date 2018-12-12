package com.tokopedia.kol.feature.post.view.subscriber;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.util.ExtensionsKt;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;

import rx.Subscriber;

/**
 * @author by milhamj on 28/11/18.
 */
public class DeletePostSubscriber extends Subscriber<Boolean> {
    private final KolPostListener.View view;
    private int rowNumber;
    private int id;

    public DeletePostSubscriber(KolPostListener.View view, int rowNumber, int id) {
        this.view = view;
        this.rowNumber = rowNumber;
        this.id = id;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        ExtensionsKt.debugTrace(e);
        view.onErrorDeletePost(ErrorHandler.getErrorMessage(view.getContext(), e), rowNumber, id);
    }

    @Override
    public void onNext(Boolean isSuccess) {
        if (isSuccess == null || !isSuccess) {
            String message = view.getContext().getString(R.string.kol_error_delete);
            onError(new MessageErrorException(message));
            return;
        }
        view.onSuccessDeletePost(rowNumber);
    }
}
