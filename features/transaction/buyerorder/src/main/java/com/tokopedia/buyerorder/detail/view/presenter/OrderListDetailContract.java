package com.tokopedia.buyerorder.detail.view.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.atc_common.domain.model.response.AtcMultiData;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.AdditionalInfo;
import com.tokopedia.buyerorder.detail.data.AdditionalTickerInfo;
import com.tokopedia.buyerorder.detail.data.ContactUs;
import com.tokopedia.buyerorder.detail.data.Detail;
import com.tokopedia.buyerorder.detail.data.Discount;
import com.tokopedia.buyerorder.detail.data.DriverDetails;
import com.tokopedia.buyerorder.detail.data.DropShipper;
import com.tokopedia.buyerorder.detail.data.Invoice;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.OrderToken;
import com.tokopedia.buyerorder.detail.data.PayMethod;
import com.tokopedia.buyerorder.detail.data.Pricing;
import com.tokopedia.buyerorder.detail.data.ShopInfo;
import com.tokopedia.buyerorder.detail.data.Status;
import com.tokopedia.buyerorder.detail.data.TickerInfo;
import com.tokopedia.buyerorder.detail.data.Title;
import com.tokopedia.buyerorder.list.data.ConditionalInfo;
import com.tokopedia.buyerorder.list.data.PaymentData;

import java.util.List;

/**
 * Created by baghira on 09/05/18.
 */

public interface OrderListDetailContract {

    interface View extends CustomerView {
        void setDetailsData(OrderDetails details);

        void setStatus(Status status);

        void setConditionalInfo(ConditionalInfo conditionalInfo);

        void setTitle(Title title);

        void setInvoice(Invoice invoice);

        void setOrderToken(OrderToken orderToken);

        void setDetail(Detail detail);

        void setIsRequestedCancel(Boolean isRequestedCancel);

        void setAdditionalInfo(AdditionalInfo additionalInfo);

        void setAdditionalTickerInfo(List<AdditionalTickerInfo> tickerInfos, @Nullable String url);

        void setTickerInfo(TickerInfo tickerInfo);

        void setPricing(Pricing pricing);

        void setDiscount(Discount discount);

        void setDiscountVisibility(int visibility);

        void setPaymentData(PaymentData paymentData);

        void setContactUs(ContactUs contactUs, String helpLink);

        void setTopActionButton(ActionButton actionButton);

        void setBottomActionButton(ActionButton actionButton);

        void setMainViewVisible(int visible);

        void setAdditionInfoVisibility(int visible);

        void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility);

        void setItems(List<Items> items, boolean isTradeIn, OrderDetails orderDetails);

        Context getActivity();

        void setPayMethodInfo(PayMethod payMethod);

        void setButtonMargin();

        void showDropshipperInfo(DropShipper dropShipper);

        void showDriverInfo(DriverDetails driverDetails);

        void showProgressBar();

        void hideProgressBar();

        void setActionButtons(List<ActionButton> actionButtons);

        void setShopInfo(ShopInfo shopInfo);

        void setBoughtDate(String boughtDate);

        void showReplacementView(List<String> reasons);

        void finishOrderDetail();

        void showSuccessMessage(String message);

        void showSuccessMessageWithAction(String message);

        void showErrorMessage(String message);

        void clearDynamicViews();

        void setRecommendation(Object object);

        JsonArray generateInputQueryBuyAgain(List<Items> items);

        void hitAnalyticsBuyAgain(List<AtcMultiData.AtcMulti.BuyAgainData.AtcProduct> listAtcProducts, Boolean isAtcMultiSuccess);

        void setActionButtonLayoutClickable(Boolean isClickable);

        void setActionButtonText(String txt);
    }

    interface Presenter extends CustomerPresenter<View> {
        void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment, String upstream, String paymentId, String cartString);

        void getActionButtonGql(String query, List<ActionButton> actionButtons, ActionInterface view, int position, boolean flag);

        void hitEventEmail(ActionButton actionButton, String metadata);

        void onBuyAgainItems(String query, List<Items> items, String eventActionLabel, String statusCode);

        void onLihatInvoiceButtonClick(String invoiceUrl);

        void onCopyButtonClick(String copiedValue);

        void onActionButtonClick(String buttonId, String buttonName);

        String getOrderCategoryName();

        String getOrderProductName();
    }

    interface ActionInterface {
        void setActionButton(int position, List<ActionButton> actionButtons);

        void setTapActionButton(int position, List<ActionButton> actionButtons);
    }
}
