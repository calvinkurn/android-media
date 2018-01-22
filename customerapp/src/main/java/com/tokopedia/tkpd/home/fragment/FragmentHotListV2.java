package com.tokopedia.tkpd.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.home.model.HotListViewModel;
import com.tokopedia.core.home.presenter.HotList;
import com.tokopedia.core.home.presenter.HotListImpl;
import com.tokopedia.core.home.presenter.HotListView;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.HotListAdapter;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 28/10/2015.
 * FragmentHotList is very suck class
 *
 * 1. swipe to refresh : display page one
 * 2. load more : display next page
 * 3. enable retry policy
 * 4. caching page one
 */
public class FragmentHotListV2 extends TkpdBaseV4Fragment implements HotListView {
    public static final String FRAGMENT_TAG = "FragmentHotListV2";
    
    private HotListAdapter adapter;
    private HotList hotList;
    @BindView(R.id.hot_product)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected String getScreenName() {
        return AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_HOTLIST;
    }

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotList = new HotListImpl(this);
        //hotList.onFetchDataAfterRotate(savedInstanceState);
        hotList.initHotListInstances(getActivity());
    }

    @Override
    public void initAdapter(@NonNull List<RecyclerViewItem> data) {
        adapter = new HotListAdapter(getActivity(), data);
        adapter.setHotList(hotList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_index_main, container, false);
        hotList.subscribe();
        unbinder = ButterKnife.bind(this, parentView);
        prepareView();
        setListener();
        return parentView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // currently retry not support
//        hotList.updateViewData(HotListViewModel.LOAD_MORE, isLoadMoreShow());
        hotList.updateViewData(HotListViewModel.SWIPE_, isSwipeShow());
        hotList.onSaveDataBeforeRotate(outState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        hotList.unSubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, FragmentHotListV2.class.getSimpleName() + " screen Rotation " + (isLandscape() ? "LANDSCAPE" : "PORTRAIT"));
        if(hotList.isAfterRotate()) {
            hotList.initDataAfterRotate();
        }else {
            hotList.initData();
        }
    }

    protected void setListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemPosition = 0;
                if(isLandscape() && layoutManager instanceof GridLayoutManager){
                    itemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else{
                    itemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (!isSwipeShow() && isLoadMoreShow()  && itemPosition == layoutManager.getItemCount() - 1) {//
                    hotList.loadMore();
                }
            }
        });
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setIsLoading(false);// disable load more if swipe to refresh proceed
                hotList.resetToPageOne();
            }
        });
//        hotList.setTimeOut();
        hotList.setRetryListener();
    }

    @Override
    public void initHolder() {
        
    }

    @Override
    public void initLinLayManager() {
        // , LinearLayoutManager.VERTICAL, false
        if(!isLandscape())
            layoutManager = new LinearLayoutManager(getActivity());
        else {
            layoutManager = new GridLayoutManager(getActivity(), COLUMN_SIZE);
            ((GridLayoutManager)layoutManager).setSpanSizeLookup(onSpanSizeLookup());
        }
    }

    // to determine size of grid columns
    GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                // column size default is one
                int headerColumnSize = 1,
                        footerColumnSize = 1,
                        regularColumnSize = 1;

                // check the orientation to determine landscape or portrait
//                switch (getResources().getConfiguration().orientation){
//                    case Configuration.ORIENTATION_LANDSCAPE:
//                        headerColumnSize = 2;
//                        regularColumnSize = DaggerFragmentProductFeed.LANDSCAPE_COLUMN;
//                        footerColumnSize = 2;
//                        break;
//                    case Configuration.ORIENTATION_PORTRAIT:
                headerColumnSize = ProductFeedHelper.PORTRAIT_COLUMN_HEADER;
                regularColumnSize = ProductFeedHelper.PORTRAIT_COLUMN;
                footerColumnSize = ProductFeedHelper.PORTRAIT_COLUMN_FOOTER;
