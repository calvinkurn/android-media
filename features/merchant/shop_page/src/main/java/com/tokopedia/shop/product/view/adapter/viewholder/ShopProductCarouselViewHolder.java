package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.shop.R;
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductCarouselViewHolder extends AbstractViewHolder<Visitable> {

    private TextView tvTitle;
    private TextView tvSeeAll;
    private RecyclerView recyclerView;
    private ShopProductAdapter shopProductCarouselAdapter;
    private boolean isVerticalLayout;
    private ImageView ivBadge;

    private ShopCarouselSeeAllClickedListener shopCarouselSeeAllClickedListener;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_carousel;
    public static final int VERTICAL_LAYOUT = R.layout.item_shop_product_carousel_vertical;

    public ShopProductCarouselViewHolder(View itemView, int deviceWidth,
                                         ShopProductClickedListener shopProductClickedListener,
                                         boolean isVerticalLayout, String titleString,
                                         @ShopTrackProductTypeDef int shopTrackType,
                                         ShopCarouselSeeAllClickedListener shopCarouselSeeAllClickedListener) {
        super(itemView);
        this.isVerticalLayout = isVerticalLayout;
        this.shopCarouselSeeAllClickedListener = shopCarouselSeeAllClickedListener;
        shopProductCarouselAdapter = new ShopProductAdapter(new ShopProductAdapterTypeFactory(
                shopProductClickedListener, null, null,
                null, null,
                false, deviceWidth, shopTrackType));
        findViews(itemView);
        tvTitle.setText(titleString);
    }

    @Override
    public void bind(Visitable visitable) {
        Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if (visitable instanceof ShopProductFeaturedViewModel) {
            shopProductCarouselAdapter.replaceProductList(
                    ((ShopProductFeaturedViewModel) visitable).getShopProductFeaturedViewModelList());
            tvSeeAll.setVisibility(View.GONE);
        } else if (visitable instanceof EtalaseHighlightCarouselViewModel) {
            shopProductCarouselAdapter.replaceProductList(
                    ((EtalaseHighlightCarouselViewModel) visitable).getShopProductViewModelList());
            ShopEtalaseViewModel shopEtalaseViewModel = ((EtalaseHighlightCarouselViewModel) visitable).getShopEtalaseViewModel();
            tvTitle.setText(shopEtalaseViewModel.getEtalaseName());
            if (!TextUtils.isEmpty(shopEtalaseViewModel.getEtalaseBadge())){
                ImageHandler.LoadImage(ivBadge, shopEtalaseViewModel.getEtalaseBadge());
                ivBadge.setVisibility(View.VISIBLE);
            } else {
                ivBadge.setVisibility(View.GONE);
            }
            tvSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shopCarouselSeeAllClickedListener != null) {
                        shopCarouselSeeAllClickedListener.onSeeAllClicked(shopEtalaseViewModel);
                    }
                }
            });
            tvSeeAll.setVisibility(View.VISIBLE);
        }

        shopProductCarouselAdapter.notifyDataSetChanged();

        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void findViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        ivBadge = view.findViewById(R.id.image_view_etalase_badge);
        tvSeeAll = view.findViewById(R.id.tvSeeAll);
        recyclerView = view.findViewById(R.id.recyclerViewCarousel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                isVerticalLayout ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setAdapter(shopProductCarouselAdapter);

    }

}
