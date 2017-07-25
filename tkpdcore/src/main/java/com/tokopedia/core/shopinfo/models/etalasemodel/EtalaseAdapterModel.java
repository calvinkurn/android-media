package com.tokopedia.core.shopinfo.models.etalasemodel;

/**
 * @author by errysuprayogi on 7/24/17.
 */

public class EtalaseAdapterModel {
    private int useAce;
    private String etalaseName;
    private String etalaseId;

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

    @Override
    public String toString() {
        return getEtalaseName();
    }
}
