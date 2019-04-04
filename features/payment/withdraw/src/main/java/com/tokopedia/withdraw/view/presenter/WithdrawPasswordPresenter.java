package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.domain.model.GqlSubmitWithDrawalResponse;
import com.tokopedia.withdraw.domain.usecase.GqlSubmitWithdrawUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawPasswordContract;
import com.tokopedia.withdraw.domain.model.BankAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPasswordPresenter extends BaseDaggerPresenter<WithdrawPasswordContract.View>
        implements WithdrawPasswordContract.Presenter {


    private UserSession userSession;
    private GqlSubmitWithdrawUseCase gqlSubmitWithdrawUseCase;

    @Inject
    public WithdrawPasswordPresenter(GqlSubmitWithdrawUseCase gqlSubmitWithdrawUseCase, UserSession userSession) {
        this.gqlSubmitWithdrawUseCase = gqlSubmitWithdrawUseCase;
        this.userSession = userSession;
    }


    @Override
    public void attachView(WithdrawPasswordContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        if (gqlSubmitWithdrawUseCase != null) {
            gqlSubmitWithdrawUseCase.unsubscribe();
        }
        super.detachView();
    }

    @Override
    public void doWithdraw(int withdrawal, BankAccount bankAccount, String password, boolean isSellerWithdrawal) {

        gqlSubmitWithdrawUseCase.setQuery(getView().loadRawString(R.raw.query_submit_withdraw));
        gqlSubmitWithdrawUseCase.setRequestParams(userSession.getEmail(), withdrawal, bankAccount, password, isSellerWithdrawal);
        gqlSubmitWithdrawUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                String error = (throwable).getMessage();
                List<String> list = new ArrayList<>(Arrays.asList(error.split("\n")));
                String indicator = getView().getActivity().getString(R.string.indicator_password_error);
                if (error.toLowerCase().contains(indicator)) {
                    StringBuilder errorSplit = new StringBuilder("");
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).toLowerCase().contains(indicator)) {
                            getView().showErrorPassword(list.get(i));
                        } else {
                            if (errorSplit.length() > 0) {
                                errorSplit = errorSplit.append("\n");
                            }
                            errorSplit = errorSplit.append(list.get(i));
                        }
                    }
                    if (errorSplit.length() > 0) {
                        getView().showError(errorSplit.toString());
                    }
                } else {
                    getView().showError(error);
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                GqlSubmitWithDrawalResponse gqlSubmitWithDrawalResponse = graphqlResponse.getData(GqlSubmitWithDrawalResponse.class);

                if (gqlSubmitWithDrawalResponse != null) {
                    if ("success".equalsIgnoreCase(gqlSubmitWithDrawalResponse.getResponse().getStatus())) {
                        getView().showSuccessWithdraw();

                    } else {
                        getView().showError(gqlSubmitWithDrawalResponse.getResponse().getMessageError());
                    }
                }
            }
        });

    }
}
