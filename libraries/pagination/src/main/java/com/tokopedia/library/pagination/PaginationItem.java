package com.tokopedia.library.pagination;

public class PaginationItem {
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
        return "PaginationItem{" +
                ", labelRetry='" + labelRetry + '\'' +
                ", retryMessage='" + retryMessage + '\'' +
                '}';
    }
}
