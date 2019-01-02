package com.tokopedia.digital.widget.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.presentation.activity.CartDigitalActivity;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.di.DigitalProductComponentInstance;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.ContactData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetView;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.presenter.DigitalWidgetCategoryCategoryPresenter;
import com.tokopedia.digital.widget.view.presenter.IDigitalWidgetCategoryPresenter;

import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author Rizky on 15/01/18.
 */
@RuntimePermissions
public class WidgetAllStyleRechargeFragment extends BasePresenterFragmentV4<IDigitalWidgetCategoryPresenter>
        implements IDigitalWidgetView, BaseDigitalProductView.ActionListener {

    private LinearLayout holderProductDetail;
    private ProgressBar pbMainLoading;

    private final String INSTANT = "instant";
    private final String NO_INSTANT = "no instant";

    protected static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";
    protected static final String ARG_TAB_INDEX_POSITION = "ARG_TAB_INDEX_POSITION";

    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";

    private String categoryId;

    private CategoryData categoryDataState;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;

    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    private BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> digitalProductView;

    @Inject
    DigitalWidgetCategoryCategoryPresenter presenter;

    public static WidgetAllStyleRechargeFragment newInstance(Category category, int position) {
        WidgetAllStyleRechargeFragment fragment = new WidgetAllStyleRechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.fetchCategory(categoryId);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA, digitalCheckoutPassDataState);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        digitalCheckoutPassDataState = savedState.getParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DigitalProductComponentInstance.getDigitalProductComponent(getActivity().getApplication())
                .inject(this);

        presenter.attachView(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        Category category = arguments.getParcelable(ARG_PARAM_CATEGORY);
        if (category != null) categoryId = String.valueOf(category.getId());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_widget2;
    }

    @Override
    protected void initView(View view) {
        holderProductDetail = view.findViewById(R.id.holder_product_detail);
        pbMainLoading = view.findViewById(R.id.pb_main_loading);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData,
                               HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        holderProductDetail.removeAllViews();
        if (this.digitalProductView == null) {
            this.digitalProductView = digitalProductView;
        }
        this.digitalProductView.setSource(BaseDigitalProductView.WIDGET);
        this.digitalProductView.setActionListener(this);
        this.digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(this.digitalProductView);
    }

    @Override
    public void onButtonBuyClicked(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
                                   boolean isInstantCheckoutChecked) {
        UnifyTracking.eventClickBuyOnWidget(getActivity(),categoryDataState.getName(), isInstantCheckoutChecked ? INSTANT : NO_INSTANT);

        if (!preCheckoutProduct.isCanBeCheckout()) {
            if (!TextUtils.isEmpty(preCheckoutProduct.getErrorCheckout())) {
                showToastMessage(preCheckoutProduct.getErrorCheckout());
            }
            return;
        }

        if (digitalProductView != null && categoryDataState != null) {
            Operator selectedOperator = digitalProductView.getSelectedOperator();
            Product selectedProduct = digitalProductView.getSelectedProduct();

            presenter.storeLastClientNumberTyped(
                    categoryId,
                    selectedOperator != null ? selectedOperator.getOperatorId() : "",
                    digitalProductView.getClientNumber(),
                    selectedProduct != null ? selectedProduct.getProductId() : "");
        }

        DigitalCheckoutPassData digitalCheckoutPassData = presenter.generateCheckoutPassData(preCheckoutProduct,
                VersionInfo.getVersionInfo(getActivity()),
                SessionHandler.getLoginID(getActivity()));

        if (SessionHandler.isV4Login(getActivity())) {
            if (getActivity().getApplication() instanceof DigitalRouter) {
                DigitalRouter digitalModuleRouter =
                        (DigitalRouter) getActivity().getApplication();
                navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        DigitalRouter.REQUEST_CODE_CART_DIGITAL
                );
            }
        } else {
            interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }

    private void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData) {
        this.digitalCheckoutPassDataState = digitalCheckoutPassData;
        Intent intent = ((IDigitalModuleRouter) MainApplication.getAppContext()).getLoginIntent(getActivity());
        navigateToActivityRequest(intent, IDigitalModuleRouter.REQUEST_CODE_LOGIN);
    }

    @Override
    public void onProductChooserClicked(List<Product> productListData, String operatorId, String titleChooser) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(
                        getActivity(), categoryId, operatorId, titleChooser
                ),
                IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
        getActivity().overridePendingTransition(R.anim.digital_slide_up_in, R.anim.digital_anim_stay);
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
        getActivity().overridePendingTransition(R.anim.digital_slide_up_in, R.anim.digital_anim_stay);
    }

    private void handleCallbackSearchNumber(OrderClientNumber orderClientNumber) {
        UnifyTracking.eventSelectNumberOnUserProfileWidget(getActivity(),categoryDataState.getName());

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

    @Override
    public void onButtonContactPickerClicked() {
        WidgetAllStyleRechargeFragmentPermissionsDispatcher.openContactPickerWithCheck(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DigitalRouter.REQUEST_CODE_CART_DIGITAL:
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
                if (SessionHandler.isV4Login(getActivity()) && digitalCheckoutPassDataState != null) {
                    if (getActivity().getApplication() instanceof DigitalRouter) {
                        DigitalRouter digitalModuleRouter =
                                (DigitalRouter) getActivity().getApplication();
                        navigateToActivityRequest(
                                digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassDataState),
                                DigitalRouter.REQUEST_CODE_CART_DIGITAL
                        );
                    }
                }
                break;
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallBackProductChooser(
                            (Product) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA
                            )
                    );
                break;
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handleCallBackOperatorChooser(
                            (Operator) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_OPERATOR_DATA
                            )
                    );
                }
                break;
        }
    }

    private void handleCallBackProductChooser(Product product) {
        if (digitalProductView != null) {
            digitalProductView.renderUpdateProductSelected(product);
        }
    }

    private void handleCallBackOperatorChooser(Operator operator) {
        if (digitalProductView != null)
            digitalProductView.renderUpdateOperatorSelected(operator);
    }

    private void renderContactDataToClientNumber(ContactData contactData) {
        if (digitalProductView != null)
            digitalProductView.renderClientNumber(contactData.getContactNumber());
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {
        pbMainLoading.setVisibility(View.VISIBLE);
        holderProductDetail.setVisibility(View.GONE);
    }

    @Override
    public void hideInitialProgressLoading() {
        pbMainLoading.setVisibility(View.GONE);
        holderProductDetail.setVisibility(View.VISIBLE);
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
    public void onProductDetailLinkClicked(String url) {

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
    public void onClientNumberClicked(String clientNumber, ClientNumber number, List<OrderClientNumber> numberList) {

    }

    @Override
    public void onClientNumberCleared(ClientNumber clientNumber, List<OrderClientNumber> recentClientNumberList) {

    }

    @Override
    public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {
        UnifyTracking.eventSelectNumberOnUserProfileWidget(getActivity(),categoryDataState.getName());

        handleCallbackSearchNumber(orderClientNumber);
    }

    @Override
    public void onOperatorSelected(String categoryName, String operatorName) {
        UnifyTracking.eventSelectOperatorOnWidget(getActivity(),categoryName,
                operatorName);
    }

    @Override
    public void onProductSelected(String categoryName, String productDesc) {
        UnifyTracking.eventSelectProductOnWidget(getActivity(),categoryName,
                productDesc);
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void onDestroy() {
        presenter.detachView();

        super.onDestroy();
    }

}
