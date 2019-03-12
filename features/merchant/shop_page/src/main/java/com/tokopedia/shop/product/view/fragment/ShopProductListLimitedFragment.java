package com.tokopedia.shop.product.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.network.TextApiUtils;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConstInternal;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent;
import com.tokopedia.merchantvoucher.common.di.MerchantVoucherComponent;
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException;
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult;
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel;
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity;
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity;
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter;
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView;
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.shop.analytic.model.ListTitleTypeDef;
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.page.view.listener.ShopPageView;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.ShopMerchantVoucherViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductLimitedListPresenter;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.FEATURED_PRODUCT;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.ETALASE_TO_SHOW;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedFragment extends BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements ShopProductListView, WishListActionListener, BaseEmptyViewHolder.Callback,
        ShopProductPromoViewHolder.PromoViewHolderListener, ShopProductClickedListener,
        ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener, ShopCarouselSeeAllClickedListener, MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener, MerchantVoucherListView {

    private static final int REQUEST_CODE_USER_LOGIN = 100;
    private static final int REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101;
    private static final int REQUEST_CODE_ETALASE = 205;
    private static final int REQUEST_CODE_LOGIN_USE_VOUCHER = 206;
    private static final int REQUEST_CODE_MERCHANT_VOUCHER = 207;
    private static final int REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 208;

    private static final int REQUEST_CODE_SORT = 300;
    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 2;

    private static final String SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION";

    public static final String SAVED_SELECTED_ETALASE_ID = "saved_etalase_id";
    public static final String SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name";
    public static final String SAVED_SHOP_ID = "saved_shop_id";
    public static final String SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official";
    public static final String SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant";
    public static final int NUM_VOUCHER_DISPLAY = 3;

    @Inject
    ShopProductLimitedListPresenter shopProductLimitedListPresenter;

    ShopPageTrackingBuyer shopPageTracking;

    private UserSessionInterface userSession;
    MerchantVoucherListPresenter merchantVoucherListPresenter;

    private ProgressDialog progressDialog;
    private String urlNeedTobBeProceed;
    private String attribution;
    private ShopInfo shopInfo;
    private ShopModuleRouter shopModuleRouter;
    private BottomActionView bottomActionView;

    private String sortName = Integer.toString(Integer.MIN_VALUE);
    private RecyclerView recyclerView;

    private String selectedEtalaseId;
    private String selectedEtalaseName;

    private ShopProductAdapter shopProductAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean needReloadData;
    private List<ShopEtalaseViewModel> highlightEtalaseViewModelList;
    private List<ShopEtalaseViewModel> shopEtalaseViewModelList;
    private boolean needLoadVoucher;

    private String shopId = null;
    private boolean isOfficialStore, isGoldMerchant;

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
        View view = inflater.inflate(R.layout.fragment_shop_product_limited_list, container, false);
        bottomActionView = view.findViewById(R.id.bottom_action_view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        userSession = new UserSession(getActivity());
        if (savedInstanceState == null) {
            selectedEtalaseId = null;
            selectedEtalaseName = "";
        } else {
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID);
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME);
            shopId = savedInstanceState.getString(SAVED_SHOP_ID);
            isGoldMerchant = savedInstanceState.getBoolean(SAVED_SHOP_IS_GOLD_MERCHANT);
            isOfficialStore = savedInstanceState.getBoolean(SAVED_SHOP_IS_OFFICIAL);
        }
        super.onCreate(savedInstanceState);
        shopPageTracking = new ShopPageTrackingBuyer((AbstractionRouter) getActivity().getApplication(),
                new TrackingQueue(getContext()));
        MerchantVoucherComponent merchantVoucherComponent = DaggerMerchantVoucherComponent.builder()
                .baseAppComponent(((BaseMainApplication) (getActivity().getApplication())).getBaseAppComponent())
                .shopCommonModule(new ShopCommonModule())
                .build();
        merchantVoucherListPresenter = merchantVoucherComponent.merchantVoucherListPresenter();
        merchantVoucherListPresenter.attachView(this);

        attribution = getArguments().getString(SHOP_ATTRIBUTION, "");
        shopProductLimitedListPresenter.attachView(this, this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo != null) {
                    Intent intent = ShopProductSortActivity.createIntent(getActivity(), sortName);
                    ShopProductListLimitedFragment.this.startActivityForResult(intent, REQUEST_CODE_SORT);
                }
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
        this.isOfficialStore = shopInfo.getInfo().isShopOfficial();
        this.isGoldMerchant = shopInfo.getInfo().isGoldMerchant();
        this.shopId = shopInfo.getInfo().getShopId();
        loadInitialData();
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        this.isOfficialStore = shopInfo.getInfo().isShopOfficial();
        this.isGoldMerchant = shopInfo.getInfo().isGoldMerchant();
        this.shopId = shopInfo.getInfo().getShopId();
    }

    public void clearCache() {
        merchantVoucherListPresenter.clearCache();
        shopProductLimitedListPresenter.clearCache();
    }

    // load data promo/featured/etalase
    protected void loadInitialData() {
        shopProductAdapter.clearAllNonDataElement();
        loadTopData();
        reloadProductData(true);
    }

    // load product list first time
    private void reloadProductData(boolean needLoadEtalaseHighlight) {
        isLoadingInitialData = true;
        bottomActionView.hide(false);
        shopProductAdapter.clearAllNonDataElement();
        shopProductAdapter.clearProductList();
        if (needLoadEtalaseHighlight) {
            shopProductAdapter.setShopProductEtalaseHighlightViewModel(null);
        }

        showLoading();
        shopId = shopInfo.getInfo().getShopId();
        if (shopEtalaseViewModelList == null || shopEtalaseViewModelList.size() == 0) {
            //load etalase list first
            shopProductLimitedListPresenter.getShopEtalaseListByShop(shopId,
                    shopId.equals(userSession.getShopId()));
        } else {
            loadData(getDefaultInitialPage());

            if (needLoadEtalaseHighlight) {
                loadEtalaseHighLight();
            }
        }
    }

    protected void loadTopData() {
        if (shopInfo != null) {
            shopProductAdapter.clearPromoData();
            shopProductAdapter.clearMerchantVoucherData();
            shopProductAdapter.clearFeaturedData();

            shopProductLimitedListPresenter.loadProductPromoModel(getOfficialWebViewUrl(shopInfo));

            loadVoucherList();

            shopProductLimitedListPresenter.getProductFeatureListWithAttributes(
                    shopInfo.getInfo().getShopId(),
                    shopInfo.getInfo().isShopOfficial());

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
            boolean isUseAce = true;
            if (shopEtalaseViewModelList.size() > 0) {
                if (!TextUtils.isEmpty(selectedEtalaseId)) {
                    isUseAce = isUseAce(shopEtalaseViewModelList, selectedEtalaseId);
                }
                shopProductLimitedListPresenter.getProductListWithAttributes(
                        shopInfo.getInfo().getShopId(),
                        !shopInfo.getInfo().isOpen(),
                        shopInfo.getInfo().isShopOfficial(),
                        page,
                        ShopPageConstant.DEFAULT_PER_PAGE,
                        selectedEtalaseId,
                        isUseAce);
            }

        }
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
    public void renderShopProductPromo(ShopProductPromoViewModel shopProductPromoViewModel) {
        shopProductAdapter.setShopProductPromoViewModel(shopProductPromoViewModel);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        gridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
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

    public String getSelectedEtalaseId() {
        return selectedEtalaseId;
    }

    @NonNull
    @Override
    protected ShopProductAdapterTypeFactory getAdapterTypeFactory() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int deviceWidth = displaymetrics.widthPixels;
        return new ShopProductAdapterTypeFactory(this,
                this, this, this,
                this, this,
                true, deviceWidth, ShopTrackProductTypeDef.PRODUCT
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
            if (shopInfo != null) {
                shopPageTracking.impressionZeroProduct(CustomDimensionShopPage.create(shopInfo));
            }
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
        if (merchantVoucherListPresenter != null) {
            merchantVoucherListPresenter.detachView();
        }
    }

    private void showUseMerchantVoucherLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.title_loading));
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.show();
    }

    private void hideUseMerchantVoucherLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onItemClicked(BaseShopProductViewModel baseShopProductViewModel) {
        // no op
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        onErrorAddToWishList(new MessageErrorException(errorMessage));
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
    public void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage) {
        if (list.size() > 0) {
            shopPageTracking.impressionProductList(
                    isOwner(),
                    ListTitleTypeDef.ETALASE,
                    selectedEtalaseName, CustomDimensionShopPageAttribution.create(shopInfo, "", attribution),
                    list, shopProductAdapter.getShopProductViewModelList().size(), shopInfo.getInfo().getShopId(), shopInfo.getInfo().getShopName()
            );
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
                shopProductAdapter.setNeedToShowEtalase(true);
                shopProductAdapter.addElement(getEmptyDataViewModel());
            } else {
                shopProductAdapter.setNeedToShowEtalase(false);
                shopProductAdapter.clearAllNonDataElement();
            }
            bottomActionView.setVisibility(View.GONE);
            bottomActionView.hide();
        } else {
            shopProductAdapter.setNeedToShowEtalase(true);
            bottomActionView.setVisibility(View.VISIBLE);
            bottomActionView.show();
            isLoadingInitialData = false;
        }
        shopProductAdapter.notifyDataSetChanged();
        shopProductAdapter.refreshSticky();
        if(getActivity() instanceof ShopPageView){
            ((ShopPageView) getActivity()).stopPerformanceMonitor();
        }
    }

    @Override
    public void onSuccessGetEtalaseHighlight(@NonNull List<List<ShopProductViewModel>> list) {
        ArrayList<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModels = new ArrayList<>();
        CustomDimensionShopPageAttribution customDimensionShopPageAttribution = CustomDimensionShopPageAttribution.create(shopInfo, null, attribution);
        for (int i = 0, sizei = list.size(); i < sizei; i++) {
            List<ShopProductViewModel> shopProductViewModelList = list.get(i);
            etalaseHighlightCarouselViewModels.add(
                    new EtalaseHighlightCarouselViewModel(shopProductViewModelList, highlightEtalaseViewModelList.get(i)));
            if (shopInfo != null && shopProductViewModelList != null && shopProductViewModelList.size() > 0) {
                shopPageTracking.impressionProductList(isOwner(),
                        ListTitleTypeDef.HIGHLIGHTED,
                        highlightEtalaseViewModelList.get(i).getEtalaseName(),
                        customDimensionShopPageAttribution,
                        shopProductViewModelList, 0, shopInfo.getInfo().getShopId(), shopInfo.getInfo().getShopName());
            }
        }
        shopProductAdapter.setShopProductEtalaseHighlightViewModel(
                new ShopProductEtalaseHighlightViewModel(etalaseHighlightCarouselViewModels));
        shopProductAdapter.refreshSticky();
    }

    @Override
    public void onErrorGetEtalaseHighlight(Throwable e) {
        shopProductAdapter.setShopProductEtalaseHighlightViewModel(null);
    }

    @Override
    public void onErrorGetProductFeature(Throwable e) {
        shopProductAdapter.setShopProductFeaturedViewModel(null);
    }

    @Override
    public void onSuccessGetProductFeature(@NonNull List<ShopProductViewModel> list) {
        shopProductAdapter.setShopProductFeaturedViewModel(new ShopProductFeaturedViewModel(list));
        if (list.size() > 0) {
            shopPageTracking.impressionProductList(
                    isOwner(),
                    ListTitleTypeDef.HIGHLIGHTED,
                    FEATURED_PRODUCT,
                    CustomDimensionShopPageAttribution.create(shopInfo, "", attribution),
                    list, 0,
                    shopInfo.getInfo().getShopId(), shopInfo.getInfo().getShopName());
        }
        shopProductAdapter.refreshSticky();
    }

    @Override
    public void showGetListError(Throwable throwable) {
        if(getActivity() instanceof ShopPageView){
            ((ShopPageView)getActivity()).stopPerformanceMonitor();
        }
        hideLoading();
        updateStateScrollListener();
        if (shopProductAdapter.getShopProductViewModelList().size() > 0) {
            onGetListErrorWithExistingData(throwable);
        } else {
            onGetListErrorWithEmptyData(throwable);
        }
    }

    @Override
    public void onSuccessGetEtalaseListByShop(ArrayList<ShopEtalaseViewModel> shopEtalaseModelList) {
        //default select first index as selected.
        this.shopEtalaseViewModelList = shopEtalaseModelList;
        String etalaseBadge = null;

        if (TextUtils.isEmpty(selectedEtalaseId) &&
                shopEtalaseModelList != null &&
                shopEtalaseModelList.size() > 0) {
            ShopEtalaseViewModel shopEtalaseViewModel = shopEtalaseModelList.get(0);
            selectedEtalaseId = shopEtalaseViewModel.getEtalaseId();
            selectedEtalaseName = shopEtalaseViewModel.getEtalaseName();
            etalaseBadge = shopEtalaseViewModel.getEtalaseBadge();
        }
        // update the adapter
        List<ShopEtalaseViewModel> shopEtalaseModelListToShow;
        if (shopEtalaseModelList != null && shopEtalaseModelList.size() > ShopPageConstant.ETALASE_TO_SHOW) {
            shopEtalaseModelListToShow = shopEtalaseModelList.subList(0, ETALASE_TO_SHOW);
        } else {
            shopEtalaseModelListToShow = shopEtalaseModelList;
        }
        shopProductAdapter.setShopEtalase(new ShopProductEtalaseListViewModel(shopEtalaseModelListToShow, selectedEtalaseId));
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge);

        loadData(getDefaultInitialPage());

        loadEtalaseHighLight();
    }

    private void loadEtalaseHighLight() {
        // load etalase highlight
        highlightEtalaseViewModelList = new ArrayList<>();
        if (shopEtalaseViewModelList != null) {
            for (ShopEtalaseViewModel shopEtalaseViewModel : shopEtalaseViewModelList) {
                if (shopEtalaseViewModel.isHighlight()) {
                    highlightEtalaseViewModelList.add(shopEtalaseViewModel);
                }
            }
        }
        shopId = shopInfo.getInfo().getShopId();
        shopProductLimitedListPresenter.getProductListHighlight(shopId,
                !shopInfo.getInfo().isOpen(),
                shopInfo.getInfo().isShopOfficial(),
                highlightEtalaseViewModelList);
    }

    @Override
    public void onErrorGetEtalaseListByShop(Throwable e) {
        shopProductAdapter.setShopEtalase(null);
        shopProductAdapter.setShopEtalaseTitle(null, null);
        shopProductAdapter.setShopProductEtalaseHighlightViewModel(null);
        showGetListError(e);
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // no-op
    }

    @Override
    public void onEmptyButtonClicked() {
        if (shopInfo != null) {
            shopPageTracking.clickZeroProduct(CustomDimensionShopPage.create(shopInfo));
        }
        ((ShopModuleRouter) getActivity().getApplication()).goToAddProduct(getActivity());
    }

    @Override
    public void promoClicked(String url) {
        if (getActivity() == null) {
            return;
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
        if (shopProductAdapter.isLoading()) {
            return;
        }
        selectedEtalaseId = shopEtalaseViewModel.getEtalaseId();
        selectedEtalaseName = shopEtalaseViewModel.getEtalaseName();
        shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId);
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, shopEtalaseViewModel.getEtalaseBadge());
        if (shopPageTracking != null) {
            shopId = shopInfo.getInfo().getShopId();
            shopPageTracking.clickEtalaseChip(
                    shopProductLimitedListPresenter.isMyShop(shopId),
                    selectedEtalaseName,
                    CustomDimensionShopPage.create(shopInfo));
        }
        //this is to reset fling and initial load position
        recyclerView.smoothScrollBy(1, 1);
        gridLayoutManager.scrollToPositionWithOffset(DEFAULT_ETALASE_POSITION, 0);

        // no need ro rearraged, just notify the adapter to reload product list by etalase id
        reloadProductData(false);
    }

    @Override
    public void onEtalaseMoreListClicked() {
        if (shopInfo != null) {
            shopPageTracking.clickMoreMenuChip(
                    shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                    CustomDimensionShopPage.create(shopInfo));

            Intent shopEtalaseIntent = ShopEtalasePickerActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId(), selectedEtalaseId,
                    true, false);
            startActivityForResult(shopEtalaseIntent, REQUEST_CODE_ETALASE);
        }
    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel, @ShopTrackProductTypeDef int shopTrackType) {
        if (shopInfo != null) {
            if (shopTrackType == ShopTrackProductTypeDef.FEATURED) {
                shopPageTracking.clickWishlist(
                        !shopProductViewModel.isWishList(),
                        ListTitleTypeDef.HIGHLIGHTED, FEATURED_PRODUCT,
                        CustomDimensionShopPageProduct.create(shopInfo, shopProductViewModel.getId()));
            } else if (shopTrackType == ShopTrackProductTypeDef.PRODUCT) {
                shopPageTracking.clickWishlist(
                        !shopProductViewModel.isWishList(),
                        ListTitleTypeDef.ETALASE, selectedEtalaseName,
                        CustomDimensionShopPageProduct.create(shopInfo, shopProductViewModel.getId()));
            } else { // highlight
                shopPageTracking.clickWishlist(
                        !shopProductViewModel.isWishList(),
                        ListTitleTypeDef.HIGHLIGHTED,
                        shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                        CustomDimensionShopPageProduct.create(shopInfo, shopProductViewModel.getId()));
            }
        }
        if (shopProductViewModel.isWishList()) {
            shopProductLimitedListPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductLimitedListPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onSeeAllClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        if (shopInfo != null) {
            shopPageTracking.clickHighLightSeeAll(isOwner(), shopEtalaseViewModel.getEtalaseName(),
                    CustomDimensionShopPage.create(shopInfo));
        }
        Intent intent = ShopProductListActivity.createIntent(getActivity(),
                shopInfo.getInfo().getShopId(), "",
                shopEtalaseViewModel.getEtalaseId(), attribution, sortName);
        startActivity(intent);
    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel, @ShopTrackProductTypeDef int shopTrackType,
                                 int productPosition) {
        if (shopInfo != null) {
            if (shopTrackType == ShopTrackProductTypeDef.FEATURED) {
                shopPageTracking.clickProductPicture(isOwner(),
                        ListTitleTypeDef.HIGHLIGHTED,
                        FEATURED_PRODUCT,
                        CustomDimensionShopPageAttribution.create(shopInfo, shopProductViewModel.getId(), attribution),
                        shopProductViewModel, productPosition, shopInfo.getInfo().getShopId(), shopInfo.getInfo().getShopName());
            } else if (shopTrackType == ShopTrackProductTypeDef.PRODUCT) {
                shopPageTracking.clickProductPicture(isOwner(),
                        ListTitleTypeDef.ETALASE,
                        selectedEtalaseName,
                        CustomDimensionShopPageAttribution.create(shopInfo, shopProductViewModel.getId(), attribution),
                        shopProductViewModel, productPosition, shopInfo.getInfo().getShopId(), shopInfo.getInfo().getShopName());
            } else if (shopTrackType == ShopTrackProductTypeDef.ETALASE_HIGHLIGHT) {
                shopPageTracking.clickProductPicture(isOwner(),
                        ListTitleTypeDef.HIGHLIGHTED,
                        shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                        CustomDimensionShopPageAttribution.create(shopInfo, shopProductViewModel.getId(), attribution),
                        shopProductViewModel, productPosition, shopInfo.getInfo().getShopId(), shopInfo.getInfo().getShopName());
            }
        }
        goToPDP(shopProductViewModel.getId());


    }

    /**
     * This function is temporary for testing to avoid router and applink
     * For Dynamic Feature Support
     */
    private void goToPDP(String productId) {
        startActivity(getProductIntent(productId));
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntentInternal(getContext(),
                    UriUtil.buildUri(ApplinkConstInternal.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK && shopProductLimitedListPresenter != null && shopInfo != null
                        && data != null) {
                    String etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    String etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);
                    String etalaseBadge = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_BADGE);

                    // if etalase id is on the list, refresh this page; if etalase id is in other list, go to new page.
                    if (shopProductAdapter.isEtalaseInChip(etalaseId)) {
                        this.selectedEtalaseId = etalaseId;
                        this.selectedEtalaseName = etalaseName;
                    } else {
                        if (shopInfo != null) {
                            Intent intent = ShopProductListActivity.createIntent(getActivity(),
                                    shopInfo.getInfo().getShopId(), "",
                                    etalaseId, attribution, sortName);
                            startActivity(intent);
                        }
                    }
                    shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId);
                    shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge);

                    needReloadData = true;

                    if (shopPageTracking != null && shopInfo != null) {
                        shopPageTracking.clickMenuFromMoreMenu(
                                shopProductLimitedListPresenter.isMyShop(shopInfo.getInfo().getShopId()),
                                etalaseName,
                                CustomDimensionShopPage.create(shopInfo));
                    }
                }
                break;
            case REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW:
                if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(urlNeedTobBeProceed)) {
                    promoClicked(urlNeedTobBeProceed);
                }
                break;
            case REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME);
                    if (shopId == null)
                        return;

                    if (shopPageTracking != null) {
                        shopPageTracking.clickSortBy(shopProductLimitedListPresenter.isMyShop(shopId),
                                sortName, CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant));
                    }
                    startActivity(ShopProductListActivity.createIntent(getActivity(), shopId,
                            "", selectedEtalaseId, "", sortName));
                }
                break;
            case REQUEST_CODE_LOGIN_USE_VOUCHER:
            case REQUEST_CODE_MERCHANT_VOUCHER:
            case REQUEST_CODE_MERCHANT_VOUCHER_DETAIL: {
                if (resultCode == Activity.RESULT_OK) {
                    needLoadVoucher = true;
                }
            }
            break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMerchantUseVoucherClicked(MerchantVoucherViewModel merchantVoucherViewModel, int position) {
        if (getContext() == null) {
            return;
        }
        shopPageTracking.clickUseMerchantVoucher(isOwner(), merchantVoucherViewModel, position);
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
        /*if (!merchantVoucherListPresenter.isLogin()) {
            if (RouteManager.isSupportApplink(getContext(), ApplinkConst.LOGIN)) {
                Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.LOGIN);
                startActivityForResult(intent, REQUEST_CODE_LOGIN_USE_VOUCHER);
            }
        } else if (!merchantVoucherListPresenter.isMyShop(shopInfo.getInfo().getShopId())) {
            showUseMerchantVoucherLoading();
            merchantVoucherListPresenter.useMerchantVoucher(merchantVoucherViewModel.getVoucherCode(),
                    merchantVoucherViewModel.getVoucherId());
        }*/
        //TOGGLE_MVC_OFF
        showSnackBarClose(getString(R.string.title_voucher_code_copied));
    }

    public void showSnackBarClose(String stringToShow) {
        if (getActivity() != null) {
            final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), stringToShow,
                    Snackbar.LENGTH_LONG);
            snackbar.setAction(getActivity().getString(R.string.close), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    @Override
    public void onItemClicked(MerchantVoucherViewModel merchantVoucherViewModel) {
        shopPageTracking.clickDetailMerchantVoucher(isOwner());

        Intent intent = MerchantVoucherDetailActivity.createIntent(getContext(), merchantVoucherViewModel.getVoucherId(),
                merchantVoucherViewModel, shopInfo.getInfo().getShopId());
        startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL);
    }

    @Override
    public void onSeeAllClicked() {
        shopPageTracking.clickSeeAllMerchantVoucher(isOwner());

        Intent intent = MerchantVoucherListActivity.createIntent(getContext(), shopInfo.getInfo().getShopId(), shopInfo.getInfo().getShopName());
        startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER);
    }

    @Override
    public boolean isOwner() {
        if (shopInfo != null) {
            return merchantVoucherListPresenter.isMyShop(shopInfo.getInfo().getShopId());
        }
        return false;
    }

    @Override
    public void onSuccessGetShopInfo(@NotNull ShopInfo shopInfo) {
        // no op, shop info is got from activity
    }

    @Override
    public void onErrorGetShopInfo(@NotNull Throwable e) {
        // no op, shop info is got from activity
    }

    @Override
    public void onSuccessUseVoucher(@NonNull UseMerchantVoucherQueryResult useMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading();
        if (getActivity() != null) {
            Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
            dialog.setTitle(useMerchantVoucherQueryResult.getErrorMessageTitle());
            dialog.setDesc(useMerchantVoucherQueryResult.getErrorMessage());
            dialog.setBtnOk(getString(R.string.label_close));
            dialog.setOnOkClickListener(v -> dialog.dismiss());
            dialog.show();

            merchantVoucherListPresenter.clearCache();
            loadVoucherList();
        }
    }

    @Override
    public void onErrorUseVoucher(@NotNull Throwable e) {
        hideUseMerchantVoucherLoading();
        if (getActivity() != null) {
            if (e instanceof MessageTitleErrorException) {

                Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
                dialog.setTitle(((MessageTitleErrorException) e).getErrorMessageTitle());
                dialog.setDesc(e.getMessage());
                dialog.setBtnOk(getString(R.string.label_close));
                dialog.setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else {
                ToasterError.showClose(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
            }
        }
    }

    @Override
    public void onSuccessGetMerchantVoucherList(@NotNull ArrayList<MerchantVoucherViewModel> merchantVoucherViewModelList) {
        shopPageTracking.impressionUseMerchantVoucher(isOwner(), merchantVoucherViewModelList);

        shopProductAdapter.setShopMerchantVoucherViewModel(new ShopMerchantVoucherViewModel(merchantVoucherViewModelList));
        shopProductAdapter.refreshSticky();
    }

    @Override
    public void onErrorGetMerchantVoucherList(@NotNull Throwable e) {
        shopProductAdapter.setShopMerchantVoucherViewModel(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needReloadData) {
            reloadProductData(false);
            needReloadData = false;
        }
        if (needLoadVoucher) {
            merchantVoucherListPresenter.clearCache();
            loadVoucherList();
            needLoadVoucher = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        shopPageTracking.sendAllTrackingQueue();
    }

    private void loadVoucherList() {
        if (shopInfo != null) {
            merchantVoucherListPresenter.getVoucherList(shopInfo.getInfo().getShopId(), NUM_VOUCHER_DISPLAY);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (shopProductLimitedListPresenter != null) {
            shopProductLimitedListPresenter.detachView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId);
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName);
        outState.putString(SAVED_SHOP_ID, shopId);
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore);
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
    }


}