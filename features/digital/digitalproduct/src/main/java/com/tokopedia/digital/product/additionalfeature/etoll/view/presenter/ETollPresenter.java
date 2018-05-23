package com.tokopedia.digital.product.additionalfeature.etoll.view.presenter;

import android.util.Log;

import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardCommandUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardInquiryUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.digital.product.view.listener.IETollView;

import rx.Subscriber;

/**
 * Created by Rizky on 18/05/18.
 */
public class ETollPresenter implements IETollPresenter {

    private final String TAG = ETollPresenter.class.getSimpleName();

    private IETollView view;
    private SmartcardInquiryUseCase smartcardInquiryUseCase;
    private SmartcardCommandUseCase smartcardCommandUseCase;

    public ETollPresenter(IETollView view, SmartcardInquiryUseCase SmartcardInquiryUseCase,
                          SmartcardCommandUseCase smartcardCommandUseCase) {
        this.view = view;
        this.smartcardInquiryUseCase = SmartcardInquiryUseCase;
        this.smartcardCommandUseCase = smartcardCommandUseCase;
    }

    @Override
    public void inquiryBalance(int issuerId, String cardAttribute, String cardInfo, String cardUID,
                               String cardLastBalance) {
        smartcardInquiryUseCase.execute(
                smartcardInquiryUseCase.createRequestParam(issuerId, cardAttribute, cardInfo, cardUID,
                        cardLastBalance),
                new Subscriber<InquiryBalanceModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                        if (e instanceof ResponseErrorException) {
                            view.showError(e.getMessage());
                        } else {
                            view.showError(view.getStringResource(R.string.update_balance_failed));
                        }
                    }

                    @Override
                    public void onNext(InquiryBalanceModel inquiryBalanceModel) {
                        if (inquiryBalanceModel.getStatus() == 0) {
                            view.sendCommand(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 1) {
                            view.showCardLastBalance(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 2) {
                            view.showError(inquiryBalanceModel.getErrorMessage());
                        }
                    }
                });
    }

    @Override
    public void sendCommand(String payload, int id) {
        smartcardCommandUseCase.execute(
                smartcardCommandUseCase.createRequestParams(payload, id),
                new Subscriber<InquiryBalanceModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                        if (e instanceof ResponseErrorException) {
                            view.showError(e.getMessage());
                        } else {
                            view.showError(view.getStringResource(R.string.update_balance_failed));
                        }
                    }

                    @Override
                    public void onNext(InquiryBalanceModel inquiryBalanceModel) {
                        if (inquiryBalanceModel.getStatus() == 0) {
                            view.sendCommand(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 1) {
                            view.showCardLastBalance(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 2) {
                            view.showError(inquiryBalanceModel.getErrorMessage());
                        }
                    }
                });
    }

}
