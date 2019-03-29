package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.image.TouchImageView;

import java.util.ArrayList;

public class TouchImageAdapter extends PagerAdapter {
    public interface OnImageStateChange {
        void OnStateDefault();

        void OnStateZoom();
    }

    private Context context;
    private ArrayList<String> FileLoc = new ArrayList<>();
    private OnImageStateChange ImageStateChangeListener;

    public TouchImageAdapter(Context context, ArrayList<String> FileLoc) {
        this.context = context;
        this.FileLoc = FileLoc;
    }

    public void SetonImageStateChangeListener(OnImageStateChange Listener) {
        ImageStateChangeListener = Listener;
    }

    @Override
    public int getCount() {
        return FileLoc.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imageView = new TouchImageView(context, new TouchImageView.OnStateChange() {

            @Override
            public void OnStateChanged(float StateSize) {
                if (StateSize <= 1)
                    ImageStateChangeListener.OnStateDefault();
                else
                    ImageStateChangeListener.OnStateZoom();
            }
        });
        ImageHandler.LoadImage(imageView, FileLoc.get(position));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((TouchImageView) object);
    }
}
