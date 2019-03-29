package com.tokopedia.core.manage.people.address.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 9/6/16.
 */
public class RegencyAdapter extends ArrayAdapter {

    public static class ViewHolder {
        TextView regencyName;
    }

    private ArrayList<City> list;
    private LayoutInflater inflater;
    private Context context;

    public RegencyAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new ArrayList<>();
    }

    public static RegencyAdapter createInstance(Context context) {
        return new RegencyAdapter(context, R.layout.spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.spinner_item, null);

            holder.regencyName = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        String text;
        if (position == 0) {
            text = context.getString(R.string.msg_choose);
        } else {
            text = list.get(position - 1).getCityName();
        }

        holder.regencyName.setText(text);

        return convertView;
    }

    @Override
    public int getCount() {
        return 1 + getList().size();
    }

    public ArrayList<City> getList() {
        return list;
    }

    public void setList(List<City> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public int getPositionFromId(String cityId) {
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getCityId().equals(cityId)){
                return i+1;
            }
        }
        return 0;
    }

    public int getPositionFromName(String cityName) {
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getCityName().equals(cityName)){
                return i+1;
            }
        }
        return 0;
    }
}
