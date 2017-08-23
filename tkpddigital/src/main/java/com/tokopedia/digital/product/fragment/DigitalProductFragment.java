package com.tokopedia.digital.product.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.activity.DigitalWebActivity;
import com.tokopedia.digital.product.adapter.BannerAdapter;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle1View;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle2View;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle3View;
import com.tokopedia.digital.product.compoundview.CategoryProductStyle4View;
import com.tokopedia.digital.product.compoundview.ClientNumberInputView;
import com.tokopedia.digital.product.data.mapper.IProductDigitalMapper;
import com.tokopedia.digital.product.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.domain.DigitalCategoryRepository;
import com.tokopedia.digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.domain.ILastOrderNumberRepository;
import com.tokopedia.digital.product.domain.LastOrderNumberRepository;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.interactor.ProductDigitalInteractor;
import com.tokopedia.digital.product.listener.IProductDigitalView;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.product.presenter.IProductDigitalPresenter;
import com.tokopedia.digital.product.presenter.ProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

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
    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";
    private static final String EXTRA_STATE_INSTANT_CHECKOUT_CHECKED =
            "EXTRA_STATE_INSTANT_CHECKOUT_CHECKED";
    private static final String EXTRA_STATE_HISTORY_CLIENT_NUMBER =
            "EXTRA_STATE_HISTORY_CLIENT_NUMBER";
    private static final String EXTRA_STATE_VOUCHER_CODE_COPIED =
            "EXTRA_STATE_VOUCHER_CODE_COPIED";

    private static final String CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL =
            "CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL";


    private Operator operatorSelectedState;
    private Product productSelectedState;
    private String clientNumberState;
    private CategoryData categoryDataState;
    private List<BannerData> bannerDataListState;
    private HistoryClientNumber historyClientNumberState;
    private String voucherCodeCopiedState;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;

    private boolean isInstantCheckoutChecked;

    @BindView(R2.id.main_container)
    NestedScrollView mainHolderContainer;
    @BindView(R2.id.pb_main_loading)
    ProgressBar pbMainLoading;
    @BindView(R2.id.rv_banner)
    RecyclerView rvBanner;
    @BindView(R2.id.holder_product_detail)
    LinearLayout holderProductDetail;

    private BannerAdapter bannerAdapter;
    private String categoryId;

    private CompositeSubscription compositeSubscription;
    private BaseDigitalProductView<
            CategoryData, Operator, Product, HistoryClientNumber
            > digitalProductView;

    private LocalCacheHandler cacheHandlerLastInputClientNumber;
    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    private ActionListener actionListener;

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
        if (digitalProductView != null) {
            state.putString(EXTRA_STATE_CLIENT_NUMBER, digitalProductView.getClientNumber());
            state.putParcelable(
                    EXTRA_STATE_OPERATOR_SELECTED, digitalProductView.getSelectedOperator()
            );
            state.putParcelable(
                    EXTRA_STATE_PRODUCT_SELECTED, digitalProductView.getSelectedProduct()
            );
            state.putBoolean(
                    EXTRA_STATE_INSTANT_CHECKOUT_CHECKED,
                    digitalProductView.isInstantCheckoutChecked()
            );
        }
        state.putParcelable(EXTRA_STATE_CATEGORY_DATA, categoryDataState);
        state.putParcelableArrayList(
                EXTRA_STATE_BANNER_LIST_DATA, (ArrayList<? extends Parcelable>) bannerDataListState
        );
        state.putParcelable(EXTRA_STATE_HISTORY_CLIENT_NUMBER, historyClientNumberState);
        state.putString(EXTRA_STATE_VOUCHER_CODE_COPIED, voucherCodeCopiedState);
        state.putParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA, digitalCheckoutPassDataState);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        clientNumberState = savedState.getString(EXTRA_STATE_CLIENT_NUMBER);
        operatorSelectedState = savedState.getParcelable(EXTRA_STATE_OPERATOR_SELECTED);
        productSelectedState = savedState.getParcelable(EXTRA_STATE_PRODUCT_SELECTED);
        isInstantCheckoutChecked = savedState.getBoolean(EXTRA_STATE_INSTANT_CHECKOUT_CHECKED);
        categoryDataState = savedState.getParcelable(EXTRA_STATE_CATEGORY_DATA);
        bannerDataListState = savedState.getParcelableArrayList(EXTRA_STATE_BANNER_LIST_DATA);
        historyClientNumberState = savedState.getParcelable(EXTRA_STATE_HISTORY_CLIENT_NUMBER);
        voucherCodeCopiedState = savedState.getString(EXTRA_STATE_VOUCHER_CODE_COPIED);
        digitalCheckoutPassDataState = savedState.getParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA);

        presenter.processStateDataToReRender();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        bannerAdapter = new BannerAdapter(this);
        cacheHandlerLastInputClientNumber = new LocalCacheHandler(
                getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER
        );
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        IProductDigitalMapper productDigitalMapper = new ProductDigitalMapper();
        IDigitalCategoryRepository digitalCategoryRepository =
                new DigitalCategoryRepository(digitalEndpointService, productDigitalMapper);
        ILastOrderNumberRepository lastOrderNumberRepository =
                new LastOrderNumberRepository(digitalEndpointService, productDigitalMapper);
        IProductDigitalInteractor productDigitalInteractor =
                new ProductDigitalInteractor(
                        compositeSubscription, digitalCategoryRepository,
                        lastOrderNumberRepository, cacheHandlerLastInputClientNumber
                );
        presenter = new ProductDigitalPresenter(this, productDigitalInteractor);
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
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
        mainHolderContainer.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mainHolderContainer.setFocusable(true);
        mainHolderContainer.setFocusableInTouchMode(true);
        mainHolderContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view instanceof ClientNumberInputView) {
                    view.requestFocusFromTouch();
                } else {
                    view.clearFocus();
                }
                return false;
            }
        });
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
        digitalProductView.restoreStateData(
                categoryDataState, historyClientNumberState, operatorSelectedState,
                productSelectedState, clientNumberState, isInstantCheckoutChecked
        );
    }

    @Override
    public void renderBannerListData(String title, List<BannerData> bannerDataList) {
        String formattedTitle = getResources().getString(R.string.promo_category, title);
        this.bannerDataListState = getBannerDataWithoutEmptyItem(bannerDataList);
        bannerAdapter.addBannerDataListAndTitle(bannerDataList, formattedTitle);
    }

    private List<BannerData> getBannerDataWithoutEmptyItem(List<BannerData> bannerDataList) {
        for (int i = bannerDataList.size() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(bannerDataList.get(i).getTitle()) && TextUtils.isEmpty(bannerDataList.get(i).getSubtitle())) {
                bannerDataList.remove(bannerDataList.get(i));
            }
        }
        return bannerDataList;
    }

    @Override
    public void renderCategoryProductDataStyle1(CategoryData categoryData,
                                                HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        this.historyClientNumberState = historyClientNumber;
        actionListener.updateTitleToolbar(categoryData.getName());
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle1View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle2(CategoryData categoryData,
                                                HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        this.historyClientNumberState = historyClientNumber;
        actionListener.updateTitleToolbar(categoryData.getName());
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle2View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle3(CategoryData categoryData,
                                                HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        this.historyClientNumberState = historyClientNumber;
        actionListener.updateTitleToolbar(categoryData.getName());
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle3View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle4(CategoryData categoryData,
                                                HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        this.historyClientNumberState = historyClientNumber;
        actionListener.updateTitleToolbar(categoryData.getName());
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle4View(getActivity());
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderErrorStyleNotSupportedProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorHttpProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorNoConnectionProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorTimeoutConnectionProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
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
    public HistoryClientNumber getHistoryClientNumberState() {
        return historyClientNumberState;
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
        pbMainLoading.setVisibility(View.VISIBLE);
        mainHolderContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideInitialProgressLoading() {
        pbMainLoading.setVisibility(View.GONE);
        mainHolderContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearContentRendered() {
        pbMainLoading.setVisibility(View.GONE);
        mainHolderContainer.setVisibility(View.GONE);
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
    public void closeViewWithMessageAlert(String message) {
        Intent intent = new Intent();
        intent.putExtra(IDigitalModuleRouter.EXTRA_MESSAGE, message);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showSnackBarCallbackCloseView(String message) {
        View view = getView();
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    clearContentRendered();
                    closeView();
                }
            });
            snackbar.show();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            closeView();
        }
    }

    @Override
    public LocalCacheHandler getLastInputClientNumberChaceHandler() {
        return cacheHandlerLastInputClientNumber;
    }

    @Override
    public boolean isUserLoggedIn() {
        return SessionHandler.isV4Login(getActivity());
    }

    @Override
    public void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData) {
        this.digitalCheckoutPassDataState = digitalCheckoutPassData;
        Intent intent = SessionRouter.getLoginActivityIntent(getActivity());
        intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        navigateToActivityRequest(intent, IDigitalModuleRouter.REQUEST_CODE_LOGIN);
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
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return DeviceUtil.getDigitalIdentifierParam(getActivity());
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
    public void onDestroyView() {
        super.onDestroyView();
        if (digitalProductView != null && categoryDataState != null) {
            Operator selectedOperator = digitalProductView.getSelectedOperator();
            Product selectedProduct = digitalProductView.getSelectedProduct();
            presenter.processStoreLastInputClientNumberByCategory(
                    digitalProductView.getClientNumber(),
                    categoryDataState.getCategoryId(),
                    selectedOperator != null ? selectedOperator.getOperatorId() : "",
                    selectedProduct != null ? selectedProduct.getProductId() : ""
            );
        }
    }

    @Override
    public void onButtonBuyClicked(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct) {
        if (!preCheckoutProduct.isCanBeCheckout()) {
            showToastMessage(preCheckoutProduct.getErrorCheckout());
            return;
        }
        preCheckoutProduct.setVoucherCodeCopied(voucherCodeCopiedState);
        presenter.processAddToCartProduct(presenter.generateCheckoutPassData(preCheckoutProduct));
    }

    @Override
    public void onProductChooserStyle1Clicked(List<Product> productListData, String titleChooser) {

        UnifyTracking.eventSelectProduct(categoryDataState.getName(), categoryDataState.getName());

        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(
                        getActivity(), productListData, titleChooser
                ),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
    }

    @Override
    public void onProductChooserStyle2Clicked(List<Product> productListData, String titleChooser) {

        UnifyTracking.eventSelectProduct(categoryDataState.getName(), categoryDataState.getName());

        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(
                        getActivity(), productListData, titleChooser
                ),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
    }

    @Override
    public void onOperatorChooserStyle3Clicked(List<Operator> operatorListData, String titleChooser) {

        UnifyTracking.eventSelectOperator(categoryDataState.getName(), categoryDataState.getName());

        startActivityForResult(
                DigitalChooserActivity.newInstanceOperatorChooser(
                        getActivity(), operatorListData, titleChooser,categoryDataState.getName()
                ),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER
        );
    }

    @Override
    public void onProductChooserStyle3Clicked(List<Product> productListData, String titleChooser) {

        UnifyTracking.eventSelectProduct(categoryDataState.getName(), categoryDataState.getName());

        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(
                        getActivity(), productListData, titleChooser
                ),
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
    public void onProductDetailLinkClicked(String url) {
        startActivity(DigitalWebActivity.newInstance(getActivity(), url));
    }

    @Override
    public boolean isRecentInstantCheckoutUsed(String categoryId) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    getActivity(), TkpdCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        return cacheHandlerRecentInstantCheckoutUsed.getBoolean(
                TkpdCache.Key.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, false
        );
    }


    @Override
    public void storeLastInstantCheckoutUsed(String categoryId, boolean checked) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    getActivity(), TkpdCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        cacheHandlerRecentInstantCheckoutUsed.putBoolean(
                TkpdCache.Key.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, checked
        );
        cacheHandlerRecentInstantCheckoutUsed.applyEditor();
    }

    @Override
    public void onButtonCopyBannerVoucherCodeClicked(String voucherCode) {
        this.voucherCodeCopiedState = voucherCode;
        ClipboardManager clipboard = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL, voucherCode
        );
        clipboard.setPrimaryClip(clip);
        showToastMessage(getString(R.string.message_voucher_code_banner_copied));
    }

    @Override
    public void onBannerItemClicked(BannerData bannerData) {
        if (TextUtils.isEmpty(bannerData.getLink())) return;
        navigateToActivity(DigitalWebActivity.newInstance(
                getActivity(), bannerData.getLink())
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallBackOperatorChooser(
                            (Operator) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_OPERATOR_DATA
                            )
                    );
                break;
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallBackProductChooser(
                            (Product) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA
                            )
                    );
                break;
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
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
            case IDigitalModuleRouter.REQUEST_CODE_LOGIN:
                if (isUserLoggedIn() && digitalCheckoutPassDataState != null) {
                    presenter.processAddToCartProduct(digitalCheckoutPassDataState);
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_digital_product_detail, menu);
        if (GlobalConfig.isSellerApp()) {
            menu.findItem(R.id.action_menu_subscription_digital).setVisible(false);
            menu.findItem(R.id.action_menu_product_list_digital).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_product_list_digital) {
            navigateToActivity(
                    DigitalWebActivity.newInstance(
                            getActivity(), TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_PRODUCT_LIST
                    )
            );
            return true;
        } else if (item.getItemId() == R.id.action_menu_subscription_digital) {
            navigateToActivity(
                    DigitalWebActivity.newInstance(
                            getActivity(), TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_SUBSCRIPTIONS
                    )
            );
            return true;
        } else if (item.getItemId() == R.id.action_menu_transaction_list_digital) {
            navigateToActivity(
                    DigitalWebActivity.newInstance(
                            getActivity(), TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_TRANSACTION_LIST
                    )
            );
            return true;
        } else {
            return super.onOptionsItemSelected(item);
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

    public interface ActionListener {
        void updateTitleToolbar(String title);
    }
}
