package com.tokopedia.core.newgallery.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.myproduct.fragment.ImageGalleryFragment;
import com.tokopedia.core.newgallery.model.ImageModel;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;

import java.util.ArrayList;
import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by m.normansyah on 12/6/15.
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder> {

    public static final int UNLIMITED_SELECTION = -1;
    private List<ImageModel> datas;
    private MultiSelector multiSelector;
    private int limit;
    private CountTitle countTitle;
    private ActionMode actionMode;

    public ImageGalleryAdapter(List<ImageModel> datas, MultiSelector multiSelector, int limit) {
        this.datas = datas;
        this.multiSelector = multiSelector;
        this.limit = limit;
    }

    public void setCountTitle(CountTitle countTitle) {
        this.countTitle = countTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_galery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addItems(ArrayList<ImageModel> data) {
        datas.clear();
        datas.addAll(data);
        notifyDataSetChanged();
    }

    public interface CountTitle {
        void onTitleChanged(int size);
    }

    public class ViewHolder extends SwappingHolder
            implements View.OnClickListener, View.OnLongClickListener {
        ImageModel imageModel;
        ImageView mImageView;
        WindowManager wm;

        public ViewHolder(View itemView) {
            super(itemView, multiSelector);
            setSelectionModeStateListAnimator(null);
            mImageView = (ImageView) itemView.findViewById(R.id.picture_gallery_imageview);
            wm = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        public void bindView(ImageModel imageModel) {
            this.imageModel = imageModel;

            int dimen = (getScreenWidth() - 4) / 3;
            mImageView.setLayoutParams(new FrameLayout.LayoutParams(dimen, dimen));
            ImageHandler.loadImageFit2(itemView.getContext(), mImageView, imageModel.getPathFile());
        }

        public int getScreenWidth() {
            Point size = new Point();
            Display display = wm.getDefaultDisplay();
            display.getSize(size);
            return size.x;
        }

        @Override
        public void onClick(View v) {
            if (imageModel == null || multiSelector == null) {
                return;
            }

            int selectedItemSize = UNLIMITED_SELECTION;

            if (!multiSelector.tapSelection(this)) {

                if (itemView.getContext() != null && itemView.getContext() instanceof ImageGalleryView) {
                    ((ImageGalleryView) itemView.getContext()).sendResultImageGallery(imageModel.getPathFile());
                }
            } else {
                if (limit != UNLIMITED_SELECTION && multiSelector.getSelectedPositions().size() > limit) {
                    multiSelector.tapSelection(this);
                    Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.maximum_instoped_limit), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkNotNull(countTitle) && checkNotNull(actionMode)) {
                    actionMode.setTitle(multiSelector.getSelectedPositions().size() + "");
                    countTitle.onTitleChanged(multiSelector.getSelectedPositions().size());
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            ImageGalleryView imageGalleryView = (ImageGalleryView) itemView.getContext();
            actionMode = imageGalleryView.showActionMode(imageGalleryView.getMultiSelectorCallback(ImageGalleryFragment.FRAGMENT_TAG));
            multiSelector.setSelected(this, true);
            return true;
        }
    }
}
