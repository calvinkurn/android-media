package com.tokopedia.manageaddress.ui.addresschoice;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.manageaddress.R;
import com.tokopedia.manageaddress.ui.addresschoice.recyclerview.ShipmentAddressListAdapter;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by fajarnuha on 07/02/19.
 */
public class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE = R.layout.item_recipient_address_rb_selectable;

    private static final int PRIME_ADDRESS = 2;

    private TextView mTvAddressName;
    private Label mTvAddressStatus;
    private Typography mTvRecipientName;
    private Typography mTvRecipientAddress;
    private Typography mTvRecipientPhone;
    private View mViewAddress;

    private TextView mTvChangeAddress;
    private RadioButton mRbCheckAddress;
    private TextView mButtonAddAddress;

    private ShipmentAddressListAdapter.ActionListener mListener;

    public RecipientAddressViewHolder(View view) {
        super(view);
        mViewAddress = view.findViewById(R.id.rl_shipment_recipient_address_layout);
        mTvAddressName = view.findViewById(com.tokopedia.logisticCommon.R.id.tv_address_name);
        mTvAddressStatus = view.findViewById(com.tokopedia.logisticCommon.R.id.tv_address_status);
        mTvRecipientName = view.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_name);
        mTvRecipientAddress = view.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_address);
        mTvRecipientPhone = view.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_phone);

        mTvChangeAddress = view.findViewById(R.id.button_change_address);
        mRbCheckAddress = view.findViewById(R.id.rb_check_address);
        mButtonAddAddress = view.findViewById(R.id.button_add_new_address);
    }

    public void bind(RecipientAddressModel address, ShipmentAddressListAdapter.ActionListener listener, int position) {
        mListener = listener;
        mTvAddressName.setText(Utils.getHtmlFormat(address.getAddressName()));
        mTvAddressStatus.setVisibility(address.getAddressStatus() == PRIME_ADDRESS ?
                View.VISIBLE : View.GONE);
        mTvRecipientName.setText(Utils.getHtmlFormat(address.getRecipientName()));
        mTvRecipientAddress.setText(Utils.getHtmlFormat(getFullAddress(address)));
        mTvRecipientPhone.setText(address.getRecipientPhoneNumber());
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
