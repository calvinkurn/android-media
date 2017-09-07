package com.tokopedia.digital.widget.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.compoundview.WidgetClientNumberView;
import com.tokopedia.digital.widget.compoundview.WidgetOperatorChoserView;
import com.tokopedia.digital.widget.compoundview.WidgetProductChoserView;
import com.tokopedia.digital.widget.compoundview.WidgetWrapperBuyView;
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
    private WidgetProductChoserView widgetProductChoserView;
    private WidgetOperatorChoserView widgetOperatorChoserView;
    private RechargeOperatorModel selectedOperator;
    private LastOrder lastOrder;
    private Product selectedProduct;
    private String selectedOperatorId;
    private int minLengthDefaultOperator;
    private boolean showPrice = true;

    public static WidgetStyle3RechargeFragment newInstance(Category category, int position) {
        WidgetStyle3RechargeFragment fragment = new WidgetStyle3RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DigitalWidgetInteractor interactor = new DigitalWidgetInteractor(new CompositeSubscription(),
                new DigitalWidgetRepository(new RechargeService(), new DigitalEndpointService()));
        presenter = new DigitalWidgetStyle2Presenter(getActivity(), interactor, this);
        presenter.fetchRecentNumber(category.getId());

        lastClientNumberTyped = presenter.getLastClientNumberTyped(String.valueOf(category.getId()));
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        widgetClientNumberView = new WidgetClientNumberView(getActivity());
        widgetWrapperBuyView = new WidgetWrapperBuyView(getActivity());
        widgetProductChoserView = new WidgetProductChoserView(getActivity());
        widgetOperatorChoserView = new WidgetOperatorChoserView(getActivity());

    }

    @Override
    public void initialViewRendered() {
        presenter.fetchOperatorByCategory(category.getId());
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        clearHolder(holderWidgetClientNumber);
        widgetWrapperBuyView.setListener(getBuyButtonListener());
        widgetWrapperBuyView.setCategory(category);

        if (category.getAttributes().getClientNumber().isShown()) {
            widgetClientNumberView.setRechargeEditTextListener(getEditTextListener());
            widgetClientNumberView.setClientNumberLabel(clientNumber.getText());
            widgetClientNumberView.setHint(clientNumber.getPlaceholder());
            widgetClientNumberView.setVisibilityPhoneBook(category.getAttributes().isUsePhonebook());
            holderWidgetClientNumber.addView(widgetClientNumberView);

            setRechargeEditTextCallback(widgetClientNumberView);
            setRechargeEditTextTouchCallback(widgetClientNumberView);
            initClientNumber();
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

                if (temp.isEmpty()) {
                    widgetClientNumberView.setImgOperatorInvisible();
                    clearHolder(holderWidgetSpinnerProduct);
                    clearHolder(holderWidgetWrapperBuy);
                } else if (s.length() >= minLengthDefaultOperator) {
                    if (selectedOperator != null) {
                        clearHolder(holderWidgetWrapperBuy);
                        widgetClientNumberView.setImgOperator(selectedOperator.image);
                        widgetClientNumberView.setImgOperatorVisible();
                        holderWidgetWrapperBuy.addView(widgetWrapperBuyView);

                        if (selectedOperator.showProduct) {
                            presenter.validateOperatorWithProducts(category.getId(),
                                    selectedOperatorId);
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
        preCheckoutDigitalWidget.setOperatorId(String.valueOf(selectedProduct.getRelationships()
                .getOperator().getData().getId()));
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
                    if (widgetProductChoserView.checkStockProduct(selectedProduct))
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

    private WidgetProductChoserView.ProductChoserListener getProductChoserListener() {
        return new WidgetProductChoserView.ProductChoserListener() {
            @Override
            public void initDataView(Product productSelected) {
                selectedProduct = productSelected;
                widgetProductChoserView.checkStockProduct(productSelected);
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
        if (SessionHandler.isV4Login(getActivity())
                && presenter.isAlreadyHaveLastOrderOnCacheByCategoryId(category.getId())) {
            renderLastOrder();
        } else if (SessionHandler.isV4Login(getActivity())
                && !presenter.isAlreadyHaveLastOrderOnCacheByCategoryId(category.getId())
                && !TextUtils.isEmpty(lastClientNumberTyped)
                && lastOperatorSelected.equals(selectedOperatorId)) {
            widgetClientNumberView.setText(lastClientNumberTyped);
        } else if (!SessionHandler.isV4Login(getActivity())
                && !TextUtils.isEmpty(lastClientNumberTyped)) {
            widgetClientNumberView.setText(lastClientNumberTyped);
        }
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
        presenter.onDestroy();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void renderDataProducts(List<Product> products) {
        clearHolder(holderWidgetSpinnerProduct);
        widgetProductChoserView.setListener(getProductChoserListener());
        widgetProductChoserView.renderDataView(products, showPrice, lastOrder, lastProductSelected);
        holderWidgetSpinnerProduct.addView(widgetProductChoserView);
    }

    @Override
    public void renderEmptyProduct(String message) {

    }

    @Override
    public void renderOperators(List<RechargeOperatorModel> operatorModels) {
        clearHolder(holderWidgetSpinnerOperator);
        widgetOperatorChoserView.setListener(getOperatorChoserListener());
        widgetOperatorChoserView.renderDataView(operatorModels, lastOrder, category.getId(),
                lastOperatorSelected);
        holderWidgetSpinnerOperator.addView(widgetOperatorChoserView);
    }

    private WidgetOperatorChoserView.OperatorChoserListener getOperatorChoserListener() {
        return new WidgetOperatorChoserView.OperatorChoserListener() {
            @Override
            public void initDataView(RechargeOperatorModel rechargeOperatorModel) {
                widgetClientNumberView.setEmptyString();
                selectedOperator = rechargeOperatorModel;
                selectedOperatorId = String.valueOf(rechargeOperatorModel.operatorId);
                widgetClientNumberView.setImgOperatorInvisible();
                minLengthDefaultOperator = rechargeOperatorModel.minimumLength;
                widgetClientNumberView.setFilterMaxLength(rechargeOperatorModel.maximumLength);
                widgetClientNumberView.setInputType(rechargeOperatorModel.allowAlphanumeric);
                widgetProductChoserView.setTitleProduct(rechargeOperatorModel.nominalText);
                widgetProductChoserView.setVisibilityProduct(rechargeOperatorModel.showProduct);
                if (!rechargeOperatorModel.showPrice) showPrice = false;

                if (!category.getAttributes().getClientNumber().isShown()) {
                    clearHolder(holderWidgetWrapperBuy);
                    presenter.validateOperatorWithProducts(category.getId(), selectedOperatorId);
                    holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
                }
            }

            @Override
            public void trackingOperator() {
                UnifyTracking.eventSelectProductWidget(category.getAttributes().getName(),
                        selectedOperator == null ? "" : selectedOperator.name);
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
            if (widgetProductChoserView.checkStockProduct(selectedProduct))
                widgetWrapperBuyView.goToNativeCheckout();
        } else {
            widgetWrapperBuyView.goToLoginPage();
        }
    }

    @Override
    public void renderDataRecent(List<String> results) {
        if (SessionHandler.isV4Login(getActivity())) {
            widgetClientNumberView.setDropdownAutoComplete(results);
        }
    }
}