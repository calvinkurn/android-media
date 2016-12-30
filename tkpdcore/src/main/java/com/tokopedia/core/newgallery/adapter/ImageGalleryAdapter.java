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
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.myproduct.fragment.ImageGalleryFragment;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;
import com.tokopedia.core.util.MethodChecker;

import java.io.File;
import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by m.normansyah on 12/6/15.
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder> {

    List<ImageModel> datas;
    MultiSelector multiSelector;
    int limit;
    public static final int UNLIMITED_SELECTION = -1;
    private CountTitle countTitle;
    private ActionMode actionMode;

    public ImageGalleryAdapter(List<ImageModel> datas, MultiSelector multiSelector) {
        this(datas, multiSelector, UNLIMITED_SELECTION);
    }

    public ImageGalleryAdapter(List<ImageModel> datas, MultiSelector multiSelector, int limit) {
        this.datas = datas;
        this.multiSelector = multiSelector;
        this.limit = limit;
    }

    public CountTitle getCountTitle() {
        return countTitle;
    }

    public void setCountTitle(CountTitle countTitle) {
        this.countTitle = countTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_galery_item, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends SwappingHolder
            implements View.OnClickListener, View.OnLongClickListener {
        ImageModel imageModel;
        ImageView mImageView;
        //        RelativeLayout mBorder;
        WindowManager wm;

        public ViewHolder(View itemView) {
            super(itemView, multiSelector);
            setSelectionModeStateListAnimator(null);
            mImageView = (ImageView) itemView.findViewById(R.id.picture_gallery_imageview);
//            mBorder	  = (RelativeLayout) itemView.findViewById(R.id.border_imageview_layout);
            wm = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        public void bindView(ImageModel imageModel) {
            this.imageModel = imageModel;
            int imageWidth = (int) (getScreenWidth() - 4) / 3;
            mImageView.setLayoutParams(new FrameLayout.LayoutParams(imageWidth, imageWidth));
            ImageHandler.loadImageFit2(itemView.getContext(), mImageView,
                    MethodChecker.getUri(MainApplication.getAppContext(), new File(imageModel.getPath())).toString());
//            ImageHandler.LoadImageCustom(Uri.fromFile(new File(imageModel.getPath())).toString())
//                    .fit()
//                    .centerCrop().into(mImageView);

        }

        public int getScreenWidth() {
            Point size = new Point();
            Display display = wm.getDefaultDisplay();
            display.getSize(size);
            return size.x;
        }

        @Override
        public void onClick(View v) {
            if (imageModel == null) {
                return;
            }

            int selectedItemSize = UNLIMITED_SELECTION;

//            Toast.makeText(itemView.getContext(), ""+imageModel, Toast.LENGTH_SHORT).show();

            if (!multiSelector.tapSelection(this)) {

                if (itemView.getContext() != null && itemView.getContext() instanceof ImageGalleryView) {
                    ((ImageGalleryView) itemView.getContext()).sendResultImageGallery(imageModel.getPath());
                }
            } else {
                if (limit != UNLIMITED_SELECTION && multiSelector.getSelectedPositions().size() > limit) {
                    multiSelector.tapSelection(this);
                    Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.maximum_instoped_limit), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkNotNull(countTitle)) {
                    actionMode.setTitle(multiSelector.getSelectedPositions().size() + "");
                    countTitle.onTitleChanged(multiSelector.getSelectedPositions().size());
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(itemView.getContext(), "onLongClick !!", Toast.LENGTH_SHORT).show();
            ImageGalleryView imageGalleryView = (ImageGalleryView) itemView.getContext();
            actionMode = imageGalleryView.showActionMode(imageGalleryView.getMultiSelectorCallback(ImageGalleryFragment.FRAGMENT_TAG));
            multiSelector.setSelected(this, true);
            return true;
        }
    }

    public interface CountTitle {
        void onTitleChanged(int size);
    }
}
