package com.tokopedia.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appsflyer.SingleInstallBroadcastReceiver;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.CampaignUtil;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;

import io.branch.referral.InstallListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class InstallReceiver extends BroadcastReceiver {
    private static final String REFERRER = "referrer";

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

                        SingleInstallBroadcastReceiver appsflyerInstall = new SingleInstallBroadcastReceiver();
                        appsflyerInstall.onReceive(receiverData.contextData, receiverData.intentData);

                        new CampaignTrackingReceiver().onReceive(receiverData.contextData, receiverData.intentData);
                        Campaign campaign = CampaignUtil.getCampaignFromQuery(
                                receiverData.intentData.getStringExtra(REFERRER)
                        );
                        TrackingUtils.eventCampaign(campaign);
                        return true;
                    }
                })
                .unsubscribeOn(Schedulers.newThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Throwable throwable) {
                        return Observable.just(true);
                    }
                })
                .subscribe();
	}

    private class ReceiverData {
        Context contextData;
        Intent intentData;
    }

}
