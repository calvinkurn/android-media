package com.tokopedia.discovery.catalog.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.discovery.catalog.listener.ICatalogSpecSectionChanged;
import com.tokopedia.discovery.catalog.model.SpecChild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author anggaprasetiyo on 10/18/16.
 */
public class CatalogSpecAdapterHelper implements ICatalogSpecSectionChanged {

    private LinkedHashMap<Section, ArrayList<SpecChild>> sectionDataMap = new LinkedHashMap<>();
    private ArrayList<Object> dataArrayList = new ArrayList<>();
    private HashMap<String, Section> sectionMap = new HashMap<>();
    private CatalogSpecAdapter catalogSpecAdapter;
    private RecyclerView recyclerView;

    public CatalogSpecAdapterHelper(Context context, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);
        this.recyclerView.setLayoutManager(layoutManager);
        this.catalogSpecAdapter = new CatalogSpecAdapter(context, dataArrayList, this);
        this.recyclerView.setAdapter(catalogSpecAdapter);
        this.recyclerView.setNestedScrollingEnabled(false);
    }

    public void notifyDataSetChanged() {
        generateDataList();
        catalogSpecAdapter.notifyDataSetChanged();
    }

    public void addSection(String section, ArrayList<SpecChild> items) {
        Section newSection;
        sectionMap.put(section, (newSection = new Section(section)));
        sectionDataMap.put(newSection, items);
    }

    @SuppressWarnings("unused")
    public void addItem(String section, SpecChild item) {
        sectionDataMap.get(sectionMap.get(section)).add(item);
    }

    @SuppressWarnings("unused")
    public void removeItem(String section, SpecChild item) {
        sectionDataMap.get(sectionMap.get(section)).remove(item);
    }

    @SuppressWarnings("unused")
    public void removeSection(String section) {
        sectionDataMap.remove(sectionMap.get(section));
        sectionMap.remove(section);
    }

    private void generateDataList() {
        dataArrayList.clear();
        for (Map.Entry<Section, ArrayList<SpecChild>> entry : sectionDataMap.entrySet()) {
            Section key;
            dataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                dataArrayList.addAll(entry.getValue());
        }
    }

    @Override
    public void onSectionStateChanged(Section section, boolean isOpen) {
        if (!recyclerView.isComputingLayout()) {
            section.isExpanded = isOpen;
            notifyDataSetChanged();
        }
    }

    public static class Section {

        private final String name;

        public boolean isExpanded;

        Section(String name) {
            this.name = name;
            this.isExpanded = false;
        }

        public String getName() {
            return name;
        }
    }
}