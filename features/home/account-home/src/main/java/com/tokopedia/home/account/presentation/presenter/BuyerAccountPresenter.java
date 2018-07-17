package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.BuyerAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountPresenter implements BuyerAccount.Presenter {
    private BuyerAccount.View view;

    @Override
    public void getData() {
        List<Visitable> datas = new ArrayList<>();
        view.loadData(datas);
    }

    @Override
    public void attachView(BuyerAccount.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }
}
