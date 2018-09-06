package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductCarouselViewHolder extends AbstractViewHolder<ShopProductFeaturedViewModel> {

    private TextView tvTitle;
    private RecyclerView recyclerView;
    private ShopProductAdapter shopProductFeatureAdapter;
    private boolean isDataSizeSmall;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_feature;

    public ShopProductCarouselViewHolder(View itemView, int deviceWidth,
                                         ShopProductClickedNewListener shopProductClickedNewListener,
                                         boolean isDataSizeSmall, String titleString) {
        super(itemView);
        this.isDataSizeSmall = isDataSizeSmall;
        shopProductFeatureAdapter = new ShopProductAdapter(new ShopProductAdapterTypeFactory(
                null,
                shopProductClickedNewListener, null,
                null,
                false, deviceWidth, true));
        findViews(itemView);
        tvTitle.setText(titleString);
    }

    @Override
    public void bind(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        shopProductFeatureAdapter.replaceProductList(shopProductFeaturedViewModel.getShopProductFeaturedViewModelList());
        shopProductFeatureAdapter.notifyDataSetChanged();

        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void findViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.recyclerViewFeature);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                isDataSizeSmall ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setAdapter(shopProductFeatureAdapter);
    }

}
