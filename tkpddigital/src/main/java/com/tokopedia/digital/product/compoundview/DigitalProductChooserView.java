package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.Product;

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

    @Override
    public void renderInitDataList(List<Product> dataList) {
        this.dataList = dataList;
        if (!dataList.isEmpty() && (this.dataSelected == null
                || !this.dataSelected.getProductId().equalsIgnoreCase(dataList.get(0).getProductId())))
            this.dataSelected = dataList.get(0);
        invalidateContentView();
        actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
        layoutProduct.setOnClickListener(getOnChooserClickedListener());
    }


    private void invalidateContentView() {
        this.tvNameProduct.setText(dataSelected.getDesc());
        //TODO bisa set error text disini berdasarkan status productnya
    }

    @Override
    public void renderUpdateDataSelected(Product data) {
        this.dataSelected = data;
        invalidateContentView();
        actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
    }
}
