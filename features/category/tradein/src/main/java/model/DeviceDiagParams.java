package model;

import com.google.gson.annotations.SerializedName;

public class DeviceDiagParams {
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("NewPrice")
    private int newPrice;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }
}
