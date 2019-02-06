package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DateHelper;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;

import java.util.Date;

/**
 * Created by henrypriyono on 22/03/18.
 */

public class SixGridChannelViewHolder extends AbstractViewHolder<DynamicChannelViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.home_six_grid_channel;
    private static final String TAG = SixGridChannelViewHolder.class.getSimpleName();
    private final Context context;
    private View channelTitleContainer;
    private TextView channelTitle;
    private TextView seeAllButton;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;
    private static final int spanCount = 3;
    private HomeCategoryListener listener;
    private CountDownView countDownView;
    private CountDownView.CountDownListener countDownListener;

    public SixGridChannelViewHolder(View itemView,
                                    HomeCategoryListener listener,
                                    CountDownView.CountDownListener countDownListener) {
        super(itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        this.countDownListener = countDownListener;
        findViews(itemView);
        itemAdapter = new ItemAdapter(context, listener, getAdapterPosition());
        recyclerView.setAdapter(itemAdapter);
    }

    private void findViews(View itemView) {
        recyclerView = itemView.findViewById(R.id.recycleList);
        channelTitle = (TextView) itemView.findViewById(R.id.channel_title);
        channelTitleContainer = itemView.findViewById(R.id.channel_title_container);
        seeAllButton = (TextView) itemView.findViewById(R.id.see_all_button);
        countDownView = itemView.findViewById(R.id.count_down);
        recyclerView = itemView.findViewById(R.id.recycleList);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, 0, true));
    }

    @Override
    public void bind(DynamicChannelViewModel element) {
        try {
            final DynamicHomeChannel.Channels channel = element.getChannel();
            String titleText = element.getChannel().getHeader().getName();
            listener.onServerTimeReceived(channel.getHeader().getServerTimeUnix());
            if (!TextUtils.isEmpty(titleText)) {
                channelTitleContainer.setVisibility(View.VISIBLE);
                channelTitle.setText(titleText);
            } else {
                channelTitleContainer.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.getHeader()))) {
                seeAllButton.setVisibility(View.VISIBLE);
            } else {
                seeAllButton.setVisibility(View.GONE);
            }
            if (isSprintSale(channel)) {
                Date expiredTime = DateHelper.getExpiredTime(channel.getHeader().getExpiredTime());
                countDownView.setup(listener.getServerTimeOffset(), expiredTime, countDownListener);
                countDownView.setVisibility(View.VISIBLE);
            } else {
                countDownView.setVisibility(View.GONE);
            }
            seeAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.getHeader()), channel.getHomeAttribution());
                    HomeTrackingUtils.homeDiscoveryWidgetViewAll(context,
                            DynamicLinkHelper.getActionLink(channel.getHeader()));
                    HomePageTracking.eventClickSeeAllLegoBannerChannel(
                            context,
                            DynamicLinkHelper.getActionLink(channel.getHeader()));
                }
            });
            itemAdapter.setChannel(channel, getAdapterPosition());
        } catch (Exception e) {
            Crashlytics.log(0, TAG, e.getLocalizedMessage());
        }
    }

    private boolean isSprintSale(DynamicHomeChannel.Channels channel) {
        return channel.getHeader().getExpiredTime() != null
                && !TextUtils.isEmpty(channel.getHeader().getExpiredTime());
    }

    private static String getAvailableLink(String applink, String url) {
        if (!TextUtils.isEmpty(applink)) {
            return applink;
        } else {
            return url;
        }
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private final Context context;
        private DynamicHomeChannel.Grid[] list;
        DynamicHomeChannel.Channels channel;
        private final HomeCategoryListener listener;
        private int parentPosition = 0;

        public ItemAdapter(Context context, HomeCategoryListener listener, int position) {
            this.listener = listener;
            this.list = new DynamicHomeChannel.Grid[0];
            parentPosition = 0;
            this.context = context;
        }

        public void setChannel(DynamicHomeChannel.Channels channel, int position) {
            this.channel = channel;
            this.list = channel.getGrids();
            this.parentPosition = position;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lego_item, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            try {
                final DynamicHomeChannel.Grid grid = list[position];
                if (grid != null) {
                    ImageHandler.loadImageFitCenter(holder.getContext(), holder.imageView, grid.getImageUrl());
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                    holder.getContext(),
                                    channel.getEnhanceClickLegoBannerHomePage(grid, position + 1)
                            );
                            listener.onSixGridItemClicked(getAvailableLink(grid.getApplink(), grid.getUrl()),
                                    channel.getHomeAttribution(position + 1, grid.getAttribution()));
                            HomeTrackingUtils.homeDiscoveryWidgetClick(context,
                                    parentPosition + 1, grid,
                                    getAvailableLink(grid.getApplink(),
                                            grid.getUrl()),
                                    channel.getType());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list.length;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView imageView;
        private View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            imageView = itemView.findViewById(R.id.image);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}
