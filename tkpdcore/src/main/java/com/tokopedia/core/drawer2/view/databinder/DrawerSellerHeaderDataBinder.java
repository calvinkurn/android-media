package com.tokopedia.core.drawer2.view.databinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

        void onGoToProfileCompletion();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView percentText;
        private final TextView completeProfile;
        private final TextView verifiedText;
        private final View verifiedIcon;
        private final View layoutProgress;
        private final ProgressBar progressBar;

        @BindView(R2.id.user_avatar)
        ImageView avatar;

        @BindView(R2.id.name_text)
        TextView name;

        @BindView(R2.id.deposit_text)
        TextView deposit;

        @BindView(R2.id.cover_img)
        ImageView coverImg;

        @BindView(R2.id.drawer_points_layout)
        LinearLayout drawerPointsLayout;

        @BindView(R2.id.drawer_saldo)
        RelativeLayout saldoLayout;

        @BindView(R2.id.loading_saldo)
        View loadingSaldo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            percentText = (TextView) itemView.findViewById(R.id.percent_text);
            completeProfile = (TextView) itemView.findViewById(R.id.complete_profile);
            layoutProgress = itemView.findViewById(R.id.layout_progress);
            verifiedText = (TextView) itemView.findViewById(R.id.verified);
            verifiedIcon = itemView.findViewById(R.id.verified_icon);
            progressBar = (ProgressBar) itemView.findViewById(R.id.ProgressBar);

        }
    }

    private Context context;
    private DrawerData data;
    private DrawerHeaderListener listener;
    private String oldUserAvatar = "";

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
//        holder.percentText.setVisibility(View.VISIBLE);

        if (data.getDrawerProfile().getUserAvatar() != null &&
                !oldUserAvatar.equals(data.getDrawerProfile().getUserAvatar())) {
            ImageHandler.loadImage(context,
                    holder.avatar,
                    data.getDrawerProfile().getUserAvatar(),
                    R.drawable.ic_image_avatar_boy,
                    R.drawable.ic_image_avatar_boy);
            oldUserAvatar = data.getDrawerProfile().getUserAvatar();
        }
        holder.name.setText(data.getDrawerProfile().getUserName());
//        holder.percentText.setText(String.format("%s%%", String.valueOf(data.getProfileCompletion())));
//        if(data.getProfileCompletion() == 100) {
//            holder.layoutProgress.setVisibility(View.GONE);
//            holder.verifiedIcon.setVisibility(View.VISIBLE);
//            holder.verifiedText.setVisibility(View.VISIBLE);
//        }else {
//            holder.progressBar.setProgress(data.getProfileCompletion());
//            holder.layoutProgress.setVisibility(View.VISIBLE);
//            holder.verifiedIcon.setVisibility(View.GONE);
//            holder.verifiedText.setVisibility(View.GONE);
//        }
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
        holder.completeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGoToProfileCompletion();
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

    @Override
    public int getItemCount() {
        return 1;
    }
}
