package com.tokopedia.navigation.domain.model;

import java.util.List;

/**
 * Created by meta on 26/07/18.
 */
public class DrawerNotification {

    private String id;
    private String title;
    private List<ChildDrawerNotification> childs;

    public static class ChildDrawerNotification {

        private String id;
        private String title;
        private Integer badge;

        public ChildDrawerNotification(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getBadge() {
            return badge;
        }

        public void setBadge(Integer badge) {
            this.badge = badge;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
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

    public List<ChildDrawerNotification> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildDrawerNotification> childs) {
        this.childs = childs;
    }
}
