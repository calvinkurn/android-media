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
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.listener.ShopProductDedicatedListView;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListPresenter;
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListFragment extends BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements ShopProductDedicatedListView, BaseEmptyViewHolder.Callback, ShopProductClickedNewListener,
        ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener {

    private static final int REQUEST_CODE_USER_LOGIN = 100;
    private static final int REQUEST_CODE_ETALASE = 200;
    private static final int REQUEST_CODE_SORT = 300;

    private ShopProductAdapter shopProductAdapter;

    private GridLayoutManager gridLayoutManager;

    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 2;

    public static final String SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list";
    public static final String SAVED_SELECTED_ETALASE_ID = "saved_etalase_id";
    public static final String SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name";
    public static final String SAVED_KEYWORD = "saved_keyword";
    public static final String SAVED_SORT_VALUE = "saved_sort_name";

    @Inject
    ShopProductListPresenter shopProductListPresenter;
    @Inject
    ShopPageTracking shopPageTracking;

    private ShopModuleRouter shopModuleRouter;

    private String shopId;
    private String keyword;
    private String sortValue;
    private String attribution;

    private ArrayList<ShopEtalaseViewModel> selectedEtalaseList;

    private RecyclerView recyclerView;
    private BottomActionView bottomActionView;
    private ShopInfo shopInfo;

    private String selectedEtalaseId;
    private String selectedEtalaseName;

    private OnShopProductListFragmentListener onShopProductListFragmentListener;
    private boolean needReloadData;
    private StickySingleHeaderView stickySingleHeaderView;

    public interface OnShopProductListFragmentListener {
        void updateUIByShopName(String shopName);
    }

    public static ShopProductListFragment createInstance(String shopId,
                                                         String keyword,
                                                         String etalaseId,
                                                         String sort,
                                                         String attribution,
                                                         ArrayList<ShopEtalaseViewModel> selectedEtalaseChipList) {
        ShopProductListFragment shopProductListFragment = new ShopProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        bundle.putString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword);
        bundle.putString(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId);
        bundle.putString(ShopParamConstant.EXTRA_SORT_ID, sort);
        bundle.putString(ShopParamConstant.EXTRA_ATTRIBUTION, attribution);
        bundle.putParcelableArrayList(ShopParamConstant.EXTRA_SELECTED_ETALASE_CHIP, selectedEtalaseChipList);
        shopProductListFragment.setArguments(bundle);
        return shopProductListFragment;
    }

    @Override
    protected ShopProductAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory(null,
                this, this,
                this, null,
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
        emptyModel.setIconRes(R.drawable.ic_empty_list_search);
        if (TextUtils.isEmpty(keyword)) {
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                emptyModel.setTitle(getString(R.string.shop_product_empty_title_desc));
            } else {
                emptyModel.setTitle(getString(R.string.shop_product_empty_title_etalase_desc));
            }
        } else {
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                emptyModel.setTitle(getString(R.string.shop_product_empty_product_title_no_etalase, keyword));
            } else {
                emptyModel.setTitle(getString(R.string.shop_product_empty_product_title_etalase, keyword));
            }
        }
        if (TextUtils.isEmpty(selectedEtalaseId)) {
            emptyModel.setContent(getString(R.string.shop_product_empty_product_title_owner_no_etalase));
        } else {
            emptyModel.setContent(getString(R.string.shop_product_empty_product_title_owner_etalase));
        }
        return emptyModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID, "");
        attribution = getArguments().getString(ShopParamConstant.EXTRA_ATTRIBUTION, "");
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            selectedEtalaseList = getArguments().getParcelableArrayList(ShopParamConstant.EXTRA_SELECTED_ETALASE_CHIP);
            selectedEtalaseId = getArguments().getString(ShopParamConstant.EXTRA_ETALASE_ID, "");
            selectedEtalaseName = "";
            keyword = getArguments().getString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, "");
            sortValue = getArguments().getString(ShopParamConstant.EXTRA_SORT_ID, String.valueOf(Integer.MIN_VALUE));
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST);
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID);
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME);
            keyword = savedInstanceState.getString(SAVED_KEYWORD);
            sortValue = savedInstanceState.getString(SAVED_SORT_VALUE);
        }
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
        View view = inflater.inflate(R.layout.fragment_shop_product_list_new, container, false);
        bottomActionView = view.findViewById(R.id.bottom_action_view);
        stickySingleHeaderView = view.findViewById(R.id.stickySingleHeaderView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    shopPageTracking.eventClickSortProductList(getString(R.string.shop_info_title_tab_product), shopId,
                            shopProductListPresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
                Intent intent = ShopProductSortActivity.createIntent(getActivity(), sortValue, shopId);
                ShopProductListFragment.this.startActivityForResult(intent, REQUEST_CODE_SORT);
            }
        });

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
                if (dy < 0) { // going up
                    if (shopProductAdapter.getShopProductViewModelList().size() > 0) {
                        bottomActionView.show();
                    }
                } else if (dy > 0) { // going down
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
                if (shopProductAdapter.getItemViewType(position) == ShopProductViewHolder.LAYOUT) {
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

    public void updateDataByChangingKeyword(String keyword) {
        if (!this.keyword.equalsIgnoreCase(keyword)) {
            this.keyword = keyword;
            // if keyword is changed, remove the selectedEtalaseId
            // we assume that new keyword means user want to start a new search.
            selectedEtalaseId = null;
            selectedEtalaseName = null;
            shopProductAdapter.setShopEtalase(null);
            stickySingleHeaderView.disable();
            loadInitialData();
        }
    }

    @Override
    public void onEtalaseChipClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        if (shopProductAdapter.isLoading()) {
            return;
        }
        selectedEtalaseId = shopEtalaseViewModel.getEtalaseId();
        selectedEtalaseName = shopEtalaseViewModel.getEtalaseName();
        shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId);
        if (shopPageTracking != null) {
            shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                    false, selectedEtalaseName, shopId, shopProductListPresenter.isMyShop(shopId),
                    ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        // no need ro rearraged, just notify the adapter to reload product list by etalase id

        loadInitialData();
    }

    // load product list first time
    public void loadInitialData() {
        bottomActionView.setVisibility(View.GONE);
        bottomActionView.hide(false);
        isLoadingInitialData = true;
        shopProductAdapter.clearProductList();
        shopProductAdapter.clearAllNonDataElement();
        showLoading();
        loadData(getDefaultInitialPage());
    }

    @Override
    public void loadData(int page) {
        if (shopInfo == null) {
            shopProductListPresenter.getShopInfo(shopId);
        } else {
            loadShopPageList(shopInfo, page);
        }
    }

    private void loadShopPageList(ShopInfo shopInfo, int page) {
        shopProductListPresenter.getShopPageList(shopInfo,
                keyword,
                selectedEtalaseId,
                0,
                page,
                Integer.valueOf(sortValue));
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

    @Override
    public void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage) {
        if (list.size() > 0) {
            shopPageTracking.eventViewProductImpression(getString(R.string.shop_info_title_tab_product),
                    list, attribution,
                    true, shopProductListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
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
            bottomActionView.setVisibility(View.GONE);
            bottomActionView.hide();
            shopProductAdapter.addElement(getEmptyDataViewModel());
        } else {
            if (isLoadingInitialData) {
                bottomActionView.setVisibility(View.VISIBLE);
                bottomActionView.show();
            }
            isLoadingInitialData = false;
        }
        shopProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(BaseShopProductViewModel baseShopProductViewModel) {
        // no op
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // no-op
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
        if (shopProductAdapter.getShopProductViewModelList().size() > 0) {
            onGetListErrorWithExistingData(throwable);
        } else {
            onGetListErrorWithEmptyData(throwable);
        }
    }

    @Override
    protected void onGetListErrorWithEmptyData(Throwable throwable) {
        super.onGetListErrorWithEmptyData(throwable);
        bottomActionView.hide(false);
    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel, boolean isFromFeatured) {
        if (shopInfo != null) {
            // isFromFeature is always false
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
        shopProductAdapter.updateWishListStatus(productId, true);
    }

    @Override
    public void onSuccessRemoveFromWishList(String productId, Boolean value) {
        shopProductAdapter.updateWishListStatus(productId, false);
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
    public void addNewEtalaseToChip(String etalaseId, String etalaseName) {
        // if etalase id is not on the list, add it
        selectedEtalaseId = etalaseId;
        selectedEtalaseName = etalaseName;
        boolean isAddedToCurrentEtalaseList = shopProductAdapter.addEtalaseFromListMore(etalaseId, etalaseName);
        if (isAddedToCurrentEtalaseList) {
            selectedEtalaseList.add(0, new ShopEtalaseViewModel(selectedEtalaseId, selectedEtalaseName));
            if (selectedEtalaseList.size() > ShopPageConstant.MAXIMUM_SELECTED_ETALASE_LIST) {
                selectedEtalaseList.remove(selectedEtalaseList.size() - 1);
            }
        }
        shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId);
    }

    @Override
    public ArrayList<ShopEtalaseViewModel> getSelectedEtalaseViewModelList() {
        return selectedEtalaseList;
    }

    @Override
    public List<ShopEtalaseViewModel> getShopEtalaseViewModelList(){
        return shopProductAdapter.getShopProductEtalaseListViewModel().getEtalaseModelList();
    }

    @Override
    public String getSelectedEtalaseName() {
        return selectedEtalaseName;
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel, boolean isFromFeature) {
        if (shopInfo != null) {
            //isFromFeature is always false anyway.
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

    @Override
    public void onErrorGetEtalaseList(Throwable e) {
        shopProductAdapter.setShopEtalase(null);
    }

    @Override
    public void onSuccessGetEtalaseList(List<ShopEtalaseViewModel> shopEtalaseViewModelList,
                                        String etalaseId, String etalaseName) {
        //default select first index as selected.
        if (shopEtalaseViewModelList == null || shopEtalaseViewModelList.size() == 0) {
            selectedEtalaseId = null;
            selectedEtalaseName = null;
        } else if (TextUtils.isEmpty(selectedEtalaseId)) {
            selectedEtalaseId = shopEtalaseViewModelList.get(0).getEtalaseId();
            selectedEtalaseName = shopEtalaseViewModelList.get(0).getEtalaseName();
        } else {
            selectedEtalaseId = etalaseId;
            selectedEtalaseName = etalaseName;
        }
        // update the adapter
        shopProductAdapter.setShopEtalase(
                new ShopProductEtalaseListViewModel(shopEtalaseViewModelList, selectedEtalaseId));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);

                    addNewEtalaseToChip(selectedEtalaseId, selectedEtalaseName);
                    if (shopPageTracking != null) {
                        shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product),
                                false, selectedEtalaseName, shopId, shopProductListPresenter.isMyShop(shopId),
                                ShopPageTracking.getShopType(shopInfo.getInfo()));
                    }
                    needReloadData = true;
                }
                break;

            case REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    String sortId = data.getStringExtra(ShopProductSortActivity.SORT_ID);
                    sortValue = data.getStringExtra(ShopProductSortActivity.SORT_NAME);
                    this.isLoadingInitialData = true;
                    loadInitialData();
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
            loadInitialData();
            needReloadData = false;
        }
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
        shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_SELECTED_ETALASE_LIST, selectedEtalaseList);
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId);
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName);
        outState.putString(SAVED_SORT_VALUE, sortValue);
        outState.putString(SAVED_KEYWORD, keyword);
    }

}
