package com.tokopedia.tkpd.home.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.home.model.HorizontalRecentViewList;
import com.tokopedia.seller.myproduct.ProductActivity;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.apiservices.etc.HomeService;
import com.tokopedia.core.network.apiservices.etc.apis.home.ProductFeedApi;
import com.tokopedia.core.network.apiservices.mojito.MojitoAuthService;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.GetListFaveShopId;
import com.tokopedia.core.network.entity.home.ProductFeedData3;
import com.tokopedia.core.network.entity.home.recentView.RecentView;
import com.tokopedia.core.network.entity.home.recentView.RecentViewData;
import com.tokopedia.core.network.entity.topads.TopAds;
import com.tokopedia.core.network.entity.topads.TopAdsResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractor;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.tkpd.home.model.ProductFeedTransformData;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * @author m.normansyah
 * @version 2
 *          modified at 26 Nov 2015, enable RxJava and Retrofit 2
 *          modified at 28 jan 2016, moved the url to ace.
 * @since 13/11/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ProductFeed2Impl implements ProductFeed, DiscoveryListener {
    public static final String ACE_URL = TkpdBaseURL.ACE_DOMAIN;
//    private List<RecyclerViewItem> data;

    public static final int[] shopIdDemo = new int[]{642687, 271947, 177783, 578674, 523451, 294558};
    private final MojitoAuthService mMojitoAuthService;
    GetListFaveShopId.ListFaveShopId listFaveShopId;

    private PagingHandler mPaging;
    Context mContext;

    ProductFeedView view;

    private CompositeSubscription _subscriptions = new CompositeSubscription();
    HomeService homeService;
    ProductFeedApi productFeedApi2;
    boolean isSwipeShow;
    private CacheHomeInteractorImpl cacheHome = new CacheHomeInteractorImpl();

    private List<ProductItem> currTopAdsItem;

    int index;
    private DiscoveryInteractorImpl discoveryInteractor;
    private int topAdsCounter = 1;

    public ProductFeed2Impl(ProductFeedView view) {
        this.view = view;
        mPaging = new PagingHandler();
        homeService = new HomeService();
        productFeedApi2 = RetrofitUtils.createRetrofit(ACE_URL).create(ProductFeedApi.class);
        mMojitoAuthService = new MojitoAuthService();
    }

    @Override
    public void initProductFeedInstances(Context context) {
        mContext = context;
        if (!isAfterRotation()) {
//            data = new ArrayList<>();
        }

        view.initGridLayoutManager();
        view.initItemDecoration();
//        view.initProductFeedRecyclerViewAdapter(data);
        view.initProductFeedRecyclerViewAdapter(new ArrayList<RecyclerViewItem>());

        discoveryInteractor = new DiscoveryInteractorImpl();
        discoveryInteractor.setDiscoveryListener(this);
    }

    @Override
    public void initProductFeedDatas() {
        getRecentProduct();
    }

    @Override
    public void fetchSavedsInstance(Bundle outState) {
        if (outState != null) {
            Log.d(TAG, "last page : " + mPaging.onCreate(outState));
            index = outState.getInt(POSITION_VIEW, defaultPositionView);
//            data = Parcels.unwrap(outState.getParcelable(MODEL_FLAG));
            isSwipeShow = outState.getBoolean(SWIPE_SHOW);
            listFaveShopId = Parcels.unwrap(outState.getParcelable(LIST_OF_SHOP_ID));
        }
    }

    @Override
    public void saveDataBeforeRotate(Bundle outState) {
        // pass paging handler the logic
        mPaging.onSavedInstanceState(outState);
        index = view.getLastPosition();
        outState.putInt(POSITION_VIEW, index);
        outState.putBoolean(SWIPE_SHOW, view.isRefresh());
//        outState.putParcelable(MODEL_FLAG, Parcels.wrap(data));
        outState.putParcelable(LIST_OF_SHOP_ID, Parcels.wrap(listFaveShopId));
    }

    @Override
    public void initAnalyticsHandler(Context context) {
        // init analytic handler
    }

    @Override
    public void subscribe() {
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        cacheHome = new CacheHomeInteractorImpl();
        discoveryInteractor.setCompositeSubscription(_subscriptions);
    }

    @Override
    public void unsubscribe() {
        RxUtils.unsubscribeIfNotNull(_subscriptions);
        cacheHome.unSubscribeObservable();
    }

    @Override
    public void getRecentProduct() {
        fecthRecentProductFromNetwork();
    }

    @Override
    public void getRecentProductFromCache() {
        cacheHome.getProductFeedCache(new CacheHomeInteractor.GetProductFeedCacheListener() {
            @Override
            public void onSuccess(ProductFeedTransformData productFeedTransformData) {

                List<RecyclerViewItem> result = new ArrayList<RecyclerViewItem>();
                List<RecentView> listProduct = new ArrayList<RecentView>();
                listProduct.addAll(productFeedTransformData.getHorizontalProductList().getRecentViewList());
                result.add(new HistoryProductListItem(listProduct));
                GetListFaveShopId getListFaveShopId = productFeedTransformData.getGetListFaveShopId();
                if (getListFaveShopId != null)
                    listFaveShopId = getListFaveShopId.getData();
                else {
                    listFaveShopId = new GetListFaveShopId.ListFaveShopId();
                    listFaveShopId.setShop_id_list(new ArrayList<String>());
                }
                if (productFeedTransformData.getListProductItems() != null)
                    result.addAll(productFeedTransformData.getListProductItems());

                view.addAll(result);

                if (view.getData() == null || view.getData().size() == 0) {
                    view.displayMainContent(false);
                    view.displayRetryFull();
                } else {
                    view.displayMainContent(true);
                    view.displayLoadMore(false);
                    view.displayRetry(true);
                }

                index = view.getLastPosition();
                view.displayLoadMore(false);
                view.loadDataChange();
                view.displayLoading(false);

                if (!view.isLoading())
                    view.displayPull(true);

                //mPaging.resetPage();
                fecthRecentProductFromNetwork();
            }

            @Override
            public void onError(Throwable e) {
                view.displayMainContent(true);
                view.displayLoadMore(false);
                view.displayRetry(false);
                fecthRecentProductFromNetwork();
            }
        });


    }

    private int getPagingIndex() {
        return mPaging != null ? mPaging.getPagingHandlerModel() != null
                ? mPaging.getPagingHandlerModel().getStartIndex() : 0 : 0;
    }

    @Override
    public void getProductFeed() {
        //[START] This is new ace.tokopedia.com - changed flow
        if (listFaveShopId != null) {
            String shopIds = "";
            for (int i = 0; i < listFaveShopId.getShop_id_list().size(); i++) {
                if (i < (listFaveShopId.getShop_id_list().size() - 1))
                    shopIds += listFaveShopId.getShop_id_list().get(i) + ",";
                else
                    shopIds += listFaveShopId.getShop_id_list().get(i);
            }
            _subscriptions.add(
                    productFeedApi2
                            .getProductFeed3(
//                                    listFaveShopId.getShop_id_list().get(mPaging.getPage())
                                    shopIds,
                                    "12",
                                    "" + getPagingIndex(),
                                    "android",
                                    "10",
                                    SessionHandler.getLoginID(mContext)
                            )
//                        .getProductFeed(shopIdDemo[(i < 0 ? 0 : i)] + "")
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .subscribe(
                                    new Subscriber<ProductFeedData3>() {
                                        @Override
                                        public void onCompleted() {
                                            Log.i(TAG, messageTag + "onCompleted : ");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e(TAG, messageTag + "onError : " + e.getLocalizedMessage());
                                            Toast.makeText(mContext, mContext.getString(R.string.message_verification_timeout), Toast.LENGTH_SHORT).show();
                                            view.displayPull(false);
                                            view.displayRetry(true);
                                            view.displayLoadMore(false);
                                            view.loadDataChange();
                                        }

                                        @Override
                                        public void onNext(ProductFeedData3 productFeedData3) {
                                            getTopads(mPaging.getPage());
                                            PagingHandler.PagingHandlerModel p = new PagingHandler.PagingHandlerModel();
                                            p.setUriNext(productFeedData3.getData().getPaging().getUriNext());
                                            p.setStartIndex(productFeedData3.getData().getPaging().getStartIndex());
                                            mPaging.setHasNext(PagingHandler.CheckHasNext(p));
                                            mPaging.setPagingHandlerModel(p);

                                            view.addNextPage(new ArrayList<RecyclerViewItem>(ProductFeedData3.toProductItems(productFeedData3.getData().getProducts())));
                                            setDataLoadMore();

                                        }
                                    }

                            )
            );
            //[END] This is new ace.tokopedia.com
        }
    }

    @Override
    public void setData() {
        view.finishLoading();
        if (isSwipeShow) {
            view.displayPull(true);
        } else {
            view.displayPull(false);
        }
        // if paging is false then disable load more
        // listFaveShopId != null && mPaging.getPage()+1 < listFaveShopId.getShop_id_list().size()
        if (mPaging.CheckNextPage())//
            view.displayLoadMore(true);
        else
            view.displayLoadMore(false);

        Log.d(TAG, messageTag + "ukuran list : " + view.getAdapter().getData().size());

        view.loadDataChange();
    }

    @Override
    public void setDataLoadMore() {

        view.finishLoading();
        if (isSwipeShow) {
            view.displayPull(true);
        } else {
            view.displayPull(false);
        }
        // if paging is false then disable load more
        // mPaging.getPage()+1 < listFaveShopId.getShop_id_list().size()
        if (mPaging.CheckNextPage())//
            view.displayLoadMore(true);
        else
            view.displayLoadMore(false);

        view.loadDataChange();
    }

    @Override
    public void refreshData() {
        mPaging.resetPage();
        if(currTopAdsItem != null) {
            currTopAdsItem.clear();
        }
        topAdsCounter = 1;
        getRecentProduct();
    }

    @Override
    public void loadMore() {
        if (mPaging.CheckNextPage()) {
            mPaging.nextPage();
            getProductFeed();
        }
    }

    @Override
    public void moveToAddProduct(Context context) {
        if (context != null) {
            //[START] revert back to old add product cause it already at production
            Intent intent = new Intent(context, ProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
            intent.putExtras(bundle);
            context.startActivity(intent);
            //[END] revert back to old add product cause it already at production
        }
    }

    /**
     * 23-10-2015,
     * In short, the check is way faster. You should use the check because:
     * Exceptions are EXPENSIVE! A stack trace must be created (if used, eg logged etc) and special flow control handled
     * Exceptions should not be used for flow control - Exceptions are for the "exceptional"
     * Exceptions are the code's way of saying "I can't handle this situation and I'm giving up... you deal with it!", but here you can handle it... so handle it
     */
    @Override
    public void setLocalyticFlow(String screenName) {
        try {
            ScreenTracking.screenLoca(screenName);
            ScreenTracking.eventLoca(screenName);
        }catch (Exception e){
            CommonUtils.dumper(TAG+" error connecting to GCM Service");
            TrackingUtils.eventLogAnalytics(ParentIndexHome.class.getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void sendAppsFlyerData() {
        ScreenTracking.sendAFGeneralScreenEvent(Jordan.AF_SCREEN_PRODUCT_FEED);
    }

    @Override
    public boolean isAfterRotation() {
        return view.isAfterRotate();
    }


    @Override
    public void saveCache(int type, JSONObject jsonToSave) throws JSONException {
        throw new RuntimeException("don't use this method, remove it!!");
    }

    @Override
    public List<RecyclerViewItem> parseJSON(int type, JSONObject jsonToParse) throws JSONException {
        throw new RuntimeException("don't use this method, remove it!!");
    }


    @Override
    public List<RecyclerViewItem> getData() {
        throw new RuntimeException();
    }

    private void fetchRecentProductFromCache() {

    }

    @Override
    public void fecthRecentProductFromNetwork() {
        Log.d(TAG, "fecthRecentProductFromNetwork");
        view.resetPaging();
        String userId = SessionHandler.getLoginID(mContext);
        _subscriptions.add(
                mMojitoAuthService.getApi().getRecentViews(userId)
                        .map(new Func1<Response<RecentViewData>, ProductFeedTransformData>() {
                            @Override
                            public ProductFeedTransformData call(Response<RecentViewData> response) {
                                ProductFeedTransformData pair = null;
                                Log.e(TAG, messageTag + response.body());
                                FileUtils.writeStringAsFileExt(mContext, response.body() + "", "using_response.txt");
                                if (response.isSuccessful()) {
                                    NetworkCalculator newProducFeed = new NetworkCalculator(NetworkConfig.POST, mContext, DeveloperOptions.getWsV4Domain(mContext) + ProductFeedApi.GET_LIST_FAVE_SHOP_ID)
                                            .setIdentity()
                                            .addParam(ProductFeedApi.LIMIT, "")
                                            .compileAllParam()
                                            .finish();

                                    pair = new ProductFeedTransformData(newProducFeed.getHeader(),
                                            newProducFeed.getContent(),
                                            getHorizontalRecentViewList(response.body()));
                                } else {
                                    new RetrofitUtils.DefaultErrorHandler(response.code());
                                }
                                return pair;
                            }
                        })
                        .flatMap(new Func1<ProductFeedTransformData, Observable<ProductFeedTransformData>>() {
                            @Override
                            public Observable<ProductFeedTransformData> call(ProductFeedTransformData productFeedTransformData) {

                                Map<String, String> params = new HashMap<>();
                                params.put(ProductFeedApi.LIMIT, productFeedTransformData.getContent().get(ProductFeedApi.LIMIT));

                                Observable<GetListFaveShopId> getList = homeService.getApi().getListFaveShopId(
                                        AuthUtil.generateParams(mContext, params)
                                );
                                return Observable.zip(Observable.just(productFeedTransformData), getList, new Func2<ProductFeedTransformData, GetListFaveShopId, ProductFeedTransformData>() {
                                    @Override
                                    public ProductFeedTransformData call(ProductFeedTransformData productFeedTransformData, GetListFaveShopId getListFaveShopId) {
                                        ArrayList<String> shop_id_list = getListFaveShopId.getData().getShop_id_list();
                                        if (shop_id_list.size() > 0) {
                                            String shopIds = "";
                                            for (int i = 0; i < shop_id_list.size(); i++) {
                                                if (i < (shop_id_list.size() - 1))
                                                    shopIds += shop_id_list.get(i) + ",";
                                                else
                                                    shopIds += shop_id_list.get(i);
                                            }
                                            productFeedTransformData.setShopId(shopIds);
                                            productFeedTransformData.setGetListFaveShopId(getListFaveShopId);
                                        } else {
                                            // get list fave shop id is null
                                        }
                                        return productFeedTransformData;
                                    }
                                });
                            }
                        })
                        .flatMap(new Func1<ProductFeedTransformData, Observable<ProductFeedTransformData>>() {
                            @Override
                            public Observable<ProductFeedTransformData> call(ProductFeedTransformData pair) {

                                if (pair.getGetListFaveShopId() != null) {

                                    final Observable<ProductFeedData3> productItems = productFeedApi2
                                            .getProductFeed3(
                                                    pair.getShopId(),
                                                    "12",
                                                    "0",
                                                    "android",
                                                    "10",
                                                    SessionHandler.getLoginID(mContext)
                                            );

                                    return Observable.zip(Observable.just(pair), productItems,
                                            new Func2<ProductFeedTransformData, ProductFeedData3, ProductFeedTransformData>() {
                                                @Override
                                                public ProductFeedTransformData call(ProductFeedTransformData pair, ProductFeedData3 productFeedData3) {
                                                    PagingHandler.PagingHandlerModel pager = new PagingHandler.PagingHandlerModel();
                                                    pair.setListProductItems(ProductFeedData3.toProductItems(productFeedData3.getData().getProducts()));
                                                    Log.d(TAG, messageTag + " start index - " + productFeedData3.getData().getPaging().getStartIndex());
                                                    if (productFeedData3.getData().getPaging().getStartIndex() <= 0) {
                                                        pager.setUriNext("0");
                                                        pair.setPagingHandlerModel(pager);
                                                    } else {
                                                        pager.setUriNext(productFeedData3.getData().getPaging().getUriNext());
                                                        pager.setUriPrevious(productFeedData3.getData().getPaging().getUriPrevious());
                                                        pager.setStartIndex(productFeedData3.getData().getPaging().getStartIndex());
                                                        pair.setPagingHandlerModel(pager);
                                                    }
                                                    return pair;
                                                }
                                            });


                                } else {
                                    //[START] return page without next
                                    PagingHandler.PagingHandlerModel pager = new PagingHandler.PagingHandlerModel();
                                    pager.setUriNext("0");
                                    pair.setPagingHandlerModel(pager);
                                    //[START] return page without next

                                    return Observable.just(pair);
                                }
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<ProductFeedTransformData>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, messageTag + "onCompleted : ");
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e != null && e.getMessage() != null && RetrofitUtils.isSessionInvalid(e.getMessage())) {
                                    NetworkHandler.forceLogout(mContext);
                                } else {
                                    Log.e(TAG, messageTag + "onError : " + e.getLocalizedMessage());
                                    Toast.makeText(mContext, mContext.getString(R.string.message_verification_timeout), Toast.LENGTH_SHORT).show();

                                    if (view.getData() == null || view.getData().size() == 0) {
                                        view.displayMainContent(false);
                                        view.displayRetryFull();
                                    } else {
                                        view.displayMainContent(true);
                                        view.displayLoadMore(false);
                                        view.displayRetry(true);
                                    }
                                    view.displayLoading(false);
                                    view.displayPull(false);
                                    view.loadDataChange();
                                }
                                view.displayPull(false);
                            }

                            @Override
                            public void onNext(ProductFeedTransformData productFeedTransformData) {
                                final List<RecyclerViewItem> result = new ArrayList<RecyclerViewItem>();
//                                FileUtils.writeStringAsFileExt(mContext, productFeedTransformData.getHorizontalProductList() + "", "check_horizontal.txt");
                                // set history product
                                List<RecentView> listProduct = productFeedTransformData.getHorizontalProductList().getRecentViewList();
                                result.add(new HistoryProductListItem(listProduct));
                                // set product feed real data
                                List<ProductItem> listProductItems = productFeedTransformData.getListProductItems();
                                if (listProductItems != null) {
                                    result.addAll(listProductItems);
                                } else {
                                    result.addAll(new ArrayList<ProductItem>());
                                }

                                Log.i(TAG, messageTag + " fecthRecentProductFromNetwork : " +
                                        (listProductItems != null ? listProductItems.size() : "-1 "));

                                Log.i("OrangKeren", "From server: " + result.toString());

                                cacheHome.setProductFeedCache(productFeedTransformData);

                                GetListFaveShopId getListFaveShopId = productFeedTransformData.getGetListFaveShopId();
                                if (getListFaveShopId != null)
                                    listFaveShopId = getListFaveShopId.getData();
                                else {
                                    listFaveShopId = new GetListFaveShopId.ListFaveShopId();
                                    listFaveShopId.setShop_id_list(new ArrayList<String>());
                                }

                                //[START] paging is moved to getListFaveShopid
                                mPaging.setHasNext(PagingHandler.CheckHasNext(productFeedTransformData.getPagingHandlerModel()));
                                mPaging.setPagingHandlerModel(productFeedTransformData.getPagingHandlerModel());
                                //[END] paging is moved to getListFaveShopid

                                index = 0; // view.getLastPosition();

                                view.hideRetryFull();
                                view.displayRetry(false);
                                view.displayPull(false);

                                view.addAll(result);

                                setData();
                                if (listProductItems != null) {
                                    getTopads(view.getTopAdsPagging());
                                }
                            }
                        })
        );
    }

    @Override
    public boolean isLoadedFirstPage() {
        return mPaging.getPage() >= 1;
    }

    @NonNull
    private HorizontalRecentViewList getHorizontalRecentViewList(RecentViewData aRecentViewData) {
        return new HorizontalRecentViewList(aRecentViewData.getData().getRecentView());
    }

    private void getTopads(int page) {
        Log.i("TOPADS", "PAGE BEFORE LOAD: " + page);
        NetworkParam.TopAds topAds = new NetworkParam.TopAds();
        topAds.page = (topAdsCounter * 2) - 1;
        topAds.depId = "0";
        topAds.src = TopAdsApi.SRC_PRODUCT_FEED;
        if (currTopAdsItem == null || currTopAdsItem.isEmpty()) {
            HashMap<String, String> topAdsParam = NetworkParam.generateTopAds(view.getMainContext(), topAds);
            discoveryInteractor.getTopAds(topAdsParam);
//            fetchTopAds(page);
        } else {
            int counter = 2;
            List<ProductItem> passProduct = new ArrayList<>();
            while(counter != 0 && !currTopAdsItem.isEmpty()) {
                passProduct.add(currTopAdsItem.remove(0));
                counter --;
            }
            view.addTopAds(passProduct, page);
        }
    }

    @Override
    public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

    }

    @Override
    public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
        Log.e(TAG, "onFailed " + data.toString());
//        if (((ErrorContainer)data.getModel2()).body() != null && ((ErrorContainer)data.getModel2()).body().getMessage()
//                != null && RetrofitUtils.isSessionInvalid(((ErrorContainer)data.getModel2()).body().getMessage())) {
//            NetworkHandler.forceLogout(mContext);
//        } else {
//            Log.e(TAG, messageTag + "onError : " + ((ErrorContainer)data.getModel2()).body().getLocalizedMessage());
//            Toast.makeText(mContext, mContext.getString(R.string.message_verification_timeout), Toast.LENGTH_SHORT).show();
//
//            if (view.getData() == null || view.getData().size() == 0) {
//                view.displayMainContent(false);
//                view.displayRetryFull();
//            } else {
//                view.displayMainContent(true);
//                view.displayLoadMore(false);
//                view.displayRetry(true);
//            }
//            view.displayLoading(false);
//            view.displayPull(false);
//            view.loadDataChange();
//        }
    }

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {

        switch (type) {
            case DiscoveryListener.TOPADS:
                Log.i(TAG, "fetch top ads " + data.getModel1());
                topAdsCounter ++;
                ObjContainer model2 = data.getModel2();
                if (model2 instanceof TopAdsResponse.TopAdsContainer) {

                    TopAdsResponse topAdsResponse = (TopAdsResponse) model2.body();
                    int page = ((TopAdsResponse.TopAdsContainer) model2).page;

                    currTopAdsItem = new ArrayList<>();
                    List<TopAdsResponse.Data> topAdsData = topAdsResponse.data;
                    for (int i = 0; i < topAdsData.size(); i++) {
                        TopAdsResponse.Data dataTopAds = topAdsData.get(i);
                        ProductItem topads = ProductFeed2Impl.convertToProductItem(dataTopAds);
                        topads.setTopAds(TopAds.from(dataTopAds));
                        currTopAdsItem.add(topads);
                    }

                    List<ProductItem> passProduct = new ArrayList<>();

                    int counter = 2;
                    while(counter != 0 && !currTopAdsItem.isEmpty()) {
                        passProduct.add(currTopAdsItem.remove(0));
                        counter --;
                    }

                    view.addTopAds(passProduct, mPaging.getPage());

                }
                break;
        }
    }

    public static ProductItem convertToProductItem(TopAdsResponse.Data data) {
        ProductItem product = new ProductItem();
        product.setId(data.product.id);
        product.setPrice(data.product.priceFormat);
        product.setName(data.product.name);
        product.setShopId(Integer.parseInt(data.shop.id));
        product.setImgUri(data.product.image.mUrl);
        product.setShop(data.shop.name);
        product.setIsGold(data.shop.goldShop ? "1" : "0");
        product.setLuckyShop(data.shop.luckyShop);
        product.setWholesale(data.product.wholesalePrice.size() > 0 ? "1" : "0");
        product.setPreorder((data.product.preorder) ? "1" : "0");
        product.setIsTopAds(true);
        product.setShopLocation(data.shop.location);
        product.setBadges(data.shop.badges);
        product.setLabels(data.product.labels);
        TopAds ads = new TopAds();
        ads.setId(data.id);
        ads.setAdRefKey(data.adRefKey);
        ads.setProductClickUrl(data.productClickUrl);
        ads.setRedirect(data.redirect);
        ads.setShopClickUrl(data.shopClickUrl);
        ads.setStickerId(data.stickerId);
        ads.setStickerImage(data.stickerId);
        product.setTopAds(ads);
        return product;
    }

}
