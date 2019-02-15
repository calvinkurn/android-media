package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.view.listener.WithdrawPasswordContract;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import javax.inject.Inject;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPasswordPresenter extends BaseDaggerPresenter<WithdrawPasswordContract.View>
        implements WithdrawPasswordContract.Presenter {


    private UserSession userSession;
//    private DoWithdrawUseCase doWithdrawUseCase;

    @Inject
    public WithdrawPasswordPresenter(/*DoWithdrawUseCase useCase,*/ UserSession userSession) {
//        this.doWithdrawUseCase = useCase;
        this.userSession = userSession;
    }


    @Override
    public void attachView(WithdrawPasswordContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
//        doWithdrawUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void doWithdraw(int withdrawal, BankAccountViewModel bankAccountViewModel, String password, boolean isSellerWithdrawal) {
        /*doWithdrawUseCase.execute(DoWithdrawUseCase.createParams
                        (userSession, withdrawal, bankAccountViewModel, password, isSellerWithdrawal)
                , new Subscriber<DoWithdrawDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                String error = (throwable).getMessage();
                List<String> list = new ArrayList<>(Arrays.asList(error.split("\n")));
                String indicator = getView().getActivity().getString(R.string.indicator_password_error);
                if(error.toLowerCase().contains(indicator)) {
                    StringBuilder errorSplit = new StringBuilder("");
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).toLowerCase().contains(indicator)){
                            getView().showErrorPassword(list.get(i));
                        }
                        else {
                            if(errorSplit.length() > 0) {
                                errorSplit = errorSplit.append("\n");
                            }
                            errorSplit = errorSplit.append(list.get(i));
                        }
                    }
                    if(errorSplit.length() > 0) {
                        getView().showError(errorSplit.toString());
                    }
                }else {
                    getView().showError(error);
                }
            }

            @Override
            public void onNext(DoWithdrawDomainModel model) {
                if(model.isSuccessWithdraw()){
                    getView().showSuccessWithdraw();
                }
            }
        });*/
    }
}
