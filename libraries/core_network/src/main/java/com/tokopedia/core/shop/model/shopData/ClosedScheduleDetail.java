package com.tokopedia.core.shop.model.shopData;

/**
 * Created by Toped10 on 5/30/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Deprecated
@Parcel
public class ClosedScheduleDetail {

    @SerializedName("close_later_note")
    @Expose
    String closeLaterNote;

    public String getCloseLaterNote() {
        return closeLaterNote;
    }

    public void setCloseLaterNote(String closeLaterNote) {
        this.closeLaterNote = closeLaterNote;
    }

    @SerializedName("close_start")
    @Expose
    String closeStart;
    @SerializedName("close_status")
    @Expose
    Integer closeStatus;
    @SerializedName("close_end")
    @Expose
    String closeEnd;

    /**
     *
     * @return
     * The closeStart
     */
    public String getCloseStart() {
        return closeStart;
    }

    /**
     *
     * @param closeStart
     * The close_start
     */
    public void setCloseStart(String closeStart) {
        this.closeStart = closeStart;
    }

    /**
     *
     * @return
     * The closeStatus
     */
    public Integer getCloseStatus() {
        return closeStatus;
    }

    /**
     *
     * @param closeStatus
     * The close_status
     */
    public void setCloseStatus(Integer closeStatus) {
        this.closeStatus = closeStatus;
    }

    /**
     *
     * @return
     * The closeEnd
     */
    public String getCloseEnd() {
        return closeEnd;
    }

    /**
     *
     * @param closeEnd
     * The close_end
     */
    public void setCloseEnd(String closeEnd) {
        this.closeEnd = closeEnd;
    }

}
