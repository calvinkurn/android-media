package com.tokopedia.core.fragment.shopstatistic;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;

/**
 * Created by Tkpd_Eka on 7/9/2015.
 */
public class ShopStatisticSatisfaction {

    public static class Model {

        public static class MonthlySatisfaction {
            public String positif;
            public String netral;
            public String negatif;

            public MonthlySatisfaction(String pos, String neu, String neg) {
                positif = pos;
                netral = neu;
                negatif = neg;
            }
        }

        public MonthlySatisfaction month1;
        public MonthlySatisfaction month6;
        public MonthlySatisfaction month12;

    }

    private class ViewHolder {
        TextView month1Positif;
        TextView month1Negatif;
        TextView month1Netral;
        TextView month6Positif;
        TextView month6Negatif;
        TextView month6Netral;
        TextView month12Positif;
        TextView month12Negatif;
        TextView month12Netral;
    }

    private ViewHolder holder;
    private Model model;
    private View view;
    private Context context;

    public ShopStatisticSatisfaction(Context context, View view) {
        this.context = context;
        this.view = view;
        holder = new ViewHolder();
        initView();
    }

    public void setModel(Model model) {
        this.model = model;
        setModelToView();
    }

    private void initView() {
        holder.month1Positif = findViewById(R.id.positif_1);
        holder.month1Negatif = findViewById(R.id.negatif_1);
        holder.month1Netral = findViewById(R.id.netral_1);
        holder.month6Positif = findViewById(R.id.positif_6);
        holder.month6Negatif = findViewById(R.id.negatif_6);
        holder.month6Netral = findViewById(R.id.netral_6);
        holder.month12Positif = findViewById(R.id.positif_12);
        holder.month12Negatif = findViewById(R.id.negatif_12);
        holder.month12Netral = findViewById(R.id.netral_12);
    }

    private TextView findViewById(int id) {
        return (TextView)view.findViewById(id);
    }

    private void setModelToView() {
        holder.month1Positif.setText(model.month1.positif);
        holder.month1Negatif.setText(model.month1.negatif);
        holder.month1Netral.setText(model.month1.netral);

        holder.month6Positif.setText(model.month6.positif);
        holder.month6Negatif.setText(model.month6.negatif);
        holder.month6Netral.setText(model.month6.netral);

        holder.month12Positif.setText(model.month12.positif);
        holder.month12Negatif.setText(model.month12.negatif);
        holder.month12Netral.setText(model.month12.netral);

    }

}