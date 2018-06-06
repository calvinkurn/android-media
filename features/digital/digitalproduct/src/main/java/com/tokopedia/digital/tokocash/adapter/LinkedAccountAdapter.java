package com.tokopedia.digital.tokocash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.AccountWalletItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class LinkedAccountAdapter extends RecyclerView.Adapter {

    private List<AccountWalletItem> accountTokoCashList;

    private ActionListener actionListener;
    private Context context;

    public LinkedAccountAdapter(List<AccountWalletItem> accountTokoCashList) {
        this.accountTokoCashList = accountTokoCashList;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_holder_wallet_connected_account_item_digital_module, parent, false);
        return new ItemAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        renderItemView((ItemAccountViewHolder) holder, accountTokoCashList.get(position));

    }

    private void renderItemView(ItemAccountViewHolder itemViewHolder, final AccountWalletItem accountWalletItem) {
        ImageHandler.loadImageThumbs(context, itemViewHolder.ivIcon, accountWalletItem.getImgUrl());
        itemViewHolder.tvEmail.setText(accountWalletItem.getIdentifier());
        itemViewHolder.tvRegisteredDate.setText(String.format(
                context.getString(R.string.tokocash_linked_account_date),
                accountWalletItem.getAuthDateFmt()));
        itemViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onDeleteAccessClicked(accountWalletItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountTokoCashList.size();
    }

    public void addAccountList(List<AccountWalletItem> accountTokoCashList) {
        this.accountTokoCashList.clear();
        this.accountTokoCashList.addAll(accountTokoCashList);
        notifyDataSetChanged();
    }

    static class ItemAccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.iv_icon)
        ImageView ivIcon;
        @BindView(R2.id.tv_email)
        TextView tvEmail;
        @BindView(R2.id.tv_registered_date)
        TextView tvRegisteredDate;
        @BindView(R2.id.btn_delete)
        TextView btnDelete;

        public ItemAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onDeleteAccessClicked(AccountWalletItem accountTokoCash);
    }
}