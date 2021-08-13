package com.tokopedia.digital_deals.view.adapter;


import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;;
import com.tokopedia.digital_deals.view.model.Media;


import java.util.ArrayList;
import java.util.List;


public class SlidingImageAdapterDealDetails extends PagerAdapter {


    private List<String> images;
    private Context context;


    public SlidingImageAdapterDealDetails(List<Media> mediaList) {
        if (mediaList != null) {
            this.images=new ArrayList<>();
            for (Media media : mediaList) {
                if (media != null && media.getUrl() != null && media.getUrl().length() > 0)
                    this.images.add(media.getUrl());
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return (images == null) ? 0 : images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        this.context=view.getContext();
        View imageLayout = LayoutInflater.from(context).inflate(com.tokopedia.digital_deals.R.layout.deal_image_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(com.tokopedia.digital_deals.R.id.deal_image);

        ImageHandler.loadImage(context, imageView, images.get(position), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);

        view.addView(imageLayout, 0);

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
}
