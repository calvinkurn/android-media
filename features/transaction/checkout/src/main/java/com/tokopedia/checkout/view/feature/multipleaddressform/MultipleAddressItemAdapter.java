package com.tokopedia.checkout.view.feature.multipleaddressform;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.view.feature.multipleaddressform.viewholder.MultipleAddressItemViewHolder;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemAdapter extends RecyclerView.Adapter
        <MultipleAddressItemViewHolder> {

    private List<MultipleAddressItemData> itemDataList;
    private MultipleAddressItemAdapterListener listener;
    private MultipleAddressAdapterData productData;
    private int parentItemPosition;
    private CompositeSubscription compositeSubscription;

    public MultipleAddressItemAdapter(int parentItemPosition,
                                      MultipleAddressAdapterData productData,
                                      List<MultipleAddressItemData> itemDataList,
                                      MultipleAddressItemAdapterListener listener,
                                      CompositeSubscription compositeSubscription) {
        this.itemDataList = itemDataList;
        this.listener = listener;
        this.productData = productData;
        this.parentItemPosition = parentItemPosition;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public MultipleAddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multiple_address_item_adapter, parent, false);
        return new MultipleAddressItemViewHolder(itemView, compositeSubscription, this);
    }

    @Override
    public void onBindViewHolder(MultipleAddressItemViewHolder holder, int position) {
        MultipleAddressItemData itemData = itemDataList.get(position);
        holder.bindItemAdapterAddress(itemData, itemDataList, listener, parentItemPosition);
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    public List<MultipleAddressItemData> getItemDataList() {
        return itemDataList;
    }

    private View.OnClickListener onDeleteOrderClickedListener(int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        };
    }

    public interface MultipleAddressItemAdapterListener {

        void onEditItemChoosen(int parentItemPosition,
                               MultipleAddressAdapterData productData,
                               MultipleAddressItemData addressData);

        void onDeleteItem(MultipleAddressItemAdapter adapter, int position,
                          List<MultipleAddressItemData> multipleAddressItemDataList);

        void onChangeAddress(MultipleAddressItemAdapter adapter,
                             RecipientAddressModel recipientAddressModel,
                             int childPosition, int parentPosition);

        void onClickPlusQuantityButton();

        void onClickMinQuantityButton();
    }

}
