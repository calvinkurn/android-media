package com.tokopedia.digital.tokocash.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class TopUpTokoCashView extends LinearLayout {

    @BindView(R2.id.digital_product_chooser_view)
    DigitalProductChooserView digitalProductChooserView;
    @BindView(R2.id.cb_instant_checkout)
    CheckBox instantCheckoutCheckbox;
    @BindView(R2.id.btn_topup)
    TextView btnTopUp;

    private ActionListener listener;
    private Product productSelected;
    private CategoryData categoryData;
    private Operator operatorSelected;

    public TopUpTokoCashView(Context context) {
        super(context);
        init();
    }

    public TopUpTokoCashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopUpTokoCashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tokocash_topup, this, true);
        ButterKnife.bind(this);
        btnTopUp.setOnClickListener(getClickListenerTopUp());
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public void renderDataTopUp(CategoryData categoryData, Operator operatorSelected) {
        this.categoryData = categoryData;
        this.operatorSelected = operatorSelected;
        digitalProductChooserView.setActionListener(getActionListener(operatorSelected.getRule().getProductText()));
        digitalProductChooserView.renderInitDataList(operatorSelected.getProductList(),
                operatorSelected.getDefaultProductId());
        digitalProductChooserView.setLabelText(operatorSelected.getRule().getProductText());
        instantCheckoutCheckbox.setVisibility(categoryData.isInstantCheckout() ? VISIBLE : GONE);
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

            }

            @Override
            public void onDigitalChooserClicked(List<Product> data) {
                UnifyTracking.eventSelectProductOnNativePage(categoryData.getName(), categoryData.getName());
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
                UnifyTracking.eventClickBuyOnNative(categoryData.getName(), isInstant);
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
