
package com.tokopedia.topads.dashboard.data.model.ticker;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMessage {

    @SerializedName("message")
    @Expose
    private List<String> message = null;

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

}
