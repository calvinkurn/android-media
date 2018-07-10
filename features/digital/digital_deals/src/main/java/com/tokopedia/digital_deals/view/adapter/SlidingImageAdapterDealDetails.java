package com.tokopedia.digital_deals.view.adapter;


import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.model.Media;


import java.util.ArrayList;
import java.util.List;


public class SlidingImageAdapterDealDetails extends PagerAdapter {


    private List<String> images;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapterDealDetails(Context context, List<Media> mediaList) {
        this.context = context;
        if (mediaList != null) {
            this.images=new ArrayList<>();
            for (Media media : mediaList) {
                if (media != null && media.getUrl() != null && media.getUrl().length() > 0)
                    this.images.add(media.getUrl());
            }
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (images != null) {
            return images.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.deal_image_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.deal_image);

        ImageHandler.loadImage(context, imageView, images.get(position), R.color.grey_1100, R.color.grey_1100);

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
