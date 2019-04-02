package com.tokopedia.abstraction.base.view.appupdate;

import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;

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
