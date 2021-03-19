package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalRadioChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.compoundview.ProductAdditionalInfoView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.unifycomponents.UnifyButton;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle2View extends
        BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber>
        implements ProductAdditionalInfoView.ActionListener {

    private TextView tvTitle;
    private LinearLayout holderRadioChooserOperator;
    private LinearLayout holderClientNumber;
    private LinearLayout holderChooserProduct;
    private LinearLayout holderAdditionalInfoProduct;
    private LinearLayout holderPriceInfoProduct;
    private UnifyButton btnBuyDigital;
    private CheckBox cbInstantCheckout;
    private RelativeLayout layoutCheckout;
    private ImageView tooltipInstantCheckout;

    private RadioChooserView radioChooserView;
    private ClientNumberInputView clientNumberInputView;
    private DigitalProductChooserView digitalProductChooserView;
    private ProductAdditionalInfoView productAdditionalInfoView;
    private ProductPriceInfoView productPriceInfoView;

    public CategoryProductStyle2View(Context context) {
        super(context);
    }

    public CategoryProductStyle2View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryProductStyle2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateView() {
        tvTitle = findViewById(R.id.tv_title_category);
        holderRadioChooserOperator = findViewById(R.id.holder_radio_chooser_operator);
        holderClientNumber = findViewById(R.id.holder_client_number);
        holderChooserProduct = findViewById(R.id.holder_chooser_product);
        holderAdditionalInfoProduct = findViewById(R.id.holder_additional_info_product);
        holderPriceInfoProduct = findViewById(R.id.holder_price_info_product);
        btnBuyDigital = findViewById(R.id.btn_buy_digital);
        cbInstantCheckout = findViewById(R.id.cb_instant_checkout);
        layoutCheckout = findViewById(R.id.layout_checkout);
        tooltipInstantCheckout = findViewById(R.id.tooltip_instant_checkout);

        radioChooserView = new RadioChooserView(context);
        clientNumberInputView = new ClientNumberInputView(context);
        digitalProductChooserView = new DigitalProductChooserView(context);
        productAdditionalInfoView = new ProductAdditionalInfoView(context);
        productPriceInfoView = new ProductPriceInfoView(context);
        productAdditionalInfoView.setActionListener(this);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_digital_product_style_2;
    }

    @Override
    protected void onInitialDataRendered() {
        if (source == NATIVE) {
            tvTitle.setText(TextUtils.isEmpty(data.titleText) ? "" : data.titleText);
        } else {
            tvTitle.setVisibility(GONE);
        }
        clearHolder(holderRadioChooserOperator);
        renderOperatorChooserOptions();
        renderInstantCheckoutOptions();
        btnBuyDigital.setOnClickListener(getButtonBuyListener());
    }

    @Override
    protected void onUpdateSelectedProductData() {
        if (operatorSelected.getRule().getProductViewStyle() != SINGLE_PRODUCT) {
            this.digitalProductChooserView.renderUpdateDataSelected(productSelected);
        }
    }

    @Override
    protected void onUpdateSelectedOperatorData() {
        radioChooserView.renderUpdateDataSelected(operatorSelected);
    }

    @Override
    public void renderClientNumber(String clientNumber) {
        clientNumberInputView.setText(clientNumber);
    }

    @Override
    protected void onInstantCheckoutUnChecked() {
        setBtnBuyDigitalText(operatorSelected.getRule().getButtonText());
    }

    @Override
    protected void onInstantCheckoutChecked() {
        btnBuyDigital.setText(context.getString(R.string.label_btn_pay_digital));
    }

    @Override
    public boolean isInstantCheckoutChecked() {
        return cbInstantCheckout.isChecked();
    }

    @Override
    public void onBuyButtonLoading(Boolean showLoading) {
        btnBuyDigital.setLoading(showLoading);
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
            radioChooserView.renderUpdateDataSelected(operatorSelected);
            if (!TextUtils.isEmpty(clientNumberState)) {
                clientNumberInputView.setText(clientNumberState);
            }
        }
    }

    @Override
    public void clearFocusOnClientNumber() {
        clientNumberInputView.clearFocus();
    }

    private void renderInstantCheckoutOptions() {
        if (data.isInstantCheckout) {
            layoutCheckout.setVisibility(VISIBLE);
            cbInstantCheckout.setOnCheckedChangeListener(getInstantCheckoutChangeListener());
            cbInstantCheckout.setChecked(
                    actionListener.isRecentInstantCheckoutUsed(data.categoryId)
            );
        } else {
            cbInstantCheckout.setChecked(false);
            layoutCheckout.setVisibility(GONE);
        }
    }

    private void renderOperatorChooserOptions() {
        clearHolder(holderRadioChooserOperator);
        radioChooserView.setActionListener(getActionListenerRadioChooserOperator());
        radioChooserView.renderInitDataList(data.operatorList, data.defaultOperatorId);
        holderRadioChooserOperator.addView(radioChooserView);

        if (hasLastOrderHistoryData()) {
            for (Operator operator : data.operatorList) {
                if (operator.getOperatorId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                )) {
                    radioChooserView.renderUpdateDataSelected(operator);
                    break;
                }
            }
        }
    }

    private void renderClientNumberInputForm(Operator operator) {
        clearHolder(holderClientNumber);
        clientNumberInputView.setActionListener(getActionListenerClientNumberInput());
        clientNumberInputView.renderData(operator.getClientNumberList().get(0));
        clientNumberInputView.setFilterMaxLength(operator.getRule().getMaximumLength());
        clientNumberInputView.resetInputTyped();
        clientNumberInputView.enableImageOperator(operatorSelected.getImage());
        holderClientNumber.addView(clientNumberInputView);

        if (hasLastOrderHistoryData()) {
            if (historyClientNumber.getLastOrderClientNumber().getOperatorId().equalsIgnoreCase(
                    operator.getOperatorId())) {
                if (!TextUtils.isEmpty(historyClientNumber
                        .getLastOrderClientNumber().getClientNumber())) {
                    clientNumberInputView.setText(
                            historyClientNumber.getLastOrderClientNumber().getClientNumber()
                    );
                }
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
        renderProductChooserOptions();
    }

    private void renderProductChooserOptions() {
        clearHolder(holderChooserProduct);
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList(),
                operatorSelected.getDefaultProductId());
        digitalProductChooserView.setLabelText(operatorSelected.getRule().getProductText());
        holderChooserProduct.addView(digitalProductChooserView);

        if (hasLastOrderHistoryData()) {
            if (!TextUtils.isEmpty(historyClientNumber.getLastOrderClientNumber().getOperatorId())) {
                for (Product product : operatorSelected.getProductList()) {
                    if (product.getProductId().equalsIgnoreCase(
                            historyClientNumber.getLastOrderClientNumber().getProductId())
                            ) {
                        digitalProductChooserView.renderUpdateDataSelected(product);
                        break;
                    }
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

    @NonNull
    private BaseDigitalRadioChooserView.ActionListener<Operator>
    getActionListenerRadioChooserOperator() {
        return new BaseDigitalRadioChooserView.ActionListener<Operator>() {
            @Override
            public void onUpdateDataDigitalRadioChooserSelectedRendered(Operator operator) {
                operatorSelected = operator;
                if (!operatorSelected.getClientNumberList().isEmpty()) {
                    renderClientNumberInputForm(operatorSelected);
                }
                if (operatorSelected.getRule().getProductViewStyle() == SINGLE_PRODUCT) {
                    renderDefaultProductSelected();
                } else {
                    showProducts();
                }
                setBtnBuyDigitalText(operatorSelected.getRule().getButtonText());
            }

            @Override
            public void tracking() {
                actionListener.onOperatorSelected(data.name, operatorSelected.getName());
            }
        };
    }

    @NonNull
    private ClientNumberInputView.ActionListener
    getActionListenerClientNumberInput() {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonCameraPickerClicked() {

            }

            @Override
            public void onButtonContactPickerClicked() {
                actionListener.onButtonContactPickerClicked();
            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {

            }

            @Override
            public void onClientNumberInputInvalid() {

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
                        historyClientNumber.getRecentClientNumberList());
            }

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
                        products, operatorSelected.getOperatorId(),
                        operatorSelected != null ? operatorSelected.getRule().getProductText() : ""
                );
            }

            @Override
            public void tracking() {
                actionListener.onProductSelected(data.name, productSelected.getDesc());
            }
        };
    }

    @NonNull
    private OnClickListener getButtonBuyListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onButtonBuyClicked(generatePreCheckoutData(), cbInstantCheckout.isChecked());
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

    private PreCheckoutProduct generatePreCheckoutData() {
        PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
        boolean canBeCheckout = false;
        if (operatorSelected == null) {
            preCheckoutProduct.setErrorCheckout(
                    context.getString(R.string.message_error_digital_operator_not_selected)
            );
        } else if (!operatorSelected.getClientNumberList().isEmpty() &&
                !isClientNumberValid()) {
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
        } else {
            preCheckoutProduct.setProductId(productSelected.getProductId());
            preCheckoutProduct.setOperatorId(operatorSelected.getOperatorId());
            canBeCheckout = true;
            if (productSelected.getPromo() != null) {
                preCheckoutProduct.setPromo(true);
            }
        }
        preCheckoutProduct.setCategoryId(data.categoryId);
        preCheckoutProduct.setCategoryName(data.name);
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
