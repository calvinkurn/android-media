package com.tokopedia.tkpd.message.interactor;

import com.tokopedia.tkpd.inboxmessage.model.inboxmessage.InboxMessage;

/**
 * Created by Angga.Prasetiyo on 26/01/2016.
 */
public interface CacheInteractor {

    interface GetMessageCacheListener {
        void onSuccess(InboxMessage result);

        void onError(Throwable e);
    }
}
