package com.tokopedia.digital.widget.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.adapter.WidgetOperatorAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 7/18/17.
 */

public class WidgetOperatorChoserView extends LinearLayout {

    @BindView(R2.id.spinner_operator)
    Spinner spinnerOperator;

    private OperatorChoserListener listener;

    public WidgetOperatorChoserView(Context context) {
        super(context);
        init();
    }

    public WidgetOperatorChoserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetOperatorChoserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public void renderDataView(final List<RechargeOperatorModel> operators, LastOrder lastOrder, int categoryId,
                                String lastOperatorSelected) {
        WidgetOperatorAdapter adapterOperator = new WidgetOperatorAdapter(
                getContext(), android.R.layout.simple_spinner_item, operators);
        spinnerOperator.setAdapter(adapterOperator);
        spinnerOperator.setOnItemSelectedListener(getItemSelectedListener(operators));
        spinnerOperator.setOnTouchListener(getOnTouchListener());
        setLastOrderSelectedOperator(operators, lastOrder, categoryId, lastOperatorSelected);
    }

    private AdapterView.OnItemSelectedListener getItemSelectedListener(final List<RechargeOperatorModel> operators) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onResetOperator();
                listener.onCheckChangeOperator(operators.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private OnTouchListener getOnTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()== MotionEvent.ACTION_UP) {
                    listener.onTrackingOperator();
                }
                return false;
            }
        };
    }

    private void setLastOrderSelectedOperator(List<RechargeOperatorModel> operators,
                                              LastOrder lastOrder, int categoryId,
                                              String lastOperatorSelected) {
        if (SessionHandler.isV4Login(getContext()) && lastOrder != null
                && lastOrder.getData().getAttributes().getCategory_id() == categoryId) {
            for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
                RechargeOperatorModel model = operators.get(i);
                if (String.valueOf(model.operatorId)
                        .equalsIgnoreCase(
                                String.valueOf(lastOrder.getData().getAttributes().getOperator_id()
                                ))) {
                    spinnerOperator.setSelection(i);
                }
            }
        } else {
            for (int i = 0, operatorsSize = operators.size(); i < operatorsSize; i++) {
                RechargeOperatorModel model = operators.get(i);
                if (String.valueOf(model.operatorId).equalsIgnoreCase(lastOperatorSelected)) {
                    spinnerOperator.setSelection(i);
                }
            }
        }
    }

    public interface OperatorChoserListener {
        void onCheckChangeOperator(RechargeOperatorModel rechargeOperatorModel);
        void onResetOperator();
        void onTrackingOperator();
    }
}
