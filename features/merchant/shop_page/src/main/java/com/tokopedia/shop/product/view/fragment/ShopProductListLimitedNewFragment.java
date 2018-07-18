package com.tokopedia.shop.product.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.analytic.ShopPageTrackingConstant;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.etalase.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductNewAdapter;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductNewViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductListView;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.newpresenter.ShopProductListNewPresenter;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedNewFragment extends BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements ShopProductListView, BaseEmptyViewHolder.Callback,
        ShopProductPromoViewHolder.PromoViewHolderListener, ShopProductClickedNewListener, ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener {

    private static final int REQUEST_CODE_USER_LOGIN = 100;
    private static final int REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101;
    private static final int REQUEST_CODE_ETALASE = 200;

    private static final int REQUEST_CODE_SORT = 300;
    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 2;

    private static final String SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION";

    public static final String SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list";
    public static final String SAVED_SELECTED_ETALASE_ID = "saved_etalase_id";
    public static final int ETALASE_TO_SHOW = 5;

    @Inject
    ShopProductListNewPresenter shopProductListNewPresenter;
    @Inject
    ShopPageTracking shopPageTracking;

    private ProgressDialog progressDialog;
    private String urlNeedTobBeProceed;
    private String attribution;
    private ShopInfo shopInfo;
    private ShopModuleRouter shopModuleRouter;
    private BottomActionView bottomActionView;
    //    private SearchInputView searchInputView;
//    private RecyclerView.ItemDecoration itemDecoration;
//    private LinearLayout linearHeaderSticky;
//    private LabelView etalaseButton;
    private String sortName = Integer.toString(Integer.MIN_VALUE);
    private RecyclerView recyclerView;
    //    protected boolean hideBottom = false;
//    protected boolean hideSearch = false;
//    protected int mTotalDyDistance;
//    private int stickyHeight;

    // selected is list, because we want to keep the history.
    private ArrayList<ShopEtalaseViewModel> selectedEtalaseList;
    // selected id of etalase, might not in selectedEtalaseList.
    private String selectedEtalaseId;

    private ShopProductNewAdapter shopProductNewAdapter;
    private GridLayoutManager gridLayoutManager;

    public static ShopProductListLimitedNewFragment createInstance(String shopAttribution) {
        ShopProductListLimitedNewFragment fragment = new ShopProductListLimitedNewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ATTRIBUTION, shopAttribution);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context.getApplicationContext() instanceof ShopModuleRouter) {
            shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_product_limited_list_new, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            selectedEtalaseList = new ArrayList<>();
            selectedEtalaseId = null;
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST);
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID);
        }
        super.onCreate(savedInstanceState);
        attribution = getArguments().getString(SHOP_ATTRIBUTION, "");
        shopProductListNewPresenter.attachView(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        bottomActionView = view.findViewById(R.id.bottom_action_view);
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopProductListLimitedNewFragment.this.startActivityForResult(ShopProductSortActivity.createIntent(getActivity(), sortName, shopInfo.getInfo().getShopId()), REQUEST_CODE_SORT);
            }
        });
