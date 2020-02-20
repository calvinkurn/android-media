package com.tokopedia.opportunity.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.opportunity.R;
import com.tokopedia.opportunity.viewmodel.FilterViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 4/7/17.
 */

public class OpportunityFilterTitleAdapter extends RecyclerView.Adapter<OpportunityFilterTitleAdapter.ViewHolder> {

    private final Context context;

    public interface FilterListener {
        void onTitleClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView filterTitle;
        ImageView redDot;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            filterTitle = (TextView) itemView.findViewById(R.id.title_filter);
            redDot = (ImageView) itemView.findViewById(R.id.red_circle);
            mainView = itemView.findViewById(R.id.main_view);

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (FilterViewModel item : list) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            notifyItemChanged(item.getPosition());
                        }
                    }
                    list.get(getAdapterPosition()).setSelected(true);
                    notifyItemChanged(getAdapterPosition());
                    listener.onTitleClicked(getAdapterPosition());
                }
            });
        }
    }

    ArrayList<FilterViewModel> list;
    FilterListener listener;

    public static OpportunityFilterTitleAdapter createInstance(Context context, FilterListener listener) {
        return new OpportunityFilterTitleAdapter(context, listener);
    }

    public OpportunityFilterTitleAdapter(Context context, FilterListener listener) {
        list = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_opportunity_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list.get(position).isSelected())
            holder.mainView.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.white));
        else
            holder.mainView.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.transparent));

        if (list.get(position).isActive())
            holder.redDot.setVisibility(View.VISIBLE);
        else
            holder.redDot.setVisibility(View.INVISIBLE);

        holder.filterTitle.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<FilterViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
