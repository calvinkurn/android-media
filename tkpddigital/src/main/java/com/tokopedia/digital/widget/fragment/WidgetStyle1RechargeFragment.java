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
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

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

/**
 * This class is used to show digital category for style 1 and 99
 * The difference between style 1 and 99 is style 1 can have prefix while style 99 can't
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
    private Product selectedProduct;

    private LastOrder lastOrder;

    private int minLengthDefaultOperator;

    private CompositeSubscription compositeSubscription;

    private QueryListener queryListener;

    private interface QueryListener {
        void onQueryChanged(String query);
    }

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
                        .filter(new Func1<String, Boolean>() {
                            @Override
                            public Boolean call(String text) {
                                if (text.isEmpty()) {
                                    return false;
                                } else {
                                    return true;
                                }
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
                                if (clientNumber != null) {
                                    String temp = clientNumber;
                                    temp = validateTextPrefix(temp);

                                    if (category.getAttributes().isValidatePrefix()) {
                                        // handle style 1
                                        if (temp.length() >= 4) {
                                            if (selectedOperator == null) {
                                                presenter.getOperatorAndProductsByPrefix(temp, category.getId(),
                                                        category.getAttributes().isValidatePrefix());
                                            }
                                        } else {
                                            if (selectedOperator != null) {
                                                selectedOperator = null;
                                                // hide operator and products
                                                widgetClientNumberView.setImgOperatorInvisible();
                                                clearHolder(holderWidgetSpinnerProduct);
                                                clearHolder(holderWidgetWrapperBuy);
                                            }
                                        }
                                    } else {
                                        // handle style 99
                                        if (clientNumber.length() >= minLengthDefaultOperator) {
                                            if (selectedOperator != null) {
                                                boolean needToShowProduct = selectedOperator.getAttributes().getRule().isShowProduct();
                                                if (needToShowProduct) {
                                                    presenter.getOperatorAndProductsByOperatorId(category.getId(), String.valueOf(selectedOperator.getId()));
                                                } else {
                                                    presenter.getOperatorById(String.valueOf(category.getAttributes().getDefaultOperatorId()));
                                                }
                                            } else {
                                                presenter.getOperatorById(String.valueOf(category.getAttributes().getDefaultOperatorId()));
                                            }
                                        }
                                    }
                                }
                            }
                        })
        );

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

    @Override
    protected void onFirstTimeLaunched() {
        lastOperatorSelected = presenter.getLastOperatorSelected(String.valueOf(category.getId()));
        lastProductSelected = presenter.getLastProductSelected(String.valueOf(category.getId()));

        if (category.getAttributes().getClientNumber().isShown()) {
            renderClientNumberView();
            presenter.fetchNumberList(String.valueOf(category.getId()), true);
        } else {
            presenter.getOperatorAndProductsByOperatorId(category.getId(), category.getAttributes().getDefaultOperatorId());
        }
    }

    private void renderClientNumberView() {
        widgetWrapperBuyView.setCategory(category);
        widgetWrapperBuyView.renderInstantCheckoutOption(
                category.getAttributes().isInstantCheckoutAvailable());

        clearHolder(holderWidgetClientNumber);
        ClientNumber clientNumber = category.getAttributes().getClientNumber();

        widgetClientNumberView.setClientNumberLabel(clientNumber.getText());
        widgetClientNumberView.setHint(clientNumber.getPlaceholder());
        widgetClientNumberView.setVisibilityPhoneBook(category.getAttributes().isUsePhonebook());
        holderWidgetClientNumber.addView(widgetClientNumberView);
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
                if (queryListener != null) {
                    queryListener.onQueryChanged(s.toString());
                }
            }

            @Override
            public void onRechargeTextClear() {
                if (category.getAttributes().isValidatePrefix()) {
                    selectedOperator = null;
                    widgetClientNumberView.setImgOperatorInvisible();
                }
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
                            String.valueOf(selectedOperator.getId()), String.valueOf(selectedOperator.getAttributes().getDefaultProductId()));
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
                            String.valueOf(selectedOperator.getId()),
                            String.valueOf(selectedOperator.getAttributes().getDefaultProductId()));
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
    public void renderDataOperator(Operator operatorModel) {
        if (!TextUtils.isEmpty(widgetClientNumberView.getText())) {
            selectedOperator = operatorModel;
            UnifyTracking.eventSelectOperatorWidget(category.getAttributes().getName(),
                    selectedOperator.getAttributes().getName());
            minLengthDefaultOperator = operatorModel.getAttributes().getMinimumLength();
            widgetClientNumberView.setFilterMaxLength(operatorModel.getAttributes().getMaximumLength());
            widgetClientNumberView.setImgOperator(operatorModel.getAttributes().getImage());
            widgetClientNumberView.setImgOperatorVisible();
//            widgetClientNumberView.setInputType(operatorModel.getAttributes().getRule().isAllowAphanumericNumber());
            widgetProductChooserView.setTitleProduct(operatorModel.getAttributes().getRule().getProductText());
            widgetProductChooserView.setVisibilityProduct(operatorModel.getAttributes().getRule().isShowProduct());

            if (!operatorModel.getAttributes().getRule().isShowProduct()) {
                clearHolder(holderWidgetWrapperBuy);
                holderWidgetWrapperBuy.addView(widgetWrapperBuyView);
            }
        }
    }

    @Override
    public void renderDataProducts(List<Product> products, boolean showPrice) {
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
    public void renderVerifiedNumber(String verifiedNumber) {
        widgetClientNumberView.setText(verifiedNumber);
    }

    @Override
    public void renderLastTypedClientNumber(String lastClientNumberTyped) {
        widgetClientNumberView.setText(lastClientNumberTyped);
        if (!category.getAttributes().isValidatePrefix()) {
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
        widgetClientNumberView.setImgOperator(operatorModel.getAttributes().getImage());
        widgetClientNumberView.setImgOperatorVisible();
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

        if (category.getAttributes().getClientNumber().isShown()) {
            renderClientNumberView();
            presenter.fetchNumberList(String.valueOf(category.getId()), false);
            widgetClientNumberView.setText(savedState.getString(STATE_CLIENT_NUMBER));
        } else {
            presenter.getOperatorAndProductsByOperatorId(category.getId(), category.getAttributes().getDefaultOperatorId());
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void onDestroyView() {
        clearHolder(holderWidgetClientNumber);
        clearHolder(holderWidgetWrapperBuy);
        clearHolder(holderWidgetSpinnerProduct);
        removeRechargeEditTextCallback(widgetClientNumberView);
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();

        super.onStop();
    }
}
