package com.tokopedia.gm.subscribe.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.gm.R;
import com.tokopedia.gm.subscribe.di.component.DaggerGmSubscribeComponent;
import com.tokopedia.gm.subscribe.di.module.GmSubscribeModule;
import com.tokopedia.gm.subscribe.view.presenter.GmCheckoutPresenterImpl;
import com.tokopedia.gm.subscribe.view.viewmodel.GmAutoSubscribeViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmCheckoutCurrentSelectedViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmCheckoutViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmVoucherViewModel;
import com.tokopedia.gm.subscribe.view.widget.checkout.AutoSubscribeViewHolder;
import com.tokopedia.gm.subscribe.view.widget.checkout.AutoSubscribeViewHolderCallback;
import com.tokopedia.gm.subscribe.view.widget.checkout.CodeVoucherViewHolder;
import com.tokopedia.gm.subscribe.view.widget.checkout.CodeVoucherViewHolderCallback;
import com.tokopedia.gm.subscribe.view.widget.checkout.CurrentSelectedProductViewHolder;
import com.tokopedia.gm.subscribe.view.widget.checkout.CurrentSelectedProductViewHolderCallback;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class GmCheckoutFragment
        extends BasePresenterFragment<GmCheckoutPresenterImpl>
        implements GmCheckoutView,
        CurrentSelectedProductViewHolderCallback,
        AutoSubscribeViewHolderCallback,
        CodeVoucherViewHolderCallback {

    public static final String TAG = "GMCheckoutFragment";
    public static final String AUTO_EXTEND_SELECTED = "AUTO_EXTEND_SELECTED";
    private static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    private static final Integer UNDEFINED_SELECTED_AUTO_EXTEND = -1;
    private Integer selectedProduct;
    private Integer autoExtendSelectedProduct = UNDEFINED_SELECTED_AUTO_EXTEND;
    private CurrentSelectedProductViewHolder currentSelectedProductViewHolder;
    private AutoSubscribeViewHolder autoSubscribeViewHolder;
    private CodeVoucherViewHolder codeVoucherViewHolder;
    private GmCheckoutFragmentCallback callback;
    private TkpdProgressDialog progressDialog;


    public static Fragment createFragment(int selectedFragment) {
        Fragment fragment = new GmCheckoutFragment();
        Bundle args = new Bundle();
        args.putInt(SELECTED_PRODUCT, selectedFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {
        bundle.putInt(AUTO_EXTEND_SELECTED, autoExtendSelectedProduct);
    }

    @Override
    public void onRestoreState(Bundle bundle) {
        autoExtendSelectedProduct = bundle.getInt(AUTO_EXTEND_SELECTED, UNDEFINED_SELECTED_AUTO_EXTEND);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = DaggerGmSubscribeComponent
                .builder()
                .appComponent(((HasComponent<AppComponent>)getActivity()).getComponent())
                .gmSubscribeModule(new GmSubscribeModule())
                .build()
                .getCheckoutPresenter();
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity instanceof GmCheckoutFragmentCallback) {
            callback = (GmCheckoutFragmentCallback) activity;
        } else {
            throw new RuntimeException("Please implement GMCheckoutFragmentListener in the activity");
        }

    }

    @Override
    protected void setupArguments(Bundle bundle) {
        selectedProduct = bundle.getInt(SELECTED_PRODUCT);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gm_subscribe_checkout;
    }

    @Override
    protected void initView(View view) {
        currentSelectedProductViewHolder = new CurrentSelectedProductViewHolder(this, view);
        autoSubscribeViewHolder = new AutoSubscribeViewHolder(
                this, view, !autoExtendSelectedProduct.equals(UNDEFINED_SELECTED_AUTO_EXTEND));
        codeVoucherViewHolder = new CodeVoucherViewHolder(this, view);
        Button buttonContinueCheckout = (Button) view.findViewById(R.id.button_checkout_gm_subscribe);
        buttonContinueCheckout.setOnClickListener(getContinueCheckoutListener());
        presenter.attachView(this);
        progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        presenter.getCurrentSelectedProduct(selectedProduct);
    }

    @Override
    public void renderCurrentSelectedProduct(GmCheckoutCurrentSelectedViewModel viewModel) {
        currentSelectedProductViewHolder.renderView(viewModel);
        if (!isAutoSubscribeUnselected()) {
            presenter.getExtendSelectedProduct(selectedProduct, autoExtendSelectedProduct);
        }
    }

    @Override
    public void changeCurrentSelected() {
        UnifyTracking.eventClickChangePackageGoldMerchant();
        callback.changeCurrentSelected(selectedProduct);
    }

    @Override
    public void updateSelectedProduct(int selectedProduct) {
        this.selectedProduct = selectedProduct;
        presenter.getCurrentSelectedProduct(selectedProduct);
        if (!isAutoSubscribeUnselected()) {
            presenter.getExtendSelectedProduct(selectedProduct, autoExtendSelectedProduct);
        }
    }

    @Override
    public void updateSelectedAutoProduct(int autoExtendSelectedProduct) {
        this.autoExtendSelectedProduct = autoExtendSelectedProduct;
        presenter.getExtendSelectedProduct(selectedProduct, autoExtendSelectedProduct);
    }

    @Override
    public void renderAutoSubscribeProduct(GmAutoSubscribeViewModel gmAutoSubscribeViewModel) {
        autoSubscribeViewHolder.renderAutoSubscribeProduct(gmAutoSubscribeViewModel);
    }

    @Override
    public void renderVoucherView(GmVoucherViewModel gmVoucherViewModel) {
        codeVoucherViewHolder.renderVoucherView(gmVoucherViewModel);
    }

    @Override
    public void goToDynamicPayment(GmCheckoutViewModel gmCheckoutDomainModel) {
        callback.goToDynamicPayment(
                gmCheckoutDomainModel.getPaymentUrl(),
                gmCheckoutDomainModel.getParameter(),
                gmCheckoutDomainModel.getCallbackUrl(),
                gmCheckoutDomainModel.getPaymentId());
    }

    @Override
    public void failedGetCurrentProduct() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getCurrentSelectedProduct(selectedProduct);
            }
        }).showRetrySnackbar();
    }

    @Override
    public void failedGetAutoSubscribeProduct() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getExtendSelectedProduct(selectedProduct, autoExtendSelectedProduct);
            }
        }).showRetrySnackbar();
    }

    @Override
    public void selectAutoSubscribePackageFirstTime() {
        callback.selectAutoSubscribePackageFirstTime();
    }

    @Override
    public void changeAutoSubscribePackage() {
        callback.changeAutoSubscribePackage(autoExtendSelectedProduct);
    }

    @Override
    public boolean isAutoSubscribeUnselected() {
        return autoExtendSelectedProduct.equals(UNDEFINED_SELECTED_AUTO_EXTEND);
    }

    @Override
    public void clearAutoSubscribePackage() {
        autoExtendSelectedProduct = UNDEFINED_SELECTED_AUTO_EXTEND;
    }

    @Override
    public void checkVoucher(String voucherCode) {
        presenter.checkVoucherCode(voucherCode, selectedProduct);
    }

    public View.OnClickListener getContinueCheckoutListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickSubscribeCheckoutGoldMerchant();
                goToCheckout();
            }
        };
    }

    private void goToCheckout() {
        if (codeVoucherViewHolder.isVoucherOpen()) {
            presenter.checkoutWithVoucherCheckGMSubscribe(selectedProduct, autoExtendSelectedProduct, codeVoucherViewHolder.getVoucherCode());
        } else {
            presenter.checkoutGMSubscribe(selectedProduct, autoExtendSelectedProduct, codeVoucherViewHolder.getVoucherCode());
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showGenericError() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void dismissKeyboardFromVoucherEditText() {
        codeVoucherViewHolder.dismissKeyboard();
    }

    @Override
    public void showMessageError(String string) {
        NetworkErrorHelper.showSnackbar(getActivity(), string);
    }

    @Override
    public void clearCacheShopInfo() {
        presenter.clearCacheShopInfo();
    }
}

