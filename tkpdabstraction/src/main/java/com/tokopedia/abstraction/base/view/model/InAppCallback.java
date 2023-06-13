package com.tokopedia.abstraction.base.view.model;

import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;

public interface InAppCallback {
    void onPositiveButtonInAppClicked(DetailUpdate detailUpdate);
    void onNegativeButtonInAppClicked(DetailUpdate detailUpdate);
    void onNotNeedUpdateInApp();
    void onNeedUpdateInApp(DetailUpdate detailUpdate);
}
