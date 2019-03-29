package com.tokopedia.home.account.presentation.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountSettingConfig;
import com.tokopedia.home.account.presentation.AccountSetting;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static com.tokopedia.home.account.AccountConstants.ErrorCodes.ERROR_CODE_NULL_MENU;

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
    protected void showErrorMessage(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        String errorMessage = ErrorHandler.getErrorMessage(context, e);

        if (e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            view.showErroNoConnection();
        } else {
            view.showError(errorMessage);
        }

        if (errorMessage.contains(context.getString(R.string.default_request_error_unknown))) {
            view.logUnknownError(e);
        }
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
