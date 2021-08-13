package com.tokopedia.buyerorder.list.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiLegacyUseCase;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.common.util.BuyerConsts;
import com.tokopedia.buyerorder.detail.data.CancelReplacementPojo;
import com.tokopedia.buyerorder.detail.data.DataResponseCommon;
import com.tokopedia.buyerorder.detail.domain.FinishOrderGqlUseCase;
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase;
import com.tokopedia.buyerorder.list.data.Data;
import com.tokopedia.buyerorder.list.data.FilterStatus;
import com.tokopedia.buyerorder.list.data.Order;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.bomorderfilter.OrderFilter;
import com.tokopedia.buyerorder.list.data.surveyrequest.CheckBOMSurveyParams;
import com.tokopedia.buyerorder.list.data.surveyrequest.InsertBOMSurveyParams;
import com.tokopedia.buyerorder.list.data.surveyresponse.CheckSurveyResponse;
import com.tokopedia.buyerorder.list.data.surveyresponse.InsertSurveyResponse;
import com.tokopedia.buyerorder.list.view.adapter.WishListResponseListener;
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListRecomTitleUiModel;
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListRecomUiModel;
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListUiModel;
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrder;
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrderParam;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.topads.sdk.domain.model.WishlistModel;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class OrderListPresenterImpl extends BaseDaggerPresenter<OrderListContract.View> implements OrderListContract.Presenter {
    OrderListContract.View view;

    private static final String ORDER_ID = "orderId";
    private static final String SEARCH = "Search";
    private static final String START_DATE = "StartDate";
    private static final String END_DATE = "EndDate";
    private static final String SORT = "Sort";
    private static final String ORDER_STATUS = "OrderStatus";
    private static final int PER_PAGE_COUNT = 10;
    private static final String SOURCE = "1";
    private static final String DEVICE_TYPE = "android";
    private static final String XSOURCE = "recom_widget";
    private static final String PAGE_NAME = "bom_empty";
    private GraphqlUseCase getOrderListUseCase;
    private GraphqlUseCase checkBomSurveyUseCase;
    private GraphqlUseCase insertBomSurveyUseCase;
    private final GetRecommendationUseCase getRecommendationUseCase;
    private final AddToCartUseCase addToCartUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final TopAdsWishlishedUseCase topAdsWishlishedUseCase;
    private final UserSessionInterface userSessionInterface;
    private List<Visitable> orderList = new ArrayList<>();
    private String recomTitle;
    private PostCancelReasonUseCase postCancelReasonUseCase;
    private AddToCartMultiLegacyUseCase addToCartMultiLegacyUseCase;
    private FinishOrderGqlUseCase finishOrderGqlUseCase;

    @Inject
    public OrderListPresenterImpl(GetRecommendationUseCase getRecommendationUseCase,
                                  AddToCartUseCase addToCartUseCase,
                                  AddWishListUseCase addWishListUseCase,
                                  RemoveWishListUseCase removeWishListUseCase,
                                  TopAdsWishlishedUseCase topAdsWishlishedUseCase,
                                  UserSessionInterface userSessionInterface,
                                  PostCancelReasonUseCase postCancelReasonUseCase,
                                  AddToCartMultiLegacyUseCase addToCartMultiLegacyUseCase,
                                  FinishOrderGqlUseCase finishOrderGqlUseCase) {
        this.getRecommendationUseCase = getRecommendationUseCase;
        this.addToCartUseCase = addToCartUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.topAdsWishlishedUseCase = topAdsWishlishedUseCase;
        this.userSessionInterface = userSessionInterface;
        this.postCancelReasonUseCase = postCancelReasonUseCase;
        this.addToCartMultiLegacyUseCase = addToCartMultiLegacyUseCase;
        this.finishOrderGqlUseCase = finishOrderGqlUseCase;
    }

    @Override
    public void processGetRecommendationData(Context context, int page, boolean isFirstTime) {
        if (getView() != null) {
            getView().displayLoadMore(true);
            RequestParams requestParam = getRecommendationUseCase.getRecomParams(
                    page,
                    XSOURCE,
                    PAGE_NAME,
                    new ArrayList<>(),
                    "");
            getRecommendationUseCase.execute(requestParam, new Subscriber<List<? extends RecommendationWidget>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                    }
                }
                @Override
                public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                        RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                        List<Visitable> visitables = new ArrayList<>();
                        if (isFirstTime && recommendationWidget.getRecommendationItemList().size() > 0) {
                            recomTitle = !TextUtils.isEmpty(recommendationWidget.getTitle())
                                    ? recommendationWidget.getTitle()
                                    : context.getResources().getString(R.string.order_list_title_recommendation);
                            visitables.add(new OrderListRecomTitleUiModel(recomTitle));
                        }
                        visitables.addAll(getRecommendationVisitables(recommendationWidget));
                        getView().addData(visitables, true, false);
                    }
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        if (getView() != null) {
            orderList.clear();
            getView().displayLoadMore(false);
        }
    }

    @NonNull
    private List<Visitable> getRecommendationVisitables(RecommendationWidget recommendationWidget) {
        List<Visitable> recommendationList = new ArrayList<>();
        for (RecommendationItem item : recommendationWidget.getRecommendationItemList()) {
            recommendationList.add(new OrderListRecomUiModel(item, recomTitle));
        }
        return recommendationList;
    }

    @NonNull
    private List<Visitable> getOrderListVisitables(Data data) {
        List<Visitable> orderList = new ArrayList<>();
        for (Order item : data.orders()) {
            orderList.add(new OrderListUiModel(item));
        }
        return orderList;
    }

    @Override
    public void getAllOrderData(Context context, String orderCategory, final int typeRequest, int page, int orderId) {
        if (getView() != null) {
            getView().showProcessGetData();
            if (page != 0) {
                getView().displayLoadMore(true);
            }
            GraphqlRequest graphqlRequest;
            Map<String, Object> variables = new HashMap<>();
            if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)|| orderCategory.equalsIgnoreCase(OrderCategory.DIGITAL)) {
                variables.put(OrderCategory.KEY_LABEL, orderCategory);
                variables.put(OrderCategory.PAGE, page);
                variables.put(OrderCategory.PER_PAGE, PER_PAGE_COUNT);
                variables.put(SEARCH, getView().getSearchedString());
                variables.put(START_DATE, getView().getStartDate());
                variables.put(END_DATE, getView().getEndDate());
                variables.put(SORT, "");
                if(orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)) {
                    variables.put(ORDER_STATUS, Integer.parseInt(getView().getSelectedFilter()));
                }
                graphqlRequest = new
                        GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                        R.raw.orderlist_marketplace), Data.class, variables, false);
            } else {
                if (orderCategory.equalsIgnoreCase(OrderCategory.EVENTS)){
                    variables.put(OrderCategory.KEY_LABEL, OrderCategory.EVENT);
                }else{
                    variables.put(OrderCategory.KEY_LABEL, orderCategory);
                }
                variables.put(OrderCategory.PAGE, page);
                variables.put(OrderCategory.PER_PAGE, PER_PAGE_COUNT);
                variables.put(ORDER_ID, orderId);
                graphqlRequest = new
                        GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                        R.raw.orderlist), Data.class, variables, false);
            }
            getOrderListUseCase = new GraphqlUseCase();
            getOrderListUseCase.clearRequest();
            getOrderListUseCase.addRequest(graphqlRequest);
            getOrderListUseCase.addRequest(getorderFiltergqlRequest(context));

            getOrderListUseCase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        Timber.d("error =" + e.toString());
                        getView().removeProgressBarView();
                        getView().displayLoadMore(false);
                        getView().unregisterScrollListener();
                        getView().showErrorNetwork(e.toString());
                    }
                }

                @Override
                public void onNext(GraphqlResponse response) {
                    if (getView() != null) {
                        getView().removeProgressBarView();
                        getView().displayLoadMore(false);
                        long elapsedDays = 0;
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        long secondsInMilli = 1000;
                        long time;
                        long daysInMilli = secondsInMilli * 60 * 60 * 24;
                        try {
                            if (getView().getEndDate() != null && getView().getStartDate() != null) {
                                Date date2 = format.parse(getView().getEndDate());
                                Date date1 = format.parse(getView().getStartDate());
                                time = date2.getTime() - date1.getTime();
                                elapsedDays = time / daysInMilli;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (response != null) {
                            Data data = response.getData(Data.class);
                            if (!data.orders().isEmpty()) {
                                orderList.addAll(getOrderListVisitables(data));
                                getView().addData(getOrderListVisitables(data), false, typeRequest == TxOrderNetInteractor.TypeRequest.INITIAL);
                                getView().setLastOrderId(data.orders().get(0).getOrderId());
                                if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)) {
                                    checkBomSurveyEligibility(context);
                                }
                            } else {
                                getView().unregisterScrollListener();
                                getView().renderEmptyList(typeRequest,elapsedDays);
                            }

                            OrderFilter orderFilter = response.getData(OrderFilter.class);
                            if (orderFilter != null && orderFilter != null) {
                                getView().setFilterRange(orderFilter.getGetBomOrderFilter().getDefaultDate(), orderFilter.getGetBomOrderFilter().getCustomDate());
                            }
                        } else {
                            getView().unregisterScrollListener();
                            getView().renderEmptyList(typeRequest,elapsedDays);
                        }
                    }
                }
            });
        }
    }

    private GraphqlRequest getorderFiltergqlRequest(Context context) {
        GraphqlRequest orderfiltergqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.bomorderfilter), OrderFilter.class);
        return orderfiltergqlRequest;
    }

    public void buildAndRenderFilterList(List<FilterStatus> filterItems) {
        if (getView() != null) {
            List<QuickFilterItem> quickFilterItems = new ArrayList<>();
            int selectedIndex = 0;
            boolean isAnyItemSelected = false;
            for (FilterStatus entry : filterItems) {
                CustomViewRoundedQuickFilterItem finishFilter = new CustomViewRoundedQuickFilterItem();
                finishFilter.setName(entry.getFilterName());
                finishFilter.setType(entry.getFilterLabel());
                finishFilter.setColorBorder(com.tokopedia.unifyprinciples.R.color.Unify_G400);
                if (getView().getSelectedFilter().equalsIgnoreCase(entry.getFilterLabel())) {
                    isAnyItemSelected = true;
                    finishFilter.setSelected(true);
                    selectedIndex = filterItems.indexOf(entry);
                } else {
                    finishFilter.setSelected(false);
                }
                quickFilterItems.add(finishFilter);
            }
            //If there is no selected Filter then we will select the first filter item by default
            if (selectedIndex == 0 && !isAnyItemSelected) {
                quickFilterItems.get(selectedIndex).setSelected(true);
            }
            getView().renderOrderStatus(quickFilterItems, selectedIndex);
        }
    }

    @Override
    public void detachView() {
        getOrderListUseCase.unsubscribe();
        if (checkBomSurveyUseCase != null) {
            checkBomSurveyUseCase.unsubscribe();
        }
        if (insertBomSurveyUseCase != null) {
            insertBomSurveyUseCase.unsubscribe();
        }
        if (getRecommendationUseCase != null) {
            getRecommendationUseCase.unsubscribe();
        }
        if (addToCartUseCase != null) {
            addToCartUseCase.unsubscribe();
        }
        addToCartMultiLegacyUseCase.unsubscribe();
        if (finishOrderGqlUseCase != null) {
            finishOrderGqlUseCase.unsubscribe();
        }
        super.detachView();
    }


    public void checkBomSurveyEligibility(Context context) {
        Map<String, Object> variables = new HashMap<>();

        CheckBOMSurveyParams checkBOMSurveyParams = new CheckBOMSurveyParams();
        checkBOMSurveyParams.setSource(SOURCE);

        variables.put(OrderCategory.SURVEY_PARAM, checkBOMSurveyParams);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.checkbomsurvey), CheckSurveyResponse.class, variables);
        checkBomSurveyUseCase = new GraphqlUseCase();
        checkBomSurveyUseCase.clearRequest();
        checkBomSurveyUseCase.addRequest(graphqlRequest);


        checkBomSurveyUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().showFailureMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (getView() != null && graphqlResponse != null) {
                    CheckSurveyResponse checkSurveyResponse = graphqlResponse.getData(CheckSurveyResponse.class);
                    if (checkSurveyResponse != null && checkSurveyResponse.getCheckResponseData() != null) {
                        if (checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders() != null && checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages() != null && checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().size() > 0) {
                            getView().showFailureMessage(checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().get(0));
                        } else {
                            if (checkSurveyResponse.getCheckResponseData().getCheckResponseSurveyData() != null) {
                                getView().showSurveyButton(checkSurveyResponse.getCheckResponseData().getCheckResponseSurveyData().isEligible());
                            }
                        }
                    }
                }
            }
        });

    }

    public void insertSurveyRequest(Context context, int rating, String comment) {
        if (getView() != null) {
            Map<String, Object> variables = new HashMap<>();

            InsertBOMSurveyParams insertBOMSurveyParams = new InsertBOMSurveyParams();
            insertBOMSurveyParams.setRating(rating);
            insertBOMSurveyParams.setComments(comment);
            insertBOMSurveyParams.setDeviceType(DEVICE_TYPE);

            variables.put(OrderCategory.SURVEY_PARAM, insertBOMSurveyParams);

            GraphqlRequest graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                    R.raw.insertbomsurvey), InsertSurveyResponse.class, variables);
            insertBomSurveyUseCase = new GraphqlUseCase();
            insertBomSurveyUseCase.clearRequest();
            insertBomSurveyUseCase.addRequest(graphqlRequest);

            insertBomSurveyUseCase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().showFailureMessage(e.getMessage());
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    if (getView() != null) {
                        if (graphqlResponse != null) {
                            InsertSurveyResponse insertSurveyResponse = graphqlResponse.getData(InsertSurveyResponse.class);
                            if (insertSurveyResponse != null && insertSurveyResponse.getCheckResponseData() != null) {
                                if (insertSurveyResponse.getCheckResponseData().getCheckResponseSurveyData().isSuccess()) {
                                    getView().showSuccessMessage(context.getResources().getString(R.string.survey_submit));
                                    getView().showSurveyButton(false);
                                } else {
                                    if (insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders() != null && insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages() != null && insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().size() > 0) {
                                        getView().showFailureMessage(insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().get(0));
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public void processAddToCart(Object productModel) {
        if (getView() != null) {
            getView().displayLoadMore(true);

            int productId = 0;
            int shopId = 0;
            int quantity = 0;
            String productName = "";
            String productCategory = "";
            String productPrice = "";
            String externalSource = "";
            String clickUrl = "";
            String imageUrl = "";
            if (productModel instanceof OrderListRecomUiModel) {
                OrderListRecomUiModel orderListRecomUiModel = (OrderListRecomUiModel) productModel;
                productId = orderListRecomUiModel.getRecommendationItem().getProductId();
                shopId = orderListRecomUiModel.getRecommendationItem().getShopId();
                productName = orderListRecomUiModel.getRecommendationItem().getName();
                productCategory = orderListRecomUiModel.getRecommendationItem().getCategoryBreadcrumbs();
                productPrice = orderListRecomUiModel.getRecommendationItem().getPrice();
                externalSource = "recommendation_list";
                clickUrl = orderListRecomUiModel.getRecommendationItem().getClickUrl();
                imageUrl = orderListRecomUiModel.getRecommendationItem().getImageUrl();
                quantity = orderListRecomUiModel.getRecommendationItem().getMinOrder();
            }

            if(!clickUrl.isEmpty()) {
                getView().sendATCTrackingUrl(clickUrl, String.valueOf(productId), productName, imageUrl);
            }
            AddToCartRequestParams addToCartRequestParams = new AddToCartRequestParams();
            addToCartRequestParams.setProductId(productId);
            addToCartRequestParams.setShopId(shopId);
            addToCartRequestParams.setQuantity(quantity);
            addToCartRequestParams.setNotes("");
            addToCartRequestParams.setWarehouseId(0);
            addToCartRequestParams.setAtcFromExternalSource(externalSource);
            addToCartRequestParams.setProductName(productName);
            addToCartRequestParams.setCategory(productCategory);
            addToCartRequestParams.setPrice(productPrice);
            addToCartRequestParams.setUserId(userSessionInterface.getUserId());

            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams);
            addToCartUseCase.createObservable(requestParams)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddToCartDataModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (getView() != null) {
                                getView().displayLoadMore(false);
                                String errorMessage = e.getMessage();
                                getView().showFailureMessage(errorMessage);
                            }
                        }

                        @Override
                        public void onNext(AddToCartDataModel addToCartDataModel) {
                            if (getView() != null) {
                                getView().displayLoadMore(false);
                                if (addToCartDataModel.getStatus().equalsIgnoreCase(AddToCartDataModel.STATUS_OK) && addToCartDataModel.getData().getSuccess() == 1) {
                                    getView().triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel);
                                    if (addToCartDataModel.getData().getMessage().size() > 0) {
                                        getView().showSuccessMessage(addToCartDataModel.getData().getMessage().get(0));
                                    }
                                } else {
                                    if (addToCartDataModel.getErrorMessage().size() > 0) {
                                        getView().showFailureMessage(addToCartDataModel.getErrorMessage().get(0));
                                    }
                                }
                            }

                        }
                    });
        }
    }

    public void addWishlist(RecommendationItem model, WishListResponseListener wishListResponseListener) {
        if (getView() != null) {
            getView().displayLoadMore(true);
            if (model.isTopAds()) {
                RequestParams params = RequestParams.create();
                params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.getWishlistUrl());
                topAdsWishlishedUseCase.execute(params, new Subscriber<WishlistModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().displayLoadMore(false);
                            String errorMessage = e.getMessage();
                            getView().showFailureMessage(errorMessage);
                        }
                    }

                    @Override
                    public void onNext(WishlistModel wishlistModel) {
                        if (getView() != null) {
                            getView().displayLoadMore(false);
                            if (wishlistModel.getData() != null) {
                                wishListResponseListener.onWhishListSuccessResponse(true);
                                getView().showSuccessMessage(getView().getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist));
                            }
                        }
                    }
                });
            } else {
                addWishListUseCase.createObservable(String.valueOf(model.getProductId()), userSessionInterface.getUserId(), new WishListActionListener() {
                    @Override
                    public void onErrorAddWishList(String errorMessage, String productId) {
                        if (getView() != null) {
                            getView().displayLoadMore(false);
                            getView().showFailureMessage(errorMessage);
                        }
                    }

                    @Override
                    public void onSuccessAddWishlist(String productId) {
                        if (getView() != null) {
                            getView().displayLoadMore(false);
                            wishListResponseListener.onWhishListSuccessResponse(true);
                            getView().showSuccessMessage(getView().getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist));
                        }
                    }

                    @Override
                    public void onErrorRemoveWishlist(String errorMessage, String productId) {

                    }

                    @Override
                    public void onSuccessRemoveWishlist(String productId) {

                    }
                });
            }
        }
    }

    public void removeWishlist(RecommendationItem model, WishListResponseListener wishListResponseListener) {
        if (getView() != null) {
            getView().displayLoadMore(true);
            removeWishListUseCase.createObservable(String.valueOf(model.getProductId()), userSessionInterface.getUserId(), new WishListActionListener() {
                @Override
                public void onErrorAddWishList(String errorMessage, String productId) {

                }

                @Override
                public void onSuccessAddWishlist(String productId) {

                }

                @Override
                public void onErrorRemoveWishlist(String errorMessage, String productId) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                        getView().showFailureMessage(errorMessage);
                    }
                }

                @Override
                public void onSuccessRemoveWishlist(String productId) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                        getView().showSuccessMessage(getView().getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist));
                        wishListResponseListener.onWhishListSuccessResponse(false);
                    }
                }
            });
        }
    }

    public void updateOrderCancelReason(Context context, String cancelReason, String orderId,
                                        int cancelOrReplacement, String url) {
        if (getView() != null) {
            UserSession userSession = new UserSession(context);
            String userId = userSession.getUserId();

            RequestParams requestParams = RequestParams.create();
            requestParams.putString("reason_cancel", cancelReason);
            requestParams.putString("user_id", userId);
            requestParams.putString("order_id", orderId);
            requestParams.putString("device_id", userSession.getDeviceId());
            if (cancelOrReplacement != 1) {
                requestParams.putInt("r_code", cancelOrReplacement);
            }
            getView().displayLoadMore(true);

            postCancelReasonUseCase.setRequestParams(requestParams);
            postCancelReasonUseCase.cancelOrReplaceOrder(url);
            postCancelReasonUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    if (getView() != null) {
                                                        Timber.d(e);
                                                        getView().showFailureMessage(e.getMessage());
                                                        getView().displayLoadMore(false);
                                                        getView().finishOrderDetail();
                                                    }
                                                }

                                                @Override
                                                public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                                                    if (getView() != null && typeDataResponseMap != null) {
                                                        Type token = new TypeToken<DataResponseCommon<CancelReplacementPojo>>() {
                                                        }.getType();
                                                        RestResponse restResponse = typeDataResponseMap.get(token);
                                                        if (restResponse != null) {
                                                            DataResponseCommon dataResponse = restResponse.getData();
                                                            if (dataResponse != null) {
                                                                CancelReplacementPojo cancelReplacementPojo = (CancelReplacementPojo) dataResponse.getData();
                                                                if (cancelReplacementPojo != null) {
                                                                    if (!TextUtils.isEmpty(cancelReplacementPojo.getMessageStatus()))
                                                                        getView().showSuccessMessage(cancelReplacementPojo.getMessageStatus());
                                                                    else if (dataResponse.getErrorMessage() != null && !dataResponse.getErrorMessage().isEmpty())
                                                                        getView().showFailureMessage((String) dataResponse.getErrorMessage().get(0));
                                                                    else if ((dataResponse.getMessageStatus() != null && !dataResponse.getMessageStatus().isEmpty()))
                                                                        getView().showSuccessMessage((String) dataResponse.getMessageStatus().get(0));
                                                                    getView().displayLoadMore(false);
                                                                    getView().finishOrderDetail();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
            );
        }
    }

    public void finishOrderGql(String orderId, String actionStatus) {
        if (getView() != null) {
            UohFinishOrderParam uohFinishOrderParam = new UohFinishOrderParam();
            uohFinishOrderParam.setOrderId(orderId);
            uohFinishOrderParam.setAction(actionStatus);
            uohFinishOrderParam.setUserId(userSessionInterface.getUserId());

            Map<String, Object> variables = new HashMap<>();
            variables.put(BuyerConsts.PARAM_INPUT, uohFinishOrderParam);
            if (getView() != null && getView().getActivity() != null) {
                finishOrderGqlUseCase.setup(GraphqlHelper.loadRawString(getView().getActivity().getResources(), R.raw.uoh_finish_order), variables);
                finishOrderGqlUseCase.execute(new Subscriber<UohFinishOrder.Data>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            Timber.d(e);
                            getView().displayLoadMore(false);
                            getView().showFailureMessage(e.getMessage());
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
                                    getView().showFailureMessage(data.getFinishOrderBuyer().getMessage().get(0));
                                }
                            }
                            getView().displayLoadMore(false);
                            getView().finishOrderDetail();
                        }
                    }
                });
            }
        }
    }
}