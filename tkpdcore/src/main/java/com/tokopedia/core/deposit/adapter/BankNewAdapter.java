package com.tokopedia.core.deposit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.R;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.Bank_Table;
import com.tokopedia.core.deposit.model.BankAccount;

import java.util.List;

/**
 * Created by Steven on 7/24/18.
 */
public class BankNewAdapter extends RecyclerView.Adapter<BankNewAdapter.ViewHolder> {

    int mSelectedItem = -1;

    public List<BankAccount> getListBank() {
        return listBank;
    }

    public interface OnBankClickListener {
        void onClick(int position);
    }

    private final List<BankAccount> listBank;
    private int isEmpty = 0;
    private Context context;
    private String selectedBankId;
    private OnBankClickListener listener;

    public BankNewAdapter(Context context, List<BankAccount> listBank) {
        this.context = context;
        this.listBank = listBank;
        this.selectedBankId = "";
    }

    public static BankNewAdapter createAdapter(Context context, List<BankAccount> listBank) {
        return new BankNewAdapter(context, listBank);
    }

    public void setList(List<BankAccount> listBank) {
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

        RadioButton mRadio;
        TextView bankName;
        TextView bankAccountName;

        public ViewHolder(View itemView) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bank_name);
            bankAccountName = itemView.findViewById(R.id.bank_account_name);
            mRadio = itemView.findViewById(R.id.radio);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                }
            };
            itemView.setOnClickListener(clickListener);
            mRadio.setOnClickListener(clickListener);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        return new ViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mRadio.setChecked(position == mSelectedItem);
        
        if (position == listBank.size() && isEmpty == 1) {
            holder.bankName.setText(context.getString(R.string.error_bank_not_found));
            holder.bankName.setOnClickListener(null);
        } else {
            holder.bankName.setText(listBank.get(position).getBankName());
            holder.bankAccountName.setText(listBank.get(position).getBankAccountName());
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
        this.selectedBankId = String.valueOf(listBank.get(position).getBankId());
    }

    public String getSelectedBankId() {
        return this.selectedBankId != null ? this.selectedBankId : "";
    }

    public String getSelectedBankId(String query) {
        Bank bank = new Select().from(Bank.class).where(Bank_Table.bank_name.like("%" + query + "%")).querySingle();
        return bank.getBankId();
    }
}
