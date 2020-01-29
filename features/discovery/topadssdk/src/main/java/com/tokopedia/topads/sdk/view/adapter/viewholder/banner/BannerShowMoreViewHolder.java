package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShowMoreViewHolder extends AbstractViewHolder<BannerShopViewMoreModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_ads_banner_shop_a_more;
    private static final String TAG = BannerShowMoreViewHolder.class.getSimpleName();
    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private TextView txtMore;

    public BannerShowMoreViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        txtMore = itemView.findViewById(R.id.txt_more);
    }

    @Override
    public void bind(final BannerShopViewMoreModel element) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topAdsBannerClickListener!=null) {
                    topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
                }
            }
        });
    }
}
