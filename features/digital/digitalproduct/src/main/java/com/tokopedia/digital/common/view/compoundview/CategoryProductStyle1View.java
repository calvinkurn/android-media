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

import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.compoundview.ProductAdditionalInfoView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.utils.DeviceUtil;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle1View extends
        BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber>
        implements ProductAdditionalInfoView.ActionListener {

    @BindView(R2.id.tv_title_category)
    TextView tvTitle;
    @BindView(R2.id.holder_client_number)
    LinearLayout holderClientNumber;
    @BindView(R2.id.holder_chooser_product)
    LinearLayout holderChooserProduct;
    @BindView(R2.id.holder_additional_info_product)
    LinearLayout holderAdditionalInfoProduct;
    @BindView(R2.id.holder_price_info_product)
    LinearLayout holderPriceInfoProduct;
    @BindView(R2.id.btn_buy_digital)
    TextView btnBuyDigital;
    @BindView(R2.id.cb_instant_checkout)
    CheckBox cbInstantCheckout;
    @BindView(R2.id.layout_checkout)
    RelativeLayout layoutCheckout;
    @BindView(R2.id.tooltip_instant_checkout)
    ImageView tooltipInstantCheckout;

    private DigitalProductChooserView digitalProductChooserView;
    private ClientNumberInputView clientNumberInputView;
    private ProductAdditionalInfoView productAdditionalInfoView;
    private ProductPriceInfoView productPriceInfoView;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle1View(Context context) {
        super(context);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle1View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateView() {
        clientNumberInputView = new ClientNumberInputView(context);
        digitalProductChooserView = new DigitalProductChooserView(context);
        productAdditionalInfoView = new ProductAdditionalInfoView(context);
        productPriceInfoView = new ProductPriceInfoView(context);
        productAdditionalInfoView.setActionListener(this);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_1;
    }

    @Override
    protected void onInitialDataRendered() {
        if (source == NATIVE) {
            tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        } else {
            tvTitle.setVisibility(GONE);
        }
        renderClientNumberInputForm();
        renderInstantCheckoutOption();
        btnBuyDigital.setOnClickListener(getButtonBuyClickListener());
    }

    @Override
    protected void onUpdateSelectedProductData() {
        if (operatorSelected.getRule().getProductViewStyle() != SINGLE_PRODUCT) {
            this.digitalProductChooserView.renderUpdateDataSelected(productSelected);
        }
    }

    @Override
    protected void onUpdateSelectedOperatorData() {

    }

    @Override
    protected void onInstantCheckoutUnChecked() {
        if (operatorSelected != null) {
            setBtnBuyDigitalText(operatorSelected.getRule().getButtonText());
        } else {
            btnBuyDigital.setText(context.getString(R.string.label_btn_buy_digital));
        }
    }

    @Override
    protected void onInstantCheckoutChecked() {
        btnBuyDigital.setText(context.getString(R.string.label_btn_pay_digital));
    }

    @Override
    public void renderClientNumber(String clientNumber) {
        this.clientNumberInputView.setText(clientNumber);
    }

    @Override
    public boolean isInstantCheckoutChecked() {
        return false;
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
        if (!TextUtils.isEmpty(clientNumberState)) {
            clientNumberInputView.setText(clientNumberState);
            if (operatorSelected != null && productSelectedState != null) {
                digitalProductChooserView.renderUpdateDataSelected(productSelectedState);
            }
        }
        if (data.isInstantCheckout()) {
            cbInstantCheckout.setChecked(isInstantCheckoutChecked);
        }
    }

    @Override
    public void clearFocusOnClientNumber() {
        clientNumberInputView.clearFocus();
    }

    /**
     * apakah mendukung instant instantCheckout ?
     */
    private void renderInstantCheckoutOption() {
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

    private void renderClientNumberInputForm() {
        final ClientNumber clientNumber = data.getClientNumberList().get(0);
        clearHolder(holderClientNumber);
        clientNumberInputView.setActionListener(getActionListenerClientNumberInput());
        clientNumberInputView.renderData(clientNumber);
        clientNumberInputView.resetInputTyped();
        holderClientNumber.addView(clientNumberInputView);

        String lastClientNumberHistory = "";
        String lastOperatorHistory = "";
        if (hasLastOrderHistoryData()) {
            lastClientNumberHistory = historyClientNumber.getLastOrderClientNumber().getClientNumber();
            lastOperatorHistory = historyClientNumber.getLastOrderClientNumber().getOperatorId();
        }
        if (!TextUtils.isEmpty(lastClientNumberHistory)) {
            clientNumberInputView.setText(lastClientNumberHistory);
        } else {
            if (!TextUtils.isEmpty(lastOperatorHistory)) {
                for (Operator operator : data.getOperatorList()) {
                    if (operator.getOperatorId().equals(lastOperatorHistory)) {
                        setOperator(operator);
                        break;
                    }
                }
            }
        }

        if (source == WIDGET) {
            if (hasLastOrderHistoryData()) {
                if (!data.getClientNumberList().isEmpty()) {
                    clientNumberInputView.setAdapterAutoCompleteClientNumber(historyClientNumber.getRecentClientNumberList());
                }
            }
        }
    }

    private void setOperator(Operator operator) {
        operatorSelected = operator;
        clientNumberInputView.tvErrorClientNumber.setText("");
        clientNumberInputView.tvErrorClientNumber.setVisibility(GONE);
        clientNumberInputView.enableImageOperator(operator.getImage());
        clientNumberInputView.setFilterMaxLength(operator.getRule().getMaximumLength());
        if (operator.getRule().getProductViewStyle() == SINGLE_PRODUCT) {
            renderDefaultProductSelected();
        } else {
            showProducts();
        }
        setBtnBuyDigitalText(operator.getRule().getButtonText());
        actionListener.onOperatorSelected(data.getName(), operator.getName());
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

        if (hasLastOrderHistoryData() && operatorSelected != null
                && operatorSelected.getOperatorId().equalsIgnoreCase(
                historyClientNumber.getLastOrderClientNumber().getOperatorId()
        )) {
            for (Product product : operatorSelected.getProductList()) {
                if (product.getProductId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getProductId()
                )) {
                    digitalProductChooserView.renderUpdateDataSelected(product);
                    break;
                }
            }
        }
    }

    private PreCheckoutProduct generatePreCheckoutData() {
        PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
        boolean canBeCheckout = false;
        if (!data.getClientNumberList().isEmpty() && !isClientNumberValid()) {
            if (clientNumberInputView.getText().isEmpty()) {
                clientNumberInputView.setErrorText(
                        context.getString(
                                R.string.message_error_digital_client_number_not_filled
                        ) + " " + data.getClientNumberList().get(0).getText().toLowerCase()
                );
            } else {
                for (Validation validation : data.getClientNumberList().get(0).getValidation()) {
                    if (!Pattern.matches(validation.getRegex(), getClientNumber())) {
                        clientNumberInputView.setErrorText(
                                validation.getError()
                        );
                        break;
                    }
                }
            }
        } else if (operatorSelected == null) {
            if (data.getOperatorList().size() == 1
                    && !data.getClientNumberList().isEmpty()
                    && !clientNumberInputView.isValidInput(
                    data.getOperatorList().get(0).getPrefixList())
                    ) {
                preCheckoutProduct.setErrorCheckout(
                        data.getClientNumberList().get(0).getText()
                                + " " + context.getString(
                                R.string.message_error_digital_client_number_format_invalid
                        )
                );
            } else {
                preCheckoutProduct.setErrorCheckout(
                        context.getString(R.string.message_error_digital_operator_not_selected)
                );
            }
        } else if (productSelected == null) {
            if (operatorSelected.getRule().getProductViewStyle() == SINGLE_PRODUCT
                    && !data.getClientNumberList().isEmpty()
                    && !clientNumberInputView.isValidInput(operatorSelected.getPrefixList())) {
                preCheckoutProduct.setErrorCheckout(
                        data.getClientNumberList().get(0).getText()
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
            for (Validation validation : data.getClientNumberList().get(0).getValidation()) {
                if (!Pattern.matches(validation.getRegex(), getClientNumber())) {
                    return false;
                }
            }
            return true;
        }
    }

    @NonNull
    private OnClickListener getButtonBuyClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onButtonBuyClicked(generatePreCheckoutData(), cbInstantCheckout.isChecked());
            }
        };
    }

    @NonNull
    private ClientNumberInputView.ActionListener getActionListenerClientNumberInput() {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {
                actionListener.onButtonContactPickerClicked();
            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                if (tempClientNumber.length() >= 4) {
                    String validClientNumber = DeviceUtil.validatePrefixClientNumber(tempClientNumber);
                    boolean operatorFound = false;
                    outerLoop:
                    for (Operator operator : data.getOperatorList()) {
                        for (String prefix : operator.getPrefixList()) {
                            if (validClientNumber.startsWith(prefix)) {
                                setOperator(operator);
                                operatorFound = true;
                                break outerLoop;
                            }
                        }
                    }
                    if (!operatorFound) {
                        resetOperator();
                    }
                } else {
                    operatorSelected = null;
                    productSelected = null;
                    clientNumberInputView.disableImageOperator();
                    clearHolder(holderChooserProduct);
                    clearHolder(holderAdditionalInfoProduct);
                    clearHolder(holderPriceInfoProduct);
                }
            }

            @Override
            public void onClientNumberInputInvalid() {
                operatorSelected = null;
                productSelected = null;
                clientNumberInputView.disableImageOperator();
                clearHolder(holderChooserProduct);
                clearHolder(holderAdditionalInfoProduct);
                clearHolder(holderPriceInfoProduct);
            }

            @Override
            public void onClientNumberHasFocus(String clientNumber) {
                actionListener.onClientNumberClicked(clientNumber,
                        data.getClientNumberList().get(0),
                        historyClientNumber.getRecentClientNumberList());
            }

            @Override
            public void onClientNumberCleared() {
                actionListener.onClientNumberCleared(data.getClientNumberList().get(0),
                        historyClientNumber.getRecentClientNumberList());
            }

            @Override
            public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {
                actionListener.onItemAutocompletedSelected(orderClientNumber);
            }
        };
    }

    private void resetOperator() {
        operatorSelected = null;
        productSelected = null;
        clientNumberInputView.disableImageOperator();
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser() {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product product) {
                productSelected = product;
                renderAdditionalInfoProduct();
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
                actionListener.onProductSelected(data.getName(), productSelected.getDesc());
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
        renderAdditionalInfoProduct();
        renderPriceProductInfo();
    }

    private void renderPriceProductInfo() {
        if (source == NATIVE) {
            clearHolder(holderPriceInfoProduct);
            if (operatorSelected != null && operatorSelected.getRule().isShowPrice()) {
                productPriceInfoView.renderData(productSelected);
                holderPriceInfoProduct.addView(productPriceInfoView);
            }
        }
    }

    private void renderAdditionalInfoProduct() {
        if (source == NATIVE) {
            clearHolder(holderAdditionalInfoProduct);
            productAdditionalInfoView.renderData(productSelected);
            holderAdditionalInfoProduct.addView(productAdditionalInfoView);
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
