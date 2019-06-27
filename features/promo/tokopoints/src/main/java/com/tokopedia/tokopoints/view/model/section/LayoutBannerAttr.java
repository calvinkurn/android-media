
package com.tokopedia.tokopoints.view.model.section;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LayoutBannerAttr {

    @SerializedName("bannerType")
    @Expose
    private String bannerType;
    @SerializedName("imageList")
    @Expose
    private List<ImageList> imageList = null;

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public List<ImageList> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageList> imageList) {
        this.imageList = imageList;
    }

}
