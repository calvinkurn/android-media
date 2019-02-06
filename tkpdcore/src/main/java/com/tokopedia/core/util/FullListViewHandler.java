package com.tokopedia.core.util;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ViewHelper;
import com.tokopedia.core2.R;

public class FullListViewHandler {
	
	private ListView lv;
	private Activity context;
	private View fakeFooterView;
	private View BlankView;
	private View view;
	
	public FullListViewHandler (Activity context, ListView lv, View view) {
		fakeFooterView = View.inflate(context, R.layout.fake_footer_shop_info, null);
		BlankView = fakeFooterView.findViewById(R.id.blank);
		this.lv = lv;
		this.view = view;
		this.context = context;
	}
	
	public void removeFakeFooter() {
		if (lv.getFooterViewsCount() > 0) {
			lv.removeFooterView(fakeFooterView);
		}
	}
	
	public void measureListView () {
		if (lv.getFooterViewsCount() > 0) {
			lv.removeFooterView(fakeFooterView);
		}
		if (lv.getCount() < 5) {
			int screenheight = view.getMeasuredHeight();
			int lvheight = ViewHelper.getListViewSize(lv);
			//int fakeheight = view.getHeight();
			CommonUtils.dumper("screenheight: " + screenheight);
			CommonUtils.dumper("lvheight: "+lvheight);
			if (lvheight < screenheight) {
				lv.addFooterView(fakeFooterView, "FakeFooter", false);
				ViewHelper.setViewSize(BlankView, (screenheight - lvheight));
				CommonUtils.dumper("HEIGHT FAKE: "+(screenheight - lvheight));
			} else {
				if (lv.getFooterViewsCount() > 0) {
					lv.removeFooterView(fakeFooterView);
				}
			}
		}
	}
	
	public void measureListView (int extrasize) {
		if (lv.getFooterViewsCount() > 0) {
			lv.removeFooterView(fakeFooterView);
		}
		if (lv.getCount() < 5) {
			int screenheight = view.getMeasuredHeight();
			int lvheight = ViewHelper.getListViewSize(lv);
			//int fakeheight = view.getHeight();
			CommonUtils.dumper("screenheight: "+screenheight);
			CommonUtils.dumper("lvheight: "+lvheight);
			if (lvheight < screenheight) {
				lv.addFooterView(fakeFooterView, "FakeFooter", false);
				ViewHelper.setViewSize(BlankView, (screenheight - lvheight - (int) CommonUtils.DptoPx(context, extrasize)));
				CommonUtils.dumper("HEIGHT FAKE: "+(screenheight - lvheight - (int) CommonUtils.DptoPx(context, extrasize)));
			} else {
				if (lv.getFooterViewsCount() > 0) {
					lv.removeFooterView(fakeFooterView);
				}
			}
		}
	}
	
	public void measureListViewIgnoreCount () {
		if (lv.getFooterViewsCount() > 0) {
			lv.removeFooterView(fakeFooterView);
		}
			int screenheight = view.getMeasuredHeight();
			int lvheight = ViewHelper.getListViewSize(lv);
			//int fakeheight = view.getHeight();
			CommonUtils.dumper("screenheight: "+screenheight);
			CommonUtils.dumper("lvheight: "+lvheight);
			if (lvheight < screenheight) {
				lv.addFooterView(fakeFooterView, "FakeFooter", false);
				ViewHelper.setViewSize(BlankView, (screenheight - lvheight));
				CommonUtils.dumper("HEIGHT FAKE: "+(screenheight - lvheight));
			} else {
				if (lv.getFooterViewsCount() > 0) {
					lv.removeFooterView(fakeFooterView);
				}
			}
	}
	
	public void measureListViewIgnoreCount (int extrasize) {
		if (lv.getFooterViewsCount() > 0) {
			lv.removeFooterView(fakeFooterView);
		}
			int screenheight = view.getMeasuredHeight();
			int lvheight = ViewHelper.getListViewSize(lv);
			//int fakeheight = view.getHeight();
			CommonUtils.dumper("screenheight: "+screenheight);
			CommonUtils.dumper("lvheight: "+lvheight);
			if (lvheight < screenheight) {
				lv.addFooterView(fakeFooterView, "FakeFooter", false);
				ViewHelper.setViewSize(BlankView, (screenheight - lvheight - (int) CommonUtils.DptoPx(context, extrasize)));
				CommonUtils.dumper("HEIGHT FAKE: "+(screenheight - lvheight - (int) CommonUtils.DptoPx(context, extrasize)));
			} else {
				if (lv.getFooterViewsCount() > 0) {
					lv.removeFooterView(fakeFooterView);
				}
			}
		
	}
}
