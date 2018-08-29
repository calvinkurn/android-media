package com.tokopedia.pms.payment.view.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.pms.R;
import com.tokopedia.pms.payment.data.model.PaymentListInside;
import com.tokopedia.pms.payment.view.model.PaymentListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 6/29/18.
 */

public class PaymentListMapper {
    public List<PaymentListModel> map(List<PaymentListInside> paymentList, Context context) {
        List<PaymentListModel> paymentListModels = new ArrayList<>();
        for(PaymentListInside paymentListInside : paymentList){
            PaymentListModel paymentListModel = new PaymentListModel();
            paymentListModel.setTransactionId(paymentListInside.getTransactionId());
            paymentListModel.setTransactionDate(paymentListInside.getTransactionDate());
            paymentListModel.setShowTickerMessage(!TextUtils.isEmpty(paymentListInside.getTickerMessage()));
            paymentListModel.setTickerMessage(paymentListInside.getTickerMessage());
            paymentListModel.setProductImage(paymentListInside.getProductImg());
            paymentListModel.setProductName(paymentListInside.getProductName());
            paymentListModel.setListOfAction(getListOfAction(paymentListInside,context));
            paymentListModel.setShowCancelTransaction(!paymentListInside.isShowHelpPage() &&
                    paymentListInside.isShowCancelButton());
            paymentListModel.setShowSeeDetail(!TextUtils.isEmpty(paymentListInside.getInvoiceUrl()));
            paymentListModel.setInvoiceUrl(paymentListInside.getInvoiceUrl());
            paymentListModel.setPaymentImage(TextUtils.isEmpty(paymentListInside.getBankImg()) ? paymentListInside.getGatewayImg() : paymentListInside.getBankImg());
            paymentListModel.setPaymentAmount(paymentListInside.getPaymentAmount());
            paymentListModel.setShowContainerDetailBankTransfer(isBankTransfer(paymentListInside));
            paymentListModel.setDestAccountNo(paymentListInside.getDestBankAccount().getAccNo());
            paymentListModel.setDestAccountName(paymentListInside.getDestBankAccount().getAccName());
            paymentListModel.setUserAccountNo(paymentListInside.getUserBankAccount().getAccNo());
            paymentListModel.setUserAccountName(paymentListInside.getUserBankAccount().getAccName());
            paymentListModel.setShowDynamicViewDetailPayment(!TextUtils.isEmpty(paymentListInside.getPaymentCode()));
            paymentListModel.setLabelDynamicViewDetailPayment(getLabelDynamicViewDetailPayment(paymentListInside, context));
            paymentListModel.setValueDynamicViewDetailPayment(paymentListInside.getPaymentCode());
            paymentListModel.setPaymentMethod(paymentListInside.getGatewayName());
            paymentListModel.setMerchantCode(paymentListInside.getMerchantCode());
            paymentListModel.setAppLink(paymentListInside.getAppLink());
            paymentListModel.setBankId(String.valueOf(paymentListInside.getUserBankAccount().getBankId()));
            paymentListModel.setShowHowToPay(paymentListInside.isShowHelpPage());
            paymentListModels.add(paymentListModel);
        }
        return paymentListModels;
    }

    private String getLabelDynamicViewDetailPayment(PaymentListInside paymentListInside, Context context) {
        if(paymentListInside.isIsKlikbca()){
            return context.getString(R.string.payment_label_klikbcaid);
        }

        if(paymentListInside.isIsVa()){
            return context.getString(R.string.payment_label_virtual_account_number);
        }
        return context.getString(R.string.payment_label_payment_code);
    }

    private boolean isBankTransfer(PaymentListInside paymentListInside) {
        return paymentListInside.getDestBankAccount() != null && !TextUtils.isEmpty(paymentListInside.getDestBankAccount().getAccNo())
                && paymentListInside.getUserBankAccount() != null && !TextUtils.isEmpty(paymentListInside.getUserBankAccount().getAccNo());
    }

    private List<String> getListOfAction(PaymentListInside paymentListInside, Context context) {
        List<String> listOfActions = new ArrayList<>();
        if(paymentListInside.isShowEditKlikbcaButton()){
            listOfActions.add(context.getString(R.string.payment_label_change_bca_user_id));
        }
        if(paymentListInside.isShowEditTransferButton()){
            listOfActions.add(context.getString(R.string.payment_label_change_account_detail));
        }
        if(paymentListInside.isShowUploadButton()){
            listOfActions.add(context.getString(R.string.payment_label_upload_proof));
        }
        if(paymentListInside.isShowCancelButton()){
            listOfActions.add(context.getString(R.string.payment_label_cancel_transaction));
        }
        return listOfActions;
    }
}
