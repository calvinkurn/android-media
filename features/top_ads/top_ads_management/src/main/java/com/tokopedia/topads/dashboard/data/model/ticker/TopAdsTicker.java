
package com.tokopedia.topads.dashboard.data.model.ticker;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdsTicker {

    @SerializedName("data")
    @Expose
    private DataMessage data;
    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("errors")
    @Expose
    private List<Object> errors = null;

    public DataMessage getData() {
        return data;
    }

    public void setData(DataMessage data) {
        this.data = data;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

}
