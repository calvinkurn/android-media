package com.tokopedia.gm.subscribe.data.mapper;

import com.tokopedia.gm.subscribe.data.source.cart.cloud.model.checkout.GmCheckoutServiceModel;
import com.tokopedia.gm.subscribe.domain.cart.exception.GmCheckoutCheckException;
import com.tokopedia.gm.subscribe.domain.cart.model.GmCheckoutDomainModel;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class GmSubscribeCheckoutMapper implements Func1<GmCheckoutServiceModel, GmCheckoutDomainModel> {

    @Inject
    public GmSubscribeCheckoutMapper() {
    }

    @Override
    public GmCheckoutDomainModel call(GmCheckoutServiceModel gmCheckoutServiceModel) {
        try {
            return mapServiceToDomain(gmCheckoutServiceModel);
        } catch (Exception e) {
            throw new GmCheckoutCheckException("Unsuported URL toppay");
        }
    }

    private GmCheckoutDomainModel mapServiceToDomain(GmCheckoutServiceModel gmCheckoutServiceModel) throws Exception {
        if (gmCheckoutServiceModel.getError() != null) {
            throw new GmCheckoutCheckException("Unsuported URL toppay");
        }
        GmCheckoutDomainModel domainModel = new GmCheckoutDomainModel();
        domainModel.setPaymentUrl(gmCheckoutServiceModel.getPaymentUrl());
        String parameterUnencoded = decodeUrl(gmCheckoutServiceModel.getParameter1());
        domainModel.setParameter(parameterUnencoded);
        domainModel.setCallbackUrl(gmCheckoutServiceModel.getCallbackurl());
        domainModel.setPaymentId(Integer.valueOf(gmCheckoutServiceModel.getPaymentId()));
        return domainModel;
    }

    private String decodeUrl(String in) {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while (index > -1) {
            int length = working.length();
            if (index > (length - 6)) break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring, 16);
            String stringStart = working.substring(0, index);
            String stringEnd = working.substring(numFinish);
            working = stringStart + ((char) number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }
}
