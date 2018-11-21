package com.tokopedia.district_recommendation.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.district_recommendation.R;
import com.tokopedia.district_recommendation.di.DaggerDistrictRecommendationComponent;
import com.tokopedia.district_recommendation.di.DistrictRecommendationComponent;
import com.tokopedia.district_recommendation.domain.mapper.AddressMapper;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.INTENT_DATA_ADDRESS;
import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.INTENT_DISTRICT_RECOMMENDATION_ADDRESS;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class DistrictRecommendationFragment
        extends BaseSearchListFragment<AddressViewModel, DistrictRecommendationTypeFactory>
        implements DistrictRecommendationContract.View {

    private static final String ARGUMENT_DATA_TOKEN = "token";
    private static final long DEBOUNCE_DELAY_IN_MILIS = 700;
    private static final int MINIMUM_SEARCH_KEYWORD_CHAR = 3;

    private ITransactionAnalyticsDistrictRecommendation transactionAnalyticsDistrictRecommendation;

    private TextView tvMessage;
    private SwipeToRefresh swipeRefreshLayout;

    @Inject
    UserSessionInterface userSession;

    @Inject
    AddressMapper addressMapper;

    @Inject
    DistrictRecommendationContract.Presenter presenter;

    public static DistrictRecommendationFragment newInstance(Token token) {
        DistrictRecommendationFragment fragment = new DistrictRecommendationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_DATA_TOKEN, token);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent appComponent = getComponent(BaseAppComponent.class);
        DistrictRecommendationComponent districtRecommendationComponent =
                DaggerDistrictRecommendationComponent.builder()
                        .baseAppComponent(appComponent)
                        .build();
        districtRecommendationComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        transactionAnalyticsDistrictRecommendation = (ITransactionAnalyticsDistrictRecommendation) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district_recommendation, container, false);
        tvMessage = view.findViewById(R.id.tv_message);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showMessageSection();
        showInitialLoadMessage();
        searchInputView.setSearchHint(getString(R.string.hint_district_recommendation_search));
        searchInputView.setDelayTextChanged(DEBOUNCE_DELAY_IN_MILIS);
        searchInputView.getCloseImageButton().setOnClickListener(v -> {
            searchInputView.setSearchText("");
            transactionAnalyticsDistrictRecommendation.sendAnalyticsOnClearTextDistrictRecommendationInput();
        });
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onDetach() {
        transactionAnalyticsDistrictRecommendation = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void loadData(int page) {
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(searchInputView.getSearchText()) &&
                    searchInputView.getSearchText().length() >= MINIMUM_SEARCH_KEYWORD_CHAR) {
                hideMessageSection();
                Token token = getArguments().getParcelable(ARGUMENT_DATA_TOKEN);
                presenter.loadData(searchInputView.getSearchText(), token, page);
            } else {
                showMessageSection();
                showInitialLoadMessage();
            }
        }
    }

    @Override
    protected DistrictRecommendationTypeFactory getAdapterTypeFactory() {
        return new DistrictRecommendationAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(AddressViewModel addressViewModel) {
        if (getActivity() != null) {
            transactionAnalyticsDistrictRecommendation.sendAnalyticsOnDistrictDropdownSelectionItemClicked(addressViewModel.getAddress().getDistrictName());
            Intent resultIntent = new Intent();
            resultIntent.putExtra(INTENT_DATA_ADDRESS, addressViewModel.getAddress());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS, addressMapper.convertAddress(addressViewModel.getAddress()));
            getActivity().setResult(Activity.RESULT_OK, resultIntent);
            getActivity().finish();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSearchSubmitted(String text) {
        clearAllData();
        loadData(getDefaultInitialPage());
    }

    @Override
    public void onSearchTextChanged(String text) {
        clearAllData();
        loadData(getDefaultInitialPage());
    }

    @Override
    public void renderList(@NonNull List<AddressViewModel> list, boolean hasNextPage) {
        super.renderList(list, hasNextPage);
        if (getCurrentPage() == getDefaultInitialPage() && hasNextPage) {
            int page = getCurrentPage() + getDefaultInitialPage();
            loadData(page);
        }
    }

    @Override
    public void showLoading() {
        hideMessageSection();
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        hideMessageSection();
        super.hideLoading();
    }

    @Override
    public void showNoResultMessage() {
        tvMessage.setText(getString(R.string.message_search_address_no_result));
        showMessageSection();
    }

    @Override
    public void showInitialLoadMessage() {
        tvMessage.setText(getString(R.string.message_advice_search_address));
        showMessageSection();
    }

    private void hideMessageSection() {
        tvMessage.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    private void showMessageSection() {
        swipeRefreshLayout.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
    }
}
