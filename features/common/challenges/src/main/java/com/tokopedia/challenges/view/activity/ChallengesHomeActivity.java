package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.adapter.ChallengesHomeAdapter;

public class ChallengesHomeActivity extends BaseTabActivity {


//    @Deeplink("sdsd")
//    public static Intent getCallingReferral(Context context, Bundle extras) {
//        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
//        return new Intent(context, ChallengesHomeActivity.class)
//                .setData(uri.build())
//                .putExtras(extras);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.tab_challenges);

    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return new ChallengesHomeAdapter(getSupportFragmentManager());
    }

    @Override
    protected int getPageLimit() {
        return 0;
    }
}
