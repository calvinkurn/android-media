package com.tokopedia.digital.product.additionalfeature.etoll.view.presenter;

import android.util.Log;

import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardInquiryUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SendCommandUseCase;
import com.tokopedia.digital.product.view.listener.IEMoneyView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

import rx.Subscriber;

/**
 * Created by Rizky on 18/05/18.
 */
public class ETollPresenter implements IETollPresenter {

    private final String TAG = ETollPresenter.class.getSimpleName();

    private IEMoneyView view;
    private SmartcardInquiryUseCase smartcardInquiryUseCase;
    private SendCommandUseCase sendCommandUseCase;

    public ETollPresenter(IEMoneyView view, SmartcardInquiryUseCase SmartcardInquiryUseCase,
                          SendCommandUseCase sendCommandUseCase) {
        this.view = view;
        this.smartcardInquiryUseCase = SmartcardInquiryUseCase;
        this.sendCommandUseCase = sendCommandUseCase;
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
                        view.renderLocalCardInfo();
                    }

                    @Override
                    public void onNext(InquiryBalanceModel inquiryBalanceModel) {
                        if (inquiryBalanceModel.getStatus() == 0) {
                            view.sendCommand(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 1) {
                            view.showCardLastBalance(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 2) {
                            view.showCardLastBalanceWithError(inquiryBalanceModel, inquiryBalanceModel.getErrorMessage());
                        }
                    }
                });
    }

    @Override
    public void sendCommand() {
        sendCommandUseCase.execute(new Subscriber<InquiryBalanceModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
                view.renderLocalCardInfo();
            }

            @Override
            public void onNext(InquiryBalanceModel inquiryBalanceModel) {
                if (inquiryBalanceModel.getStatus() == 0) {
                    view.sendCommand(inquiryBalanceModel);
                } else if (inquiryBalanceModel.getStatus() == 1) {
                    view.showCardLastBalance(inquiryBalanceModel);
                } else if (inquiryBalanceModel.getStatus() == 2) {
                    view.showCardLastBalanceWithError(inquiryBalanceModel, inquiryBalanceModel.getErrorMessage());
                }
            }
        });
    }

}
