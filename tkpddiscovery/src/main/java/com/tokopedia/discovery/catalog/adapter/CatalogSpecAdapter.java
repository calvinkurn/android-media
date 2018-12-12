package com.tokopedia.discovery.catalog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tokopedia.core2.R;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.catalog.listener.ICatalogSpecSectionChanged;
import com.tokopedia.discovery.catalog.model.SpecChild;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 10/18/16.
 */

class CatalogSpecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_VIEW_SPEC_SECTION = R.layout.catalog_spec_section_item;
    private static final int TYPE_VIEW_SPEC_ITEM = R.layout.catalog_spec_item;

    private ArrayList<Object> dataArrayList;
    private final Context context;
    private final ICatalogSpecSectionChanged catalogSpecSectionChanged;

    CatalogSpecAdapter(Context context, ArrayList<Object> dataArrayList,
                       ICatalogSpecSectionChanged ICatalogSpecSectionChanged) {
        this.context = context;
        catalogSpecSectionChanged = ICatalogSpecSectionChanged;
        this.dataArrayList = dataArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if (viewType == TYPE_VIEW_SPEC_ITEM) {
            return new SpecChildHolder(view);
        } else if (viewType == TYPE_VIEW_SPEC_SECTION) {
            return new SectionHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionHolder) {
            final SectionHolder sectionHolder = (SectionHolder) holder;
            final CatalogSpecAdapterHelper.Section data =
                    (CatalogSpecAdapterHelper.Section) dataArrayList.get(position);
            sectionHolder.tvName.setText(data.getName());
            sectionHolder.toggleButton.setChecked(data.isExpanded);
            sectionHolder.toggleButton.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            catalogSpecSectionChanged.onSectionStateChanged(data, isChecked);
                        }
                    }
            );
            sectionHolder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sectionHolder.toggleButton.setChecked(!sectionHolder.toggleButton.isChecked());
                }
            });
        } else if (holder instanceof SpecChildHolder) {
            SpecChildHolder specChildHolder = (SpecChildHolder) holder;
            final SpecChild data = (SpecChild) dataArrayList.get(position);
            specChildHolder.tvLabel.setText(data.getSpecKey());
            StringBuilder stringBuilder = new StringBuilder();
            List<String> specVal = data.getSpecVal();
            for (int i = 0, specValSize = specVal.size(); i < specValSize; i++) {
                String string = specVal.get(i);
                if (i == specValSize - 1) stringBuilder.append(string);
                else stringBuilder.append(MethodChecker.fromHtml(string)).append("\n");
            }
            specChildHolder.tvValue.setText(stringBuilder.toString());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isSection(position) ? TYPE_VIEW_SPEC_SECTION : TYPE_VIEW_SPEC_ITEM;
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    static class SpecChildHolder extends RecyclerView.ViewHolder {

        private TextView tvLabel;
        private TextView tvValue;

        SpecChildHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            tvLabel = view.findViewById(R.id.tv_label);
            tvValue = view.findViewById(R.id.tv_value);
        }
    }

    static class SectionHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ToggleButton toggleButton;

        SectionHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            tvName = view.findViewById(R.id.tv_name);
            toggleButton = view.findViewById(R.id.toggle_button_section);
        }
    }

    private boolean isSection(int position) {
        return dataArrayList.get(position) instanceof CatalogSpecAdapterHelper.Section;
    }
}
