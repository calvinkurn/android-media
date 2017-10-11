package com.tokopedia.posapp.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.pojo.payment.PaymentDetail;
import com.tokopedia.posapp.data.pojo.payment.PaymentStatusItem;
import com.tokopedia.posapp.data.pojo.payment.PaymentStatusResponse;
import com.tokopedia.posapp.domain.model.payment.PaymentDetailDomain;
import com.tokopedia.posapp.domain.model.payment.PaymentStatusDomain;
import com.tokopedia.posapp.domain.model.payment.PaymentStatusItemDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentStatusMapper implements Func1<Response<String>, PaymentStatusDomain> {
    private Gson gson;

    public PaymentStatusMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public PaymentStatusDomain call(Response<String> response) {
        if (response.isSuccessful() && response.body() != null) {
            PaymentStatusResponse data = gson.fromJson(response.body(), PaymentStatusResponse.class);
            if (data != null && data.getData() != null && data.getData().size() != 0) {
                PaymentStatusDomain domain = new PaymentStatusDomain();
                domain.setMerchantCode(data.getData().get(0).getMerchantCode());
                domain.setProfileCode(data.getData().get(0).getProfileCode());
                domain.setTransactionId(data.getData().get(0).getTransactionId());
                domain.setTransactionCode(data.getData().get(0).getTransactionCode());
                domain.setCurrency(data.getData().get(0).getCurrency());
                domain.setAmount(data.getData().get(0).getAmount());
                domain.setGatewayCode(data.getData().get(0).getGatewayCode());
                domain.setGatewayType(data.getData().get(0).getGatewayType());
                domain.setFee(data.getData().get(0).getFee());
                domain.setAdditionalFee(data.getData().get(0).getAdditionalFee());
                domain.setUserDefinedValue(data.getData().get(0).getUserDefinedValue());
                domain.setCustomerEmail(data.getData().get(0).getCustomerEmail());
                domain.setState(data.getData().get(0).getState());
                domain.setExpiredOn(data.getData().get(0).getExpiredOn());
                domain.setPaymentDetails(getPaymentDetail(data.getData().get(0).getPaymentDetails()));
                domain.setItems(getItems(data.getData().get(0).getItems()));
                domain.setValidParam(data.getData().get(0).getValidParam());
                domain.setSignature(data.getData().get(0).getSignature());
                domain.setTokocashUsage(data.getData().get(0).getTokocashUsage());
                domain.setPairData(data.getData().get(0).getPairData());
                return domain;
            }
        }
        return null;
    }

    private List<PaymentStatusItemDomain> getItems(List<PaymentStatusItem> items) {
        List<PaymentStatusItemDomain> itemDomains = new ArrayList<>();
        for (PaymentStatusItem item : items) {
            PaymentStatusItemDomain domain = new PaymentStatusItemDomain();
            domain.setId(item.getId());
            domain.setName(item.getName());
            domain.setPrice(item.getPrice());
            domain.setQuantity(item.getQuantity());
            itemDomains.add(domain);
        }
        return itemDomains;
    }

    private List<PaymentDetailDomain> getPaymentDetail(List<PaymentDetail> paymentDetails) {
        List<PaymentDetailDomain> domains = new ArrayList<>();
        for (PaymentDetail payment : paymentDetails) {
            PaymentDetailDomain domain = new PaymentDetailDomain();
            domain.setName(payment.getName());
            domain.setAmount(payment.getAmount());
            domains.add(domain);
        }
        return domains;
    }
}
