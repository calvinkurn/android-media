package com.tokopedia.contactus.orderquery.view;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.orderquery.view.fragment.PopularFiveArticleFragment;

/**
 * Created by baghira on 01/05/18.
 */

public class PopularFiveArticleActivity extends BaseSimpleActivity {

    public static final String KEY_CONTENT = "KEY_CONTENT";
    @Override
    protected Fragment getNewFragment() {
        return PopularFiveArticleFragment.newInstance((String)getIntent().getSerializableExtra(KEY_CONTENT));
    }

    public static Intent getInstance(Context context, String slug) {
        Intent intent = new Intent(context, PopularFiveArticleActivity.class);
        intent.putExtra(KEY_CONTENT,slug);
        return intent;
    }
}
