package com.tokopedia.digital.tokocash.network.request;

/**
 * Created by nabillasabbaha on 10/17/17.
 */

public class RequestHelpHistory {

    private String subject;
    private String message;
    private String category;
    private String transaction_id;

    public RequestHelpHistory() {
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
