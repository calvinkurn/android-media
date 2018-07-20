package com.tokopedia.home.account.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountPresenter implements BuyerAccount.Presenter {
    private BuyerAccount.View view;

    @Override
    public void getData() {
        Log.d("okasurya", "BuyerAccountPresenter.getData");
        view.loadData(mock());
    }

    @Override
    public void attachView(BuyerAccount.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    private List<Visitable> mock() {
        List<Visitable> datas = new ArrayList<>();
        BuyerCardViewModel buyerCardViewModel = new BuyerCardViewModel();
        buyerCardViewModel.setImageUrl("https://ecs7.tokopedia.net/img/cache/100-square/user-1/2017/12/2/3388428/3388428_ac98fbbf-7993-4fff-8392-29cb5ee3a19e.jpg");
        buyerCardViewModel.setName("Brilliant Oka Suryanegara");
        buyerCardViewModel.setProgress(10);
        buyerCardViewModel.setTokopoint("100.000");
        buyerCardViewModel.setVoucher("3");
        datas.add(buyerCardViewModel);

        return datas;
    }
}
