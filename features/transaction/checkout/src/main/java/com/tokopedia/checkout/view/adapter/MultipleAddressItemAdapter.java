package com.tokopedia.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.view.viewholder.MultipleAddressItemViewHolder;

import java.util.List;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemAdapter extends RecyclerView.Adapter
        <MultipleAddressItemViewHolder> {

    private List<MultipleAddressItemData> itemDataList;
    private MultipleAddressItemAdapterListener listener;
    private MultipleAddressAdapterData productData;
    private int parentItemPosition;

    public MultipleAddressItemAdapter(int parentItemPosition,
                                      MultipleAddressAdapterData productData,
                                      List<MultipleAddressItemData> itemDataList,
                                      MultipleAddressItemAdapterListener listener) {
        this.itemDataList = itemDataList;
        this.listener = listener;
        this.productData = productData;
        this.parentItemPosition = parentItemPosition;
    }

    @Override
    public MultipleAddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multiple_address_item_adapter, parent, false);
        return new MultipleAddressItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MultipleAddressItemViewHolder holder, int position) {
        MultipleAddressItemData itemData = itemDataList.get(position);
        holder.bindItemAdapterAddress(
                itemData,
                itemDataList,
                onDeleteOrderClickedListener(position),
                position);
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    private View.OnClickListener onDeleteOrderClickedListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String defaultCartId = null;
                for (MultipleAddressItemData multipleAddressItemData : itemDataList) {
                    if (!TextUtils.isEmpty(multipleAddressItemData.getCartId()) &&
                            !multipleAddressItemData.getCartId().equals("0")) {
                        defaultCartId = multipleAddressItemData.getCartId();
                    } else {
                        if (!TextUtils.isEmpty(defaultCartId) ||
                                multipleAddressItemData.getCartId().equals("0")) {
                            multipleAddressItemData.setCartId(defaultCartId);
                            break;
                        }
                    }
                }
                itemDataList.remove(position);
                notifyDataSetChanged();
            }
        };
    }

    public interface MultipleAddressItemAdapterListener {

        void onEditItemChoosen(int parentItemPosition,
                               MultipleAddressAdapterData productData,
                               MultipleAddressItemData addressData);

    }

}
