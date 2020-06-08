package com.tokopedia.home.account.presentation.subscriber;

import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author okasurya on 8/23/18.
 */
public class GetSellerAccountSubscriber extends BaseAccountSubscriber<SellerViewModel> {
    private SellerAccount.View view;

    public GetSellerAccountSubscriber(SellerAccount.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(SellerViewModel model) {
        view.loadSellerData(model);
        view.hideLoading();
    }

    @Override
    String getErrorCode() {
        return AccountConstants.ErrorCodes.ERROR_CODE_SELLER_ACCOUNT;
    }
}
