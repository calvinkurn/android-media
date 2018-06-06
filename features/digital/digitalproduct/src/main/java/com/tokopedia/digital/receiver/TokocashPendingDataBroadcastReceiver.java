package com.tokopedia.digital.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.constants.DrawerActivityBroadcastReceiverConstant;
import com.tokopedia.core.constants.HomeFragmentBroadcastReceiverConstant;
import com.tokopedia.digital.DigitalRouter;
import com.tokopedia.digital.tokocash.mapper.TokoCashMapper;
import com.tokopedia.digital.tokocash.model.CashBackData;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 12/12/17.
 */

public class TokocashPendingDataBroadcastReceiver extends BroadcastReceiver {
    private CompositeSubscription compositeSubscription;
    private TokoCashMapper tokoCashMapper;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        if (tokoCashMapper == null) tokoCashMapper = new TokoCashMapper();

        Application application = ((Application) context.getApplicationContext());
        if (application != null && application instanceof DigitalRouter) {
            Observable<CashBackData> cashBackDataObservable = ((DigitalRouter) context.getApplicationContext()).getPendingCashbackUseCase(context);
            compositeSubscription.add(cashBackDataObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<CashBackData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(CashBackData cashBackData) {
                            Intent intentHomeFragment = new Intent(
                                    HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                            );
                            intentHomeFragment.putExtra(
                                    HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                                    HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA
                            );
                            intentHomeFragment.putExtra(
                                    HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_PENDING_DATA,
                                    cashBackData
                            );
                            context.sendBroadcast(intentHomeFragment);


                            Intent intentDrawerActivity = new Intent(
                                    DrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                            );
                            intentDrawerActivity.putExtra(
                                    DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                                    DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA
                            );
                            intentDrawerActivity.putExtra(
                                    DrawerActivityBroadcastReceiverConstant.EXTRA_TOKOCASH_PENDING_DATA,
                                    cashBackData
                            );
                            context.sendBroadcast(intentDrawerActivity);
                        }
                    }));
        }
    }
}
