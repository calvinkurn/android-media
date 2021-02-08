package com.tokopedia.buyerorder.detail.view.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.atc_common.domain.model.response.AtcMultiData;
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiLegacyUseCase;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.common.util.BuyerConsts;
import com.tokopedia.buyerorder.detail.analytics.OrderListAnalyticsUtils;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.ActionButtonList;
import com.tokopedia.buyerorder.detail.data.DetailsData;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.RequestCancelInfo;
import com.tokopedia.buyerorder.detail.data.SendEventEmail;
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo.RecommendationResponse;
import com.tokopedia.buyerorder.detail.data.recommendationPojo.RechargeWidgetResponse;
import com.tokopedia.buyerorder.detail.domain.BuyerGetRecommendationUseCase;
import com.tokopedia.buyerorder.detail.domain.FinishOrderGqlUseCase;
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase;
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase;
import com.tokopedia.buyerorder.detail.domain.SetActionButtonUseCase;
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
import com.tokopedia.user.session.UserSessionInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
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
    private static final int DEFAULT_TAB_ID = 1;
    private static final String DEVICE_ID = "device_id";
    private static final String CATEGORY_IDS = "category_ids";
    private static final String MP_CATEGORY_IDS = "mp_category_ids";
    private static final String PAYMENT_ID = "paymentId";
    private static final String CART_STRING = "cartString";
    private static final int DEFAULT_DEVICE_ID = 5;

    private final UserSessionInterface userSessionInterface;
    GraphqlUseCase orderDetailUseCase;
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
    FinishOrderGqlUseCase finishOrderGqlUseCase;
    @Inject
    SetActionButtonUseCase setActionButtonUseCase;
    @Inject
    BuyerGetRecommendationUseCase buyerGetRecommendationUseCase;

    public String pdfUri = " ";
    private OrderDetails details;
    ArrayList<Integer> categoryList;
    String category;

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailUseCase,
                                    FinishOrderGqlUseCase finishOrderGqlUseCase,
                                    AddToCartMultiLegacyUseCase addToCartMultiLegacyUseCase,
                                    UserSessionInterface userSessionInterface,
                                    SetActionButtonUseCase setActionButtonUseCase,
                                    SendEventNotificationUseCase sendEventNotificationUseCase,
                                    BuyerGetRecommendationUseCase buyerGetRecommendationUseCase) {
        this.orderDetailUseCase = orderDetailUseCase;
        this.finishOrderGqlUseCase = finishOrderGqlUseCase;
        this.addToCartMultiLegacyUseCase = addToCartMultiLegacyUseCase;
        this.userSessionInterface = userSessionInterface;
        this.setActionButtonUseCase = setActionButtonUseCase;
        this.sendEventNotificationUseCase = sendEventNotificationUseCase;
        this.buyerGetRecommendationUseCase = buyerGetRecommendationUseCase;
    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment, String upstream, String paymentId, String cartString) {
        if (getView() != null && getView().getActivity() != null) {
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
                            R.raw.orderdetail_marketplace), DetailsData.class, variables, false);

                } else {
                    variables.put("orderCategory", orderCategory);
                    variables.put(PAYMENT_ID, paymentId);
                    variables.put(CART_STRING, cartString);
                    graphqlRequest = new
                            GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                            R.raw.orderdetail_marketplace_waiting_invoice), DetailsData.class, variables, false);
                }

            } else {
                variables.put(ORDER_CATEGORY, orderCategory);
                variables.put(ORDER_ID, orderId);
                variables.put(DETAIL, 1);
                variables.put(ACTION, 1);
                variables.put(UPSTREAM, upstream != null ? upstream : "");
                graphqlRequest = new
                        GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                        R.raw.orderdetails), DetailsData.class, variables, false);
            }


            orderDetailUseCase.addRequest(graphqlRequest);

            GraphqlRequest requestRecomm = null;
            Map<String, Object> variablesWidget = new HashMap<>();
            variablesWidget.put(TAB_ID, DEFAULT_TAB_ID);
            if (getView() != null && getView().getActivity() != null) {
                requestRecomm = new
                        GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                        R.raw.query_recharge_widget), RechargeWidgetResponse.class, variablesWidget, false);
            }

            if (requestRecomm != null) {
                orderDetailUseCase.addRequest(requestRecomm);
                orderDetailUseCase.execute(new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            Timber.d("error occured" + e);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onNext(GraphqlResponse response) {
                        if (response != null) {
                            DetailsData data = response.getData(DetailsData.class);
                            if (data != null && getView() != null) {
                                orderDetails = data.orderDetails();
                                details = data.orderDetails();
                                getView().setDetailsData(data.orderDetails());
                                if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)
                                        || orderCategory.equalsIgnoreCase(OrderListContants.BELANJA)) {
                                    requestCancelInfo = details.getRequestCancelInfo();
                                }
                                orderListAnalytics.sendOrderDetailImpression(
                                        OrderListAnalyticsUtils.INSTANCE.getCategoryName(orderDetails),
                                        OrderListAnalyticsUtils.INSTANCE.getProductName(orderDetails),
                                        userSessionInterface.getUserId()
                                );
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
                                if (getView() != null) {
                                    getView().setRecommendation(rechargeWidgetResponse);
                                }
                            }
                        }
                        if (getView() != null && getView().getActivity() != null) {
                            getRecommendation(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                                    R.raw.recommendation_mp));
                        }
                    }
                });
            }
        }
    }

    public void getRecommendation(String query) {
        if (TextUtils.isEmpty(category)) category = "";
        Map<String, Object> variables = new HashMap<>();
        variables.put(DEVICE_ID, DEFAULT_DEVICE_ID);
        variables.put(CATEGORY_IDS, category);
        variables.put(MP_CATEGORY_IDS, categoryList);
        buyerGetRecommendationUseCase.setup(query, variables);
        buyerGetRecommendationUseCase.execute(new Subscriber<RecommendationResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(RecommendationResponse recommendationResponse) {
                if (getView() != null) {
                    getView().setRecommendation(recommendationResponse);
                }
            }
        });
    }

    @Override
    public void hitEventEmail(ActionButton actionButton, String metadata) {
        sendEventNotificationUseCase.setPath(actionButton.getUri());
        sendEventNotificationUseCase.setBody(metadata);
        sendEventNotificationUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().showSuccessMessageWithAction(e.getMessage());
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                if (getView() != null && getView().getActivity() != null) {
                    Type token = new TypeToken<SendEventEmail>() {
                    }.getType();
                    RestResponse restResponse = typeDataResponseMap.get(token);
                    getView().setActionButtonLayoutClickable(false);
                    if (restResponse.getCode() == 200 && restResponse.getErrorBody() == null) {
                        getView().setActionButtonText(getView().getActivity().getString(R.string.event_voucher_code_success));
                        getView().showSuccessMessageWithAction(getView().getActivity().getString(R.string.event_voucher_code_copied));
                    } else {
                        Gson gson = new Gson();
                        getView().setActionButtonText(getView().getActivity().getString(R.string.event_voucher_code_fail));
                        SendEventEmail dataResponse = gson.fromJson(restResponse.getErrorBody(), SendEventEmail.class);
                        getView().showSuccessMessageWithAction(dataResponse.getData().getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getActionButtonGql(String query, List<ActionButton> actionButtons, OrderListDetailContract.ActionInterface view, int position, boolean flag) {
        if (getView() != null) {
            Map<String, Object> variables = new HashMap<>();
            variables.put(PARAM, actionButtons);
            setActionButtonUseCase.setup(query, variables);
            setActionButtonUseCase.execute(new Subscriber<ActionButtonList>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Timber.d("error occured" + e);
                }

                @Override
                public void onNext(ActionButtonList actionButtonList) {
                    if (view != null) {
                        if (flag) {
                            if (actionButtonList != null) {
                                view.setTapActionButton(position, actionButtonList.getActionButtonList());
                                for (int i = 0; i < actionButtonList.getActionButtonList().size(); i++) {
                                    if (actionButtonList.getActionButtonList().get(i).getControl().equalsIgnoreCase(ItemsAdapter.KEY_REFRESH)) {
                                        actionButtonList.getActionButtonList().get(i).setBody(actionButtons.get(i).getBody());
                                    }
                                }
                            }
                        } else {
                            if (actionButtonList != null) {
                                view.setActionButton(position, actionButtonList.getActionButtonList());
                            }
                        }
                    } else {
                        if (actionButtonList != null && actionButtonList.getActionButtonList() != null
                                && actionButtonList.getActionButtonList().size() > 0 && getView() != null)
                            getView().setActionButtons(actionButtonList.getActionButtonList());
                    }
                }
            });
        }
    }

    @Override
    public void onBuyAgainItems(String query, List<Items> items, String eventActionLabel, String statusCode) {
        if (getView() != null && getView().getActivity() != null) {
            Map<String, Object> variables = new HashMap<>();

            JsonArray params = getView().generateInputQueryBuyAgain(items);
            variables.put(PARAM, params);
            addToCartMultiLegacyUseCase.setup(query, variables, userSessionInterface.getUserId());
            addToCartMultiLegacyUseCase.execute(new Subscriber<AtcMultiData>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().hideProgressBar();
                        getView().showErrorMessage(e.getMessage());
                    }
                }

                @Override
                public void onNext(AtcMultiData atcMultiData) {
                    if (getView() != null) {
                        getView().hideProgressBar();
                        if (atcMultiData.getAtcMulti().getBuyAgainData().getSuccess() == 1) {
                            getView().showSuccessMessageWithAction(StringUtils.convertListToStringDelimiter(atcMultiData.getAtcMulti().getBuyAgainData().getMessage(), ","));
                        } else {
                            getView().showErrorMessage(StringUtils.convertListToStringDelimiter(atcMultiData.getAtcMulti().getBuyAgainData().getMessage(), ","));
                        }
                        getView().hitAnalyticsBuyAgain(atcMultiData.getAtcMulti().getBuyAgainData().getListProducts(), atcMultiData.getAtcMulti().getBuyAgainData().getSuccess() == 1);
                    }
                }
            });
        }
    }

    public void finishOrderGql(String query, String orderId, String actionStatus, String userId) {
        if (getView() != null) {
            UohFinishOrderParam uohFinishOrderParam = new UohFinishOrderParam();
            uohFinishOrderParam.setOrderId(orderId);
            uohFinishOrderParam.setAction(actionStatus);
            uohFinishOrderParam.setUserId(userId);

            Map<String, Object> variables = new HashMap<>();
            variables.put(BuyerConsts.PARAM_INPUT, uohFinishOrderParam);
            finishOrderGqlUseCase.setup(query, variables);
            finishOrderGqlUseCase.execute(new Subscriber<UohFinishOrder.Data>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        Timber.d(e);
                        getView().hideProgressBar();
                        getView().showErrorMessage(e.getMessage());
                        getView().finishOrderDetail();
                    }
                }

                @Override
                public void onNext(UohFinishOrder.Data data) {
                    if (getView() != null) {
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
                }
            });
        }
    }

    @Override
    public void onLihatInvoiceButtonClick(String invoiceUrl) {
        orderListAnalytics.sendInvoiceClickEvent(
                OrderListAnalyticsUtils.INSTANCE.getCategoryName(orderDetails),
                OrderListAnalyticsUtils.INSTANCE.getProductName(orderDetails),
                userSessionInterface.getUserId()
        );
    }

    @Override
    public void onCopyButtonClick(String copiedValue) {
        orderListAnalytics.sendCopyButtonClickEvent(
                OrderListAnalyticsUtils.INSTANCE.getCategoryName(orderDetails),
                OrderListAnalyticsUtils.INSTANCE.getProductName(orderDetails),
                userSessionInterface.getUserId()
        );
    }

    @Override
    public void onActionButtonClick(String buttonId, String buttonName) {
        orderListAnalytics.sendActionButtonClickEvent(
                OrderListAnalyticsUtils.INSTANCE.getCategoryName(orderDetails),
                OrderListAnalyticsUtils.INSTANCE.getProductName(orderDetails),
                buttonId,
                buttonName,
                userSessionInterface.getUserId()
        );
    }

    @Override
    public String getOrderCategoryName() {
        return OrderListAnalyticsUtils.INSTANCE.getCategoryName(orderDetails);
    }

    @Override
    public String getOrderProductName() {
        return OrderListAnalyticsUtils.INSTANCE.getProductName(orderDetails);
    }

    @Override
    public void detachView() {
        orderDetailUseCase.unsubscribe();
        if (postCancelReasonUseCase != null) {
            postCancelReasonUseCase.unsubscribe();
        }
        if (finishOrderGqlUseCase != null) {
            finishOrderGqlUseCase.unsubscribe();
        }
        addToCartMultiLegacyUseCase.unsubscribe();
        super.detachView();
    }
}
