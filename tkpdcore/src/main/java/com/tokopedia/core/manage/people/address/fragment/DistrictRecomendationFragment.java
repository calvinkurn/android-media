package com.tokopedia.core.manage.people.address.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.manage.people.address.adapter.DistrictRecommendationAdapter;
import com.tokopedia.core.manage.people.address.listener.DistrictRecomendationFragmentView;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;
import com.tokopedia.core.manage.people.address.presenter.DistrictRecomendationFragmentPresenter;
import com.tokopedia.core.manage.people.address.presenter.DistrictRecomendationFragmentPresenterImpl;
import com.tokopedia.design.text.SearchInputView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DistrictRecomendationFragment extends BasePresenterFragment<DistrictRecomendationFragmentPresenter>
        implements DistrictRecomendationFragmentView {

    private static final String TAG = DistrictRecomendationFragment.class.getSimpleName();

    @BindView(R2.id.search_input_view_address)
    SearchInputView searchInputViewAddress;

    @BindView(R2.id.recycler_view_suggestion)
    RecyclerView rvAddressSuggestion;

    private OnQueryListener queryListener;

    private DistrictRecommendationAdapter adapter;

    private CompositeSubscription compositeSubscription;

    public DistrictRecomendationFragment() {
        // Required empty public constructor
    }

    public static DistrictRecomendationFragment newInstance() {
        return new DistrictRecomendationFragment();
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
        presenter = new DistrictRecomendationFragmentPresenterImpl(this);
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
            }

            @Override
            public void onSearchTextChanged(String text) {
                if (text.length() > 2) {
                    if (queryListener != null) {
                        queryListener.onQuerySubmit(text);
                    }
                }
            }
        });
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        adapter = DistrictRecommendationAdapter.createInstance(presenter.getAddresses());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAddressSuggestion.setLayoutManager(linearLayoutManager);
        rvAddressSuggestion.setAdapter(adapter);

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
    public void showRecommendation(ArrayList<Address> addresses) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
                Log.d(TAG, "StartQueryWithKeyword:" + query);
                presenter.searchAddress(query);
            }
        };
    }

    private interface OnQueryListener {
        void onQuerySubmit(String query);
    }
}
