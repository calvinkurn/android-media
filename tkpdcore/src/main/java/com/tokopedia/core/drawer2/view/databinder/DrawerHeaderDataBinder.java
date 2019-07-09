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
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AnalyticsEventTrackingHelper;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;

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

        void onGotoTokoCard();
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

        ImageView avatar;
        TextView name;
        TextView deposit;
        TextView topPoint;
        TextView tokoCash;
        ImageView coverImg;
        LinearLayout drawerPointsLayout;
        RelativeLayout saldoLayout;
        RelativeLayout topPointsLayout;
        RelativeLayout tokoCashLayout;
        View loadingSaldo;
        View loadingLoyalty;
        View loadingTokoCash;
        TextView tokoCashLabel;
        ImageView retryTopCash;
        TextView tokoCashActivationButton;
        View drawerHeader;
        View tokoPointContainer;
        ImageView ivTokoPointBadge;
        TextView tvTokoPointAction;
        TextView tvTokoPointCount;
        RelativeLayout tokocardLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            percentText = (TextView) itemView.findViewById(R.id.percent_text);
            completeProfile = (TextView) itemView.findViewById(R.id.complete_profile);
            layoutProgress = itemView.findViewById(R.id.layout_progress);
            verifiedText = (TextView) itemView.findViewById(R.id.verified);
            verifiedIcon = itemView.findViewById(R.id.verified_icon);
            progressBar = (ProgressBar) itemView.findViewById(R.id.ProgressBar);

            avatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            name = (TextView) itemView.findViewById(R.id.name_text);
            deposit = (TextView) itemView.findViewById(R.id.deposit_text);
            topPoint = (TextView) itemView.findViewById(R.id.toppoints_text);
            tokoCash = (TextView) itemView.findViewById(R.id.top_cash_value);

            coverImg = (ImageView) itemView.findViewById(R.id.cover_img);
            drawerPointsLayout = (LinearLayout) itemView.findViewById(R.id.drawer_points_layout);
            saldoLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_saldo);
            topPointsLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_top_points);
            tokoCashLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_top_cash);
            loadingSaldo = (View) itemView.findViewById(R.id.loading_saldo);
            loadingLoyalty = (View) itemView.findViewById(R.id.loading_loyalty);
            loadingTokoCash = (View) itemView.findViewById(R.id.loading_top_cash);
            tokoCashLabel = (TextView) itemView.findViewById(R.id.toko_cash_label);
            retryTopCash = (ImageView) itemView.findViewById(R.id.retry_top_cash);
            tokoCashActivationButton = (TextView) itemView.findViewById(R.id.toko_cash_activation_button);
            drawerHeader = (View) itemView.findViewById(R.id.drawer_header);
            tokoPointContainer = (View) itemView.findViewById(R.id.tokopoint_container);
            ivTokoPointBadge = (ImageView) itemView.findViewById(R.id.iv_tokopoint_badge);
            tvTokoPointAction = (TextView) itemView.findViewById(R.id.tv_tokopoint_action);
            tvTokoPointCount = (TextView) itemView.findViewById(R.id.tv_tokopoint_count);
            tokocardLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_tokocard);
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
        deposit.setDeposit("");
        //deposit.setDeposit(drawerCache.getString(DRAWER_CACHE_DEPOSIT, ""));

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
        setTokoCard(holder);
        setListener(holder);
    }

    private void setTokoCard(ViewHolder holder){
        FirebaseRemoteConfigImpl remoteConfig = new FirebaseRemoteConfigImpl(context);
        boolean showTokoCard = remoteConfig.getBoolean(RemoteConfigKey.SHOW_TOKOCARD, true);

        if(!showTokoCard){
            holder.tokocardLayout.setVisibility(View.GONE);
        }
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
                    eventUserClickedPoints();
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

    private void eventUserClickedPoints(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                "clickHomePage",
                "homepage-tokopoints",
                "click point & tier status",
                "tokopoints");
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
                        AnalyticsEventTrackingHelper.homepageTokocashClick(v.getContext(), data.getDrawerTokoCash().getDrawerWalletAction().getRedirectUrlBalance());

                    } else {
                        listener.onWalletActionButtonClicked(
                                data.getDrawerTokoCash().getDrawerWalletAction().getRedirectUrlActionButton(),
                                data.getDrawerTokoCash().getDrawerWalletAction().getAppLinkActionButton()
                        );
                        AnalyticsEventTrackingHelper.hamburgerTokocashActivateClick(v.getContext());

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
        holder.tokocardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGotoTokoCard();
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
