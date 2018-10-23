package com.tokopedia.core.manage.people.address.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 9/6/16.
 */
public class ProvinceAdapter extends ArrayAdapter {

    public static class ViewHolder {
        TextView provinceName;
    }

    private ArrayList<Province> list;
    private LayoutInflater inflater;
    private Context context;

    public ProvinceAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new ArrayList<>();
    }

    public static ProvinceAdapter createInstance(Context context) {
        return new ProvinceAdapter(context, R.layout.spinner_item);
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

            holder.provinceName = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        String text;
        if (position == 0) {
            text = context.getString(R.string.msg_choose);
        } else {
            text = list.get(position - 1).getProvinceName();
        }

        holder.provinceName.setText(text);

        return convertView;
    }

    @Override
    public int getCount() {
        return 1 + getList().size();
    }

    public ArrayList<Province> getList() {
        return list;
    }

    public void setList(List<Province> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public int getPositionFromId(String provinceId) {
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getProvinceId().equals(provinceId)){
                return i+1;
            }
        }
        return 0;
    }

    public int getPositionFromName(String provinceName) {
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getProvinceName().contains(provinceName)){
                return i+1;
            }
        }
        return 0;
    }
}
