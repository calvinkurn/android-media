package com.tokopedia.navigation.presentation.view;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.navigation.domain.model.RecomTitle;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

import java.util.List;

/**
 * Created by meta on 25/07/18.
 */
public interface InboxView extends LoadDataView {

    void onRenderNotifInbox(NotificationsModel entity);

    void onRenderRecomInbox(List<Visitable> list, RecomTitle title);

    void hideLoadMoreLoading();

    void showLoadMoreLoading();

    void onTopAdsHeadlineReceived(CpmModel data);

}
