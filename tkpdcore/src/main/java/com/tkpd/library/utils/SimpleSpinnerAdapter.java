package com.tkpd.library.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.tokopedia.core2.R;

import java.util.List;

/**
 * Created by Tkpd_Eka on 3/17/2015.
 */
public class SimpleSpinnerAdapter extends ArrayAdapter<String>{

    public SimpleSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public static SimpleSpinnerAdapter createAdapter(Context context, List<String> itemList){
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(context, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
