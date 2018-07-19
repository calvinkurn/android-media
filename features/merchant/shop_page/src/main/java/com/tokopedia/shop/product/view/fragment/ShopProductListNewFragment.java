package com.tokopedia.shop.product.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
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
import com.tokopedia.shop.etalase.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductNewAdapter;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductNewViewHolder;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductDedicatedListView;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.newpresenter.ShopProductListPresenterNew;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListNewFragment extends BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements ShopProductDedicatedListView, BaseEmptyViewHolder.Callback, ShopProductClickedNewListener,
        ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener {

    private static final int REQUEST_CODE_USER_LOGIN = 100;
    private static final int REQUEST_CODE_ETALASE = 200;
    private static final int REQUEST_CODE_SORT = 300;

    private ShopProductNewAdapter shopProductNewAdapter;

    private GridLayoutManager gridLayoutManager;

    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 2;

    @Inject
    ShopProductListPresenterNew shopProductListPresenter;
    @Inject
    ShopPageTracking shopPageTracking;

    //    private LabelView etalaseLabelView;
    private ShopModuleRouter shopModuleRouter;

    private String shopId;
    private String keyword;
    private String sortName = Integer.toString(Integer.MIN_VALUE);
    private String attribution;

    private String sortId;
    private RecyclerView recyclerView;
    private BottomActionView bottomActionView;
    private ShopInfo shopInfo;

    private String selectedEtalaseId;
    private String selectedEtalaseName;

    private OnShopProductListFragmentListener onShopProductListFragmentListener;
    public interface OnShopProductListFragmentListener{
        void updateUIByShopName(String shopName);
    }

    public static ShopProductListNewFragment createInstance(String shopId,
                                                            String keyword,
                                                            String etalaseId,
                                                            String sort,
                                                            String attribution) {
        ShopProductListNewFragment shopProductListFragment = new ShopProductListNewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        bundle.putString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword);
        bundle.putString(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId);
        bundle.putString(ShopParamConstant.EXTRA_SORT_ID, sort);
        bundle.putString(ShopParamConstant.EXTRA_ATTRIBUTION, attribution);
        shopProductListFragment.setArguments(bundle);
        return shopProductListFragment;
    }

    @Override
    protected ShopProductAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory(null,
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
        emptyModel.setIconRes(R.drawable.ic_empty_list_search);
        if (TextUtils.isEmpty(keyword)) {
            emptyModel.setTitle(getString(R.string.shop_product_empty_title_desc));
        } else {
            emptyModel.setTitle(getString(R.string.shop_product_empty_product_title, keyword));
        }
        emptyModel.setContent(getString(R.string.shop_product_empty_product_title_owner));
        return emptyModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context.getApplicationContext() instanceof ShopModuleRouter) {
            shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID, "");
        keyword = getArguments().getString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, "");
        selectedEtalaseId = getArguments().getString(ShopParamConstant.EXTRA_ETALASE_ID, "");
        sortName = getArguments().getString(ShopParamConstant.EXTRA_SORT_ID, Integer.toString(Integer.MIN_VALUE));
        attribution = getArguments().getString(ShopParamConstant.EXTRA_ATTRIBUTION, "");
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        shopProductListPresenter.attachView(this);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_product_list_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        searchInputView.setSearchHint(getString(R.string.shop_product_search_hint));
//        recyclerView = view.findViewById(R.id.recycler_view);
//        etalaseLabelView = view.findViewById(R.id.label_view_etalase);
        bottomActionView = view.findViewById(R.id.bottom_action_view);
        bottomActionView.hide(false);

//        setBottomActionViewImage(currentImgBottomNav);
//        RecyclerView.LayoutManager layoutManager = iterate(recyclerView);
//        recyclerView.setLayoutManager(layoutManager);

//        etalaseLabelView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (shopModuleRouter != null) {
//                    if (shopInfo != null) {
//                        shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), false, shopId,
//                                shopProductListPresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
//                    }
//                    Intent etalaseIntent = ShopEtalaseActivity.createIntent(getActivity(), shopId, selectedEtalaseId);
//                    startActivityForResult(etalaseIntent, REQUEST_CODE_ETALASE);
//                }
//            }
//        });

//        bottomActionView.setButton2OnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RecyclerView.LayoutManager layoutManager = iterate(recyclerView);
//                recyclerView.setLayoutManager(layoutManager);
//                getAdapter().notifyDataSetChanged();
//                setBottomActionViewImage(++currentImgBottomNav);
//                if (shopInfo != null) {
//                    shopPageTracking.eventClickViewTypeProduct(getString(R.string.shop_info_title_tab_product),
//                            currentImgBottomNav, shopId, shopProductListPresenter.isMyShop(shopId),
//                            ShopPageTracking.getShopType(shopInfo.getInfo()));
//                }
//            }
//        });

        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    shopPageTracking.eventClickSortProductList(getString(R.string.shop_info_title_tab_product), shopId,
                            shopProductListPresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
                ShopProductListNewFragment.this.startActivityForResult(ShopProductSortActivity.createIntent(getActivity(), sortName, shopId), REQUEST_CODE_SORT);
            }
        });
