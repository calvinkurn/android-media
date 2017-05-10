package com.tokopedia.digital.product.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.RequestPermissionUtil;
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
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.product.presenter.IProductDigitalPresenter;
import com.tokopedia.digital.product.presenter.ProductDigitalPresenter;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 4/25/17.
 */
@RuntimePermissions
public class DigitalProductFragment extends BasePresenterFragment<IProductDigitalPresenter>
        implements IProductDigitalView, BannerAdapter.ActionListener,
        BaseDigitalProductView.ActionListener {
    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";

    private static final String EXTRA_STATE_OPERATOR_SELECTED = "EXTRA_STATE_OPERATOR_SELECTED";
    private static final String EXTRA_STATE_PRODUCT_SELECTED = "EXTRA_STATE_PRODUCT_SELECTED";
    private static final String EXTRA_STATE_CLIENT_NUMBER = "EXTRA_STATE_CLIENT_NUMBER";
    private static final String EXTRA_STATE_CATEGORY_DATA = "EXTRA_STATE_CATEGORY_DATA";
    private static final String EXTRA_STATE_BANNER_LIST_DATA = "EXTRA_STATE_BANNER_LIST_DATA";
    private static final String EXTRA_STATE_INSTANT_CHECKOUT_CHECKED =
            "EXTRA_STATE_INSTANT_CHECKOUT_CHECKED";

    private Operator operatorSelectedState;
    private Product productSelectedState;
    private String clientNumberState;
    private CategoryData categoryDataState;
    private List<BannerData> bannerDataListState;
    private boolean isInstantCheckoutChecked;

    @BindView(R2.id.rv_banner)
    RecyclerView rvBanner;
    @BindView(R2.id.holder_product_detail)
    LinearLayout holderProductDetail;

    private BannerAdapter bannerAdapter;
    private String categoryId;

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
        state.putString(EXTRA_STATE_CLIENT_NUMBER, digitalProductView.getClientNumber());
        state.putParcelable(EXTRA_STATE_OPERATOR_SELECTED, digitalProductView.getSelectedOperator());
        state.putParcelable(EXTRA_STATE_PRODUCT_SELECTED, digitalProductView.getSelectedProduct());
        state.putParcelable(EXTRA_STATE_CATEGORY_DATA, categoryDataState);
        state.putParcelableArrayList(
                EXTRA_STATE_BANNER_LIST_DATA, (ArrayList<? extends Parcelable>) bannerDataListState
        );
        state.putBoolean(
                EXTRA_STATE_INSTANT_CHECKOUT_CHECKED, digitalProductView.isInstantCheckoutChecked()
        );
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        clientNumberState = savedState.getString(EXTRA_STATE_CLIENT_NUMBER);
        operatorSelectedState = savedState.getParcelable(EXTRA_STATE_OPERATOR_SELECTED);
        productSelectedState = savedState.getParcelable(EXTRA_STATE_PRODUCT_SELECTED);
        isInstantCheckoutChecked = savedState.getBoolean(EXTRA_STATE_INSTANT_CHECKOUT_CHECKED);
        categoryDataState = savedState.getParcelable(EXTRA_STATE_CATEGORY_DATA);
        bannerDataListState = savedState.getParcelableArrayList(EXTRA_STATE_BANNER_LIST_DATA);

        presenter.processStateDataToReRender();
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

    public void renderStateSelectedAllData() {
        digitalProductView.renderStateDataSelected(
                clientNumberState, operatorSelectedState, productSelectedState,
                isInstantCheckoutChecked
        );
    }

    @Override
    public void renderBannerListData(String categoryName, List<BannerData> bannerDataList) {
        this.bannerDataListState = bannerDataList;
        bannerAdapter.addBannerDataListAndTitle(bannerDataList, categoryName);
    }

    @Override
    public void renderCategoryProductDataStyle1(CategoryData categoryData) {
        this.categoryDataState = categoryData;
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle1View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle2(CategoryData categoryData) {
        this.categoryDataState = categoryData;
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle2View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle3(CategoryData categoryData) {
        this.categoryDataState = categoryData;
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle3View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle4(CategoryData categoryData) {
        this.categoryDataState = categoryData;
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
    public ContentResolver getContentResolver() {
        return getActivity().getContentResolver();
    }

    @Override
    public CategoryData getCategoryDataState() {
        return categoryDataState;
    }

    @Override
    public List<BannerData> getBannerDataListState() {
        return bannerDataListState;
    }

    @Override
    public String getVersionInfoApplication() {
        return VersionInfo.getVersionInfo(getActivity());
    }

    @Override
    public String getUserLoginId() {
        return SessionHandler.getLoginID(getActivity());
    }

    @Override
    public Application getMainApplication() {
        return getActivity().getApplication();
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
    public String getCategoryId() {
        return categoryId;
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
        presenter.processAddToCartProduct(preCheckoutProduct);
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

    @Override
    public void onButtonContactPickerClicked() {
        DigitalProductFragmentPermissionsDispatcher.openContactPickerWithCheck(this);
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
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        showToastMessage(message);
                    }
                }
                break;
            case IDigitalModuleRouter.REQUEST_CODE_CONTACT_PICKER:
                try {
                    Uri contactURI = data.getData();
                    ContactData contact = presenter.processGenerateContactDataFromUri(contactURI);
                    renderContactDataToClientNumber(contact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DigitalProductFragmentPermissionsDispatcher.onRequestPermissionsResult(
                this, requestCode, grantResults
        );
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void openContactPicker() {
        Intent contactPickerIntent = new Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        );
        navigateToActivityRequest(
                contactPickerIntent, IDigitalModuleRouter.REQUEST_CODE_CONTACT_PICKER
        );
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForContacts() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_CONTACTS);
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showNeverAskForContacts() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_CONTACTS);
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForContacts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(
                getActivity(), request, Manifest.permission.READ_CONTACTS
        );
    }

    private void renderContactDataToClientNumber(ContactData contactData) {
        digitalProductView.renderClientNumberFromContact(contactData.getContactNumber());
    }

    private void handleCallBackProductChooser(Product product) {
        digitalProductView.renderUpdateProductSelected(product);
    }

    private void handleCallBackOperatorChooser(Operator operator) {
        digitalProductView.renderUpdateOperatorSelected(operator);
    }
}