//        searchInputView = view.findViewById(R.id.search_input_view);
//        searchInputView.setListener(this);
//        searchInputView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (shopInfo != null) {
//                    shopPageTracking.eventClickSearchProduct(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
//                            shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
//                }
//            }
//        });
//        etalaseButton = view.findViewById(R.id.label_view_etalase);
//        etalaseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (shopInfo != null) {
//                    shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), true, shopInfo.getInfo().getShopId(),
//                            shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
//                }
//                startActivityForResult(ShopEtalaseActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), null), REQUEST_CODE_ETALASE);
//            }
//        });
//        linearHeaderSticky = view.findViewById(R.id.linear_header_sticky);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        recyclerView = getRecyclerView(view);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0) {
                    return;
                }
                if (dy < 0) { // going up
                    //determine to remove view of etalase.
                } else { // going down
                    // determine to add view of etalase
                }
            }
        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0 && mTotalDyDistance < 0 || dy < 0 && mTotalDyDistance > 0) {
//                    mTotalDyDistance = 0;
//                }
//                mTotalDyDistance += dy;
//                if (!hideBottom && mTotalDyDistance > bottomActionView.getHeight()) {
//                    moveView(bottomActionView, 0, bottomActionView.getHeight());
//                    hideBottom = true;
//                } else if (hideBottom && mTotalDyDistance < -bottomActionView.getHeight()) {
//                    moveView(bottomActionView, bottomActionView.getHeight(), 0);
//                    hideBottom = false;
//                }
//                if (!hideSearch && mTotalDyDistance > stickyHeight) {
//                    moveView(linearHeaderSticky, 0, -stickyHeight);
//                    hideSearch = true;
//                } else if (hideSearch && mTotalDyDistance < -stickyHeight) {
//                    moveView(linearHeaderSticky, -stickyHeight, 0);
//                    hideSearch = false;
//                }
//            }
//        });
//        itemDecoration = new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                int position = parent.getChildAdapterPosition(view);
//                if (position == 0) {
//                    outRect.top = stickyHeight;
//                }
//            }
//        };

        super.onViewCreated(view, savedInstanceState);

        if (shopInfo != null) {
            loadInitialData();
        }
    }

    public void displayProduct(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
//        mTotalDyDistance = 0;
        loadInitialData();
    }

    // load data promo/featured/etalase
    protected void loadInitialData() {
        loadTopData();
        reloadProductData();
    }

    // load product list first time
    private void reloadProductData(){
        isLoadingInitialData = true;
        shopProductNewAdapter.clearProductList();
        showLoading();
        loadData(getDefaultInitialPage());
    }

    protected void loadTopData(){
        if (shopInfo!= null) {
            shopProductNewAdapter.clearDataExceptProduct();

            String officialWebViewUrl = shopInfo.getInfo().getShopOfficialTop();
            officialWebViewUrl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? officialWebViewUrl : "";
            shopProductListNewPresenter.loadProductPromoModel(officialWebViewUrl);

            shopProductListNewPresenter.getProductFeatureListWithAttributes(
                    shopInfo.getInfo().getShopId(),
                    shopInfo.getInfo().isShopOfficial());

            shopProductListNewPresenter.getShopEtalase(shopInfo.getInfo().getShopId(), selectedEtalaseList, ETALASE_TO_SHOW);
        }
    }

    @Override
    public void loadData(int page) {
        if (shopInfo != null) {
            shopProductListNewPresenter.getProductListWithAttributes(
                    shopInfo.getInfo().getShopId(),
                    TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsGold()),
                    TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial()),
                    page,
                    !shopInfo.getInfo().isOpen(),
                    ShopPageTrackingConstant.DEFAULT_PER_PAGE,
                    selectedEtalaseId);
        }
    }

    @Override
    public void renderShopProductPromo(ShopProductPromoViewModel shopProductPromoViewModel) {
        shopProductNewAdapter.setShopProductPromoViewModel(shopProductPromoViewModel);
    }

    //    public void moveView(final View view, int start, int end) {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", start, end);
