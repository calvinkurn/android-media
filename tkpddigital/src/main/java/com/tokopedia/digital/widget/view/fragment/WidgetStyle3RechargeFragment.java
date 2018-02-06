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
import com.tokopedia.digital.widget.view.compoundview.WidgetOperatorChooserView;
import com.tokopedia.digital.widget.view.compoundview.WidgetProductChooserView;
import com.tokopedia.digital.widget.view.compoundview.WidgetWrapperBuyView;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetStyle2View;
import com.tokopedia.digital.widget.view.model.PreCheckoutDigitalWidget;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.category.ClientNumber;
import com.tokopedia.digital.widget.view.model.lastorder.Attributes;
import com.tokopedia.digital.widget.view.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.view.model.mapper.OperatorMapper;
import com.tokopedia.digital.widget.view.model.mapper.ProductMapper;
import com.tokopedia.digital.widget.view.model.operator.Operator;
import com.tokopedia.digital.widget.view.model.product.Product;
import com.tokopedia.digital.widget.view.presenter.DigitalWidgetStyle2Presenter;
import com.tokopedia.digital.widget.view.presenter.IDigitalWidgetStyle2Presenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author rizkyfadillah on 10/16/2017.
 */
@Deprecated
public class WidgetStyle3RechargeFragment extends BaseWidgetRechargeFragment<IDigitalWidgetStyle2Presenter> implements IDigitalWidgetStyle2View {

    private static final String STATE_CLIENT_NUMBER = "STATE_CLIENT_NUMBER";

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
    private Product selectedProduct;

    private LastOrder lastOrder;

    private String selectedOperatorId;

    private boolean showPrice = true;

    private CompositeSubscription compositeSubscription;

    private List<Operator> operators;

    private QueryListener queryListener;

    private interface QueryListener {
        void onQueryChanged(String query);
    }

    public static WidgetStyle3RechargeFragment newInstance(Category category, int position) {
        WidgetStyle3RechargeFragment fragment = new WidgetStyle3RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_CATEGORY, category);
        bundle.putInt(ARG_TAB_INDEX_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onFirstTimeLaunched() {
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        renderView();

        presenter.getOperatorsByCategoryId(category.getId(), true);
    }

    private void renderView() {
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        clearHolder(holderWidgetClientNumber);
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

            setRechargeEditTextTouchCallback(widgetClientNumberView);
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
                new UIThread());

        presenter = new DigitalWidgetStyle2Presenter(getActivity(),
                new LocalCacheHandler(getActivity(), TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER),
                interactor, this);
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
        widgetOperatorChooserView = new WidgetOperatorChooserView(getActivity());
    }

