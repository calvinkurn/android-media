package com.tokopedia.affiliate.feature.explore.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.analytics.AffiliateEventTracking;
import com.tokopedia.affiliate.common.constant.AffiliateConstant;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.common.widget.ExploreSearchView;
import com.tokopedia.affiliate.feature.explore.di.DaggerExploreComponent;
import com.tokopedia.affiliate.feature.explore.view.adapter.AutoCompleteSearchAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.ExploreAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactoryImpl;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.AutoCompleteViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreFragment
        extends BaseDaggerFragment
        implements ExploreContract.View,
        SearchInputView.Listener,
        SearchInputView.ResetListener, SwipeToRefresh.OnRefreshListener {

    private static final String PRODUCT_ID_PARAM = "{product_id}";
    private static final String AD_ID_PARAM = "{ad_id}";
    private static final String USER_ID_USER_ID = "{user_id}";
    private static final String PRODUCT_ID_QUERY_PARAM = "?product_id=";
    private static final int ITEM_COUNT = 10;
    private static final int IMAGE_SPAN_COUNT = 2;
    private static final int SINGLE_SPAN_COUNT = 1;
    private static final int LOGIN_CODE = 13;


    private static final int TIME_DEBOUNCE_MILIS = 500;

    private RecyclerView rvExplore, rvAutoComplete;
    private GridLayoutManager layoutManager;
    private SwipeToRefresh swipeRefreshLayout;
    private ExploreSearchView searchView;
    private ExploreAdapter adapter;
    private ImageView ivBack, ivBantuan;
    private ExploreParams exploreParams;
    private EmptyModel emptyResultModel;
    private FrameLayout autoCompleteLayout;
    private AutoCompleteSearchAdapter autoCompleteAdapter;
    private FrameLayout layoutEmpty;

    private boolean isCanDoAction;

    @Inject
    UserSessionInterface userSession;

    @Inject
    ExploreContract.Presenter presenter;

    @Inject
    AffiliateAnalytics affiliateAnalytics;

    public static ExploreFragment getInstance(Bundle bundle) {
        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_explore, container, false);
        rvExplore = view.findViewById(R.id.rv_explore);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        searchView = view.findViewById(R.id.search_input_view);
        ivBack = view.findViewById(R.id.iv_back);
        ivBantuan = view.findViewById(R.id.action_bantuan);
        autoCompleteLayout = view.findViewById(R.id.layout_auto_complete);
        rvAutoComplete = view.findViewById(R.id.rv_search_auto_complete);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this), new ArrayList<>());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initView();
        initListener();
        exploreParams.setLoading(true);
        presenter.getFirstData(exploreParams, false);
    }

    private void initView() {
        layoutEmpty.setVisibility(View.GONE);
        dropKeyboard();
        initEmptyResultModel();
        autoCompleteLayout.setVisibility(View.GONE);
        exploreParams = new ExploreParams();
        swipeRefreshLayout.setOnRefreshListener(this);
        searchView.setListener(this);
        searchView.setDelayTextChanged(TIME_DEBOUNCE_MILIS);
        searchView.setResetListener(this);
        searchView.getSearchTextView().setOnClickListener(v -> {
            searchView.getSearchTextView().setCursorVisible(true);
        });

        layoutManager = new GridLayoutManager(getContext(),
                IMAGE_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter == null) {
                    return 0;
                }

                if (adapter.getData().get(position) instanceof ExploreViewModel) {
                    return SINGLE_SPAN_COUNT;
                }
                return IMAGE_SPAN_COUNT;
            }
        });
        rvExplore.setLayoutManager(layoutManager);
        rvExplore.addOnScrollListener(getScrollListener());

        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this), new ArrayList<>());
        rvExplore.setAdapter(adapter);
    }

    private void initEmptyResultModel() {
        emptyResultModel = new EmptyModel();
        emptyResultModel.setIconRes(R.drawable.ic_empty_search);
        emptyResultModel.setTitle(
                getActivity().getResources().getString(R.string.text_product_not_found)
        );
    }

    private void initListener() {
        ivBack.setOnClickListener(view -> getActivity().onBackPressed());
        ivBantuan.setOnClickListener(view ->
                RouteManager.route(
                        getContext(),
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, AffiliateConstant.FAQ_URL)
                )
        );
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent =
                ((BaseMainApplication) getActivity().getApplicationContext()).getBaseAppComponent();

        DaggerAffiliateComponent affiliateComponent =
                (DaggerAffiliateComponent) DaggerAffiliateComponent
                        .builder()
                        .baseAppComponent(baseAppComponent).build();

        DaggerExploreComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE;
    }

    @Override
    public void onRefresh() {
        exploreParams.setFirstData();
        exploreParams.setLoading(true);
        presenter.getFirstData(exploreParams, true);
    }

    private RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
                if (exploreParams.isCanLoadMore()
                        && !TextUtils.isEmpty(exploreParams.getCursor())
                        && totalItemCount <= lastVisibleItemPos + ITEM_COUNT) {
                    exploreParams.setCanLoadMore(false);
                    adapter.addElement(new LoadingMoreModel());
                    presenter.loadMoreData(exploreParams);
                }
            }
        };
    }

    @Override
    public void onSearchSubmitted(String text) {
        dropKeyboard();
        searchView.removeSearchTextWatcher();
        if (autoCompleteLayout.getVisibility() == View.VISIBLE)
            autoCompleteLayout.setVisibility(View.GONE);
        adapter.clearAllElements();
        exploreParams.setSearchParam(text);
        exploreParams.setLoading(true);
        presenter.getFirstData(exploreParams, false);
    }

    @Override
    public void onSearchTextChanged(String text) {
        onSearchTextModified(text, false);
    }

    private void onSearchTextModified(String text, boolean isFromAutoComplete) {
        if (TextUtils.isEmpty(text)) {
            onSearchReset();
        } else {
            autoCompleteLayout.setVisibility(View.VISIBLE);
            if (!isFromAutoComplete && !exploreParams.isLoading()) {
                affiliateAnalytics.onSearchSubmitted(text);
                presenter.getAutoComplete(text);
            }
        }
    }

    @Override
    public void onSearchReset() {
        if (autoCompleteLayout.getVisibility() == View.VISIBLE)
            autoCompleteLayout.setVisibility(View.GONE);
        dropKeyboard();
        searchView.removeSearchTextWatcher();
        exploreParams.resetSearch();
        exploreParams.setLoading(true);
        presenter.getFirstData(exploreParams, true);
    }

    @Override
    public AffiliateAnalytics getAffiliateAnalytics() {
        return affiliateAnalytics;
    }

    @Override
    public void showLoading() {
        adapter.addElement(new LoadingModel());
    }

    @Override
    public void hideLoading() {
        adapter.hideLoading();
    }

    @Override
    public void dropKeyboard() {
        searchView.getSearchTextView().setCursorVisible(false);
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onBymeClicked(ExploreViewModel model) {
        affiliateAnalytics.onByMeButtonClicked(model.getProductId());
        if (isCanDoAction) {
            isCanDoAction = false;
            if (userSession.isLoggedIn()) {
                presenter.checkIsAffiliate(model.getProductId(), model.getAdId());
            } else {
                startActivityForResult(
                        RouteManager.getIntent(
                                getContext(),
                                ApplinkConst.LOGIN
                        ),
                        LOGIN_CODE);
            }
        }
    }

    @Override
    public void onProductClicked(ExploreViewModel model) {
        affiliateAnalytics.onProductClicked(model.getProductId());
        if (isCanDoAction) {
            isCanDoAction = false;
            RouteManager.route(
                    getContext(),
                    ApplinkConst.AFFILIATE_PRODUCT.replace(PRODUCT_ID_PARAM, model.getProductId())
            );
        }
    }

    @Override
    public void onSuccessGetFirstData(List<Visitable> itemList, String cursor) {
        layoutEmpty.setVisibility(View.GONE);
        exploreParams.setLoading(false);
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        if (itemList.size() == 0) {
            itemList = new ArrayList<>();
            itemList.add(emptyResultModel);
            exploreParams.disableLoadMore();
            if (!TextUtils.isEmpty(exploreParams.getKeyword())) {
                affiliateAnalytics.onSearchNotFound(exploreParams.getKeyword());
            }
        } else {
            exploreParams.setCursorForLoadMore(cursor);
        }
        adapter.clearAllElements();
        adapter.addElement(itemList);
        if (autoCompleteLayout.getVisibility() == View.VISIBLE)
            autoCompleteLayout.setVisibility(View.GONE);
        searchView.addTextWatcherToSearch();
        presenter.unsubscribeAutoComplete();
    }

    @Override
    public void onErrorGetFirstData(String error) {
        layoutEmpty.setVisibility(View.VISIBLE);
        exploreParams.setLoading(false);
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        NetworkErrorHelper.showEmptyState(getActivity(),
                layoutEmpty,
                error,
                () -> {
                    layoutEmpty.setVisibility(View.GONE);
                    presenter.getFirstData(exploreParams, false);
                }
        );
    }

    @Override
    public void onSuccessGetMoreData(List<Visitable> itemList, String cursor) {
        adapter.hideLoading();
        adapter.addElement(itemList);
        if (TextUtils.isEmpty(cursor)) {
            exploreParams.disableLoadMore();
        } else {
            exploreParams.setCursorForLoadMore(cursor);
        }
        if (autoCompleteLayout.getVisibility() == View.VISIBLE)
            autoCompleteLayout.setVisibility(View.GONE);
    }

    @Override
    public void onErrorGetMoreData(String error) {
        showError(error, (view) -> presenter.loadMoreData(exploreParams));
    }

    @Override
    public void onButtonEmptySearchClicked() {
        adapter.clearAllElements();
        exploreParams.resetParams();
        searchView.getSearchTextView().setText("");
        searchView.getSearchTextView().setCursorVisible(false);
        exploreParams.setLoading(true);
        presenter.getFirstData(exploreParams, false);
    }

    @Override
    public void onEmptySearchResult() {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        adapter.clearAllElements();
        adapter.addElement(new ExploreEmptySearchViewModel());
        exploreParams.disableLoadMore();
    }

    @Override
    public void onSuccessCheckAffiliate(boolean isAffiliate, String productId, String adId) {
        if (isAffiliate) {
            presenter.checkAffiliateQuota(productId, adId);
        } else {
            String onboardingApplink = ApplinkConst.AFFILIATE_ONBOARDING
                    .concat(PRODUCT_ID_QUERY_PARAM)
                    .concat(productId);
            Intent intent = RouteManager.getIntent(getContext(), onboardingApplink);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onErrorCheckAffiliate(String error, String productId, String adId) {
        isCanDoAction = true;
    }

    @Override
    public void onSuccessCheckQuota(String productId, String adId) {
        RouteManager.route(
                getActivity(),
                ApplinkConst.AFFILIATE_CREATE_POST
                        .replace(PRODUCT_ID_PARAM, productId)
                        .replace(AD_ID_PARAM, adId)
        );
    }

    @Override
    public void onSuccessCheckQuotaButEmpty() {
        isCanDoAction = true;
        affiliateAnalytics.onJatahRekomendasiHabisDialogShow();
        Dialog dialog = buildDialog();
        dialog.setOnOkClickListener(view -> {
            RouteManager.route(
                    getActivity(),
                    ApplinkConst.PROFILE.replace(USER_ID_USER_ID, userSession.getUserId()));
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private Dialog buildDialog() {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(getActivity().getResources().getString(R.string.text_full_affiliate_title));
        dialog.setDesc(getActivity().getResources().getString(R.string.text_full_affiliate));
        dialog.setBtnOk(getActivity().getResources().getString(R.string.text_full_affiliate_ok));
        dialog.setBtnCancel(getActivity().getResources().getString(R.string
                .text_full_affiliate_no));
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onErrorCheckQuota(String error, String productId, String adId) {
        isCanDoAction = true;
        showError(error, (view) -> presenter.checkAffiliateQuota(productId, adId));
    }

    @Override
    public void onSuccessGetAutoComplete(List<AutoCompleteViewModel> modelList) {
        if (autoCompleteLayout.getVisibility() == View.GONE)
            autoCompleteLayout.setVisibility(View.VISIBLE);
        searchView.setDelayTextChanged(TIME_DEBOUNCE_MILIS);
        rvAutoComplete.setLayoutManager(new LinearLayoutManager(getActivity()));
        autoCompleteAdapter = new AutoCompleteSearchAdapter(this, modelList);
        rvAutoComplete.setAdapter(autoCompleteAdapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAutoCompleteItemClicked(String keyword) {
        clearAutoCompleteAdapter(keyword);
        onSearchSubmitted(keyword);
        autoCompleteLayout.setVisibility(View.GONE);
    }

    @Override
    public void onAutoCompleteIconClicked(String keyword) {
        clearAutoCompleteAdapter(keyword);
    }

    private void clearAutoCompleteAdapter(String keyword) {
        searchView.setDelayTextChanged(0);
        searchView.getSearchTextView().setText(keyword);
        onSearchTextModified(keyword, true);
        autoCompleteAdapter.clearAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        isCanDoAction = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void showError(String message) {
        showError(message, null);
    }

    private void showError(String message, View.OnClickListener listener) {
        ToasterError.make(getView(), message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show();
    }
}
