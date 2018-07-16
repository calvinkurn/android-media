package com.tokopedia.tokocash.pendingcashback.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.constants.DrawerActivityBroadcastReceiverConstant;
import com.tokopedia.core.constants.HomeFragmentBroadcastReceiverConstant;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.util.TokoCashUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by meta on 16/07/18.
 */
public class TokocashDataBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (!DrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP.equalsIgnoreCase(intent.getAction()))
                return;
            switch (intent.getIntExtra(DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER, 0)) {
                case DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_GET_TOKOCASH_DATA:
                    getTokoCash(context);
                    break;
            }
        } catch (Exception e) {
            // no op
        }
    }

    private void getTokoCash(Context context) {

        JobExecutor jobExecutor = new JobExecutor();
        PostExecutionThread uiThread = new UIThread();

        Observable<TokoCashData> tokoCashModelObservable = ((TkpdCoreRouter) context.getApplicationContext()).getTokoCashBalance();
        TokoCashUseCase tokoCashUseCase = new TokoCashUseCase(
                jobExecutor,
                uiThread,
                tokoCashModelObservable
        );

        tokoCashUseCase.execute(RequestParams.EMPTY, getSubscriberTokocash(context));
    }

    private Subscriber<TokoCashData> getSubscriberTokocash(Context context) {
        return new Subscriber<TokoCashData>() {

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                Intent intentHomeFragment = new Intent(
                        HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                );
                intentHomeFragment.putExtra(HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                        HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA_ERROR);
                context.sendBroadcast(intentHomeFragment);
            }

            @Override
            public void onNext(TokoCashData tokoCashData) {
                DrawerTokoCash tokoCash = TokoCashUtil.convertToViewModel(tokoCashData);

                Intent intentDrawerActivity = new Intent(
                        DrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                );
                intentDrawerActivity.putExtra(DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                        DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA);
                intentDrawerActivity.putExtra(DrawerActivityBroadcastReceiverConstant.EXTRA_TOKOCASH_DRAWER_DATA,
                        tokoCash);
                context.sendBroadcast(intentDrawerActivity);

                Intent intentHomeFragment = new Intent(
                        HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                );
                intentHomeFragment.putExtra(HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                        HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA);
                intentHomeFragment.putExtra(HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_DRAWER_DATA,
                        tokoCash.getHomeHeaderWalletAction());
                context.sendBroadcast(intentHomeFragment);
            }
        };
    }
}
