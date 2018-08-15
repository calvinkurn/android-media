package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.analytics.domain.GetUserAttributesUseCase;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.AccountHome;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountHomePresenter extends BaseDaggerPresenter<AccountHome.View> implements AccountHome.Presenter {

    private GetUserAttributesUseCase getUserAttributesUseCase;
    private AccountAnalytics accountAnalytics;
    private AccountHome.View view;

    public AccountHomePresenter(GetUserAttributesUseCase getUserAttributesUseCase,
                                AccountAnalytics accountAnalytics) {
        this.getUserAttributesUseCase = getUserAttributesUseCase;
        this.accountAnalytics = accountAnalytics;
    }

    @Override
    public void attachView(AccountHome.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getUserAttributesUseCase.unsubscribe();
        view = null;
    }

    @Override
    public void sendUserAttributeTracker() {
        getUserAttributesUseCase.execute(new Subscriber<UserAttributeData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserAttributeData data) {
                accountAnalytics.setUserAttributes(data);
            }
        });
    }
}
