package com.tokopedia.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.appsflyer.SingleInstallBroadcastReceiver;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.router.CustomerRouter;

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

                        ((CustomerRouter.IrisInstallRouter) context.getApplicationContext()).sendIrisInstallEvent();

                        trackIfFromCampaignUrl(data.contextData, receiverData.intentData.getStringExtra(REFERRER));
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
                .subscribe(ignored -> {}, throwable -> {});
	}

    private void trackIfFromCampaignUrl(Context context,String referrer) {
	    Uri uri = Uri.parse(referrer);
	    if(uri != null && DeeplinkUTMUtils.isValidCampaignUrl(uri)) {
            Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(uri);
            UnifyTracking.eventCampaign(context, campaign);
        }
    }

    private class ReceiverData {
        Context contextData;
        Intent intentData;
    }

}
