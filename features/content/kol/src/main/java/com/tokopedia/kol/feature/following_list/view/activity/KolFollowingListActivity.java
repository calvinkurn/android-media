package com.tokopedia.kol.feature.following_list.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.kol.feature.following_list.view.fragment.KolFollowingListFragment;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListActivity extends BaseSimpleActivity {
    public static final String ARGS_USER_ID = "user_id";

    public static Intent getCallingIntent(Context context, int userId) {
        Intent intent = new Intent(context, KolFollowingListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_USER_ID, userId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return KolFollowingListFragment.createInstance(getIntent().getExtras());
    }
}
