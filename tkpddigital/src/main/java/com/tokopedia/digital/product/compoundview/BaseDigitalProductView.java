package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public abstract class BaseDigitalProductView<T> extends RelativeLayout {

    protected ActionListener actionListener;
    protected Context context;
    protected List<OrderClientNumber> recentClientNumberList = new ArrayList<>();
    protected OrderClientNumber lastOrderClientNumber;

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public BaseDigitalProductView(Context context) {
        super(context);
        this.context = context;
        initialView(context, null, 0);
    }

    public BaseDigitalProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialView(context, attrs, 0);
    }

    public BaseDigitalProductView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialView(context, attrs, defStyleAttr);
    }

    private void initialView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(getHolderLayoutId(), this, true);
        ButterKnife.bind(this);
        initialViewListener(context);
    }

    protected abstract void initialViewListener(Context context);

    protected abstract int getHolderLayoutId();

    public abstract void renderData(T data);

    public abstract void renderUpdateProductSelected(Product product);

    public abstract void renderUpdateOperatorSelected(Operator operator);

    public abstract void renderClientNumberFromContact(String clientNumber);

    public abstract Operator getSelectedOperator();

    public abstract Product getSelectedProduct();

    public abstract boolean isInstantCheckoutChecked();

    public abstract String getClientNumber();

    public abstract void renderStateDataSelected(String clientNumberState,
                                                 Operator operatorSelectedState,
                                                 Product productSelectedState,
                                                 boolean isInstantCheckoutChecked);

    public abstract void renderDataRecentClientNumber(
            List<OrderClientNumber> recentClientNumberList, OrderClientNumber lastOrderClientNumber
    );

    public interface ActionListener {
        void onButtonBuyClicked(PreCheckoutProduct preCheckoutProduct);

        void onProductChooserStyle1Clicked(List<Product> productListData);

        void onProductChooserStyle2Clicked(List<Product> productListData);

        void onOperatorChooserStyle3Clicked(List<Operator> operatorListData);

        void onProductChooserStyle3Clicked(List<Product> productListData);

        void onCannotBeCheckoutProduct(String messageError);

        void onButtonContactPickerClicked();

    }

    public static class PreCheckoutProduct {
        private String clientNumber;
        private String categoryId;
        private String operatorId;
        private String productId;
        private String categoryName;
        private boolean instantCheckout;
        private boolean promo;

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

        public void setPromo(boolean promo) {
            this.promo = promo;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

}