//        if (!TextUtils.isEmpty(keyword)) {
//            searchInputView.getSearchTextView().setText(keyword);
//        }

    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        recyclerView = super.getRecyclerView(view);
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

                if (dy < 0) { // going up
                    bottomActionView.show();
                } else if (dy > 0){ // going down
                    bottomActionView.hide();
                }
            }
        });
        return recyclerView;
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

    @Override
    public void onSwipeRefresh() {
        //TODO need clear etalase?
        shopProductListPresenter.clearProductCache();
        super.onSwipeRefresh();
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

    // load data promo/featured/etalase
    protected void loadInitialData() {
        reloadProductData();
    }

    // load product list first time
    private void reloadProductData() {
        isLoadingInitialData = true;
        shopProductNewAdapter.clearProductList();
        showLoading();
        loadData(getDefaultInitialPage());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_shop_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            onShareShop();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onShareShop() {
        if (shopInfo != null) {
            ((ShopModuleRouter) getActivity().getApplication()).goToShareShop(getActivity(), shopId, shopInfo.getInfo().getShopUrl(),
                    getString(R.string.shop_label_share_formatted, shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopLocation()));
        }
    }

//    private void setBottomActionViewImage(int index) {
//        if (bottomActionView != null && (index >= 0 && index < LAYOUT_IMAGE_DRAWABLE_LIST.length))
//            bottomActionView.setSecondImageDrawable(LAYOUT_IMAGE_DRAWABLE_LIST[index]);
//        else {
//            currentImgBottomNav = 0;
//            bottomActionView.setSecondImageDrawable(LAYOUT_IMAGE_DRAWABLE_LIST[currentImgBottomNav]);
//        }
//    }

//    private RecyclerView.LayoutManager iterate(final RecyclerView recyclerView) {
//        RecyclerView.LayoutManager layoutManager = null;
//        if (getNextIndex(currentIndex, layoutType.length) < 0) {
//            currentLayoutType = layoutType[currentIndex = 0];
//        } else {
//            currentLayoutType = layoutType[currentIndex];
//        }
//        switch (currentLayoutType.second) {
//            case LAYOUT_GRID_TYPE:
//                layoutManager = new GridLayoutManager(recyclerView.getContext(), SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
//                ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        if (recyclerView.getAdapter().getItemViewType(position) == ShopProductViewHolderOld.LAYOUT) {
//                            return ShopProductViewHolderOld.SPAN_LOOK_UP;
//                        }
//                        return SPAN_COUNT;
//                    }
//                });
//                break;
//            default:
//                layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
//                break;
//        }
//        currentIndex++;
//
//        return layoutManager;
//    }

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
            if (isLoadingInitialData) {
                bottomActionView.show();
            }
            isLoadingInitialData = false;
        }
        shopProductNewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(BaseShopProductViewModel baseShopProductViewModel) {
        // no op
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        //TODO
    }

    @Override
    public void onEtalaseMoreListClicked() {
        if (shopInfo != null) {
            shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), true, shopInfo.getInfo().getShopId(),
                    shopProductListPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));

            Intent shopEtalaseIntent = ShopEtalaseActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), selectedEtalaseId);
            startActivityForResult(shopEtalaseIntent, REQUEST_CODE_ETALASE);
        }
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

//    private void showBottomActionView() {
//        bottomActionView.setVisibility(getAdapter().isContainData() ? View.VISIBLE : View.GONE);
//    }

//    private int getNextIndex(int currentIndex, int max) {
//        if (currentIndex >= 0 && currentIndex < max) {
//            return currentIndex;
//        } else {
//            return -1;
//        }
//    }

    @Override
    public void loadData(int page) {
        if (shopInfo == null) {
            shopProductListPresenter.getShopInfo(shopId);
        } else {
            loadShopPageList(shopInfo, page);
        }
    }

    private void loadShopPageList(ShopInfo shopInfo, int page) {
        shopProductListPresenter.getShopPageList(shopInfo, keyword, selectedEtalaseId, 0, page, Integer.valueOf(sortName));
    }

//    @Override
//    public void onItemClicked(ShopProductViewModelOld shopProductViewModelOld) {
//
//    }

