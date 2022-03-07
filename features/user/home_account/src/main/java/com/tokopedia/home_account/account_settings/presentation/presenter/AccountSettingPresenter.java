package com.tokopedia.home_account.account_settings.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.home_account.account_settings.domain.GetAccountSettingConfigUseCase;
import com.tokopedia.home_account.account_settings.presentation.AccountSetting;
import com.tokopedia.home_account.account_settings.presentation.subscriber.GetAccountSettingConfigSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 14/11/18.
 */
public class AccountSettingPresenter extends BaseDaggerPresenter<AccountSetting.View> implements
        AccountSetting.Presenter {

    private final GetAccountSettingConfigUseCase getAccountSettingConfigUseCase;

    @Inject
    public AccountSettingPresenter(GetAccountSettingConfigUseCase getAccountSettingConfigUseCase) {
        this.getAccountSettingConfigUseCase = getAccountSettingConfigUseCase;
    }

    @Override
    public void getMenuAccountSetting() {
        getAccountSettingConfigUseCase.execute(GetAccountSettingConfigUseCase.getRequestParam(),
                new GetAccountSettingConfigSubscriber(getView(), getView().getContext()));
    }

    @Override
    public void detachView() {
        super.detachView();
        getAccountSettingConfigUseCase.unsubscribe();
    }
}
