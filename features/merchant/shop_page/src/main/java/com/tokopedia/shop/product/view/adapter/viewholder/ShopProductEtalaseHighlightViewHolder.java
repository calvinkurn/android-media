package com.tokopedia.shop.product.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapter;
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapterTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
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
                                                 ShopProductClickedListener shopProductClickedListener,
                                                 ShopCarouselSeeAllClickedListener shopCarouselSeeAllClickedListener) {
        super(itemView);
        etalaseHighlightAdapter = new EtalaseHighlightAdapter(
                new EtalaseHighlightAdapterTypeFactory(shopProductClickedListener,
                        shopCarouselSeeAllClickedListener,
                        deviceWidth));
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
