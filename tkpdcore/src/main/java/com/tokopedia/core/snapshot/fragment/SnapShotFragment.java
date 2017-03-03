package com.tokopedia.core.snapshot.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.snapshot.customview.ButtonView;
import com.tokopedia.core.snapshot.customview.DescriptionView;
import com.tokopedia.core.snapshot.customview.DetailInfoView;
import com.tokopedia.core.snapshot.customview.FreeReturnView;
import com.tokopedia.core.snapshot.customview.HeaderInfoView;
import com.tokopedia.core.snapshot.customview.PictureView;
import com.tokopedia.core.snapshot.customview.ShopInfoView;
import com.tokopedia.core.snapshot.listener.SnapShotFragmentView;
import com.tokopedia.core.snapshot.presenter.SnapShotFragmentImpl;
import com.tokopedia.core.snapshot.presenter.SnapShotFragmentPresenter;

import butterknife.BindView;

/**
 * Created by hangnadi on 3/1/17.
 */
public class SnapShotFragment extends BasePresenterFragment<SnapShotFragmentPresenter>
    implements SnapShotFragmentView {

    private static final String ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA";
    private static final String ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK";
    private static final String TAG = SnapShotFragment.class.getSimpleName();
    public static final int INIT_REQUEST = 1;
    public static final int RE_REQUEST = 2;

    @BindView(R2.id.view_header)
    HeaderInfoView headerInfoView;
    @BindView(R2.id.view_detail)
    DetailInfoView detailInfoView;
    @BindView(R2.id.view_picture)
    PictureView pictureView;
    @BindView(R2.id.view_desc)
    DescriptionView descriptionView;
    @BindView(R2.id.view_shop_info)
    ShopInfoView shopInfoView;
    @BindView(R2.id.view_buy)
    ButtonView buttonView;
    @BindView(R2.id.view_progress)
    ProgressBar progressBar;
    @BindView(R2.id.view_free_return)
    FreeReturnView freeReturnView;

    private ProductPass productPass;
    private ProductDetailData productData;

    public static Fragment newInstance(ProductPass productPass) {
        SnapShotFragment fragment = new SnapShotFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        Log.d(TAG, "onFirstTimeLaunched");
        if (productData != null) {
            Log.d(TAG, "productData != null");
            onProductDetailLoaded(productData);
        } else {
            Log.d(TAG, "productData == null");
            presenter.processDataPass(productPass);
            presenter.requestProductDetail(context, productPass, INIT_REQUEST, false);
        }
    }

    @Override
    public void renderTempProductData(ProductPass productPass) {
        this.headerInfoView.renderTempData(productPass);
        this.pictureView.renderTempData(productPass);
    }

    @Override
    public void onProductDetailLoaded(ProductDetailData productData) {
        this.productData = productData;
        this.headerInfoView.renderData(productData);
        this.pictureView.renderData(productData);
        this.detailInfoView.renderData(productData);
        this.descriptionView.renderData(productData);
        this.shopInfoView.renderData(productData);
        this.freeReturnView.renderData(productData);
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new SnapShotFragmentImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        productPass = arguments.getParcelable(ARG_PARAM_PRODUCT_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_snapshot;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        headerInfoView.setListener(this);
        pictureView.setListener(this);
        detailInfoView.setListener(this);
        descriptionView.setListener(this);
        shopInfoView.setListener(this);
        freeReturnView.setListener(this);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onProductPictureClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, PreviewProductImage.class);
        intent.putExtras(bundle);
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

    private NetworkErrorHelper.RetryClickedListener initializationErrorListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.requestProductDetail(context, productPass, INIT_REQUEST, false);
            }
        };
    }

    @Override
    public void onNullData() {
        showToastMessage("Produk tidak ditemukan!");
        closeView();
    }

    @Override
    public void showToastMessage(String message) {
        CommonUtils.UniversalToast(getActivity(), message);
    }

    @Override
    public void closeView() {
        this.getActivity().finish();
    }

    @Override
    public void showFullScreenError() {
        NetworkErrorHelper.showEmptyState(getActivity(),
                getActivity().findViewById(R.id.root_view),
                initializationErrorListener());
    }

    private void navigateShopActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onProductShopNameClicked(Bundle bundle) {
        navigateShopActivity(bundle);
    }

    @Override
    public void onProductShopRatingClicked(Bundle bundle) {
        navigateShopActivity(bundle);
    }

    @Override
    public void onProductShopAvatarClicked(Bundle bundle) {
        navigateShopActivity(bundle);
    }
}
