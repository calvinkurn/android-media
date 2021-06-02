package com.tokopedia.review.feature.reputationhistory.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.base.list.seller.view.adapter.BaseRetryDataBinder;
import com.tokopedia.base.list.seller.view.old.RetryDataBinder;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.datepicker.range.view.listener.DatePickerResultListener;
import com.tokopedia.review.R;
import com.tokopedia.review.ReviewInstance;
import com.tokopedia.review.feature.reputationhistory.di.DaggerSellerReputationComponent;
import com.tokopedia.review.feature.reputationhistory.di.SellerReputationModule;
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationMergeUseCase;
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationUseCase;
import com.tokopedia.review.feature.reputationhistory.util.DefaultErrorSubscriber;
import com.tokopedia.review.feature.reputationhistory.util.NetworkStatus;
import com.tokopedia.review.feature.reputationhistory.view.SellerReputationView;
import com.tokopedia.review.feature.reputationhistory.view.activity.SellerReputationInfoActivity;
import com.tokopedia.review.feature.reputationhistory.view.adapter.SellerReputationAdapter;
import com.tokopedia.review.feature.reputationhistory.view.adapter.SimpleDividerItemDecoration;
import com.tokopedia.review.feature.reputationhistory.view.helper.GMStatHeaderViewHelper;
import com.tokopedia.review.feature.reputationhistory.view.helper.RefreshHandler;
import com.tokopedia.review.feature.reputationhistory.view.helper.ReputationViewHelper;
import com.tokopedia.review.feature.reputationhistory.view.model.SetDateHeaderModel;
import com.tokopedia.review.feature.reputationhistory.view.presenter.SellerReputationFragmentPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author normansyahputa
 */
