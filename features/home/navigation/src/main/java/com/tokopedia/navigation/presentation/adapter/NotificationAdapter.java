package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.navigation.domain.model.DrawerNotification;

import java.util.List;

import static com.tokopedia.navigation.data.GlobalNavConstant.BUYER;
import static com.tokopedia.navigation.data.GlobalNavConstant.KOMPLAIN;
import static com.tokopedia.navigation.data.GlobalNavConstant.MENUNGGU_KONFIRMASI;
import static com.tokopedia.navigation.data.GlobalNavConstant.PENJUALAN;
import static com.tokopedia.navigation.data.GlobalNavConstant.PESANAN_BARU;
import static com.tokopedia.navigation.data.GlobalNavConstant.PESANAN_DIPROSES;
import static com.tokopedia.navigation.data.GlobalNavConstant.SAMPAI_TUJUAN;
import static com.tokopedia.navigation.data.GlobalNavConstant.SEDANG_DIKIRIM;
import static com.tokopedia.navigation.data.GlobalNavConstant.SELLER;
import static com.tokopedia.navigation.data.GlobalNavConstant.PEMBELIAN;
import static com.tokopedia.navigation.data.GlobalNavConstant.SIAP_DIKIRIM;

/**
 * Created by meta on 03/07/18.
 */
public class NotificationAdapter extends BaseListAdapter<DrawerNotification, BaseViewHolder> {

    private static final int TYPE_ITEM = 2;
    private static final int TYPE_SINGLE = 3;

    public interface OnNotifClickListener {
        void onNotifClick(int parent, int child);
    }

    private OnNotifClickListener onNotifClickListener;

    public void setOnNotifClickListener(OnNotifClickListener onNotifClickListener) {
        this.onNotifClickListener = onNotifClickListener;
    }

    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        if(viewType == TYPE_SINGLE) {
            return R.layout.item_single_notification;
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

        private LabelView labelView;
        private View separator;

        NotificationSingleHolder(View itemView, BaseListAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            labelView = itemView.findViewById(R.id.labelview);
            separator = itemView.findViewById(R.id.separator);
        }

        @Override
        public void bind(DrawerNotification item) {
            DrawerNotification.ChildDrawerNotification child = item.getChilds().get(0);
            if (child == null)
                return;

            labelView.setTitle(child.getTitle());

            if (child.getBadge() != null && child.getBadge() > 0) {
                labelView.showRightArrow(false);
                labelView.setBadgeCounter(String.format("%s", child.getBadge()));
            } else {
                labelView.showRightArrow(true);
            }
            separator.setVisibility(View.GONE);
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
            adapter.setOnItemClickListener((view, position) -> {
                if (onNotifClickListener != null)
                    onNotifClickListener.onNotifClick(getAdapterPosition(), position);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    public void updateValue(NotificationsModel data) {
        if (this.items == null)
            return;

        for (DrawerNotification item : items) {
            if (item.getId() != null) {
                List<DrawerNotification.ChildDrawerNotification> childs = item.getChilds();
                for (DrawerNotification.ChildDrawerNotification child : childs) {
                    if (item.getId() == PEMBELIAN) {
                        if (child.getId() == MENUNGGU_KONFIRMASI) {
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
            } else {
                item.getChilds().get(0).setBadge(data.getSellerInfo().getNotification());
            }
        }
        notifyDataSetChanged();
    }
}
