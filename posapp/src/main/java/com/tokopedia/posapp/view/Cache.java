package com.tokopedia.posapp.view;

/**
 * Created by okasurya on 8/29/17.
 */

public interface Cache {
    interface Presenter {
        void getData();

        void setCallbackListener(CallbackListener callbackListener);

        void onDestroy();
    }

    interface CallbackListener {
        void onProductListStored();

        void onError(Throwable e);
    }
}
