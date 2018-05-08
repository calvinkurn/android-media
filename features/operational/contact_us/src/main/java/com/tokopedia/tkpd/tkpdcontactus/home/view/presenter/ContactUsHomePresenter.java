package com.tokopedia.tkpd.tkpdcontactus.home.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.data.TopBotStatus;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsArticleUseCase;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsPurchaseListUseCase;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsTopBotUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sandeepgoyal on 04/04/18.
 */

public class  ContactUsHomePresenter extends BaseDaggerPresenter<ContactUsHomeContract.View> implements ContactUsHomeContract.Presenter {

    private final Context context;
    ContactUsArticleUseCase articleUseCase;
    ContactUsPurchaseListUseCase purchaseListUseCase;
    ContactUsTopBotUseCase topBotUseCase;

    @Inject
    public ContactUsHomePresenter(ContactUsPurchaseListUseCase purchaseListUseCase,ContactUsArticleUseCase contactUsArticleUseCase, ContactUsTopBotUseCase topBotUseCase,@ApplicationContext Context context) {
        this.articleUseCase = contactUsArticleUseCase;
        this.purchaseListUseCase = purchaseListUseCase;
        this.topBotUseCase = topBotUseCase;
        this.context = context;
    }

    @Override
    public void attachView(ContactUsHomeContract.View view) {
        super.attachView(view);
        articleUseCase.execute(new Subscriber<List<ContactUsArticleResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("contacus ", " onerror "+e);
            }

            @Override
            public void onNext(List<ContactUsArticleResponse> contactUsArticleResponse) {
                for (ContactUsArticleResponse response : contactUsArticleResponse) {
                    getView().addPopularArticle(response);
                }
            }
        });

        purchaseListUseCase.execute(new Subscriber<List<BuyerPurchaseList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("contactus", "exception " + e);
            }

            @Override
            public void onNext(List<BuyerPurchaseList> buyerPurchaseLists) {
                if(buyerPurchaseLists.size()>0) {
                    getView().setEmptyPurchaseListHide();
                }else {
                    return;
                }
                if(buyerPurchaseLists.size()>4) {
                    getView().setPurchaseList(buyerPurchaseLists.subList(0, 4));
                }else {
                    getView().setPurchaseList(buyerPurchaseLists);
                }
            }
        });

        topBotUseCase.execute(new Subscriber<TopBotStatus>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TopBotStatus topBotStatus) {
                if(topBotStatus.isIsActive()) {
                    getView().setChatBotVisible();
                    getView().setChatBotMessageId(topBotStatus.getMsgId());
                    getView().setHighMessageUserName(SessionHandler.getLoginName(context));
                }

            }
        });


    }

    @Override
    public void detachView() {
        super.detachView();
        articleUseCase.unsubscribe();
        purchaseListUseCase.unsubscribe();
        topBotUseCase.unsubscribe();
    }
}
