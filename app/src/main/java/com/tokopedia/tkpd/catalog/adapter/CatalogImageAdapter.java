package com.tokopedia.tkpd.catalog.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.catalog.model.CatalogImage;

import java.util.List;


/**
 * @author anggaprasetiyo on 10/18/16.
 */

public class CatalogImageAdapter extends PagerAdapter {

    private final Activity activity;
    private final List<CatalogImage> catalogImageList;

    public CatalogImageAdapter(Activity activity, List<CatalogImage> catalogImageList) {
        this.activity = activity;
        this.catalogImageList = catalogImageList;
    }

    @Override
    public int getCount() {
        return catalogImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(activity);
        ImageHandler.loadImageWithoutFit(activity, imageView,
                catalogImageList.get(position).getImageSrc());
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
