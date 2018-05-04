package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.pickuppoint.PickupPointLayout;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

public abstract class ShipmentItemViewHolder extends RecyclerView.ViewHolder {

    static final int IMAGE_ALPHA_DISABLED = 128;
    static final int IMAGE_ALPHA_ENABLED = 255;

    static final int GRAM = 0;
    static final int KILOGRAM = 1;

    TextView tvError;
    FrameLayout layoutError;
    TextView tvWarning;
    FrameLayout layoutWarning;
    TextView tvTextSentBy;
    TextView tvShopName;
    ImageView imgShippingWarning;
    TextView tvShippingWarning;
    LinearLayout llShippingWarningContainer;
    ImageView ivProductImage;
    TextView tvProductName;
    TextView tvProductPrice;
    ImageView ivFreeReturnIcon;
    TextView tvFreeReturnLabel;
    TextView tvPreOrder;
    TextView tvCashback;
    RelativeLayout rlProductPoliciesLayout;
    TextView tvTextProductWeight;
    TextView tvProductWeight;
    TextView tvProductTotalItem;
    TextView tvLabelItemCount;
    TextView tvNoteToSellerLabel;
    TextView tvOptionalNoteToSeller;
    LinearLayout llOptionalNoteToSellerLayout;
    LinearLayout llItemProductContainer;
    TextView tvAddressName;
    TextView tvAddressStatus;
    LinearLayout llAddressName;
    TextView tvRecipientName;
    TextView tvRecipientAddress;
    TextView tvRecipientPhone;
    TextView tvChangeAddress;
    LinearLayout addressLayout;
    PickupPointLayout pickupPointLayout;
    RecyclerView rvCartItem;
    TextView tvExpandOtherProduct;
    RelativeLayout rlExpandOtherProduct;
    TextView tvTextShipment;
    TextView chooseCourierButton;
    LinearLayout llShipmentOptionViewLayout;
    TextView tvCartSubTotal;
    ImageView ivDetailOptionChevron;
    TextView tvSubTotalPrice;
    TextView tvShipmentOption;
    RelativeLayout rlCartSubTotal;
    TextView tvTotalItem;
    TextView tvTotalItemPrice;
    TextView tvShippingFee;
    TextView tvShippingFeePrice;
    TextView tvInsuranceFee;
    TextView tvInsuranceFeePrice;
    TextView tvPromoText;
    TextView tvPromoPrice;
    RelativeLayout rlShipmentCost;
    LinearLayout llSelectedCourier;
    TextView tvCourierName;
    TextView tvCourierPrice;
    TextView tvChangeCourier;
    LinearLayout llInsurance;
    CheckBox cbInsurance;
    ImageView imgInsuranceInfo;
    LinearLayout llDropshipper;
    CheckBox cbDropshipper;
    ImageView imgDropshipperInfo;
    LinearLayout llDropshipperInfo;
    EditText etShipperName;
    EditText etShipperPhone;
    View vSeparatorMultipleProductSameStore;

