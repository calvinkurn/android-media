package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsSortByView;
import com.tokopedia.topads.dashboard.view.model.TopAdsSortByModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nakama on 10/04/18.
 */

public class TopAdsSortByPresenter implements CustomerPresenter<TopAdsSortByView> {
    private TopAdsSortByView view;

    public void getListSortTopAds(String[] names, String[] values) {
        List<TopAdsSortByModel> productManageSortModels = new ArrayList<>();
        for(int i = 0; i < names.length; ++i){
            productManageSortModels.add(new TopAdsSortByModel(values[i], names[i]));
        }
        view.onSuccessGetListSort(productManageSortModels);
    }

    @Override
    public void attachView(TopAdsSortByView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
