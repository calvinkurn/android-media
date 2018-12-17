package com.tokopedia.loyalty.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.common.DrawerActivityBroadcastReceiverConstant;
import com.tokopedia.loyalty.common.HomeFragmentBroadcastReceiverConstant;
import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.di.component.DaggerTokoPointBroadcastComponent;
import com.tokopedia.loyalty.di.component.TokoPointBroadcastComponent;
import com.tokopedia.loyalty.di.module.ServiceApiModule;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class TokoPointDrawerBroadcastReceiver extends BroadcastReceiver {
    private CompositeSubscription compositeSubscription;
    @Inject
    ITokoPointRepository tokoplusRepository;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        TokoPointBroadcastComponent tokoPointBroadcastComponent = DaggerTokoPointBroadcastComponent
                .builder()
                .baseAppComponent(((BaseMainApplication)context.getApplicationContext()).getBaseAppComponent())
                .serviceApiModule(new ServiceApiModule())
                .build();
        tokoPointBroadcastComponent.inject(this);


        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(
                tokoplusRepository.getPointDrawer(GraphqlHelper.loadRawString(context.getResources(), R.raw.tokopoints_query))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberTokoPointDrawerData(context))
        );
    }

    @NonNull
    private Subscriber<TokoPointDrawerData> getSubscriberTokoPointDrawerData(final Context context) {
        return new Subscriber<TokoPointDrawerData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Intent intentHomeFragment = new Intent(
                        HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                );
                intentHomeFragment.putExtra(
                        HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                        HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA_ERROR
                );
                context.sendBroadcast(intentHomeFragment);
            }

            @Override
            public void onNext(TokoPointDrawerData topPointDrawerData) {
                Intent intentHomeFragment = new Intent(
                        HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                );
                intentHomeFragment.putExtra(
                        HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                        HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA
                );
                intentHomeFragment.putExtra(
                        HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOPOINT_DRAWER_DATA,
                        topPointDrawerData
                );
                context.sendBroadcast(intentHomeFragment);


                Intent intentDrawerActivity = new Intent(
                        DrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                );
                intentDrawerActivity.putExtra(
                        DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                        DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA
                );

                Bundle bundle = new Bundle();
                bundle.putParcelable(DrawerActivityBroadcastReceiverConstant.EXTRA_TOKOPOINT_DRAWER_DATA, topPointDrawerData);
                intentDrawerActivity.putExtras(bundle);
                context.sendBroadcast(intentDrawerActivity);

            }
        };
    }
}
