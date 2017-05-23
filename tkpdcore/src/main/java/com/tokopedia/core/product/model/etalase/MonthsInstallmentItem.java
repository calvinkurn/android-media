package com.tokopedia.core.product.model.etalase;

/**
 * Created by alifa on 5/17/17.
 */

public class MonthsInstallmentItem {

    private String value;
    private String info;
    private String imageUrl;

    public MonthsInstallmentItem(String value, String info, String imageUrl) {
        this.value = value;
        this.info = info;
        this.imageUrl = imageUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
