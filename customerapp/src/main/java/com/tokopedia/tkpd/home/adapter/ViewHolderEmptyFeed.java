package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsView;

/**
 * @author Kulomady on 2/27/17.
 */

public class ViewHolderEmptyFeed extends RecyclerView.ViewHolder {

    public TextView checkFavoriteShopButton;
    public View officialStoreLinkContainer;
    public LinearLayout container;
    private Context context;

    public ViewHolderEmptyFeed(View itemView) {
        super(itemView);
        context = itemView.getContext();
        checkFavoriteShopButton = (TextView) itemView.findViewById(R.id.find_favorite_shop);
        officialStoreLinkContainer = itemView.findViewById(R.id.official_store_link_container);
        container = (LinearLayout) itemView.findViewById(R.id.empty_product_feed);
    }

    public void generateTopAds(){
        for (int i = 1; i <= 3; i++) {
            generateTopAds(i);
        }
    }

    private void generateTopAds(int page) {
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, TopAdsParams.SRC_PRODUCT_FEED);
        params.getParam().put(TopAdsParams.KEY_PAGE, String.valueOf(page));
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(context))
                .setEndpoint(Endpoint.SHOP)
                .topAdsParams(params)
                .build();
        TopAdsView adsView = new TopAdsView(context);
        adsView.setConfig(config);
        adsView.setMaxItems(1);
        adsView.setDisplayMode(DisplayMode.FEED);
        adsView.loadTopAds();
        container.addView(adsView);
    }

}
