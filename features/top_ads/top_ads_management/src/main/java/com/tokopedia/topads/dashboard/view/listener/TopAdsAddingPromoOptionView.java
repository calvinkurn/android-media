package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddingPromoOptionModel;

import java.util.List;

/**
 * Created by hadi.putra on 26/04/18.
 */

public interface TopAdsAddingPromoOptionView extends CustomerView {
    void onSuccessGetListTopAdsAddingOption(List<TopAdsAddingPromoOptionModel> optionModelList);
}
