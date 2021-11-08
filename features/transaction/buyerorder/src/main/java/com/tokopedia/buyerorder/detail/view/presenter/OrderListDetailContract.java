package com.tokopedia.buyerorder.detail.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.AdditionalInfo;
import com.tokopedia.buyerorder.detail.data.ConditionalInfo;
import com.tokopedia.buyerorder.detail.data.ContactUs;
import com.tokopedia.buyerorder.detail.data.Detail;
import com.tokopedia.buyerorder.detail.data.Invoice;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.PayMethod;
import com.tokopedia.buyerorder.detail.data.PaymentData;
import com.tokopedia.buyerorder.detail.data.Pricing;
import com.tokopedia.buyerorder.detail.data.Status;
import com.tokopedia.buyerorder.detail.data.Title;

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

        void setDetail(Detail detail);

        void setAdditionalInfo(AdditionalInfo additionalInfo);

        void setPricing(Pricing pricing, Boolean isCategoryEvent);

        void setPaymentData(PaymentData paymentData, Boolean isCategoryEvent);

        void setContactUs(ContactUs contactUs, String helpLink);

        void setTopActionButton(ActionButton actionButton);

        void setBottomActionButton(ActionButton actionButton);

        void setMainViewVisible(int visible);

        void setAdditionInfoVisibility(int visible);

        void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility);

        void setItems(List<Items> items, boolean isTradeIn, OrderDetails orderDetails);

        Context getActivity();

        void setPayMethodInfo(PayMethod payMethod, Boolean isCategoryEvent);

        void setButtonMargin();

        void showProgressBar();

        void hideProgressBar();

        void setActionButtons(List<ActionButton> actionButtons);

        void showSuccessMessageWithAction(String message);

        void setRecommendation(Object object);

        void setActionButtonLayoutClickable(Boolean isClickable);

        void setActionButtonText(String txt);
    }

    interface Presenter extends CustomerPresenter<View> {
        void setOrderDetailsContent(String orderId, String orderCategory, String upstream);

        void getActionButtonGql(String query, List<ActionButton> actionButtons, ActionInterface view, int position, boolean flag);

        void hitEventEmail(ActionButton actionButton, String metadata);

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