    @Override
    protected void setViewListener() {
        compositeSubscription.add(
                Observable
                        .create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(final Subscriber<? super String> subscriber) {
                                queryListener = new QueryListener() {
                                    @Override
                                    public void onQueryChanged(String query) {
                                        subscriber.onNext(query);
                                    }
                                };
                            }
                        })
                        .distinctUntilChanged()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .switchMap(new Func1<String, Observable<String>>() {
                            @Override
                            public Observable<String> call(String s) {
                                return Observable.just(s);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String clientNumber) {
                                if (selectedOperator != null) {
                                    if (clientNumber.length() >= selectedOperator.getAttributes().getMinimumLength()) {
                                        if (selectedOperator.getAttributes().getRule().isShowProduct()) {
                                            if (selectedProduct == null) {
                                                presenter.validateOperatorWithProducts(category.getId(),
                                                        String.valueOf(selectedOperator.getId()));
                                            }
                                        } else {
                                            clearHolder(holderWidgetWrapperBuy);
                                            holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
                                        }
                                    } else {
                                        selectedProduct = null;
                                        clearHolder(holderWidgetSpinnerProduct);
                                        clearHolder(holderWidgetWrapperBuy);
                                    }
                                } else {
                                    widgetClientNumberView.setEmptyString();
                                }
                            }
                        })
        );

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
                        .doLaunchContactPickerWithCheck(WidgetStyle3RechargeFragment.this);
            }
        };
    }

    private WidgetClientNumberView.RechargeEditTextListener getEditTextListener() {
        return new WidgetClientNumberView.RechargeEditTextListener() {
            @Override
            public void onRechargeTextChanged(CharSequence s, int start, int before, int count) {
                if (queryListener != null) {
                    queryListener.onQueryChanged(s.toString());
                }
            }

            @Override
            public void onRechargeTextClear() {
                selectedProduct = null;
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
                if (orderClientNumber.getProductId() != null) {
                    attributes.setProductId(Integer.valueOf(orderClientNumber.getProductId()));
                }
                lastOrder.setAttributes(attributes);

                renderLastOrder(lastOrder);
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

    private WidgetOperatorChooserView.OperatorChoserListener getOperatorChoserListener() {
        return new WidgetOperatorChooserView.OperatorChoserListener() {
            @Override
            public void onCheckChangeOperator(Operator rechargeOperatorModel) {
                selectedProduct = null;
                selectedOperator = rechargeOperatorModel;
                widgetWrapperBuyView.resetInstantCheckout();
                widgetWrapperBuyView.setBuyButtonText(selectedOperator.getAttributes().getRule().getButtonLabel());
                selectedOperatorId = String.valueOf(rechargeOperatorModel.getId());
                widgetClientNumberView.setFilterMaxLength(rechargeOperatorModel.getAttributes().getMaximumLength());
                widgetClientNumberView.setInputType(rechargeOperatorModel.getAttributes().getRule().isAllowAphanumericNumber());
                widgetClientNumberView.setImgOperator(selectedOperator.getAttributes().getImage());
                widgetClientNumberView.setImgOperatorVisible();
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
            public void onResetOperator(boolean resetClientNumber) {
                if (resetClientNumber) {
                    clearHolder(holderWidgetWrapperBuy);
                    widgetClientNumberView.setEmptyString();
                }
            }

            @Override
            public void onTrackingOperator() {
                UnifyTracking.eventSelectOperatorOnWidget(category.getAttributes().getName(),
                        selectedOperator == null ? "" : selectedOperator.getAttributes().getName());
            }
        };
    }

    @Override
    public void saveAndDisplayPhoneNumber(String phoneNumber) {
        widgetClientNumberView.setText(phoneNumber);
        //save to last input key
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
    public void renderOperators(List<Operator> operators, boolean b) {
        this.operators = operators;
        clearHolder(holderWidgetSpinnerOperator);
        widgetOperatorChooserView.setListener(getOperatorChoserListener());
        widgetOperatorChooserView.renderDataView(operators, lastOrder, category.getId(),
                lastOperatorSelected);
        holderWidgetSpinnerOperator.addView(widgetOperatorChooserView);

        if (category.getAttributes().getClientNumber().isShown()) {
            presenter.fetchNumberList(String.valueOf(category.getId()), true);
        }
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
        widgetWrapperBuyView.resetInstantCheckout();
        widgetWrapperBuyView.setBuyButtonText(selectedOperator.getAttributes().getRule().getButtonLabel());
        UnifyTracking.eventSelectOperatorOnWidget(category.getAttributes().getName(),
                selectedOperator.getAttributes().getName());
        selectedOperatorId = String.valueOf(selectedOperator.getId());
    }

    @Override
    public void renderNumberList(List<OrderClientNumber> results) {
        if (SessionHandler.isV4Login(getActivity())) {
            widgetClientNumberView.setDropdownAutoComplete(results);
        }
    }

    @Override
    public void renderLastTypedClientNumber(String lastClientNumberTyped) {
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
                widgetOperatorChooserView.setLastOrderSelectedOperator(operators, lastOrder, category.getId());
                widgetClientNumberView.setText(lastOrder.getAttributes().getClientNumber());
            }
        }
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(STATE_CLIENT_NUMBER, widgetClientNumberView.getText());
        super.onSaveState(state);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        super.onRestoreState(savedState);
        if (category.getAttributes().getClientNumber().isShown()) {
            widgetClientNumberView.setText(savedState.getString(STATE_CLIENT_NUMBER));
        }
        renderView();
        presenter.getOperatorsByCategoryId(category.getId(), false);
    }

    @Override
    public void onDestroyView() {
        clearHolder(holderWidgetClientNumber);
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
        clearHolder(holderWidgetSpinnerOperator);
        removeRechargeEditTextCallback(widgetClientNumberView);

        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }

        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
