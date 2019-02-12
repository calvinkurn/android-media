
package com.tokopedia.core.network.entity.intermediary.brands;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class MojitoBrandsModel {

    @SerializedName("data")
    @Expose
    private List<Brand> data = null;
    @SerializedName("process-time")
    @Expose
    private String processTime;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Brand> getData() {
        return data;
    }

    public void setData(List<Brand> data) {
        this.data = data;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
