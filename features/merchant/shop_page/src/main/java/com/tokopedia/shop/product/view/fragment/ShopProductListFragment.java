package com.tokopedia.shop.product.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer;
import com.tokopedia.shop.analytic.ShopPageTrackingConstant;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.shop.analytic.model.ListTitleTypeDef;
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.etalase.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.adapter.EtalaseChipAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductDedicatedListView;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListPresenter;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.shop.common.constant.ShopPageConstant.ETALASE_TO_SHOW;

public class ShopProductListFragment extends BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements ShopProductDedicatedListView, WishListActionListener, BaseEmptyViewHolder.Callback, ShopProductClickedListener,
        ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener, EtalaseChipAdapter.OnEtalaseChipAdapterListener {

    private static final int REQUEST_CODE_USER_LOGIN = 100;
    private static final int REQUEST_CODE_ETALASE = 200;
    private static final int REQUEST_CODE_SORT = 300;

    private ShopProductAdapter shopProductAdapter;

    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 2;

    public static final String SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list";
    public static final String SAVED_SELECTED_ETALASE_ID = "saved_etalase_id";
    public static final String SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name";
    public static final String SAVED_SHOP_ID = "saved_shop_id";
    public static final String SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official";
    public static final String SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant";
    public static final String SAVED_KEYWORD = "saved_keyword";
    public static final String SAVED_SORT_VALUE = "saved_sort_name";

    @Inject
    ShopProductListPresenter shopProductListPresenter;

    ShopPageTrackingBuyer shopPageTracking;
    @Inject
    UserSession userSession;

    private ShopModuleRouter shopModuleRouter;

    private String shopId;
    private String keyword;
    private String prevAnalyticKeyword = "";
    private String sortValue;
    private String attribution;

    private ArrayList<ShopEtalaseViewModel> selectedEtalaseList;
    private List<ShopEtalaseViewModel> shopEtalaseViewModelList;

    private RecyclerView recyclerView;
    private BottomActionView bottomActionView;
    private ShopInfo shopInfo;

    private String selectedEtalaseId;
    private String selectedEtalaseName;

    private OnShopProductListFragmentListener onShopProductListFragmentListener;
    private boolean needReloadData;
    private View vgEtalaseList;
    private EtalaseChipAdapter etalaseChipAdapter;
    private boolean isOfficialStore, isGoldMerchant;

    public interface OnShopProductListFragmentListener {
        void updateUIByShopName(String shopName);
    }

    public static ShopProductListFragment createInstance(String shopId,
                                                         String keyword,
                                                         String etalaseId,
                                                         String sort,
                                                         String attribution) {
        ShopProductListFragment shopProductListFragment = new ShopProductListFragment();
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
                this, null, this,
                this, null,
                true, 0, ShopTrackProductTypeDef.PRODUCT
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
                emptyModel.setTitle(getString(R.string.shop_product_empty_product_title_no_etalase));
            } else {
                emptyModel.setTitle(getString(R.string.shop_product_empty_product_title_etalase));
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
        attribution = getArguments().getString(ShopParamConstant.EXTRA_ATTRIBUTION, "");
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            selectedEtalaseList = new ArrayList<>();
            selectedEtalaseId = getArguments().getString(ShopParamConstant.EXTRA_ETALASE_ID, "");
            selectedEtalaseName = "";
            keyword = getArguments().getString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, "");
            sortValue = getArguments().getString(ShopParamConstant.EXTRA_SORT_ID, String.valueOf(Integer.MIN_VALUE));
            shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID, "");
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST);
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID);
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME);
            keyword = savedInstanceState.getString(SAVED_KEYWORD);
            sortValue = savedInstanceState.getString(SAVED_SORT_VALUE);
            shopId = savedInstanceState.getString(SAVED_SHOP_ID);
        }
        super.onCreate(savedInstanceState);
        shopPageTracking = new ShopPageTrackingBuyer((AbstractionRouter) getActivity().getApplication(),
                new TrackingQueue(getContext()));
        etalaseChipAdapter = new EtalaseChipAdapter(null, null, this);
        shopProductListPresenter.attachView(this, this);
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
        vgEtalaseList = view.findViewById(R.id.vg_etalase_list);
        setUpEtalaseView(view);
        return view;
    }

    public void setUpEtalaseView(View view) {
        RecyclerView recyclerViewEtalase = view.findViewById(R.id.recycler_view_etalase);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEtalase.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = recyclerViewEtalase.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerViewEtalase.setAdapter(etalaseChipAdapter);

        View buttonEtalaseMore = view.findViewById(R.id.v_etalase_more);
        buttonEtalaseMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEtalaseMoreListClicked();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    shopPageTracking.clickSort(isOwner(),
                            CustomDimensionShopPage.create(shopInfo));
                }
                Intent intent = ShopProductSortActivity.createIntent(getActivity(), sortValue);
                ShopProductListFragment.this.startActivityForResult(intent, REQUEST_CODE_SORT);
            }
        });

    }

    private void showEtalaseList() {
        vgEtalaseList.setVisibility(View.VISIBLE);
    }

    private void hideEtalaseList() {
        vgEtalaseList.setVisibility(View.GONE);
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (shopProductAdapter.getItemViewType(position) == ShopProductViewHolder.GRID_LAYOUT) {
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
        shopProductListPresenter.clearCache();
        shopEtalaseViewModelList = null;
        hideEtalaseList();
        etalaseChipAdapter.setEtalaseViewModelList(null);
        shopProductAdapter.setShopEtalaseTitle(null, null);
        super.onSwipeRefresh();
    }

    public void updateDataByChangingKeyword(String keyword) {
        if (!this.keyword.equalsIgnoreCase(keyword)) {
            this.keyword = keyword;
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
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId);
        etalaseChipAdapter.notifyDataSetChanged();
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, shopEtalaseViewModel.getEtalaseBadge());
        if (shopPageTracking != null) {
            shopPageTracking.clickEtalaseChip(isOwner(),
                    selectedEtalaseName, CustomDimensionShopPage.create(shopInfo));
        }
        // no need ro rearraged, just notify the adapter to reload product list by etalase id

        loadInitialData();
    }

    private void bindEtalaseChipData(ShopProductEtalaseListViewModel shopProductEtalaseListViewModel) {
        etalaseChipAdapter.setEtalaseViewModelList(shopProductEtalaseListViewModel.getEtalaseModelList());
        String selectedEtalaseId = shopProductEtalaseListViewModel.getSelectedEtalaseId();
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId);
        etalaseChipAdapter.notifyDataSetChanged();
        showEtalaseList();
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
        if (shopEtalaseViewModelList == null || shopEtalaseViewModelList.size() == 0) {
            shopProductListPresenter.getShopEtalase(shopId, shopId.equals(userSession.getShopId()));
        } else {
            // continue to load ProductData
            boolean isUseAce = isUseAce(shopEtalaseViewModelList, selectedEtalaseId);
            loadShopPageList(shopInfo, page, isUseAce);
        }
    }

    private void loadShopPageList(ShopInfo shopInfo, int page, boolean isUseAce) {
        shopProductListPresenter.getShopPageList(shopInfo,
                keyword,
                selectedEtalaseId,
                0,
                page,
                Integer.valueOf(sortValue),
                isUseAce);
    }

    private boolean isUseAce(List<ShopEtalaseViewModel> etalaseViewModelList, String selectedEtalaseId) {
        if (etalaseViewModelList != null) {
            for (ShopEtalaseViewModel shopEtalaseViewModel : etalaseViewModelList) {
                if (shopEtalaseViewModel.getEtalaseId().equalsIgnoreCase(selectedEtalaseId)) {
                    return shopEtalaseViewModel.isUseAce();
                }
            }
        }
        return true;
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

    private boolean isOwner(){
        return shopProductListPresenter.isMyShop(shopId);
    }

    @Override
    public void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage) {
        if (list.size() > 0) {
            shopPageTracking.impressionProductList(
                    isOwner(),
                    TextUtils.isEmpty(keyword) ? ListTitleTypeDef.ETALASE : ListTitleTypeDef.SEARCH_RESULT,
                    selectedEtalaseName, CustomDimensionShopPageAttribution.create(shopInfo, "", attribution),
                    list, shopProductAdapter.getShopProductViewModelList().size(), shopId, shopInfo.getInfo().getShopName()
            );
        }
        if (!TextUtils.isEmpty(keyword) && !prevAnalyticKeyword.equals(keyword)) {
            shopPageTracking.searchKeyword(isOwner(),
                    keyword,
                    list.size() > 0,
                    CustomDimensionShopPage.create(shopInfo));
            prevAnalyticKeyword = keyword;
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
            shopPageTracking.clickMoreMenuChip(isOwner(),
                    CustomDimensionShopPage.create(shopInfo));

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
    public void onProductClicked(ShopProductViewModel shopProductViewModel, @ShopTrackProductTypeDef int shopTrackType,
                                 int productPosition) {
        if (shopInfo != null) {
            // shopTrackType is always from product
            shopPageTracking.clickProductPicture(
                    isOwner(),
                    TextUtils.isEmpty(keyword) ? ListTitleTypeDef.ETALASE : ListTitleTypeDef.SEARCH_RESULT,
                    selectedEtalaseName,
                    CustomDimensionShopPageAttribution.create(shopInfo, shopProductViewModel.getId(), attribution),
                    shopProductViewModel, productPosition, shopId, shopInfo.getInfo().getShopName());
        }
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getId(), shopProductViewModel.getName(),
                shopProductViewModel.getDisplayedPrice(), shopProductViewModel.getImageUrl(), attribution,
                shopPageTracking.getListNameOfProduct(ShopPageTrackingConstant.SEARCH, selectedEtalaseName));
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        shopProductAdapter.updateWishListStatus(productId, true);
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        shopProductAdapter.updateWishListStatus(productId, false);
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        onErrorAddToWishList(new MessageErrorException(errorMessage));
    }

    @Override
    public void onErrorAddToWishList(Throwable e) {
        if (!shopProductListPresenter.isLogin()) {
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
        this.shopId = shopInfo.getInfo().getShopId();
        this.isOfficialStore = shopInfo.getInfo().isShopOfficial();
        this.isGoldMerchant = shopInfo.getInfo().isGoldMerchant();
        onShopProductListFragmentListener.updateUIByShopName(shopInfo.getInfo().getShopName());
        loadInitialData();
    }

    @Override
    public void onErrorGetShopInfo(Throwable e) {
        showGetListError(e);
    }

    public void addToSelectedEtalaseList(String etalaseId, String etalaseName, boolean useAce,
                                         String etalaseBadge) {
        // only add the etalase with not-empty name. Empty name is a deleted etalase (that come from deeplink)
        if (TextUtils.isEmpty(etalaseName)) {
            return;
        }
        // if etalase id is not on the list, add it
        boolean isAddedToCurrentEtalaseList = addEtalaseFromListMore(etalaseId, etalaseName, useAce);
        if (isAddedToCurrentEtalaseList) {
            selectedEtalaseList.add(0, new ShopEtalaseViewModel(selectedEtalaseId, selectedEtalaseName, useAce,
                    ShopEtalaseTypeDef.ETALASE_CUSTOM, false));
            if (selectedEtalaseList.size() > ShopPageConstant.MAXIMUM_SELECTED_ETALASE_LIST) {
                selectedEtalaseList.remove(selectedEtalaseList.size() - 1);
            }
        }
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId);
        etalaseChipAdapter.notifyDataSetChanged();
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge);
    }

    public void addEtalaseToChip(String etalaseId, String etalaseName, boolean useAce) {
        // add the etalase by permutation
        // 1 2 3 4 5; after add 6 will be 1 6 2 3 4
        List<ShopEtalaseViewModel> shopEtalaseViewModelList = etalaseChipAdapter.getEtalaseViewModelList();
        ShopEtalaseViewModel shopEtalaseViewModelToAdd = new ShopEtalaseViewModel(etalaseId, etalaseName, useAce,
                ShopEtalaseTypeDef.ETALASE_CUSTOM, false);
        // index no 0 will always be "All Etalase", so, add from index 1.
        int indexToAdd = shopEtalaseViewModelList.size() > 1 ? 1 : 0;
        shopEtalaseViewModelList.add(indexToAdd, shopEtalaseViewModelToAdd);
        if (shopEtalaseViewModelList.size() > ShopPageConstant.ETALASE_TO_SHOW) {
            shopEtalaseViewModelList.remove(shopEtalaseViewModelList.size() - 1);
        }
    }

    public boolean isEtalaseInChip(String etalaseId) {
        List<ShopEtalaseViewModel> shopEtalaseViewModelList = etalaseChipAdapter.getEtalaseViewModelList();
        for (int i = 0, sizei = shopEtalaseViewModelList.size(); i < sizei; i++) {
            if (shopEtalaseViewModelList.get(i).getEtalaseId().equalsIgnoreCase(etalaseId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true, if add etalase to current list; false if no add needed.
     */
    public boolean addEtalaseFromListMore(String etalaseId, String etalaseName, boolean useAce) {
        if (isEtalaseInChip(etalaseId)) {
            return false;
        }
        addEtalaseToChip(etalaseId, etalaseName, useAce);
        return true;
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel, @ShopTrackProductTypeDef int shopTrackType) {
        if (shopInfo != null) {
            //shopTrackType is always from Product
            shopPageTracking.clickWishlist(!shopProductViewModel.isWishList(),
                    TextUtils.isEmpty(keyword) ? ListTitleTypeDef.ETALASE : ListTitleTypeDef.SEARCH_RESULT,
                    selectedEtalaseName,
                    CustomDimensionShopPageProduct.create(shopInfo, shopProductViewModel.getId()));
        }
        if (shopProductViewModel.isWishList()) {
            shopProductListPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductListPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onErrorGetEtalaseList(Throwable e) {
        // etalase load is error
        etalaseChipAdapter.setEtalaseViewModelList(null);
        hideEtalaseList();
        shopProductAdapter.setShopEtalaseTitle(null, null);
        // assume use ace is true, to continue load product.
        loadShopPageList(shopInfo, getDefaultInitialPage(), true);
    }

    @Override
    public void onSuccessGetEtalaseList(@NonNull List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        this.shopEtalaseViewModelList = shopEtalaseViewModelList;
        boolean isUseAce = true;
        String etalaseBadge = null;

        if (shopEtalaseViewModelList.size() == 0) {
            selectedEtalaseId = null;
            selectedEtalaseName = null;
        } else {
            // id might come from deeplink
            if (!TextUtils.isEmpty(selectedEtalaseId)) {
                for (ShopEtalaseViewModel etalaseModel : shopEtalaseViewModelList) {
                    if (selectedEtalaseId.equalsIgnoreCase(etalaseModel.getEtalaseId())) {
                        selectedEtalaseName = etalaseModel.getEtalaseName();
                        isUseAce = etalaseModel.isUseAce();
                        etalaseBadge = etalaseModel.getEtalaseBadge();
                        break;
                    }
                }
                // etalase name still empty, then we check the selectedEtalaseId with name.
                if (TextUtils.isEmpty(selectedEtalaseName)) {
                    String cleanedSelectedEtalaseId = cleanString(selectedEtalaseId);
                    for (ShopEtalaseViewModel etalaseModel : shopEtalaseViewModelList) {
                        String cleanedEtalaseName = cleanString(etalaseModel.getEtalaseName());
                        if (cleanedSelectedEtalaseId.equalsIgnoreCase(cleanedEtalaseName)) {
                            selectedEtalaseId = etalaseModel.getEtalaseId();
                            selectedEtalaseName = etalaseModel.getEtalaseName();
                            isUseAce = etalaseModel.isUseAce();
                            etalaseBadge = etalaseModel.getEtalaseBadge();
                            break;
                        }
                    }
                    // name is empty means etalase is deleted, so no need to add to chip, and make it to all etalase.
                    if (TextUtils.isEmpty(selectedEtalaseName)) {
                        selectedEtalaseId = "";
                    }
                }
            }

            // if id not exist, set default to index 0
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                ShopEtalaseViewModel firstModel = shopEtalaseViewModelList.get(0);
                selectedEtalaseId = firstModel.getEtalaseId();
                selectedEtalaseName = firstModel.getEtalaseName();
                isUseAce = firstModel.isUseAce();
                etalaseBadge = firstModel.getEtalaseBadge();
            }
        }

        /// limit etalase to show in chip
        List<ShopEtalaseViewModel> shopEtalaseModelListToShow;
        if (shopEtalaseViewModelList.size() > ShopPageConstant.ETALASE_TO_SHOW) {
            shopEtalaseModelListToShow = shopEtalaseViewModelList.subList(0, ETALASE_TO_SHOW);
        } else {
            shopEtalaseModelListToShow = shopEtalaseViewModelList;
        }
        // update the adapter
        bindEtalaseChipData(new ShopProductEtalaseListViewModel(shopEtalaseModelListToShow, selectedEtalaseId));
        addToSelectedEtalaseList(selectedEtalaseId, selectedEtalaseName, isUseAce, etalaseBadge);
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge);

        // continue to load ProductData
        loadShopPageList(shopInfo, getDefaultInitialPage(), isUseAce);
    }

    private String cleanString(String text) {
        return text.replaceAll("[\\W_]", "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);
                    boolean useAce = data.getBooleanExtra(ShopParamConstant.EXTRA_USE_ACE, true);
                    String etalaseBadge = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_BADGE);

                    addToSelectedEtalaseList(selectedEtalaseId, selectedEtalaseName, useAce, etalaseBadge);
                    if (shopPageTracking != null) {
                        shopPageTracking.clickMenuFromMoreMenu(isOwner(),
                                selectedEtalaseName, CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant));
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
                    if (shopPageTracking != null) {
                        shopPageTracking.clickSortBy(isOwner(),
                                sortValue, CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant));
                    }
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
    public void onPause() {
        super.onPause();
        shopPageTracking.sendAllTrackingQueue();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_SELECTED_ETALASE_LIST, selectedEtalaseList);
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId);
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName);
        outState.putString(SAVED_SORT_VALUE, sortValue);
        outState.putString(SAVED_KEYWORD, keyword);
        outState.putString(SAVED_SHOP_ID, shopId);
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore);
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant);
    }

}
