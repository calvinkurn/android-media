package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreUiModel;
import com.tokopedia.viewallcard.ViewAllCard;

import kotlin.Unit;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShowMoreViewHolder extends AbstractViewHolder<BannerShopViewMoreUiModel> {

    @LayoutRes
    public static int LAYOUT = com.tokopedia.topads.sdk.R.layout.layout_ads_banner_shop_a_more;
    private static final String className = BannerShowMoreViewHolder.class.getSimpleName();

    private static final Integer CARD_TYPE_BORDER = 1;
    private final TopAdsBannerClickListener topAdsBannerClickListener;

    public BannerShowMoreViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
    }

    @Override
    public void bind(final BannerShopViewMoreUiModel element) {
        @Nullable ViewAllCard viewAll = itemView.findViewById(R.id.viewAllAdsBannerShop);
        if(viewAll != null){
            viewAll.getCardView().setCardType(CARD_TYPE_BORDER);
            viewAll.setCta("", () -> {
                invokeClickListener(element);
                return Unit.INSTANCE;
            });
        }
        itemView.setOnClickListener(v -> {
            invokeClickListener(element);
        });
    }

    private void invokeClickListener(BannerShopViewMoreUiModel element) {
        if(topAdsBannerClickListener!=null) {
            topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
            new TopAdsUrlHitter(itemView.getContext()).hitClickUrl(className, element.getAdsClickUrl(),"","","");
        }
    }
}