    public ShipmentItemViewHolder(View itemView) {
        super(itemView);

        tvError = itemView.findViewById(R.id.tv_error);
        layoutError = itemView.findViewById(R.id.layout_error);
        tvWarning = itemView.findViewById(R.id.tv_warning);
        layoutWarning = itemView.findViewById(R.id.layout_warning);
        tvTextSentBy = itemView.findViewById(R.id.tv_text_sent_by);
        tvShopName = itemView.findViewById(R.id.tv_shop_name);
        imgShippingWarning = itemView.findViewById(R.id.img_shipping_warning);
        tvShippingWarning = itemView.findViewById(R.id.tv_shipping_warning);
        llShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        ivProductImage = itemView.findViewById(R.id.iv_product_image);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        ivFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        tvFreeReturnLabel = itemView.findViewById(R.id.tv_free_return_label);
        tvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        tvCashback = itemView.findViewById(R.id.tv_cashback);
        rlProductPoliciesLayout = itemView.findViewById(R.id.layout_policy);
        tvTextProductWeight = itemView.findViewById(R.id.tv_text_product_weight);
        tvProductWeight = itemView.findViewById(R.id.tv_product_weight);
        tvProductTotalItem = itemView.findViewById(R.id.tv_product_total_item);
        tvLabelItemCount = itemView.findViewById(R.id.tv_label_item_count);
        tvNoteToSellerLabel = itemView.findViewById(R.id.tv_note_to_seller_label);
        tvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);
        llOptionalNoteToSellerLayout = itemView.findViewById(R.id.ll_optional_note_to_seller_layout);
        llItemProductContainer = itemView.findViewById(R.id.ll_item_product_container);
        tvAddressName = itemView.findViewById(R.id.tv_address_name);
        tvAddressStatus = itemView.findViewById(R.id.tv_address_status);
        llAddressName = itemView.findViewById(R.id.ll_address_name);
        tvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
        tvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
        tvRecipientPhone = itemView.findViewById(R.id.tv_recipient_phone);
        tvChangeAddress = itemView.findViewById(R.id.tv_change_address);
        addressLayout = itemView.findViewById(R.id.address_layout);
        pickupPointLayout = itemView.findViewById(R.id.pickup_point_layout);
        rvCartItem = itemView.findViewById(R.id.rv_cart_item);
        tvExpandOtherProduct = itemView.findViewById(R.id.tv_expand_other_product);
        rlExpandOtherProduct = itemView.findViewById(R.id.rl_expand_other_product);
        tvTextShipment = itemView.findViewById(R.id.tv_text_shipment);
        chooseCourierButton = itemView.findViewById(R.id.choose_courier_button);
        llShipmentOptionViewLayout = itemView.findViewById(R.id.ll_shipment_option_view_layout);
        tvCartSubTotal = itemView.findViewById(R.id.tv_cart_sub_total);
        ivDetailOptionChevron = itemView.findViewById(R.id.iv_detail_option_chevron);
        tvSubTotalPrice = itemView.findViewById(R.id.tv_sub_total_price);
        tvShipmentOption = itemView.findViewById(R.id.tv_shipment_option);
        rlCartSubTotal = itemView.findViewById(R.id.rl_cart_sub_total);
        tvTotalItem = itemView.findViewById(R.id.tv_total_item);
        tvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price);
        tvShippingFee = itemView.findViewById(R.id.tv_shipping_fee);
        tvShippingFeePrice = itemView.findViewById(R.id.tv_shipping_fee_price);
        tvInsuranceFee = itemView.findViewById(R.id.tv_insurance_fee);
        tvInsuranceFeePrice = itemView.findViewById(R.id.tv_insurance_fee_price);
        tvPromoText = itemView.findViewById(R.id.tv_promo_text);
        tvPromoPrice = itemView.findViewById(R.id.tv_promo_price);
        rlShipmentCost = itemView.findViewById(R.id.rl_shipment_cost);
        llSelectedCourier = itemView.findViewById(R.id.ll_selected_courier);
        tvCourierName = itemView.findViewById(R.id.tv_courier_name);
        tvCourierPrice = itemView.findViewById(R.id.tv_courier_price);
        tvChangeCourier = itemView.findViewById(R.id.tv_change_courier);
        llInsurance = itemView.findViewById(R.id.ll_insurance);
        cbInsurance = itemView.findViewById(R.id.cb_insurance);
        imgInsuranceInfo = itemView.findViewById(R.id.img_insurance_info);
        llDropshipper = itemView.findViewById(R.id.ll_dropshipper);
        cbDropshipper = itemView.findViewById(R.id.cb_dropshipper);
        imgDropshipperInfo = itemView.findViewById(R.id.img_dropshipper_info);
        llDropshipperInfo = itemView.findViewById(R.id.ll_dropshipper_info);
        etShipperName = itemView.findViewById(R.id.et_shipper_name);
        etShipperPhone = itemView.findViewById(R.id.et_shipper_phone);
        vSeparatorMultipleProductSameStore = itemView.findViewById(R.id.v_separator_multiple_product_same_store);
    }

    protected abstract void bindViewHolder(ShipmentItem shipmentSingleAddressItem,
                                           RecipientAddressModel recipientAddressModel,
                                           ArrayList<ShowCaseObject> showCaseObjectList);

    protected void showBottomSheet(Context context, String title, String message, int image) {
        BottomSheetView bottomSheetView = new BottomSheetView(context);
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(message)
                .setImg(image)
                .build());

        bottomSheetView.show();
    }


}
