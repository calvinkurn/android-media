package com.tokopedia.digital.product.model;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Product {

    private String productId;
    private String productType;

    private String desc;
    private String detail;
    private String info;
    private String price;
    private int pricePlain;
    private Promo promo;
    private int status;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPricePlain() {
        return pricePlain;
    }

    public void setPricePlain(int pricePlain) {
        this.pricePlain = pricePlain;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
