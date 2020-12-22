package com.tokopedia.home.explore.view.adapter.datamodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 2/6/18.
 */

public class ExploreSectionDataModel {

    String title;
    int icon;
    DynamicHomeIcon.UseCaseIcon useCaseIcon;
    List<Visitable> visitableList = new ArrayList<>();

    public ExploreSectionDataModel() {
    }

    public ExploreSectionDataModel(String title, int icon, List<Visitable> visitableList) {
        this.title = title;
        this.icon = icon;
        this.visitableList = visitableList;
    }

    public DynamicHomeIcon.UseCaseIcon getUseCaseIcon() {
        return useCaseIcon;
    }

    public void setUseCaseIcon(DynamicHomeIcon.UseCaseIcon useCaseIcon) {
        this.useCaseIcon = useCaseIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public List<Visitable> getVisitableList() {
        return visitableList;
    }


    public void addVisitable(Visitable visitableList) {
        this.visitableList.add(visitableList);
    }

}
