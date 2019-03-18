package com.tokopedia.kol.feature.comment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * @author by nisie on 10/27/17.
 */

public class KolCommentActivity extends BaseSimpleActivity {

    public static final String ARGS_HEADER = "ARGS_HEADER";
    public static final String ARGS_FOOTER = "ARGS_FOOTER";
    public static final String ARGS_ID = "ARGS_ID";
    public static final String ARGS_POSITION = "ARGS_POSITION";
    public static final String ARGS_FROM_FEED = "ARGS_FROM_FEED";
    public static final String ARGS_KOL_ID = "id";
    public static final String ARGS_FROM_APPLINK = "isFromApplink";

    public static Intent getCallingIntent(Context context, int id, int rowNumber) {
        Intent intent = new Intent(context, KolCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_ID, id);
        bundle.putInt(ARGS_POSITION, rowNumber);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallingIntentFromFeed(Context context, int id, int rowNumber) {
        Intent intent = new Intent(context, KolCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_ID, id);
        bundle.putInt(ARGS_POSITION, rowNumber);
        bundle.putBoolean(ARGS_FROM_FEED, true);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink(ApplinkConst.KOL_COMMENT)
    public static Intent getCallingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, KolCommentActivity.class);
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, Integer.valueOf(bundle.getString(ARGS_KOL_ID)));
        args.putBoolean(ARGS_FROM_APPLINK, true);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        return KolCommentFragment.createInstance(bundle);
    }

    @Override
    public void onBackPressed() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                    KolEventTracking.Category.FEED_CONTENT_COMMENT_DETAIL,
                    KolEventTracking.Action.FEED_COMMENT_CLICK_BACK,
                    KolEventTracking.EventLabel.FEED_CONTENT_COMMENT_DETAIL_BACK
            ));
        super.onBackPressed();
    }
}
