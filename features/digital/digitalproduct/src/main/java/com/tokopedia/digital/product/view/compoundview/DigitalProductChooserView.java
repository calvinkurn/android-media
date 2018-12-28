package com.tokopedia.digital.product.view.compoundview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;

import java.util.List;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalProductChooserView extends BaseDigitalChooserView<Product> {

    private TextView tvLabel;
    private TextView tvNameProduct;
    private TextView tvErrorProduct;
    private RelativeLayout layoutProduct;

    public DigitalProductChooserView(Context context) {
        super(context);
    }

    public DigitalProductChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DigitalProductChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateView() {
        tvLabel = findViewById(R.id.tv_label_chooser);
        tvNameProduct = findViewById(R.id.tv_operator_product_name);
        tvErrorProduct = findViewById(R.id.tv_error_chooser);
        layoutProduct = findViewById(R.id.layout_product);
    }

    @Override
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_chooser_digital_module;
    }

    @Override
    public void setLabelText(String labelText) {
        if (!TextUtils.isEmpty(labelText)) {
            tvLabel.setText(labelText);
            tvLabel.setVisibility(VISIBLE);
        } else {
            tvLabel.setVisibility(GONE);
        }
    }

    @Override
    public void enableError(String errorMessage) {
        tvErrorProduct.setVisibility(VISIBLE);
        tvErrorProduct.setText(errorMessage);
    }

    @Override
    public void disableError() {
        tvErrorProduct.setText("");
        tvErrorProduct.setVisibility(GONE);
    }

    public void renderInitDataList(List<Product> productList, int defaultProductId) {
        dataList = productList;
        if (!productList.isEmpty()) {
            dataSelected = findProductById(defaultProductId);
            if (dataSelected == null) {
                dataSelected = productList.get(0);
            }
        }
        invalidateContentView();
        if (actionListener != null) {
            actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
            actionListener.tracking();
        }
        layoutProduct.setOnClickListener(getOnChooserClickedListener());
    }

    private Product findProductById(int defaultProductId) {
        for (int i = 0; i < dataList.size(); i++) {
            if (Integer.valueOf(dataList.get(i).getProductId()) == defaultProductId) {
                return dataList.get(i);
            }
        }
        return null;
    }

    private void invalidateContentView() {
        if (dataSelected != null) {
            tvNameProduct.setText(dataSelected.getDesc());
            switch (dataSelected.getStatus()) {
                case Product.STATUS_OUT_OF_STOCK:
                    tvErrorProduct.setVisibility(VISIBLE);
                    tvErrorProduct.setText(
                            R.string.error_message_product_out_of_stock_digital_module
                    );
                    break;
                default:
                    tvErrorProduct.setVisibility(GONE);
                    break;
            }
        }
    }

    @Override
    public void renderUpdateDataSelected(Product data) {
        dataSelected = data;
        invalidateContentView();
        if (actionListener != null) {
            actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
            actionListener.tracking();
        }
    }
}
