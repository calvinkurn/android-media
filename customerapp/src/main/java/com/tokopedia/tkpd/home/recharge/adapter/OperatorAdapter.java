package com.tokopedia.tkpd.home.recharge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.tkpd.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alifa on 11/28/2016.
 */

public class OperatorAdapter extends ArrayAdapter<RechargeOperatorModel> {
    private final static int OUT_OF_STOCK = 3;
    private final LayoutInflater inflater;
    private List<RechargeOperatorModel> operatorList;

    public OperatorAdapter(Context context, int resource, List<RechargeOperatorModel> operatorList) {
        super(context, resource, operatorList);
        this.operatorList = operatorList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        RechargeOperatorModel operator = operatorList.get(position);
        NormalViewHolder normalViewHolder;
        View rowNormal = convertView;
        if (rowNormal == null) {
            rowNormal = inflater.inflate(R.layout.recharge_operator_spinner_item, parent, false);
            normalViewHolder = new NormalViewHolder(rowNormal);
            rowNormal.setTag(normalViewHolder);
        } else {
            normalViewHolder = (OperatorAdapter.NormalViewHolder) rowNormal.getTag();
        }
        renderProduct(normalViewHolder, operator);
        return rowNormal;
    }

    private void renderProduct(OperatorAdapter.NormalViewHolder holder, RechargeOperatorModel operator) {
        holder.operatorTextView.setText(operator.name);
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
        /*int status = operatorList.get(position).getAttributes().getStatus();
        switch (status) {
            case OUT_OF_STOCK:
                return false;
            default:
                return true;
        }*/
    }

    static class NormalViewHolder {
        @BindView(R.id.operator_title_textview)
        TextView operatorTextView;


        public NormalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public List<RechargeOperatorModel> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(List<RechargeOperatorModel> operatorList) {
        this.operatorList = operatorList;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }
}