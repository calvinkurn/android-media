package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteShopViewHolder extends AbstractViewHolder<FavoriteShopViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.listview_manage_favorited_shop;

    private FavoriteShopViewModel favoriteShop;
    private Context context;
    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView locationTextview;
    private ImageView favoriteImageView;
    private ImageView badgeIcon;

    public FavoriteShopViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
        context = itemView.getContext();
    }

    private void initView(View itemView) {
        avatarImageView = itemView.findViewById(R.id.shop_avatar);
        nameTextView = itemView.findViewById(R.id.shop_name);
        locationTextview = itemView.findViewById(R.id.location);
        favoriteImageView = itemView.findViewById(R.id.fav_button);
        badgeIcon = itemView.findViewById(R.id.image_badge);
        View shopLayout = itemView.findViewById(R.id.shop_layout);
        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShopLayoutClicked();
            }
        });
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

    void onShopLayoutClicked() {
        UnifyTracking.eventFavoriteShop(context);
        Intent intent = ShopPageActivity.createIntent(context, favoriteShop.getShopId());
        context.startActivity(intent);
    }
}
