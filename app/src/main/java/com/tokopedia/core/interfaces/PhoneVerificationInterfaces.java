package com.tokopedia.core.interfaces;

import com.tokopedia.core.service.model.GetVerificationNumberForm;

/**
 * Created by Kris on 4/20/2015.
 */
public class PhoneVerificationInterfaces {
    public interface getVerificationResponseListener{
        void onPhoneNumberResult(GetVerificationNumberForm dataKampret );
        void onProcessFinished();
    }
    public interface requestCodeListener{
        void onProcessDone();
    }
    public interface sendVerificationListener{
        void onVerificationSuccess();
        void onProcessDone();
    }
}
