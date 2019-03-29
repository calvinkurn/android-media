package com.tokopedia.recentview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.recentview.view.activity.RecentViewActivity;

public class RecentViewInternalRouter {
    public static Intent getRecentViewIntent(Context context) {
        return RecentViewActivity.newInstance(context);
    }

    public static Intent getRecentViewIntentFromDeeplink(Context context, Bundle extras){
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return RecentViewActivity.newInstance(context)
                    .setData(uri.build())
                    .putExtras(extras);
    }
}
