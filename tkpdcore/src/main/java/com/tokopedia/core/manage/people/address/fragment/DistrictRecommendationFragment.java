package com.tokopedia.core.manage.people.address.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.manage.people.address.adapter.DistrictRecommendationAdapter;
import com.tokopedia.core.manage.people.address.listener.DistrictRecomendationFragmentView;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Token;
import com.tokopedia.core.manage.people.address.presenter.DistrictRecomendationFragmentPresenter;
import com.tokopedia.core.manage.people.address.presenter.DistrictRecomendationFragmentPresenterImpl;
import com.tokopedia.design.text.SearchInputView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DistrictRecommendationFragment extends BasePresenterFragment<DistrictRecomendationFragmentPresenter>
        implements DistrictRecomendationFragmentView, DistrictRecommendationAdapter.Listener {

    private static final String TAG = DistrictRecommendationFragment.class.getSimpleName();

    @BindView(R2.id.search_input_view_address)
    SearchInputView searchInputViewAddress;
    @BindView(R2.id.recycler_view_suggestion)
    RecyclerView rvAddressSuggestion;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.tv_advice)
    TextView tvAdvice;
    @BindView(R2.id.tv_no_result)
    TextView tvNoResult;

    private int maxItemPosition;
    private OnQueryListener queryListener;
    private DistrictRecommendationAdapter adapter;
    private CompositeSubscription compositeSubscription;

    public DistrictRecommendationFragment() {
        // Required empty public constructor
    }

    public static DistrictRecommendationFragment newInstance(Token token) {
        DistrictRecommendationFragment fragment = new DistrictRecommendationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.ARGUMENT_DATA_TOKEN, token);
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
        presenter = new DistrictRecomendationFragmentPresenterImpl(this,
                (Token) getArguments().getParcelable(Constant.ARGUMENT_DATA_TOKEN));
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
        searchInputViewAddress.setSearchHint(getString(R.string.hint_district_recommendation_search));
        searchInputViewAddress.setListener(new SearchInputView.Listener() {
            @Override
            public void onSearchSubmitted(String text) {
                submitQuery(text);
            }

            @Override
            public void onSearchTextChanged(String text) {
                submitQuery(text);
            }
        });
    }

    private void submitQuery(String text) {
        if (text.length() == 0) {
            presenter.clearData();
        } else {
            if (text.length() > 2) {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
                        presenter.searchNextIfAvailable(searchInputViewAddress.getSearchText());
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
        if (searchInputViewAddress.getSearchText().length() == 0) {
            tvNoResult.setVisibility(View.GONE);
            rvAddressSuggestion.setVisibility(View.GONE);
            tvAdvice.setVisibility(View.VISIBLE);
        } else {
            adapter.notifyDataSetChanged();
            if (adapter.getItemCount() == 0) {
                rvAddressSuggestion.setVisibility(View.GONE);
                tvAdvice.setVisibility(View.GONE);
                tvNoResult.setVisibility(View.VISIBLE);
            } else {
                tvAdvice.setVisibility(View.GONE);
                tvNoResult.setVisibility(View.GONE);
                rvAddressSuggestion.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showLoading() {
        tvNoResult.setVisibility(View.GONE);
        tvAdvice.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
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
                presenter.searchAddress(query);
            }
        };
    }

    @Override
    public void onItemClick(Address address) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(DistrictRecomendationFragmentView.Constant.INTENT_DATA_ADDRESS, address);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }

    private interface OnQueryListener {
        void onQuerySubmit(String query);
    }
}
