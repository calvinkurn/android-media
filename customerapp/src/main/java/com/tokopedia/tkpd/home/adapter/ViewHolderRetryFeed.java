package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;

/**
 * @author Erry on 2/27/17.
 */

public class ViewHolderRetryFeed extends RecyclerView.ViewHolder {

    private Context context;
    public TextView mainRetry;

    public ViewHolderRetryFeed(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mainRetry = (TextView) itemView.findViewById(R.id.main_retry);
    }

}
