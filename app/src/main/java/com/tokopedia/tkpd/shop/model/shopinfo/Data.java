
package com.tokopedia.tkpd.shop.model.shopinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("is_allow")
    @Expose
    private Integer isAllow;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("closed_detail")
    @Expose
    private ClosedDetail closedDetail;

    /**
     * 
     * @return
     *     The isAllow
     */
    public Integer getIsAllow() {
        return isAllow;
    }

    /**
     * 
     * @param isAllow
     *     The is_allow
     */
    public void setIsAllow(Integer isAllow) {
        this.isAllow = isAllow;
    }

    /**
     * 
     * @return
     *     The image
     */
    public Image getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The info
     */
    public Info getInfo() {
        return info;
    }

    /**
     * 
     * @param info
     *     The info
     */
    public void setInfo(Info info) {
        this.info = info;
    }

    /**
     * 
     * @return
     *     The closedDetail
     */
    public ClosedDetail getClosedDetail() {
        return closedDetail;
    }

    /**
     * 
     * @param closedDetail
     *     The closed_detail
     */
    public void setClosedDetail(ClosedDetail closedDetail) {
        this.closedDetail = closedDetail;
    }

}
