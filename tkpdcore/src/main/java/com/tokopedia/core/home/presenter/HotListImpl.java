package com.tokopedia.core.home.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.home.model.HotListModel;
import com.tokopedia.core.home.model.HotListViewModel;
import com.tokopedia.core.network.apiservices.search.HotListService;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by m.normansyah on 28/10/2015.
 * modified by m.normansyah caching is worked later
 * modified by m.normansyah move hotlist from download service
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class HotListImpl implements HotList {
    private HotListView hotListView;
    private HotListViewModel hotListViewModel;

    private List<RecyclerViewItem> data;
    private PagingHandler mPaging;
    private URLParser urlParser;

    private int index;
    private Context mContext;
    private CompositeSubscription subscription = new CompositeSubscription();

    public HotListImpl(HotListView hotListView) {
        Log.i(TAG, messageTAG + "HotListImpl(HotListView hotListView)  ");
        this.hotListView = hotListView;
        mPaging = new PagingHandler();
    }

    @Override
    public void initHotListInstances(Context context) {
        mContext = context;
        hotListView.initHolder();
        if (!isAfterRotate()) {
            data = new ArrayList<RecyclerViewItem>();
            hotListViewModel = new HotListViewModel();
        }
        hotListView.initAdapter(data);
        hotListView.initLinLayManager();
    }

    @Override
    public void setLocalyticFlow(Context context) {
        if (context != null)
            CommonUtils.dumper("LocalTag : hot list");
        String screenName = context.getString(R.string.home_hot_list);
        ScreenTracking.screenLoca(screenName);
    }

    @Override
    public void sendAppsFlyerData(Context context) {
        ScreenTracking.sendAFGeneralScreenEvent(Jordan.AF_SCREEN_HOME_HOTLIST);
    }

    @Override
    public void loadMore() {
        if (mPaging.CheckNextPage()) {
            mPaging.nextPage();
            fetchHotListData();
        }
    }

    @Override
    public void initData() {
        hotListView.displayLoadMore(true);
        hotListView.displayPull(true);
        populateDataFromCache();

        // if cache lifetime is less than ooe day and page 1
        // then get data from cache
        // only for page 1, nothing more
//        String temp  = cache.getString(HOT_LIST_PAGE_1);
//        if(temp!=null){
//            try {
//                if(isCacheOneDay()) {
//                    JSONObject result = new JSONObject(temp);
//                    mPaging.setNewParameter(result);
//                    // if paging is false then disable load more
//                    if(mPaging.CheckNextPage())
//                        hotListView.displayLoadMore(true);
//                    else
//                        hotListView.displayLoadMore(false);
//                    List<RecyclerViewItem> items = parseJSON(result);
//                    data.addAll(items);
//                    hotListView.loadDataChange();
//                    hotListView.displayPull(false);// hide pull to refresh
//                }else{
//                    fetchHotListData();// fetch data from internet
//                }
//            }catch (JSONException e){
//                Log.d(TAG, HotList.class.getSimpleName()+" error page 1 cache parsing : "+e.getLocalizedMessage());
//                // if failed parse then get from the internet
//                fetchHotListData();// fetch data from internet
//            }
//        }else{
        //  fetchHotListData();// fetch data from internet
//        }
    }

    private void populateDataFromCache() {
        subscription.add(Observable.just(TkpdState.CacheKeys.HOTLIST)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String key) {
                        GlobalCacheManager cacheManager = new GlobalCacheManager();
                        return cacheManager.getValueString(key);
                    }
                })
                .map(new Func1<String, Bundle>() {
                    @Override
                    public Bundle call(String s) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        PagingHandler mPaging = new PagingHandler();
                        mPaging.setNewParameter(jsonObject);

                        boolean hasNext = mPaging.CheckNextPage();
                        mPaging.nextPage();
                        int nextPage = mPaging.getPage();

                        // add to real container data
                        List<RecyclerViewItem> items = null;
                        try {
                            assert jsonObject != null;
                            JSONArray listHot = new JSONArray(jsonObject.getString(LIST_KEY));
                            java.lang.reflect.Type listType = new TypeToken<List<HotListModel>>() {
                            }.getType();
                            items = new GsonBuilder().create().fromJson(listHot.toString(), listType);
                        } catch (JSONException json) {
                            Log.e(TAG, HotListImpl.class.getSimpleName() + " is error : " + json.getLocalizedMessage());
                        }
                        Bundle result = new Bundle();
                        result.putParcelable(DownloadService.HOTLIST_DATA, Parcels.wrap(items));
                        result.putBoolean(DownloadService.HOTLIST_HAS_NEXT, hasNext);
                        result.putInt(DownloadService.HOTLIST_NEXT_PAGE, nextPage);
                        result.putBoolean(DownloadService.RETRY_FLAG, false);
                        return result;
                    }
                })
                .subscribe(new rx.Subscriber<Bundle>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "CACHE ACIL, gagal ambil cache cil");
                        e.printStackTrace();
                        fetchHotListData();
                    }

                    @Override
                    public void onNext(Bundle bundle) {
                        Log.d(TAG, "CACHE ACIL, ini dari cache cil");
                        hotListView.setData(DownloadService.HOTLIST, bundle);
                        fetchHotListData();
                        hotListView.displayPull(true);
                    }
                }));

    }

    @Override
    public void fetchHotListData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put(QUERY_KEY, "");
        params.put(PAGE_KEY, mPaging.getPage() + "");
        params.put(PER_PAGE_KEY, PER_PAGE_VALUE + "");
        AuthService service = new HotListService();
        subscription.add(
                ((HotListService) service).getApi().getHotList(AuthUtil.generateParams(mContext, params))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber(DownloadServiceConstant.HOTLIST))
        );

        //[START] remove fetching data from internet to local
