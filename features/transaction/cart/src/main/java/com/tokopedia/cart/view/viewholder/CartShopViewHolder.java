package com.tokopedia.cart.view.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.cart.R;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.purchase_platform.common.utils.CheckboxWatcher;
import com.tokopedia.cart.view.ActionListener;
import com.tokopedia.cart.view.adapter.CartItemAdapter;
import com.tokopedia.cart.view.uimodel.CartItemHolderData;
import com.tokopedia.cart.view.uimodel.CartShopHolderData;
import com.tokopedia.unifycomponents.ImageUnify;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifyprinciples.Typography;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.purchase_platform.common.utils.CheckboxWatcher.CHECKBOX_WATCHER_DEBOUNCE_TIME;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartShopViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_VIEW_ITEM_SHOP = R.layout.item_shop;

    private LinearLayout llWarningAndError;
    private FrameLayout flShopItemContainer;
    private LinearLayout llShopContainer;
    private CheckBox cbSelectShop;
    private Typography tvShopName;
    private ImageView imgShopBadge;
    private Typography tvFulfillDistrict;
    private RecyclerView rvCartItem;

    private LinearLayout layoutError;
    private Ticker tickerError;
    private LinearLayout layoutWarning;
    private Ticker tickerWarning;

    private Typography separatorPreOrder;
    private Label labelPreOrder;
    private Typography separatorIncident;
    private Label labelIncident;
    private Typography separatorFreeShipping;
    private ImageView imgFreeShipping;
    private Label labelFulfillment;

    private ActionListener actionListener;
    private CartItemAdapter.ActionListener cartItemAdapterListener;
    private CartItemAdapter cartItemAdapter;
    private CompositeSubscription compositeSubscription;
    private TickerPromoStackingCheckoutView tickerPromoStackingCheckoutView;
    private CheckboxWatcher.CheckboxWatcherListener checkboxWatcherListener = null;

    public CartShopViewHolder(View itemView, ActionListener actionListener,
                              CartItemAdapter.ActionListener cartItemAdapterListener,
                              CompositeSubscription compositeSubscription) {
        super(itemView);
        this.actionListener = actionListener;
        this.cartItemAdapterListener = cartItemAdapterListener;
        this.compositeSubscription = compositeSubscription;

        llWarningAndError = itemView.findViewById(R.id.ll_warning_and_error);
        flShopItemContainer = itemView.findViewById(R.id.fl_shop_item_container);
        llShopContainer = itemView.findViewById(R.id.ll_shop_container);
        cbSelectShop = itemView.findViewById(R.id.cb_select_shop);
        tvShopName = itemView.findViewById(R.id.tv_shop_name);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);
        rvCartItem = itemView.findViewById(R.id.rv_cart_item);

        layoutError = itemView.findViewById(R.id.layout_error);
        tickerError = itemView.findViewById(R.id.ticker_error);
        layoutWarning = itemView.findViewById(R.id.layout_warning);
        tickerWarning = itemView.findViewById(R.id.ticker_warning);

        tvFulfillDistrict = itemView.findViewById(R.id.tv_fulfill_district);

        separatorPreOrder = itemView.findViewById(R.id.separator_pre_order);
        labelPreOrder = itemView.findViewById(R.id.label_pre_order);
        separatorIncident = itemView.findViewById(R.id.separator_incident);
        labelIncident = itemView.findViewById(R.id.label_incident);
        separatorFreeShipping = itemView.findViewById(R.id.separator_free_shipping);
        imgFreeShipping = itemView.findViewById(R.id.img_free_shipping);
        labelFulfillment = itemView.findViewById(R.id.label_fulfillment);

        initCheckboxWatcherDebouncer(compositeSubscription);
    }

    private void initCheckboxWatcherDebouncer(CompositeSubscription compositeSubscription) {
        compositeSubscription.add(Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                checkboxWatcherListener = new CheckboxWatcher.CheckboxWatcherListener() {
                    @Override
                    public void onCheckboxChanged(Boolean isChecked) {
                        subscriber.onNext(isChecked);
                    }
                };
            }
        }).debounce(CHECKBOX_WATCHER_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean isChecked) {
                        itemCheckboxWatcherAction(isChecked);
                    }
                }));
    }

    private void itemCheckboxWatcherAction(Boolean isChecked) {
        actionListener.onCartShopNameChecked(isChecked);
    }

    public void bindData(CartShopHolderData cartShopHolderData, final int position) {
        if (cartShopHolderData.getShopGroupAvailableData().isError() || cartShopHolderData.getShopGroupAvailableData().isWarning()) {
            llWarningAndError.setVisibility(View.VISIBLE);
        } else {
            llWarningAndError.setVisibility(View.GONE);
        }
        renderErrorItemHeader(cartShopHolderData);
        renderWarningItemHeader(cartShopHolderData);

        String shopName = cartShopHolderData.getShopGroupAvailableData().getShopName();
        tvShopName.setText(shopName);
        tvShopName.setOnClickListener(v -> actionListener.onCartShopNameClicked(
                cartShopHolderData.getShopGroupAvailableData().getShopId(),
                cartShopHolderData.getShopGroupAvailableData().getShopName())
        );

        if (cartShopHolderData.getShopGroupAvailableData().isOfficialStore() || cartShopHolderData.getShopGroupAvailableData().isGoldMerchant()) {
            if (!cartShopHolderData.getShopGroupAvailableData().getShopBadge().isEmpty()) {
                ImageHandler.loadImageWithoutPlaceholder(imgShopBadge, cartShopHolderData.getShopGroupAvailableData().getShopBadge());
                imgShopBadge.setVisibility(View.VISIBLE);
            }
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        cartItemAdapter = new CartItemAdapter(cartItemAdapterListener, compositeSubscription, getAdapterPosition());
        cartItemAdapter.addDataList(cartShopHolderData.getShopGroupAvailableData().getCartItemDataList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rvCartItem.getContext());
        rvCartItem.setLayoutManager(linearLayoutManager);
        rvCartItem.setAdapter(cartItemAdapter);
        ((SimpleItemAnimator) rvCartItem.getItemAnimator()).setSupportsChangeAnimations(false);

        cbSelectShop.setEnabled(!cartShopHolderData.getShopGroupAvailableData().isError());
        cbSelectShop.setChecked(cartShopHolderData.isAllSelected());
        cbSelectShop.setOnClickListener(cbSelectShopClickListener(cartShopHolderData));
        cbSelectShop.setOnCheckedChangeListener(new CheckboxWatcher(checkboxWatcherListener));
        labelFulfillment.setVisibility(cartShopHolderData.getShopGroupAvailableData().isFulfillment() ?
                View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getFulfillmentName())) {
            tvFulfillDistrict.setVisibility(View.VISIBLE);
            tvFulfillDistrict.setText(cartShopHolderData.getShopGroupAvailableData().getFulfillmentName());
        } else {
            tvFulfillDistrict.setVisibility(View.GONE);
        }

        renderPreOrder(cartShopHolderData);
        renderIncidentLabel(cartShopHolderData);
        renderFreeShipping(cartShopHolderData);
    }

    private void renderErrorItemHeader(CartShopHolderData data) {
        if (data.getShopGroupAvailableData().isError()) {
            cbSelectShop.setEnabled(false);
            flShopItemContainer.setForeground(ContextCompat.getDrawable(flShopItemContainer.getContext(), R.drawable.fg_disabled_item));
            llShopContainer.setBackgroundResource(R.drawable.bg_error_shop);

            if (!TextUtils.isEmpty(data.getShopGroupAvailableData().getErrorTitle())) {
                String errorDescription = data.getShopGroupAvailableData().getErrorDescription();
                if (!TextUtils.isEmpty(errorDescription)) {
                    tickerError.setTickerTitle(data.getShopGroupAvailableData().getErrorTitle());
                    tickerError.setTextDescription(errorDescription);
                } else {
                    tickerError.setTickerTitle(null);
                    tickerError.setTextDescription(data.getShopGroupAvailableData().getErrorTitle());
                }
                tickerError.setTickerType(Ticker.TYPE_ERROR);
                tickerError.setTickerShape(Ticker.SHAPE_LOOSE);
                tickerError.setCloseButtonVisibility(View.GONE);
                tickerError.setVisibility(View.VISIBLE);
                tickerError.post(() -> {
                    tickerError.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    tickerError.requestLayout();
                });
                layoutError.setVisibility(View.VISIBLE);
            } else {
                layoutError.setVisibility(View.GONE);
            }
        } else {
            cbSelectShop.setEnabled(true);
            flShopItemContainer.setForeground(ContextCompat.getDrawable(flShopItemContainer.getContext(), R.drawable.fg_enabled_item));
            llShopContainer.setBackgroundColor(llShopContainer.getContext().getResources().getColor(R.color.white));

            layoutError.setVisibility(View.GONE);
        }
    }

    private void renderWarningItemHeader(CartShopHolderData data) {
        if (data.getShopGroupAvailableData().isWarning()) {
            String warningDescription = data.getShopGroupAvailableData().getWarningDescription();
            if (!TextUtils.isEmpty(warningDescription)) {
                tickerWarning.setTickerTitle(data.getShopGroupAvailableData().getWarningTitle());
                tickerWarning.setTextDescription(warningDescription);
            } else {
                tickerWarning.setTickerTitle(null);
                tickerWarning.setTextDescription(data.getShopGroupAvailableData().getWarningTitle());
            }
            tickerWarning.setTickerType(Ticker.TYPE_WARNING);
            tickerWarning.setTickerShape(Ticker.SHAPE_LOOSE);
            tickerWarning.setCloseButtonVisibility(View.GONE);
            tickerWarning.setVisibility(View.VISIBLE);
            tickerWarning.post(() -> {
                tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                tickerWarning.requestLayout();
            });
            layoutWarning.setVisibility(View.VISIBLE);
        } else {
            tickerWarning.setVisibility(View.GONE);
            layoutWarning.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener cbSelectShopClickListener(CartShopHolderData cartShopHolderData) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cartShopHolderData.getShopGroupAvailableData().isError()) {
                    boolean isChecked;
                    if (cartShopHolderData.isPartialSelected()) {
                        isChecked = false;
                        cartShopHolderData.setAllSelected(false);
                        cartShopHolderData.setPartialSelected(false);
                    } else {
                        isChecked = !cartShopHolderData.isAllSelected();
                    }
                    cbSelectShop.setChecked(isChecked);
                    boolean isAllSelected = true;
                    for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                        if (cartItemHolderData.getCartItemData().isError() && cartItemHolderData.getCartItemData().isSingleChild()) {
                            isAllSelected = false;
                            break;
                        }
                    }

                    cartShopHolderData.setAllSelected(isAllSelected);
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        actionListener.onShopItemCheckChanged(getAdapterPosition(), isChecked);
                    }
                }
            }
        };
    }

    private void renderPreOrder(CartShopHolderData cartShopHolderData) {
        if (!TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getPreOrderInfo())) {
            labelPreOrder.setText(cartShopHolderData.getShopGroupAvailableData().getPreOrderInfo());
            labelPreOrder.setVisibility(View.VISIBLE);
            separatorPreOrder.setVisibility(View.VISIBLE);
        } else {
            labelPreOrder.setVisibility(View.GONE);
            separatorPreOrder.setVisibility(View.GONE);
        }
    }

    private void renderIncidentLabel(CartShopHolderData cartShopHolderData) {
        if (!TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getIncidentInfo())) {
            labelIncident.setText(cartShopHolderData.getShopGroupAvailableData().getIncidentInfo());
            labelIncident.setVisibility(View.VISIBLE);
            separatorIncident.setVisibility(View.VISIBLE);
        } else {
            labelIncident.setVisibility(View.GONE);
            separatorIncident.setVisibility(View.GONE);
        }
    }

    private void renderFreeShipping(CartShopHolderData cartShopHolderData) {
        if (!TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getFreeShippingBadgeUrl())) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFreeShipping, cartShopHolderData.getShopGroupAvailableData().getFreeShippingBadgeUrl()
            );
            imgFreeShipping.setVisibility(View.VISIBLE);
            separatorFreeShipping.setVisibility(View.VISIBLE);
        } else {
            imgFreeShipping.setVisibility(View.GONE);
            separatorFreeShipping.setVisibility(View.GONE);
        }
    }
}
