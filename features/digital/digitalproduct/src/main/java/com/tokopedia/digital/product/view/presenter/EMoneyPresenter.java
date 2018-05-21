package com.tokopedia.digital.product.view.presenter;

import android.util.Log;

import com.tokopedia.digital.product.domain.interactor.InquiryBalanceUseCase;
import com.tokopedia.digital.product.domain.interactor.SendCommandUseCase;
import com.tokopedia.digital.product.view.listener.IEMoneyView;
import com.tokopedia.digital.product.view.model.InquiryBalanceModel;

import rx.Subscriber;

/**
 * Created by Rizky on 18/05/18.
 */
public class EMoneyPresenter implements IEMoneyPresenter {

    private final String TAG = EMoneyPresenter.class.getSimpleName();

    private IEMoneyView view;
    private InquiryBalanceUseCase inquiryBalanceUseCase;
    private SendCommandUseCase sendCommandUseCase;

    public EMoneyPresenter(IEMoneyView view, InquiryBalanceUseCase InquiryBalanceUseCase,
                           SendCommandUseCase sendCommandUseCase) {
        this.view = view;
        this.inquiryBalanceUseCase = InquiryBalanceUseCase;
        this.sendCommandUseCase = sendCommandUseCase;
    }

    @Override
    public void inquiryBalance(int issuerId, String cardAttribute, String cardInfo, String cardUID,
                               String cardLastBalance) {
        inquiryBalanceUseCase.execute(
                inquiryBalanceUseCase.createRequestParam(issuerId, cardAttribute, cardInfo, cardUID,
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
