package com.tokopedia.contactus.home.view.customview;

import android.content.Context;

import com.tokopedia.contactus.common.api.ContactUsURL;
import com.tokopedia.contactus.common.customview.CustomTextView;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.TkpdInboxRouter;

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
        getContext().startActivity(((TkpdInboxRouter)(getContext().getApplicationContext())).getWebviewActivityWithIntent(getContext(), TkpdBaseURL.MOBILE_DOMAIN + ContactUsURL.CONTENT_BASE_URL + contactUsArticle.getSlug()));

       // TransactionPurchaseRouter.startWebViewActivity(getContext(), TkpdBaseURL.MOBILE_DOMAIN + ContactUsURL.CONTENT_BASE_URL + contactUsArticle.getSlug());
      //  getContext().startActivity(
        //        PopularFiveArticleActivity.getInstance(getContext(),contactUsArticle.getSlug()));*/
    }
}
