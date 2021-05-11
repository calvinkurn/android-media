package com.tokopedia.checkout.view.viewholder;

import android.animation.Animator;
import android.annotation.SuppressLint;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.utils.WeightFormatterUtil;
import com.tokopedia.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.adapter.ShipmentInnerProductListAdapter;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.iconunify.IconUnify;
import com.tokopedia.kotlin.extensions.view.TextViewExtKt;
import com.tokopedia.logisticCommon.data.constant.CourierConstant;
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherProductModel;
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.unifycomponents.ImageUnify;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifycomponents.TextFieldUnify;
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.utils.currency.CurrencyFormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
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
    private CheckboxUnify cbPPP;
    private RecyclerView rvCartItem;
    private Typography tvExpandOtherProduct;
    private IconUnify ivExpandOtherProduct;
    private RelativeLayout rlExpandOtherProduct;
    private IconUnify ivDetailOptionChevron;
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
    private CheckboxUnify cbInsurance;
    private ImageView imgInsuranceInfo;
    private LinearLayout llDropshipper;
    private CheckboxUnify cbDropshipper;
    private Typography tvDropshipper;
    private ImageView imgDropshipperInfo;
    private LinearLayout llDropshipperInfo;
    private TextFieldUnify textInputLayoutShipperName;
    private TextFieldUnify textInputLayoutShipperPhone;
    private View vSeparatorMultipleProductSameStore;
    private View vSeparatorBelowProduct;
    private Typography tvAdditionalFee;
    private Typography tvAdditionalFeePrice;
    private TextView tvLabelInsurance;
    private ImageView imgShopBadge;
    private LinearLayout llShippingOptionsContainer;
    private LinearLayout layoutWarningAndError;
    private TextView tvErrorShipmentItemTitle;
    private TextView tvErrorShipmentItemDescription;
    private FrameLayout flDisableContainer;
    private Ticker productTicker;
    private ConstraintLayout layoutTradeInShippingInfo;
    private Typography tvTradeInShippingPriceTitle;
    private Typography tvTradeInShippingPriceDetail;
    private Typography textVariant;
    private FlexboxLayout layoutProductInfo;

    private TextView tvTradeInLabel;

    private List<Object> shipmentDataList;
    private Pattern phoneNumberRegexPattern;
    private CompositeSubscription compositeSubscription;
    private SaveStateDebounceListener saveStateDebounceListener;
    private TextView tvFulfillName;
    private ImageUnify imgFulfillmentBadge;
    private Typography separatorFreeShipping;
    private ImageView imgFreeShipping;
    private Typography textOrderNumber;
    private Typography separatorPreOrder;
    private Label labelPreOrder;
    private Typography separatorIncidentShopLevel;
    private Label labelIncidentShopLevel;

    // order prioritas
    private CheckboxUnify checkBoxPriority;
    private Typography tvPrioritasTicker;
    private LinearLayout llPrioritasTicker;
    private RelativeLayout llPrioritas;
    private Typography tvPrioritasFee;
    private Typography tvPrioritasFeePrice;
    private ImageView imgPriorityTnc;
    private Typography tvPrioritasInfo;
    private boolean isPriorityChecked = false;

    // Shipping Experience
    private LinearLayout llShippingExperienceStateLoading;
    private FrameLayout containerShippingExperience;
    private ConstraintLayout layoutStateNoSelectedShipping;
    private ConstraintLayout layoutStateHasSelectedNormalShipping;
    private ConstraintLayout layoutStateFailedShipping;
    private ConstraintLayout layoutStateHasErrorShipping;
    private Typography labelErrorShippingTitle;
    private Typography labelErrorShippingDescription;
    private ConstraintLayout layoutStateHasSelectedSingleShipping;
    private Typography labelSelectedShippingDuration;
    private ImageView iconChevronChooseDuration;
    private Typography labelSelectedShippingCourier;
    private Typography labelSelectedShippingPriceorDuration;
    private Typography labelDescCourier;
    private Typography labelDescCourierTnc;
    private ImageView iconChevronChooseCourier;
    private ConstraintLayout layoutStateHasSelectedFreeShipping;
    private Typography labelSelectedFreeShipping;
    private Typography labelFreeShippingCourierName;
    private Typography labelFreeShippingOriginalPrice;
    private Typography labelFreeShippingDiscountedPrice;
    private Typography labelFreeShippingEtaText;
    private Typography labelFreeShippingOriginalEtaPrice;
    private Typography labelFreeShippingDiscountedEtaPrice;
    private ImageView imageMerchantVoucher;
    private IconUnify mIconTooltip;
    private Typography mPricePerProduct;

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
        tvPPPPrice = itemView.findViewById(R.id.text_protection_desc);
        cbPPP = itemView.findViewById(R.id.checkbox_ppp);
        mIconTooltip = itemView.findViewById(R.id.icon_tooltip);
        mPricePerProduct = itemView.findViewById(R.id.text_item_per_product);
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
        imgInsuranceInfo = itemView.findViewById(R.id.img_insurance_info);
        llDropshipper = itemView.findViewById(R.id.ll_dropshipper);
        cbDropshipper = itemView.findViewById(R.id.cb_dropshipper);
        tvDropshipper = itemView.findViewById(R.id.label_dropshipper);
        imgDropshipperInfo = itemView.findViewById(R.id.img_dropshipper_info);
        llDropshipperInfo = itemView.findViewById(R.id.ll_dropshipper_info);
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
        tvErrorShipmentItemTitle = itemView.findViewById(R.id.tv_error_shipment_item_title);
        tvErrorShipmentItemDescription = itemView.findViewById(R.id.tv_error_shipment_item_description);
        flDisableContainer = itemView.findViewById(R.id.fl_disable_container);
        imgFreeShipping = itemView.findViewById(R.id.img_free_shipping);
        separatorFreeShipping = itemView.findViewById(R.id.separator_free_shipping);
        layoutTradeInShippingInfo = itemView.findViewById(R.id.layout_trade_in_shipping_info);
        tvTradeInShippingPriceTitle = itemView.findViewById(R.id.tv_trade_in_shipping_price_title);
        tvTradeInShippingPriceDetail = itemView.findViewById(R.id.tv_trade_in_shipping_price_detail);
        productTicker = itemView.findViewById(R.id.product_ticker);
        textOrderNumber = itemView.findViewById(R.id.text_order_number);
        labelPreOrder = itemView.findViewById(R.id.label_pre_order);
        separatorPreOrder = itemView.findViewById(R.id.separator_pre_order);
        labelIncidentShopLevel = itemView.findViewById(R.id.label_incident_shop_level);
        separatorIncidentShopLevel = itemView.findViewById(R.id.separator_incident_shop_level);
        textVariant = itemView.findViewById(R.id.text_variant);
        layoutProductInfo = itemView.findViewById(R.id.layout_product_info);

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
        imgFulfillmentBadge = itemView.findViewById(R.id.iu_image_fulfill);
        tvTradeInLabel = itemView.findViewById(R.id.tv_trade_in_label);

        // Shipping Experience
        llShippingExperienceStateLoading = itemView.findViewById(R.id.ll_shipping_experience_state_loading);
        containerShippingExperience = itemView.findViewById(R.id.container_shipping_experience);
        layoutStateNoSelectedShipping = itemView.findViewById(R.id.layout_state_no_selected_shipping);
        layoutStateHasSelectedNormalShipping = itemView.findViewById(R.id.layout_state_has_selected_normal_shipping);
        layoutStateFailedShipping = itemView.findViewById(R.id.layout_state_failed_shipping);
        layoutStateHasErrorShipping = itemView.findViewById(R.id.layout_state_has_error_shipping);
        labelErrorShippingTitle = itemView.findViewById(R.id.label_error_shipping_title);
        labelErrorShippingDescription = itemView.findViewById(R.id.label_error_shipping_description);
        layoutStateHasSelectedSingleShipping = itemView.findViewById(R.id.layout_state_has_selected_single_shipping);
        labelSelectedShippingDuration = itemView.findViewById(R.id.label_selected_shipping_duration);
        iconChevronChooseDuration = itemView.findViewById(R.id.icon_chevron_choose_duration);
        labelSelectedShippingCourier = itemView.findViewById(R.id.label_selected_shipping_courier);
        labelSelectedShippingPriceorDuration = itemView.findViewById(R.id.label_selected_shipping_price_or_duration);
        labelDescCourier = itemView.findViewById(R.id.label_description_courier);
        labelDescCourierTnc = itemView.findViewById(R.id.label_description_courier_tnc);
        iconChevronChooseCourier = itemView.findViewById(R.id.icon_chevron_choose_courier);
        layoutStateHasSelectedFreeShipping = itemView.findViewById(R.id.layout_state_has_selected_free_shipping);
        labelSelectedFreeShipping = itemView.findViewById(R.id.label_selected_free_shipping);
        labelFreeShippingCourierName = itemView.findViewById(R.id.label_free_shipping_courier_name);
        labelFreeShippingOriginalPrice = itemView.findViewById(R.id.label_free_shipping_original_price);
        labelFreeShippingDiscountedPrice = itemView.findViewById(R.id.label_free_shipping_discounted_price);
        labelFreeShippingEtaText = itemView.findViewById(R.id.label_free_shipping_eta);
        labelFreeShippingOriginalEtaPrice = itemView.findViewById(R.id.label_free_shipping_original_price_eta_partial);
        labelFreeShippingDiscountedEtaPrice = itemView.findViewById(R.id.label_free_shipping_discounted_price_eta_partial);
        imageMerchantVoucher = itemView.findViewById(R.id.img_mvc);

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
        if (!TextUtils.isEmpty(model.getShopLocation())) {
            if (model.isFulfillment() && !TextUtils.isEmpty(model.getFulfillmentBadgeUrl())) {
                ImageHandler.loadImageWithoutPlaceholderAndError(imgFulfillmentBadge, model.getFulfillmentBadgeUrl());
                imgFulfillmentBadge.setVisibility(View.VISIBLE);
            } else {
                imgFulfillmentBadge.setVisibility(View.GONE);
            }
            tvFulfillName.setVisibility(View.VISIBLE);
            tvFulfillName.setText(model.getShopLocation());
        } else {
            imgFulfillmentBadge.setVisibility(View.GONE);
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
        if (shipmentCartItemModel.getOrderNumber() > 0) {
            String orderlabel = String.format(itemView.getContext().getString(R.string.label_order_counter), shipmentCartItemModel.getOrderNumber());
            textOrderNumber.setText(orderlabel);
            textOrderNumber.setVisibility(View.VISIBLE);
        } else {
            textOrderNumber.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shipmentCartItemModel.getPreOrderInfo())) {
            labelPreOrder.setText(shipmentCartItemModel.getPreOrderInfo());
            labelPreOrder.setVisibility(View.VISIBLE);
            separatorPreOrder.setVisibility(View.VISIBLE);
        } else {
            labelPreOrder.setVisibility(View.GONE);
            separatorPreOrder.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shipmentCartItemModel.getFreeShippingBadgeUrl())) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFreeShipping, shipmentCartItemModel.getFreeShippingBadgeUrl()
            );
            if (shipmentCartItemModel.isFreeShippingExtra()) {
                imgFreeShipping.setContentDescription(itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_boe));
            } else {
                imgFreeShipping.setContentDescription(itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo));
            }
            imgFreeShipping.setVisibility(View.VISIBLE);
            separatorFreeShipping.setVisibility(View.VISIBLE);
        } else {
            imgFreeShipping.setVisibility(View.GONE);
            separatorFreeShipping.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(shipmentCartItemModel.getShopAlertMessage())) {
            labelIncidentShopLevel.setText(shipmentCartItemModel.getShopAlertMessage());
            labelIncidentShopLevel.setVisibility(View.VISIBLE);
            separatorIncidentShopLevel.setVisibility(View.VISIBLE);
        } else {
            labelIncidentShopLevel.setVisibility(View.GONE);
            separatorIncidentShopLevel.setVisibility(View.GONE);
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
                String shopType = itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_shop_type_power_merchant);
                if (shipmentCartItemModel.isOfficialStore()) {
                    shopType = itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_shop_type_official_store);
                }
                imgShopBadge.setContentDescription(itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, shopType));
            }
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        String shopName = shipmentCartItemModel.getShopName();

        tvShopName.setText(shopName);
    }

    @SuppressLint("StringFormatInvalid")
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
        renderProductProperties(cartItemModel);
    }

    @SuppressLint("SetTextI18n")
    private void renderProductProperties(CartItemModel cartItemModel) {
        List<String> productInformationList = cartItemModel.getProductInformation();
        if (productInformationList != null && !productInformationList.isEmpty()) {
            layoutProductInfo.removeAllViews();
            for (int i = 0; i < productInformationList.size(); i++) {
                Typography productInfo = new Typography(itemView.getContext());
                productInfo.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                productInfo.setType(Typography.SMALL);
                if (layoutProductInfo.getChildCount() > 0) {
                    productInfo.setText(", " + productInformationList.get(i));
                } else {
                    productInfo.setText(productInformationList.get(i));
                }
                layoutProductInfo.addView(productInfo);
            }
            layoutProductInfo.setVisibility(View.VISIBLE);
        } else {
            layoutProductInfo.setVisibility(View.GONE);
        }
    }

    private void renderProductPrice(CartItemModel cartItemModel) {
        tvProductPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                (long) cartItemModel.getPrice(), false)));
        int dp4 = tvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_4);
        int dp10 = tvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_10);
        if (cartItemModel.getOriginalPrice() > 0) {
            tvProductPrice.setPadding(dp4, dp4, 0, 0);
            tvProductOriginalPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                    cartItemModel.getOriginalPrice(), false
            )));
            tvProductOriginalPrice.setPaintFlags(tvProductOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvProductOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            tvProductPrice.setPadding(dp10, dp4, 0, 0);
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
            mIconTooltip.setOnClickListener(view -> mActionListener.navigateToProtectionMore(cartItemModel.getProtectionLinkUrl()));
            tvPPPLinkText.setText(cartItemModel.getProtectionTitle());
            tvPPPPrice.setText(cartItemModel.getProtectionSubTitle());
            mPricePerProduct.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat((long) cartItemModel.getProtectionPricePerProduct(), false)));

            if (cartItemModel.isProtectionCheckboxDisabled()) {
                cbPPP.setEnabled(false);
                cbPPP.setChecked(true);
                cbPPP.skipAnimation();
            } else {
                cbPPP.setEnabled(true);
                cbPPP.setChecked(cartItemModel.isProtectionOptIn());
                cbPPP.skipAnimation();
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

    @SuppressLint("StringFormatInvalid")
    private void renderOtherCartItems(ShipmentCartItemModel shipmentItem, List<CartItemModel> cartItemModels) {
        rlExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem));
        initInnerRecyclerView(cartItemModels);
        if (shipmentItem.isStateAllItemViewExpanded()) {
            rvCartItem.setVisibility(View.VISIBLE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(R.string.label_hide_other_item_new);
            ivExpandOtherProduct.setImage(IconUnify.CHEVRON_UP, null, null, null, null);
        } else {
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(String.format(tvExpandOtherProduct.getContext().getString(R.string.label_other_item_count_format),
                    String.valueOf(cartItemModels.size())));
            tvExpandOtherProduct.setTextColor(ContextCompat.getColor(tvExpandOtherProduct.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
            ivExpandOtherProduct.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null);
        }
    }

    private void renderShipping(ShipmentCartItemModel shipmentCartItemModel,
                                RecipientAddressModel currentAddress,
                                RatesDataConverter ratesDataConverter) {
        layoutTradeInShippingInfo.setVisibility(View.GONE);

        if (shipmentCartItemModel.isError()) {
            renderErrorCourierState();
            return;
        }

        CourierItemData selectedCourierItemData = null;
        boolean isTradeInDropOff = mActionListener.isTradeInByDropOff();

        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
            if (isTradeInDropOff && shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                selectedCourierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff();
            } else if (!isTradeInDropOff && shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                selectedCourierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            }
        }

        if (selectedCourierItemData != null) {
            // Has select shipping
            renderSelectedCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData);
        } else {
            // Has not select shipping
            prepareLoadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, isTradeInDropOff);
        }
    }

    private void renderErrorCourierState() {
        llShippingOptionsContainer.setVisibility(View.VISIBLE);
        layoutStateNoSelectedShipping.setVisibility(View.GONE);
        layoutStateHasSelectedSingleShipping.setVisibility(View.GONE);
        layoutStateHasSelectedFreeShipping.setVisibility(View.GONE);
        layoutStateHasSelectedNormalShipping.setVisibility(View.GONE);
        layoutStateFailedShipping.setVisibility(View.GONE);

        labelErrorShippingTitle.setText("Pengiriman tidak tersedia");
        labelErrorShippingDescription.setText("Error yang aneh dan panjang sekali, Error yang aneh dan panjang sekali, Error yang aneh dan panjang sekali");
        layoutStateHasErrorShipping.setVisibility(View.VISIBLE);

        llShippingExperienceStateLoading.setVisibility(View.GONE);
        containerShippingExperience.setVisibility(View.VISIBLE);
        containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey);
    }

    private void renderSelectedCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, CourierItemData selectedCourierItemData) {
        llShippingOptionsContainer.setVisibility(View.VISIBLE);
        layoutStateNoSelectedShipping.setVisibility(View.GONE);

        llShippingExperienceStateLoading.setVisibility(View.GONE);
        containerShippingExperience.setVisibility(View.VISIBLE);
        containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey);
        if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
            // Is free ongkir shipping
            renderFreeShippingCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData);
        } else {
            // Is normal shipping
            renderNormalShippingCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData);
        }
    }

    private void renderNormalShippingCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, CourierItemData selectedCourierItemData) {
        layoutStateHasSelectedFreeShipping.setVisibility(View.GONE);
        layoutStateFailedShipping.setVisibility(View.GONE);
        layoutStateHasErrorShipping.setVisibility(View.GONE);
        layoutStateHasSelectedSingleShipping.setVisibility(View.GONE);
        layoutStateHasSelectedNormalShipping.setVisibility(View.VISIBLE);

        TextViewExtKt.setTextAndContentDescription(labelSelectedShippingDuration, selectedCourierItemData.getEstimatedTimeDelivery(), R.string.content_desc_label_selected_shipping_duration);
        labelSelectedShippingDuration.setOnClickListener(
                getOnChangeDurationClickListener(shipmentCartItemModel, currentAddress)
        );
        iconChevronChooseDuration.setOnClickListener(
                getOnChangeDurationClickListener(shipmentCartItemModel, currentAddress)
        );

        String courierName = selectedCourierItemData.getName() + " (" +
                Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                        selectedCourierItemData.getShipperPrice(), false
                )) + ")";

        if (selectedCourierItemData.getEtaErrorCode() == 0 && !selectedCourierItemData.getEtaText().isEmpty()) {
            TextViewExtKt.setTextAndContentDescription(labelSelectedShippingCourier, courierName, R.string.content_desc_label_selected_shipping_courier);
            labelSelectedShippingPriceorDuration.setText(selectedCourierItemData.getEtaText());
        } else if (selectedCourierItemData.getEtaErrorCode() == 0 && selectedCourierItemData.getEtaText().isEmpty()) {
            TextViewExtKt.setTextAndContentDescription(labelSelectedShippingCourier, courierName, R.string.content_desc_label_selected_shipping_courier);
            labelSelectedShippingPriceorDuration.setText(R.string.estimasi_tidak_tersedia);
        } else {
            TextViewExtKt.setTextAndContentDescription(labelSelectedShippingCourier, selectedCourierItemData.getName(), R.string.content_desc_label_selected_shipping_courier);
            labelSelectedShippingPriceorDuration.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                    selectedCourierItemData.getShipperPrice(), false
            )));
        }

        labelSelectedShippingPriceorDuration.setOnClickListener(
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
        MerchantVoucherProductModel merchantVoucherProductModel = selectedCourierItemData.getMerchantVoucherProductModel();

        if (merchantVoucherProductModel != null && merchantVoucherProductModel.isMvc() == 1) {
            imageMerchantVoucher.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(imageMerchantVoucher, merchantVoucherProductModel.getMvcLogo());
        } else {
            imageMerchantVoucher.setVisibility(View.GONE);
        }

        if (ontimeDelivery != null && ontimeDelivery.getAvailable()) {
            // On time delivery guarantee
            labelDescCourier.setText(ontimeDelivery.getTextLabel());
            labelDescCourierTnc.setOnClickListener(view -> {
                mActionListener.onOntimeDeliveryClicked(ontimeDelivery.getUrlDetail());
            });
            labelDescCourier.setVisibility(View.VISIBLE);
            labelDescCourierTnc.setVisibility(View.VISIBLE);
        } else if (codProductData != null && codProductData.isCodAvailable() == 1) {
            // Cash on delivery
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

    private void renderFreeShippingCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, CourierItemData selectedCourierItemData) {
        layoutStateHasSelectedNormalShipping.setVisibility(View.GONE);
        layoutStateFailedShipping.setVisibility(View.GONE);
        layoutStateHasErrorShipping.setVisibility(View.GONE);
        layoutStateHasSelectedSingleShipping.setVisibility(View.GONE);
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

        /*With ETA*/
        if (selectedCourierItemData.getEtaErrorCode() == 0) {
            labelSelectedFreeShipping.setText(selectedCourierItemData.getPromoTitle());
            if (selectedCourierItemData.getDiscountedRate() == 0) {
                // Free Shipping Price
                labelFreeShippingOriginalPrice.setVisibility(View.GONE);
                labelFreeShippingDiscountedPrice.setVisibility(View.GONE);
            } else if (selectedCourierItemData.getDiscountedRate() > 0) {
                // Discounted Shipping Price
                labelFreeShippingOriginalEtaPrice.setVisibility(View.VISIBLE);
                String originalEtaPrice = "(" + Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                        selectedCourierItemData.getShippingRate(), false
                ));
                labelFreeShippingOriginalEtaPrice.setText(originalEtaPrice);
                labelFreeShippingOriginalPrice.setVisibility(View.GONE);
                labelFreeShippingOriginalEtaPrice.setPaintFlags(labelFreeShippingOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                String DiscountedEtaPrice = Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                        selectedCourierItemData.getDiscountedRate(), false
                )) + ")";
                labelFreeShippingDiscountedEtaPrice.setText(DiscountedEtaPrice);
                labelFreeShippingDiscountedPrice.setVisibility(View.GONE);

            }
            labelFreeShippingEtaText.setVisibility(View.VISIBLE);
            if (!selectedCourierItemData.getEtaText().isEmpty()) {
                labelFreeShippingEtaText.setText(selectedCourierItemData.getEtaText());
            } else {
                labelFreeShippingEtaText.setText(R.string.estimasi_tidak_tersedia);
            }

            /*Without ETA*/
        } else {
            // Change duration to promo title after promo is applied
            labelSelectedFreeShipping.setText(selectedCourierItemData.getPromoTitle());
            if (selectedCourierItemData.getDiscountedRate() == 0) {
                // Free Shipping Price
                labelFreeShippingEtaText.setVisibility(View.GONE);
                labelFreeShippingOriginalPrice.setVisibility(View.GONE);
                labelFreeShippingDiscountedPrice.setVisibility(View.GONE);
            } else if (selectedCourierItemData.getDiscountedRate() > 0) {
                // Discounted Shipping Price
                labelFreeShippingOriginalPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                        selectedCourierItemData.getShippingRate(), false
                )));
                labelFreeShippingOriginalPrice.setPaintFlags(labelFreeShippingOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                labelFreeShippingDiscountedPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                        selectedCourierItemData.getDiscountedRate(), false
                )));
                labelFreeShippingOriginalPrice.setVisibility(View.VISIBLE);
                labelFreeShippingDiscountedPrice.setVisibility(View.VISIBLE);
                labelFreeShippingEtaText.setVisibility(View.GONE);
            }
        }
    }

    private void prepareLoadCourierState(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, RatesDataConverter ratesDataConverter, boolean isTradeInDropOff) {
        layoutStateHasSelectedFreeShipping.setVisibility(View.GONE);
        layoutStateHasSelectedNormalShipping.setVisibility(View.GONE);
        llShippingOptionsContainer.setVisibility(View.GONE);

        if (isTradeInDropOff) {
            renderNoSelectedCourier(shipmentCartItemModel, currentAddress, SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
            loadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
        } else {
            renderNoSelectedCourier(shipmentCartItemModel, currentAddress, SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE);
            loadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE);
        }
    }

    private void renderNoSelectedCourierNormalShipping(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress) {
        layoutTradeInShippingInfo.setVisibility(View.GONE);
        layoutStateNoSelectedShipping.setVisibility(View.VISIBLE);
        layoutStateNoSelectedShipping.setOnClickListener(
                getOnChangeDurationClickListener(shipmentCartItemModel, currentAddress)
        );
        containerShippingExperience.setVisibility(View.VISIBLE);
    }

    private void renderNoSelectedCourierTradeInDropOff(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, boolean hasSelectTradeInLocation) {
        if (hasSelectTradeInLocation) {
            renderNoSelectedCourierNormalShipping(shipmentCartItemModel, currentAddress);
        } else {
            layoutTradeInShippingInfo.setVisibility(View.VISIBLE);
            layoutStateNoSelectedShipping.setVisibility(View.GONE);
            tvTradeInShippingPriceTitle.setVisibility(View.VISIBLE);
            tvTradeInShippingPriceDetail.setVisibility(View.VISIBLE);
            containerShippingExperience.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener getOnChangeCourierClickListener(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress) {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION && !shipmentCartItemModel.isDisableChangeCourier()) {
                mActionListener.onChangeShippingCourier(currentAddress, shipmentCartItemModel, getAdapterPosition());
            }
        };
    }

    private View.OnClickListener getOnChangeDurationClickListener(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress) {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION && !shipmentCartItemModel.isDisableChangeCourier()) {
                mActionListener.onChangeShippingDuration(shipmentCartItemModel, currentAddress, getAdapterPosition());
            }
        };
    }

    private void loadCourierState(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  RatesDataConverter ratesDataConverter,
                                  int saveStateType) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        if (shipmentCartItemModel.isStateLoadingCourierState()) {
            renderLoadingCourierState();
        } else {
            boolean hasLoadCourier = false;
            llShippingExperienceStateLoading.setVisibility(View.GONE);
            switch (saveStateType) {
                case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                    hasLoadCourier = shipmentDetailData != null && shipmentDetailData.getSelectedCourierTradeInDropOff() != null;
                    break;
                case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                    hasLoadCourier = shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null;
                    break;
            }

            if (shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0) {
                if (!hasLoadCourier) {
                    ShipmentDetailData tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
                            shipmentCartItemModel, recipientAddressModel);

                    boolean hasLoadCourierState;
                    if (saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF) {
                        hasLoadCourierState = shipmentCartItemModel.isStateHasLoadCourierTradeInDropOffState();
                    } else {
                        hasLoadCourierState = shipmentCartItemModel.isStateHasLoadCourierState();
                    }

                    if (!hasLoadCourierState) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            loadCourierStateData(shipmentCartItemModel, saveStateType, tmpShipmentDetailData, position);
                        }
                    } else {
                        renderNoSelectedCourier(shipmentCartItemModel, recipientAddressModel, saveStateType);
                    }
                }
            } else {
                renderNoSelectedCourier(shipmentCartItemModel, recipientAddressModel, saveStateType);
            }
        }
    }

    private void renderNoSelectedCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, int saveStateType) {
        switch (saveStateType) {
            case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                boolean hasSelectTradeInLocation = mActionListener.hasSelectTradeInLocation();
                renderNoSelectedCourierTradeInDropOff(shipmentCartItemModel, recipientAddressModel, hasSelectTradeInLocation);
                break;
            case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                renderNoSelectedCourierNormalShipping(shipmentCartItemModel, recipientAddressModel);
                break;
        }
    }

    private void loadCourierStateData(ShipmentCartItemModel shipmentCartItemModel, int saveStateType, ShipmentDetailData tmpShipmentDetailData, int position) {
        mActionListener.onLoadShippingState(shipmentCartItemModel.getShippingId(),
                shipmentCartItemModel.getSpId(), position, tmpShipmentDetailData,
                shipmentCartItemModel, shipmentCartItemModel.getShopShipmentList(),
                saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
        shipmentCartItemModel.setStateLoadingCourierState(true);
        llShippingExperienceStateLoading.setVisibility(View.VISIBLE);
        containerShippingExperience.setVisibility(View.GONE);
        switch (saveStateType) {
            case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                shipmentCartItemModel.setStateHasLoadCourierTradeInDropOffState(true);
                tvTradeInShippingPriceTitle.setVisibility(View.GONE);
                tvTradeInShippingPriceDetail.setVisibility(View.GONE);
                break;
            case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                shipmentCartItemModel.setStateHasLoadCourierState(true);
                break;
        }
    }

    private void renderLoadingCourierState() {
        llShippingExperienceStateLoading.setVisibility(View.VISIBLE);
        containerShippingExperience.setVisibility(View.GONE);
        tvTradeInShippingPriceTitle.setVisibility(View.GONE);
        tvTradeInShippingPriceDetail.setVisibility(View.GONE);
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
            ivDetailOptionChevron.setImage(IconUnify.CHEVRON_UP, null, null, null, null);
        } else {
            rlShipmentCost.setVisibility(View.GONE);
            ivDetailOptionChevron.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null);
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
        @SuppressLint("DefaultLocale") String totalPPPItemLabel = String.format("Proteksi Produk (%d Barang)", totalPurchaseProtectionItem);

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
        TextViewExtKt.setTextAndContentDescription(tvSubTotalPrice, subTotalPrice == 0 ? "-" : Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(subTotalPrice, false)), R.string.content_desc_tv_sub_total_price);
        TextViewExtKt.setTextAndContentDescription(tvTotalItemPrice, totalItemPrice == 0 ? "-" : getPriceFormat(tvTotalItem, tvTotalItemPrice, totalItemPrice), R.string.content_desc_tv_total_item_price_subtotal);
        tvTotalItem.setText(totalItemLabel);
        tvShippingFee.setText(shippingFeeLabel);
        TextViewExtKt.setTextAndContentDescription(tvShippingFeePrice, getPriceFormat(tvShippingFee, tvShippingFeePrice, shippingPrice), R.string.content_desc_tv_shipping_fee_price_subtotal);
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                voucherLogisticItemUiModel != null) {
            if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getDiscountedRate() == 0) {
                TextViewExtKt.setTextAndContentDescription(tvShippingFeePrice, tvShippingFeePrice.getContext().getString(com.tokopedia.purchase_platform.common.R.string.label_free_shipping), R.string.content_desc_tv_shipping_fee_price_subtotal);
            } else {
                TextViewExtKt.setTextAndContentDescription(tvShippingFeePrice, getPriceFormat(tvShippingFee, tvShippingFeePrice, shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getDiscountedRate()), R.string.content_desc_tv_shipping_fee_price_subtotal);
            }
        }
        TextViewExtKt.setTextAndContentDescription(tvInsuranceFeePrice, getPriceFormat(tvInsuranceFee, tvInsuranceFeePrice, insurancePrice), R.string.content_desc_tv_insurance_fee_price_subtotal);
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
                    textInputLayoutShipperName.getTextFieldInput().setText("");
                    textInputLayoutShipperPhone.getTextFieldInput().setText("");
                } else {
                    llDropshipper.setVisibility(View.VISIBLE);
                }

                cbDropshipper.setOnCheckedChangeListener((compoundButton, checked) -> {
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
                            textInputLayoutShipperName.getTextFieldInput().setText(data.getDropshiperName());
                            textInputLayoutShipperPhone.getTextFieldInput().setText(data.getDropshiperPhone());
                            data.getSelectedShipmentDetailData().setDropshipperName(data.getDropshiperName());
                            data.getSelectedShipmentDetailData().setDropshipperPhone(data.getDropshiperPhone());
                            llDropshipperInfo.setVisibility(View.VISIBLE);
                            mActionListener.onDropshipCheckedForTrackingAnalytics();
                        } else {
                            textInputLayoutShipperName.getTextFieldInput().setText("");
                            textInputLayoutShipperPhone.getTextFieldInput().setText("");
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
                    cbDropshipper.setEnabled(false);
                    cbDropshipper.setChecked(false);
                    llDropshipper.setOnClickListener(null);
                    tvDropshipper.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_20));
                    imgDropshipperInfo.setOnClickListener(v -> showBottomSheet(imgDropshipperInfo.getContext(),
                            imgDropshipperInfo.getContext().getString(R.string.title_dropshipper_army),
                            imgDropshipperInfo.getContext().getString(R.string.desc_dropshipper_army),
                            R.drawable.checkout_module_ic_dropshipper));
                } else {
                    cbDropshipper.setEnabled(true);
                    tvDropshipper.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                    llDropshipper.setOnClickListener(getDropshipperClickListener());
                    imgDropshipperInfo.setOnClickListener(view -> showBottomSheet(imgDropshipperInfo.getContext(),
                            imgDropshipperInfo.getContext().getString(R.string.label_dropshipper_new),
                            imgDropshipperInfo.getContext().getString(R.string.label_dropshipper_info),
                            R.drawable.checkout_module_ic_dropshipper));
                }

                textInputLayoutShipperName.getTextFieldInput().addTextChangedListener(new TextWatcher() {
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
                    textInputLayoutShipperName.getTextFieldInput().setText(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperName());
                } else {
                    textInputLayoutShipperName.getTextFieldInput().setText("");
                }
                if (shipmentCartItemModel.isStateDropshipperHasError()) {
                    validateDropshipperName(shipmentCartItemModel, textInputLayoutShipperName.getTextFieldInput().getText(), true);
                } else {
                    validateDropshipperName(shipmentCartItemModel, textInputLayoutShipperName.getTextFieldInput().getText(), false);
                }
                textInputLayoutShipperName.getTextFieldInput().setSelection(textInputLayoutShipperName.getTextFieldInput().length());

                textInputLayoutShipperPhone.getTextFieldInput().addTextChangedListener(new TextWatcher() {
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
                    textInputLayoutShipperPhone.getTextFieldInput().setText(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperPhone());
                } else {
                    textInputLayoutShipperPhone.getTextFieldInput().setText("");
                }
                if (shipmentCartItemModel.isStateDropshipperHasError()) {
                    validateDropshipperPhone(shipmentCartItemModel, textInputLayoutShipperPhone.getTextFieldInput().getText(), true);
                } else {
                    validateDropshipperPhone(shipmentCartItemModel, textInputLayoutShipperPhone.getTextFieldInput().getText(), false);
                }
                textInputLayoutShipperPhone.getTextFieldInput().setSelection(textInputLayoutShipperPhone.getTextFieldInput().length());
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
            textInputLayoutShipperPhone.setError(true);
            textInputLayoutShipperPhone.setMessage(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_empty));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (textInputLayoutShipperPhone.getTextFieldInput().getText().length() != 0 && !matcher.matches()) {
            textInputLayoutShipperPhone.setError(true);
            textInputLayoutShipperPhone.setMessage(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_invalid));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (textInputLayoutShipperPhone.getTextFieldInput().getText().length() != 0 &&
                textInputLayoutShipperPhone.getTextFieldInput().getText().length() < DROPSHIPPER_MIN_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(true);
            textInputLayoutShipperPhone.setMessage(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (textInputLayoutShipperPhone.getTextFieldInput().getText().length() != 0 &&
                textInputLayoutShipperPhone.getTextFieldInput().getText().length() > DROPSHIPPER_MAX_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(true);
            textInputLayoutShipperPhone.setMessage(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else {
            textInputLayoutShipperPhone.setError(false);
            textInputLayoutShipperPhone.setMessage("");
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(true);
            mActionListener.onDataEnableToCheckout();
        }
    }

    private void validateDropshipperName(ShipmentCartItemModel shipmentCartItemModel, CharSequence charSequence, boolean fromTextWatcher) {
        if (charSequence.length() == 0 && fromTextWatcher) {
            textInputLayoutShipperName.setError(true);
            textInputLayoutShipperName.setMessage(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_empty));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (textInputLayoutShipperName.getTextFieldInput().getText().length() != 0 &&
                textInputLayoutShipperName.getTextFieldInput().getText().length() < DROPSHIPPER_MIN_NAME_LENGTH) {
            textInputLayoutShipperName.setError(true);
            textInputLayoutShipperName.setMessage(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else if (textInputLayoutShipperName.getTextFieldInput().getText().length() != 0 &&
                textInputLayoutShipperName.getTextFieldInput().getText().length() > DROPSHIPPER_MAX_NAME_LENGTH) {
            textInputLayoutShipperName.setError(true);
            textInputLayoutShipperName.setMessage(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onDataDisableToCheckout(null);
        } else {
            textInputLayoutShipperName.setError(false);
            textInputLayoutShipperName.setMessage("");
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
                checkBoxPriority.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        isPriorityChecked = isChecked;
                        shipmentCartItemModel.getSelectedShipmentDetailData().setOrderPriority(isChecked);
                        mActionListener.onPriorityChecked(getAdapterPosition());
                        mActionListener.onNeedUpdateRequestData();
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

            if (isCourierSelected && !shipmentCartItemModel.isError()) {
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
        } else {
            hideAllTicker();
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
            cbInsurance.setOnCheckedChangeListener((compoundButton, checked) -> {
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
                cbInsurance.setEnabled(false);
                cbInsurance.setChecked(true);
                if (useInsurance == null) {
                    shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(true);
                    mActionListener.onInsuranceChecked(getAdapterPosition());
                }
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_NO) {
                cbInsurance.setEnabled(true);
                cbInsurance.setChecked(false);
                llInsurance.setVisibility(View.GONE);
                llInsurance.setBackground(null);
                llInsurance.setOnClickListener(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(false);
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tvLabelInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance);
                llInsurance.setVisibility(View.VISIBLE);
                cbInsurance.setEnabled(true);
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
            mIconTooltip.setClickable(false);
            textInputLayoutShipperName.getTextFieldInput().setClickable(false);
            textInputLayoutShipperName.getTextFieldInput().setFocusable(false);
            textInputLayoutShipperName.getTextFieldInput().setFocusableInTouchMode(false);
            textInputLayoutShipperPhone.getTextFieldInput().setClickable(false);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusable(false);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusableInTouchMode(false);
        } else {
            layoutError.setVisibility(View.GONE);
            tickerError.setVisibility(View.GONE);

            flDisableContainer.setForeground(ContextCompat.getDrawable(flDisableContainer.getContext(), com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item));
            llInsurance.setClickable(true);
            llDropshipper.setClickable(true);
            mIconTooltip.setClickable(true);
            textInputLayoutShipperName.getTextFieldInput().setClickable(true);
            textInputLayoutShipperName.getTextFieldInput().setFocusable(true);
            textInputLayoutShipperName.getTextFieldInput().setFocusableInTouchMode(true);
            textInputLayoutShipperPhone.getTextFieldInput().setClickable(true);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusable(true);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusableInTouchMode(true);
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
        GeneralBottomSheet generalBottomSheet = new GeneralBottomSheet();
        generalBottomSheet.setTitle(title);
        generalBottomSheet.setDesc(message);
        generalBottomSheet.setButtonText(context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close));
        generalBottomSheet.setIcon(image);
        generalBottomSheet.setButtonOnClickListener(bottomSheetUnify -> {
            bottomSheetUnify.dismiss();
            return Unit.INSTANCE;
        });
        generalBottomSheet.show(context, mActionListener.getCurrentFragmentManager());
    }

    private String getPriceFormat(TextView textViewLabel, TextView textViewPrice, long price) {
        if (price == 0) {
            textViewLabel.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.GONE);
            return "-";
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textViewPrice.setVisibility(View.VISIBLE);
            return Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(price, false));
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
        int nonActiveTextColor = ContextCompat.getColor(tvProductName.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_20);
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
        tvProductName.setTextColor(ContextCompat.getColor(tvProductName.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
        textVariant.setTextColor(ContextCompat.getColor(textVariant.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        tvProductPrice.setTextColor(ContextCompat.getColor(tvProductPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
        tvProductOriginalPrice.setTextColor(ContextCompat.getColor(tvProductOriginalPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        tvItemCountAndWeight.setTextColor(ContextCompat.getColor(tvItemCountAndWeight.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(tvOptionalNoteToSeller.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
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