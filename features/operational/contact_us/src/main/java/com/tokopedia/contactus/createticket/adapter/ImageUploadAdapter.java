package com.tokopedia.contactus.createticket.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.createticket.model.ImageUpload;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Nisie on 2/16/16.
 */
public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    private static final int VIEW_UPLOAD_BUTTON = 100;
    private int canUpload = 0;

    static class ViewHolder extends RecyclerView.ViewHolder {

       private final ImageView image;
       private final ImageView deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_upload);
            deleteButton = itemView.findViewById(R.id.delete_but);
        }
    }

    public interface ProductImageListener {
        View.OnClickListener onUploadClicked(int position);

        View.OnClickListener onImageClicked(int position, ImageUpload imageUpload);
    }


    private ProductImageListener listener;
    private final ArrayList<ImageUpload> data;

    public ImageUploadAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public Context context;

    public static ImageUploadAdapter createAdapter(Context context) {
        return new ImageUploadAdapter(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contactus_listview_image_upload_delete, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_UPLOAD_BUTTON) {
            bindUploadButton(holder, position);
        } else {
            bindImage(holder, position);
        }
    }

    @SuppressLint({"DeprecatedMethod", "NotifyDataSetChanged"})
    private void bindImage(ViewHolder holder, final int position) {

        try {
            if (data.get(position).getFileLoc() == null) {
                ImageHandler.LoadImage(holder.image, data.get(position).getPicSrc());
            } else {
                ImageHandler.loadImageFromFile(context, holder.image, new File(data.get(position).getFileLoc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.image.setOnClickListener(listener.onImageClicked(position, data.get(position)));
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setOnClickListener(v -> {
            data.remove(position);
            notifyDataSetChanged();
        });

        setBorder(holder, position);

    }

    private void setBorder(ViewHolder holder, int position) {
        if (data.get(position).isSelected()) {
            holder.image.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500));
        } else {
            holder.image.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }
    }

    private void bindUploadButton(ViewHolder holder, int position) {
        holder.image.setOnClickListener(listener.onUploadClicked(position));
        holder.deleteButton.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (data.size() < 5) {
            return data.size() + canUpload;
        } else {
            return data.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int maxImage = 5;
        if (position == data.size() && data.size() < maxImage) {
            return VIEW_UPLOAD_BUTTON;
        } else {
            return super.getItemViewType(position);
        }
    }

    public ArrayList<ImageUpload> getList() {
        return this.data;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addImage(ImageUpload image) {
        data.add(image);
        notifyDataSetChanged();
    }


    public void setListener(ProductImageListener listener) {
        this.listener = listener;
    }

    public void setCanUpload(boolean canUpload) {
        this.canUpload = canUpload ? 1 : 0;
    }

}
