
package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KYCDetails {

    @SerializedName("Message")
    private List<Object> message;
    @SerializedName("Detail")
    private Detail detail;

    public List<Object> getMessage() {
        return message;
    }

    public void setMessage(List<Object> message) {
        this.message = message;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

}
