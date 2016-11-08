package com.tokopedia.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appsflyer.MultipleInstallBroadcastReceiver;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.localytics.android.ReferralReceiver;
import com.tkpd.library.utils.CommonUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class InstallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        ReceiverData data = new ReceiverData();
        data.contextData = context;
        data.intentData = intent;
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ReceiverData, Boolean>() {
                    @Override
                    public Boolean call(ReceiverData receiverData) {
                        CommonUtils.dumper("RECEIVED BROADCAST");
                        new CampaignTrackingReceiver().onReceive(receiverData.contextData, receiverData.intentData);

                        MultipleInstallBroadcastReceiver appsflyerInstall = new MultipleInstallBroadcastReceiver();
                        appsflyerInstall.onReceive(receiverData.contextData, receiverData.intentData);

                        ReferralReceiver localyticsInstall = new ReferralReceiver();
                        localyticsInstall.onReceive(receiverData.contextData, receiverData.intentData);
                        return true;
                    }
                })
                .subscribe();
	}

    private class ReceiverData {
        Context contextData;
        Intent intentData;
    }

}
