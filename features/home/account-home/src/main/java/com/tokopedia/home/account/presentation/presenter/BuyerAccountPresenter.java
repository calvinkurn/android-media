package com.tokopedia.home.account.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountPresenter implements BuyerAccount.Presenter {
    private BuyerAccount.View view;

    @Override
    public void getData() {

    }

    @Override
    public void attachView(BuyerAccount.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }
}
