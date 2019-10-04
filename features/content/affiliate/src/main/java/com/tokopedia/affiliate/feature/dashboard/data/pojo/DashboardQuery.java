package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.feedcomponent.data.pojo.profileheader.BymeProfileHeader;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardQuery {

    @SerializedName("affiliatedProduct")
    @Expose
    private DashboardProduct product;

    @SerializedName("affiliateStats")
    @Expose
    private DashboardHeaderPojo affiliateStats;

    @SerializedName("affiliateCheck")
    @Expose
    private DashboardAffiliateCheck affiliateCheck;

    @SerializedName("affiliatePostQuota")
    @Expose
    private DashboardQuotaStatus postQuota;

    @SerializedName("balance")
    @Expose
    private DashboardBalance balance;

    @SerializedName("bymeProfileHeader")
    @Expose
    private BymeProfileHeader header;

    public DashboardProduct getProduct() {
        return product;
    }

    public void setProduct(DashboardProduct product) {
        this.product = product;
    }

    public DashboardHeaderPojo getAffiliateStats() {
        return affiliateStats;
    }

    public void setAffiliateStats(DashboardHeaderPojo affiliateStats) {
        this.affiliateStats = affiliateStats;
    }

    public DashboardAffiliateCheck getAffiliateCheck() {
        return affiliateCheck;
    }

    public void setAffiliateCheck(DashboardAffiliateCheck affiliateCheck) {
        this.affiliateCheck = affiliateCheck;
    }

    public DashboardQuotaStatus getPostQuota() {
        return postQuota;
    }

    public void setPostQuota(DashboardQuotaStatus postQuota) {
        this.postQuota = postQuota;
    }

    public DashboardBalance getBalance() {
        return balance;
    }

    public void setBalance(DashboardBalance balance) {
        this.balance = balance;
    }

    public BymeProfileHeader getHeader() {
        return header;
    }

    public void setHeader(BymeProfileHeader header) {
        this.header = header;
    }
}
