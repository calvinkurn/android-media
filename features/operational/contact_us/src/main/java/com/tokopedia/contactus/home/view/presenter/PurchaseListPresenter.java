package com.tokopedia.contactus.home.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.domain.ContactUsPurchaseListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


/**
 * Created by sandeepgoyal on 11/04/18.
 */

public class PurchaseListPresenter extends BaseDaggerPresenter<PurchaseListContract.View>
        implements PurchaseListContract.Presenter {


    private final ContactUsPurchaseListUseCase purchaseListUseCase;

    @Inject
    public PurchaseListPresenter(ContactUsPurchaseListUseCase purchaseListUseCase) {
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
                Log.d(ContactUsHomeContract.ContactUsName, "PurchaseList OnError" + e.getLocalizedMessage());
                e.printStackTrace();
                getView().setEmptyLayout();
            }

            @Override
            public void onNext(List<BuyerPurchaseList> buyerPurchaseLists) {
                getView().setPurchaseList(buyerPurchaseLists);

            }
        });
    }
}
