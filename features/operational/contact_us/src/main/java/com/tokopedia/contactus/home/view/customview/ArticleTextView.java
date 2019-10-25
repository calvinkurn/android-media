package com.tokopedia.contactus.home.view.customview;

import android.content.Context;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.api.ContactUsURL;
import com.tokopedia.contactus.common.customview.CustomTextView;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.url.TokopediaUrl;

/**
 * Created by sandeepgoyal on 05/04/18.
 */

public class ArticleTextView extends CustomTextView {
    ContactUsArticleResponse contactUsArticle;

    public ArticleTextView(Context context) {
        super(context);
    }


    public void setContactUsArticle(ContactUsArticleResponse contactUsArticle) {
        this.contactUsArticle = contactUsArticle;
        setText(contactUsArticle.getTitle());
    }


    @Override
    public void onViewClick() {
        ContactUsTracking.eventPopularArticleClick(contactUsArticle.getTitle());
        getContext().startActivity(RouteManager.getIntent(getContext(), TokopediaUrl.Companion.getInstance().getMOBILEWEB()
                + ContactUsURL.CONTENT_BASE_URL
                + contactUsArticle.getSlug() + "?flag_app=1"));
    }
}
