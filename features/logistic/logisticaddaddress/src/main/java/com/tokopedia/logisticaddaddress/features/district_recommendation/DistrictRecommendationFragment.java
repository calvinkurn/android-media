package com.tokopedia.logisticaddaddress.features.district_recommendation;

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
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent;
import com.tokopedia.logisticaddaddress.di.DistrictRecommendationComponent;
import com.tokopedia.logisticaddaddress.domain.mapper.AddressMapper;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.INTENT_DISTRICT_RECOMMENDATION_ADDRESS;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class DistrictRecommendationFragment
        extends BaseSearchListFragment<AddressViewModel, DistrictRecommendationTypeFactory>
        implements DiscomContract.View {

    private static final String ARGUMENT_DATA_TOKEN = "token";
    private static final long DEBOUNCE_DELAY_IN_MILIS = 700;
    private static final int MINIMUM_SEARCH_KEYWORD_CHAR = 3;

    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID = "district_id";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME = "district_name";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID = "city_id";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME = "city_name";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID = "province_id";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME = "province_name";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES = "zipcodes";

    private ITransactionAnalyticsDistrictRecommendation transactionAnalyticsDistrictRecommendation;

    private TextView tvMessage;
    private SwipeToRefresh swipeRefreshLayout;
    private Token mToken;

    @Inject
    UserSessionInterface userSession;

    @Inject
    AddressMapper addressMapper;

    @Inject
    DiscomContract.Presenter presenter;

    public static DistrictRecommendationFragment newInstance() {
        return new DistrictRecommendationFragment();
    }

    public static DistrictRecommendationFragment newInstance(Token token) {
        DistrictRecommendationFragment fragment = new DistrictRecommendationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_DATA_TOKEN, token);
        fragment.setArguments(bundle);
        return fragment;
    }

    public DistrictRecommendationFragment() {
    }

    @Override
    protected void initInjector() {
        BaseAppComponent appComponent = getComponent(BaseAppComponent.class);
        DistrictRecommendationComponent districtRecommendationComponent =
                DaggerDistrictRecommendationComponent.builder()
                        .baseAppComponent(appComponent)
                        .build();
        districtRecommendationComponent.inject(this);
        presenter.attach(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        transactionAnalyticsDistrictRecommendation = (ITransactionAnalyticsDistrictRecommendation) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToken = getArguments().getParcelable(ARGUMENT_DATA_TOKEN);
        }
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
            presenter.detach();
        }
        super.onDestroy();
    }

    @Override
    public void loadData(int page) {
        if (isAdded()) {
            if (!TextUtils.isEmpty(searchInputView.getSearchText()) &&
                    searchInputView.getSearchText().length() >= MINIMUM_SEARCH_KEYWORD_CHAR) {
                if (mToken != null) {
                    presenter.loadData(searchInputView.getSearchText(), mToken, page);
                } else {
                    presenter.loadData(searchInputView.getSearchText(), page);

                }
            } else {
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
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS, addressMapper.convertAddress(addressViewModel.getAddress()));

            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, addressViewModel.getAddress().getDistrictId());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, addressViewModel.getAddress().getDistrictName());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, addressViewModel.getAddress().getCityId());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, addressViewModel.getAddress().getCityName());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, addressViewModel.getAddress().getProvinceId());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, addressViewModel.getAddress().getProvinceName());
            resultIntent.putStringArrayListExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES, addressViewModel.getAddress().getZipCodes());

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
        setMessageSection(false);
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        setMessageSection(false);
        super.hideLoading();
    }

    @Override
    public void showNoResultMessage() {
        tvMessage.setText(getString(R.string.message_search_address_no_result));
        setMessageSection(true);
    }

    @Override
    public void showInitialLoadMessage() {
        tvMessage.setText(getString(R.string.message_advice_search_address));
        setMessageSection(true);
    }

    private void setMessageSection(boolean active) {
        tvMessage.setVisibility(active ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setVisibility(active ? View.GONE : View.VISIBLE);
    }
}
