package com.tokopedia.navigation.analytics.performance;

public class PerformanceData {
    private String allFramesTag = "";
    private String jankyFramesTag = "";
    private String jankyFramesPercentageTag = "";
    private int allFrames = 0;
    private int jankyFrames = 0;

    public PerformanceData(String allFramesTag, String jankyFramesTag, String jankyFramesPercentageTag) {
        this.allFramesTag = allFramesTag;
        this.jankyFramesTag = jankyFramesTag;
        this.jankyFramesPercentageTag = jankyFramesPercentageTag;
    }

    public int getAllFrames() {
        return allFrames;
    }

    public void setAllFrames(int allFrames) {
        this.allFrames = allFrames;
    }

    public int getJankyFrames() {
        return jankyFrames;
    }

    public void setJankyFrames(int jankyFrames) {
        this.jankyFrames = jankyFrames;
    }

    public int getJankyFramePercentage() {
        if (this.allFrames == 0) return 0;
        return (int)((float)this.jankyFrames/(float)this.allFrames * 100);
    }

    public void incrementAllFrames() {
        this.allFrames++;
    }

    public void incremenetJankyFrames() {
        this.jankyFrames++;
    }

    public String getAllFramesTag() {
        return allFramesTag;
    }

    public void setAllFramesTag(String allFramesTag) {
        this.allFramesTag = allFramesTag;
    }

    public String getJankyFramesTag() {
        return jankyFramesTag;
    }

    public void setJankyFramesTag(String jankyFramesTag) {
        this.jankyFramesTag = jankyFramesTag;
    }

    public String getJankyFramesPercentageTag() {
        return jankyFramesPercentageTag;
    }

    public void setJankyFramesPercentageTag(String jankyFramesPercentageTag) {
        this.jankyFramesPercentageTag = jankyFramesPercentageTag;
    }
}
