package com.tokopedia.tkpd.rescenter.create.customadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.List;

/**
 * Created on 7/11/16.
 */
public class SolutionSpinnerAdapter extends ArrayAdapter<CreateResCenterFormData.SolutionData> {

    private class ViewHolder {
        TextView textView;
    }

    public SolutionSpinnerAdapter(Context context, int textViewResourceId, List<CreateResCenterFormData.SolutionData> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.textView.setText(getContext().getString(R.string.index_spinner_0_default));
        } else {
            holder.textView.setText(getItem(position - 1).getSolutionText());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomDropDownView(position, parent);
    }

    private View getCustomDropDownView(int position, ViewGroup parent) {
        ViewHolder holderDropDown = new ViewHolder();
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        View localView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        holderDropDown.textView = (TextView) localView.findViewById(android.R.id.text1);

        if (position == 0) {
            holderDropDown.textView.setText(getContext().getString(R.string.index_spinner_0_default));
        } else {
            holderDropDown.textView.setText(getItem(position - 1).getSolutionText());
        }

        return localView;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }
}
