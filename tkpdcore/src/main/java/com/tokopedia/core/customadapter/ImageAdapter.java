package com.tokopedia.core.customadapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<String> FileLoc = new ArrayList<String>();

	
	public ImageAdapter(Context context, ArrayList<String> FileLoc, int dp) {
		this.context=context;
		this.FileLoc = FileLoc;
	}
	

	@Override
	public int getCount() {
		return FileLoc.size();
	}
	 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}
	 
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		ImageView imageView = new ImageView(context);
		ImageHandler.loadImageWithoutFit(context, imageView, FileLoc.get(position));
		container.addView(imageView, 0);
		return imageView;
	}
	 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((ImageView) object);
	}
}
