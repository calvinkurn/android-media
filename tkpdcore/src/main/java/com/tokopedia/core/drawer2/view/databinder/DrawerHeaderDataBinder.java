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
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.people.customview.PeopleInfoDetailView;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SessionHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerHeaderDataBinder extends DataBinder<DrawerHeaderDataBinder.ViewHolder> {

    public static final String DRAWER_CACHE_DEPOSIT = "DRAWER_CACHE_DEPOSIT";

    public interface DrawerHeaderListener {
        void onGoToDeposit();

        void onGoToProfile();

        void onGoToTopPoints(String topPointsUrl);

        void onGoToTopCash(String topCashUrl);

        void onGoToTopCashWithOtp(String topCashUrl);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.user_avatar)
        ImageView avatar;

        @BindView(R2.id.name_text)
        TextView name;

        @BindView(R2.id.deposit_text)
        TextView deposit;

        @BindView(R2.id.toppoints_text)
        TextView topPoint;

        @BindView(R2.id.top_cash_value)
        TextView tokoCash;

        @BindView(R2.id.cover_img)
        ImageView coverImg;

        @BindView(R2.id.gradient_black)
        RelativeLayout gradientBlack;

        @BindView(R2.id.drawer_points_layout)
        LinearLayout drawerPointsLayout;

        @BindView(R2.id.drawer_saldo)
        RelativeLayout saldoLayout;

        @BindView(R2.id.drawer_top_points)
        RelativeLayout topPointsLayout;

        @BindView(R2.id.drawer_top_cash)
        RelativeLayout tokoCashLayout;

        @BindView(R2.id.loading_saldo)
        View loadingSaldo;

        @BindView(R2.id.loading_loyalty)
        View loadingLoyalty;

        @BindView(R2.id.loading_top_cash)
        View loadingTokoCash;

        @BindView(R2.id.toko_cash_label)
        TextView tokoCashLabel;

        @BindView(R2.id.toko_cash_redirect_arrow)
        View tokoCashRedirectArrow;

        @BindView(R2.id.toko_cash_activation_button)
        TextView tokoCashActivationButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context context;
    private DrawerData data;
    private DrawerHeaderListener listener;

    public DrawerHeaderDataBinder(DataBindAdapter dataBindAdapter,
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
    public DrawerHeaderDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        return new DrawerHeaderDataBinder.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_header, parent, false));
    }

    @Override
    public void bindViewHolder(DrawerHeaderDataBinder.ViewHolder holder, int position) {

        if (SessionHandler.isV4Login(context))
            bindDrawerHeader(holder);
        else
            bindDrawerHeaderGuest(holder);

    }

    protected void bindDrawerHeaderGuest(ViewHolder holder) {
        holder.name.setVisibility(View.GONE);
        holder.avatar.setVisibility(View.GONE);
        ImageHandler.loadImageWithId(holder.coverImg, R.drawable.drawer_header_bg);
        holder.gradientBlack.setBackgroundResource(0);
        holder.drawerPointsLayout.setVisibility(View.GONE);
    }

    protected void bindDrawerHeader(ViewHolder holder) {
        holder.drawerPointsLayout.setVisibility(View.VISIBLE);
        holder.name.setVisibility(View.VISIBLE);
        holder.avatar.setVisibility(View.VISIBLE);

        setCover(holder);

        if (data.getDrawerProfile().getUserAvatar() != null && !data.getDrawerProfile().getUserAvatar().equals(""))
            ImageHandler.loadImageCircle2(context, holder.avatar, data.getDrawerProfile().getUserAvatar());

        holder.name.setText(data.getDrawerProfile().getUserName());

        setDeposit(holder);
        setTopPoints(holder);
        setTopCash(holder);

        setListener(holder);
    }


    private void setListener(ViewHolder holder) {
        holder.saldoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoToDeposit();
            }
        });
        holder.topPointsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoToTopPoints(data.getDrawerTopPoints().getTopPointsUrl());
            }
        });
        holder.tokoCashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUnregistered(data.getDrawerTokoCash()))
                listener.onGoToTopCash(data.getDrawerTokoCash().getRedirectUrl());
                else
                    listener.onGoToTopCashWithOtp(data.getDrawerTokoCash().getRedirectUrl());
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

    private void setTopCash(ViewHolder holder) {
        if (isTokoCashDisabled(data.getDrawerTokoCash())) {
            holder.tokoCashLayout.setVisibility(View.GONE);
        } else {
            if (isUnregistered(data.getDrawerTokoCash())) {
                showTokoCashActivateView(holder);
                holder.tokoCashActivationButton.setText(data.getDrawerTokoCash().getText());
            }else{
                showTokoCashBalanceView(holder);
                holder.tokoCash.setText(data.getDrawerTokoCash().getBalance());
                holder.tokoCashLabel.setText(data.getDrawerTokoCash().getText());

            }
            holder.loadingTokoCash.setVisibility(View.GONE);
        }
    }

    private void showTokoCashBalanceView(ViewHolder holder) {
        holder.tokoCashLayout.setVisibility(View.VISIBLE);
        holder.tokoCashActivationButton.setVisibility(View.GONE);
        holder.tokoCash.setVisibility(View.VISIBLE);
        holder.tokoCashRedirectArrow.setVisibility(View.VISIBLE);
    }

    private void showTokoCashActivateView(ViewHolder holder) {
        holder.tokoCashLayout.setVisibility(View.VISIBLE);
        holder.tokoCashRedirectArrow.setVisibility(View.GONE);
        holder.tokoCashActivationButton.setVisibility(View.VISIBLE);
        holder.tokoCash.setVisibility(View.GONE);
    }

    private boolean isUnregistered(DrawerTokoCash drawerTokoCash) {
        return drawerTokoCash.getLink() != 1;
    }

    private boolean isTokoCashDisabled(DrawerTokoCash drawerTokoCash) {
        return drawerTokoCash.getText() == null
                || drawerTokoCash.getText().equals("");
    }

    private void setTopPoints(ViewHolder holder) {
        if (data.getDrawerTopPoints().getTopPoints().equals("")) {
            holder.loadingLoyalty.setVisibility(View.VISIBLE);
            holder.topPoint.setVisibility(View.GONE);
        } else {
            holder.loadingLoyalty.setVisibility(View.GONE);
            holder.topPoint.setVisibility(View.VISIBLE);
            holder.topPoint.setText(data.getDrawerTopPoints().getTopPoints());
        }
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
        if (data.getDrawerProfile().getShopCover() != null && !data.getDrawerProfile().getShopCover().equals("")) {
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
