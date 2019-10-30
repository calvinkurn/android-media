package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsGroupManagePromoFragment;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsGroupManagePromoActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, String adId, int choosenOption,
                                      String groupName, String groupId){
        Intent intent = new Intent(context, TopAdsGroupManagePromoActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP, choosenOption);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        return TopAdsGroupManagePromoFragment.createInstance(
                getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID),
                getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP, 0),
                getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_GROUP_ID),
                getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME));
    }

    @Override
    protected String getTagFragment() {
        return TopAdsGroupManagePromoFragment.class.getSimpleName();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