public class SellerReputationFragment extends BaseDaggerFragment
        implements SellerReputationView, RetryDataBinder.OnRetryListener,
        DefaultErrorSubscriber.ErrorNetworkListener, DatePickerResultListener.DatePickerResult {

    public static final String TAG = "SellerReputationFragmen";

    RecyclerView listViewBalance;

    View mainView;

    RelativeLayout topSlideOffBar;

    SellerReputationAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;

    SwipeToRefresh swipeToRefresh;

    @Inject
    ReviewReputationUseCase reviewReputationUseCase;
    @Inject
    GCMHandler gcmHandler;
    @Inject
    ReviewReputationMergeUseCase reviewReputationMergeUseCase;

    SellerReputationFragmentPresenter presenter;

    @Inject
    UserSessionInterface userSession;

    private SnackbarRetry snackbarRetry;
    private View rootView;
    private ReputationViewHelper reputationViewHelper;
    private boolean isFirstTime = true;
    private boolean isEndOfFile = true;
    private RecyclerView.OnScrollListener onScrollListener;
    private DatePickerResultListener datePickerResultListener;
    private RelativeLayout rlReputationPointCalculation;
    private AppBarLayout appBarLayout;
    private int appBarLayoutHeight;
    private CoordinatorLayout.LayoutParams orignalLp;

    private ArrayList<Parcelable> tempParcelables;

    public static SellerReputationFragment createInstance() {
        SellerReputationFragment fragment = new SellerReputationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null) {
            adapter.saveStates(outState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * this is limitation due to {@link BasePresenterFragment}
         */
        if (savedInstanceState != null) {
            tempParcelables = savedInstanceState.getParcelableArrayList(
                    SellerReputationAdapter.KEY_LIST_DATA);
        }

        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        setRetainInstance(isRetainInstance());
        Log.d(TAG, "ON CREATE");
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        initialPresenter();
    }

    @Override
    protected void initInjector() {

    }

    protected boolean isRetainInstance() {
        return false;
    }

    protected void onFirstTimeLaunched() {
        setActionsEnabled(false);
    }

    public void onSaveState(Bundle state) {

    }

    public void onRestoreState(Bundle savedState) {

    }

    protected boolean getOptionsMenuEnable() {
        return false;
    }

    protected void initialPresenter() {
        presenter = new SellerReputationFragmentPresenter();
        presenter.attachView(this);
    }

    protected void initialListener(Activity activity) {
    }

    protected void setupArguments(Bundle arguments) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        adapter.setFragment(null);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_seller_reputation;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (getActivity() != null) {
            appBarLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }
        initialVar();
        setViewListener();
        setActionVar();
    }


    protected void initView(View view) {
        this.rootView = view;
        listViewBalance = view.findViewById(R.id.balance_list);
        mainView = view.findViewById(R.id.main_view);
        topSlideOffBar = view.findViewById(R.id.seller_reputation_header);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        appBarLayout = view.findViewById(R.id.seller_reputation_app_bar_layout);
        appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                appBarLayoutHeight = appBarLayout.getHeight();
            }
        });
        this.rootView = view;
        this.refreshHandler = new RefreshHandler(swipeToRefresh, onRefresh());
        rlReputationPointCalculation = (RelativeLayout) view.findViewById(R.id.rl_reputation_point_calculation);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTime) {
            UserSessionInterface userSession = new UserSession(getActivity());
            presenter.setUserSession(userSession);
            presenter.setReviewReputationUseCase(reviewReputationUseCase);
            presenter.setGcmHandler(gcmHandler);
            presenter.setErrorNetworkListener(this);
            presenter.setReviewReputationMergeUseCase(reviewReputationMergeUseCase);
            presenter.fillMessages(getActivity());
            reputationViewHelper = new ReputationViewHelper(topSlideOffBar);
        }
        setupRecyclerView();
        fetchData();
    }

    private void fetchData() {
        if (presenter.getNetworkStatus()
                == NetworkStatus.ONACTIVITYFORRESULT) {
            refreshHandler.setRefreshing(true);
            firstTimeNetworkCall();
        } else {
            if (isFirstTime) {
                Observable
                        .just(true)
                        .delay(100, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.immediate())
                        .doOnNext(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                presenter.setNetworkStatus(NetworkStatus.PULLTOREFRESH);
                                adapter.showLoadingFull(true);
                                /*
                                below is a must otherwise it will throw
                                "CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views"
                                because it doesn't use any subscriber.
                                 */
                                SellerReputationFragment.this.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeToRefresh.setEnabled(false);
                                    }
                                });
                                firstTimeNetworkCall();

                                isFirstTime = false;
                            }
                        }).toBlocking().first();

            }
        }
    }

    private void setupRecyclerView() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isEndOfFile) {
                    return;
                }

                if (presenter.isHitNetwork())
                    return;

                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem
                        && adapter.getDataSize() < Integer.MAX_VALUE) {
                    presenter.incrementPage();
                    presenter.setNetworkStatus(NetworkStatus.LOADMORE);
                    presenter.loadMoreNetworkCall();
                }
            }
        };
        listViewBalance.addOnScrollListener(onScrollListener);
    }

    private void inject() {
       if(getActivity() != null) {
           //[START] This is for dependent component
           DaggerSellerReputationComponent.builder()
                   .sellerReputationModule(new SellerReputationModule())
                   .reviewComponent(ReviewInstance.Companion.getComponent(getActivity().getApplication()))
                   .build().inject(this);
           //[END] This is for dependent component
       }
    }

    @Override
    public void onPause() {
        super.onPause();
        listViewBalance.removeOnScrollListener(onScrollListener);
    }

    @Override
    public void dismissSnackbar() {
        if (snackbarRetry != null) {
            snackbarRetry.hideRetrySnackbar();
        }
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                dismissSnackbar();

                if (adapter.getDataSize() < 0) {
                    adapter.clear();
                    adapter.notifyDataSetChanged();

                    adapter.showLoadingFull(true);
                }

                presenter.resetPage();
                presenter.setNetworkStatus(NetworkStatus.PULLTOREFRESH);
                firstTimeNetworkCall();
            }
        };
    }

    private void firstTimeNetworkCall() {
        presenter.firstTimeNetworkCall2();
    }

    private void loadMoreCall() {
        presenter.loadMoreNetworkCall();
    }

    protected void setViewListener() {
        discardOnClickInfo();
    }

    private void discardOnClickInfo() {
        rlReputationPointCalculation.setOnClickListener(null);
    }

    private void setOnClickInfo() {
        rlReputationPointCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerReputationFragment.this.startActivity(
                        new Intent(
                                SellerReputationFragment.this.getActivity(),
                                SellerReputationInfoActivity.class
                        )
                );
            }
        });
    }

    protected void initialVar() {
        if (tempParcelables != null) {
            adapter = SellerReputationAdapter.createInstance(getActivity(), tempParcelables);
        } else {
            adapter = SellerReputationAdapter.createInstance(getActivity());
        }
        adapter.setFragment(this);
        RetryDataBinder topAdsRetryDataBinder = new BaseRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                dismissSnackbar();

                if (adapter.getDataSize() < 0) {
                    adapter.clear();
                    adapter.notifyDataSetChanged();

                    adapter.showLoadingFull(true);
                    swipeToRefresh.setEnabled(false);
                }

                presenter.resetPage();
                presenter.setNetworkStatus(NetworkStatus.PULLTOREFRESH);
                firstTimeNetworkCall();
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);
        adapter.setOnRetryListenerRV(this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listViewBalance.setLayoutManager(linearLayoutManager);
        listViewBalance.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        listViewBalance.setAdapter(adapter);
        datePickerResultListener = new DatePickerResultListener(this, GMStatHeaderViewHelper.MOVE_TO_SET_DATE);
    }

    protected void setActionVar() {
    }

    @Override
    public String getStartDate() {
        return null;
    }

    @Override
    public void setStartDate(String date) {

    }

    @Override
    public void setLoadMoreFlag(boolean loadmoreflag) {
        isEndOfFile = loadmoreflag;
    }

    @Override
    public void loadData(List<ItemType> datas) {
        renderDatas(datas);
    }

    @Override
    public void loadMore(List<ItemType> datas) {
        renderDatas(datas);
    }

    @Override
    public void loadShopInfo(ShopModel shopModel) {
        reputationViewHelper.renderData(shopModel);
    }

    @Override
    public SetDateHeaderModel getHeaderModel() {
        return adapter.getHeaderModel();
    }

    private void renderDatas(List<ItemType> datas) {
        showAppBarLayout();
        setOnClickInfo();
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
        }

        adapter.showLoadingFull(false);
        adapter.showEmpty(false);
        adapter.showRetry(false);

        switch (presenter.getNetworkStatus()) {
            case LOADMORE:
                break;
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
            case SEARCHVIEW:
                adapter.clear();
                break;
            case RETRYNETWORKCALL:
                if (adapter.getDataSize() <= 0) {
                    adapter.clear();
                }
                break;
        }
        adapter.addAllWithoutNotify(datas);
        boolean isEmpty = adapter.getDataSize() <= 0;
        if (isEmpty) {
            if (!isEndOfFile) {
                adapter.showLoading(true);
            } else {
                adapter.showEmptyFull(true);
            }
            adapter.notifyDataSetChanged();
        } else {
            if (!isEndOfFile) {
                adapter.showLoading(true);
            } else {
                adapter.showLoading(false);
            }
            adapter.notifyDataSetChanged();
        }
        swipeToRefresh.setEnabled(true);
    }

    @Override
    public String getEndDate() {
        return null;
    }

    @Override
    public void setEndDate(String date) {

    }

    @Override
    public void finishLoading() {
        adapter.showLoading(false);
        adapter.showEmpty(false);
        refreshHandler.setPullEnabled(true);
        refreshHandler.setRefreshing(false);

    }

    @Override
    public SellerReputationAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {
        adapter.showLoading(true);
    }

    private void showAppBarLayout() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        if (orignalLp == null) {
            orignalLp = new CoordinatorLayout.LayoutParams(lp);
        }
        appBarLayout.setLayoutParams(orignalLp);
        appBarLayout.requestLayout();
    }

    private void hideAppBarLayout() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

        if (orignalLp == null) {
            orignalLp = new CoordinatorLayout.LayoutParams(lp);
        }
        lp.height = 0;
        appBarLayout.setLayoutParams(lp);
    }

    @Override
    public void showErrorMessage(final String errorMessage) {

        // disable pull to refresh + hide
        refreshHandler.setRefreshing(false);
        adapter.showLoading(false);

        if (!isAdded())
            return;

        setOnClickInfo();

        final String tryAgain = getString(R.string.try_again);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && rootView != null) {
                    if (adapter.getDataSize() <= 0) {
                        adapter.clear();
                        adapter.showRetryFull(true);
                        hideAppBarLayout();
                    } else {
                        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, getListenerOnRetryErrorSnackbar());
                        snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
                        snackbarRetry.showRetrySnackbar();
                    }

                }
            }
        }, 100);
    }


    @NonNull
    private NetworkErrorHelper.RetryClickedListener getListenerOnRetryErrorSnackbar() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                onRetryConnectionSnackBar();
            }
        };
    }

    private void onRetryConnectionSnackBar() {
        dismissSnackbar();
        refreshHandler.setRefreshing(true);
        switch (presenter.getNetworkStatus()) {
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
                presenter.setNetworkStatus(NetworkStatus.PULLTOREFRESH);
                firstTimeNetworkCall();
                break;
            default:
                presenter.setNetworkStatus(NetworkStatus.RETRYNETWORKCALL);
                loadMoreCall();
                break;
        }
    }

    @Override
    public void removeError() {
        dismissSnackbar();
        adapter.showEmpty(false);

    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {
        refreshHandler.setPullEnabled(isEnabled);
    }

    @Override
    public boolean isRefreshing() {
        return refreshHandler.isRefreshing();
    }

    @Override
    public void refresh() {
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void showEmptyState() {
    }

    @Override
    public void setRetry() {
    }

    @Override
    public void showEmptyState(String error) {
    }

    @Override
    public void setRetry(String error) {
        setActionsEnabled(false);
        showErrorMessage(error);
    }

    @Override
    public void onRetryCliked() {

    }

    @Override
    public void showMessageError(String errorMessage) {
        showErrorMessage(errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (datePickerResultListener != null) {
            datePickerResultListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {

        // get header - index 0
        SetDateHeaderModel headerModel = adapter.getHeaderModel();
        // reformat view model
        headerModel.setStartDate(presenter.formatDate(sDate));
        headerModel.setEndDate(presenter.formatDate(eDate));
        headerModel.setsDate(sDate);
        headerModel.seteDate(eDate);
        // add to header adapter back
        adapter.notifyHeaderChange(headerModel);

        // set start date and end date to presenter
        presenter.setStartDate(sDate);
        presenter.setEndDate(eDate);

        presenter.setNetworkStatus(NetworkStatus.ONACTIVITYFORRESULT);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SELLER_REP_HISTORY;
    }
}
