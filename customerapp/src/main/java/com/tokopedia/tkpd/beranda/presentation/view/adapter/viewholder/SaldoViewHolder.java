package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class SaldoViewHolder extends AbstractViewHolder<SaldoViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_tokocash;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private Context context;
    private final HomeCategoryListener listener;
    private ItemAdapter itemAdapter;
    private static final int spanCount = 2;
    private GridLayoutManager layoutManager;

    public SaldoViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
        layoutManager = new GridLayoutManager(context, spanCount,GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, 2, false));
    }

    @Override
    public void bind(SaldoViewModel element) {
        layoutManager.setSpanCount(element.getListItems().size());
        itemAdapter.setList(element.getListItems());
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private List<SaldoViewModel.ItemModel> list;

        public ItemAdapter() {
            list = new ArrayList<>();
        }

        public void setList(List<SaldoViewModel.ItemModel> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_tokocash, parent, false));
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            final SaldoViewModel.ItemModel item = list.get(position);
            holder.title.setText(item.getTitle());
            holder.icon.setImageResource(item.getIcon());
            holder.subtitle.setText(item.getSubtitle());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item.getApplinks().contains("wallet/activation")){
                        holder.onWalletActionButtonClicked(item.getRedirectUrl(), item.getApplinks());
                    } else if(item.getApplinks().contains(Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY)){
                        holder.onWalletBalanceClicked(item.getRedirectUrl(), item.getApplinks());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.icon)
        AppCompatImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.subtitle)
        TextView subtitle;
        private Activity activity;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.activity = ((Activity) context);
        }

        private void onWalletBalanceClicked(String redirectUrlBalance, String appLinkBalance) {
            WalletRouterUtil.navigateWallet(
                    activity.getApplication(),
                    context,
                    IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                    appLinkBalance,
                    redirectUrlBalance,
                    new Bundle()
            );
        }

        private void onWalletActionButtonClicked(String redirectUrlActionButton, String appLinkActionButton) {
            WalletRouterUtil.navigateWallet(
                    activity.getApplication(),
                    context,
                    IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                    appLinkActionButton,
                    redirectUrlActionButton,
                    new Bundle()
            );
        }
    }
}
