package com.tokopedia.checkout.view.feature.removecartitem.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.removecartitem.RemoveCartItemAdapter;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartProductHeaderViewModel;

/**
 * @author Irfan Khoirul on 24/05/18.
 */
@Deprecated
public class RemoveCartProductHeaderViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_REMOVE_ALL_CHECKBOX =
            R.layout.view_item_remove_all_checkbox;

    private RemoveCartItemAdapter removeCartItemAdapter;

    private CheckBox cbSelectAll;

    public RemoveCartProductHeaderViewHolder(View itemView, RemoveCartItemAdapter removeCartItemAdapter) {
        super(itemView);
        this.removeCartItemAdapter = removeCartItemAdapter;
        cbSelectAll = itemView.findViewById(R.id.checkBox);
    }

    public void bindData(final CartProductHeaderViewModel cartProductHeaderViewModel) {
        cbSelectAll.setChecked(cartProductHeaderViewModel.isChecked());
        cbSelectAll.setClickable(false);

        itemView.setOnClickListener(view -> {
            cbSelectAll.setChecked(!cbSelectAll.isChecked());
            cartProductHeaderViewModel.setChecked(cbSelectAll.isChecked());
            removeCartItemAdapter.getRemoveCartItemViewListener().onAllItemCheckChanged(cbSelectAll.isChecked());
        });
    }

}
