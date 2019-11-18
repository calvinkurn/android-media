package com.tokopedia.otp.cotp.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

/**
 * @author by nisie on 11/30/17.
 */

public interface VerificationOtpMiscall {
    interface View extends Verification.View {

        void updatePhoneHint(String phoneHint);
    }

    interface Presenter extends Verification.Presenter {

    }
}
