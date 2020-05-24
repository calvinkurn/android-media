package com.tokopedia.buyerorder.list.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.detail.data.CancelReplacementPojo;
import com.tokopedia.buyerorder.detail.data.DataResponseCommon;
import com.tokopedia.buyerorder.detail.data.DetailsData;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.RequestCancelInfo;
import com.tokopedia.buyerorder.detail.data.ShopInfo;
import com.tokopedia.buyerorder.detail.data.Status;
import com.tokopedia.buyerorder.detail.domain.FinishOrderUseCase;
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
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
import com.tokopedia.buyerorder.list.view.adapter.viewModel.OrderListRecomViewModel;
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListRecomTitleViewModel;
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListViewModel;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.topads.sdk.domain.model.WishlistModel;
import com.tokopedia.transaction.common.sharedata.buyagain.ResponseBuyAgain;
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

import static com.tokopedia.buyerorder.list.view.fragment.OrderListFragment.ACTION_ASK_SELLER;
import static com.tokopedia.buyerorder.list.view.fragment.OrderListFragment.ACTION_BUY_AGAIN;
import static com.tokopedia.buyerorder.list.view.fragment.OrderListFragment.ACTION_SUBMIT_CANCELLATION;

public class OrderListPresenterImpl extends BaseDaggerPresenter<OrderListContract.View> implements OrderListContract.Presenter {

    private static final String ORDER_CATEGORY = "orderCategoryStr";
    private static final String ORDER_ID = "orderId";
    private static final String PAYMENT_ID = "paymentId";
    private static final String CART_STRING = "cartString";
    private static final String DETAIL = "detail";
    private static final String ACTION = "action";
    private static final String UPSTREAM = "upstream";
    private static final String PARAM = "param";
    private static final String INVOICE = "invoice";
    private static final String PRODUCT_ID = "product_id";
    private static final String QUANTITY = "quantity";
    private static final String NOTES = "notes";
    private static final String SHOP_ID = "shop_id";
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
    private OrderListAnalytics orderListAnalytics;
    private OrderDetails orderDetails;
    private RequestCancelInfo requestCancelInfo;
    private PostCancelReasonUseCase postCancelReasonUseCase;
    private FinishOrderUseCase finishOrderUseCase;

    @Inject
    public OrderListPresenterImpl(GetRecommendationUseCase getRecommendationUseCase,
                                  AddToCartUseCase addToCartUseCase,
                                  AddWishListUseCase addWishListUseCase,
                                  RemoveWishListUseCase removeWishListUseCase,
                                  TopAdsWishlishedUseCase topAdsWishlishedUseCase,
                                  UserSessionInterface userSessionInterface,
                                  OrderListAnalytics orderListAnalytics,
                                  PostCancelReasonUseCase postCancelReasonUseCase, FinishOrderUseCase finishOrderUseCase) {
        this.getRecommendationUseCase = getRecommendationUseCase;
        this.addToCartUseCase = addToCartUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.topAdsWishlishedUseCase = topAdsWishlishedUseCase;
        this.userSessionInterface = userSessionInterface;
        this.orderListAnalytics = orderListAnalytics;
        this.postCancelReasonUseCase = postCancelReasonUseCase;
        this.finishOrderUseCase = finishOrderUseCase;
    }

