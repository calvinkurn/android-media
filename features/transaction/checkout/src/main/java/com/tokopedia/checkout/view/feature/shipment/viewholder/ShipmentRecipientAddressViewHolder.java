package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentRecipientAddressViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;

    private CardView cardAddress;
    private RelativeLayout rlRecipientAddressLayout;
    private TextView tvAddressStatus;
    private TextView tvAddressName;
    private TextView tvRecipientName;
    private TextView tvRecipientAddress;
    private TextView tvRecipientPhone;
    private TextView tvRecipientChangeAddress;
    private TextView tvSendToMultipleAddress;
    private LinearLayout llTradeIn;
    private TextView tvTradeInInfo;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentRecipientAddressViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        cardAddress = itemView.findViewById(R.id.card_address);
        rlRecipientAddressLayout = itemView.findViewById(R.id.rl_shipment_recipient_address_layout);
        tvAddressStatus = itemView.findViewById(R.id.tv_address_status);
        tvAddressName = itemView.findViewById(R.id.tv_address_name);
        tvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
        tvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
        tvRecipientPhone = itemView.findViewById(R.id.tv_recipient_phone);
        tvRecipientChangeAddress = itemView.findViewById(R.id.tv_change_recipient_address);
        tvSendToMultipleAddress = itemView.findViewById(R.id.tv_send_to_multiple_address);
        llTradeIn = itemView.findViewById(R.id.ll_trade_in);
        tvTradeInInfo = itemView.findViewById(R.id.tv_trade_in_info);
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
        tvAddressName.setText(recipientAddress.getAddressName());
        tvRecipientName.setText(recipientAddress.getRecipientName());
        tvRecipientAddress.setText(getFullAddress(recipientAddress));
        tvRecipientPhone.setVisibility(View.GONE);

        tvRecipientChangeAddress.setOnClickListener(v -> shipmentAdapterActionListener.onChangeAddress());
        tvSendToMultipleAddress.setOnClickListener(v -> shipmentAdapterActionListener.onSendToMultipleAddress(recipientAddress, cartIds));

        if (recipientAddress.isTradeIn()) {
            formatTradeInInfo(recipientAddress);
            llTradeIn.setVisibility(View.VISIBLE);
        } else {
            llTradeIn.setVisibility(View.GONE);
        }

        setShowCase(rlRecipientAddressLayout, showCaseObjectList);
    }

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

    private void formatTradeInInfo(RecipientAddressModel recipientAddressModel) {
        tvTradeInInfo.setText(String.format(tvTradeInInfo.getContext().getString(R.string.checkout_shipment_label_tradein), recipientAddressModel.getRecipientPhoneNumber()));
        String clickableText = "Ganti nomor";
        int startSpan = tvTradeInInfo.getText().toString().indexOf(clickableText);
        int endSpan = tvTradeInInfo.getText().toString().indexOf(clickableText) + clickableText.length();
        Spannable formattedTrandeInInfoText = new SpannableString(tvTradeInInfo.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                shipmentAdapterActionListener.onClickChangePhoneNumber(recipientAddressModel);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
                textPaint.setColor(ContextCompat.getColor(tvTradeInInfo.getContext(), R.color.tkpd_green_header));
            }
        };
        formattedTrandeInInfoText.setSpan(clickableSpan, startSpan, endSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTradeInInfo.setMovementMethod(LinkMovementMethod.getInstance());
        tvTradeInInfo.setText(formattedTrandeInInfoText);
    }

}
