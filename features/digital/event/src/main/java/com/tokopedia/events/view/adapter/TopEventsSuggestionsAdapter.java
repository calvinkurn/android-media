package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 25/01/18.
 */

public class TopEventsSuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        EventsContract.AdapterCallbacks {

    private Context mContext;
    private List<CategoryItemsViewModel> categoryItems;
    private EventSearchPresenter mPresenter;
    private String highLightText;
    private boolean showCards;
    private boolean isFooterAdded = false;
    private EventsAnalytics eventsAnalytics;

    private static final int ITEM = 1;
    private static final int FOOTER = 2;

    public TopEventsSuggestionsAdapter(Context context, List<CategoryItemsViewModel> categoryItems, EventSearchPresenter presenter, boolean showcards) {
        this.mContext = context;
        this.mPresenter = presenter;
        this.categoryItems = categoryItems;
        this.showCards = showcards;
        mPresenter.setupCallback(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder;
        this.mContext = parent.getContext();
        eventsAnalytics = new EventsAnalytics(mContext.getApplicationContext());
        View v;
        switch (viewType) {
            case ITEM:
                if (showCards) {
                    v = inflater.inflate(R.layout.event_category_item_revamp, parent, false);
                    holder = new EventsTitleHolder(v);
                } else {
                    v = inflater.inflate(R.layout.simple_recycler_item, parent, false);
                    holder = new EventsSuggestionHolder(v);
                }
                return holder;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                return holder;
            default:
                return new EventsTitleHolder(null);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                CategoryItemsViewModel model = categoryItems.get(position);
                if (showCards) {
                    ((EventsTitleHolder) holder).setViewHolder(model, position);
                    ((EventsTitleHolder) holder).setShown(model.isTrack());
                } else {
                    ((EventsSuggestionHolder) holder).bindView(position);
                }
                break;
            case FOOTER:
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public void setHighLightText(String text) {
        if (text != null && text.length() > 0) {
            String first = text.substring(0, 1).toUpperCase();
            highLightText = first + text.substring(1).toLowerCase();
        }
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new CategoryItemsViewModel());
        }
    }

    private CategoryItemsViewModel getItem(int position) {
        return categoryItems.get(position);
    }

    public void add(CategoryItemsViewModel item) {
        categoryItems.add(item);
        notifyItemInserted(categoryItems.size() - 1);
    }

    public void addAll(List<CategoryItemsViewModel> items) {
        for (CategoryItemsViewModel item : items) {
            add(item);
        }
    }

    private void remove(CategoryItemsViewModel item) {
        int position = categoryItems.indexOf(item);
        if (position > -1) {
            categoryItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            CategoryItemsViewModel item = getItem(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof EventsTitleHolder) {
            EventsTitleHolder titleHolder = (EventsTitleHolder) holder;
            if (!titleHolder.isShown()) {
                titleHolder.setShown(true);
                categoryItems.get(titleHolder.getAdapterPosition()).setTrack(true);
                eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_SEARCH_IMPRESSION,
                        highLightText
                                + " - " + categoryItems.get(titleHolder.getAdapterPosition()).getTitle()
                                + " - " + titleHolder.getAdapterPosition());
            }
        }
    }

    @Override
    public void notifyDatasetChanged(int position) {
        notifyItemChanged(position);
    }

    private SpannableString getBoldTitle(String title) {
        SpannableString spannableString = new SpannableString(title);
        try {
            if (!TextUtils.isEmpty(highLightText)
                    && Utils.containsIgnoreCase(title, highLightText)) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                int fromindex = title.toLowerCase().indexOf(highLightText.toLowerCase());
                int toIndex = fromindex + highLightText.length();
                spannableString.setSpan(styleSpan, fromindex, toIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return spannableString;
        } catch (Exception e) {
            e.printStackTrace();
            return spannableString;
        }
    }


    public class EventsTitleHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_add_to_wishlist)
        public TextView tvAddToWishlist;
        @BindView(R2.id.tv_event_share)
        public TextView tvEventShare;
        @BindView(R2.id.tv3_sold_cnt)
        public TextView tv3SoldCnt;
        @BindView(R2.id.event_category_cardview)
        public CardView eventCategoryCardview;
        @BindView(R2.id.tv4_event_title)
        public TextView eventTitle;
        @BindView(R2.id.tv1_price)
        public TextView eventPrice;
        @BindView(R2.id.iv_event_thumb)
        public ImageView eventImage;
        @BindView(R2.id.tv4_location)
        public TextView eventLocation;
        @BindView(R2.id.tv4_date_time)
        public TextView eventTime;
        @BindView(R2.id.tv3_tag)
        public TextView tvDisplayTag;
        @BindView(R2.id.tv_calendar)
        public TextView tvCalendar;
        private int index;
        private boolean isShown = false;

        EventsTitleHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }

