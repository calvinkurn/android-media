package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DateHelper;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.helper.GravitySnapHelper;
import com.tokopedia.home.beranda.listener.GridItemClickListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeAbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.unifyprinciples.Typography;

import java.util.Date;
import java.util.Map;

/**
 * Created by errysuprayogi on 3/22/18.
 */

/**
 * No further development for this viewholder
 * Backend possibly return this layout for version android  >= 2.19
 */

@Deprecated
public class SprintSaleCarouselViewHolder extends HomeAbstractViewHolder<DynamicChannelViewModel>
        implements GridItemClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_sprint_card_item;
    private static final String TAG = SprintSaleCarouselViewHolder.class.getSimpleName();
    public static final String ATTRIBUTION = "attribution";
    private RelativeLayout container;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private Context context;
    private Typography title;
    private TextView seeMore;
    private ImageView headerBg;
    private CountDownView countDownView;
    private HomeCategoryListener listener;
    private DynamicHomeChannel.Channels channels;
    private CountDownView.CountDownListener countDownListener;

    public SprintSaleCarouselViewHolder(View itemView, HomeCategoryListener listener, CountDownView.CountDownListener countDownListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        this.countDownListener = countDownListener;
        itemAdapter = new ItemAdapter();
        countDownView = itemView.findViewById(R.id.count_down);
        container = itemView.findViewById(R.id.container);
        headerBg = itemView.findViewById(R.id.header_bg);
        title = itemView.findViewById(R.id.channel_title);
        title.setSelected(true);
        seeMore = itemView.findViewById(R.id.see_all_button);
        recyclerView = itemView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new SpacingItemDecoration(convertDpToPixel(16, context), SpacingItemDecoration.Companion.getHORIZONTAL()));
        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(recyclerView);
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    @Override
    public void onGridItemClick(int pos, DynamicHomeChannel.Grid grid) {
        Map<String, Object> evenMap = channels.getEnhanceClickSprintSaleCarouselHomePage(pos,
                countDownView.getCurrentCountDown(), grid.getLabel());
        HomePageTracking.eventEnhancedClickSprintSaleProduct(
                context,
                evenMap);

        listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid));
        HomeTrackingUtils.homeSprintSaleClick(context,
                pos+1,channels,grid,DynamicLinkHelper.getActionLink(grid));
    }

    @Override
    public void bind(DynamicChannelViewModel element) {
        try {
            this.channels = element.getChannel();
            title.setText(channels.getHeader().getName());
            if (channels.getHeader().getBackColor() != null) {
                Glide.with(context).load(channels.getHeader().getBackImage()).into(headerBg);
            }
            itemAdapter.setList(channels.getGrids());
            itemAdapter.setGridItemClickListener(this);
            HomeTrackingUtils.homeSprintSaleImpression(context,
                    channels.getGrids(),channels.getType());
            Date expiredTime = DateHelper.getExpiredTime(channels.getHeader().getExpiredTime());
            if (!DateHelper.isExpired(element.getServerTimeOffset(), expiredTime)){
                countDownView.setup(element.getServerTimeOffset(), expiredTime, countDownListener);
                countDownView.setVisibility(View.VISIBLE);
            } else {
                countDownView.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channels.getHeader()))) {
                seeMore.setVisibility(View.VISIBLE);
            } else {
                seeMore.setVisibility(View.GONE);
            }
            seeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickSeeAll();
                }
            });
            String color = channels.getHeader().getBackColor();
            if (color != null && !color.isEmpty()) {
                container.setBackgroundColor(Color.parseColor(color));
            }
        } catch (Exception e) {
            if(!GlobalConfig.DEBUG) {
                Crashlytics.log(0, TAG, e.getLocalizedMessage());
            }
        }
    }

    private void onClickSeeAll() {
        listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channels.getHeader()));
        HomePageTracking.eventClickSeeAllProductSprintBackground(context, channels.getId());
        HomeTrackingUtils.homeSprintSaleViewAll(context,
                DynamicLinkHelper.getActionLink(channels.getHeader()));
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private DynamicHomeChannel.Grid[] list;
        private GridItemClickListener gridItemClickListener;

        public ItemAdapter() {
            this.list = new DynamicHomeChannel.Grid[0];
        }

        public void setGridItemClickListener(GridItemClickListener gridItemClickListener) {
            this.gridItemClickListener = gridItemClickListener;
        }

        public void setList(DynamicHomeChannel.Grid[] list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sprint_product_item, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            try {
                final DynamicHomeChannel.Grid grid = list[position];
                ImageHandler.loadImageThumbs(holder.getContext(), holder.imageView, grid.getImageUrl());
                holder.price1.setText(grid.getSlashedPrice());
                holder.price1.setPaintFlags(holder.price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.price2.setText(grid.getPrice());
                holder.stockStatus.setText(grid.getLabel());
                if (grid.getDiscount().isEmpty()) {
                    holder.channelDiscount.setVisibility(View.GONE);
                } else {
                    holder.channelDiscount.setVisibility(View.VISIBLE);
                    holder.channelDiscount.setText(grid.getDiscount());
                }
                if (grid.getLabel().equalsIgnoreCase(holder.getContext().getString(R.string.hampir_habis))) {
                    holder.stockStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_flame, 0, 0, 0);
                } else {
                    holder.stockStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (grid.getSoldPercentage() >= 100) {
                    holder.stockProgress.setVisibility(View.INVISIBLE);
                    holder.channelDiscount.setEnabled(false);
                } else {
                    holder.stockProgress.setProgress(grid.getSoldPercentage());
                    holder.stockProgress.setVisibility(View.VISIBLE);
                    holder.channelDiscount.setEnabled(true);
                }
                if (gridItemClickListener != null && grid != null) {
                    holder.countainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gridItemClickListener.onGridItemClick(position, grid);
                        }
                    });
                } else {
                    holder.countainer.setOnClickListener(null);
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

        public CardView countainer;
        public ImageView imageView;
        public TextView channelDiscount;
        public TextView price1;
        public TextView price2;
        public TextViewCompat stockStatus;
        public ProgressBar stockProgress;
        public View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            countainer = itemView.findViewById(R.id.container);
            imageView = itemView.findViewById(R.id.image);
            channelDiscount = itemView.findViewById(R.id.channel_discount);
            price1 = itemView.findViewById(R.id.price1);
            price2 = itemView.findViewById(R.id.price2);
            stockStatus = itemView.findViewById(R.id.stock_status);
            stockProgress = itemView.findViewById(R.id.stock_progress);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setBackgroundTintList(stockProgress,
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(),
                                R.color.grey_hint_full)));
            } else {
                stockProgress.setBackgroundTintList(ColorStateList.valueOf(ContextCompat
                        .getColor(itemView.getContext(), R.color.grey_hint_full)));
            }
        }

        public Context getContext() {
            return view.getContext();
        }
    }
}
