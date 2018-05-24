package com.tokopedia.contact_us;

import android.content.Context;
import android.content.Intent;

/**
 * @author by alvinatin on 26/04/18.
 */

public interface ContactUsModuleRouter {
    Intent getChatBotIntent(Context context, String messageId);
}
