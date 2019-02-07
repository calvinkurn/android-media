package com.tokopedia.checkout.view.feature.addressoptions.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListAdapter extends RecyclerView.Adapter<RecipientAddressViewHolder> {

    private boolean adaSampai;
    private List<RecipientAddressModel> mAddressModelList;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        mActionListener = actionListener;
        mAddressModelList = new ArrayList<>();
        adaSampai = false;
    }

    @NonNull
    @Override
    public RecipientAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipient_address_rb_selectable, parent, false);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipientAddressViewHolder holder, int position) {
        RecipientAddressModel address = mAddressModelList.get(position);
        holder.bind(address, this, mActionListener, position);
        if (position == getItemCount() - 1)
            holder.setState(RecipientAddressViewHolder.VIEW_TYPE.BUTTON_ON);
        if (position == 0) holder.setState(RecipientAddressViewHolder.VIEW_TYPE.HEADER_ON);
    }

    @Override
    public int getItemCount() {
        int hasHeader = (adaSampai) ? 0 : 1;
        return mAddressModelList.size() + hasHeader;
    }

    @Override
    public int getItemViewType(int position) {
        int hasHeader = (adaSampai) ? 0 : 1;
        if (position == 0 && adaSampai) return R.layout.item_sampai;
        else return RecipientAddressViewHolder.TYPE;
    }

    public void setAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.clear();
        mAddressModelList.addAll(addressModelList);
    }

    public void updateAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.addAll(addressModelList);
    }

    void setSelectedAddressData(int position) {
        for (int i = 0; i < mAddressModelList.size(); i++) {
            if (position == i) {
                mAddressModelList.get(i).setSelected(true);
            } else {
                mAddressModelList.get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
        mActionListener.onAddressContainerClicked(mAddressModelList.get(position));
    }

    /**
     * Implemented by adapter host fragment
     */
    public interface ActionListener {
        /**
         * Executed when address container is clicked
         */
        void onAddressContainerClicked(RecipientAddressModel model);

        /**
         * Executed when edit address button is clicked
         */
        void onEditClick(RecipientAddressModel model);
    }

}