//        animator.setDuration(ANIMATION_DURATION);
//        animator.start();
//    }


    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        gridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (shopProductNewAdapter.getItemViewType(position) == ShopProductNewViewHolder.LAYOUT) {
                    return LIST_SPAN_COUNT;
                } else {
                    return GRID_SPAN_COUNT;
                }
            }
        });
        return gridLayoutManager;
    }

    // load data after get shop info
    @Override
    protected boolean callInitialLoadAutomatically() {
        return false;
    }

    @Override
    protected EndlessRecyclerViewScrollListener createEndlessRecyclerViewListener() {
        return new DataEndlessScrollListener(recyclerView.getLayoutManager(), shopProductNewAdapter) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showLoading();
                loadData(page);
            }
        };
    }

    @NonNull
    @Override
    protected ShopProductAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory(this,
                this, this, this, false);
    }

    @NonNull
    @Override
    protected BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> createAdapterInstance() {
        shopProductNewAdapter = new ShopProductNewAdapter(getAdapterTypeFactory());
        return shopProductNewAdapter;
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_empty_list_product);
        if (shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId())) {
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

//    @Override
//    public void setUserVisibleHint(boolean visibleToUser) {
//        super.setUserVisibleHint(visibleToUser);
//        shopProductNewAdapter.updateVisibleStatus(visibleToUser);
//    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductListNewPresenter != null) {
            shopProductListNewPresenter.detachView();
        }
    }

    //TODO tracking impression for feature and productlist
