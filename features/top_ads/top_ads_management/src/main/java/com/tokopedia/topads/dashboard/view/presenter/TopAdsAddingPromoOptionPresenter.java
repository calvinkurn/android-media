package com.tokopedia.topads.dashboard.view.presenter;

import android.content.res.TypedArray;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddingPromoOptionView;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddingPromoOptionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi.putra on 26/04/18.
 */

public class TopAdsAddingPromoOptionPresenter implements CustomerPresenter<TopAdsAddingPromoOptionView> {
    private TopAdsAddingPromoOptionView view;

    @Override
    public void attachView(TopAdsAddingPromoOptionView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void getListAddingOption(String[] title, String[] subtitles, String[] ids, TypedArray icons) {
        List<TopAdsAddingPromoOptionModel> optionModelList = new ArrayList();
        for (int i = 0; i < title.length; ++i){
            optionModelList.add(new TopAdsAddingPromoOptionModel(Integer.parseInt(ids[i]),
                    title[i], subtitles[i], icons.getResourceId(i, -1)));
        }
        if (view != null){
            view.onSuccessGetListTopAdsAddingOption(optionModelList);
        }
    }
}
