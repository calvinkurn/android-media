package com.tokopedia.tkpd.myproduct.adapter;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.database.manager.DbManagerImpl;
import com.tokopedia.tkpd.database.model.PictureDB;
import com.tokopedia.tkpd.myproduct.BaseProductActivity;
import com.tokopedia.tkpd.myproduct.ProductActivity;
import com.tokopedia.tkpd.myproduct.ProductSocMedActivity;
import com.tokopedia.tkpd.myproduct.dialog.DialogFragmentImageAddProduct;
import com.tokopedia.tkpd.myproduct.model.ImageModel;

import java.io.File;
import java.net.URI;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by m.normansyah on 08/12/2015.
 */
public class PhotoAdapter  extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
    List<ImageModel> datas;
//    MultiSelector multiSelector;
    int limit;
    private static final int UNLIMITED_SELECTION =  -1;

    public PhotoAdapter(List<ImageModel> datas){
        this(datas,UNLIMITED_SELECTION);
    }

    public PhotoAdapter(List<ImageModel> datas, int limit){
        this.datas = datas;
//        this.multiSelector = multiSelector;
        this.limit = limit;
//        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_galery_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

//    @Override
//    public long getItemId(int position) {
//         return datas.get(position).hashCode()+position;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
//            , View.OnLongClickListener
    {

        @Bind(R2.id.picture_gallery_imageview)
        ImageView mImageView;
        @Bind(R2.id.main_picture_info)
        TextView mainPicInfo;

        // helper variable
        WindowManager wm;

        // datas
        ImageModel imageModel;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            wm = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
            itemView.setOnClickListener(this);
//            itemView.setLongClickable(true);
//            itemView.setOnLongClickListener(this);

//            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                setSelectionModeStateListAnimator(null);
//                setDefaultModeStateListAnimator(null);
//            }
//            // Default selection mode background drawable is this
//            setSelectionModeBackgroundDrawable(null);
//            setDefaultModeBackgroundDrawable(null);
        }

        public void bindView(ImageModel imageModel, int position){
            this.position = position;
            this.imageModel = imageModel;
            int imageWidth = (int) (getScreenWidth() - 4) / 3;
            mImageView.setLayoutParams(new FrameLayout.LayoutParams(imageWidth, imageWidth));
            if(imageModel.getPath()==null)
                mImageView.setImageResource(imageModel.getResId());
            else if(!isValidURL(imageModel.getPath()))// assumed from file path
                ImageHandler.loadImageFit2(itemView.getContext()
                        , mImageView
                        , Uri.fromFile(new File(imageModel.getPath())).toString()
//                        ,new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
////                                ViewHolder.this.itemView.setOnClickListener(null);
//                                return true;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                                ViewHolder.this.itemView.setOnClickListener(ViewHolder.this);
//                                return true;
//                            }
//                        }
                );
            else {// assumed from url
                ImageHandler.loadImageFit2(itemView.getContext(), mImageView, imageModel.getPath());
            }

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
            mainPicInfo.setLayoutParams(params);
            if (position == 0) {
                mainPicInfo.setVisibility(View.VISIBLE);
            }else{
                mainPicInfo.setVisibility(View.GONE);
            }
        }

        public int getScreenWidth() {
            Point size = new Point();
            Display display	 = wm.getDefaultDisplay();
            display.getSize(size);
            return size.x;
        }

        @Override
        public void onClick(View v) {
            if(imageModel.getPath() == null) {
                int defaultNumber = ImageModel.calculateDefaults(datas);
                ProductActivity.moveToImageGallery((AppCompatActivity) itemView.getContext(), position, defaultNumber);
            }else {
                if(itemView.getContext() instanceof DialogFragmentImageAddProduct.DFIAListener){
                    if(itemView.getContext() != null
                        && itemView.getContext() instanceof ProductActivity) {
                            FragmentManager fm = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
                            PictureDB pictureDB = DbManagerImpl.getInstance().getGambarById(imageModel.getDbId());
                            if(pictureDB != null) {
                                BaseProductActivity.showEditImageDialog(fm, position, pictureDB.picturePrimary == PictureDB.PRIMARY_IMAGE, (DialogFragmentImageAddProduct.DFIAListener) itemView.getContext());
                            }
                    } else if (itemView.getContext() != null
                            && itemView.getContext() instanceof ProductSocMedActivity){
                        FragmentManager fm = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
                        PictureDB pictureDB = DbManagerImpl.getInstance().getGambarById(imageModel.getDbId());
                        if(pictureDB != null) {
                            BaseProductActivity.showEditImageDialog(fm, position, ((ProductSocMedActivity) itemView.getContext()).getCurrentFragmentPosition(), pictureDB.picturePrimary == PictureDB.PRIMARY_IMAGE, (DialogFragmentImageAddProduct.DFIAListener) itemView.getContext());
                        }
                    }
                }
            }
        }

        /**
         * On long click not used, but keep this, it may be used for later
         * @param v
         * @return
         */
//        @Override
//        public boolean onLongClick(View v) {
//            Context context = itemView.getContext();
//            if(context != null && context instanceof MultiSelectInterface){
//                ((AppCompatActivity) context).startSupportActionMode(
//                        ((MultiSelectInterface)context)
//                                .getMultiSelectorCallback(AddProductFragment.FRAGMENT_TAG)
//                );
//            }
//            multiSelector.setSelected(this, true);
//            return true;
//        }
    }

    /**
     * check if path is url or not
     * @param urlStr url or sdcard path
     * @return
     */
    public static boolean isValidURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        }
        catch (Exception e) {
            return false;
        }
    }


}
