package com.tokopedia.navigation.domain.model;

import java.util.List;

/**
 * Created by meta on 26/07/18.
 */
public class DrawerNotification {

    private Integer id;
    private String title;
    private List<ChildDrawerNotification> childs;

    public static class ChildDrawerNotification {

        private Integer id;
        private String title;
        private String applink;
        private Integer badge;

        public ChildDrawerNotification(Integer id, String title) {
            this.id = id;
            this.title = title;
        }

        public ChildDrawerNotification(Integer id, String title, String applink) {
            this.id = id;
            this.title = title;
            this.applink = applink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public Integer getBadge() {
            return badge;
        }

        public void setBadge(Integer badge) {
            this.badge = badge;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
