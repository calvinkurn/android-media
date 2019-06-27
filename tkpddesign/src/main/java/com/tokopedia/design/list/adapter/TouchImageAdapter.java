package com.tokopedia.design.list.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.tokopedia.design.image.ImageLoader;
import com.tokopedia.design.image.TouchImageView;

import java.io.File;
import java.util.ArrayList;

public class TouchImageAdapter extends PagerAdapter {
    public interface OnImageStateChange {
        void OnStateDefault();

        void OnStateZoom();
    }

    private Context context;
    private ArrayList<String> FileLoc;
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
        TouchImageView imageView = new TouchImageView(context, StateSize -> {
            if (StateSize <= 1)
                ImageStateChangeListener.OnStateDefault();
            else
                ImageStateChangeListener.OnStateZoom();
        });
        String thumbnail = FileLoc.get(position);
        if (URLUtil.isNetworkUrl(thumbnail)) {
            ImageLoader.LoadImage(imageView, thumbnail);
        } else {
            imageView.setImageURI(Uri.fromFile(new File(thumbnail)));
        }

        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((TouchImageView) object);
    }
}
