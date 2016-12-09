package com.tkpd.library.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewHelper {
    public static int getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return 0;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        int height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
        return height;
    }

    public static void setViewSize(View view, int height ) {
        System.out.println("HEIGHT: "+height);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }
}

