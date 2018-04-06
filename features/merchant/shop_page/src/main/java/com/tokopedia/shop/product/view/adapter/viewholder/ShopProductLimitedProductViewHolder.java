package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedProductAdapter;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedProductViewHolder extends AbstractViewHolder<ShopProductLimitedProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_product;
    private static final int SPAN_COUNT = 2;
    private RecyclerView recyclerView;
    private AppCompatButton showMoreProductButton;
    private LabelView etalaseButton;

    private ShopProductClickedListener shopProductClickedListener;


    public ShopProductLimitedProductViewHolder(View itemView,
                                               View.OnClickListener showMoreProductOnclickListener,
                                               View.OnClickListener showMoreEtalaseOnclickListener,
                                               ShopProductClickedListener shopProductClickedListener) {
        super(itemView);
        this.shopProductClickedListener = shopProductClickedListener;
        findViews(itemView, showMoreProductOnclickListener, showMoreEtalaseOnclickListener);
    }

    private void findViews(View view, View.OnClickListener showMoreProductOnclickListener, View.OnClickListener showMoreEtalaseOnclickListener) {
        recyclerView = view.findViewById(R.id.recycler_view);
        etalaseButton = view.findViewById(R.id.label_view_etalase);
        etalaseButton.setOnClickListener(showMoreEtalaseOnclickListener);
        showMoreProductButton = view.findViewById(R.id.button_show_complete_product);
        showMoreProductButton.setOnClickListener(showMoreProductOnclickListener);
    }

    @Override
    public void bind(ShopProductLimitedProductViewModel shopProductLimitedProductViewModel) {
        ShopProductLimitedProductAdapter adapter = new ShopProductLimitedProductAdapter(shopProductClickedListener);
        adapter.setList(shopProductLimitedProductViewModel.getShopProductViewModelList());
        LinearLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}