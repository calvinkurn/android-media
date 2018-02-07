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

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalOperatorChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.compoundview.ProductAdditionalInfoView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.ClientNumber;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.product.view.model.Validation;
import com.tokopedia.digital.widget.view.compoundview.NewWidgetOperatorChooserView;
import com.tokopedia.digital.widget.view.compoundview.NewWidgetProductChooserView;

import java.util.List;
import java.util.regex.Pattern;

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

    private NewWidgetOperatorChooserView widgetOperatorChooserView;
    private DigitalOperatorChooserView digitalOperatorChooserView;
    private ClientNumberInputView clientNumberInputView;
    private NewWidgetProductChooserView widgetProductChooserView;
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
        widgetOperatorChooserView = new NewWidgetOperatorChooserView(context);
        digitalOperatorChooserView = new DigitalOperatorChooserView(context);
        clientNumberInputView = new ClientNumberInputView(context);
        widgetProductChooserView = new NewWidgetProductChooserView(context);
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
        if (source == NATIVE) {
            tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        } else {
            tvTitle.setVisibility(GONE);
        }
        if (source == NATIVE) {
            renderOperatorChooserOptions();
        } else {
            renderOperatorChooserOptionsWidget();
        }
        renderInstantCheckoutOptions();
        btnBuyDigital.setOnClickListener(getButtonBuyClickedListener());
    }

    @Override
    protected void onUpdateSelectedOperatorData() {
        if (source == NATIVE) {
            digitalOperatorChooserView.renderUpdateDataSelected(operatorSelected);
        } else {
            widgetOperatorChooserView.renderUpdateDataSelected(operatorSelected);
        }
    }

    @Override
    protected void onUpdateSelectedProductData() {
        if (operatorSelected.getRule().getProductViewStyle() != SINGLE_PRODUCT) {
            if (source == NATIVE) {
                this.digitalProductChooserView.renderUpdateDataSelected(productSelected);
            } else {
                this.widgetProductChooserView.renderUpdateDataSelected(productSelected);
            }
        }
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
        widgetOperatorChooserView.setActionListener(getActionListenerOperatorChooser());
        widgetOperatorChooserView.renderInitDataList(data.getOperatorList(), data.getDefaultOperatorId());
        holderChooserOperator.addView(widgetOperatorChooserView);

        if (hasLastOrderHistoryData()) {
            for (Operator operator : data.getOperatorList()) {
                if (operator.getOperatorId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                )) {
                    widgetOperatorChooserView.renderInitDataList(data.getOperatorList(), operator.getOperatorId());
                    break;
                }
            }
        }
    }

    private void renderOperatorChooserOptions() {
        clearHolder(holderChooserOperator);
        digitalOperatorChooserView.setLabelText(data.getOperatorLabel());
        digitalOperatorChooserView.setActionListener(getActionListenerOperatorChooser());
        digitalOperatorChooserView.renderInitDataList(data.getOperatorList(), data.getDefaultOperatorId());
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
        clientNumberInputView.setActionListener(getActionListenerClientNumberInputView());
        clientNumberInputView.renderData(operator.getClientNumberList().get(0));
        clientNumberInputView.setFilterMaxLength(operator.getRule().getMaximumLength());
        clientNumberInputView.resetInputTyped();
        clientNumberInputView.enableImageOperator(operatorSelected.getImage());
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

        if (source == WIDGET) {
            if (hasLastOrderHistoryData()) {
                if (!operator.getClientNumberList().isEmpty()) {
                    clientNumberInputView.setAdapterAutoCompleteClientNumber(historyClientNumber.getRecentClientNumberList());
                }
            }
        }
    }

    private void showProducts() {
        if (source == NATIVE) {
            renderProductChooserOptions();
        } else {
            renderProductChooserOptionsWidget();
        }
    }

    private void renderProductChooserOptionsWidget() {
        clearHolder(holderChooserProduct);
        widgetProductChooserView.setLabelText(operatorSelected.getRule().getProductText());
        widgetProductChooserView.setActionListener(getActionListenerProductChooser());
        widgetProductChooserView.renderInitDataList(
                operatorSelected.getProductList(),
                operatorSelected.getRule().isShowPrice(),
                String.valueOf(operatorSelected.getDefaultProductId())
        );
        holderChooserProduct.addView(widgetProductChooserView);

        if (hasLastOrderHistoryData() && operatorSelected != null) {
            for (Product product : operatorSelected.getProductList()) {
                if (product.getProductId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getProductId()
                ) && operatorSelected.getOperatorId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                )) {
                    widgetProductChooserView.renderUpdateDataSelected(product);
                    break;
                }
            }
        }
    }

    private void renderProductChooserOptions() {
        clearHolder(holderChooserProduct);
        digitalProductChooserView.setLabelText(operatorSelected.getRule().getProductText());
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList(),
                operatorSelected.getDefaultProductId());
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
        if (!operatorSelected.getProductList().isEmpty() && operatorSelected.getProductList().get(0) != null) {
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
        if (source == NATIVE) {
            clearHolder(holderPriceInfoProduct);
            if (operatorSelected.getRule().isShowPrice()) {
                productPriceInfoView.renderData(productSelected);
                holderPriceInfoProduct.addView(productPriceInfoView);
            }
        }
    }

    private void renderAdditionalProductInfo() {
        if (source == NATIVE) {
            clearHolder(holderAdditionalInfoProduct);
            productAdditionalInfoView.renderData(productSelected);
            holderAdditionalInfoProduct.addView(productAdditionalInfoView);
        }
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

    private void setBtnBuyDigitalText(String buttonText) {
        if (!TextUtils.isEmpty(buttonText)) {
            btnBuyDigital.setText(buttonText);
        } else {
            btnBuyDigital.setText(context.getString(R.string.label_btn_buy_digital));
        }
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Operator> getActionListenerOperatorChooser() {
        return new BaseDigitalChooserView.ActionListener<Operator>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Operator operator) {
                operatorSelected = operator;
                if (!operator.getClientNumberList().isEmpty()) {
                    renderClientNumberInputForm(operator);
                }
                if (operator.getRule().getProductViewStyle() == SINGLE_PRODUCT) {
                    renderDefaultProductSelected();
                } else {
                    showProducts();
                }
                setBtnBuyDigitalText(operator.getRule().getButtonText());
            }

            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Operator operator, boolean resetClientNumber) {
                operatorSelected = operator;
                if (!operator.getClientNumberList().isEmpty()) {
                    if (resetClientNumber) {
                        renderClientNumberInputForm(operator);
                    }
                }
                if (operator.getRule().getProductViewStyle() == SINGLE_PRODUCT) {
                    renderDefaultProductSelected();
                } else {
                    showProducts();
                }
                setBtnBuyDigitalText(operator.getRule().getButtonText());
            }

            @Override
            public void onDigitalChooserClicked(List<Operator> operators) {
                actionListener.onOperatorChooserStyle3Clicked(operators, "");
            }

            @Override
            public void tracking() {
                actionListener.onOperatorSelected(data.getName(), operatorSelected.getName());
            }
        };
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
            public void onClientNumberHasFocus(String number) {
                ClientNumber clientNumber = null;
                if (!operatorSelected.getClientNumberList().isEmpty()) {
                    clientNumber = operatorSelected.getClientNumberList().get(0);
                }
                actionListener.onClientNumberClicked(number,
                        clientNumber,
                        historyClientNumber.getRecentClientNumberList());
            }

            @Override
            public void onClientNumberCleared() {
                ClientNumber clientNumber = null;
                if (!operatorSelected.getClientNumberList().isEmpty()) {
                    clientNumber = operatorSelected.getClientNumberList().get(0);
                }
                actionListener.onClientNumberCleared(clientNumber,
                        historyClientNumber.getRecentClientNumberList());            }

            @Override
            public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {
                actionListener.onItemAutocompletedSelected(orderClientNumber);
            }
        };
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser() {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product product) {
                productSelected = product;
                renderAdditionalProductInfo();
                renderPriceProductInfo();
            }

            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data, boolean resetClientNumber) {

            }

            @Override
            public void onDigitalChooserClicked(List<Product> products) {
                actionListener.onProductChooserClicked(
                        products, operatorSelected.getOperatorId(), operatorSelected != null
                                ? operatorSelected.getRule().getProductText() : ""
                );
            }

            @Override
            public void tracking() {
                actionListener.onProductSelected(data.getName(), productSelected.getDesc());
            }
        };
    }

    @NonNull
    private OnClickListener getButtonBuyClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
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
            if (operatorSelected.getRule().getProductViewStyle() == SINGLE_PRODUCT
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
                && !isClientNumberValid()) {
            if (clientNumberInputView.getText().isEmpty()) {
                clientNumberInputView.setErrorText(
                        context.getString(
                                R.string.message_error_digital_client_number_not_filled
                        ) + " " + operatorSelected.getClientNumberList().get(0).getText()
                                .toLowerCase()
                );
            } else {
                for (Validation validation : operatorSelected.getClientNumberList().get(0).getValidation()) {
                    if (!Pattern.matches(validation.getRegex(), getClientNumber())) {
                        clientNumberInputView.setErrorText(
                                validation.getError()
                        );
                        break;
                    }
                }
            }
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
        preCheckoutProduct.setCategoryId(data.getCategoryId());
        preCheckoutProduct.setCategoryName(data.getName());
        preCheckoutProduct.setClientNumber(clientNumberInputView.getText());
        preCheckoutProduct.setInstantCheckout(cbInstantCheckout.isChecked());
        preCheckoutProduct.setCanBeCheckout(canBeCheckout);
        return preCheckoutProduct;
    }

    private boolean isClientNumberValid() {
        if (clientNumberInputView.getText().isEmpty()) {
            return false;
        } else {
            for (Validation validation : operatorSelected.getClientNumberList().get(0).getValidation()) {
                if (!Pattern.matches(validation.getRegex(), getClientNumber())) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean hasLastOrderHistoryData() {
        return historyClientNumber != null && historyClientNumber.getLastOrderClientNumber() != null;
    }

    @Override
    public void onProductLinkClicked(String url) {
        actionListener.onProductDetailLinkClicked(url);
    }

}