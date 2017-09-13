package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ClientNumber;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

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
        tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        if (!data.getClientNumberList().isEmpty()) {
            renderClientNumberInputForm();
        } else {
            for (Operator operator : data.getOperatorList()) {
                if (operator.getOperatorId().equalsIgnoreCase(data.getDefaultOperatorId())) {
                    operatorSelected = operator;
                    renderProductChooserOptions();
                    break;
                }
            }
        }
        renderInstantCheckoutOption();
        btnBuyDigital.setOnClickListener(getButtonBuyClickListener());
    }

    @Override
    protected void onUpdateSelectedProductData() {
        this.digitalProductChooserView.renderUpdateDataSelected(productSelected);
    }

    @Override
    protected void onUpdateSelectedOperatorData() {

    }

    @Override
    protected void onInstantCheckoutUnChecked() {
        btnBuyDigital.setText(context.getString(R.string.label_btn_buy_digital));
    }

    @Override
    protected void onInstantCheckoutChecked() {
        btnBuyDigital.setText(context.getString(R.string.label_btn_pay_digital));
    }

    @Override
    public void renderClientNumberFromContact(String clientNumber) {
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

    /**
     * apakah mendukung instant checkout ?
     */
    private void renderInstantCheckoutOption() {
        if (data.isInstantCheckout()) {
            cbInstantCheckout.setVisibility(VISIBLE);
            cbInstantCheckout.setOnCheckedChangeListener(getInstantCheckoutChangeListener());
            cbInstantCheckout.setChecked(
                    actionListener.isRecentInstantCheckoutUsed(data.getCategoryId())
            );
        } else {
            cbInstantCheckout.setChecked(false);
            cbInstantCheckout.setVisibility(GONE);
        }
    }

    private void renderClientNumberInputForm() {
        final ClientNumber clientNumber = data.getClientNumberList().get(0);
        clearHolder(holderClientNumber);
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        clientNumberInputView.setActionListener(getActionListenerClientNumberInput());
        clientNumberInputView.renderData(clientNumber);
        holderClientNumber.addView(clientNumberInputView);
        clientNumberInputView.resetInputTyped();

        String lastClientNumberHistory = "";
        if (hasLastOrderHistoryData())
            lastClientNumberHistory = historyClientNumber.getLastOrderClientNumber().getClientNumber();
        if (!TextUtils.isEmpty(lastClientNumberHistory)) {
            clientNumberInputView.setText(lastClientNumberHistory);
        }


        if (hasLastOrderHistoryData()) {
            if (!data.getClientNumberList().isEmpty()) {
                List<String> recentClientNumberString = new ArrayList<>();
                for (OrderClientNumber orderClientNumber
                        : historyClientNumber.getRecentClientNumberList()) {
                    recentClientNumberString.add(orderClientNumber.getClientNumber());
                }
                clientNumberInputView.setAdapterAutoCompleteClientNumber(recentClientNumberString);
            }
        }
    }

    private void renderProductChooserOptions() {
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList());
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

    @NonNull
    private OnClickListener getButtonBuyClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbInstantCheckout.isChecked())
                    UnifyTracking.eventClickBeliInstantSaldo(data.getName(), data.getName());
                else
                    UnifyTracking.eventClickBeli(data.getName(), data.getName());

                actionListener.onButtonBuyClicked(generatePreCheckoutData());
            }
        };
    }

    private PreCheckoutProduct generatePreCheckoutData() {
        PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
        boolean canBeCheckout = false;
        if (!data.getClientNumberList().isEmpty()
                && clientNumberInputView.getText().isEmpty()) {
            preCheckoutProduct.setErrorCheckout(
                    context.getString(
                            R.string.message_error_digital_client_number_not_filled
                    ) + " " + data.getClientNumberList().get(0).getText().toLowerCase()
            );
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
            if (operatorSelected.getRule().getProductViewStyle() == 99
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

    @NonNull
    private ClientNumberInputView.ActionListener getActionListenerClientNumberInput() {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {
                actionListener.onButtonContactPickerClicked();
            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                String validClientNumber = DeviceUtil.validatePrefixClientNumber(tempClientNumber);
                outerLoop:
                for (Operator operator : data.getOperatorList()) {
                    for (String prefix : operator.getPrefixList()) {
                        if (validClientNumber.startsWith(prefix)) {
                            operatorSelected = operator;
                            clientNumberInputView.enableImageOperator(operator.getImage());
                            if (operatorSelected.getRule().getProductViewStyle() == 99) {
                                renderDefaultProductSelected();
                            } else {
                                renderProductChooserOptions();
                            }
                            break outerLoop;
                        } else {
                            clientNumberInputView.disableImageOperator();
                            productSelected = null;
                            clearHolder(holderChooserProduct);
                            clearHolder(holderAdditionalInfoProduct);
                            clearHolder(holderPriceInfoProduct);
                        }
                    }
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
        };
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
        renderAdditionalInfoProduct();
        renderPriceInfoProduct();

    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser() {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;
                renderAdditionalInfoProduct();
                renderPriceInfoProduct();
            }

            @Override
            public void onDigitalChooserClicked(List<Product> data) {
                actionListener.onProductChooserStyle1Clicked(
                        data, operatorSelected != null ? operatorSelected.getRule().getProductText() : ""
                );
            }
        };
    }

    private void renderPriceInfoProduct() {
        clearHolder(holderPriceInfoProduct);
        if (operatorSelected != null && operatorSelected.getRule().isShowPrice()) {
            productPriceInfoView.renderData(productSelected);
            holderPriceInfoProduct.addView(productPriceInfoView);
        }
    }

    private void renderAdditionalInfoProduct() {
        clearHolder(holderAdditionalInfoProduct);
        productAdditionalInfoView.renderData(productSelected);
        holderAdditionalInfoProduct.addView(productAdditionalInfoView);
    }


    private boolean hasLastOrderHistoryData() {
        return historyClientNumber != null && historyClientNumber.getLastOrderClientNumber() != null;
    }

    @Override
    public void onProductLinkClicked(String url) {
        actionListener.onProductDetailLinkClicked(url);
    }
}
