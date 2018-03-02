package com.tokopedia.posapp.payment.cardscanner.view.presenter;

import com.google.gson.Gson;
import com.tokopedia.posapp.cart.data.pojo.CheckoutDataResponse;
import com.tokopedia.posapp.payment.cardscanner.data.pojo.PaymentDataResponse;
import com.tokopedia.posapp.base.data.pojo.GeneralResponse;
import com.tokopedia.posapp.payment.cardscanner.view.CardDetail;
import com.tokopedia.posapp.payment.cardscanner.view.viewmodel.CreditCardViewModel;
import com.tokopedia.posapp.payment.cardscanner.view.viewmodel.PaymentViewModel;

import java.util.Calendar;
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
        return isCardValid(paymentViewModel.getCreditCard())
                && isBinValid(paymentViewModel)
                && isDateValid(paymentViewModel)
                && isCvvValid(paymentViewModel);
    }

    private boolean isCardValid(CreditCardViewModel creditCard) {
        if(creditCard != null
                && creditCard.getCardNumber() != null
                && creditCard.getCardNumber().length() == 16) {
            return true;
        }
        view.onValidationError(CardDetail.ERROR_CARD_NOT_VALID);
        return false;
    }

    private boolean isBinValid(PaymentViewModel paymentViewModel) {
        if(paymentViewModel.getAllowInstallment()) {
            if(!isBinMatch(paymentViewModel.getCreditCard().getCardNumber(), paymentViewModel.getInstallmentBin())) {
                view.onValidationError(CardDetail.ERROR_CARD_NOT_VALID_FOR_INSTALLMENT);
                return false;
            }
        } else {
            if(!isBinMatch(paymentViewModel.getCreditCard().getCardNumber(), paymentViewModel.getValidateBin())) {
                view.onValidationError(CardDetail.ERROR_CARD_NOT_MATCH_WITH_BANK);
                return false;
            }
        }

        return true;
    }

    private boolean isDateValid(PaymentViewModel paymentViewModel) {
        if(paymentViewModel.getCreditCard().getExpiryYear() != null
            && paymentViewModel.getCreditCard().getExpiryMonth() != null) {

            Calendar now = Calendar.getInstance();
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.set(Calendar.YEAR, paymentViewModel.getCreditCard().getExpiryYear());
            expiryDate.set(Calendar.MONTH, paymentViewModel.getCreditCard().getExpiryMonth());

            if (expiryDate.before(now)) {
                view.onValidationError(CardDetail.ERROR_DATE_NOT_VALID);
                return false;
            }

            return true;
        } else {
            view.onValidationError(CardDetail.ERROR_EMPTY_DATE);
            return false;
        }

    }

    private boolean isCvvValid(PaymentViewModel paymentViewModel) {
        if(paymentViewModel.getCreditCard().getCvv().length() != 3) {
            view.onValidationError(CardDetail.ERROR_INVALID_CVV);
            return false;
        }
        return true;
    }

    private boolean isBinMatch(String cardNo, List<String> bins) {
        for(String bin: bins) {
            if(cardNo.startsWith(bin)) {
                return true;
            }
        }
        view.onError(new Exception("Bank not match with card number"));
        return false;
    }

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
