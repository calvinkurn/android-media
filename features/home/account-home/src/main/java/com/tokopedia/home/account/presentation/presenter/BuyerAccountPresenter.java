package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.subscriber.BuyerAccountSubscriber;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.usecase.RequestParams;

import java.net.UnknownHostException;
import java.util.HashMap;

import rx.Subscriber;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountPresenter implements BuyerAccount.Presenter {

    private GetBuyerAccountUseCase getBuyerAccountUseCase;
    private BuyerAccount.View view;

    public BuyerAccountPresenter(GetBuyerAccountUseCase getBuyerAccountUseCase) {
        this.getBuyerAccountUseCase = getBuyerAccountUseCase;
    }

    @Override
    public void getBuyerData(String query) {
        view.showLoading();
        RequestParams requestParams = RequestParams.create();

        requestParams.putString(AccountConstants.QUERY, query);
        requestParams.putObject(AccountConstants.VARIABLES, new HashMap<>());

        getBuyerAccountUseCase.execute(requestParams, new BuyerAccountSubscriber(view));
    }

    @Override
    public void attachView(BuyerAccount.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getBuyerAccountUseCase.unsubscribe();
        view = null;
    }
}
