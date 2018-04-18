package com.tokopedia.tkpd.tkpdcontactus.home.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.contactus.R;

import com.tokopedia.tkpd.tkpdcontactus.common.customview.CustomTextView;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.view.ContactUsWebViewActivity;

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
       setText(contactUsArticle.getTitle().getRendered());
    }


    @Override
    public void onViewClick() {
        getContext().startActivity(
                ContactUsWebViewActivity.getInstance(getContext(),contactUsArticle.getGuid().getRendered()));
    }
}
