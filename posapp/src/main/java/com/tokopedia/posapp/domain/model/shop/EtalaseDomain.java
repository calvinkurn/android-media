package com.tokopedia.posapp.domain.model.shop;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;

/**
 * Created by okasurya on 9/18/17.
 */

public class EtalaseDomain {
    private String etalaseId;
    private String etalaseName;
    private String etalaseAlias;
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
