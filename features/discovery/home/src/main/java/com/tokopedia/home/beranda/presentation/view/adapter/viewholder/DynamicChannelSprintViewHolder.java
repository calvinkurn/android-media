package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DateHelper;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.helper.TextViewHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;

import java.util.Date;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class DynamicChannelSprintViewHolder extends AbstractViewHolder<DynamicChannelViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.home_channel_3_image;
    private static final String TAG = DynamicChannelSprintViewHolder.class.getSimpleName();
    private TextView homeChannelTitle;
    private TextView seeAllButton;
    private HomeCategoryListener listener;
    private static CountDownView countDownView;
    private String sprintSaleExpiredText;
    private CountDownView.CountDownListener countDownListener;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;
    private static final int spanCount = 3;
    private Context context;

    public DynamicChannelSprintViewHolder(View itemView, HomeCategoryListener listener, CountDownView.CountDownListener countDownListener) {
        super(itemView);
        context = itemView.getContext();
        this.countDownListener = countDownListener;
        this.listener = listener;
        initResources(itemView.getContext());
        findViews(itemView);
        itemAdapter = new ItemAdapter(itemView.getContext(), listener);
        recyclerView.setAdapter(itemAdapter);
    }

    private void initResources(Context context) {
        sprintSaleExpiredText = context.getString(R.string.sprint_sale_expired_text);
    }

    private void findViews(View itemView) {
        homeChannelTitle = (TextView) itemView.findViewById(R.id.home_channel_title);
        seeAllButton = (TextView) itemView.findViewById(R.id.see_all_button);
        countDownView = itemView.findViewById(R.id.count_down);
        recyclerView = itemView.findViewById(R.id.recycleList);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10), true));
    }

    @Override
    public void bind(final DynamicChannelViewModel element) {
        try {
            final DynamicHomeChannel.Channels channel = element.getChannel();
            itemAdapter.setChannel(channel);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/NunitoSans-ExtraBold.ttf");
            homeChannelTitle.setTypeface(typeface);
            homeChannelTitle.setText(channel.getHeader().getName());

            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.getHeader()))) {
                seeAllButton.setVisibility(View.VISIBLE);
            } else {
                seeAllButton.setVisibility(View.GONE);
            }
            setupClickListeners(channel);

            if (isSprintSale(channel) || isSprintSaleLego(channel)) {
                Date expiredTime = DateHelper.getExpiredTime(channel.getHeader().getExpiredTime());
                countDownView.setup(element.getServerTimeOffset(), expiredTime, countDownListener);
                countDownView.setVisibility(View.VISIBLE);
            } else {
                countDownView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Crashlytics.log(0, TAG, e.getLocalizedMessage());
        }
    }

    private void setupClickListeners(final DynamicHomeChannel.Channels channel) {
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSprintSale(channel) || isSprintSaleLego(channel)) {
                    HomePageTracking.eventClickSeeAllProductSprint(context);
                } else {
                    HomePageTracking.eventClickSeeAllDynamicChannel(
                            context,
                            DynamicLinkHelper.getActionLink(channel.getHeader()));
                }
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.getHeader()), channel.getHomeAttribution());
            }
        });
    }

    private static boolean isSprintSale(DynamicHomeChannel.Channels channel) {
        return DynamicHomeChannel.Channels.LAYOUT_SPRINT.equals(channel.getLayout())
                || DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL.equals(channel.getLayout());
    }

    private static boolean isSprintSaleLego(DynamicHomeChannel.Channels channel) {
        return DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO.equals(channel.getLayout());
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private final HomeCategoryListener listener;
        private final Context context;
        private DynamicHomeChannel.Channels channel;
        private DynamicHomeChannel.Grid[] list;


        public ItemAdapter(Context context, HomeCategoryListener listener) {
            this.listener = listener;
            this.list = new DynamicHomeChannel.Grid[0];
            this.context = context;
        }

        public void setChannel(DynamicHomeChannel.Channels channel) {
            this.channel = channel;
            this.list = channel.getGrids();
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sprint_product_item_simple, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            try {
                final DynamicHomeChannel.Grid grid = list[position];
                if (grid != null) {
                    ImageHandler.loadImageThumbs(holder.getContext(),
                            holder.channelImage1, grid.getImageUrl());
                    TextViewHelper.displayText(holder.channelName, grid.getName());
                    TextViewHelper.displayText(holder.channelPrice1, grid.getPrice());
                    TextViewHelper.displayText(holder.channelDiscount1, grid.getDiscount());
                    TextViewHelper.displayText(holder.channelBeforeDiscPrice1, grid.getSlashedPrice());
                    TextViewHelper.displayText(holder.channelCashback, grid.getCashback());

                    holder.itemContainer1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isSprintSale(channel)) {
                                HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                                        channel.getEnhanceClickSprintSaleHomePage(position, countDownView.getCurrentCountDown()));
                                String attr = channel.getHomeAttribution(position + 1, channel.getGrids()[position].getId());
                                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid), attr);
                            } else if(isSprintSaleLego(channel)){
                                HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                                        channel.getEnhanceClickSprintSaleLegoHomePage(position, countDownView.getCurrentCountDown()));
                                String attr = channel.getHomeAttribution(position + 1, channel.getGrids()[position].getId());
                                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid), attr);
                            } else  {
                                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(context,
                                        channel.getEnhanceClickDynamicChannelHomePage(grid, position + 1));
                                String attr = channel.getHomeAttribution(position + 1, grid.getAttribution());
                                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid), attr);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Crashlytics.log(0, TAG, e.getLocalizedMessage());
            }
        }

        @Override
        public int getItemCount() {
            return list.length;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView channelImage1;
        private TextView channelName;
        private TextView channelPrice1;
        private TextView channelDiscount1;
        private TextView channelCashback;
        private TextView channelBeforeDiscPrice1;
        private RelativeLayout itemContainer1;
        private View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            channelImage1 = (ImageView) itemView.findViewById(R.id.channel_image_1);
            channelPrice1 = (TextView) itemView.findViewById(R.id.channel_price_1);
            channelCashback = (TextView) itemView.findViewById(R.id.channel_cashback);
            channelName = (TextView) itemView.findViewById(R.id.product_name);
            channelDiscount1 = (TextView) itemView.findViewById(R.id.channel_discount_1);
            channelBeforeDiscPrice1 = (TextView) itemView.findViewById(R.id.channel_before_disc_price_1);
            itemContainer1 = itemView.findViewById(R.id.channel_item_container_1);
            channelBeforeDiscPrice1.setPaintFlags(channelBeforeDiscPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public Context getContext() {
            return view.getContext();
        }
    }

}
