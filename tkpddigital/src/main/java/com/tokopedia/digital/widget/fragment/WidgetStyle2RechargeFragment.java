package com.tokopedia.digital.widget.fragment;

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
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.compoundview.WidgetClientNumberView;
import com.tokopedia.digital.widget.compoundview.WidgetProductChooserView;
import com.tokopedia.digital.widget.compoundview.WidgetRadioChooserView;
import com.tokopedia.digital.widget.compoundview.WidgetWrapperBuyView;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.interactor.DigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle2View;
import com.tokopedia.digital.widget.model.PreCheckoutDigitalWidget;
import com.tokopedia.digital.widget.presenter.DigitalWidgetStyle2Presenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/18/17.
 */

public class WidgetStyle2RechargeFragment extends BaseWidgetRechargeFragment implements IDigitalWidgetStyle2View {

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
    private WidgetRadioChooserView widgetRadioChooserView;
    private RechargeOperatorModel selectedOperator;
    private LastOrder lastOrder;
    private Product selectedProduct;
    private String selectedOperatorId;
    private int minLengthDefaultOperator;
    private boolean showPrice = true;

    public static WidgetStyle2RechargeFragment newInstance(Category category, int position) {
        WidgetStyle2RechargeFragment fragment = new WidgetStyle2RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initialVariable() {
        DigitalWidgetInteractor interactor = new DigitalWidgetInteractor(new CompositeSubscription(),
                new DigitalWidgetRepository(new RechargeService(), new DigitalEndpointService(), new FavoriteNumberListDataMapper()));
        presenter = new DigitalWidgetStyle2Presenter(getActivity(), interactor, this);
        presenter.fetchNumberList(String.valueOf(category.getId()));

        lastClientNumberTyped = presenter.getLastClientNumberTyped(String.valueOf(category.getId()));
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        widgetClientNumberView = new WidgetClientNumberView(getActivity());
        widgetWrapperBuyView = new WidgetWrapperBuyView(getActivity());
        widgetProductChooserView = new WidgetProductChooserView(getActivity());
        widgetRadioChooserView = new WidgetRadioChooserView(getActivity());
    }

    @Override
    public void initialViewRendered() {
        clearHolder(holderWidgetClientNumber);
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        widgetClientNumberView.setButtonPickerListener(getButtonPickerListener());
        widgetClientNumberView.setRechargeEditTextListener(getEditTextListener());
        widgetWrapperBuyView.setListener(getBuyButtonListener());

        setRechargeEditTextCallback(widgetClientNumberView);
        setRechargeEditTextTouchCallback(widgetClientNumberView);

        widgetClientNumberView.setClientNumberLabel(clientNumber.getText());
        widgetClientNumberView.setHint(clientNumber.getPlaceholder());
        widgetClientNumberView.setVisibilityPhoneBook(category.getAttributes().isUsePhonebook());
        widgetWrapperBuyView.setCategory(category);
        widgetWrapperBuyView.renderInstantCheckoutOption(
                category.getAttributes().isInstantCheckoutAvailable());
        holderWidgetClientNumber.addView(widgetClientNumberView);

        presenter.fetchOperatorByCategory(category.getId());
//        initClientNumber();
    }

    private WidgetClientNumberView.OnButtonPickerListener getButtonPickerListener() {
        return new WidgetClientNumberView.OnButtonPickerListener() {
            @Override
            public void onButtonContactClicked() {
                BaseWidgetRechargeFragmentPermissionsDispatcher
                        .doLaunchContactPickerWithCheck(WidgetStyle2RechargeFragment.this);
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
                selectedOperator == null ? "" : selectedOperator.name);
    }

    private WidgetClientNumberView.RechargeEditTextListener getEditTextListener() {
        return new WidgetClientNumberView.RechargeEditTextListener() {
            @Override
            public void onRechargeTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 || before == 1 && count == 0) {
                    widgetClientNumberView.setImgOperatorInvisible();
                    clearHolder(holderWidgetSpinnerProduct);
                    clearHolder(holderWidgetWrapperBuy);
                } else if (s.length() >= minLengthDefaultOperator) {
                    if (s.length() >= minLengthDefaultOperator) {
                        if (selectedOperator != null) {
                            widgetClientNumberView.setImgOperator(selectedOperator.image);
                            widgetClientNumberView.setImgOperatorVisible();

                            if (selectedOperator.showProduct) {
                                presenter.validateOperatorWithProducts(category.getId(),
                                        selectedOperatorId);
                            } else {
                                clearHolder(holderWidgetWrapperBuy);
                                holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
                            }
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

//    private void renderLastOrder() {
//        if (presenter != null) {
//            lastOrder = presenter.getLastOrderFromCache();
//            if (lastOrder != null && lastOrder.getData() != null && category != null) {
//                if (lastOrder.getData().getAttributes().getCategory_id() == category.getId()) {
//                    widgetClientNumberView.setText(lastOrder.getData().getAttributes().getClient_number());
//                }
//            }
//        }
//    }

//    private void initClientNumber() {
//        if (sessionHandler.isV4Login(getActivity())
//                && presenter.isAlreadyHaveLastOrderOnCacheByCategoryId(category.getId())) {
//            renderLastOrder();
//        } else if (sessionHandler.isV4Login(getActivity())
//                && !presenter.isAlreadyHaveLastOrderOnCacheByCategoryId(category.getId())
//                && !TextUtils.isEmpty(lastClientNumberTyped)) {
//            presenter.getOperatorById(lastOperatorSelected);
//        } else if (!sessionHandler.isV4Login(getActivity())
//                && !TextUtils.isEmpty(lastClientNumberTyped)) {
//            presenter.getOperatorById(lastOperatorSelected);
//        }
//    }

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
        presenter.onDestroy();
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
    public void renderEmptyOperators(String message) {

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
    public void renderOperator(RechargeOperatorModel rechargeOperatorModel) {
        selectedOperator = rechargeOperatorModel;
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

    @Override
    public void renderNumberList(List<OrderClientNumber> results) {
        if (sessionHandler.isV4Login(getActivity())) {
            widgetClientNumberView.setDropdownAutoComplete(results);
        }
    }

    @Override
    public void renderLastOrder(LastOrder lastOrder) {
        if (presenter != null) {
            this.lastOrder = lastOrder;
            if (lastOrder != null && lastOrder.getData() != null && category != null) {
                if (lastOrder.getData().getAttributes().getCategory_id() == category.getId()) {
                    widgetClientNumberView.setText(lastOrder.getData().getAttributes().getClient_number());
                }
            }
        }
    }

//    @Override
//    public void renderLastOrderFromApi(OrderClientNumber lastOrder) {
//        if (presenter != null) {
//            if (lastOrder != null) {
//                widgetClientNumberView.setText(lastOrder.getClientNumber());
//                if (lastOrder.getLastProduct() != null) {
//                    presenter.fetchDefaultProduct(String.valueOf(category.getId()), lastOrder.getOperatorId(), lastOrder.getLastProduct());
//                }
//            }
//        }
//    }

//    @Override
//    public void renderLastOrderFromCache(LastOrder lastOrder) {
//        if (lastOrder != null && lastOrder.getData() != null && category != null) {
//            if (lastOrder.getData().getAttributes().getCategory_id() == category.getId()) {
//                widgetClientNumberView.setText(lastOrder.getData().getAttributes().getClient_number());
//            }
//        }
//    }

//    @Override
//    public void renderLastOrder(OrderClientNumber lastOrder) {
//        if (presenter != null) {
//            if (lastOrder != null) {
//                widgetClientNumberView.setText(lastOrder.getClientNumber());
//            }
//        }
//    }

    @Override
    public void renderOperators(List<RechargeOperatorModel> operatorModels) {
        clearHolder(holderWidgetSpinnerOperator);
        widgetRadioChooserView.setListener(getRadioChoserListener());
        widgetRadioChooserView.renderDataView(operatorModels, lastOrder, lastOperatorSelected);
        holderWidgetSpinnerOperator.addView(widgetRadioChooserView);
    }

    private WidgetRadioChooserView.RadioChoserListener getRadioChoserListener() {
        return new WidgetRadioChooserView.RadioChoserListener() {

            @Override
            public void onCheckChange(RechargeOperatorModel rechargeOperatorModel) {
                selectedProduct = null;
                selectedOperator = rechargeOperatorModel;
                selectedOperatorId = String.valueOf(rechargeOperatorModel.operatorId);
                minLengthDefaultOperator = rechargeOperatorModel.minimumLength;
                widgetClientNumberView.setInputType(rechargeOperatorModel.allowAlphanumeric);
                widgetClientNumberView.setFilterMaxLength(rechargeOperatorModel.maximumLength);
                widgetProductChooserView.setTitleProduct(rechargeOperatorModel.nominalText);
                widgetProductChooserView.setVisibilityProduct(rechargeOperatorModel.showProduct);
                if (!rechargeOperatorModel.showPrice) showPrice = false;
            }

            @Override
            public void onResetClientNumber() {
                clearHolder(holderWidgetWrapperBuy);
                clearHolder(holderWidgetSpinnerProduct);
                widgetClientNumberView.setEmptyString();
                widgetClientNumberView.setImgOperatorInvisible();
            }

            @Override
            public void onTrackingOperator() {
                UnifyTracking.eventSelectProductWidget(category.getAttributes().getName(),
                        selectedOperator == null ? "" : selectedOperator.name);
            }
        };
    }
}