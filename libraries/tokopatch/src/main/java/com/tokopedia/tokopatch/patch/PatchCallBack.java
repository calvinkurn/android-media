package com.tokopedia.tokopatch.patch;


import android.content.Context;

import com.tokopedia.tokopatch.model.Patch;

import java.util.List;

/**
 * Author errysuprayogi on 11,June,2020
 */
public interface PatchCallBack {

    void onPatchListFetched(Context context, boolean result, boolean isNet, List<Patch> patches);
    void onPatchFetched(Context context, boolean result, boolean isNet);
    void onPatchApplied(Context context, boolean result, Patch patch);
    void logNotify(Context context, String log, String where);
    void logMessage(Context context, String log);
    void exceptionNotify(Context context, Throwable throwable, String where);

}
