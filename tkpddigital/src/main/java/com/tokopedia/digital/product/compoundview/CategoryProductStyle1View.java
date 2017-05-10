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
import com.tokopedia.digital.product.model.ClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle1View extends BaseDigitalProductView<CategoryData> {

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

    private Product productSelected;
    private Operator operatorSelected;

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
    protected void initialViewListener(Context context) {
        clientNumberInputView = new ClientNumberInputView(context);
        digitalProductChooserView = new DigitalProductChooserView(context);
        productAdditionalInfoView = new ProductAdditionalInfoView(context);
        productPriceInfoView = new ProductPriceInfoView(context);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_1;
    }

    @Override
    public void renderData(final CategoryData data) {
        final ClientNumber clientNumber = data.getClientNumberList().get(0);
        if (holderClientNumber.getChildAt(0) != null) {
            holderClientNumber.removeAllViews();
        }
        holderClientNumber.addView(clientNumberInputView);

        clientNumberInputView.setActionListener(getActionListenerClientNumberInput(data));
        clientNumberInputView.renderData(clientNumber);

        if (data.isInstantCheckout()) {
            cbInstantCheckout.setVisibility(VISIBLE);
        } else {
            cbInstantCheckout.setChecked(false);
            cbInstantCheckout.setVisibility(GONE);
        }
        btnBuyDigital.setOnClickListener(getButtonBuyClickListener(data));
    }

    @Override
    public void renderUpdateProductSelected(Product product) {
        this.digitalProductChooserView.renderUpdateDataSelected(product);
    }

    @Override
    public void renderUpdateOperatorSelected(Operator operator) {

    }

    @Override
    public void renderClientNumberFromContact(String clientNumber) {
        this.clientNumberInputView.setText(clientNumber);
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
        if (!TextUtils.isEmpty(clientNumberState)) {
            clientNumberInputView.setText(clientNumberState);
            if (operatorSelectedState != null && productSelectedState != null) {
                digitalProductChooserView.renderUpdateDataSelected(productSelected);
            }
        }
        if (cbInstantCheckout.getVisibility() == VISIBLE) {
            cbInstantCheckout.setChecked(isInstantCheckoutChecked);
        }
    }

    @Override
    public void renderDataRecentClientNumber(List<OrderClientNumber> recentClientNumberList,
                                             OrderClientNumber lastOrderClientNumber) {
        this.recentClientNumberList = recentClientNumberList;
        this.lastOrderClientNumber = lastOrderClientNumber;
        if (recentClientNumberList != null) {
            List<String> lastClientNumberList = new ArrayList<>();
            for (OrderClientNumber data : recentClientNumberList) {
                lastClientNumberList.add(data.getClientNumber());
            }
            this.clientNumberInputView.setAdapterAutoCompleteClientNumber(lastClientNumberList);
        }
        if (lastOrderClientNumber != null)
            this.clientNumberInputView.setText(lastOrderClientNumber.getClientNumber());
        if (operatorSelected != null && lastOrderClientNumber != null
                && TextUtils.isEmpty(lastOrderClientNumber.getProductId())) {
            String productId = lastOrderClientNumber.getProductId();
            for (Product product : operatorSelected.getProductList()) {
                if (product.getProductId().equalsIgnoreCase(productId)) {
                    renderUpdateProductSelected(product);
                    return;
                }
            }
        }

    }

    @NonNull
    private OnClickListener getButtonBuyClickListener(final CategoryData data) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                PreCheckoutProduct preCheckoutProduct = new PreCheckoutProduct();
                boolean canBeCheckout = false;

                if (productSelected == null) {
                    actionListener.onCannotBeCheckoutProduct(
                            context.getString(R.string.message_error_digital_product_not_selected)
                    );
                } else if (operatorSelected == null) {
                    actionListener.onCannotBeCheckoutProduct(
                            context.getString(R.string.message_error_digital_operator_not_selected)
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
    private ClientNumberInputView.ActionListener getActionListenerClientNumberInput(
            final CategoryData data
    ) {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {
                actionListener.onButtonContactPickerClicked();
            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                for (Operator operator : data.getOperatorList()) {
                    operatorSelected = operator;
                    for (String prefix : operator.getPrefixList()) {
                        if (tempClientNumber.startsWith(prefix)) {
                            clientNumberInputView.enableImageOperator(operator.getImage());
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
                            holderChooserProduct.addView(
                                    digitalProductChooserView
                            );
                            return;
                        } else {
                            clientNumberInputView.disableImageOperator();
                            if (holderChooserProduct.getChildAt(0) != null) {
                                holderChooserProduct.removeAllViews();
                            }
                            productSelected = null;
                            if (holderAdditionalInfoProduct.getChildCount() > 0) {
                                holderAdditionalInfoProduct.removeAllViews();
                            }
                            if (holderPriceInfoProduct.getChildCount() > 0) {
                                holderPriceInfoProduct.removeAllViews();
                            }
                        }
                    }
                }
            }

            @Override
            public void onClientNumberInputInvalid() {
                operatorSelected = null;
                productSelected = null;
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


}
