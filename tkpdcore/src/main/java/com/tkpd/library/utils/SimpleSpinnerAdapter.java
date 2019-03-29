package com.tkpd.library.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.tokopedia.core2.R;

import java.util.List;

/**
 * Created by Tkpd_Eka on 3/17/2015.
 */
public class SimpleSpinnerAdapter extends ArrayAdapter<String>{

    public SimpleSpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SimpleSpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SimpleSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public SimpleSpinnerAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SimpleSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public SimpleSpinnerAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public static SimpleSpinnerAdapter createAdapter(Context context, List<String> itemList){
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(context, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static SimpleSpinnerAdapter createAdapter(Context context, List<String> itemList, int textViewResourceId){
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(context, textViewResourceId, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
	
	public static SimpleSpinnerAdapter createAdapter(Context context, int arrayResId){
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(arrayResId));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static SimpleSpinnerAdapter createAdapterAddProduct(Context context, List<String> itemList){
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(context, R.layout.spinner_add_product_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

}
