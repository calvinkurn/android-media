package com.tokopedia.contactus.orderquery.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.orderquery.data.ImageUpload;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sandeepgoyal on 18/04/18.
 */

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ImageViewHolder> {

    public ArrayList<ImageUpload> getImageUpload() {
        return imageUpload;
    }

    ArrayList<ImageUpload> imageUpload = new ArrayList<>();

    private Context context;

    public ImageUploadAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.selected_image_item, parent, false));

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setImage(imageUpload.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUpload.size();
    }

    public void addImage(ImageUpload image) {
        imageUpload.add(image);
        notifyDataSetChanged();
    }




    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageUpload image;

        @BindView(R2.id.selected_image)
        ImageView selectedImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImage(ImageUpload image) {
            this.image = image;
            ImageHandler.loadImageFromFile(context, selectedImage, new File(image.getFileLoc()));
        }

        @OnClick(R2.id.delete_image)
        public void onViewClicked() {
            imageUpload.remove(imageUpload.indexOf(image));
            notifyDataSetChanged();
        }
    }
}
