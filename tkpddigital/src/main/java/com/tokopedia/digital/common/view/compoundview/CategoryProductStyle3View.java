package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalOperatorChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.compoundview.ProductAdditionalInfoView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.widget.view.compoundview.WidgetOperatorChooserView2;
import com.tokopedia.digital.widget.view.compoundview.WidgetProductChooserView2;

import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle3View extends
        BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber>
        implements ProductAdditionalInfoView.ActionListener {

    @BindView(R2.id.tv_title_category)
    TextView tvTitle;
    @BindView(R2.id.holder_chooser_operator)
    LinearLayout holderChooserOperator;
    @BindView(R2.id.holder_client_number)
    LinearLayout holderClientNumber;
    @BindView(R2.id.holder_chooser_product)
    LinearLayout holderChooserProduct;
    @BindView(R2.id.holder_additional_info_product)
    LinearLayout holderAdditionalInfoProduct;
    @BindView(R2.id.holder_price_info_product)
    LinearLayout holderPriceInfoProduct;
    @BindView(R2.id.cb_instant_checkout)
    CheckBox cbInstantCheckout;
    @BindView(R2.id.btn_buy_digital)
    TextView btnBuyDigital;
    @BindView(R2.id.layout_checkout)
    RelativeLayout layoutCheckout;
    @BindView(R2.id.tooltip_instant_checkout)
    ImageView tooltipInstantCheckout;

    private WidgetOperatorChooserView2 widgetOperatorChooserView2;
    private DigitalOperatorChooserView digitalOperatorChooserView;
    private ClientNumberInputView clientNumberInputView;
    private WidgetProductChooserView2 widgetProductChooserView2;
    private DigitalProductChooserView digitalProductChooserView;
    private ProductAdditionalInfoView productAdditionalInfoView;
    private ProductPriceInfoView productPriceInfoView;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle3View(Context context) {
        super(context);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle3View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle3View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateView() {
        widgetOperatorChooserView2 = new WidgetOperatorChooserView2(context);
        digitalOperatorChooserView = new DigitalOperatorChooserView(context);
        clientNumberInputView = new ClientNumberInputView(context);
        widgetProductChooserView2 = new WidgetProductChooserView2(context);
        digitalProductChooserView = new DigitalProductChooserView(context);
        productAdditionalInfoView = new ProductAdditionalInfoView(context);
        productPriceInfoView = new ProductPriceInfoView(context);
        productAdditionalInfoView.setActionListener(this);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_3;
    }

    @Override
    protected void onInitialDataRendered() {
        if (source != WIDGET) {
            tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        } else {
            tvTitle.setVisibility(GONE);
        }
        renderInstantCheckoutOptions();
        if (source == WIDGET) {
            renderOperatorChooserOptionsWidget();
        } else {
            renderOperatorChooserOptions();
        }
        btnBuyDigital.setOnClickListener(getButtonBuyClickedListener());
    }

    @Override
    protected void onUpdateSelectedProductData() {
        digitalProductChooserView.renderUpdateDataSelected(productSelected);
    }

    @Override
    protected void onUpdateSelectedOperatorData() {
        digitalOperatorChooserView.renderUpdateDataSelected(operatorSelected);
    }

    @Override
    protected void onInstantCheckoutUnChecked() {
        if (operatorSelected != null) setBtnBuyDigitalText(operatorSelected.getRule().getButtonText());
    }

    @Override
    protected void onInstantCheckoutChecked() {
        btnBuyDigital.setText(context.getString(R.string.label_btn_pay_digital));
    }

    @Override
    public void renderClientNumber(String clientNumber) {
        clientNumberInputView.setText(clientNumber);
    }

    @Override
    public boolean isInstantCheckoutChecked() {
        return cbInstantCheckout.isChecked();
    }

    @Override
    public String getClientNumber() {
        return clientNumberInputView.getText();
    }

    @Override
    protected void onRestoreSelectedData(
            Operator operatorSelectedState, Product productSelectedState,
            String clientNumberState, boolean isInstantCheckoutChecked
    ) {
        if (operatorSelected != null) {
            digitalOperatorChooserView.renderUpdateDataSelected(operatorSelected);
            if (!TextUtils.isEmpty(clientNumberState)) {
                clientNumberInputView.setText(clientNumberState);
            }
        }
    }

    @Override
    public void clearFocusOnClientNumber() {
        clientNumberInputView.clearFocus();
    }

    private void renderOperatorChooserOptionsWidget() {
        clearHolder(holderChooserOperator);
        clearHolder(holderClientNumber);
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        widgetOperatorChooserView2.setListener(getListenerOperatorChooser());
        widgetOperatorChooserView2.renderDataView(data.getOperatorList(), data.getDefaultOperatorId());
        holderChooserOperator.addView(widgetOperatorChooserView2);

        if (hasLastOrderHistoryData()) {
            for (Operator operator : data.getOperatorList()) {
                if (operator.getOperatorId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                )) {
                    widgetOperatorChooserView2.updateOperator(
                            data.getOperatorList(),
                            operator.getOperatorId());
                    break;
                }
            }
        }
    }

    private void renderOperatorChooserOptions() {
        clearHolder(holderChooserOperator);
        clearHolder(holderClientNumber);
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        digitalOperatorChooserView.setLabelText(data.getOperatorLabel());
        digitalOperatorChooserView.setActionListener(getActionListenerOperatorChooser());
        digitalOperatorChooserView.renderInitDataList(data.getOperatorList());
        holderChooserOperator.addView(digitalOperatorChooserView);

        if (hasLastOrderHistoryData()) {
            for (Operator operator : data.getOperatorList()) {
                if (operator.getOperatorId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                )) {
                    digitalOperatorChooserView.renderUpdateDataSelected(operator);
                    break;
                }
            }
        }
    }

    private void renderClientNumberInputForm(Operator operator) {
        clearHolder(holderClientNumber);
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        clientNumberInputView.setActionListener(getActionListenerClientNumberInputView());
        clientNumberInputView.renderData(operator.getClientNumberList().get(0));
        clientNumberInputView.setFilterMaxLength(operator.getRule().getMaximumLength());
        clientNumberInputView.resetInputTyped();
        holderClientNumber.addView(clientNumberInputView);

        if (hasLastOrderHistoryData()) {
            if (operatorSelected != null && operator.getOperatorId().equalsIgnoreCase(
                    historyClientNumber.getLastOrderClientNumber().getOperatorId())
                    && !historyClientNumber.getLastOrderClientNumber().getClientNumber().isEmpty()
                    ) {
                clientNumberInputView.setText(
                        historyClientNumber.getLastOrderClientNumber().getClientNumber()
                );
            }
        }

        if (hasLastOrderHistoryData()) {
            if (!operator.getClientNumberList().isEmpty()) {
                clientNumberInputView.setAdapterAutoCompleteClientNumber(historyClientNumber.getRecentClientNumberList());
            }
        }

        clientNumberInputView.enableImageOperator(operatorSelected.getImage());
    }

    private void showProducts() {
        if (source == WIDGET) {
            renderProductChooserOptionsWidget();
        } else {
            renderProductChooserOptions();
        }
    }

    private void renderProductChooserOptionsWidget() {
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        widgetProductChooserView2.setTitleProduct(operatorSelected.getRule().getProductText());
        widgetProductChooserView2.setListener(getProductChooserListener());
        widgetProductChooserView2.renderDataView(
                operatorSelected.getProductList(),
                operatorSelected.getRule().isShowPrice(),
                String.valueOf(operatorSelected.getDefaultProductId())
        );
        holderChooserProduct.addView(widgetProductChooserView2);

        if (hasLastOrderHistoryData() && operatorSelected != null) {
            for (Product product : operatorSelected.getProductList()) {
                if (product.getProductId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getProductId()
                ) && operatorSelected.getOperatorId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                )) {
                    widgetProductChooserView2.updateProduct(
                            operatorSelected.getProductList(),
                            product.getProductId()
                    );
                    break;
                }
            }
        }
    }

    private void renderProductChooserOptions() {
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        digitalProductChooserView.setLabelText(operatorSelected.getRule().getProductText());
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList());
        holderChooserProduct.addView(digitalProductChooserView);

        if (hasLastOrderHistoryData() && operatorSelected != null) {
            for (Product product : operatorSelected.getProductList()) {
                if (product.getProductId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getProductId()
                ) && operatorSelected.getOperatorId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                )) {
                    digitalProductChooserView.renderUpdateDataSelected(product);
                    break;
                }
            }
        }
    }

    private void renderDefaultProductSelected() {
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        if (operatorSelected.getProductList().get(0) != null) {
            productSelected = operatorSelected.getProductList().get(0);
        } else {
            productSelected = new Product.Builder()
                    .productId(String.valueOf(operatorSelected.getDefaultProductId()))
                    .info("")
                    .price("")
                    .desc("")
                    .detail("")
                    .build();
        }
        renderAdditionalProductInfo();
        renderPriceProductInfo();
    }

    private void renderPriceProductInfo() {
        clearHolder(holderPriceInfoProduct);
        if (operatorSelected.getRule().isShowPrice()) {
            productPriceInfoView.renderData(productSelected);
            holderPriceInfoProduct.addView(productPriceInfoView);
        }
    }

    private void renderAdditionalProductInfo() {
        clearHolder(holderAdditionalInfoProduct);
        productAdditionalInfoView.renderData(productSelected);
        holderAdditionalInfoProduct.addView(productAdditionalInfoView);
    }

    private void renderInstantCheckoutOptions() {
        if (data.isInstantCheckout()) {
            layoutCheckout.setVisibility(VISIBLE);
            cbInstantCheckout.setOnCheckedChangeListener(getInstantCheckoutChangeListener());
            cbInstantCheckout.setChecked(
                    actionListener.isRecentInstantCheckoutUsed(data.getCategoryId())
            );
        } else {
            cbInstantCheckout.setChecked(false);
            layoutCheckout.setVisibility(GONE);
        }
        tooltipInstantCheckout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetView.show();
            }
        });
    }

    private WidgetOperatorChooserView2.OperatorChoserListener getListenerOperatorChooser() {
        return new WidgetOperatorChooserView2.OperatorChoserListener() {
            @Override
            public void onCheckChangeOperator(Operator operator) {
                operatorSelected = operator;
                setBtnBuyDigitalText(operatorSelected.getRule().getButtonText());
                if (!operator.getClientNumberList().isEmpty()) {
                    renderClientNumberInputForm(operator);
                    if (operatorSelected.getRule().getProductViewStyle() == 99) {
                        renderDefaultProductSelected();
                    } else {
                        showProducts();
                    }
                }
            }

            @Override
            public void onResetOperator(boolean resetClientNumber) {

            }

            @Override
            public void onTrackingOperator() {

            }
        };
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Operator> getActionListenerOperatorChooser() {
        return new BaseDigitalChooserView.ActionListener<Operator>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Operator operator) {
                operatorSelected = operator;
                setBtnBuyDigitalText(operatorSelected.getRule().getButtonText());
                if (!operator.getClientNumberList().isEmpty()) {
                    renderClientNumberInputForm(operator);
                    if (operatorSelected.getRule().getProductViewStyle() == 99) {
                        renderDefaultProductSelected();
                    } else {
                        showProducts();
                    }
                }
            }

            @Override
            public void onDigitalChooserClicked(List<Operator> data) {
                actionListener.onOperatorChooserStyle3Clicked(data, "");
            }
        };
    }

    private void setBtnBuyDigitalText(String buttonText) {
        if (!TextUtils.isEmpty(buttonText)) {
            btnBuyDigital.setText(buttonText);
        } else {
            btnBuyDigital.setText(context.getString(R.string.label_btn_buy_digital));
        }
    }

    @NonNull
    private ClientNumberInputView.ActionListener getActionListenerClientNumberInputView() {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {
                actionListener.onButtonContactPickerClicked();
            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                clientNumberInputView.enableImageOperator(operatorSelected.getImage());
            }

            @Override
            public void onClientNumberInputInvalid() {
                clearHolder(holderChooserProduct);
                clearHolder(holderAdditionalInfoProduct);
                clearHolder(holderPriceInfoProduct);
            }

            @Override
            public void onClientNumberHasFocus(String clientNumber) {
                actionListener.onClientNumberClicked(clientNumber,
                        operatorSelected.getClientNumberList().get(0),
                        historyClientNumber.getRecentClientNumberList());
            }

            @Override
            public void onClientNumberCleared() {
                actionListener.onClientNumberCleared(operatorSelected.getClientNumberList().get(0),
                        historyClientNumber.getRecentClientNumberList());
            }

            @Override
            public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {

            }
        };
    }

    private WidgetProductChooserView2.ProductChoserListener getProductChooserListener() {
        return new WidgetProductChooserView2.ProductChoserListener() {
            @Override
            public void initDataView(Product product) {
                productSelected = product;
                renderPriceProductInfo();
            }

            @Override
            public void trackingProduct() {
                if (productSelected != null)
                    UnifyTracking.eventSelectProductWidget(data.getName(),
                            productSelected.getDesc());
            }
        };
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser() {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;
                renderAdditionalProductInfo();
                renderPriceProductInfo();
            }

            @Override
            public void onDigitalChooserClicked(List<Product> data) {
                actionListener.onProductChooserClicked(
                        data, operatorSelected.getOperatorId(), operatorSelected != null
                                ? operatorSelected.getRule().getProductText() : ""
                );
            }
        };
    }

    @NonNull
    private OnClickListener getButtonBuyClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickBeli(data.getName(), data.getName());
                actionListener.onButtonBuyClicked(generatePreCheckoutData(), cbInstantCheckout.isChecked());
            }
        };
    }

    @NonNull
    private PreCheckoutProduct generatePreCheckoutData() {
        PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
        boolean canBeCheckout = false;

        if (operatorSelected == null) {
            preCheckoutProduct.setErrorCheckout(
                    context.getString(R.string.message_error_digital_operator_not_selected)
            );
        } else if (productSelected == null) {
            if (operatorSelected.getRule().getProductViewStyle() == 99
                    && !operatorSelected.getClientNumberList().isEmpty()
                    && !clientNumberInputView.isValidInput(operatorSelected.getPrefixList())) {
                preCheckoutProduct.setErrorCheckout(
                        operatorSelected.getClientNumberList().get(0).getText()
                                + " " + context.getString(
                                R.string.message_error_digital_client_number_format_invalid
                        )
                );
            } else {
                preCheckoutProduct.setErrorCheckout(
                        context.getString(R.string.message_error_digital_product_not_selected)
                );
            }
        } else if (!operatorSelected.getClientNumberList().isEmpty()
                && clientNumberInputView.getText().isEmpty()) {
            preCheckoutProduct.setErrorCheckout(
                    context.getString(
                            R.string.message_error_digital_client_number_not_filled
                    ) + " " + operatorSelected.getClientNumberList().get(0).getText()
                            .toLowerCase()
            );
        } else {
            preCheckoutProduct.setProductId(productSelected.getProductId());
            preCheckoutProduct.setOperatorId(operatorSelected.getOperatorId());
            canBeCheckout = true;
            if (productSelected.getPromo() != null) {
                preCheckoutProduct.setPromo(true);
            }
        }
        if (canBeCheckout) {
            actionListener.storeLastInstantCheckoutUsed(
                    data.getCategoryId(), cbInstantCheckout.isChecked()
            );
        }

        if (canBeCheckout) {
            actionListener.storeLastInstantCheckoutUsed(
                    data.getCategoryId(), cbInstantCheckout.isChecked()
            );
        }
        preCheckoutProduct.setCategoryId(data.getCategoryId());
        preCheckoutProduct.setCategoryName(data.getName());
        preCheckoutProduct.setClientNumber(clientNumberInputView.getText());
        preCheckoutProduct.setInstantCheckout(cbInstantCheckout.isChecked());
        preCheckoutProduct.setCanBeCheckout(canBeCheckout);
        return preCheckoutProduct;
    }

    private boolean hasLastOrderHistoryData() {
        return historyClientNumber != null && historyClientNumber.getLastOrderClientNumber() != null;
    }

    @Override
    public void onProductLinkClicked(String url) {
        actionListener.onProductDetailLinkClicked(url);
    }

}