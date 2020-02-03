package com.tokopedia.events;

import android.content.Context;
import android.content.Intent;

public interface ScanQrCodeRouter {

    Intent gotoQrScannerPage(boolean needResult);
}
