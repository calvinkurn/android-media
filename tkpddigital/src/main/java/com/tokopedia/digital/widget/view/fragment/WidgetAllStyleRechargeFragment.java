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
import android.widget.Toast;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.repository.IDigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.data.source.FavoriteListDataSource;
import com.tokopedia.digital.common.domain.DigitalCategoryUseCase;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.compoundview.CategoryProductStyle1View;
import com.tokopedia.digital.common.view.compoundview.CategoryProductStyle2View;
import com.tokopedia.digital.common.view.compoundview.CategoryProductStyle3View;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.ClientNumber;
import com.tokopedia.digital.product.view.model.ContactData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetView;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.presenter.DigitalWidgetPresenter;
import com.tokopedia.digital.widget.view.presenter.IDigitalWidgetPresenter;

import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rizky on 15/01/18.
 */
@RuntimePermissions
public class WidgetAllStyleRechargeFragment extends BasePresenterFragmentV4<IDigitalWidgetPresenter>
        implements IDigitalWidgetView, BaseDigitalProductView.ActionListener {

    protected static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";
    protected static final String ARG_TAB_INDEX_POSITION = "ARG_TAB_INDEX_POSITION";

    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";

    private String categoryId;

    private CategoryData categoryDataState;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;

    private DigitalWidgetPresenter presenter;

    private CompositeSubscription compositeSubscription;

    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    @BindView(R2.id.holder_product_detail)
    LinearLayout holderProductDetail;

    private BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> digitalProductView;

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
    protected void initialPresenter() {
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        CategoryDetailDataSource categoryDetailDataSource = new CategoryDetailDataSource(
                digitalEndpointService, new GlobalCacheManager(), new ProductDigitalMapper()
        );
        FavoriteListDataSource favoriteListDataSource = new FavoriteListDataSource(
                digitalEndpointService, new FavoriteNumberListDataMapper()
        );
        IDigitalCategoryRepository digitalRepository = new DigitalCategoryRepository(
                categoryDetailDataSource, favoriteListDataSource
        );
        DigitalCategoryUseCase digitalCategoryUseCase = new DigitalCategoryUseCase(
                getContext(), digitalRepository
        );

        presenter = new DigitalWidgetPresenter(getActivity(), this,
                digitalCategoryUseCase);
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
    public void renderCategoryProductDataStyle1(CategoryData categoryData, HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle1View(getActivity());
        digitalProductView.setSource(BaseDigitalProductView.WIDGET);
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle2(CategoryData categoryData, HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle2View(getActivity());
        digitalProductView.setSource(BaseDigitalProductView.WIDGET);
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void renderCategoryProductDataStyle3(CategoryData categoryData, HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        holderProductDetail.removeAllViews();
        if (digitalProductView == null)
            digitalProductView = new CategoryProductStyle3View(getActivity());
        digitalProductView.setSource(BaseDigitalProductView.WIDGET);
        digitalProductView.setActionListener(this);
        digitalProductView.renderData(categoryData, historyClientNumber);
        holderProductDetail.addView(digitalProductView);
    }

    @Override
    public void onButtonBuyClicked(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
                                   boolean isInstantCheckoutChecked) {
        if (!preCheckoutProduct.isCanBeCheckout()) {
            showToastMessage(preCheckoutProduct.getErrorCheckout());
            return;
        }

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

    private void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData) {
        this.digitalCheckoutPassDataState = digitalCheckoutPassData;
        Intent intent = OldSessionRouter.getLoginActivityIntent(getActivity());
        intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        navigateToActivityRequest(intent, IDigitalModuleRouter.REQUEST_CODE_LOGIN);
    }

    @Override
    public void onProductChooserStyle1Clicked(List<Product> productListData, String operatorId, String titleChooser) {

    }

    @Override
    public void onProductChooserStyle2Clicked(List<Product> productListData, String titleChooser) {

    }

    @Override
    public void onProductChooserStyle3Clicked(List<Product> productListData, String operatorId, String titleChooser) {

    }

    @Override
    public void onOperatorChooserStyle3Clicked(List<Operator> operatorListData, String titleChooser) {

    }

    @Override
    public void onCannotBeCheckoutProduct(String messageError) {

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
                if (SessionHandler.isV4Login(getActivity()) && digitalCheckoutPassDataState != null) {
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
        }
    }

    private void renderContactDataToClientNumber(ContactData contactData) {
        digitalProductView.renderClientNumber(contactData.getContactNumber());
    }

    private void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
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
        UnifyTracking.eventSelectNumberOnUserProfileWidget(categoryDataState.getName());
    }

    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

            presenter.storeLastClientNumberTyped(categoryId, selectedOperator.getOperatorId(),
                    digitalProductView.getClientNumber(), selectedProduct.getProductId());
        }
    }
}
