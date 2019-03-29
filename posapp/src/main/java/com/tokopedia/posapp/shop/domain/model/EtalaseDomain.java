package com.tokopedia.posapp.shop.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;

/**
 * Created by okasurya on 9/18/17.
 */

public class EtalaseDomain {
    @SerializedName("etalaseId")
    @Expose
    private String etalaseId;
    @SerializedName("etalaseName")
    @Expose
    private String etalaseName;
    @SerializedName("etalaseAlias")
    @Expose
    private String etalaseAlias;
    @SerializedName("useAce")
    @Expose
    private int useAce;

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public String getEtalaseAlias() {
        return etalaseAlias;
    }

    public void setEtalaseAlias(String etalaseAlias) {
        this.etalaseAlias = etalaseAlias;
    }

    public int getUseAce() {
        return useAce;
    }

    public void setUseAce(int useAce) {
        this.useAce = useAce;
    }
}
