package com.tokopedia.core.deposit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.database.model.Bank;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 5/13/16.
 */
public class BankDialogAdapter extends RecyclerView.Adapter<BankDialogAdapter.ViewHolder> {

    public interface OnBankClickListener {
        void onClick(int position);
    }

    private final List<Bank> listBank;
    private int isEmpty = 0;
    private Context context;
    private String selectedBankId;
    private OnBankClickListener listener;

    public BankDialogAdapter(Context context, List<Bank> listBank) {
        this.context = context;
        this.listBank = listBank;
        this.selectedBankId = "";
    }

    public static BankDialogAdapter createAdapter(Context context, List<Bank> listBank) {
        return new BankDialogAdapter(context, listBank);
    }

    public void setList(List<Bank> listBank) {
        this.listBank.clear();
        this.listBank.addAll(listBank);
        notifyDataSetChanged();
    }

    public void showEmpty() {
        this.isEmpty = 1;
    }

    public void removeEmpty() {
        this.isEmpty = 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == listBank.size() && isEmpty == 1) {
            holder.title.setText(context.getString(R.string.error_bank_not_found));
            holder.title.setOnClickListener(null);
        } else {
            holder.title.setText(listBank.get(position).getBankName());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listBank.size() + isEmpty;
    }

    public void setListener(OnBankClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedBankId(int position) {
        this.selectedBankId = listBank.get(position).getBankId();
    }

    public String getSelectedBankId(){
        return this.selectedBankId != null ? this.selectedBankId : "";
    }

}
