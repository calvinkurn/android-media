package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.design.pickuppoint.PickupPointLayout;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentRecipientAddressViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;

    private static final int PRIME_ADDRESS = 2;

    private RelativeLayout rlRecipientAddressLayout;
    private TextView tvAddressStatus;
    private TextView tvAddressName;
    private TextView tvRecipientName;
    private TextView tvRecipientAddress;
    private TextView tvRecipientPhone;
    private TextView tvAddOrChangeAddress;
    private PickupPointLayout pickupPointLayout;
    private TextViewCompat tvChangeAddress;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentRecipientAddressViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        rlRecipientAddressLayout = itemView.findViewById(R.id.rl_shipment_recipient_address_layout);
        tvAddressStatus = itemView.findViewById(R.id.tv_address_status);
        tvAddressName = itemView.findViewById(R.id.tv_address_name);
        tvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
        tvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
        tvRecipientPhone = itemView.findViewById(R.id.tv_recipient_phone);
        tvAddOrChangeAddress = itemView.findViewById(R.id.tv_add_or_change_address);
        pickupPointLayout = itemView.findViewById(R.id.pickup_point_layout);
        tvChangeAddress = itemView.findViewById(R.id.tv_change_address);
    }

    public void bindViewHolder(RecipientAddressModel recipientAddress,
                               ArrayList<ShowCaseObject> showCaseObjectList) {

        tvAddressStatus.setVisibility(View.GONE);
        tvAddressName.setText(recipientAddress.getAddressName());
        tvRecipientName.setText(recipientAddress.getRecipientName());
        tvRecipientAddress.setText(getFullAddress(recipientAddress));
        tvRecipientPhone.setVisibility(View.GONE);

        tvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());

        renderPickupPoint(pickupPointLayout, recipientAddress);
        tvChangeAddress.setVisibility(View.GONE);

        setShowCase(rlRecipientAddressLayout, showCaseObjectList);
    }

    private void setShowCase(ViewGroup viewGroup, ArrayList<ShowCaseObject> showCaseObjectList) {
        showCaseObjectList.add(new ShowCaseObject(viewGroup,
                "Alamat Pengiriman",
                "Pastikan alamat pengiriman sudah sesuai dengan\nyang kamu inginkan",
                ShowCaseContentPosition.UNDEFINED)
        );
    }

    private String getFullAddress(RecipientAddressModel recipientAddress) {
        return recipientAddress.getAddressStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getAddressCityName() + ", "
                + recipientAddress.getAddressProvinceName() + ", "
                + recipientAddress.getRecipientPhoneNumber();
    }

    private void renderPickupPoint(PickupPointLayout pickupPointLayout,
                                   final RecipientAddressModel recipientAddress) {

        pickupPointLayout.setListener(pickupPointListener(recipientAddress));

        if (recipientAddress.getStore() == null) {
            pickupPointLayout.unSetData(pickupPointLayout.getContext());
            pickupPointLayout.enableChooserButton(pickupPointLayout.getContext());
        } else {
            pickupPointLayout.setData(pickupPointLayout.getContext(),
                    recipientAddress.getStore().getStoreName(), recipientAddress.getStore().getAddress());
        }

    }

    private View.OnClickListener addOrChangeAddressListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shipmentAdapterActionListener.onAddOrChangeAddress();
            }
        };
    }

    private PickupPointLayout.ViewListener pickupPointListener(
            final RecipientAddressModel recipientAddress) {

        return new PickupPointLayout.ViewListener() {
            @Override
            public void onChoosePickupPoint() {
                shipmentAdapterActionListener.onChoosePickupPoint(recipientAddress);
            }

            @Override
            public void onClearPickupPoint() {
                shipmentAdapterActionListener.onClearPickupPoint(recipientAddress);
            }

            @Override
            public void onEditPickupPoint() {
                shipmentAdapterActionListener.onEditPickupPoint(recipientAddress);
            }
        };
    }

}
