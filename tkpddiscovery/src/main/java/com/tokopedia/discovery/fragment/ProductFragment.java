package com.tokopedia.discovery.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.discovery.BannerOfficialStoreModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.intermediary.Child;
import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.DefaultCategoryAdapter;
import com.tokopedia.discovery.adapter.OsBannerAdapter;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.discovery.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenter;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.discovery.view.FragmentBrowseProductView;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static com.tokopedia.core.router.discovery.BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID;

/**
 * Created by noiz354 on 3/24/16.
 */
public class ProductFragment extends BaseFragment<FragmentDiscoveryPresenter>
        implements FetchNetwork, FragmentBrowseProductView, DefaultCategoryAdapter.CategoryListener,
        RevampCategoryAdapter.CategoryListener, ProductAdapter.ScrollListener,
        TopAdsItemClickListener, TopAdsListener {

    public static final String TAG = "BrowseProductFragment";
    public static final String INDEX = "FRAGMENT_INDEX";
    public static final int GOTO_PRODUCT_DETAIL = 123;
    // this value for main colum recyclerview
    private static final int LANDSCAPE_COLUMN_MAIN = 3;
    private static final int PORTRAIT_COLUMN_MAIN = 2;

    @BindView(R2.id.fragmentv2list)
    RecyclerView mRecyclerView;

    private static String ARG_1 = "";
    private static String ARG_2 = "";

    private ProductAdapter productAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private BrowseProductRouter.GridType gridType = BrowseProductRouter.GridType.GRID_2;
    int spanCount = 2;
    private TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private ProgressDialog loading;

    private ProductFragmentListener mListener;

    public interface ProductFragmentListener {
        String getDepartmentId();
    }

    private BroadcastReceiver changeGridReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isViewInitialized()) {
                return;
            }
            BrowseProductRouter.GridType gridType = (BrowseProductRouter.GridType) intent.getSerializableExtra(BrowseProductActivity.GRID_TYPE_EXTRA);
            int lastItemPosition = getLastItemPosition();
            changeLayoutType(gridType);
            productAdapter.notifyItemChanged(productAdapter.getItemCount());
            mRecyclerView.scrollToPosition(lastItemPosition);
        }
    };

    private boolean isViewInitialized() {
        return productAdapter != null
                && linearLayoutManager != null
                && gridLayoutManager != null
                && mRecyclerView != null;
    }

    private void changeLayoutType(BrowseProductRouter.GridType gridType) {
        if (topAdsRecyclerAdapter == null) {
            initTopAdsRecyclerAdapter();
        }

        this.gridType = gridType;
        switch (gridType) {
            case GRID_1: //List
                spanCount = 1;
                productAdapter.setgridView(gridType);
                topAdsRecyclerAdapter.setLayoutManager(linearLayoutManager);
                break;
            case GRID_2: //Grid 2x2
                spanCount = 2;
                gridLayoutManager.setSpanCount(spanCount);
                productAdapter.setgridView(gridType);
                topAdsRecyclerAdapter.setLayoutManager(gridLayoutManager);
                break;
            case GRID_3: //Grid 1x1
                spanCount = 1;
                gridLayoutManager.setSpanCount(spanCount);
                productAdapter.setgridView(gridType);
                topAdsRecyclerAdapter.setLayoutManager(gridLayoutManager);
                break;
        }
    }

    public static ProductFragment newInstance(int index) {

        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(changeGridReceiver, new IntentFilter(BrowseProductActivity.CHANGE_GRID_ACTION_INTENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(changeGridReceiver);
    }

    @Override
    public View onCreateView(View parentView, Bundle savedInstanceState) {
        loading = new ProgressDialog(getActivity());
        loading.setCancelable(false);
        loading.setMessage(getResources().getString(R.string.title_loading));

        return parentView;
    }

    @Override
    protected void initPresenter() {
        presenter = new FragmentDiscoveryPresenterImpl(this);
        presenter.setTAG(TAG);
        ScreenTracking.eventDiscoveryScreenAuth(mListener.getDepartmentId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fragment_browse_product_v2;
    }

    @Override
    public void onCallProductServiceResult(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        productAdapter.addAll(new ArrayList<RecyclerViewItem>(model));
        productAdapter.notifyDataSetChanged();
        productAdapter.setgridView(((BrowseProductActivity) getActivity()).getGridType());
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        if (getActivity() != null && getActivity() instanceof BrowseProductActivity) {
            BrowseProductActivityModel browseModel = ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel();

            productAdapter.incrementPage();

            UnifyTracking.eventAppsFlyerViewListingSearch(model, browseModel.q);
            TrackingUtils.sendMoEngageSearchAttempt(browseModel.q, !model.isEmpty());

        }
    }

    @Override
    public BrowseProductModel getDataModel() {
        return ((FragmentDiscoveryPresenterImpl) presenter).getBrowseProductModel();
    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(getActivity());
    }

    @Override
    public void onWishlistButtonClick(ProductItem data, int position) {
        presenter.onWishlistButtonClick(data, position, getActivity());
    }

    @Override
    public void finishLoadingWishList() {
        loading.dismiss();
    }

    @Override
    public void loadingWishList() {
        loading.show();
    }

    @Override
    public void updateWishListStatus(boolean isWishlist, int position) {
        if (productAdapter != null) productAdapter.updateWishlistStatus(isWishlist, position);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showToastMessage(String message) {
        CommonUtils.UniversalToast(getActivity(), message);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void closeView() {
        this.getActivity().finish();
    }

    @Override
    public void showWishListRetry(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void updateTotalProduct(Long totalProduct) {
        BrowseProductActivityModel browseModel = ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel();
        if (totalProduct > 0) {
            browseModel.setTotalDataCategory(NumberFormat.getNumberInstance(Locale.US)
                    .format(totalProduct.longValue()).replace(',', '.'));
        } else {
            browseModel.setTotalDataCategory("0");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode
                == GOTO_PRODUCT_DETAIL && resultCode == Activity.RESULT_CANCELED) {
            int position = data.getIntExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist
                    = data.getBooleanExtra(ProductDetailRouter.WIHSLIST_STATUS_IS_WISHLIST, false);
            if (productAdapter != null && position != -1) {
                productAdapter.updateWishlistStatus(isWishlist, position);
            }
        }
    }

    @Override
    public void onCallProductServiceLoadMore(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        productAdapter.addAll(true, new ArrayList<RecyclerViewItem>(model));
        if (getActivity() != null && getActivity() instanceof BrowseProductActivity) {
            productAdapter.setgridView(((BrowseProductActivity) getActivity()).getGridType());
        }
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        productAdapter.incrementPage();
    }

    @Override
    public void setHotlistData(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        topAdsRecyclerAdapter.setHasHeader(true);
        productAdapter.addAll(new ArrayList<RecyclerViewItem>(model));
        productAdapter.setgridView(((BrowseProductActivity) getActivity()).getGridType());
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        productAdapter.incrementPage();
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public int getDataSize(String TAG) {
        if (productAdapter == null) return -1;
        return productAdapter.getData() != null ? productAdapter.getData().size() : -1;
    }

    @Override
    public void setupAdapter() {
        if (productAdapter != null) {
            return;
        }
        productAdapter = new ProductAdapter(getActivity(), new ArrayList<RecyclerViewItem>(), this);
        productAdapter.setTopAdsListener(this);
        productAdapter.setIsLoading(false);
        spanCount = calcColumnSize(getResources().getConfiguration().orientation);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductFragmentListener) {
            mListener = (ProductFragmentListener) context;
        } else {
            throw new RuntimeException("Please implement ProductFragmentListener in the Activity");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ProductFragmentListener) {
            mListener = (ProductFragmentListener) activity;
        } else {
            throw new RuntimeException("Please implement ProductFragmentListener in the Activity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // to determine size of grid columns
    private GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (topAdsRecyclerAdapter.isTopAdsViewHolder(position)
                        || topAdsRecyclerAdapter.isLoading(position)
                        || productAdapter.isHotListBanner(position)
                        || productAdapter.isCategoryHeader(position)
                        || productAdapter.isEmptySearch(position)
                        || productAdapter.isOfficialStoreBanner(position)
                        || productAdapter.isEmpty()) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    public boolean setupRecyclerView() {
        initTopAdsRecyclerAdapter();
        changeLayoutType(((BrowseProductActivity) getActivity()).getGridType());
        return true;
    }

    private void initTopAdsRecyclerAdapter() {
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .setEndpoint(Endpoint.PRODUCT)
                .topAdsParams(populatedNetworkParams())
                .build();

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), productAdapter);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setConfig(config);
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                if (productAdapter.getPagingHandlerModel() != null &&
                        !TextUtils.isEmpty(productAdapter.getPagingHandlerModel().getUriNext())) {
                    presenter.loadMore(getActivity());
                    if (gridLayoutManager.findLastVisibleItemPosition() == gridLayoutManager.getItemCount() - 1
                            && productAdapter.getPagingHandlerModel().getUriNext() != null
                            && productAdapter.getPagingHandlerModel().getUriNext().isEmpty()) {
                        ((BrowseProductActivity) getActivity()).showBottomBar();
                    }
                } else {
                    topAdsRecyclerAdapter.hideLoading();
                    topAdsRecyclerAdapter.unsetEndlessScrollListener();
                    if (getActivity() instanceof BrowseProductActivity) {
                        ((BrowseProductActivity) getActivity()).showBottomBar();
                    }
                }
            }
        });
        mRecyclerView.setAdapter(topAdsRecyclerAdapter);
    }

    private TopAdsParams populatedNetworkParams() {
        NetworkParam.Product networkParam = ((BrowseProductActivity) getActivity()).getProductParam();
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, networkParam.source);
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, networkParam.sc);
        if (networkParam.keyword != null) {
            params.getParam().put(TopAdsParams.KEY_QUERY, networkParam.keyword);
        }
        if (networkParam.h != null) {
            params.getParam().put(TopAdsParams.KEY_HOTLIST_ID, networkParam.h);
        }
        if (networkParam.extraFilter != null) {
            params.getParam().putAll(networkParam.extraFilter);
        }
        return params;
    }

    @Override
    public void onProductItemClicked(Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        navigateToActivityRequest(intent, ProductFragment.GOTO_PRODUCT_DETAIL);
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shop.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, com.tokopedia.topads.sdk.domain.model.Data shop) {

    }

    @Override
    public void onCallNetwork() {
        if (getActivity() != null && getActivity() instanceof BrowseView) {
            BrowseView browseView = (BrowseView) getActivity();
            presenter.callNetwork(browseView);
        }
    }

    @Override
    public int getFragmentId() {
        return VALUES_PRODUCT_FRAGMENT_ID;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

        Snackbar snackbar = Snackbar.make(parentView, CommonUtils.generateMessageError(getActivity(), (String) data[0]), Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof BrowseView) {
                    BrowseView browseView = (BrowseView) getActivity();
                    presenter.callNetwork(browseView);
                }
            }
        };
        if (listener != null) {
            snackbar.setAction(getString(R.string.title_retry), listener);
        }
        snackbar.show();
    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    private int getLastItemPosition() {
        switch (gridType) {
            case GRID_1:
                return linearLayoutManager.findFirstVisibleItemPosition();
            case GRID_2:
            case GRID_3:
            default:
                return gridLayoutManager.findFirstVisibleItemPosition();
        }
    }

    @Override
    public boolean isLoading() {
        switch (gridType) {
            case GRID_1:
                return productAdapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
            case GRID_2:
            case GRID_3:
            default:
                return productAdapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
        }
    }

    @Override
    public int getStartIndexForQuery(String TAG) {
        return productAdapter.getPagingHandlerModel().getStartIndex();
    }

    /**
     * need to supply correct TAG
     *
     * @param TAG
     * @return -1 means invalid index
     */
    @Override
    public int getPage(String TAG) {
        if (TAG.equals(ProductFragment.TAG))
            return productAdapter.getPage();
        else
            return -1;
    }

    @Override
    public void savePaging(Bundle savedState) {
        if (productAdapter != null) {
            productAdapter.saveAdapterPaging(savedState);
        }
    }

    @Override
    public void restorePaging(Bundle savedState) {
        if (productAdapter != null) {
            productAdapter.restoreAdapterPaging(savedState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (productAdapter != null) {
            productAdapter.saveAdapterPaging(outState);
        }
    }

    @Override
    public void addHotListHeader(ProductAdapter.HotListBannerModel hotListBannerModel) {
        topAdsRecyclerAdapter.setHasHeader(true);
        productAdapter.addHotListHeader(hotListBannerModel);
        Log.d(TAG, "addHotListHeader");
    }

    @Override
    public void addCategoryHeader(Data categoryHeader) {
        topAdsRecyclerAdapter.setHasHeader(true);
        if (getActivity() != null && getActivity() instanceof BrowseProductActivity) {
            BrowseProductActivityModel browseModel = ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel();
            if (categoryHeader.getIsRevamp() != null && categoryHeader.getIsRevamp()) {
                productAdapter.addCategoryRevampHeader(
                        new ProductAdapter.CategoryHeaderRevampModel(categoryHeader, getActivity(), getCategoryWidth(),
                                this, browseModel.getTotalDataCategory(), this));
            } else {
                productAdapter.addCategoryHeader(
                        new ProductAdapter.CategoryHeaderModel(categoryHeader, getActivity(), getCategoryWidth(),
                                this, browseModel.getTotalDataCategory(), this));
            }

            TrackingUtils.sendMoEngageOpenCatScreen(
                    categoryHeader.getName(),
                    categoryHeader.getId()

            );
        }
    }

    private int calcColumnSize(int orientation) {
        int defaultColumnNumber = 1;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    private int getCategoryWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width / 2;
    }

    @Override
    public void onCategoryClick(Child child) {
        UnifyTracking.eventLevelCategory(((BrowseProductActivity) getActivity()).
                getBrowseProductActivityModel().getParentDepartement(), child.getId());
        ((BrowseProductActivity) getActivity()).renderLowerCategoryLevel(child);
    }

    @Override
    public void onCategoryRevampClick(Child child) {
        UnifyTracking.eventLevelCategory(((BrowseProductActivity) getActivity()).
                getBrowseProductActivityModel().getParentDepartement(), child.getId());
        ((BrowseProductActivity) getActivity()).renderLowerCategoryLevel(child);
    }

    @Override
    public void backToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    public void showOfficialStoreBanner(BannerOfficialStoreModel model) {
        if (getActivity() != null && getActivity() instanceof BrowseProductActivity) {
            if (!TextUtils.isEmpty(model.getBannerUrl()) && !TextUtils.isEmpty(model.getShopUrl())) {
                topAdsRecyclerAdapter.setHasHeader(true);
                productAdapter.addOfficialStoreBanner(new OsBannerAdapter.OsBannerViewModel(model));
                productAdapter.notifyDataSetChanged();
                backToTop();
                UnifyTracking.eventImpressionOsBanner(model.getBannerUrl() + " - " + model.getKeyword());
            }
        }
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (topAdsRecyclerAdapter == null) {
            initTopAdsRecyclerAdapter();
        }

        if (isLoading) {
            topAdsRecyclerAdapter.showLoading();
        } else {
            topAdsRecyclerAdapter.hideLoading();
        }
    }

    @Override
    public void actionSuccessRemoveFromWishlist(Integer productId) {
        if(getActivity().getApplication() instanceof IReactNativeRouter) {
            IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getActivity().getApplication();
            reactNativeRouter.sendRemoveWishlistEmitter(String.valueOf(productId), SessionHandler.getLoginID(getActivity()));
        }
    }

    @Override
    public void actionSuccessAddToWishlist(Integer productId) {
        if(getActivity().getApplication() instanceof IReactNativeRouter) {
            IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getActivity().getApplication();
            reactNativeRouter.sendAddWishlistEmitter(String.valueOf(productId), SessionHandler.getLoginID(getActivity()));
        }
    }

    @Override
    public void displayEmptyResult() {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        productAdapter.setSearchNotFound();
        productAdapter.notifyItemInserted(1);
        setLoading(false);
        ((BrowseProductActivity) getActivity()).showLoading(false);
    }

    @Override
    public void onTopAdsLoaded() {
        setLoading(false);
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        setLoading(false);
    }
}
