package com.tokopedia.library.baseadapter;

public class BaseItem {
    private String labelRetry;
    private String retryMessage;

    public String getLabelRetry() {
        return labelRetry;
    }

    public void setLabelRetry(String labelRetry) {
        this.labelRetry = labelRetry;
    }

    public String getRetryMessage() {
        return retryMessage;
    }

    public void setRetryMessage(String retryMessage) {
        this.retryMessage = retryMessage;
    }

    @Override
    public String toString() {
        return "BaseItem{" +
                ", labelRetry='" + labelRetry + '\'' +
                ", retryMessage='" + retryMessage + '\'' +
                '}';
    }
}
