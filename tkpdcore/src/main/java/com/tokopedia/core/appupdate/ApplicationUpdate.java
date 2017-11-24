package com.tokopedia.core.appupdate;

import com.tokopedia.core.appupdate.model.DetailUpdate;

/**
 * Created by okasurya on 7/25/17.
 */

public interface ApplicationUpdate {
    void checkApplicationUpdate(OnUpdateListener listener);

    interface OnUpdateListener {
        void onNeedUpdate(DetailUpdate detail);

        void onError(Exception e);

        void onNotNeedUpdate();
    }
}
