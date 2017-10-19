package com.tokopedia.digital.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.model.operator.Operator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alifa on 11/28/2016.
 */

public class WidgetOperatorAdapter extends ArrayAdapter<Operator> {
    private final static int OUT_OF_STOCK = 3;
    private final LayoutInflater inflater;
    private List<Operator> operatorList;

    public WidgetOperatorAdapter(Context context, int resource, List<Operator> operatorList) {
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
        Operator operator = operatorList.get(position);
        NormalViewHolder normalViewHolder;
        View rowNormal = convertView;
        if (rowNormal == null) {
            rowNormal = inflater.inflate(R.layout.view_widget_operator_spinner_item, parent, false);
            normalViewHolder = new NormalViewHolder(rowNormal);
            rowNormal.setTag(normalViewHolder);
        } else {
            normalViewHolder = (NormalViewHolder) rowNormal.getTag();
        }
        renderProduct(normalViewHolder, operator);
        return rowNormal;
    }

    private void renderProduct(NormalViewHolder holder, Operator operator) {
        holder.operatorTextView.setText(operator.getAttributes().getName());
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    static class NormalViewHolder {
        @BindView(R2.id.operator_title_textview)
        TextView operatorTextView;


        public NormalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public LayoutInflater getInflater() {
        return inflater;
    }
}