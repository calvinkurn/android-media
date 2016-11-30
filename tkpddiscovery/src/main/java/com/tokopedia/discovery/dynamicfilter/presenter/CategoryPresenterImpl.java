package com.tokopedia.discovery.dynamicfilter.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.discovery.dynamicfilter.facade.HadesNetwork;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.DynamicObject;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by noiz354 on 7/12/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class CategoryPresenterImpl extends CategoryPresenter {

    private static final String TAG = CategoryPresenterImpl.class.getSimpleName();
    private HadesV1Model hadesV1Model;
    private CategoryDatabaseManager categoryDatabaseManager = new CategoryDatabaseManager();
    private int categoryId;

    public CategoryPresenterImpl(CategoryView view) {
        super(view);
    }

    @Override
    public String getMessageTAG() {
        return "CategoryPresenterImpl";
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return "CategoryPresenterImpl";
    }

    @Override
    public void initData(@NonNull Context context) {
        if (!isAfterRotate) {

        }

        if (hadesV1Model == null || hadesV1Model.getData().getCategories().isEmpty()) {
            if (categoryDatabaseManager.getDepartmentParent() != null && categoryDatabaseManager.getDepartmentParent().size() > 0) {
                view.setupAdapter(getDynamicObjectList());
                view.setupRecyclerView();
            } else {
                fetchAllDepartment(context);
            }
        } else {
            List<HadesV1Model.Category> categoryList = hadesV1Model.getData().getCategories();
            view.setupAdapter(getDynamicObjectList(categoryList));
            view.setupRecyclerView();
        }

    }

    private void fetchAllDepartment(final Context context) {
        view.showLoading(true);
        compositeSubscription.add(HadesNetwork.fetchDepartment(-1, -2, HadesNetwork.TREE)// fetch all department
                .map(new Func1<Response<HadesV1Model>, Response<HadesV1Model>>() {
                    @Override
                    public Response<HadesV1Model> call(Response<HadesV1Model> hadesV1ModelResponse) {


                        categoryDatabaseManager.deleteAll();

                        //save to db
                        saveHadesV1Model(hadesV1ModelResponse, categoryDatabaseManager);
                        //save to db

                        //get only parent
                        List<CategoryDB> departmentParent = categoryDatabaseManager.getDepartmentParent();
                        int[] depCount = new int[departmentParent.size()];
                        for (int i = 0; i < departmentParent.size(); i++) {
                            CategoryDB kategori = departmentParent.get(i);
                            depCount[i] = categoryDatabaseManager.getDepartmentCount(kategori.getLevelId(), kategori.getDepartmentId());
                        }

                        return hadesV1ModelResponse;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<HadesV1Model>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                view.showLoading(false);
                                view.onMessageError(0, CommonUtils.generateMessageError(context, e.getMessage()));
                            }

                            @Override
                            public void onNext(Response<HadesV1Model> hadesV1ModelResponse) {
                                Log.d(TAG, "4 " + getMessageTAG() + hadesV1ModelResponse);
                                view.showLoading(false);
                                view.setupAdapter(getDynamicObjectList());
                                view.setupRecyclerView();
                            }
                        }
                ));
    }

    private void saveHadesV1Model(Response<HadesV1Model> hadesV1ModelResponse,
                                  CategoryDatabaseManager manager) {
        HadesV1Model body = hadesV1ModelResponse.body();
        HadesV1Model.Data data = body.getData();


        List<CategoryDB> categoryDBList = getCategoryDBList(data.getCategories(), 0);
        manager.setCategory(categoryDBList);
        manager.store();
    }

    private List<CategoryDB> getCategoryDBList(List<HadesV1Model.Category> categoryList, int parentCategoryId) {
        List<CategoryDB> categoryDBList = new ArrayList<>();
        for (HadesV1Model.Category category : categoryList) {
            CategoryDB categoryDB = getCategoryDB(category, parentCategoryId);
            categoryDBList.add(categoryDB);
            if (category.getChildList() != null) {
                categoryDBList.addAll(getCategoryDBList(category.getChildList(), Integer.parseInt(category.getId())));
            }
        }
        return categoryDBList;
    }

    private CategoryDB getCategoryDB(HadesV1Model.Category category, int parentCategoryId) {
        return new CategoryDB(
                category.getName(),
                category.getTree(),
                0,
                parentCategoryId,
                Integer.parseInt(category.getId()),
                category.getIdentifier());
    }

    @Override
    public void fetchArguments(Bundle argument) {
        List<Breadcrumb> breadCrumb = Parcels.unwrap(
                argument.getParcelable(DynamicFilterPresenter.EXTRA_PRODUCT_BREADCRUMB_LIST));
        categoryId = Integer.parseInt(argument.getString(DynamicFilterPresenter.EXTRA_CURRENT_CATEGORY, ""));

        if (breadCrumb != null) {
            hadesV1Model = setupDataBreadcrumb(breadCrumb);
        }
    }

    private HadesV1Model setupDataBreadcrumb(List<Breadcrumb> breadcrumbList) {
        HadesV1Model hadesV1Model = new HadesV1Model();
        HadesV1Model.Data data = new HadesV1Model.Data();

        data.setCategories(getCategoryList(breadcrumbList));
        hadesV1Model.setData(data);
        return hadesV1Model;

    }

    private List<HadesV1Model.Category> getCategoryList(List<Breadcrumb> breadcrumbList) {
        List<HadesV1Model.Category> categoryList = new ArrayList<>();
        for (Breadcrumb breadcrumbChild : breadcrumbList) {
            categoryList.add(getCategory(breadcrumbChild));
        }
        return categoryList;
    }

    /**
     * Convert Breadcrumb object to Category
     *
     * @param breadcrumb
     * @return
     */
    private HadesV1Model.Category getCategory(Breadcrumb breadcrumb) {
        HadesV1Model.Category category = new HadesV1Model.Category();
        category.setId(breadcrumb.id);
        category.setIdentifier(breadcrumb.identifier);
        category.setName(breadcrumb.name);
        category.setParent(Integer.valueOf(breadcrumb.parentId));
        category.setTree(Integer.valueOf(breadcrumb.tree));
        if (breadcrumb.child != null) {
            category.setChildList(getCategoryList(breadcrumb.child));
        }
        return category;
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {

    }


    /**
     * Get dynamic object from database
     *
     * @return
     */
    private List<DynamicObject> getDynamicObjectList() {
        List<DynamicObject> list = getDynamicObjectListFromDatabase(1);
        if (list.isEmpty()) {
            list = getDynamicObjectListFromDatabase(2);
            if (list.isEmpty()) {
                list = getDynamicObjectListFromDatabase(3);
                if (list.isEmpty()) {
                    list = getDynamicObjectListFromDatabase(0);
                }
            }
        }
        return list;
    }


    private List<DynamicObject> getDynamicObjectListFromDatabase(int level) {
        Log.d(TAG, "getDynamicObjectListFromDatabase level " + level + " currentCategoryId " + categoryId);
        categoryDatabaseManager = new CategoryDatabaseManager();
        List<CategoryDB> categoryDBList;
        if (level == 0) {
            categoryDBList = categoryDatabaseManager.getDepartementChild(categoryId);
        } else {
            categoryDBList = categoryDatabaseManager.getDepartmentChild(level, categoryId);
        }
        return getDynamicObjects(categoryDBList);
    }


    @NonNull
    private ArrayList<DynamicObject> getDynamicObjects(List<CategoryDB> departmentParent) {
        ArrayList<DynamicObject> parentObjectList = new ArrayList<>();
        Log.d(TAG, "List CategoryDB size " + departmentParent.size());
        for (int i = 0; i < departmentParent.size(); i++) {
            List<DynamicObject> nestedParentList = new ArrayList<>();
            CategoryDB level1 = departmentParent.get(i);
            Log.d(TAG, "CategoryDB dep_id " + level1.getDepartmentId() + " level id " + level1.getLevelId());
            if (level1.getDepartmentId() == categoryId) {
                int levelId_ONE = level1.getLevelId();
                List<CategoryDB> departmentChild = categoryDatabaseManager.getDepartmentChild(1 + levelId_ONE, level1.getDepartmentId());

                DynamicObject unnestedParent = DynamicObject.createOptionForAll(level1);
                nestedParentList.add(unnestedParent);

                // Every parent gets a few nested-parents
                // (we set their depth to 2 in order to facilitate the single expanded parent feature)
                for (int j = 0; j < departmentChild.size(); j++) {
                    CategoryDB level2 = departmentChild.get(j);
                    int levelId_TWO = level2.getLevelId();
                    List<CategoryDB> departmentChildLvl2 = categoryDatabaseManager.getDepartmentChild(1 + levelId_TWO, level2.getDepartmentId());

                    DynamicObject nestedParent = new DynamicObject(level2);
                    nestedParentList.add(nestedParent);

                    // Every nested parent gets some children
                    List<DynamicObject> children = new ArrayList<>();
                    unnestedParent = DynamicObject.createOptionForAll(level2);
                    children.add(unnestedParent);

                    for (int k = 0; k < departmentChildLvl2.size(); k++) {
                        CategoryDB level3 = departmentChildLvl2.get(k);
                        children.add(new DynamicObject(level3));
                    }

                    nestedParent.addChild(children, 2);
                }

                DynamicObject customParentObject = new DynamicObject(level1);
                customParentObject.addChild(nestedParentList, 1);
                parentObjectList.add(customParentObject);
            }
        }
        Log.d(TAG, "parentObjectList size " + parentObjectList.size());
        return parentObjectList;
    }

    /**
     * Get dynamic object from network
     *
     * @param categoryList
     * @return
     */
    private List<DynamicObject> getDynamicObjectList(List<HadesV1Model.Category> categoryList) {
        return getDynamicObjectList(categoryList, 1);
    }

    /**
     * Get dynamic object based on category list
     *
     * @param categoryList
     * @param indention
     * @return
     */
    private List<DynamicObject> getDynamicObjectList(List<HadesV1Model.Category> categoryList, int indention) {
        int nextIndention = indention + 1;
        List<DynamicObject> dynamicObjectList = new ArrayList<>();
        for (HadesV1Model.Category category : categoryList) {
            DynamicObject dynamicObject = new DynamicObject(category);
            dynamicObjectList.add(dynamicObject);
            if (nextIndention < 4 && category.getChildList() != null && category.getChildList().size() > 0) {
                dynamicObject.addChild(getDynamicObjectList(category.getChildList(), nextIndention), indention);
            }
        }
        return dynamicObjectList;
    }

}
