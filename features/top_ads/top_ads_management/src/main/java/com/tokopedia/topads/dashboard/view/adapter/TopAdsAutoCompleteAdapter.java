package com.tokopedia.topads.dashboard.view.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 3/6/17.
 */

public class TopAdsAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> mData;
    private ListenerGetData listenerGetData;


    public TopAdsAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mData = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    try {
                        mData = listenerGetData.getData();
                    } catch (Exception e) {
                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }

    public void setListenerGetData(ListenerGetData listenerGetData){
        this.listenerGetData = listenerGetData;
    }

    public interface ListenerGetData{
        ArrayList<String> getData();
    }
}
