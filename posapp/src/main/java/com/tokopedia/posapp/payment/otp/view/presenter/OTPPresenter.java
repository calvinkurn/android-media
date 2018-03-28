package com.tokopedia.posapp.payment.otp.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.payment.otp.data.pojo.CartDetail;
import com.tokopedia.posapp.payment.otp.data.pojo.CreateOrderParameter;
import com.tokopedia.posapp.payment.otp.data.pojo.OrderCartParameter;
import com.tokopedia.posapp.payment.otp.domain.model.CreateOrderDomain;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusItemDomain;
import com.tokopedia.posapp.payment.otp.domain.usecase.CheckPaymentStatusUseCase;
import com.tokopedia.posapp.payment.otp.domain.usecase.CreateOrderUseCase;
import com.tokopedia.posapp.cart.domain.usecase.GetAllCartUseCase;
import com.tokopedia.posapp.payment.otp.OTP;
import com.tokopedia.posapp.payment.otp.view.viewmodel.OTPData;
import com.tokopedia.posapp.payment.otp.view.viewmodel.OTPDetailTransaction;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 10/5/17.
 */

public class OTPPresenter implements OTP.Presenter {
    private static final String UTF_8 = "UTF-8";
    private static final String PARAM_URL = "url";
    private static final String PARAM_METHOD = "method";
    private static final String PARAM_GATEWAY = "gateway";
    private static final String PARAM_FORM = "form";
    private static final String POST_METHOD = "POST";
    private static final String PARAM_TRANSACTION_ID = "transaction_id";
    private static final String PARAM_ID = "id";
    private static final String PARAM_SIGNATURE = "signature";
    private static final String PARAM_MERCHANT_CODE = "merchant_code";
    private static final String PARAM_IP_ADDRESS = "ip_address";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_DETAIL = "detail";
    private static final String PARAM_ERRORS = "errors";
    private static final String CREATE_ORDER_PARAMETER = "CREATE_ORDER_PARAMETER";
    private static final String EMPTY_URL = "Empty url";
    private static final String CREDITCARD = "CREDITCARD";
    private static final String INSTALLMENT = "INSTALLMENT";
    public static final String PARAM_BANK_NAME = "bank_name";
    public static final String PARAM_BANK_ID = "bank_id";
    public static final String PARAM_BANK_LOGO = "bank_logo";

    private OTP.View viewListener;
    private OTPData otpData;

    private CheckPaymentStatusUseCase checkPaymentStatusUseCase;
    private CreateOrderUseCase createOrderUseCase;
    private GetAllCartUseCase getAllCartUseCase;
    private Context context;
    private BankDomain bankDomain;
    private String transactionId;
    private UserSession userSession;

    @Inject
    public OTPPresenter(CheckPaymentStatusUseCase checkPaymentStatusUseCase,
                        CreateOrderUseCase createOrderUseCase,
                        GetAllCartUseCase getAllCartUseCase,
                        @ApplicationContext Context context,
                        UserSession userSession) {
        this.checkPaymentStatusUseCase = checkPaymentStatusUseCase;
        this.createOrderUseCase = createOrderUseCase;
        this.getAllCartUseCase = getAllCartUseCase;
        this.context = context;
        this.userSession = userSession;
    }

