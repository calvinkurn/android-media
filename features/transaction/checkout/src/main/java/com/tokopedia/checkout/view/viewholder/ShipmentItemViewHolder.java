package com.tokopedia.checkout.view.viewholder;

import android.animation.Animator;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.utils.WeightFormatterUtil;
import com.tokopedia.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.adapter.ShipmentInnerProductListAdapter;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticdata.data.constant.CourierConstant;
import com.tokopedia.logisticdata.data.constant.InsuranceConstant;
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentItemViewHolder extends RecyclerView.ViewHolder implements ShipmentCartItemViewHolder.ShipmentItemListener {

    public static final int ITEM_VIEW_SHIPMENT_ITEM = R.layout.item_shipment;

    private static final int FIRST_ELEMENT = 0;

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private static final int DROPSHIPPER_MIN_NAME_LENGTH = 3;
    private static final int DROPSHIPPER_MAX_NAME_LENGTH = 100;
    private static final int DROPSHIPPER_MIN_PHONE_LENGTH = 6;
    private static final int DROPSHIPPER_MAX_PHONE_LENGTH = 20;
    private static final String PHONE_NUMBER_REGEX_PATTERN = "[0-9]+";

    private static final int SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF = 1;
    private static final int SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE = 2;

    private static final int VIBRATION_ANIMATION_DURATION = 1250;
    private static final int VIBRATION_ANIMATION_TRANSLATION_X = -10;
    private static final float VIBRATION_ANIMATION_CYCLE = 4f;

    private ShipmentAdapterActionListener mActionListener;

    private LinearLayout layoutError;
    private Ticker tickerError;
    private LinearLayout layoutWarning;
    private Typography tvShopName;
    private LinearLayout llShippingWarningContainer;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvProductOriginalPrice;
    private TextView tvItemCountAndWeight;
    private TextView tvOptionalNoteToSeller;
    private RelativeLayout rlPurchaseProtection;
    private TextView tvPPPLinkText;
    private TextView tvPPPPrice;
    private TextView tvPPPMore;
    private CheckBox cbPPP;
    private CheckBox cbPPPDisabled;
    private RecyclerView rvCartItem;
    private Typography tvExpandOtherProduct;
    private ImageView ivExpandOtherProduct;
    private RelativeLayout rlExpandOtherProduct;
    private ImageView ivDetailOptionChevron;
    private Typography tvSubTotalPrice;
    private RelativeLayout rlCartSubTotal;
    private Typography tvTotalItem;
    private Typography tvTotalItemPrice;
    private Typography tvShippingFee;
    private Typography tvShippingFeePrice;
    private Typography tvInsuranceFee;
    private Typography tvInsuranceFeePrice;
    private Typography tvProtectionLabel;
    private Typography tvProtectionFee;
    private RelativeLayout rlShipmentCost;
    private LinearLayout llInsurance;
    private CheckBox cbInsurance;
    private CheckBox cbInsuranceDisabled;
    private ImageView imgInsuranceInfo;
    private LinearLayout llDropshipper;
    private CheckBox cbDropshipper;
    private Typography tvDropshipper;
    private ImageView imgDropshipperInfo;
    private LinearLayout llDropshipperInfo;
    private AppCompatEditText etShipperName;
    private AppCompatEditText etShipperPhone;
    private TextInputLayout textInputLayoutShipperName;
    private TextInputLayout textInputLayoutShipperPhone;
    private View vSeparatorMultipleProductSameStore;
    private View vSeparatorBelowProduct;
    private Typography tvAdditionalFee;
    private Typography tvAdditionalFeePrice;
    private TextView tvLabelInsurance;
    private ImageView imgShopBadge;
    private LinearLayout llShippingOptionsContainer;
    private LinearLayout layoutWarningAndError;
    private LinearLayout llCourierRecommendationTradeInDropOffStateLoading;
    private TextView tvErrorShipmentItemTitle;
    private TextView tvErrorShipmentItemDescription;
    private FrameLayout flDisableContainer;
    private Ticker productTicker;
    private ConstraintLayout layoutTradeInShippingInfo;
    private Typography tvTradeInShippingPriceTitle;
    private Typography tvTradeInShippingPriceDetail;
    private Typography labelChooseDurationTradeIn;
    private Typography tvChooseDurationTradeIn;
    private Typography textVariant;

    private TextView tvTradeInLabel;

    private List<Object> shipmentDataList;
    private Pattern phoneNumberRegexPattern;
    private CompositeSubscription compositeSubscription;
    private SaveStateDebounceListener saveStateDebounceListener;
    private TextView tvFulfillName;
    private Label labelFulfillment;
    private ImageView imgFreeShipping;
    private Typography textOrderNumber;
    private Label labelPreOrder;
    private Typography textLabelIncidentShopLevel;

    // order prioritas
    private CheckBox checkBoxPriority;
    private TextView tvPrioritasTicker;
    private LinearLayout llPrioritasTicker;
    private RelativeLayout llPrioritas;
    private Typography tvPrioritasFee;
    private Typography tvPrioritasFeePrice;
    private ImageView imgPriorityTnc;
    private TextView tvPrioritasInfo;
    private boolean isPriorityChecked = false;

    // Shipping Experience
    private LinearLayout llShippingExperienceContainer;
    private LinearLayout llShippingExperienceStateLoading;
    private FrameLayout containerShippingExperience;
    private ConstraintLayout layoutStateNoSelectedShipping;
    private ConstraintLayout layoutStateHasSelectedNormalShipping;
    private Typography labelSelectedShippingDuration;
    private ImageView iconChevronChooseDuration;
    private Typography labelSelectedShippingCourier;
    private Typography labelSelectedShippingPrice;
    private Typography labelDescCourier;
    private Typography labelDescCourierTnc;
    private ImageView iconChevronChooseCourier;
    private ConstraintLayout layoutStateHasSelectedFreeShipping;
    private Typography labelSelectedFreeShipping;
    private Typography labelFreeShippingCourierName;
    private Typography labelFreeShippingOriginalPrice;
    private Typography labelFreeShippingDiscountedPrice;

    public ShipmentItemViewHolder(View itemView) {
        super(itemView);
    }

    public ShipmentItemViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        this.mActionListener = actionListener;
        phoneNumberRegexPattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN);

        bindViewIds(itemView);
    }

    private void bindViewIds(View itemView) {
        layoutError = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.layout_error);
        tickerError = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.ticker_error);
        layoutWarning = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.layout_warning);
        layoutWarning.setVisibility(View.GONE);
        tvShopName = itemView.findViewById(R.id.tv_shop_name);
        llShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        ivProductImage = itemView.findViewById(R.id.iv_product_image);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        tvProductOriginalPrice = itemView.findViewById(R.id.tv_product_original_price);
        rlPurchaseProtection = itemView.findViewById(R.id.rlayout_purchase_protection);
        tvPPPLinkText = itemView.findViewById(R.id.text_link_text);
        tvPPPPrice = itemView.findViewById(R.id.text_price_per_product);
        tvPPPMore = itemView.findViewById(R.id.text_ppp_more);
        cbPPP = itemView.findViewById(R.id.checkbox_ppp);
        cbPPPDisabled = itemView.findViewById(R.id.checkbox_ppp_disabled);
        tvItemCountAndWeight = itemView.findViewById(R.id.tv_item_count_and_weight);
        tvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);
        tvProtectionLabel = itemView.findViewById(R.id.tv_purchase_protection_label);
        tvProtectionFee = itemView.findViewById(R.id.tv_purchase_protection_fee);
        rvCartItem = itemView.findViewById(R.id.rv_cart_item);
        tvExpandOtherProduct = itemView.findViewById(R.id.tv_expand_other_product);
        ivExpandOtherProduct = itemView.findViewById(R.id.iv_expand_other_product);
        rlExpandOtherProduct = itemView.findViewById(R.id.rl_expand_other_product);
        ivDetailOptionChevron = itemView.findViewById(R.id.iv_detail_option_chevron);
        tvSubTotalPrice = itemView.findViewById(R.id.tv_sub_total_price);
        rlCartSubTotal = itemView.findViewById(R.id.rl_cart_sub_total);
        tvTotalItem = itemView.findViewById(R.id.tv_total_item);
        tvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price);
        tvShippingFee = itemView.findViewById(R.id.tv_shipping_fee);
        tvShippingFeePrice = itemView.findViewById(R.id.tv_shipping_fee_price);
        tvInsuranceFee = itemView.findViewById(R.id.tv_insurance_fee);
        tvInsuranceFeePrice = itemView.findViewById(R.id.tv_insurance_fee_price);
        rlShipmentCost = itemView.findViewById(R.id.rl_shipment_cost);
        llInsurance = itemView.findViewById(R.id.ll_insurance);
        cbInsurance = itemView.findViewById(R.id.cb_insurance);
        cbInsuranceDisabled = itemView.findViewById(R.id.cb_insurance_disabled);
        imgInsuranceInfo = itemView.findViewById(R.id.img_insurance_info);
        llDropshipper = itemView.findViewById(R.id.ll_dropshipper);
        cbDropshipper = itemView.findViewById(R.id.cb_dropshipper);
        tvDropshipper = itemView.findViewById(R.id.label_dropshipper);
        imgDropshipperInfo = itemView.findViewById(R.id.img_dropshipper_info);
        llDropshipperInfo = itemView.findViewById(R.id.ll_dropshipper_info);
        etShipperName = itemView.findViewById(R.id.et_shipper_name);
        etShipperPhone = itemView.findViewById(R.id.et_shipper_phone);
        textInputLayoutShipperName = itemView.findViewById(R.id.text_input_layout_shipper_name);
        textInputLayoutShipperPhone = itemView.findViewById(R.id.text_input_layout_shipper_phone);
        vSeparatorMultipleProductSameStore = itemView.findViewById(R.id.v_separator_multiple_product_same_store);
        vSeparatorBelowProduct = itemView.findViewById(R.id.v_separator_below_product);
        tvAdditionalFee = itemView.findViewById(R.id.tv_additional_fee);
        tvAdditionalFeePrice = itemView.findViewById(R.id.tv_additional_fee_price);
        tvLabelInsurance = itemView.findViewById(R.id.tv_label_insurance);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);
        llShippingOptionsContainer = itemView.findViewById(R.id.ll_shipping_options_container);
        layoutWarningAndError = itemView.findViewById(R.id.layout_warning_and_error);
        llCourierRecommendationTradeInDropOffStateLoading = itemView.findViewById(R.id.ll_courier_recommendation_Trade_in_drop_off_state_loading);
        tvErrorShipmentItemTitle = itemView.findViewById(R.id.tv_error_shipment_item_title);
        tvErrorShipmentItemDescription = itemView.findViewById(R.id.tv_error_shipment_item_description);
        flDisableContainer = itemView.findViewById(R.id.fl_disable_container);
        imgFreeShipping = itemView.findViewById(R.id.img_free_shipping);
        layoutTradeInShippingInfo = itemView.findViewById(R.id.layout_trade_in_shipping_info);
        tvTradeInShippingPriceTitle = itemView.findViewById(R.id.tv_trade_in_shipping_price_title);
        tvTradeInShippingPriceDetail = itemView.findViewById(R.id.tv_trade_in_shipping_price_detail);
        labelChooseDurationTradeIn = itemView.findViewById(R.id.label_choose_duration_trade_in);
        tvChooseDurationTradeIn = itemView.findViewById(R.id.tv_choose_duration_trade_in);
        productTicker = itemView.findViewById(R.id.product_ticker);
        textOrderNumber = itemView.findViewById(R.id.text_order_number);
        labelPreOrder = itemView.findViewById(R.id.label_pre_order);
        textLabelIncidentShopLevel = itemView.findViewById(R.id.text_label_incident_shop_level);
        textVariant = itemView.findViewById(R.id.text_variant);

        //priority
        llPrioritas = itemView.findViewById(R.id.ll_prioritas);
        checkBoxPriority = itemView.findViewById(R.id.cb_prioritas);
        llPrioritasTicker = itemView.findViewById(R.id.ll_prioritas_ticker);
        tvPrioritasTicker = itemView.findViewById(R.id.tv_prioritas_ticker);
        tvPrioritasFee = itemView.findViewById(R.id.tv_priority_fee);
        tvPrioritasFeePrice = itemView.findViewById(R.id.tv_priority_fee_price);
        imgPriorityTnc = itemView.findViewById(R.id.img_prioritas_info);
        tvPrioritasInfo = itemView.findViewById(R.id.tv_order_prioritas_info);

        tvFulfillName = itemView.findViewById(R.id.tv_fulfill_district);
        labelFulfillment = itemView.findViewById(R.id.label_fulfillment);
        tvTradeInLabel = itemView.findViewById(R.id.tv_trade_in_label);

        // Shipping Experience
        llShippingExperienceContainer = itemView.findViewById(R.id.ll_shipping_experience_container);
        llShippingExperienceStateLoading = itemView.findViewById(R.id.ll_shipping_experience_state_loading);
        containerShippingExperience = itemView.findViewById(R.id.container_shipping_experience);
        layoutStateNoSelectedShipping = itemView.findViewById(R.id.layout_state_no_selected_shipping);
        layoutStateHasSelectedNormalShipping = itemView.findViewById(R.id.layout_state_has_selected_normal_shipping);
        labelSelectedShippingDuration = itemView.findViewById(R.id.label_selected_shipping_duration);
        iconChevronChooseDuration = itemView.findViewById(R.id.icon_chevron_choose_duration);
        labelSelectedShippingCourier = itemView.findViewById(R.id.label_selected_shipping_courier);
        labelSelectedShippingPrice = itemView.findViewById(R.id.label_selected_shipping_price);
        labelDescCourier = itemView.findViewById(R.id.label_description_courier);
        labelDescCourierTnc = itemView.findViewById(R.id.label_description_courier_tnc);
        iconChevronChooseCourier = itemView.findViewById(R.id.icon_chevron_choose_courier);
        layoutStateHasSelectedFreeShipping = itemView.findViewById(R.id.layout_state_has_selected_free_shipping);
        labelSelectedFreeShipping = itemView.findViewById(R.id.label_selected_free_shipping);
        labelFreeShippingCourierName = itemView.findViewById(R.id.label_free_shipping_courier_name);
        labelFreeShippingOriginalPrice = itemView.findViewById(R.id.label_free_shipping_original_price);
        labelFreeShippingDiscountedPrice = itemView.findViewById(R.id.label_free_shipping_discounted_price);

        compositeSubscription = new CompositeSubscription();
        initSaveStateDebouncer();
    }

    @Override
    public void notifyOnPurchaseProtectionChecked(boolean checked, int position) {
        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
            if (shipmentDataList.get(getAdapterPosition()) instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel data = ((ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition()));
                data.getCartItemModels().get(position).setProtectionOptIn(checked);
                if (checked && (cbDropshipper.isChecked() && data.getSelectedShipmentDetailData().getUseDropshipper())) {
                    data.getSelectedShipmentDetailData().setUseDropshipper(false);
                    cbDropshipper.setChecked(false);
                    mActionListener.onPurchaseProtectionLogicError();
                }
            }
            mActionListener.onNeedUpdateRequestData();
            mActionListener.onPurchaseProtectionChangeListener(getAdapterPosition());
        }
    }

    @Override
    public void navigateToWebView(String protectionLinkUrl) {
        mActionListener.navigateToProtectionMore(protectionLinkUrl);
    }

    public void bindViewHolder(ShipmentCartItemModel shipmentCartItemModel,
                               List<Object> shipmentDataList,
                               RecipientAddressModel recipientAddressModel,
                               RatesDataConverter ratesDataConverter) {
        if (this.shipmentDataList == null) {
            this.shipmentDataList = shipmentDataList;
        }
        renderShop(shipmentCartItemModel);
        renderFulfillment(shipmentCartItemModel);
        renderShipping(shipmentCartItemModel, recipientAddressModel, ratesDataConverter);
        renderPrioritas(shipmentCartItemModel);
        renderInsurance(shipmentCartItemModel);
        renderDropshipper(recipientAddressModel != null && recipientAddressModel.isCornerAddress());
        renderCostDetail(shipmentCartItemModel);
        renderCartItem(shipmentCartItemModel);
        renderErrorAndWarning(shipmentCartItemModel);
        renderShippingVibrationAnimation(shipmentCartItemModel);
    }

    public void unsubscribeDebouncer() {
        compositeSubscription.unsubscribe();
    }

    private void renderShippingVibrationAnimation(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isShippingBorderRed()) {
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_red);
        } else {
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey);
        }
        if (shipmentCartItemModel.isTriggerShippingVibrationAnimation()) {
            containerShippingExperience.animate()
                    .translationX(VIBRATION_ANIMATION_TRANSLATION_X)
                    .setDuration(VIBRATION_ANIMATION_DURATION)
                    .setInterpolator(new CycleInterpolator(VIBRATION_ANIMATION_CYCLE))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            shipmentCartItemModel.setTriggerShippingVibrationAnimation(false);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .start();
        }
    }

    private void renderFulfillment(ShipmentCartItemModel model) {
        labelFulfillment.setVisibility(model.isFulfillment() ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(model.getShopLocation())) {
            tvFulfillName.setVisibility(View.VISIBLE);
            tvFulfillName.setText(model.getShopLocation());
        } else {
            tvFulfillName.setVisibility(View.GONE);
        }
    }

    private void renderErrorAndWarning(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isError()) {
            layoutWarningAndError.setVisibility(View.VISIBLE);
        } else {
            layoutWarningAndError.setVisibility(View.GONE);
        }
        renderError(shipmentCartItemModel);
    }

    private void renderShipping(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, RatesDataConverter ratesDataConverter) {
        boolean isTradeInDropOff = mActionListener.isTradeInByDropOff();

        RecipientAddressModel currentAddress;
        if (recipientAddressModel == null) {
            currentAddress = shipmentCartItemModel.getRecipientAddressModel();
        } else {
            currentAddress = recipientAddressModel;
        }

        if (isTradeInDropOff) {
            renderRobinhoodV2(shipmentCartItemModel, currentAddress, ratesDataConverter);
        } else {
            renderShippingExperience(shipmentCartItemModel, currentAddress, ratesDataConverter);
        }
    }

    private void renderCartItem(ShipmentCartItemModel shipmentCartItemModel) {
        List<CartItemModel> cartItemModelList = new ArrayList<>(shipmentCartItemModel.getCartItemModels());
        if (cartItemModelList.size() > 0) {
            renderFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT));
        }
        if (shipmentCartItemModel.getCartItemModels() != null && shipmentCartItemModel.getCartItemModels().size() > 1) {
            rlExpandOtherProduct.setVisibility(View.VISIBLE);
            renderOtherCartItems(shipmentCartItemModel, cartItemModelList);
            vSeparatorBelowProduct.setVisibility(View.VISIBLE);
        } else {
            rlExpandOtherProduct.setVisibility(View.GONE);
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            vSeparatorBelowProduct.setVisibility(View.GONE);
        }
    }

    private void renderShop(ShipmentCartItemModel shipmentCartItemModel) {
        textOrderNumber.setText("Pesanan " + shipmentCartItemModel.getOrderNumber());

        if (!TextUtils.isEmpty(shipmentCartItemModel.getPreOrderInfo())) {
            labelPreOrder.setText(shipmentCartItemModel.getPreOrderInfo());
            labelPreOrder.setVisibility(View.VISIBLE);
        } else {
            labelPreOrder.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shipmentCartItemModel.getFreeShippingBadgeUrl())) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFreeShipping, shipmentCartItemModel.getFreeShippingBadgeUrl()
            );
            imgFreeShipping.setVisibility(View.VISIBLE);
        } else {
            imgFreeShipping.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shipmentCartItemModel.getShopAlertMessage())) {
            textLabelIncidentShopLevel.setText(shipmentCartItemModel.getShopAlertMessage());
            textLabelIncidentShopLevel.setVisibility(View.VISIBLE);
        } else {
            textLabelIncidentShopLevel.setVisibility(View.GONE);
        }

        boolean hasTradeInItem = false;
        for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
            if (cartItemModel.isValidTradeIn()) {
                hasTradeInItem = true;
                break;
            }
        }
        if (hasTradeInItem) {
            tvTradeInLabel.setVisibility(View.VISIBLE);
        } else {
            tvTradeInLabel.setVisibility(View.GONE);
        }
        if (shipmentCartItemModel.isOfficialStore() || shipmentCartItemModel.isGoldMerchant()) {
            if (!shipmentCartItemModel.getShopBadge().isEmpty()) {
                ImageHandler.loadImageWithoutPlaceholder(imgShopBadge, shipmentCartItemModel.getShopBadge());
                imgShopBadge.setVisibility(View.VISIBLE);
            }
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        String shopName = shipmentCartItemModel.getShopName();

        tvShopName.setText(shopName);
    }

    private void renderFirstCartItem(CartItemModel cartItemModel) {
        if (cartItemModel.isError()) {
            showShipmentWarning(cartItemModel);
        } else {
            hideShipmentWarning();
        }
        ImageHandler.LoadImage(ivProductImage, cartItemModel.getImageUrl());
        tvProductName.setText(cartItemModel.getName());
        tvItemCountAndWeight.setText(String.format(tvItemCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItemModel.getQuantity()),
                WeightFormatterUtil.getFormattedWeight(cartItemModel.getWeight(), cartItemModel.getQuantity())));
        if (!TextUtils.isEmpty(cartItemModel.getVariant())) {
            textVariant.setText(cartItemModel.getVariant());
            textVariant.setVisibility(View.VISIBLE);
        } else {
            textVariant.setVisibility(View.GONE);
        }
        renderProductPrice(cartItemModel);
        renderNotesForSeller(cartItemModel);
        renderPurchaseProtection(cartItemModel);
        renderProductTicker(cartItemModel);
    }

    private void renderProductPrice(CartItemModel cartItemModel) {
        tvProductPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (long) cartItemModel.getPrice(), false)));
        if (cartItemModel.getOriginalPrice() > 0) {
            tvProductPrice.setPadding(tvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                    tvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_4), 0, 0);
            tvProductOriginalPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cartItemModel.getOriginalPrice(), false
            )));
            tvProductOriginalPrice.setPaintFlags(tvProductOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvProductOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            tvProductPrice.setPadding(tvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_10),
                    tvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_4), 0, 0);
            tvProductOriginalPrice.setVisibility(View.GONE);
        }
    }

    private void renderNotesForSeller(CartItemModel cartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.getNoteToSeller())) {
            tvOptionalNoteToSeller.setText(Utils.getHtmlFormat(cartItemModel.getNoteToSeller()));
            tvOptionalNoteToSeller.setVisibility(View.VISIBLE);
        } else {
            tvOptionalNoteToSeller.setVisibility(View.GONE);
        }
    }

    private void renderPurchaseProtection(CartItemModel cartItemModel) {
        rlPurchaseProtection.setVisibility(cartItemModel.isProtectionAvailable() ? View.VISIBLE : View.GONE);
        if (cartItemModel.isProtectionAvailable()) {
            tvPPPMore.setText(cartItemModel.getProtectionLinkText());
            tvPPPMore.setOnClickListener(view -> mActionListener.navigateToProtectionMore(cartItemModel.getProtectionLinkUrl()));
            tvPPPLinkText.setText(cartItemModel.getProtectionTitle());
            tvPPPPrice.setText(cartItemModel.getProtectionSubTitle());

            if (cartItemModel.isProtectionCheckboxDisabled()) {
                cbPPP.setVisibility(View.GONE);
                cbPPPDisabled.setVisibility(View.VISIBLE);
                cbPPPDisabled.setChecked(true);
                cbPPPDisabled.setClickable(false);
            } else {
                cbPPPDisabled.setVisibility(View.GONE);
                cbPPP.setVisibility(View.VISIBLE);
                cbPPP.setChecked(cartItemModel.isProtectionOptIn());
                cbPPP.setClickable(true);
                cbPPP.setOnCheckedChangeListener((compoundButton, checked) -> notifyOnPurchaseProtectionChecked(checked, 0));
            }

        }
    }

    private void renderProductTicker(CartItemModel cartItemModel) {
        if (cartItemModel.isShowTicker() && !TextUtils.isEmpty(cartItemModel.getTickerMessage())) {
            productTicker.setVisibility(View.VISIBLE);
            productTicker.setTextDescription(cartItemModel.getTickerMessage());
        } else productTicker.setVisibility(View.GONE);
    }

    private void renderOtherCartItems(ShipmentCartItemModel shipmentItem, List<CartItemModel> cartItemModels) {
        rlExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem));
        initInnerRecyclerView(cartItemModels);
        if (shipmentItem.isStateAllItemViewExpanded()) {
            rvCartItem.setVisibility(View.VISIBLE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(R.string.label_hide_other_item_new);
            ivExpandOtherProduct.setImageResource(R.drawable.checkout_module_ic_up);
        } else {
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(String.format(tvExpandOtherProduct.getContext().getString(R.string.label_other_item_count_format),
                    String.valueOf(cartItemModels.size())));
            tvExpandOtherProduct.setTextColor(ContextCompat.getColor(tvExpandOtherProduct.getContext(), com.tokopedia.design.R.color.medium_green));
            ivExpandOtherProduct.setImageResource(R.drawable.checkout_module_ic_down);
        }
    }

    private void renderShippingExperience(ShipmentCartItemModel shipmentCartItemModel,
                                          RecipientAddressModel currentAddress,
                                          RatesDataConverter ratesDataConverter) {
        layoutTradeInShippingInfo.setVisibility(View.GONE);
        llShippingExperienceContainer.setVisibility(View.VISIBLE);

        boolean isCourierSelected = shipmentCartItemModel.getSelectedShipmentDetailData() != null
                && shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null;
        if (isCourierSelected) {
            // Has select shipping
            CourierItemData selectedCourierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            llShippingOptionsContainer.setVisibility(View.VISIBLE);
            layoutStateNoSelectedShipping.setVisibility(View.GONE);

            llShippingExperienceStateLoading.setVisibility(View.GONE);
            containerShippingExperience.setVisibility(View.VISIBLE);
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey);
            if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                // Is free ongkir shipping
                layoutStateHasSelectedNormalShipping.setVisibility(View.GONE);
                layoutStateHasSelectedFreeShipping.setVisibility(View.VISIBLE);
                layoutStateHasSelectedFreeShipping.setOnClickListener(
                        getOnChangeDurationClickListener(shipmentCartItemModel, currentAddress)
                );

                if (shipmentCartItemModel.isError()) {
                    mActionListener.onCancelVoucherLogisticClicked(
                            shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode(),
                            getAdapterPosition());
                }

                if (selectedCourierItemData.isHideShipperName()) {
                    // Hide shipper name
                    labelFreeShippingCourierName.setVisibility(View.GONE);
                } else {
                    // Show shipper name
                    labelFreeShippingCourierName.setVisibility(View.VISIBLE);
                }

                // Change duration to promo title after promo is applied
                labelSelectedFreeShipping.setText(selectedCourierItemData.getPromoTitle());
                if (selectedCourierItemData.getDiscountedRate() == 0) {
                    // Free Shipping Price
                    labelFreeShippingOriginalPrice.setVisibility(View.GONE);
                    labelFreeShippingDiscountedPrice.setVisibility(View.GONE);
                } else if (selectedCourierItemData.getDiscountedRate() > 0) {
                    // Discounted Shipping Price
                    labelFreeShippingOriginalPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            selectedCourierItemData.getShippingRate(), false
                    )));
                    labelFreeShippingOriginalPrice.setPaintFlags(labelFreeShippingOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    labelFreeShippingDiscountedPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            selectedCourierItemData.getDiscountedRate(), false
                    )));
                    labelFreeShippingOriginalPrice.setVisibility(View.VISIBLE);
                    labelFreeShippingDiscountedPrice.setVisibility(View.VISIBLE);
                }
            } else {
                // Is normal shipping
                layoutStateHasSelectedFreeShipping.setVisibility(View.GONE);
                layoutStateHasSelectedNormalShipping.setVisibility(View.VISIBLE);

                labelSelectedShippingDuration.setText(selectedCourierItemData.getEstimatedTimeDelivery());
                labelSelectedShippingDuration.setOnClickListener(
                        getOnChangeDurationClickListener(shipmentCartItemModel, currentAddress)
                );
                iconChevronChooseDuration.setOnClickListener(
                        getOnChangeDurationClickListener(shipmentCartItemModel, currentAddress)
                );

                labelSelectedShippingCourier.setText(selectedCourierItemData.getName());
                labelSelectedShippingPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        selectedCourierItemData.getShipperPrice(), false
                )));
                labelSelectedShippingPrice.setOnClickListener(
                        getOnChangeCourierClickListener(shipmentCartItemModel, currentAddress)
                );
                labelSelectedShippingCourier.setOnClickListener(
                        getOnChangeCourierClickListener(shipmentCartItemModel, currentAddress)
                );
                iconChevronChooseCourier.setOnClickListener(
                        getOnChangeCourierClickListener(shipmentCartItemModel, currentAddress)
                );

                OntimeDelivery ontimeDelivery = selectedCourierItemData.getOntimeDelivery();
                CashOnDeliveryProduct codProductData = selectedCourierItemData.getCodProductData();

                if (ontimeDelivery != null && ontimeDelivery.getAvailable()) {
                    // On time delivery guarantee
                    labelDescCourier.setText(ontimeDelivery.getTextLabel());
                    labelDescCourierTnc.setOnClickListener(view -> {
                        mActionListener.onOntimeDeliveryClicked(ontimeDelivery.getUrlDetail());
                    });
                    labelDescCourier.setVisibility(View.VISIBLE);
                    labelDescCourierTnc.setVisibility(View.VISIBLE);
                } else if (codProductData != null && codProductData.isCodAvailable() == 1) {
                    /*Cash on delivery*/
                    labelDescCourier.setText(codProductData.getCodText());
                    labelDescCourierTnc.setOnClickListener(view -> {
                        mActionListener.onOntimeDeliveryClicked(codProductData.getTncLink());
                    });
                    labelDescCourier.setVisibility(View.VISIBLE);
                    labelDescCourierTnc.setVisibility(View.VISIBLE);
                } else {
                    labelDescCourier.setVisibility(View.GONE);
                    labelDescCourierTnc.setVisibility(View.GONE);
                }
            }
        } else {
            // Has not select shipping
            llShippingOptionsContainer.setVisibility(View.GONE);
            layoutStateHasSelectedNormalShipping.setVisibility(View.GONE);
            layoutStateHasSelectedFreeShipping.setVisibility(View.GONE);
            layoutStateNoSelectedShipping.setVisibility(View.VISIBLE);
            layoutStateNoSelectedShipping.setOnClickListener(
                    getOnChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            );

            loadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE);
        }
    }

    private View.OnClickListener getOnChangeCourierClickListener(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress) {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                mActionListener.onChangeShippingCourier(currentAddress, shipmentCartItemModel, getAdapterPosition());
            }
        };
    }

    private View.OnClickListener getOnChangeDurationClickListener(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress) {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                mActionListener.onChangeShippingDuration(shipmentCartItemModel, currentAddress, getAdapterPosition());
            }
        };
    }

    private void renderRobinhoodV2(ShipmentCartItemModel shipmentCartItemModel,
                                   RecipientAddressModel currentAddress,
                                   RatesDataConverter ratesDataConverter) {
        llShippingExperienceContainer.setVisibility(View.GONE);

        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        tvChooseDurationTradeIn.setOnClickListener(view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                mActionListener.onChooseShipmentDuration(
                        shipmentCartItemModel, currentAddress, getAdapterPosition()
                );
            }
        });
        layoutTradeInShippingInfo.setVisibility(View.VISIBLE);
        llCourierRecommendationTradeInDropOffStateLoading.setVisibility(View.GONE);
        boolean isCourierTradeInDropOffSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourierTradeInDropOff() != null;
        if (isCourierTradeInDropOffSelected) {
            tvTradeInShippingPriceDetail.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff().getShipperPrice(), false)));
            tvTradeInShippingPriceDetail.setVisibility(View.VISIBLE);
            tvTradeInShippingPriceTitle.setVisibility(View.VISIBLE);
            labelChooseDurationTradeIn.setVisibility(View.GONE);
            tvChooseDurationTradeIn.setVisibility(View.GONE);
            llShippingOptionsContainer.setVisibility(View.VISIBLE);
        } else {
            llShippingOptionsContainer.setVisibility(View.GONE);
            if (shipmentCartItemModel.isHasSetDropOffLocation()) {
                loadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
            } else {
                tvTradeInShippingPriceDetail.setText(R.string.label_trade_in_shipping_price);
            }
        }
    }

    private void loadCourierState(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  RatesDataConverter ratesDataConverter,
                                  int saveStateType) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        if (shipmentCartItemModel.isStateLoadingCourierState()) {
            switch (saveStateType) {
                case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                    llCourierRecommendationTradeInDropOffStateLoading.setVisibility(View.VISIBLE);
                    labelChooseDurationTradeIn.setVisibility(View.GONE);
                    tvChooseDurationTradeIn.setVisibility(View.GONE);
                    tvTradeInShippingPriceTitle.setVisibility(View.GONE);
                    tvTradeInShippingPriceDetail.setVisibility(View.GONE);
                    break;
                case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                    llShippingExperienceStateLoading.setVisibility(View.VISIBLE);
                    containerShippingExperience.setVisibility(View.GONE);
                    break;
            }
        } else {
            boolean hasLoadCourier = false;
            switch (saveStateType) {
                case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                    hasLoadCourier = shipmentDetailData != null && shipmentDetailData.getSelectedCourierTradeInDropOff() != null;
                    llCourierRecommendationTradeInDropOffStateLoading.setVisibility(View.GONE);
                    break;
                case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                    hasLoadCourier = shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null;
                    llShippingExperienceStateLoading.setVisibility(View.GONE);
                    break;
            }

            if (shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0) {
                if (!hasLoadCourier) {
                    RecipientAddressModel tmpRecipientAddressModel;
                    if (recipientAddressModel != null) {
                        tmpRecipientAddressModel = recipientAddressModel;
                    } else {
                        tmpRecipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                    }
                    ShipmentDetailData tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
                            shipmentCartItemModel, tmpRecipientAddressModel);

                    boolean hasLoadCourierState = false;
                    if (saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF) {
                        hasLoadCourierState = shipmentCartItemModel.isStateHasLoadCourierTradeInDropOffState();
                    } else {
                        hasLoadCourierState = shipmentCartItemModel.isStateHasLoadCourierState();
                    }

                    if (!hasLoadCourierState) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mActionListener.onLoadShippingState(shipmentCartItemModel.getShippingId(),
                                    shipmentCartItemModel.getSpId(), position, tmpShipmentDetailData,
                                    shipmentCartItemModel, shipmentCartItemModel.getShopShipmentList(),
                                    saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
                            shipmentCartItemModel.setStateLoadingCourierState(true);
                            switch (saveStateType) {
                                case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                                    shipmentCartItemModel.setStateHasLoadCourierTradeInDropOffState(true);
                                    llCourierRecommendationTradeInDropOffStateLoading.setVisibility(View.VISIBLE);
                                    labelChooseDurationTradeIn.setVisibility(View.GONE);
                                    tvChooseDurationTradeIn.setVisibility(View.GONE);
                                    tvTradeInShippingPriceTitle.setVisibility(View.GONE);
                                    tvTradeInShippingPriceDetail.setVisibility(View.GONE);
                                    break;
                                case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                                    shipmentCartItemModel.setStateHasLoadCourierState(true);
                                    llShippingExperienceStateLoading.setVisibility(View.VISIBLE);
                                    containerShippingExperience.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    } else {
                        switch (saveStateType) {
                            case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                                llCourierRecommendationTradeInDropOffStateLoading.setVisibility(View.GONE);
                                labelChooseDurationTradeIn.setVisibility(View.VISIBLE);
                                tvChooseDurationTradeIn.setVisibility(View.VISIBLE);
                                tvTradeInShippingPriceTitle.setVisibility(View.GONE);
                                tvTradeInShippingPriceDetail.setVisibility(View.GONE);
                                break;
                            case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                                llShippingExperienceStateLoading.setVisibility(View.GONE);
                                containerShippingExperience.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            } else {
                switch (saveStateType) {
                    case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                        llCourierRecommendationTradeInDropOffStateLoading.setVisibility(View.GONE);
                        labelChooseDurationTradeIn.setVisibility(View.VISIBLE);
                        tvChooseDurationTradeIn.setVisibility(View.VISIBLE);
                        tvTradeInShippingPriceTitle.setVisibility(View.GONE);
                        tvTradeInShippingPriceDetail.setVisibility(View.GONE);
                        break;
                    case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                        llShippingExperienceStateLoading.setVisibility(View.GONE);
                        containerShippingExperience.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    private void renderCostDetail(ShipmentCartItemModel shipmentCartItemModel) {
        rlCartSubTotal.setVisibility(View.VISIBLE);
        rlShipmentCost.setVisibility(shipmentCartItemModel.isStateDetailSubtotalViewExpanded() ? View.VISIBLE : View.GONE);

        int totalItem = 0;
        double totalWeight = 0;
        int shippingPrice = 0;
        int insurancePrice = 0;
        int priorityPrice = 0;
        long totalPurchaseProtectionPrice = 0;
        int totalPurchaseProtectionItem = 0;
        int additionalPrice = 0;
        long subTotalPrice = 0;
        long totalItemPrice = 0;

        if (shipmentCartItemModel.isStateDetailSubtotalViewExpanded()) {
            rlShipmentCost.setVisibility(View.VISIBLE);
            // ivDetailOptionChevron.setImageResource(R.drawable.ic_keyboard_arrow_up_24dp);
            ivDetailOptionChevron.setImageResource(R.drawable.checkout_module_ic_up);
        } else {
            rlShipmentCost.setVisibility(View.GONE);
            // ivDetailOptionChevron.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);
            ivDetailOptionChevron.setImageResource(R.drawable.checkout_module_ic_down);
        }

        String shippingFeeLabel = tvShippingFee.getContext().getString(R.string.label_delivery_price);
        String totalItemLabel = tvTotalItem.getContext().getString(R.string.label_item_count_without_format);

        for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
            if (!cartItemModel.isError()) {
                totalItemPrice += (cartItemModel.getQuantity() * cartItemModel.getPrice());
                totalItem += cartItemModel.getQuantity();
                totalWeight += cartItemModel.getWeight();
                if (cartItemModel.isProtectionOptIn()) {
                    totalPurchaseProtectionItem += cartItemModel.getQuantity();
                    totalPurchaseProtectionPrice += cartItemModel.getProtectionPrice();
                }
            }
        }
        totalItemLabel = String.format(tvTotalItem.getContext().getString(R.string.label_item_count_with_format), totalItem);
        String totalPPPItemLabel = String.format("Proteksi Produk (%d Barang)", totalPurchaseProtectionItem);

        VoucherLogisticItemUiModel voucherLogisticItemUiModel = shipmentCartItemModel.getVoucherLogisticItemUiModel();
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null ||
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) &&
                !shipmentCartItemModel.isError()) {
            CourierItemData courierItemData = null;
            if (mActionListener.isTradeInByDropOff() && shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff();
            } else if (!mActionListener.isTradeInByDropOff() && shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            }

            if (courierItemData != null) {
                shippingPrice = courierItemData.getShipperPrice();
                Boolean useInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance();
                if (useInsurance != null && useInsurance) {
                    insurancePrice = courierItemData.getInsurancePrice();
                }
                Boolean isOrderPriority = shipmentCartItemModel.getSelectedShipmentDetailData().isOrderPriority();
                if (isOrderPriority != null && isOrderPriority) {
                    priorityPrice = courierItemData.getPriorityPrice();
                }
                additionalPrice = courierItemData.getAdditionalPrice();
                subTotalPrice += (totalItemPrice + insurancePrice + totalPurchaseProtectionPrice + additionalPrice + priorityPrice);
                if (voucherLogisticItemUiModel != null) {
                    int discountedRate = courierItemData.getDiscountedRate();
                    subTotalPrice += discountedRate;
                } else {
                    subTotalPrice += shippingPrice;
                }
            } else {
                subTotalPrice = totalItemPrice;
            }
        } else {
            subTotalPrice = totalItemPrice;
        }
        tvSubTotalPrice.setText(subTotalPrice == 0 ? "-" : Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(subTotalPrice, false)));
        tvTotalItemPrice.setText(totalItemPrice == 0 ? "-" : getPriceFormat(tvTotalItem, tvTotalItemPrice, totalItemPrice));
        tvTotalItem.setText(totalItemLabel);
        tvShippingFee.setText(shippingFeeLabel);
        tvShippingFeePrice.setText(getPriceFormat(tvShippingFee, tvShippingFeePrice, shippingPrice));
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                voucherLogisticItemUiModel != null) {
            if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getDiscountedRate() == 0) {
                tvShippingFeePrice.setText(tvShippingFeePrice.getContext().getString(com.tokopedia.purchase_platform.common.R.string.label_free_shipping));
            } else {
                tvShippingFeePrice.setText(getPriceFormat(tvShippingFee, tvShippingFeePrice, shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getDiscountedRate()));
            }
        }
        tvInsuranceFeePrice.setText(getPriceFormat(tvInsuranceFee, tvInsuranceFeePrice, insurancePrice));
        tvPrioritasFeePrice.setText(getPriceFormat(tvPrioritasFee, tvPrioritasFeePrice, priorityPrice));
        tvProtectionLabel.setText(totalPPPItemLabel);
        tvProtectionFee.setText(getPriceFormat(tvProtectionLabel, tvProtectionFee, totalPurchaseProtectionPrice));
        tvAdditionalFeePrice.setText(getPriceFormat(tvAdditionalFee, tvAdditionalFeePrice, additionalPrice));
        rlCartSubTotal.setOnClickListener(getCostDetailOptionListener(shipmentCartItemModel));
    }

    private void renderDropshipper(boolean isCorner) {
        if (shipmentDataList != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
            ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition());
            boolean isTradeInDropOff = mActionListener.isTradeInByDropOff();
            CourierItemData courierItemData = null;
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                if (isTradeInDropOff) {
                    courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff();
                } else {
                    courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
                }
            }
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null && courierItemData != null) {

                if (shipmentCartItemModel.isDropshipperDisable() || !courierItemData.isAllowDropshiper() || isCorner) {
                    llDropshipper.setVisibility(View.GONE);
                    llDropshipperInfo.setVisibility(View.GONE);
                    shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperName(null);
                    shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhone(null);
                    etShipperName.setText("");
                    etShipperPhone.setText("");
                } else {
                    llDropshipper.setVisibility(View.VISIBLE);
                }

                cbDropshipper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        mActionListener.hideSoftKeyboard();

                        if (checked && isHavingPurchaseProtectionChecked()) {
                            compoundButton.setChecked(false);
                            mActionListener.onPurchaseProtectionLogicError();
                            return;
                        }

                        if (getAdapterPosition() != RecyclerView.NO_POSITION &&
                                shipmentDataList.get(getAdapterPosition()) instanceof ShipmentCartItemModel) {
                            ShipmentCartItemModel data = ((ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition()));
                            data.getSelectedShipmentDetailData().setUseDropshipper(checked);
                            if (checked) {
                                etShipperName.setText(data.getDropshiperName());
                                etShipperPhone.setText(data.getDropshiperPhone());
                                data.getSelectedShipmentDetailData().setDropshipperName(data.getDropshiperName());
                                data.getSelectedShipmentDetailData().setDropshipperPhone(data.getDropshiperPhone());
                                llDropshipperInfo.setVisibility(View.VISIBLE);
                                mActionListener.onDropshipCheckedForTrackingAnalytics();
                            } else {
                                etShipperName.setText("");
                                etShipperPhone.setText("");
                                data.getSelectedShipmentDetailData().setDropshipperName("");
                                data.getSelectedShipmentDetailData().setDropshipperPhone("");
                                data.setDropshiperName("");
                                data.setDropshiperPhone("");
                                llDropshipperInfo.setVisibility(View.GONE);
                                data.setStateDropshipperHasError(false);
                            }
                            mActionListener.onNeedUpdateViewItem(getAdapterPosition());
                            mActionListener.onNeedUpdateRequestData();
                            saveStateDebounceListener.onNeedToSaveState(data);
                        }
                    }
                });

                Boolean useDropshipper = shipmentCartItemModel.getSelectedShipmentDetailData().getUseDropshipper();
                if (useDropshipper != null) {
                    if (useDropshipper) {
                        cbDropshipper.setChecked(true);
                    } else {
                        checkDropshipperState(shipmentCartItemModel);
                    }
                } else {
                    checkDropshipperState(shipmentCartItemModel);
                }

                if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                    cbDropshipper.setChecked(false);
                    cbDropshipper.setEnabled(false);
                    llDropshipper.setOnClickListener(null);
                    tvDropshipper.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.logisticcart.R.color.font_disabled));
                    imgDropshipperInfo.setOnClickListener(v -> showBottomSheet(imgDropshipperInfo.getContext(),
                            imgDropshipperInfo.getContext().getString(R.string.title_dropshipper_army),
                            imgDropshipperInfo.getContext().getString(R.string.desc_dropshipper_army),
                            R.drawable.checkout_module_ic_dropshipper));
                } else {
                    tvDropshipper.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.abstraction.R.color.font_black_primary_70));
                    llDropshipper.setOnClickListener(getDropshipperClickListener());
                    imgDropshipperInfo.setOnClickListener(view -> showBottomSheet(imgDropshipperInfo.getContext(),
                            imgDropshipperInfo.getContext().getString(R.string.label_dropshipper_new),
                            imgDropshipperInfo.getContext().getString(R.string.label_dropshipper_info),
                            R.drawable.checkout_module_ic_dropshipper));
                }

                etShipperName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION &&
                                shipmentDataList.get(getAdapterPosition()) instanceof ShipmentCartItemModel) {
                            ShipmentCartItemModel data = ((ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition()));
                            if (data.getSelectedShipmentDetailData() != null) {
                                if (!TextUtils.isEmpty(charSequence)) {
                                    data.getSelectedShipmentDetailData().setDropshipperName(charSequence.toString());
                                    validateDropshipperName(data, charSequence, true);
                                    saveStateDebounceListener.onNeedToSaveState(data);
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                if (!TextUtils.isEmpty(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperName()) ||
                        !TextUtils.isEmpty(shipmentCartItemModel.getDropshiperName())) {
                    etShipperName.setText(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperName());
                } else {
                    etShipperName.setText("");
                }
                if (shipmentCartItemModel.isStateDropshipperHasError()) {
                    validateDropshipperName(shipmentCartItemModel, etShipperName.getText(), true);
                } else {
                    validateDropshipperName(shipmentCartItemModel, etShipperName.getText(), false);
                }
                etShipperName.setSelection(etShipperName.length());

                etShipperPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION &&
                                shipmentDataList.get(getAdapterPosition()) instanceof ShipmentCartItemModel) {
                            ShipmentCartItemModel data = ((ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition()));
                            if (data.getSelectedShipmentDetailData() != null) {
                                if (!TextUtils.isEmpty(charSequence)) {
                                    data.getSelectedShipmentDetailData().setDropshipperPhone(charSequence.toString());
                                    validateDropshipperPhone(data, charSequence, true);
                                    saveStateDebounceListener.onNeedToSaveState(data);
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                if (!TextUtils.isEmpty(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperPhone()) ||
                        !TextUtils.isEmpty(shipmentCartItemModel.getDropshiperPhone())) {
                    etShipperPhone.setText(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperPhone());
                } else {
                    etShipperPhone.setText("");
                }
                if (shipmentCartItemModel.isStateDropshipperHasError()) {
                    validateDropshipperPhone(shipmentCartItemModel, etShipperPhone.getText(), true);
                } else {
                    validateDropshipperPhone(shipmentCartItemModel, etShipperPhone.getText(), false);
                }
                etShipperPhone.setSelection(etShipperPhone.length());
            }
        }
    }

    private void checkDropshipperState(ShipmentCartItemModel shipmentCartItemModel) {
        if (!TextUtils.isEmpty(shipmentCartItemModel.getDropshiperName()) ||
                !TextUtils.isEmpty(shipmentCartItemModel.getDropshiperPhone())) {
            cbDropshipper.setChecked(true);
        } else {
            cbDropshipper.setChecked(false);
        }
    }

    private void validateDropshipperPhone(ShipmentCartItemModel shipmentCartItemModel, CharSequence charSequence, boolean fromTextWatcher) {
        Matcher matcher = phoneNumberRegexPattern.matcher(charSequence);
        if (charSequence.length() == 0 && fromTextWatcher) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_empty));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (etShipperPhone.getText().length() != 0 && !matcher.matches()) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_invalid));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (etShipperPhone.getText().length() != 0 && etShipperPhone.getText().length() < DROPSHIPPER_MIN_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (etShipperPhone.getText().length() != 0 && etShipperPhone.getText().length() > DROPSHIPPER_MAX_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else {
            textInputLayoutShipperPhone.setErrorEnabled(false);
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(true);
            mActionListener.onDataEnableToCheckout();
        }
    }

    private void validateDropshipperName(ShipmentCartItemModel shipmentCartItemModel, CharSequence charSequence, boolean fromTextWatcher) {
        if (charSequence.length() == 0 && fromTextWatcher) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_empty));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (etShipperName.getText().length() != 0 && etShipperName.getText().length() < DROPSHIPPER_MIN_NAME_LENGTH) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (etShipperName.getText().length() != 0 && etShipperName.getText().length() > DROPSHIPPER_MAX_NAME_LENGTH) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else {
            textInputLayoutShipperName.setErrorEnabled(false);
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(true);
            mActionListener.onDataEnableToCheckout();
        }
    }

    private void renderPrioritas(final ShipmentCartItemModel shipmentCartItemModel) {
        List<CartItemModel> cartItemModelList = new ArrayList<>(shipmentCartItemModel.getCartItemModels());
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();

        boolean renderOrderPriority = false;
        boolean isTradeInDropOff = mActionListener.isTradeInByDropOff();
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
            if (isTradeInDropOff) {
                renderOrderPriority = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null;
            } else {
                renderOrderPriority = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null;
            }
        }

        if (getAdapterPosition() != RecyclerView.NO_POSITION && renderOrderPriority) {
            if (!cartItemModelList.remove(FIRST_ELEMENT).isPreOrder()) {
                checkBoxPriority.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            isPriorityChecked = isChecked;
                            shipmentCartItemModel.getSelectedShipmentDetailData().setOrderPriority(isChecked);
                            mActionListener.onPriorityChecked(getAdapterPosition());
                            mActionListener.onNeedUpdateRequestData();
                        }
                    }
                });
            }


            SpannableString spanText = new SpannableString(tvPrioritasTicker.getResources().getString(R.string.label_hardcoded_courier_ticker));
            spanText.setSpan(new StyleSpan(Typeface.BOLD), 43, 52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            final CourierItemData courierItemData;
            if (isTradeInDropOff) {
                courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff();
            } else {
                courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            }
            boolean isCourierSelected = shipmentDetailData != null && courierItemData != null;

            if (isCourierSelected) {
                if (isCourierInstantOrSameday(courierItemData.getShipperId())) {
                    if (!shipmentCartItemModel.isOrderPrioritasDisable() && (courierItemData.getNow() && !shipmentCartItemModel.isProductIsPreorder())) {
                        tvPrioritasInfo.setText(courierItemData.getPriorityCheckboxMessage());
                        llPrioritas.setVisibility(View.VISIBLE);
                        llPrioritasTicker.setVisibility(View.VISIBLE);
                    } else {
                        llPrioritas.setVisibility(View.GONE);
                        llPrioritasTicker.setVisibility(View.GONE);
                    }
                } else {
                    hideAllTicker();
                }
            } else {
                hideAllTicker();
            }

            if (courierItemData != null && isPriorityChecked) {
                tvPrioritasTicker.setText(courierItemData.getPriorityWarningboxMessage());
            } else {
                tvPrioritasTicker.setText(spanText);
            }

        }
        imgPriorityTnc.setOnClickListener(v -> mActionListener.onPriorityTncClicker());
    }

    private void hideAllTicker() {
        llPrioritas.setVisibility(View.GONE);
        llPrioritasTicker.setVisibility(View.GONE);
    }

    private void renderInsurance(final ShipmentCartItemModel shipmentCartItemModel) {
        boolean renderInsurance = false;
        boolean isTradeInDropOff = mActionListener.isTradeInByDropOff();
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
            if (isTradeInDropOff) {
                renderInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null;
            } else {
                renderInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null;
            }
        }

        if (getAdapterPosition() != RecyclerView.NO_POSITION && renderInsurance) {
            cbInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(checked);
                        if (checked) {
                            mActionListener.onInsuranceCheckedForTrackingAnalytics();
                        }
                        mActionListener.onInsuranceChecked(getAdapterPosition());
                        mActionListener.onNeedUpdateRequestData();
                        if (saveStateDebounceListener != null) {
                            saveStateDebounceListener.onNeedToSaveState(shipmentCartItemModel);
                        }
                    }
                }
            });

            Boolean useInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance();
            if (useInsurance != null) {
                cbInsurance.setChecked(useInsurance);
            }

            final CourierItemData courierItemData;
            if (isTradeInDropOff) {
                courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff();
            } else {
                courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            }
            if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_MUST) {
                llInsurance.setVisibility(View.VISIBLE);
                llInsurance.setBackground(null);
                llInsurance.setOnClickListener(null);
                tvLabelInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_must_insurance);
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
                llInsurance.setBackground(null);
                llInsurance.setOnClickListener(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(false);
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tvLabelInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance);
                llInsurance.setVisibility(View.VISIBLE);
                cbInsuranceDisabled.setVisibility(View.GONE);
                cbInsurance.setVisibility(View.VISIBLE);
                TypedValue outValue = new TypedValue();
                llInsurance.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                llInsurance.setBackgroundResource(outValue.resourceId);
                llInsurance.setOnClickListener(getInsuranceClickListener());
                if (useInsurance == null) {
                    if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                        cbInsurance.setChecked(true);
                        shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(true);
                        mActionListener.onInsuranceChecked(getAdapterPosition());
                    } else if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                        cbInsurance.setChecked(shipmentCartItemModel.isInsurance());
                        shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(shipmentCartItemModel.isInsurance());
                    }
                }
            }

            if (!TextUtils.isEmpty(courierItemData.getInsuranceUsedInfo())) {
                if (TextUtils.isEmpty(courierItemData.getInsuranceUsedInfo())) {
                    imgInsuranceInfo.setVisibility(View.GONE);
                } else {
                    imgInsuranceInfo.setVisibility(View.VISIBLE);
                    imgInsuranceInfo.setOnClickListener(view -> showBottomSheet(imgInsuranceInfo.getContext(),
                            imgInsuranceInfo.getContext().getString(com.tokopedia.purchase_platform.common.R.string.title_bottomsheet_insurance),
                            courierItemData.getInsuranceUsedInfo(),
                            com.tokopedia.purchase_platform.common.R.drawable.ic_pp_insurance));
                }
            }

        }
    }

    private void renderError(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isError()) {
            String errorDescription = shipmentCartItemModel.getErrorDescription();
            if (!TextUtils.isEmpty(errorDescription)) {
                tickerError.setTickerTitle(shipmentCartItemModel.getErrorTitle());
                tickerError.setTextDescription(errorDescription);
            } else {
                tickerError.setTextDescription(shipmentCartItemModel.getErrorTitle());
            }
            tickerError.setTickerType(Ticker.TYPE_ERROR);
            tickerError.setTickerShape(Ticker.SHAPE_LOOSE);
            tickerError.setCloseButtonVisibility(View.GONE);
            tickerError.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.VISIBLE);

            flDisableContainer.setForeground(ContextCompat.getDrawable(flDisableContainer.getContext(), com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item));
            cbPPP.setEnabled(false);
            cbInsurance.setEnabled(false);
            llInsurance.setClickable(false);
            cbDropshipper.setEnabled(false);
            llDropshipper.setClickable(false);
            tvPPPMore.setClickable(false);
            etShipperName.setClickable(false);
            etShipperName.setFocusable(false);
            etShipperName.setFocusableInTouchMode(false);
            etShipperPhone.setClickable(false);
            etShipperPhone.setFocusable(false);
            etShipperPhone.setFocusableInTouchMode(false);
        } else {
            layoutError.setVisibility(View.GONE);
            tickerError.setVisibility(View.GONE);

            flDisableContainer.setForeground(ContextCompat.getDrawable(flDisableContainer.getContext(), com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item));
            cbPPP.setEnabled(true);
            cbInsurance.setEnabled(true);
            llInsurance.setClickable(true);
            cbDropshipper.setEnabled(true);
            llDropshipper.setClickable(true);
            tvPPPMore.setClickable(true);
            etShipperName.setClickable(true);
            etShipperName.setFocusable(true);
            etShipperName.setFocusableInTouchMode(true);
            etShipperPhone.setClickable(true);
            etShipperPhone.setFocusable(true);
            etShipperPhone.setFocusableInTouchMode(true);
        }
    }

    private void initSaveStateDebouncer() {
        compositeSubscription.add(Observable.create(new Observable.OnSubscribe<ShipmentCartItemModel>() {
            @Override
            public void call(final Subscriber<? super ShipmentCartItemModel> subscriber) {
                saveStateDebounceListener = new SaveStateDebounceListener() {
                    @Override
                    public void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel) {
                        subscriber.onNext(shipmentCartItemModel);
                    }
                };
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShipmentCartItemModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ShipmentCartItemModel shipmentCartItemModel) {
                        mActionListener.onNeedToSaveState(shipmentCartItemModel);
                    }
                }));
    }

    private void showBottomSheet(Context context, String title, String message, int image) {
        Tooltip tooltip = new Tooltip(context);
        tooltip.setTitle(title);
        tooltip.setDesc(message);
        tooltip.setTextButton(context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close));
        tooltip.setIcon(image);
        tooltip.getBtnAction().setOnClickListener(v -> tooltip.dismiss());
        tooltip.show();
    }

    private String getPriceFormat(TextView textViewLabel, TextView textViewPrice, long price) {
        if (price == 0) {
            textViewLabel.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.GONE);
            return "-";
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textViewPrice.setVisibility(View.VISIBLE);
            return Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(price, false));
        }
    }

    private View.OnClickListener getCostDetailOptionListener(final ShipmentCartItemModel shipmentCartItemModel) {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                shipmentCartItemModel.setStateDetailSubtotalViewExpanded(!shipmentCartItemModel.isStateDetailSubtotalViewExpanded());
                mActionListener.onNeedUpdateViewItem(getAdapterPosition());
                mActionListener.onSubTotalItemClicked(getAdapterPosition());
            }
        };
    }

    private View.OnClickListener getInsuranceClickListener() {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                cbInsurance.setChecked(!cbInsurance.isChecked());
                mActionListener.onInsuranceChecked(getAdapterPosition());
            }
        };
    }

    private View.OnClickListener getDropshipperClickListener() {
        return view -> cbDropshipper.setChecked(!cbDropshipper.isChecked());
    }

    private void initInnerRecyclerView(List<CartItemModel> cartItemList) {
        rvCartItem.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rvCartItem.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCartItem.setLayoutManager(layoutManager);

        ShipmentInnerProductListAdapter shipmentInnerProductListAdapter =
                new ShipmentInnerProductListAdapter(cartItemList, this);
        rvCartItem.setAdapter(shipmentInnerProductListAdapter);
    }

    private View.OnClickListener showAllProductListener(final ShipmentCartItemModel shipmentCartItemModel) {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                shipmentCartItemModel.setStateAllItemViewExpanded(!shipmentCartItemModel.isStateAllItemViewExpanded());
                mActionListener.onNeedUpdateViewItem(getAdapterPosition());
            }
        };
    }

    private void showShipmentWarning(CartItemModel cartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.getErrorMessage())) {
            tvErrorShipmentItemTitle.setText(cartItemModel.getErrorMessage());
            tvErrorShipmentItemTitle.setVisibility(View.VISIBLE);
            llShippingWarningContainer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(cartItemModel.getErrorMessageDescription())) {
                tvErrorShipmentItemDescription.setText(cartItemModel.getErrorMessageDescription());
                tvErrorShipmentItemDescription.setVisibility(View.VISIBLE);
            } else {
                tvErrorShipmentItemDescription.setVisibility(View.GONE);
            }
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
        tvProductOriginalPrice.setTextColor(nonActiveTextColor);
        tvOptionalNoteToSeller.setTextColor(nonActiveTextColor);
        tvItemCountAndWeight.setTextColor(nonActiveTextColor);
        textVariant.setTextColor(nonActiveTextColor);
        setImageFilterGrayScale();
    }

    private void setImageFilterGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter disabledColorFilter = new ColorMatrixColorFilter(matrix);
        ivProductImage.setColorFilter(disabledColorFilter);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
    }

    private void enableItemView() {
        tvProductName.setTextColor(ContextCompat.getColor(tvProductName.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_96));
        textVariant.setTextColor(ContextCompat.getColor(textVariant.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68));
        tvProductPrice.setTextColor(ContextCompat.getColor(tvProductPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_96));
        tvProductOriginalPrice.setTextColor(ContextCompat.getColor(tvProductOriginalPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68));
        tvItemCountAndWeight.setTextColor(ContextCompat.getColor(tvItemCountAndWeight.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(tvOptionalNoteToSeller.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_96));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        ivProductImage.setColorFilter(null);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

    private boolean isHavingPurchaseProtectionChecked() {
        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
            ShipmentCartItemModel data = ((ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition()));
            for (CartItemModel item : data.getCartItemModels()) {
                if (item.isProtectionOptIn()) return true;
            }
        }
        return false;
    }

    private boolean isCourierInstantOrSameday(int shipperId) {
        int[] ids = CourierConstant.INSTANT_SAMEDAY_COURIER;
        for (int id : ids) {
            if (shipperId == id) return true;
        }
        return false;
    }

    private interface SaveStateDebounceListener {

        void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    }

}