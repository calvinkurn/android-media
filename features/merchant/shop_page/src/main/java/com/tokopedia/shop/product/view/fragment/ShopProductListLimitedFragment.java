package com.tokopedia.shop.product.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.etalase.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedPromoViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListLimitedPresenter;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedFragment extends BaseListFragment<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory>
        implements SearchInputView.Listener, ShopProductLimitedPromoViewHolder.PromoViewHolderListener,
        ShopProductListLimitedView, ShopProductClickedListener, EmptyViewHolder.Callback, ShopProductFeaturedViewHolder.ShopProductFeaturedListener {

    private static final int REQUEST_CODE_USER_LOGIN = 100;
    private static final int REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101;
    private static final int REQUEST_CODE_ETALASE = 200;
    @Inject
    ShopProductListLimitedPresenter shopProductListLimitedPresenter;
    @Inject
    ShopPageTracking shopPageTracking;
    private ProgressDialog progressDialog;
    private String urlNeedTobBeProceed;
    private ShopInfo shopInfo;
    private ShopModuleRouter shopModuleRouter;
    private ShopPagePromoWebView.Listener promoWebViewListener;

    public static ShopProductListLimitedFragment createInstance() {
        ShopProductListLimitedFragment fragment = new ShopProductListLimitedFragment();
        return fragment;
    }

    public void setPromoWebViewListener(ShopPagePromoWebView.Listener promoWebViewListener) {
        this.promoWebViewListener = promoWebViewListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context.getApplicationContext() instanceof ShopModuleRouter) {
            shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
            promoWebViewListener = (ShopPagePromoWebView.Listener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopProductListLimitedPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        VerticalRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.clearItemDecoration();
        getRecyclerView(view).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                if (lastVisibleItem >= recyclerView.getLayoutManager().getItemCount()) {
                    onLastItemVisibleTracking();
                }
            }
        });
    }

    @Override
    public void loadData(int i) {
        if (shopInfo != null) {
            String officialWebViewUrl = shopInfo.getInfo().getShopOfficialTop();
            officialWebViewUrl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? officialWebViewUrl : "";
            shopProductListLimitedPresenter.getProductLimitedList(
                    shopInfo.getInfo().getShopId(),
                    TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsGold()),
                    TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial()),
                    officialWebViewUrl);
        }
    }

    @NonNull
    @Override
    protected ShopProductLimitedAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductLimitedAdapterTypeFactory(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    shopPageTracking.eventClickSearchProduct(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
                            shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
            }
        }, this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopPageTracking.eventClickSeeMoreProduct(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
                        shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
                startActivity(ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId()));
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), true, shopInfo.getInfo().getShopId(),
                            shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
                startActivityForResult(ShopEtalaseActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), null), REQUEST_CODE_ETALASE);
            }
        }, this, this, promoWebViewListener, this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory> createAdapterInstance() {
        return new ShopProductLimitedAdapter(getAdapterTypeFactory());
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_empty_list_product);
        if (shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId())) {
            emptyModel.setTitle(getString(R.string.shop_product_limited_empty_product_title_owner));
            emptyModel.setContent(getString(R.string.shop_product_limited_empty_product_content_owner));
            emptyModel.setButtonTitle(getString(R.string.shop_page_label_add_product));
        } else {
            emptyModel.setContent(getString(R.string.shop_product_limited_empty_product_title));
        }
        return emptyModel;
    }

    @Override
    protected void initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(new ShopProductModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    public void displayProduct(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        loadInitialData();
    }

    @Override
    public void setUserVisibleHint(boolean visibleToUser) {
        super.setUserVisibleHint(visibleToUser);
        if (getAdapter() != null) {
            ((ShopProductLimitedAdapter) getAdapter()).updateVisibleStatus(visibleToUser);
        }
    }

    @Override
    public void onEmptyContentItemTextClicked() {

    }

    @Override
    public void onEmptyButtonClicked() {
        ((ShopModuleRouter) getActivity().getApplication()).goToAddProduct(getActivity());
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductListLimitedPresenter != null) {
            shopProductListLimitedPresenter.detachView();
        }
    }

    @Override
    public void onItemClicked(ShopProductBaseViewModel shopProductBaseViewModel) {

    }

    @Override
    public void promoClicked(String url) {
        if (shopInfo != null) {
            shopPageTracking.eventClickBannerImpression(getString(R.string.shop_info_title_tab_product),
                    shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopId(), shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        boolean urlProceed = ShopProductOfficialStoreUtils.proceedUrl(getActivity(), url, shopInfo.getInfo().getShopId(),
                shopProductListLimitedPresenter.isLogin(),
                shopProductListLimitedPresenter.getDeviceId(),
                shopProductListLimitedPresenter.getUserId());
        // Need to login
        if (!urlProceed) {
            urlNeedTobBeProceed = url;
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW);
        }
    }

    @Override
    public void onSearchSubmitted(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (shopInfo != null) {
            shopPageTracking.eventTypeKeywordSearchProduct(getString(R.string.shop_info_title_tab_product), text, shopInfo.getInfo().getShopId(),
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        startActivity(ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), text, ""));
    }

    @Override
    public void onSearchTextChanged(String text) {
        // Do nothing
    }

    @Override
    public void renderList(@NonNull List<ShopProductBaseViewModel> list) {
        super.renderList(list);
        trackingImpressionFeatureProduct(list);
    }

    private void trackingImpressionFeatureProduct(List<ShopProductBaseViewModel> list) {
        for (ShopProductBaseViewModel shopProductBaseViewModel : list) {
            if (shopProductBaseViewModel instanceof ShopProductLimitedFeaturedViewModel) {
                if (shopInfo != null) {
                    shopPageTracking.eventViewProductFeaturedImpression(getString(R.string.shop_info_title_tab_product),
                            ((ShopProductLimitedFeaturedViewModel) shopProductBaseViewModel).getShopProductViewModelList(),
                            shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()), false);
                }
            } else if (shopProductBaseViewModel instanceof ShopProductLimitedProductViewModel) {
                if (shopInfo != null) {
                    shopPageTracking.eventViewProductImpression(getString(R.string.shop_info_title_tab_product),
                            ((ShopProductLimitedProductViewModel) shopProductBaseViewModel).getShopProductViewModelList(),
                            true, shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()),
                            false);
                }
            } else if (shopProductBaseViewModel instanceof ShopProductLimitedPromoViewModel) {
                if (shopInfo != null) {
                    shopPageTracking.eventViewBannerImpression(getString(R.string.shop_info_title_tab_product),
                            shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopId(), shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                            ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
            }
        }
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel) {
        if (shopInfo != null) {
            shopPageTracking.eventClickWishlistShop(getString(R.string.shop_info_title_tab_product), shopProductViewModel.isWishList(),
                    true, shopProductViewModel.getId(),
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        if (shopProductViewModel.isWishList()) {
            shopProductListLimitedPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductListLimitedPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel, int adapterPosition) {
        if (shopInfo != null) {
            shopPageTracking.eventClickProductImpression(getString(R.string.shop_info_title_tab_product),
                    shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(), adapterPosition, true,
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()), false);
        }
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getProductUrl());
    }

    @Override
    public void onErrorRemoveFromWishList(Throwable e) {
        if (e instanceof UserNotLoginException) {
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
            return;
        }
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onSuccessRemoveFromWishList(String productId, Boolean value) {
        ((ShopProductLimitedAdapter) getAdapter()).updateWishListStatus(productId, false);
    }

    @Override
    public void onErrorAddToWishList(Throwable e) {
        if (e instanceof UserNotLoginException) {
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
            return;
        }
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onSuccessAddToWishList(String productId, Boolean value) {
        ((ShopProductLimitedAdapter) getAdapter()).updateWishListStatus(productId, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK && shopPageTracking != null && shopProductListLimitedPresenter != null && shopInfo != null) {
                    String etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    String etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);
                    shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                            true, etalaseName, shopInfo.getInfo().getShopId(),
                            shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
                    startActivity(ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), "", etalaseId));
                }
                break;
            case REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW:
                if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(urlNeedTobBeProceed)) {
                    promoClicked(urlNeedTobBeProceed);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (shopProductListLimitedPresenter != null) {
            shopProductListLimitedPresenter.detachView();
        }
    }

    @Override
    public void onFeatureWishlistClickedTracking(ShopProductViewModel shopProductViewModel) {
        if (shopInfo != null) {
            shopPageTracking.eventClickWishlistShopPageFeatured(getString(R.string.shop_info_title_tab_product),
                    shopProductViewModel.isWishList(), shopProductViewModel.getId(), shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
    }

    @Override
    public void onProductImageFeaturedClickedTracking(ShopProductViewModel shopProductViewModel, int adapterPosition) {
        if (shopInfo != null) {
            shopPageTracking.eventClickProductPictureFeaturedImpression(getString(R.string.shop_info_title_tab_product), shopProductViewModel.getName(),
                    shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(), adapterPosition,
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
    }

    @Override
    public void onProductTitleFeaturedClickedTracking(ShopProductViewModel shopProductViewModel, int adapterPosition) {
        if (shopInfo != null) {
            shopPageTracking.eventClickProductTitleFeaturedImpression(getString(R.string.shop_info_title_tab_product), shopProductViewModel.getName(),
                    shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(), adapterPosition,
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
    }

    public void onLastItemVisibleTracking() {
        if (shopInfo != null) {
            shopPageTracking.eventViewBottomNavigation(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
    }
}