package com.tokopedia.withdraw.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.view.listener.WithdrawContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Steven on 7/24/18.
 */
public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    private static final int DEFAULT_BANK_ID = 1;
    WithdrawAnalytics analytics;

    public static final int REFUND = 0;
    public static final int PENGHASILAN = 1;
    public static final int MAX_BANK_DISPLAY_COUNT = 3;

    private int currentTab = 0;

    int selectedItem;

    public interface OnBankClickListener {

        void onClick(int position);
    }

    private final List<BankAccount> listBank;
    private final List<BankAccount> masterBankList;

    private int isEmpty = 0;
    private WithdrawContract.View context;
    private String selectedBankId;
    private OnBankClickListener listener;
    private BankAccount accountButton;

    public BankAdapter(WithdrawContract.View context, List<BankAccount> listBank, WithdrawAnalytics analytics) {
        this.context = context;
        this.listBank = listBank;
        this.selectedBankId = "";
        this.selectedItem = -1;
        this.accountButton = new BankAccount();
        this.masterBankList = new ArrayList<>();
        this.analytics = analytics;
    }

    public static BankAdapter createAdapter(WithdrawContract.View context, List<BankAccount> listBank, WithdrawAnalytics analytics) {
        return new BankAdapter(context, listBank, analytics);
    }

    public void setList(List<BankAccount> listBank) {

        masterBankList.clear();
        masterBankList.addAll(listBank);
        Collections.sort(masterBankList, (o1, o2) -> {
            if (o1.getIsDefaultBank() < o2.getIsDefaultBank()) {
                return 1;
            }
            return 0;
        });
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new Callback(new ArrayList<>(this.listBank), new ArrayList<>(listBank)));
        diffResult.dispatchUpdatesTo(this);
        this.listBank.clear();
        for (int i = 0; i <= MAX_BANK_DISPLAY_COUNT; i++) {
            if (masterBankList.size() > i)
                this.listBank.add(masterBankList.get(i));
        }
        this.listBank.add(accountButton);
    }


    public void addItem(BankAccount bankViewModel) {
        this.listBank.remove(accountButton);
        this.listBank.add(bankViewModel);
        this.listBank.add(accountButton);
        notifyItemRangeChanged(listBank.size() - 1, 2);
    }

    public List<BankAccount> getListBank() {
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
            if (text != null) {
                text.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                        (itemView.getContext(), R.drawable.ic_add_round), null, null, null);
            }
        }
    }

    public class ItemBankViewHolder extends ViewHolder {
        ImageView mRadio;
        TextView bankName;
        TextView bankAccountName;
        TextView adminFee;

        public ItemBankViewHolder(View itemView) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bank_name);
            bankAccountName = itemView.findViewById(R.id.bank_acc_name);
            adminFee = itemView.findViewById(R.id.tvAdminFee);
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

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 1:
                if (listBank.size()-1 < 3) {
                    holder.text.setText(context.getStringResource(R.string.title_add_account_bank));
                } else if (listBank.size()-1 == 3) {
                    holder.text.setText(context.getStringResource(R.string.title_set_account_bank));
                } else {
                    holder.text.setText(context.getStringResource(R.string.title_set_rekening_utama));
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listBank.size() < 4) {
                            analytics.eventClickAddAccount();
                            context.goToAddBank();
                        } else {
                            context.goToSettingBank();
                        }

                    }
                });
                break;
            default:
                ItemBankViewHolder viewHolder = (ItemBankViewHolder) holder;
                Context context = viewHolder.itemView.getContext();
                BankAccount thisItem = listBank.get(position);

                if (thisItem.getAdminFee() > 0) {
                    String adminFeeStr = CurrencyFormatUtil.convertPriceValueToIdrFormat(thisItem.getAdminFee(), false);
                    viewHolder.adminFee.setText(context.getString(R.string.swd_admin_fee, adminFeeStr));
                    viewHolder.adminFee.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.adminFee.setText("");
                    viewHolder.adminFee.setVisibility(View.GONE);
                }

                View.OnClickListener l = (View v) -> {
                    analytics.eventClickAccountBank();
                    changeItemSelected(position);
                };
                if (thisItem.getStatus() == 0) {
                    viewHolder.bankName.setTextColor(context.getResources().getColor(R.color.swd_grey_100));
                    viewHolder.bankAccountName.setTextColor(context.getResources().getColor(R.color.swd_grey_100));
                    viewHolder.adminFee.setTextColor(context.getResources().getColor(R.color.swd_grey_100));
                    holder.itemView.setOnClickListener(null);
                } else {
                    viewHolder.bankName.setTextColor(context.getResources().getColor(R.color.grey_796));
                    viewHolder.bankAccountName.setTextColor(context.getResources().getColor(R.color.grey_button_compat));
                    viewHolder.adminFee.setTextColor(context.getResources().getColor(R.color.black_40));
                    holder.itemView.setOnClickListener(l);
                }

                Drawable drawabl;
                if (thisItem.getStatus() == 0) {
                    drawabl = MethodChecker.getDrawable(context, R.drawable.bank_withdraw_radio_disabled);
                } else if (listBank.get(position).isChecked()) {
                    drawabl = MethodChecker.getDrawable(context, R.drawable.bank_withdraw_radio_button_selected);
                } else {
                    drawabl = MethodChecker.getDrawable(context, R.drawable.bank_withdraw_radio_button_default);
                }
                ((ItemBankViewHolder) holder).mRadio.setImageDrawable(drawabl);

                if (position == listBank.size() && isEmpty == 1) {
                    viewHolder.bankName.setOnClickListener(null);
                } else {
                    viewHolder.bankName.setText(thisItem.getBankName());
                    viewHolder.bankAccountName.setText(
                            String.format("%s • %s", thisItem.getAccountNo(), thisItem.getAccountName()));
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


    public BankAccount getSelectedBank() {
        if (selectedItem < 0) {
            return null;
        }
        return listBank.get(selectedItem);
    }

    public void setDefault() {
        if (listBank.size() > 0) {
            for (int i = 0; i < listBank.size(); i++) {
                if (DEFAULT_BANK_ID == listBank.get(i).getIsDefaultBank()) {
                    selectedItem = i;
                    listBank.get(selectedItem).setChecked(true);
                    break;
                }
            }
        }
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

            if (oldItem instanceof BankAccount) {
                return newItem instanceof BankAccount
                        && ((BankAccount) oldItem).getBankAccountID() != 0
                        && (((BankAccount) oldItem).getBankAccountID() == ((BankAccount) newItem).getBankAccountID());
            }
            return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Object oldItem = oldList.get(oldItemPosition);
            Object newItem = newList.get(newItemPosition);

            if (oldItem instanceof BankAccount && newItem instanceof BankAccount) {
                BankAccount oldPost = ((BankAccount) oldItem);
                BankAccount newPost = ((BankAccount) newItem);

                return oldPost.getBankName() != null &&
                        oldPost.getBankName().equalsIgnoreCase(newPost.getBankName()) &&
                        oldPost.getAccountNo() != null &&
                        oldPost.getAccountNo().equalsIgnoreCase(newPost.getAccountNo()) &&
                        oldPost.getAccountNo() != null &&
                        oldPost.getAccountNo().equalsIgnoreCase(newPost.getAccountNo());
            }
            return oldItem.equals(newItem);
        }
    }

}
