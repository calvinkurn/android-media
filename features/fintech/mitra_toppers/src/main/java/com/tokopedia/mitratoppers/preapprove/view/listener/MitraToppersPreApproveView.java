package com.tokopedia.mitratoppers.preapprove.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;

public interface MitraToppersPreApproveView extends CustomerView {

    void onSuccessGetPreApprove(ResponsePreApprove responsePreApprove);

    void onErrorGetPreApprove(Throwable e);
}
