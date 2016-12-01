package com.tokopedia.core.util;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;

import com.tokopedia.core.msisdn.MSISDNConstant;
import com.tokopedia.core.msisdn.fragment.MsisdnVerificationFragment;
import com.tokopedia.core.msisdn.interactor.MsisdnVerificationRetrofitInteractor;
import com.tokopedia.core.msisdn.interactor.MsisdnVerificationRetrofitInteractorImpl;
import com.tokopedia.core.msisdn.model.VerificationForm;

import java.util.HashMap;

/**
 * Created by Kris on 4/20/2015.
 * modified by m.normansyah on 29/11/2015
 * modified by Nisie on 14/06/2016
 */
public class PhoneVerificationUtil implements MSISDNConstant {
    Context context;

    MsisdnVerificationRetrofitInteractor msisdnNetworkInteractor;
    MSISDNListener msisdnListener;
    //    IncomingSms smsReceiver;
    boolean hasShown = false;
    DialogFragment dialogFragment;

    public void dismissDialog() {
        if (dialogFragment != null)
            dialogFragment.dismiss();
    }

    public interface MSISDNListener {
        void onMSISDNVerified();
    }

    public PhoneVerificationUtil(Context context) {
        this.context = context;
        this.msisdnNetworkInteractor = new MsisdnVerificationRetrofitInteractorImpl();
//        this.smsReceiver = new IncomingSms();

    }

    public void checkIsMSISDNVerified() {
        if (SessionHandler.isV4Login(context)
                && !SessionHandler.isMsisdnVerified()
                && SessionHandler.canRemind()) {

            msisdnNetworkInteractor.checkVerification(context,
                    new HashMap<String, String>(),
                    new MsisdnVerificationRetrofitInteractor.CheckVerificationListener() {
                        @Override
                        public void onSuccess(VerificationForm result) {
                            SessionHandler.setIsMSISDNVerified(result.getMsisdn().isVerified());
                            SessionHandler.setPhoneNumber(result.getMsisdn().getUserPhone());
                            if (!result.getMsisdn().isVerified()) {
                                showVerificationDialog();
                            } else if (msisdnListener != null) {
                                msisdnListener.onMSISDNVerified();
                            }

                        }

                        @Override
                        public void onTimeout() {

                        }

                        @Override
                        public void onFailAuth() {

                        }

                        @Override
                        public void onThrowable(Throwable e) {

                        }

                        @Override
                        public void onError(String error) {

                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoConnection() {

                        }
                    });

        }
    }

    public void showVerificationDialog() {
        hasShown = true;

        FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
        Fragment prev = ((Activity) context).getFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment = MsisdnVerificationFragment.createInstance();
        if (msisdnListener != null)
            ((MsisdnVerificationFragment) dialogFragment).setListener(msisdnListener);
        dialogFragment.show(ft, TAG_DIALOG);

//        smsReceiver.setListener(((MsisdnVerificationFragment) dialogFragment));

    }

//    public void registerSMSReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
//        context.registerReceiver(smsReceiver, filter);
//    }

    public boolean isShowing() {
        return dialogFragment.getShowsDialog();
    }


    public void setMSISDNListener(MSISDNListener msisdnListener) {
        this.msisdnListener = msisdnListener;
    }

//    public void unregister() {
//        if (smsReceiver != null)
//            context.unregisterReceiver(smsReceiver);
//    }

    public void unSubscribe() {
        if (msisdnNetworkInteractor != null)
            msisdnNetworkInteractor.unSubscribeObservable();
    }

    public boolean hasShown() {
        return hasShown;
    }

    public void setHasShown(boolean hasShown) {
        this.hasShown = hasShown;
    }
}
