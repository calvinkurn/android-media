package com.tokopedia.tkpd.myproduct.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.myproduct.model.FolderModel;
import com.tokopedia.tkpd.myproduct.presenter.ImageGalleryView;

import java.io.File;
import java.util.List;

/**
 * Created by m.normansyah on 12/6/15.
 */
public class ImageAlbumAdapter extends RecyclerView.Adapter<ImageAlbumAdapter.ViewHolder>{
    int maxSelection = -1;

    public int getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_galery_album_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(data.get(position), maxSelection);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * This is view holder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImageView;
        LinearLayout mBorder;
        TextView mAlbumname;
        FolderModel folderModel;
        int maxSelection = ImageGalleryAdapter.UNLIMITED_SELECTION;

        public ViewHolder(View itemView){
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.picture_gallery_album_imageview);
            mBorder	  = (LinearLayout) itemView.findViewById(R.id.border_gallery_album_layout);
            mAlbumname = (TextView) itemView.findViewById(R.id.picture_gallery_album_name);

            mImageView.setOnClickListener(this);
            mBorder.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        public void bindView(FolderModel folderModel, int maxSelection){
            this.folderModel = folderModel;
            mAlbumname.setText(folderModel.getPath());
            ImageHandler.loadImageFit2(itemView.getContext(),mImageView,
                Uri.fromFile(new File(folderModel.getImageModels().get(0).getPath())).toString());
            setMaxSelection(maxSelection);
//            ImageHandler.LoadImageCustom(Uri.fromFile(new File(folderModel.getImageModels().get(0).getPath())).toString())
//                    .fit()
//                    .centerCrop().into(mImageView);
        }

        public void moveToImageGallery(){
            if(itemView.getContext()!= null && itemView.getContext() instanceof ImageGalleryView){
                ((ImageGalleryView)itemView.getContext()).moveToGallery(folderModel.getImageModels(), maxSelection);
            }
        }

        public int getMaxSelection() {
            return maxSelection;
        }

        public void setMaxSelection(int maxSelection) {
            this.maxSelection = maxSelection;
        }

        @Override
        public void onClick(View v) {
            moveToImageGallery();
        }
    }

    List<FolderModel> data;

    public ImageAlbumAdapter(List<FolderModel> data){
        this.data = data;
    }

    public void addAll(List<FolderModel> data){
        this.data.addAll(data);
    }

    public void add(FolderModel data){
        this.data.add(data);
    }
}
