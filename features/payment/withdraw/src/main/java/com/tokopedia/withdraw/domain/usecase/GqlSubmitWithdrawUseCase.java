package com.tokopedia.withdraw.domain.usecase;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.domain.model.GqlSubmitWithDrawalResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class GqlSubmitWithdrawUseCase {

    private static final String MOBILE_DEVICE = "mobile";
    private static final String SALDO_OPERATION_NAME = "withdrawSaldoMutation";
    private static final String PARAM_WITHDRAW_TYPE = "withdrawalType";
    private Map<String, Object> params = new HashMap<>();
    private GraphqlUseCase graphqlUseCase;
    private String query;

    private static final String PARAM_ACTION_USER = "action";
    private static final String PARAM_DEVICE_TYPE = "deviceType";
    private static final String PARAM_BANK_ACC_ID = "destination";
    private static final String PARAM_SELECT_OTP = "selectOtp";
    private static final String PARAM_LANG = "lang";
    private static final String PARAM_EMAIL = "userEmail";
    private static final String PARAM_WITHDRAW = "withdrawalAmount";
    private static final String PARAM_IS_SELLER = "isSeller";
    private static final String PARAM_BANK_ACC_NAME = "accountName";
    private static final String PARAM_BANK_ACC_NUMBER = "accountNumber";
    private static final String PARAM_BANK_ID = "bankId";
    private static final String PARAM_BANK_NAME = "bankName";
    private static final String PARAM_BANK_BRANCH = "branch";
    private static final String PARAM_PASSWORD = "password";

    /*private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";*/


    @Inject
    public GqlSubmitWithdrawUseCase() {
        graphqlUseCase = new GraphqlUseCase();
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();
        GraphqlRequest graphqlRequestForUsable = new GraphqlRequest(query,
                GqlSubmitWithDrawalResponse.class, params, SALDO_OPERATION_NAME);
        graphqlUseCase.addRequest(graphqlRequestForUsable);
        graphqlUseCase.execute(subscriber);
    }

    public void setQuery(String loadRawString) {
        this.query = loadRawString;
    }

    public void setRequestParams(String email, int withdrawal
            , BankAccount bankAccount, String password, boolean isSellerWithdrawal) {
//        HashMap<String, Object> params = new HashMap<>();
        params.put(PARAM_ACTION_USER, "user");
        params.put(PARAM_DEVICE_TYPE, MOBILE_DEVICE);
        params.put(PARAM_EMAIL, email);
        params.put(PARAM_WITHDRAW, String.valueOf(withdrawal));
        params.put(PARAM_BANK_ACC_ID, bankAccount.getBankAccountId());
        params.put(PARAM_SELECT_OTP, "password");
        params.put(PARAM_LANG, "EN");
        params.put(PARAM_IS_SELLER, isSellerWithdrawal);
        params.put(PARAM_WITHDRAW_TYPE, 1);


        /*params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));*/


        params.put(PARAM_BANK_ACC_NAME, bankAccount.getBankAccountName());
        params.put(PARAM_BANK_ACC_NUMBER, bankAccount.getBankAccountNumber());
        params.put(PARAM_BANK_ID, bankAccount.getBankId());
        params.put(PARAM_BANK_NAME, bankAccount.getBankName());
        params.put(PARAM_BANK_BRANCH, bankAccount.getBankBranch());

        params.put(PARAM_PASSWORD, password);

    }
}
