package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.fragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core2.R;
import com.tokopedia.core.customView.ReputationRecyclerView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.adapter.ProductReviewAdapter;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.listener.ProductReviewView;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review.HelpfulReviewList;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.AdvanceReview;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductModel;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter.ProductReviewPresenter;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter.ProductReviewPresenterImpl;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.util.RatingStatsUtils;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SlideOffViewHandler;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Steven on 15/12/16.
 */
public class ProductReviewFragment extends Fragment implements ProductReviewView {

    private boolean isRequest;
    private String NAV;
    private String productID;
    private View rootView;

    private ProductReviewAdapter adapter;
    private PagingHandler page;
    private RatingStatsUtils ratingStatsUtils;
    private RefreshHandler refresh;
    private SlideOffViewHandler slideOffViewHandler;

    private List<RecyclerViewItem> recyclerList;
    private GridLayoutManager layoutManager;
    private ProductReviewPresenter presenter;
    int scroll;
    private boolean isFirstTime;
    private TkpdProgressDialog progressDialog;

    ProgressBar progressBar;
    ReputationRecyclerView recyclerView;
    View statsView;

    SnackbarRetry snackbarRetry;

    public static ProductReviewFragment createInstance(String NAV, String productID, String shopID) {
        ProductReviewFragment fragment = new ProductReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nav", NAV);
        bundle.putString("product_id", productID);
        bundle.putString("shop_id", shopID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        if (isFirstTime && isDataEmpty()) {
            presenter.getReputationFromCache(getProductID(), NAV);
        }
    }


    private void initVariable() {
        isFirstTime = true;
        NAV = getArguments().getString("nav");
        productID = getArguments().getString("product_id");
        page = new PagingHandler();
        recyclerList = new ArrayList<>();
        slideOffViewHandler = new SlideOffViewHandler();
        ratingStatsUtils = RatingStatsUtils.createInstance(getActivity());
        presenter = new ProductReviewPresenterImpl(getActivity(), NAV, this);
        adapter = new ProductReviewAdapter(getActivity(), recyclerList);
        adapter.setPresenter(presenter);
        adapter.setProductId(getProductID());
        scroll = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_reputation_product, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.include_loading);
        recyclerView = (ReputationRecyclerView) rootView.findViewById(R.id.recycler_view);
        statsView = rootView.findViewById(R.id.view_rating_stats);
        initView();
        setAdapter();
        setListener();
        if (savedInstanceState != null) {
            presenter.restoreStateList(savedInstanceState);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveStateList(outState, recyclerList,
                layoutManager.findLastVisibleItemPosition()
                , ratingStatsUtils.getAdvanceReview()
                , ratingStatsUtils.getCurrentRating(), ratingStatsUtils.getCurrentRatingType()
                , page.getPage(), page.CheckNextPage());

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null)
            presenter.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribeNetwork();
    }

    private void initView() {
        displayLoading(true);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        ratingStatsUtils.setView(rootView);
        refresh = new RefreshHandler(getActivity(), rootView, onRefreshListener());
    }

