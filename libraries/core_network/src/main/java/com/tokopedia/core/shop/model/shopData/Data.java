
package com.tokopedia.core.shop.model.shopData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Deprecated
@Parcel
public class Data {

    @SerializedName("is_allow")
    @Expose
    Integer isAllow;
    @SerializedName("closed_detail")
    @Expose
    ClosedDetail closedDetail;
    @SerializedName("info")
    @Expose
    Info info;
    @SerializedName("image")
    @Expose
    Image image;
    @SerializedName("closed_schedule_detail")
    @Expose
    ClosedScheduleDetail closedScheduleDetail;

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
     * The closedScheduleDetail
     */
    public ClosedScheduleDetail getClosedScheduleDetail() {
        return closedScheduleDetail;
    }

    /**
     *
     * @param closedScheduleDetail
     * The closed_schedule_detail
     */
    public void setClosedScheduleDetail(ClosedScheduleDetail closedScheduleDetail) {
        this.closedScheduleDetail = closedScheduleDetail;
    }
}
