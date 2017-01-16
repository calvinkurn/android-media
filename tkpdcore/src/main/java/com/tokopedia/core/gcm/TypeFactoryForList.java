package com.tokopedia.core.gcm;

import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.model.notification.NewDiscussionNotification;
import com.tokopedia.core.gcm.model.notification.NewMessageNotification;
import com.tokopedia.core.gcm.model.notification.NewReviewNotification;

/**
 * @author  by alvarisi on 1/12/17.
 */

public class TypeFactoryForList implements TypeFactory{
    @Override
    public NotificationPass type(NewMessageNotification newMessageNotification) {
        return null;
    }

    @Override
    public NotificationPass type(NewDiscussionNotification newDiscussionNotification) {
        return null;
    }

    @Override
    public NotificationPass type(NewReviewNotification newReviewNotification) {
        return null;
    }
}