package com.tokopedia.digital.wallets.accountsetting;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.base.BaseItemHolderItemData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 8/30/17.
 */

public class ConnectedWalletAccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM =
            R.layout.view_holder_wallet_connected_account_item_digital_module;
    public static final int TYPE_HOLDER_TITLE =
            R.layout.view_holder_wallet_connected_account_list_title_digital_module;

    private List<BaseItemHolderItemData> dataList;
    private final Fragment hostFragment;
    private final ActionListener actionListener;
    private final LayoutInflater layoutInflater;

    public ConnectedWalletAccountListAdapter(Fragment hostFragment,
                                             ActionListener actionListener) {
        this.hostFragment = hostFragment;
        this.actionListener = actionListener;
        this.dataList = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(hostFragment.getActivity());
    }

    public void addAllDataList(@NonNull final String title,
                               @NonNull List<WalletAccountSettingConnectedUserData> connectedUserDataList) {
        for (int i = 0, connectedUserDataListSize = connectedUserDataList.size(); i < connectedUserDataListSize; i++) {
            final WalletAccountSettingConnectedUserData data = connectedUserDataList.get(i);
            dataList.add(new BaseItemHolderItemData<WalletAccountSettingConnectedUserData>() {
                @Override
                public int getHolderLayoutId() {
                    return TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM;
                }

                @Override
                public WalletAccountSettingConnectedUserData getItemData() {
                    return data;
                }
            });
            notifyItemInserted(i);
        }

        dataList.add(0, new BaseItemHolderItemData<String>() {
            @Override
            public int getHolderLayoutId() {
                return TYPE_HOLDER_TITLE;
            }

            @Override
            public String getItemData() {
                return title;
            }
        });
        notifyItemInserted(0);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM) {
            return new ConnectedUserViewHolder(
                    layoutInflater.inflate(TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM, parent, false)
            );
        } else if (viewType == TYPE_HOLDER_TITLE) {
            return new TitleViewHolder(layoutInflater.inflate(TYPE_HOLDER_TITLE, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_HOLDER_CONNECTED_ACCOUNT_ITEM) {
            ConnectedUserViewHolder itemHolder = (ConnectedUserViewHolder) holder;
            final WalletAccountSettingConnectedUserData data
                    = (WalletAccountSettingConnectedUserData) dataList.get(position).getItemData();
            itemHolder.tvEmail.setText(data.getEmail());
            itemHolder.tvRegisteredDate.setText(data.getRegisteredDate());
            itemHolder.btnDelete.setText(data.getLabelButtonAction());
            itemHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.onConnectedWalletAccountDeleteAccessClicked(data);
                }
            });
        } else if (type == TYPE_HOLDER_TITLE) {
            TitleViewHolder titleHolder = (TitleViewHolder) holder;
            final String titleStr = (String) dataList.get(position).getItemData();
            titleHolder.tvTitle.setText(titleStr);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getHolderLayoutId();
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
