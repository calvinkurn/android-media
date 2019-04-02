package com.tokopedia.gm.subscribe.view.viewmodel;

import com.tokopedia.gm.subscribe.domain.cart.model.GmCheckoutDomainModel;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmCheckoutViewModel {
    private String paymentUrl;
    private String parameter;
    private String callbackUrl;
    private Integer paymentId;

    public static GmCheckoutViewModel mapFromDomain(GmCheckoutDomainModel domainModel) {
        GmCheckoutViewModel vieModel = new GmCheckoutViewModel();
        vieModel.setPaymentUrl(domainModel.getPaymentUrl());
        vieModel.setParameter(domainModel.getParameter());
        vieModel.setCallbackUrl(domainModel.getCallbackUrl());
        vieModel.setPaymentId(domainModel.getPaymentId());
        return vieModel;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
}
