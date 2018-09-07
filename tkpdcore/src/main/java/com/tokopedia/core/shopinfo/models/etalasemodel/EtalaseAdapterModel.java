package com.tokopedia.core.shopinfo.models.etalasemodel;

/**
 * @author by errysuprayogi on 7/24/17.
 */

public class EtalaseAdapterModel {
    private int useAce;
    private String etalaseName;
    private String etalaseId;
    private String etalaseBadge;

    public EtalaseAdapterModel() {
    }

    public void setUseAce(int useAce) {
        this.useAce = useAce;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public boolean isUseAce() {
        return useAce==1;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public String getEtalaseBadge() {
        return etalaseBadge;
    }

    public void setEtalaseBadge(String etalaseBadge) {
        this.etalaseBadge = etalaseBadge;
    }

    @Override
    public String toString() {
        return getEtalaseName();
    }
}
