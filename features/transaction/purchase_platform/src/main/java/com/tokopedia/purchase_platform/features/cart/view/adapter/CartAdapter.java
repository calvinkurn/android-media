package com.tokopedia.purchase_platform.features.cart.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.checkout.view.common.TickerAnnouncementActionListener;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct;
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShopItems;
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops;
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener;
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionViewHolder;
import com.tokopedia.purchase_platform.common.feature.seller_cashback.ShipmentSellerCashbackModel;
import com.tokopedia.purchase_platform.common.feature.seller_cashback.ShipmentSellerCashbackViewHolder;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartTickerErrorData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData;
import com.tokopedia.purchase_platform.features.cart.view.ActionListener;
import com.tokopedia.purchase_platform.features.cart.view.InsuranceItemActionListener;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartEmptyViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartLoadingViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecentViewViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecommendationViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartSectionHeaderViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartSelectAllViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartShopViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartTickerErrorViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartWishlistViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledCartItemViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledItemHeaderViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledShopViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.InsuranceCartShopViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.TickerAnnouncementViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartEmptyHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemTickerErrorHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartLoadingHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartSectionHeaderHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledCartItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledItemHeaderHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledShopHolderData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.transaction.insurance.utils.TransactionalInsuranceUtilsKt.PAGE_TYPE_CART;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ActionListener actionListener;
    private final PromoActionListener promoActionListener;
    private final InsuranceItemActionListener insuranceItemActionlistener;
    private final TickerAnnouncementActionListener tickerAnnouncementActionListener;
    private final CartItemAdapter.ActionListener cartItemActionListener;
    private List<Object> cartDataList;
    private ShipmentSellerCashbackModel shipmentSellerCashbackModel;
    private CompositeSubscription compositeSubscription;
    private ArrayList<InsuranceCartShops> allInsuranceProductsList = new ArrayList<>();

    private ArrayList<InsuranceCartShops> insuranceRecommendationList = new ArrayList<>();
    private ArrayList<InsuranceCartShops> insuranceCartList = new ArrayList<>();
    private CartEmptyHolderData cartEmptyHolderData;
    private CartLoadingHolderData cartLoadingHolderData;
    private CartWishlistAdapter cartWishlistAdapter;
    private CartRecentViewAdapter cartRecentViewAdapter;
    private int cartSelectAllViewHolderPosition = -1;
    private boolean sendInsuranceImpressionEvent = false;
    private boolean insuranceSelected;
    private String selectedInsuranceProductId = "";
    private String selectedInsuranceProductTitle = "";

    @Inject
    public CartAdapter(ActionListener actionListener,
                       PromoActionListener promoActionListener,
                       CartItemAdapter.ActionListener cartItemActionListener,
                       InsuranceItemActionListener insuranceItemActionlistener,
                       TickerAnnouncementActionListener tickerAnnouncementActionListener) {
        this.cartDataList = new ArrayList<>();
        this.actionListener = actionListener;
        this.cartItemActionListener = cartItemActionListener;
        this.promoActionListener = promoActionListener;
        this.insuranceItemActionlistener = insuranceItemActionlistener;
        this.tickerAnnouncementActionListener = tickerAnnouncementActionListener;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = cartDataList.get(position);
        if (object instanceof CartShopHolderData) {
            return CartShopViewHolder.TYPE_VIEW_ITEM_SHOP;
        } else if (object instanceof CartPromoSuggestionHolderData) {
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
        } else if (object instanceof PromoStackingData) {
            return PromoGlobalViewHolder.TYPE_VIEW_PROMO;
        } else if (object instanceof CartItemTickerErrorHolderData) {
            return CartTickerErrorViewHolder.Companion.getTYPE_VIEW_TICKER_CART_ERROR();
        } else if (object instanceof ShipmentSellerCashbackModel) {
            return ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK;
        } else if (object instanceof CartEmptyHolderData) {
            return CartEmptyViewHolder.Companion.getLAYOUT();
        } else if (object instanceof CartRecentViewHolderData) {
            return CartRecentViewViewHolder.Companion.getLAYOUT();
        } else if (object instanceof CartWishlistHolderData) {
            return CartWishlistViewHolder.Companion.getLAYOUT();
        } else if (object instanceof CartSectionHeaderHolderData) {
            return CartSectionHeaderViewHolder.Companion.getLAYOUT();
        } else if (object instanceof CartRecommendationItemHolderData) {
            return CartRecommendationViewHolder.Companion.getLAYOUT();
        } else if (object instanceof CartLoadingHolderData) {
            return CartLoadingViewHolder.Companion.getLAYOUT();
        } else if (object instanceof TickerAnnouncementHolderData) {
            return TickerAnnouncementViewHolder.Companion.getLAYOUT();
        } else if (object instanceof Boolean) {
            return CartSelectAllViewHolder.Companion.getLAYOUT();
        } else if (object instanceof InsuranceCartShops) {
            return InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP;
        } else if (object instanceof DisabledCartItemHolderData) {
            return DisabledCartItemViewHolder.Companion.getLAYOUT();
        } else if (object instanceof DisabledItemHeaderHolderData) {
            return DisabledItemHeaderViewHolder.Companion.getLAYOUT();
        } else if (object instanceof DisabledShopHolderData) {
            return DisabledShopViewHolder.Companion.getLAYOUT();
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartShopViewHolder.TYPE_VIEW_ITEM_SHOP, parent, false);
            return new CartShopViewHolder(view, actionListener, cartItemActionListener, compositeSubscription);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION, parent, false);
            return new CartPromoSuggestionViewHolder(view, promoActionListener);
        } else if (viewType == PromoGlobalViewHolder.TYPE_VIEW_PROMO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(PromoGlobalViewHolder.TYPE_VIEW_PROMO, parent, false);
            return new PromoGlobalViewHolder(view, promoActionListener);
        } else if (viewType == CartTickerErrorViewHolder.Companion.getTYPE_VIEW_TICKER_CART_ERROR()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartTickerErrorViewHolder.Companion.getTYPE_VIEW_TICKER_CART_ERROR(), parent, false);
            return new CartTickerErrorViewHolder(view, actionListener);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK, parent, false);
            return new ShipmentSellerCashbackViewHolder(view);
        } else if (viewType == CartEmptyViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartEmptyViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartEmptyViewHolder(view, actionListener);
        } else if (viewType == CartRecentViewViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartRecentViewViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartRecentViewViewHolder(view, actionListener);
        } else if (viewType == CartWishlistViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartWishlistViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartWishlistViewHolder(view, actionListener);
        } else if (viewType == CartSectionHeaderViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartSectionHeaderViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartSectionHeaderViewHolder(view, actionListener);
        } else if (viewType == CartRecommendationViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartRecommendationViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartRecommendationViewHolder(view, actionListener);
        } else if (viewType == CartLoadingViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartLoadingViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartLoadingViewHolder(view);
        } else if (viewType == CartSelectAllViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartSelectAllViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartSelectAllViewHolder(view, actionListener);
        } else if (viewType == TickerAnnouncementViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(TickerAnnouncementViewHolder.Companion.getLAYOUT(), parent, false);
            return new TickerAnnouncementViewHolder(view, tickerAnnouncementActionListener);
        } else if (viewType == InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP, parent, false);
            return new InsuranceCartShopViewHolder(view, insuranceItemActionlistener);
        } else if (viewType == DisabledCartItemViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(DisabledCartItemViewHolder.Companion.getLAYOUT(), parent, false);
            return new DisabledCartItemViewHolder(view, actionListener);
        } else if (viewType == DisabledItemHeaderViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(DisabledItemHeaderViewHolder.Companion.getLAYOUT(), parent, false);
            return new DisabledItemHeaderViewHolder(view, actionListener);
        } else if (viewType == DisabledShopViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(DisabledShopViewHolder.Companion.getLAYOUT(), parent, false);
            return new DisabledShopViewHolder(view, actionListener);
        }

        throw new RuntimeException("No view holder type found");
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            final CartShopViewHolder holderView = (CartShopViewHolder) holder;
            final CartShopHolderData data = (CartShopHolderData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            final CartPromoSuggestionViewHolder holderView = (CartPromoSuggestionViewHolder) holder;
            final CartPromoSuggestionHolderData data = (CartPromoSuggestionHolderData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (viewType == PromoGlobalViewHolder.TYPE_VIEW_PROMO) {
            final PromoGlobalViewHolder holderView = (PromoGlobalViewHolder) holder;
            final PromoStackingData data = (PromoStackingData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (viewType == CartTickerErrorViewHolder.Companion.getTYPE_VIEW_TICKER_CART_ERROR()) {
            final CartTickerErrorViewHolder holderView = (CartTickerErrorViewHolder) holder;
            final CartItemTickerErrorHolderData data = (CartItemTickerErrorHolderData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            final ShipmentSellerCashbackViewHolder holderView = (ShipmentSellerCashbackViewHolder) holder;
            final ShipmentSellerCashbackModel data = (ShipmentSellerCashbackModel) cartDataList.get(position);
            holderView.bindViewHolder(data);
        } else if (viewType == CartEmptyViewHolder.Companion.getLAYOUT()) {
            final CartEmptyViewHolder holderView = (CartEmptyViewHolder) holder;
            final CartEmptyHolderData data = (CartEmptyHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (viewType == CartRecentViewViewHolder.Companion.getLAYOUT()) {
            final CartRecentViewViewHolder holderView = (CartRecentViewViewHolder) holder;
            final CartRecentViewHolderData data = (CartRecentViewHolderData) cartDataList.get(position);
            holderView.bind(data);
            cartRecentViewAdapter = holderView.getRecentViewAdapter();
        } else if (viewType == CartWishlistViewHolder.Companion.getLAYOUT()) {
            final CartWishlistViewHolder holderView = (CartWishlistViewHolder) holder;
            final CartWishlistHolderData data = (CartWishlistHolderData) cartDataList.get(position);
            holderView.bind(data);
            cartWishlistAdapter = holderView.getWishlistAdapter();
        } else if (viewType == CartSectionHeaderViewHolder.Companion.getLAYOUT()) {
            final CartSectionHeaderViewHolder holderView = (CartSectionHeaderViewHolder) holder;
            final CartSectionHeaderHolderData data = (CartSectionHeaderHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (viewType == CartRecommendationViewHolder.Companion.getLAYOUT()) {
            final CartRecommendationViewHolder holderView = (CartRecommendationViewHolder) holder;
            final CartRecommendationItemHolderData data = (CartRecommendationItemHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (viewType == CartLoadingViewHolder.Companion.getLAYOUT()) {
            final CartLoadingViewHolder holderView = (CartLoadingViewHolder) holder;
            final CartLoadingHolderData data = (CartLoadingHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (viewType == TickerAnnouncementViewHolder.Companion.getLAYOUT()) {
            final TickerAnnouncementViewHolder holderView = (TickerAnnouncementViewHolder) holder;
            final TickerAnnouncementHolderData cartTickerData = ((TickerAnnouncementHolderData) cartDataList.get(position));
            holderView.bind(cartTickerData);
        } else if (viewType == CartSelectAllViewHolder.Companion.getLAYOUT()) {
            final CartSelectAllViewHolder holderView = ((CartSelectAllViewHolder) holder);
            Boolean isAllSelected = (Boolean) cartDataList.get(position);
            holderView.bind(isAllSelected);
        } else if (getItemViewType(position) == InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP) {
            final InsuranceCartShopViewHolder insuranceCartShopViewHolder = (InsuranceCartShopViewHolder) holder;
            final InsuranceCartShops insuranceCartShops = (InsuranceCartShops) cartDataList.get(position);
            insuranceCartShopViewHolder.bindData(insuranceCartShops, position, PAGE_TYPE_CART);
        } else if (viewType == DisabledItemHeaderViewHolder.Companion.getLAYOUT()) {
            final DisabledItemHeaderViewHolder holderView = (DisabledItemHeaderViewHolder) holder;
            final DisabledItemHeaderHolderData data = (DisabledItemHeaderHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (viewType == DisabledShopViewHolder.Companion.getLAYOUT()) {
            final DisabledShopViewHolder holderView = (DisabledShopViewHolder) holder;
            final DisabledShopHolderData data = (DisabledShopHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (viewType == DisabledCartItemViewHolder.Companion.getLAYOUT()) {
            final DisabledCartItemViewHolder holderView = (DisabledCartItemViewHolder) holder;
            final DisabledCartItemHolderData data = (DisabledCartItemHolderData) cartDataList.get(position);
            holderView.bind(data);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder instanceof InsuranceCartShopViewHolder && !sendInsuranceImpressionEvent) {
            sendInsuranceImpressionEvent = true;
            insuranceItemActionlistener.sendEventInsuranceImpression(((InsuranceCartShopViewHolder) holder).getProductTitle());
        }
    }



    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.getItemViewType() == CartRecommendationViewHolder.Companion.getLAYOUT()) {
            ((CartRecommendationViewHolder) holder).clearImage();
        }
    }

    @Override
    public int getItemCount() {
        return cartDataList.size();
    }

    public void unsubscribeSubscription() {
        compositeSubscription.unsubscribe();
    }

    public void addAvailableDataList(List<ShopGroupAvailableData> shopGroupAvailableDataList) {
        for (ShopGroupAvailableData shopGroupAvailableData : shopGroupAvailableDataList) {
            if (shopGroupAvailableData.getCartItemDataList() != null && shopGroupAvailableData.getCartItemDataList().size() > 0) {
                CartShopHolderData cartShopHolderData = new CartShopHolderData();
                cartShopHolderData.setShopGroupAvailableData(shopGroupAvailableData);
                if (shopGroupAvailableData.isError()) {
                    cartShopHolderData.setAllSelected(false);
                } else {
                    if (shopGroupAvailableData.isChecked()) {
                        cartShopHolderData.setAllSelected(true);
                    } else if (shopGroupAvailableData.getCartItemDataList() != null && shopGroupAvailableData.getCartItemDataList().size() > 1) {
                        for (CartItemHolderData cartItemHolderData : shopGroupAvailableData.getCartItemDataList()) {
                            if (cartItemHolderData.isSelected()) {
                                cartShopHolderData.setPartialSelected(true);
                                break;
                            }
                        }
                    }
                }
                cartShopHolderData.setShopGroupAvailableData(shopGroupAvailableData);
                cartDataList.add(cartShopHolderData);
            }
        }
    }

    public void addNotAvailableHeader(DisabledItemHeaderHolderData disabledItemHeaderHolderData) {
        cartDataList.add(disabledItemHeaderHolderData);
    }

    public void addNotAvailableShop(DisabledShopHolderData disabledShopHolderData) {
        cartDataList.add(disabledShopHolderData);
    }

    public void addNotAvailableProduct(DisabledCartItemHolderData disabledCartItemHolderData) {
        cartDataList.add(disabledCartItemHolderData);
    }

    public List<CartShopHolderData> getAllShopGroupDataList() {
        List<CartShopHolderData> cartShopHolderDataFinalList = new ArrayList<>();
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof CartShopHolderData) {
                cartShopHolderDataFinalList.add((CartShopHolderData) object);
            }
        }
        return cartShopHolderDataFinalList;
    }

    public List<CartItemData> getSelectedCartItemData() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if ((cartShopHolderData.isPartialSelected() || cartShopHolderData.isAllSelected()) &&
                            cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                            if (cartItemHolderData.isSelected() && !cartItemHolderData.getCartItemData().isError()) {
                                cartItemDataList.add(cartItemHolderData.getCartItemData());
                            }
                        }
                    }
                }
            }
        }

        return cartItemDataList;
    }

    public List<CartShopHolderData> getSelectedCartShopHolderData() {
        List<CartShopHolderData> cartShopHolderDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if ((cartShopHolderData.isPartialSelected() || cartShopHolderData.isAllSelected()) &&
                            cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                        cartShopHolderDataList.add(cartShopHolderData);
                    }
                }
            }
        }

        return cartShopHolderDataList;
    }

    public List<CartItemData> getAllCartItemData() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                            cartItemDataList.add(cartItemHolderData.getCartItemData());
                        }
                    }
                } else if (data instanceof DisabledCartItemHolderData) {
                    cartItemDataList.add(((DisabledCartItemHolderData) data).getData());
                } else if (data instanceof CartWishlistHolderData || data instanceof CartRecentViewHolderData || data instanceof CartRecommendationItemHolderData) {
                    break;
                }
            }
        }

        return cartItemDataList;
    }

    public List<CartItemData> getAllAvailableCartItemData() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                            cartItemDataList.add(cartItemHolderData.getCartItemData());
                        }
                    }
                } else if (data instanceof CartWishlistHolderData || data instanceof CartRecentViewHolderData || data instanceof CartRecommendationItemHolderData) {
                    break;
                }
            }
        }

        return cartItemDataList;
    }

    public List<CartItemData> getAllDisabledCartItemData() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof DisabledCartItemHolderData) {
                    DisabledCartItemHolderData cartShopHolderData = (DisabledCartItemHolderData) data;
                    cartItemDataList.add(cartShopHolderData.getData());
                } else if (data instanceof CartWishlistHolderData ||
                        data instanceof CartRecentViewHolderData ||
                        data instanceof CartRecommendationItemHolderData) {
                    break;
                }
            }
        }

        return cartItemDataList;
    }

    public List<String> getAllCartItemProductId() {
        List<String> productIdList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                            productIdList.add(cartItemHolderData.getCartItemData().getOriginData().getProductId());
                        }
                    }
                } else if (data instanceof DisabledCartItemHolderData) {
                    productIdList.add(((DisabledCartItemHolderData) data).getProductId());
                }
            }
        }

        return productIdList;
    }

    public List<CartItemHolderData> getAllCartItemHolderData() {
        List<CartItemHolderData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                        cartItemDataList.addAll(cartShopHolderData.getShopGroupAvailableData().getCartItemDataList());
                    }
                }
            }
        }

        return cartItemDataList;
    }

    public void setAllShopSelected(boolean selected) {
        if (cartDataList != null) {
            for (int i = 0; i < cartDataList.size(); i++) {
                if (cartDataList.get(i) instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = ((CartShopHolderData) cartDataList.get(i));
                    if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null &&
                            cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().size() == 1) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                            if (cartItemHolderData.getCartItemData().isError() && cartItemHolderData.getCartItemData().isSingleChild()) {
                                setShopSelected(i, false);
                            } else {
                                setShopSelected(i, selected);
                            }
                        }
                    } else {
                        setShopSelected(i, selected);
                    }
                }
            }
        }
    }

    public void setShopSelected(int position, boolean selected) {
        Object object = cartDataList.get(position);
        if (object instanceof CartShopHolderData) {
            CartShopHolderData cartShopHolderData = (CartShopHolderData) object;
            cartShopHolderData.setAllSelected(selected);
            for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                cartItemHolderData.setSelected(selected);
            }
        }
    }

    public boolean setItemSelected(int position, int parentPosition, boolean selected) {
        boolean needToUpdateParent = false;
        Object object = cartDataList.get(parentPosition);
        if (object instanceof CartShopHolderData) {
            CartShopHolderData cartShopHolderData = (CartShopHolderData) object;
            boolean shopAlreadySelected = cartShopHolderData.isAllSelected() || cartShopHolderData.isPartialSelected();
            int selectedCount = 0;
            for (int i = 0; i < cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().size(); i++) {
                CartItemHolderData cartItemHolderData = cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().get(i);
                if (i == position) {
                    cartItemHolderData.setSelected(selected);
                }

                if (cartItemHolderData.isSelected()) {
                    selectedCount++;
                }
            }

            if (selectedCount == 0) {
                cartShopHolderData.setAllSelected(false);
                cartShopHolderData.setPartialSelected(false);
                needToUpdateParent = shopAlreadySelected;
            } else if (selectedCount > 0 && selectedCount < cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().size()) {
                cartShopHolderData.setAllSelected(false);
                cartShopHolderData.setPartialSelected(true);
                needToUpdateParent = !shopAlreadySelected;
            } else {
                cartShopHolderData.setAllSelected(true);
                cartShopHolderData.setPartialSelected(false);
                needToUpdateParent = !shopAlreadySelected;
            }

            // Check does has cash back item
            boolean hasCashbackItem = false;
            for (CartItemHolderData data : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                if (data.getCartItemData().getOriginData().isCashBack()) {
                    hasCashbackItem = true;
                    break;
                }
            }
            // Check is it the last cash back item to uncheck & still contain non cash back item
            boolean isTheLastCashbackItem = true;
            boolean hasUncheckedItem = false;
            if (!selected && hasCashbackItem) {
                // Check does it still contain unchecked item
                for (CartItemHolderData data : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                    if (data.isSelected() && !data.getCartItemData().getOriginData().isCashBack()) {
                        hasUncheckedItem = true;
                        break;
                    }
                }
                // Check is it the last cash back item to be unchecked
                for (int j = 0; j < cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().size(); j++) {
                    CartItemHolderData data = cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().get(j);
                    if (j != position && data.isSelected() && data.getCartItemData().getOriginData().isCashBack()) {
                        isTheLastCashbackItem = false;
                        break;
                    }
                }
            }
            if (hasUncheckedItem && isTheLastCashbackItem) {
                needToUpdateParent = true;
            }
        }

        return needToUpdateParent;
    }

    public void increaseQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupAvailableData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().increaseQuantity();
        }
        checkForShipmentForm();
    }

    public void decreaseQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupAvailableData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().decreaseQuantity();
        }
        checkForShipmentForm();
    }

    public void resetQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupAvailableData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().resetQuantity();
        }
        checkForShipmentForm();
    }

    public void notifyItems(int position) {
        Object itemData = cartDataList.get(position);
        String itemDataParentId = ((CartItemHolderData) itemData).getCartItemData().getOriginData().getParentId();
        notifyItemChanged(position);
        for (Object object : cartDataList) {
            if (object instanceof CartItemHolderData) {
                String parentId = ((CartItemHolderData) object).getCartItemData().getOriginData().getParentId();
                if (parentId.equals(itemDataParentId)) {
                    notifyItemChanged(cartDataList.indexOf(object));
                }
            }
        }
    }

    public void addPromoSuggestion(CartPromoSuggestionHolderData cartPromoSuggestionHolderData) {
        cartDataList.add(cartPromoSuggestionHolderData);
        checkForShipmentForm();
    }

    public void removeInsuranceDataItem(List<Long> productIdList) {
        try {
            for (Long productId : productIdList) {
                int position = 0;
                for (Object item : cartDataList) {
                    position++;
                    if (item instanceof InsuranceCartShops) {
                        for (InsuranceCartShopItems insuranceCartShopItems : ((InsuranceCartShops) item).getShopItemsList()) {
                            if (insuranceCartShopItems.getProductId() == productId) {
                                cartDataList.remove(item);
                                notifyItemRemoved(position);
                                break;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addInsuranceDataList(InsuranceCartShops insuranceCartShops, boolean isRecommendation) {
        allInsuranceProductsList.clear();
        allInsuranceProductsList.add(insuranceCartShops);

        if (isRecommendation) {
            insuranceRecommendationList.clear();
            insuranceRecommendationList.add(insuranceCartShops);
        } else {
            insuranceCartList.clear();
            insuranceCartList.add(insuranceCartShops);
        }

        int insuranceIndex = 0;

        for (Object item : cartDataList) {
            if (item instanceof CartEmptyHolderData ||
                    item instanceof CartShopHolderData) {
                insuranceIndex = cartDataList.indexOf(item);
            }
        }

        if (insuranceCartShops != null) {
            cartDataList.add(++insuranceIndex, insuranceCartShops);
        }

        notifyDataSetChanged();
    }

    public List<InsuranceCartShops> getSelectedRecommendedInsuranceList() {

        List<InsuranceCartShops> insuranceCartShopsList = new ArrayList<>();
        for (InsuranceCartShops insuranceCartShops : insuranceRecommendationList) {

            if (insuranceCartShops != null &&
                    insuranceCartShops.getShopItemsList().size() > 0 &&
                    insuranceCartShops.getShopItemsList().get(0) != null &&
                    insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().size() > 0 &&
                    insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0) != null &&
                    insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0).getOptIn()) {

                insuranceCartShopsList.add(insuranceCartShops);
            }
        }
        return insuranceCartShopsList;
    }

    public ArrayList<InsuranceCartDigitalProduct> isInsuranceCartProductUnSelected() {

        insuranceSelected = true;
        ArrayList<InsuranceCartDigitalProduct> insuranceCartDigitalProductArrayList = new ArrayList<>();
        for (InsuranceCartShops insuranceCartShops : insuranceCartList) {
            if (insuranceCartShops != null &&
                    !insuranceCartShops.getShopItemsList().isEmpty()) {
                for (InsuranceCartShopItems insuranceCartShopItems : insuranceCartShops.getShopItemsList()) {
                    if (insuranceCartShopItems.getDigitalProductList() != null &&
                            !insuranceCartShopItems.getDigitalProductList().isEmpty()) {
                        for (InsuranceCartDigitalProduct insuranceCartDigitalProduct : insuranceCartShopItems.getDigitalProductList()) {
                            selectedInsuranceProductTitle = insuranceCartDigitalProduct.getProductInfo().getTitle();
                            selectedInsuranceProductId = insuranceCartDigitalProduct.getProductId();
                            if (!insuranceCartDigitalProduct.getOptIn()) {
                                insuranceSelected = false;
                                insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct);
                            }
                        }
                    } else {
                        insuranceSelected = false;
                    }
                }
            } else {
                insuranceSelected = false;
            }

        }
        return insuranceCartDigitalProductArrayList;
    }

    public String getSelectedInsuranceProductId() {
        return selectedInsuranceProductId;
    }

    public String getSelectedInsuranceProductTitle() {
        return selectedInsuranceProductTitle;
    }

    public boolean isInsuranceSelected(){
        return insuranceSelected;
    }

    public ArrayList<InsuranceCartShops> getInsuranceCartShops() {
        return allInsuranceProductsList;
    }

    public void resetData() {
        cartDataList.clear();
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void updateItemPromoStackVoucher(PromoStackingData promoStackingData) {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof PromoStackingData) {
                cartDataList.set(i, promoStackingData);
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestionHolderData) {
                ((CartPromoSuggestionHolderData) object).setVisible(false);
                notifyItemChanged(i);
            }
        }
    }

    public void addPromoStackingVoucherData(PromoStackingData promoStackingData) {
        cartDataList.add(promoStackingData);
        checkForShipmentForm();
    }

    public void removePromoStackingVoucherData() {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof PromoStackingData) {
                cartDataList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void addCartTickerError(CartItemTickerErrorHolderData cartItemTickerErrorHolderData) {
        cartDataList.add(cartItemTickerErrorHolderData);
        checkForShipmentForm();
    }

    public void addCartEmptyData() {
        if (cartEmptyHolderData == null) {
            cartEmptyHolderData = new CartEmptyHolderData();
        }
        cartDataList.add(cartEmptyHolderData);
    }

    public void addCartRecentViewData(CartSectionHeaderHolderData cartSectionHeaderHolderData,
                                      CartRecentViewHolderData cartRecentViewHolderData) {
        int recentViewIndex = 0;
        for (Object item : cartDataList) {
            if (item instanceof CartEmptyHolderData ||
                    item instanceof CartShopHolderData ||
                    item instanceof ShipmentSellerCashbackModel ||
                    item instanceof InsuranceCartShops ||
                    item instanceof DisabledCartItemHolderData) {
                recentViewIndex = cartDataList.indexOf(item); // 3
            }
        }
        cartDataList.add(++recentViewIndex, cartSectionHeaderHolderData);
        cartDataList.add(++recentViewIndex, cartRecentViewHolderData);
        notifyDataSetChanged();
    }

    public void addCartWishlistData(CartSectionHeaderHolderData cartSectionHeaderHolderData,
                                    CartWishlistHolderData cartWishlistHolderData) {
        int wishlistIndex = 0;
        for (Object item : cartDataList) {
            if (item instanceof CartEmptyHolderData ||
                    item instanceof CartShopHolderData ||
                    item instanceof ShipmentSellerCashbackModel ||
                    item instanceof CartRecentViewHolderData ||
                    item instanceof InsuranceCartShops||
                    item instanceof DisabledCartItemHolderData) {

                wishlistIndex = cartDataList.indexOf(item);
            }
        }
        cartDataList.add(++wishlistIndex, cartSectionHeaderHolderData);
        cartDataList.add(++wishlistIndex, cartWishlistHolderData);
        notifyDataSetChanged();
    }

    public void addCartRecommendationData(CartSectionHeaderHolderData cartSectionHeaderHolderData,
                                          List<CartRecommendationItemHolderData> cartRecommendationItemHolderDataList) {
        int recommendationIndex = 0;
        for (Object item : cartDataList) {
            if (item instanceof CartEmptyHolderData ||
                    item instanceof CartShopHolderData ||
                    item instanceof ShipmentSellerCashbackModel ||
                    item instanceof CartRecentViewHolderData ||
                    item instanceof CartWishlistHolderData ||
                    item instanceof CartRecommendationItemHolderData ||
                    item instanceof InsuranceCartShops||
                    item instanceof DisabledCartItemHolderData) {
                recommendationIndex = cartDataList.indexOf(item);
            }
        }

        if (cartSectionHeaderHolderData != null) {
            cartDataList.add(++recommendationIndex, cartSectionHeaderHolderData);
        }

        cartDataList.addAll(++recommendationIndex, cartRecommendationItemHolderDataList);
        notifyDataSetChanged();
    }

    public void removeCartEmptyData() {
        cartDataList.remove(cartEmptyHolderData);
        notifyDataSetChanged();

        cartEmptyHolderData = null;
    }

    public void checkForShipmentForm() {
        boolean canProcess = true;
        int checkedCount = 0;
        for (Object object : cartDataList) {
            if (object instanceof CartShopHolderData) {
                CartShopHolderData cartShopHolderData = (CartShopHolderData) object;
                if (!cartShopHolderData.getShopGroupAvailableData().isError()) {
                    if (cartShopHolderData.isAllSelected()) {
                        checkedCount += cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().size();
                    } else if (cartShopHolderData.isPartialSelected()) {
                        if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                            for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                                if (cartItemHolderData.isSelected()) {
                                    checkedCount++;
                                    if (cartItemHolderData.getErrorFormItemValidationTypeValue() != CartItemHolderData.CREATOR.getERROR_EMPTY() ||
                                            cartItemHolderData.getCartItemData().isError()) {
                                        canProcess = false;
                                        break;
                                    }
                                }
                            }
                            if (!canProcess) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (canProcess && checkedCount > 0) {
            actionListener.onCartDataEnableToCheckout();
        } else {
            String errorMessage = actionListener.getDefaultCartErrorMessage();
            for (Object object : cartDataList) {
                if (object instanceof CartItemTickerErrorHolderData) {
                    CartTickerErrorData cartTickerErrorData = ((CartItemTickerErrorHolderData) object).getCartTickerErrorData();
                    if (!TextUtils.isEmpty(cartTickerErrorData.getErrorInfo())) {
                        errorMessage = cartTickerErrorData.getErrorInfo();
                    }
                    break;
                }
            }

            actionListener.onCartDataDisableToCheckout(errorMessage);
        }
    }

    public void updateShipmentSellerCashback(double cashback) {
        if (cashback > 0) {
            if (shipmentSellerCashbackModel == null || cartDataList.indexOf(shipmentSellerCashbackModel) == -1) {
                int index = 0;
                for (Object item : cartDataList) {
                    if (item instanceof CartShopHolderData) {
                        index = cartDataList.indexOf(item);
                    }
                }
                shipmentSellerCashbackModel = new ShipmentSellerCashbackModel();
                shipmentSellerCashbackModel.setVisible(true);
                shipmentSellerCashbackModel.setSellerCashback(CurrencyFormatUtil.convertPriceValueToIdrFormat((long) cashback, false));
                cartDataList.add(++index, shipmentSellerCashbackModel);
                notifyItemInserted(index);
            } else {
                shipmentSellerCashbackModel.setVisible(true);
                shipmentSellerCashbackModel.setSellerCashback(CurrencyFormatUtil.convertPriceValueToIdrFormat((long) cashback, false));
                int index = cartDataList.indexOf(shipmentSellerCashbackModel);
                notifyItemChanged(index);
            }
        } else {
            if (shipmentSellerCashbackModel != null) {
                int index = cartDataList.indexOf(shipmentSellerCashbackModel);
                cartDataList.remove(shipmentSellerCashbackModel);
                notifyItemRemoved(index);
                shipmentSellerCashbackModel = null;
            }
        }

        int index = cartDataList.indexOf(shipmentSellerCashbackModel);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    public void notifyByProductId(String productId, boolean isWishlisted) {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object obj = cartDataList.get(i);
            if (obj instanceof CartShopHolderData) {
                CartShopHolderData cartShopHolderData = (CartShopHolderData) cartDataList.get(i);
                if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                    for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                        if (cartItemHolderData.getCartItemData().getOriginData().getProductId().equals(productId)) {
                            cartItemHolderData.getCartItemData().getOriginData().setWishlisted(isWishlisted);
                            notifyItemChanged(i);
                            break;
                        }
                    }
                }
            } else if (obj instanceof DisabledCartItemHolderData) {
                DisabledCartItemHolderData disabledCartItemHolderData = (DisabledCartItemHolderData) obj;
                if (disabledCartItemHolderData.getProductId().equals(productId)) {
                    disabledCartItemHolderData.setWishlisted(isWishlisted);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public PromoData getPromoData() {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof PromoData) {
                return (PromoData) cartDataList.get(i);
            }
        }
        return null;
    }

    public PromoStackingData getPromoStackingGlobalData() {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof PromoStackingData) {
                return (PromoStackingData) cartDataList.get(i);
            }
        }
        return null;
    }

    public CartShopHolderData getCartShopHolderDataByIndex(int index) {
        if (cartDataList.get(index) instanceof CartShopHolderData) {
            return (CartShopHolderData) cartDataList.get(index);
        }
        return null;
    }

    public List<CartShopHolderData> getAllCartShopHolderData() {
        List<CartShopHolderData> cartShopHolderDataList = new ArrayList<>();
        for (int i = 0; i < cartDataList.size(); i++) {
            CartShopHolderData cartShopHolderData = getCartShopHolderDataByIndex(i);
            if (cartShopHolderData != null) {
                cartShopHolderDataList.add(cartShopHolderData);
            }
        }

        return cartShopHolderDataList;
    }

    public void notifyWishlist(String productId, boolean isWishlist) {
        for (Object object : cartDataList) {
            if (object instanceof CartWishlistHolderData) {
                List<CartWishlistItemHolderData> wishlist = ((CartWishlistHolderData) object).getWishList();
                for (CartWishlistItemHolderData data : wishlist) {
                    if (data.getId().equals(productId)) {
                        data.setWishlist(isWishlist);
                        if (cartWishlistAdapter != null) {
                            cartWishlistAdapter.notifyItemChanged(wishlist.indexOf(data));
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    public void notifyRecentView(String productId, boolean isWishlist) {
        for (Object object : cartDataList) {
            if (object instanceof CartRecentViewHolderData) {
                List<CartRecentViewItemHolderData> recentViews = ((CartRecentViewHolderData) object).getRecentViewList();
                for (CartRecentViewItemHolderData data : recentViews) {
                    if (data.getId().equals(productId)) {
                        data.setWishlist(isWishlist);
                        if (cartRecentViewAdapter != null) {
                            cartRecentViewAdapter.notifyItemChanged(recentViews.indexOf(data));
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    public void notifyRecommendation(String productId, boolean isWishlist) {
        for (int i = cartDataList.size() - 1; i >= 0; i--) {
            Object object = cartDataList.get(i);
            if (object instanceof CartRecommendationItemHolderData) {
                if (String.valueOf(((CartRecommendationItemHolderData) object).getRecommendationItem().getProductId()).equals(productId)) {
                    ((CartRecommendationItemHolderData) object).getRecommendationItem().setWishlist(isWishlist);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void addCartLoadingData() {
        if (cartLoadingHolderData == null) {
            cartLoadingHolderData = new CartLoadingHolderData();
        }
        cartDataList.add(cartLoadingHolderData);
        notifyItemInserted(cartDataList.indexOf(cartLoadingHolderData));
    }

    public void removeCartLoadingData() {
        int index = cartDataList.indexOf(cartLoadingHolderData);
        cartDataList.remove(cartLoadingHolderData);
        notifyItemRemoved(index);
    }

    public void removeCartItemById(List<Integer> cartIds, Context context) {
        // Store item first before remove item to prevent ConcurrentModificationException
        List<Object> toBeRemovedData = new ArrayList<>();
        DisabledItemHeaderHolderData disabledItemHeaderHolderData = null;
        CartItemTickerErrorHolderData cartItemTickerErrorHolderData = null;
        for (int i = 0; i < cartDataList.size(); i++) {
            Object obj = cartDataList.get(i);
            if (obj instanceof CartShopHolderData) {
                List<CartItemHolderData> cartItemHolderDataList = ((CartShopHolderData) obj).getShopGroupAvailableData().getCartItemDataList();
                List<CartItemHolderData> toBeRemovedCartItemHolderData = new ArrayList<>();
                for (CartItemHolderData cartItemHolderData : cartItemHolderDataList) {
                    if (cartIds.contains(cartItemHolderData.getCartItemData().getOriginData().getCartId())) {
                        toBeRemovedCartItemHolderData.add(cartItemHolderData);
                    }
                }
                for (CartItemHolderData cartItemHolderData : toBeRemovedCartItemHolderData) {
                    cartItemHolderDataList.remove(cartItemHolderData);
                }
                if (cartItemHolderDataList.size() == 0) {
                    toBeRemovedData.add(obj);
                }
            } else if (obj instanceof DisabledCartItemHolderData) {
                if (cartIds.contains(((DisabledCartItemHolderData) obj).getCartId())) {
                    Object before = cartDataList.get(i - 1);
                    Object after = null;
                    if ((i + 1) < cartDataList.size()) {
                        after = cartDataList.get(i + 1);
                    }
                    if (before instanceof DisabledShopHolderData) {
                        if (!(after instanceof DisabledCartItemHolderData)) {
                            toBeRemovedData.add(before);
                        }
                        toBeRemovedData.add(obj);
                    } else if (before instanceof DisabledCartItemHolderData) {
                        if (!(after instanceof DisabledCartItemHolderData)) {
                            ((DisabledCartItemHolderData) before).setShowDivider(false);
                        }
                        toBeRemovedData.add(obj);
                    }
                }
            } else if (obj instanceof DisabledItemHeaderHolderData) {
                disabledItemHeaderHolderData = (DisabledItemHeaderHolderData) obj;
            } else if (obj instanceof CartItemTickerErrorHolderData) {
                cartItemTickerErrorHolderData = (CartItemTickerErrorHolderData) obj;
            } else if (obj instanceof CartRecentViewHolderData ||
                    obj instanceof CartWishlistHolderData ||
                    obj instanceof CartRecommendationItemHolderData) {
                break;
            }
        }

        for (Object cartShopHolderData : toBeRemovedData) {
            cartDataList.remove(cartShopHolderData);
        }

        if (cartItemTickerErrorHolderData != null || disabledItemHeaderHolderData != null) {
            int errorItemCount = 0;
            for (Object object : cartDataList) {
                if (object instanceof CartShopHolderData) {
                    List<CartItemHolderData> cartItemHolderDataList = ((CartShopHolderData) object).getShopGroupAvailableData().getCartItemDataList();
                    for (CartItemHolderData cartItemHolderData : cartItemHolderDataList) {
                        if (cartItemHolderData.getCartItemData().isError()) {
                            errorItemCount++;
                        }
                    }
                } else if (object instanceof DisabledCartItemHolderData) {
                    errorItemCount++;
                } else if (object instanceof CartRecentViewHolderData ||
                        object instanceof CartWishlistHolderData ||
                        object instanceof CartRecommendationItemHolderData) {
                    break;
                }
            }

            if (errorItemCount > 0) {
                if (context != null) {
                    if (cartItemTickerErrorHolderData != null) {
                        cartItemTickerErrorHolderData.getCartTickerErrorData().setErrorInfo(
                                String.format(context.getString(R.string.cart_error_message), errorItemCount)
                        );
                    }
                    if (disabledItemHeaderHolderData != null) {
                        disabledItemHeaderHolderData.setDisabledItemCount(errorItemCount);
                    }
                }
            } else {
                cartDataList.remove(cartItemTickerErrorHolderData);
                cartDataList.remove(disabledItemHeaderHolderData);
            }
        }

        notifyDataSetChanged();
    }

    public void addCartTicker(TickerAnnouncementHolderData tickerAnnouncementHolderData) {
        cartDataList.add(0, tickerAnnouncementHolderData);
    }

    @Deprecated
    public void addCartSelectAll(Boolean isAllSelected) {
        int positionToPlaced = 0;
        if (!cartDataList.isEmpty() && cartDataList.get(0) instanceof TickerAnnouncementHolderData) {
            positionToPlaced += 1;
        }
        if (cartDataList.size() >= positionToPlaced + 1) {
            Object currentAllSelected = cartDataList.get(positionToPlaced);
            if (currentAllSelected instanceof Boolean) {
                cartDataList.remove(positionToPlaced);
                cartDataList.add(positionToPlaced, isAllSelected);
                cartSelectAllViewHolderPosition = positionToPlaced;
                notifyItemChanged(positionToPlaced);
                return;
            }
        }
        cartDataList.add(positionToPlaced, isAllSelected);
        cartSelectAllViewHolderPosition = positionToPlaced;
        notifyItemInserted(positionToPlaced);
    }

    @Deprecated
    public int getCartSelectAllViewHolderPosition() {
        return cartSelectAllViewHolderPosition;
    }

    public int getDisabledItemHeaderPosition() {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof DisabledItemHeaderHolderData) {
                return i;
            }
        }
        return 0;
    }
}
