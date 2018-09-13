package com.tokopedia.navigation.presentation.view;

import com.tokopedia.navigation_common.model.NotificationsModel;

/**
 * Created by meta on 25/07/18.
 */
public interface InboxView extends LoadDataView {

    void onRenderNotifInbox(NotificationsModel entity);

}
