package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.widget.view.adapter.NewWidgetOperatorAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Rizky on 31/01/18.
 */

public class NewWidgetOperatorChooserView extends BaseDigitalChooserView<Operator> {

    @BindView(R2.id.spinner_operator)
    Spinner spinnerOperator;

    private boolean resetClientNumber;

    public NewWidgetOperatorChooserView(Context context) {
        super(context);
    }

    public NewWidgetOperatorChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewWidgetOperatorChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void renderInitDataList(final List<Operator> operators, String defaultOperatorId) {
        this.dataList = operators;
        NewWidgetOperatorAdapter adapterOperator = new NewWidgetOperatorAdapter(
                getContext(), android.R.layout.simple_spinner_item, operators);
        spinnerOperator.setAdapter(adapterOperator);
        spinnerOperator.setOnItemSelectedListener(getItemSelectedListener());
        initSetLastOrderSelectedOperator(defaultOperatorId);
    }

    private AdapterView.OnItemSelectedListener getItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                actionListener.onUpdateDataDigitalChooserSelectedRendered(dataList.get(i), resetClientNumber);
                actionListener.tracking();
                resetClientNumber = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void initSetLastOrderSelectedOperator(String defaultOperatorId) {
        for (int i = 0, operatorsSize = dataList.size(); i < operatorsSize; i++) {
            Operator operator = dataList.get(i);
            if (String.valueOf(operator.getOperatorId())
                    .equalsIgnoreCase(String.valueOf(defaultOperatorId))) {
                resetClientNumber = true;
                spinnerOperator.setSelection(i);
                actionListener.onUpdateDataDigitalChooserSelectedRendered(dataList.get(i), resetClientNumber);
                actionListener.tracking();
                break;
            }
        }
    }

    @Override
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_widget_spinner_operator;
    }

    @Override
    public void setLabelText(String labelText) {

    }

    @Override
    public void enableError(String errorMessage) {

    }

    @Override
    public void disableError() {

    }

    @Override
    public void renderUpdateDataSelected(Operator data) {
        for (int i = 0, operatorsSize = dataList.size(); i < operatorsSize; i++) {
            Operator operator = dataList.get(i);
            if (String.valueOf(operator.getOperatorId())
                    .equalsIgnoreCase(String.valueOf(data.getOperatorId()))) {
                resetClientNumber = false;
                spinnerOperator.setSelection(i);
                actionListener.onUpdateDataDigitalChooserSelectedRendered(dataList.get(i), resetClientNumber);
                actionListener.tracking();
                break;
            }
        }
    }

}
