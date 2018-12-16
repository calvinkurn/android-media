package com.tokopedia.digital.tokocash;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.model.CategoryData;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class TopUpTokoCashView extends FrameLayout {

    private DigitalProductChooserView digitalProductChooserView;
    private CheckBox instantCheckoutCheckbox;
    private TextView btnTopUp;

    private ActionListener listener;
    private Product productSelected;
    private CategoryData categoryData;
    private Operator operatorSelected;
    private TextView title;

    public TopUpTokoCashView(Context context) {
        super(context);
        init(context);
    }

    public TopUpTokoCashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopUpTokoCashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_tokocash_topup, this, true);
        digitalProductChooserView = view.findViewById(R.id.digital_product_chooser_view);
        instantCheckoutCheckbox = view.findViewById(R.id.cb_instant_checkout);
        btnTopUp = view.findViewById(R.id.btn_topup);
        title = view.findViewById(R.id.title_tokocash);

        btnTopUp.setOnClickListener(getClickListenerTopUp());
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public void renderDataTopUp(CategoryData categoryData, Operator operatorSelected) {
        renderDataTopUp(categoryData, operatorSelected, null);
    }

    public void renderDataTopUp(CategoryData categoryData, Operator operatorSelected, Product selectedProduct) {
        if (selectedProduct != null) {
            this.productSelected = selectedProduct;
        }
        this.categoryData = categoryData;
        this.operatorSelected = operatorSelected;
        digitalProductChooserView.setActionListener(getActionListener(operatorSelected.getRule().getProductText()));
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList(),
                operatorSelected.getDefaultProductId());
        digitalProductChooserView.setLabelText(operatorSelected.getRule().getProductText().equals("") ?
                getContext().getString(R.string.title_topup_tokocash) : operatorSelected.getRule().getProductText());
        title.setText(categoryData.getTitleText());
        instantCheckoutCheckbox.setVisibility(categoryData.isInstantCheckout() ? VISIBLE : GONE);
        btnTopUp.setText(operatorSelected.getRule().getButtonText().equals("") ?
                getContext().getString(R.string.label_btn_buy_digital) : operatorSelected.getRule().getButtonText());
    }

    public void renderUpdateDataSelected(Product data) {
        digitalProductChooserView.renderUpdateDataSelected(data);
    }

    private BaseDigitalChooserView.ActionListener<Product> getActionListener(final String productText) {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;
            }

            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data, boolean resetClientNumber) {
                productSelected = data;
            }

            @Override
            public void onDigitalChooserClicked(List<Product> data) {
                UnifyTracking.eventSelectProductOnNativePage(getContext(),categoryData.getName(), categoryData.getName());
                listener.onDigitalChooserClicked(data, productText);
            }

            @Override
            public void tracking() {

            }
        };
    }

    private OnClickListener getClickListenerTopUp() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                String isInstant = instantCheckoutCheckbox.isChecked() ? "instant" : "no instant";
                UnifyTracking.eventClickBuyOnNative(getContext(),categoryData.getName(), isInstant);
                listener.onProcessAddToCart(generatePreCheckoutData());
            }
        };
    }

    private BaseDigitalProductView.PreCheckoutProduct generatePreCheckoutData() {
        BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct = new BaseDigitalProductView.PreCheckoutProduct();
        preCheckoutProduct.setProductId(productSelected.getProductId());
        preCheckoutProduct.setOperatorId(operatorSelected.getOperatorId());
        if (productSelected.getPromo() != null) {
            preCheckoutProduct.setPromo(true);
        }
        preCheckoutProduct.setCategoryId(categoryData.getCategoryId());
        preCheckoutProduct.setCategoryName(categoryData.getName());
        preCheckoutProduct.setInstantCheckout(instantCheckoutCheckbox.isChecked());
        preCheckoutProduct.setCanBeCheckout(true);
        return preCheckoutProduct;
    }

    public interface ActionListener {
        void onDigitalChooserClicked(List<Product> productList, String productText);

        void onProcessAddToCart(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct);
    }
}
