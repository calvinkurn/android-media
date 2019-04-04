package com.tokopedia.kyc.view.interfaces;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.kyc.model.EligibilityBase;

public interface EligibilityView extends CustomerView {
    void eligibilitySuccess(EligibilityBase eligibilityBase);
    void eligibilityFailure(EligibilityBase eligibilityBase);
    void showHideProgressBar(boolean showProgressBar);
    Activity getActivity();
}
