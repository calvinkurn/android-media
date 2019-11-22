package com.tokopedia.affiliate.feature.explore.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.AutoCompleteViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 24/10/18.
 */
public class AutoCompleteSearchAdapter extends RecyclerView.Adapter<AutoCompleteSearchAdapter.Holder> {

    private ExploreContract.View mainView;
    private List<AutoCompleteViewModel> modelList = new ArrayList<>();

    public AutoCompleteSearchAdapter(ExploreContract.View mainView, List<AutoCompleteViewModel> modelList) {
        this.mainView = mainView;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mainView.getContext()).inflate(R.layout.item_af_search_auto_complete, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AutoCompleteViewModel model = modelList.get(position);
        bindView(holder, model);
        bindViewListener(holder, model);

    }

    private void bindView(Holder holder, AutoCompleteViewModel model) {
        holder.tvAutoComplete.setText(MethodChecker.fromHtml(model.getFormatted()));
    }

    private void bindViewListener(Holder holder, AutoCompleteViewModel model) {
        holder.tvAutoComplete.setOnClickListener(v -> {
            mainView.onAutoCompleteItemClicked(model.getText(), model.getKeyword());
        });

        holder.ivAutoComplete.setOnClickListener(v -> {
            mainView.onAutoCompleteIconClicked(model.getText());
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvAutoComplete;
        ImageView ivAutoComplete;

        public Holder(View itemView) {
            super(itemView);
            tvAutoComplete = (TextView) itemView.findViewById(R.id.tv_auto_complete);
            ivAutoComplete = (ImageView) itemView.findViewById(R.id.iv_auto_complete);
        }
    }

    public void clearAdapter() {
        modelList = new ArrayList<>();
        notifyDataSetChanged();
    }
}
