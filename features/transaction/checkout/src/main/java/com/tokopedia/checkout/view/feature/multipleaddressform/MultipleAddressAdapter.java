package com.tokopedia.checkout.view.feature.multipleaddressform;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.view.feature.multipleaddressform.viewholder.MultipleAddressViewHolder;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements MultipleAddressItemAdapter.MultipleAddressItemAdapterListener {

    private static final int MULTIPLE_ADDRESS_ADAPTER_LAYOUT =
            R.layout.multiple_address_adapter;
    private static final int MULTIPLE_ADDRESS_FOOTER_LAYOUT =
            R.layout.multiple_address_footer;

    private ArrayList<MultipleAddressAdapterData> addressData;
    private MultipleAddressAdapterListener listener;
    private List<Object> adapterObjectList;
    private CompositeSubscription compositeSubscription;

    public MultipleAddressAdapter(List<MultipleAddressAdapterData> addressData,
                                  MultipleAddressAdapterListener listener) {
        this.addressData = new ArrayList<>(addressData);
        this.listener = listener;
        compositeSubscription = new CompositeSubscription();
        adapterObjectList = new ArrayList<>();
        adapterObjectList.addAll(addressData);
        adapterObjectList.add(addressData);
    }

    @Override
    public int getItemViewType(int position) {
        if (adapterObjectList.get(position) instanceof MultipleAddressAdapterData)
            return MULTIPLE_ADDRESS_ADAPTER_LAYOUT;
        else if (adapterObjectList.get(position) instanceof List)
            return MULTIPLE_ADDRESS_FOOTER_LAYOUT;
        else
            return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == MULTIPLE_ADDRESS_FOOTER_LAYOUT)
            return new MultipleAddressFooterViewHolder(itemView);
        else return new MultipleAddressViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == MULTIPLE_ADDRESS_ADAPTER_LAYOUT) {
            MultipleAddressViewHolder itemViewHolder = (MultipleAddressViewHolder) holder;
            MultipleAddressAdapterData data = (MultipleAddressAdapterData)
                    adapterObjectList.get(position);
            itemViewHolder.bindAdapterView(addressData, data, this, listener, compositeSubscription, isFirstItem(data));
        } else if (getItemViewType(position) == MULTIPLE_ADDRESS_FOOTER_LAYOUT) {
            ((MultipleAddressFooterViewHolder) holder).goToCourierPageButton
                    .setOnClickListener(onGoToCourierPageButtonClicked(addressData));
        }
    }

    private boolean isFirstItem(MultipleAddressAdapterData data) {
        return (data.getItemListData().get(0).getCartPosition() == 0);
    }

    @Override
    public int getItemCount() {
        return adapterObjectList.size();
    }

    @Override
    public void onEditItemChoosen(int parentItemPosotion, MultipleAddressAdapterData productData,
                                  MultipleAddressItemData addressData) {
        listener.onItemChoosen(parentItemPosotion, this.addressData, productData, addressData);
    }

    @Override
    public void onDeleteItem(MultipleAddressItemAdapter adapter, int position,
                             List<MultipleAddressItemData> multipleAddressItemDataList) {
        listener.onDeleteItem(position, multipleAddressItemDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChangeAddress(MultipleAddressItemAdapter adapter,
                                RecipientAddressModel recipientAddressModel,
                                int childPosition, int parentPosition) {
        listener.onChangeAddress(adapter, addressData, recipientAddressModel, childPosition, parentPosition);
    }

    @Override
    public void onClickPlusQuantityButton() {
        listener.sendAnalyticsPlusButtonItemMultipleAddress();
    }

    @Override
    public void onClickMinQuantityButton() {
        listener.sendAnalyticsMinButtonItemMultipleAddress();
    }

    public void unsubscribeSubscription() {
        compositeSubscription.unsubscribe();
    }

    class MultipleAddressFooterViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup goToCourierPageButton;

        MultipleAddressFooterViewHolder(View itemView) {
            super(itemView);

            goToCourierPageButton = itemView.findViewById(R.id.go_to_courier_page_button);
        }
    }

    private View.OnClickListener onGoToCourierPageButtonClicked(
            final List<MultipleAddressAdapterData> listData
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGoToChooseCourier(listData);
            }
        };
    }

    public interface MultipleAddressAdapterListener {

        void onGoToChooseCourier(List<MultipleAddressAdapterData> data);

        void onItemChoosen(int itemPosition,
                           ArrayList<MultipleAddressAdapterData> dataList,
                           MultipleAddressAdapterData productData,
                           MultipleAddressItemData addressData);

        void onAddNewShipmentAddress(ArrayList<MultipleAddressAdapterData> dataList, int parentPosition);

        void onDeleteItem(int position, List<MultipleAddressItemData> multipleAddressItemDataList);

        void onChangeAddress(MultipleAddressItemAdapter adapter,
                             ArrayList<MultipleAddressAdapterData> dataList,
                             RecipientAddressModel recipientAddressModel,
                             int childPosition,
                             int parentPosition);

        void sendAnalyticsPlusButtonItemMultipleAddress();

        void sendAnalyticsMinButtonItemMultipleAddress();
    }
}
