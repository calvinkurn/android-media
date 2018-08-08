package com.tokopedia.core.gcm.notification.applink;

import java.util.List;

/**
 * Created by alvarisi on 2/23/17.
 */

public interface ApplinkVisitor<T> {
    AbstractApplinkBuildAndShowNotification type(ApplinkTypeFactory typeFactory);
}
