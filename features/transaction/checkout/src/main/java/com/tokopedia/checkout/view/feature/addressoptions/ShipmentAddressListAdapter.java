package com.tokopedia.checkout.view.feature.addressoptions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListAdapter
        extends RecyclerView.Adapter<ShipmentAddressListAdapter.RecipientAddressViewHolder> {

    private static final int PRIME_ADDRESS = 2;

    private List<RecipientAddressModel> mAddressModelList;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        mActionListener = actionListener;
        mAddressModelList = new ArrayList<>();
    }

    public void setAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.clear();
        mAddressModelList.addAll(addressModelList);
    }

    public void updateAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList.addAll(addressModelList);
    }

    @Override
    public RecipientAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipient_address_rb_selectable, parent, false);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipientAddressViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.vSeparator.setVisibility(View.GONE);
        } else {
            holder.vSeparator.setVisibility(View.VISIBLE);
        }

        RecipientAddressModel address = mAddressModelList.get(position);

        holder.mTvAddressName.setText(address.getAddressName());
        holder.mTvAddressStatus.setVisibility(address.getAddressStatus() == PRIME_ADDRESS ?
                View.VISIBLE : View.GONE);
        holder.mTvRecipientName.setText(address.getRecipientName());
        holder.mTvRecipientAddress.setText(getFullAddress(address));
        holder.mTvRecipientPhone.setText(address.getRecipientPhoneNumber());

        holder.mRbCheckAddress.setChecked(address.isSelected());
        holder.mTvChangeAddress.setVisibility(View.VISIBLE);
        holder.mTvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.onEditClick(mAddressModelList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(getItemClickListener(address, position));
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size();
    }

    private String getFullAddress(RecipientAddressModel recipientAddress) {
        return recipientAddress.getStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getCityName() + ", "
                + recipientAddress.getProvinceName();
    }

    private View.OnClickListener getItemClickListener(final RecipientAddressModel recipientAddressModel,
                                                      final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recipientAddressModel.isSelected()) {
                    for (RecipientAddressModel viewModel : mAddressModelList) {
                        if (viewModel.getId().equals(recipientAddressModel.getId())) {
                            if (mAddressModelList.size() > position && position >= 0) {
                                viewModel.setSelected(!viewModel.isSelected());
                                mActionListener.onAddressContainerClicked(mAddressModelList.get(position));
                            }
                        } else {
                            viewModel.setSelected(false);
                        }
                    }

                    notifyDataSetChanged();
                } else {
                    mActionListener.onAddressContainerClicked(mAddressModelList.get(position));
                }
            }
        };
    }

    class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

        TextView mTvAddressName;
        TextView mTvAddressStatus;
        TextView mTvRecipientName;
        TextView mTvRecipientAddress;
        TextView mTvRecipientPhone;

        TextViewCompat mTvChangeAddress;
        RadioButton mRbCheckAddress;

        RelativeLayout mRlAddressContainer;
        LinearLayout mLlRadioButtonContainer;
        View vSeparator;

        RecipientAddressViewHolder(View view) {
            super(view);

            mTvAddressName = view.findViewById(R.id.tv_address_name);
            mTvAddressStatus = view.findViewById(R.id.tv_address_status);
            mTvRecipientName = view.findViewById(R.id.tv_recipient_name);
            mTvRecipientAddress = view.findViewById(R.id.tv_recipient_address);
            mTvRecipientPhone = view.findViewById(R.id.tv_recipient_phone);

            mTvChangeAddress = view.findViewById(R.id.tv_change_address);
            mRbCheckAddress = view.findViewById(R.id.rb_check_address);

            mRlAddressContainer = view.findViewById(R.id.rl_address_container);
            mLlRadioButtonContainer = view.findViewById(R.id.ll_radio_button_container);
            vSeparator = view.findViewById(R.id.v_separator);
        }

    }

    /**
     * Implemented by adapter host fragment
     */
    public interface ActionListener {
        /**
         * Executed when address container is clicked
         *
         * @param model RecipientAddressModel
         */
        void onAddressContainerClicked(RecipientAddressModel model);

        /**
         * Executed when edit address button is clicked
         *
         * @param model RecipientAddressModel
         */
        void onEditClick(RecipientAddressModel model);
    }

}
