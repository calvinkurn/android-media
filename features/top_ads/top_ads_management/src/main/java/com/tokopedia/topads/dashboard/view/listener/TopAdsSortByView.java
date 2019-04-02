package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.dashboard.view.model.TopAdsSortByModel;

import java.util.List;

/**
 * Created by nakama on 10/04/18.
 */

public interface TopAdsSortByView extends CustomerView{
    void onSuccessGetListSort(List<TopAdsSortByModel> topAdsSortByModels);
}
