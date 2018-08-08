package com.tkpd.library.utils;

import android.os.Bundle;

/**
 * Created by m.normansyah on 4/11/16.
 */
public interface DownloadResultSender {
    void sendDataToInternet(int type, Bundle data);
}