    public void attachView(OTP.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void initializeData(String jsonData) {
        Log.d("o2o", jsonData);
        if (jsonData != null) {
            try {
                JSONObject response = new JSONObject(jsonData);
                if (!response.isNull(PARAM_ERRORS)) {
                    viewListener.onLoadDataError(response.optJSONArray(PARAM_ERRORS)
                            .getJSONObject(0).optString(PARAM_DETAIL));
                    return;
                }

                setTransactionId(response.getString("transaction_id"));

                bankDomain = getBankData(response);

                JSONObject data = response.getJSONObject(PARAM_DATA);
                if (data != null
                        && !data.getString(PARAM_URL).isEmpty()) {
                    otpData = new OTPData();
                    otpData.setUrl(data.getString(PARAM_URL));
                    otpData.setMethod(data.getString(PARAM_METHOD));
                    otpData.setGateway(data.getString(PARAM_GATEWAY));
                    otpData.setParameters(getQueryParam(data.getJSONObject(PARAM_FORM)).getBytes(UTF_8));
                    otpData.setOtpDetailTransaction(getDetailTransaction(data.getJSONObject(PARAM_FORM)));

                    if (otpData.getMethod().equals(POST_METHOD)) {
                        viewListener.postOTPWebview(otpData);
                    } else {
                        viewListener.getOTPWebview(otpData);
                    }
                } else {
                    viewListener.onLoadDataError(EMPTY_URL);
                }
            } catch (Exception e) {
                viewListener.onLoadDataError(e.getMessage());
            }
        }
    }

    public BankDomain getBankData(JSONObject response) {
        try {
            if(response.has(PARAM_BANK_ID) && !response.isNull(PARAM_BANK_ID)
                    && response.has(PARAM_BANK_NAME) && !response.isNull(PARAM_BANK_NAME)
                    && response.has(PARAM_BANK_LOGO) && !response.isNull(PARAM_BANK_LOGO)) {
                BankDomain bankDomain = new BankDomain();
                bankDomain.setBankId(response.getInt(PARAM_BANK_ID));
                bankDomain.setBankName(response.getString(PARAM_BANK_NAME));
                bankDomain.setBankLogo(response.getString(PARAM_BANK_LOGO));
                return bankDomain;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private OTPDetailTransaction getDetailTransaction(JSONObject form) throws JSONException {
        OTPDetailTransaction detailTransaction = new OTPDetailTransaction();
        if (form.has(PARAM_SIGNATURE)) {
            detailTransaction.setSignature(getFormItem(PARAM_SIGNATURE, form));
        }
        if (transactionId != null && !transactionId.isEmpty()) {
            detailTransaction.setTransactionId(transactionId);
        }
        if (form.has(PARAM_ID)) {
            detailTransaction.setId(getFormItem(PARAM_ID, form));
        }
        return detailTransaction;
    }

    @Override
    public void processPayment() {
        if (otpData != null
                && otpData.getOtpDetailTransaction() != null
                && otpData.getOtpDetailTransaction().getTransactionId() != null) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(PARAM_MERCHANT_CODE, PosConstants.Payment.MERCHANT_CODE);
            requestParams.putString(PARAM_TRANSACTION_ID, otpData.getOtpDetailTransaction().getTransactionId());
            requestParams.putString(PARAM_IP_ADDRESS, "");
            Observable.just(requestParams)
                    .delay(6, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(checkPaymentStatus())
                    .flatMap(getCartItem())
                    .map(getBankDetail())
                    .flatMap(createOrder())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<PaymentStatusDomain>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            viewListener.onPaymentError(e);
                        }

                        @Override
                        public void onNext(PaymentStatusDomain paymentStatusDomain) {
                            viewListener.onPaymentCompleted(paymentStatusDomain);
                        }
                    });
        } else {
            viewListener.onPaymentError(new Exception("Data error"));
        }
    }

    @Override
    public void setTransactionId(String transactionId) {
        if(transactionId != null && !transactionId.isEmpty()) {
            this.transactionId = transactionId;
        } else {
            throw new RuntimeException("Transaction Id not found");
        }
    }

    private Func1<RequestParams, Observable<PaymentStatusDomain>> checkPaymentStatus() {
        return new Func1<RequestParams, Observable<PaymentStatusDomain>>() {
            @Override
            public Observable<PaymentStatusDomain> call(RequestParams requestParams) {
                return checkPaymentStatusUseCase.createObservable(requestParams);
            }
        };
    }

    private String getQueryParam(JSONObject form) throws JSONException, UnsupportedEncodingException {
        String queryParam = "";
        Iterator<String> keys = form.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            queryParam += key + "=" + URLEncoder.encode(getFormItem(key, form), UTF_8);
            if (keys.hasNext()) queryParam += "&";
        }
        return queryParam;
    }

    private String getFormItem(String key, JSONObject form) throws JSONException {
        if (form.get(key) instanceof String) {
            return form.getString(key);
        } else if (form.get(key) instanceof JSONArray) {
            JSONArray jsonArray = form.getJSONArray(key);
            return jsonArray.getString(0);
        }
        return "";
    }

    public Func1<PaymentStatusDomain, Observable<PaymentStatusDomain>> getCartItem() {
        return new Func1<PaymentStatusDomain, Observable<PaymentStatusDomain>>() {
            @Override
            public Observable<PaymentStatusDomain> call(PaymentStatusDomain paymentStatusDomain) {
                return Observable.zip(
                    Observable.just(paymentStatusDomain),
                    getAllCartUseCase.createObservable(com.tokopedia.usecase.RequestParams.create()),
                    new Func2<PaymentStatusDomain, List<CartDomain>, PaymentStatusDomain>() {
                        @Override
                        public PaymentStatusDomain call(PaymentStatusDomain paymentStatusDomain, List<CartDomain> cartDomains) {
                            List<PaymentStatusItemDomain> items = new ArrayList<>();
                            for (CartDomain cartItem : cartDomains) {
                                PaymentStatusItemDomain paymentItem = new PaymentStatusItemDomain();
                                paymentItem.setId(cartItem.getProductId());
                                paymentItem.setQuantity(cartItem.getQuantity());
                                if(cartItem.getProduct() != null) {
                                    paymentItem.setPrice(cartItem.getProduct().getProductPriceUnformatted());
                                    paymentItem.setName(cartItem.getProduct().getProductName());
                                    paymentItem.setImageUrl(cartItem.getProduct().getProductImage());
                                }
                                items.add(paymentItem);
                            }
                            paymentStatusDomain.setItems(items);
                            return paymentStatusDomain;
                        }
                    }
                );
            }
        };
    }

