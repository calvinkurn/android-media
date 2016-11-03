package com.tokopedia.tkpd.customadapter;

import android.content.Context;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.GalleryBrowser.ListImageHolder;
import com.tokopedia.tkpd.ImageGallery.ImageGalleryListener;
import com.tokopedia.tkpd.R;

import java.io.File;
import java.util.List;

@Deprecated
public class ImageGalleryAdapter extends BaseAdapter{

	private Context mContext;
	private List<ListImageHolder> mData;
	private LayoutInflater mInflater;
	private ImageGalleryListener mListener;


	public ImageGalleryAdapter(Context mContext,List<ListImageHolder> mData, ImageGalleryListener mListener) {
		this.mContext	 = mContext;
		this.mData		 = mData;
		this.mListener	 = mListener;
		this.mInflater	 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int fPosition = position;

		final ViewHolder holder;
		if(convertView == null){
			convertView		  = mInflater.inflate(R.layout.picture_galery_item, null);
			holder			  = new ViewHolder();
			holder.mImageView = (ImageView) convertView.findViewById(R.id.picture_gallery_imageview);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
//		holder.mImageView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(mListener != null){
//					mListener.onPictureClicked(fPosition);
//				}
//			}
//		});

		int imageWidth = (int) (getScreenWidth() - 4) / 3;
		holder.mImageView.setLayoutParams(new FrameLayout.LayoutParams(imageWidth,imageWidth));
		ImageHandler.loadImageFit2(mContext, holder.mImageView,
				mData.get(position).getFirstPhotoURL());
//		ImageHandler.LoadImageCustom(mData.get(position).getFirstPhotoURL())
//		.fit()
//		.centerCrop().into(holder.mImageView);
		return convertView;
	}
	

	public int getScreenWidth() {
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display	 = wm.getDefaultDisplay();
		return display.getWidth();
	}

	
	
	public class ViewHolder{
		public ImageView mImageView;
	}
	
}
