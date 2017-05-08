package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.adapter.BannerAdapter;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle1View;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle2View;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle3View;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle4View;
import com.tokopedia.digital.product.data.mapper.IProductDigitalMapper;
import com.tokopedia.digital.product.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.domain.DigitalCategoryRepository;
import com.tokopedia.digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.interactor.ProductDigitalInteractor;
import com.tokopedia.digital.product.listener.IProductDigitalView;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.product.presenter.IProductDigitalPresenter;
import com.tokopedia.digital.product.presenter.ProductDigitalPresenter;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class DigitalProductFragment extends BasePresenterFragment<IProductDigitalPresenter>
        implements IProductDigitalView, BannerAdapter.ActionListener,
        BaseDigitalProductView.ActionListener {
    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";

    private String categoryId;

    @BindView(R2.id.rv_banner)
    RecyclerView rvBanner;
    @BindView(R2.id.holder_product_detail)
    LinearLayout holderProductDetail;

    private BannerAdapter bannerAdapter;

    private CompositeSubscription compositeSubscription;
    private BaseDigitalProductView<CategoryData> digitalProductView;

    public static Fragment newInstance(String categoryId) {
        Fragment fragment = new DigitalProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.processGetCategoryAndBannerData();
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
        bannerAdapter = new BannerAdapter(this);
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        IProductDigitalMapper productDigitalMapper = new ProductDigitalMapper();
        IDigitalCategoryRepository digitalCategoryRepository =
                new DigitalCategoryRepository(digitalEndpointService, productDigitalMapper);
        IProductDigitalInteractor productDigitalInteractor =
                new ProductDigitalInteractor(compositeSubscription, digitalCategoryRepository);
        presenter = new ProductDigitalPresenter(this, productDigitalInteractor);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        categoryId = arguments.getString(ARG_PARAM_EXTRA_CATEGORY_ID);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_digital_module;
    }

    @Override
    protected void initView(View view) {
        rvBanner.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
    }

    @Override
    protected void setViewListener() {
        rvBanner.setAdapter(bannerAdapter);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public void renderBannerListData(String categoryName, List<BannerData> bannerDataList) {
        bannerAdapter.addBannerDataListAndTitle(bannerDataList, categoryName);
    }

    @Override
    public void renderCategoryProductDataStyle1(CategoryData categoryData) {
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle1View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle2(CategoryData categoryData) {
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle2View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle3(CategoryData categoryData) {
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle3View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle4(CategoryData categoryData) {
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle4View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderErrorStyleNotSupportedProductDigitalData(String message) {

    }

    @Override
    public void renderErrorProductDigitalData(String message) {

    }

    @Override
    public void renderErrorHttpProductDigitalData(String message) {

    }

    @Override
    public void renderErrorNoConnectionProductDigitalData(String message) {

    }

    @Override
    public void renderErrorTimeoutConnectionProductDigitalData(String message) {

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
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onButtonBuyClicked(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct) {
        String clientNumber = preCheckoutProduct.getClientNumber();
        DigitalCheckoutPassData digitalCheckoutPassData = new DigitalCheckoutPassData.Builder()
                .action("init_data")
                .categoryId(preCheckoutProduct.getCategoryId())
                .clientNumber(clientNumber)
                .instantCheckout(preCheckoutProduct.isInstantCheckout() ? "1" : "0")
                .isPromo(preCheckoutProduct.isPromo() ? "1" : "0")
                .operatorId(preCheckoutProduct.getOperatorId())
                .productId(preCheckoutProduct.getProductId())
                .utmCampaign((preCheckoutProduct.getCategoryName()))
                .utmContent(VersionInfo.getVersionInfo(getActivity()))
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource("android")
                .utmMedium("widget")
                .build();
        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }
    }

    @Override
    public void onProductChooserStyle1Clicked(List<Product> productListData) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(getActivity(), productListData),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
    }

    @Override
    public void onProductChooserStyle2Clicked(List<Product> productListData) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(getActivity(), productListData),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
    }

    @Override
    public void onOperatorChooserStyle3Clicked(List<Operator> operatorListData) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceOperatorChooser(getActivity(), operatorListData),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER
        );
    }

    @Override
    public void onProductChooserStyle3Clicked(List<Product> productListData) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(getActivity(), productListData),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
    }

    @Override
    public void onCannotBeCheckoutProduct(String messageError) {
        showToastMessage(messageError);
    }

    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return SessionHandler.getLoginID(getActivity()) + "_"
                + (token.isEmpty() ? timeMillis : token);
    }

    @Override
    public void onButtonCopyBannerVoucherCodeClicked(String voucherCode) {

    }

    @Override
    public void onBannerItemClicked(BannerData bannerData) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER:
                if (resultCode == Activity.RESULT_OK)
                    handleCallBackOperatorChooser(
                            (Operator) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_OPERATOR_DATA
                            )
                    );
                break;
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK)
                    handleCallBackProductChooser(
                            (Product) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA
                            )
                    );
                break;
        }
    }

    private void handleCallBackProductChooser(Product product) {
        digitalProductView.renderUpdateProductSelected(product);
    }

    private void handleCallBackOperatorChooser(Operator operator) {
        digitalProductView.renderUpdateOperatorSelected(operator);
    }
}
