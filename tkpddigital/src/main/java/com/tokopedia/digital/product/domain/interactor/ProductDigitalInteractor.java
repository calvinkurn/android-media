package com.tokopedia.digital.product.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.common.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.common.data.repository.IDigitalCategoryRepository;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalInteractor implements IProductDigitalInteractor {

    private final IUssdCheckBalanceRepository ussdCheckBalanceRepository;

    public ProductDigitalInteractor(IUssdCheckBalanceRepository ussdCheckBalanceRepository) {
        this.ussdCheckBalanceRepository = ussdCheckBalanceRepository;
    }

    @Override
    public void porcessPulsaUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance, Subscriber<PulsaBalance> subscriber) {
        ussdCheckBalanceRepository.processPulsaBalanceUssdResponse(requestBodyPulsaBalance)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

}
