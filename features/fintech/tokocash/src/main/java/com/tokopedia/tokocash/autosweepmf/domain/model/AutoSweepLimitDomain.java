package com.tokopedia.tokocash.autosweepmf.domain.model;

public class AutoSweepLimitDomain extends BaseModelDomain {
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AutoSweepLimitDomain{" +
                "status=" + status +
                '}';
    }
}