//    private void trackingImpressionFeatureProduct(List<BaseShopProductViewModel> list) {
//        List<BaseShopProductViewModel> featuredViewModelList = new ArrayList<>();
//        List<BaseShopProductViewModel> productHomeViewModelList = new ArrayList<>();
//        for (BaseShopProductViewModel baseShopProductViewModel : list) {
//            if (baseShopProductViewModel instanceof ShopProductFeaturedViewModel) {
//                if (shopInfo != null) {
//                    featuredViewModelList.add((ShopProductFeaturedViewModel) baseShopProductViewModel);
//                }
//            } else if (baseShopProductViewModel instanceof ShopProductHomeViewModelOld) {
//                if (shopInfo != null) {
//                    productHomeViewModelList.add((ShopProductHomeViewModelOld) shopProductBaseViewModel);
//                }
//            } else if (baseShopProductViewModel instanceof ShopProductPromoViewModel) {
//                if (shopInfo != null) {
//                    shopPageTracking.eventViewBannerImpression(getString(R.string.shop_info_title_tab_product),
//                            shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopId(), shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()),
//                            ShopPageTracking.getShopType(shopInfo.getInfo()));
//                }
//            }
//        }
//        if (featuredViewModelList.size() > 0) {
//            shopPageTracking.eventViewProductFeaturedImpression(getString(R.string.shop_info_title_tab_product),
//                    featuredViewModelList, attribution,
//                    shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()), false);
//        }
//        if (productHomeViewModelList.size() > 0) {
//            shopPageTracking.eventViewProductImpression(getString(R.string.shop_info_title_tab_product),
//                    productHomeViewModelList, attribution,
//                    true, shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()),
//                    ShopPageTracking.getShopType(shopInfo.getInfo()),
//                    false);
//        }
//    }

    @Override
    public void onItemClicked(BaseShopProductViewModel baseShopProductViewModel) {

    }


    @Override
    public void onErrorRemoveFromWishList(Throwable e) {

    }

    @Override
    public void onSuccessRemoveFromWishList(String productId, Boolean value) {
        shopProductNewAdapter.updateWishListStatus(productId, false);
    }

    @Override
    public void onErrorAddToWishList(Throwable e) {
        if (!shopProductListNewPresenter.isLogin()) {
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
            return;
        }
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onSuccessAddToWishList(String productId, Boolean value) {
        shopProductNewAdapter.updateWishListStatus(productId, true);

        //TEST
        startActivityForResult(ShopEtalaseActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), null), REQUEST_CODE_ETALASE);
    }

    @Override
    public void hideLoadingDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage) {
        // TODO trackingImpressionFeatureProduct(list);
        hideLoading();
        if (isLoadingInitialData) {
            shopProductNewAdapter.clearProductList();
            endlessRecyclerViewScrollListener.resetState();
        } else {
            shopProductNewAdapter.clearAllNonDataElement();
        }
        shopProductNewAdapter.addProductList(list);
        updateScrollListenerState(hasNextPage);

        if (shopProductNewAdapter.getShopProductViewModelList().size() == 0) {
            shopProductNewAdapter.addElement(getEmptyDataViewModel());
        } else {
            isLoadingInitialData = false;
        }
        shopProductNewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetProductFeature(Throwable e) {
        //TODO on get product feature error
        shopProductNewAdapter.setShopProductFeaturedViewModel(null);
    }

    @Override
    public void onSuccessGetProductFeature(@NonNull List<ShopProductViewModel> list) {
        shopProductNewAdapter.setShopProductFeaturedViewModel(new ShopProductFeaturedViewModel(list));
    }

    @Override
    public void showGetListError(Throwable throwable) {
        hideLoading();
        updateStateScrollListener();
        if (shopProductNewAdapter.getShopProductViewModelList().size() > 0) {
            onGetListErrorWithExistingData(throwable);
        } else {
            onGetListErrorWithEmptyData(throwable);
        }
    }

    @Override
    public void onErrorGetEtalaseList(Throwable e) {
        //TODO error get etalase, will show all product instead.
        Log.i("Test", "Test");
    }

    @Override
    public void onSuccessGetEtalaseList(List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        //TODO success get etalase, show all etalase list
        shopProductNewAdapter.setShopEtalase(new ShopProductEtalaseListViewModel(shopEtalaseViewModelList, selectedEtalaseId));
    }

    @Override
    public void onEmptyContentItemTextClicked() {

    }

    @Override
    public void onEmptyButtonClicked() {
        ((ShopModuleRouter) getActivity().getApplication()).goToAddProduct(getActivity());
    }

    @Override
    public void promoClicked(String url) {
        if (shopInfo != null) {
            shopPageTracking.eventClickBannerImpression(getString(R.string.shop_info_title_tab_product),
                    shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopId(), shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        boolean urlProceed = ShopProductOfficialStoreUtils.proceedUrl(getActivity(), url, shopInfo.getInfo().getShopId(),
                shopProductListNewPresenter.isLogin(),
                shopProductListNewPresenter.getDeviceId(),
                shopProductListNewPresenter.getUserId());
        // Need to login
        if (!urlProceed) {
            urlNeedTobBeProceed = url;
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW);
        }
    }

    @Override
    public void onEtalaseChipClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        selectedEtalaseId = shopEtalaseViewModel.getEtalaseId();
        shopProductNewAdapter.setSelectedEtalaseId(selectedEtalaseId);
        // no need ro rearraged, just notify the adapter to reload product list by etalase id
        reloadProductData();
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel) {
        if (shopInfo != null) {
            shopPageTracking.eventClickWishlistShop(getString(R.string.shop_info_title_tab_product), shopProductViewModel.isWishList(),
                    true, shopProductViewModel.getId(),
                    shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        if (shopProductViewModel.isWishList()) {
            shopProductListNewPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductListNewPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel) {
        if (shopInfo != null) {
            shopPageTracking.eventClickProductImpression(getString(R.string.shop_info_title_tab_product),
                    shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(),
                    attribution, shopProductViewModel.getPositionTracking(), true,
                    shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()), false);
        }
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getId(), shopProductViewModel.getName(),
                shopProductViewModel.getDisplayedPrice(), shopProductViewModel.getImageUrl(), attribution,
                shopPageTracking.getListNameOfProduct(shopProductViewModel.getPositionTracking(), false, ShopPageTrackingConstant.PRODUCT_ETALASE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK && shopPageTracking != null && shopProductListNewPresenter != null && shopInfo != null) {
                    String etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    String etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);
                    shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                            true, etalaseName, shopInfo.getInfo().getShopId(),
                            shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
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
    public void onDestroyView() {
        super.onDestroyView();
        if (shopProductListNewPresenter != null) {
            shopProductListNewPresenter.detachView();
        }
    }

    public void onLastItemVisibleTracking() {
        if (shopInfo != null) {
            shopPageTracking.eventViewBottomNavigation(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
                    shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_SELECTED_ETALASE_LIST, selectedEtalaseList);
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId);
    }


}