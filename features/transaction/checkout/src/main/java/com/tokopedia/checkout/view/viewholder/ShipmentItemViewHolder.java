package com.tokopedia.checkout.view.viewholder;

import static com.tokopedia.checkout.domain.mapper.ShipmentMapper.BUNDLING_ITEM_HEADER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.analytics.CheckoutScheduleDeliveryAnalytics;
import com.tokopedia.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.checkout.utils.WeightFormatterUtil;
import com.tokopedia.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.adapter.ShipmentInnerProductListAdapter;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryHolderData;
import com.tokopedia.coachmark.CoachMark2;
import com.tokopedia.coachmark.CoachMark2Item;
import com.tokopedia.iconunify.IconUnify;
import com.tokopedia.kotlin.extensions.view.TextViewExtKt;
import com.tokopedia.logisticCommon.data.constant.CourierConstant;
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingWidget;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel;
import com.tokopedia.logisticcart.shipping.model.SelectedShipperModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet;
import com.tokopedia.purchase_platform.common.feature.bottomsheet.InsuranceBottomSheet;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnButtonModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnDataItemModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel;
import com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView;
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.unifycomponents.CardUnify;
import com.tokopedia.unifycomponents.ImageUnify;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifycomponents.TextFieldUnify;
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.utils.currency.CurrencyFormatUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentItemViewHolder extends RecyclerView.ViewHolder implements ShipmentCartItemViewHolder.ShipmentItemListener,
        ShippingWidget.ShippingWidgetListener{

    public static final int ITEM_VIEW_SHIPMENT_ITEM = R.layout.item_shipment_checkout;

    private static final int FIRST_ELEMENT = 0;
    private static final int DROPSHIPPER_MIN_NAME_LENGTH = 3;
    private static final int DROPSHIPPER_MAX_NAME_LENGTH = 100;
    private static final int DROPSHIPPER_MIN_PHONE_LENGTH = 6;
    private static final int DROPSHIPPER_MAX_PHONE_LENGTH = 20;
    private static final String PHONE_NUMBER_REGEX_PATTERN = "[0-9]+";

    private static final int SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF = 1;
    private static final int SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE = 2;

    private ShipmentAdapterActionListener mActionListener;

    private LinearLayout layoutError;
    private Ticker tickerError;
    private LinearLayout layoutWarning;
    private Typography tvShopName;
    private LinearLayout layoutWarningAndError;
    private Ticker tickerWarningCloseable;

    // Custom Error Ticker
    private CardUnify customTickerError;
    private Typography customTickerDescription;
    private Typography customTickerAction;

    private LinearLayout containerOrder;
    private ConstraintLayout productBundlingInfo;
    private ImageUnify imageBundle;
    private Typography textBundleTitle;
    private Typography textBundlePrice;
    private Typography textBundleSlashPrice;
    private LinearLayout llFrameItemProductContainer;
    private ConstraintLayout rlProductInfo;
    private View vBundlingProductSeparator;
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
    private Ticker tickerProductError;
    private Ticker productTicker;
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
    private ShippingWidget shippingWidget;
    private RatesDataConverter ratesDataConverter;

    private IconUnify mIconTooltip;
    private Typography mPricePerProduct;

    // Gifting AddOn
    private LinearLayout llGiftingAddOnOrderLevel;
    private LinearLayout llGiftingAddOnProductLevel;
    private ButtonGiftingAddOnView buttonGiftingAddonOrderLevel;
    private ButtonGiftingAddOnView buttonGiftingAddonProductLevel;
    private Typography tvAddOnCostLabel;
    private Typography tvAddOnPrice;

    private ScheduleDeliveryDebouncedListener scheduleDeliveryDebouncedListener;
    private CompositeSubscription scheduleDeliveryCompositeSubscription;

    private PublishSubject<Boolean> scheduleDeliveryDonePublisher;
    private Subscription scheduleDeliverySubscription;

    private PlusCoachmarkPrefs plusCoachmarkPrefs;

    public ShipmentItemViewHolder(View itemView) {
        super(itemView);
    }

    public ShipmentItemViewHolder(View itemView, ShipmentAdapterActionListener actionListener, CompositeSubscription scheduleDeliveryCompositeSubscription) {
        super(itemView);
        this.mActionListener = actionListener;
        phoneNumberRegexPattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN);
        this.scheduleDeliveryCompositeSubscription = scheduleDeliveryCompositeSubscription;
        plusCoachmarkPrefs = new PlusCoachmarkPrefs(itemView.getContext());

        bindViewIds(itemView);
    }

    private void bindViewIds(View itemView) {
        containerOrder = itemView.findViewById(R.id.container_order);
        layoutError = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.layout_error);
        tickerError = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.ticker_error);
        layoutWarning = itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.layout_warning);
        tickerWarningCloseable = itemView.findViewById(R.id.ticker_warning_closable);
        layoutWarning.setVisibility(View.GONE);
        layoutWarningAndError = itemView.findViewById(R.id.layout_warning_and_error);
        tvShopName = itemView.findViewById(R.id.tv_shop_name);
        customTickerError = itemView.findViewById(R.id.checkout_custom_ticker_error);
        customTickerDescription = itemView.findViewById(R.id.checkout_custom_ticker_description);
        customTickerAction = itemView.findViewById(R.id.checkout_custom_ticker_action);
        productBundlingInfo = itemView.findViewById(R.id.product_bundling_info);
        imageBundle = itemView.findViewById(R.id.image_bundle);
        textBundleTitle = itemView.findViewById(R.id.text_bundle_title);
        textBundlePrice = itemView.findViewById(R.id.text_bundle_price);
        textBundleSlashPrice = itemView.findViewById(R.id.text_bundle_slash_price);
        llFrameItemProductContainer = itemView.findViewById(R.id.ll_frame_item_product_container);
        rlProductInfo = itemView.findViewById(R.id.rl_product_info);
        vBundlingProductSeparator = itemView.findViewById(R.id.v_bundling_product_separator);
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
        tickerProductError = itemView.findViewById(R.id.checkout_ticker_product_error);
        imgFreeShipping = itemView.findViewById(R.id.img_free_shipping);
        separatorFreeShipping = itemView.findViewById(R.id.separator_free_shipping);
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
        shippingWidget = itemView.findViewById(R.id.shipping_widget);
        shippingWidget.setupListener(this);

        // AddOn Experience
        llGiftingAddOnOrderLevel = itemView.findViewById(R.id.ll_gifting_addon_order_level);
        llGiftingAddOnProductLevel = itemView.findViewById(R.id.ll_gifting_addon_product_level);
        buttonGiftingAddonOrderLevel = itemView.findViewById(R.id.button_gifting_addon_order_level);
        buttonGiftingAddonProductLevel = itemView.findViewById(R.id.button_gifting_addon_product_level);
        tvAddOnCostLabel = itemView.findViewById(R.id.tv_add_on_fee);
        tvAddOnPrice = itemView.findViewById(R.id.tv_add_on_price);

        compositeSubscription = new CompositeSubscription();
        initSaveStateDebouncer();
        initScheduleDeliveryPublisher();
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
    public void navigateToWebView(CartItemModel cartItemModel) {
        mActionListener.navigateToProtectionMore(cartItemModel);
    }

    @Override
    public void openAddOnProductLevelBottomSheet(@NotNull CartItemModel cartItem, @NotNull AddOnWordingModel addOnWordingModel) {
        mActionListener.openAddOnProductLevelBottomSheet(cartItem, addOnWordingModel);
    }

    @Override
    public void addOnProductLevelImpression(@NonNull String productId) {
        mActionListener.addOnProductLevelImpression(productId);
    }

    public void bindViewHolder(ShipmentCartItemModel shipmentCartItemModel,
                               List<Object> shipmentDataList,
                               RecipientAddressModel recipientAddressModel,
                               RatesDataConverter ratesDataConverter) {
        if (this.shipmentDataList == null) {
            this.shipmentDataList = shipmentDataList;
        }
        this.ratesDataConverter = ratesDataConverter;
        renderShop(shipmentCartItemModel);
        renderFulfillment(shipmentCartItemModel);
        renderShipping(shipmentCartItemModel, recipientAddressModel, ratesDataConverter);
        renderPrioritas(shipmentCartItemModel);
        renderInsurance(shipmentCartItemModel);
        renderDropshipper(recipientAddressModel != null && recipientAddressModel.isCornerAddress());
        renderCostDetail(shipmentCartItemModel);
        renderCartItem(shipmentCartItemModel);
        renderErrorAndWarning(shipmentCartItemModel);
        renderCustomError(shipmentCartItemModel);
        renderShippingVibrationAnimation(shipmentCartItemModel);
        renderAddOnOrderLevel(shipmentCartItemModel, shipmentCartItemModel.getAddOnWordingModel());
    }

    public void unsubscribeDebouncer() {
        compositeSubscription.unsubscribe();
    }

    private void renderShippingVibrationAnimation(ShipmentCartItemModel shipmentCartItemModel) {
        shippingWidget.renderShippingVibrationAnimation(shipmentCartItemModel);
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
        renderWarningCloseable(shipmentCartItemModel);
    }

    private void renderCustomError(ShipmentCartItemModel shipmentCartItemModel) {
        if (!shipmentCartItemModel.isError() && shipmentCartItemModel.isHasUnblockingError()
                && !TextUtils.isEmpty(shipmentCartItemModel.getUnblockingErrorMessage()) && shipmentCartItemModel.getFirstProductErrorIndex() > -1) {
            final String errorMessage = shipmentCartItemModel.getUnblockingErrorMessage();
            customTickerDescription.setText(errorMessage);
            customTickerAction.setOnClickListener(v -> {
                mActionListener.onClickLihatOnTickerOrderError(String.valueOf(shipmentCartItemModel.getShopId()), errorMessage);
                if (!shipmentCartItemModel.isStateAllItemViewExpanded()) {
                    shipmentCartItemModel.setTriggerScrollToErrorProduct(true);
                    showAllProductListener(shipmentCartItemModel).onClick(tickerError);
                    return;
                }
                scrollToErrorProduct(shipmentCartItemModel);
            });
            customTickerError.setVisibility(View.VISIBLE);
            if (shipmentCartItemModel.isTriggerScrollToErrorProduct()) {
                scrollToErrorProduct(shipmentCartItemModel);
            }
        } else {
            customTickerError.setVisibility(View.GONE);
        }
    }

    private void scrollToErrorProduct(ShipmentCartItemModel shipmentCartItemModel) {
        rvCartItem.post(() -> {
            int firstProductErrorIndex = shipmentCartItemModel.getFirstProductErrorIndex() - 1;
            View child = rvCartItem.getChildAt(firstProductErrorIndex);
            float dy = 0;
            if (child != null) {
                dy = child.getY();
                View parent = null;
                while (rvCartItem != parent) {
                    parent = ((View) child.getParent());
                    dy += parent.getY();
                }
            }
            shipmentCartItemModel.setTriggerScrollToErrorProduct(false);
            mActionListener.scrollToPositionWithOffset(getAdapterPosition(), dy * -1);
        });
    }

    private void renderCartItem(ShipmentCartItemModel shipmentCartItemModel) {
        List<CartItemModel> cartItemModelList = new ArrayList<>(shipmentCartItemModel.getCartItemModels());
        if (cartItemModelList.size() > 0) {
            renderFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT), shipmentCartItemModel.getAddOnWordingModel());
        }
        if (shipmentCartItemModel.getCartItemModels().size() > 1) {
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
            if (shipmentCartItemModel.isFreeShippingPlus()) {
                imgFreeShipping.setContentDescription(itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus));
            } else {
                imgFreeShipping.setContentDescription(itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo));
            }
            imgFreeShipping.setVisibility(View.VISIBLE);
            separatorFreeShipping.setVisibility(View.VISIBLE);
            if (!shipmentCartItemModel.getHasSeenFreeShippingBadge() && shipmentCartItemModel.isFreeShippingPlus()) {
                shipmentCartItemModel.setHasSeenFreeShippingBadge(true);
                mActionListener.onViewFreeShippingPlusBadge();
            }
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
        if (!shipmentCartItemModel.getShopTypeInfoData().getShopBadge().isEmpty()) {
            ImageHandler.loadImageWithoutPlaceholder(imgShopBadge, shipmentCartItemModel.getShopTypeInfoData().getShopBadge());
            imgShopBadge.setVisibility(View.VISIBLE);
            imgShopBadge.setContentDescription(itemView.getContext().getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, shipmentCartItemModel.getShopTypeInfoData().getTitle().toLowerCase()));
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        String shopName = shipmentCartItemModel.getShopName();

        tvShopName.setText(shopName);
    }

    @SuppressLint("StringFormatInvalid")
    private void renderFirstCartItem(CartItemModel cartItemModel, AddOnWordingModel addOnWordingModel) {
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
        renderBundlingInfo(cartItemModel);
        renderAddOnProductLevel(cartItemModel, addOnWordingModel);
    }

    private void renderBundlingInfo(CartItemModel cartItemModel) {
        ViewGroup.MarginLayoutParams ivProductImageLayoutParams = (ViewGroup.MarginLayoutParams) ivProductImage.getLayoutParams();
        ViewGroup.MarginLayoutParams tvOptionalNoteToSellerLayoutParams = (ViewGroup.MarginLayoutParams) tvOptionalNoteToSeller.getLayoutParams();
        ViewGroup.MarginLayoutParams productContainerLayoutParams = (ViewGroup.MarginLayoutParams) llFrameItemProductContainer.getLayoutParams();
        ViewGroup.MarginLayoutParams productInfoLayoutParams = (ViewGroup.MarginLayoutParams) rlProductInfo.getLayoutParams();
        int bottomMargin = itemView.getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8);

        if (cartItemModel.isBundlingItem()) {
            if (!TextUtils.isEmpty(cartItemModel.getBundleIconUrl())) {
                ImageHandler.loadImage2(imageBundle, cartItemModel.getBundleIconUrl(), com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder);
            }
            ivProductImageLayoutParams.leftMargin = itemView.getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_14);
            tvOptionalNoteToSellerLayoutParams.leftMargin = itemView.getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_14);
            vBundlingProductSeparator.setVisibility(View.VISIBLE);
            if (cartItemModel.getBundlingItemPosition() == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                productBundlingInfo.setVisibility(View.VISIBLE);
                vSeparatorMultipleProductSameStore.setVisibility(View.VISIBLE);
            } else {
                productBundlingInfo.setVisibility(View.GONE);
                vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            }
            textBundleTitle.setText(cartItemModel.getBundleTitle());
            textBundlePrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(cartItemModel.getBundlePrice(), false)));
            textBundleSlashPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(cartItemModel.getBundleOriginalPrice(), false)));
            textBundleSlashPrice.setPaintFlags(textBundleSlashPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            productContainerLayoutParams.bottomMargin = 0;
            productInfoLayoutParams.bottomMargin = 0;
        } else {
            ivProductImageLayoutParams.leftMargin = 0;
            tvOptionalNoteToSellerLayoutParams.leftMargin = 0;
            vBundlingProductSeparator.setVisibility(View.GONE);
            productBundlingInfo.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.VISIBLE);
            productContainerLayoutParams.bottomMargin = bottomMargin;
            productInfoLayoutParams.bottomMargin = bottomMargin;
        }
    }

    private void renderAddOnProductLevel(CartItemModel cartItemModel, AddOnWordingModel addOnWordingModel) {
        AddOnsDataModel addOnsDataModel = cartItemModel.getAddOnProductLevelModel();
        int status = addOnsDataModel.getStatus();
        if (status == 0) {
            llGiftingAddOnProductLevel.setVisibility(View.GONE);
        } else {
            if (status == 1) {
                buttonGiftingAddonProductLevel.setState(ButtonGiftingAddOnView.State.ACTIVE);
            } else if (status == 2) {
                buttonGiftingAddonProductLevel.setState(ButtonGiftingAddOnView.State.INACTIVE);
            }
            llGiftingAddOnProductLevel.setVisibility(View.VISIBLE);
            buttonGiftingAddonProductLevel.setTitle(addOnsDataModel.getAddOnsButtonModel().getTitle());
            buttonGiftingAddonProductLevel.setDesc(addOnsDataModel.getAddOnsButtonModel().getDescription());
            buttonGiftingAddonProductLevel.setUrlLeftIcon(addOnsDataModel.getAddOnsButtonModel().getLeftIconUrl());
            buttonGiftingAddonProductLevel.setUrlRightIcon(addOnsDataModel.getAddOnsButtonModel().getRightIconUrl());
            buttonGiftingAddonProductLevel.setOnClickListener(view -> mActionListener.openAddOnProductLevelBottomSheet(cartItemModel, addOnWordingModel));
        }
    }

    @SuppressLint("SetTextI18n")
    private void renderProductProperties(CartItemModel cartItemModel) {
        List<String> productInformationList = cartItemModel.getProductInformation();
        layoutProductInfo.removeAllViews();
        if (productInformationList != null && !productInformationList.isEmpty()) {
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
            renderEthicalDrugsProperty(cartItemModel);
        } else {
            layoutProductInfo.setVisibility(View.GONE);
        }
        renderEthicalDrugsProperty(cartItemModel);
    }

    private void renderEthicalDrugsProperty(CartItemModel cartItemModel) {
        if(cartItemModel.getEthicalDrugDataModel().getNeedPrescription()){
            View ethicalDrugView = createProductInfoTextWithIcon(cartItemModel);
            if(layoutProductInfo.getChildCount() > 0){
                ethicalDrugView.setPadding(itemView.getResources().getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),0,0,0);
            }
            layoutProductInfo.addView(ethicalDrugView);
            layoutProductInfo.setVisibility(View.VISIBLE);
        }
    }

    private LinearLayout createProductInfoTextWithIcon(CartItemModel cartItemModel )  {
        LinearLayout propertyLayoutWithIcon = new LinearLayout(itemView.getContext());
        propertyLayoutWithIcon.setOrientation(LinearLayout.HORIZONTAL);
        View propertiesBinding = LayoutInflater.from(itemView.getContext()).inflate(com.tokopedia.purchase_platform.common.R.layout.item_product_info_add_on,null);
        ImageUnify propertyIcon = propertiesBinding.findViewById(com.tokopedia.purchase_platform.common.R.id.pp_iv_product_info_add_on);
        if(propertyIcon != null && !TextUtils.isEmpty(cartItemModel.getEthicalDrugDataModel().getIconUrl())){
            ImageHandler.loadImageWithoutPlaceholderAndError(propertyIcon, cartItemModel.getEthicalDrugDataModel().getIconUrl());
        }
        Typography propertyText = propertiesBinding.findViewById(com.tokopedia.purchase_platform.common.R.id.pp_label_product_info_add_on);
        if(propertyText != null && !TextUtils.isEmpty(cartItemModel.getEthicalDrugDataModel().getText())){
            propertyText.setText(cartItemModel.getEthicalDrugDataModel().getText());
        }
        propertyLayoutWithIcon.addView(propertiesBinding);
        return propertyLayoutWithIcon;
    }

    private void renderProductPrice(CartItemModel cartItemModel) {
        tvProductPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                (long) cartItemModel.getPrice(), false)));
        int dp4 = tvProductPrice.getResources().getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_4);
        if (cartItemModel.getOriginalPrice() > 0) {
            tvProductPrice.setPadding(0, dp4, 0, 0);
            tvProductOriginalPrice.setPadding(0, dp4, 0, 0);
            tvProductOriginalPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(
                    cartItemModel.getOriginalPrice(), false
            )));
            tvProductOriginalPrice.setPaintFlags(tvProductOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvProductOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            tvProductPrice.setPadding(0, dp4, 0, 0);
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
        rlPurchaseProtection.setVisibility(cartItemModel.isProtectionAvailable() && !cartItemModel.isError() ? View.VISIBLE : View.GONE);
        if (cartItemModel.isProtectionAvailable() && !cartItemModel.isError()) {
            mIconTooltip.setOnClickListener(view -> mActionListener.navigateToProtectionMore(cartItemModel));
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
        initInnerRecyclerView(cartItemModels, shipmentItem.getAddOnWordingModel());
        if (shipmentItem.isStateAllItemViewExpanded()) {
            rvCartItem.setVisibility(View.VISIBLE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(R.string.label_hide_other_item_new);
            ivExpandOtherProduct.setImage(IconUnify.CHEVRON_UP, null, null, null, null);
        } else {
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(String.format(tvExpandOtherProduct.getContext().getString(R.string.label_show_other_item_count), cartItemModels.size()));
            ivExpandOtherProduct.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null);
        }
    }

    private void renderShipping(ShipmentCartItemModel shipmentCartItemModel,
                                RecipientAddressModel currentAddress,
                                RatesDataConverter ratesDataConverter) {
        shippingWidget.hideTradeInShippingInfo();

        if (shipmentCartItemModel.isError()) {
            renderErrorCourierState(shipmentCartItemModel);
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
            if (shipmentCartItemModel.isStateLoadingCourierState()) {
                // Has select shipping, but still loading
                renderLoadingCourierState();
                return;
            }
            // Has select shipping
            renderSelectedCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData);
        } else {
            // Has not select shipping
            prepareLoadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, isTradeInDropOff);
        }
    }

    private void renderErrorCourierState(ShipmentCartItemModel shipmentCartItemModel) {
        llShippingOptionsContainer.setVisibility(View.VISIBLE);
        shippingWidget.renderErrorCourierState(shipmentCartItemModel);
    }

    private void renderSelectedCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, CourierItemData selectedCourierItemData) {
        llShippingOptionsContainer.setVisibility(View.VISIBLE);
        shippingWidget.showContainerShippingExperience();

        if (shipmentCartItemModel.isShowScheduleDelivery()) {
            sendScheduleDeliveryAnalytics(shipmentCartItemModel, selectedCourierItemData);
            // Show Schedule delivery widget
            shippingWidget.renderScheduleDeliveryWidget(shipmentCartItemModel, selectedCourierItemData);
        } else if (shipmentCartItemModel.isDisableChangeCourier()) {
            // Is single shipping only
            shippingWidget.renderSingleShippingCourier(shipmentCartItemModel, selectedCourierItemData);
        } else if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
            // Is free ongkir shipping
            renderFreeShippingCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData);
        } else if (shipmentCartItemModel.isHideChangeCourierCard()) {
            // normal shipping but not show `pilih kurir` card
            shippingWidget.renderNormalShippingWithoutChooseCourierCard(shipmentCartItemModel, currentAddress, selectedCourierItemData);
        } else {
            // Is normal shipping
            shippingWidget.renderNormalShippingCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData);
        }
        showMultiplePlusOrderCoachmark(shipmentCartItemModel, shippingWidget);
    }

    @Override
    public void onChangeDurationClickListener(@NonNull ShipmentCartItemModel shipmentCartItemModel, @NonNull RecipientAddressModel currentAddress) {
        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
            mActionListener.onChangeShippingDuration(shipmentCartItemModel, currentAddress, getAdapterPosition());
        }
    }

    @Override
    public void onChangeCourierClickListener(@NonNull ShipmentCartItemModel shipmentCartItemModel, @NonNull RecipientAddressModel currentAddress) {
        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
            mActionListener.onChangeShippingCourier(currentAddress, shipmentCartItemModel, getAdapterPosition(), null);
        }
    }

    @Override
    public void onOnTimeDeliveryClicked(@NonNull String url) {
        mActionListener.onOntimeDeliveryClicked(url);
    }

    @Override
    public void onClickSetPinpoint() {
        mActionListener.onClickSetPinpoint(getAdapterPosition());
    }

    @Override
    public void onClickLayoutFailedShipping(@NonNull ShipmentCartItemModel shipmentCartItemModel, @NonNull RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
                shipmentCartItemModel, recipientAddressModel);

        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            loadCourierStateData(shipmentCartItemModel, SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE, tmpShipmentDetailData, position);
            mActionListener.onClickRefreshErrorLoadCourier();
            initScheduleDeliveryPublisher();
        }
    }

    @Override
    public void onViewErrorInCourierSection(@NonNull String logPromoDesc) {
        mActionListener.onViewErrorInCourierSection(logPromoDesc);
    }

    @Override
    public void onChangeScheduleDelivery(@NonNull ScheduleDeliveryUiModel scheduleDeliveryUiModel) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            scheduleDeliveryDebouncedListener.onScheduleDeliveryChanged(new ShipmentScheduleDeliveryHolderData(
                    scheduleDeliveryUiModel,
                    position
            ));
        }
    }

    private void renderFreeShippingCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, CourierItemData selectedCourierItemData) {
        shippingWidget.showLayoutFreeShippingCourier(shipmentCartItemModel, currentAddress);

        if (shipmentCartItemModel.isError()) {
            mActionListener.onCancelVoucherLogisticClicked(
                    shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode(),
                    getAdapterPosition(),
                    shipmentCartItemModel);
        }

        shippingWidget.renderFreeShippingCourier(selectedCourierItemData);
    }

    private void prepareLoadCourierState(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, RatesDataConverter ratesDataConverter, boolean isTradeInDropOff) {
        shippingWidget.prepareLoadCourierState();
        llShippingOptionsContainer.setVisibility(View.GONE);

        if (isTradeInDropOff) {
            renderNoSelectedCourier(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
            loadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
        } else {
            renderNoSelectedCourier(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE);
            loadCourierState(shipmentCartItemModel, currentAddress, ratesDataConverter, SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE);
        }
    }

    private void renderNoSelectedCourierNormalShipping(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, RatesDataConverter ratesDataConverter) {
        if (shipmentCartItemModel.isDisableChangeCourier()) {
            if (shipmentCartItemModel.getHasGeolocation()) {
                renderFailShipmentState(shipmentCartItemModel, currentAddress, ratesDataConverter);
            }
        } else {
            shippingWidget.showLayoutNoSelectedShipping(shipmentCartItemModel, currentAddress);
        }
    }

    private void renderNoSelectedCourierTradeInDropOff(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress, RatesDataConverter ratesDataConverter, boolean hasSelectTradeInLocation) {
        if (hasSelectTradeInLocation) {
            renderNoSelectedCourierNormalShipping(shipmentCartItemModel, currentAddress, ratesDataConverter);
        } else {
            shippingWidget.showLayoutTradeIn();
        }
    }

    private void renderFailShipmentState(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, RatesDataConverter ratesDataConverter) {
        shippingWidget.showLayoutStateFailedShipping(shipmentCartItemModel, recipientAddressModel);
    }

    private void renderAddOnOrderLevel(ShipmentCartItemModel shipmentCartItemModel, AddOnWordingModel addOnWordingModel) {
        if (shipmentCartItemModel.getAddOnsOrderLevelModel() != null) {
            AddOnsDataModel addOnsDataModel = shipmentCartItemModel.getAddOnsOrderLevelModel();
            AddOnButtonModel addOnButtonModel = addOnsDataModel.getAddOnsButtonModel();
            int statusAddOn = addOnsDataModel.getStatus();
            if (statusAddOn == 0) {
                llGiftingAddOnOrderLevel.setVisibility(View.GONE);
            } else {
                if (statusAddOn == 1) {
                    buttonGiftingAddonOrderLevel.setState(ButtonGiftingAddOnView.State.ACTIVE);
                } else if (statusAddOn == 2) {
                    buttonGiftingAddonOrderLevel.setState(ButtonGiftingAddOnView.State.INACTIVE);
                }

                llGiftingAddOnOrderLevel.setVisibility(View.VISIBLE);
                buttonGiftingAddonOrderLevel.setTitle(addOnButtonModel.getTitle());
                buttonGiftingAddonOrderLevel.setDesc(addOnButtonModel.getDescription());
                buttonGiftingAddonOrderLevel.setUrlLeftIcon(addOnButtonModel.getLeftIconUrl());
                buttonGiftingAddonOrderLevel.setUrlRightIcon(addOnButtonModel.getRightIconUrl());
                buttonGiftingAddonOrderLevel.setOnClickListener(openAddOnOrderLevelBottomSheet(shipmentCartItemModel, addOnWordingModel));
                mActionListener.addOnOrderLevelImpression(shipmentCartItemModel.getCartItemModels());
            }
        }
    }

    private View.OnClickListener openAddOnOrderLevelBottomSheet(ShipmentCartItemModel shipmentCartItemModel, AddOnWordingModel addOnWordingModel) {
        return view -> {
            mActionListener.openAddOnOrderLevelBottomSheet(shipmentCartItemModel, addOnWordingModel);
        };
    }

    private View.OnClickListener getOnChangeCourierClickListener(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel currentAddress) {
        return view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                mActionListener.onChangeShippingCourier(currentAddress, shipmentCartItemModel, getAdapterPosition(), null);
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

    private void loadCourierState(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  RatesDataConverter ratesDataConverter,
                                  int saveStateType) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        if (shipmentCartItemModel.isStateLoadingCourierState()) {
            renderLoadingCourierState();
        } else {
            boolean hasLoadCourier = false;
            shippingWidget.hideShippingStateLoading();
            switch (saveStateType) {
                case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                    hasLoadCourier = shipmentDetailData != null && shipmentDetailData.getSelectedCourierTradeInDropOff() != null;
                    break;
                case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                    hasLoadCourier = shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null;
                    break;
            }

            if (shipmentCartItemModel.isCustomPinpointError()) {
                renderErrorPinpointCourier();
            } else if (shouldAutoLoadCourier(shipmentCartItemModel, recipientAddressModel)) {
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
                        renderNoSelectedCourier(shipmentCartItemModel, recipientAddressModel, ratesDataConverter, saveStateType);
                    }
                }
            } else {
                renderNoSelectedCourier(shipmentCartItemModel, recipientAddressModel, ratesDataConverter, saveStateType);
                showMultiplePlusOrderCoachmark(shipmentCartItemModel, shippingWidget);
            }
        }
    }

    private boolean shouldAutoLoadCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel) {
        return (recipientAddressModel.isTradeIn() && recipientAddressModel.getSelectedTabIndex() != 0 && shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0 && !TextUtils.isEmpty(recipientAddressModel.getDropOffAddressName())) // trade in dropoff
                || (recipientAddressModel.isTradeIn() && recipientAddressModel.getSelectedTabIndex() == 0 && shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0 && !TextUtils.isEmpty(recipientAddressModel.getProvinceName())) // trade in pickup
                || (!recipientAddressModel.isTradeIn() && shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0 && !TextUtils.isEmpty(recipientAddressModel.getProvinceName())) // normal address
                || (!recipientAddressModel.isTradeIn() && !shipmentCartItemModel.getBoCode().isEmpty() && !TextUtils.isEmpty(recipientAddressModel.getProvinceName())) // normal address auto apply BO
                || shipmentCartItemModel.isAutoCourierSelection(); // tokopedia now
    }

    private void renderNoSelectedCourier(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, RatesDataConverter ratesDataConverter, int saveStateType) {
        switch (saveStateType) {
            case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                boolean hasSelectTradeInLocation = mActionListener.hasSelectTradeInLocation();
                renderNoSelectedCourierTradeInDropOff(shipmentCartItemModel, recipientAddressModel, ratesDataConverter, hasSelectTradeInLocation);
                break;
            case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                renderNoSelectedCourierNormalShipping(shipmentCartItemModel, recipientAddressModel, ratesDataConverter);
                break;
        }
    }

    private void renderErrorPinpointCourier() {
        llShippingOptionsContainer.setVisibility(View.VISIBLE);
        shippingWidget.renderErrorPinpointCourier();
    }

    private void loadCourierStateData(ShipmentCartItemModel shipmentCartItemModel, int saveStateType, ShipmentDetailData tmpShipmentDetailData, int position) {
        mActionListener.onLoadShippingState(shipmentCartItemModel.getShippingId(),
                shipmentCartItemModel.getSpId(), position, tmpShipmentDetailData,
                shipmentCartItemModel, shipmentCartItemModel.getShopShipmentList(),
                saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF);
        shipmentCartItemModel.setStateLoadingCourierState(true);
        shippingWidget.onLoadCourierStateData();
        switch (saveStateType) {
            case SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF:
                shipmentCartItemModel.setStateHasLoadCourierTradeInDropOffState(true);
                shippingWidget.hideTradeInTitleAndDetail();
                break;
            case SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE:
                shipmentCartItemModel.setStateHasLoadCourierState(true);
                break;
        }
    }

    private void renderLoadingCourierState() {
        shippingWidget.renderLoadingCourierState();
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
        int totalAddOnPrice = 0;
        boolean hasAddOnSelected = false;

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
                if (cartItemModel.isBundlingItem()) {
                    if (cartItemModel.getBundlingItemPosition() == BUNDLING_ITEM_HEADER) {
                        totalItemPrice += (cartItemModel.getBundlePrice() * cartItemModel.getBundleQuantity());
                    }
                } else {
                    totalItemPrice += (cartItemModel.getQuantity() * cartItemModel.getPrice());
                }
                totalItem += cartItemModel.getQuantity();
                totalWeight += cartItemModel.getWeight();
                if (cartItemModel.isProtectionOptIn()) {
                    totalPurchaseProtectionItem += cartItemModel.getQuantity();
                    totalPurchaseProtectionPrice += cartItemModel.getProtectionPrice();
                }

                if (cartItemModel.getAddOnProductLevelModel().getStatus() == 1) {
                    if (!cartItemModel.getAddOnProductLevelModel().getAddOnsDataItemModelList().isEmpty()) {
                        for (AddOnDataItemModel addOnDataItemModel : cartItemModel.getAddOnProductLevelModel().getAddOnsDataItemModelList()) {
                            totalAddOnPrice += addOnDataItemModel.getAddOnPrice();
                            hasAddOnSelected = true;
                        }
                    }
                }
            }
        }

        if (shipmentCartItemModel.getAddOnsOrderLevelModel() != null) {
            if (shipmentCartItemModel.getAddOnsOrderLevelModel().getStatus() == 1) {
                if (!shipmentCartItemModel.getAddOnsOrderLevelModel().getAddOnsDataItemModelList().isEmpty()) {
                    for (AddOnDataItemModel addOnDataItemModel : shipmentCartItemModel.getAddOnsOrderLevelModel().getAddOnsDataItemModelList()) {
                        totalAddOnPrice += addOnDataItemModel.getAddOnPrice();
                        hasAddOnSelected = true;
                    }
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
                shippingPrice = courierItemData.getSelectedShipper().getShipperPrice();
                Boolean useInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance();
                if (useInsurance != null && useInsurance) {
                    insurancePrice = courierItemData.getSelectedShipper().getInsurancePrice();
                }
                Boolean isOrderPriority = shipmentCartItemModel.getSelectedShipmentDetailData().isOrderPriority();
                if (isOrderPriority != null && isOrderPriority) {
                    priorityPrice = courierItemData.getPriorityPrice();
                }
                additionalPrice = courierItemData.getAdditionalPrice();
                subTotalPrice += (totalItemPrice + insurancePrice + totalPurchaseProtectionPrice + additionalPrice + priorityPrice);
                if (voucherLogisticItemUiModel != null) {
                    int discountedRate = courierItemData.getSelectedShipper().getDiscountedRate();
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
        subTotalPrice += totalAddOnPrice;
        TextViewExtKt.setTextAndContentDescription(tvSubTotalPrice, subTotalPrice == 0 ? "-" : Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(subTotalPrice, false)), R.string.content_desc_tv_sub_total_price);
        TextViewExtKt.setTextAndContentDescription(tvTotalItemPrice, totalItemPrice == 0 ? "-" : getPriceFormat(tvTotalItem, tvTotalItemPrice, totalItemPrice), R.string.content_desc_tv_total_item_price_subtotal);
        tvTotalItem.setText(totalItemLabel);
        tvShippingFee.setText(shippingFeeLabel);
        TextViewExtKt.setTextAndContentDescription(tvShippingFeePrice, getPriceFormat(tvShippingFee, tvShippingFeePrice, shippingPrice), R.string.content_desc_tv_shipping_fee_price_subtotal);
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                voucherLogisticItemUiModel != null) {
            if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getSelectedShipper().getDiscountedRate() == 0) {
                TextViewExtKt.setTextAndContentDescription(tvShippingFeePrice, Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(0.0, false)), R.string.content_desc_tv_shipping_fee_price_subtotal);
            } else {
                TextViewExtKt.setTextAndContentDescription(tvShippingFeePrice, getPriceFormat(tvShippingFee, tvShippingFeePrice, shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getSelectedShipper().getDiscountedRate()), R.string.content_desc_tv_shipping_fee_price_subtotal);
            }
        }
        TextViewExtKt.setTextAndContentDescription(tvInsuranceFeePrice, getPriceFormat(tvInsuranceFee, tvInsuranceFeePrice, insurancePrice), R.string.content_desc_tv_insurance_fee_price_subtotal);
        tvPrioritasFeePrice.setText(getPriceFormat(tvPrioritasFee, tvPrioritasFeePrice, priorityPrice));
        tvProtectionLabel.setText(totalPPPItemLabel);
        tvProtectionFee.setText(getPriceFormat(tvProtectionLabel, tvProtectionFee, totalPurchaseProtectionPrice));
        tvAdditionalFeePrice.setText(getPriceFormat(tvAdditionalFee, tvAdditionalFeePrice, additionalPrice));

        if (hasAddOnSelected) {
            tvAddOnCostLabel.setVisibility(View.VISIBLE);
            tvAddOnPrice.setVisibility(View.VISIBLE);
            tvAddOnPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(totalAddOnPrice, false)));
        } else {
            tvAddOnCostLabel.setVisibility(View.GONE);
            tvAddOnPrice.setVisibility(View.GONE);
        }
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
            final SelectedShipperModel selectedShipperModel = courierItemData.getSelectedShipper();
            if (selectedShipperModel.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_MUST) {
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
            } else if (selectedShipperModel.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_NO || selectedShipperModel.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_NONE) {
                cbInsurance.setEnabled(true);
                cbInsurance.setChecked(false);
                llInsurance.setVisibility(View.GONE);
                llInsurance.setBackground(null);
                llInsurance.setOnClickListener(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(false);
            } else if (selectedShipperModel.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tvLabelInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance);
                llInsurance.setVisibility(View.VISIBLE);
                cbInsurance.setEnabled(true);
                TypedValue outValue = new TypedValue();
                llInsurance.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                llInsurance.setBackgroundResource(outValue.resourceId);
                llInsurance.setOnClickListener(getInsuranceClickListener());
                if (useInsurance == null) {
                    if (selectedShipperModel.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                        cbInsurance.setChecked(true);
                        shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(true);
                        mActionListener.onInsuranceChecked(getAdapterPosition());
                    } else if (selectedShipperModel.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                        cbInsurance.setChecked(shipmentCartItemModel.isInsurance());
                        shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(shipmentCartItemModel.isInsurance());
                    }
                }
            }

            if (!TextUtils.isEmpty(selectedShipperModel.getInsuranceUsedInfo())) {
                if (TextUtils.isEmpty(selectedShipperModel.getInsuranceUsedInfo())) {
                    imgInsuranceInfo.setVisibility(View.GONE);
                } else {
                    imgInsuranceInfo.setVisibility(View.VISIBLE);
                    imgInsuranceInfo.setOnClickListener(view -> {
                        mActionListener.onInsuranceInfoTooltipClickedTrackingAnalytics();
                        showInsuranceBottomSheet(
                                imgInsuranceInfo.getContext(),
                                imgInsuranceInfo.getContext().getString(com.tokopedia.purchase_platform.common.R.string.title_bottomsheet_insurance),
                                selectedShipperModel.getInsuranceUsedInfo()
                        );
                    });
                }
            }
        }
    }

    private void renderError(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isError()) {
            String errorTitle = shipmentCartItemModel.getErrorTitle();
            String errorDescription = shipmentCartItemModel.getErrorDescription();
            if (!TextUtils.isEmpty(errorTitle)) {
                if (!TextUtils.isEmpty(errorDescription)) {
                    tickerError.setTickerTitle(errorTitle);
                    tickerError.setTextDescription(errorDescription);
                } else {
                    tickerError.setTextDescription(errorTitle);
                }
                tickerError.setTickerType(Ticker.TYPE_ERROR);
                tickerError.setTickerShape(Ticker.SHAPE_LOOSE);
                tickerError.setCloseButtonVisibility(View.GONE);
                tickerError.setVisibility(View.VISIBLE);
                layoutError.setVisibility(View.VISIBLE);
            } else {
                tickerError.setVisibility(View.GONE);
                layoutError.setVisibility(View.GONE);
                layoutWarningAndError.setVisibility(View.GONE);
            }

            containerOrder.setAlpha(0.5f);
            cbPPP.setEnabled(false);
            cbInsurance.setEnabled(false);
            llInsurance.setClickable(false);
            cbDropshipper.setEnabled(false);
            llDropshipper.setClickable(false);
            mIconTooltip.setClickable(false);
            rlCartSubTotal.setClickable(false);
            textInputLayoutShipperName.getTextFieldInput().setClickable(false);
            textInputLayoutShipperName.getTextFieldInput().setFocusable(false);
            textInputLayoutShipperName.getTextFieldInput().setFocusableInTouchMode(false);
            textInputLayoutShipperPhone.getTextFieldInput().setClickable(false);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusable(false);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusableInTouchMode(false);
        } else {
            layoutError.setVisibility(View.GONE);
            tickerError.setVisibility(View.GONE);

            containerOrder.setAlpha(1.0f);
            llInsurance.setClickable(true);
            llDropshipper.setClickable(true);
            mIconTooltip.setClickable(true);
            rlCartSubTotal.setClickable(true);
            textInputLayoutShipperName.getTextFieldInput().setClickable(true);
            textInputLayoutShipperName.getTextFieldInput().setFocusable(true);
            textInputLayoutShipperName.getTextFieldInput().setFocusableInTouchMode(true);
            textInputLayoutShipperPhone.getTextFieldInput().setClickable(true);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusable(true);
            textInputLayoutShipperPhone.getTextFieldInput().setFocusableInTouchMode(true);
        }
    }

    private void renderWarningCloseable(ShipmentCartItemModel shipmentCartItemModel) {
        if (!shipmentCartItemModel.isError() && !TextUtils.isEmpty(shipmentCartItemModel.getShopTicker())) {
            tickerWarningCloseable.setTickerTitle(shipmentCartItemModel.getShopTickerTitle());
            tickerWarningCloseable.setHtmlDescription(shipmentCartItemModel.getShopTicker());
            tickerWarningCloseable.setVisibility(View.VISIBLE);
            tickerWarningCloseable.setDescriptionClickEvent(new TickerCallback() {
                @Override
                public void onDescriptionViewClick(@NonNull CharSequence charSequence) {
                    // no op //
                }

                @Override
                public void onDismiss() {
                    shipmentCartItemModel.setShopTicker("");
                    tickerWarningCloseable.setVisibility(View.GONE);
                }
            });
        } else {
            tickerWarningCloseable.setVisibility(View.GONE);
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

    private void initScheduleDeliveryPublisher() {
        if (scheduleDeliverySubscription != null && !scheduleDeliverySubscription.isUnsubscribed()) {
            scheduleDeliverySubscription.unsubscribe();
        }
        if (scheduleDeliveryDonePublisher != null && !scheduleDeliveryDonePublisher.hasCompleted()) {
            scheduleDeliveryDonePublisher.onCompleted();
        }
        scheduleDeliverySubscription = Observable.create((Action1<Emitter<ShipmentScheduleDeliveryHolderData>>) emitter ->
                                scheduleDeliveryDebouncedListener = emitter::onNext,
                        Emitter.BackpressureMode.LATEST)
                .observeOn(AndroidSchedulers.mainThread(), false, 1)
                .subscribeOn(AndroidSchedulers.mainThread())
                .concatMap(shipmentScheduleDeliveryHolderData -> {
                    scheduleDeliveryDonePublisher = PublishSubject.create();
                    mActionListener.onChangeScheduleDelivery(
                            shipmentScheduleDeliveryHolderData.getScheduleDeliveryUiModel(),
                            shipmentScheduleDeliveryHolderData.getPosition(),
                            scheduleDeliveryDonePublisher
                    );
                    return scheduleDeliveryDonePublisher;
                })
                .subscribe();
        scheduleDeliveryCompositeSubscription.add(scheduleDeliverySubscription);
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

    private void showInsuranceBottomSheet(Context context, String title, String message) {
        InsuranceBottomSheet insuranceBottomSheet = new InsuranceBottomSheet();
        insuranceBottomSheet.setDesc(message);
        insuranceBottomSheet.show(title, context, mActionListener.getCurrentFragmentManager());
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

    private void initInnerRecyclerView(List<CartItemModel> cartItemList, AddOnWordingModel addOnWordingModel) {
        rvCartItem.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rvCartItem.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCartItem.setLayoutManager(layoutManager);

        ShipmentInnerProductListAdapter shipmentInnerProductListAdapter =
                new ShipmentInnerProductListAdapter(cartItemList, addOnWordingModel, this);
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
            if (!TextUtils.isEmpty(cartItemModel.getErrorMessageDescription())) {
                tickerProductError.setTickerTitle(cartItemModel.getErrorMessage());
                tickerProductError.setTextDescription(cartItemModel.getErrorMessageDescription());
            } else {
                tickerProductError.setTextDescription(cartItemModel.getErrorMessage());
            }

            if (cartItemModel.isBundlingItem()) {
                if (cartItemModel.getBundlingItemPosition() == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                    tickerProductError.setVisibility(View.VISIBLE);
                } else {
                    tickerProductError.setVisibility(View.GONE);
                }
            } else {
                tickerProductError.setVisibility(View.VISIBLE);
            }
        } else {
            tickerProductError.setVisibility(View.GONE);
        }

        if (!cartItemModel.isShopError()) {
            disableItemView();
        }
    }

    private void hideShipmentWarning() {
        tickerProductError.setVisibility(View.GONE);
        enableItemView();
    }

    private void disableItemView() {
        productBundlingInfo.setAlpha(0.5f);
        llFrameItemProductContainer.setAlpha(0.5f);
    }

    private void enableItemView() {
        productBundlingInfo.setAlpha(1.0f);
        llFrameItemProductContainer.setAlpha(1.0f);
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

    private void sendScheduleDeliveryAnalytics(ShipmentCartItemModel shipmentCartItemModel, CourierItemData selectedCourierItemData) {
        if (!shipmentCartItemModel.getHasSentScheduleDeliveryAnalytics()) {
            CheckoutScheduleDeliveryAnalytics.INSTANCE.sendViewScheduledDeliveryWidgetOnTokopediaNowEvent();
            if (selectedCourierItemData.getScheduleDeliveryUiModel() != null &&
                    !selectedCourierItemData.getScheduleDeliveryUiModel().getAvailable()) {
                CheckoutScheduleDeliveryAnalytics.INSTANCE.sendViewUnavailableScheduledDeliveryEvent();
            }
            shipmentCartItemModel.setHasSentScheduleDeliveryAnalytics(true);
        }

    }

    @NonNull
    @Override
    public FragmentManager getHostFragmentManager() {
        return mActionListener.getCurrentFragmentManager();
    }

    private void showMultiplePlusOrderCoachmark(ShipmentCartItemModel shipmentCartItemModel, View anchorView) {
        if (shipmentCartItemModel.getCoachmarkPlus().isShown() && !plusCoachmarkPrefs.getPlusCoachMarkHasShown()) {
            ArrayList<CoachMark2Item> coachMarkItem = new ArrayList<>();
            CoachMark2 coachMark = new CoachMark2(itemView.getContext());
            coachMarkItem.add(
                    new CoachMark2Item(
                            anchorView,
                            shipmentCartItemModel.getCoachmarkPlus().getTitle(),
                            shipmentCartItemModel.getCoachmarkPlus().getContent(),
                            CoachMark2.POSITION_BOTTOM
                    )
            );
            coachMark.showCoachMark(coachMarkItem, null, 0);
            plusCoachmarkPrefs.setPlusCoachmarkHasShown(true);
        }
    }

    private interface SaveStateDebounceListener {

        void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    }

    private interface ScheduleDeliveryDebouncedListener {
        void onScheduleDeliveryChanged(ShipmentScheduleDeliveryHolderData shipmentScheduleDeliveryHolderData);
    }

}