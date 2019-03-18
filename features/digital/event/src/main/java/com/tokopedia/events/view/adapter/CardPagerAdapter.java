package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.utils.CardAdapter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter, EventsContract.AdapterCallbacks {

    private List<CardView> mViews;
    private List<CategoryItemsViewModel> mData;
    private float mBaseElevation;
    private CardOnClickListener clickListener;
    private EventHomePresenter mPresenter;
    private int currentDataIndex;
    private ViewGroup parent;
    private int MAX_TOP = 5;
    private EventsAnalytics eventsAnalytics;
    private Context context;

    public CardPagerAdapter(EventHomePresenter presenter) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        clickListener = new CardOnClickListener();
        mPresenter = presenter;
        mPresenter.setupCallback(this);
    }

    public void addData(List<CategoryItemsViewModel> items) {
        int max = items.size() > MAX_TOP ? MAX_TOP : items.size();
        for (int i = 0; i < max; i++) {
            mData.add(items.get(i));
            mViews.add(null);
        }
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        parent = container;
        this.context = parent.getContext();
        Log.d("CardPagerAdapter", "View Created");
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.event_top_item_revamp, container, false);
        container.addView(view);
        view.setTag(position);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.event_category_cardview);

        eventsAnalytics = new EventsAnalytics();
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        cardView.setScaleY((float) (0.9));
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("CardPagerAdapter", "View Destroyed");
        container.removeView((View) object);
        mViews.set(position, null);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentDataIndex = position;
        if (!mData.get(currentDataIndex).isTrack()) {
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PRODUCT_IMPRESSION, mData.get(currentDataIndex).getTitle()
                    + " - " + currentDataIndex);
            mData.get(currentDataIndex).setTrack(true);
        }
    }

    private void bind(CategoryItemsViewModel item, View view) {
        TextView tv1Price = view.findViewById(R.id.tv1_price);
        tv1Price.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(item.getSalesPrice()));
        ImageView ivEventThumb = view.findViewById(R.id.iv_event_thumb);
        ivEventThumb.setOnClickListener(clickListener);
        TextView tv3Tag = view.findViewById(R.id.tv3_tag);
        if (item.getDisplayTags() != null && item.getDisplayTags().length() > 2)
            tv3Tag.setText(item.getDisplayTags());
        else
            tv3Tag.setVisibility(View.GONE);
        TextView tv4DateTime = view.findViewById(R.id.tv4_date_time);
        tv4DateTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_calendar_white, 0, 0, 0);
        if (item.getMinStartDate() == 0) {
            tv4DateTime.setVisibility(View.INVISIBLE);
        } else {
            if (item.getMinStartDate() == item.getMaxEndDate())
                tv4DateTime.setText(Utils.getSingletonInstance().convertEpochToString(item.getMinStartDate()));
            else
                tv4DateTime.setText(Utils.getSingletonInstance().convertEpochToString(item.getMinStartDate())
                        + " - " + Utils.getSingletonInstance().convertEpochToString(item.getMaxEndDate()));
            tv4DateTime.setVisibility(View.VISIBLE);
        }
        tv4DateTime.setOnClickListener(clickListener);
        TextView tv4EventTitle = view.findViewById(R.id.tv4_event_title);
        tv4EventTitle.setText(item.getDisplayName());
        tv4EventTitle.setOnClickListener(clickListener);
        ImageHandler.loadImageCover2(ivEventThumb, item.getThumbnailApp());
    }

    @Override
    public void notifyDatasetChanged(int position) {

    }

    private class CardOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_add_to_wishlist) {
                mPresenter.setEventLike(mData.get(currentDataIndex), currentDataIndex);
            } else if (id == R.id.tv_event_share) {
                mPresenter.shareEvent(mData.get(currentDataIndex));
            } else if (id == R.id.iv_event_thumb ||
                    id == R.id.tv4_event_title || id == R.id.tv4_location ||
                    id == R.id.tv4_date_time) {
                mPresenter.showEventDetails(mData.get(currentDataIndex));
            }
        }
    }

}