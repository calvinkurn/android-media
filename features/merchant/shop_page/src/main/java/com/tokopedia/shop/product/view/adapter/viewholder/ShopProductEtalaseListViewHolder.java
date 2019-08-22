package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.EtalaseChipAdapter;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductEtalaseListViewHolder extends AbstractViewHolder<ShopProductEtalaseListViewModel> implements EtalaseChipAdapter.OnEtalaseChipAdapterListener, View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_etalase_chip_list;

    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    private OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener;
    private View buttonEtalaseMore;

    public interface OnShopProductEtalaseListViewHolderListener{
        void onEtalaseChipClicked(ShopEtalaseViewModel shopEtalaseViewModel);
        void onEtalaseMoreListClicked();
    }

    private EtalaseChipAdapter etalaseChipAdapter;
    public ShopProductEtalaseListViewHolder(View itemView,
                                            OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener) {
        super(itemView);
        this.onShopProductEtalaseListViewHolderListener = onShopProductEtalaseListViewHolderListener;
        etalaseChipAdapter = new EtalaseChipAdapter(null, null, this);
        findViews(itemView);
        buttonEtalaseMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // click buttonEtalaseMore
        if (onShopProductEtalaseListViewHolderListener!= null) {
            onShopProductEtalaseListViewHolderListener.onEtalaseMoreListClicked();
        }
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

        buttonEtalaseMore = view.findViewById(R.id.v_etalase_more);
    }

    @Override
    public void bind(ShopProductEtalaseListViewModel shopProductEtalaseListViewModel) {
        if (recyclerViewState == null) {
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        }
        etalaseChipAdapter.setEtalaseViewModelList(shopProductEtalaseListViewModel.getEtalaseModelList());
        String selectedEtalaseId = shopProductEtalaseListViewModel.getSelectedEtalaseId();
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId);
        etalaseChipAdapter.notifyDataSetChanged();

        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewState = null;
        }
    }

    public void setRecyclerViewState(Parcelable recyclerViewState) {
        this.recyclerViewState = recyclerViewState;
    }

    public Parcelable getRecyclerViewState() {
        return recyclerView.getLayoutManager().onSaveInstanceState();
    }

    // scroll to selected position (only if the item is not visible)
    private void goToSelectedPositionIfNeeded(){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int firstPosVisible = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            int lastPosVisible = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            int position = etalaseChipAdapter.getSelectedPosition();
            if (position< firstPosVisible || position > lastPosVisible) {
                recyclerView.smoothScrollToPosition(position);
            }
        }
    }

    @Override
    public void onEtalaseChipClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        if (onShopProductEtalaseListViewHolderListener!= null) {
            onShopProductEtalaseListViewHolderListener.onEtalaseChipClicked(shopEtalaseViewModel);
        }
    }

}