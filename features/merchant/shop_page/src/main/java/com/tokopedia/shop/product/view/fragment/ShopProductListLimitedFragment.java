package com.tokopedia.shop.product.view.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.analytic.ShopPageTrackingConstant;
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
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListLimitedPresenter;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;

import java.util.ArrayList;
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

    private static final int REQUEST_CODE_SORT = 300;
    private static final int ANIMATION_DURATION = 400;
    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 2;

    private static final String SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION";

    @Inject
    ShopProductListLimitedPresenter shopProductListLimitedPresenter;
    @Inject
    ShopPageTracking shopPageTracking;
    private ProgressDialog progressDialog;
    private String urlNeedTobBeProceed;
    private String attribution;
    private ShopInfo shopInfo;
    private ShopModuleRouter shopModuleRouter;
    private ShopPagePromoWebView.Listener promoWebViewListener;
    private BottomActionView bottomActionView;
    private SearchInputView searchInputView;
    private RecyclerView.ItemDecoration itemDecoration;
    private LinearLayout linearHeaderSticky;
    private LabelView etalaseButton;
    private String sortName = Integer.toString(Integer.MIN_VALUE);
    private RecyclerView recyclerView;
    protected boolean hideBottom = false;
    protected boolean hideSearch = false;
    protected int mTotalDyDistance;
    private int stickyHeight;

    public static ShopProductListLimitedFragment createInstance(String shopAttribution) {
        ShopProductListLimitedFragment fragment = new ShopProductListLimitedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ATTRIBUTION, shopAttribution);
        fragment.setArguments(bundle);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_product_limited_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attribution = getArguments().getString(SHOP_ATTRIBUTION, "");
        shopProductListLimitedPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomActionView = view.findViewById(R.id.bottom_action_view);
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopProductListLimitedFragment.this.startActivityForResult(ShopProductSortActivity.createIntent(getActivity(), sortName, shopInfo.getInfo().getShopId()), REQUEST_CODE_SORT);
            }
        });
        searchInputView = view.findViewById(R.id.search_input_view);
        searchInputView.setListener(this);
        searchInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    shopPageTracking.eventClickSearchProduct(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
                            shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
            }
        });
        etalaseButton = view.findViewById(R.id.label_view_etalase);
        etalaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), true, shopInfo.getInfo().getShopId(),
                            shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
                startActivityForResult(ShopEtalaseActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), null), REQUEST_CODE_ETALASE);
            }
        });
        linearHeaderSticky = view.findViewById(R.id.linear_header_sticky);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        recyclerView = getRecyclerView(view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mTotalDyDistance < 0 || dy < 0 && mTotalDyDistance > 0) {
                    mTotalDyDistance = 0;
                }
                mTotalDyDistance += dy;
                if (!hideBottom && mTotalDyDistance > bottomActionView.getHeight()) {
                    moveView(bottomActionView, 0, bottomActionView.getHeight());
                    hideBottom = true;
                } else if (hideBottom && mTotalDyDistance < -bottomActionView.getHeight()) {
                    moveView(bottomActionView, bottomActionView.getHeight(), 0);
                    hideBottom = false;
                }
                if (!hideSearch && mTotalDyDistance > stickyHeight) {
                    moveView(linearHeaderSticky, 0, -stickyHeight);
                    hideSearch = true;
                } else if (hideSearch && mTotalDyDistance < -stickyHeight) {
                    moveView(linearHeaderSticky, -stickyHeight, 0);
                    hideSearch = false;
                }
            }
        });
        itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.top = stickyHeight;
                }
            }
        };
    }

    public void moveView(final View view, int start, int end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", start, end);
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }


    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (getAdapter().getItemViewType(position) == ShopProductViewHolder.LAYOUT) {
                    return LIST_SPAN_COUNT;
                } else {
                    return GRID_SPAN_COUNT;
                }
            }
        });
        return gridLayoutManager;
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
                    officialWebViewUrl,
                    i);
        }
    }

    @NonNull
    @Override
    protected ShopProductLimitedAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductLimitedAdapterTypeFactory(this, this, this, promoWebViewListener, this);
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
        mTotalDyDistance = 0;
        loadInitialData();
    }

    public void resetRecyclerview() {
        recyclerView.removeItemDecoration(itemDecoration);
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
        startActivity(ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), text, "", attribution));
    }

    @Override
    public void onSearchTextChanged(String text) {
        // Do nothing
    }

    @Override
    public void renderList(@NonNull List<ShopProductBaseViewModel> list, boolean hasNextPage, boolean hasProduct) {
        renderList(list, hasNextPage);
        if (hasProduct) {
            initialProductAttributes();
        }
    }

    @Override
    public void renderList(@NonNull List<ShopProductBaseViewModel> list, boolean hasNextPage) {
        trackingImpressionFeatureProduct(list);
        super.renderList(list, hasNextPage);
    }

    private void initialProductAttributes() {
        linearHeaderSticky.setVisibility(View.VISIBLE);
        bottomActionView.setVisibility(View.VISIBLE);
        linearHeaderSticky.post(new Runnable() {
            @Override
            public void run() {
                stickyHeight = linearHeaderSticky.getHeight();
                recyclerView.addItemDecoration(itemDecoration, 0);
            }
        });
    }

    private void trackingImpressionFeatureProduct(List<ShopProductBaseViewModel> list) {
        List<ShopProductViewModel> featuredViewModelList = new ArrayList<>();
        List<ShopProductViewModel> productHomeViewModelList = new ArrayList<>();
        for (ShopProductBaseViewModel shopProductBaseViewModel : list) {
            if (shopProductBaseViewModel instanceof ShopProductLimitedFeaturedViewModel) {
                if (shopInfo != null) {
                    featuredViewModelList.add((ShopProductLimitedFeaturedViewModel) shopProductBaseViewModel);
                }
            } else if (shopProductBaseViewModel instanceof ShopProductHomeViewModel) {
                if (shopInfo != null) {
                    productHomeViewModelList.add((ShopProductHomeViewModel) shopProductBaseViewModel);
                }
            } else if (shopProductBaseViewModel instanceof ShopProductLimitedPromoViewModel) {
                if (shopInfo != null) {
                    shopPageTracking.eventViewBannerImpression(getString(R.string.shop_info_title_tab_product),
                            shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopId(), shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                            ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
            }
        }
        if (featuredViewModelList.size() > 0) {
            shopPageTracking.eventViewProductFeaturedImpression(getString(R.string.shop_info_title_tab_product),
                    featuredViewModelList, attribution, 
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()), false);
        }
        if (productHomeViewModelList.size() > 0) {
            shopPageTracking.eventViewProductImpression(getString(R.string.shop_info_title_tab_product),
                    productHomeViewModelList, attribution,
                    true, shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()),
                    false, getCurrentPage());
        }
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel) {
        if (shopInfo != null) {
            shopPageTracking.eventClickWishlistShop(getString(R.string.shop_info_title_tab_product), shopProductViewModel.isWishList(),
                    true, shopProductViewModel.getId(),
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
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
                    shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(),
                    attribution, adapterPosition, true,
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()), false);
        }
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getId(), shopProductViewModel.getName(),
                shopProductViewModel.getDisplayedPrice(), shopProductViewModel.getImageUrl(), attribution,
                shopPageTracking.getListNameOfProduct(adapterPosition, false, ShopPageTrackingConstant.PRODUCT_ETALASE));
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
                    startActivity(ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), "", etalaseId, attribution));
                }
                break;
            case REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW:
                if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(urlNeedTobBeProceed)) {
                    promoClicked(urlNeedTobBeProceed);
                }
                break;
            case REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME);
                    startActivity(ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), "", "", "", sortName));
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
    public void onProductFeaturedClickedTracking(ShopProductViewModel shopProductViewModel, int adapterPosition) {
        if (shopInfo != null) {
            shopPageTracking.eventClickProductFeaturedImpression(getString(R.string.shop_info_title_tab_product),
                    shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(), attribution, adapterPosition, true,
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()), false);
        }
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getId(), shopProductViewModel.getName(),
                shopProductViewModel.getDisplayedPrice(), shopProductViewModel.getImageUrl(),
                attribution, shopPageTracking.getListNameOfProduct(adapterPosition, false, ShopPageTrackingConstant.PRODUCT_FEATURED));
    }

    public void onLastItemVisibleTracking() {
        if (shopInfo != null) {
            shopPageTracking.eventViewBottomNavigation(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
                    shopProductListLimitedPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
    }
}