package com.tokopedia.digital.product.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.data.source.FavoriteListDataSource;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.compoundview.ClientNumberInputView;
import com.tokopedia.digital.product.data.mapper.USSDMapper;
import com.tokopedia.digital.product.data.repository.UssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.interactor.DigitalGetHelpUrlUseCase;
import com.tokopedia.digital.product.domain.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.domain.interactor.ProductDigitalInteractor;
import com.tokopedia.digital.product.receiver.USSDBroadcastReceiver;
import com.tokopedia.digital.product.service.USSDAccessibilityService;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.activity.DigitalSearchNumberActivity;
import com.tokopedia.digital.product.view.activity.DigitalUssdActivity;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.digital.product.view.adapter.BannerAdapter;
import com.tokopedia.digital.product.view.compoundview.CheckPulsaBalanceView;
import com.tokopedia.digital.product.view.listener.IProductDigitalView;
import com.tokopedia.digital.product.view.listener.IUssdUpdateListener;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.ClientNumber;
import com.tokopedia.digital.product.view.model.ContactData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.product.view.presenter.IProductDigitalPresenter;
import com.tokopedia.digital.product.view.presenter.ProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.digital.product.view.activity.DigitalSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER;

/**
 * @author anggaprasetiyo on 4/25/17.
 */
