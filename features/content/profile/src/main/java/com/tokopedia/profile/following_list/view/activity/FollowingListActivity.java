package com.tokopedia.profile.following_list.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.profile.R;
import com.tokopedia.profile.following_list.view.fragment.UserFollowingListFragment;

/**
 * Created by yfsx on 28/12/17.
 */

public class FollowingListActivity extends BaseSimpleActivity {
    public static final String ARGS_USER_ID = "user_id";
    public static final String ARGS_OPEN_FOLLOWER = "OPEN_FOLLOWER";

    public static Intent getFollowingInstance(Context context, int userId) {
        Intent intent = new Intent(context, FollowingListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_USER_ID, userId);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getFollowerInstance(Context context, int userId) {
        Intent intent = new Intent(context, FollowingListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_USER_ID, userId);
        bundle.putBoolean(ARGS_OPEN_FOLLOWER, true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(ARGS_OPEN_FOLLOWER, false)) {
            toolbar.setTitle(getResources().getString(R.string.title_follower_kol));
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return UserFollowingListFragment.createInstance(getIntent().getExtras());
    }
}
