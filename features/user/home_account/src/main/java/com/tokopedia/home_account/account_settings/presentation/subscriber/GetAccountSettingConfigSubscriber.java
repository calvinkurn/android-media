package com.tokopedia.home_account.account_settings.presentation.subscriber;

import static com.tokopedia.home_account.account_settings.AccountConstants.ErrorCodes.ERROR_CODE_NULL_MENU;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home_account.R;
import com.tokopedia.home_account.account_settings.AccountConstants;
import com.tokopedia.home_account.account_settings.data.model.AccountSettingConfig;
import com.tokopedia.home_account.account_settings.presentation.AccountSetting;

/**
 * @author by nisie on 14/11/18.
 */
public class GetAccountSettingConfigSubscriber extends BaseAccountSubscriber<GraphqlResponse> {

    private final AccountSetting.View view;
    private final Context context;

    public GetAccountSettingConfigSubscriber(AccountSetting.View view,
                                             @ApplicationContext Context context) {
        super(view);
        this.view = view;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    String getErrorCode() {
        return AccountConstants.ErrorCodes.ERROR_CODE_ACCOUNT_SETTING_CONFIG;
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        AccountSettingConfig accountSettingConfig = graphqlResponse.getData(AccountSettingConfig.class);
        if (accountSettingConfig != null) {
            view.onSuccessGetConfig(accountSettingConfig);
        } else {
            view.showError(String.format("%s (%s)", context.getString(R.string
                    .default_request_error_unknown), ERROR_CODE_NULL_MENU));
        }

    }
}
