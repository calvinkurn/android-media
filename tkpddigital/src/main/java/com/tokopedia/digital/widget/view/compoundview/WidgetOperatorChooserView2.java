package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.widget.view.adapter.WidgetOperatorAdapter2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rizky on 17/01/18.
 */

public class WidgetOperatorChooserView2 extends LinearLayout {

    @BindView(R2.id.spinner_operator)
    Spinner spinnerOperator;

    private WidgetOperatorChooserView2.OperatorChoserListener listener;

    private boolean resetClientNumber;

    public WidgetOperatorChooserView2(Context context) {
        super(context);
        init();
    }

    public WidgetOperatorChooserView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetOperatorChooserView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(WidgetOperatorChooserView2.OperatorChoserListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_widget_spinner_operator, this);
        ButterKnife.bind(this);
    }

    public void renderDataView(final List<Operator> operators, String operatorId) {
        WidgetOperatorAdapter2 adapterOperator = new WidgetOperatorAdapter2(
                getContext(), android.R.layout.simple_spinner_item, operators);
        spinnerOperator.setAdapter(adapterOperator);
        spinnerOperator.setOnItemSelectedListener(getItemSelectedListener(operators));
        initSetLastOrderSelectedOperator(operators, operatorId);
    }

    private AdapterView.OnItemSelectedListener getItemSelectedListener(final List<Operator> operators) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onResetOperator(resetClientNumber);
                listener.onCheckChangeOperator(operators.get(i));
                listener.onTrackingOperator();
                resetClientNumber = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void initSetLastOrderSelectedOperator(List<Operator> operators, String operatorId) {
        for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
            Operator operator = operators.get(i);
            if (String.valueOf(operator.getOperatorId())
                    .equalsIgnoreCase(String.valueOf(operatorId))) {
                spinnerOperator.setSelection(i);
                listener.onCheckChangeOperator(operator);
            }
        }
    }

    public void updateOperator(List<Operator> operators, String operatorId) {
        for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
            Operator operator = operators.get(i);
            if (String.valueOf(operator.getOperatorId())
                    .equalsIgnoreCase(String.valueOf(operatorId))) {
                spinnerOperator.setSelection(i);
                listener.onCheckChangeOperator(operator);
            }
        }
    }

    public interface OperatorChoserListener {
        void onCheckChangeOperator(Operator rechargeOperatorModel);

        void onResetOperator(boolean resetClientNumber);

        void onTrackingOperator();
    }

}
