package com.tokopedia.withdraw.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import java.util.List;


/**
 * Created by Steven on 7/24/18.
 */
public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    int selectedItem = -1;

    public interface OnBankClickListener {
        void onClick(int position);
    }

    private final List<BankAccountViewModel> listBank;
    private int isEmpty = 0;
    private Context context;
    private String selectedBankId;
    private OnBankClickListener listener;

    public BankAdapter(Context context, List<BankAccountViewModel> listBank) {
        this.context = context;
        this.listBank = listBank;
        this.selectedBankId = "";
    }

    public static BankAdapter createAdapter(Context context, List<BankAccountViewModel> listBank) {
        return new BankAdapter(context, listBank);
    }

    public void setList(List<BankAccountViewModel> listBank) {
        this.listBank.clear();
        this.listBank.addAll(listBank);
        this.listBank.add(new BankAccountViewModel());
        notifyDataSetChanged();
    }

    public List<BankAccountViewModel> getListBank() {
        return listBank;
    }

    public boolean hasSelectedItem() {
        return selectedItem != -1;
    }

    public void showEmpty() {
        this.isEmpty = 1;
    }

    public void removeEmpty() {
        this.isEmpty = 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ItemBankViewHolder extends ViewHolder {
        RadioButton mRadio;
        TextView bankName;
        TextView bankAccountName;

        public ItemBankViewHolder(View itemView) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bank_name);
            bankAccountName = itemView.findViewById(R.id.bank_account_name);
            mRadio = itemView.findViewById(R.id.radio);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == listBank.size() - 1 ? 1 : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView;
        if (viewType == 1) {
            parentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_bank, parent, false);
            return new ViewHolder(parentView);
        }

        parentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        return new ItemBankViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 1:
                break;
            default:
                ItemBankViewHolder viewHolder = (ItemBankViewHolder) holder;
                BankAccountViewModel thisItem = listBank.get(position);
                viewHolder.mRadio.setChecked(thisItem.isChecked());

                if (position == listBank.size() && isEmpty == 1) {
                    viewHolder.bankName.setOnClickListener(null);
                } else {
                    viewHolder.bankName.setText(thisItem.getBankName());
                    viewHolder.bankAccountName.setText(thisItem.getBankAccountName());
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedItem == position){
                            return;
                        }
                        listBank.get(selectedItem).setChecked(false);
                        notifyItemChanged(selectedItem);
                        selectedItem = position;
                        listBank.get(selectedItem).setChecked(true);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listBank.size() + isEmpty;
    }

    public void setListener(OnBankClickListener listener) {
        this.listener = listener;
    }
}
