package com.tokopedia.digital.product.view.compoundview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.model.Operator;

import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalOperatorChooserView extends BaseDigitalChooserView<Operator> {

    @BindView(R2.id.tv_label_chooser)
    TextView tvLabel;
    @BindView(R2.id.tv_operator_product_name)
    TextView tvNameOperator;
    @BindView(R2.id.tv_error_chooser)
    TextView tvErrorOperator;
    @BindView(R2.id.layout_product)
    RelativeLayout layoutProduct;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalOperatorChooserView(Context context) {
        super(context);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalOperatorChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    public DigitalOperatorChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        tvErrorOperator.setVisibility(VISIBLE);
        tvErrorOperator.setText(errorMessage);
    }

    @Override
    public void disableError() {
        tvErrorOperator.setText("");
        tvErrorOperator.setVisibility(GONE);
    }

    private void invalidateContentView() {
        this.tvNameOperator.setText(dataSelected.getName());
        //TODO bisa set error text disini berdasarkan status operatornya
    }


    @Override
    public void renderInitDataList(List<Operator> dataList) {
        this.dataList = dataList;
        if (!dataList.isEmpty() && (this.dataSelected == null
                || !this.dataSelected.getOperatorId().equalsIgnoreCase(dataList.get(0).getOperatorId())))
            this.dataSelected = dataList.get(0);
        invalidateContentView();
        if (actionListener != null)
            actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
        layoutProduct.setOnClickListener(getOnChooserClickedListener());
    }

    @Override
    public void renderUpdateDataSelected(Operator data) {
        this.dataSelected = data;
        invalidateContentView();
        if (actionListener != null)
            actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
    }
}
