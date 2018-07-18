package com.tokopedia.shop.product.view.adapter.newadapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.newadapter.EtalaseChipAdapter;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseListViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductEtalaseListViewHolder extends AbstractViewHolder<ShopProductEtalaseListViewModel> implements EtalaseChipAdapter.OnEtalaseChipAdapterListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_etalase_title_view;
    private RecyclerView recyclerView;

    private OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener;
    public interface OnShopProductEtalaseListViewHolderListener{
        void onEtalaseChipClicked(ShopEtalaseViewModel shopEtalaseViewModel);
    }
    private EtalaseChipAdapter etalaseChipAdapter;
    public ShopProductEtalaseListViewHolder(View itemView, OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener) {
        super(itemView);
        this.onShopProductEtalaseListViewHolderListener = onShopProductEtalaseListViewHolderListener;
        etalaseChipAdapter = new EtalaseChipAdapter(null, null, this);
        findViews(itemView);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setAdapter(etalaseChipAdapter);
    }

    @Override
    public void bind(ShopProductEtalaseListViewModel shopProductEtalaseListViewModel) {
        etalaseChipAdapter.setEtalaseViewModelList(shopProductEtalaseListViewModel.getEtalaseModelList());
        String selectedEtalaseId = shopProductEtalaseListViewModel.getSelectedEtalaseId();
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId);
        etalaseChipAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEtalaseChipClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        if (onShopProductEtalaseListViewHolderListener!= null) {
            onShopProductEtalaseListViewHolderListener.onEtalaseChipClicked(shopEtalaseViewModel);
        }
    }

}