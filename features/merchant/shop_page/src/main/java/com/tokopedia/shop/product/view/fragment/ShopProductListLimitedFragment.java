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
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductNewViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductLimitedListPresenter;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_TITLE_POSITION;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedFragment extends BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements ShopProductListView, BaseEmptyViewHolder.Callback,
        ShopProductPromoViewHolder.PromoViewHolderListener, ShopProductClickedNewListener,
        ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener, ShopProductAdapterTypeFactory.OnShopProductAdapterTypeFactoryListener {

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

    @Inject
    ShopProductLimitedListPresenter shopProductLimitedListPresenter;
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

    // selected is list, because we want to keep the history.
    private ArrayList<ShopEtalaseViewModel> selectedEtalaseList;
    // selected id of etalase, might not in selectedEtalaseList.
    private String selectedEtalaseId;
    private String selectedEtalaseName;

    private ShopProductAdapter shopProductAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean needReloadData;
    private boolean needToShowEtalase;

    public static ShopProductListLimitedFragment createInstance(String shopAttribution) {
        ShopProductListLimitedFragment fragment = new ShopProductListLimitedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ATTRIBUTION, shopAttribution);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_product_limited_list_new, container, false);
        bottomActionView = view.findViewById(R.id.bottom_action_view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            selectedEtalaseList = new ArrayList<>();
            selectedEtalaseId = null;
            selectedEtalaseName = "";
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST);
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID);
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME);
        }
        super.onCreate(savedInstanceState);
        attribution = getArguments().getString(SHOP_ATTRIBUTION, "");
        shopProductLimitedListPresenter.attachView(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShopProductSortActivity.createIntent(getActivity(), sortName, shopInfo.getInfo().getShopId());
                ShopProductListLimitedFragment.this.startActivityForResult(intent, REQUEST_CODE_SORT);
            }
        });
        bottomActionView.setVisibility(View.GONE);
        bottomActionView.hide(false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        super.onViewCreated(view, savedInstanceState);

        if (shopInfo != null) {
            loadInitialData();
        }
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        recyclerView = super.getRecyclerView(view);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalDy;

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) { // going up
                    if (shopProductAdapter.getShopProductViewModelList().size() > 0) {
                        int lastCompleteVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                        if (lastCompleteVisiblePosition > -1) {
                            if (lastCompleteVisiblePosition >= ShopPageConstant.ITEM_OFFSET) {
                                bottomActionView.show();
                            } else {
                                bottomActionView.hide();
                            }
                        }
                    } else {
                        bottomActionView.hide();
                    }
                } else if (dy > 0) { // going down
                    bottomActionView.hide();
                }
            }
        });
        return recyclerView;
    }

    public void displayProduct(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        loadInitialData();
    }

    // load data promo/featured/etalase
    protected void loadInitialData() {
        shopProductAdapter.clearAllNonDataElement();
        loadTopData();
        reloadProductData();
    }

    //TODO on refresh, clear cache

    // load product list first time
    private void reloadProductData() {
        isLoadingInitialData = true;
        bottomActionView.hide(false);
        shopProductAdapter.clearAllNonDataElement();
        shopProductAdapter.clearProductList();
        showLoading();
        loadData(getDefaultInitialPage());
    }

    protected void loadTopData() {
        if (shopInfo != null) {
            shopProductAdapter.clearDataExceptProduct();

            shopProductLimitedListPresenter.loadProductPromoModel(getOfficialWebViewUrl(shopInfo));

            shopProductLimitedListPresenter.getProductFeatureListWithAttributes(
                    shopInfo.getInfo().getShopId(),
                    shopInfo.getInfo().isShopOfficial());

            shopProductLimitedListPresenter.getShopEtalase(shopInfo.getInfo().getShopId(), selectedEtalaseList,
                    ShopPageConstant.ETALASE_TO_SHOW);
        }
    }

    private String getOfficialWebViewUrl(ShopInfo shopInfo) {
        if (shopInfo == null) {
            return "";
        }
        String officialWebViewUrl = shopInfo.getInfo().getShopOfficialTop();
        officialWebViewUrl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? officialWebViewUrl : "";
        officialWebViewUrl = TextApiUtils.isTextEmpty(officialWebViewUrl) ? "" : officialWebViewUrl;
        return officialWebViewUrl;
    }

    @Override
    public void loadData(int page) {
        if (shopInfo != null) {
            shopProductLimitedListPresenter.getProductListWithAttributes(
                    shopInfo.getInfo().getShopId(),
                    !shopInfo.getInfo().isOpen(),
                    TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial()),
                    page,
                    ShopPageConstant.DEFAULT_PER_PAGE,
                    selectedEtalaseId);
        }
    }

    @Override
    public void renderShopProductPromo(ShopProductPromoViewModel shopProductPromoViewModel) {
        shopProductAdapter.setShopProductPromoViewModel(shopProductPromoViewModel);
        if (!TextUtils.isEmpty(shopProductPromoViewModel.getUrl())){
            if (shopInfo != null) {
                shopPageTracking.eventViewBannerImpression(getString(R.string.shop_info_title_tab_product),
                        shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopId(),
                        shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                        ShopPageTracking.getShopType(shopInfo.getInfo()));
            }
        }
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        gridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (shopProductAdapter.getItemViewType(position) == ShopProductNewViewHolder.LAYOUT) {
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
        return new DataEndlessScrollListener(recyclerView.getLayoutManager(), shopProductAdapter) {
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
                this, this,
                false, false
        );
    }

    @NonNull
    @Override
    protected BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> createAdapterInstance() {
        shopProductAdapter = new ShopProductAdapter(getAdapterTypeFactory());
        return shopProductAdapter;
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_empty_list_product);

        if (shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId())) {
            if (isCurrentlyShowAllEtalase()) {
                emptyModel.setTitle(getString(R.string.shop_product_limited_empty_product_title_owner));
            } else {
                emptyModel.setTitle(getString(R.string.shop_product_limited_empty_product_title_owner_at_etalase));
            }
            emptyModel.setContent(getString(R.string.shop_product_limited_empty_product_content_owner));
            emptyModel.setButtonTitle(getString(R.string.shop_page_label_add_product));
        } else {
            if (isCurrentlyShowAllEtalase()) {
                emptyModel.setContent(getString(R.string.shop_product_limited_empty_product_title));
            } else {
                emptyModel.setContent(getString(R.string.shop_product_empty_title_etalase_desc));
            }
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

    private boolean isCurrentlyShowAllEtalase() {
        if (TextUtils.isEmpty(selectedEtalaseId)) {
            return true;
        }
        List<ShopEtalaseViewModel> etalaseViewModelList = shopProductAdapter.getShopProductEtalaseListViewModel().getEtalaseModelList();
        return etalaseViewModelList.size() > 0 && etalaseViewModelList.get(0).getEtalaseId().equalsIgnoreCase(selectedEtalaseId);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductLimitedListPresenter != null) {
            shopProductLimitedListPresenter.detachView();
        }
    }

    @Override
    public void onItemClicked(BaseShopProductViewModel baseShopProductViewModel) {
        // no op
    }


    @Override
    public void onErrorRemoveFromWishList(Throwable e) {

    }

    @Override
    public void onSuccessRemoveFromWishList(String productId, Boolean value) {
        shopProductAdapter.updateWishListStatus(productId, false);
    }

    @Override
    public void onErrorAddToWishList(Throwable e) {
        if (!shopProductLimitedListPresenter.isLogin()) {
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
            return;
        }
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onSuccessAddToWishList(String productId, Boolean value) {
        shopProductAdapter.updateWishListStatus(productId, true);
    }

    @Override
    public void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage) {
        if (list.size() > 0) {
            shopPageTracking.eventViewProductImpressionNew(getString(R.string.shop_info_title_tab_product),
                    list, attribution,
                    true, shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()),
                    false);
        }

        hideLoading();
        shopProductAdapter.clearAllNonDataElement();
        if (isLoadingInitialData) {
            shopProductAdapter.clearProductList();
            endlessRecyclerViewScrollListener.resetState();
        }
        shopProductAdapter.addProductList(list);
        updateScrollListenerState(hasNextPage);

        if (shopProductAdapter.getShopProductViewModelList().size() == 0) {
            // only add the empty state when the shop has No Product And No Official URL

            boolean needShowEmpty;
            String officialWebViewUrl = getOfficialWebViewUrl(shopInfo);
            if (TextUtils.isEmpty(officialWebViewUrl)) { // no promo web, show empty
                needShowEmpty = true;
            } else {
                // no need to show empty (even if the product is empty) when there is official web
                // check if it is all product or specific etalase.
                needShowEmpty = !isCurrentlyShowAllEtalase();
            }

            if (needShowEmpty) {
                setNeedToShowEtalase(true);
                shopProductAdapter.addElement(getEmptyDataViewModel());
            } else {
                setNeedToShowEtalase(false);
                shopProductAdapter.clearAllNonDataElement();
            }
            bottomActionView.setVisibility(View.GONE);
            bottomActionView.hide();
        } else {
            setNeedToShowEtalase(true);
            bottomActionView.setVisibility(View.VISIBLE);
            bottomActionView.show();
            isLoadingInitialData = false;
        }
        shopProductAdapter.notifyDataSetChanged();
        shopProductAdapter.refreshSticky();
    }

    @Override
    public void onErrorGetProductFeature(Throwable e) {
        shopProductAdapter.setShopProductFeaturedViewModel(null);
    }

    @Override
    public void onSuccessGetProductFeature(@NonNull List<ShopProductViewModel> list) {
        shopProductAdapter.setShopProductFeaturedViewModel(new ShopProductFeaturedViewModel(list));
        if (list.size() > 0) {
            shopPageTracking.eventViewProductFeaturedImpressionNew(getString(R.string.shop_info_title_tab_product),
                    list, attribution,
                    shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()), false);
        }
    }

    @Override
    public void showGetListError(Throwable throwable) {
        hideLoading();
        updateStateScrollListener();
        if (shopProductAdapter.getShopProductViewModelList().size() > 0) {
            onGetListErrorWithExistingData(throwable);
        } else {
            onGetListErrorWithEmptyData(throwable);
        }
    }

    @Override
    public void onErrorGetEtalaseList(Throwable e) {
        shopProductAdapter.setShopEtalase(null);
        shopProductAdapter.setShopEtalaseTitle(null);
    }

    @Override
    public void onSuccessGetEtalaseList(List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        //default select first index as selected.
        if (TextUtils.isEmpty(selectedEtalaseId) && shopEtalaseViewModelList != null && shopEtalaseViewModelList.size() > 0) {
            selectedEtalaseId = shopEtalaseViewModelList.get(0).getEtalaseId();
            selectedEtalaseName = shopEtalaseViewModelList.get(0).getEtalaseName();
        }
        // update the adapter
        shopProductAdapter.setShopEtalase(new ShopProductEtalaseListViewModel(shopEtalaseViewModelList, selectedEtalaseId));
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName);
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // no-op
    }

    @Override
    public void onEmptyButtonClicked() {
        ((ShopModuleRouter) getActivity().getApplication()).goToAddProduct(getActivity());
    }

    @Override
    public void promoClicked(String url) {
        if (shopInfo != null) {
            shopPageTracking.eventClickBannerImpression(getString(R.string.shop_info_title_tab_product),
                    shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopId(), shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        boolean urlProceed = ShopProductOfficialStoreUtils.proceedUrl(getActivity(), url, shopInfo.getInfo().getShopId(),
                shopProductLimitedListPresenter.isLogin(),
                shopProductLimitedListPresenter.getDeviceId(),
                shopProductLimitedListPresenter.getUserId());
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
        shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId);
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName);
        if (shopPageTracking != null) {
            String shopId = shopInfo.getInfo().getShopId();
            shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                    false, selectedEtalaseName, shopId,
                    shopProductLimitedListPresenter.isMyShop(shopId),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        // no need ro rearraged, just notify the adapter to reload product list by etalase id
        reloadProductData();
    }

    @Override
    public void onEtalaseMoreListClicked() {
        if (shopInfo != null) {
            shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), true, shopInfo.getInfo().getShopId(),
                    shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));

            Intent shopEtalaseIntent = ShopEtalaseActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), selectedEtalaseId);
            startActivityForResult(shopEtalaseIntent, REQUEST_CODE_ETALASE);
        }
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel, boolean isFromFeatured) {
        if (shopInfo != null) {
            if (isFromFeatured) {
                shopPageTracking.eventClickWishlistShopPageFeatured(getString(R.string.shop_info_title_tab_product),
                        shopProductViewModel.isWishList(), shopProductViewModel.getId(), shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                        ShopPageTracking.getShopType(shopInfo.getInfo()));
            } else {
                shopPageTracking.eventClickWishlistShop(getString(R.string.shop_info_title_tab_product), shopProductViewModel.isWishList(),
                        true, shopProductViewModel.getId(),
                        shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                        ShopPageTracking.getShopType(shopInfo.getInfo()));
            }
        }
        if (shopProductViewModel.isWishList()) {
            shopProductLimitedListPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductLimitedListPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel, boolean isFromFeatured) {
        if (shopInfo != null) {
            if (isFromFeatured) {
                shopPageTracking.eventClickProductFeaturedImpression(getString(R.string.shop_info_title_tab_product),
                        shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(),
                        attribution, shopProductViewModel.getPositionTracking(), true,
                        shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                        ShopPageTracking.getShopType(shopInfo.getInfo()), false);
            } else {
                shopPageTracking.eventClickProductImpression(getString(R.string.shop_info_title_tab_product),
                        shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(),
                        attribution, shopProductViewModel.getPositionTracking(), true,
                        shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                        ShopPageTracking.getShopType(shopInfo.getInfo()), false);
            }
        }
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getId(), shopProductViewModel.getName(),
                shopProductViewModel.getDisplayedPrice(), shopProductViewModel.getImageUrl(), attribution,
                shopPageTracking.getListNameOfProduct(shopProductViewModel.getPositionTracking(), false, ShopPageTrackingConstant.PRODUCT_ETALASE));
    }

    // TODO onActivityResult from ShopProductList will move the cursor to the top

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK && shopProductLimitedListPresenter != null && shopInfo != null) {
                    selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);

                    // if etalase id is on the list, refresh this page; if etalase id is in other list, go to new page.
                    boolean isAddedToCurrentEtalaseList = shopProductAdapter.addEtalaseFromListMore(selectedEtalaseId, selectedEtalaseName);
                    if (isAddedToCurrentEtalaseList) {
                        selectedEtalaseList.add(0, new ShopEtalaseViewModel(selectedEtalaseId, selectedEtalaseName));
                        if (selectedEtalaseList.size() > ShopPageConstant.MAXIMUM_SELECTED_ETALASE_LIST) {
                            selectedEtalaseList.remove(selectedEtalaseList.size());
                        }
                        Intent intent = ShopProductListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), "",
                                selectedEtalaseId, attribution, sortName, selectedEtalaseList);
                        startActivity(intent);
                    }
                    shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId);
                    shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName);
                    needReloadData = true;

                    if (shopPageTracking != null) {
                        shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                                true, selectedEtalaseName, shopInfo.getInfo().getShopId(),
                                shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
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
        if (shopProductLimitedListPresenter != null) {
            shopProductLimitedListPresenter.detachView();
        }
    }

    public void onLastItemVisibleTracking() {
        if (shopInfo != null) {
            shopPageTracking.eventViewBottomNavigation(getString(R.string.shop_info_title_tab_product), shopInfo.getInfo().getShopId(),
                    shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_SELECTED_ETALASE_LIST, selectedEtalaseList);
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId);
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName);
    }

    public void setNeedToShowEtalase(boolean needToShowEtalase) {
        if (this.needToShowEtalase != needToShowEtalase) {
            this.needToShowEtalase = needToShowEtalase;
            shopProductAdapter.notifyItemChanged(DEFAULT_ETALASE_POSITION);
            shopProductAdapter.notifyItemChanged(DEFAULT_ETALASE_TITLE_POSITION);
        }
    }

    @Override
    public boolean needToShowEtalase() {
        return needToShowEtalase;
    }


    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
    }
}