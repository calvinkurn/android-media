package com.tokopedia.checkout.view.feature.shipment.viewholder;

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
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.utils.WeightFormatterUtil;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentInnerProductListAdapter;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.design.pickuppoint.PickupPointLayout;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.logisticdata.data.constant.CourierConstant;
import com.tokopedia.logisticdata.data.constant.InsuranceConstant;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;

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
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private static final int DROPSHIPPER_MIN_NAME_LENGTH = 3;
    private static final int DROPSHIPPER_MAX_NAME_LENGTH = 100;
    private static final int DROPSHIPPER_MIN_PHONE_LENGTH = 6;
    private static final int DROPSHIPPER_MAX_PHONE_LENGTH = 20;
    private static final String PHONE_NUMBER_REGEX_PATTERN = "[0-9]+";

    private ShipmentAdapterActionListener mActionListener;
    private Context context;

    private LinearLayout layoutError;
    private TextView tvErrorTitle;
    private TextView tvErrorDescription;
    private LinearLayout layoutWarning;
    private TextView tvWarningTitle;
    private TextView tvWarningDescription;
    private TextView tvShopName;
    private LinearLayout llShippingWarningContainer;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private ImageView ivFreeReturnIcon;
    private TextView tvFreeReturnLabel;
    private TextView tvPreOrder;
    private TextView tvCashback;
    private FlexboxLayout llProductPoliciesLayout;
    private TextView tvItemCountAndWeight;
    private TextView tvNoteToSellerLabel;
    private TextView tvOptionalNoteToSeller;
    private LinearLayout llOptionalNoteToSellerLayout;
    private LinearLayout llItemProductContainer;
    private RelativeLayout rlPurchaseProtection;
    private TextView tvPPPLinkText;
    private TextView tvPPPPrice;
    private TextView tvPPPMore;
    private CheckBox cbPPP;
    private TextView tvAddressName;
    private TextView tvAddressStatus;
    private LinearLayout llAddressName;
    private TextView tvRecipientName;
    private TextView tvRecipientAddress;
    private TextView tvRecipientPhone;
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
    private TextView tvProtectionLabel;
    private TextView tvProtectionFee;
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
    private LinearLayout llShipmentBlackboxContainer;
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
    private LinearLayout layoutWarningAndError;
    private LinearLayout llCourierStateLoading;
    private LinearLayout llCourierRecommendationStateLoading;
    private TextView tvErrorShipmentItemTitle;
    private TextView tvErrorShipmentItemDescription;

    // robinhood III
    private LinearLayout llCourierBlackboxStateLoading;
    private LinearLayout llSelectShipmentBlackbox;
    private TextView tvChooseCourierBlackbox;
    private LinearLayout llSelectedShipmentBlackbox;
    private LinearLayout llSelectedCourierBlackboxContainer;
    private TextView tvSelectedCourierBlackbox;
    private TextView tvSelectedPriceBlackbox;
    private TextView tvChangeSelectedCourierBlackbox;
    private LinearLayout llShipmentBlackboxInfoTicker;
    private ImageView imgYellowBulb;
    private TextView tvShipmentBlackboxTickerInfo;

    private TextView tvTradeInLabel;

    private List<Object> shipmentDataList;
    private Pattern phoneNumberRegexPattern;
    private CompositeSubscription compositeSubscription;
    private SaveStateDebounceListener saveStateDebounceListener;
    private TextView tvFulfillName;
    private ImageView imgFulfill;

    // promostacking
    private TickerPromoStackingCheckoutView tickerPromoStackingCheckoutView;
    private View llLogPromo;
    private TextView tvLogPromoLabel;
    private TextView tvLogPromoMsg;

    public ShipmentItemViewHolder(View itemView) {
        super(itemView);
    }

    public ShipmentItemViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        this.mActionListener = actionListener;
        this.context = itemView.getContext();
        phoneNumberRegexPattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN);

        bindViewIds(itemView);
    }

    private void bindViewIds(View itemView) {
        layoutError = itemView.findViewById(R.id.layout_error);
        tvErrorTitle = itemView.findViewById(R.id.tv_error_title);
        tvErrorDescription = itemView.findViewById(R.id.tv_error_description);
        layoutWarning = itemView.findViewById(R.id.layout_warning);
        tvWarningTitle = itemView.findViewById(R.id.tv_warning_title);
        tvWarningDescription = itemView.findViewById(R.id.tv_warning_description);
        tvShopName = itemView.findViewById(R.id.tv_shop_name);
        llShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        ivProductImage = itemView.findViewById(R.id.iv_product_image);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        rlPurchaseProtection = itemView.findViewById(R.id.rlayout_purchase_protection);
        tvPPPLinkText = itemView.findViewById(R.id.text_link_text);
        tvPPPPrice = itemView.findViewById(R.id.text_price_per_product);
        tvPPPMore = itemView.findViewById(R.id.text_ppp_more);
        cbPPP = itemView.findViewById(R.id.checkbox_ppp);
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
        tvProtectionLabel = itemView.findViewById(R.id.tv_purchase_protection_label);
        tvProtectionFee = itemView.findViewById(R.id.tv_purchase_protection_fee);
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
        llShipmentBlackboxContainer = itemView.findViewById(R.id.ll_shipment_blackbox_container);
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
        layoutWarningAndError = itemView.findViewById(R.id.layout_warning_and_error);
        llCourierStateLoading = itemView.findViewById(R.id.ll_courier_state_loading);
        llCourierRecommendationStateLoading = itemView.findViewById(R.id.ll_courier_recommendation_state_loading);
        tvErrorShipmentItemTitle = itemView.findViewById(R.id.tv_error_shipment_item_title);
        tvErrorShipmentItemDescription = itemView.findViewById(R.id.tv_error_shipment_item_description);

        // robinhood III
        llCourierBlackboxStateLoading = itemView.findViewById(R.id.ll_courier_blackbox_state_loading);
        llSelectShipmentBlackbox = itemView.findViewById(R.id.ll_select_shipment_blackbox);
        tvChooseCourierBlackbox = itemView.findViewById(R.id.tv_choose_courier_blackbox);
        llSelectedShipmentBlackbox = itemView.findViewById(R.id.ll_selected_shipment_blackbox);
        llSelectedCourierBlackboxContainer = itemView.findViewById(R.id.ll_selected_courier_blackbox_container);
        tvSelectedCourierBlackbox = itemView.findViewById(R.id.tv_selected_courier_blackbox);
        tvSelectedPriceBlackbox = itemView.findViewById(R.id.tv_selected_price_blackbox);
        tvChangeSelectedCourierBlackbox = itemView.findViewById(R.id.tv_change_selected_courier_blackbox);
        llShipmentBlackboxInfoTicker = itemView.findViewById(R.id.ll_shipment_blackbox_info_ticker);
        imgYellowBulb = itemView.findViewById(R.id.img_bulb);
        tvShipmentBlackboxTickerInfo = itemView.findViewById(R.id.tv_shipment_blackbox_ticker_info);
        tvFulfillName = itemView.findViewById(R.id.tv_fulfill_district);
        imgFulfill = itemView.findViewById(R.id.img_shop_fulfill);
        tvTradeInLabel = itemView.findViewById(R.id.tv_trade_in_label);

        // promostacking
        tickerPromoStackingCheckoutView = itemView.findViewById(R.id.voucher_merchant_holder_view);
        llLogPromo = itemView.findViewById(R.id.layout_logistic_promo_stacking);
        tvLogPromoLabel = itemView.findViewById(R.id.tv_logistic_promo_label);
        tvLogPromoMsg = itemView.findViewById(R.id.tv_logistic_promo_msg);

        compositeSubscription = new CompositeSubscription();
        initSaveStateDebouncer();
    }

    @Override
    public void notifyOnPurchaseProtectionChecked(boolean checked, int position) {
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

    @Override
    public void navigateToWebView(String protectionLinkUrl) {
        mActionListener.navigateToProtectionMore(protectionLinkUrl);
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

    public void unsubscribeDebouncer() {
        compositeSubscription.unsubscribe();
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
                               List<Object> shipmentDataList,
                               RecipientAddressModel recipientAddressModel,
                               RatesDataConverter ratesDataConverter,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        if (this.shipmentDataList == null) {
            this.shipmentDataList = shipmentDataList;
        }
        renderShop(shipmentCartItemModel);
        renderPromoMerchant(shipmentCartItemModel);
        renderFulfillment(shipmentCartItemModel);
        renderAddress(shipmentCartItemModel.getRecipientAddressModel());
        renderShippingType(shipmentCartItemModel, recipientAddressModel, ratesDataConverter, showCaseObjectList);
        renderErrorAndWarning(shipmentCartItemModel);
        renderInsurance(shipmentCartItemModel);
        renderDropshipper(recipientAddressModel != null && recipientAddressModel.isCornerAddress());
        renderCostDetail(shipmentCartItemModel);
        renderCartItem(shipmentCartItemModel);
    }

    private void renderFulfillment(ShipmentCartItemModel model) {
        if (model.isFulfillment()) {
            imgFulfill.setVisibility(View.VISIBLE);
            tvFulfillName.setVisibility(View.VISIBLE);
            tvFulfillName.setText(model.getFulfillmentName());
        } else {
            imgFulfill.setVisibility(View.GONE);
            tvFulfillName.setVisibility(View.GONE);
        }
    }

    private void renderErrorAndWarning(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isWarning() || shipmentCartItemModel.isError()) {
            layoutWarningAndError.setVisibility(View.VISIBLE);
        } else {
            layoutWarningAndError.setVisibility(View.GONE);
        }
        renderError(shipmentCartItemModel);
        renderWarnings(shipmentCartItemModel);
    }

    private void renderShippingType(ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel, RatesDataConverter ratesDataConverter, ArrayList<ShowCaseObject> showCaseObjectList) {
        llLogPromo.setVisibility(View.GONE);
        if (shipmentCartItemModel.isUseCourierRecommendation()) {
            if (shipmentCartItemModel.getIsBlackbox()) {
                renderRobinhoodV3(shipmentCartItemModel, shipmentCartItemModel.getSelectedShipmentDetailData(),
                        recipientAddressModel, shipmentCartItemModel.getShopShipmentList(), ratesDataConverter);
                if (showCaseObjectList.size() == 1) {
                    showCaseObjectList.add(new ShowCaseObject(llSelectShipmentRecommendation,
                            llSelectShipmentRecommendation.getContext().getString(R.string.label_title_showcase_shipment_blackbox),
                            llSelectShipmentRecommendation.getContext().getString(R.string.label_message_showcase_shipment_courier_recommendation_blackbox),
                            ShowCaseContentPosition.UNDEFINED)
                    );
                }
            } else {
                renderRobinhoodV2(shipmentCartItemModel, shipmentCartItemModel.getSelectedShipmentDetailData(),
                        recipientAddressModel, shipmentCartItemModel.getShopShipmentList(), ratesDataConverter);
                if (showCaseObjectList.size() == 1) {
                    showCaseObjectList.add(new ShowCaseObject(llSelectShipmentRecommendation,
                            llSelectShipmentRecommendation.getContext().getString(R.string.label_title_showcase_shipment_courier_recommendation),
                            llSelectShipmentRecommendation.getContext().getString(R.string.label_message_showcase_shipment_courier_recommendation),
                            ShowCaseContentPosition.UNDEFINED)
                    );
                }
            }
        } else {
            renderRobinhoodV1(shipmentCartItemModel, shipmentCartItemModel.getSelectedShipmentDetailData(),
                    recipientAddressModel, ratesDataConverter);
            if (showCaseObjectList.size() == 1) {
                showCaseObjectList.add(new ShowCaseObject(llShipmentOptionViewLayout,
                        llShipmentOptionViewLayout.getContext().getString(R.string.label_title_showcase_shipment),
                        llShipmentOptionViewLayout.getContext().getString(R.string.label_message_showcase_shipment),
                        ShowCaseContentPosition.UNDEFINED)
                );
            }
        }
    }

    /*private void setMargin(int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) cvInvoiceItem.getLayoutParams();
        int sideMargin = (int) cvInvoiceItem.getContext().getResources().getDimension(R.dimen.dp_16);
        layoutParams.setMargins(sideMargin, topMargin, sideMargin, 0);
        cvInvoiceItem.requestLayout();
    }*/

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

        tvShopName.setText(shipmentCartItemModel.getShopName());
    }

    private void renderPromoMerchant(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isError()) {
            tickerPromoStackingCheckoutView.setVisibility(View.VISIBLE);
            tickerPromoStackingCheckoutView.setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
            tickerPromoStackingCheckoutView.disableView();
            if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                mActionListener.onCancelVoucherMerchantClicked(shipmentCartItemModel.getVoucherOrdersItemUiModel().getCode(), getAdapterPosition(), true);
                shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
            }
        } else {
            if (shipmentCartItemModel.getHasPromoList()) {
                tickerPromoStackingCheckoutView.setVisibility(View.VISIBLE);
                tickerPromoStackingCheckoutView.setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
                if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                    tickerPromoStackingCheckoutView.setState(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(shipmentCartItemModel.getVoucherOrdersItemUiModel().getMessage().getState()));
                    tickerPromoStackingCheckoutView.setDesc(shipmentCartItemModel.getVoucherOrdersItemUiModel().getInvoiceDescription());
                    tickerPromoStackingCheckoutView.setTitle(shipmentCartItemModel.getVoucherOrdersItemUiModel().getMessage().getText());
                } else {
                    tickerPromoStackingCheckoutView.enableView();
                    tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.EMPTY);
                }
                tickerPromoStackingCheckoutView.setActionListener(new TickerPromoStackingCheckoutView.ActionListener() {
                    @Override
                    public void onClickUsePromo() {
                        mActionListener.onVoucherMerchantPromoClicked(shipmentCartItemModel);
                    }

                    @Override
                    public void onResetPromoDiscount() {
                        if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                            mActionListener.onCancelVoucherMerchantClicked(shipmentCartItemModel.getVoucherOrdersItemUiModel().getCode(), getAdapterPosition(), false);
                        }
                    }

                    @Override
                    public void onClickDetailPromo() {

                    }

                    @Override
                    public void onDisablePromoDiscount() {
                        if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                            mActionListener.onCancelVoucherMerchantClicked(shipmentCartItemModel.getVoucherOrdersItemUiModel().getCode(), getAdapterPosition(), false);
                        }
                    }
                });
            } else {
                tickerPromoStackingCheckoutView.setVisibility(View.GONE);
            }
        }
    }

    private void renderFirstCartItem(CartItemModel cartItemModel) {
        if (cartItemModel.isError()) {
            showShipmentWarning(cartItemModel);
        } else {
            hideShipmentWarning();
        }
        ImageHandler.LoadImage(ivProductImage, cartItemModel.getImageUrl());
        tvProductName.setText(cartItemModel.getName());
        tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (long) cartItemModel.getPrice(), false));
        tvItemCountAndWeight.setText(String.format(tvItemCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItemModel.getQuantity()),
                WeightFormatterUtil.getFormattedWeight(cartItemModel.getWeight(), cartItemModel.getQuantity())));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItemModel.getNoteToSeller());
        llOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        tvOptionalNoteToSeller.setText(cartItemModel.getNoteToSeller());
        tvNoteToSellerLabel.setVisibility(View.GONE);

        rlPurchaseProtection.setVisibility(cartItemModel.isProtectionAvailable() ? View.VISIBLE : View.GONE);
        if (cartItemModel.isProtectionAvailable()) {
            tvPPPMore.setText(cartItemModel.getProtectionLinkText());
            tvPPPMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActionListener.navigateToProtectionMore(cartItemModel.getProtectionLinkUrl());
                }
            });
            tvPPPLinkText.setText(cartItemModel.getProtectionTitle());
            tvPPPPrice.setText(cartItemModel.getProtectionSubTitle());
            cbPPP.setChecked(cartItemModel.isProtectionOptIn());
            cbPPP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    notifyOnPurchaseProtectionChecked(checked, 0);
                }
            });
        }

        ivFreeReturnIcon.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        if (cartItemModel.isPreOrder()) {
            tvPreOrder.setText(cartItemModel.getPreOrderInfo());
            tvPreOrder.setVisibility(View.VISIBLE);
        } else {
            tvPreOrder.setVisibility(View.GONE);
        }
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
            // tvExpandOtherProduct.setTextColor(ContextCompat.getColor(tvExpandOtherProduct.getContext(), R.color.black_54));
            tvExpandOtherProduct.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_24dp, 0);
        } else {
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(String.format(tvExpandOtherProduct.getContext().getString(R.string.label_other_item_count_format),
                    String.valueOf(cartItemModels.size())));
            tvExpandOtherProduct.setTextColor(ContextCompat.getColor(tvExpandOtherProduct.getContext(), R.color.medium_green));
            tvExpandOtherProduct.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_24dp, 0);
        }
    }

    // Choose duration, then manually choose courier
    // Deprecated
    private void renderRobinhoodV1(ShipmentCartItemModel shipmentCartItemModel,
                                   ShipmentDetailData shipmentDetailData,
                                   RecipientAddressModel recipientAddressModel,
                                   RatesDataConverter ratesDataConverter) {
        llShipmentRecommendationContainer.setVisibility(View.GONE);
        llShipmentBlackboxContainer.setVisibility(View.GONE);
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
                    shipmentDetailData.getSelectedCourier().getShipperPrice(), false);
            tvCourierPrice.setText(courierPrice);
            tvCourierPrice.setVisibility(View.VISIBLE);
            tvDash.setVisibility(View.VISIBLE);
            llShipmentOptionViewLayout.setVisibility(View.GONE);
            llSelectedCourier.setVisibility(View.VISIBLE);
            llCourierStateLoading.setVisibility(View.GONE);
        } else {
            llShippingOptionsContainer.setVisibility(View.GONE);
            llSelectedCourier.setVisibility(View.GONE);
            llShipmentOptionViewLayout.setVisibility(View.VISIBLE);

            if (shipmentCartItemModel.isStateLoadingCourierState()) {
                llCourierStateLoading.setVisibility(View.VISIBLE);
                llShipmentOptionViewLayout.setVisibility(View.GONE);
            } else {
                llCourierStateLoading.setVisibility(View.GONE);
                if (shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0) {
                    if (shipmentDetailData == null) {
                        RecipientAddressModel tmpRecipientAddressModel;
                        if (recipientAddressModel != null) {
                            tmpRecipientAddressModel = recipientAddressModel;
                        } else {
                            tmpRecipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                        }
                        ShipmentDetailData tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
                                shipmentCartItemModel, tmpRecipientAddressModel);

                        if (!shipmentCartItemModel.isStateHasLoadCourierState()) {
                            mActionListener.onLoadShippingState(shipmentCartItemModel.getShippingId(),
                                    shipmentCartItemModel.getSpId(), getAdapterPosition(), tmpShipmentDetailData,
                                    shipmentCartItemModel, shipmentCartItemModel.getShopShipmentList(), false);
                            shipmentCartItemModel.setStateLoadingCourierState(true);
                            shipmentCartItemModel.setStateHasLoadCourierState(true);
                            llCourierStateLoading.setVisibility(View.VISIBLE);
                            llShipmentOptionViewLayout.setVisibility(View.GONE);
                        }
                    }
                } else {
                    llCourierStateLoading.setVisibility(View.GONE);
                    llShipmentOptionViewLayout.setVisibility(View.VISIBLE);
                }
            }

        }

    }

    private void renderRobinhoodV2(ShipmentCartItemModel shipmentCartItemModel,
                                   ShipmentDetailData shipmentDetailData,
                                   RecipientAddressModel recipientAddressModel,
                                   List<ShopShipment> shopShipmentList,
                                   RatesDataConverter ratesDataConverter) {
        RecipientAddressModel currentAddress;
        if (recipientAddressModel == null) {
            currentAddress = shipmentCartItemModel.getRecipientAddressModel();
        } else {
            currentAddress = recipientAddressModel;
        }
        llShipmentContainer.setVisibility(View.GONE);
        llShipmentBlackboxContainer.setVisibility(View.GONE);
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

        tvChangeSelectedCourierRecommendation.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_grey_round));
        tvChangeSelectedCourierRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.onChangeShippingCourier(
                        shipmentCartItemModel.getSelectedShipmentDetailData().getShippingCourierViewModels(),
                        currentAddress, shipmentCartItemModel, shopShipmentList, getAdapterPosition());
            }
        });

        // Logistic Promo
        if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
            llLogPromo.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCouponDesc())) {
                tvLogPromoLabel.setText(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCouponDesc());
            } else tvLogPromoLabel.setVisibility(View.GONE);
            tvChangeSelectedCourierRecommendation.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_button_disabled));
            tvChangeSelectedCourierRecommendation.setOnClickListener(null);
        }

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        if (isCourierSelected) {
            if (isCourierInstantOrSameday(shipmentDetailData.getSelectedCourier().getShipperId())) {
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
                    shipmentDetailData.getSelectedCourier().getShipperPrice(), false));
            llCourierRecommendationStateLoading.setVisibility(View.GONE);
            tvLogPromoMsg.setText(shipmentDetailData.getSelectedCourier().getLogPromoMsg());
        } else {
            llSelectedShipmentRecommendation.setVisibility(View.GONE);
            llSelectShipmentRecommendation.setVisibility(View.VISIBLE);
            llShippingOptionsContainer.setVisibility(View.GONE);

            if (shipmentCartItemModel.isStateLoadingCourierState()) {
                llCourierRecommendationStateLoading.setVisibility(View.VISIBLE);
                llSelectShipmentRecommendation.setVisibility(View.GONE);
            } else {
                llCourierRecommendationStateLoading.setVisibility(View.GONE);
                if (shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0) {
                    if (shipmentDetailData == null) {
                        RecipientAddressModel tmpRecipientAddressModel;
                        if (recipientAddressModel != null) {
                            tmpRecipientAddressModel = recipientAddressModel;
                        } else {
                            tmpRecipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                        }
                        ShipmentDetailData tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
                                shipmentCartItemModel, tmpRecipientAddressModel);

                        if (!shipmentCartItemModel.isStateHasLoadCourierState()) {
                            mActionListener.onLoadShippingState(shipmentCartItemModel.getShippingId(),
                                    shipmentCartItemModel.getSpId(), getAdapterPosition(), tmpShipmentDetailData,
                                    shipmentCartItemModel, shipmentCartItemModel.getShopShipmentList(), true);
                            shipmentCartItemModel.setStateLoadingCourierState(true);
                            shipmentCartItemModel.setStateHasLoadCourierState(true);
                            llCourierRecommendationStateLoading.setVisibility(View.VISIBLE);
                            llSelectShipmentRecommendation.setVisibility(View.GONE);
                        }
                    }
                } else {
                    llCourierRecommendationStateLoading.setVisibility(View.GONE);
                    llSelectShipmentRecommendation.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    // Choose duration, then remove courier option, forcing user to continue without choosing courier
    // Deprecated
    private void renderRobinhoodV3(ShipmentCartItemModel shipmentCartItemModel,
                                   ShipmentDetailData shipmentDetailData,
                                   RecipientAddressModel recipientAddressModel,
                                   List<ShopShipment> shopShipmentList,
                                   RatesDataConverter ratesDataConverter) {
        RecipientAddressModel currentAddress;
        if (recipientAddressModel == null) {
            currentAddress = shipmentCartItemModel.getRecipientAddressModel();
        } else {
            currentAddress = recipientAddressModel;
        }
        llShipmentContainer.setVisibility(View.GONE);
        llShipmentRecommendationContainer.setVisibility(View.GONE);
        llShipmentBlackboxContainer.setVisibility(View.VISIBLE);
        tvChooseCourierBlackbox.setOnClickListener(v -> mActionListener.onChooseShipmentDuration(
                shipmentCartItemModel, currentAddress, shopShipmentList, getAdapterPosition()
        ));

        tvChangeSelectedCourierBlackbox.setOnClickListener(v -> mActionListener.onChangeShippingDuration(shipmentCartItemModel, currentAddress,
                shopShipmentList, getAdapterPosition()));

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        if (isCourierSelected) {
            if (!shipmentDetailData.getSelectedCourier().isAllowDropshiper()) {
                String tickerInfo = tvTickerInfo.getResources().getString(R.string.label_hardcoded_courier_blackbox_ticker);
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
            llSelectShipmentBlackbox.setVisibility(View.GONE);
            llSelectedShipmentBlackbox.setVisibility(View.VISIBLE);
            llShippingOptionsContainer.setVisibility(View.VISIBLE);
            tvSelectedCourierBlackbox.setText(shipmentDetailData.getSelectedCourier().getEstimatedTimeDelivery());
            tvSelectedPriceBlackbox.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    shipmentDetailData.getSelectedCourier().getShipperPrice(), false));
            llCourierBlackboxStateLoading.setVisibility(View.GONE);
            tvShipmentBlackboxTickerInfo.setVisibility(View.VISIBLE);
            tvShipmentBlackboxTickerInfo.setText(shipmentCartItemModel.getBlackboxInfo());
            if (!TextUtils.isEmpty(shipmentDetailData.getSelectedCourier().getBlackboxInfo())) {
                tvShipmentBlackboxTickerInfo.setText(shipmentDetailData.getSelectedCourier().getBlackboxInfo());
            } else {
                tvShipmentBlackboxTickerInfo.setVisibility(View.GONE);
            }
        } else {
            llSelectedShipmentBlackbox.setVisibility(View.GONE);
            llSelectShipmentBlackbox.setVisibility(View.VISIBLE);
            llShippingOptionsContainer.setVisibility(View.GONE);

            if (shipmentCartItemModel.isStateLoadingCourierState()) {
                llCourierBlackboxStateLoading.setVisibility(View.VISIBLE);
                llSelectShipmentBlackbox.setVisibility(View.GONE);
            } else {
                llCourierBlackboxStateLoading.setVisibility(View.GONE);
                if (shipmentCartItemModel.getShippingId() != 0 && shipmentCartItemModel.getSpId() != 0) {
                    if (shipmentDetailData == null) {
                        RecipientAddressModel tmpRecipientAddressModel;
                        if (recipientAddressModel != null) {
                            tmpRecipientAddressModel = recipientAddressModel;
                        } else {
                            tmpRecipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                        }
                        ShipmentDetailData tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
                                shipmentCartItemModel, tmpRecipientAddressModel);

                        if (!shipmentCartItemModel.isStateHasLoadCourierState()) {
                            shipmentCartItemModel.setStateLoadingCourierState(true);
                            shipmentCartItemModel.setStateHasLoadCourierState(true);
                            mActionListener.onLoadShippingState(shipmentCartItemModel.getShippingId(),
                                    shipmentCartItemModel.getSpId(), getAdapterPosition(), tmpShipmentDetailData,
                                    shipmentCartItemModel, shipmentCartItemModel.getShopShipmentList(), true);
                            llCourierBlackboxStateLoading.setVisibility(View.VISIBLE);
                            llSelectShipmentBlackbox.setVisibility(View.GONE);
                        }
                    }
                } else {
                    llCourierBlackboxStateLoading.setVisibility(View.GONE);
                    llSelectShipmentBlackbox.setVisibility(View.VISIBLE);
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
        long totalPurchaseProtectionPrice = 0;
        int totalPurchaseProtectionItem = 0;
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
            if (cartItemModel.isProtectionOptIn()) {
                totalPurchaseProtectionItem += cartItemModel.getQuantity();
                totalPurchaseProtectionPrice += cartItemModel.getProtectionPrice();
            }
        }
        totalItemLabel = String.format(tvTotalItem.getContext().getString(R.string.label_item_count_with_format), totalItem);
        String totalPPPItemLabel = String.format("Proteksi Gadget (%d Barang)", totalPurchaseProtectionItem);

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
            subTotalPrice += (totalItemPrice + shippingPrice + insurancePrice + totalPurchaseProtectionPrice + additionalPrice);
        } else {
            subTotalPrice = totalItemPrice;
        }
        tvSubTotalPrice.setText(subTotalPrice == 0 ? "-" : CurrencyFormatUtil.convertPriceValueToIdrFormat(subTotalPrice, false));
        tvTotalItemPrice.setText(totalItemPrice == 0 ? "-" : getPriceFormat(tvTotalItem, tvTotalItemPrice, totalItemPrice));
        tvTotalItem.setText(totalItemLabel);
        tvShippingFee.setText(shippingFeeLabel);
        tvShippingFeePrice.setText(getPriceFormat(tvShippingFee, tvShippingFeePrice, shippingPrice));
        tvInsuranceFeePrice.setText(getPriceFormat(tvInsuranceFee, tvInsuranceFeePrice, insurancePrice));
        tvProtectionLabel.setText(totalPPPItemLabel);
        tvProtectionFee.setText(getPriceFormat(tvProtectionLabel, tvProtectionFee, totalPurchaseProtectionPrice));
        tvAdditionalFeePrice.setText(getPriceFormat(tvAdditionalFee, tvAdditionalFeePrice, additionalPrice));
        rlCartSubTotal.setOnClickListener(getCostDetailOptionListener(shipmentCartItemModel));
    }

    private void renderDropshipper(boolean isCorner) {
        if (shipmentDataList != null) {
            ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition());
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                    shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {

                if (!shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().isAllowDropshiper() || isCorner) {
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

                        if (shipmentDataList.get(getAdapterPosition()) instanceof ShipmentCartItemModel) {
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

                etShipperName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (shipmentDataList.get(getAdapterPosition()) instanceof ShipmentCartItemModel) {
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
                        if (shipmentDataList.get(getAdapterPosition()) instanceof ShipmentCartItemModel) {
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
            mActionListener.onCartDataDisableToCheckout(null);
        } else if (etShipperPhone.getText().length() != 0 && !matcher.matches()) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_invalid));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onCartDataDisableToCheckout(null);
        } else if (etShipperPhone.getText().length() != 0 && etShipperPhone.getText().length() < DROPSHIPPER_MIN_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onCartDataDisableToCheckout(null);
        } else if (etShipperPhone.getText().length() != 0 && etShipperPhone.getText().length() > DROPSHIPPER_MAX_PHONE_LENGTH) {
            textInputLayoutShipperPhone.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_phone_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhoneValid(false);
            mActionListener.onCartDataDisableToCheckout(null);
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
            mActionListener.onCartDataDisableToCheckout(null);
        } else if (etShipperName.getText().length() != 0 && etShipperName.getText().length() < DROPSHIPPER_MIN_NAME_LENGTH) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_min_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onCartDataDisableToCheckout(null);
        } else if (etShipperName.getText().length() != 0 && etShipperName.getText().length() > DROPSHIPPER_MAX_NAME_LENGTH) {
            textInputLayoutShipperName.setError(textInputLayoutShipperName.getContext().getString(R.string.message_error_dropshipper_name_max_length));
            shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperNameValid(false);
            mActionListener.onCartDataDisableToCheckout(null);
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
                    if (saveStateDebounceListener != null) {
                        saveStateDebounceListener.onNeedToSaveState(shipmentCartItemModel);
                    }
                }
            });

            Boolean useInsurance = shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance();
            if (useInsurance != null) {
                cbInsurance.setChecked(useInsurance);
            }

            final CourierItemData courierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_MUST) {
                llInsurance.setVisibility(View.VISIBLE);
                llInsurance.setBackground(null);
                llInsurance.setOnClickListener(null);
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
                llInsurance.setBackground(null);
                llInsurance.setOnClickListener(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(false);
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tvLabelInsurance.setText(R.string.label_shipment_insurance);
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
            tvAddressStatus.setVisibility(View.GONE);
            if (recipientAddressModel.getAddressStatus() == 2) {
                tvAddressStatus.setVisibility(View.VISIBLE);
            } else {
                tvAddressStatus.setVisibility(View.GONE);
            }
            String addressName = recipientAddressModel.getAddressName();
            String recipientName = recipientAddressModel.getRecipientName();
            tvRecipientName.setText(recipientName);
            tvAddressName.setText(addressName);
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
            tvErrorTitle.setText(shipmentCartItemModel.getErrorTitle());
            String errorDescription = shipmentCartItemModel.getErrorDescription();
            if (!TextUtils.isEmpty(errorDescription)) {
                tvErrorDescription.setText(errorDescription);
                tvErrorDescription.setVisibility(View.VISIBLE);
            } else {
                tvErrorDescription.setVisibility(View.GONE);
            }
            layoutError.setVisibility(View.VISIBLE);

        } else {
            layoutError.setVisibility(View.GONE);
        }
    }

    private void renderWarnings(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isWarning()) {
            tvWarningTitle.setText(shipmentCartItemModel.getWarningTitle());
            String warningDescription = shipmentCartItemModel.getWarningDescription();
            if (!TextUtils.isEmpty(warningDescription)) {
                tvWarningDescription.setText(warningDescription);
                tvWarningDescription.setVisibility(View.VISIBLE);
            } else {
                tvWarningDescription.setVisibility(View.GONE);
            }
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
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(price, false);
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

        ShipmentInnerProductListAdapter shipmentInnerProductListAdapter =
                new ShipmentInnerProductListAdapter(cartItemList, this);
        rvCartItem.setAdapter(shipmentInnerProductListAdapter);
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

    private boolean isHavingPurchaseProtectionChecked() {
        ShipmentCartItemModel data = ((ShipmentCartItemModel) shipmentDataList.get(getAdapterPosition()));
        for (CartItemModel item : data.getCartItemModels()) {
            if (item.isProtectionOptIn()) return true;
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
