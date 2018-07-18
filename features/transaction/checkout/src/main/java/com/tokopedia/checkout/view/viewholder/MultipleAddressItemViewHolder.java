package com.tokopedia.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.design.component.TextViewCompat;

import java.util.List;

/**
 * Created by kris on 3/14/18. Tokopedia
 */

public class MultipleAddressItemViewHolder extends RecyclerView.ViewHolder {

    private static final int SINGLE_DATA_SIZE = 1;

    private TextView shippingIndex;
    private TextViewCompat pseudoEditButton;
    private ImageView deleteButton;
    private ViewGroup addressLayout;
    private TextView addressTitle;
    private TextView addressReceiverName;
    private TextView address;
    private View borderLine;
    private TextView phoneNumber;

    public MultipleAddressItemViewHolder(View itemView) {
        super(itemView);

        shippingIndex = itemView.findViewById(R.id.shipping_index);
        pseudoEditButton = itemView.findViewById(R.id.tv_change_address);
        deleteButton = itemView.findViewById(R.id.delete_button);
        addressLayout = itemView.findViewById(R.id.address_layout);
        addressTitle = itemView.findViewById(R.id.tv_address_name);
        addressReceiverName = itemView.findViewById(R.id.tv_recipient_name);
        address = itemView.findViewById(R.id.tv_recipient_address);
        borderLine = itemView.findViewById(R.id.border_line);
        phoneNumber = itemView.findViewById(R.id.tv_recipient_phone);
        phoneNumber.setVisibility(View.GONE);

    }

    public void bindItemAdapterAddress(MultipleAddressItemData itemData,
                                       List<MultipleAddressItemData> itemDataList,
                                       View.OnClickListener onDeleteClicekdListener,
                                       int position) {

        shippingIndex.setText(
                shippingIndex.getText().toString().replace(
                        "#", String.valueOf(itemData.getAddressPosition() + 1)
                )
        );
        addressTitle.setText(itemData.getAddressTitle());
        addressReceiverName.setText(itemData.getAddressReceiverName());
        address.setText(itemData.getAddressStreet()
                + ", " + itemData.getAddressCityName()
                + ", " + itemData.getAddressProvinceName()
                + ", " + itemData.getRecipientPhoneNumber());
        pseudoEditButton.setVisibility(View.GONE);
        deleteButton.setOnClickListener(onDeleteClicekdListener);
        addressLayout.setOnClickListener(
                onAddressLayoutClickedListener(itemData)
        );
        if (itemDataList.size() == SINGLE_DATA_SIZE) deleteButton.setVisibility(View.GONE);
        else deleteButton.setVisibility(View.VISIBLE);
        if (position == itemDataList.size() - 1) borderLine.setVisibility(View.GONE);
        else borderLine.setVisibility(View.VISIBLE);

    }

    private View.OnClickListener onAddressLayoutClickedListener(MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
}
