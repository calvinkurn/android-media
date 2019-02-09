package com.tokopedia.checkout.view.feature.addressoptions.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CornerAddressModel mSampaiModel;
    private List<RecipientAddressModel> mAddressModelList;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        mActionListener = actionListener;
        mAddressModelList = new ArrayList<>();
        mSampaiModel = null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == R.layout.item_sampai) return new SampaiViewHolder(view);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == R.layout.item_sampai) {
            SampaiViewHolder sampaiViewHolder = (SampaiViewHolder) holder;
            sampaiViewHolder.bind(mSampaiModel);
        } else {
            int addressPosition = position - getExtraCount();
            RecipientAddressViewHolder addressHolder = (RecipientAddressViewHolder) holder;
            RecipientAddressModel address = mAddressModelList.get(addressPosition);
            addressHolder.bind(address, this, mActionListener, addressPosition);
            if (position == getItemCount() - 1) addressHolder.setState(RecipientAddressViewHolder.VIEW_TYPE.BUTTON_ON);
            if (position == 0) addressHolder.setState(RecipientAddressViewHolder.VIEW_TYPE.HEADER_ON);
        }
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size() + getExtraCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mSampaiModel != null) return R.layout.item_sampai;
        else return RecipientAddressViewHolder.TYPE;
    }

    public void setAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.clear();
        mAddressModelList.addAll(addressModelList);
    }

    public void updateAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.addAll(addressModelList);
        notifyDataSetChanged();
    }

    public void showSampaiWithoutSelected() {
        mSampaiModel = new CornerAddressModel();
        notifyDataSetChanged();
    }

    public void setSampai(CornerAddressModel cornerAddressModel) {
        mSampaiModel = cornerAddressModel;
        notifyDataSetChanged();
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

    private int getExtraCount() {
        return mSampaiModel != null ? 1 : 0;
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

        void onAddAddressButtonClicked();
    }

    class SampaiViewHolder extends RecyclerView.ViewHolder {

        Button mButton;
        View mCornerView;
        TextView mCornerName, mBranchName;
        RadioButton mRadio;

        public SampaiViewHolder(View itemView) {
            super(itemView);
            mCornerView = itemView.findViewById(R.id.view_tkpd_corner);
            mButton = itemView.findViewById(R.id.button_tkpd_corner);
            mCornerName = itemView.findViewById(R.id.text_view_corner);
            mBranchName = itemView.findViewById(R.id.text_view_branch);
            mRadio = itemView.findViewById(R.id.radio_button_corner);
        }

        public void bind(CornerAddressModel model) {
            if (!TextUtils.isEmpty(model.getCornerName())) {
                mCornerView.setVisibility(View.VISIBLE);
                mCornerName.setText(model.getCornerName());
                mBranchName.setText(model.getCornerBranchName());
                mRadio.setSelected(model.isSelected());
                mButton.setText("Ubah Lokasi Tokopedia Corner");
            } else {
                mCornerView.setVisibility(View.GONE);
                mButton.setText("Pilih Lokasi Tokopedia Corner");
            }
        }
    }

}
