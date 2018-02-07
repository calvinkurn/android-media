package com.tokopedia.core.manage.general.districtrecommendation.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.manage.general.districtrecommendation.di.DaggerDistrictRecommendationComponent;
import com.tokopedia.core.manage.general.districtrecommendation.di.DistrictRecommendationComponent;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationContract.Constant.ARGUMENT_DATA_TOKEN;
import static com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationContract.Constant.INTENT_DATA_ADDRESS;

public class DistrictRecommendationFragment
        extends BasePresenterFragment<DistrictRecommendationContract.Presenter>
        implements DistrictRecommendationContract.View, DistrictRecommendationAdapter.Listener {

    private static final int THRESHOLD = 3;
    @BindView(R2.id.search_address)
    SearchView searchAddress;
    @BindView(R2.id.recycler_view_suggestion)
    RecyclerView rvAddressSuggestion;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.tv_message)
    TextView tvMessage;
    @BindView(R2.id.network_error_view)
    LinearLayout networkErrorView;
    private int maxItemPosition;
    private OnQueryListener queryListener;
    private DistrictRecommendationAdapter adapter;
    private CompositeSubscription compositeSubscription;

    @Inject
    DistrictRecommendationContract.Presenter presenter;

    public DistrictRecommendationFragment() {
        // Required empty public constructor
    }

    public static DistrictRecommendationFragment newInstance(Token token) {
        DistrictRecommendationFragment fragment = new DistrictRecommendationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_DATA_TOKEN, token);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        initializeInjector();
        presenter.attachView(this);
        presenter.setToken((Token) getArguments().getParcelable(ARGUMENT_DATA_TOKEN));
    }

    private void initializeInjector() {
        AppComponent component = ((DistrictRecommendationActivity) getActivity()).getApplicationComponent();
        DistrictRecommendationComponent districtRecommendationComponent =
                DaggerDistrictRecommendationComponent.builder()
                        .appComponent(component)
                        .build();
        districtRecommendationComponent.inject(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search_address;
    }

    @Override
    protected void initView(View view) {
        searchAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                submitQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                submitQuery(newText);
                return true;
            }
        });
    }

    private void submitQuery(String text) {
        try {
            View networkError = networkErrorView.findViewById(R.id.main_retry);
            if (networkError != null) {
                networkErrorView.removeAllViewsInLayout();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (text.length() == 0) {
            hideLoading();
            presenter.clearData();
        } else {
            if (text.length() >= THRESHOLD) {
                if (queryListener != null) {
                    maxItemPosition = 0;
                    queryListener.onQuerySubmit(text);
                }
            }
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        adapter = new DistrictRecommendationAdapter(presenter.getAddresses(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvAddressSuggestion.setLayoutManager(linearLayoutManager);
        rvAddressSuggestion.setAdapter(adapter);

        rvAddressSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                int totalItemCount = adapter.getItemCount();
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();

                if (maxItemPosition < lastVisibleItemPosition) {
                    maxItemPosition = lastVisibleItemPosition;
                }

                if ((maxItemPosition + 1) == totalItemCount) {
                    if (pbLoading.getVisibility() == View.GONE) {
                        presenter.searchNextIfAvailable(searchAddress.getQuery().toString());
                    }
                }
            }
        });

        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                queryListener = new OnQueryListener() {
                    @Override
                    public void onQuerySubmit(String query) {
                        subscriber.onNext(String.valueOf(query));
                    }
                };
            }
        }).debounce(700, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.from(new JobExecutor()))
                .observeOn(new UIThread().getScheduler()).subscribe(queryAutoCompleteSubscriber()));
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void updateRecommendation() {
        if (searchAddress.getQuery().toString().length() == 0) {
            showMessage(getString(R.string.hint_advice_search_address));
        } else {
            if (adapter.getItemCount() == 0) {
                showMessage(getString(R.string.hint_search_address_no_result));
            } else {
                hideMessage();
            }
        }
    }

    @Override
    public void notifyUpdateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        rvAddressSuggestion.setVisibility(View.GONE);
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMessage() {
        tvMessage.setVisibility(View.GONE);
        rvAddressSuggestion.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        tvMessage.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void setInitialLoading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            pbLoading.setLayoutParams(params);
        }
    }

    @Override
    public void setLoadMoreLoading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.removeRule(RelativeLayout.CENTER_VERTICAL);
            pbLoading.setLayoutParams(params);
        }
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showNoConnection(@NonNull String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), networkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        submitQuery(searchAddress.getQuery().toString());
                    }
                });
    }

    private Subscriber<String> queryAutoCompleteSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String query) {
                if (isAdded()) {
                    presenter.searchAddress(query);
                }
            }
        };
    }

    @Override
    public void onItemClick(Address address) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(INTENT_DATA_ADDRESS, address);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
        presenter.detachView();
    }

    private interface OnQueryListener {
        void onQuerySubmit(String query);
    }
}
