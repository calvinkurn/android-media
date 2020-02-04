package com.tokopedia.shop.oldpage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

public class ShopWebViewActivity extends BaseSimpleActivity {
    private static final String PARAM_URL = "url";
    private String url;

    public static void startIntent(Context context, String url){
        Intent intent = new Intent(context, ShopWebViewActivity.class);
        intent.putExtra(PARAM_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(PARAM_URL);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(url);
    }
}
