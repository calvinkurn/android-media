package com.tokopedia.checkout.view.feature.addressoptions.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CornerAddressModel mCornerData;
    private List<RecipientAddressModel> mAddressModelList;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        mActionListener = actionListener;
        mAddressModelList = new ArrayList<>();
        mCornerData = new CornerAddressModel();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == SampaiViewHolder.getTYPE()) return new SampaiViewHolder(view);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SampaiViewHolder.getTYPE()) {
            SampaiViewHolder sampaiViewHolder = (SampaiViewHolder) holder;
            sampaiViewHolder.bind(mCornerData, mActionListener, position);
        } else {
            int addressPosition = position - getExtraCount();
            RecipientAddressViewHolder addressHolder = (RecipientAddressViewHolder) holder;
            RecipientAddressModel address = mAddressModelList.get(addressPosition);
            addressHolder.bind(address, mActionListener, position);
        }
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size() + getExtraCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mCornerData != null) return SampaiViewHolder.getTYPE();
        else return RecipientAddressViewHolder.TYPE;
    }

    public void setAddressList(List<RecipientAddressModel> addressModelList, String selectedId) {
        for (RecipientAddressModel addressModel : addressModelList) {
            if (addressModel.getId().equals(selectedId)) {
                addressModel.setSelected(true);
            } else addressModel.setSelected(false);
        }
        if (mCornerData != null && mCornerData.getCornerModel() != null) {
            mCornerData.setSelected(mCornerData.getCornerModel().getId().equals(selectedId));
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

    public void hideCornerOption() {
        mCornerData = null;
        notifyDataSetChanged();
    }

    public void setCorner(RecipientAddressModel cornerAddressModel) {
        mCornerData.setCornerModel(cornerAddressModel);
        notifyDataSetChanged();
    }

    public void updateSelected(int position) {
        if (getItemViewType(position) == SampaiViewHolder.getTYPE()) {
            mCornerData.setSelected(true);
            for (RecipientAddressModel addressModel : mAddressModelList) {
                addressModel.setSelected(false);
            }
        } else {
            for (int i = 0; i < mAddressModelList.size(); i++) {
                if (position - getExtraCount() == i) {
                    mAddressModelList.get(i).setSelected(true);
                } else {
                    mAddressModelList.get(i).setSelected(false);
                }
            }
            if (mCornerData != null) mCornerData.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public Boolean isHavingCornerAddress() {
        return (mCornerData != null && mCornerData.getCornerModel() != null);
    }

    private int getExtraCount() {
        return mCornerData != null ? 1 : 0;
    }

    private void updateHeaderAndFooterPosition() {
        for (int i = 0; i < mAddressModelList.size(); i++) {
            mAddressModelList.get(i).setHeader(i == 0);
            mAddressModelList.get(i).setFooter(i == mAddressModelList.size() - 1);
        }
    }

    public interface ActionListener {

        void onAddressContainerClicked(RecipientAddressModel model, int position);

        void onCornerAddressClicked(RecipientAddressModel addressModel, int position);

        void onEditClick(RecipientAddressModel model);

        void onAddAddressButtonClicked();

        void onCornerButtonClicked();
    }

}
