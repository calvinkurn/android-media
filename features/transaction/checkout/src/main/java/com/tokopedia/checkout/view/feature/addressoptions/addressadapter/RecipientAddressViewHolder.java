package com.tokopedia.checkout.view.feature.addressoptions.addressadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by fajarnuha on 07/02/19.
 */
public class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE = R.layout.item_recipient_address_rb_selectable;

    private static final int PRIME_ADDRESS = 2;

    private TextView mTvAddressName;
    private TextView mTvAddressStatus;
    private TextView mTvRecipientName;
    private TextView mTvRecipientAddress;
    private TextView mTvRecipientPhone;
    private View mViewAddress;

    private TextView mTvChangeAddress;
    private RadioButton mRbCheckAddress;
    private TextView mButtonAddAddress;
    private TextView mHeaderText;

    private ShipmentAddressListAdapter.ActionListener mListener;

    public RecipientAddressViewHolder(View view) {
        super(view);
        mViewAddress = view.findViewById(R.id.rl_shipment_recipient_address_layout);
        mTvAddressName = view.findViewById(R.id.tv_address_name);
        mTvAddressStatus = view.findViewById(R.id.tv_address_status);
        mTvRecipientName = view.findViewById(R.id.tv_recipient_name);
        mTvRecipientAddress = view.findViewById(R.id.tv_recipient_address);
        mTvRecipientPhone = view.findViewById(R.id.tv_recipient_phone);

        mTvChangeAddress = view.findViewById(R.id.button_change_address);
        mRbCheckAddress = view.findViewById(R.id.rb_check_address);
        mButtonAddAddress = view.findViewById(R.id.button_add_new_address);
        mHeaderText = view.findViewById(R.id.text_view_address_header);
    }

    public void bind(RecipientAddressModel address, ShipmentAddressListAdapter.ActionListener listener, int position) {
        mListener = listener;
        mTvAddressName.setText(address.getAddressName());
        mTvAddressStatus.setVisibility(address.getAddressStatus() == PRIME_ADDRESS ?
                View.VISIBLE : View.GONE);
        mTvRecipientName.setText(address.getRecipientName());
        mTvRecipientAddress.setText(getFullAddress(address));
        mTvRecipientPhone.setText(address.getRecipientPhoneNumber());
        mHeaderText.setVisibility(address.isHeader() ? View.VISIBLE : View.GONE);
        mButtonAddAddress.setVisibility(address.isFooter() ? View.VISIBLE : View.GONE);

        mRbCheckAddress.setChecked(address.isSelected());
        mTvChangeAddress.setVisibility(View.VISIBLE);
        mTvChangeAddress.setOnClickListener(v ->
                mListener.onEditClick(address)
        );
        mViewAddress.setOnClickListener(view -> mListener.onAddressContainerClicked(address, position));
        mButtonAddAddress.setOnClickListener(view -> mListener.onAddAddressButtonClicked());
    }

    private String getFullAddress(RecipientAddressModel recipientAddress) {
        return recipientAddress.getStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getCityName() + ", "
                + recipientAddress.getProvinceName();
    }

}
