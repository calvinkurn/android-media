package com.tokopedia.review.feature.inbox.buyerreview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.HeaderOptionUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.OptionUiModel;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface FilterListener {
        void onFilterSelected(OptionUiModel optionUiModel);

        void onFilterUnselected(OptionUiModel optionUiModel);
    }

    private static final int VIEW_HEADER = 101;

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        Typography title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.title = (Typography) itemView.findViewById(R.id.title);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Typography filter;
        ImageView check;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.filter = (Typography) itemView.findViewById(R.id.filter);
            this.check = (ImageView) itemView.findViewById(R.id.check);
            this.mainView = itemView.findViewById(R.id.main_view);
            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OptionUiModel viewModel : listOption) {
                        if (viewModel == listOption.get(getAdapterPosition()))
                            viewModel.setSelected(!viewModel.isSelected());
                        else if (viewModel.getKey() == listOption.get(getAdapterPosition()).getKey())
                            viewModel.setSelected(false);
                    }

                    if (listOption.get(getAdapterPosition()).isSelected()) {
                        listener.onFilterSelected(listOption.get(getAdapterPosition()));
                    }else{
                        listener.onFilterUnselected(listOption.get(getAdapterPosition()));

                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    private final ArrayList<OptionUiModel> listOption;
    private final FilterListener listener;
    private Context context;


    public static InboxReputationFilterAdapter createInstance(Context context, FilterListener listener,
                                                              ArrayList<OptionUiModel>
                                                                      listOption) {
        return new InboxReputationFilterAdapter(context, listener, listOption);
    }

    private InboxReputationFilterAdapter(Context context, FilterListener listener,
                                         ArrayList<OptionUiModel> listOption) {
        this.listener = listener;
        this.listOption = listOption;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listview_filter_header, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listview_filter, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent,
                                 int position) {
        if (getItemViewType(position) == VIEW_HEADER) {
            HeaderViewHolder holder = (HeaderViewHolder) parent;
            holder.title.setText(listOption.get(position).getName());
        } else {
            ViewHolder holder = (ViewHolder) parent;
            holder.filter.setText(listOption.get(position).getName());
            if (listOption.get(position).isSelected()) {
                holder.filter.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400));
                holder.check.setVisibility(View.VISIBLE);
            } else {
                holder.filter.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
                holder.check.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (listOption.get(position) instanceof HeaderOptionUiModel)
            return VIEW_HEADER;
        else return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listOption.size();
    }

    public void resetFilter() {
        for (OptionUiModel optionUiModel : listOption) {
            optionUiModel.setSelected(false);
        }
        notifyDataSetChanged();
    }
}
