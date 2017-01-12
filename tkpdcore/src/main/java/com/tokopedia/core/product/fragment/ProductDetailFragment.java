package com.tokopedia.core.product.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.customview.ButtonBuyView;
import com.tokopedia.core.product.customview.ButtonShareView;
import com.tokopedia.core.product.customview.DescriptionView;
import com.tokopedia.core.product.customview.DetailInfoView;
import com.tokopedia.core.product.customview.ErrorProductView;
import com.tokopedia.core.product.customview.ErrorShopView;
import com.tokopedia.core.product.customview.FreeReturnView;
import com.tokopedia.core.product.customview.HeaderInfoView;
import com.tokopedia.core.product.customview.LastUpdateView;
import com.tokopedia.core.product.customview.ManageView;
import com.tokopedia.core.product.customview.NewShopView;
import com.tokopedia.core.product.customview.PictureView;
import com.tokopedia.core.product.customview.RatingView;
import com.tokopedia.core.product.customview.ShopInfoView;
import com.tokopedia.core.product.customview.TalkReviewView;
import com.tokopedia.core.product.customview.TransactionSuccessView;
import com.tokopedia.core.product.customview.VideoLayout;
import com.tokopedia.core.product.customview.WholesaleView;
import com.tokopedia.core.product.dialog.ReportProductDialogFragment;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.product.presenter.ProductDetailPresenter;
import com.tokopedia.core.product.presenter.ProductDetailPresenterImpl;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.AppIndexHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;

import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * ProductDetailFragment
 * Created by Angga.Prasetiyo on 22/10/2015.
 */
