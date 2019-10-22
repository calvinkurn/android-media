package com.tokopedia.purchase_platform.features.checkout.view.viewholder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.unifycomponents.TabsUnify;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentRecipientAddressViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;

    private RelativeLayout rlRecipientAddressLayout;
    private Typography tvAddressStatus;
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
    }

    public void bindViewHolder(RecipientAddressModel recipientAddress,
                               ArrayList<ShowCaseObject> showCaseObjectList,
                               String cartIds) {
        renderBaseAddress(recipientAddress, cartIds);

        if (recipientAddress.isTradeIn()) {
            renderTradeInAddress(recipientAddress);
        } else {
            renderNormalAddress();
        }
        renderTradeInAddressWithTabs(recipientAddress);

        setShowCase(rlRecipientAddressLayout, showCaseObjectList);
    }

    private void renderBaseAddress(RecipientAddressModel recipientAddress, String cartIds) {
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
        tvAddressName.setText(Utils.getHtmlFormat(recipientAddress.getAddressName()));
        tvRecipientName.setText(Utils.getHtmlFormat(recipientAddress.getRecipientName()));
        tvRecipientAddress.setText(Utils.getHtmlFormat(getFullAddress(recipientAddress)));
        tvRecipientPhone.setVisibility(View.GONE);

        tvRecipientChangeAddress.setOnClickListener(v -> shipmentAdapterActionListener.onChangeAddress());
        tvSendToMultipleAddress.setOnClickListener(v -> shipmentAdapterActionListener.onSendToMultipleAddress(recipientAddress, cartIds));
    }

    private void setShowCase(ViewGroup viewGroup, ArrayList<ShowCaseObject> showCaseObjectList) {
        showCaseObjectList.add(new ShowCaseObject(viewGroup,
                viewGroup.getContext().getString(R.string.label_showcase_address_title),
                viewGroup.getContext().getString(R.string.label_showcase_address_message),
                ShowCaseContentPosition.UNDEFINED)
        );
    }

    private void renderTradeInAddress(RecipientAddressModel recipientAddress) {
        renderTradeInInfo(recipientAddress);
        llTradeInInfo.setVisibility(View.VISIBLE);
        if (recipientAddress.isTradeInDropOffEnable()) {
            renderTradeInAddressWithTabs(recipientAddress);
        } else {
            renderTradeInAddressWithoutTabs();
        }
    }

    private void renderTradeInAddressWithoutTabs() {
        tabUnifyTradeInAddress.setVisibility(View.GONE);
    }

    private void renderTradeInAddressWithTabs(RecipientAddressModel recipientAddress) {
        tabUnifyTradeInAddress.setVisibility(View.VISIBLE);
        if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() == 0) {
            tabUnifyTradeInAddress.addNewTab("Antar");
            tabUnifyTradeInAddress.addNewTab("Titik Ambil");
        }
        tabUnifyTradeInAddress.getUnifyTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                recipientAddress.setSelectedTabIndex(tab.getPosition());
                if (tab.getPosition() == 0) {
                    layoutAddressDropOff.setVisibility(View.GONE);
                    layoutAddressNormal.setVisibility(View.VISIBLE);
                } else if (tab.getPosition() == 1) {
                    layoutAddressNormal.setVisibility(View.GONE);
                    layoutAddressDropOff.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (recipientAddress.getSelectedTabIndex() == 0) {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() > 0) {
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(0).select();
            }
        } else {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() > 1) {
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(1).select();
            }
        }
    }

    private void renderNormalAddress() {
        llTradeInInfo.setVisibility(View.GONE);
        tabUnifyTradeInAddress.setVisibility(View.GONE);
        layoutAddressDropOff.setVisibility(View.GONE);
        layoutAddressNormal.setVisibility(View.VISIBLE);
    }

    private String getFullAddress(RecipientAddressModel recipientAddress) {
        return recipientAddress.getStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getCityName() + ", "
                + recipientAddress.getProvinceName() + ", "
                + recipientAddress.getRecipientPhoneNumber();
    }

    private void renderTradeInInfo(RecipientAddressModel recipientAddressModel) {
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
