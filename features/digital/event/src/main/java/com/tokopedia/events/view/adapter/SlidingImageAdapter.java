package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class SlidingImageAdapter extends PagerAdapter {


    private List<CategoryItemsViewModel> mData;
    private List<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private EventHomePresenter mPresenter;
    private EventsAnalytics eventsAnalytics;
    List<CategoryItemsViewModel> itemsForGA = new ArrayList<>();


    public SlidingImageAdapter(Context context, List<String> IMAGES, EventHomePresenter presenter) {
        this.context = context;
        this.IMAGES = IMAGES;
        this.mPresenter = presenter;
        inflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.evnt_banner_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.banner_item);

        eventsAnalytics = new EventsAnalytics();
        ImageHandler.loadImageCover2(imageView, IMAGES.get(position));
        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsAnalytics.sendPromoClickEvent(mData.get(position), position);
                mPresenter.onClickBanner();
            }
        });
        itemsForGA.add(mData.get(position));
        int  itemsToSend = (mData.size() - 1) - position;
        if (itemsForGA != null && (itemsToSend == 0 || itemsForGA.size() == Utils.MAX_ITEMS_FOR_GA)) {
            eventsAnalytics.sendPromoImpressions(EventsAnalytics.EVENT_PROMO_VIEW, EventsAnalytics.DIGITAL_EVENT, EventsAnalytics.ACTION_PROMO_IMPRESSION, itemsForGA);
            itemsForGA.clear();
        }
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);

    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void addItems(List<CategoryItemsViewModel> items) {
        mData.addAll(items);
    }
}
