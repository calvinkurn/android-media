package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.design.pickuppoint.PickupPointLayout;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentRecipientAddressViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";

    // private CardView cardAddress;
    private RelativeLayout rlRecipientAddressLayout;
    private TextView tvAddressStatus;
    private TextView tvAddressName;
    private TextView tvRecipientName;
    private TextView tvRecipientAddress;
    private TextView tvRecipientPhone;
    private TextView tvRecipientChangeAddress;
    private TextView tvSendToMultipleAddress;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentRecipientAddressViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        // cardAddress = itemView.findViewById(R.id.card_address);
        rlRecipientAddressLayout = itemView.findViewById(R.id.rl_shipment_recipient_address_layout);
        tvAddressStatus = itemView.findViewById(R.id.tv_address_status);
        tvAddressName = itemView.findViewById(R.id.tv_address_name);
        tvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
        tvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
        tvRecipientPhone = itemView.findViewById(R.id.tv_recipient_phone);
        tvRecipientChangeAddress = itemView.findViewById(R.id.tv_change_recipient_address);
        tvSendToMultipleAddress = itemView.findViewById(R.id.tv_send_to_multiple_address);
    }

    public void bindViewHolder(RecipientAddressModel recipientAddress,
                               ArrayList<ShowCaseObject> showCaseObjectList,
                               String cartIds) {
        if (recipientAddress.isDisableMultipleAddress()) {
            tvSendToMultipleAddress.setVisibility(View.GONE);
        } else {
            tvSendToMultipleAddress.setVisibility(View.VISIBLE);
        }

        tvAddressStatus.setVisibility(View.GONE);
        if (recipientAddress.getAddressStatus() == 2) {
            tvAddressStatus.setVisibility(View.VISIBLE);
        } else {
            tvAddressStatus.setVisibility(View.GONE);
        }
        // tvAddressName.setVisibility(View.GONE);
        tvAddressName.setText(recipientAddress.getAddressName());
        tvRecipientName.setText(recipientAddress.getRecipientName());
        // formatAddressName(tvRecipientName, recipientAddress.getRecipientName(), recipientAddress.getAddressName());
        tvRecipientAddress.setText(getFullAddress(recipientAddress));
        tvRecipientPhone.setVisibility(View.GONE);

        tvRecipientChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipmentAdapterActionListener.onChangeAddress();
            }
        });

        tvSendToMultipleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipmentAdapterActionListener.onSendToMultipleAddress(recipientAddress, cartIds);
            }
        });

        setShowCase(rlRecipientAddressLayout, showCaseObjectList);
    }

    /*private void setMargin(int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) cardAddress.getLayoutParams();
        int sideMargin = (int) cardAddress.getContext().getResources().getDimension(R.dimen.dp_16);
        layoutParams.setMargins(sideMargin, topMargin, sideMargin, 0);
        cardAddress.requestLayout();
    }*/

    private void setShowCase(ViewGroup viewGroup, ArrayList<ShowCaseObject> showCaseObjectList) {
        showCaseObjectList.add(new ShowCaseObject(viewGroup,
                viewGroup.getContext().getString(R.string.label_showcase_address_title),
                viewGroup.getContext().getString(R.string.label_showcase_address_message),
                ShowCaseContentPosition.UNDEFINED)
        );
    }

    private String getFullAddress(RecipientAddressModel recipientAddress) {
        return recipientAddress.getStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getCityName() + ", "
                + recipientAddress.getProvinceName() + ", "
                + recipientAddress.getRecipientPhoneNumber();
    }

    private void formatAddressName(TextView textView, String recipientName, String addressName) {
        // addressName = " (" + addressName + ")";
        recipientName += addressName;
        int startSpan = recipientName.indexOf(addressName);
        int endSpan = recipientName.indexOf(addressName) + addressName.length();
        Spannable formattedPromoMessage = new SpannableString(recipientName);
        final int color = ContextCompat.getColor(textView.getContext(), R.color.black_38);
        formattedPromoMessage.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
        textView.setText(formattedPromoMessage);
    }


}
