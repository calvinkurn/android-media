package com.tokopedia.saldodetails.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.presentation.listener.SaldoItemListener;
import com.tokopedia.saldodetails.response.model.DepositHistoryList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaldoTransactionViewHolder extends AbstractViewHolder<DepositHistoryList> {

    private TextView dateTV;
    private TextView note;
    private TextView nominal;
    private TextView heading;
    private ImageView imageView;
    private SaldoItemListener listener;
    private Context context;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_saldo_transaction;

    public SaldoTransactionViewHolder(View itemView, SaldoItemListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        dateTV = itemView.findViewById(R.id.date);
        note = itemView.findViewById(R.id.note);
        nominal = itemView.findViewById(R.id.nominal);
        heading = itemView.findViewById(R.id.transaction_heading);
        imageView = itemView.findViewById(R.id.transaction_image_view);
    }


    @Override
    public void bind(DepositHistoryList element) {

        Date sourceDate;
        String strDate = "";
        SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdfView = new SimpleDateFormat("dd MMM yyyy hh:mm");
        try {
            sourceDate = sdfSource.parse(element.getCreateTime());
            strDate = sdfView.format(sourceDate);
        } catch (ParseException e) {
        }

        dateTV.setText(String.format(context.getResources().getString(R.string.sp_date_time_view), strDate));
        note.setText(element.getNote());
        heading.setText(element.getTransactionClass());
        ImageHandler.LoadImage(imageView, element.getImageURL());
        if (Float.parseFloat(String.valueOf(element.getAmount())) > 0) {
            listener.setTextColor(nominal, R.color.tkpd_light_green);
            if (context != null) {
                nominal.setTextColor(context.getResources().getColor(R.color.tkpd_light_green));
            }
            if (context != null) {
                nominal.setText(String.format(
                        context.getResources().getString(R.string.sp_positive_saldo_balance),
                        String.valueOf(element.getAmount())));
            } else {
                nominal.setText(String.valueOf(element.getAmount()));
            }

        } else {
            listener.setTextColor(nominal, R.color.tkpd_prod_price);
            if (context != null) {
                nominal.setTextColor(context.getResources().getColor(R.color.tkpd_prod_price));
            }

            if (context != null) {
                nominal.setText(String.format(
                        context.getResources().getString(R.string.sp_negative_saldo_balance),
                        String.valueOf(element.getAmount()).replace("-", "")));
            } else {
                nominal.setText(String.valueOf(element.getAmount()));
            }
        }
    }

    /*private View.OnClickListener onNotesClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotesClicked(note.getText().toString());
            }
        };
    }*/
}
