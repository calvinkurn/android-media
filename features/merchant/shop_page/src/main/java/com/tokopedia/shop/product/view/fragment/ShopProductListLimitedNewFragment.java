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
import com.tokopedia.shop.common.constant.ShopPageConstant;
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
    public static final String SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name";
    public static final String SAVED_Y_DISTANCE = "saved_y_distance";

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
    //    protected boolean hideSearch = false;
    protected int totalYDistance;

    // selected is list, because we want to keep the history.
    private ArrayList<ShopEtalaseViewModel> selectedEtalaseList;
    // selected id of etalase, might not in selectedEtalaseList.
    private String selectedEtalaseId;
    private String selectedEtalaseName;

    private ShopProductNewAdapter shopProductNewAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean needReloadData;

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
            selectedEtalaseName = "";
            totalYDistance = 0;
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST);
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID);
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME);
            totalYDistance = savedInstanceState.getInt(SAVED_Y_DISTANCE);
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
                Intent intent = ShopProductSortActivity.createIntent(getActivity(), sortName, shopInfo.getInfo().getShopId());
                ShopProductListLimitedNewFragment.this.startActivityForResult(intent, REQUEST_CODE_SORT);
            }
        });
        bottomActionView.hide(false);
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
                int firstCompletelyVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition();
                int lastCompleteVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (firstCompletelyVisiblePosition > -1) {
                    if (firstCompletelyVisiblePosition > ShopPageConstant.DEFAULT_ETALASE_POSITION) {
                        // make the etalase label always visible
                    } else {
                        // make the etalase label always gone
                    }
                }
                if (lastCompleteVisiblePosition > -1) {
                    if (lastCompleteVisiblePosition >= ShopPageConstant.ITEM_OFFSET) {
                        if (shopProductNewAdapter.getShopProductViewModelList().size() > 0) {
                            bottomActionView.show();
                        }
                    } else {
                        bottomActionView.hide();
                    }
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);

        if (shopInfo != null) {
            loadInitialData();
        }
    }

    public void displayProduct(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        loadInitialData();
    }

    // load data promo/featured/etalase
    protected void loadInitialData() {
        loadTopData();
        reloadProductData();
    }

    // load product list first time
    private void reloadProductData() {
        isLoadingInitialData = true;
        shopProductNewAdapter.clearProductList();
        showLoading();
        loadData(getDefaultInitialPage());
    }

    protected void loadTopData() {
        if (shopInfo != null) {
            shopProductNewAdapter.clearDataExceptProduct();

            shopProductListNewPresenter.loadProductPromoModel(getOfficialWebViewUrl(shopInfo));

            shopProductListNewPresenter.getProductFeatureListWithAttributes(
                    shopInfo.getInfo().getShopId(),
                    shopInfo.getInfo().isShopOfficial());

            shopProductListNewPresenter.getShopEtalase(shopInfo.getInfo().getShopId(), selectedEtalaseList,
                    ShopPageConstant.ETALASE_TO_SHOW);
        }
    }

    private String getOfficialWebViewUrl(ShopInfo shopInfo) {
        if (shopInfo == null) {
            return "";
        }
        String officialWebViewUrl = shopInfo.getInfo().getShopOfficialTop();
        officialWebViewUrl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? officialWebViewUrl : "";
        return officialWebViewUrl;
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
                    ShopPageConstant.DEFAULT_PER_PAGE,
                    selectedEtalaseId);
        }
    }

    @Override
    public void renderShopProductPromo(ShopProductPromoViewModel shopProductPromoViewModel) {
        shopProductNewAdapter.setShopProductPromoViewModel(shopProductPromoViewModel);
    }

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
                this, this,
                this, shopProductNewAdapter, false
        );
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
            // only add the empty state when the shop has No Product And No Official URL
            String officialWebViewUrl = getOfficialWebViewUrl(shopInfo);
            if (TextUtils.isEmpty(officialWebViewUrl)) {
                shopProductNewAdapter.setNeedToShowEtalase(true);
                shopProductNewAdapter.addElement(getEmptyDataViewModel());
            } else {
                // no need to show empty (even if the product is empty) when there is official web
                shopProductNewAdapter.setNeedToShowEtalase(false);
                shopProductNewAdapter.clearAllNonDataElement();
            }
        } else {
            shopProductNewAdapter.setNeedToShowEtalase(true);
            isLoadingInitialData = false;
        }
        shopProductNewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetProductFeature(Throwable e) {
        //TODO need test this
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
        //TODO need check this.
        shopProductNewAdapter.setShopEtalase(null);
        shopProductNewAdapter.setShopEtalaseTitle(null);
    }

    @Override
    public void onSuccessGetEtalaseList(List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        //default select first index as selected.
        if (TextUtils.isEmpty(selectedEtalaseId) && shopEtalaseViewModelList != null && shopEtalaseViewModelList.size() > 0) {
            selectedEtalaseId = shopEtalaseViewModelList.get(0).getEtalaseId();
            selectedEtalaseName = shopEtalaseViewModelList.get(0).getEtalaseName();
        }
        // update the adapter
        shopProductNewAdapter.setShopEtalase(new ShopProductEtalaseListViewModel(shopEtalaseViewModelList, selectedEtalaseId));
        shopProductNewAdapter.setShopEtalaseTitle(selectedEtalaseName);
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
        selectedEtalaseName = shopEtalaseViewModel.getEtalaseName();
        shopProductNewAdapter.setSelectedEtalaseId(selectedEtalaseId);
        shopProductNewAdapter.setShopEtalaseTitle(selectedEtalaseName);
        // no need ro rearraged, just notify the adapter to reload product list by etalase id
        reloadProductData();
    }

    @Override
    public void onEtalaseMoreListClicked() {
        if (shopInfo != null) {
            shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), true, shopInfo.getInfo().getShopId(),
                    shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));

            Intent shopEtalaseIntent = ShopEtalaseActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), selectedEtalaseId);
            startActivityForResult(shopEtalaseIntent, REQUEST_CODE_ETALASE);
        }
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
                if (resultCode == Activity.RESULT_OK && shopProductListNewPresenter != null && shopInfo != null) {
                    selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);

                    // if etalase id is on the list, refresh this page; if etalase id is in other list, go to new page.
                    boolean isAddedToCurrentEtalaseList = shopProductNewAdapter.addEtalaseFromListMore(selectedEtalaseId, selectedEtalaseName);
                    if (isAddedToCurrentEtalaseList) {
                        selectedEtalaseList.add(0, new ShopEtalaseViewModel(selectedEtalaseId, selectedEtalaseName));
                        if (selectedEtalaseList.size() > ShopPageConstant.MAXIMUM_SELECTED_ETALASE_LIST) {
                            selectedEtalaseList.remove(selectedEtalaseList.size());
                        }
                        startActivity(ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), "",
                                selectedEtalaseId, attribution));
                    }
                    needReloadData = true;

                    if (shopPageTracking != null) {
                        shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                                true, selectedEtalaseName, shopInfo.getInfo().getShopId(),
                                shopProductListNewPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
                    }
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
    public void onResume() {
        super.onResume();
        if (needReloadData) {
            reloadProductData();
            needReloadData = false;
        }
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
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName);
        outState.putInt(SAVED_Y_DISTANCE, totalYDistance);
    }


}