package com.tokopedia.posapp.data.source.cloud;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.data.mapper.CreateOrderMapper;
import com.tokopedia.posapp.data.mapper.PaymentStatusMapper;
import com.tokopedia.posapp.data.pojo.payment.CreateOrderParameter;
import com.tokopedia.posapp.data.source.cloud.api.PaymentApi;
import com.tokopedia.posapp.data.source.cloud.api.ScroogeApi;
import com.tokopedia.posapp.domain.model.CreateOrderDomain;
import com.tokopedia.posapp.domain.model.payment.PaymentStatusDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentCloudSource {
    private static final String PARAM_TRANSACTION_ID = "transaction_id";
    private static final String PARAM_IP_ADDRESS = "ip_address";
    private static final String PARAM_MERCHANT_CODE = "merchant_code";
    public static final String CREATE_ORDER_PARAMETER = "CREATE_ORDER_PARAMETER";
    private String PARAM_SIGNATURE = "signature";

    private ScroogeApi scroogeApi;
    private PaymentApi paymentApi;
    private PaymentStatusMapper paymentStatusMapper;
    private CreateOrderMapper createOrderMapper;

    public PaymentCloudSource(ScroogeApi scroogeApi,
                              PaymentApi paymentApi,
                              PaymentStatusMapper paymentStatusMapper,
                              CreateOrderMapper createOrderMapper) {
        this.scroogeApi = scroogeApi;
        this.paymentApi = paymentApi;
        this.paymentStatusMapper = paymentStatusMapper;
        this.createOrderMapper = createOrderMapper;
    }

    public Observable<PaymentStatusDomain> getPaymentStatus(RequestParams requestParams) {
        String signatureInput = requestParams.getString(PARAM_MERCHANT_CODE, "") +
                requestParams.getString(PARAM_TRANSACTION_ID, "") +
                requestParams.getString(PARAM_IP_ADDRESS, "");

        requestParams.putString(
                PARAM_SIGNATURE,
                AuthUtil.calculateHmacSHA1(signatureInput, PosConstants.KEY_PAYMENT)
        );

        return scroogeApi.getPaymentStatus(requestParams.getParamsAllValueInString())
                .map(paymentStatusMapper);
    }

    public Observable<CreateOrderDomain> createOrder(RequestParams requestParams) {
        CreateOrderParameter orderParam =
                (CreateOrderParameter) requestParams.getObject(CREATE_ORDER_PARAMETER);

        String signatureInput = orderParam.getMerchantCode() +
                orderParam.getProfileCode() +
                orderParam.getTransactionId() +
                orderParam.getCurrency() +
                orderParam.getAmount() +
                orderParam.getGatewayCode() +
                orderParam.getState() +
                orderParam.getUserDefinedString();

        orderParam.setSignature(
            AuthUtil.calculateHmacSHA1(signatureInput, PosConstants.KEY_PAYMENT)
        );

        return paymentApi
                .createOrder(new Gson().toJson(orderParam))
                .map(createOrderMapper);
    }
}
