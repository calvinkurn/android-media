package com.tokopedia.tokocash.autosweepmf.view.model;

public class AutoSweepLimit extends BaseModel {
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AutoSweepLimit{" +
                "status=" + status +
                '}';
    }
}
