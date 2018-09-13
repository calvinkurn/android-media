package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapter;
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapterTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopProductEtalaseHighlightViewHolder extends AbstractViewHolder<ShopProductEtalaseHighlightViewModel> {

    private RecyclerView recyclerView;
    private EtalaseHighlightAdapter etalaseHighlightAdapter;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_etalase_highlight;

    public ShopProductEtalaseHighlightViewHolder(View itemView, int deviceWidth,
                                                 ShopProductClickedNewListener shopProductClickedNewListener) {
        super(itemView);
        etalaseHighlightAdapter = new EtalaseHighlightAdapter(
                new EtalaseHighlightAdapterTypeFactory(shopProductClickedNewListener,deviceWidth));
        findViews(itemView);
    }

    @Override
    public void bind(ShopProductEtalaseHighlightViewModel shopProductEtalaseHighlightViewModel) {
        List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList
                = shopProductEtalaseHighlightViewModel.getEtalaseHighlightCarouselViewModelList();
        if (etalaseHighlightCarouselViewModelList == null) {
            etalaseHighlightCarouselViewModelList = new ArrayList<>();
        }
        etalaseHighlightAdapter.softClear();
        etalaseHighlightAdapter.setElement(etalaseHighlightCarouselViewModelList);
        etalaseHighlightAdapter.notifyDataSetChanged();
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewEtalaseHighLight);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(etalaseHighlightAdapter);
    }

}
