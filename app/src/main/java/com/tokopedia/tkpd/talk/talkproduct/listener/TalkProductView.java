package com.tokopedia.tkpd.talk.talkproduct.listener;

import com.tokopedia.tkpd.talk.talkproduct.model.TalkProductModel;
import com.tokopedia.tkpd.util.PagingHandler;

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
