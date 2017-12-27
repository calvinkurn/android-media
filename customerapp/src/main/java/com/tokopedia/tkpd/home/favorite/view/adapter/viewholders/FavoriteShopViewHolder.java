package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteShopViewHolder extends AbstractViewHolder<FavoriteShopViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.listview_manage_favorited_shop;

    private FavoriteShopViewModel favoriteShop;
    private Context context;
    @BindView(R.id.shop_avatar)
    ImageView avatarImageView;
    @BindView(R.id.shop_name)
    TextView nameTextView;
    @BindView(R.id.location)
    TextView locationTextview;
    @BindView(R.id.fav_button)
    ImageView favoriteImageView;
    @BindView(R.id.image_badge)
    ImageView badgeIcon;

    public FavoriteShopViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    @Override
    public void bind(FavoriteShopViewModel favoriteShop) {
        this.favoriteShop = favoriteShop;
        if (favoriteShop.getShopName() != null) {
            nameTextView.setText(MethodChecker.fromHtml(favoriteShop.getShopName()));
        }
        if (favoriteShop.getShopLocation() != null) {
            locationTextview.setText(favoriteShop.getShopLocation());
        }
        favoriteImageView.setImageResource(
                favoriteShop.isFavoriteShop() ? R.drawable.ic_faved : R.drawable.ic_fav);
        if(favoriteShop.getShopAvatarImageUrl() !=null) {
            ImageHandler.loadImageFit2(
                    itemView.getContext(), avatarImageView, favoriteShop.getShopAvatarImageUrl());
        }
        if(favoriteShop.getBadgeUrl() != null && !favoriteShop.getBadgeUrl().isEmpty()) {
            badgeIcon.setVisibility(View.VISIBLE);
            ImageHandler.loadImageFit2(itemView.getContext(), badgeIcon, favoriteShop.getBadgeUrl());
        } else {
            badgeIcon.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.shop_layout)
    void onShopLayoutClicked() {
        UnifyTracking.eventFavoriteShop();
        Intent intent = new Intent(context, ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(favoriteShop.getShopId(), "");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
