package com.tokopedia.core.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.GalleryBrowser.ListImageHolder;
import com.tokopedia.core.ImageGallery.ImageGalleryListener;
import com.tokopedia.core.R;

import java.util.List;

@Deprecated
public class ImageGalleryAlbumAdapter extends BaseAdapter{

	private List<ListImageHolder> mData;
	private LayoutInflater mInflater;
	private ImageGalleryListener mListener;


	public ImageGalleryAlbumAdapter(Context mContext,List<ListImageHolder> mData, ImageGalleryListener mListener) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int fPosition = position;

		final ViewHolder holder;
		if(convertView == null){
			convertView		  = mInflater.inflate(R.layout.picture_galery_album_item, null);
			holder			  = new ViewHolder();
			holder.mImageView = (ImageView) convertView.findViewById(R.id.picture_gallery_album_imageview);
			holder.mBorder	  = (LinearLayout) convertView.findViewById(R.id.border_gallery_album_layout);
			holder.mAlbumname = (TextView) convertView.findViewById(R.id.picture_gallery_album_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mAlbumname.setText(mData.get(fPosition).getFolderName());
//		holder.mBorder.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				System.out.println(mData.get(position).getFirstPhotoURL());
//				if(mListener != null){
//					mListener.onPictureClicked(fPosition);
//				}
//			}
//		});

//		ImageHandler.LoadImageCustom(mData.get(position).getFirstPhotoURL())
//		.fit()
//		.centerCrop().into(holder.mImageView);

		ImageHandler.loadImageFit2(convertView.getContext(), holder.mImageView,
				mData.get(position).getFirstPhotoURL());

		return convertView;
	}
	
	
	public class ViewHolder{
		public ImageView mImageView;
		public LinearLayout mBorder;
		public TextView mAlbumname;
	}
	
}
