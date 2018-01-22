package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.compoundview.ProductAdditionalInfoView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.widget.view.compoundview.WidgetProductChooserView2;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Rizky on 22/01/18.
 */

public class CategoryProductStyle99View extends
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

    private ClientNumberInputView clientNumberInputView;
    private WidgetProductChooserView2 widgetProductChooserView;
    private DigitalProductChooserView digitalProductChooserView;
    private ProductAdditionalInfoView productAdditionalInfoView;
    private ProductPriceInfoView productPriceInfoView;

    public CategoryProductStyle99View(Context context) {
        super(context);
    }

    public CategoryProductStyle99View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryProductStyle99View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onProductLinkClicked(String url) {

    }

    @Override
    protected void onCreateView() {
        clientNumberInputView = new ClientNumberInputView(context);
        digitalProductChooserView = new DigitalProductChooserView(context);
        widgetProductChooserView = new WidgetProductChooserView2(context);
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
        if (source != WIDGET) {
            tvTitle.setText(TextUtils.isEmpty(data.getTitleText()) ? "" : data.getTitleText());
        } else {
            tvTitle.setVisibility(GONE);
        }
        for (Operator operator : data.getOperatorList()) {
            if (operator.getOperatorId().equals(data.getDefaultOperatorId())) {
                operatorSelected = operator;
                if (operatorSelected.getRule().getProductViewStyle() == 99) {
                    renderDefaultProductSelected();
                } else {
                    showProducts();
                }
            }
        }
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
    protected void onRestoreSelectedData(Operator operatorSelectedState, Product productSelectedState, String clientNumberState, boolean isInstantCheckoutChecked) {
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

    private WidgetProductChooserView2.ProductChoserListener getProductChooserListener() {
        return new WidgetProductChooserView2.ProductChoserListener() {
            @Override
            public void initDataView(Product product) {
                productSelected = product;
                renderPriceProductInfo();
            }

            @Override
            public void trackingProduct() {
                if (productSelected != null) {
                    UnifyTracking.eventSelectProductWidget(data.getName(),
                            productSelected.getDesc());
                }
            }
        };
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
            public void onDigitalChooserClicked(List<Product> products) {
                actionListener.onProductChooserClicked(
                        products, operatorSelected.getOperatorId(),
                        operatorSelected != null ? operatorSelected.getRule().getProductText() : ""
                );
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
        renderPriceProductInfo();
    }

    private void showProducts() {
        if (source == NATIVE) {
            renderProductChooserOptions();
        } else {
            renderProductChooserOptionsWidget();
        }
    }

    private void renderProductChooserOptionsWidget() {
        clearHolder(holderChooserProduct);
        widgetProductChooserView.setListener(getProductChooserListener());
        widgetProductChooserView.setTitleProduct(operatorSelected.getRule().getProductText());
        widgetProductChooserView.renderDataView(operatorSelected.getProductList(),
                operatorSelected.getRule().isShowPrice(),
                String.valueOf(operatorSelected.getDefaultProductId())
        );
        holderChooserProduct.addView(widgetProductChooserView);

        if (hasLastOrderHistoryData() && operatorSelected != null
                && operatorSelected.getOperatorId().equalsIgnoreCase(
                historyClientNumber.getLastOrderClientNumber().getOperatorId()
        )) {
            for (Product product : operatorSelected.getProductList()) {
                if (product.getProductId().equalsIgnoreCase(
                        historyClientNumber.getLastOrderClientNumber().getProductId()
                )) {
                    widgetProductChooserView.updateProduct(
                            operatorSelected.getProductList(),
                            product.getProductId()
                    );
                    break;
                }
            }
        }
    }

    private void renderProductChooserOptions() {
        clearHolder(holderChooserProduct);
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

    private void renderPriceProductInfo() {
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

}
