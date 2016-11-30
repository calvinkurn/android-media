/*
 * Created By Kulomady on 11/25/16 11:54 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:54 PM
 */

package com.tokopedia.core.discovery.dynamicfilter.facade;

import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by noiz354 on 7/12/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class HadesNetwork {

    public static final String LIST_TEXT = "list";
    public static final String TREE_TEXT = "tree";
    public static final String SEPARATOR = ";";
    public static final String BREADCRUMB = "breadcrumb";
    public static final int LIST = 1;
    public static final int TREE = 0;
    public static final int BREADCRUMB_LEVEL = -1;

    public static final HadesApi createHadesNetwork() {
        HadesApi hadesApi = RetrofitUtils.createRetrofit("https://hades.tokopedia.com").create(HadesApi.class);
        return hadesApi;
    }

    /**
     * @param department 0 means department parent, larger than zero is proceed, less than 0
     * @param level      -1 if {@value BREADCRUMB_LEVEL}, level 1,2,3
     * @param view       {@value TREE} for tree, {@value LIST} for list
     */
    public static Observable<Response<HadesV1Model>> fetchDepartment(int department, int level, int view) {
        HadesApi hadesNetwork = createHadesNetwork();
        Observable<Response<HadesV1Model>> category = null;
        switch (view) {
            case TREE:
                String filterValue = getTreeView() + getlevel(level);
                if (isDepartmentParent(department, level)) {
                    category = hadesNetwork.getCategory("", filterValue);
                } else if (level == BREADCRUMB_LEVEL) {
                    filterValue = getTreeView() + getLevelBreadcrumb();
                    category = hadesNetwork.getCategory(Integer.toString(department), filterValue);
                } else if (department < 0) {// get all category
                    filterValue = getTreeView();
                    category = hadesNetwork.getCategory("", filterValue);
                } else {
                    category = hadesNetwork.getCategory(Integer.toString(department), filterValue);
                }
                break;
            case LIST:
            default:
                filterValue = getListView() + getlevel(level);
                if (isDepartmentParent(department, level)) {
                    category = hadesNetwork.getCategory("", filterValue);
                } else if (level == BREADCRUMB_LEVEL) {
                    filterValue = getListView() + getLevelBreadcrumb();
                    category = hadesNetwork.getCategory(Integer.toString(department), filterValue);
                } else if (department < 0) {// get all category
                    filterValue = getListView();
                    category = hadesNetwork.getCategory("", filterValue);
                } else {
                    category = hadesNetwork.getCategory(Integer.toString(department), filterValue);
                }
                break;
        }

        return category;
    }

    public static boolean isDepartmentParent(int department, int level) {
        return department == 0 && level == 1;
    }

    public static String getTreeView() {
        return "type==" + TREE_TEXT;
    }

    public static String getListView() {
        return "type==" + LIST_TEXT;
    }

    public static String getlevel(int level) {
        return SEPARATOR + "level==" + level;
    }

    public static String getLevelBreadcrumb() {
        return SEPARATOR + "level==" + BREADCRUMB;
    }
}
