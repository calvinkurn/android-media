package com.tokopedia.digital.widget.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetInteractor;
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

/**
 * This class is used to show digital category for style 1 and 99
 * The difference between style 1 and 99 is style 1 can have prefix while style 99 can't
 * @author rizkyfadillah on 10/16/2017.
 */
@Deprecated
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

    private CompositeSubscription compositeSubscription;

    private QueryListener queryListener;

    private interface QueryListener {
        void onQueryChanged(String query);
    }

    public static WidgetStyle1RechargeFragment newInstance(Category category, int position) {
        WidgetStyle1RechargeFragment fragment = new WidgetStyle1RechargeFragment();
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
    protected int getFragmentLayout() {
        return R.layout.fragment_widget;
    }

    @Override
    protected void initView(View view) {
        widgetClientNumberView = new WidgetClientNumberView(getActivity());
        widgetWrapperBuyView = new WidgetWrapperBuyView(getActivity());
        widgetProductChooserView = new WidgetProductChooserView(getActivity());
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

        renderClientNumberView();
        presenter.fetchNumberList(String.valueOf(category.getId()), true);
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
                                return !text.isEmpty();
                            }
                        })
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
                                }
                            }
                        })
        );

        widgetClientNumberView.setButtonPickerListener(getButtonPickerListener());
        widgetClientNumberView.setRechargeEditTextListener(getEditTextListener());
        widgetWrapperBuyView.setListener(getBuyButtonListener());
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
                if (orderClientNumber.getProductId() != null) {
                    attributes.setProductId(Integer.valueOf(orderClientNumber.getProductId()));
                }
                lastOrder.setAttributes(attributes);

                setLastOrder(lastOrder);
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

                    Intent intent = ((IDigitalModuleRouter) MainApplication.getAppContext())
                            .getLoginIntent(getActivity());
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

    private PreCheckoutDigitalWidget getDataPreCheckout() {
        PreCheckoutDigitalWidget preCheckoutDigitalWidget = new PreCheckoutDigitalWidget();
        preCheckoutDigitalWidget.setClientNumber(widgetClientNumberView.getText());
        preCheckoutDigitalWidget.setOperatorId(String.valueOf(selectedOperator.getId()));
        preCheckoutDigitalWidget.setProductId(selectedProduct.getId());
        preCheckoutDigitalWidget.setPromoProduct(selectedProduct.getAttributes().getPromo() != null);
        preCheckoutDigitalWidget.setBundle(bundle);
        return preCheckoutDigitalWidget;
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
    public void renderDataOperator(Operator operatorModel) {
        selectedOperator = operatorModel;
        UnifyTracking.eventSelectOperatorOnWidget(category.getAttributes().getName(),
                selectedOperator.getAttributes().getName());
        widgetClientNumberView.setFilterMaxLength(operatorModel.getAttributes().getMaximumLength());
        widgetClientNumberView.setImgOperator(operatorModel.getAttributes().getImage());
        widgetClientNumberView.setImgOperatorVisible();
        widgetProductChooserView.setTitleProduct(operatorModel.getAttributes().getRule().getProductText());
        widgetProductChooserView.setVisibilityProduct(operatorModel.getAttributes().getRule().isShowProduct());
        widgetWrapperBuyView.setBuyButtonText(selectedOperator.getAttributes().getRule().getButtonLabel());
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
        widgetWrapperBuyView.resetInstantCheckout();
        widgetWrapperBuyView.setBuyButtonText(selectedOperator.getAttributes().getRule().getButtonLabel());
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

        renderClientNumberView();
        presenter.fetchNumberList(String.valueOf(category.getId()), false);
        widgetClientNumberView.setText(savedState.getString(STATE_CLIENT_NUMBER));
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

        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }

        super.onDestroyView();
    }

}