@RuntimePermissions
public class ProductDetailFragment extends BasePresenterFragment<ProductDetailPresenter>
        implements ProductDetailView {
    public static final int REQUEST_CODE_SHOP_INFO = 998;
    public static final int REQUEST_CODE_TALK_PRODUCT = 1;
    public static final int REQUEST_CODE_EDIT_PRODUCT = 2;
    public static final int REQUEST_CODE_LOGIN = 561;

    public static final int INIT_REQUEST = 1;
    public static final int RE_REQUEST = 2;

    private static final String ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA";
    private static final String ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK";
    public static final String STATE_DETAIL_PRODUCT = "STATE_DETAIL_PRODUCT";
    public static final String STATE_OTHER_PRODUCTS = "STATE_OTHER_PRODUCTS";
    public static final String STATE_VIDEO = "STATE_VIDEO";
    private static final String TAG = ProductDetailFragment.class.getSimpleName();

    @BindView(R2.id.tv_ticker_gtm)
    TextView tvTickerGTM;
    @BindView(R2.id.view_header)
    HeaderInfoView headerInfoView;
    @BindView(R2.id.view_detail)
    DetailInfoView detailInfoView;
    @BindView(R2.id.view_picture)
    PictureView pictureView;
    @BindView(R2.id.view_desc)
    DescriptionView descriptionView;
    @BindView(R2.id.view_talk_review)
    TalkReviewView talkReviewView;
    @BindView(R2.id.view_manage)
    ManageView manageView;
    @BindView(R2.id.view_shop_info)
    ShopInfoView shopInfoView;
    @BindView(R2.id.view_wholesale)
    WholesaleView wholesaleView;
    @BindView(R2.id.view_share)
    ButtonShareView buttonShareView;
    @BindView(R2.id.view_rating)
    RatingView ratingView;
    @BindView(R2.id.view_error_product)
    ErrorProductView errorProductView;
    @BindView(R2.id.view_error_shop)
    ErrorShopView errorShopView;
    @BindView(R2.id.view_new_shop)
    NewShopView newShopView;
    @BindView(R2.id.view_buy)
    ButtonBuyView buttonBuyView;
    @BindView(R2.id.view_last_update)
    LastUpdateView lastUpdateView;
    @BindView(R2.id.view_progress)
    ProgressBar progressBar;
    @BindView(R2.id.view_free_return)
    FreeReturnView freeReturnView;
    @BindView(R2.id.view_transaction_success)
    TransactionSuccessView transactionSuccess;
    @BindView(R2.id.video_layout)
    VideoLayout videoLayout;

    private ProductPass productPass;
    private ProductDetailData productData;
    private List<ProductOther> productOthers;
    private VideoData videoData;
    private AppIndexHandler appIndexHandler;
    private ProgressDialog loading;

    private OnFragmentInteractionListener interactionListener;
    private DeepLinkWebViewHandleListener webViewHandleListener;

    ReportProductDialogFragment fragment;

    Bundle recentBundle;

    public static ProductDetailFragment newInstance(@NonNull ProductPass productPass) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductDetailFragment newInstanceForDeeplink(@NonNull ProductPass productPass) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        args.putBoolean(ARG_FROM_DEEPLINK, true);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductDetailFragment() {
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        this.presenter = new ProductDetailPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) throws ClassCastException {
        interactionListener = (OnFragmentInteractionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        productPass = arguments.getParcelable(ARG_PARAM_PRODUCT_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_info_main_v2;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        errorProductView.setListener(this);
        errorShopView.setListener(this);
        headerInfoView.setListener(this);
        pictureView.setListener(this);
        buttonShareView.setListener(this);
        buttonBuyView.setListener(this);
        talkReviewView.setListener(this);
        manageView.setListener(this);
        ratingView.setListener(this);
        detailInfoView.setListener(this);
        descriptionView.setListener(this);
        wholesaleView.setListener(this);
        lastUpdateView.setListener(this);
        shopInfoView.setListener(this);
        newShopView.setListener(this);
        freeReturnView.setListener(this);
        transactionSuccess.setListener(this);
    }

    @Override
    protected void initialVar() {
        appIndexHandler = new AppIndexHandler(getActivity());
        loading = new ProgressDialog(context);
        loading.setCancelable(false);
        loading.setMessage("Loading");
        if (presenter != null)
            presenter.initRetrofitInteractor();
        else
            initialPresenter();
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void onProductDepartmentClicked(@NonNull Bundle bundle) {
        presenter.processToBrowseProduct(context, bundle);
    }

    @Override
    public void onProductCatalogClicked(@NonNull String catalogId) {
        presenter.processToCatalog(context, catalogId);
    }

    @Override
    public void onProductEtalaseClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
    }

    @Override
    public void onProductTalkClicked(@NonNull Bundle bundle) {
        presenter.processToTalk(context, bundle);
    }

    @Override
    public void onProductReviewClicked(@NonNull Bundle bundle) {
        presenter.processToReputation(context, bundle);
    }

    @Override
    public void onProductManagePromoteClicked(ProductDetailData productData) {
        presenter.requestPromoteProduct(context, productData);
    }

    @Override
    public void onProductManageToEtalaseClicked(int productId) {
        presenter.requestMoveToEtalase(context, productId);
    }

    @Override
    public void onProductManageEditClicked(@NonNull Bundle bundle) {
        presenter.processToEditProduct(context, bundle);
    }

    @Override
    public void onProductManageSoldOutClicked(int productId) {
        presenter.requestMoveToWarehouse(context, productId);
    }

    @Override
    public void onProductShopAvatarClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
    }

    @Override
    public void onProductOtherClicked(@NonNull ProductPass productPass) {
        interactionListener.jumpOtherProductDetail(productPass);
    }

    @Override
    public void onProductShopNameClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
    }

    @Override
    public void onProductShareClicked(@NonNull ShareData data) {
        ProductDetailFragmentPermissionsDispatcher.shareProductWithCheck(ProductDetailFragment.this, data);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void shareProduct(ShareData data) {
        interactionListener.shareProductInfo(data);
    }

    @Override
    public void onProductRatingClicked(@NonNull Bundle bundle) {
        presenter.processToReputation(context, bundle);
    }

    @Override
    public void onProductShopInfoError() {
        buttonBuyView.setVisibility(View.GONE);
    }

    @Override
    public void onProductStatusError() {
        buttonBuyView.setVisibility(View.GONE);
    }

    @Override
    public void onProductNewShopClicked() {
        presenter.processToCreateShop(context);
    }

    @Override
    public void onProductBuySessionLogin(@NonNull ProductCartPass data) {
        presenter.processToCart(context, data);
    }

    @Override
    public void onProductBuySessionNotLogin(@NonNull Bundle bundle) {
        presenter.processToLogin(context, bundle);
    }

    @Override
    public void renderTempProductData(ProductPass productPass) {
        this.headerInfoView.renderTempData(productPass);
        this.pictureView.renderTempData(productPass);
    }

    @Override
    public void onProductDetailLoaded(@NonNull ProductDetailData successResult) {
        presenter.processGetGTMTicker();
        this.productData = successResult;
        this.errorProductView.renderData(successResult);
        this.errorShopView.renderData(successResult);
        this.headerInfoView.renderData(successResult);
        this.pictureView.renderData(successResult);
        this.manageView.renderData(successResult);
        this.buttonBuyView.renderData(successResult);
        this.talkReviewView.renderData(successResult);
        this.ratingView.renderData(successResult);
        this.freeReturnView.renderData(successResult);
        this.transactionSuccess.renderData(successResult);
        this.detailInfoView.renderData(successResult);
        this.descriptionView.renderData(successResult);
        this.wholesaleView.renderData(successResult);
        this.lastUpdateView.renderData(successResult);
        this.shopInfoView.renderData(successResult);
        this.newShopView.renderData(successResult);
        this.buttonShareView.renderData(successResult);
        this.interactionListener.onProductDetailLoaded(successResult);
        this.presenter.sendAnalytics(successResult);
        this.presenter.sendAppsFlyerData(context, successResult, AFInAppEventType.CONTENT_VIEW);
        this.presenter.startIndexingApp(appIndexHandler, successResult);
        this.refreshMenu();
    }

    @Override
    public void onProductPictureClicked(@NonNull Bundle bundle) {
        presenter.processToPicturePreview(context, bundle);
    }

    @Override
    public void onOtherProductLoaded(List<ProductOther> productOthers) {
        this.productOthers = productOthers;
        shopInfoView.renderOtherProduct(productOthers);
    }

    @Override
    public void onProductShopMessageClicked(@NonNull Bundle bundle) {
        presenter.processToSendMessage(context, bundle);
    }

    @Override
    public void onProductHasEdited() {
        presenter.requestProductDetail(context, productPass, RE_REQUEST, true);
    }

    @Override
    public void onProductTalkUpdated() {
        presenter.requestProductDetail(context, productPass, RE_REQUEST, true);
    }

    @Override
    public void onShopFavoriteUpdated(int statFave) {
        shopInfoView.updateFavoriteStatus(statFave);
    }

    @Override
    public void refreshFaveShopStatus() {
        shopInfoView.reverseFavoriteStatus();
    }

    @Override
    public void onProductShopFaveClicked(String shopId) {
        presenter.requestFaveShop(context, shopId);
    }

    @Override
    public void onSuccessToEtalase() {
        manageView.hideToEtalase();
    }

    @Override
    public void onSuccessToWarehouse() {
        manageView.hideToWareHouse();
    }

    @Override
    public void onProductShopRatingClicked(Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
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
    public void updateWishListStatus(int status) {
        this.productData.getInfo().setProductAlreadyWishlist(status);
    }

    @Override
    public void loadVideo(VideoData data) {
        videoLayout.setVisibility(View.VISIBLE);
        this.videoLayout.renderData(data);
        videoData = data;
    }

    @Override
    public void refreshMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showProductDetailRetry() {
        if(productPass !=null && !productPass.getProductName().isEmpty()){
            NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                    initializationErrorListener()).showRetrySnackbar();
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getActivity().findViewById(R.id.root_view),
                    initializationErrorListener());
        }

    }

    @Override
    public void showProductOthersRetry() {

    }

    @Override
    public void showFaveShopRetry() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void showWishListRetry(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void showPromoteRetry() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void onNullData() {
        Boolean isFromDeeplink = getArguments().getBoolean(ARG_FROM_DEEPLINK, false);
        if (isFromDeeplink) {
            ProductPass pass = (ProductPass) getArguments().get(ARG_PARAM_PRODUCT_PASS_DATA);
            webViewHandleListener = (DeepLinkWebViewHandleListener) getActivity();
            webViewHandleListener.catchToWebView(pass != null ? pass.getProductUri() : "");
        } else {
            showToastMessage("Produk tidak ditemukan!");
            closeView();
        }

    }

    @Override
    public void showReportDialog() {
        fragment = ReportProductDialogFragment.createInstance(productData, recentBundle);
        fragment.setListener(this);
        fragment.show(getFragmentManager(), "ReportProductDialogFragment");
    }

    @Override
    public void onProductReportClicked() {
        presenter.reportProduct(context);
    }

    @Override
    public void showTickerGTM(String message) {
        tvTickerGTM.setText(message);
        tvTickerGTM.setVisibility(View.VISIBLE);
        tvTickerGTM.setAutoLinkMask(0);
        Linkify.addLinks(tvTickerGTM, Linkify.WEB_URLS);
        tvTickerGTM.setText(MethodChecker.fromHtml(tvTickerGTM.getText().toString()));
    }

    @Override
    public void hideTickerGTM() {
        tvTickerGTM.setVisibility(View.GONE);
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
    public void showProgressLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressLoading() {
        progressBar.setVisibility(View.GONE);
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
    public void dismissDialog(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void closeView() {
        this.getActivity().finish();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView(context);
        if (videoLayout != null && videoLayout.isShown()) {
            videoLayout.destroyVideoLayoutProcess();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.product_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        presenter.prepareOptionMenu(menu, context, productData);
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        Log.d(TAG, "onFirstTimeLaunched");
        if (productData != null) {
            onProductDetailLoaded(productData);
            Log.d(TAG, "productData != null");
        } else {
            Log.d(TAG, "productData == null");
            presenter.processDataPass(productPass);
            presenter.requestProductDetail(context, productPass, INIT_REQUEST, false);
        }
    }

    @Override
    public void onSaveState(Bundle outState) {
        Log.d(TAG, "onSaveState");
        presenter.saveStateProductDetail(outState, STATE_DETAIL_PRODUCT, productData);
        presenter.saveStateProductOthers(outState, STATE_OTHER_PRODUCTS, productOthers);
        presenter.saveStateVideoData(outState, STATE_VIDEO, videoData);
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreState");
        presenter.processStateData(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_wishlist) {
            presenter.processWishList(context, productData);
            return true;
        } else if (item.getItemId() == R.id.action_report) {
            presenter.reportProduct(context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(ProductDetailFragment.class.getSimpleName(), "onActivityResult requestCode " + requestCode + " resultCode " + resultCode);
        switch (requestCode) {
            case REQUEST_CODE_EDIT_PRODUCT:
                presenter.processResultEdit(resultCode, data);
                break;
            case REQUEST_CODE_SHOP_INFO:
                presenter.processResultShop(resultCode, data);
                break;
            case REQUEST_CODE_TALK_PRODUCT:
                presenter.processResultTalk(resultCode, data);
                break;
            case REQUEST_CODE_LOGIN:
                presenter.requestProductDetail(context, productPass, RE_REQUEST, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (productData != null) {
            presenter.startIndexingApp(appIndexHandler, productData);
            this.newShopView.renderData(productData);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopIndexingApp(appIndexHandler);
    }

    public interface OnFragmentInteractionListener {

        void shareProductInfo(@NonNull ShareData shareData);

        void onProductDetailLoaded(@NonNull ProductDetailData productData);

        void onNullResponseData(ProductPass productPass);

        void jumpOtherProductDetail(ProductPass productPass);
    }

    public void onSuccessAction(Bundle resultData, int resultCode) {
        if (fragment != null) fragment.dismiss();
        recentBundle = null;
        SnackbarManager.make(getActivity(),
                resultData.getString(ProductInfoIntentService.EXTRA_RESULT),
                Snackbar.LENGTH_LONG).show();
    }

    public void onErrorAction(Bundle resultData, int resultCode) {
        if (fragment != null) {
            recentBundle = resultData.getBundle(ProductInfoIntentService.EXTRA_BUNDLE);
            fragment.onErrorAction(resultData.getString(ProductInfoIntentService.EXTRA_RESULT));
        } else {
            SnackbarManager.make(getActivity(),
                    resultData.getString(ProductInfoIntentService.EXTRA_RESULT),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    public void setRecentBundle(Bundle recentBundle) {
        this.recentBundle = recentBundle;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProductDetailFragmentPermissionsDispatcher.onRequestPermissionsResult(ProductDetailFragment.this,
                requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private NetworkErrorHelper.RetryClickedListener initializationErrorListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.requestProductDetail(context, productPass, INIT_REQUEST, false);
            }
        };
    }

    @Override
    public void showFullScreenError() {
        NetworkErrorHelper.showEmptyState(getActivity(),
                getActivity().findViewById(R.id.root_view),
                initializationErrorListener());
    }

    @Override
    public void moveToEditFragment(boolean isEdit, String productId) {
        if(getActivity().getApplication() instanceof TkpdCoreRouter){
            ((TkpdCoreRouter)getActivity().getApplication()).goToEditProduct(context, isEdit, productId);
        }
    }

}
