package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.adapter.InnerProductListAdapter;
import com.tokopedia.checkout.view.utils.WeightFormatterUtil;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.design.pickuppoint.PickupPointLayout;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.logisticdata.data.constant.InsuranceConstant;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentItemViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_ITEM = R.layout.item_shipment;

    private static final int FIRST_ELEMENT = 0;
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private static final int DROPSHIPPER_MIN_NAME_LENGTH = 3;
    private static final int DROPSHIPPER_MAX_NAME_LENGTH = 100;
    private static final int DROPSHIPPER_MIN_PHONE_LENGTH = 6;
    private static final int DROPSHIPPER_MAX_PHONE_LENGTH = 20;
    private static final String PHONE_NUMBER_REGEX_PATTERN = "[0-9]+";

    private ShipmentAdapterActionListener mActionListener;

    private TextViewCompat tvError;
    private FrameLayout layoutError;
    private TextViewCompat tvWarning;
    private FrameLayout layoutWarning;
    private TextView tvTextSentBy;
    private TextView tvShopName;
    private ImageView imgShippingWarning;
    private TextView tvShippingWarning;
    private LinearLayout llShippingWarningContainer;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private ImageView ivFreeReturnIcon;
    private TextView tvFreeReturnLabel;
    private TextView tvPreOrder;
    private TextView tvCashback;
    private LinearLayout llProductPoliciesLayout;
    private TextView tvItemCountAndWeight;
    private TextView tvNoteToSellerLabel;
    private TextView tvOptionalNoteToSeller;
    private LinearLayout llOptionalNoteToSellerLayout;
    private LinearLayout llItemProductContainer;
    private TextView tvAddressName;
    private TextView tvAddressStatus;
    private LinearLayout llAddressName;
    private TextView tvRecipientName;
    private TextView tvRecipientAddress;
    private TextView tvRecipientPhone;
    private TextViewCompat tvChangeAddress;
    private LinearLayout addressLayout;
    private PickupPointLayout pickupPointLayout;
    private RecyclerView rvCartItem;
    private TextViewCompat tvExpandOtherProduct;
    private RelativeLayout rlExpandOtherProduct;
    private TextView tvTextShipment;
    private TextView chooseCourierButton;
    private LinearLayout llShipmentOptionViewLayout;
    private TextView tvCartSubTotal;
    private ImageView ivDetailOptionChevron;
    private TextView tvSubTotalPrice;
    private TextView tvShipmentOption;
    private RelativeLayout rlCartSubTotal;
    private TextView tvTotalItem;
    private TextView tvTotalItemPrice;
    private TextView tvShippingFee;
    private TextView tvShippingFeePrice;
    private TextView tvInsuranceFee;
    private TextView tvInsuranceFeePrice;
    private TextView tvPromoText;
    private TextView tvPromoPrice;
    private RelativeLayout rlShipmentCost;
    private LinearLayout llSelectedCourier;
    private TextView tvCourierName;
    private TextView tvCourierPrice;
    private TextView tvChangeCourier;
    private LinearLayout llInsurance;
    private CheckBox cbInsurance;
    private CheckBox cbInsuranceDisabled;
    private ImageView imgInsuranceInfo;
    private LinearLayout llDropshipper;
    private CheckBox cbDropshipper;
    private ImageView imgDropshipperInfo;
    private LinearLayout llDropshipperInfo;
    private AppCompatEditText etShipperName;
    private AppCompatEditText etShipperPhone;
    private TextInputLayout textInputLayoutShipperName;
    private TextInputLayout textInputLayoutShipperPhone;
    private View vSeparatorMultipleProductSameStore;
    private View vSeparatorAboveCourier;
    private LinearLayout llShipmpingType;
    private TextView tvShippingTypeName;
    private TextView tvShippingEtd;
    private TextView tvAdditionalFee;
    private TextView tvAdditionalFeePrice;
    private TextView tvLabelInsurance;
    private ImageView imgShopBadge;
    private TextView tvDash;
    private LinearLayout llShippingOptionsContainer;
    private LinearLayout llShipmentContainer;
    private LinearLayout llShipmentRecommendationContainer;
    private LinearLayout llSelectShipmentRecommendation;
    private TextView tvChooseDuration;
    private LinearLayout llSelectedShipmentRecommendation;
    private LinearLayout llSelectedDurationRecommendationContainer;
    private TextView tvSelectedDurationRecommendation;
    private TextView tvChangeSelectedDuration;
    private LinearLayout llSelectedCourierRecommendationContainer;
    private TextView tvSelectedCourierRecommendation;
    private TextView tvSelectedPriceRecommendation;
    private TextView tvChangeSelectedCourierRecommendation;
    private LinearLayout llShipmentInfoTicker;
    private TextView tvTickerInfo;

    public ShipmentItemViewHolder(View itemView) {
        super(itemView);
    }

    public ShipmentItemViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        this.mActionListener = actionListener;
        bindViewIds(itemView);
    }

    private void bindViewIds(View itemView) {
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
        llProductPoliciesLayout = itemView.findViewById(R.id.layout_policy);
        tvItemCountAndWeight = itemView.findViewById(R.id.tv_item_count_and_weight);
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
        cbInsuranceDisabled = itemView.findViewById(R.id.cb_insurance_disabled);
        imgInsuranceInfo = itemView.findViewById(R.id.img_insurance_info);
        llDropshipper = itemView.findViewById(R.id.ll_dropshipper);
        cbDropshipper = itemView.findViewById(R.id.cb_dropshipper);
        imgDropshipperInfo = itemView.findViewById(R.id.img_dropshipper_info);
        llDropshipperInfo = itemView.findViewById(R.id.ll_dropshipper_info);
        etShipperName = itemView.findViewById(R.id.et_shipper_name);
        etShipperPhone = itemView.findViewById(R.id.et_shipper_phone);
        textInputLayoutShipperName = itemView.findViewById(R.id.text_input_layout_shipper_name);
        textInputLayoutShipperPhone = itemView.findViewById(R.id.text_input_layout_shipper_phone);
        vSeparatorMultipleProductSameStore = itemView.findViewById(R.id.v_separator_multiple_product_same_store);
        vSeparatorAboveCourier = itemView.findViewById(R.id.v_separator_above_courier);
        llShipmpingType = itemView.findViewById(R.id.ll_shipmping_type);
        tvShippingTypeName = itemView.findViewById(R.id.tv_shipping_type_name);
        tvShippingEtd = itemView.findViewById(R.id.tv_shipping_etd);
        tvAdditionalFee = itemView.findViewById(R.id.tv_additional_fee);
        tvAdditionalFeePrice = itemView.findViewById(R.id.tv_additional_fee_price);
        tvLabelInsurance = itemView.findViewById(R.id.tv_label_insurance);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);
        tvDash = itemView.findViewById(R.id.tv_dash);
        llShippingOptionsContainer = itemView.findViewById(R.id.ll_shipping_options_container);
        llShipmentContainer = itemView.findViewById(R.id.ll_shipment_container);
        llShipmentRecommendationContainer = itemView.findViewById(R.id.ll_shipment_recommendation_container);
        llSelectShipmentRecommendation = itemView.findViewById(R.id.ll_select_shipment_recommendation);
        tvChooseDuration = itemView.findViewById(R.id.tv_choose_duration);
        llSelectedShipmentRecommendation = itemView.findViewById(R.id.ll_selected_shipment_recommendation);
        llSelectedDurationRecommendationContainer = itemView.findViewById(R.id.ll_selected_duration_recommendation_container);
        tvSelectedDurationRecommendation = itemView.findViewById(R.id.tv_selected_duration_recommendation);
        tvChangeSelectedDuration = itemView.findViewById(R.id.tv_change_selected_duration);
        llSelectedCourierRecommendationContainer = itemView.findViewById(R.id.ll_selected_courier_recommendation_container);
        tvSelectedCourierRecommendation = itemView.findViewById(R.id.tv_selected_courier_recommendation);
        tvSelectedPriceRecommendation = itemView.findViewById(R.id.tv_selected_price_recommendation);
        tvChangeSelectedCourierRecommendation = itemView.findViewById(R.id.tv_change_selected_courier_recommendation);
        tvTickerInfo = itemView.findViewById(R.id.tv_ticker_info);
        llShipmentInfoTicker = itemView.findViewById(R.id.ll_shipment_info_ticker);
    }

    private void showBottomSheet(Context context, String title, String message, int image) {
        Tooltip tooltip = new Tooltip(context);
        tooltip.setTitle(title);
        tooltip.setDesc(message);
        tooltip.setTextButton(context.getString(R.string.label_button_bottomsheet_close));
        tooltip.setIcon(image);
        tooltip.getBtnAction().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltip.dismiss();
            }
        });
        tooltip.show();
    }

    public void bindViewHolder(ShipmentCartItemModel shipmentCartItemModel,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        renderShop(shipmentCartItemModel);
        renderAddress(shipmentCartItemModel.getRecipientAddressModel());
        if (shipmentCartItemModel.isUseCourierRecommendation()) {
            renderCourierRecommendation(shipmentCartItemModel, shipmentCartItemModel.getSelectedShipmentDetailData(),
                    recipientAddressModel, shipmentCartItemModel.getShopShipmentList());
            if (showCaseObjectList.size() == 1) {
                setShowCase(llSelectShipmentRecommendation, showCaseObjectList);
            }
        } else {
            renderCourier(shipmentCartItemModel, shipmentCartItemModel.getSelectedShipmentDetailData(), recipientAddressModel);
            if (showCaseObjectList.size() == 1) {
                setShowCase(llShipmentOptionViewLayout, showCaseObjectList);
            }
        }
        renderError(shipmentCartItemModel);
        renderWarnings(shipmentCartItemModel);
        renderInsurance(shipmentCartItemModel);
        renderDropshipper(shipmentCartItemModel);
        renderCostDetail(shipmentCartItemModel);
        renderCartItem(shipmentCartItemModel);
    }

    private void renderCartItem(ShipmentCartItemModel shipmentCartItemModel) {
        List<CartItemModel> cartItemModelList = new ArrayList<>(shipmentCartItemModel.getCartItemModels());
        if (cartItemModelList.size() > 0) {
            renderFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT));
        }
        if (shipmentCartItemModel.getCartItemModels() != null && shipmentCartItemModel.getCartItemModels().size() > 1) {
            rlExpandOtherProduct.setVisibility(View.VISIBLE);
            renderOtherCartItems(shipmentCartItemModel, cartItemModelList);
            vSeparatorAboveCourier.setVisibility(View.VISIBLE);
        } else {
            rlExpandOtherProduct.setVisibility(View.GONE);
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            vSeparatorAboveCourier.setVisibility(View.GONE);
        }
    }

    private void renderShop(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isOfficialStore()) {
            imgShopBadge.setImageDrawable(ContextCompat.getDrawable(imgShopBadge.getContext(), R.drawable.ic_badge_official));
            imgShopBadge.setVisibility(View.VISIBLE);
        } else if (shipmentCartItemModel.isGoldMerchant()) {
            imgShopBadge.setImageDrawable(ContextCompat.getDrawable(imgShopBadge.getContext(), R.drawable.ic_shop_gold));
            imgShopBadge.setVisibility(View.VISIBLE);
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }
        tvShopName.setText(shipmentCartItemModel.getShopName());
    }

    private void renderFirstCartItem(CartItemModel cartItemModel) {
        if (cartItemModel.isError()) {
            showShipmentWarning(cartItemModel.getErrorMessage());
        } else {
            hideShipmentWarning();
        }
        ImageHandler.LoadImage(ivProductImage, cartItemModel.getImageUrl());
        tvProductName.setText(cartItemModel.getName());
        tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (long) cartItemModel.getPrice(), true));
        tvItemCountAndWeight.setText(String.format(tvItemCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItemModel.getQuantity()),
                WeightFormatterUtil.getFormattedWeight(cartItemModel.getWeight(), cartItemModel.getQuantity())));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItemModel.getNoteToSeller());
        llOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        tvOptionalNoteToSeller.setText(cartItemModel.getNoteToSeller());
        tvNoteToSellerLabel.setVisibility(View.GONE);

        ivFreeReturnIcon.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        tvPreOrder.setVisibility(cartItemModel.isPreOrder() ? View.VISIBLE : View.GONE);
        tvCashback.setVisibility(cartItemModel.isCashback() ? View.VISIBLE : View.GONE);
        String cashback = "    " + tvCashback.getContext().getString(R.string.label_cashback) + " " +
                cartItemModel.getCashback() + "    ";
        tvCashback.setText(cashback);

        if (cartItemModel.isFreeReturn() || cartItemModel.isPreOrder() || cartItemModel.isCashback()) {
            llProductPoliciesLayout.setVisibility(View.VISIBLE);
        } else {
            llProductPoliciesLayout.setVisibility(View.GONE);
        }
    }

    private void renderOtherCartItems(ShipmentCartItemModel shipmentItem, List<CartItemModel> cartItemModels) {
        rlExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem));
        initInnerRecyclerView(cartItemModels);
        if (shipmentItem.isStateAllItemViewExpanded()) {
            rvCartItem.setVisibility(View.VISIBLE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(R.string.label_hide_other_item);
            tvExpandOtherProduct.setTextColor(ContextCompat.getColor(tvExpandOtherProduct.getContext(), R.color.black_54));
            tvExpandOtherProduct.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_grey_24dp, 0);
        } else {
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(String.format(tvExpandOtherProduct.getContext().getString(R.string.label_other_item_count_format),
                    String.valueOf(cartItemModels.size())));
            tvExpandOtherProduct.setTextColor(ContextCompat.getColor(tvExpandOtherProduct.getContext(), R.color.medium_green));
            tvExpandOtherProduct.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_24dp, 0);
        }
    }

    private void renderCourier(ShipmentCartItemModel shipmentCartItemModel,
                               ShipmentDetailData shipmentDetailData,
                               RecipientAddressModel recipientAddressModel) {
        llShipmentRecommendationContainer.setVisibility(View.GONE);
        llShipmentContainer.setVisibility(View.VISIBLE);

        chooseCourierButton.setOnClickListener(getSelectShippingOptionListener(getAdapterPosition(),
                shipmentCartItemModel, recipientAddressModel));
        tvChangeCourier.setOnClickListener(getSelectShippingOptionListener(getAdapterPosition(),
                shipmentCartItemModel, recipientAddressModel));

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        tvTickerInfo.setVisibility(View.GONE);
        llShipmentInfoTicker.setVisibility(View.GONE);

        if (isCourierSelected) {
            llShippingOptionsContainer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(shipmentDetailData.getSelectedCourier().getShipmentItemDataType())) {
                if (shipmentDetailData.getSelectedCourier().getMinEtd() != 0 &&
                        shipmentDetailData.getSelectedCourier().getMaxEtd() != 0) {
                    String etd = "(" + shipmentDetailData.getSelectedCourier().getEstimatedTimeDelivery() + ")";
                    tvShippingEtd.setText(etd);
                    tvShippingEtd.setVisibility(View.VISIBLE);
                } else if (!TextUtils.isEmpty(shipmentDetailData.getSelectedCourier().getShipmentItemDataType()) &&
                        !TextUtils.isEmpty(shipmentDetailData.getSelectedCourier().getShipmentItemDataEtd())) {
                    String etd = "(" + shipmentDetailData.getSelectedCourier().getShipmentItemDataEtd() + ")";
                    tvShippingEtd.setText(etd);
                    tvShippingEtd.setVisibility(View.VISIBLE);
                } else {
                    tvShippingEtd.setVisibility(View.GONE);
                }
                tvShippingTypeName.setText(shipmentDetailData.getSelectedCourier().getShipmentItemDataType());
                llShipmpingType.setVisibility(View.VISIBLE);
            } else {
                llShipmpingType.setVisibility(View.GONE);
            }
            tvCourierName.setText(shipmentDetailData.getSelectedCourier().getName());
            String courierPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    shipmentDetailData.getSelectedCourier().getShipperPrice(), true);
            tvCourierPrice.setText(courierPrice);
            tvCourierPrice.setVisibility(View.VISIBLE);
            tvDash.setVisibility(View.VISIBLE);
            llShipmentOptionViewLayout.setVisibility(View.GONE);
            llSelectedCourier.setVisibility(View.VISIBLE);
        } else {
            llShippingOptionsContainer.setVisibility(View.GONE);
            llSelectedCourier.setVisibility(View.GONE);
            llShipmentOptionViewLayout.setVisibility(View.VISIBLE);
        }

    }

    private void renderCourierRecommendation(ShipmentCartItemModel shipmentCartItemModel,
                                             ShipmentDetailData shipmentDetailData,
                                             RecipientAddressModel recipientAddressModel,
                                             List<ShopShipment> shopShipmentList) {
        RecipientAddressModel currentAddress;
        if (recipientAddressModel == null) {
            currentAddress = shipmentCartItemModel.getRecipientAddressModel();
        } else {
            currentAddress = recipientAddressModel;
        }
        llShipmentContainer.setVisibility(View.GONE);
        llShipmentRecommendationContainer.setVisibility(View.VISIBLE);
        tvChooseDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.onChooseShipmentDuration(
                        shipmentCartItemModel, currentAddress, shopShipmentList, getAdapterPosition()
                );
            }
        });

        tvChangeSelectedDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.onChangeShippingDuration(shipmentCartItemModel, currentAddress,
                        shopShipmentList, getAdapterPosition());
            }
        });

        tvChangeSelectedCourierRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.onChangeShippingCourier(
                        shipmentCartItemModel.getSelectedShipmentDetailData().getShippingCourierViewModels(),
                        currentAddress, getAdapterPosition());
            }
        });

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        if (isCourierSelected) {
            if (!shipmentDetailData.getSelectedCourier().isAllowDropshiper()) {
                String tickerInfo = tvTickerInfo.getResources().getString(R.string.label_hardcoded_courier_ticker);
                String boldText = tvTickerInfo.getResources().getString(R.string.label_hardcoded_courier_ticker_bold_part);
                tvTickerInfo.setText(tickerInfo);

                int startSpan = tvTickerInfo.getText().toString().indexOf(boldText);
                int endSpan = tvTickerInfo.getText().toString().indexOf(boldText) + boldText.length();

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tvTickerInfo.getText().toString());
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvTickerInfo.setText(spannableStringBuilder);
                tvTickerInfo.setVisibility(View.VISIBLE);
                llShipmentInfoTicker.setVisibility(View.VISIBLE);
            } else {
                tvTickerInfo.setVisibility(View.GONE);
                llShipmentInfoTicker.setVisibility(View.GONE);
            }
            llSelectShipmentRecommendation.setVisibility(View.GONE);
            llSelectedShipmentRecommendation.setVisibility(View.VISIBLE);
            llShippingOptionsContainer.setVisibility(View.VISIBLE);
            tvSelectedDurationRecommendation.setText(shipmentDetailData.getSelectedCourier().getEstimatedTimeDelivery());
            tvSelectedCourierRecommendation.setText(shipmentDetailData.getSelectedCourier().getName());
            tvSelectedPriceRecommendation.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    shipmentDetailData.getSelectedCourier().getShipperPrice(), true));
        } else {
            llSelectedShipmentRecommendation.setVisibility(View.GONE);
            llSelectShipmentRecommendation.setVisibility(View.VISIBLE);
            llShippingOptionsContainer.setVisibility(View.GONE);
        }
    }

    private void renderCostDetail(ShipmentCartItemModel shipmentCartItemModel) {
        rlCartSubTotal.setVisibility(View.VISIBLE);
        rlShipmentCost.setVisibility(shipmentCartItemModel.isStateDetailSubtotalViewExpanded() ? View.VISIBLE : View.GONE);

        int totalItem = 0;
        double totalWeight = 0;
        int shippingPrice = 0;
        int insurancePrice = 0;
        int additionalPrice = 0;
        long subTotalPrice = 0;
        long totalItemPrice = 0;

        if (shipmentCartItemModel.isStateDetailSubtotalViewExpanded()) {
            rlShipmentCost.setVisibility(View.VISIBLE);
            ivDetailOptionChevron.setImageResource(R.drawable.ic_keyboard_arrow_up_24dp);
        } else {
            rlShipmentCost.setVisibility(View.GONE);
            ivDetailOptionChevron.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);
        }

        String shippingFeeLabel = tvShippingFee.getContext().getString(R.string.label_delivery_price);
        String totalItemLabel = tvTotalItem.getContext().getString(R.string.label_item_count_without_format);

        for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
            totalItemPrice += (cartItemModel.getQuantity() * cartItemModel.getPrice());
            totalItem += cartItemModel.getQuantity();
            totalWeight += cartItemModel.getWeight();
        }
        totalItemLabel = String.format(tvTotalItem.getContext().getString(R.string.label_item_count_with_format), totalItem);

        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            shippingPrice = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier()
                    .getShipperPrice();
            Boolean useInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance();
            if (useInsurance != null && useInsurance) {
                insurancePrice = shipmentCartItemModel.getSelectedShipmentDetailData()
                        .getSelectedCourier().getInsurancePrice();
            }
            additionalPrice = shipmentCartItemModel.getSelectedShipmentDetailData()
                    .getSelectedCourier().getAdditionalPrice();
            subTotalPrice += (totalItemPrice + shippingPrice + insurancePrice + additionalPrice);
        } else {
            subTotalPrice = totalItemPrice;
        }
        tvSubTotalPrice.setText(subTotalPrice == 0 ? "-" : CurrencyFormatUtil.convertPriceValueToIdrFormat(subTotalPrice, true));
        tvTotalItemPrice.setText(totalItemPrice == 0 ? "-" : getPriceFormat(tvTotalItem, tvTotalItemPrice, totalItemPrice));
        tvTotalItem.setText(totalItemLabel);
        tvShippingFee.setText(shippingFeeLabel);
        tvShippingFeePrice.setText(getPriceFormat(tvShippingFee, tvShippingFeePrice, shippingPrice));
        tvInsuranceFeePrice.setText(getPriceFormat(tvInsuranceFee, tvInsuranceFeePrice, insurancePrice));
        tvAdditionalFeePrice.setText(getPriceFormat(tvAdditionalFee, tvAdditionalFeePrice, additionalPrice));
        rlCartSubTotal.setOnClickListener(getCostDetailOptionListener(shipmentCartItemModel));
    }

    private void renderDropshipper(final ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().isAllowDropshiper()) {

            cbDropshipper.setChecked(shipmentCartItemModel.getSelectedShipmentDetailData().getUseDropshipper());
            if (shipmentCartItemModel.getSelectedShipmentDetailData().getUseDropshipper()) {
                llDropshipperInfo.setVisibility(View.VISIBLE);
            } else {
                llDropshipperInfo.setVisibility(View.GONE);
            }

            llDropshipper.setOnClickListener(getDropshipperClickListener());
            imgDropshipperInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheet(imgDropshipperInfo.getContext(),
                            imgDropshipperInfo.getContext().getString(R.string.label_dropshipper_new),
                            imgDropshipperInfo.getContext().getString(R.string.label_dropshipper_info),
                            R.drawable.ic_dropshipper);
                }
            });
            cbDropshipper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    shipmentCartItemModel.getSelectedShipmentDetailData().setUseDropshipper(checked);
                    if (checked) {
                        mActionListener.onDropshipCheckedForTrackingAnalytics();
                    } else {
                        shipmentCartItemModel.setStateDropshipperHasError(false);
                    }
                    mActionListener.onNeedUpdateViewItem(getAdapterPosition());
                    mActionListener.onNeedUpdateRequestData();
                }
            });

            etShipperName.setText(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperName());
            if (shipmentCartItemModel.isStateDropshipperHasError()) {
                validateDropshipperName(shipmentCartItemModel, etShipperName.getText(), true);
            } else {
                validateDropshipperName(shipmentCartItemModel, etShipperName.getText(), false);
            }
            etShipperName.setSelection(etShipperName.length());
            etShipperName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                        shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperName(charSequence.toString());
                        validateDropshipperName(shipmentCartItemModel, charSequence, true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN);
            etShipperPhone.setText(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperPhone());
            if (shipmentCartItemModel.isStateDropshipperHasError()) {
                validateDropshipperPhone(shipmentCartItemModel, etShipperPhone.getText(), pattern, true);
            } else {
                validateDropshipperPhone(shipmentCartItemModel, etShipperPhone.getText(), pattern, false);
            }
            etShipperPhone.setSelection(etShipperPhone.length());
            etShipperPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                        shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhone(charSequence.toString());
                        validateDropshipperPhone(shipmentCartItemModel, charSequence, pattern, true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            llDropshipper.setVisibility(View.VISIBLE);
        } else {
            cbDropshipper.setChecked(false);
            etShipperName.setText("");
            etShipperPhone.setText("");
            llDropshipper.setVisibility(View.GONE);
            llDropshipperInfo.setVisibility(View.GONE);
        }
    }

    private void validateDropshipperPhone(ShipmentCartItemModel shipmentCartItemModel, CharSequence charSequence, Pattern pattern, boolean fromTextWatcher) {
        Matcher matcher = pattern.matcher(charSequence);
        if (charSequence.length() == 0 && fromTextWatcher) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_empty));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onCartDataDisableToCheckout();
        } else if (etShipperPhone.getText().length() != 0 && !matcher.matches()) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_invalid));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onCartDataDisableToCheckout();
        } else if (etShipperPhone.getText().length() != 0 && etShipperPhone.getText().length() < DROPSHIPPER_MIN_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onCartDataDisableToCheckout();
        } else if (etShipperPhone.getText().length() != 0 && etShipperPhone.getText().length() > DROPSHIPPER_MAX_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onCartDataDisableToCheckout();
        } else {
            textInputLayoutShipperPhone.setErrorEnabled(false);
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(true);
            mActionListener.onCartDataEnableToCheckout();
        }
    }

    private void validateDropshipperName(ShipmentCartItemModel shipmentCartItemModel, CharSequence charSequence, boolean fromTextWatcher) {
        if (charSequence.length() == 0 && fromTextWatcher) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_empty));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onCartDataDisableToCheckout();
        } else if (etShipperName.getText().length() != 0 && etShipperName.getText().length() < DROPSHIPPER_MIN_NAME_LENGTH) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onCartDataDisableToCheckout();
        } else if (etShipperName.getText().length() != 0 && etShipperName.getText().length() > DROPSHIPPER_MAX_NAME_LENGTH) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onCartDataDisableToCheckout();
        } else {
            textInputLayoutShipperName.setErrorEnabled(false);
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(true);
            mActionListener.onCartDataEnableToCheckout();
        }
    }

    private void renderInsurance(final ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {

            cbInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(checked);
                    if (checked) {
                        mActionListener.onInsuranceCheckedForTrackingAnalytics();
                    }
                    mActionListener.onInsuranceChecked(getAdapterPosition());
                    mActionListener.onNeedUpdateRequestData();
                }
            });

            Boolean useInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance();
            if (useInsurance != null) {
                cbInsurance.setChecked(useInsurance);
            }

            final CourierItemData courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_MUST) {
                llInsurance.setVisibility(View.VISIBLE);
                tvLabelInsurance.setText(R.string.label_must_insurance);
                cbInsurance.setVisibility(View.GONE);
                cbInsuranceDisabled.setVisibility(View.VISIBLE);
                cbInsuranceDisabled.setChecked(true);
                cbInsuranceDisabled.setClickable(false);
                if (useInsurance == null) {
                    cbInsurance.setChecked(true);
                    shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(true);
                    mActionListener.onInsuranceChecked(getAdapterPosition());
                }
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_NO) {
                cbInsurance.setChecked(false);
                llInsurance.setVisibility(View.GONE);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(false);
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tvLabelInsurance.setText(R.string.label_shipment_insurance);
                llInsurance.setVisibility(View.VISIBLE);
                cbInsuranceDisabled.setVisibility(View.GONE);
                cbInsurance.setVisibility(View.VISIBLE);
                llInsurance.setOnClickListener(getInsuranceClickListener());
                if (useInsurance == null) {
                    if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                        cbInsurance.setChecked(true);
                        shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(true);
                        mActionListener.onInsuranceChecked(getAdapterPosition());
                    } else if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                        cbInsurance.setChecked(false);
                        shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(false);
                    }
                }
            }

            if (!TextUtils.isEmpty(courierItemData.getInsuranceUsedInfo())) {
                if (TextUtils.isEmpty(courierItemData.getInsuranceUsedInfo())) {
                    imgInsuranceInfo.setVisibility(View.GONE);
                } else {
                    imgInsuranceInfo.setVisibility(View.VISIBLE);
                    imgInsuranceInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showBottomSheet(imgInsuranceInfo.getContext(),
                                    imgInsuranceInfo.getContext().getString(R.string.title_bottomsheet_insurance),
                                    courierItemData.getInsuranceUsedInfo(),
                                    R.drawable.ic_insurance);
                        }
                    });
                }
            }

        }
    }

    private void renderAddress(RecipientAddressModel recipientAddressModel) {
        if (recipientAddressModel != null) {
            tvAddressName.setVisibility(View.GONE);
            tvChangeAddress.setVisibility(View.GONE);
            tvAddressStatus.setVisibility(View.GONE);
            String addressName = recipientAddressModel.getAddressName();
            String recipientName = recipientAddressModel.getRecipientName();
            addressName = " (" + addressName + ")";
            recipientName += addressName;
            int startSpan = recipientName.indexOf(addressName);
            int endSpan = recipientName.indexOf(addressName) + addressName.length();
            Spannable formattedPromoMessage = new SpannableString(recipientName);
            final int color = ContextCompat.getColor(tvRecipientName.getContext(), R.color.black_38);
            formattedPromoMessage.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvRecipientName.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
            tvRecipientName.setText(formattedPromoMessage);
            String fullAddress = recipientAddressModel.getStreet() + ", "
                    + recipientAddressModel.getDestinationDistrictName() + ", "
                    + recipientAddressModel.getCityName() + ", "
                    + recipientAddressModel.getProvinceName();
            tvRecipientAddress.setText(fullAddress);
            tvRecipientPhone.setText(recipientAddressModel.getRecipientPhoneNumber());
        } else {
            addressLayout.setVisibility(View.GONE);
        }
    }

    private void renderError(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isError()) {
            tvError.setText(shipmentCartItemModel.getErrorMessage());
            layoutError.setVisibility(View.VISIBLE);
        } else {
            layoutError.setVisibility(View.GONE);
        }
    }

    private void renderWarnings(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isWarning()) {
            tvWarning.setText(shipmentCartItemModel.getWarningMessage());
            layoutWarning.setVisibility(View.VISIBLE);
        } else {
            layoutWarning.setVisibility(View.GONE);
        }
    }

    private String getPriceFormat(TextView textViewLabel, TextView textViewPrice, long price) {
        if (price == 0) {
            textViewLabel.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.GONE);
            return "-";
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textViewPrice.setVisibility(View.VISIBLE);
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(price, true);
        }
    }

    private View.OnClickListener getCostDetailOptionListener(final ShipmentCartItemModel shipmentCartItemModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shipmentCartItemModel.setStateDetailSubtotalViewExpanded(!shipmentCartItemModel.isStateDetailSubtotalViewExpanded());
                mActionListener.onNeedUpdateViewItem(getAdapterPosition());
                mActionListener.onSubTotalCartItemClicked(getAdapterPosition());
            }
        };
    }

    private View.OnClickListener getInsuranceClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbInsurance.setChecked(!cbInsurance.isChecked());
                mActionListener.onInsuranceChecked(getAdapterPosition());
            }
        };
    }

    private View.OnClickListener getDropshipperClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbDropshipper.setChecked(!cbDropshipper.isChecked());
            }
        };
    }

    private View.OnClickListener getSelectShippingOptionListener(final int position,
                                                                 final ShipmentCartItemModel shipmentCartItemModel,
                                                                 final RecipientAddressModel recipientAddressModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onChooseShipment(position, shipmentCartItemModel, recipientAddressModel);
            }
        };
    }

    private void initInnerRecyclerView(List<CartItemModel> cartItemList) {
        rvCartItem.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rvCartItem.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCartItem.setLayoutManager(layoutManager);

        InnerProductListAdapter innerProductListAdapter =
                new InnerProductListAdapter(cartItemList);
        rvCartItem.setAdapter(innerProductListAdapter);
    }

    private View.OnClickListener showAllProductListener(final ShipmentCartItemModel shipmentCartItemModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shipmentCartItemModel.setStateAllItemViewExpanded(!shipmentCartItemModel.isStateAllItemViewExpanded());
                mActionListener.onNeedUpdateViewItem(getAdapterPosition());
            }
        };
    }

    private void setShowCase(ViewGroup viewGroup, ArrayList<ShowCaseObject> showCaseObjectList) {
        showCaseObjectList.add(new ShowCaseObject(viewGroup,
                viewGroup.getContext().getString(R.string.label_title_showcase_shipment),
                viewGroup.getContext().getString(R.string.label_message_showcase_shipment),
                ShowCaseContentPosition.UNDEFINED)
        );
    }

    private void showShipmentWarning(String message) {
        if (!TextUtils.isEmpty(message)) {
            tvShippingWarning.setText(message);
            llShippingWarningContainer.setVisibility(View.VISIBLE);
        } else {
            llShippingWarningContainer.setVisibility(View.GONE);
        }
        disableItemView();
    }

    private void hideShipmentWarning() {
        llShippingWarningContainer.setVisibility(View.GONE);
        enableItemView();
    }

    private void disableItemView() {
        int nonActiveTextColor = ContextCompat.getColor(tvProductName.getContext(), R.color.grey_nonactive_text);
        tvProductName.setTextColor(nonActiveTextColor);
        tvProductPrice.setTextColor(nonActiveTextColor);
        tvFreeReturnLabel.setTextColor(nonActiveTextColor);
        tvPreOrder.setTextColor(nonActiveTextColor);
        tvNoteToSellerLabel.setTextColor(nonActiveTextColor);
        tvOptionalNoteToSeller.setTextColor(nonActiveTextColor);
        tvItemCountAndWeight.setTextColor(nonActiveTextColor);
        tvCashback.setTextColor(nonActiveTextColor);
        tvCashback.setBackground(ContextCompat.getDrawable(tvCashback.getContext(), R.drawable.bg_cashback_disabled));
        setImageFilterGrayScale();
    }

    private void setImageFilterGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter disabledColorFilter = new ColorMatrixColorFilter(matrix);
        ivProductImage.setColorFilter(disabledColorFilter);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
        ivFreeReturnIcon.setColorFilter(disabledColorFilter);
        ivFreeReturnIcon.setImageAlpha(IMAGE_ALPHA_DISABLED);
    }

    private void enableItemView() {
        tvProductName.setTextColor(ContextCompat.getColor(tvProductName.getContext(), R.color.black_70));
        tvProductPrice.setTextColor(ContextCompat.getColor(tvProductPrice.getContext(), R.color.orange_red));
        tvFreeReturnLabel.setTextColor(ContextCompat.getColor(tvFreeReturnLabel.getContext(), R.color.font_black_secondary_54));
        tvPreOrder.setTextColor(ContextCompat.getColor(tvPreOrder.getContext(), R.color.font_black_secondary_54));
        tvNoteToSellerLabel.setTextColor(ContextCompat.getColor(tvNoteToSellerLabel.getContext(), R.color.black_38));
        tvItemCountAndWeight.setTextColor(ContextCompat.getColor(tvItemCountAndWeight.getContext(), R.color.black_38));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(tvOptionalNoteToSeller.getContext(), R.color.black_38));
        tvCashback.setTextColor(ContextCompat.getColor(tvCashback.getContext(), R.color.cashback_text_color));
        tvCashback.setBackground(ContextCompat.getDrawable(tvCashback.getContext(), R.drawable.bg_cashback));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        ivProductImage.setColorFilter(null);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

}