//    @Override
//    public void onWishListClicked(ShopProductViewModelOld shopProductViewModelOld) {
//        if (shopInfo != null) {
//            shopPageTracking.eventClickWishlistShop(getString(R.string.shop_info_title_tab_product),
//                    shopProductViewModelOld.isWishList(), false, shopId, shopProductListPresenter.isMyShop(shopId),
//                    ShopPageTracking.getShopType(shopInfo.getInfo()));
//        }
//        if (shopProductViewModelOld.isWishList()) {
//            shopProductListPresenter.removeFromWishList(shopProductViewModelOld.getId());
//        } else {
//            shopProductListPresenter.addToWishList(shopProductViewModelOld.getId());
//        }
//    }
//
//    @Override
//    public void onProductClicked(ShopProductViewModelOld shopProductViewModelOld) {
//        if (shopInfo != null) {
//            shopPageTracking.eventClickProductImpression(getString(R.string.shop_info_title_tab_product),
//                    shopProductViewModelOld.getName(), shopProductViewModelOld.getId(), shopProductViewModelOld.getDisplayedPrice(), attribution,
//                    shopProductViewModelOld.getPositionTracking(), false, shopProductListPresenter.isMyShop(shopId),
//                    ShopPageTracking.getShopType(shopInfo.getInfo()), (currentLayoutType.second == LAYOUT_GRID_TYPE));
//        }
//        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModelOld.getId(),
//                shopProductViewModelOld.getName(), shopProductViewModelOld.getDisplayedPrice(), shopProductViewModelOld.getImageUrl(),
//                attribution, shopPageTracking.getListNameOfProduct(shopProductViewModelOld.getPositionTracking(), (currentLayoutType.second == LAYOUT_GRID_TYPE),
//                        ShopPageTrackingConstant.PRODUCT_ETALASE));
//    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel) {
        if (shopInfo != null) {
            shopPageTracking.eventClickProductImpression(getString(R.string.shop_info_title_tab_product),
                    shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getDisplayedPrice(),
                    attribution, shopProductViewModel.getPositionTracking(), true,
                    shopProductListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()), false);
        }
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getId(), shopProductViewModel.getName(),
                shopProductViewModel.getDisplayedPrice(), shopProductViewModel.getImageUrl(), attribution,
                shopPageTracking.getListNameOfProduct(shopProductViewModel.getPositionTracking(), false, ShopPageTrackingConstant.PRODUCT_ETALASE));
    }


//    @Override
//    public void onSuccessAddToWishList(String productId, Boolean value) {
//        ((ShopProductAdapter) getAdapter()).updateWishListStatus(productId, true);
//    }

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
        shopProductNewAdapter.updateWishListStatus(productId, true);
    }

    @Override
    public void onSuccessRemoveFromWishList(String productId, Boolean value) {
        shopProductNewAdapter.updateWishListStatus(productId, false);
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
    public void onErrorRemoveFromWishList(Throwable e) {
        if (e instanceof UserNotLoginException) {
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
            return;
        }
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onEmptyButtonClicked() {
        ((ShopModuleRouter) getActivity().getApplication()).goToAddProduct(getActivity());
    }

    @Override
    public void onSuccessGetShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        onShopProductListFragmentListener.updateUIByShopName(shopInfo.getInfo().getShopName());
        loadInitialData();
    }

    @Override
    public void onErrorGetShopInfo(Throwable e) {
        //TODO check this.
        showGetListError(e);
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel) {
        if (shopInfo != null) {
            shopPageTracking.eventClickWishlistShop(getString(R.string.shop_info_title_tab_product), shopProductViewModel.isWishList(),
                    true, shopProductViewModel.getId(),
                    shopProductListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        if (shopProductViewModel.isWishList()) {
            shopProductListPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductListPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    //    @Override
//    public void on(String etalaseId, String etalaseName) {
//        if (!etalaseId.equalsIgnoreCase(this.etalaseId)) {
//            this.etalaseId = etalaseId;
//        }
//        //TODO etalase
//        if (TextUtils.isEmpty(etalaseName)) {
//            if (shopProductListPresenter.isMyShop(shopId)) {
//                etalaseLabelView.setContent(getString(R.string.shop_info_filter_all_showcase));
//            } else {
//                etalaseLabelView.setContent(getString(R.string.shop_info_filter_menu_etalase_all));
//            }
//        } else {
//            etalaseLabelView.setContent(MethodChecker.fromHtml(etalaseName));
//        }
//    }

    @Override
    public void onSuccessGetEtalaseName(String etalaseId, String etalaseName) {
        if (!etalaseId.equalsIgnoreCase(this.selectedEtalaseId)) {
            this.selectedEtalaseId = etalaseId;
        }
    }


    //TODO search logic
//    @Override
//    public void onSearchSubmitted(String s) {
//        keyword = s;
//        loadInitialData();
//        KeyboardHandler.hideSoftKeyboard(getActivity());
//    }
//
//    @Override
//    public void onSearchTextChanged(String s) {
//        if (TextUtils.isEmpty(s)) {
//            keyword = s;
//            loadInitialData();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);
                    if (shopInfo != null) {
                        shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                                false, selectedEtalaseName, shopId, shopProductListPresenter.isMyShop(shopId),
                                ShopPageTracking.getShopType(shopInfo.getInfo()));
                    }
                    this.isLoadingInitialData = true;
                    loadInitialData();
                }
                break;

            case REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    sortId = data.getStringExtra(ShopProductSortActivity.SORT_ID);
                    sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME);
                    this.isLoadingInitialData = true;
                    loadInitialData();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_shop_info, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_share) {
//            onShareShop();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(new ShopProductModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductListPresenter != null) {
            shopProductListPresenter.detachView();
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onShopProductListFragmentListener = (OnShopProductListFragmentListener) context;
    }

    //    @Override
//    public int getType(Object type) {
//        return currentLayoutType.first;
//    }
}
