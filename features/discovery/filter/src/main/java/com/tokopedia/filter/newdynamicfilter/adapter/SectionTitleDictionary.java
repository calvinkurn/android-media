package com.tokopedia.filter.newdynamicfilter.adapter;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;


public class SectionTitleDictionary {
    private static final String SECTION_TITLE_POPULAR = "Populer";

    private Map<Integer, String> idToTitleMap = new HashMap<>();

    public SectionTitleDictionary() {
        idToTitleMap.put(SectionDividedItem.POPULAR_SECTION_ID, SECTION_TITLE_POPULAR);
    }

    public String getSectionTitle(int sectionId) {
        String title = idToTitleMap.get(sectionId);
        if (!TextUtils.isEmpty(title)) {
            return title;
        } else {
            return String.valueOf((char) sectionId);
        }
    }
}
