package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class AttributesBanner {

    @SerializedName("recharge_cmsbanner_id")
    @Expose
    private int rechargeCmsbannerId;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_name_webp")
    @Expose
    private String fileNameWebp;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("priority")
    @Expose
    private int priority;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("promocode")
    @Expose
    private String promocode;
    @SerializedName("data_title")
    @Expose
    private String dataTitle;

    public int getRechargeCmsbannerId() {
        return rechargeCmsbannerId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileNameWebp() {
        return fileNameWebp;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getPriority() {
        return priority;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPromocode() {
        return promocode;
    }

    public String getDataTitle() {
        return dataTitle;
    }
}
