package com.tokopedia.home.beranda.presentation.view.viewmodel;

import com.google.android.gms.tagmanager.DataLayer;

public class FeedTabModel {
    public static final String DATA_ID = "id";
    public static final String DATA_NAME = "name";
    public static final String DATA_CREATIVE = "creative";
    public static final String DATA_POSITION = "position";
    public static final String DATA_CREATIVE_URL = "creative_url";

    public static final String DATA_VALUE_NAME = "/ - homepage recommendation tab - %s";
    private final int position;
    private String id;
    private String name;
    private String imageUrl;

    public FeedTabModel(String id,
                        String name,
                        String imageUrl,
                        int position) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPosition() {
        return position;
    }

    public Object convertFeedTabModelToDataObject() {
        return DataLayer.mapOf(
                DATA_ID, getId(),
                DATA_NAME, String.format(DATA_VALUE_NAME, getName()),
                DATA_POSITION, getPosition(),
                DATA_CREATIVE, getName(),
                DATA_CREATIVE_URL, getImageUrl());
    }
}
