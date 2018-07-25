package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductFeaturedViewHolder extends AbstractViewHolder<ShopProductFeaturedViewModel> {

    private RecyclerView recyclerView;
    private ShopProductAdapter shopProductAdapter;
    private Parcelable recyclerViewState;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_feature_new;

    public ShopProductFeaturedViewHolder(View itemView, ShopProductClickedNewListener shopProductClickedNewListener) {
        super(itemView);
        shopProductAdapter = new ShopProductAdapter(new ShopProductAdapterTypeFactory(
                null,
                shopProductClickedNewListener, null,
                null, null,
                true , true));
        findViews(itemView);
    }

    @Override
    public void bind(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        shopProductAdapter.replaceProductList(shopProductFeaturedViewModel.getShopProductFeaturedViewModelList());
        shopProductAdapter.notifyDataSetChanged();

        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewState = null;
        }
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewFeature);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setAdapter(shopProductAdapter);
    }

}
