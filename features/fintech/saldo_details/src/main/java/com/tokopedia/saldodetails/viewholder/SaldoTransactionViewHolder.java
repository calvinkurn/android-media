package com.tokopedia.saldodetails.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.presentation.listener.SaldoItemListener;
import com.tokopedia.saldodetails.response.model.Deposit;

public class SaldoTransactionViewHolder extends AbstractViewHolder<Deposit> {

    private TextView date;
    private TextView note;
    private TextView nominal;
    private TextView balance;
    private SaldoItemListener listener;
    private Context context;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_saldo_transaction;


    public SaldoTransactionViewHolder(View itemView, SaldoItemListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        date = itemView.findViewById(R.id.date);
        note = itemView.findViewById(R.id.note);
        nominal = itemView.findViewById(R.id.nominal);
        balance = itemView.findViewById(R.id.balance);
    }


    @Override
    public void bind(Deposit element) {
        date.setText(element.getDepositDate());
        balance.setText(element.getDepositSaldoIdr());
        nominal.setText(element.getDepositAmountIdr());
        note.setText(element.getDepositNotes());
        note.setOnClickListener(onNotesClicked());
        if (Float.parseFloat(element.getDepositAmount()) > 0) {
            listener.setTextColor(nominal, R.color.tkpd_light_green);
            if (context != null) {
                nominal.setTextColor(context.getResources().getColor(R.color.tkpd_light_green));
            }
        } else {
            listener.setTextColor(nominal, R.color.tkpd_prod_price);
            if (context != null) {
                nominal.setTextColor(context.getResources().getColor(R.color.tkpd_prod_price));
            }
        }
    }

    private View.OnClickListener onNotesClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotesClicked(note.getText().toString());
            }
        };
    }
}
