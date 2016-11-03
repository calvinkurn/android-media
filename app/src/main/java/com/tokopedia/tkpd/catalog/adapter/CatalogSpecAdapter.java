package com.tokopedia.tkpd.catalog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.catalog.listener.ICatalogSpecSectionChanged;
import com.tokopedia.tkpd.catalog.model.SpecChild;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        switch (viewType) {
            case TYPE_VIEW_SPEC_ITEM:
                return new SpecChildHolder(view);
            case TYPE_VIEW_SPEC_SECTION:
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
                else stringBuilder.append(Html.fromHtml(string)).append("\n");
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

        @Bind(R.id.tv_label)
        TextView tvLabel;
        @Bind(R.id.tv_value)
        TextView tvValue;

        SpecChildHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SectionHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.toggle_button_section)
        ToggleButton toggleButton;

        SectionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private boolean isSection(int position) {
        return dataArrayList.get(position) instanceof CatalogSpecAdapterHelper.Section;
    }
}
