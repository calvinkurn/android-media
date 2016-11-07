package com.tokopedia.tkpd.deposit.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.tkpd.deposit.model.Deposit;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 3/30/16.
 */
public class DepositAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_DEPOSIT = 100;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.date)
        TextView date;

        @Bind(R2.id.note)
        TextView note;

        @Bind(R2.id.nominal)
        TextView nominal;

        @Bind(R2.id.balance)
        TextView balance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<Deposit> list;
    private final Context context;

    public DepositAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_DEPOSIT:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_deposit, null);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_DEPOSIT:
                bindDeposit((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindDeposit(ViewHolder holder, int position) {
        holder.date.setText(list.get(position).getDepositDate());
        holder.balance.setText(list.get(position).getDepositSaldoIdr());
        holder.nominal.setText(list.get(position).getDepositAmountIdr());
        holder.note.setText(list.get(position).getDepositNotes());
        holder.note.setOnClickListener(onNotesClicked(holder));
        if(Float.parseFloat(list.get(position).getDepositAmount()) > 0) {
            holder.nominal.setTextColor(context.getResources().getColor(R.color.tkpd_light_green));
        } else {
            holder.nominal.setTextColor(context.getResources().getColor(R.color.tkpd_prod_price));
        }
    }
    
    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_DEPOSIT;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    private View.OnClickListener onNotesClicked(final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Salin", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String textHolderNote = holder.note.getText().toString();
                        ClipboardManager clipBoard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", textHolderNote);
                        clipBoard.setPrimaryClip(clip);
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount() ;
    }

    public static DepositAdapter createInstance(Context context) {
        return new DepositAdapter(context);
    }

    public ArrayList<Deposit> getList() {
        return list;
    }

    public void addList(List<Deposit> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
