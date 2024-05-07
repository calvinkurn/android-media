package com.tokopedia.imagepreview.touch_view_pager_lib;

import static com.tokopedia.utils.image.ImageProcessingUtil.DEF_HEIGHT;
import static com.tokopedia.utils.image.ImageProcessingUtil.DEF_WIDTH;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.tokopedia.media.loader.ExtensionKt;
import com.tokopedia.media.loader.data.Properties;
import com.tokopedia.utils.image.ImageProcessingUtil;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ImagePreviewTouchImageAdapter extends PagerAdapter {
    public interface OnImageStateChange {
        void OnStateDefault();

        void OnStateZoom();
    }

    private Context context;
    private ArrayList<String> FileLoc;
    private OnImageStateChange ImageStateChangeListener;

    public ImagePreviewTouchImageAdapter(Context context, ArrayList<String> FileLoc) {
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
        ImagePreviewTouchImageView imageView = new ImagePreviewTouchImageView(context, StateSize -> {
            if (StateSize <= 1)
                ImageStateChangeListener.OnStateDefault();
            else
                ImageStateChangeListener.OnStateZoom();
        });
        String thumbnail = FileLoc.get(position);
        if (URLUtil.isNetworkUrl(thumbnail)) {
            ExtensionKt.loadImage((ImageView) imageView, thumbnail, properties -> null);
//            ImageLoader.LoadImage(imageView, thumbnail);
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
        container.removeView((ImagePreviewTouchImageView) object);
    }
}
