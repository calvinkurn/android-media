package com.tokopedia.tokocash.accountsetting.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.accountsetting.presentation.model.AccountWalletItem;

import java.util.List;

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
                R.layout.item_account_wallet_connected, parent, false);
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

        private ImageView ivIcon;
        private TextView tvEmail;
        private TextView tvRegisteredDate;
        private TextView btnDelete;

        public ItemAccountViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvRegisteredDate = itemView.findViewById(R.id.tv_registered_date);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface ActionListener {
        void onDeleteAccessClicked(AccountWalletItem accountTokoCash);
    }
}