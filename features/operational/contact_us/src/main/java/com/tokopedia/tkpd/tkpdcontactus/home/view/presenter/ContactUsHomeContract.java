package com.tokopedia.tkpd.tkpdcontactus.home.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;

/**
 * Created by sandeepgoyal on 04/04/18.
 */

public interface ContactUsHomeContract {
    public interface View extends CustomerView{
        public void addPopularArticle(ContactUsArticleResponse articleResponse);
        public void addPopularArticleDivider();

    }

    public interface Presenter extends CustomerPresenter<View> {


    }
}
