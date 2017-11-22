package com.tokopedia.digital.widget.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.compoundview.WidgetClientNumberView;
import com.tokopedia.digital.widget.compoundview.WidgetProductChooserView;
import com.tokopedia.digital.widget.compoundview.WidgetWrapperBuyView;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.interactor.DigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle1View;
import com.tokopedia.digital.widget.model.PreCheckoutDigitalWidget;
import com.tokopedia.digital.widget.model.category.Category;
import com.tokopedia.digital.widget.model.category.ClientNumber;
import com.tokopedia.digital.widget.model.lastorder.Attributes;
import com.tokopedia.digital.widget.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.model.mapper.OperatorMapper;
import com.tokopedia.digital.widget.model.mapper.ProductMapper;
import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;
import com.tokopedia.digital.widget.presenter.DigitalWidgetStyle1Presenter;
import com.tokopedia.digital.widget.presenter.IDigitalWidgetStyle1Presenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author rizkyfadillah on 10/16/2017.
 */

public class WidgetStyle1RechargeFragment extends BaseWidgetRechargeFragment<IDigitalWidgetStyle1Presenter> implements IDigitalWidgetStyle1View {

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
    private Operator selectedOperator;
    private LastOrder lastOrder;
    private Product selectedProduct;

    private String selectedOperatorId;
    private int minLengthDefaultOperator;
    private boolean showPrice = true;

    private CompositeSubscription compositeSubscription;

