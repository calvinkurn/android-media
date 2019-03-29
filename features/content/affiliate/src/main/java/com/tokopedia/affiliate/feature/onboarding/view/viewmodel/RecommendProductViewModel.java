package com.tokopedia.affiliate.feature.onboarding.view.viewmodel;

/**
 * @author by milhamj on 10/4/18.
 */
public class RecommendProductViewModel {
    private String productName;
    private String productImage;
    private String commission;
    private String adId;

    public RecommendProductViewModel(String productName, String productImage, String commission,
                                     String adId) {
        this.productName = productName;
        this.productImage = productImage;
        this.commission = commission;
        this.adId = adId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }
}
