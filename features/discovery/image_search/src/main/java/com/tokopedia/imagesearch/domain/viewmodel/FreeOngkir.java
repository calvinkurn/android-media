package com.tokopedia.imagesearch.domain.viewmodel;

public class FreeOngkir {
    private boolean isActive;
    private String imageUrl;

    public FreeOngkir(boolean isActive, String imageUrl) {
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
