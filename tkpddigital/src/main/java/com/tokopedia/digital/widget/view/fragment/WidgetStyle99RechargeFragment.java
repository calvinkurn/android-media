package com.tokopedia.digital.widget.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetInteractor;
import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetRepository;
import com.tokopedia.digital.widget.view.compoundview.WidgetClientNumberView;
import com.tokopedia.digital.widget.view.compoundview.WidgetProductChooserView;
import com.tokopedia.digital.widget.view.compoundview.WidgetWrapperBuyView;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetStyle1View;
import com.tokopedia.digital.widget.view.model.PreCheckoutDigitalWidget;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.category.ClientNumber;
import com.tokopedia.digital.widget.view.model.lastorder.Attributes;
import com.tokopedia.digital.widget.view.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.view.model.mapper.OperatorMapper;
import com.tokopedia.digital.widget.view.model.mapper.ProductMapper;
import com.tokopedia.digital.widget.view.model.operator.Operator;
import com.tokopedia.digital.widget.view.model.product.Product;
import com.tokopedia.digital.widget.view.presenter.DigitalWidgetStyle1Presenter;
import com.tokopedia.digital.widget.view.presenter.IDigitalWidgetStyle1Presenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rizky on 10/01/18.
 */
@Deprecated
public class WidgetStyle99RechargeFragment extends BaseWidgetRechargeFragment<IDigitalWidgetStyle1Presenter>
        implements IDigitalWidgetStyle1View {

    private static final String STATE_CLIENT_NUMBER = "STATE_CLIENT_NUMBER";

    @BindView(R2.id.holder_widget_client_number)
    LinearLayout holderWidgetClientNumber;
    @BindView(R2.id.holder_widget_wrapper_buy)
    LinearLayout holderWidgetWrapperBuy;
    @BindView(R2.id.holder_widget_spinner_product)
    LinearLayout holderWidgetSpinnerProduct;

    private DigitalWidgetStyle1Presenter presenter;

    private WidgetClientNumberView widgetClientNumberView;
    private WidgetWrapperBuyView widgetWrapperBuyView;
    private WidgetProductChooserView widgetProductChooserView;

    private CompositeSubscription compositeSubscription;

    private WidgetStyle99RechargeFragment.QueryListener queryListener;

    private Operator selectedOperator;
    private Product selectedProduct;

    private LastOrder lastOrder;

    private interface QueryListener {
        void onQueryChanged(String query);
    }

    public static WidgetStyle99RechargeFragment newInstance(Category category, int position) {
        WidgetStyle99RechargeFragment fragment = new WidgetStyle99RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        compositeSubscription = new CompositeSubscription();

        DigitalWidgetInteractor interactor = new DigitalWidgetInteractor(
                compositeSubscription,
                new DigitalWidgetRepository(new DigitalEndpointService(), new FavoriteNumberListDataMapper()),
                new ProductMapper(),
                new OperatorMapper(),
                new JobExecutor(),
                new UIThread());

        presenter = new DigitalWidgetStyle1Presenter(getActivity(),
                new LocalCacheHandler(getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER),
                interactor, this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_widget;
    }

    @Override
    protected void onFirstTimeLaunched() {
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        presenter.getOperatorAndProductsByOperatorId(category.getId(),
                String.valueOf(category.getAttributes().getDefaultOperatorId()));
    }

    private void renderClientNumberView() {
        clearHolder(holderWidgetClientNumber);
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        widgetClientNumberView.setClientNumberLabel(clientNumber.getText());
        widgetClientNumberView.setHint(clientNumber.getPlaceholder());
        widgetClientNumberView.setVisibilityPhoneBook(category.getAttributes().isUsePhonebook());
        holderWidgetClientNumber.addView(widgetClientNumberView);
    }

    @Override
    protected void initView(View view) {
        widgetClientNumberView = new WidgetClientNumberView(getActivity());
        widgetWrapperBuyView = new WidgetWrapperBuyView(getActivity());
        widgetProductChooserView = new WidgetProductChooserView(getActivity());
    }

    @Override
    protected void setViewListener() {
        setRechargeEditTextTouchCallback(widgetClientNumberView);

        widgetClientNumberView.setButtonPickerListener(getButtonPickerListener());
        widgetClientNumberView.setRechargeEditTextListener(getEditTextListener());
        widgetWrapperBuyView.setListener(getBuyButtonListener());
    }

    private WidgetClientNumberView.OnButtonPickerListener getButtonPickerListener() {
        return new WidgetClientNumberView.OnButtonPickerListener() {
            @Override
            public void onButtonContactClicked() {
                BaseWidgetRechargeFragmentPermissionsDispatcher
                        .doLaunchContactPickerWithCheck(WidgetStyle99RechargeFragment.this);
            }
        };
    }

    private WidgetClientNumberView.RechargeEditTextListener getEditTextListener() {
        return new WidgetClientNumberView.RechargeEditTextListener() {
            @Override
            public void onRechargeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onRechargeTextClear() {

            }

            @Override
            public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {
                UnifyTracking.eventSelectNumberOnUserProfileWidget(category.getAttributes().getName());

                LastOrder lastOrder = new LastOrder();
                Attributes attributes = new Attributes();
                attributes.setClientNumber(orderClientNumber.getClientNumber());
                attributes.setCategoryId(Integer.valueOf(orderClientNumber.getCategoryId()));
                attributes.setOperatorId(Integer.valueOf(orderClientNumber.getOperatorId()));
                if (orderClientNumber.getProductId() != null) {
                    attributes.setProductId(Integer.valueOf(orderClientNumber.getProductId()));
                }
                lastOrder.setAttributes(attributes);

                setLastOrder(lastOrder);
            }
        };
    }

    private void setLastOrder(LastOrder lastOrder) {
        this.lastOrder = lastOrder;
    }

    private WidgetWrapperBuyView.OnBuyButtonListener getBuyButtonListener() {
        return new WidgetWrapperBuyView.OnBuyButtonListener() {
            @Override
            public void goToNativeCheckout() {
                if (widgetProductChooserView.checkStockProduct(selectedProduct))
                    presenter.storeLastInstantCheckoutUsed(String.valueOf(category.getId()),
                            widgetWrapperBuyView.isCreditCheckboxChecked());

                DigitalCheckoutPassData digitalCheckoutPassData =
                        widgetWrapperBuyView.getGeneratedCheckoutPassData(getDataPreCheckout());

                if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
                    IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
                    startActivityForResult(
                            digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                            IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                    );
                }

                presenter.storeLastClientNumberTyped(String.valueOf(category.getId()),
                        widgetClientNumberView.getText(), selectedProduct);
            }

            @Override
            public void goToLoginPage() {
                digitalCheckoutPassDataState =
                        widgetWrapperBuyView.getGeneratedCheckoutPassData(getDataPreCheckout());

                Intent intent = OldSessionRouter.getLoginActivityIntent(getActivity());
                intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);

                presenter.storeLastClientNumberTyped(String.valueOf(category.getId()),
                        widgetClientNumberView.getText(), selectedProduct);

                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }

            @Override
            public boolean isRecentInstantCheckoutUsed(String categoryId) {
                return presenter.isRecentInstantCheckoutUsed(categoryId);
            }

            @Override
            public void trackingCheckInstantSaldo(boolean isChecked) {

            }
        };
    }

    private WidgetProductChooserView.ProductChoserListener getProductChoserListener() {
        return new WidgetProductChooserView.ProductChoserListener() {
            @Override
            public void initDataView(Product productSelected) {
                selectedProduct = productSelected;
                widgetProductChooserView.checkStockProduct(productSelected);
            }

            @Override
            public void trackingProduct() {
                if (selectedProduct != null)
                    UnifyTracking.eventSelectProductOnWidget(category.getAttributes().getName(),
                            selectedProduct.getAttributes().getDesc());
            }
        };
    }

    private PreCheckoutDigitalWidget getDataPreCheckout() {
        PreCheckoutDigitalWidget preCheckoutDigitalWidget = new PreCheckoutDigitalWidget();
        preCheckoutDigitalWidget.setClientNumber(widgetClientNumberView.getText());
        preCheckoutDigitalWidget.setOperatorId(String.valueOf(selectedOperator.getId()));
        preCheckoutDigitalWidget.setProductId(selectedProduct.getId());
        preCheckoutDigitalWidget.setPromoProduct(selectedProduct.getAttributes().getPromo() != null);
        preCheckoutDigitalWidget.setBundle(bundle);
        return preCheckoutDigitalWidget;
    }

    @Override
    public void saveAndDisplayPhoneNumber(String phoneNumber) {
        selectedOperator = null;
        widgetClientNumberView.setText(phoneNumber);
    }

    @Override
    public void renderNumberList(List<OrderClientNumber> results) {
        if (SessionHandler.isV4Login(getActivity())) {
            widgetClientNumberView.setDropdownAutoComplete(results);
        }
    }

    @Override
    public void renderLastOrder(LastOrder lastOrder) {
        if (presenter != null) {
            this.lastOrder = lastOrder;
            if (lastOrder != null && lastOrder.getAttributes() != null && category != null) {
                widgetClientNumberView.setText(lastOrder.getAttributes().getClientNumber());
            }
        }
    }

    @Override
    public void renderDataOperator(Operator operatorModel) {
        selectedOperator = operatorModel;
        if (category.getAttributes().getClientNumber().isShown()) {
            UnifyTracking.eventSelectOperatorOnWidget(category.getAttributes().getName(),
                    selectedOperator.getAttributes().getName());
            renderClientNumberView();
            widgetClientNumberView.setFilterMaxLength(operatorModel.getAttributes().getMaximumLength());
            widgetClientNumberView.setImgOperator(operatorModel.getAttributes().getImage());
            widgetClientNumberView.setImgOperatorVisible();
        }
        widgetProductChooserView.setTitleProduct(operatorModel.getAttributes().getRule().getProductText());
        widgetProductChooserView.setVisibilityProduct(operatorModel.getAttributes().getRule().isShowProduct());
    }

    @Override
    public void renderDataProducts(List<Product> products, boolean showPrice) {
        clearHolder(holderWidgetSpinnerProduct);
        clearHolder(holderWidgetWrapperBuy);

        widgetProductChooserView.setListener(getProductChoserListener());
        widgetProductChooserView.renderDataView(products, showPrice, lastOrder, lastProductSelected);
        widgetWrapperBuyView.setCategory(category);
        widgetWrapperBuyView.renderInstantCheckoutOption(
                category.getAttributes().isInstantCheckoutAvailable());
        holderWidgetSpinnerProduct.addView(widgetProductChooserView);
        holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
    }

    @Override
    public void renderEmptyProduct(String message) {
        widgetClientNumberView.setImgOperatorInvisible();
        clearHolder(holderWidgetWrapperBuy);
    }

    @Override
    public void renderEmptyOperator(String message) {

    }

    @Override
    public void renderProduct(Product product) {
        selectedProduct = product;
        if (SessionHandler.isV4Login(getActivity())) {
            if (widgetProductChooserView.checkStockProduct(selectedProduct))
                widgetWrapperBuyView.goToNativeCheckout();
        } else {
            widgetWrapperBuyView.goToLoginPage();
        }
    }

    @Override
    public void renderOperator(Operator operatorModel) {
        selectedOperator = operatorModel;
        widgetClientNumberView.setImgOperator(operatorModel.getAttributes().getImage());
        widgetClientNumberView.setImgOperatorVisible();
    }

    @Override
    public void renderLastTypedClientNumber(String lastClientNumberTyped) {
        widgetClientNumberView.setText(lastClientNumberTyped);
        presenter.getOperatorById(lastOperatorSelected);
    }

    @Override
    public void renderErrorProduct(String message) {
        showSnackbarErrorMessage(message);
    }

    @Override
    public void renderDefaultError() {

    }

    @Override
    public void renderVerifiedNumber(String verifiedNumber) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(STATE_CLIENT_NUMBER, widgetClientNumberView.getText());
        super.onSaveState(state);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        super.onRestoreState(savedState);

        presenter.getOperatorAndProductsByOperatorId(category.getId(), category.getAttributes().getDefaultOperatorId());
    }

    @Override
    public void onDestroyView() {
        clearHolder(holderWidgetClientNumber);
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
        removeRechargeEditTextCallback(widgetClientNumberView);

        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }

        super.onDestroyView();
    }

}
