package com.tokopedia.pushnotif.model;

import java.util.List;

/**
 * @author ricoharisin .
 */

public class SummaryNotificationModel {

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public List<String> getHistoryString() {
        return historyString;
    }

    public void setHistoryString(List<String> historyString) {
        this.historyString = historyString;
    }

    private String summaryText;

    private List<String> historyString;

    public int getTotalHistory() {
        return historyString.size();
    }

    public void setTotalHistory(int totalHistory) {
        this.totalHistory = totalHistory;
    }

    private int totalHistory;
}
