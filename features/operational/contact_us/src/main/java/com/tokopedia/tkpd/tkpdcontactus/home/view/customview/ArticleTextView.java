package com.tokopedia.tkpd.tkpdcontactus.home.view.customview;

import android.content.Context;

import com.tokopedia.tkpd.tkpdcontactus.common.customview.CustomTextView;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.PopularFiveArticleActivity;

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
        getContext().startActivity(
                PopularFiveArticleActivity.getInstance(getContext(),contactUsArticle.getContent()));
    }
}
