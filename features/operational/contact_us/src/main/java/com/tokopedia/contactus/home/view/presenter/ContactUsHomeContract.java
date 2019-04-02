package com.tokopedia.contactus.home.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;

import java.util.List;

/**
 * Created by sandeepgoyal on 04/04/18.
 */

public interface ContactUsHomeContract {
    String ContactUsName = "Contact_Us";
    String CONTACT_US_WEB = "app_enable_contactus_native";

    interface View extends CustomerView {
        void addPopularArticle(ContactUsArticleResponse articleResponse);

        void addPopularArticleDivider();

        void setEmptyPurchaseListHide();

        void setPurchaseList(List<BuyerPurchaseList> buyerPurchaseLists);

        void setChatBotVisible();

        void setChatBotMessageId(int msgId);

        void setChatBotMessage(String message);

        void setHighMessageUserName(String userName);
    }

    interface Presenter extends CustomerPresenter<View> {


    }
}
