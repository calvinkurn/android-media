package com.tokopedia.core.drawer2.domain;

import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public interface NotificationRepository {

    Observable<NotificationModel> getNotification(TKPDMapParam<String, Object> params);

    Observable<TopChatNotificationModel> getNotificationTopChat(TKPDMapParam<String, Object> params);


}
