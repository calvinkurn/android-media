package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventFavouriteActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.contractor.EventsContract;
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
 * Created by pranaymohapatra on 02/04/18.
 */

public class EventCategoryAdapterRevamp extends RecyclerView.Adapter<EventCategoryAdapterRevamp.ViewHolder> implements EventsContract.AdapterCallbacks {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;
    private int redColor;
    private int blackColor;
    private boolean isFavActivity;
    private boolean isTrackingEnabled;
    private EventsAnalytics eventsAnalytics;

    public EventCategoryAdapterRevamp(Context context, List<CategoryItemsViewModel> categoryItems, boolean isFav) {
        this.context = context;
        this.categoryItems = categoryItems;
        isFavActivity = isFav;
        if (!isFav)
            ((EventsHomeActivity) context).eventHomePresenter.setupCallback(this);
        redColor = context.getResources().getColor(R.color.red_a700);
        blackColor = context.getResources().getColor(R.color.black_54);
    }

    @Override
    public void notifyDatasetChanged(int position) {
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        private String hyphen = "%1$s - %2$s";

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }

        void setViewHolder(CategoryItemsViewModel data, int position) {
            index = position;
            tvCalendar.setVisibility(View.GONE);
            eventTitle.setText(data.getDisplayName());
            eventPrice.setText(String.format(context.getString(R.string.price_holder), CurrencyUtil.convertToCurrencyString(data.getSalesPrice())));
            eventLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.event_ic_putih, 0, 0, 0);
            tvEventShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_share, 0, 0, 0);
            eventLocation.setText(data.getCityName());
            eventTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_calendar_white, 0, 0, 0);
            if (data.getMinStartDate() == 0) {
                eventTime.setVisibility(View.GONE);
            } else {
                if (data.getMinStartDate() == data.getMaxEndDate())
                    eventTime.setText(Utils.getSingletonInstance().convertEpochToString(data.getMinStartDate()));
                else
                    eventTime.setText(String.format(hyphen, Utils.getSingletonInstance().convertEpochToString(data.getMinStartDate())
                            , Utils.getSingletonInstance().convertEpochToString(data.getMaxEndDate())));
                eventTime.setVisibility(View.VISIBLE);
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

            if (Utils.getSingletonInstance().containsUnlikeEvent(data.getId())) {
                data.setLiked(false);
                data.setLikes();
            } else if (Utils.getSingletonInstance().containsLikedEvent(data.getId())) {
                if (!data.isLiked()) {
                    data.setLiked(true);
                    data.setLikes();
                }
            }

            tvAddToWishlist.setText(String.valueOf(data.getLikes()));
            if (Utils.getSingletonInstance().containsLikedEvent(data.getId())) {
                tvAddToWishlist.setTextColor(redColor);
                tvAddToWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_wishlist_red,
                        0, 0, 0);
            } else {
                tvAddToWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_wishlist,
                        0, 0, 0);
                tvAddToWishlist.setTextColor(blackColor);

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
            Intent detailsIntent = new Intent(context, EventDetailsActivity.class);
            detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
            detailsIntent.putExtra("homedata", categoryItems.get(index));
            context.startActivity(detailsIntent);
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PRODUCT_CLICK,
                    categoryItems.get(getAdapterPosition()).getTitle()
                            + " - " + getAdapterPosition());
        }

        @OnClick(R2.id.tv_add_to_wishlist)
        public void toggleLike() {
            String like;
            String title = categoryItems.get(getAdapterPosition()).getTitle();
            if (categoryItems.get(getAdapterPosition()).isLiked())
                like = "unlike";
            else
                like = "like";
            if (!isFavActivity)
                ((EventsHomeActivity) context).eventHomePresenter.setEventLike(categoryItems.get(getAdapterPosition()), getAdapterPosition());
            else {
                ((EventFavouriteActivity) context).eventFavouritePresenter.removeEventLike(categoryItems.get(getAdapterPosition()), getAdapterPosition());
                CategoryItemsViewModel item = categoryItems.remove(getAdapterPosition());
                item.setLiked(false);
                itemRemoved(getAdapterPosition());
            }
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_LIKE,
                    title
                            + " - " + String.valueOf(getAdapterPosition())
                            + " - " + like);

        }

        @OnClick(R2.id.tv_event_share)
        public void shareEvent() {
            if (!isFavActivity)
                ((EventsHomeActivity) context).eventHomePresenter.shareEvent(categoryItems.get(getAdapterPosition()));
            else
                ((EventFavouriteActivity) context).eventFavouritePresenter.shareEvent(categoryItems.get(getAdapterPosition()));
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

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_category_item_revamp, parent, false);

        this.context = parent.getContext();
        eventsAnalytics = new EventsAnalytics();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryItemsViewModel model = categoryItems.get(position);
        holder.setViewHolder(model, position);
        holder.setShown(model.isTrack());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!holder.isShown() && isTrackingEnabled) {
            holder.setShown(true);
            categoryItems.get(holder.getIndex()).setTrack(true);
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PRODUCT_IMPRESSION, categoryItems.get(holder.getIndex()).getTitle()
                    + " - " + holder.getIndex());
        }
    }

    private void itemRemoved(int position) {
        notifyItemRemoved(position);
        if (categoryItems.size() == 0)
            ((EventFavouriteActivity) context).toggleEmptyLayout(View.VISIBLE);
    }

    public void enableTracking() {
        isTrackingEnabled = true;
    }

    public void disableTracking() {
        isTrackingEnabled = false;
    }

}
