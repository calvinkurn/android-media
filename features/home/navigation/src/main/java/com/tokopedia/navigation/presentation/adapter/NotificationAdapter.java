package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation.util.IntegerUtil;
import com.tokopedia.navigation_common.model.NotifcenterUnread;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.navigation.domain.model.DrawerNotification;

import java.util.List;

import static com.tokopedia.navigation.GlobalNavConstant.BUYER;
import static com.tokopedia.navigation.GlobalNavConstant.BUYER_INFO;
import static com.tokopedia.navigation.GlobalNavConstant.KOMPLAIN;
import static com.tokopedia.navigation.GlobalNavConstant.MENUNGGU_KONFIRMASI;
import static com.tokopedia.navigation.GlobalNavConstant.MENUNGGU_PEMBAYARAN;
import static com.tokopedia.navigation.GlobalNavConstant.NEWEST_INFO;
import static com.tokopedia.navigation.GlobalNavConstant.PENJUALAN;
import static com.tokopedia.navigation.GlobalNavConstant.PESANAN_BARU;
import static com.tokopedia.navigation.GlobalNavConstant.PESANAN_DIPROSES;
import static com.tokopedia.navigation.GlobalNavConstant.SAMPAI_TUJUAN;
import static com.tokopedia.navigation.GlobalNavConstant.SEDANG_DIKIRIM;
import static com.tokopedia.navigation.GlobalNavConstant.SELLER;
import static com.tokopedia.navigation.GlobalNavConstant.PEMBELIAN;
import static com.tokopedia.navigation.GlobalNavConstant.SELLER_INFO;
import static com.tokopedia.navigation.GlobalNavConstant.SIAP_DIKIRIM;
import static com.tokopedia.navigation.GlobalNavConstant.UPDATE;
import static com.tokopedia.navigation_common.model.NotifcenterUnread.NOTIF_99;
import static com.tokopedia.navigation_common.model.NotifcenterUnread.NOTIF_99_NUMBER;

/**
 * Created by meta on 03/07/18.
 */
public class NotificationAdapter extends BaseListAdapter<DrawerNotification, BaseViewHolder> {

    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_notification;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(getView(parent, viewType));
    }

    public interface OnNotifClickListener {
        void onNotifClick(int parent, int child);
    }

    private OnNotifClickListener onNotifClickListener;

    public void setOnNotifClickListener(OnNotifClickListener onNotifClickListener) {
        this.onNotifClickListener = onNotifClickListener;
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
            adapter.setOnItemClickListener((view, position) -> {
                if (onNotifClickListener != null)
                    onNotifClickListener.onNotifClick(getAdapterPosition(), position);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    public void updateValue(NotificationsModel data, NotifcenterUnread unread) {
        if (this.items == null)
            return;

        for (DrawerNotification item : items) {
            if (item.getId() != null) {
                List<DrawerNotification.ChildDrawerNotification> childs = item.getChilds();
                for (DrawerNotification.ChildDrawerNotification child : childs) {
                    if (item.getId() == UPDATE){
                        if (child.getId() == NEWEST_INFO) {
                            child.setBadge(getNotifCenterUnread(unread));
                        }
                    }
                    else if (item.getId() == PEMBELIAN) {
                        if (child.getId() == MENUNGGU_PEMBAYARAN) {
                            try {
                                child.setBadge(Integer.parseInt(data.getBuyerOrder().getPaymentStatus()));
                            } catch (NumberFormatException e) {
                                child.setBadge(0);
                            }
                        } else if (child.getId() == MENUNGGU_KONFIRMASI) {
                            child.setBadge(data.getBuyerOrder().getConfirmed());
                        } else if (child.getId() == PESANAN_DIPROSES) {
                            child.setBadge(data.getBuyerOrder().getProcessed());
                        } else if (child.getId() == SEDANG_DIKIRIM) {
                            child.setBadge(data.getBuyerOrder().getShipped());
                        } else if (child.getId() == SAMPAI_TUJUAN) {
                            child.setBadge(data.getBuyerOrder().getArriveAtDestination());
                        }
                    } else if (item.getId() == PENJUALAN) {
                        if (child.getId() == PESANAN_BARU) {
                            child.setBadge(data.getSellerOrder().getNewOrder());
                        } else if (child.getId() == SIAP_DIKIRIM) {
                            child.setBadge(data.getSellerOrder().getReadyToShip());
                        } else if (child.getId() == SEDANG_DIKIRIM) {
                            child.setBadge(data.getSellerOrder().getShipped());
                        } else if (child.getId() == SAMPAI_TUJUAN) {
                            child.setBadge(data.getSellerOrder().getArriveAtDestination());
                        }
                    } else if (item.getId() == KOMPLAIN) {
                        if (child.getId() == BUYER) {
                            child.setBadge(data.getResolution().getBuyer());
                        } else if (child.getId() == SELLER) {
                            child.setBadge(data.getResolution().getSeller());
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private static Integer getNotifCenterUnread(NotifcenterUnread unread) {
        if (unread == null) {
            return 0;
        }

        return TextUtils.equals(unread.getNotifUnread(), NOTIF_99)
                ? NOTIF_99_NUMBER
                : IntegerUtil.tryParseInt(unread.getNotifUnread());
    }
}
