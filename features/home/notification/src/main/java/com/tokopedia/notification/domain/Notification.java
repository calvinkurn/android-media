package com.tokopedia.notification.domain;

import java.util.List;

/**
 * Created by meta on 21/06/18.
 */
public class Notification {

    private String id;
    private String title;
    private List<ChildNotification> childs;

    public static class ChildNotification {

        private String title;
        private boolean isNew;
        private Integer badge;

        public ChildNotification(String title, boolean isNew, Integer badge) {
            this.title = title;
            this.isNew = isNew;
            this.badge = badge;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isNew() {
            return isNew;
        }

        public void setNew(boolean aNew) {
            isNew = aNew;
        }

        public Integer getBadge() {
            return badge;
        }

        public void setBadge(Integer badge) {
            this.badge = badge;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChildNotification> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildNotification> childs) {
        this.childs = childs;
    }
}
