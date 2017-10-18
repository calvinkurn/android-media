package com.tokopedia.posapp.view.presenter;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.pojo.cart.CheckoutDataResponse;
import com.tokopedia.posapp.data.pojo.PaymentDataResponse;
import com.tokopedia.posapp.data.pojo.base.GeneralResponse;
import com.tokopedia.posapp.view.CardDetail;
import com.tokopedia.posapp.view.viewmodel.card.PaymentViewModel;

import java.util.List;

/**
 * Created by okasurya on 10/9/17.
 */

public class CardDetailPresenter implements CardDetail.Presenter {
    private CardDetail.View view;

    @Override
    public void attachView(CardDetail.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void pay(PaymentViewModel paymentViewModel) {
        if(isValid(paymentViewModel)) {
            view.onGetPayData(getPaymentData(paymentViewModel));
        }
    }

    private boolean isValid(PaymentViewModel paymentViewModel) {
        if(paymentViewModel.getAllowInstallment()) {
            return isBinMatch(
                    paymentViewModel.getCreditCard().getCardNumber().replace(" ", ""),
                    paymentViewModel.getInstallmentBin()
            );
        } else {
            return isBinMatch(
                    paymentViewModel.getCreditCard().getCardNumber().replace(" ", ""),
                    paymentViewModel.getValidateBin()
            );
        }
    }

    private boolean isBinMatch(String cardNo, List<String> bins) {
        for(String bin: bins) {
            if(cardNo.startsWith(bin)) {
                return true;
            }
        }
        view.onError(new Exception("Bank not match with card number"));
        return false;
    };

    private String getPaymentData(PaymentViewModel paymentViewModel) {
        PaymentDataResponse payment = new PaymentDataResponse();
        if(paymentViewModel.getCreditCard() != null) {
            payment.setCcNum(paymentViewModel.getCreditCard().getCardNumber());
            payment.setCvv(paymentViewModel.getCreditCard().getCvv());
            payment.setMon(paymentViewModel.getCreditCard().getExpiryMonth());
            payment.setYear(paymentViewModel.getCreditCard().getExpiryYear());
            payment.setSelectedEmiId(paymentViewModel.getEmiId());
            payment.setCheckoutDataResponse(getCheckoutData(paymentViewModel));
        }
        return new Gson().toJson(payment);
    }

    private GeneralResponse<CheckoutDataResponse> getCheckoutData(PaymentViewModel paymentViewModel) {
        GeneralResponse<CheckoutDataResponse> response = new GeneralResponse<>();
        response.setCode(200);
        response.setMessage("Success");

        CheckoutDataResponse checkoutData = new CheckoutDataResponse();
        checkoutData.setTransactionId(paymentViewModel.getTransactionId());
        checkoutData.setMerchantCode(paymentViewModel.getMerchantCode());
        checkoutData.setPaymentAmount(paymentViewModel.getPaymentAmount());
        checkoutData.setProfileCode(paymentViewModel.getProfileCode());
        checkoutData.setSignature(paymentViewModel.getSignature());
        response.setData(checkoutData);
        return response;
    }
}
