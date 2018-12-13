package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.OrderClientNumber;

import java.util.List;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public abstract class BaseDigitalProductView<C, O, P, H> extends RelativeLayout {

    protected static final int SINGLE_PRODUCT = 99;

    public static final int WIDGET = 0;
    public static final int NATIVE = 1;

    protected ActionListener actionListener;
    protected Context context;

    protected P productSelected;
    protected O operatorSelected;

    protected C data;
    protected H historyClientNumber;

    protected BottomSheetView bottomSheetView;
    protected int source = 1;

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
        return (buttonView, isChecked) -> {
            if (isChecked) onInstantCheckoutChecked();
            else onInstantCheckoutUnChecked();

            if (data instanceof CategoryData)
                UnifyTracking.eventCheckInstantSaldo(getContext(), ((CategoryData) data).getName(), ((CategoryData) data).getName(), isChecked);
        };
    }

    protected abstract void onCreateView();

    protected abstract int getHolderLayoutId();

    protected abstract void onInitialDataRendered();

    protected abstract void onUpdateSelectedOperatorData();

    protected abstract void onUpdateSelectedProductData();

    protected abstract void onInstantCheckoutUnChecked();

    protected abstract void onInstantCheckoutChecked();

    public abstract void renderClientNumber(String clientNumber);

    public abstract boolean isInstantCheckoutChecked();

    public abstract String getClientNumber();

    protected abstract void onRestoreSelectedData(
            O operatorSelectedState, P productSelectedState,
            String clientNumberState, boolean isInstantCheckoutChecked
    );

    public abstract void clearFocusOnClientNumber();

    public void setSource(int source) {
        this.source = source;
    }

    public interface ActionListener {
        void onButtonBuyClicked(PreCheckoutProduct preCheckoutProduct, boolean isInstantCheckoutChecked);

        void onProductChooserClicked(
                List<Product> productListData, String operatorId, String titleChooser
        );

        void onOperatorChooserStyle3Clicked(List<Operator> operatorListData, String titleChooser);

        void onButtonContactPickerClicked();

        void onProductDetailLinkClicked(String url);

        boolean isRecentInstantCheckoutUsed(String categoryId);

        void storeLastInstantCheckoutUsed(String categoryId, boolean checked);

        void onClientNumberClicked(String clientNumber, ClientNumber number, List<OrderClientNumber> numberList);

        void onClientNumberCleared(ClientNumber clientNumber, List<OrderClientNumber> recentClientNumberList);

        void onItemAutocompletedSelected(OrderClientNumber orderClientNumber);

        void onOperatorSelected(String categoryName, String operatorName);

        void onProductSelected(String categoryName, String productDesc);
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
