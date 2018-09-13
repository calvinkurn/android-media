package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.List;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductCarouselViewHolder extends AbstractViewHolder<Visitable> {

    private TextView tvTitle;
    private RecyclerView recyclerView;
    private ShopProductAdapter shopProductCarouselAdapter;
    private boolean isVerticalLayout;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_carousel;
    public static final int VERTICAL_LAYOUT = R.layout.item_shop_product_carousel_vertical;

    public ShopProductCarouselViewHolder(View itemView, int deviceWidth,
                                         ShopProductClickedNewListener shopProductClickedNewListener,
                                         boolean isVerticalLayout, String titleString,
                                         @ShopTrackProductTypeDef int shopTrackType) {
        super(itemView);
        this.isVerticalLayout = isVerticalLayout;
        shopProductCarouselAdapter = new ShopProductAdapter(new ShopProductAdapterTypeFactory(
                null,
                shopProductClickedNewListener, null,
                null,
                false, deviceWidth, shopTrackType));
        findViews(itemView);
        tvTitle.setText(titleString);
    }

    @Override
    public void bind(Visitable visitable) {
        Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if (visitable instanceof ShopProductFeaturedViewModel) {
            shopProductCarouselAdapter.replaceProductList(
                    ((ShopProductFeaturedViewModel)visitable).getShopProductFeaturedViewModelList());
        } else if (visitable instanceof EtalaseHighlightCarouselViewModel){
            shopProductCarouselAdapter.replaceProductList(
                    ((EtalaseHighlightCarouselViewModel)visitable).getShopProductViewModelList());
            tvTitle.setText(((EtalaseHighlightCarouselViewModel) visitable).getShopEtalaseViewModel().getEtalaseName());
        }
        shopProductCarouselAdapter.notifyDataSetChanged();

        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void findViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
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
