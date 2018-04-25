package com.tokopedia.digital_deals.view.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tokopedia.digital_deals.R;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class DealDetailActivity extends BaseSimpleActivity {


    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_brand_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Bakerzin");
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                if (offset < -425) {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getApplicationContext(), R.color.tkpd_dark_gray));
                } else {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getApplicationContext(), R.color.white));
                }
            }
        });

//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

//    @Override
//    protected void setupStatusBar() {
//        super.setupStatusBar();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
//        }
//    }
}
