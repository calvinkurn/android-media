package com.tokopedia.checkout.view.view.shipment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.view.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentCheckoutButtonViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentInsuranceTncViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentRecipientAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCheckoutButtonModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentInsuranceTncModel;
import com.tokopedia.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.checkout.view.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int DEFAULT_ERROR_POSITION = -1;
    private static final int SHIPMENT_COST_POSITION_INDEX_GAP_WITHOUT_INSURANCE = 2;
    private static final int SHIPMENT_COST_POSITION_INDEX_GAP_WITH_INSURANCE = 3;

    private ArrayList<ShowCaseObject> showCaseObjectList;
    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    private List<ShipmentData> shipmentDataList;

    private CartItemPromoHolderData cartItemPromoHolderData;
    private CartPromoSuggestion cartPromoSuggestion;
    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private ShipmentInsuranceTncModel shipmentInsuranceTncModel;
    private ShipmentCheckoutButtonModel shipmentCheckoutButtonModel;

    private ShipmentDataRequestConverter shipmentDataRequestConverter;

    private int shipmentCostItemPosition;
    private boolean hasShownShowCase;
    private int lastChooseCourierItemPosition;

    @Inject
    public ShipmentAdapter(ShipmentAdapterActionListener shipmentAdapterActionListener,
                           ShipmentDataRequestConverter shipmentDataRequestConverter) {
        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
        this.shipmentDataRequestConverter = shipmentDataRequestConverter;
        this.shipmentDataList = new ArrayList<>();
        this.showCaseObjectList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        ShipmentData item = shipmentDataList.get(position);

        if (item instanceof CartItemPromoHolderData) {
            return CartVoucherPromoViewHolder.TYPE_VIEW_PROMO;
        } else if (item instanceof CartPromoSuggestion) {
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
        } else if (item instanceof RecipientAddressModel) {
            return ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS;
        } else if (item instanceof ShipmentCartItemModel) {
            return ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS;
        } else if (item instanceof ShipmentCostModel) {
            return ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST;
        } else if (item instanceof ShipmentInsuranceTncModel) {
            return ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC;
        } else if (item instanceof ShipmentCheckoutButtonModel) {
            return ShipmentCheckoutButtonViewHolder.ITEM_VIEW_CHECKOUT_BUTTON;
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            return new CartVoucherPromoViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            return new CartPromoSuggestionViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS) {
            return new ShipmentRecipientAddressViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            return new ShipmentItemViewHolder(view, shipmentAdapterActionListener, this);
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            return new ShipmentInsuranceTncViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentCheckoutButtonViewHolder.ITEM_VIEW_CHECKOUT_BUTTON) {
            return new ShipmentCheckoutButtonViewHolder(view, this);
        }
        throw new RuntimeException("No view holder type found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ShipmentData data = shipmentDataList.get(position);

        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            ((CartVoucherPromoViewHolder) holder).bindData((CartItemPromoHolderData) data, position);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionViewHolder) holder).bindData((CartPromoSuggestion) data, position);
        } else if (viewType == ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS) {
            ((ShipmentRecipientAddressViewHolder) holder).bindViewHolder((RecipientAddressModel) data,
                    showCaseObjectList);
        } else if (viewType == ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            ((ShipmentItemViewHolder) holder).bindViewHolder(
                    (ShipmentCartItemModel) data, recipientAddressModel, showCaseObjectList);
            setShowCase(holder.itemView.getContext());
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) holder).bindViewHolder((ShipmentCostModel) data);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            ((ShipmentInsuranceTncViewHolder) holder).bindViewHolder((ShipmentInsuranceTncModel) data);
        } else if (viewType == ShipmentCheckoutButtonViewHolder.ITEM_VIEW_CHECKOUT_BUTTON) {
            ((ShipmentCheckoutButtonViewHolder) holder).bindViewHolder(shipmentCheckoutButtonModel);
        }
    }

    @Override
    public int getItemCount() {
        return shipmentDataList.size();
    }

    private void setShowCase(Context context) {
        if (!hasShownShowCase && !ShowCasePreference.hasShown(context, SingleAddressShipmentFragment.class.getName())) {
            hasShownShowCase = true;
            createShowCaseDialog().show((Activity) context,
                    SingleAddressShipmentFragment.class.getName(),
                    showCaseObjectList
            );
        }
    }

    @SuppressLint("PrivateResource")
    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .prevStringRes(R.string.show_case_prev)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        this.cartItemPromoHolderData = cartItemPromoHolderData;
        shipmentDataList.add(cartItemPromoHolderData);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addPromoSuggestionData(CartPromoSuggestion cartPromoSuggestion) {
        if (!TextUtils.isEmpty(cartPromoSuggestion.getPromoCode())) {
            this.cartPromoSuggestion = cartPromoSuggestion;
            shipmentDataList.add(cartPromoSuggestion);
            notifyDataSetChanged();
            checkDataForCheckout();
        }
    }

    public void addAddressShipmentData(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
        shipmentDataList.add(recipientAddressModel);
        notifyDataSetChanged();
    }

    public void addCartItemDataList(List<ShipmentCartItemModel> shipmentCartItemModel) {
        this.shipmentCartItemModelList = shipmentCartItemModel;
        shipmentDataList.addAll(shipmentCartItemModel);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addShipmentCostData(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;
        shipmentDataList.add(shipmentCostModel);
        updateShipmentCostModel();
        notifyDataSetChanged();
    }

    public void addShipmentCheckoutButtonModel(ShipmentCheckoutButtonModel shipmentCheckoutButtonModel) {
        this.shipmentCheckoutButtonModel = shipmentCheckoutButtonModel;
        shipmentDataList.add(shipmentCheckoutButtonModel);
        notifyDataSetChanged();
    }

    public void updateCheckoutButtonData(String defaultTotal) {
        boolean availableCheckout = true;
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() == null ||
                        ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getSelectedCourier() == null ||
                        ((ShipmentCartItemModel) shipmentData).isError()) {
                    availableCheckout = false;
                }
            }
        }

        if (shipmentCheckoutButtonModel == null) {
            shipmentCheckoutButtonModel = new ShipmentCheckoutButtonModel();
        }

        shipmentCheckoutButtonModel.setAbleToCheckout(availableCheckout);
        if (shipmentCostModel != null) {
            String priceTotal = shipmentCostModel.getTotalPrice() == 0 ? "-" :
                    CurrencyFormatUtil.convertPriceValueToIdrFormat((int) shipmentCostModel.getTotalPrice(), true);
            shipmentCheckoutButtonModel.setTotalPayment(priceTotal);
        } else if (defaultTotal != null) {
            shipmentCheckoutButtonModel.setTotalPayment(defaultTotal);
        }

        shipmentAdapterActionListener.onNeedUpdateViewItem(getItemCount() - 1);
    }

    public void updateInsuranceTncVisibility() {
        if (checkItemUseInsuranceExist()) {
            if (shipmentInsuranceTncModel == null) {
                shipmentInsuranceTncModel = new ShipmentInsuranceTncModel();
                shipmentInsuranceTncModel.setVisible(true);
                shipmentDataList.add(shipmentDataList.size() - 1, shipmentInsuranceTncModel);
                notifyItemInserted(shipmentDataList.size() - SHIPMENT_COST_POSITION_INDEX_GAP_WITHOUT_INSURANCE);
                shipmentCostItemPosition = getItemCount() - SHIPMENT_COST_POSITION_INDEX_GAP_WITH_INSURANCE;
            }
        } else {
            for (int i = 0; i < shipmentDataList.size(); i++) {
                if (shipmentDataList.get(i) instanceof ShipmentInsuranceTncModel) {
                    shipmentInsuranceTncModel = null;
                    shipmentDataList.remove(i);
                    notifyItemRemoved(i);
                    shipmentCostItemPosition = getItemCount() - SHIPMENT_COST_POSITION_INDEX_GAP_WITHOUT_INSURANCE;
                    break;
                }
            }
        }
    }

    private boolean checkItemUseInsuranceExist() {
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentData;
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateSelectedAddress(RecipientAddressModel recipientAddress) {
        int addressIndex = 0;
        for (ShipmentData item : shipmentDataList) {
            if (item instanceof RecipientAddressModel) {
                if (!((RecipientAddressModel) item).getId().equalsIgnoreCase(recipientAddress.getId())) {
                    addressIndex = shipmentDataList.indexOf(item);
                }
            }
        }
        if (addressIndex != 0) {
            shipmentDataList.set(addressIndex, recipientAddress);
            this.recipientAddressModel = recipientAddress;
            resetCourier();
            notifyDataSetChanged();
            shipmentAdapterActionListener.resetTotalPrice();
        }
    }

    private void resetCourier() {
        for (ShipmentData item : shipmentDataList) {
            if (item instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) item).getSelectedShipmentDetailData() != null) {
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setSelectedShipment(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setSelectedCourier(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setUseDropshipper(false);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setDropshipperPhone(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setDropshipperName(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setUseInsurance(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setUsePartialOrder(false);
                }
            } else if (item instanceof ShipmentCostModel) {
                ((ShipmentCostModel) item).setAdditionalFee(0);
                ((ShipmentCostModel) item).setInsuranceFee(0);
                ((ShipmentCostModel) item).setShippingFee(0);
                ((ShipmentCostModel) item).setTotalPrice(0);
                ((ShipmentCostModel) item).setTotalItemPrice(0);
                ((ShipmentCostModel) item).setPromoPrice(0);
                ((ShipmentCostModel) item).setPromoMessage(null);
            }
        }
        updateInsuranceTncVisibility();
    }

    public RecipientAddressModel getAddressShipmentData() {
        return recipientAddressModel;
    }

    private void checkDataForCheckout() {
        boolean availableCheckout = true;
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() == null ||
                        ((ShipmentCartItemModel) shipmentData).isError()) {
                    availableCheckout = false;
                }
            }
        }
        if (availableCheckout) {
            shipmentAdapterActionListener.onCartDataEnableToCheckout();
        } else {
            shipmentAdapterActionListener.onCartDataDisableToCheckout();
        }
    }

    public void checkDropshipperValidation() {
        boolean availableCheckout = true;
        int errorPosition = DEFAULT_ERROR_POSITION;
        ShipmentData selectedShipmentData = null;
        for (int i = 0; i < shipmentDataList.size(); i++) {
            ShipmentData shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() != null &&
                        ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getUseDropshipper()) {
                    if (TextUtils.isEmpty(((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getDropshipperName()) ||
                            TextUtils.isEmpty(((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getDropshipperPhone())) {
                        availableCheckout = false;
                        errorPosition = i;
                        selectedShipmentData = shipmentData;
                    }
                }
            }
        }
        shipmentAdapterActionListener.onDropshipperValidationResult(availableCheckout, selectedShipmentData, errorPosition);
    }

    public void setSelecteCourier(int position, CourierItemData courierItemData) {
        ShipmentData currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItemModel) {
            if (((ShipmentCartItemModel) currentShipmentData).getSelectedShipmentDetailData() != null) {
                ((ShipmentCartItemModel) currentShipmentData).getSelectedShipmentDetailData().setUseInsurance(null);
                ((ShipmentCartItemModel) currentShipmentData).getSelectedShipmentDetailData().setSelectedCourier(courierItemData);
            } else {
                ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
                shipmentDetailData.setSelectedCourier(courierItemData);
                shipmentDetailData.setShipmentCartData(((ShipmentCartItemModel) currentShipmentData).getShipmentCartData());
                ((ShipmentCartItemModel) currentShipmentData).setSelectedShipmentDetailData(shipmentDetailData);
            }
            updateShipmentCostModel();
            checkDataForCheckout();
        }
        notifyItemChanged(getShipmentCostPosition());
        notifyItemChanged(position);
        checkHasSelectAllCourier();
    }

    public void checkHasSelectAllCourier() {
        int cartItemCounter = 0;
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                    shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                cartItemCounter++;
            }
        }
        if (cartItemCounter == shipmentCartItemModelList.size()) {
            RequestData requestData = getRequestData();
            shipmentAdapterActionListener.onFinishChoosingShipment(requestData.getPromoRequestData(),
                    requestData.getCheckoutRequestData());
        }
    }

    public void updateShipmentCostModel() {
        double totalWeight = 0;
        double totalPrice = 0;
        double additionalFee = 0;
        double totalItemPrice = 0;
        int totalItem = 0;
        double shippingFee = 0;
        double insuranceFee = 0;
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                if (shipmentData instanceof ShipmentCartItemModel) {
                    ShipmentCartItemModel shipmentSingleAddressItem =
                            (ShipmentCartItemModel) shipmentData;
                    List<CartItemModel> cartItemModels = shipmentSingleAddressItem.getCartItemModels();
                    for (CartItemModel cartItemModel : cartItemModels) {
                        totalWeight += (cartItemModel.getWeight() * cartItemModel.getQuantity());
                        totalItem += cartItemModel.getQuantity();
                        totalItemPrice += (cartItemModel.getPrice() * cartItemModel.getQuantity());
                    }

                    if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() != null &&
                            ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getSelectedCourier() != null) {
                        Boolean useInsurance = ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getUseInsurance();
                        shippingFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getDeliveryPrice();
                        if (useInsurance != null && useInsurance) {
                            insuranceFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                    .getSelectedCourier().getInsurancePrice();
                        }
                        additionalFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getAdditionalPrice();
                    }
                }
            }
        }
        totalPrice = totalItemPrice + shippingFee + insuranceFee - shipmentCostModel.getPromoPrice();
        shipmentCostModel.setTotalWeight(totalWeight);
        shipmentCostModel.setAdditionalFee(additionalFee);
        shipmentCostModel.setTotalItemPrice(totalItemPrice);
        shipmentCostModel.setTotalItem(totalItem);
        shipmentCostModel.setShippingFee(shippingFee);
        shipmentCostModel.setInsuranceFee(insuranceFee);
        shipmentCostModel.setTotalPrice(totalPrice);
        shipmentAdapterActionListener.onTotalPaymentChange(shipmentCostModel);
    }

    public int getShipmentCostPosition() {
        if (shipmentCostItemPosition == 0) {
            return getItemCount() - SHIPMENT_COST_POSITION_INDEX_GAP_WITHOUT_INSURANCE;
        } else {
            return shipmentCostItemPosition;
        }
    }

    public void updateItemAndTotalCost(int position) {
        notifyItemChanged(getShipmentCostPosition());
        notifyItemChanged(position);
    }

    public void updatePromo(CheckPromoCodeCartShipmentResult.DataVoucher dataVoucher) {
        if (dataVoucher != null) {
            shipmentCostModel.setPromoPrice(dataVoucher.getVoucherAmount());
            shipmentCostModel.setPromoMessage(dataVoucher.getVoucherPromoDesc());
            for (int i = 0; i < shipmentDataList.size(); i++) {
                ShipmentData itemAdapter = shipmentDataList.get(i);
                if (itemAdapter instanceof CartPromoSuggestion) {
                    ((CartPromoSuggestion) itemAdapter).setVisible(false);
                    notifyItemChanged(i);
                }
            }
        } else {
            shipmentCostModel.setPromoPrice(0);
            shipmentCostModel.setPromoMessage(null);
            for (int i = 0; i < shipmentDataList.size(); i++) {
                ShipmentData itemAdapter = shipmentDataList.get(i);
                if (itemAdapter instanceof CartItemPromoHolderData) {
                    ((CartItemPromoHolderData) itemAdapter).setPromoNotActive();
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof CartPromoSuggestion) {
                    ((CartPromoSuggestion) itemAdapter).setVisible(true);
                    notifyItemChanged(i);
                }
            }
        }
        updateShipmentCostModel();
        notifyItemChanged(getShipmentCostPosition());
    }

    public void updateItemPromoVoucher(CartItemPromoHolderData cartPromo) {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            ShipmentData shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof CartItemPromoHolderData) {
                shipmentDataList.set(i, cartPromo);
                checkDataForCheckout();
                notifyItemChanged(i);
            } else if (shipmentData instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) shipmentData).setVisible(false);
                notifyItemChanged(i);
            }
        }
        notifyItemChanged(getShipmentCostPosition());
    }

    public void cancelAutoApplyCoupon() {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            ShipmentData shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof CartItemPromoHolderData) {
                ((CartItemPromoHolderData) shipmentData).setPromoNotActive();
                notifyItemChanged(i);
            } else if (shipmentData instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) shipmentData).setVisible(true);
                notifyItemChanged(i);
            }
        }
    }

    public void updateShipmentDestinationPinpoint(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            if (recipientAddressModel != null) {
                recipientAddressModel.setLatitude(latitude);
                recipientAddressModel.setLongitude(longitude);
            }
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel.getShipmentCartData() != null) {
                    shipmentCartItemModel.getShipmentCartData().setDestinationLatitude(latitude);
                    shipmentCartItemModel.getShipmentCartData().setDestinationLongitude(longitude);
                }
            }
        }
    }

    public boolean hasSetAllCourier() {
        for (ShipmentData itemData : shipmentDataList) {
            if (itemData instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) itemData).getSelectedShipmentDetailData() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasAppliedPromoCode() {
        for (ShipmentData itemAdapter : shipmentDataList) {
            if (itemAdapter instanceof CartItemPromoHolderData) {
                return ((CartItemPromoHolderData) itemAdapter).getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER ||
                        ((CartItemPromoHolderData) itemAdapter).getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON;
            }
        }
        return false;
    }

    public RequestData getRequestData() {
        return shipmentDataRequestConverter.generateRequestData(shipmentCartItemModelList, recipientAddressModel);
    }

    public void clearData() {
        shipmentDataList.clear();
        notifyDataSetChanged();
    }

    public ShipmentCartItemModel getShipmentCartItemModelByIndex(int index) {
        if (shipmentDataList.get(index) instanceof ShipmentCartItemModel) {
            return (ShipmentCartItemModel) shipmentDataList.get(index);
        } else {
            return null;
        }
    }

    public int getLastChooseCourierItemPosition() {
        return lastChooseCourierItemPosition;
    }

    public void setLastChooseCourierItemPosition(int lastChooseCourierItemPosition) {
        this.lastChooseCourierItemPosition = lastChooseCourierItemPosition;
    }

    public static class RequestData {

        private List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData;
        private List<DataCheckoutRequest> checkoutRequestData;
        private List<DataChangeAddressRequest> changeAddressRequestData;

        @Inject
        public RequestData() {
            promoRequestData = new ArrayList<>();
            checkoutRequestData = new ArrayList<>();
            changeAddressRequestData = new ArrayList<>();
        }

        public List<CheckPromoCodeCartShipmentRequest.Data> getPromoRequestData() {
            return promoRequestData;
        }

        public void setPromoRequestData(List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData) {
            this.promoRequestData = promoRequestData;
        }

        List<DataCheckoutRequest> getCheckoutRequestData() {
            return checkoutRequestData;
        }

        public void setCheckoutRequestData(List<DataCheckoutRequest> checkoutRequestData) {
            this.checkoutRequestData = checkoutRequestData;
        }

        public List<DataChangeAddressRequest> getChangeAddressRequestData() {
            return changeAddressRequestData;
        }

        public void setChangeAddressRequestData(List<DataChangeAddressRequest> changeAddressRequestData) {
            this.changeAddressRequestData = changeAddressRequestData;
        }
    }

}