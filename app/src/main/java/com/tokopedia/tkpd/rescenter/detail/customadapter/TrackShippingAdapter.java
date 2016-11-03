package com.tokopedia.tkpd.rescenter.detail.customadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;

import java.util.List;

/**
 * Created by hangnadi on 3/3/16.
 */
public class TrackShippingAdapter extends BaseAdapter {

    private List<ResCenterTrackShipping.TrackHistory> historyList;
    private Activity context;
    private LayoutInflater inflater;

    public TrackShippingAdapter(Activity context, List<ResCenterTrackShipping.TrackHistory> historyList){
        this.context = context;
        this.historyList = historyList;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView Status;
        TextView City;
        TextView Time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.listview_track_result,null);
            holder.Status = (TextView) convertView.findViewById(R.id.status);
            holder.City = (TextView) convertView.findViewById(R.id.city);
            holder.Time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }else
            holder = (Holder)convertView.getTag();

        holder.Status.setText(historyList.get(position).getStatus());
        holder.City.setText(historyList.get(position).getCity());
        holder.Time.setText(historyList.get(position).getDate());
        return convertView;
    }

}