package com.tokopedia.withdraw.domain.usecase;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.domain.model.DoWithdrawDomainModel;
import com.tokopedia.withdraw.domain.source.DoWithdrawSource;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

import static com.tokopedia.network.utils.AuthUtil.md5;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class DoWithdrawUseCase extends UseCase<DoWithdrawDomainModel> {

    private DoWithdrawSource doWithdrawSource;

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";
    private static final String PARAM_BANK_ID = "bank_id";
    private static final String PARAM_BANK_NAME = "bank_name";
    private static final String PARAM_BANK_ACC_ID = "bank_account_id";
    private static final String PARAM_BANK_ACC_NAME = "bank_account_name";
    private static final String PARAM_BANK_ACC_NUMBER = "bank_account_number";
    private static final String PARAM_BANK_BRANCH = "bank_branch";
    private static final String PARAM_PASSWORD = "user_password";
    private static final String PARAM_WITHDRAW = "withdraw_amount";
    private static final String PARAM_EMAIL= "user_email";

    @Inject
    public DoWithdrawUseCase(DoWithdrawSource source) {
        this.doWithdrawSource = source;
    }

    @Override
    public Observable<DoWithdrawDomainModel> createObservable(RequestParams requestParams) {
        return doWithdrawSource.doWithdraw(getParameters(requestParams));
    }

    private HashMap<String, Object> getParameters(RequestParams requestParams) {
        return requestParams.getParameters();
    }

    public static RequestParams createParams(UserSession userSession, int withdrawal
            , BankAccountViewModel bankAccountViewModel, String password, boolean isSellerWithdrawal) {
        RequestParams requestParams = RequestParams.EMPTY;
        String deviceId = userSession.getDeviceId();
        String userId = userSession.getUserId();
        String hash = md5(userId + "~" + deviceId);

        HashMap<String, Object> params = new HashMap<>();
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        params.put(PARAM_BANK_ACC_ID, bankAccountViewModel.getBankAccountId());
        params.put(PARAM_BANK_ACC_NAME, bankAccountViewModel.getBankAccountName());
        params.put(PARAM_BANK_ACC_NUMBER, bankAccountViewModel.getBankAccountNumber());
        params.put(PARAM_BANK_ID, bankAccountViewModel.getBankId());
        params.put(PARAM_BANK_NAME, bankAccountViewModel.getBankName());
        params.put(PARAM_BANK_BRANCH, bankAccountViewModel.getBankBranch());

        params.put(PARAM_PASSWORD, password);
        params.put(PARAM_WITHDRAW, withdrawal);

        params.put(PARAM_EMAIL, userSession.getEmail());

        requestParams.putAll(params);

        return requestParams;
    }

}
