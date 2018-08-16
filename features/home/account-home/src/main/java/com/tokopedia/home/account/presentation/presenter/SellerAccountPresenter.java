package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.home.account.domain.GetSellerAccountUseCase;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.home.account.AccountConstants.QUERY;
import static com.tokopedia.home.account.AccountConstants.VARIABLES;

/**
 * @author by alvinatin on 10/08/18.
 */

public class SellerAccountPresenter extends BaseDaggerPresenter<SellerAccount.View>
        implements SellerAccount.Presenter{

    private GetSellerAccountUseCase getSellerAccountUseCase;
    private SellerAccount.View view;

    @Inject
    public SellerAccountPresenter(GetSellerAccountUseCase getSellerAccountUseCase) {
        this.getSellerAccountUseCase = getSellerAccountUseCase;
    }

    @Override
    public void attachView(SellerAccount.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getSellerAccountUseCase.unsubscribe();
        view = null;
    }

    @Override
    public void getSellerData(String query) {
        view.showLoading();
        RequestParams requestParams = RequestParams.create();

        requestParams.putString(QUERY, query);
        requestParams.putObject(VARIABLES, new HashMap<>());

        getSellerAccountUseCase.execute(requestParams, new Subscriber<SellerViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                view.showError(throwable.getLocalizedMessage());
                view.hideLoading();
            }

            @Override
            public void onNext(SellerViewModel model) {
                view.loadSellerData(model);
                view.hideLoading();
            }
        });
    }


}
