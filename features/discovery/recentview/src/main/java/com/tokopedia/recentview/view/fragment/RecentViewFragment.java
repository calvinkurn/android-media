package com.tokopedia.recentview.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.recentview.R;
import com.tokopedia.recentview.analytics.RecentViewTracking;
import com.tokopedia.recentview.di.DaggerRecentViewComponent;
import com.tokopedia.recentview.view.adapter.RecentViewDetailAdapter;
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory;
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactoryImpl;
import com.tokopedia.recentview.view.listener.RecentView;
import com.tokopedia.recentview.view.presenter.RecentViewPresenter;
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel;
import com.tokopedia.recentview.view.viewmodel.RecentViewProductViewModel;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.design.utils.CurrencyFormatHelper.convertRupiahToInt;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewFragment extends BaseDaggerFragment
        implements RecentView.View, WishListActionListener {

    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

    private RecyclerView recyclerView;
    private RecentViewDetailAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Inject
    RecentViewPresenter presenter;

    public static RecentViewFragment createInstance(Bundle bundle) {
        RecentViewFragment fragment = new RecentViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    protected void initInjector() {
        DaggerRecentViewComponent.builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()
                )
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(savedInstanceState);
    }

    private void initVar(Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        RecentViewTypeFactory typeFactory = new RecentViewTypeFactoryImpl(this);
        adapter = new RecentViewDetailAdapter(typeFactory);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_recent_view_detail, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.list);
        prepareView();
        presenter.attachView(this, this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getRecentViewProduct();

    }

    @Override
    public void onWishlistClicked(int adapterPosition, Integer productId, boolean isWishlist) {
        if (!isWishlist) {
            presenter.addToWishlist(adapterPosition, String.valueOf(productId));
        } else {
            presenter.removeFromWishlist(adapterPosition, String.valueOf(productId));
        }
    }

    @Override
    public void onGoToProductDetail(String productId,
                                    String productName,
                                    String  productPrice,
                                    String productImage) {
        getActivity().startActivity(getProductIntent(productId));
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
    }

    @Override
    public void showLoadingProgress() {

    }

    @Override
    public void onErrorGetRecentView(String errorMessage) {
        adapter.dismissLoading();
        if (getActivity() != null && getView() != null && presenter != null)
            NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                    errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getRecentViewProduct();
                        }
                    });
    }

    @Override
    public void onSuccessGetRecentView(ArrayList<Visitable> recentViewProductViewModels) {
        adapter.dismissLoading();
        adapter.addList(recentViewProductViewModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEmptyGetRecentView() {
        adapter.dismissLoading();
        adapter.showEmpty();
    }

    @Override
    public void sendRecentViewClickTracking(RecentViewDetailProductViewModel element) {
        RecentViewTracking.trackEventClickOnProductRecentView(getActivity(),
                element.getRecentViewAsObjectDataLayerForClick()
                );
    }

    @Override
    public void sendRecentViewImpressionTracking(List<RecentViewDetailProductViewModel> recentViewModel) {
        RecentViewTracking.trackEventImpressionOnProductRecentView(getActivity(),
                getRecentViewAsDataLayerForImpression(recentViewModel));
    }

    public List<Object> getRecentViewAsDataLayerForImpression(List<RecentViewDetailProductViewModel> recentViewModel) {
        List<Object> objects = new ArrayList<>();
        for(RecentViewDetailProductViewModel model : recentViewModel){
            objects.add(DataLayer.mapOf(
                    "name", model.getName(),
                    "id", model.getProductId(),
                    "price", Integer.toString(convertRupiahToInt(String.valueOf(model.getPrice()))),
                    "list", "/recent",
                    "brand", DEFAULT_VALUE_NONE_OTHER,
                    "category", "",
                    "position", String.valueOf(model.getPositionForRecentViewTracking())
            ));
        }
        return objects;
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productID) {
        dismissLoadingProgress();

        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i) instanceof RecentViewProductViewModel) {
                RecentViewProductViewModel feedDetailViewModel
                        = ((RecentViewProductViewModel) adapter.getList().get(i));
                if (productID.equals(String.valueOf(feedDetailViewModel.getId()))) {
                    feedDetailViewModel.setWishlist(true);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_success_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productID) {
        dismissLoadingProgress();

        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i) instanceof RecentViewProductViewModel) {
                RecentViewProductViewModel feedDetailViewModel
                        = ((RecentViewProductViewModel) adapter.getList().get(i));
                if (productID.equals(String.valueOf(feedDetailViewModel.getId()))) {
                    feedDetailViewModel.setWishlist(true);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_success_remove_wishlist));
    }

    @Override
    public void dismissLoadingProgress() {

    }
}
