package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle2View extends
        BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> {

    @BindView(R2.id.tv_title_category)
    TextView tvTitle;
    @BindView(R2.id.holder_radio_chooser_operator)
    LinearLayout holderRadioChooserOperator;
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

    private DigitalProductChooserView digitalProductChooserView;
    private DigitalOperatorRadioChooserView digitalOperatorRadioChooserView;
    private ClientNumberInputView clientNumberInputView;
    private ProductAdditionalInfoView productAdditionalInfoView;
    private ProductPriceInfoView productPriceInfoView;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle2View(Context context) {
        super(context);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle2View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public CategoryProductStyle2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateView() {
        digitalOperatorRadioChooserView = new DigitalOperatorRadioChooserView(context);
        clientNumberInputView = new ClientNumberInputView(context);
        digitalProductChooserView = new DigitalProductChooserView(context);
        productAdditionalInfoView = new ProductAdditionalInfoView(context);
        productPriceInfoView = new ProductPriceInfoView(context);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_2;
    }

    @Override
    protected void onInitialDataRendered() {
        tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "Title belum ada" : data.getTitleText());
        clearHolder(holderRadioChooserOperator);
        renderInstantCheckoutOptions();
        renderOperatorChooserOptions();
        btnBuyDigital.setOnClickListener(getButtonBuyListener(data));
    }

    @Override
    protected void onUpdateSelectedProductData() {
        digitalProductChooserView.renderUpdateDataSelected(productSelected);
    }

    @Override
    protected void onUpdateSelectedOperatorData() {
        digitalOperatorRadioChooserView.renderUpdateDataSelected(operatorSelected);
    }

    @Override
    public void renderClientNumberFromContact(String clientNumber) {
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
            digitalOperatorRadioChooserView.renderUpdateDataSelected(operatorSelected);
            if (!TextUtils.isEmpty(clientNumberState)) {
                clientNumberInputView.setText(clientNumberState);
            }
        }
    }

    private void renderInstantCheckoutOptions() {
        if (data.isInstantCheckout()) {
            cbInstantCheckout.setVisibility(VISIBLE);
        } else {
            cbInstantCheckout.setChecked(false);
            cbInstantCheckout.setVisibility(GONE);
        }
    }

    private void renderOperatorChooserOptions() {
        clearHolder(holderRadioChooserOperator);
        digitalOperatorRadioChooserView.setActionListener(getActionListenerRadioChooserOperator());
        digitalOperatorRadioChooserView.renderInitDataList(data.getOperatorList());
        holderRadioChooserOperator.addView(digitalOperatorRadioChooserView);

        if (hasLastOrderHistoryData()) {
            if (!TextUtils.isEmpty(historyClientNumber.getLastOrderClientNumber().getOperatorId())) {
                digitalOperatorRadioChooserView.renderUpdateDataSelectedByOperatorId(
                        historyClientNumber.getLastOrderClientNumber().getOperatorId()
                );
            }
        }
    }

    private void renderClientNumberInputForm() {
        clearHolder(holderClientNumber);
        clientNumberInputView.enableImageOperator(operatorSelected.getImage());
        clientNumberInputView.setActionListener(getActionListenerClientNumberInput());
        clientNumberInputView.renderData(operatorSelected.getClientNumberList().get(0));
        holderClientNumber.addView(clientNumberInputView);

        if (hasLastOrderHistoryData()) {
            if (historyClientNumber.getLastOrderClientNumber().getOperatorId().equalsIgnoreCase(
                    operatorSelected.getOperatorId())) {
                if (!TextUtils.isEmpty(historyClientNumber
                        .getLastOrderClientNumber().getClientNumber())) {
                    clientNumberInputView.setText(
                            historyClientNumber.getLastOrderClientNumber().getClientNumber()
                    );
                }
            }
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
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList());
        digitalProductChooserView.setLabelText(operatorSelected.getRule().getProductText());
        holderChooserProduct.addView(digitalProductChooserView);

        if (hasLastOrderHistoryData()) {
            if (!TextUtils.isEmpty(historyClientNumber.getLastOrderClientNumber().getOperatorId())) {
                for (Product product : operatorSelected.getProductList()) {
                    if (product.getProductId().equalsIgnoreCase(
                            historyClientNumber.getLastOrderClientNumber().getProductId())
                            ) {
                        digitalProductChooserView.renderUpdateDataSelected(product);
                    }
                }
            }
        }
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

    @NonNull
    private BaseDigitalRadioChooserView.ActionListener<Operator>
    getActionListenerRadioChooserOperator() {
        return new BaseDigitalRadioChooserView.ActionListener<Operator>() {

            @Override
            public void onUpdateDataDigitalRadioChooserSelectedRendered(Operator data) {
                operatorSelected = data;
                if (!data.getClientNumberList().isEmpty())
                    renderClientNumberInputForm();
            }
        };
    }

    @NonNull
    private ClientNumberInputView.ActionListener
    getActionListenerClientNumberInput() {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {

            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                if (operatorSelected.getRule().getProductViewStyle() == 99
                        || (operatorSelected.getProductList().size() == 1
                        && String.valueOf(operatorSelected.getDefaultProductId())
                        .equalsIgnoreCase(operatorSelected.getProductList().get(0).getProductId()))) {
                    productSelected = operatorSelected.getProductList().get(0);
                    renderAdditionalProductInfo();
                    renderPriceProductInfo();
                } else {
                    renderProductChooserOptions();
                }
            }

            @Override
            public void onClientNumberInputInvalid() {
                clientNumberInputView.disableImageOperator();
                clearHolder(holderChooserProduct);
                clearHolder(holderAdditionalInfoProduct);
                clearHolder(holderPriceInfoProduct);
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
                actionListener.onProductChooserStyle1Clicked(
                        data, operatorSelected != null ? operatorSelected.getRule().getProductText() : ""
                );
            }
        };
    }

    @NonNull
    private OnClickListener getButtonBuyListener(final CategoryData data) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
                boolean canBeCheckout = false;

                if (operatorSelected == null) {
                    actionListener.onCannotBeCheckoutProduct(
                            context.getString(R.string.message_error_digital_operator_not_selected)
                    );
                } else if (!operatorSelected.getClientNumberList().isEmpty()
                        && clientNumberInputView.getText().isEmpty()) {
                    actionListener.onCannotBeCheckoutProduct(
                            context.getString(
                                    R.string.message_error_digital_client_number_not_filled
                            ) + " " + operatorSelected.getClientNumberList().get(0).getText()
                                    .toLowerCase()
                    );
                } else if (productSelected == null) {
                    actionListener.onCannotBeCheckoutProduct(
                            context.getString(R.string.message_error_digital_product_not_selected)
                    );
                } else {
                    preCheckoutProduct.setProductId(productSelected.getProductId());
                    preCheckoutProduct.setOperatorId(operatorSelected.getOperatorId());
                    canBeCheckout = true;
                    if (productSelected.getPromo() != null) {
                        preCheckoutProduct.setPromo(true);
                    }
                }
                preCheckoutProduct.setCategoryId(data.getCategoryId());
                preCheckoutProduct.setCategoryName(data.getName());
                preCheckoutProduct.setClientNumber(clientNumberInputView.getText());
                preCheckoutProduct.setInstantCheckout(cbInstantCheckout.isChecked());

                if (canBeCheckout) actionListener.onButtonBuyClicked(preCheckoutProduct);
            }
        };
    }


    private boolean hasLastOrderHistoryData() {
        return historyClientNumber != null && historyClientNumber.getLastOrderClientNumber() != null;
    }

}
