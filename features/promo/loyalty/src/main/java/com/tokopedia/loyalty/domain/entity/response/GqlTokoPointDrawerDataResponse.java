package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 5/16/18.
 */

public class GqlTokoPointDrawerDataResponse {

    @SerializedName("offFlag")
    @Expose
    private boolean offFlag;

    @SerializedName("url")
    @Expose
    private GqlTokoPointUrl gqlTokoPointUrl;

    @SerializedName("status")
    @Expose
    private GqlTokoPointStatus gqlTokoPointStatus;

    @SerializedName("popupNotif")
    @Expose
    private GqlTokoPointPopupNotif gqlTokoPointPopupNotif;

    public boolean getOffFlag() {
        return offFlag;
    }

    public void setOffFlag(boolean offFlag) {
        this.offFlag = offFlag;
    }

    public GqlTokoPointUrl getGqlTokoPointUrl() {
        return gqlTokoPointUrl;
    }

    public void setGqlTokoPointUrl(GqlTokoPointUrl gqlTokoPointUrl) {
        this.gqlTokoPointUrl = gqlTokoPointUrl;
    }

    public GqlTokoPointStatus getGqlTokoPointStatus() {
        return gqlTokoPointStatus;
    }

    public void setGqlTokoPointStatus(GqlTokoPointStatus gqlTokoPointStatus) {
        this.gqlTokoPointStatus = gqlTokoPointStatus;
    }

    public GqlTokoPointPopupNotif getGqlTokoPointPopupNotif() {
        return gqlTokoPointPopupNotif;
    }

    public void setGqlTokoPointPopupNotif(GqlTokoPointPopupNotif gqlTokoPointPopupNotif) {
        this.gqlTokoPointPopupNotif = gqlTokoPointPopupNotif;
    }
}
