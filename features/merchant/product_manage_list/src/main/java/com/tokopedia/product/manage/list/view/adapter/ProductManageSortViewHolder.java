package com.tokopedia.product.manage.list.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.product.manage.list.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.product.manage.list.constant.SortProductOption;
import com.tokopedia.product.manage.list.view.model.ProductManageSortModel;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public class ProductManageSortViewHolder extends BaseViewHolder<ProductManageSortModel> {

    public interface ListenerCheckedSort {
        boolean isItemChecked(@SortProductOption String productSortId);
    }

    private ListenerCheckedSort listenerCheckedSort;
    private TextView titleSort;
    private ImageView imageCheckList;

    public ProductManageSortViewHolder(View layoutView) {
        super(layoutView);
        titleSort = (TextView) layoutView.findViewById(R.id.text_view_title);
        imageCheckList = (ImageView) layoutView.findViewById(R.id.image_view_check);
    }

    public void setListenerCheckedSort(ListenerCheckedSort listenerCheckedSort) {
        this.listenerCheckedSort = listenerCheckedSort;
    }

    @Override
    public void bindObject(ProductManageSortModel productManageSortModel) {
        boolean isItemChecked = false;
        if (listenerCheckedSort != null) {
            isItemChecked = listenerCheckedSort.isItemChecked(productManageSortModel.getId());
        }

        titleSort.setText(productManageSortModel.getTitleSort());
        if (isItemChecked) {
            imageCheckList.setVisibility(View.VISIBLE);
        } else {
            imageCheckList.setVisibility(View.INVISIBLE);
        }
    }
}
