
package com.tokopedia.checkout.domain.datamodel.addresscorner;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokopediaCornerDatum {

    @SerializedName("user_fullname")
    @Expose
    private String userFullname;
    @SerializedName("corner_type")
    @Expose
    private Integer cornerType;
    @SerializedName("user_corner_id")
    @Expose
    private String userCornerId;
    @SerializedName("corner_name")
    @Expose
    private String cornerName;
    @SerializedName("corner_branch")
    @Expose
    private List<CornerBranch> cornerBranch = null;

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public Integer getCornerType() {
        return cornerType;
    }

    public void setCornerType(Integer cornerType) {
        this.cornerType = cornerType;
    }

    public String getUserCornerId() {
        return userCornerId;
    }

    public void setUserCornerId(String userCornerId) {
        this.userCornerId = userCornerId;
    }

    public String getCornerName() {
        return cornerName;
    }

    public void setCornerName(String cornerName) {
        this.cornerName = cornerName;
    }

    public List<CornerBranch> getCornerBranch() {
        return cornerBranch;
    }

    public void setCornerBranch(List<CornerBranch> cornerBranch) {
        this.cornerBranch = cornerBranch;
    }

}