@RuntimePermissions
public class DigitalProductFragment extends BasePresenterFragment<IProductDigitalPresenter>
        implements IProductDigitalView, BannerAdapter.ActionListener,
        BaseDigitalProductView.ActionListener, IUssdUpdateListener, CheckPulsaBalanceView.ActionListener {

    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";
    private static final String ARG_PARAM_EXTRA_OPERATOR_ID = "ARG_PARAM_EXTRA_OPERATOR_ID";
    private static final String ARG_PARAM_EXTRA_PRODUCT_ID = "ARG_PARAM_EXTRA_PRODUCT_ID";
    private static final String ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER";

    private static final String EXTRA_STATE_OPERATOR_SELECTED = "EXTRA_STATE_OPERATOR_SELECTED";
    private static final String EXTRA_STATE_PRODUCT_SELECTED = "EXTRA_STATE_PRODUCT_SELECTED";
    private static final String EXTRA_STATE_CLIENT_NUMBER = "EXTRA_STATE_CLIENT_NUMBER";
    private static final String EXTRA_STATE_CATEGORY_DATA = "EXTRA_STATE_CATEGORY_DATA";
    private static final String EXTRA_STATE_BANNER_LIST_DATA = "EXTRA_STATE_BANNER_LIST_DATA";
    private static final String EXTRA_STATE_OTHER_BANNER_LIST_DATA = "EXTRA_STATE_OTHER_BANNER_LIST_DATA";
    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";
    private static final String EXTRA_STATE_INSTANT_CHECKOUT_CHECKED =
            "EXTRA_STATE_INSTANT_CHECKOUT_CHECKED";
    private static final String EXTRA_STATE_HISTORY_CLIENT_NUMBER =
            "EXTRA_STATE_HISTORY_CLIENT_NUMBER";
    private static final String EXTRA_STATE_VOUCHER_CODE_COPIED =
            "EXTRA_STATE_VOUCHER_CODE_COPIED";
    private static final String EXTRA_STATE_HELP_URL = "EXTRA_STATE_HELP_URL";

    private static final String CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL =
            "CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL";

    @BindView(R2.id.main_container)
    NestedScrollView mainHolderContainer;
    @BindView(R2.id.pb_main_loading)
    ProgressBar pbMainLoading;
    @BindView(R2.id.rv_banner)
    RecyclerView rvBanner;
    @BindView(R2.id.holder_product_detail)
    LinearLayout holderProductDetail;
    @BindView(R2.id.holder_check_balance)
    LinearLayout holderCheckBalance;

    private ProductDigitalPresenter presenter;

    private Operator operatorSelectedState;
    private Product productSelectedState;
    private String clientNumberState;
    private CategoryData categoryDataState;
    private List<BannerData> bannerDataListState;
    private List<BannerData> otherBannerDataListState;
    private HistoryClientNumber historyClientNumberState;
    private String voucherCodeCopiedState;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;
    private String digitalHelpUrl;

    private boolean isInstantCheckoutChecked;

    private BannerAdapter bannerAdapter;

    private String categoryId;
    private String operatorId;
    private String productId;
    private String clientNumber;

    private CheckPulsaBalanceView selectedCheckPulsaBalanceView;

    private BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> digitalProductView;

    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    private ActionListener actionListener;

    private USSDBroadcastReceiver ussdBroadcastReceiver;
    private ShowCaseDialog showCaseDialog;
    private int selectedSimIndex = 0;//start from 0
    private boolean ussdInProgress = false;

    public static Fragment newInstance(
            String categoryId, String operatorId, String productId, String clientNumber) {
        Fragment fragment = new DigitalProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_ID, operatorId);
        bundle.putString(ARG_PARAM_EXTRA_PRODUCT_ID, productId);
        bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.processGetHelpUrlData(categoryId);
        presenter.processGetCategoryAndBannerData(
                categoryId, operatorId, productId, clientNumber);
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
        state.putParcelableArrayList(
                EXTRA_STATE_OTHER_BANNER_LIST_DATA, (ArrayList<? extends Parcelable>) otherBannerDataListState
        );
        state.putParcelable(EXTRA_STATE_HISTORY_CLIENT_NUMBER, historyClientNumberState);
        state.putString(EXTRA_STATE_VOUCHER_CODE_COPIED, voucherCodeCopiedState);
        state.putParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA, digitalCheckoutPassDataState);
        state.putString(EXTRA_STATE_HELP_URL, digitalHelpUrl);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        clientNumberState = savedState.getString(EXTRA_STATE_CLIENT_NUMBER);
        operatorSelectedState = savedState.getParcelable(EXTRA_STATE_OPERATOR_SELECTED);
        productSelectedState = savedState.getParcelable(EXTRA_STATE_PRODUCT_SELECTED);
        isInstantCheckoutChecked = savedState.getBoolean(EXTRA_STATE_INSTANT_CHECKOUT_CHECKED);
        categoryDataState = savedState.getParcelable(EXTRA_STATE_CATEGORY_DATA);
        bannerDataListState = savedState.getParcelableArrayList(EXTRA_STATE_BANNER_LIST_DATA);
        otherBannerDataListState = savedState.getParcelableArrayList(EXTRA_STATE_OTHER_BANNER_LIST_DATA);
        historyClientNumberState = savedState.getParcelable(EXTRA_STATE_HISTORY_CLIENT_NUMBER);
        voucherCodeCopiedState = savedState.getString(EXTRA_STATE_VOUCHER_CODE_COPIED);
        digitalCheckoutPassDataState = savedState.getParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA);
        digitalHelpUrl = savedState.getString(EXTRA_STATE_HELP_URL);
        presenter.processStateDataToReRender();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        bannerAdapter = new BannerAdapter(this);

        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        CategoryDetailDataSource categoryDetailDataSource = new CategoryDetailDataSource(
                digitalEndpointService, new GlobalCacheManager(), new ProductDigitalMapper()
        );
        FavoriteListDataSource favoriteListDataSource = new FavoriteListDataSource(
                digitalEndpointService, new FavoriteNumberListDataMapper()
        );
        IDigitalCategoryRepository digitalCategoryRepository = new DigitalCategoryRepository(
                categoryDetailDataSource, favoriteListDataSource
        );

        IUssdCheckBalanceRepository ussdCheckBalanceRepository = new UssdCheckBalanceRepository(
                digitalEndpointService, new USSDMapper());

        IProductDigitalInteractor productDigitalInteractor =
                new ProductDigitalInteractor(ussdCheckBalanceRepository
                );

        GetCategoryByIdUseCase getCategoryByIdUseCase = new GetCategoryByIdUseCase(
                getActivity(), digitalCategoryRepository
        );

        DigitalGetHelpUrlUseCase digitalGetHelpUrlUseCase = new DigitalGetHelpUrlUseCase(
                digitalCategoryRepository
        );

        presenter = new ProductDigitalPresenter(getActivity(),
                new LocalCacheHandler(getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER),
                this, productDigitalInteractor, getCategoryByIdUseCase, digitalGetHelpUrlUseCase);
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        categoryId = arguments.getString(ARG_PARAM_EXTRA_CATEGORY_ID);
        operatorId = arguments.getString(ARG_PARAM_EXTRA_OPERATOR_ID);
        productId = arguments.getString(ARG_PARAM_EXTRA_PRODUCT_ID);
        clientNumber = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER);
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

        selectedCheckPulsaBalanceView = null;

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

    @Override
    public void renderOtherBannerListData(String title, List<BannerData> otherBannerDataList) {
        String formattedTitle = getResources().getString(R.string.promo_category, title);
        this.otherBannerDataListState = getBannerDataWithoutEmptyItem(otherBannerDataList);
        bannerAdapter.addBannerDataListAndTitle(otherBannerDataList, formattedTitle);
    }

    private List<BannerData> getBannerDataWithoutEmptyItem(List<BannerData> bannerDataList) {
        for (int i = bannerDataList.size() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(bannerDataList.get(i).getTitle()) &&
                    TextUtils.isEmpty(bannerDataList.get(i).getSubtitle())) {
                bannerDataList.remove(bannerDataList.get(i));
            }
        }
        return bannerDataList;
    }

    @Override
    public void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData, HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        this.historyClientNumberState = historyClientNumber;
        actionListener.updateTitleToolbar(categoryData.getName());
        holderProductDetail.removeAllViews();
        if (this.digitalProductView == null) {
            this.digitalProductView = digitalProductView;
        }
        this.digitalProductView.setActionListener(this);
        this.digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(this.digitalProductView);
    }

    @Override
    public void renderCheckPulsaBalanceData(int selectedSim, String ussdCode, String phoneNumber, String operatorErrorMsg, Boolean isSimActive, String carrierName) {
        CheckPulsaBalanceView checkPulsaBalanceView = new CheckPulsaBalanceView(getActivity());
        checkPulsaBalanceView.setActionListener(this);

        checkPulsaBalanceView.renderData(selectedSim, ussdCode, phoneNumber, operatorErrorMsg, isSimActive, carrierName);
        holderCheckBalance.addView(checkPulsaBalanceView);

        startShowCaseUSSD();
    }

    @Override
    public void removeCheckPulsaCards() {
        holderCheckBalance.removeAllViews();
    }

    @Override
    public void showHelpMenu(String url) {
        digitalHelpUrl = url;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public String getHelpUrl() {
        return digitalHelpUrl;
    }

    @Override
    public void navigateToWebview(String helpUrl) {
        if (getActivity() != null && getActivity().getApplication() instanceof DigitalModuleRouter) {
            Intent intent = ((DigitalModuleRouter) getActivity().getApplication())
                    .getDefaultContactUsIntent(getActivity(), helpUrl, getString(R.string.digital_product_help_menu_label));
            if (intent != null) {
                startActivity(intent);
            }
        }
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
    public CategoryData getCategoryDataState() {
        return categoryDataState;
    }

    @Override
    public List<BannerData> getBannerDataListState() {
        return bannerDataListState;
    }

    @Override
    public List<BannerData> getOtherBannerDataListState() {
        return otherBannerDataListState;
    }

    @Override
    public HistoryClientNumber getHistoryClientNumberState() {
        return historyClientNumberState;
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
        if (pbMainLoading != null) {
            pbMainLoading.setVisibility(View.GONE);
        }
        if (mainHolderContainer != null) {
            mainHolderContainer.setVisibility(View.GONE);
        }
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
    public boolean isUserLoggedIn() {
        return SessionHandler.isV4Login(getActivity());
    }

    @Override
    public void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData) {
        this.digitalCheckoutPassDataState = digitalCheckoutPassData;
        Intent intent = ((IDigitalModuleRouter) MainApplication.getAppContext())
                .getLoginIntent(getActivity());
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
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onButtonBuyClicked(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
                                   boolean isInstantCheckoutChecked) {
        String isInstant = isInstantCheckoutChecked ? "instant" : "no instant";
        UnifyTracking.eventClickBuyOnNative(categoryDataState.getName(), isInstant);

        if (!preCheckoutProduct.isCanBeCheckout()) {
            if (!TextUtils.isEmpty(preCheckoutProduct.getErrorCheckout())) {
                showToastMessage(preCheckoutProduct.getErrorCheckout());
            }
            return;
        }
        preCheckoutProduct.setVoucherCodeCopied(voucherCodeCopiedState);

        DigitalCheckoutPassData digitalCheckoutPassData = presenter.generateCheckoutPassData(preCheckoutProduct,
                VersionInfo.getVersionInfo(getActivity()),
                SessionHandler.getLoginID(getActivity()));

        if (SessionHandler.isV4Login(getActivity())) {
            if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
                IDigitalModuleRouter digitalModuleRouter =
                        (IDigitalModuleRouter) getActivity().getApplication();
                navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                );
            }
        } else {
            interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }

    @Override
    public void onButtonCheckBalanceClicked(int simPosition, String ussdCode, CheckPulsaBalanceView checkPulsaBalanceView) {
        if (ussdInProgress) {
            showToastMessage(getString(R.string.msg_ussd_please_wait));
        } else {
            selectedSimIndex = simPosition;
            selectedCheckPulsaBalanceView = checkPulsaBalanceView;
            Operator operator = presenter.getSelectedUssdOperator(simPosition);
            String phoneNumber = presenter.getUssdPhoneNumberFromCache(simPosition);
            String carrierName = DeviceUtil.getOperatorName(getActivity(), simPosition);
            if (carrierName != null && !presenter.isCarrierSignalsNotAvailable(carrierName)
                    && !DeviceUtil.validateNumberAndMatchOperator(categoryDataState.getClientNumberList().get(0).getValidation(),
                    operator, phoneNumber)) {
                presenter.storeUssdPhoneNumber(simPosition, "");
            }
            DigitalProductFragmentPermissionsDispatcher.checkBalanceByUSSDWithCheck(this, simPosition, ussdCode);
        }
    }

    @Override
    public void onProductChooserClicked(List<Product> productListData, String operatorId, String titleChooser) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(
                        getActivity(), categoryId, operatorId, titleChooser
                ),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
    }

    @Override
    public void onOperatorChooserStyle3Clicked(List<Operator> operatorListData, String titleChooser) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceOperatorChooser(
                        getActivity(), categoryId, titleChooser,
                        categoryDataState.getOperatorLabel(),
                        categoryDataState.getName()
                ),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER
        );
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
    public void onClientNumberClicked(String number, ClientNumber clientNumber, List<OrderClientNumber> numberList) {
        if (!numberList.isEmpty()) {
            startActivityForResult(
                    DigitalSearchNumberActivity.newInstance(
                            getActivity(), categoryId, clientNumber, number, numberList
                    ),
                    IDigitalModuleRouter.REQUEST_CODE_DIGITAL_SEARCH_NUMBER
            );
        }
    }

    @Override
    public void onClientNumberCleared(ClientNumber clientNumber, List<OrderClientNumber> numberList) {
        if (!numberList.isEmpty()) {
            startActivityForResult(
                    DigitalSearchNumberActivity.newInstance(
                            getActivity(), categoryId, clientNumber, "", numberList
                    ),
                    IDigitalModuleRouter.REQUEST_CODE_DIGITAL_SEARCH_NUMBER
            );
        }
    }

    @Override
    public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {

    }

    @Override
    public void onOperatorSelected(String categoryName, String operatorName) {
        UnifyTracking.eventSelectOperatorOnNativePage(categoryName,
                operatorName);
    }

    @Override
    public void onProductSelected(String categoryName, String productDesc) {
        UnifyTracking.eventSelectProductOnNativePage(categoryName,
                productDesc);
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
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                        String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            showToastMessage(message);
                        }
                    }
                }
                break;
            case IDigitalModuleRouter.REQUEST_CODE_CONTACT_PICKER:
                try {
                    Uri contactURI = data.getData();
                    ContactData contact = presenter.processGenerateContactDataFromUri(contactURI,
                            getActivity().getContentResolver());
                    renderContactDataToClientNumber(contact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case IDigitalModuleRouter.REQUEST_CODE_LOGIN:
                if (isUserLoggedIn() && digitalCheckoutPassDataState != null) {
                    if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
                        IDigitalModuleRouter digitalModuleRouter =
                                (IDigitalModuleRouter) getActivity().getApplication();
                        navigateToActivityRequest(
                                digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassDataState),
                                IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                        );
                    }
                }
                break;
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_SEARCH_NUMBER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    OrderClientNumber orderClientNumber = data.getParcelableExtra(EXTRA_CALLBACK_CLIENT_NUMBER);
                    handleCallbackSearchNumber(orderClientNumber);
                } else {
                    handleCallbackSearchNumberCancel();
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
        if (digitalHelpUrl != null && digitalHelpUrl.length() > 0) {
            menu.findItem(R.id.action_menu_help_digital).setVisible(true);
        } else {
            menu.findItem(R.id.action_menu_help_digital).setVisible(false);
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
            if (categoryDataState != null) {
                UnifyTracking.eventClickDaftarTransaksiEvent(categoryDataState.getName(), categoryDataState.getName());
            }

            navigateToActivity(
                    DigitalWebActivity.newInstance(
                            getActivity(), TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_TRANSACTION_LIST
                    )
            );
            return true;
        } else if (item.getItemId() == R.id.action_menu_help_digital) {
            presenter.onHelpMenuClicked();
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
        try {
            navigateToActivityRequest(
                    contactPickerIntent, IDigitalModuleRouter.REQUEST_CODE_CONTACT_PICKER
            );
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            NetworkErrorHelper.showSnackbar(getActivity(),
                    getString(R.string.error_message_contact_not_found));
        }
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

    @NeedsPermission({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    public void checkBalanceByUSSD(int simPosition, String ussdCode) {
        presenter.processToCheckBalance(null, simPosition, ussdCode);
        UnifyTracking.eventUssd(AppEventTracking.Action.CLICK_USSD_CEK_SALDO, DeviceUtil.getOperatorName(getActivity(), simPosition) + " - " + presenter.getDeviceMobileNumber(simPosition));
        UnifyTracking.eventUssdAttempt(getString(R.string.ussd_permission_allowed_label));
    }

    @OnPermissionDenied({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void showDeniedForPhone() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CALL_PHONE);
        UnifyTracking.eventUssdAttempt(getString(R.string.ussd_permission_denied_label));
    }

    @OnNeverAskAgain({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void showNeverAskForPhone() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CALL_PHONE);
        UnifyTracking.eventUssdAttempt(getString(R.string.ussd_permission_denied_label));
    }

    private void renderContactDataToClientNumber(ContactData contactData) {
        digitalProductView.renderClientNumber(contactData.getContactNumber());
    }

    private void handleCallbackSearchNumber(OrderClientNumber orderClientNumber) {
        if (orderClientNumber != null) {
            UnifyTracking.eventSelectNumberOnUserProfileNative(categoryDataState.getName());
        }

        if (categoryDataState.isSupportedStyle()) {
            switch (categoryDataState.getOperatorStyle()) {
                case CategoryData.STYLE_PRODUCT_CATEGORY_1:
                case CategoryData.STYLE_PRODUCT_CATEGORY_99:
                    handleStyle1(orderClientNumber);
                    break;
                case CategoryData.STYLE_PRODUCT_CATEGORY_2:
                case CategoryData.STYLE_PRODUCT_CATEGORY_3:
                case CategoryData.STYLE_PRODUCT_CATEGORY_4:
                case CategoryData.STYLE_PRODUCT_CATEGORY_5:
                    handleStyleOther(orderClientNumber);
                    break;
            }
        }
    }

    private void handleStyleOther(OrderClientNumber orderClientNumber) {
        Operator selectedOperator = null;
        if (orderClientNumber.getOperatorId() != null) {
            for (Operator operator : categoryDataState.getOperatorList()) {
                if (orderClientNumber.getOperatorId().equals(operator.getOperatorId())) {
                    selectedOperator = operator;
                    digitalProductView.renderUpdateOperatorSelected(operator);
                }
            }
        }

        digitalProductView.renderClientNumber(orderClientNumber.getClientNumber());
        digitalProductView.clearFocusOnClientNumber();

        if (selectedOperator != null) {
            for (Product product : selectedOperator.getProductList()) {
                if (orderClientNumber.getProductId() != null) {
                    if (orderClientNumber.getProductId().equals(product.getProductId())) {
                        digitalProductView.renderUpdateProductSelected(product);
                    }
                }
            }
        }
    }

    private void handleStyle1(OrderClientNumber orderClientNumber) {
        digitalProductView.renderClientNumber(orderClientNumber.getClientNumber());
        digitalProductView.clearFocusOnClientNumber();

        if (orderClientNumber.getOperatorId() != null) {
            for (Operator operator : categoryDataState.getOperatorList()) {
                if (orderClientNumber.getOperatorId().equals(operator.getOperatorId())) {
                    for (Product product : operator.getProductList()) {
                        if (orderClientNumber.getProductId() != null) {
                            if (orderClientNumber.getProductId().equals(product.getProductId())) {
                                digitalProductView.renderUpdateProductSelected(product);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleCallbackSearchNumberCancel() {
        digitalProductView.clearFocusOnClientNumber();
    }

    private void handleCallBackProductChooser(Product product) {
        digitalProductView.renderUpdateProductSelected(product);
    }

    private void handleCallBackOperatorChooser(Operator operator) {
        digitalProductView.renderUpdateOperatorSelected(operator);
    }

    @Override
    public void showMessageAlert(String message, String title) {
        View view = getView();
        if (view == null) {
            return;
        }
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        Dialog dialog = alertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void showAccessibilityAlertDialog() {
        View view = getView();
        if (view == null) {
            return;
        }
        AlertDialog.Builder accessibiltyDialog = new AlertDialog.Builder(getActivity());
        accessibiltyDialog.setMessage(getActivity().getString(R.string.dialog_accessibility_service_on));
        accessibiltyDialog.setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
        accessibiltyDialog.setNegativeButton("DENY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        Dialog dialog = accessibiltyDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void registerUssdReciever() {
        if (selectedCheckPulsaBalanceView != null)
            selectedCheckPulsaBalanceView.showCheckBalanceProgressbar();
        if (ussdBroadcastReceiver == null) {
            ussdBroadcastReceiver = new USSDBroadcastReceiver(this);
            getActivity().registerReceiver(ussdBroadcastReceiver, new IntentFilter(
                    USSDBroadcastReceiver.ACTION_GET_BALANCE_FROM_USSD
            ));

        }
        Intent intent = new Intent(context, USSDAccessibilityService.class);
        intent.putExtra(USSDAccessibilityService.KEY_START_SERVICE_FROM_APP, true);
        context.startService(intent);
        ussdInProgress = true;

    }

    @Override
    public void renderPulsaBalance(PulsaBalance pulsaBalance, int selectedSim) {
        if (getActivity() != null) {
            ussdInProgress = false;
            String number = "";
            if (selectedCheckPulsaBalanceView != null) {
                selectedCheckPulsaBalanceView.hideProgressbar();
                number = selectedCheckPulsaBalanceView.getPhoneNumberText();
            }
            if (pulsaBalance != null && pulsaBalance.isSuccess()) {
                pulsaBalance.setMobileNumber(number);
                UnifyTracking.eventUssdAttempt(getString(R.string.status_success_label) );
                startActivity(DigitalUssdActivity.newInstance(getActivity(), pulsaBalance, presenter.getSelectedUssdOperator(selectedSim),
                        categoryDataState.getClientNumberList().get(0).getValidation(),
                        categoryId, categoryDataState.getName(), selectedSim, presenter.getSelectedUssdOperatorList(selectedSim)));
            } else {
                showMessageAlert(getActivity().getString(R.string.error_message_ussd_msg_not_parsed), getActivity().getString(R.string.message_ussd_title));
                UnifyTracking.eventUssdAttempt(getString(R.string.status_failed_label) +  getActivity().getString(R.string.error_message_ussd_msg_not_parsed));
            }
        }
    }

    private void startShowCaseUSSD() {
        final String showCaseTag = DigitalProductFragment.class.getName();
        if (ShowCasePreference.hasShown(getActivity(), showCaseTag)) {
            return;
        }
        if (showCaseDialog != null) {
            return;
        }
        showCaseDialog = createShowCase();
        showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                return false;
            }
        });

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();
        showCaseObjectList.add(new ShowCaseObject(
                holderCheckBalance,
                getString(R.string.title_showcase_ussd),
                getString(R.string.message_showcase_ussd),
                ShowCaseContentPosition.UNDEFINED,
                R.color.tkpd_main_green));
        showCaseDialog.show(getActivity(), showCaseTag, showCaseObjectList);
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .customView(R.layout.view_layout_showcase)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .prevStringRes(R.string.navigate_back)
                .nextStringRes(R.string.next)
                .finishStringRes(R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(false)
                .build();
    }

    @Override
    public void showPulsaBalanceError(String message) {
        if(getActivity() != null) {
            ussdInProgress = false;
            if (selectedCheckPulsaBalanceView != null)
                selectedCheckPulsaBalanceView.hideProgressbar();
            showMessageAlert(message, getActivity().getString(R.string.message_ussd_title));
        }
    }

    @Override
    public void onReceivedUssdData(String result) {
        presenter.processPulsaBalanceUssdResponse(result, selectedSimIndex);
    }

    @Override
    public void onUssdDataError(String errorMessage) {

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.renderCheckPulsa();
    }

    @Override
    public void onDestroy() {
        if (ussdBroadcastReceiver != null)
            getActivity().unregisterReceiver(ussdBroadcastReceiver);
        presenter.removeUssdTimerCallback();

        presenter.detachView();

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (digitalProductView != null && categoryDataState != null) {
            Operator selectedOperator = digitalProductView.getSelectedOperator();
            Product selectedProduct = digitalProductView.getSelectedProduct();

            presenter.storeLastClientNumberTyped(
                    categoryId,
                    selectedOperator != null ? selectedOperator.getOperatorId() : "",
                    digitalProductView.getClientNumber(),
                    selectedProduct != null ? selectedProduct.getProductId() : "");
        }
    }

    public interface ActionListener {
        void updateTitleToolbar(String title);
    }

}
