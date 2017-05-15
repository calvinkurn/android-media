package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
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

    @BindView(R2.id.tv_title_category) //TODO Nabilla, tolong XML nya dirapihin
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
        if (holderChooserOperator.getChildAt(0) != null) {
            holderChooserOperator.removeAllViews();
        }
        if (data.isInstantCheckout()) {
            cbInstantCheckout.setVisibility(VISIBLE);
        } else {
            cbInstantCheckout.setChecked(false);
            cbInstantCheckout.setVisibility(GONE);
        }
        holderChooserOperator.addView(digitalOperatorChooserView);
        digitalOperatorChooserView.setActionListener(getActionListenerOperatorChooser());
        digitalOperatorChooserView.renderInitDataList(data.getOperatorList());
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Operator> getActionListenerOperatorChooser() {
        return new BaseDigitalChooserView.ActionListener<Operator>() {
            @Override
            public void onInitialDataDigitalChooserSelectedRendered(Operator data) {
                operatorSelected = data;
                if (holderClientNumber.getChildCount() > 0) holderClientNumber.removeAllViews();
                if (holderChooserProduct.getChildCount() > 0) holderChooserProduct.removeAllViews();
                if (!data.getClientNumberList().isEmpty()) {
                    clientNumberInputView.setActionListener(getActionListenerClientNumberInputView(data));
                    clientNumberInputView.renderData(operatorSelected.getClientNumberList().get(0));
                    holderClientNumber.addView(clientNumberInputView);
                } else {
                    digitalProductChooserView.setActionListener(getActionListenerProductChooser(data));
                    digitalProductChooserView.renderInitDataList(data.getProductList());
                    holderChooserProduct.addView(digitalOperatorChooserView);
                }
            }

            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Operator data) {
                operatorSelected = data;
                if (holderClientNumber.getChildCount() > 0) holderClientNumber.removeAllViews();
                if (holderChooserProduct.getChildCount() > 0) holderChooserProduct.removeAllViews();
                if (!data.getClientNumberList().isEmpty()) {
                    clientNumberInputView.setActionListener(getActionListenerClientNumberInputView(data));
                    clientNumberInputView.renderData(operatorSelected.getClientNumberList().get(0));
                    holderClientNumber.addView(clientNumberInputView);
                } else {
                    digitalProductChooserView.setActionListener(getActionListenerProductChooser(data));
                    digitalProductChooserView.renderInitDataList(data.getProductList());
                    holderChooserProduct.addView(digitalOperatorChooserView);
                }
            }

            @Override
            public void onDigitalChooserClicked(List<Operator> data) {
                actionListener.onOperatorChooserStyle3Clicked(data);
            }
        };
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser(final Operator operator) {
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
                actionListener.onProductChooserStyle3Clicked(data);
            }
        };
    }

    @NonNull
    private ClientNumberInputView.ActionListener getActionListenerClientNumberInputView(final Operator data) {
        return new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {

            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {
                if (holderChooserProduct.getChildCount() > 0) holderChooserProduct.removeAllViews();
                if (data.getProductList().size() == 1
                        && String.valueOf(data.getDefaultProductId()).equalsIgnoreCase(data.getProductList().get(0).getProductId())) {
                    productSelected = data.getProductList().get(0);
                    clientNumberInputView.enableImageOperator(data.getImage());
                } else if (data.getProductList().isEmpty()) {
                    Product product = new Product();
                    product.setProductId(String.valueOf(data.getDefaultProductId()));
                    productSelected = product;
                } else {
                    digitalProductChooserView.setActionListener(getActionListenerProductChooser(data));
                    digitalProductChooserView.renderInitDataList(data.getProductList());
                    holderChooserProduct.addView(digitalProductChooserView);
                }
                if (productSelected != null) {
                    if (data.getRule().isShowPrice()) {
                        productPriceInfoView.renderData(productSelected);
                        if (holderPriceInfoProduct.getChildAt(0) != null)
                            holderPriceInfoProduct.removeAllViews();
                        holderPriceInfoProduct.addView(productPriceInfoView);
                    } else {
                        if (holderPriceInfoProduct.getChildAt(0) != null)
                            holderPriceInfoProduct.removeAllViews();
                    }
                }

            }

            @Override
            public void onClientNumberInputInvalid() {
                clientNumberInputView.disableImageOperator();
            }
        };
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

    }

    @Override
    protected void onHistoryClientNumberRendered() {
        //TODO Angga
    }


}
