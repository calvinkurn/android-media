package com.tokopedia.product.manage.list.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.product.manage.list.view.factory.ProductManageFragmentFactoryImpl;
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel;

import java.util.HashSet;
import java.util.List;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ProductManageListAdapter extends BaseListCheckableAdapter<ProductManageViewModel, ProductManageFragmentFactoryImpl>{

    public ProductManageListAdapter(ProductManageFragmentFactoryImpl baseListAdapterTypeFactory, BaseListCheckableAdapter.OnCheckableAdapterListener<ProductManageViewModel> onCheckableAdapterListener) {
        super(baseListAdapterTypeFactory, onCheckableAdapterListener);
    }

    @Override
    public void resetCheckedItemSet() {
        super.resetCheckedItemSet();
    }

    @Override
    public int getTotalChecked() {
        return super.getTotalChecked();
    }

    @Override
    public List<ProductManageViewModel> getCheckedDataList() {
        return super.getCheckedDataList();
    }

    @Override
    public void updateListByCheck(boolean isItemChecked, int position) {
        super.updateListByCheck(isItemChecked, position);
    }

    @Override
    public void setCheckedPositionList(HashSet<Integer> checkedPositionList) {
        super.setCheckedPositionList(checkedPositionList);
    }

    @Override
    public boolean isChecked(int position) {
        return super.isChecked(position);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    public void updatePrice(String productId, String price, String currencyId, String priceCurrency) {
        int i = 0;
        for (ProductManageViewModel productManageViewModel : getData()) {
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
        for (ProductManageViewModel productManageViewModel : getData()) {
            if (productManageViewModel.getId().equalsIgnoreCase(productId)) {
                productManageViewModel.setProductCashback(cashback);
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }
}
