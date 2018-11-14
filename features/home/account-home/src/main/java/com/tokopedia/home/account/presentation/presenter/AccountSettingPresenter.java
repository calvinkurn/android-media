package com.tokopedia.home.account.presentation.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home.account.domain.GetAccountSettingConfigUseCase;
import com.tokopedia.home.account.presentation.AccountSetting;
import com.tokopedia.home.account.presentation.subscriber.GetAccountSettingConfigSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 14/11/18.
 */
public class AccountSettingPresenter extends BaseDaggerPresenter<AccountSetting.View> implements
        AccountSetting.Presenter {

    private final GetAccountSettingConfigUseCase getAccountSettingConfigUseCase;
    private final Context context;

    @Inject
    public AccountSettingPresenter(GetAccountSettingConfigUseCase getAccountSettingConfigUseCase,
                                   @ApplicationContext Context context) {
        this.getAccountSettingConfigUseCase = getAccountSettingConfigUseCase;
        this.context = context;
    }

    @Override
    public void getMenuAccountSetting() {
        getAccountSettingConfigUseCase.execute(GetAccountSettingConfigUseCase.getRequestParam(),
                new GetAccountSettingConfigSubscriber(getView(), context));
    }

    @Override
    public void detachView() {
        super.detachView();
        getAccountSettingConfigUseCase.unsubscribe();
    }
}
