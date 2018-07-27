package com.tokopedia.kol.feature.post.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.kol.feature.post.view.fragment.KolPostDetailFragment;

/**
 * @author by yfsx on 23/07/18.
 */
public class KolPostDetailActivity extends BaseSimpleActivity {

    public static final String PARAM_POST_ID = "param_post_id";

    public static Intent getInstance(Context context, String id) {
        Intent intent = new Intent(context, KolPostDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_POST_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return KolPostDetailFragment.getInstance(getIntent().getExtras());
    }

}
