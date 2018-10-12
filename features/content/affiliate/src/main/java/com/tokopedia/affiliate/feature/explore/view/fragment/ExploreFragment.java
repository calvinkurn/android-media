package com.tokopedia.affiliate.feature.explore.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.feature.explore.di.DaggerExploreComponent;
import com.tokopedia.affiliate.feature.explore.view.adapter.ExploreAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactoryImpl;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.presenter.ExplorePresenter;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.user.session.UserSession;

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

    private static final String TERMS_AND_CONDITION_URL = "https://www.tokopedia.com/bantuan/pembeli/";
    private static final int ITEM_COUNT = 10;

    private RecyclerView rvExplore;
    private GridLayoutManager layoutManager;
    private SwipeToRefresh swipeRefreshLayout;
    private SearchInputView searchView;
    private ExploreAdapter adapter;
    private ImageView ivBack, ivBantuan;
    private ExploreParams exploreParams;
    private EmptyModel emptyResultModel;

    @Inject
    UserSession userSession;

    @Inject
    ExplorePresenter presenter;

    public static ExploreFragment getInstance(Bundle bundle) {
        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_explore, container, false);
        rvExplore = (RecyclerView) view.findViewById(R.id.rv_explore);
        swipeRefreshLayout = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        searchView = (SearchInputView) view.findViewById(R.id.search_input_view);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivBantuan = (ImageView) view.findViewById(R.id.action_bantuan);
        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this), new ArrayList<>());
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        initEmptyResultModel();
        exploreParams = new ExploreParams();
        swipeRefreshLayout.setOnRefreshListener(this);
        rvExplore.addOnScrollListener(getScrollListener());
        searchView.setListener(this);
        searchView.setResetListener(this);
        searchView.getSearchTextView().setOnClickListener(v -> {
            searchView.getSearchTextView().setCursorVisible(true);
        });
//        testData();
        presenter.getFirstData(exploreParams, false);
    }

    private void initEmptyResultModel() {
        emptyResultModel = new EmptyModel();
        emptyResultModel.setIconRes(R.drawable.ic_empty_search);
        emptyResultModel.setTitle(getActivity().getResources().getString(R.string.text_product_not_found));
    }

    private void initListener() {
        ivBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        ivBantuan.setOnClickListener(view -> {
            RouteManager.route(
                    getContext(),
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, TERMS_AND_CONDITION_URL)
            );
        });
    }

    @Override
    protected void initInjector() {

        DaggerAffiliateComponent affiliateComponent = (DaggerAffiliateComponent) DaggerAffiliateComponent.builder()
                .baseAppComponent(((BaseMainApplication)getActivity().getApplicationContext()).getBaseAppComponent()).build();

        DaggerExploreComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onRefresh() {
        exploreParams.setFirstData();
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
                        && totalItemCount <= lastVisibleItemPos + ITEM_COUNT){
                    exploreParams.setCanLoadMore(false);
                    adapter.addElement(new LoadingMoreModel());
                    presenter.loadMoreData(exploreParams);
                }
            }
        };
    }

    @Override
    public void onSearchSubmitted(String text) {
        adapter.clearAllElements();
        exploreParams.setSearchParam(text);
        presenter.getFirstData(exploreParams, false);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchReset() {
        exploreParams.resetSearch();
        presenter.getFirstData(exploreParams, true);
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
        presenter.checkAffiliateQuota(model.getProductId(), model.getAdId());
    }

    @Override
    public void onProductClicked(ExploreViewModel model) {
        //TODO Yoas : transition do applink to pdp
    }

    @Override
    public void onSuccessGetFirstData(List<Visitable> itemList, String cursor) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        adapter.clearAllElements();
        if (itemList.size() == 0) {
            layoutManager = new GridLayoutManager(getActivity(), 1);
            itemList = new ArrayList<>();
            itemList.add(emptyResultModel);
            exploreParams.disableLoadMore();
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
            exploreParams.setCursorForLoadMore(cursor);
        }
        rvExplore.setLayoutManager(layoutManager);
        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this), itemList);
        rvExplore.setAdapter(adapter);
    }

    @Override
    public void onErrorGetFirstData(String error) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        NetworkErrorHelper.showEmptyState(getActivity(),
                getView().getRootView(),
                error,
                () -> {
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
    }

    @Override
    public void onErrorGetMoreData(String error) {
        NetworkErrorHelper.createSnackbarWithAction(
                getActivity(),
                error,
                () -> {
                    presenter.loadMoreData(exploreParams);
                });
    }

    @Override
    public void onButtonEmptySearchClicked() {
        adapter.clearAllElements();
        exploreParams.resetParams();
        searchView.getSearchTextView().setText("");
        searchView.getSearchTextView().setCursorVisible(false);
        presenter.getFirstData(exploreParams, false);
    }

    @Override
    public void onEmptySearchResult() {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        adapter.clearAllElements();
        layoutManager = new GridLayoutManager(getActivity(), 1);
        adapter.addElement(new ExploreEmptySearchViewModel());
        exploreParams.disableLoadMore();
    }

    @Override
    public void onErrorNonAffiliateUser() {
        //TODO Yoas : need to be tested
        RouteManager.route(getActivity(), ApplinkConst.AFFILIATE_ONBOARDING);
    }

    @Override
    public void onSuccessCheckQuota(String productId, String adId) {
        //TODO Yoas : transition do add product, need to be tested
        RouteManager.route(
                getActivity(),
                ApplinkConst.AFFILIATE_CREATE_POST
                        .replace("{product_id}", productId)
                        .replace("{ad_id}", adId));
    }

    @Override
    public void onSuccessCheckQuotaButEmpty() {
          Dialog dialog = buildDialog();
          dialog.setOnOkClickListener(view ->{
              RouteManager.route(
                      getActivity(),
                      ApplinkConst.PROFILE.replace("{user_id}", userSession.getUserId()));
          });
          dialog.setOnCancelClickListener(view -> {
              dialog.dismiss();
          });
          dialog.show();
    }

    private Dialog buildDialog() {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(getActivity().getResources().getString(R.string.text_full_affiliate_title));
        dialog.setDesc(getActivity().getResources().getString(R.string.text_full_affiliate));
        dialog.setBtnOk(getActivity().getResources().getString(R.string.text_full_affiliate_ok));
        dialog.setBtnCancel(getActivity().getResources().getString(R.string.text_full_affiliate_no));
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onErrorCheckQuota(String error, String productId, String adId) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, () -> {
           presenter.checkAffiliateQuota(productId, adId);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void testData() {
        List<Visitable> itemList = new ArrayList<>();

        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi Tambah Esteh Manis Enak Rasanya Bung Sedap Nikmat Mntap",
                "Rp. 12.000",
                "1",
                "1"));
        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi",
                "Rp. 12.000",
                "1",
                "1"));
        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng Sapi Tambah Esteh Manis Enak Rasanya Bung Sedap Nikmat Mntap",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi",
                "Rp. 12.000",
                "1",
                "1"));
        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi Tambah Esteh Manis Enak Rasanya Bung Sedap Nikmat Mntap",
                "Rp. 12.000",
                "1",
                "1"));

        onSuccessGetFirstData(itemList,"");
    }
}
