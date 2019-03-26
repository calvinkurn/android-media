
package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceDiagInput {

    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("UniqueCode")
    @Expose
    private String uniqueCode;
    @SerializedName("OldPrice")
    @Expose
    private Integer oldPrice;
    @SerializedName("NewPrice")
    @Expose
    private Integer newPrice;
    @SerializedName("DeviceAttr")
    @Expose
    private DeviceAttr deviceAttr;
    @SerializedName("DeviceReview")
    @Expose
    private List<String> deviceReview = null;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Integer getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Integer oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Integer getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Integer newPrice) {
        this.newPrice = newPrice;
    }

    public DeviceAttr getDeviceAttr() {
        return deviceAttr;
    }

    public void setDeviceAttr(DeviceAttr deviceAttr) {
        this.deviceAttr = deviceAttr;
    }

    public List<String> getDeviceReview() {
        return deviceReview;
    }

    public void setDeviceReview(List<String> deviceReview) {
        this.deviceReview = deviceReview;
    }

}
