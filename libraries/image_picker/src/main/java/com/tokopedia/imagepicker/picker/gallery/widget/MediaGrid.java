package com.tokopedia.imagepicker.picker.gallery.widget;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
import com.tokopedia.unifycomponents.Label;

import java.util.ArrayList;

/**
 * Created by hangnadi on 5/29/17.
 */

public class MediaGrid extends SquareFrameLayout implements View.OnClickListener {

    private ImageView mThumbnail;
    private TextView mVideoDuration;

    private MediaItem mMedia;
    private String selectedUrl;
    private PreBindInfo mPreBindInfo;
    private OnMediaGridClickListener mListener;
    private View ivCheck;
    private Label counterLabel;

    public MediaGrid(Context context) {
        super(context);
        init(context);
    }

    public MediaGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.media_grid_picker, this, true);

        mThumbnail = (ImageView) findViewById(R.id.media_thumbnail);
        mVideoDuration = (TextView) findViewById(R.id.video_duration);
        ivCheck = findViewById(R.id.iv_check);
        counterLabel = findViewById(R.id.label_counter);
        mThumbnail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (mMedia != null) {
                mListener.onThumbnailClicked(mThumbnail, mMedia, mPreBindInfo.mViewHolder);
            } else if (!TextUtils.isEmpty(selectedUrl)) {
                mListener.onThumbnailClicked(mThumbnail, selectedUrl, mPreBindInfo.mViewHolder);
            }
        }
    }

    public void preBindMedia(PreBindInfo info) {
        mPreBindInfo = info;
    }

    public void bindMedia(MediaItem item, ArrayList<String> selectionIdList,
                          boolean hasCounterLabel) {
        mMedia = item;
        selectedUrl = null;
        setImage();
        setVideoDuration();
        setSelectionCursor(selectionIdList, hasCounterLabel, mMedia.getRealPath());
    }

    public void bindMedia(String pathOrUrl, ArrayList<String> selectionIdList,
                          boolean hasCounterLabel) {
        selectedUrl = pathOrUrl;
        mMedia = null;
        if (URLUtil.isNetworkUrl(pathOrUrl)) {
            ImageHandler.LoadImage(mThumbnail, pathOrUrl);
        } else {
            ImageHandler.loadImageFit2(getContext(), mThumbnail, pathOrUrl);
        }
        setSelectionCursor(selectionIdList, hasCounterLabel, pathOrUrl);
    }

    private void setImage() {
        Glide.with(getContext())
                .load(mMedia.getContentUri())
                .placeholder(mPreBindInfo.mPlaceholder)
                .error(mPreBindInfo.error)
                .override(mPreBindInfo.mResize, mPreBindInfo.mResize)
                .centerCrop()
                .into(mThumbnail);
    }

    private void setVideoDuration() {
        if (mMedia.isVideo()) {
            mVideoDuration.setVisibility(VISIBLE);
            mVideoDuration.setText(DateUtils.formatElapsedTime(mMedia.getDuration() / 1000));
        } else {
            mVideoDuration.setVisibility(GONE);
        }
    }

    private void setSelectionCursor(ArrayList<String> selectionIdList,
                                    boolean hasCounterLabel,
                                    String path) {
        int index = selectionIdList.indexOf(path);
        if (index >= 0) {
            ivCheck.setVisibility(View.VISIBLE);
            if (hasCounterLabel) {
                counterLabel.setLabel(String.valueOf(index + 1));
                counterLabel.setVisibility(View.VISIBLE);
            } else {
                counterLabel.setVisibility(View.GONE);
            }
        } else {
            ivCheck.setVisibility(View.GONE);
        }
    }

    public void setOnMediaGridClickListener(OnMediaGridClickListener listener) {
        mListener = listener;
    }

    public interface OnMediaGridClickListener {

        void onThumbnailClicked(ImageView thumbnail, MediaItem item, RecyclerView.ViewHolder holder);
        void onThumbnailClicked(ImageView thumbnail, String imageUrl, RecyclerView.ViewHolder holder);

    }

    public static class PreBindInfo {
        int mResize;
        int mPlaceholder;
        int error;
        RecyclerView.ViewHolder mViewHolder;

        public PreBindInfo(int resize, int placeholder, int error,
                           RecyclerView.ViewHolder viewHolder) {
            mResize = resize;
            mPlaceholder = placeholder;
            this.error = error;
            mViewHolder = viewHolder;
        }
    }

}
