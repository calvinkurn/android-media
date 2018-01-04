package com.tokopedia.core.drawer2.view.databinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SessionHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.core.drawer2.data.source.CloudDepositSource.DRAWER_CACHE_DEPOSIT;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerHeaderDataBinder extends DataBinder<DrawerHeaderDataBinder.ViewHolder> {

    public interface DrawerHeaderListener {
        void onGoToDeposit();

        void onGoToProfile();

        void onGoToTopPoints(String topPointsUrl);

        void onGoToProfileCompletion();

        void onWalletBalanceClicked(String redirectUrlBalance, String appLinkBalance);

        void onWalletActionButtonClicked(String redirectUrlActionButton, String appLinkActionButton);

        void onTokoPointActionClicked(String mainPageUrl, String title);
    }

    public interface RetryTokoCashListener {
        void onRetryTokoCash();
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

        @BindView(R2.id.toppoints_text)
        TextView topPoint;

        @BindView(R2.id.top_cash_value)
        TextView tokoCash;

        @BindView(R2.id.cover_img)
        ImageView coverImg;

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

        @BindView(R2.id.retry_top_cash)
        ImageView retryTopCash;

        @BindView(R2.id.toko_cash_activation_button)
        TextView tokoCashActivationButton;


        @BindView(R2.id.drawer_header)
        View drawerHeader;


        @BindView(R2.id.tokopoint_container)
        View tokoPointContainer;
        @BindView(R2.id.iv_tokopoint_badge)
        ImageView ivTokoPointBadge;
        @BindView(R2.id.tv_tokopoint_action)
        TextView tvTokoPointAction;
        @BindView(R2.id.tv_tokopoint_count)
        TextView tvTokoPointCount;


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
    private RetryTokoCashListener tokoCashListener;
    private String oldUserAvatar = "";

    public DrawerHeaderDataBinder(DataBindAdapter dataBindAdapter,
                                  Context context,
                                  DrawerHeaderListener listener,
                                  LocalCacheHandler drawerCache) {
        super(dataBindAdapter);
        this.context = context;
        this.data = createDataFromCache(drawerCache);
        this.listener = listener;
        if (context instanceof RetryTokoCashListener) {
            this.tokoCashListener = (RetryTokoCashListener) context;
        }
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
        holder.coverImg.setVisibility(View.VISIBLE);
        holder.drawerPointsLayout.setVisibility(View.GONE);
        holder.percentText.setVisibility(View.GONE);
        holder.layoutProgress.setVisibility(View.GONE);
        holder.verifiedIcon.setVisibility(View.GONE);
        holder.verifiedText.setVisibility(View.GONE);
        holder.drawerHeader.setVisibility(View.GONE);
        ImageHandler.loadImageWithId(holder.coverImg, R.drawable.drawer_header_bg);
    }

    protected void bindDrawerHeader(ViewHolder holder) {
        holder.drawerHeader.setVisibility(View.VISIBLE);
        holder.drawerPointsLayout.setVisibility(View.VISIBLE);
        holder.coverImg.setVisibility(View.GONE);

        holder.name.setVisibility(View.VISIBLE);
        holder.avatar.setVisibility(View.VISIBLE);
        holder.percentText.setVisibility(View.VISIBLE);


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
        holder.percentText.setText(String.format("%s%%", String.valueOf(data.getProfileCompletion())));
        if (data.getProfileCompletion() == 100) {
            holder.layoutProgress.setVisibility(View.GONE);
            holder.verifiedIcon.setVisibility(View.VISIBLE);
            holder.verifiedText.setVisibility(View.VISIBLE);
        } else {
            holder.progressBar.setProgress(data.getProfileCompletion());
            holder.layoutProgress.setVisibility(View.VISIBLE);
            holder.verifiedIcon.setVisibility(View.GONE);
            holder.verifiedText.setVisibility(View.GONE);
        }

        setDeposit(holder);
        setTopPoints(holder);
        setTopCash(holder);
        setTokoPoint(holder);
        setListener(holder);
    }

    @SuppressLint("SetTextI18n")
    private void setTokoPoint(ViewHolder holder) {
        final String TITLE_HEADER_WEBSITE = "TokoPoints";
        if (data.getTokoPointDrawerData() != null && data.getTokoPointDrawerData().getOffFlag() == 0) {
            holder.tokoPointContainer.setVisibility(View.VISIBLE);
            final String title = data.getTokoPointDrawerData().getMainPageTitle();
            holder.tvTokoPointAction.setText(TextUtils.isEmpty(title) ? TITLE_HEADER_WEBSITE : "TokoPoints");
            ImageHandler.loadImageThumbs(context,
                    holder.ivTokoPointBadge,
                    data.getTokoPointDrawerData().getUserTier().getTierImageUrl());
            holder.tvTokoPointCount.setText(data.getTokoPointDrawerData().getUserTier().getRewardPointsStr());
            holder.tvTokoPointAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UnifyTracking.eventUserClickedPoints();
                    listener.onTokoPointActionClicked(
                            data.getTokoPointDrawerData().getMainPageUrl(),
                            TextUtils.isEmpty(title) ? TITLE_HEADER_WEBSITE : title
                    );
                }
            });
        } else {
            holder.tokoPointContainer.setVisibility(View.GONE);
        }
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
                if (data.getDrawerTopPoints() != null
                        && data.getDrawerTopPoints().getTopPointsUrl() != null)
                    listener.onGoToTopPoints(data.getDrawerTopPoints().getTopPointsUrl());
            }
        });
        holder.tokoCashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getDrawerTokoCash() != null && data.getDrawerTokoCash().getDrawerWalletAction() != null) {
                    if (isRegistered(data.getDrawerTokoCash())) {
                        listener.onWalletBalanceClicked(
                                data.getDrawerTokoCash().getDrawerWalletAction().getRedirectUrlBalance(),
                                data.getDrawerTokoCash().getDrawerWalletAction().getAppLinkBalance()
                        );
                    } else {
                        listener.onWalletActionButtonClicked(
                                data.getDrawerTokoCash().getDrawerWalletAction().getRedirectUrlActionButton(),
                                data.getDrawerTokoCash().getDrawerWalletAction().getAppLinkActionButton()
                        );
                    }
                } else {
                    tokoCashListener.onRetryTokoCash();
                }
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

    private void setTopCash(ViewHolder holder) {
        holder.loadingTokoCash.setVisibility(View.VISIBLE);
        if (isTokoCashDisabled(data.getDrawerTokoCash())) {
            holder.loadingTokoCash.setVisibility(View.GONE);
            holder.retryTopCash.setVisibility(View.VISIBLE);
        } else {
            if (data.getDrawerTokoCash().getDrawerWalletAction().getTypeAction()
                    == DrawerWalletAction.TYPE_ACTION_BALANCE) {
                showTokoCashBalanceView(holder);
                holder.tokoCash.setText(data.getDrawerTokoCash().getDrawerWalletAction().getBalance());
                holder.tokoCashLabel.setText(data.getDrawerTokoCash().getDrawerWalletAction().getLabelTitle());
            } else {
                showTokoCashActivateView(holder);
                holder.tokoCashActivationButton.setText(data.getDrawerTokoCash()
                        .getDrawerWalletAction().getLabelActionButton());
            }
            holder.loadingTokoCash.setVisibility(View.GONE);
            holder.retryTopCash.setVisibility(View.GONE);
        }
    }

    private void showTokoCashBalanceView(ViewHolder holder) {
        holder.tokoCashLayout.setVisibility(View.VISIBLE);
        holder.tokoCashActivationButton.setVisibility(View.GONE);
        holder.tokoCash.setVisibility(View.VISIBLE);
    }

    private void showTokoCashActivateView(ViewHolder holder) {
        holder.tokoCashLayout.setVisibility(View.VISIBLE);
        holder.tokoCashActivationButton.setVisibility(View.VISIBLE);
        holder.tokoCash.setVisibility(View.GONE);
    }

    private boolean isRegistered(DrawerTokoCash drawerTokoCash) {
        return drawerTokoCash.getDrawerWalletAction().getTypeAction()
                == DrawerWalletAction.TYPE_ACTION_BALANCE;
    }

    private boolean isTokoCashDisabled(DrawerTokoCash drawerTokoCash) {
        return drawerTokoCash == null || drawerTokoCash.getDrawerWalletAction() == null
                || drawerTokoCash.getDrawerWalletAction().getLabelTitle() == null
                || drawerTokoCash.getDrawerWalletAction().getLabelTitle().equals("");
    }

    private void setTopPoints(ViewHolder holder) {
        if (!data.getDrawerTopPoints().isActive()) {
            holder.topPointsLayout.setVisibility(View.GONE);
        } else if (data.getDrawerTopPoints().getTopPoints().equals("")) {
            holder.topPointsLayout.setVisibility(View.VISIBLE);
            holder.loadingLoyalty.setVisibility(View.VISIBLE);
            holder.topPoint.setVisibility(View.GONE);
        } else {
            holder.topPointsLayout.setVisibility(View.VISIBLE);
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

    @Override
    public int getItemCount() {
        return 1;
    }

}
