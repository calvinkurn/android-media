package com.tokopedia.contactus.inboxticket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.customadapter.ImageUpload;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 10/4/16.
 */

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image_upload)
        ImageView image;

        @BindView(R2.id.delete_but)
        ImageView deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface ProductImageListener {

        void onImageDeleted(int size);
    }

    private ProductImageListener listener;
    private ArrayList<ImageUpload> data;
    public Context context;

    public ImageUploadAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public static ImageUploadAdapter createAdapter(Context context) {
        return new ImageUploadAdapter(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_image_upload_delete, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            if (data.get(position).getFileLoc() == null) {
                ImageHandler.LoadImage(holder.image, data.get(position).getPicSrc());
            } else {
                ImageHandler.loadImageFromFile(context, holder.image, new File(data.get(position).getFileLoc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(position);
                notifyDataSetChanged();
                listener.onImageDeleted(data.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addImage(ImageUpload image) {
        data.add(image);
        notifyDataSetChanged();
    }

    public void setListener(ProductImageListener listener) {
        this.listener = listener;
    }

    public ArrayList<ImageUpload> getList() {
        return data;
    }

}
