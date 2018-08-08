package com.tokopedia.core.manage.people.bank.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;
import com.tokopedia.core.manage.people.bank.model.BankAccount;
import com.tokopedia.core.manage.people.bank.presenter.ManagePeopleBankFragmentPresenter;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 6/10/16.
 */
public class BankAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_BANK = 100;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.account_name)
        TextView accountName;

        @BindView(R2.id.account_number)
        TextView accountNumber;

        @BindView(R2.id.bank_name)
        TextView bankName;

        @BindView(R2.id.bank_branch)
        TextView bankBranch;

        @BindView(R2.id.bank_icon)
        ImageView bankIcon;

        @BindView(R2.id.delete_bank)
        ImageView deleteBank;

        @BindView(R2.id.default_bank)
        ImageView defaultBank;

        @BindView(R2.id.edit_bank)
        ImageView editBank;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<BankAccount> list;
    private final Context context;
    private ManagePeopleBankFragmentPresenter presenter;

    private BankAdapter(Context context, ManagePeopleBankFragmentPresenter presenter) {
        this.context = context;
        this.list = new ArrayList<>();
        this.presenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_BANK:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_bank_account, null);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_BANK:
                bindBank((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindBank(ViewHolder holder, int position) {
//        ImageHandler.loadImageWithId(holder.defaultBank, R.drawable.ic_done_36dp);
//        ImageHandler.loadImageWithId(holder.deleteBank, R.drawable.ic_clear_36dp);
//        ImageHandler.loadImageWithId(holder.editBank, R.drawable.ic_create_36dp);
        if (list.get(position).getBankLogo() != null
                && !list.get(position).getBankLogo().equals("")
                && !list.get(position).getBankLogo().equals("0")) {
            holder.bankIcon.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(holder.bankIcon, list.get(position).getBankLogo());
        } else {
            holder.bankIcon.setVisibility(View.GONE);
        }
        holder.accountName.setText(list.get(position).getBankAccountName());
        holder.accountNumber.setText(list.get(position).getBankAccountNumber());
        holder.bankBranch.setText(list.get(position).getBankBranch());
        holder.bankName.setText(list.get(position).getBankName());

        if (list.get(position).getIsDefaultBank() == 1) {
            holder.defaultBank.setVisibility(View.GONE);
        } else {
            holder.defaultBank.setVisibility(View.VISIBLE);
        }
        setListener(holder, position);
    }

    private void setListener(ViewHolder holder, int position) {
        holder.defaultBank.setOnClickListener(onDefaultBankClicked(position));
        holder.deleteBank.setOnClickListener(onDeleteBankClicked(position));
        holder.editBank.setOnClickListener(onEditBankClicked(position));
    }

    private View.OnClickListener onEditBankClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActSettingBankPass pass = new ActSettingBankPass();
                pass.setAccountId(list.get(position).getBankAccountId());
                pass.setAccountNumber(list.get(position).getBankAccountNumber());
                pass.setAccountName(list.get(position).getBankAccountName());
                pass.setBankId(String.valueOf(list.get(position).getBankId()));
                pass.setBankName(list.get(position).getBankName());
                pass.setBankBranch(list.get(position).getBankBranch());

                presenter.onEditBank(pass);
            }
        };
    }

    private View.OnClickListener onDeleteBankClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog dialog = new android.support.v7.app
                        .AlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.title_delete_bank)
                                + " " + list.get(position).getBankAccountName() + " ?")
                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActSettingBankPass pass = new ActSettingBankPass();
                                pass.setAccountId(list.get(position).getBankAccountId());
                                pass.setPosition(position);
                                presenter.onDeleteBank(pass);
                            }
                        })
                        .setNegativeButton(context.getString(R.string.No), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();


            }
        };
    }

    private View.OnClickListener onDefaultBankClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog dialog = new android.support.v7.app
                        .AlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.dialog_default_bank_1) + " " +
                                list.get(position).getBankAccountName() + " "
                                + context.getString(R.string.dialog_default_bank_2))
                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActSettingBankPass pass = new ActSettingBankPass();
                                pass.setPosition(position);
                                pass.setAccountId(list.get(position).getBankAccountId());
                                pass.setOwnerId(SessionHandler.getLoginID(context));
                                presenter.onDefaultBank(pass);
                            }
                        })
                        .setNegativeButton(context.getString(R.string.No), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_BANK;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    public static BankAdapter createInstance(Context context, ManagePeopleBankFragmentPresenter presenter) {
        return new BankAdapter(context, presenter);
    }

    public ArrayList<BankAccount> getList() {
        return list;
    }

    public void addList(List<BankAccount> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setDefaultBank(int position) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsDefaultBank(0);
        }
        list.get(position).setIsDefaultBank(1);
        notifyDataSetChanged();
    }

}
