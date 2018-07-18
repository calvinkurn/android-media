package com.tokopedia.shop.product.view.adapter.newadapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductNewAdapter;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductFeaturedViewHolder extends AbstractViewHolder<ShopProductFeaturedViewModel> {

    private RecyclerView recyclerView;
    private ShopProductNewAdapter shopProductNewAdapter;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_feature_new;

    public ShopProductFeaturedViewHolder(View itemView, ShopProductClickedNewListener shopProductClickedNewListener) {
        super(itemView);
        shopProductNewAdapter = new ShopProductNewAdapter(new ShopProductAdapterTypeFactory(
                null,
                shopProductClickedNewListener, null,
                null, true ));
        findViews(itemView);
    }

    @Override
    public void bind(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        shopProductNewAdapter.replaceProductList(shopProductFeaturedViewModel.getShopProductFeaturedViewModelList());
        shopProductNewAdapter.notifyDataSetChanged();
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewFeature);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setAdapter(shopProductNewAdapter);
    }

}
