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
        tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        clearHolder(holderRadioChooserOperator);
        if (data.isInstantCheckout()) {
            cbInstantCheckout.setVisibility(VISIBLE);
        } else {
            cbInstantCheckout.setChecked(false);
            cbInstantCheckout.setVisibility(GONE);
        }
        holderRadioChooserOperator.addView(digitalOperatorRadioChooserView);
        digitalOperatorRadioChooserView.setActionListener(getActionListenerRadioChooserOperator());
        digitalOperatorRadioChooserView.renderInitDataList(data.getOperatorList());
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

    @Override
    protected void onHistoryClientNumberRendered() {
        if (historyClientNumber == null) return;
        if (!TextUtils.isEmpty(historyClientNumber.getLastOrderClientNumber().getOperatorId())) {
            digitalOperatorRadioChooserView.renderUpdateDataSelectedByOperatorId(
                    historyClientNumber.getLastOrderClientNumber().getOperatorId()
            );
            if (operatorSelected != null && !TextUtils.isEmpty(
                    historyClientNumber.getLastOrderClientNumber().getClientNumber()
            )) {
                clientNumberInputView.setText(
                        historyClientNumber.getLastOrderClientNumber().getClientNumber()
                );
            }
            List<String> lastClientNumberList = new ArrayList<>();
            for (OrderClientNumber data : historyClientNumber.getRecentClientNumberList()) {
                lastClientNumberList.add(data.getClientNumber());
            }
            this.clientNumberInputView.setAdapterAutoCompleteClientNumber(lastClientNumberList);
        }
    }

    @NonNull
    private BaseDigitalRadioChooserView.ActionListener<Operator>
    getActionListenerRadioChooserOperator() {
        return new BaseDigitalRadioChooserView.ActionListener<Operator>() {
            @Override
            public void onInitialDataDigitalRadioChooserSelectedRendered(Operator data) {
                operatorSelected = data;
                clientNumberInputView.setActionListener(getActionListenerClientNumberInput(data));
                clientNumberInputView.renderData(data.getClientNumberList().get(0));
                clearHolder(holderClientNumber);
                holderClientNumber.addView(clientNumberInputView);
            }

            @Override
            public void onUpdateDataDigitalRadioChooserSelectedRendered(Operator data) {
                operatorSelected = data;
                clientNumberInputView.setActionListener(getActionListenerClientNumberInput(data));
                clientNumberInputView.renderData(data.getClientNumberList().get(0));
                clearHolder(holderClientNumber);
                holderClientNumber.addView(clientNumberInputView);
                if (!clientNumberInputView.getText().equals("")) {
                    clientNumberInputView.enableImageOperator(data.getImage());
                    renderDropdownProduct(data);
                }
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

    @NonNull
    private ClientNumberInputView.ActionListener
    getActionListenerClientNumberInput(final Operator operator) {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {

            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                clientNumberInputView.enableImageOperator(operator.getImage());
                renderDropdownProduct(operator);
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
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser(
            final Operator operator
    ) {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onInitialDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;
                clearHolder(holderAdditionalInfoProduct);
                productAdditionalInfoView.renderData(data);
                holderAdditionalInfoProduct.addView(productAdditionalInfoView);

                clearHolder(holderPriceInfoProduct);
                if (operator.getRule().isShowPrice()) {
                    productPriceInfoView.renderData(productSelected);
                    holderPriceInfoProduct.addView(productPriceInfoView);
                }
            }

            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;

                clearHolder(holderAdditionalInfoProduct);
                productAdditionalInfoView.renderData(data);
                holderAdditionalInfoProduct.addView(productAdditionalInfoView);

                clearHolder(holderPriceInfoProduct);
                if (operator.getRule().isShowPrice()) {
                    productPriceInfoView.renderData(productSelected);
                    holderPriceInfoProduct.addView(productPriceInfoView);
                }
            }

            @Override
            public void onDigitalChooserClicked(List<Product> data) {
                actionListener.onProductChooserStyle1Clicked(data);
            }
        };
    }

    private void renderDropdownProduct(Operator operator) {
        if (operator.getRule().getProductViewStyle() == 99) {
            clearHolder(holderChooserProduct);
            Product product = new Product();
            product.setProductId(String.valueOf(operator.getDefaultProductId()));
            productSelected = product;
        } else {
            digitalProductChooserView.setActionListener(
                    getActionListenerProductChooser(operator)
            );
            digitalProductChooserView.renderInitDataList(
                    operator.getProductList()
            );
            digitalProductChooserView.setLabelText(
                    operator.getRule().getProductText()
            );
            clearHolder(holderChooserProduct);
            holderChooserProduct.addView(digitalProductChooserView);
        }
    }


}
