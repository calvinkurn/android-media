package com.tokopedia.imagesearch.search.fragment.product;

import android.content.Context;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactory;
import com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder.GridProductItemViewHolder;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListAdapter extends BaseAdapter<ImageProductListTypeFactory> {

    public ImageProductListAdapter(ImageProductListTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }

    public void updateWishlistStatus(String productId, boolean isWishlisted) {

        for (int i = 0; i < visitables.size(); i++) {
            if (visitables.get(i) instanceof ProductItem) {
                ProductItem model = ((ProductItem) visitables.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlisted(isWishlisted);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void updateWishlistStatus(int adapterPosition, boolean isWishlisted) {
        if (visitables.get(adapterPosition) instanceof ProductItem) {
            ((ProductItem) visitables.get(adapterPosition)).setWishlisted(isWishlisted);
            notifyItemChanged(adapterPosition);
        }
    }

    public void setWishlistButtonEnabled(String productId, boolean isEnabled) {
        for (int i = 0; i < visitables.size(); i++) {
            if (visitables.get(i) instanceof ProductItem) {
                ProductItem model = ((ProductItem) visitables.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlistButtonEnabled(isEnabled);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public boolean isProductItem(int position) {
        return checkDataSize(position) && visitables.get(position) instanceof ProductItem;
    }

    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && visitables.get(position) instanceof EmptyModel;
    }

    private boolean checkDataSize(int position) {
        return getList() != null && getList().size() > 0
                && position > -1 && position < getList().size();
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        setFullSpanForStaggeredGrid(holder, holder.getItemViewType());
    }

    private void setFullSpanForStaggeredGrid(AbstractViewHolder holder, int viewType) {
        if(holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();

            layoutParams.setFullSpan(isStaggeredGridFullSpan(viewType));
        }
    }

    private boolean isStaggeredGridFullSpan(int viewType) {
        return viewType != GridProductItemViewHolder.LAYOUT;
    }
}
