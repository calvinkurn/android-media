package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.challenges.ChallengesAnalytics;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.adapter.ChallengesHomeAdapter;
import com.tokopedia.design.utils.TabUtil;

/**
 * Created by ashwanityagi on 06/08/18.
 */

public class ChallengesHomeActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ChallengesAnalytics analytics;

    @DeepLink({ChallengesUrl.AppLink.CHALLENGES_HOME})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        Intent destination;
        try {
            String deepLink = extras.getString(DeepLink.URI);

            Uri.Builder uri = Uri.parse(deepLink).buildUpon();
            destination = new Intent(context, ChallengesHomeActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);

        } catch (Exception e) {
            destination = new Intent(context, ChallengesHomeActivity.class);
        }
        return destination;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public String getScreenName() {
        return super.getScreenName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        analytics = new ChallengesAnalytics(this);
        tabLayout = findViewById(R.id.tab_challenges);
        viewPager = findViewById(R.id.pager);
        ChallengesHomeAdapter adapter = new ChallengesHomeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                        ChallengesAnalytics.EVENT_CATEGORY_CHALLENGES,
                        ChallengesAnalytics.EVENT_ACTION_CLICK,
                        String.valueOf(adapter.getPageTitle(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null && isShowCloseButton()) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_tokopedia_challenges));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        TabUtil.wrapTabIndicatorToTitle(tabLayout, (int) getResources().getDimension(R.dimen.dp_16), (int) getResources().getDimension(R.dimen.dp_12));

    }

}
