package com.tokopedia.digital.widget.view.model;

import android.os.Bundle;

/**
 * Created by nabillasabbaha on 8/10/17.
 */

public class PreCheckoutDigitalWidget {

    private String clientNumber;

    private String operatorId;

    private String productId;

    private boolean promoProduct;

    private Bundle bundle;

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isPromoProduct() {
        return promoProduct;
    }

    public void setPromoProduct(boolean promoProduct) {
        this.promoProduct = promoProduct;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
