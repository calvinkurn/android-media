package com.tokopedia.analyticsdebugger.debugger.domain.model;

public class ApplinkLogModel {
    private String applink;
    private String traces;

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getTraces() {
        return traces;
    }

    public void setTraces(String traces) {
        this.traces = traces;
    }

    public String getData() {
        return "Applink: " + applink +
                "\r\nTraces: " + traces;
    }
}