    private void setAdapter() {
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setListener() {
        ratingStatsUtils.setListener(OnRatingStatsClickListener());
        recyclerView.addOnScrollListener(OnScrollRecyclerView());
        recyclerView.setOnTouchListener(OnListTouchListener());
    }

    private RecyclerView.OnScrollListener OnScrollRecyclerView() {
        return new RecyclerView.OnScrollListener() {
            @Override

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastPosition() && !isRequest && !refresh.isRefreshing() && !isSnackbarVisible(snackbarRetry)) {
                    if (page.CheckNextPage()) {
                        refresh.setPullEnabled(false);
                        getNextPage();
                    }
                }
                scroll += dy;
                if (dy > 75 && isNotFirstPosition()) disableFilterStats();

                Log.d("scroll", dy + " " + scroll + " " + layoutManager.findFirstCompletelyVisibleItemPosition());

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if ((scroll < -75) && newState == RecyclerView.SCROLL_STATE_IDLE && !refresh.isRefreshing()) {
                    enableFilterStats();
                }
                scroll = 0;
            }
        };
    }

    private boolean isSnackbarVisible(SnackbarRetry snackbarRetry) {
        return snackbarRetry != null && snackbarRetry.isShown();
    }

    @Override
    public String getProductID() {
        return this.productID;
    }

    @Override
    public String getNAV() {
        return this.NAV;
    }

    @Override
    public void showLoadingDialog() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.showDialog();
    }

    @Override
    public void dismissLoadingDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showSnackbar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showNetworkErrorSnackbar() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public String getShopId() {
        return getArguments().getString("shop_id");

    }

    @Override
    public void returnHelpfulReview(HelpfulReviewList model) {
        Log.d("steven", "gotcha most helpful");

        if (model.getList().size() > 0) {
            if (containHelpfulReview())
                recyclerList.set(0, model);
            else
                recyclerList.add(0, model);
            adapter.notifyDataSetChanged();
        }
    }

    private boolean containHelpfulReview() {
        return recyclerList.get(0) instanceof HelpfulReviewList;
    }

    private void getReputation() {
        if (statsView != null) disableFilterStats();
        isFirstTime = false;
        isRequest = true;
        presenter.getReputation(getParamReview());
        Log.d("steven ", getParamReview().toString());
    }


    private HashMap<String, String> getParamReview() {
        HashMap<String, String> map = new HashMap<>();
        map.put("product_id", productID);
        map.put("page", String.valueOf(page.getPage()));
        map.put("NAV", NAV);
        map.put("filter", String.valueOf(ratingStatsUtils.getCurrentRatingType()));
        map.put("filter_value", String.valueOf(ratingStatsUtils.getCurrentRating()));
        return map;
    }

    public void onConnectionResponse(AdvanceReview model, List<ReviewProductModel> listResponse,
                                     PagingHandler.PagingHandlerModel result) {
        Log.d("steven ", "getting from connection " + String.valueOf(page.getPage()));
        refresh.setPullEnabled(true);
        isRequest = false;
        if (page.getPage() == 1) {
            displayLoading(false);
            //set header
            ratingStatsUtils.setModelToView(model);
            enableFilterStats();
            //get most helpful
            presenter.getMostHelpfulReview();
            Log.d("steven", "go Most Helpful");
        }
        page.setHasNext(PagingHandler.CheckHasNext(result));
        if (page.CheckNextPage()) setLoadingFooter();
        else removeLoadingFooter();
        adapter.notifyDataSetChanged();
        if (refresh.isRefreshing()) {
            refresh.finishRefresh();
            cleanArrayList();
        }
        recyclerList.addAll(listResponse);

    }

    @Override
    public void onConnectionTimeOut() {
        onConnectionTimeOut("");
    }

    @Override
    public void onConnectionTimeOut(String error) {
        Log.d("steven", "timeout " + page.getPage());
        isRequest = false;
        if (page.getPage() == 1) {
            if (refresh.isRefreshing()) {
                refresh.finishRefresh();
            }
            if (recyclerList.size() > 0) {
                removeLoadingFooter();
                adapter.notifyDataSetChanged();
                snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), refreshSnackbarListener());
                snackbarRetry.showRetrySnackbar();
            } else {
                displayView(false);
                if (error.length() <= 0) {
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryListener());
                } else {
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, retryListener());
                }
            }
        } else {
            removeLoadingFooter();
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), retrySnackbarListener());
            adapter.notifyDataSetChanged();
            snackbarRetry.showRetrySnackbar();
        }
    }

    private NetworkErrorHelper.RetryClickedListener refreshSnackbarListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (!isRequest) {
                    doRefresh();
                }
            }
        };
    }

    private NetworkErrorHelper.RetryClickedListener retrySnackbarListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                getReputation();
                setLoadingFooter();
                adapter.notifyDataSetChanged();
            }
        };
    }

    private NetworkErrorHelper.RetryClickedListener retryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                displayLoading(true);
                getReputation();
            }
        };
    }

    @Override
    public void onStateResponse(List list, int position, AdvanceReview advance, int currentRating, RatingStatsUtils.RatingType currentRatingType, int page, boolean pageHasNext) {
        Log.d("steven", "state " + position);

        isRequest = false;
        if (advance != null) {
            displayLoading(false);
            if (recyclerList.isEmpty()) recyclerList.addAll(list);
            ratingStatsUtils.setModelToView(advance);
            ratingStatsUtils.cacheSetBackground(currentRating);
            ratingStatsUtils.setCurrentRating(currentRating);
            ratingStatsUtils.setCurrentRatingType(currentRatingType);
            if (currentRatingType.equals(RatingStatsUtils.RatingType.ByAccuracy)) {
                ratingStatsUtils.flipCard();
                ratingStatsUtils.changeModelAdvance();
            }
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(position);
            if (position < 2) enableFilterStats();
            this.page.setPage(page);
            this.page.setHasNext(pageHasNext);
            if (pageHasNext) setLoadingFooter();
        }
    }

    @Override
    public void onCacheResponse(AdvanceReview advanceReview, List<ReviewProductModel> listReputation, PagingHandler.PagingHandlerModel paging) {
        Log.d("steven", "cache");
        if (advanceReview != null) {
            displayLoading(false);
            enableFilterStats();
            recyclerList.clear();
            recyclerList.addAll(listReputation);
            ratingStatsUtils.setModelToView(advanceReview);
            adapter.notifyDataSetChanged();
            presenter.getMostHelpfulReview();
            page.resetPage();
            boolean pageHasNext = PagingHandler.CheckHasNext(paging);
            this.page.setHasNext(pageHasNext);
            if (pageHasNext) setLoadingFooter();
        } else {
            getReputation();
        }
    }

    private boolean isDataEmpty() {
        try {
            return (page.getPage() == 1 && !isRequest && recyclerList.size() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                OnRefreshReputationProduct();
            }
        };
    }

    private void OnRefreshReputationProduct() {
        if (!isRequest) {
            doRefresh();
        } else {
            refresh.finishRefresh();
        }
    }

    private void doRefresh() {
        if (!refresh.isRefreshing()) {
            refresh.setRefreshing(true);
            refresh.setIsRefreshing(true);
        }
        disableFilterStats();
        page.resetPage();
        adapter.resetLastPosition();
        removeLoadingFooter();
        getReputation();

    }

    private void getNextPage() {
        page.nextPage();
        getReputation();
    }


    private View.OnTouchListener OnListTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                slideOffViewHandler.MotionEventListener(event,
                        new SlideOffViewHandler.SlideOffMotionEventListener() {
                            @Override
                            public void OnMoveDown() {

                            }

                            @Override
                            public void OnMoveUp() {
                                if (layoutManager.findFirstCompletelyVisibleItemPosition() <= 0
                                        && layoutManager.findFirstVisibleItemPosition() == 0)
                                    enableFilterStats();
                            }
                        });
                return false;
            }
        };
    }


    private RatingStatsUtils.RatingStatsClickListener OnRatingStatsClickListener() {
        return new RatingStatsUtils.RatingStatsClickListener() {
            @Override
            public void setOnChangeRatingType(RatingStatsUtils.RatingType filter) {

            }

            @Override
            public void setOnSortByRatingStar(int star, RatingStatsUtils.RatingType rating) {
                cleanArrayList();
                displayLoading(true);
                doRefresh();
            }
        };
    }


    private void enableFilterStats() {
        if (!isSnackbarVisible(snackbarRetry)) {
            slideOffViewHandler.ToggleSlideOffScreen(statsView, true, true);
        }
    }

    private void disableFilterStats() {
        slideOffViewHandler.ToggleSlideOffScreen(statsView, true, false);
    }

    public void displayLoading(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void displayView(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }


    private void setLoadingFooter() {
        adapter.setIsLoading(true);
    }

    private void removeLoadingFooter() {
        adapter.setIsLoading(false);
    }

    private void cleanArrayList() {
        recyclerList.clear();
    }

    private boolean isLastPosition() {
        int lastIndex = layoutManager.findLastCompletelyVisibleItemPosition();
        int size = layoutManager.getItemCount() - 1;
        return lastIndex == size;
    }

    private boolean isNotFirstPosition() {
        switch (getActivity().getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                return (layoutManager.findFirstCompletelyVisibleItemPosition() > 2
                        || layoutManager.findFirstCompletelyVisibleItemPosition() == -1);
            case Configuration.ORIENTATION_PORTRAIT:
                return (layoutManager.findFirstCompletelyVisibleItemPosition() > 2);
            default:
                return (layoutManager.findFirstCompletelyVisibleItemPosition() > 2);
        }
    }
}
