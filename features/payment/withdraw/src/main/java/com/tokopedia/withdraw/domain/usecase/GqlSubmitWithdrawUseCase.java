package com.tokopedia.withdraw.domain.usecase;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.domain.model.BaseFormSubmitResponse;

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
    private static final String AMOUNT = "amount";
    private static final String USERID = "userId";
    private static final String EMAIL = "email";
    private static final String TYPE = "type";
    private static final String TOKEN = "token";
    private static final String MASTER_EMAIL = "masterEmail";
    private static final String MASTER_ID = "masterID";
    private static final String ACCOUNT_ID = "accountID";
    private static final String VALIDATE_TOKEN = "validateToken";
    private static final String PROGRAM = "program";


    @Inject
    public GqlSubmitWithdrawUseCase(GraphqlUseCase graphqlUseCase) {
        this.graphqlUseCase = graphqlUseCase;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();
        GraphqlRequest graphqlRequestForUsable = new GraphqlRequest(query,
                BaseFormSubmitResponse.class, params);
        graphqlUseCase.addRequest(graphqlRequestForUsable);
        graphqlUseCase.execute(subscriber);
    }

    public void setQuery(String loadRawString) {
        this.query = loadRawString;
    }

    public void setRequestParams(String email, int withdrawal
            , BankAccount bankAccount, String password, boolean isSellerWithdrawal, String userId, String programName) {
        params.put(PARAM_ACTION_USER, "user");
        params.put(PARAM_DEVICE_TYPE, MOBILE_DEVICE);
        params.put(EMAIL, email);
        params.put(ACCOUNT_ID, bankAccount.getBankAccountID()+"");
        params.put(PARAM_LANG, "ID");
        params.put(PARAM_IS_SELLER, isSellerWithdrawal);


        params.put(USERID, userId);
        params.put(TYPE, 1);
        params.put(TOKEN, "");
        params.put(MASTER_EMAIL, "");
        params.put(MASTER_ID, "");
        params.put(VALIDATE_TOKEN, "");
        params.put(PROGRAM, programName);

        params.put(PARAM_BANK_ACC_NAME, bankAccount.getAccountName());
        params.put(PARAM_BANK_ACC_NUMBER, bankAccount.getAccountNo());
        params.put(PARAM_BANK_ID, bankAccount.getBankID()+"");
        params.put(PARAM_BANK_NAME, bankAccount.getBankName());
        params.put(AMOUNT, String.valueOf(withdrawal));

        params.put(PARAM_PASSWORD, password);

    }
}
