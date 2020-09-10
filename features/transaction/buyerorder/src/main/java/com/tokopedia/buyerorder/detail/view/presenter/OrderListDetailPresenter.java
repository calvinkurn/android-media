package com.tokopedia.buyerorder.detail.view.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.ActionButtonList;
import com.tokopedia.buyerorder.detail.data.AdditionalInfo;
import com.tokopedia.buyerorder.detail.data.AdditionalTickerInfo;
import com.tokopedia.buyerorder.detail.data.Body;
import com.tokopedia.buyerorder.detail.data.CancelReplacementPojo;
import com.tokopedia.buyerorder.detail.data.DataResponseCommon;
import com.tokopedia.buyerorder.detail.data.DetailsData;
import com.tokopedia.buyerorder.detail.data.Flags;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.MetaDataInfo;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.PayMethod;
import com.tokopedia.buyerorder.detail.data.Pricing;
import com.tokopedia.buyerorder.detail.data.RequestCancelInfo;
import com.tokopedia.buyerorder.detail.data.SendEventEmail;
import com.tokopedia.buyerorder.detail.data.Title;
import com.tokopedia.buyerorder.detail.data.buyagain.ResponseBuyAgain;
import com.tokopedia.buyerorder.detail.data.recommendationMPPojo.RecommendationResponse;
import com.tokopedia.buyerorder.detail.data.recommendationPojo.RechargeWidgetResponse;
import com.tokopedia.buyerorder.detail.domain.FinishOrderUseCase;
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase;
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter;
import com.tokopedia.buyerorder.list.common.OrderListContants;
import com.tokopedia.buyerorder.list.data.Order;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kotlin.util.DownloadHelper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailPresenter extends BaseDaggerPresenter<OrderListDetailContract.View> implements OrderListDetailContract.Presenter {
    private static final String ORDER_CATEGORY = "orderCategoryStr";
    private static final String ORDER_ID = "orderId";
    private static final String DETAIL = "detail";
    private static final String ACTION = "action";
    private static final String UPSTREAM = "upstream";
    private static final String PARAM = "param";
    private static final String INVOICE = "invoice";
    private static final String TAB_ID = "tabId";
    private static final String CATEGORY_PRODUCT = "Kategori Produk";
    private static final int DEFAULT_TAB_ID = 1;
    private static final String DEVICE_ID = "device_id";
    private static final String CATEGORY_IDS = "category_ids";
    private static final String MP_CATEGORY_IDS = "mp_category_ids";
    private static final String PAYMENT_ID = "paymentId";
    private static final String CART_STRING = "cartString";
    private static final int DEFAULT_DEVICE_ID = 5;

    GraphqlUseCase orderDetailsUseCase;
    List<ActionButton> actionButtonList;
    @Inject
    PostCancelReasonUseCase postCancelReasonUseCase;
    @Inject
    FinishOrderUseCase finishOrderUseCase;
    OrderListDetailContract.ActionInterface view;
    String orderCategory;
    OrderDetails orderDetails;
    @Inject
    OrderListAnalytics orderListAnalytics;
    String fromPayment = null;
    String orderId;
    RequestCancelInfo requestCancelInfo;
    @Inject
    SendEventNotificationUseCase sendEventNotificationUseCase;

    private String Insurance_File_Name = "Invoice";
    public String pdfUri = " ";
    private boolean isdownloadable = false;
    private OrderDetails details;
    private List<Body> retryBody = new ArrayList<>();
    ArrayList<Integer> categoryList;
    String category;

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailsUseCase) {
        this.orderDetailsUseCase = orderDetailsUseCase;
    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment, String upstream, String paymentId, String cartString) {
        if (getView() == null || getView().getAppContext() == null)
            return;

        this.orderCategory = orderCategory;
        this.fromPayment = fromPayment;
        this.orderId = orderId;
        getView().showProgressBar();
        GraphqlRequest graphqlRequest;
        Map<String, Object> variables = new HashMap<>();
        if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)) {
            if (orderId != null && !orderId.isEmpty()) {
                variables.put("orderCategory", orderCategory);
                variables.put(ORDER_ID, orderId);
                graphqlRequest = new
                        GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                        R.raw.orderdetail_marketplace), DetailsData.class, variables, false);

            } else {
                variables.put("orderCategory", orderCategory);
                variables.put(PAYMENT_ID, paymentId);
                variables.put(CART_STRING, cartString);
                graphqlRequest = new
                        GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                        R.raw.orderdetail_marketplace_waiting_invoice), DetailsData.class, variables, false);
            }

        } else {
            variables.put(ORDER_CATEGORY, orderCategory);
            variables.put(ORDER_ID, orderId);
            variables.put(DETAIL, 1);
            variables.put(ACTION, 1);
            variables.put(UPSTREAM, upstream != null ? upstream : "");
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderdetails), DetailsData.class, variables, false);
        }


        orderDetailsUseCase.addRequest(graphqlRequest);
        orderDetailsUseCase.addRequest(makegraphqlRequestForRecommendation());
        orderDetailsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    Timber.d("error occured" + e);
                    getView().hideProgressBar();
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (response != null) {
                    DetailsData data = response.getData(DetailsData.class);
                    setDetailsData(data.orderDetails());
                    orderDetails = data.orderDetails();

                    if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)) {
                        List<Items> list = orderDetails.getItems();
                        categoryList = new ArrayList<>();
                        for (Items item : list) {
                            categoryList.add(item.getCategoryID());
                            categoryList.add(item.getCategoryL1());
                            categoryList.add(item.getCategoryL2());
                            categoryList.add(item.getCategoryL3());
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            category = String.join(",", category);
                        } else {
                            category = category.toString().substring(1, category.toString().length() - 1);
                        }
                    } else {
                        RechargeWidgetResponse rechargeWidgetResponse = response.getData(RechargeWidgetResponse.class);
                        getView().setRecommendation(rechargeWidgetResponse);
                    }
                }
                getRecommendation();
            }
        });

    }

    public void getRecommendation() {
        orderDetailsUseCase = new GraphqlUseCase();
        orderDetailsUseCase.clearRequest();
        orderDetailsUseCase.addRequest(makegraphqlRequestForMPRecommendation());
        orderDetailsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GraphqlResponse response) {
                RecommendationResponse recommendationResponse = response.getData(RecommendationResponse.class);
                getView().setRecommendation(recommendationResponse);

            }
        });
    }

    @Override
    public void hitEventEmail(ActionButton actionButton, String metadata, TextView actionButtonText, RelativeLayout actionButtonLayout){
        if (actionButton.getName().equalsIgnoreCase("customer_notification")){
            sendEventNotificationUseCase.setPath(actionButton.getUri());
            sendEventNotificationUseCase.setBody(metadata);
            sendEventNotificationUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null && getView().getAppContext() != null) {
                        getView().showSuccessMessageWithAction(e.getMessage());
                    }
                }

                @Override
                public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                        if (getView() != null && getView().getAppContext() != null) {
                            Type token = new TypeToken<SendEventEmail>() {
                            }.getType();
                            RestResponse restResponse = typeDataResponseMap.get(token);
                            actionButtonLayout.setClickable(false);
                            if(restResponse.getCode()==200 && restResponse.getErrorBody()==null) {
                                actionButtonText.setText(getView().getAppContext().getString(R.string.event_voucher_code_success));
                                getView().showSuccessMessageWithAction(getView().getAppContext().getString(R.string.event_voucher_code_copied));
                            } else {
                                Gson gson = new Gson();
                                actionButtonText.setText(getView().getAppContext().getString(R.string.event_voucher_code_fail));
                                SendEventEmail dataResponse = gson.fromJson(restResponse.getErrorBody(), SendEventEmail.class);
                                getView().showSuccessMessageWithAction(dataResponse.getData().getMessage());
                            }
                        }
                }
            });
        }
    }

    @Override
    public void setActionButton(List<ActionButton> actionButtons, OrderListDetailContract.ActionInterface view, int position, boolean flag) {
        Map<String, Object> variables = new HashMap<>();
        this.view = view;
        variables.put(PARAM, actionButtons);

        orderDetailsUseCase = new GraphqlUseCase();


        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tapactions), ActionButtonList.class, variables, false);

        orderDetailsUseCase.clearRequest();
        orderDetailsUseCase.setRequest(graphqlRequest);
        orderDetailsUseCase.createObservable(RequestParams.EMPTY).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("error occured" + e);
                    }

                    @Override
                    public void onNext(GraphqlResponse response) {
                        if (view != null) {
                            if (response != null) {
                                ActionButtonList data = response.getData(ActionButtonList.class);
                                actionButtonList = data.getActionButtonList();
                                if (actionButtonList != null)
                                    if (flag) {
                                        view.setTapActionButton(position, actionButtonList);
                                        for (int i=0; i< actionButtonList.size();i++) {
                                            if (actionButtonList.get(i).getControl().equalsIgnoreCase(ItemsAdapter.KEY_REFRESH))
                                            {
                                                actionButtonList.get(i).setBody(actionButtons.get(i).getBody());
                                            }
                                        }
                                    } else {
                                        view.setActionButton(position, actionButtonList);
                                    }
                            }
                        } else {
                            if (response != null) {
                                ActionButtonList data = response.getData(ActionButtonList.class);
                                actionButtonList = data.getActionButtonList();
                                if (actionButtonList != null && actionButtonList.size() > 0)
                                getView().setActionButtons(actionButtonList);
                            }
                        }
                    }
                });
    }

    public static final String PRODUCT_ID = "product_id";
    public static final String QUANTITY = "quantity";
    public static final String NOTES = "notes";
    public static final String SHOP_ID = "shop_id";


    private JsonArray generateInputQueryBuyAgain(List<Items> items) {
        JsonArray jsonArray = new JsonArray();
        for (Items item : items) {
            JsonObject passenger = new JsonObject();

            int productId = 0;
            int quantity = 0;
            int shopId = 0;
            String notes = "";
            try {
                productId = item.getId();
                quantity = item.getQuantity();
                shopId = orderDetails.getShopInfo().getShopId();
                notes = item.getDescription();
            } catch (Exception e) {
                Log.e("error parse", e.getMessage());
            }
            passenger.addProperty(PRODUCT_ID, productId);
            passenger.addProperty(QUANTITY, quantity);
            passenger.addProperty(NOTES, notes);
            passenger.addProperty(SHOP_ID, shopId);
            jsonArray.add(passenger);
        }
        return jsonArray;
    }

    @Override
    public List<ActionButton> getActionList() {
        return actionButtonList;
    }

    @Override
    public void onBuyAgainAllItems(String eventActionLabel, String statusCode) {
        onBuyAgainItems(orderDetails.getItems(), eventActionLabel, statusCode);
    }

    private GraphqlUseCase buyAgainUseCase;

    @Override
    public void onBuyAgainItems(List<Items> items, String eventActionLabel, String statusCode) {
        Map<String, Object> variables = new HashMap<>();
        JsonObject passenger = new JsonObject();
        variables.put(PARAM, generateInputQueryBuyAgain(items));

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.buy_again), ResponseBuyAgain.class, variables, false);

        buyAgainUseCase = new GraphqlUseCase();
        buyAgainUseCase.clearRequest();
        buyAgainUseCase.addRequest(graphqlRequest);

        buyAgainUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    getView().hideProgressBar();
                    getView().showErrorMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                if (getView() != null && getView().getAppContext() != null) {
                    getView().hideProgressBar();
                    ResponseBuyAgain responseBuyAgain = objects.getData(ResponseBuyAgain.class);
                    if (responseBuyAgain.getAddToCartMulti().getData().getSuccess() == 1) {
                        getView().showSuccessMessageWithAction(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(), ","));
                    } else {
                        getView().showErrorMessage(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(), ","));
                    }
                    orderListAnalytics.sendBuyAgainEvent(items, orderDetails.getShopInfo(), responseBuyAgain.getAddToCartMulti().getData().getData(), responseBuyAgain.getAddToCartMulti().getData().getSuccess() == 1, true, eventActionLabel, statusCode);
                }

            }
        });
    }

    @Override
    public void assignInvoiceDataTo(Intent intent) {
        if (orderDetails == null) return;
        String id = orderDetails.getInvoiceId();
        String invoiceCode = orderDetails.getInvoiceCode();
        String productName = orderDetails.getProductName();
        String date = orderDetails.getBoughtDate();
        String imageUrl = orderDetails.getProductImageUrl();
        String invoiceUrl = orderDetails.getInvoiceUrl();
        String statusId = orderDetails.getStatusId();
        String status = orderDetails.getStatusInfo();
        String totalPriceAmount = orderDetails.getTotalPriceAmount();

        intent.putExtra(ApplinkConst.Chat.INVOICE_ID, id);
        intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, invoiceCode);
        intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, productName);
        intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, date);
        intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, imageUrl);
        intent.putExtra(ApplinkConst.Chat.INVOICE_URL, invoiceUrl);
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, statusId);
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, status);
        intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, totalPriceAmount);
    }

    private void setDetailsData(OrderDetails details) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        this.details = details;
        getView().hideProgressBar();
        getView().setStatus(details.status());
        getView().clearDynamicViews();
        if (details.conditionalInfo().text() != null && !details.conditionalInfo().text().equals("")) {
            getView().setConditionalInfo(details.conditionalInfo());
        }
        for (Title title : details.title()) {
            getView().setTitle(title);
        }
        getView().setInvoice(details.invoice());
        getView().setOrderToken(details.orderToken());
        for (int i = 0; i < details.detail().size(); i++) {
            if ((orderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || orderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE))) {
                if (i == 2) {
                    if (details.getDriverDetails() != null) {
                        getView().showDriverInfo(details.getDriverDetails());
                    }
                }
                if (i == details.detail().size() - 1) {
                    if (!TextUtils.isEmpty(details.getDropShipper().getDropShipperName()) && !TextUtils.isEmpty(details.getDropShipper().getDropShipperPhone())) {
                        getView().showDropshipperInfo(details.getDropShipper());
                    }
                }
            }
            getView().setDetail(details.detail().get(i));
        }

        if (orderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || orderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
            if (details.getRequestCancelInfo() != null && details.getRequestCancelInfo().getIsRequestedCancel() != null) {
                getView().setIsRequestedCancel(details.getRequestCancelInfo().getIsRequestedCancel());
            }
        }
        getView().setBoughtDate(details.getBoughtDate());
        if (details.getShopInfo() != null) {
            getView().setShopInfo(details.getShopInfo());
        }
        if (details.getItems() != null && details.getItems().size() > 0) {
            Flags flags = details.getFlags();
            if (flags != null)
                getView().setItems(details.getItems(), flags.isIsOrderTradeIn(), details);
            else
                getView().setItems(details.getItems(), false, details);
        }
        if (details.additionalInfo().size() > 0) {
            getView().setAdditionInfoVisibility(View.VISIBLE);
        }
        for (AdditionalInfo additionalInfo : details.additionalInfo()) {

            getView().setAdditionalInfo(additionalInfo);
        }

        if (details.getAdditionalTickerInfos() != null
                && details.getAdditionalTickerInfos().size() > 0) {
            String url = null;
            for (AdditionalTickerInfo tickerInfo : details.getAdditionalTickerInfos()) {
                if (tickerInfo.getUrlDetail() != null && !tickerInfo.getUrlDetail().isEmpty()) {
                    String formattedTitle = formatTitleHtml(
                            tickerInfo.getNotes(),
                            tickerInfo.getUrlDetail(),
                            tickerInfo.getUrlText()
                    );
                    tickerInfo.setNotes(formattedTitle);
                    url = tickerInfo.getUrlDetail();
                }
            }
            getView().setAdditionalTickerInfo(details.getAdditionalTickerInfos(), url);
        }

        if (details.getTickerInfo() != null) {
            getView().setTickerInfo(details.getTickerInfo());
        }

        for (PayMethod payMethod : details.getPayMethods()) {
            if (!TextUtils.isEmpty(payMethod.getValue()))
                getView().setPayMethodInfo(payMethod);
        }

        for (Pricing pricing : details.pricing()) {
            getView().setPricing(pricing);
        }
        getView().setPaymentData(details.paymentData());
        getView().setContactUs(details.contactUs(),details.getHelpLink());

        if(details.getItems() != null && details.getItems().size() > 0 && details.getItems().get(0).getCategory().equalsIgnoreCase(OrderCategory.EVENT)){
            getView().setActionButtonsVisibility(View.GONE, View.GONE);
        } else if (!(orderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || orderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE))) {
            if (details.actionButtons().size() == 2) {
                ActionButton leftActionButton = details.actionButtons().get(0);
                ActionButton rightActionButton = details.actionButtons().get(1);
                getView().setTopActionButton(leftActionButton);
                getView().setBottomActionButton(rightActionButton);
            } else if (details.actionButtons().size() == 1) {
                ActionButton actionButton = details.actionButtons().get(0);
                getView().setButtonMargin();
                if (actionButton.getLabel().equals(INVOICE)) {
                    getView().setBottomActionButton(actionButton);
                    getView().setActionButtonsVisibility(View.GONE, View.VISIBLE);
                } else {
                    getView().setTopActionButton(actionButton);
                    getView().setActionButtonsVisibility(View.VISIBLE, View.GONE);

                }
            } else {
                getView().setActionButtonsVisibility(View.GONE, View.GONE);
            }
        } else {
            getView().setActionButtons(details.actionButtons());
        }
        getView().setMainViewVisible(View.VISIBLE);
        this.requestCancelInfo = details.getRequestCancelInfo();
    }


    public void updateOrderCancelReason(String cancelReason, String orderId,
                                        int cancelOrReplacement, String url) {
        if (getView() == null || getView().getAppContext() == null)
            return;

        UserSession userSession = new UserSession(getView().getAppContext());
        String userId = userSession.getUserId();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("reason_cancel", cancelReason);
        requestParams.putString("user_id", userId);
        requestParams.putString("order_id", orderId);
        requestParams.putString("device_id", userSession.getDeviceId());
        if (cancelOrReplacement != 1) {
            requestParams.putInt("r_code", cancelOrReplacement);
        }
        getView().showProgressBar();

        postCancelReasonUseCase.setRequestParams(requestParams);
        postCancelReasonUseCase.cancelOrReplaceOrder(url);
        postCancelReasonUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (getView() != null && getView().getAppContext() != null) {
                                                    Timber.d(e);
                                                    getView().showErrorMessage(e.getMessage());
                                                    getView().hideProgressBar();
                                                    getView().finishOrderDetail();
                                                }
                                            }

                                            @Override
                                            public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                                                if (getView() != null && getView().getAppContext() != null) {
                                                    Type token = new TypeToken<DataResponseCommon<CancelReplacementPojo>>() {
                                                    }.getType();
                                                    RestResponse restResponse = typeDataResponseMap.get(token);
                                                    DataResponseCommon dataResponse = restResponse.getData();
                                                    CancelReplacementPojo cancelReplacementPojo = (CancelReplacementPojo) dataResponse.getData();
                                                    if (!TextUtils.isEmpty(cancelReplacementPojo.getMessageStatus()))
                                                        getView().showSucessMessage(cancelReplacementPojo.getMessageStatus());
                                                    else if (dataResponse.getErrorMessage() != null && !dataResponse.getErrorMessage().isEmpty())
                                                        getView().showErrorMessage((String) dataResponse.getErrorMessage().get(0));
                                                    else if ((dataResponse.getMessageStatus() != null && !dataResponse.getMessageStatus().isEmpty()))
                                                        getView().showSucessMessage((String) dataResponse.getMessageStatus().get(0));
                                                    getView().hideProgressBar();
                                                    getView().finishOrderDetail();
                                                }
                                            }
                                        }
        );
    }

    public void finishOrder(String orderId, String url) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        UserSession userSession = new UserSession(getView().getAppContext());
        String userId = userSession.getUserId();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("user_id", userId);
        requestParams.putString("order_id", orderId);
        requestParams.putString("device_id", userSession.getDeviceId());
        getView().showProgressBar();

        finishOrderUseCase.setRequestParams(requestParams);
        finishOrderUseCase.setEndPoint(url);
        finishOrderUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    Timber.d(e);
                    getView().hideProgressBar();
                    getView().showErrorMessage(e.getMessage());
                    getView().finishOrderDetail();
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                if (getView() != null && getView().getAppContext() != null) {
                    Type token = new TypeToken<DataResponseCommon<CancelReplacementPojo>>() {
                    }.getType();
                    RestResponse restResponse = typeDataResponseMap.get(token);
                    DataResponseCommon dataResponse = restResponse.getData();
                    CancelReplacementPojo cancelReplacementPojo = (CancelReplacementPojo) dataResponse.getData();
                    if (!TextUtils.isEmpty(cancelReplacementPojo.getMessageStatus()))
                        getView().showSucessMessage(cancelReplacementPojo.getMessageStatus());
                    else if (dataResponse.getErrorMessage() != null && !dataResponse.getErrorMessage().isEmpty())
                        getView().showErrorMessage((String) dataResponse.getErrorMessage().get(0));
                    else if ((dataResponse.getMessageStatus() != null && !dataResponse.getMessageStatus().isEmpty()))
                        getView().showSucessMessage((String) dataResponse.getMessageStatus().get(0));
                    getView().hideProgressBar();
                    getView().finishOrderDetail();
                }
            }
        });
    }

    @Override
    public void detachView() {
        orderDetailsUseCase.unsubscribe();
        if (postCancelReasonUseCase != null) {
            postCancelReasonUseCase.unsubscribe();
        }
        if (finishOrderUseCase != null) {
            finishOrderUseCase.unsubscribe();
        }
        if (buyAgainUseCase != null) {
            buyAgainUseCase.unsubscribe();
        }
        super.detachView();
    }

    public void onClick(String uri) {
        pdfUri = uri;
        if (isdownloadable(uri)) {
            getView().askPermission();
        } else {
            if (getView() != null && getView().getAppContext() != null && getView().getAppContext().getApplicationContext() != null && getView().getActivity() != null) {
                RouteManager.route(getView().getActivity(), ApplinkConstInternalGlobal.WEBVIEW, uri);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void permissionGrantedContinueDownload() {
        DownloadHelper downloadHelper = new DownloadHelper(getView().getAppContext(), pdfUri, Insurance_File_Name, () -> {
            // download success call back

        });
        downloadHelper.downloadFile(this::isdownloadable);
    }
    private Boolean isdownloadable(String uri ) {
        Pattern pattern = Pattern.compile("^.+\\.([pP][dD][fF])$");
        Matcher matcher = pattern.matcher(uri);
        return (matcher.find() || this.isdownloadable);
    }

    public void sendThankYouEvent(MetaDataInfo metaDataInfo, int categoryType) {
        if ("true".equalsIgnoreCase(this.fromPayment)) {
            String paymentStatus = "", paymentMethod = "";
            if (details != null && details.status() != null && !TextUtils.isEmpty(details.status().statusText())) {
                paymentStatus = details.status().statusText();
            }
            if (details != null && details.getPayMethods() != null && details.getPayMethods().size() > 0 && !TextUtils.isEmpty(details.getPayMethods().get(0).getValue())) {
                paymentMethod = details.getPayMethods().get(0).getValue();
            }
            if(categoryType==3){
                orderListAnalytics.sendThankYouEvent(metaDataInfo.getEntityProductId(), metaDataInfo.getProductName(), metaDataInfo.getTotalPrice(), metaDataInfo.getQuantity(), metaDataInfo.getEntityBrandName(), orderId, categoryType, paymentMethod, paymentStatus);
            }else {
                orderListAnalytics.sendThankYouEvent(metaDataInfo.getEntityProductId(), metaDataInfo.getEntityProductName(), metaDataInfo.getTotalTicketPrice(), metaDataInfo.getTotalTicketCount(), metaDataInfo.getEntityBrandName(), orderId, categoryType, paymentMethod, paymentStatus);
            }
        }
    }

    public void setDownloadableFlag(boolean isdownloadable) {
        this.isdownloadable = isdownloadable;
    }

    public void setDownloadableFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            Insurance_File_Name = fileName;
        }
    }

    public String getCancelTime() {
        return requestCancelInfo.getRequestCancelNote();
    }

    public boolean shouldShowTimeForCancellation(){
        return requestCancelInfo != null && !requestCancelInfo.getIsRequestCancelAvail()
                && !TextUtils.isEmpty(requestCancelInfo.getRequestCancelMinTime());
    }


    private GraphqlRequest makegraphqlRequestForRecommendation() {
        GraphqlRequest graphqlRequestForRecommendation;
        Map<String, Object> variablesWidget = new HashMap<>();
        variablesWidget.put(TAB_ID, DEFAULT_TAB_ID);
        graphqlRequestForRecommendation = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.query_recharge_widget), RechargeWidgetResponse.class, variablesWidget);
        return graphqlRequestForRecommendation;
    }

    private GraphqlRequest makegraphqlRequestForMPRecommendation() {
        if (TextUtils.isEmpty(category)) category = "";
        GraphqlRequest graphqlRequestForMPRecommendation;
        Map<String, Object> variable = new HashMap<>();
        variable.put(DEVICE_ID, DEFAULT_DEVICE_ID);
        variable.put(CATEGORY_IDS, category);
        variable.put(MP_CATEGORY_IDS, categoryList);
        graphqlRequestForMPRecommendation = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.recommendation_mp), RecommendationResponse.class, variable);
        return graphqlRequestForMPRecommendation;
    }

    public boolean isValidUrl(String invoiceUrl) {
        Pattern pattern = Pattern.compile("^(https|HTTPS):\\/\\/");
        Matcher matcher = pattern.matcher(invoiceUrl);
        return matcher.find();
    }

    private String formatTitleHtml(String desc, String urlText, String url) {
        return String.format("%s <a href=\"%s\">%s</a>", desc, urlText, url);
    }

    public String getProductCategory() {
        if (details.title() != null) {
            for (Title title : details.title()) {
                if (title.label().equalsIgnoreCase(CATEGORY_PRODUCT))
                    return title.value();
            }
        }
        return null;
    }

    public String getFirstProductId() {
        if (details != null && details.getItems() != null && !details.getItems().isEmpty()) {
            return String.valueOf(details.getItems().get(0).getId());
        }
        return "";
    }

    public void showRetryButtonToaster(String message) {
        if (getView() == null)
            return;

        getView().showSuccessMessageWithAction(message);
    }
}