//        if(mContext instanceof ParentIndexHome)
//            ((ParentIndexHome)mContext).getHotList(mPaging.getPage(), PER_PAGE_VALUE);
//        else if(mContext instanceof DeepLinkViewer)
//            ((DeepLinkActivity)mContext).getHotList(mPaging.getPage(), PER_PAGE_VALUE);
    }

    @Override
    public void setData(List<RecyclerViewItem> items, boolean hasNext, int nextPage) {
        Log.d(TAG, HotListImpl.class.getSimpleName() + " hasNext " + hasNext + " setData !!!");

        // set paging
//        mPaging.setPage(nextPage);
        mPaging.setHasNext(hasNext);

        // kondisi ketika refresh
        if (hotListView.isSwipeShow() && !hotListView.isLoadMoreShow()) {
            data.clear();
            hotListView.displayPull(false);// hide pull to refresh
        }

        data.addAll(items);
        // cache page one
//        cachePageOne(Response);

        // if paging is false then disable load more
        if (mPaging.CheckNextPage())
            hotListView.displayLoadMore(true);
        else
            hotListView.displayLoadMore(false);

        if (hotListView.isSwipeShow()) {
            hotListView.displayPull(false);// hide pull to refresh
        }

        hotListView.loadDataChange();
    }

    @Override
    public void onMessageError(String text) {
        throw new RuntimeException("no need for impl");
    }

    @Override
    public void onNetworkError(String text) {
        throw new RuntimeException("no need for impl");
    }

    @Override
    public void ariseRetry() {
        hotListView.displayLoadMore(false);
        hotListView.displayRetry(true);
        if (data.size() == 0) {
            hotListView.setPullEnabled(false);
        } else {
            hotListView.displayTimeout();
            hotListView.setPullEnabled(true);
            if (hotListView.isSwipeShow())
                hotListView.setRetry(false);
        }
        if (hotListView.isSwipeShow())
            hotListView.displayPull(false);
        hotListView.loadDataChange();
    }

    @Override
    public List<RecyclerViewItem> parseJSON(JSONObject Result) {
        throw new RuntimeException("no need for impl");
    }

    @Override
    public void resetToPageOne() {
//        if(retryHandler!=null)
//            retryHandler.disableRetryView();
        mPaging.resetPage();
        fetchHotListData();
    }

    @Override
    public void onSaveDataBeforeRotate(Bundle outState) {
        // pass paging handler the logic
        mPaging.onSavedInstanceState(outState);
        outState.putInt(POSITION_VIEW, index);
        outState.putParcelable(VIEW_, Parcels.wrap(hotListViewModel));
        outState.putParcelable(DATA, Parcels.wrap(data));
    }

    @Override
    public void onFetchDataAfterRotate(Bundle outState) {
        if (outState != null) {
            mPaging.onCreate(outState);
            outState.getInt(POSITION_VIEW, defaultPositionView);
            data = Parcels.unwrap(outState.getParcelable(DATA));
            hotListViewModel = Parcels.unwrap(outState.getParcelable(VIEW_));
        }
    }

    @Override
    public boolean isAfterRotate() {
        return data != null && data.size() > 0;
    }

    @Override
    public void moveToOtherActivity(RecyclerViewItem data) {
        HotListModel temp = null;
        if (data instanceof HotListModel)
            temp = (HotListModel) data;
        if (temp == null) {
            throw new RuntimeException("invalid passing data !!! at " + HotListImpl.class.getSimpleName());
        }
        urlParser = new URLParser(temp.getHotListProductUrl());
        Log.d(TAG, "urlParser type " + urlParser.getType());
        switch (urlParser.getType()) {
            case HOT_KEY:
                Bundle bundle = new Bundle();
                bundle.putString(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS, urlParser.getHotAlias());
                bundle.putString(BrowseProductRouter.EXTRA_SOURCE, BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT);
                hotListView.moveToOtherActivity(bundle);
                break;
            case CATALOG_KEY:
                hotListView.moveToOtherActivity(
                        DetailProductRouter.getCatalogDetailActivity(mContext, urlParser.getHotAlias()));
                break;
            default:
                bundle = new Bundle();
                bundle.putString(BrowseProductRouter.DEPARTMENT_ID,
                        urlParser.getDepIDfromURI(mContext));

                bundle.putInt(BrowseProductRouter.FRAGMENT_ID,
                        BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);

                bundle.putString(BrowseProductRouter.EXTRA_TITLE, urlParser.getDeepestCategoryName());
                bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_HOTLIST);
                bundle.putString(BrowseProductRouter.EXTRA_SOURCE, BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY);
                hotListView.moveToOtherActivity(bundle);

                break;
        }
    }

    @Override
    public void setRetryListener() {
        hotListView.setOnRetryListenerRV();
    }

    @Override
    public void updateViewData(int type, Object... data) {
        switch (type) {
            case HotListViewModel.LOAD_MORE:
                hotListViewModel.setIsLoadMoreShow((boolean) data[0]);
                break;
            case HotListViewModel.SWIPE_:
                hotListViewModel.setIsSwipeShow((boolean) data[0]);
                break;
            case HotListViewModel.RETRY_:
                throw new RuntimeException("this time not implement yet");
        }
    }

    @Override
    public void initDataAfterRotate() {
        Log.d(TAG, HotListImpl.class.getSimpleName() + " : hasNext " + mPaging.CheckNextPage());
        if (mPaging.CheckNextPage())
            hotListView.displayLoadMore(true);
        else
            hotListView.displayLoadMore(false);
        hotListView.loadDataChange();
//        if(hotListViewModel.isLoadMoreShow()){
//            loadMore();
//        }
        hotListView.displayPull(hotListViewModel.isSwipeShow());
        if (hotListViewModel.isSwipeShow()) {
            resetToPageOne();
        }
        // currently retry not support
    }

    @Override
    public int getDataSize() {
        return data.size();
    }

    @Override
    public int getItemDataType(int position) {
        return data.get(position).getType();
    }

    @Override
    public void subscribe() {
        RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(subscription);
    }

    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        int type;
        ErrorListener listener;

        public Subscriber(int type) {
            this.type = type;
            listener = new ErrorListener(type);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, messageTAG + e.getLocalizedMessage());
            if (e.getLocalizedMessage() != null &&
                    e.getLocalizedMessage().contains("Unable to resolve host")) {
                listener.noConnection();
            }
        }

        @Override
        public void onNext(Response<TkpdResponse> responseData) {
            if (responseData.isSuccessful()) {
                TkpdResponse response = responseData.body();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.getStringData());
                } catch (JSONException je) {
                    Log.e(TAG, messageTAG + je.getLocalizedMessage());
                }
                if (!response.isError()) {
                    Log.d(TAG, HotListImpl.class.getSimpleName() + " " + jsonObject.toString());
                    // set paging
                    PagingHandler mPaging = new PagingHandler();
                    mPaging.setNewParameter(jsonObject);
                    boolean hasNext = mPaging.CheckNextPage();
                    mPaging.nextPage();
                    int nextPage = mPaging.getPage();

                    // add to real container data
                    List<RecyclerViewItem> items = (List<RecyclerViewItem>) parseJSON(type, jsonObject);
                    cache(jsonObject, nextPage - 1);

                    Bundle result = new Bundle();
                    result.putParcelable(DownloadService.HOTLIST_DATA, Parcels.wrap(items));
                    result.putBoolean(DownloadService.HOTLIST_HAS_NEXT, hasNext);
                    result.putInt(DownloadService.HOTLIST_NEXT_PAGE, nextPage);
                    result.putBoolean(DownloadService.RETRY_FLAG, false);

                    //[START] set result here
                    hotListView.setData(DownloadService.HOTLIST, result);


                    if (nextPage - 1 == 1) {
                        GlobalCacheManager cacheManager = new GlobalCacheManager();
                        cacheManager.setKey(TkpdState.CacheKeys.HOTLIST);
                        cacheManager.setValue(response.getStringData());
                        cacheManager.setCacheDuration(10800);
                        cacheManager.store();
                    }
                } else {
                    onMessageError(response.getErrorMessages());
                }
            } else {
                new ErrorHandler(listener, responseData.code());
            }
        }

        /**
         * No connection still not known
         */
        public class ErrorListener implements com.tokopedia.core.network.retrofit.response.ErrorListener {
            int errorCode;
            String error;

            public ErrorListener(int errorCode) {
                this.errorCode = errorCode;
                switch (errorCode) {
                    case ResponseStatus.SC_REQUEST_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_GATEWAY_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                        error = "SERVER ERROR";
                        break;
                    case ResponseStatus.SC_FORBIDDEN:
                        error = "FORBIDDEN ACCESS";
                        break;
                    case ResponseStatus.SC_BAD_GATEWAY:
                        error = "INVALID INPUT";
                        break;
                    case ResponseStatus.SC_BAD_REQUEST:
                        error = "INVALID INPUT";
                        break;
                    default:
                        error = mContext.getString(R.string.default_request_error_unknown);
                        break;
                }
            }


            public void onResponse() {
                if (error != null) {
                    Bundle resultData = new Bundle();
                    HotListImpl.this.hotListView.onNetworkError(DownloadServiceConstant.HOTLIST, error);
                    hotListView.ariseRetry(DownloadServiceConstant.HOTLIST, (Object[]) null);
                }
            }

            public void noConnection() {
                error = DownloadServiceConstant.noNetworkConnection;
                onResponse();
            }

            @Override
            public void onUnknown() {
                onResponse();
            }

            @Override
            public void onTimeout() {
                error = DownloadServiceConstant.noNetworkConnection;
                onResponse();
            }

            @Override
            public void onServerError() {
                onResponse();
            }

            @Override
            public void onBadRequest() {
                onResponse();
            }

            @Override
            public void onForbidden() {
                onResponse();
            }
        }

        public void onMessageError(List<String> MessageError) {
            Log.d(TAG, HotList.class.getSimpleName() + " onMessageError " + MessageError.toString());
            if (MessageError == null || !(MessageError.size() > 0))
                return;

            HotListImpl.this.hotListView.onMessageError(DownloadServiceConstant.HOTLIST, MessageError.toString());
        }

        private Object parseJSON(int type, JSONObject response) {
            switch (type) {
                case DownloadServiceConstant.HOTLIST:
                    List<RecyclerViewItem> temps = new ArrayList<>();
                    try {
                        JSONArray listHot = new JSONArray(response.getString(LIST_KEY));
                        java.lang.reflect.Type listType = new TypeToken<List<HotListModel>>() {
                        }.getType();
                        temps = new GsonBuilder().create().fromJson(listHot.toString(), listType);
                    } catch (JSONException json) {
                        Log.e(TAG, HotListImpl.class.getSimpleName() + " is error : " + json.getLocalizedMessage());
                    }
                    return temps;
            }
            return null;
        }

        private void cache(JSONObject jsonObject, Object... data) {
            switch (type) {
                case DownloadServiceConstant.HOTLIST:
                    // only cache first page
                    int page = (int) data[0];
                    if (page == 1) {
                        LocalCacheHandler cache = new LocalCacheHandler(mContext, CACHE_KEY);
                        cache.putString(HOT_LIST_PAGE_1, jsonObject.toString());
                        cache.putLong(EXPIRY, System.currentTimeMillis() / 1000);
                        cache.applyEditor();
                    }
                    break;
            }
        }
    }
}