    @Override
    public void processGetRecommendationData(int page, boolean isFirstTime) {
        if (getView() == null)
            return;
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
                getView().displayLoadMore(false);
            }
            @Override
            public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                getView().displayLoadMore(false);
                RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                List<Visitable> visitables = new ArrayList<>();
                if (isFirstTime && recommendationWidget.getRecommendationItemList().size() > 0) {
                    recomTitle = !TextUtils.isEmpty(recommendationWidget.getTitle())
                            ? recommendationWidget.getTitle()
                            : getView().getAppContext().getResources().getString(R.string.order_list_title_recommendation);
                    visitables.add(new OrderListRecomTitleViewModel(recomTitle));
                }
                visitables.addAll(getRecommendationVisitables(recommendationWidget));
                getView().addData(visitables, true, false);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (getView() == null)
            return;
        orderList.clear();
        getView().displayLoadMore(false);
    }

    @NonNull
    private List<Visitable> getRecommendationVisitables(RecommendationWidget recommendationWidget) {
        List<Visitable> recommendationList = new ArrayList<>();
        for (RecommendationItem item : recommendationWidget.getRecommendationItemList()) {
            recommendationList.add(new OrderListRecomViewModel(item, recomTitle));
        }
        return recommendationList;
    }

    @NonNull
    private List<Visitable> getOrderListVisitables(Data data) {
        List<Visitable> orderList = new ArrayList<>();
        for (Order item : data.orders()) {
            orderList.add(new OrderListViewModel(item));
        }
        return orderList;
    }

    @Override
    public void getAllOrderData(Context context, String orderCategory, final int typeRequest, int page, int orderId) {
        if (getView() == null || getView().getAppContext() == null)
            return;
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
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist_marketplace), Data.class, variables, false);
        } else {
            variables.put(OrderCategory.KEY_LABEL, orderCategory);
            variables.put(OrderCategory.PAGE, page);
            variables.put(OrderCategory.PER_PAGE, PER_PAGE_COUNT);
            variables.put(ORDER_ID, orderId);
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist), Data.class, variables, false);
        }
        getOrderListUseCase = new GraphqlUseCase();
        getOrderListUseCase.clearRequest();
        getOrderListUseCase.addRequest(graphqlRequest);
        getOrderListUseCase.addRequest(getorderFiltergqlRequest());

        getOrderListUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    Timber.d("error =" + e.toString());
                    getView().removeProgressBarView();
                    getView().displayLoadMore(false);
                    getView().unregisterScrollListener();
                    getView().showErrorNetwork(
                            ErrorHandler.getErrorMessage(getView().getAppContext(), e));
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (getView() == null || getView().getAppContext() == null)
                    return;
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
                            checkBomSurveyEligibility();
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
        });
    }

    private GraphqlRequest getorderFiltergqlRequest() {
        GraphqlRequest orderfiltergqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.bomorderfilter), OrderFilter.class);
        return orderfiltergqlRequest;
    }

    public void buildAndRenderFilterList(List<FilterStatus> filterItems) {
        if (getView() == null)
            return;
        List<QuickFilterItem> quickFilterItems = new ArrayList<>();
        int selectedIndex = 0;
        boolean isAnyItemSelected = false;
        for (FilterStatus entry : filterItems) {
            CustomViewRoundedQuickFilterItem finishFilter = new CustomViewRoundedQuickFilterItem();
            finishFilter.setName(entry.getFilterName());
            finishFilter.setType(entry.getFilterLabel());
            finishFilter.setColorBorder(R.color.tkpd_main_green);
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
        super.detachView();
    }


    public void checkBomSurveyEligibility() {
        if (getView() == null || getView().getAppContext() == null)
            return;
        Map<String, Object> variables = new HashMap<>();

        CheckBOMSurveyParams checkBOMSurveyParams = new CheckBOMSurveyParams();
        checkBOMSurveyParams.setSource(SOURCE);

        variables.put(OrderCategory.SURVEY_PARAM, checkBOMSurveyParams);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
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
                if (isViewAttached()) {
                    getView().showFailureMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewAttached()) {
                    if (graphqlResponse != null) {
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
            }
        });

    }

    public void insertSurveyRequest(int rating, String comment) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        Map<String, Object> variables = new HashMap<>();

        InsertBOMSurveyParams insertBOMSurveyParams = new InsertBOMSurveyParams();
        insertBOMSurveyParams.setRating(rating);
        insertBOMSurveyParams.setComments(comment);
        insertBOMSurveyParams.setDeviceType(DEVICE_TYPE);

        variables.put(OrderCategory.SURVEY_PARAM, insertBOMSurveyParams);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
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
                if (isViewAttached())
                    getView().showFailureMessage(e.getMessage());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewAttached()) {
                    if (graphqlResponse != null) {
                        InsertSurveyResponse insertSurveyResponse = graphqlResponse.getData(InsertSurveyResponse.class);
                        if (insertSurveyResponse != null && insertSurveyResponse.getCheckResponseData() != null) {
                            if (insertSurveyResponse.getCheckResponseData().getCheckResponseSurveyData().isSuccess()) {
                                getView().showSuccessMessage(getView().getAppContext().getResources().getString(R.string.survey_submit));
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

    public void processAddToCart(Object productModel) {
        if (getView() == null)
            return;
        getView().displayLoadMore(true);

        int productId = 0;
        int shopId = 0;
        String productName = "";
        String productCategory = "";
        String productPrice = "";
        String externalSource = "";
        String clickUrl = "";
        if (productModel instanceof OrderListRecomViewModel) {
            OrderListRecomViewModel orderListRecomViewModel = (OrderListRecomViewModel) productModel;
            productId = orderListRecomViewModel.getRecommendationItem().getProductId();
            shopId = orderListRecomViewModel.getRecommendationItem().getShopId();
            productName = orderListRecomViewModel.getRecommendationItem().getName();
            productCategory = orderListRecomViewModel.getRecommendationItem().getCategoryBreadcrumbs();
            productPrice = orderListRecomViewModel.getRecommendationItem().getPrice();
            externalSource = "recommendation_list";
            clickUrl = orderListRecomViewModel.getRecommendationItem().getClickUrl();
        }

        if(!clickUrl.isEmpty()) {
            getView().sendATCTrackingUrl(clickUrl);
        }
        AddToCartRequestParams addToCartRequestParams = new AddToCartRequestParams();
        addToCartRequestParams.setProductId(productId);
        addToCartRequestParams.setShopId(shopId);
        addToCartRequestParams.setQuantity(0);
        addToCartRequestParams.setNotes("");
        addToCartRequestParams.setWarehouseId(0);
        addToCartRequestParams.setAtcFromExternalSource(externalSource);
        addToCartRequestParams.setProductName(productName);
        addToCartRequestParams.setCategory(productCategory);
        addToCartRequestParams.setPrice(productPrice);

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

    public void addWishlist(RecommendationItem model, WishListResponseListener wishListResponseListener) {
        if (getView() == null)
            return;
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
                            getView().showSuccessMessage(getView().getString(R.string.msg_success_add_wishlist));
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
                        getView().showSuccessMessage(getView().getString(R.string.msg_success_add_wishlist));
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

    public void removeWishlist(RecommendationItem model, WishListResponseListener wishListResponseListener) {
        if (getView() == null)
            return;
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
                    getView().showSuccessMessage(getView().getString(R.string.msg_success_remove_wishlist));
                    wishListResponseListener.onWhishListSuccessResponse(false);
                }
            }
        });
    }

    private void buyAgainItem() {
        if (getView() == null || getView().getAppContext() == null)
            return;
        Map<String, Object> variables = new HashMap<>();
        JsonObject passenger = new JsonObject();
        variables.put(PARAM, generateInputQueryBuyAgain(orderDetails.getItems()));
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.buy_again), ResponseBuyAgain.class, variables, false);
        getView().displayLoadMore(true);
        GraphqlUseCase buyAgainUseCase = new GraphqlUseCase();
        buyAgainUseCase.clearRequest();
        buyAgainUseCase.addRequest(graphqlRequest);

        buyAgainUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    getView().displayLoadMore(false);
                    getView().showFailureMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                if (getView() != null && getView().getAppContext() != null) {
                    getView().displayLoadMore(false);
                    ResponseBuyAgain responseBuyAgain = objects.getData(ResponseBuyAgain.class);
                    if (responseBuyAgain.getAddToCartMulti().getData().getSuccess() == 1) {
                        getView().showSuccessMessageWithAction(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(), ","));
                    } else {
                        getView().showFailureMessage(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(), ","));
                    }
                    orderListAnalytics.sendBuyAgainEvent(orderDetails.getItems(), orderDetails.getShopInfo(), responseBuyAgain.getAddToCartMulti().getData().getData(), responseBuyAgain.getAddToCartMulti().getData().getSuccess() == 1, false, "", getStatus().status());
                }

            }
        });
    }

    public void setOrderDetails(String orderId, String orderCategory, String buttonLabel, String paymentId, String cartString) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        getView().displayLoadMore(true);
        GraphqlRequest graphqlRequest;
        Map<String, Object> variables = new HashMap<>();
        if (orderCategory.equalsIgnoreCase("marketplace")) {
            variables.put("orderCategory", orderCategory);
            variables.put(PAYMENT_ID, paymentId);
            variables.put(CART_STRING, cartString);
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderdetail_marketplace), DetailsData.class, variables, false);
        } else {
            variables.put(ORDER_CATEGORY, orderCategory);
            variables.put(ORDER_ID, orderId);
            variables.put(DETAIL, 1);
            //assuming that fromPayment is false, although confirmation required
            variables.put(ACTION, 1);
            variables.put(UPSTREAM, "");
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderdetails), DetailsData.class, variables, false);
        }

        GraphqlUseCase orderDetailsUseCase = new GraphqlUseCase();
        orderDetailsUseCase.clearRequest();
        orderDetailsUseCase.addRequest(graphqlRequest);

        orderDetailsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    Timber.d("error occured" + e);
                    getView().displayLoadMore(false);
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (getView() == null || getView().getAppContext() == null)
                    return;
                if (response != null) {
                    DetailsData data = response.getData(DetailsData.class);
                    orderDetails = data.orderDetails();
                    requestCancelInfo = orderDetails.getRequestCancelInfo();
                    handleActionButtonClick(buttonLabel);
                }
            }
        });
    }

    private void handleActionButtonClick(String buttonLabel) {
        switch (buttonLabel) {
            case ACTION_BUY_AGAIN:
                buyAgainItem();
                break;
            case ACTION_ASK_SELLER:
                getView().startSellerAndAddInvoice();
                orderListAnalytics.sendActionButtonClickEventList("click ask seller",orderDetails.getStatusInfo());
                break;
            case ACTION_SUBMIT_CANCELLATION:
                getView().requestCancelOrder(getStatus());
                orderListAnalytics.sendActionButtonClickEventList("", "");
                break;
            default:
                break;
        }
    }

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

    public ShopInfo getShopInfo() {
        return orderDetails.getShopInfo();
    }

    public Status getStatus() {
        return orderDetails.status();
    }

    public String getCancelTime() {
        return requestCancelInfo.getRequestCancelNote();
    }

    public boolean shouldShowTimeForCancellation(){
        return requestCancelInfo != null && !requestCancelInfo.getIsRequestCancelAvail()
                && !TextUtils.isEmpty(requestCancelInfo.getRequestCancelMinTime());
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
        getView().displayLoadMore(true);

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
                                                    getView().showFailureMessage(e.getMessage());
                                                    getView().displayLoadMore(false);
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
        getView().displayLoadMore(true);

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
                    getView().displayLoadMore(false);
                    getView().showFailureMessage(e.getMessage());
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
                        getView().showSuccessMessage(cancelReplacementPojo.getMessageStatus());
                    else if (dataResponse.getErrorMessage() != null && !dataResponse.getErrorMessage().isEmpty())
                        getView().showFailureMessage((String) dataResponse.getErrorMessage().get(0));
                    else if ((dataResponse.getMessageStatus() != null && !dataResponse.getMessageStatus().isEmpty()))
                        getView().showSuccessMessage((String) dataResponse.getMessageStatus().get(0));
                    getView().displayLoadMore(false);
                    getView().finishOrderDetail();
                }
            }
        });

    }
}