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
import com.tokopedia.atc_common.domain.model.response.AtcMultiData;
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiLegacyUseCase;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.common.util.BuyerConsts;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.ActionButtonList;
import com.tokopedia.buyerorder.detail.data.AdditionalInfo;
import com.tokopedia.buyerorder.detail.data.AdditionalTickerInfo;
import com.tokopedia.buyerorder.detail.data.CancelReplacementPojo;
import com.tokopedia.buyerorder.detail.data.DataResponseCommon;
import com.tokopedia.buyerorder.detail.data.DetailsData;
import com.tokopedia.buyerorder.detail.data.Discount;
import com.tokopedia.buyerorder.detail.data.Flags;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.MetaDataInfo;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.PayMethod;
import com.tokopedia.buyerorder.detail.data.Pricing;
import com.tokopedia.buyerorder.detail.data.RequestCancelInfo;
import com.tokopedia.buyerorder.detail.data.SendEventEmail;
import com.tokopedia.buyerorder.detail.data.Title;
import com.tokopedia.buyerorder.detail.data.recommendationMPPojo.RecommendationResponse;
import com.tokopedia.buyerorder.detail.data.recommendationPojo.RechargeWidgetResponse;
import com.tokopedia.buyerorder.detail.domain.FinishOrderGqlUseCase;
import com.tokopedia.buyerorder.detail.domain.GetOrderDetailUseCase;
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase;
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter;
import com.tokopedia.buyerorder.list.common.OrderListContants;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrder;
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrderParam;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kotlin.util.DownloadHelper;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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
    @Inject
    AddToCartMultiLegacyUseCase addToCartMultiLegacyUseCase;
    @Inject
    GetOrderDetailUseCase getOrderDetailUseCase;
    @Inject
    UserSessionInterface userSessionInterface;
    @Inject
    FinishOrderGqlUseCase finishOrderGqlUseCase;

    private String Insurance_File_Name = "Invoice";
    public String pdfUri = " ";
    private boolean isdownloadable = false;
    private OrderDetails details;
    ArrayList<Integer> categoryList;
    String category;

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailsUseCase) {
        this.orderDetailsUseCase = orderDetailsUseCase;
    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment, String upstream, String paymentId, String cartString) {
        if (getView() == null || getView().getActivity() == null)
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
                        GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                        R.raw.orderdetail_marketplace), DetailsData.class, variables);

            } else {
                variables.put("orderCategory", orderCategory);
                variables.put(PAYMENT_ID, paymentId);
                variables.put(CART_STRING, cartString);
                graphqlRequest = new
                        GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                        R.raw.orderdetail_marketplace_waiting_invoice), DetailsData.class, variables);
            }

        } else {
            variables.put(ORDER_CATEGORY, orderCategory);
            variables.put(ORDER_ID, orderId);
            variables.put(DETAIL, 1);
            variables.put(ACTION, 1);
            variables.put(UPSTREAM, upstream != null ? upstream : "");
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                    R.raw.orderdetails), DetailsData.class, variables);
        }


        orderDetailsUseCase.addRequest(graphqlRequest);
        orderDetailsUseCase.addRequest(makegraphqlRequestForRecommendation());
        orderDetailsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getActivity() != null) {
                    Timber.d("error occured" + e);
                    getView().hideProgressBar();
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (response != null) {
                    DetailsData data = response.getData(DetailsData.class);
                    if (data != null && isViewAttached()) {
                        orderDetails = data.orderDetails();
                        details = data.orderDetails();
                        getView().setDetailsData(data.orderDetails());
                        if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)
                           || orderCategory.equalsIgnoreCase(OrderListContants.BELANJA)) {
                            requestCancelInfo = details.getRequestCancelInfo();
                        }
                    }

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
                            category = category.substring(1, category.length() - 1);
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
    public void hitEventEmail(ActionButton actionButton, String metadata, TextView actionButtonText, RelativeLayout actionButtonLayout) {
        if (actionButton.getName().equalsIgnoreCase("customer_notification")) {
            sendEventNotificationUseCase.setPath(actionButton.getUri());
            sendEventNotificationUseCase.setBody(metadata);
            sendEventNotificationUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null && getView().getActivity() != null) {
                        getView().showSuccessMessageWithAction(e.getMessage());
                    }
                }

                @Override
                public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                    if (getView() != null && getView().getActivity() != null) {
                        Type token = new TypeToken<SendEventEmail>() {
                        }.getType();
                        RestResponse restResponse = typeDataResponseMap.get(token);
                        actionButtonLayout.setClickable(false);
                        if (restResponse.getCode() == 200 && restResponse.getErrorBody() == null) {
                            actionButtonText.setText(getView().getActivity().getString(R.string.event_voucher_code_success));
                            getView().showSuccessMessageWithAction(getView().getActivity().getString(R.string.event_voucher_code_copied));
                        } else {
                            Gson gson = new Gson();
                            actionButtonText.setText(getView().getActivity().getString(R.string.event_voucher_code_fail));
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
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                R.raw.tapactions), ActionButtonList.class, variables);

        orderDetailsUseCase.clearRequest();
        orderDetailsUseCase.addRequest(graphqlRequest);
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
                                if (data != null) {
                                    actionButtonList = data.getActionButtonList();
                                    if (actionButtonList != null)
                                        if (flag) {
                                            view.setTapActionButton(position, actionButtonList);
                                            for (int i = 0; i < actionButtonList.size(); i++) {
                                                if (actionButtonList.get(i).getControl().equalsIgnoreCase(ItemsAdapter.KEY_REFRESH)) {
                                                    actionButtonList.get(i).setBody(actionButtons.get(i).getBody());
                                                }
                                            }
                                        } else {
                                            view.setActionButton(position, actionButtonList);
                                        }
                                }
                            }
                        } else {
                            if (response != null) {
                                ActionButtonList data = response.getData(ActionButtonList.class);
                                if (data != null) {
                                    actionButtonList = data.getActionButtonList();
                                    if (actionButtonList != null && actionButtonList.size() > 0)
                                        getView().setActionButtons(actionButtonList);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public List<ActionButton> getActionList() {
        return actionButtonList;
    }

    @Override
    public void onBuyAgainItems(List<Items> items, String eventActionLabel, String statusCode) {
        Map<String, Object> variables = new HashMap<>();

        JsonArray params = getView().generateInputQueryBuyAgain(items);
        variables.put(PARAM, params);
        addToCartMultiLegacyUseCase.setup(GraphqlHelper.loadRawString(getView().getActivity().getResources(), com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi), variables, userSessionInterface.getUserId());
        addToCartMultiLegacyUseCase.execute(new Subscriber<AtcMultiData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getActivity() != null) {
                    getView().hideProgressBar();
                    getView().showErrorMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(AtcMultiData atcMultiData) {
                if (getView() != null && getView().getActivity() != null) {
                    getView().hideProgressBar();
                    if (atcMultiData.getAtcMulti().getBuyAgainData().getSuccess() == 1) {
                        getView().showSuccessMessageWithAction(StringUtils.convertListToStringDelimiter(atcMultiData.getAtcMulti().getBuyAgainData().getMessage(), ","));
                    } else {
                        getView().showErrorMessage(StringUtils.convertListToStringDelimiter(atcMultiData.getAtcMulti().getBuyAgainData().getMessage(), ","));
                    }
                    orderListAnalytics.sendBuyAgainEvent(items, orderDetails.getShopInfo(), atcMultiData.getAtcMulti().getBuyAgainData().getListProducts(), atcMultiData.getAtcMulti().getBuyAgainData().getSuccess() == 1, true, eventActionLabel, statusCode);
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

    public void updateOrderCancelReason(String cancelReason, String orderId,
                                        int cancelOrReplacement, String url) {
        if (getView() == null || getView().getActivity() == null)
            return;

        UserSession userSession = new UserSession(getView().getActivity());
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
                                                if (getView() != null && getView().getActivity() != null) {
                                                    Timber.d(e);
                                                    getView().showErrorMessage(e.getMessage());
                                                    getView().hideProgressBar();
                                                    getView().finishOrderDetail();
                                                }
                                            }

                                            @Override
                                            public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                                                if (getView() != null && getView().getActivity() != null) {
                                                    Type token = new TypeToken<DataResponseCommon<CancelReplacementPojo>>() {
                                                    }.getType();
                                                    RestResponse restResponse = typeDataResponseMap.get(token);
                                                    DataResponseCommon dataResponse = restResponse.getData();
                                                    CancelReplacementPojo cancelReplacementPojo = (CancelReplacementPojo) dataResponse.getData();
                                                    if (!TextUtils.isEmpty(cancelReplacementPojo.getMessageStatus()))
                                                        getView().showSuccessMessage(cancelReplacementPojo.getMessageStatus());
                                                    else if (dataResponse.getErrorMessage() != null && !dataResponse.getErrorMessage().isEmpty())
                                                        getView().showErrorMessage((String) dataResponse.getErrorMessage().get(0));
                                                    else if ((dataResponse.getMessageStatus() != null && !dataResponse.getMessageStatus().isEmpty()))
                                                        getView().showSuccessMessage((String) dataResponse.getMessageStatus().get(0));
                                                    getView().hideProgressBar();
                                                    getView().finishOrderDetail();
                                                }
                                            }
                                        }
        );
    }

    public void finishOrderGql(String orderId, String actionStatus) {
        if (getView() == null)
            return;

        UohFinishOrderParam uohFinishOrderParam = new UohFinishOrderParam();
        uohFinishOrderParam.setOrderId(orderId);
        uohFinishOrderParam.setAction(actionStatus);
        uohFinishOrderParam.setUserId(userSessionInterface.getUserId());

        Map<String, Object> variables = new HashMap<>();
        variables.put(BuyerConsts.PARAM_INPUT, uohFinishOrderParam);
        finishOrderGqlUseCase.setup(GraphqlHelper.loadRawString(getView().getActivity().getResources(), R.raw.uoh_finish_order), variables);
        finishOrderGqlUseCase.execute(new Subscriber<UohFinishOrder.Data>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getActivity() != null) {
                    Timber.d(e);
                    getView().hideProgressBar();
                    getView().showErrorMessage(e.getMessage());
                    getView().finishOrderDetail();
                }
            }

            @Override
            public void onNext(UohFinishOrder.Data data) {
                if (data.getFinishOrderBuyer().getSuccess() == 1) {
                    if (!data.getFinishOrderBuyer().getMessage().isEmpty()) {
                        getView().showSuccessMessage(data.getFinishOrderBuyer().getMessage().get(0));
                    }
                } else {
                    if (!data.getFinishOrderBuyer().getMessage().isEmpty()) {
                        getView().showErrorMessage(data.getFinishOrderBuyer().getMessage().get(0));
                    }
                }
                getView().hideProgressBar();
                getView().finishOrderDetail();
            }
        });
    }

    @Override
    public void detachView() {
        orderDetailsUseCase.unsubscribe();
        if (postCancelReasonUseCase != null) {
            postCancelReasonUseCase.unsubscribe();
        }
        if (finishOrderGqlUseCase != null) {
            finishOrderGqlUseCase.unsubscribe();
        }
        addToCartMultiLegacyUseCase.unsubscribe();
        super.detachView();
    }

    public void onClick(String uri) {
        pdfUri = uri;
        if (isdownloadable(uri)) {
            getView().askPermission();
        } else {
            if (getView() != null && getView().getActivity() != null && getView().getActivity().getApplicationContext() != null && getView().getActivity() != null) {
                RouteManager.route(getView().getActivity(), ApplinkConstInternalGlobal.WEBVIEW, uri);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void permissionGrantedContinueDownload() {
        DownloadHelper downloadHelper = new DownloadHelper(getView().getActivity(), pdfUri, Insurance_File_Name, () -> {
            // download success call back

        });
        downloadHelper.downloadFile(this::isdownloadable);
    }

    private Boolean isdownloadable(String uri) {
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
            if (categoryType == 3) {
                orderListAnalytics.sendThankYouEvent(metaDataInfo.getEntityProductId(), metaDataInfo.getProductName(), metaDataInfo.getTotalPrice(), metaDataInfo.getQuantity(), metaDataInfo.getEntityBrandName(), orderId, categoryType, paymentMethod, paymentStatus);
            } else {
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

    public boolean shouldShowTimeForCancellation() {
        return requestCancelInfo != null && !requestCancelInfo.getIsRequestCancelAvail()
                && !TextUtils.isEmpty(requestCancelInfo.getRequestCancelMinTime());
    }


    private GraphqlRequest makegraphqlRequestForRecommendation() {
        GraphqlRequest graphqlRequestForRecommendation;
        Map<String, Object> variablesWidget = new HashMap<>();
        variablesWidget.put(TAB_ID, DEFAULT_TAB_ID);
        graphqlRequestForRecommendation = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
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
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                R.raw.recommendation_mp), RecommendationResponse.class, variable);
        return graphqlRequestForMPRecommendation;
    }

    public boolean isValidUrl(String invoiceUrl) {
        Pattern pattern = Pattern.compile("^(https|HTTPS):\\/\\/");
        Matcher matcher = pattern.matcher(invoiceUrl);
        return matcher.find();
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
