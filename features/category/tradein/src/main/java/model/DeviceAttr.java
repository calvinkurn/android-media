
package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceAttr {

    @SerializedName("ModelId")
    @Expose
    private Integer modelId;
    @SerializedName("Imei")
    @Expose
    private List<String> imei;
    @SerializedName("Model")
    @Expose
    private String model;
    @SerializedName("Brand")
    @Expose
    private String brand;
    @SerializedName("Storage")
    @Expose
    private String storage;
    @SerializedName("Ram")
    @Expose
    private String ram;
    @SerializedName("Grade")
    @Expose
    private String grade;

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public List<String> getImei() {
        return imei;
    }

    public void setImei(List<String> imei) {
        this.imei = imei;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
