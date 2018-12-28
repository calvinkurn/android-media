package com.tokopedia.linker.requests;

import com.tokopedia.linker.interfaces.ShareCallback;

public class LinkerShareRequest<T> extends LinkerGenericRequest {

    private ShareCallback shareCallback;

    public ShareCallback getShareCallbackInterface() {
        return shareCallback;
    }

    public void setShareCallbackInterface(ShareCallback shareCallbackInterface) {
        this.shareCallback = shareCallbackInterface;
    }

    public LinkerShareRequest(int eventId, T dataObject, ShareCallback shareCallback){
        super(eventId, dataObject);
        this.shareCallback = shareCallback;
    }
}
