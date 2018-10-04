package com.tokopedia.gm.subscribe.view.viewmodel;

import com.tokopedia.gm.subscribe.domain.product.model.GmAutoSubscribeDomainModel;

/**
 * Created by sebastianuskh on 1/30/17.
 */
public class GmAutoSubscribeViewModel {
    private String title;
    private String price;
    private String nextAutoSubscribe;
    private String paymentMethod;

    public GmAutoSubscribeViewModel(GmAutoSubscribeDomainModel gmAutoSubscribeDomainModel) {
        setTitle(gmAutoSubscribeDomainModel.getTitle());
        setPrice(gmAutoSubscribeDomainModel.getPrice());
        setNextAutoSubscribe(gmAutoSubscribeDomainModel.getNextAutoSubscribe());
        setPaymentMethod(gmAutoSubscribeDomainModel.getPaymentMethod());
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
