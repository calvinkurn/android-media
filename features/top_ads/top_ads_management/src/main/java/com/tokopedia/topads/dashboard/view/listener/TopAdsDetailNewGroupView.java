package com.tokopedia.topads.dashboard.view.listener;

import android.app.Activity;

/**
 * Created by hendry on 3/3/17.
 */
public interface TopAdsDetailNewGroupView extends TopAdsDetailEditView {

    void goToGroupDetail(String groupId);

    Activity getActivity();
}
