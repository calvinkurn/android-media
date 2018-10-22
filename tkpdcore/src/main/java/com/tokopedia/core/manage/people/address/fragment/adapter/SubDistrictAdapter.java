package com.tokopedia.core.manage.people.address.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.District;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 9/6/16.
 */
public class SubDistrictAdapter extends ArrayAdapter {

    public static class ViewHolder {
        TextView subDistrictName;
    }

    private ArrayList<District> list;
    private LayoutInflater inflater;
    private Context context;

    public SubDistrictAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new ArrayList<>();
    }

    public static SubDistrictAdapter createInstance(Context context) {
        return new SubDistrictAdapter(context, R.layout.spinner_item);
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

            holder.subDistrictName = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        String text;
        if (position == 0) {
            text = context.getString(R.string.msg_choose);
        } else {
            text = list.get(position - 1).getDistrictName();
        }

        holder.subDistrictName.setText(text);

        return convertView;
    }

    @Override
    public int getCount() {
        return 1 + getList().size();
    }

    public ArrayList<District> getList() {
        return list;
    }

    public void setList(List<District> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public int getPositionFromId(String districtId) {
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getDistrictId().equals(districtId)){
                return i+1;
            }
        }
        return 0;
    }

    public int getPositionFromName(String districtName) {
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getDistrictName().equals(districtName)){
                return i+1;
            }
        }
        return 0;
    }
}
