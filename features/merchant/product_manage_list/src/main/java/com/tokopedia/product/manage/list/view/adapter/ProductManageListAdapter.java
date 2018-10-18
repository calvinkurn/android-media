package com.tokopedia.product.manage.list.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.base.list.seller.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.list.constant.StatusProductOption;
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ProductManageListAdapter extends BaseMultipleCheckListAdapter<ProductManageViewModel> implements ProductManageListViewHolder.ClickOptionCallbackHolder {


    public interface ClickOptionCallback {
        void onClickOptionItem(ProductManageViewModel productManageViewModel);

        boolean isActionModeActive();
    }

    private ClickOptionCallback clickOptionCallback;

    private List<String> featuredProduct;
    private boolean isActionMode;

    public void setFeaturedProduct(List<String> featuredProduct) {
        this.featuredProduct = featuredProduct;
    }

    public List<String> getFeaturedProduct() {
        return featuredProduct;
    }

    public ProductManageListAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductManageViewModel.TYPE:
                ProductManageListViewHolder productManageListViewHolder = new ProductManageListViewHolder(
                        getLayoutView(parent, R.layout.item_manage_product_list));
                productManageListViewHolder.setClickOptionCallbackHolder(this);
                return productManageListViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected void bindData(final int position, final RecyclerView.ViewHolder viewHolder) {
        super.bindData(position, viewHolder);
        final ProductManageViewModel productManageViewModel = data.get(position);
        final boolean statusUnderSupervision = productManageViewModel.getProductStatus().equals(StatusProductOption.UNDER_SUPERVISION);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClicked(productManageViewModel);
                }
                if (clickOptionCallback != null && clickOptionCallback.isActionModeActive() && !statusUnderSupervision) {
                    boolean checked = ((BaseMultipleCheckViewHolder<ProductManageViewModel>) viewHolder).isChecked();
                    ((BaseMultipleCheckViewHolder<ProductManageViewModel>) viewHolder).setChecked(!checked);
                    updateChecked(data.get(position), position, !checked);
                }
            }
        });
        ((ProductManageListViewHolder) viewHolder).bindFeaturedProduct(isFeaturedProduct(data.get(position).getProductId()));
        ((ProductManageListViewHolder) viewHolder).bindActionMode(isActionMode);
    }

    public void setClickOptionCallback(ClickOptionCallback clickOptionCallback) {
        this.clickOptionCallback = clickOptionCallback;
    }

    @Override
    public void onClickOptionItem(ProductManageViewModel productManageViewModel) {
        if (clickOptionCallback != null) {
            clickOptionCallback.onClickOptionItem(productManageViewModel);
        }
    }

    public void setActionMode(boolean actionMode) {
        isActionMode = actionMode;
    }

    private boolean isFeaturedProduct(String productId) {
        return featuredProduct != null && featuredProduct.contains(productId);
    }

    public void updatePrice(String productId, String price, String currencyId, String priceCurrency) {
        int i = 0;
        for (Iterator<ProductManageViewModel> it = data.iterator(); it.hasNext(); ) {
            ProductManageViewModel productManageViewModel = it.next();
            if (productManageViewModel.getId().equalsIgnoreCase(productId)) {
                productManageViewModel.setProductPricePlain(price);
                productManageViewModel.setProductCurrencyId(Integer.parseInt(currencyId));
                productManageViewModel.setProductCurrencySymbol(priceCurrency);
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }

    public void updateCashback(String productId, int cashback) {
        int i = 0;
        for (Iterator<ProductManageViewModel> it = data.iterator(); it.hasNext(); ) {
            ProductManageViewModel productManageViewModel = it.next();
            if (productManageViewModel.getId().equalsIgnoreCase(productId)) {
                productManageViewModel.setProductCashback(cashback);
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }
}
