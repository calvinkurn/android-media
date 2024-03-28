package com.tokopedia.abstraction.base.view.appupdate.model;

/**
 * Created by okasurya on 7/25/17.
 */

public class DetailUpdate {
    private boolean isNeedUpdate;
    private boolean isNeedSpecificUpdate;
    private long latestVersionCode;
    private boolean isForceUpdate;
    private String updateTitle;
    private String updateMessage;
    private String updateLink;
    private boolean inAppUpdateEnabled;

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public boolean isNeedSpecificUpdate() {
        return isNeedSpecificUpdate;
    }

    public boolean isInAppUpdateEnabled() {
        return inAppUpdateEnabled;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public void setNeedSpecificUpdate(boolean needSpecificUpdate) {
        isNeedSpecificUpdate = needSpecificUpdate;
    }

    public long getLatestVersionCode() {
        return latestVersionCode;
    }

    public void setLatestVersionCode(long latestVersionCode) {
        this.latestVersionCode = latestVersionCode;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        isForceUpdate = forceUpdate;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getUpdateLink() {
        return updateLink;
    }

    public void setUpdateLink(String updateLink) {
        this.updateLink = updateLink;
    }

    public void setInAppUpdateEnabled(boolean inAppUpdateEnabled) {
        this.inAppUpdateEnabled = inAppUpdateEnabled;
    }
}