    public static WidgetStyle1RechargeFragment newInstance(Category category, int position, boolean useCache) {
        WidgetStyle1RechargeFragment fragment = new WidgetStyle1RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        bundle.putBoolean(ARG_USE_CACHE, useCache);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onFirstTimeLaunched() {
        lastClientNumberTyped = presenter.getLastClientNumberTyped(String.valueOf(category.getId()));
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        renderView();

        if (category.getAttributes().getClientNumber().isShown()) {
            presenter.fetchNumberList(String.valueOf(category.getId()), true);
        } else {
            presenter.validateOperatorWithProducts(category.getId(), selectedOperatorId);
        }
    }

    private void renderView() {
        widgetWrapperBuyView.setCategory(category);
        widgetWrapperBuyView.renderInstantCheckoutOption(
                category.getAttributes().isInstantCheckoutAvailable());

        clearHolder(holderWidgetClientNumber);
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        if (category.getAttributes().getClientNumber().isShown()) {
            widgetClientNumberView.setClientNumberLabel(clientNumber.getText());
            widgetClientNumberView.setHint(clientNumber.getPlaceholder());
            widgetClientNumberView.setVisibilityPhoneBook(category.getAttributes().isUsePhonebook());
            holderWidgetClientNumber.addView(widgetClientNumberView);
        } else {
            selectedOperatorId = category.getAttributes().getDefaultOperatorId();
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
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
                new UIThread(),
                useCache);

        presenter = new DigitalWidgetStyle1Presenter(getActivity(), interactor, this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_widget;
    }

    @Override
    protected void initView(View view) {
        widgetClientNumberView = new WidgetClientNumberView(getActivity());
        widgetWrapperBuyView = new WidgetWrapperBuyView(getActivity());
        widgetProductChooserView = new WidgetProductChooserView(getActivity());

        holderWidgetClientNumber.addView(widgetClientNumberView);
    }

    @Override
    protected void setViewListener() {
        setRechargeEditTextTouchCallback(widgetClientNumberView);

        widgetClientNumberView.setButtonPickerListener(getButtonPickerListener());
        widgetClientNumberView.setRechargeEditTextListener(getEditTextListener());
        widgetWrapperBuyView.setListener(getBuyButtonListener());
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {

    }

    private WidgetClientNumberView.OnButtonPickerListener getButtonPickerListener() {
        return new WidgetClientNumberView.OnButtonPickerListener() {
            @Override
            public void onButtonContactClicked() {
                BaseWidgetRechargeFragmentPermissionsDispatcher
                        .doLaunchContactPickerWithCheck(WidgetStyle1RechargeFragment.this);
            }
        };
    }

    private WidgetClientNumberView.RechargeEditTextListener getEditTextListener() {
        return new WidgetClientNumberView.RechargeEditTextListener() {
            @Override
            public void onRechargeTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString();
                temp = validateTextPrefix(temp);

                if (before == 1 && count == 0) {
                    selectedOperator = null;
                    widgetClientNumberView.setImgOperatorInvisible();
                    clearHolder(holderWidgetSpinnerProduct);
                    clearHolder(holderWidgetWrapperBuy);
                } else {
                    if (category.getAttributes().isValidatePrefix()) {
                        if (selectedOperator == null) {
                            if (temp.length() >= 3) {
                                presenter.validatePhonePrefix(temp, category.getId(),
                                        category.getAttributes().isValidatePrefix());
                            }
                        }
                    } else {
                        if (s.length() >= minLengthDefaultOperator) {
                            if (selectedOperator != null && selectedOperator.getAttributes().getRule().isShowProduct()) {
                                presenter.validateOperatorWithProducts(category.getId(),
                                        selectedOperatorId);
                            } else {
                                if (selectedOperatorId == null) {
                                    selectedOperatorId = category.getAttributes().getDefaultOperatorId();
                                } else {
                                    presenter.validateOperatorWithoutProducts(category.getId(),
                                            selectedOperatorId);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onRechargeTextClear() {
                selectedOperator = null;
                clearHolder(holderWidgetSpinnerProduct);
                clearHolder(holderWidgetWrapperBuy);
            }

            @Override
            public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {
                UnifyTracking.eventSelectNumberOnUserProfileWidget(category.getAttributes().getName());

                LastOrder lastOrder = new LastOrder();
                Attributes attributes = new Attributes();
                attributes.setClientNumber(orderClientNumber.getClientNumber());
                attributes.setCategoryId(Integer.valueOf(orderClientNumber.getCategoryId()));
                attributes.setOperatorId(Integer.valueOf(orderClientNumber.getOperatorId()));
                if (orderClientNumber.getLastProduct() != null) {
                    attributes.setProductId(Integer.valueOf(orderClientNumber.getLastProduct()));
                }
                lastOrder.setAttributes(attributes);

                setLastOrder(lastOrder);
            }
        };
    }

    private WidgetWrapperBuyView.OnBuyButtonListener getBuyButtonListener() {
        return new WidgetWrapperBuyView.OnBuyButtonListener() {
            @Override
            public void goToNativeCheckout() {
                if (selectedProduct == null) {
                    presenter.fetchDefaultProduct(String.valueOf(category.getId()),
                            selectedOperatorId, String.valueOf(selectedOperator.getAttributes().getDefaultProductId()));
                } else {
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
            }

            @Override
            public void goToLoginPage() {
                if (selectedProduct == null) {
                    presenter.fetchDefaultProduct(String.valueOf(category.getId()),
                            selectedOperatorId, String.valueOf(selectedOperator.getAttributes().getDefaultProductId()));
                } else {
                    digitalCheckoutPassDataState =
                            widgetWrapperBuyView.getGeneratedCheckoutPassData(getDataPreCheckout());

                    Intent intent = OldSessionRouter.getLoginActivityIntent(getActivity());
                    intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);

                    presenter.storeLastClientNumberTyped(String.valueOf(category.getId()),
                            widgetClientNumberView.getText(), selectedProduct);

                    startActivityForResult(intent, LOGIN_REQUEST_CODE);
                }
            }

            @Override
            public boolean isRecentInstantCheckoutUsed(String categoryId) {
                return presenter.isRecentInstantCheckoutUsed(categoryId);
            }

            @Override
            public void trackingCheckInstantSaldo(boolean isChecked) {
                UnifyTracking.eventCheckInstantSaldoWidget(category.getAttributes().getName(),
                        selectedOperator == null ? "" : selectedOperator.getAttributes().getName(), isChecked);
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
                    UnifyTracking.eventSelectProductWidget(category.getAttributes().getName(),
                            selectedProduct.getAttributes().getDesc());
            }
        };
    }

    private PreCheckoutDigitalWidget getDataPreCheckout() {
        PreCheckoutDigitalWidget preCheckoutDigitalWidget = new PreCheckoutDigitalWidget();
        preCheckoutDigitalWidget.setClientNumber(widgetClientNumberView.getText());
        preCheckoutDigitalWidget.setOperatorId(String.valueOf(selectedOperator.getId()));
        preCheckoutDigitalWidget.setProductId(String.valueOf(selectedProduct.getId()));
        preCheckoutDigitalWidget.setPromoProduct(selectedProduct.getAttributes().getPromo() != null);
        preCheckoutDigitalWidget.setBundle(bundle);
        return preCheckoutDigitalWidget;
    }

    @Override
    public void renderDataProducts(List<Product> products) {
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
        if (!TextUtils.isEmpty(widgetClientNumberView.getText())) {
            widgetProductChooserView.setListener(getProductChoserListener());
            widgetProductChooserView.renderDataView(products, showPrice, lastOrder, lastProductSelected);
            holderWidgetSpinnerProduct.addView(widgetProductChooserView);
            holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
        }
    }

    @Override
    public void renderEmptyProduct(String message) {
        widgetClientNumberView.setImgOperatorInvisible();
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
    }

    @Override
    public void renderDataOperator(Operator operatorModel) {
        if (!TextUtils.isEmpty(widgetClientNumberView.getText())) {
            selectedOperator = operatorModel;
            UnifyTracking.eventSelectOperatorWidget(category.getAttributes().getName(),
                    selectedOperator.getAttributes().getName());
            selectedOperatorId = String.valueOf(operatorModel.getId());
            minLengthDefaultOperator = operatorModel.getAttributes().getMinimumLength();
            widgetClientNumberView.setImgOperator(operatorModel.getAttributes().getImage());
            widgetClientNumberView.setFilterMaxLength(operatorModel.getAttributes().getMaximumLength());
            widgetClientNumberView.setImgOperatorVisible();
            widgetClientNumberView.setInputType(operatorModel.getAttributes().getRule().isAllowAphanumericNumber());
            widgetProductChooserView.setTitleProduct(operatorModel.getAttributes().getRule().getProductText());
            widgetProductChooserView.setVisibilityProduct(operatorModel.getAttributes().getRule().isShowProduct());
            if (!operatorModel.getAttributes().getRule().isShowPrice()) showPrice = false;
            if (!operatorModel.getAttributes().getRule().isShowProduct()) {
                clearHolder(holderWidgetWrapperBuy);
                holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
            }
        }
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
    public void renderNumberList(List<OrderClientNumber> results) {
        if (SessionHandler.isV4Login(getActivity())) {
            widgetClientNumberView.setDropdownAutoComplete(results);
        }
    }

    @Override
    public void renderVerifiedNumber() {
        String verifiedNumber = SessionHandler.getPhoneNumber();
        widgetClientNumberView.setText(verifiedNumber);
    }

    @Override
    public void renderLastTypedClientNumber() {
        if (category.getAttributes().isValidatePrefix()) {
            widgetClientNumberView.setText(lastClientNumberTyped);
        } else {
            presenter.getOperatorById(lastOperatorSelected);
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

    private void setLastOrder(LastOrder lastOrder) {
        this.lastOrder = lastOrder;
    }

    @Override
    public void renderOperator(Operator operatorModel) {
        selectedOperator = operatorModel;
        selectedOperatorId = String.valueOf(selectedOperator.getId());
        widgetClientNumberView.setText(lastClientNumberTyped);
    }

    @Override
    public void renderErrorProduct(String message) {
        showSnackbarErrorMessage(message);
    }

    @Override
    public void renderDefaultError() {

    }

    @Override
    public void saveAndDisplayPhoneNumber(String phoneNumber) {
        selectedOperator = null;
        widgetClientNumberView.setText(phoneNumber);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(STATE_CLIENT_NUMBER, widgetClientNumberView.getText());
        super.onSaveState(state);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        super.onRestoreState(savedState);
        renderView();
        if (category.getAttributes().getClientNumber().isShown()) {
            presenter.fetchNumberList(String.valueOf(category.getId()), false);
            widgetClientNumberView.setText(savedState.getString(STATE_CLIENT_NUMBER));
        }
    }

    @Override
    public void onDestroyView() {
        clearHolder(holderWidgetClientNumber);
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
        removeRechargeEditTextCallback(widgetClientNumberView);
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
        super.onDestroyView();
    }

}
