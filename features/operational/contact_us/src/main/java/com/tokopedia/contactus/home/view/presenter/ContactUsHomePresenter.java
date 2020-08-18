package com.tokopedia.contactus.home.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.contactus.home.data.TopBotStatus;
import com.tokopedia.contactus.home.domain.ContactUsArticleUseCase;
import com.tokopedia.contactus.home.domain.ContactUsPurchaseListUseCase;
import com.tokopedia.contactus.home.domain.ContactUsTopBotUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sandeepgoyal on 04/04/18.
 */

public class ContactUsHomePresenter extends BaseDaggerPresenter<ContactUsHomeContract.View> implements ContactUsHomeContract.Presenter {

    private final Context context;
    ContactUsArticleUseCase articleUseCase;
    ContactUsPurchaseListUseCase purchaseListUseCase;
    ContactUsTopBotUseCase topBotUseCase;

    @Inject
    public ContactUsHomePresenter(ContactUsPurchaseListUseCase purchaseListUseCase, ContactUsArticleUseCase contactUsArticleUseCase, ContactUsTopBotUseCase topBotUseCase, @ApplicationContext Context context) {
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
                Log.d(ContactUsHomeContract.ContactUsName, "Article OnError " + e.getLocalizedMessage());
                e.printStackTrace();
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
                Log.d(ContactUsHomeContract.ContactUsName, "PurchaseList OnError" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(List<BuyerPurchaseList> buyerPurchaseLists) {
                if (buyerPurchaseLists.size() > 0) {
                    getView().setEmptyPurchaseListHide();
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
                Log.d(ContactUsHomeContract.ContactUsName, "TopBot OnError" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(TopBotStatus topBotStatus) {
                if (topBotStatus.isIsActive()) {
                    getView().setChatBotVisible();
                    getView().setChatBotMessageId(topBotStatus.getMsgId());

                    UserSessionInterface userSessionInterface = new UserSession(context);
                    String username = userSessionInterface.getName();

                    // Showing user's first name only
                    if(username != null && username.split(" ").length >0) {
                        getView().setHighMessageUserName(username.split(" ")[0]);
                    }
                    getView().setChatBotMessage(topBotStatus.getWelcomeMessge());
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
