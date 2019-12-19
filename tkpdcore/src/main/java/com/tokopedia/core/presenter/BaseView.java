package com.tokopedia.core.presenter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Created by m.normansyah on 06/11/2015.
 */
public interface BaseView {
    String TAG = "MNORMANSYAH";

    /**
     * fragment id hold by Activity
     *
     * @return fragment id determined for Activity
     */
    int getFragmentId();


    /**
     * arise network timeout
     *
     * @param type please see DownloadServiceConstant
     * @param data non null data
     */
    void ariseRetry(int type, @Nullable Object... data);

    /**
     * set data to the presenter to view for user
     *
     * @param type please see DownloadServiceConstant
     * @param data non null data
     */
    void setData(int type, Bundle data);

    /**
     * message error sent from server
     *
     * @param type please see DownloadServiceConstant
     * @param data non null data
     */
    void onNetworkError(int type, Object... data);

    /**
     * message error sent from server
     *
     * @param type please see DownloadServiceConstant
     * @param data non null data
     */
    void onMessageError(int type, Object... data);

    /**
     * get Activity Context
     */
    Context getContext();
}
