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

    public void renderInitDataList(List<Operator> operatorList, String defaultOperatorId) {
        dataList = operatorList;
        if (!operatorList.isEmpty()) {
            dataSelected = findOperatorById(defaultOperatorId);
            if (dataSelected == null) {
                dataSelected = dataList.get(0);
            }
        }
        invalidateContentView();
        if (actionListener != null)
            actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
        layoutProduct.setOnClickListener(getOnChooserClickedListener());
    }

    private Operator findOperatorById(String defaultOperatorId) {
        for (int i = 0, operatorsSize = dataList.size(); i < operatorsSize; i++) {
            Operator operator = dataList.get(i);
            if (String.valueOf(operator.getOperatorId())
                    .equalsIgnoreCase(defaultOperatorId)) {
                return dataList.get(i);
            }
        }
        return null;
    }

    private void invalidateContentView() {
        tvNameOperator.setText(dataSelected.getName());
        //TODO bisa set error text disini berdasarkan status operatornya
    }

    @Override
    public void renderUpdateDataSelected(Operator operator) {
        dataSelected = operator;
        invalidateContentView();
        if (actionListener != null)
            actionListener.onUpdateDataDigitalChooserSelectedRendered(dataSelected);
    }
}
