package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardHeaderPojo {

    /**
     * totalCommission : Rp 300.000.000
     * productClick : 90 product diklik
     * profileView : 30x profile dilihat
     * productSold : 10 product dibeli
     * postQuota : {"formatted":"Tinggal 9 Lagi Kuota Anda","number":9}
     */

    @SerializedName("totalCommission")
    @Expose
    private String totalCommission;
    @SerializedName("productClick")
    @Expose
    private String productClick;
    @SerializedName("profileView")
    @Expose
    private String profileView;
    @SerializedName("productSold")
    @Expose
    private String productSold;
    @SerializedName("postQuota")
    @Expose
    private PostQuotaBean postQuota;

    public String getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(String totalCommission) {
        this.totalCommission = totalCommission;
    }

    public String getProductClick() {
        return productClick;
    }

    public void setProductClick(String productClick) {
        this.productClick = productClick;
    }

    public String getProfileView() {
        return profileView;
    }

    public void setProfileView(String profileView) {
        this.profileView = profileView;
    }

    public String getProductSold() {
        return productSold;
    }

    public void setProductSold(String productSold) {
        this.productSold = productSold;
    }

    public PostQuotaBean getPostQuota() {
        return postQuota;
    }

    public void setPostQuota(PostQuotaBean postQuota) {
        this.postQuota = postQuota;
    }

    public static class PostQuotaBean {
        /**
         * formatted : Tinggal 9 Lagi Kuota Anda
         * number : 9
         */

        @SerializedName("formatted")
        private String formatted;
        @SerializedName("number")
        private int number;

        public String getFormatted() {
            return formatted;
        }

        public void setFormatted(String formatted) {
            this.formatted = formatted;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
