package com.tokopedia.tokocash.pendingcashback.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.constants.DrawerActivityBroadcastReceiverConstant;
import com.tokopedia.core.constants.HomeFragmentBroadcastReceiverConstant;
import com.tokopedia.tokocash.pendingcashback.data.PendingCashbackMapper;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.tokocash.TokoCashRouter;

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
    private PendingCashbackMapper pendingCashbackMapper;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        if (pendingCashbackMapper == null) pendingCashbackMapper = new PendingCashbackMapper();

        Application application = ((Application) context.getApplicationContext());
        if (application != null && application instanceof TokoCashRouter) {
            Observable<PendingCashback> cashBackDataObservable =
                    ((TokoCashRouter) application).getPendingCashbackUseCase();

            compositeSubscription.add(cashBackDataObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<PendingCashback>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(PendingCashback pendingCashback) {
                            Intent intentHomeFragment = new Intent(
                                    HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                            );
                            intentHomeFragment.putExtra(
                                    HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                                    HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA
                            );
                            intentHomeFragment.putExtra(
                                    HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_PENDING_AMOUNT,
                                    pendingCashback.getAmount()
                            );
                            intentHomeFragment.putExtra(
                                    HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_PENDING__AMOUNT_TEXT,
                                    pendingCashback.getAmountText()
                            );
                            context.sendBroadcast(intentHomeFragment);


                            Intent intentDrawerActivity = new Intent(
                                    DrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                            );
                            intentDrawerActivity.putExtra(
                                    DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                                    DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA
                            );
                            context.sendBroadcast(intentDrawerActivity);
                        }
                    }));
        }
    }
}
