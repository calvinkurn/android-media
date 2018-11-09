package com.tokopedia.tkpd.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tkpd.home.TkpdYoutubeVideoActivity;
import com.tokopedia.tkpd.home.analytics.HomeGATracking;
import com.tokopedia.tkpd.home.model.VideoPushBannerModel;

import java.util.List;

/**
 * Created by Ashwani Tyagi on 09/11/18.
 */
public class SlidingImageBannerAdapter extends PagerAdapter {

    private List<VideoPushBannerModel> bannerModelList;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImageBannerAdapter(Context context, List<VideoPushBannerModel> bannerModelList) {
        this.context = context;
        this.bannerModelList = bannerModelList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerModelList.size();
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(com.tokopedia.events.R.layout.video_push_banner_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(com.tokopedia.events.R.id.banner_item);

        ImageHandler.loadImageCover2(imageView, bannerModelList.get(position).getBannerImg());
        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(bannerModelList.get(position).getLink())) {
                    RouteManager.route(context, bannerModelList.get(position).getLink());
                }
                ((Activity)context).finish();
            }
        });
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
