package com.tokopedia.design.list.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.tokopedia.design.image.ImageLoader;
import com.tokopedia.design.image.TouchImageView;
import com.tokopedia.utils.image.ImageProcessingUtil;

import java.util.ArrayList;

import static com.tokopedia.utils.image.ImageProcessingUtil.DEF_HEIGHT;
import static com.tokopedia.utils.image.ImageProcessingUtil.DEF_WIDTH;

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
            loadImageFromFile(context, imageView, thumbnail);
        }

        container.addView(imageView, 0);
        return imageView;
    }

    private void loadImageFromFile(Context context, ImageView imageView, String thumbnail){
        if (!TextUtils.isEmpty(thumbnail)) {
            Bitmap bitmap = ImageProcessingUtil.getBitmapFromPath(thumbnail, DEF_WIDTH, DEF_HEIGHT, false);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((TouchImageView) object);
    }
}
