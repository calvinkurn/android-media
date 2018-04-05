package com.tokopedia.tkpd.tkpdcontactus.home.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;

/**
 * Created by sandeepgoyal on 05/04/18.
 */

public class ArticleTextView extends AppCompatTextView {
    ContactUsArticleResponse contactUsArticle;
    public ArticleTextView(Context context) {
        super(context);
    }

    public ArticleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArticleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setContactUsArticle(ContactUsArticleResponse contactUsArticle) {
       this.contactUsArticle = contactUsArticle;
       setText(contactUsArticle.getTitle().getRendered());
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
