package com.tokopedia.navigation.presentation.view;

import com.tokopedia.navigation.data.entity.RecomendationEntity;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.navigation_common.model.NotificationsModel;

import java.util.List;

/**
 * Created by meta on 25/07/18.
 */
public interface InboxView extends LoadDataView {

    void onRenderNotifInbox(NotificationsModel entity);

    void onRenderRecomInbox(List<Recomendation> recomendationList);
}
