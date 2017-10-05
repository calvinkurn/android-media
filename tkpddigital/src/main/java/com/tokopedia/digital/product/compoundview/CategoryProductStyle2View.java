package com.tokopedia.digital.product.compoundview;

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
        BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber>
        implements ProductAdditionalInfoView.ActionListener {

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
    @BindView(R2.id.layout_checkout)
    RelativeLayout layoutCheckout;
    @BindView(R2.id.tooltip_instant_checkout)
    ImageView tooltipInstantCheckout;

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
        productAdditionalInfoView.setActionListener(this);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_2;
    }

    @Override
    protected void onInitialDataRendered() {
        tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        clearHolder(holderRadioChooserOperator);
        renderInstantCheckoutOptions();
        if (!data.getOperatorStyle().equals(CategoryData.STYLE_PRODUCT_CATEGORY_99)) {
            renderOperatorChooserOptions();
        } else {
            for (Operator operator : data.getOperatorList()) {
                if (operator.getOperatorId().equalsIgnoreCase(data.getDefaultOperatorId())) {
                    operatorSelected = operator;
                    renderClientNumberInputForm();
                }
            }
        }
        btnBuyDigital.setOnClickListener(getButtonBuyListener());
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
    protected void onInstantCheckoutUnChecked() {
        btnBuyDigital.setText(context.getString(R.string.label_btn_buy_digital));
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

    private void renderOperatorChooserOptions() {
        clearHolder(holderRadioChooserOperator);
        clearHolder(holderClientNumber);
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
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
        clearHolder(holderChooserProduct);
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
        clientNumberInputView.setActionListener(getActionListenerClientNumberInput());
        clientNumberInputView.renderData(operatorSelected.getClientNumberList().get(0));
        holderClientNumber.addView(clientNumberInputView);
        clientNumberInputView.resetInputTyped();

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
        clearHolder(holderAdditionalInfoProduct);
        clearHolder(holderPriceInfoProduct);
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
                actionListener.onButtonContactPickerClicked();
            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                clientNumberInputView.enableImageOperator(operatorSelected.getImage());
                if (operatorSelected.getRule().getProductViewStyle() == 99) {
                    if (operatorSelected.getProductList().get(0) != null)
                        productSelected = operatorSelected.getProductList().get(0);
                    else
                        productSelected = new Product.Builder()
                                .productId(String.valueOf(operatorSelected.getDefaultProductId()))
                                .desc("")
                                .detail("")
                                .price("")
                                .pricePlain(0)
                                .info("")
                                .build();
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

            @Override
            public void onClientNumberHasFocus(String clientNumber) {
                actionListener.onClientNumberClicked(clientNumber, historyClientNumber.getRecentClientNumberList());
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
    private OnClickListener getButtonBuyListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventClickBeli(data.getName(), data.getName());
                actionListener.onButtonBuyClicked(generatePreCheckoutData());
            }
        };
    }

    private PreCheckoutProduct generatePreCheckoutData() {
        PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
        boolean canBeCheckout = false;
        if (operatorSelected == null) {
            preCheckoutProduct.setErrorCheckout(
                    context.getString(R.string.message_error_digital_operator_not_selected)
            );
        } else if (!operatorSelected.getClientNumberList().isEmpty()
                && clientNumberInputView.getText().isEmpty()) {
            preCheckoutProduct.setErrorCheckout(
                    context.getString(
                            R.string.message_error_digital_client_number_not_filled
                    ) + " " + operatorSelected.getClientNumberList().get(0).getText()
                            .toLowerCase()
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
