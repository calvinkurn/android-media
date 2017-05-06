package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public abstract class BaseDigitalProductView<T> extends RelativeLayout {

    protected ActionListener actionListener;

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
        LayoutInflater.from(context).inflate(getHolderLayoutId(), this, true);
        ButterKnife.bind(this);
        initialViewListener();
    }

    protected abstract void initialViewListener();

    protected abstract int getHolderLayoutId();

    public abstract void renderData(T data);

    public void setClientNumberInputViewCallback(ClientNumberInputView clientNumberInputView) {
        if (clientNumberInputView != null) {
            clientNumberInputView.getAutoCompleteTextView().setOnFocusChangeListener(getFocusChangeListener());
        }
    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //need more investigate to scroll page bottom banner and use customer activity
                    //setParentToScroolToTop();
                }
            }
        };
    }

    public void setClientNumberInputViewTouchCallback(ClientNumberInputView clientNumberInputView) {
        if (clientNumberInputView != null) {
            clientNumberInputView.getAutoCompleteTextView().setOnTouchListener(getTouchListener(clientNumberInputView));
        }
    }

    private View.OnTouchListener getTouchListener(final ClientNumberInputView clientNumberInputView) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clientNumberInputView.getAutoCompleteTextView().setFocusable(true);
                        clientNumberInputView.getAutoCompleteTextView().requestFocus();
                        break;
                    case MotionEvent.ACTION_UP:
                        //setParentToScroolToTop();
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
    }

    public interface ActionListener {
        void onButtonBuyClicked(PreCheckoutProduct preCheckoutProduct);
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
