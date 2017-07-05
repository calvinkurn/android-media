package com.tokopedia.core.drawer2.view.databinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.core.drawer2.data.source.CloudDepositSource.DRAWER_CACHE_DEPOSIT;

/**
 * Created by nisie on 5/6/17.
 */

public class DrawerSellerHeaderDataBinder extends DataBinder<DrawerSellerHeaderDataBinder.ViewHolder> {

    public interface DrawerHeaderListener {
        void onGoToDeposit();

        void onGoToProfile();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.user_avatar)
        ImageView avatar;

        @BindView(R2.id.name_text)
        TextView name;

        @BindView(R2.id.deposit_text)
        TextView deposit;

        @BindView(R2.id.cover_img)
        ImageView coverImg;

        @BindView(R2.id.gradient_black)
        RelativeLayout gradientBlack;

        @BindView(R2.id.drawer_points_layout)
        LinearLayout drawerPointsLayout;

        @BindView(R2.id.drawer_saldo)
        RelativeLayout saldoLayout;

        @BindView(R2.id.loading_saldo)
        View loadingSaldo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context context;
    private DrawerData data;
    private DrawerHeaderListener listener;

    public DrawerSellerHeaderDataBinder(DataBindAdapter dataBindAdapter,
                                        Context context,
                                        DrawerHeaderListener listener,
                                        LocalCacheHandler drawerCache) {
        super(dataBindAdapter);
        this.context = context;
        this.data = createDataFromCache(drawerCache);
        this.listener = listener;
    }

    private DrawerData createDataFromCache(LocalCacheHandler drawerCache) {
        DrawerData data = new DrawerData();
        DrawerDeposit deposit = new DrawerDeposit();
        deposit.setDeposit(drawerCache.getString(DRAWER_CACHE_DEPOSIT, ""));

        data.setDrawerDeposit(deposit);
        return data;
    }

    public void setData(DrawerData data) {
        data = data;
    }

    public DrawerData getData() {
        return data;
    }

    @Override
    public DrawerSellerHeaderDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        return new DrawerSellerHeaderDataBinder.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_header_seller, parent, false));
    }


    @Override
    public void bindViewHolder(DrawerSellerHeaderDataBinder.ViewHolder holder, int position) {
        holder.drawerPointsLayout.setVisibility(View.VISIBLE);
        holder.name.setVisibility(View.VISIBLE);
        holder.avatar.setVisibility(View.VISIBLE);

//        setCover(holder);

        if (data.getDrawerProfile().getUserAvatar() != null
                && !data.getDrawerProfile().getUserAvatar().equals(""))
            ImageHandler.LoadImage(holder.avatar, data.getDrawerProfile().getUserAvatar());

        holder.name.setText(data.getDrawerProfile().getUserName());

        setDeposit(holder);
        setListener(holder);
    }

    private void setListener(ViewHolder holder) {
        holder.saldoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoToDeposit();
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoToProfile();
            }
        });
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoToProfile();
            }
        });
    }

    private void setDeposit(ViewHolder holder) {
        if (data.getDrawerDeposit().getDeposit().equals("")) {
            holder.loadingSaldo.setVisibility(View.VISIBLE);
            holder.deposit.setVisibility(View.GONE);
        } else {
            holder.loadingSaldo.setVisibility(View.GONE);
            holder.deposit.setText(data.getDrawerDeposit().getDeposit());
            holder.deposit.setVisibility(View.VISIBLE);
        }
    }

    private void setCover(ViewHolder holder) {
        if (data.getDrawerProfile().getShopCover() != null
                && !data.getDrawerProfile().getShopCover().equals("")) {
            holder.gradientBlack.setBackgroundResource(R.drawable.gradient_black);
            holder.coverImg.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(holder.coverImg, data.getDrawerProfile().getShopCover());
            holder.name.setShadowLayer(1.5f, 2, 2, R.color.trans_black_40);
        } else {
            holder.gradientBlack.setBackgroundResource(0);
            holder.coverImg.setVisibility(View.INVISIBLE);
            holder.name.setShadowLayer(0, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
