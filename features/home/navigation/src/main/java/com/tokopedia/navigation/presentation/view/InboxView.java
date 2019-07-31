package com.tokopedia.navigation.presentation.view;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.navigation_common.model.NotificationsModel;

import java.util.List;

/**
 * Created by meta on 25/07/18.
 */
public interface InboxView extends LoadDataView {

    void onRenderNotifInbox(NotificationsModel entity);

    void onRenderRecomInbox(List<Visitable> list);

    void hideLoadMoreLoading();

    void showLoadMoreLoading();

}
