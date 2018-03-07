package com.tokopedia.gm.subscribe.domain.product.model;

/**
 * Created by sebastianuskh on 1/30/17.
 */
public class GmAutoSubscribeDomainModel {
    private String title;
    private String price;
    private String nextAutoSubscribe;
    private String paymentMethod;

    public static GmAutoSubscribeDomainModel createFromGenericModel(GmProductDomainModel domainModel) {
        GmAutoSubscribeDomainModel autoSubscribeDomainModel = new GmAutoSubscribeDomainModel();
        autoSubscribeDomainModel.setTitle(domainModel.getName());
        autoSubscribeDomainModel.setPrice(domainModel.getPrice());
        return autoSubscribeDomainModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNextAutoSubscribe() {
        return nextAutoSubscribe;
    }

    public void setNextAutoSubscribe(String nextAutoSubscribe) {
        this.nextAutoSubscribe = nextAutoSubscribe;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
