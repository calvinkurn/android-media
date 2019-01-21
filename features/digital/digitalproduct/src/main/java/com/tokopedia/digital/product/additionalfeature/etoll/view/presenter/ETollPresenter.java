package com.tokopedia.digital.product.additionalfeature.etoll.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardCommandUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardInquiryUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.digital.product.view.listener.IETollView;
import com.tokopedia.network.exception.ResponseErrorException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 18/05/18.
 */
public class ETollPresenter extends BaseDaggerPresenter<IETollView> implements IETollPresenter {

    private final String TAG = ETollPresenter.class.getSimpleName();

    private SmartcardInquiryUseCase smartcardInquiryUseCase;
    private SmartcardCommandUseCase smartcardCommandUseCase;

    @Inject
    public ETollPresenter(SmartcardInquiryUseCase SmartcardInquiryUseCase,
                          SmartcardCommandUseCase smartcardCommandUseCase) {
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
                        if (isViewAttached()) {
                            if (e instanceof ResponseErrorException) {
                                getView().showError(e.getMessage());
                            } else {
                                getView().showError(getView().getStringResource(R.string.update_balance_failed));
                            }
                        }
                    }

                    @Override
                    public void onNext(InquiryBalanceModel inquiryBalanceModel) {
                        if (inquiryBalanceModel.getStatus() == 0) {
                            getView().sendCommand(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 1) {
                            getView().showCardLastBalance(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 2) {
                            getView().showError(inquiryBalanceModel.getErrorMessage());
                        }
                    }
                });
    }

    @Override
    public void sendCommand(String payload, int id, int issuerId) {
        smartcardCommandUseCase.execute(
                smartcardCommandUseCase.createRequestParams(payload, id, issuerId),
                new Subscriber<InquiryBalanceModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            if (e instanceof ResponseErrorException) {
                                getView().showError(e.getMessage());
                            } else {
                                getView().showError(getView().getStringResource(R.string.update_balance_failed));
                            }
                        }
                    }

                    @Override
                    public void onNext(InquiryBalanceModel inquiryBalanceModel) {
                        if (inquiryBalanceModel.getStatus() == 0) {
                            getView().sendCommand(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 1) {
                            getView().showCardLastBalance(inquiryBalanceModel);
                        } else if (inquiryBalanceModel.getStatus() == 2) {
                            getView().showError(inquiryBalanceModel.getErrorMessage());
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        smartcardCommandUseCase.unsubscribe();
        smartcardInquiryUseCase.unsubscribe();
        super.detachView();
    }
}
