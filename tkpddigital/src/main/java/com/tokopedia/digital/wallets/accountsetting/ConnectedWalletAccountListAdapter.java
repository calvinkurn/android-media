package com.tokopedia.digital.wallets.accountsetting;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 8/30/17.
 */

public class ConnectedWalletAccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM =
            R.layout.view_holder_wallet_connected_account_item;
    public static final int TYPE_HOLDER_TITLE =
            R.layout.view_holder_wallet_connected_account_list_title;

    private List<Object> dataList = new ArrayList<>();
    private final Fragment hostFragment;
    private final ActionListener actionListener;

    public ConnectedWalletAccountListAdapter(Fragment hostFragment,
                                             ActionListener actionListener) {
        this.hostFragment = hostFragment;
        this.actionListener = actionListener;
    }

    public void addAllDataList(String title,
                               List<WalletAccountSettingConnectedUserData> connectedUserDataList) {
        dataList.clear();
        dataList.add(title);
        dataList.addAll(connectedUserDataList);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM) {
            return new ConnectedUserViewHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        } else if (viewType == TYPE_HOLDER_TITLE) {
            return new TitleViewHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM) {
            ConnectedUserViewHolder itemHolder = (ConnectedUserViewHolder) holder;
            final WalletAccountSettingConnectedUserData data
                    = (WalletAccountSettingConnectedUserData) dataList.get(position);
            itemHolder.tvEmail.setText(data.getEmail());
            itemHolder.tvRegisteredDate.setText(data.getRegisteredDate());
            itemHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.onConnectedWalletAccountDeleteAccessClicked(data);
                }
            });
        } else if (type == TYPE_HOLDER_TITLE) {
            TitleViewHolder titleHolder = (TitleViewHolder) holder;
            final String titleStr = (String) dataList.get(position);
            titleHolder.tvTitle.setText(titleStr);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object data = dataList.get(position);
        if (data instanceof WalletAccountSettingConnectedUserData) {
            return TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM;
        } else if (data instanceof String) {
            return TYPE_HOLDER_TITLE;
        }
        return super.getItemViewType(position);
    }

    public void clearAllDataList() {
        dataList.clear();
        notifyDataSetChanged();
    }

    static class ConnectedUserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_icon)
        ImageView ivIcon;
        @BindView(R2.id.tv_email)
        TextView tvEmail;
        @BindView(R2.id.tv_registered_date)
        TextView tvRegisteredDate;
        @BindView(R2.id.btn_delete)
        TextView btnDelete;

        public ConnectedUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_title)
        TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onConnectedWalletAccountDeleteAccessClicked(
                WalletAccountSettingConnectedUserData data
        );
    }
}
