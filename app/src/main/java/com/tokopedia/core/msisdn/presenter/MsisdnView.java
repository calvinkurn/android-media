package com.tokopedia.core.msisdn.presenter;

/**
 * Created by m.normansyah on 23/11/2015.
 */
public interface MsisdnView {

    void moveToManualPhoneVerification(int type, String phoneNumber);

    void moveToSmsIncoming(int type, String phoneNumber);

    void moveToThankYou(int type);

    void moveToSendVerification(String phoneNumber);

    boolean isDialogShow(String FragmentTAG);
}
