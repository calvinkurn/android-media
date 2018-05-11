package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.constants.InsuranceConstant;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressCartItem;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.pickuppoint.PickupPointLayout;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public abstract class ShipmentItemViewHolder extends RecyclerView.ViewHolder {

    protected static final int IMAGE_ALPHA_DISABLED = 128;
    protected static final int IMAGE_ALPHA_ENABLED = 255;

    private static final int GRAM = 0;
    private static final int KILOGRAM = 1;

    protected ShipmentAdapterActionListener mActionListener;
    private ShipmentAdapter shipmentAdapter;
    protected Context context;

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
    LinearLayout llProductPoliciesLayout;
    TextView tvItemCountAndWeight;
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
    TextInputLayout textInputLayoutShipperName;
    TextInputLayout textInputLayoutShipperPhone;
    View vSeparatorMultipleProductSameStore;

    public ShipmentItemViewHolder(View itemView, Context context, ShipmentAdapterActionListener actionListener,
                                  ShipmentAdapter shipmentAdapter) {
        super(itemView);
        this.context = context;
        this.mActionListener = actionListener;
        this.shipmentAdapter = shipmentAdapter;
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
    }

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

    protected void bindViewHolder(ShipmentCartItem shipmentCartItem,
                                  RecipientAddressModel recipientAddressModel,
                                  ArrayList<ShowCaseObject> showCaseObjectList) {
        tvShopName.setText(shipmentCartItem.getShopName());
        renderCourier(shipmentCartItem, shipmentCartItem.getSelectedShipmentDetailData(), recipientAddressModel);
        renderError(shipmentCartItem);
        renderWarnings(shipmentCartItem);
        renderInsurance(shipmentCartItem);
        renderDropshipper(shipmentCartItem);
        renderCostDetail(shipmentCartItem);
    }

    protected void renderCourier(ShipmentCartItem shipmentCartItem, final ShipmentDetailData shipmentDetailData,
                                 RecipientAddressModel recipientAddressModel) {
        chooseCourierButton.setOnClickListener(getSelectShippingOptionListener(getAdapterPosition(),
                shipmentCartItem, recipientAddressModel));
        tvChangeCourier.setOnClickListener(getSelectShippingOptionListener(getAdapterPosition(),
                shipmentCartItem, recipientAddressModel));

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        if (isCourierSelected) {
            tvCourierName.setText(shipmentDetailData.getSelectedCourier().getName());
            String courierPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    shipmentDetailData.getSelectedCourier().getDeliveryPrice(), true);
            tvCourierPrice.setText(courierPrice);
            llShipmentOptionViewLayout.setVisibility(View.GONE);
            llSelectedCourier.setVisibility(View.VISIBLE);
        } else {
            llSelectedCourier.setVisibility(View.GONE);
            llShipmentOptionViewLayout.setVisibility(View.VISIBLE);
        }

    }

    private void renderCostDetail(ShipmentCartItem shipmentCartItem) {
        rlCartSubTotal.setVisibility(View.VISIBLE);
        rlShipmentCost.setVisibility(shipmentCartItem.isStateDetailSubtotalViewExpanded() ? View.VISIBLE : View.GONE);

        int totalItem = 0;
        double totalWeight = 0;
        int shippingPrice = 0;
        int insurancePrice = 0;
        int additionalPrice = 0;
        int subTotalPrice = 0;
        int totalItemPrice = 0;

        if (shipmentCartItem.isStateDetailSubtotalViewExpanded()) {
            rlShipmentCost.setVisibility(View.VISIBLE);
            ivDetailOptionChevron.setImageResource(R.drawable.chevron_thin_up);
        } else {
            rlShipmentCost.setVisibility(View.GONE);
            ivDetailOptionChevron.setImageResource(R.drawable.chevron_thin_down);
        }

        String tvShippingFeeLabel = tvShippingFee.getContext().getString(R.string.label_delivery_price);
        String tvTotalItemLabel = tvTotalItem.getContext().getString(R.string.label_item_count_without_format);

        if (shipmentCartItem.getSelectedShipmentDetailData() != null &&
                shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            shippingPrice = shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier()
                    .getDeliveryPrice();
            Boolean useInsurance = shipmentCartItem.getSelectedShipmentDetailData().getUseInsurance();
            if (useInsurance != null && useInsurance) {
                insurancePrice = shipmentCartItem.getSelectedShipmentDetailData()
                        .getSelectedCourier().getInsurancePrice();
            }
            additionalPrice = shipmentCartItem.getSelectedShipmentDetailData()
                    .getSelectedCourier().getAdditionalPrice();
            if (shipmentCartItem instanceof ShipmentSingleAddressCartItem) {
                for (CartItemModel cartItemModel : ((ShipmentSingleAddressCartItem) shipmentCartItem).getCartItemModels()) {
                    totalItemPrice += (cartItemModel.getQuantity() * cartItemModel.getPrice());
                    totalItem += cartItemModel.getQuantity();
                    totalWeight += cartItemModel.getWeight();
                }
            } else if (shipmentCartItem instanceof ShipmentMultipleAddressCartItem) {
                ShipmentMultipleAddressCartItem shipmentMultipleAddressItem = (ShipmentMultipleAddressCartItem) shipmentCartItem;
                int productQuantity = Integer.parseInt(shipmentMultipleAddressItem.getMultipleAddressItemData().getProductQty());
                totalItemPrice = shipmentMultipleAddressItem.getProductPriceNumber() * productQuantity;
                totalItem = productQuantity;
                totalWeight = ((ShipmentMultipleAddressCartItem) shipmentCartItem).getMultipleAddressItemData().getProductRawWeight();
            }
            tvShippingFeeLabel = getTotalWeightLabel(totalWeight, shipmentCartItem.getWeightUnit());
            tvTotalItemLabel = String.format(tvTotalItem.getContext().getString(R.string.label_item_count_with_format), totalItem);
            subTotalPrice += (totalItemPrice + shippingPrice + insurancePrice + additionalPrice);
        }
        tvTotalItemPrice.setText(getPriceFormat(totalItemPrice));
        tvTotalItem.setText(tvTotalItemLabel);
        tvShippingFee.setText(tvShippingFeeLabel);
        tvSubTotalPrice.setText(getPriceFormat(subTotalPrice));
        tvShippingFeePrice.setText(getPriceFormat(shippingPrice));
        tvInsuranceFeePrice.setText(getPriceFormat(insurancePrice));
        rlCartSubTotal.setOnClickListener(getCostDetailOptionListener(shipmentCartItem));
    }

    private void renderDropshipper(final ShipmentCartItem shipmentCartItem) {
        if (shipmentCartItem.getSelectedShipmentDetailData() != null &&
                shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier().isAllowDropshiper()) {

            cbDropshipper.setChecked(shipmentCartItem.getSelectedShipmentDetailData().getUseDropshipper());
            if (shipmentCartItem.getSelectedShipmentDetailData().getUseDropshipper()) {
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
                    shipmentCartItem.getSelectedShipmentDetailData().setUseDropshipper(checked);
                    mActionListener.onViewVisibilityStateChanged(getAdapterPosition());
                    mActionListener.onNeedUpdateRequestData();
                }
            });

            textInputLayoutShipperName.setError(context.getString(R.string.message_error_dropshipper_name));
            etShipperName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    shipmentCartItem.getSelectedShipmentDetailData().setDropshipperName(charSequence.toString());
                    if (charSequence.length() == 0) {
                        textInputLayoutShipperName.setError(context.getString(R.string.message_error_dropshipper_name));
                    } else {
                        textInputLayoutShipperName.setErrorEnabled(false);
                    }
                    mActionListener.onNeedUpdateRequestData();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            textInputLayoutShipperPhone.setError(context.getString(R.string.message_error_dropshipper_phone));
            etShipperPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    shipmentCartItem.getSelectedShipmentDetailData().setDropshipperPhone(charSequence.toString());
                    if (charSequence.length() == 0) {
                        textInputLayoutShipperPhone.setError(context.getString(R.string.message_error_dropshipper_phone));
                    } else {
                        textInputLayoutShipperPhone.setErrorEnabled(false);
                    }
                    mActionListener.onNeedUpdateRequestData();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            llDropshipper.setVisibility(View.VISIBLE);
        } else {
            etShipperName.setText("");
            etShipperPhone.setText("");
            llDropshipper.setVisibility(View.GONE);
        }
    }

    private void renderInsurance(final ShipmentCartItem shipmentCartItem) {
        if (shipmentCartItem.getSelectedShipmentDetailData() != null &&
                shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier() != null) {

            cbInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    shipmentCartItem.getSelectedShipmentDetailData().setUseInsurance(checked);
                    mActionListener.onInsuranceChecked(getAdapterPosition());
                    mActionListener.onNeedUpdateRequestData();
                }
            });

            Boolean useInsurance = shipmentCartItem.getSelectedShipmentDetailData().getUseInsurance();
            if (useInsurance != null) {
                cbInsurance.setChecked(useInsurance);
            }

            final CourierItemData courierItemData = shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier();
            if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_MUST) {
                cbInsurance.setChecked(true);
                cbInsurance.setEnabled(false);
                shipmentCartItem.getSelectedShipmentDetailData().setUseInsurance(true);
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_NO) {
                cbInsurance.setChecked(false);
                cbInsurance.setEnabled(false);
                shipmentCartItem.getSelectedShipmentDetailData().setUseInsurance(false);
            } else if (courierItemData.getInsuranceType() == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                cbInsurance.setEnabled(true);
                llInsurance.setOnClickListener(getInsuranceClickListener());
                if (useInsurance == null) {
                    if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                        cbInsurance.setChecked(true);
                        shipmentCartItem.getSelectedShipmentDetailData().setUseInsurance(true);
                        mActionListener.onInsuranceChecked(getAdapterPosition());
                    } else if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                        cbInsurance.setChecked(false);
                        shipmentCartItem.getSelectedShipmentDetailData().setUseInsurance(false);
                    }
                }
            }

            if (courierItemData.getInsuranceUsedType() == InsuranceConstant.INSURANCE_USED_TYPE_TOKOPEDIA_INSURANCE) {
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

    private void renderError(ShipmentCartItem shipmentCartItem) {
        if (shipmentCartItem.isError()) {
            tvError.setText(shipmentCartItem.getErrorMessage());
            layoutError.setVisibility(View.VISIBLE);
        } else {
            layoutError.setVisibility(View.GONE);
        }
    }

    private void renderWarnings(ShipmentCartItem shipmentCartItem) {
        if (shipmentCartItem.isWarning()) {
            tvWarning.setText(shipmentCartItem.getWarningMessage());
            layoutWarning.setVisibility(View.VISIBLE);
        } else {
            layoutWarning.setVisibility(View.GONE);
        }
    }

    private String getTotalWeightLabel(double weight, int weightUnit) {
        String unit = weightUnit == GRAM ? context.getString(R.string.weight_unit_gram) :
                context.getString(R.string.weight_unit_kilogram);
        return String.format(context.getString(R.string.label_shipping_price_format), String.valueOf(weight), unit);
    }

    private String getPriceFormat(int price) {
        return price == 0 ? "-" : CurrencyFormatUtil.convertPriceValueToIdrFormat(price, true);
    }

    protected int getResourceDrawerChevron(boolean isExpanded) {
        return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
    }

    private View.OnClickListener getCostDetailOptionListener(final ShipmentCartItem shipmentCartItem) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shipmentCartItem.setStateDetailSubtotalViewExpanded(!shipmentCartItem.isStateDetailSubtotalViewExpanded());
                mActionListener.onViewVisibilityStateChanged(getAdapterPosition());
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
                mActionListener.onViewVisibilityStateChanged(getAdapterPosition());
            }
        };
    }

    private View.OnClickListener getSelectShippingOptionListener(final int position,
                                                                 final ShipmentCartItem shipmentCartItem,
                                                                 final RecipientAddressModel recipientAddressModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onChooseShipment(position, shipmentCartItem, recipientAddressModel);
            }
        };
    }

}
