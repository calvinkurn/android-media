package com.tokopedia.navigation.presentation.view;

import com.tokopedia.navigation.domain.model.Notification;

/**
 * Created by meta on 25/07/18.
 */
public interface MainParentView extends LoadDataView {

    void renderNotification(Notification notification);

    boolean isFirstTimeUser();
}
