package com.tokopedia.ovo;

import android.content.Context;
import android.content.Intent;

public interface OvoPayWithQrRouter {
    void openTokopointWebview(Context context, String url, String title);
    Intent tokopointWebviewIntent(Context context, String url, String title);
}
