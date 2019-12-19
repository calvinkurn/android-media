package com.tokopedia.contactus.home.view.adapter;

import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {


    private List<CardView> mViews = new ArrayList<>();
    private List<BuyerPurchaseList> mData = new ArrayList<>();
    private float mBaseElevation;
    private int currentDataIndex;

    public void addData(List<BuyerPurchaseList> items) {
        mData.addAll(items);
        for (int i = 0; i < mData.size(); i++)
            mViews.add(null);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.order_list_item, container, false);
        container.addView(view);
        new ContactUsPurchaseViewHolder(view, "Home").bind(mData.get(position));

        CardView cardView = view.findViewById(R.id.base_cardview);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentDataIndex = position;
    }



}