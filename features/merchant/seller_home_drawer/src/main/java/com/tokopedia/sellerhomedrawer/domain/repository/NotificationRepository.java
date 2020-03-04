package com.tokopedia.sellerhomedrawer.domain.repository;

import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public interface NotificationRepository {

    Observable<NotificationModel> getNotification(HashMap<String, Object> params);

}
