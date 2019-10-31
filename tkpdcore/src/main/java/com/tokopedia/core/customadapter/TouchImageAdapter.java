package com.tokopedia.core.customadapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.ui.widget.TouchImageView;
import com.tkpd.library.ui.widget.TouchImageView.OnStateChange;

import java.util.ArrayList;

public class TouchImageAdapter extends PagerAdapter {
	public interface OnImageStateChange{
		void OnStateDefault();
		void OnStateZoom();
	}
	
	private Context context;
	private int dp;
	private ArrayList<String> FileLoc = new ArrayList<String>();
	private OnImageStateChange ImageStateChangeListener;
	
	public TouchImageAdapter(Context context, ArrayList<String> FileLoc, int dp) {
		this.context=context;
		this.FileLoc = FileLoc;
		this.dp = dp;
	}
	
	public void SetonImageStateChangeListener(OnImageStateChange Listener){
		ImageStateChangeListener = Listener;
	}
	
	@Override
	public int getCount() {
		return FileLoc.size();
	//return GalImages.length;
	}
	 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((TouchImageView) object);
	}
	 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TouchImageView imageView = new TouchImageView(context, new OnStateChange() {

			@Override
			public void OnStateChanged(float StateSize) {
				// TODO Auto-generated method stub
				if(StateSize <= 1)
					ImageStateChangeListener.OnStateDefault();
				else
					ImageStateChangeListener.OnStateZoom();
			}
		});
		/*Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		Bitmap bitmap = BitmapFactory.decodeFile(FileLoc.get(position));
		bitmap = ih.ResizeBitmap(bitmap, px);
		imageView.setImageBitmap(bitmap);
		((ViewPager) container).addView(imageView, 0);*/
		ImageHandler.LoadImage(imageView, FileLoc.get(position));
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}
	 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((TouchImageView) object);
	}
}
