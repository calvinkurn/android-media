package com.tokopedia.digital.product.view.compoundview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.model.Product;

import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalProductChooserView extends BaseDigitalChooserView<Product> {

    @BindView(R2.id.tv_label_chooser)
    TextView tvLabel;
    @BindView(R2.id.tv_operator_product_name)
    TextView tvNameProduct;
    @BindView(R2.id.tv_error_chooser)
    TextView tvErrorProduct;
    @BindView(R2.id.layout_product)
    RelativeLayout layoutProduct;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalProductChooserView(Context context) {
        super(context);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalProductChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalProductChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
