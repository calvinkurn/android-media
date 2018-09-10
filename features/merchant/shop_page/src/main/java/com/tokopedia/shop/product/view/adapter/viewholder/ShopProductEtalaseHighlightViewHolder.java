package com.tokopedia.shop.product.view.adapter.viewholder;

import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductEtalaseHighlightViewHolder extends AbstractViewHolder<ShopProductEtalaseHighlightViewModel> {

    private RecyclerView recyclerView;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_etalase_highlight;

    public ShopProductEtalaseHighlightViewHolder(View itemView, int deviceWidth) {
        super(itemView);
        findViews(itemView);
    }

    @Override
    public void bind(ShopProductEtalaseHighlightViewModel shopProductEtalaseHighlightViewModel) {
        Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewEtalaseHighLight);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

}