        void setViewHolder(CategoryItemsViewModel data, int position) {
            index = position;

            String rp = "Rp %s";
            String hyphen = "%1$s - %2$s";
            eventTitle.setText(getBoldTitle(data.getDisplayName()));
            eventPrice.setText(String.format(rp, CurrencyUtil.convertToCurrencyString(data.getSalesPrice())));
            eventLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.event_ic_putih, 0, 0, 0);
            tvEventShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_share, 0, 0, 0);
            eventLocation.setText(data.getCityName());
            eventTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_calendar_white, 0, 0, 0);
            if (data.getMinStartDate() == 0) {
                eventTime.setVisibility(View.GONE);
                tvCalendar.setVisibility(View.GONE);
            } else {
                if (data.getMinStartDate() == data.getMaxEndDate())
                    eventTime.setText(Utils.getSingletonInstance().convertEpochToString(data.getMinStartDate()));
                else
                    eventTime.setText(String.format(hyphen, Utils.getSingletonInstance().convertEpochToString(data.getMinStartDate()),
                            Utils.getSingletonInstance().convertEpochToString(data.getMaxEndDate())));
                eventTime.setVisibility(View.VISIBLE);
                Utils.getSingletonInstance().setCalendar(tvCalendar,
                        Utils.getDateArray(Utils.getSingletonInstance().convertEpochToString(data.getMinStartDate())));
                tvCalendar.setVisibility(View.VISIBLE);
            }

            if (data.getDisplayTags() != null && data.getDisplayTags().length() > 3) {
                tvDisplayTag.setText(data.getDisplayTags());
                tvDisplayTag.setVisibility(View.VISIBLE);
            } else {
                tvDisplayTag.setVisibility(View.GONE);
            }

            if (data.getOfferText() != null && data.getOfferText().length() > 2) {
                tv3SoldCnt.setText(data.getOfferText());
                tv3SoldCnt.setVisibility(View.VISIBLE);
            } else {
                tv3SoldCnt.setVisibility(View.GONE);
            }
            if (Utils.getSingletonInstance().containsLikedEvent(data.getId())) {
                if (!data.isLiked()) {
                    data.setLiked(true);
                    data.setLikes();
                }
            } else {
                if (data.isLiked()) {
                    data.setLiked(false);
                    data.setLikes();
                }
            }
            tvAddToWishlist.setText(String.valueOf(data.getLikes()));
            if (Utils.getSingletonInstance().containsLikedEvent(data.getId())) {
                tvAddToWishlist.setTextColor(mContext.getResources().getColor(R.color.red_a700));
                tvAddToWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_wishlist_red,
                        0, 0, 0);
            } else {
                tvAddToWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_wishlist,
                        0, 0, 0);
                tvAddToWishlist.setTextColor(mContext.getResources().getColor(R.color.black_54));
            }

            ImageHandler.loadImageCover2(eventImage, categoryItems.get(getAdapterPosition()).getThumbnailApp());

        }

        @OnClick({
                R2.id.tv4_event_title,
                R2.id.iv_event_thumb,
                R2.id.tv4_location,
                R2.id.tv4_date_time,
                R2.id.tv3_sold_cnt})
        public void openEventDetails() {
            Intent detailsIntent = new Intent(mContext, EventDetailsActivity.class);
            detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
            detailsIntent.putExtra("homedata", categoryItems.get(index));
            mContext.startActivity(detailsIntent);
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_SEARCH_CLICK,
                    highLightText
                            + " - " + categoryItems.get(getAdapterPosition()).getTitle().toLowerCase()
                            + " - " + getAdapterPosition());
        }

        @OnClick(R2.id.tv_add_to_wishlist)
        public void toggleLike() {
            mPresenter.setEventLike(categoryItems.get(getAdapterPosition()), getAdapterPosition());
            String like;
            if (categoryItems.get(getAdapterPosition()).isLiked())
                like = "like";
            else
                like = "unlike";
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_LIKE,
                    categoryItems.get(getAdapterPosition()).getTitle()
                            + " - " + String.valueOf(getAdapterPosition())
                            + " - " + like);
        }

        @OnClick(R2.id.tv_event_share)
        public void shareEvent() {
            CategoryItemsViewModel item = categoryItems.get(getAdapterPosition());
            Utils.getSingletonInstance().shareEvent(mContext, item.getTitle(), item.getSeoUrl());
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_SHARE,
                    categoryItems.get(getAdapterPosition()).getTitle()
                            + " - " + String.valueOf(getAdapterPosition()));
        }

        public int getIndex() {
            return this.index;
        }

        boolean isShown() {
            return isShown;
        }

        void setShown(boolean shown) {
            isShown = shown;
        }
    }

    public class EventsSuggestionHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_simple_item)
        TextView tvTitle;

        EventsSuggestionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(int position) {
            tvTitle.setText(getBoldTitle(categoryItems.get(position).getDisplayName()));
        }

        @OnClick(R2.id.tv_simple_item)
        void onClick() {
            Intent detailsIntent = new Intent(mContext, EventDetailsActivity.class);
            detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
            detailsIntent.putExtra("homedata", categoryItems.get(getAdapterPosition()));
            mContext.startActivity(detailsIntent);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.loading_fl)
        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
