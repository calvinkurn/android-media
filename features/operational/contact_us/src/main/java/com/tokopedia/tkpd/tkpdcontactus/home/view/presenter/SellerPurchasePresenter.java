package com.tokopedia.tkpd.tkpdcontactus.home.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsPurchaseListUseCase;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsSellerPurchaseLIstUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sandeepgoyal on 14/05/18.
 */

public class SellerPurchasePresenter extends  BaseDaggerPresenter<PurchaseListContract.View> implements PurchaseListContract.Presenter{

    ContactUsSellerPurchaseLIstUseCase contactUsSellerPurchaseLIstUseCase;

    @Inject
    public SellerPurchasePresenter(ContactUsSellerPurchaseLIstUseCase contactUsSellerPurchaseLIstUseCase) {
        this.contactUsSellerPurchaseLIstUseCase = contactUsSellerPurchaseLIstUseCase;
    }

    @Override
    public void attachView(PurchaseListContract.View view) {
        super.attachView(view);
        contactUsSellerPurchaseLIstUseCase.execute(new Subscriber<List<BuyerPurchaseList>>() {
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
