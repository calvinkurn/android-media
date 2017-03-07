package com.tokopedia.digital.cart.model;

/**
 * Created by Nabilla Sabbaha on 3/7/2017.
 */

public class VoucherDigital {

    private String type;

    private String id;

    private VoucherAttributeDigital attributeVoucher;

    private Relation cart;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VoucherAttributeDigital getAttributeVoucher() {
        return attributeVoucher;
    }

    public void setAttributeVoucher(VoucherAttributeDigital attributeVoucher) {
        this.attributeVoucher = attributeVoucher;
    }

    public Relation getCart() {
        return cart;
    }

    public void setCart(Relation cart) {
        this.cart = cart;
    }
}
