package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public abstract class BaseDigitalProductView<C, O, P, H> extends RelativeLayout {

    protected ActionListener actionListener;
    protected Context context;

    protected P productSelected;
    protected O operatorSelected;
    protected H historyClientNumber;
    protected C data;

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public BaseDigitalProductView(Context context) {
        super(context);
        initialView(context, null, 0);
    }

    public BaseDigitalProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView(context, attrs, 0);
    }

    public BaseDigitalProductView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialView(context, attrs, defStyleAttr);
    }

    private void initialView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        LayoutInflater.from(context).inflate(getHolderLayoutId(), this, true);
        ButterKnife.bind(this);
        onCreateView();
    }

    public void renderData(C data, H historyClientNumber) {
        this.data = data;
        this.historyClientNumber = historyClientNumber;
        onInitialDataRendered();
    }

    public void renderUpdateProductSelected(P product) {
        this.productSelected = product;
        onUpdateSelectedProductData();
    }

    public void renderUpdateOperatorSelected(O operator) {
        this.operatorSelected = operator;
        onUpdateSelectedOperatorData();
    }

    public O getSelectedOperator() {
        return operatorSelected;
    }

    public P getSelectedProduct() {
        return productSelected;
    }

    public void restoreStateData(C categoryDataState,
                                 H historyClientNumberState,
                                 O operatorSelectedState,
                                 P productSelectedState, String clientNumberState,
                                 boolean isInstantCheckoutChecked) {
        if (data == null) renderData(categoryDataState, historyClientNumberState);
        this.operatorSelected = operatorSelectedState;
        this.productSelected = productSelectedState;
        onRestoreSelectedData(
                operatorSelectedState, productSelectedState,
                clientNumberState, isInstantCheckoutChecked
        );
    }

    protected void clearHolder(LinearLayout holderView) {
        if (holderView.getChildCount() > 0) {
            holderView.removeAllViews();
        }
    }


    protected abstract void onCreateView();

    protected abstract int getHolderLayoutId();

    protected abstract void onInitialDataRendered();

    protected abstract void onUpdateSelectedProductData();

    protected abstract void onUpdateSelectedOperatorData();

    public abstract void renderClientNumberFromContact(String clientNumber);

    public abstract boolean isInstantCheckoutChecked();

    public abstract String getClientNumber();

    protected abstract void onRestoreSelectedData(
            O operatorSelectedState, P productSelectedState,
            String clientNumberState, boolean isInstantCheckoutChecked
    );

    public interface ActionListener {
        void onButtonBuyClicked(PreCheckoutProduct preCheckoutProduct);

        void onProductChooserStyle1Clicked(
                List<Product> productListData, String titleChooser
        );

        void onProductChooserStyle2Clicked(
                List<Product> productListData, String titleChooser
        );

        void onOperatorChooserStyle3Clicked(
                List<Operator> operatorListData, String titleChooser
        );

        void onProductChooserStyle3Clicked(
                List<Product> productListData, String titleChooser
        );

        void onCannotBeCheckoutProduct(String messageError);

        void onButtonContactPickerClicked();

        void onProductDetailLinkClicked(String url);

    }

    public static class PreCheckoutProduct {
        private String clientNumber;
        private String categoryId;
        private String operatorId;
        private String productId;
        private String categoryName;
        private boolean instantCheckout;
        private boolean promo;
        private String voucherCodeCopied;

        public String getClientNumber() {
            return clientNumber;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public String getProductId() {
            return productId;
        }

        public boolean isInstantCheckout() {
            return instantCheckout;
        }

        public boolean isPromo() {
            return promo;
        }

        public void setClientNumber(String clientNumber) {
            this.clientNumber = clientNumber;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setInstantCheckout(boolean instantCheckout) {
            this.instantCheckout = instantCheckout;
        }

        public String getVoucherCodeCopied() {
            return voucherCodeCopied == null ? "" : voucherCodeCopied;
        }

        public void setVoucherCodeCopied(String voucherCodeCopied) {
            this.voucherCodeCopied = voucherCodeCopied;
        }

        public void setPromo(boolean promo) {
            this.promo = promo;
        }

        public String getCategoryName() {
            return categoryName;
        }

        void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

}
