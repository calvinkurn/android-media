package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.adapter.ChallengesHomeAdapter;

/**
 * Created by ashwanityagi on 06/08/18.
 */

public class ChallengesHomeActivity extends BaseActivity {
private TabLayout tabLayout;
private ViewPager viewPager;

//    @Deeplink("sdsd")
//    public static Intent getCallingReferral(Context context, Bundle extras) {
//        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
//        return new Intent(context, ChallengesHomeActivity.class)
//                .setData(uri.build())
//                .putExtras(extras);
//    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.tab_challenges);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new ChallengesHomeAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        setTitle("Home");
    }

}
