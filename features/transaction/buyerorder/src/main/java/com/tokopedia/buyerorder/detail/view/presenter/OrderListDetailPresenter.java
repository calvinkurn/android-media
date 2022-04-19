package com.tokopedia.buyerorder.detail.view.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.detail.analytics.OrderListAnalyticsUtils;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.ActionButtonList;
import com.tokopedia.buyerorder.detail.data.DetailsData;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.SendEventEmail;
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse;
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase;
import com.tokopedia.buyerorder.detail.domain.SetActionButtonUseCase;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter;
import com.tokopedia.common.network.data.model.RestResponse;
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
    private static final String PARAM_INPUT = "input";
    private static final String PARAM_CHANNEL_NAME = "channelName";
    private static final String PARAM_CLIENT_NUMBERS = "clientNumbers";
    private static final String PARAM_DG_CATEGORY_IDS = "dgCategoryIDs";
    private static final String PARAM_PG_CATEGORY_IDS = "pgCategoryIDs";
    private static final String DIGI_PERSO_CHANNEL_NAME = "dg_order_detail_recommendation";

    private final UserSessionInterface userSessionInterface;
    GraphqlUseCase orderDetailUseCase;
    OrderDetails orderDetails;
    @Inject
    OrderListAnalytics orderListAnalytics;
    @Inject
    SendEventNotificationUseCase sendEventNotificationUseCase;
    @Inject
    SetActionButtonUseCase setActionButtonUseCase;

    public String pdfUri = " ";

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailUseCase,
                                    UserSessionInterface userSessionInterface,
                                    SetActionButtonUseCase setActionButtonUseCase,
                                    SendEventNotificationUseCase sendEventNotificationUseCase) {
        this.orderDetailUseCase = orderDetailUseCase;
        this.userSessionInterface = userSessionInterface;
        this.setActionButtonUseCase = setActionButtonUseCase;
        this.sendEventNotificationUseCase = sendEventNotificationUseCase;
    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory, String upstream) {
        if (getView() != null && getView().getActivity() != null) {
            getView().showProgressBar();
            GraphqlRequest graphqlRequest;
            Map<String, Object> variables = new HashMap<>();
            variables.put(ORDER_CATEGORY, orderCategory);
            variables.put(ORDER_ID, orderId);
            variables.put(DETAIL, 1);
            variables.put(ACTION, 1);
            variables.put(UPSTREAM, upstream != null ? upstream : "");
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                    R.raw.orderdetails), DetailsData.class, variables, false);

            orderDetailUseCase.addRequest(graphqlRequest);

            GraphqlRequest requestDigiPersoRecomm = null;
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> input = new HashMap<>();

            ArrayList<Integer> dgCategoryIds = new ArrayList<>();
            ArrayList<Integer> pgCategoryIds = new ArrayList<>();
            ArrayList<String> clientNumbers = new ArrayList<>();

            clientNumbers.add(userSessionInterface.getPhoneNumber());

            input.put(PARAM_CHANNEL_NAME, DIGI_PERSO_CHANNEL_NAME);
            input.put(PARAM_CLIENT_NUMBERS, clientNumbers);
            input.put(PARAM_DG_CATEGORY_IDS, dgCategoryIds);
            input.put(PARAM_PG_CATEGORY_IDS, pgCategoryIds);
            params.put(PARAM_INPUT, input);

            if (getView() != null && getView().getActivity() != null) {
                requestDigiPersoRecomm = new
                        GraphqlRequest(GraphqlHelper.loadRawString(getView().getActivity().getResources(),
                        R.raw.recharge_widget), RecommendationDigiPersoResponse.class, params, false);
            }

            if (requestDigiPersoRecomm != null) {
                orderDetailUseCase.addRequest(requestDigiPersoRecomm);
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
                                orderDetails = data.getOrderDetails();
                                getView().setDetailsData(data.getOrderDetails());
                                orderListAnalytics.sendOrderDetailImpression(
                                        userSessionInterface.getUserId()
                                );
                            }

                            RecommendationDigiPersoResponse rechargeWidgetResponse = response.getData(RecommendationDigiPersoResponse.class);
                            if (getView() != null) {
                                getView().setRecommendation(rechargeWidgetResponse);
                            }
                        }
                    }
                });
            }
        }
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
        super.detachView();
    }
}
