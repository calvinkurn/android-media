package com.tokopedia.tkpd.tkpdcontactus.home.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsPurchaseListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


/**
 * Created by sandeepgoyal on 11/04/18.
 */

public class PurchaseListPresenter extends BaseDaggerPresenter<PurchaseListContract.View> implements PurchaseListContract.Presenter {


    private final Context context;
    private final ContactUsPurchaseListUseCase purchaseListUseCase;

    @Inject
    public PurchaseListPresenter(ContactUsPurchaseListUseCase purchaseListUseCase, @ApplicationContext Context context) {
        this.context = context;
        this.purchaseListUseCase = purchaseListUseCase;
    }
    @Override
    public void attachView(PurchaseListContract.View view) {
        super.attachView(view);
        purchaseListUseCase.execute(new Subscriber<List<BuyerPurchaseList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<BuyerPurchaseList> buyerPurchaseLists) {
                getView().setPurchaseList(buyerPurchaseLists);

            }
        });
    }
}
