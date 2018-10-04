
package com.tokopedia.core.network.entity.affiliateProductData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("affiliate")
    @Expose
    private List<Affiliate> affiliate = null;
    @SerializedName("ui")
    @Expose
    private Ui ui;

    public List<Affiliate> getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(List<Affiliate> affiliate) {
        this.affiliate = affiliate;
    }

    public Ui getUi() {
        return ui;
    }

    public void setUi(Ui ui) {
        this.ui = ui;
    }

}
