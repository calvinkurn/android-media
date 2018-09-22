package com.tokopedia.withdraw.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Steven on 7/24/18.
 */
public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    @Inject
    WithdrawAnalytics analytics;


    int selectedItem;
    public interface OnBankClickListener {

        void onClick(int position);
    }
    private final List<BankAccountViewModel> listBank;

    private int isEmpty = 0;
    private WithdrawContract.View context;
    private String selectedBankId;
    private OnBankClickListener listener;
    private BankAccountViewModel accountButton;

    public BankAdapter(WithdrawContract.View context, List<BankAccountViewModel> listBank) {
        this.context = context;
        this.listBank = listBank;
        this.selectedBankId = "";
        this.selectedItem = -1;
        this.accountButton = new BankAccountViewModel();
    }

    public static BankAdapter createAdapter(WithdrawContract.View context, List<BankAccountViewModel> listBank) {
        return new BankAdapter(context, listBank);
    }

    public void setList(List<BankAccountViewModel> listBank) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new Callback(new ArrayList<>(this.listBank), new ArrayList<>(listBank)));
        diffResult.dispatchUpdatesTo(this);

        this.listBank.clear();
        this.listBank.addAll(listBank);
        this.listBank.add(accountButton);
    }


    public void addItem(BankAccountViewModel bankViewModel) {
        this.listBank.remove(accountButton);
        this.listBank.add(bankViewModel);
        this.listBank.add(accountButton);
        notifyItemRangeChanged(listBank.size()-1, 2);
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
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public class ItemBankViewHolder extends ViewHolder {
        ImageView mRadio;
        TextView bankName;
        TextView bankAccountName;

        public ItemBankViewHolder(View itemView) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bank_name);
            bankAccountName = itemView.findViewById(R.id.bank_acc_name);
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
                    .inflate(R.layout.item_add_bank_withdraw, parent, false);
            return new ViewHolder(parentView);
        }

        parentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank_withdraw, parent, false);
        return new ItemBankViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 1:
                if(listBank.size()<4){
                    holder.text.setText(context.getStringResource(R.string.title_add_account_bank));
                }else {
                    holder.text.setText(context.getStringResource(R.string.title_set_account_bank));
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listBank.size()<4){
                            analytics.eventClickAddAccount();
                            context.goToAddBank();
                        }else {
                            context.goToSettingBank();
                        }

                    }
                });
                break;
            default:
                ItemBankViewHolder viewHolder = (ItemBankViewHolder) holder;
                Context context = viewHolder.itemView.getContext();
                BankAccountViewModel thisItem = listBank.get(position);

                View.OnClickListener l = (View v) -> {
                    analytics.eventClickAccountBank();
                    changeItemSelected(position);
                };
                holder.itemView.setOnClickListener(l);

                Drawable drawabl;
                if(listBank.get(position).isChecked()){
                    drawabl = MethodChecker.getDrawable(context, R.drawable.bank_withdraw_radio_button_selected);
                }else {
                    drawabl =MethodChecker.getDrawable(context, R.drawable.bank_withdraw_radio_button_default);
                }
                ((ItemBankViewHolder) holder).mRadio.setImageDrawable(drawabl);

                if (position == listBank.size() && isEmpty == 1) {
                    viewHolder.bankName.setOnClickListener(null);
                } else {
                    viewHolder.bankName.setText(thisItem.getBankName());
                    viewHolder.bankAccountName.setText(
                            String.format("%s • %s", thisItem.getBankAccountNumber(), thisItem.getBankAccountName()));
                }
                break;
        }
    }

    public void changeItemSelected(int position) {
        if (selectedItem >= 0 && selectedItem < listBank.size()) {
            listBank.get(selectedItem).setChecked(false);
        }
        listBank.get(position).setChecked(true);
        selectedItem = position;
        notifyItemRangeChanged(0, listBank.size());
        this.context.itemSelected();
    }

    @Override
    public int getItemCount() {
        return listBank.size() + isEmpty;
    }

    public void setListener(OnBankClickListener listener) {
        this.listener = listener;
    }


    public BankAccountViewModel getSelectedBank() {
        if(selectedItem < 0){
            return null;
        }
        return listBank.get(selectedItem);
    }

    public void setDefault(int defaultBank) {
        selectedItem = defaultBank;
    }

    static class Callback extends DiffUtil.Callback {
        private final List<Object> oldList;
        private final List<Object> newList;

        public Callback(List<Object> oldList, List<Object> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Object oldItem = oldList.get(oldItemPosition);
            Object newItem = newList.get(newItemPosition);

            if (oldItem instanceof BankAccountViewModel) {
                return newItem instanceof BankAccountViewModel
                        && ((BankAccountViewModel) oldItem).getBankAccountId()
                        == ((BankAccountViewModel) oldItem).getBankAccountId();
            }
            return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Object oldItem = oldList.get(oldItemPosition);
            Object newItem = newList.get(newItemPosition);

            if (oldItem instanceof BankAccountViewModel && newItem instanceof BankAccountViewModel) {
                BankAccountViewModel oldPost = ((BankAccountViewModel) oldItem);
                BankAccountViewModel newPost = ((BankAccountViewModel) newItem);

                return oldPost.getBankName() == newPost.getBankAccountName()
                        && oldPost.getBankAccountNumber() == newPost.getBankAccountNumber()
                        && oldPost.getBankAccountName() == newPost.getBankAccountName();
            }
            return oldItem.equals(newItem);
        }
    }
}
