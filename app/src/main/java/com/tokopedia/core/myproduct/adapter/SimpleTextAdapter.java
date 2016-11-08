package com.tokopedia.core.myproduct.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.myproduct.fragment.ChooserFragment;
import com.tokopedia.core.myproduct.model.SimpleTextModel;

import java.util.List;

/**
 * Created by m.normansyah on 12/9/15.
 */
public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

    private int empty = 0;
    private final List<SimpleTextModel> mValues;
    private final ChooserFragment.OnListFragmentInteractionListener mListener;

    public SimpleTextAdapter(List<SimpleTextModel> items, ChooserFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(empty == 1 && position == 0){
            String emptyString = "Tidak ada hasil";
            Spannable WordtoSpan = new SpannableString(Html.fromHtml(emptyString));
            WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY),0, emptyString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mContentView.setText(WordtoSpan);
        } else {
            String query = mValues.get(position).getQuery();
            if (query != null && !query.equals("")) {
                holder.mItem = mValues.get(position);
                holder.mIdView.setText(mValues.get(position).getText());

                Spannable WordtoSpan = new SpannableString(Html.fromHtml(mValues.get(position).getText()));
                int start = mValues.get(position).getText().toLowerCase().indexOf(query.toLowerCase());
                //int end = mValues.get(position).getText().toLowerCase().indexOf(query.charAt(query.length() - 1)) + 1;
                if (start != -1) {
                    WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), start, start + query.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.mContentView.setText(WordtoSpan);
                } else {
                    holder.mContentView.setText(Html.fromHtml(mValues.get(position).getText()));
                }
            } else {
                holder.mItem = mValues.get(position);
                holder.mIdView.setText(mValues.get(position).getText());
                holder.mContentView.setText(Html.fromHtml(mValues.get(position).getText()));
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onLongClick();
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if(mValues.isEmpty()){
            empty = 1;
            return empty;
        } else {
            empty = 0;
            return mValues.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public SimpleTextModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
