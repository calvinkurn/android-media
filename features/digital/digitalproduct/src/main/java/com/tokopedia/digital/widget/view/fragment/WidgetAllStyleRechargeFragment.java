package com.tokopedia.digital.widget.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
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
import com.tokopedia.user.session.UserSession;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author Rizky on 15/01/18.
 */
@RuntimePermissions
public class WidgetAllStyleRechargeFragment extends BaseDaggerFragment
        implements IDigitalWidgetView, BaseDigitalProductView.ActionListener {

    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final int REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER = 1002;
    private static final int REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER = 1003;
    private static final int REQUEST_CODE_CONTACT_PICKER = 1004;
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
    UserSession userSession;
    @Inject
    DigitalWidgetCategoryCategoryPresenter presenter;
    @Inject
    DigitalAnalytics digitalAnalytics;
    @Inject
    DigitalModuleRouter digitalModuleRouter;

    public static WidgetAllStyleRechargeFragment newInstance(Category category, int position) {
        WidgetAllStyleRechargeFragment fragment = new WidgetAllStyleRechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Category category = getArguments().getParcelable(ARG_PARAM_CATEGORY);
        if (category != null) categoryId = String.valueOf(category.getId());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        DigitalProductComponentInstance.getDigitalProductComponent(getActivity().getApplication())
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_widget2, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //fix crash drawHardwareAccelerated on home
        //because hierarchy of view is too deep, then require a lot memories
        //https://fabric.io/pt-tokopedia/android/apps/com.tokopedia.tkpd/issues/5c77446af8b88c29635d8e38?time=last-ninety-days
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        presenter.attachView(this);
        presenter.fetchCategory(categoryId);
    }

    protected void initView(View view) {
        holderProductDetail = view.findViewById(R.id.holder_product_detail);
        pbMainLoading = view.findViewById(R.id.pb_main_loading);
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
        digitalAnalytics.eventClickBuyOnWidget(categoryDataState.getName(), isInstantCheckoutChecked ? INSTANT : NO_INSTANT);

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
                GlobalConfig.VERSION_NAME,
                userSession.getUserId());

        if (userSession.isLoggedIn()) {
            if (getActivity().getApplication() instanceof DigitalRouter) {
                DigitalRouter digitalModuleRouter =
                        (DigitalRouter) getActivity().getApplication();
                navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        DigitalRouter.Companion.getREQUEST_CODE_CART_DIGITAL()
                );
            }
        } else {
            interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }

    private void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData) {
        this.digitalCheckoutPassDataState = digitalCheckoutPassData;
        navigateToActivityRequest(digitalModuleRouter.getLoginIntent(getActivity()), REQUEST_CODE_LOGIN);
    }

    @Override
    public void onProductChooserClicked(List<Product> productListData, String operatorId, String titleChooser) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(
                        getActivity(), categoryId, operatorId, titleChooser
                ),
                REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
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
                REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER
        );
        getActivity().overridePendingTransition(R.anim.digital_slide_up_in, R.anim.digital_anim_stay);
    }

    private void handleCallbackSearchNumber(OrderClientNumber orderClientNumber) {
        digitalAnalytics.eventSelectNumberOnUserProfileWidget(categoryDataState.getName());

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
                    contactPickerIntent, REQUEST_CODE_CONTACT_PICKER
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
            case REQUEST_CODE_CONTACT_PICKER:
                try {
                    Uri contactURI = data.getData();
                    ContactData contact = presenter.processGenerateContactDataFromUri(contactURI,
                            getActivity().getContentResolver());
                    renderContactDataToClientNumber(contact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_LOGIN:
                if (userSession.isLoggedIn() && digitalCheckoutPassDataState != null) {
                    if (getActivity().getApplication() instanceof DigitalRouter) {
                        DigitalRouter digitalModuleRouter =
                                (DigitalRouter) getActivity().getApplication();
                        navigateToActivityRequest(
                                digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassDataState),
                                DigitalRouter.Companion.getREQUEST_CODE_CART_DIGITAL()
                        );
                    }
                }
                break;
            case REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallBackProductChooser(
                            (Product) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA
                            )
                    );
                break;
            case REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handleCallBackOperatorChooser(
                            (Operator) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_OPERATOR_DATA
                            )
                    );
                }
                break;
        }
        if (requestCode == DigitalRouter.Companion.getREQUEST_CODE_CART_DIGITAL()) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (data.hasExtra(DigitalRouter.Companion.getEXTRA_MESSAGE())) {
                    String message = data.getStringExtra(DigitalRouter.Companion.getEXTRA_MESSAGE());
                    if (!TextUtils.isEmpty(message)) {
                        showToastMessage(message);
                    }
                }
            }
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
                    getActivity(), DigitalCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        return cacheHandlerRecentInstantCheckoutUsed.getBoolean(
                DigitalCache.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, false
        );
    }

    @Override
    public void storeLastInstantCheckoutUsed(String categoryId, boolean checked) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    getActivity(), DigitalCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        cacheHandlerRecentInstantCheckoutUsed.putBoolean(
                DigitalCache.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, checked
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
        digitalAnalytics.eventSelectNumberOnUserProfileWidget(categoryDataState.getName());

        handleCallbackSearchNumber(orderClientNumber);
    }

    @Override
    public void onOperatorSelected(String categoryName, String operatorName) {
        digitalAnalytics.eventSelectOperatorOnWidget(categoryName,
                operatorName);
    }

    @Override
    public void onProductSelected(String categoryName, String productDesc) {
        digitalAnalytics.eventSelectProductOnWidget(categoryName,
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
    public Map<String, String> getGeneratedAuthParamNetwork(Map<String, String> originParams) {
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

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onInstantCheckoutChanged(String categoryName, boolean isChecked) {
        digitalAnalytics.eventCheckInstantSaldo(categoryName, isChecked);
    }
}
