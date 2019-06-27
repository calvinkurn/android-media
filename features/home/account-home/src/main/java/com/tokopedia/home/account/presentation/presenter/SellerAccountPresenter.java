package com.tokopedia.home.account.presentation.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.home.account.domain.GetSellerAccountUseCase;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.subscriber.GetSellerAccountSubscriber;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.KYCConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.home.account.AccountConstants.QUERY;
import static com.tokopedia.home.account.AccountConstants.SALDO_QUERY;
import static com.tokopedia.home.account.AccountConstants.TOPADS_QUERY;
import static com.tokopedia.home.account.AccountConstants.VARIABLES;

/**
 * @author by alvinatin on 10/08/18.
 */

public class SellerAccountPresenter extends BaseDaggerPresenter<SellerAccount.View>
        implements SellerAccount.Presenter{

    private GetSellerAccountUseCase getSellerAccountUseCase;
    private UserSessionInterface userSession;
    private SellerAccount.View view;

    @Inject
    public SellerAccountPresenter(GetSellerAccountUseCase getSellerAccountUseCase,
                                  UserSessionInterface userSession) {
        this.getSellerAccountUseCase = getSellerAccountUseCase;
        this.userSession = userSession;
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
    public void getSellerData(String query, String topadsQuery, String saldoQuery) {
        view.showLoading();
        RequestParams requestParams = RequestParams.create();

        requestParams.putString(QUERY, query);
        requestParams.putString(TOPADS_QUERY, topadsQuery);
        requestParams.putString(SALDO_QUERY, saldoQuery);
        Map<String, Object> variables = new HashMap<>();
        int[] shopId = new int[1];
        if(!TextUtils.isEmpty(userSession.getShopId())) {
            shopId[0] = Integer.parseInt(userSession.getShopId());
        }
        variables.put("shop_ids", shopId);
        int merchantId = 0;
        if (!TextUtils.isEmpty(userSession.getShopId())) {
            merchantId = Integer.parseInt(userSession.getShopId());
        }
        variables.put("merchantID", merchantId);
        variables.put("projectId", KYCConstant.KYC_PROJECT_ID);
        requestParams.putObject(VARIABLES, variables);

        getSellerAccountUseCase.execute(requestParams, new GetSellerAccountSubscriber(view));
    }


}
