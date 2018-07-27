package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation_common.NotificationsModel;
import com.tokopedia.navigation.domain.model.DrawerNotification;

/**
 * Created by meta on 03/07/18.
 */
public class NotificationAdapter extends BaseListAdapter<DrawerNotification, BaseViewHolder> {

    private int SELLER_INFO = 0;
    private int PEMBELIAN = 1;
    private int PENJUALAN = 2;
    private int KOMPLAIN = 3;

    private int BELUM_DIBAYAR = 0;
    private int SIAP_DIKIRIM = 1;
    private int SAMPAI_TUJUAN = 2;

    private int PESANAN_BARU = 0;

    private int BUYER = 0;
    private int SELLER = 1;

    private static final int TYPE_ITEM = 2;
    private static final int TYPE_SINGLE = 3;

    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        if(viewType == TYPE_SINGLE) {
            return R.layout.item_common_title;
        } else if(viewType == TYPE_ITEM) {
            return R.layout.item_notification;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getTitle() == null || items.get(position).getTitle().isEmpty()) {
            return TYPE_SINGLE;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SINGLE) {
            return new NotificationSingleHolder(getView(parent, viewType), onItemClickListener);
        }
        return new NotificationHolder(getView(parent, viewType));
    }

    public class NotificationSingleHolder extends BaseViewHolder<DrawerNotification> {

        private TextView title;
        private TextView badge;
        private ImageView arrow;
        private View border;

        NotificationSingleHolder(View itemView, BaseListAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            title = itemView.findViewById(R.id.title);
            badge = itemView.findViewById(R.id.badge);
            arrow = itemView.findViewById(R.id.arrow);
            border = itemView.findViewById(R.id.border);
        }

        @Override
        public void bind(DrawerNotification item) {
            DrawerNotification.ChildDrawerNotification child = item.getChilds().get(0);
            if (child == null)
                return;

            title.setText(child.getTitle());

            if (child.getBadge() != null && child.getBadge() > 0) {
                badge.setVisibility(View.VISIBLE);
                arrow.setVisibility(View.VISIBLE);
                badge.setText(String.format("%s", child.getBadge()));
            } else {
                badge.setVisibility(View.GONE);
                arrow.setVisibility(View.GONE);
            }

            border.setVisibility(View.GONE);
        }
    }

    public class NotificationHolder extends BaseViewHolder<DrawerNotification> {

        private RecyclerView recyclerView;
        private TextView textView;

        NotificationHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerview);
            textView = itemView.findViewById(R.id.title);
        }

        @Override
        public void bind(DrawerNotification item) {
            textView.setText(item.getTitle());

            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);

            NotificationChildAdapter adapter = new NotificationChildAdapter(context);
            adapter.addAll(item.getChilds());
            recyclerView.setAdapter(adapter);
        }
    }

    public void updateValue(NotificationsModel notificationsModel) {
        if (this.items == null)
            return;

        DrawerNotification.ChildDrawerNotification sellerInfo = items.get(SELLER_INFO).getChilds().get(SELLER_INFO);
        sellerInfo.setBadge(notificationsModel.getSellerInfo().getNotification());

        DrawerNotification.ChildDrawerNotification belumDibayar = items.get(PEMBELIAN).getChilds().get(BELUM_DIBAYAR);
        belumDibayar.setBadge(notificationsModel.getBuyerOrder().getConfirmed());

        DrawerNotification.ChildDrawerNotification siapDikirim = items.get(PEMBELIAN).getChilds().get(SIAP_DIKIRIM);
        siapDikirim.setBadge(notificationsModel.getBuyerOrder().getShipped());

        DrawerNotification.ChildDrawerNotification sampaiTujuan = items.get(PEMBELIAN).getChilds().get(SAMPAI_TUJUAN);
        sampaiTujuan.setBadge(notificationsModel.getBuyerOrder().getArriveAtDestination());

        DrawerNotification.ChildDrawerNotification pesananBaru = items.get(PENJUALAN).getChilds().get(PESANAN_BARU);
        pesananBaru.setBadge(notificationsModel.getSellerOrder().getNewOrder());

        DrawerNotification.ChildDrawerNotification penjualanSiapDikirim = items.get(PENJUALAN).getChilds().get(SIAP_DIKIRIM);
        penjualanSiapDikirim.setBadge(notificationsModel.getSellerOrder().getShipped());

        DrawerNotification.ChildDrawerNotification penjualanSampaiTujuan = items.get(PENJUALAN).getChilds().get(SAMPAI_TUJUAN);
        penjualanSampaiTujuan.setBadge(notificationsModel.getSellerOrder().getArriveAtDestination());

        DrawerNotification.ChildDrawerNotification pembeli = items.get(KOMPLAIN).getChilds().get(BUYER);
        pembeli.setBadge(notificationsModel.getResolution().getBuyer());

        DrawerNotification.ChildDrawerNotification penjual = items.get(KOMPLAIN).getChilds().get(SELLER);
        penjual.setBadge(notificationsModel.getResolution().getSeller());

        notifyDataSetChanged();
    }
}