//                        break;
//                }

                // set the value of footer, regular and header
                if (position == hotList.getDataSize()) {
                    // header column
                    return footerColumnSize;
                } else if (position % 5 == 0 && hotList.getItemDataType(position) == TkpdState.RecyclerViewItem.TYPE_LIST) {
                    // top ads span column
                    return headerColumnSize;
                } else {
                    // regular one column
                    return regularColumnSize;
                }
            }
        };
    }

    @Override
    public void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() !=null) {
            hotList.sendAppsFlyerData(getActivity());
            ScreenTracking.screen(getScreenName());
            TrackingUtils.sendMoEngageOpenHotListEvent();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void setPullEnabled(boolean isPullEnabled) {
        Log.d(TAG, FragmentHotListV2.class.getSimpleName() + (isPullEnabled ? " hidupkan" : " matikan") + " pull to refresh");
        swipeToRefresh.setEnabled(isPullEnabled);
    }

    @Override
    public void displayPull(boolean isShow) {
        Log.d(TAG, FragmentHotListV2.class.getSimpleName() + (isShow ? " tampilkan" : " hilangkan ") + " pull to refresh");
        swipeToRefresh.setRefreshing(isShow);
    }

    @Override
    public boolean isSwipeShow() {
        if(swipeToRefresh != null)
            return swipeToRefresh.isRefreshing();
        else
            return false;
    }

    @Override
    public void loadDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        Log.d(TAG, FragmentHotListV2.class.getSimpleName() + (isLoadMore ? " tampilkan" : " hilangkan ") + " load more");
        adapter.setIsLoading(isLoadMore);
    }

    @Override
    public void displayRetry(boolean isRetry) {
        Log.d(TAG, FragmentHotListV2.class.getSimpleName() + (isRetry ? " tampilkan" : " hilangkan ") + " isRetry");
        adapter.setIsRetry(isRetry);
    }

    @Override
    public boolean isLoadMoreShow() {

        if(isLandscape() && layoutManager instanceof GridLayoutManager){
            GridLayoutManager temp = (GridLayoutManager) layoutManager;
            return adapter.getItemViewType(temp.findLastCompletelyVisibleItemPosition())== TkpdState.RecyclerView.VIEW_LOADING;
        }else{
            LinearLayoutManager temp = (LinearLayoutManager) layoutManager;
            return adapter.getItemViewType(temp.findLastCompletelyVisibleItemPosition())== TkpdState.RecyclerView.VIEW_LOADING;
        }
    }

    @Override
    public void moveToOtherActivity(Bundle bundle) {
        Intent intent = BrowseProductRouter.getDefaultBrowseIntent(getContext());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void openHotlistActivity(String url) {
        getActivity().startActivity(
                BrowseProductRouter.getHotlistIntent(getActivity(), url)
        );
    }

    @Override
    public void openSearch(String url) {
        Uri uriData = Uri.parse(url);
        Bundle bundle = new Bundle();

        String departmentId = uriData.getQueryParameter("sc");
        String searchQuery = uriData.getQueryParameter("q");

        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
        bundle.putString(BrowseProductRouter.EXTRAS_SEARCH_TERM, searchQuery);

        Intent intent = BrowseProductRouter.getSearchProductIntent(getActivity());
        intent.putExtras(bundle);

        getActivity().startActivity(intent);
    }

    @Override
    public void openCategory(String categoryUrl) {
        URLParser urlParser = new URLParser(categoryUrl);
        if (urlParser.getParamKeyValueMap().size()>0) {
            CategoryActivity.moveTo(
                    getActivity(),
                   categoryUrl
            );
        } else {
            getActivity().startActivity(
                    BrowseProductRouter.getIntermediaryIntent(getActivity(), urlParser.getDepIDfromURI(getActivity()))
            );
        }
    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void displayTimeout() {
        CommonUtils.UniversalToast(getActivity(), getActivity().getString(R.string.msg_connection_timeout_toast));
    }

    @Override
    public void setRetry(boolean isRetry) {
        adapter.setIsRetry(isRetry);
    }

    @Override
    public void setOnRetryListenerRV() {
        adapter.setOnRetryListenerRV(new BaseRecyclerViewAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                Log.d(TAG, FragmentHotListV2.class.getSimpleName() + " retry clicked !!!");
                displayPull(true);
                hotList.fetchHotListData();
            }
        });
    }

    @Override
    public boolean isLandscape() {
        return getScreenRotation()==LANDSCAPE;
    }

    @Override
    public int getScreenRotation() {
        return getActivity().getResources().getConfiguration().orientation;
    }

    @Override
    public void startIntentActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    @Override
    public int getFragmentId() {
        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        if(type== DownloadService.HOTLIST && hotList!= null){
            hotList.ariseRetry();
        }
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String)data[0];
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        setPullEnabled(true);// enable pull to refresh
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String)data[0];
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        setPullEnabled(true);// enable pull to refresh
    }

    @Override
    public void setData(int type, Bundle data) {
        List<RecyclerViewItem> items = Parcels.unwrap(data.getParcelable(DownloadService.HOTLIST_DATA));
        boolean hasNext = data.getBoolean(DownloadService.HOTLIST_HAS_NEXT);
        int nextPage = data.getInt(DownloadService.HOTLIST_NEXT_PAGE);

        if(hotList!=null)
                hotList.setData(items, hasNext, nextPage);
    }

}
