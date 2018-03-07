package com.tokopedia.core.drawer2.view.subscriber;

import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.util.TokoCashUtil;

import rx.Subscriber;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashSubscriber extends Subscriber<TokoCashModel> {
    private final DrawerDataListener viewListener;

    public TokoCashSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetTokoCash(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(TokoCashModel tokoCashModel) {
        if (tokoCashModel.isSuccess()) {
            viewListener.onGetTokoCash(
                    TokoCashUtil.convertToViewModel(
                            tokoCashModel.getData()));
        } else {
            viewListener.onErrorGetTokoCash(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }
}

