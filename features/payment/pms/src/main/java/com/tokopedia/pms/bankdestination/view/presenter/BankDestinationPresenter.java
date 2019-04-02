package com.tokopedia.pms.bankdestination.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.pms.bankdestination.domain.interactor.GetBankListUseCase;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankDestinationPresenter extends BaseDaggerPresenter<BankDestinationContract.View> implements BankDestinationContract.Presenter {

    private GetBankListUseCase getBankListUseCase;

    public BankDestinationPresenter(GetBankListUseCase getBankListUseCase) {
        this.getBankListUseCase = getBankListUseCase;
    }


    public void getListBank() {
        getBankListUseCase.execute(new Subscriber<List<BankListModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showGetListError(e);
            }

            @Override
            public void onNext(List<BankListModel> bankListModels) {
                getView().renderList(bankListModels);
            }
        });
    }
}
