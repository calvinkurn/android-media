package com.tokopedia.tkpd.home.interactor;

import com.tokopedia.core.network.apiservices.tokocash.TokoCashCashBackService;
import com.tokopedia.digital.tokocash.domain.TokoCashPendingRepository;
import com.tokopedia.digital.tokocash.mapper.ITokoCashMapper;
import com.tokopedia.digital.tokocash.model.CashBackData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 6/15/17. Tokopedia
 */

public class TokoCashHomeInteractorImpl implements TokoCashHomeInteractor {

    private final CompositeSubscription compositeSubscription;
    private final TokoCashCashBackService tokoCashCashBackService;
    private final TokoCashPendingRepository tokoCashRepository;

    public TokoCashHomeInteractorImpl(ITokoCashMapper iTokoCashMapper) {
        tokoCashCashBackService = new TokoCashCashBackService();
        tokoCashRepository = new TokoCashPendingRepository(tokoCashCashBackService, iTokoCashMapper);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void requestPendingCashBack(Subscriber<CashBackData> subscriber) {
        compositeSubscription.add(tokoCashRepository.getTokoCashPending()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
    }
}
