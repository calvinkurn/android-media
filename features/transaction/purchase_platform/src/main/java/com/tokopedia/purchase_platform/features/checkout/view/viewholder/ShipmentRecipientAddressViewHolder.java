package com.tokopedia.purchase_platform.features.checkout.view.viewholder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifycomponents.TabsUnify;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentRecipientAddressViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;

    private RelativeLayout rlRecipientAddressLayout;
    private Label tvAddressStatus;
    private Typography tvAddressName;
    private Typography tvRecipientName;
    private Typography tvRecipientAddress;
    private Typography tvRecipientPhone;
    private TextView tvRecipientChangeAddress;
    private TextView tvSendToMultipleAddress;
    private LinearLayout llTradeInInfo;
    private TextView tvTradeInInfo;
    private TabsUnify tabUnifyTradeInAddress;
    private RelativeLayout layoutAddressNormal;
    private ConstraintLayout layoutAddressDropOff;
    private LinearLayout llAddOrChangeAddressContainer;
    private Typography tvShipmentAddress;
    private View separator;
    private Typography tvDropOffAddressTitle;
    private Typography tvDropOffAddressDescription;
    private Typography tvChangeDropOff;
    private ImageView imgPinpoint;
    private Typography tvDisabledMultipleAddressInfo;
    private Typography tvChangeAddressTop;
    private View separatorBottom;

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
        tvRecipientChangeAddress = itemView.findViewById(R.id.tv_change_recipient_address);
        tvSendToMultipleAddress = itemView.findViewById(R.id.tv_send_to_multiple_address);
        llTradeInInfo = itemView.findViewById(R.id.ll_trade_in_info);
        tvTradeInInfo = itemView.findViewById(R.id.tv_trade_in_info);
        tabUnifyTradeInAddress = itemView.findViewById(R.id.tab_unify_trade_in_address);
        layoutAddressNormal = itemView.findViewById(R.id.layout_address_normal);
        layoutAddressDropOff = itemView.findViewById(R.id.layout_address_drop_off);
        llAddOrChangeAddressContainer = itemView.findViewById(R.id.ll_add_or_change_address_container);
        tvShipmentAddress = itemView.findViewById(R.id.tv_shipment_address);
        separator = itemView.findViewById(R.id.separator);
        tvDropOffAddressTitle = itemView.findViewById(R.id.tv_drop_off_address_title);
        tvDropOffAddressDescription = itemView.findViewById(R.id.tv_drop_off_address_description);
        tvChangeDropOff = itemView.findViewById(R.id.tv_change_drop_off);
        imgPinpoint = itemView.findViewById(R.id.img_pinpoint);
        tvDisabledMultipleAddressInfo = itemView.findViewById(R.id.tv_disabled_multiple_address_info);
        tvChangeAddressTop = itemView.findViewById(R.id.tv_change_address_top);
        separatorBottom = itemView.findViewById(R.id.separator_bottom);
    }

    public void bindViewHolder(RecipientAddressModel recipientAddress,
                               ArrayList<ShowCaseObject> showCaseObjectList,
                               String cartIds) {
        renderBaseAddress(recipientAddress, cartIds);
        if (recipientAddress.isTradeIn()) {
            renderAddressOptionOnBottom();
            renderTradeInAddress(recipientAddress);
        } else {
            renderAddressOptionOnTop(recipientAddress);
            renderNormalAddress(recipientAddress);
            llTradeInInfo.setVisibility(View.GONE);
        }

        setShowCase(rlRecipientAddressLayout, showCaseObjectList);
    }

    private void renderBaseAddress(RecipientAddressModel recipientAddress, String cartIds) {
        if (recipientAddress.isDisableMultipleAddress()) {
            tvSendToMultipleAddress.setVisibility(View.GONE);
        } else {
            tvSendToMultipleAddress.setVisibility(View.VISIBLE);
        }
        tvRecipientChangeAddress.setOnClickListener(v -> shipmentAdapterActionListener.onChangeAddress());
        tvSendToMultipleAddress.setOnClickListener(v -> shipmentAdapterActionListener.onSendToMultipleAddress(recipientAddress, cartIds));
        tvChangeAddressTop.setOnClickListener(v -> shipmentAdapterActionListener.onChangeAddress());
    }

    private void setShowCase(ViewGroup viewGroup, ArrayList<ShowCaseObject> showCaseObjectList) {
        showCaseObjectList.add(new ShowCaseObject(viewGroup,
                viewGroup.getContext().getString(R.string.label_showcase_address_title),
                viewGroup.getContext().getString(R.string.label_showcase_address_message),
                ShowCaseContentPosition.UNDEFINED)
        );
    }

    private void renderTradeInAddress(RecipientAddressModel recipientAddress) {
        llTradeInInfo.setVisibility(View.VISIBLE);
        if (recipientAddress.isTradeInDropOffEnable()) {
            renderTradeInAddressWithTabs(recipientAddress);
        } else {
            renderTradeInAddressWithoutTabs(recipientAddress);
            formatTradeInDeliveryInfo(recipientAddress);
        }
    }

    private void renderTradeInAddressWithoutTabs(RecipientAddressModel recipientAddressModel) {
        renderNormalAddress(recipientAddressModel);
        tvShipmentAddress.setText(R.string.label_showcase_address_title);
        separator.setVisibility(View.VISIBLE);
        tabUnifyTradeInAddress.setVisibility(View.GONE);
        layoutAddressDropOff.setVisibility(View.GONE);
        layoutAddressNormal.setVisibility(View.VISIBLE);
    }

    private void renderTradeInAddressWithTabs(RecipientAddressModel recipientAddress) {
        tvShipmentAddress.setText(R.string.lebal_trade_in_address_mode);
        separator.setVisibility(View.GONE);
        llAddOrChangeAddressContainer.setVisibility(View.GONE);
        tabUnifyTradeInAddress.setVisibility(View.VISIBLE);
        if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() == 0) {
            tabUnifyTradeInAddress.addNewTab(tabUnifyTradeInAddress.getContext().getString(R.string.label_tab_trade_in_address_deliver));
            tabUnifyTradeInAddress.addNewTab(tabUnifyTradeInAddress.getContext().getString(R.string.label_tab_trade_in_address_pickup));
        }

        if (recipientAddress.getSelectedTabIndex() == 0) {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() > 0) {
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(0).select();
                renderTradeInDeliveryTab(recipientAddress);
            }
        } else {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() > 1) {
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(1).select();
                renderTradeInPickUpTab(recipientAddress);
            }
        }

        tabUnifyTradeInAddress.getUnifyTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    recipientAddress.setSelectedTabIndex(tab.getPosition());
                    shipmentAdapterActionListener.onNeedUpdateViewItem(position);
                    int shipmentItemTradeInPosition = position + 1;
                    shipmentAdapterActionListener.onTradeInAddressTabChanged(shipmentItemTradeInPosition);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tvChangeDropOff.setOnClickListener(view -> shipmentAdapterActionListener.onChangeTradeInDropOffClicked());
    }

    private void renderTradeInPickUpTab(RecipientAddressModel recipientAddress) {
        layoutAddressNormal.setVisibility(View.GONE);
        layoutAddressDropOff.setVisibility(View.VISIBLE);
        llAddOrChangeAddressContainer.setVisibility(View.GONE);
        if (TextUtils.isEmpty(recipientAddress.getDropOffAddressName())) {
            tvDropOffAddressTitle.setVisibility(View.GONE);
            tvDropOffAddressDescription.setVisibility(View.GONE);
            imgPinpoint.setVisibility(View.GONE);
            tvChangeDropOff.setBackground(ContextCompat.getDrawable(tvChangeDropOff.getContext(), R.drawable.bg_green_button));
            tvChangeDropOff.setTextColor(ContextCompat.getColor(tvChangeDropOff.getContext(), R.color.white));
        } else {
            tvDropOffAddressTitle.setVisibility(View.VISIBLE);
            tvDropOffAddressDescription.setVisibility(View.VISIBLE);
            tvDropOffAddressTitle.setText(recipientAddress.getDropOffAddressName());
            tvDropOffAddressDescription.setText(recipientAddress.getDropOffAddressDetail());
            imgPinpoint.setVisibility(View.VISIBLE);
            tvChangeDropOff.setBackground(ContextCompat.getDrawable(tvChangeDropOff.getContext(), R.drawable.bg_green_round_address));
            tvChangeDropOff.setTextColor(ContextCompat.getColor(tvChangeDropOff.getContext(), R.color.address_green));
        }
        tvTradeInInfo.setText(tvTradeInInfo.getContext().getString(R.string.message_trade_in_checkout_pickup_info));
    }

    private void renderTradeInDeliveryTab(RecipientAddressModel recipientAddress) {
        formatTradeInDeliveryInfo(recipientAddress);
        layoutAddressDropOff.setVisibility(View.GONE);
        layoutAddressNormal.setVisibility(View.VISIBLE);
        llAddOrChangeAddressContainer.setVisibility(View.VISIBLE);
        renderBasicAddress(recipientAddress);
    }

    private void renderNormalAddress(RecipientAddressModel recipientAddress) {
        tabUnifyTradeInAddress.setVisibility(View.GONE);
        layoutAddressDropOff.setVisibility(View.GONE);
        layoutAddressNormal.setVisibility(View.VISIBLE);
        renderBasicAddress(recipientAddress);
    }

    private void renderBasicAddress(RecipientAddressModel recipientAddress) {
        tvAddressStatus.setVisibility(View.GONE);
        if (recipientAddress.getAddressStatus() == 2) {
            tvAddressStatus.setVisibility(View.VISIBLE);
        } else {
            tvAddressStatus.setVisibility(View.GONE);
        }
        tvAddressName.setText(Utils.getHtmlFormat(recipientAddress.getAddressName()));
        tvRecipientName.setText(String.format(tvRecipientName.getContext().getString(R.string.recipient_name_format),
                Utils.getHtmlFormat(recipientAddress.getRecipientName()), recipientAddress.getRecipientPhoneNumber()));
        tvRecipientAddress.setText(Utils.getHtmlFormat(getFullAddress(recipientAddress)));
        tvRecipientPhone.setVisibility(View.GONE);
    }

    private String getFullAddress(RecipientAddressModel recipientAddress) {
        return recipientAddress.getStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getCityName() + ", "
                + recipientAddress.getProvinceName() + ", "
                + recipientAddress.getRecipientPhoneNumber();
    }

    private void formatTradeInDeliveryInfo(RecipientAddressModel recipientAddressModel) {
        tvTradeInInfo.setText(String.format(tvTradeInInfo.getContext().getString(R.string.message_trade_in_checkout_delivery_info), recipientAddressModel.getRecipientPhoneNumber()));
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

    private void renderAddressOptionOnTop(RecipientAddressModel recipientAddressModel) {
        llAddOrChangeAddressContainer.setVisibility(View.GONE);
        tvChangeAddressTop.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(recipientAddressModel.getDisabledMultiAddressMessage())) {
            tvDisabledMultipleAddressInfo.setText(recipientAddressModel.getDisabledMultiAddressMessage());
            tvDisabledMultipleAddressInfo.setVisibility(View.VISIBLE);
            separatorBottom.setVisibility(View.VISIBLE);
        } else {
            tvDisabledMultipleAddressInfo.setVisibility(View.GONE);
            separatorBottom.setVisibility(View.GONE);
        }
    }

    private void renderAddressOptionOnBottom() {
        llAddOrChangeAddressContainer.setVisibility(View.VISIBLE);
        tvChangeAddressTop.setVisibility(View.GONE);
        tvDisabledMultipleAddressInfo.setVisibility(View.GONE);
        separatorBottom.setVisibility(View.GONE);
    }

}
