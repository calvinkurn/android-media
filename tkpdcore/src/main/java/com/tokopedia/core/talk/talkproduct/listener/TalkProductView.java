package com.tokopedia.core.talk.talkproduct.listener;

import com.tokopedia.core.talk.talkproduct.model.TalkProductModel;

import java.util.List;

/**
 * Created by stevenfredian on 4/5/16.
 */
public interface TalkProductView {
    void onSuccessConnection(TalkProductModel parse, int pagingHandlerModel);

    void onTimeoutConnection(int page);

    void onTimeoutConnection(String error, int page);

    void showError(String s);

    void onStateResponse(List list, int position, int page);

    void cancelRequest();

    void removeLoadingFooter();

    void setLoadingFooter();

    interface addTemporary{
        void onSuccess();
    }
}
