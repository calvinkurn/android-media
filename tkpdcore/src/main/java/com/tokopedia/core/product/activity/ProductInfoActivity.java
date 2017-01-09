package com.tokopedia.core.product.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.DownloadResultSender;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.myproduct.service.ProductServiceConstant;
import com.tokopedia.core.product.fragment.ProductDetailFragment;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.intentservice.ProductInfoResultReceiver;
import com.tokopedia.core.product.listener.ProductInfoView;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.product.presenter.ProductInfoPresenter;
import com.tokopedia.core.product.presenter.ProductInfoPresenterImpl;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.core.var.TkpdState;

public class ProductInfoActivity extends BasePresenterActivity<ProductInfoPresenter> implements
        ProductInfoView,
        ProductDetailFragment.OnFragmentInteractionListener,
        DownloadResultSender,
        ProductInfoResultReceiver.Receiver{
    public static final String EXTRA_PRODUCT_PASS = "EXTRA_PRODUCT_PASS";
    public static final String EXTRA_PRODUCT_ITEM = "EXTRA_PRODUCT_ITEM";
    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String SHARE_DATA = "SHARE_DATA";

    private Uri uriData;
    private Bundle bundleData;
    private DownloadResultReceiver addProductReceiver;

    ProductInfoResultReceiver mReceiver;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_INFO;
    }

    public static Intent createInstance(Context context, @NonNull String productId) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PRODUCT_ID, productId);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createInstance(Context context, @NonNull ProductPass productPass) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRODUCT_PASS, productPass);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent createInstance(Context context, @NonNull ShareData shareData){
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHARE_DATA, shareData);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * Author : Sebast
     * Adding this for uploading product from product share activity
     * @param context
     * @param bundle
     * @return
     */
    public static Intent createInstance(Context context, @NonNull Bundle bundle){
        Intent intent = new Intent(context, ProductInfoActivity.class);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductInfoPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_info_fragmented;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void setViewListener() {
        presenter.initialFragment(this, uriData, bundleData);
    }

    @Override
    protected void initVar() {
        mReceiver = new ProductInfoResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void shareProductInfo(@NonNull ShareData shareData) {
        presenter.processToShareProduct(this, shareData);
        inflateNewFragment(ProductShareFragment.newInstance(shareData), ProductShareFragment.class.getSimpleName());
    }

    @Override
    public void onProductDetailLoaded(@NonNull ProductDetailData data) {
        this.invalidateOptionsMenu();
    }

    @Override
    public void onNullResponseData(ProductPass productPass) {

    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void jumpOtherProductDetail(ProductPass productPass) {
        inflateNewFragment(ProductDetailFragment.newInstance(productPass),
                ProductDetailFragment.class.getSimpleName());
    }

    private void inflateNewFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {
        CommonUtils.UniversalToast(this, message);
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
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    public void doReport(Bundle bundle) {
        ProductInfoIntentService.startAction(this, bundle, mReceiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if(fragment!=null){
            switch (resultCode) {
                case ProductInfoIntentService.STATUS_SUCCESS_REPORT_PRODUCT:
                    onReceiveResultSuccess(fragment,resultData,resultCode);
                    break;
                case ProductInfoIntentService.STATUS_ERROR_REPORT_PRODUCT:
                    onReceiveResultError(fragment, resultData,resultCode);
                    break;
            }
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData, int resultCode) {
        ((ProductDetailFragment) fragment).onErrorAction(resultData, resultCode);
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData, int resultCode) {
        ((ProductDetailFragment) fragment).onSuccessAction(resultData,resultCode);
    }

    @Override
    public void sendDataToInternet(int type, Bundle data) {
        /* Starting Download Service */
        addProductReceiver = new DownloadResultReceiver(new Handler());
        addProductReceiver.setReceiver(getAddProductReceiver());
        switch (type){
            case TkpdState.AddProduct.EDIT_PRODUCT:
            case TkpdState.AddProduct.ADD_PRODUCT:
            case TkpdState.AddProduct.ADD_PRODUCT_WITHOUT_IMAGE:
            case TkpdState.AddProduct.DELETE_PRODUCT:
                ProductService.startDownload(this, addProductReceiver, data, type);
                break;
            default :
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    private DownloadResultReceiver.Receiver getAddProductReceiver() {
        return new DownloadResultReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

            }
        };
    }
}