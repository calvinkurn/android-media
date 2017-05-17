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
import com.tokopedia.digital.product.model.Product;

import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle3View extends
        BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> {

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

    private DigitalOperatorChooserView digitalOperatorChooserView;
    private ClientNumberInputView clientNumberInputView;
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
        digitalOperatorChooserView = new DigitalOperatorChooserView(context);
        clientNumberInputView = new ClientNumberInputView(context);
        digitalProductChooserView = new DigitalProductChooserView(context);
        productAdditionalInfoView = new ProductAdditionalInfoView(context);
        productPriceInfoView = new ProductPriceInfoView(context);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_3;
    }

    @Override
    protected void onInitialDataRendered() {
        tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        renderInstantCheckoutOptions();
        renderOperatorChooserOptions();
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
    public void renderClientNumberFromContact(String clientNumber) {

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

    private void renderInstantCheckoutOptions() {
        if (data.isInstantCheckout()) {
            cbInstantCheckout.setVisibility(VISIBLE);
        } else {
            cbInstantCheckout.setChecked(false);
            cbInstantCheckout.setVisibility(GONE);
        }
    }

    private void renderOperatorChooserOptions() {
        clearHolder(holderChooserOperator);
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

    private void renderClientNumberInputForm() {
        clearHolder(holderClientNumber);
        clientNumberInputView.setActionListener(getActionListenerClientNumberInputView());
        clientNumberInputView.renderData(operatorSelected.getClientNumberList().get(0));
        holderClientNumber.addView(clientNumberInputView);
        clientNumberInputView.enableImageOperator(operatorSelected.getImage());

        if (hasLastOrderHistoryData()) {
            if (operatorSelected != null && operatorSelected.getOperatorId().equalsIgnoreCase(
                    historyClientNumber.getLastOrderClientNumber().getOperatorId())
                    && !historyClientNumber.getLastOrderClientNumber().getClientNumber().isEmpty()
                    ) {
                clientNumberInputView.setText(
                        historyClientNumber.getLastOrderClientNumber().getClientNumber()
                );
            }
        }
    }

    private void renderProductChooserOptions() {
        clearHolder(holderChooserProduct);
        digitalProductChooserView.setLabelText(operatorSelected.getRule().getProductText());
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList());
        holderChooserProduct.addView(digitalProductChooserView);

        if (hasLastOrderHistoryData() && operatorSelected != null) {
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
    private BaseDigitalChooserView.ActionListener<Operator> getActionListenerOperatorChooser() {
        return new BaseDigitalChooserView.ActionListener<Operator>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Operator data) {
                operatorSelected = data;

                if (!data.getClientNumberList().isEmpty()) {
                    renderClientNumberInputForm();
                } else {
                    renderProductChooserOptions();
                }
            }

            @Override
            public void onDigitalChooserClicked(List<Operator> data) {
                actionListener.onOperatorChooserStyle3Clicked(data);
            }
        };
    }

    @NonNull
    private ClientNumberInputView.ActionListener getActionListenerClientNumberInputView() {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {

            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                if (operatorSelected.getProductList().size() == 1
                        && String.valueOf(operatorSelected.getDefaultProductId())
                        .equalsIgnoreCase(operatorSelected.getProductList().get(0).getProductId())) {
                    productSelected = operatorSelected.getProductList().get(0);
                    renderAdditionalProductInfo();
                    renderPriceProductInfo();
                } else {
                    renderProductChooserOptions();
                }

            }

            @Override
            public void onClientNumberInputInvalid() {
                clearHolder(holderChooserProduct);
                clearHolder(holderAdditionalInfoProduct);
                clearHolder(holderPriceInfoProduct);
                clientNumberInputView.disableImageOperator();
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
                actionListener.onProductChooserStyle3Clicked(data);
            }
        };
    }

    @NonNull
    private OnClickListener getButtonBuyClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
                boolean canBeCheckout = false;

                if (productSelected == null) {
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
