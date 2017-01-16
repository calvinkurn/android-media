package com.tokopedia.core.gcm;

import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.notification.dedicated.NewDiscussionNotification;
import com.tokopedia.core.gcm.notification.dedicated.NewMessageNotification;
import com.tokopedia.core.gcm.notification.dedicated.NewReviewNotification;

/**
 * @author by alvarisi on 1/12/17.
 */

public interface TypeFactory {
    NotificationPass type(NewMessageNotification newMessageNotification);

    NotificationPass type(NewDiscussionNotification newDiscussionNotification);

    NotificationPass type(NewReviewNotification newReviewNotification);
}