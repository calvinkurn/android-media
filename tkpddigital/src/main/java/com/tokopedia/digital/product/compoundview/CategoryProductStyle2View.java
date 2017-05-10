package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;

import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle2View extends BaseDigitalProductView<CategoryData> {

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
    protected void initialViewListener(Context context) {
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
    public void renderData(CategoryData data) {
        if (holderRadioChooserOperator.getChildAt(0) != null) {
            holderRadioChooserOperator.removeAllViews();
        }
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

    @NonNull
    private BaseDigitalRadioChooserView.ActionListener<Operator> getActionListenerRadioChooserOperator() {
        return new BaseDigitalRadioChooserView.ActionListener<Operator>() {
            @Override
            public void onInitialDataDigitalRadioChooserSelectedRendered(Operator data) {
                operatorSelected = data;
                clientNumberInputView.setActionListener(getActionListenerClientNumberInput(data));
                clientNumberInputView.renderData(data.getClientNumberList().get(0));
                if (holderClientNumber.getChildAt(0) != null)
                    holderClientNumber.removeAllViews();
                holderClientNumber.addView(clientNumberInputView);
            }

            @Override
            public void onUpdateDataDigitalRadioChooserSelectedRendered(Operator data) {
                operatorSelected = data;
                clientNumberInputView.setActionListener(getActionListenerClientNumberInput(data));
                clientNumberInputView.renderData(data.getClientNumberList().get(0));
                if (holderClientNumber.getChildAt(0) != null)
                    holderClientNumber.removeAllViews();
                holderClientNumber.addView(clientNumberInputView);
                if (!clientNumberInputView.getText().equals("")) {
                    clientNumberInputView.enableImageOperator(data.getImage());
                    renderDropdownProduct(data);
                }
            }
        };
    }

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
    private ClientNumberInputView.ActionListener getActionListenerClientNumberInput(final Operator operator) {
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
                if (holderChooserProduct.getChildAt(0) != null) {
                    holderChooserProduct.removeAllViews();
                }
                if (holderAdditionalInfoProduct.getChildCount() > 0) {
                    holderAdditionalInfoProduct.removeAllViews();
                }
                if (holderPriceInfoProduct.getChildCount() > 0) {
                    holderPriceInfoProduct.removeAllViews();
                }
            }
        };
    }



    @Override
    public void renderUpdateProductSelected(Product product) {
        digitalProductChooserView.renderUpdateDataSelected(product);
    }

    @Override
    public void renderUpdateOperatorSelected(Operator operator) {
        digitalOperatorRadioChooserView.renderUpdateDataSelected(operator);
    }

    @Override
    public void renderClientNumberFromContact(String clientNumber) {

    }

    @Override
    public Operator getSelectedOperator() {
        return operatorSelected;
    }

    @Override
    public Product getSelectedProduct() {
        return productSelected;
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
    public void renderStateDataSelected(
            String clientNumberState, Operator operatorSelectedState,
            Product productSelectedState, boolean isInstantCheckoutChecked
    ) {
        //TODO Angga
    }

    @Override
    protected void onHistoryClientNumberRendered() {
        //TODO Angga
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser(
            final Operator operator
    ) {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onInitialDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;

                if (holderAdditionalInfoProduct.getChildAt(0) != null)
                    holderAdditionalInfoProduct.removeAllViews();
                productAdditionalInfoView.renderData(data);
                holderAdditionalInfoProduct.addView(productAdditionalInfoView);

                if (operator.getRule().isShowPrice()) {
                    productPriceInfoView.renderData(productSelected);
                    if (holderPriceInfoProduct.getChildAt(0) != null)
                        holderPriceInfoProduct.removeAllViews();
                    holderPriceInfoProduct.addView(productPriceInfoView);
                } else {
                    if (holderPriceInfoProduct.getChildAt(0) != null)
                        holderPriceInfoProduct.removeAllViews();
                }
            }

            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;

                if (holderAdditionalInfoProduct.getChildAt(0) != null)
                    holderAdditionalInfoProduct.removeAllViews();
                productAdditionalInfoView.renderData(data);
                holderAdditionalInfoProduct.addView(productAdditionalInfoView);

                if (operator.getRule().isShowPrice()) {
                    productPriceInfoView.renderData(productSelected);
                    if (holderPriceInfoProduct.getChildAt(0) != null)
                        holderPriceInfoProduct.removeAllViews();
                    holderPriceInfoProduct.addView(productPriceInfoView);
                } else {
                    if (holderPriceInfoProduct.getChildAt(0) != null)
                        holderPriceInfoProduct.removeAllViews();
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
            if (holderChooserProduct.getChildAt(0) != null) {
                holderChooserProduct.removeAllViews();
            }
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
            if (holderChooserProduct.getChildAt(0) != null) {
                holderChooserProduct.removeAllViews();
            }
            holderChooserProduct.addView(digitalProductChooserView);
        }
    }


}
