package com.tokopedia.core.customadapter;

import com.tokopedia.core.customView.SimpleListView;
import com.tokopedia.core2.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

public class LazyListView extends SimpleListView {

	private LazyLoadListener lazylistener;
	private View footerLV;

	public LazyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		footerLV = View.inflate(context, R.layout.footer_list_view, null);
		footerLV.setOnClickListener(null);
		// TODO Auto-generated constructor stub
	}

	public interface LazyLoadListener {
		public void onLazyLoad(View view);
	}

	public void setOnLazyLoadListener(LazyLoadListener lazyload) {
		lazylistener = lazyload;
		setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if (arg1+arg2 == arg3 && arg3!=0) {
					lazylistener.onLazyLoad(arg0);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void AddLoadingView() {
		addFooterView(footerLV);
	}

	public void RemoveLoadingView() {
		if (getFooterViewsCount() > 0) {
			removeFooterView(footerLV);
		}
	}

}