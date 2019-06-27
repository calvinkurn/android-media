package com.tokopedia.linker.requests;

import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;

public class LinkerDeeplinkRequest<T> extends LinkerGenericRequest {

    //Check for accuracy
    private DefferedDeeplinkCallback defferedDeeplinkCallback;

    public DefferedDeeplinkCallback getDefferedDeeplinkCallback() {
        return defferedDeeplinkCallback;
    }

    public void setDefferedDeeplinkCallback(DefferedDeeplinkCallback defferedDeeplinkCallback) {
        this.defferedDeeplinkCallback = defferedDeeplinkCallback;
    }

    public LinkerDeeplinkRequest(int eventId, T dataObj, DefferedDeeplinkCallback defferedDeeplinkCallback) {
        super(eventId, dataObj);
        this.defferedDeeplinkCallback = defferedDeeplinkCallback;
    }
}
