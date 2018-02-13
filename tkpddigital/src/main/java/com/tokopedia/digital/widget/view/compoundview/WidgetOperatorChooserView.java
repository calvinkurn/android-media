package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.view.adapter.WidgetOperatorAdapter;
import com.tokopedia.digital.widget.view.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.view.model.operator.Operator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 7/18/17.
 */
@Deprecated
public class WidgetOperatorChooserView extends LinearLayout {

    @BindView(R2.id.spinner_operator)
    Spinner spinnerOperator;

    private OperatorChoserListener listener;

    private boolean resetClientNumber;

    public WidgetOperatorChooserView(Context context) {
        super(context);
        init();
    }

    public WidgetOperatorChooserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetOperatorChooserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OperatorChoserListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_widget_spinner_operator, this);
        ButterKnife.bind(this);
    }

    public void renderDataView(final List<Operator> operators, LastOrder lastOrder, int categoryId,
                               String lastOperatorSelected) {
        WidgetOperatorAdapter adapterOperator = new WidgetOperatorAdapter(
                getContext(), android.R.layout.simple_spinner_item, operators);
        spinnerOperator.setAdapter(adapterOperator);
        spinnerOperator.setOnItemSelectedListener(getItemSelectedListener(operators));
        initSetLastOrderSelectedOperator(operators, lastOrder, categoryId, lastOperatorSelected);
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

    private void initSetLastOrderSelectedOperator(List<Operator> operators,
                                              LastOrder lastOrder, int categoryId,
                                              String lastOperatorSelected) {
        if (SessionHandler.isV4Login(getContext()) && lastOrder != null &&
                lastOrder.getAttributes().getCategoryId() == categoryId) {
            for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
                Operator model = operators.get(i);
                if (String.valueOf(model.getId())
                        .equalsIgnoreCase(
                                String.valueOf(lastOrder.getAttributes().getOperatorId()
                                ))) {
                    spinnerOperator.setSelection(i);
                    listener.onCheckChangeOperator(model);
                }
            }
        } else {
            for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
                Operator model = operators.get(i);
                if (String.valueOf(model.getId()).equalsIgnoreCase(lastOperatorSelected)) {
                    spinnerOperator.setSelection(i);
                    listener.onCheckChangeOperator(model);
                }
            }
        }
    }

    public void setLastOrderSelectedOperator(List<Operator> operators,
                                              LastOrder lastOrder, int categoryId) {
        if (SessionHandler.isV4Login(getContext())  &&
                lastOrder.getAttributes().getCategoryId() == categoryId) {
            for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
                Operator model = operators.get(i);
                if (String.valueOf(model.getId())
                        .equalsIgnoreCase(
                                String.valueOf(lastOrder.getAttributes().getOperatorId()
                                ))) {
                    resetClientNumber = false;
                    spinnerOperator.setSelection(i);
                    listener.onCheckChangeOperator(model);
                }
            }
        }
    }

    public interface OperatorChoserListener {
        void onCheckChangeOperator(Operator rechargeOperatorModel);

        void onResetOperator(boolean resetClientNumber);

        void onTrackingOperator();
    }
}
