package com.tokopedia.tokocash.autosweepmf.data.model;

public class ResponseAutoSweepLimit extends ResponseContainer {
    private AutoSweepLimitEntity data;

    public AutoSweepLimitEntity getData() {
        return data;
    }

    public void setData(AutoSweepLimitEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseAutoSweepLimit{" +
                "data=" + data +
                '}';
    }
}
