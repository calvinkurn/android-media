package com.tokopedia.digital.widget.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
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
import com.tokopedia.digital.widget.compoundview.WidgetOperatorChooserView;
import com.tokopedia.digital.widget.compoundview.WidgetProductChooserView;
import com.tokopedia.digital.widget.compoundview.WidgetWrapperBuyView;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.interactor.DigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle2View;
import com.tokopedia.digital.widget.model.PreCheckoutDigitalWidget;
import com.tokopedia.digital.widget.model.category.Category;
import com.tokopedia.digital.widget.model.category.ClientNumber;
import com.tokopedia.digital.widget.model.lastorder.Attributes;
import com.tokopedia.digital.widget.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.model.mapper.OperatorMapper;
import com.tokopedia.digital.widget.model.mapper.ProductMapper;
import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;
import com.tokopedia.digital.widget.presenter.DigitalWidgetStyle2Presenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/18/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public class WidgetStyle3RechargeFragment extends BaseWidgetRechargeFragment implements IDigitalWidgetStyle2View {

    @BindView(R2.id.holder_widget_client_number)
    LinearLayout holderWidgetClientNumber;
    @BindView(R2.id.holder_widget_wrapper_buy)
    LinearLayout holderWidgetWrapperBuy;
    @BindView(R2.id.holder_widget_spinner_operator)
    LinearLayout holderWidgetSpinnerOperator;
    @BindView(R2.id.holder_widget_spinner_product)
    LinearLayout holderWidgetSpinnerProduct;

    private DigitalWidgetStyle2Presenter presenter;
    private WidgetClientNumberView widgetClientNumberView;
    private WidgetWrapperBuyView widgetWrapperBuyView;
    private WidgetProductChooserView widgetProductChooserView;
    private WidgetOperatorChooserView widgetOperatorChooserView;
    private Operator selectedOperator;
    private LastOrder lastOrder;
    private Product selectedProduct;
    private String selectedOperatorId;
    private int minLengthDefaultOperator;
    private boolean showPrice = true;
    private CompositeSubscription compositeSubscription;

    public static WidgetStyle3RechargeFragment newInstance(Category category, int position) {
        WidgetStyle3RechargeFragment fragment = new WidgetStyle3RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initialVariable() {
        compositeSubscription = new CompositeSubscription();
        DigitalWidgetInteractor interactor = new DigitalWidgetInteractor(
                compositeSubscription,
                new DigitalWidgetRepository(new DigitalEndpointService(), new FavoriteNumberListDataMapper()),
                new ProductMapper(),
                new OperatorMapper(),
                new JobExecutor(),
                new UIThread());
        presenter = new DigitalWidgetStyle2Presenter(getActivity(), interactor, this);

        lastClientNumberTyped = presenter.getLastClientNumberTyped(String.valueOf(category.getId()));
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        widgetClientNumberView = new WidgetClientNumberView(getActivity());
        widgetWrapperBuyView = new WidgetWrapperBuyView(getActivity());
        widgetProductChooserView = new WidgetProductChooserView(getActivity());
        widgetOperatorChooserView = new WidgetOperatorChooserView(getActivity());
    }

    @Override
    public void initialViewRendered() {
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        clearHolder(holderWidgetClientNumber);
        widgetWrapperBuyView.setListener(getBuyButtonListener());
        widgetWrapperBuyView.setCategory(category);
        widgetWrapperBuyView.renderInstantCheckoutOption(
                category.getAttributes().isInstantCheckoutAvailable());

        if (category.getAttributes().getClientNumber().isShown()) {
            widgetClientNumberView.setButtonPickerListener(getButtonPickerListener());
            widgetClientNumberView.setRechargeEditTextListener(getEditTextListener());
            widgetClientNumberView.setClientNumberLabel(clientNumber.getText());
            widgetClientNumberView.setHint(clientNumber.getPlaceholder());
            widgetClientNumberView.setVisibilityPhoneBook(category.getAttributes().isUsePhonebook());
            holderWidgetClientNumber.addView(widgetClientNumberView);

            setRechargeEditTextCallback(widgetClientNumberView);
            setRechargeEditTextTouchCallback(widgetClientNumberView);
        }

        presenter.fetchOperatorByCategory(category.getId());
    }

    private WidgetClientNumberView.OnButtonPickerListener getButtonPickerListener() {
        return new WidgetClientNumberView.OnButtonPickerListener() {
            @Override
            public void onButtonContactClicked() {
                BaseWidgetRechargeFragmentPermissionsDispatcher
                        .doLaunchContactPickerWithCheck(WidgetStyle3RechargeFragment.this);
            }
        };
    }

    @Override
    public void saveAndDisplayPhoneNumber(String phoneNumber) {
        widgetClientNumberView.setText(phoneNumber);
        //save to last input key
    }

    @Override
    protected void trackingOnClientNumberFocusListener() {
        UnifyTracking.eventSelectOperatorWidget(category.getAttributes().getName(),
                selectedOperator == null ? "" : selectedOperator.getAttributes().getName());
    }

    private WidgetClientNumberView.RechargeEditTextListener getEditTextListener() {
        return new WidgetClientNumberView.RechargeEditTextListener() {
            @Override
            public void onRechargeTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 1 && count == 0) {
                    widgetClientNumberView.setImgOperatorInvisible();
                    clearHolder(holderWidgetSpinnerProduct);
                    clearHolder(holderWidgetWrapperBuy);
                } else if (s.length() >= minLengthDefaultOperator) {
                    if (selectedOperator != null) {
                        widgetClientNumberView.setImgOperator(selectedOperator.getAttributes().getImage());
                        widgetClientNumberView.setImgOperatorVisible();

                        if (selectedOperator.getAttributes().getRule().isShowProduct()) {
                            presenter.validateOperatorWithProducts(category.getId(),
                                    selectedOperatorId);
                        } else {
                            clearHolder(holderWidgetWrapperBuy);
                            holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
                        }
                    } else {
                        selectedOperatorId = category.getAttributes().getDefaultOperatorId();
                    }
                }
            }

            @Override
            public void onRechargeTextClear() {
                clearHolder(holderWidgetSpinnerProduct);
                clearHolder(holderWidgetWrapperBuy);
            }

            @Override
            public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {
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

    private void setLastOrder(LastOrder lastOrder) {
        this.lastOrder = lastOrder;
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

                    Intent intent = SessionRouter.getLoginActivityIntent(getActivity());
                    intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);

                    storeLastStateTabSelected();
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
                            selectedProduct.getAttributes().getPrice());
            }
        };
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_widget;
    }

    @Override
    public void onDestroy() {
        clearHolder(holderWidgetClientNumber);
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
        clearHolder(holderWidgetSpinnerOperator);
        removeRechargeEditTextCallback(widgetClientNumberView);
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void renderDataProducts(List<Product> products) {
        clearHolder(holderWidgetSpinnerProduct);
        clearHolder(holderWidgetWrapperBuy);
        widgetProductChooserView.setListener(getProductChoserListener());
        widgetProductChooserView.renderDataView(products, showPrice, lastOrder, lastProductSelected);
        holderWidgetSpinnerProduct.addView(widgetProductChooserView);
        holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
    }

    @Override
    public void renderEmptyProduct(String message) {
        widgetClientNumberView.setImgOperatorInvisible();
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
    }

    @Override
    public void renderOperators(List<Operator> operatorModels) {
        clearHolder(holderWidgetSpinnerOperator);
        widgetOperatorChooserView.setListener(getOperatorChoserListener());
        widgetOperatorChooserView.renderDataView(operatorModels, lastOrder, category.getId(),
                lastOperatorSelected);
        holderWidgetSpinnerOperator.addView(widgetOperatorChooserView);

        if (category.getAttributes().getClientNumber().isShown()) {
            presenter.fetchNumberList(String.valueOf(category.getId()));
        }
    }

    private WidgetOperatorChooserView.OperatorChoserListener getOperatorChoserListener() {
        return new WidgetOperatorChooserView.OperatorChoserListener() {
            @Override
            public void onCheckChangeOperator(Operator rechargeOperatorModel) {
                selectedProduct = null;
                selectedOperator = rechargeOperatorModel;
                selectedOperatorId = String.valueOf(rechargeOperatorModel.getId());
                minLengthDefaultOperator = rechargeOperatorModel.getAttributes().getMinimumLength();
                widgetClientNumberView.setFilterMaxLength(rechargeOperatorModel.getAttributes().getMaximumLength());
                widgetClientNumberView.setInputType(rechargeOperatorModel.getAttributes().getRule().isAllowAphanumericNumber());
                widgetProductChooserView.setTitleProduct(rechargeOperatorModel.getAttributes().getRule().getProductText());
                widgetProductChooserView.setVisibilityProduct(rechargeOperatorModel.getAttributes().getRule().isShowProduct());
                if (!rechargeOperatorModel.getAttributes().getRule().isShowPrice()) showPrice = false;

                if (!category.getAttributes().getClientNumber().isShown()) {
                    clearHolder(holderWidgetWrapperBuy);
                    presenter.validateOperatorWithProducts(category.getId(), selectedOperatorId);
                    holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
                }
            }

            @Override
            public void onResetOperator() {
                clearHolder(holderWidgetWrapperBuy);
                widgetClientNumberView.setEmptyString();
                widgetClientNumberView.setImgOperatorInvisible();
            }

            @Override
            public void onTrackingOperator() {
                UnifyTracking.eventSelectProductWidget(category.getAttributes().getName(),
                        selectedOperator == null ? "" : selectedOperator.getAttributes().getName());
            }
        };
    }

    @Override
    public void renderEmptyOperators(String message) {

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
    public void renderErrorProduct(String message) {
        showSnackbarErrorMessage(message);
    }

    @Override
    public void renderDefaultError() {

    }

    @Override
    public void renderOperator(Operator rechargeOperatorModel) {
        selectedOperator = rechargeOperatorModel;
        selectedOperatorId = String.valueOf(selectedOperator.getId());
        widgetClientNumberView.setText(lastClientNumberTyped);
    }

    @Override
    public void renderNumberList(List<OrderClientNumber> results) {
        if (SessionHandler.isV4Login(getActivity())) {
            widgetClientNumberView.setDropdownAutoComplete(results);
        }
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

}