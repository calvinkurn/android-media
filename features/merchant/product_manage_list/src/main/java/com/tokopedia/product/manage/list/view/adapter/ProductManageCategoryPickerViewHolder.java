package com.tokopedia.product.manage.list.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.product.manage.list.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.product.manage.list.view.model.ProductManageCategoryViewModel;
import com.tokopedia.product.manage.list.view.model.ProductManageSortModel;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public class ProductManageCategoryPickerViewHolder extends BaseViewHolder<ProductManageCategoryViewModel> {

    public interface ListenerCheckedCategory {
        boolean isItemChecked(long idCateogrySelected);
    }

    private ListenerCheckedCategory listenerCheckedCategory;
    private TextView titleSort;
    private ImageView imageCheckList;

    public ProductManageCategoryPickerViewHolder(View layoutView) {
        super(layoutView);
        titleSort = (TextView) layoutView.findViewById(R.id.text_view_title);
        imageCheckList = (ImageView) layoutView.findViewById(R.id.image_view_check);
    }

    public void setListenerCheckedCategory(ListenerCheckedCategory listenerCheckedCategory) {
        this.listenerCheckedCategory = listenerCheckedCategory;
    }

    @Override
    public void bindObject(ProductManageCategoryViewModel productManageCategoryViewModel) {
        boolean isItemChecked = false;
        if (listenerCheckedCategory != null) {
            isItemChecked = listenerCheckedCategory.isItemChecked(productManageCategoryViewModel.getId());
        }

        titleSort.setText(productManageCategoryViewModel.getName());
        if (isItemChecked) {
            imageCheckList.setVisibility(View.VISIBLE);
        } else {
            imageCheckList.setVisibility(View.INVISIBLE);
        }
    }
}