    private Func1<PaymentStatusDomain, PaymentStatusDomain> getBankDetail() {
        return new Func1<PaymentStatusDomain, PaymentStatusDomain>() {
            @Override
            public PaymentStatusDomain call(PaymentStatusDomain paymentStatusDomain) {
                if(bankDomain != null) {
                    paymentStatusDomain.setBankId(bankDomain.getBankId());
                    paymentStatusDomain.setBankName(bankDomain.getBankName());
                    paymentStatusDomain.setBankLogo(bankDomain.getBankLogo());
                }
                return paymentStatusDomain;
            }
        };
    }

    private Func1<PaymentStatusDomain, Observable<PaymentStatusDomain>> createOrder() {
        return new Func1<PaymentStatusDomain, Observable<PaymentStatusDomain>>() {
            @Override
            public Observable<PaymentStatusDomain> call(PaymentStatusDomain paymentStatusDomain) {
                RequestParams requestParams = RequestParams.create();
                requestParams.putObject(CREATE_ORDER_PARAMETER, getCreateOrderParam(paymentStatusDomain));
                return Observable.zip(
                        Observable.just(paymentStatusDomain),
                        createOrderUseCase.createObservable(requestParams),
                        new Func2<PaymentStatusDomain, CreateOrderDomain, PaymentStatusDomain>() {
                            @Override
                            public PaymentStatusDomain call(PaymentStatusDomain paymentStatusDomain, CreateOrderDomain createOrderDomain) {
                                if(paymentStatusDomain.getState() == 3) {
                                    if (createOrderDomain.isStatus()) {
                                        paymentStatusDomain.setOrderId(createOrderDomain.getOrderId());
                                        paymentStatusDomain.setInvoiceRef(createOrderDomain.getInvoiceRef());
                                    } else {
                                        paymentStatusDomain.setOrderId(0);
                                        paymentStatusDomain.setInvoiceRef(paymentStatusDomain.getTransactionId());
                                    }
                                } else {
                                    throw new RuntimeException("Payment Failed");
                                }
                                return paymentStatusDomain;
                            }
                        }
                );
            }
        };
    }

    private CreateOrderParameter getCreateOrderParam(PaymentStatusDomain paymentStatusDomain) {
        CreateOrderParameter createOrderParameter = new CreateOrderParameter();
        createOrderParameter.setUserId(Integer.parseInt(userSession.getUserId()));
        createOrderParameter.setTransactionId(paymentStatusDomain.getTransactionId());
        createOrderParameter.setState(paymentStatusDomain.getState() + "");
        createOrderParameter.setAmount(paymentStatusDomain.getAmount());
        createOrderParameter.setGatewayCode(paymentStatusDomain.getGatewayCode());
        createOrderParameter.setUserDefinedString(paymentStatusDomain.getUserDefinedValue());
        createOrderParameter.setIpAddress("");
        createOrderParameter.setMerchantCode(paymentStatusDomain.getMerchantCode());
        createOrderParameter.setProfileCode(paymentStatusDomain.getProfileCode());
        createOrderParameter.setCurrency(paymentStatusDomain.getCurrency());

        OrderCartParameter cart = new OrderCartParameter();
        List<CartDetail> cartDetails = new ArrayList<>();
        for (PaymentStatusItemDomain item : paymentStatusDomain.getItems()) {
            CartDetail cartDetail = new CartDetail();
            cartDetail.setProductId(item.getId());
            cartDetail.setQty(item.getQuantity());
            cartDetails.add(cartDetail);
        }
        cart.setDetails(cartDetails);
        cart.setShopId(Integer.parseInt(userSession.getShopId()));
        cart.setAddressId(Integer.parseInt(PosSessionHandler.getOutletId(context)));
        createOrderParameter.setCart(cart);
        return createOrderParameter;
    }
}
