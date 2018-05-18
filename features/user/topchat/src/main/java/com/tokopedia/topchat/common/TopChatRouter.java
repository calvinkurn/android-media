package com.tokopedia.topchat.common;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 5/18/18.
 */
public interface TopChatRouter {

    Intent getHelpPageActivity(Context context, String url, boolean isFromChatBot);
}
