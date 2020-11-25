package com.tokopedia.manageaddress.ui.addresschoice.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.manageaddress.ui.addresschoice.RecipientAddressViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipientAddressModel> mAddressModelList;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        mActionListener = actionListener;
        mAddressModelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int addressPosition = position;
        RecipientAddressViewHolder addressHolder = (RecipientAddressViewHolder) holder;
        RecipientAddressModel address = mAddressModelList.get(addressPosition);
        addressHolder.bind(address, mActionListener, position);
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return RecipientAddressViewHolder.TYPE;
    }

    public void setAddressList(List<RecipientAddressModel> addressModelList, String selectedId) {
        for (RecipientAddressModel addressModel : addressModelList) {
            if (addressModel.getId().equals(selectedId)) {
                addressModel.setSelected(true);
            } else addressModel.setSelected(false);
        }
        mAddressModelList.clear();
        mAddressModelList.addAll(addressModelList);
        updateHeaderAndFooterPosition();
        notifyDataSetChanged();
    }

    public void updateAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.addAll(addressModelList);
        updateHeaderAndFooterPosition();
        notifyDataSetChanged();
    }


    public void updateSelected(int position) {
        for (int i = 0; i < mAddressModelList.size(); i++) {
            if (position == i) {
                mAddressModelList.get(i).setSelected(true);
            } else {
                mAddressModelList.get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    private void updateHeaderAndFooterPosition() {
        for (int i = 0; i < mAddressModelList.size(); i++) {
            mAddressModelList.get(i).setHeader(i == 0);
            mAddressModelList.get(i).setFooter(i == mAddressModelList.size() - 1);
        }
    }

    public interface ActionListener {

        void onAddressContainerClicked(RecipientAddressModel model, int position);

        void onEditClick(RecipientAddressModel model);

        void onAddAddressButtonClicked();
    }

}
