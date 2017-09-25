package com.tokopedia.digital.widget.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.model.category.Category;
import com.tokopedia.core.database.model.category.ClientNumber;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.compoundview.WidgetClientNumberView;
import com.tokopedia.digital.widget.compoundview.WidgetProductChooserView;
import com.tokopedia.digital.widget.compoundview.WidgetWrapperBuyView;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.interactor.DigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle1View;
import com.tokopedia.digital.widget.model.PreCheckoutDigitalWidget;
import com.tokopedia.digital.widget.presenter.DigitalWidgetStyle1Presenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/18/17.
 */

public class WidgetStyle1RechargeFragment extends BaseWidgetRechargeFragment implements IDigitalWidgetStyle1View {

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
    private RechargeOperatorModel selectedOperator;
    private LastOrder lastOrder;
    private Product selectedProduct;
    private String selectedOperatorId;
    private int minLengthDefaultOperator;
    private boolean showPrice = true;
    private SessionHandler sessionHandler;

    public static WidgetStyle1RechargeFragment newInstance(Category category, int position) {
        WidgetStyle1RechargeFragment fragment = new WidgetStyle1RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initialVariable() {
        DigitalWidgetInteractor interactor = new DigitalWidgetInteractor(new CompositeSubscription(),
                new DigitalWidgetRepository(new RechargeService(), new DigitalEndpointService()));
        presenter = new DigitalWidgetStyle1Presenter(getActivity(), interactor, this);
        presenter.fetchRecentNumber(category.getId());

        sessionHandler = new SessionHandler(getActivity());
        lastClientNumberTyped = presenter.getLastClientNumberTyped(String.valueOf(category.getId()));
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        widgetClientNumberView = new WidgetClientNumberView(getActivity());
        widgetWrapperBuyView = new WidgetWrapperBuyView(getActivity());
        widgetProductChooserView = new WidgetProductChooserView(getActivity());
    }

    @Override
    public void initialViewRendered() {
        clearHolder(holderWidgetClientNumber);
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        widgetClientNumberView.setButtonPickerListener(getButtonPickerListener());
        widgetClientNumberView.setRechargeEditTextListener(getEditTextListener());
        widgetWrapperBuyView.setListener(getBuyButtonListener());
        widgetWrapperBuyView.setCategory(category);
        widgetWrapperBuyView.renderInstantCheckoutOption(
                category.getAttributes().isInstantCheckoutAvailable());

        setRechargeEditTextCallback(widgetClientNumberView);
        setRechargeEditTextTouchCallback(widgetClientNumberView);

        if (category.getAttributes().getClientNumber().isShown()) {
            widgetClientNumberView.setClientNumberLabel(clientNumber.getText());
            widgetClientNumberView.setHint(clientNumber.getPlaceholder());
            widgetClientNumberView.setVisibilityPhoneBook(category.getAttributes().isUsePhonebook());
            holderWidgetClientNumber.addView(widgetClientNumberView);
            initClientNumber();
        } else {
            selectedOperatorId = category.getAttributes().getDefaultOperatorId();
            presenter.validateOperatorWithProducts(category.getId(), selectedOperatorId);
        }
    }

    @Override
    public void saveAndDisplayPhoneNumber(String phoneNumber) {
        widgetClientNumberView.setText(phoneNumber);
        //save to last input key
    }

    @Override
    protected void trackingOnClientNumberFocusListener() {
        UnifyTracking.eventSelectOperatorWidget(category.getAttributes().getName(),
                selectedOperator == null ? "" : selectedOperator.name);
    }

    private WidgetClientNumberView.RechargeEditTextListener getEditTextListener() {
        return new WidgetClientNumberView.RechargeEditTextListener() {
            @Override
            public void onRechargeTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString();
                temp = validateTextPrefix(temp);

                if (before == 1 && count == 0) {
                    widgetClientNumberView.setImgOperatorInvisible();
                    clearHolder(holderWidgetSpinnerProduct);
                    clearHolder(holderWidgetWrapperBuy);
                } else {
                    if (category.getAttributes().isValidatePrefix()) {
                        if (temp.length() >= 3) {
                            if (s.length() >= 3) {
                                presenter.validatePhonePrefix(temp, category.getId(),
                                        category.getAttributes().isValidatePrefix());
                            }
                        } else {
                            widgetClientNumberView.setImgOperatorInvisible();
                            clearHolder(holderWidgetSpinnerProduct);
                            clearHolder(holderWidgetWrapperBuy);
                        }
                    } else {
                        if (s.length() >= minLengthDefaultOperator) {
                            if (selectedOperator != null && selectedOperator.showProduct) {
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
                clearHolder(holderWidgetSpinnerProduct);
                clearHolder(holderWidgetWrapperBuy);
            }
        };
    }

    private WidgetClientNumberView.OnButtonPickerListener getButtonPickerListener() {
        return new WidgetClientNumberView.OnButtonPickerListener() {
            @Override
            public void onButtonContactClicked() {
                try {
                    BaseWidgetRechargeFragmentPermissionsDispatcher
                            .doLaunchContactPickerWithCheck(WidgetStyle1RechargeFragment.this);
                } catch (Exception e) {
                    if (e instanceof ActivityNotFoundException) {
                        NetworkErrorHelper.showSnackbar(getActivity(),
                                getString(R.string.error_message_contact_not_found));
                    }
                }
            }
        };
    }

    private PreCheckoutDigitalWidget getDataPreCheckout() {
        PreCheckoutDigitalWidget preCheckoutDigitalWidget = new PreCheckoutDigitalWidget();
        preCheckoutDigitalWidget.setClientNumber(widgetClientNumberView.getText());
        preCheckoutDigitalWidget.setOperatorId(String.valueOf(selectedOperator.operatorId));
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
                            selectedOperatorId, String.valueOf(selectedOperator.defaultProductId));
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
                            selectedOperatorId, String.valueOf(selectedOperator.defaultProductId));
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
                        selectedOperator == null ? "" : selectedOperator.name, isChecked);
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
                UnifyTracking.eventSelectProductWidget(category.getAttributes().getName(),
                        selectedProduct.getAttributes().getPrice());
            }
        };
    }

    private void renderLastOrder() {
        if (presenter != null) {
            lastOrder = presenter.getLastOrderFromCache();
            if (lastOrder != null && lastOrder.getData() != null && category != null) {
                if (lastOrder.getData().getAttributes().getCategory_id() == category.getId()) {
                    widgetClientNumberView.setText(lastOrder.getData().getAttributes().getClient_number());
                }
            }
        }
    }

    private void initClientNumber() {
        String defaultPhoneNumber = sessionHandler.getPhoneNumber();

        if (sessionHandler.isV4Login(getActivity())
                && presenter.isAlreadyHaveLastOrderOnCacheByCategoryId(category.getId())) {
            renderLastOrder();
        } else if (sessionHandler.isV4Login(getActivity())
                && !presenter.isAlreadyHaveLastOrderOnCacheByCategoryId(category.getId())
                && !TextUtils.isEmpty(lastClientNumberTyped)) {
            renderLastTypeClientNumber();
        } else if (sessionHandler.isV4Login(getActivity())
                && !presenter.isAlreadyHaveLastOrderOnCacheByCategoryId(category.getId())
                && TextUtils.isEmpty(lastClientNumberTyped)
                && (category.getId() == 1 || category.getId() == 2)
                && !TextUtils.isEmpty(defaultPhoneNumber)) {
            widgetClientNumberView.setText(defaultPhoneNumber);
        } else if (!sessionHandler.isV4Login(getActivity())
                && !TextUtils.isEmpty(lastClientNumberTyped)) {
            renderLastTypeClientNumber();
        }
    }

    private void renderLastTypeClientNumber() {
        if (category.getAttributes().isValidatePrefix())
            widgetClientNumberView.setText(lastClientNumberTyped);
        else
            presenter.getOperatorById(lastOperatorSelected);
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
        removeRechargeEditTextCallback(widgetClientNumberView);
        presenter.onDestroy();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void renderDataProducts(List<Product> products) {
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
        if (!TextUtils.isEmpty(widgetClientNumberView.getText())) {
            widgetProductChooserView.setListener(getProductChoserListener());
            widgetProductChooserView.renderDataView(products, showPrice, lastOrder, lastProductSelected);
            holderWidgetSpinnerProduct.addView(widgetProductChooserView);
        }
        holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
    }

    @Override
    public void renderEmptyProduct(String message) {
        widgetClientNumberView.setImgOperatorInvisible();
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
    }

    @Override
    public void renderDataOperator(RechargeOperatorModel operatorModel) {
        if (!TextUtils.isEmpty(widgetClientNumberView.getText())) {
            selectedOperator = operatorModel;
            selectedOperatorId = String.valueOf(operatorModel.operatorId);
            minLengthDefaultOperator = operatorModel.minimumLength;
            widgetClientNumberView.setImgOperator(operatorModel.image);
            widgetClientNumberView.setFilterMaxLength(operatorModel.maximumLength);
            widgetClientNumberView.setImgOperatorVisible();
            widgetClientNumberView.setInputType(operatorModel.allowAlphanumeric);
            widgetProductChooserView.setTitleProduct(operatorModel.nominalText);
            widgetProductChooserView.setVisibilityProduct(operatorModel.showProduct);
            if (!operatorModel.showPrice) showPrice = false;
            if (operatorModel.showProduct) {
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
        if (sessionHandler.isV4Login(getActivity())) {
            if (widgetProductChooserView.checkStockProduct(selectedProduct))
                widgetWrapperBuyView.goToNativeCheckout();
        } else {
            widgetWrapperBuyView.goToLoginPage();
        }
    }

    @Override
    public void renderDataRecent(List<String> results) {
        if (sessionHandler.isV4Login(getActivity())) {
            widgetClientNumberView.setDropdownAutoComplete(results);
        }
    }

    @Override
    public void renderOperator(RechargeOperatorModel operatorModel) {
        selectedOperator = operatorModel;
        selectedOperatorId = String.valueOf(selectedOperator.operatorId);
        widgetClientNumberView.setText(lastClientNumberTyped);
    }

    @Override
    public void renderErrorProduct(String message) {
        showSnackbarErrorMessage(message);
    }

    @Override
    public void renderDefaultError() {

    }
}