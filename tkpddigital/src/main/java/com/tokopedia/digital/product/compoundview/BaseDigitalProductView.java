package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
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

    protected C data;
    protected H historyClientNumber;

    protected BottomSheetView bottomSheetView;

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
        setBottomSheetDialog();
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

    private void setBottomSheetDialog() {
        bottomSheetView = new BottomSheetView(context);
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(context.getString(R.string.title_tooltip_instan_payment))
                .setBody(context.getString(R.string.body_tooltip_instan_payment))
                .setImg(R.drawable.ic_digital_instant_payment)
                .build());
    }

    @NonNull
    protected CompoundButton.OnCheckedChangeListener getInstantCheckoutChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) onInstantCheckoutChecked();
                else onInstantCheckoutUnChecked();

                if (data instanceof CategoryData)
                    UnifyTracking.eventCheckInstantSaldo(((CategoryData) data).getName(), ((CategoryData) data).getName(), isChecked);

            }
        };
    }

    protected abstract void onCreateView();

    protected abstract int getHolderLayoutId();

    protected abstract void onInitialDataRendered();

    protected abstract void onUpdateSelectedProductData();

    protected abstract void onUpdateSelectedOperatorData();

    protected abstract void onInstantCheckoutUnChecked();

    protected abstract void onInstantCheckoutChecked();

    public abstract void renderClientNumberFromContact(String clientNumber);

    public abstract boolean isInstantCheckoutChecked();

    public abstract String getClientNumber();

    protected abstract void onRestoreSelectedData(
            O operatorSelectedState, P productSelectedState,
            String clientNumberState, boolean isInstantCheckoutChecked
    );

    public abstract void clearFocusOnClientNumber();

    public interface ActionListener {
        void onButtonBuyClicked(PreCheckoutProduct preCheckoutProduct);

        void onProductChooserStyle1Clicked(
                List<Product> productListData, String operatorId, String titleChooser
        );

        void onProductChooserStyle2Clicked(
                List<Product> productListData, String titleChooser
        );

        void onProductChooserStyle3Clicked(
                List<Product> productListData, String operatorId, String titleChooser
        );

        void onOperatorChooserStyle3Clicked(
                List<Operator> operatorListData, String titleChooser
        );

        void onCannotBeCheckoutProduct(String messageError);

        void onButtonContactPickerClicked();

        void onProductDetailLinkClicked(String url);

        boolean isRecentInstantCheckoutUsed(String categoryId);

        void storeLastInstantCheckoutUsed(String categoryId, boolean checked);

        void onClientNumberClicked(String clientNumber, ClientNumber number, List<OrderClientNumber> numberList);

        void onClientNumberCleared(ClientNumber clientNumber, List<OrderClientNumber> recentClientNumberList);
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
        private boolean canBeCheckout;
        private String errorCheckout;

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

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public boolean isCanBeCheckout() {
            return canBeCheckout;
        }

        public void setCanBeCheckout(boolean canBeCheckout) {
            this.canBeCheckout = canBeCheckout;
        }

        public String getErrorCheckout() {
            return errorCheckout;
        }

        public void setErrorCheckout(String errorCheckout) {
            this.errorCheckout = errorCheckout;
        }
    }

}
