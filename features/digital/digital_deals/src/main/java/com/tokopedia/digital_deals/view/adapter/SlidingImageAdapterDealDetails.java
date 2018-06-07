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


import java.util.List;


public class SlidingImageAdapterDealDetails extends PagerAdapter {


    private List<String> image;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapterDealDetails(Context context, List<String> image) {
        this.context = context;
        this.image = image;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.deal_image_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.deal_image);

        ImageHandler.loadImage(context, imageView, image.get(position), R.color.grey_1100, R.color.grey_1100);

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
