package com.tokopedia.checkout.view.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.empty_state.EmptyStateUnify;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.unifycomponents.ChipsUnify;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifycomponents.TabsUnify;
import com.tokopedia.unifycomponents.UnifyImageButton;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;

import kotlin.Unit;

import static com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData.DEFAULT_ADDRESS;
import static com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData.TRADE_IN_ADDRESS;
import static com.tokopedia.checkout.view.TradeInInfoBottomsheetHelperKt.TRADE_IN_DROP_OFF_IMAGE_URL;

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
    private TabsUnify tabUnifyTradeInAddress;
    private RelativeLayout layoutAddressNormal;
    private ConstraintLayout layoutAddressDropOff;
    private Typography tvShipmentAddress;
    private UnifyImageButton imgButtonTradeInInfo;
    private View separator;
    private Typography tvDropOffAddressTitle;
    private Typography tvDropOffAddressDescription;
    private Typography tvChangeDropOff;
    private ImageView imgPinpoint;
    private Typography tvChangeAddressTop;
    private View separatorBottom;
    private Space space;
    private ConstraintLayout layoutTradeInOption;
    private ChipsUnify chipsTradeInNormal;
    private ChipsUnify chipsTradeInDropOff;
    private EmptyStateUnify emptyStateDropOff;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentRecipientAddressViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        rlRecipientAddressLayout = itemView.findViewById(R.id.rl_shipment_recipient_address_layout);
        tvAddressStatus = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_address_status);
        tvAddressName = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_address_name);
        tvRecipientName = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_name);
        tvRecipientAddress = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_address);
        tvRecipientPhone = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_phone);
        tabUnifyTradeInAddress = itemView.findViewById(R.id.tab_unify_trade_in_address);
        layoutAddressNormal = itemView.findViewById(R.id.layout_address_normal);
        layoutAddressDropOff = itemView.findViewById(R.id.layout_address_drop_off);
        tvShipmentAddress = itemView.findViewById(R.id.tv_shipment_address);
        imgButtonTradeInInfo = itemView.findViewById(R.id.img_button_trade_in_info);
        separator = itemView.findViewById(R.id.separator);
        tvDropOffAddressTitle = itemView.findViewById(R.id.tv_drop_off_address_title);
        tvDropOffAddressDescription = itemView.findViewById(R.id.tv_drop_off_address_description);
        tvChangeDropOff = itemView.findViewById(R.id.tv_change_drop_off);
        imgPinpoint = itemView.findViewById(R.id.img_pinpoint);
        tvChangeAddressTop = itemView.findViewById(R.id.tv_change_address_top);
        separatorBottom = itemView.findViewById(R.id.separator_bottom);
        space = itemView.findViewById(R.id.space);
        layoutTradeInOption = itemView.findViewById(R.id.layout_trade_in_option);
        chipsTradeInNormal = itemView.findViewById(R.id.chips_trade_in_normal);
        chipsTradeInDropOff = itemView.findViewById(R.id.chips_trade_in_drop_off);
        emptyStateDropOff = itemView.findViewById(R.id.empty_state_drop_off);
    }

    public void bindViewHolder(RecipientAddressModel recipientAddress,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        renderChangeAddress(recipientAddress);
        if (recipientAddress.isTradeIn()) {
            renderTradeInAddressWithTabs(recipientAddress);
        } else {
            renderNormalAddress(recipientAddress);
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

    private void renderTradeInAddressWithTabs(RecipientAddressModel recipientAddress) {
        tvShipmentAddress.setText(R.string.lebal_trade_in_address_mode);
        layoutTradeInOption.setVisibility(View.VISIBLE);
        tvShipmentAddress.setType(Typography.HEADING_5);
        imgButtonTradeInInfo.setVisibility(View.VISIBLE);
        separator.setVisibility(View.GONE);

        imgButtonTradeInInfo.setOnClickListener(v -> {
            shipmentAdapterActionListener.onClickTradeInInfo();
        });

        if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() == 0) {
            tabUnifyTradeInAddress.addNewTab(tabUnifyTradeInAddress.getContext().getString(R.string.label_tab_trade_in_address_deliver));
            tabUnifyTradeInAddress.addNewTab(tabUnifyTradeInAddress.getContext().getString(R.string.label_tab_trade_in_address_pickup));
        }

        renderSelectedTab(recipientAddress);

        setChipTabsClickListener(recipientAddress);

        renderDisabledTab(recipientAddress);

        tabUnifyTradeInAddress.getUnifyTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (!recipientAddress.isIgnoreSelectionAction()) {
                        recipientAddress.setIgnoreSelectionAction(true);
                        recipientAddress.setSelectedTabIndex(tab.getPosition());
                        shipmentAdapterActionListener.onTradeInAddressTabChanged(position);
                    }
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

    private void renderDisabledTab(RecipientAddressModel recipientAddress) {
        if (recipientAddress.getDisabledAddress() != null && !recipientAddress.getDisabledAddress().isEmpty()) {
            if (recipientAddress.getDisabledAddress().contains(DEFAULT_ADDRESS)) {
                chipsTradeInNormal.setChipType(ChipsUnify.TYPE_DISABLE);
                chipsTradeInNormal.setClickable(false);
                chipsTradeInNormal.setOnClickListener(null);
            }
            if (recipientAddress.getDisabledAddress().contains(TRADE_IN_ADDRESS)) {
                chipsTradeInDropOff.setChipType(ChipsUnify.TYPE_DISABLE);
                chipsTradeInDropOff.setClickable(false);
                chipsTradeInNormal.setOnClickListener(null);
            }
        }
    }

    private void renderSelectedTab(RecipientAddressModel recipientAddress) {
        if (recipientAddress.getSelectedTabIndex() == 0) {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() > 0) {
                recipientAddress.setIgnoreSelectionAction(true);
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(0).select();
                renderTradeInDeliveryTab(recipientAddress);
                setTradeInDefaultAddress();
            }
        } else {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().getTabCount() > 1) {
                recipientAddress.setIgnoreSelectionAction(true);
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(1).select();
                renderTradeInPickUpTab(recipientAddress);
                setTradeInDropOffAddress();
            }
        }
    }

    private void setChipTabsClickListener(RecipientAddressModel recipientAddress) {
        chipsTradeInNormal.setOnClickListener(v -> {
            shipmentAdapterActionListener.onSwapInUserAddress();
            TabLayout.Tab tab = tabUnifyTradeInAddress.getTabLayout().getTabAt(0);
            if (tab != null) {
                recipientAddress.setIgnoreSelectionAction(false);
                setTradeInDefaultAddress();
                tab.select();
            }
        });

        chipsTradeInDropOff.setOnClickListener(v -> {
            shipmentAdapterActionListener.onClickSwapInIndomaret();
            TabLayout.Tab tab = tabUnifyTradeInAddress.getTabLayout().getTabAt(1);
            if (tab != null) {
                recipientAddress.setIgnoreSelectionAction(false);
                setTradeInDropOffAddress();
                tab.select();
            }
        });
    }

    private void setTradeInDefaultAddress() {
        chipsTradeInNormal.setChipType(ChipsUnify.TYPE_SELECTED);
        if (chipsTradeInDropOff.getChipType() != null && chipsTradeInDropOff.getChipType().equals(ChipsUnify.TYPE_SELECTED)) {
            chipsTradeInDropOff.setChipType(ChipsUnify.TYPE_NORMAL);
        }
    }

    private void setTradeInDropOffAddress() {
        chipsTradeInDropOff.setChipType(ChipsUnify.TYPE_SELECTED);
        if (chipsTradeInNormal.getChipType() != null && chipsTradeInNormal.getChipType().equals(ChipsUnify.TYPE_SELECTED)) {
            chipsTradeInNormal.setChipType(ChipsUnify.TYPE_NORMAL);
        }
    }

    private void renderTradeInPickUpTab(RecipientAddressModel recipientAddress) {
        layoutAddressNormal.setVisibility(View.GONE);
        layoutAddressDropOff.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(recipientAddress.getDropOffAddressName())) {
            tvDropOffAddressTitle.setVisibility(View.GONE);
            tvDropOffAddressDescription.setVisibility(View.GONE);
            imgPinpoint.setVisibility(View.GONE);
            tvChangeDropOff.setVisibility(View.GONE);
            emptyStateDropOff.setVisibility(View.VISIBLE);
            emptyStateDropOff.setTitle(emptyStateDropOff.getContext().getString(R.string.label_drop_off_empty_title));
            emptyStateDropOff.setDescription(emptyStateDropOff.getContext().getString(R.string.label_drop_off_empty_description));
            emptyStateDropOff.setPrimaryCTAText(emptyStateDropOff.getContext().getString(R.string.label_drop_off_cta));
            emptyStateDropOff.setPrimaryCTAClickListener(() -> {
                shipmentAdapterActionListener.onChangeTradeInDropOffClicked();
                return Unit.INSTANCE;
            });
            emptyStateDropOff.setImageUrl(TRADE_IN_DROP_OFF_IMAGE_URL);
        } else {
            tvDropOffAddressTitle.setVisibility(View.VISIBLE);
            tvDropOffAddressDescription.setVisibility(View.VISIBLE);
            tvDropOffAddressTitle.setText(recipientAddress.getDropOffAddressName());
            tvDropOffAddressDescription.setText(recipientAddress.getDropOffAddressDetail());
            imgPinpoint.setVisibility(View.VISIBLE);
            tvChangeDropOff.setVisibility(View.VISIBLE);
            emptyStateDropOff.setVisibility(View.GONE);
        }
    }

    private void renderTradeInDeliveryTab(RecipientAddressModel recipientAddress) {
        layoutAddressDropOff.setVisibility(View.GONE);
        layoutAddressNormal.setVisibility(View.VISIBLE);
        renderBasicAddress(recipientAddress);
    }

    private void renderNormalAddress(RecipientAddressModel recipientAddress) {
        tvShipmentAddress.setText(R.string.label_showcase_address_title);
        layoutTradeInOption.setVisibility(View.GONE);
        imgButtonTradeInInfo.setVisibility(View.GONE);
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

    private void renderChangeAddress(RecipientAddressModel recipientAddress) {
        separatorBottom.setVisibility(View.GONE);
        space.setVisibility(View.VISIBLE);
        if (recipientAddress.getSelectedTabIndex() == 0) {
            tvChangeAddressTop.setVisibility(View.VISIBLE);
            tvChangeAddressTop.setOnClickListener(v -> shipmentAdapterActionListener.onChangeAddress());
        } else {
            tvChangeAddressTop.setVisibility(View.GONE);
        }
    }

}
