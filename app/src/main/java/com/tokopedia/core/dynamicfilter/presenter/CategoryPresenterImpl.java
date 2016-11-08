package com.tokopedia.core.dynamicfilter.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.dynamicfilter.facade.HadesNetwork;
import com.tokopedia.core.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.core.dynamicfilter.model.DynamicObject;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.dynamicfilter.presenter.DynamicFilterPresenter.BREADCRUMB;
import static com.tokopedia.core.dynamicfilter.presenter.DynamicFilterPresenter.CURR_CATEGORY;

/**
 * Created by noiz354 on 7/12/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class CategoryPresenterImpl extends CategoryPresenter {

    private static final String TAG = CategoryPresenterImpl.class.getSimpleName();
    HadesV1Model hadesV1Model;
    CategoryDatabaseManager manager = new CategoryDatabaseManager();
    private String currentCategory;

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
            if (manager.getDepartmentParent() != null && manager.getDepartmentParent().size() > 0) {
                view.setupAdapter(setupCategoryData(context));
                view.setupRecyclerView();
            } else {
                fetchAllDepartment(context);
            }
        } else {
            view.setupAdapter(setupCategoryData(context, hadesV1Model));
            view.setupRecyclerView();
        }

    }

    private void fetchAllDepartment(final Context context) {
        view.showLoading(true);
        compositeSubscription.add(HadesNetwork.fetchDepartment(-1, -2, HadesNetwork.TREE)// fetch all department
                .map(new Func1<Response<HadesV1Model>, Response<HadesV1Model>>() {
                    @Override
                    public Response<HadesV1Model> call(Response<HadesV1Model> hadesV1ModelResponse) {


                        // delete all existing database
                        manager.deleteAll();
                        // [END] delete all existing database

                        //[START] save to db
                        saveHadesV1Model(hadesV1ModelResponse, manager);
                        //[END] save to db

                        // get only parent
                        List<CategoryDB> departmentParent = manager.getDepartmentParent();
                        int[] depCount = new int[departmentParent.size()];
                        for (int i = 0; i < departmentParent.size(); i++) {
                            CategoryDB kategori = departmentParent.get(i);
                            depCount[i] = manager.getDepartmentCount(kategori.getLevelId(), kategori.getDepartmentId());
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
                                view.setupAdapter(setupCategoryData(context));
                                view.setupRecyclerView();
                            }
                        }
                ));
    }

    public static void saveHadesV1Model(Response<HadesV1Model> hadesV1ModelResponse, CategoryDatabaseManager manager) {
        HadesV1Model body = hadesV1ModelResponse.body();
        HadesV1Model.Data data = body.getData();
        List<CategoryDB> resultCategories = new ArrayList<CategoryDB>();
        List<HadesV1Model.Category> level1List = data.getCategories();
        for (HadesV1Model.Category level1 :
                level1List) {
            //[START] LEVEL 1
            resultCategories.add(new CategoryDB(
                    level1.getName(),
                    level1.getTree(),
                    0,
                    0,
                    Integer.parseInt(level1.getId()),
                    level1.getIdentifier()));
            //[END] LEVEL 1

            List<HadesV1Model.Child> level2List = level1.getChild();
            for (HadesV1Model.Child level2 :
                    level2List) {
                //[START] LEVEL 2
                resultCategories.add(new CategoryDB(
                        level2.getName(),
                        level2.getTree(),
                        0,
                        Integer.parseInt(level1.getId()),
                        Integer.parseInt(level2.getId()),
                        level2.getIdentifier()
                ));
                //[END] LEVEL 2

                List<HadesV1Model.Child_> level3List = level2.getChild();
                for (HadesV1Model.Child_ level3 :
                        level3List) {
                    //[START] LEVEL 3
                    resultCategories.add(new CategoryDB(
                            level3.getName(),
                            level3.getTree(),
                            0,
                            Integer.parseInt(level2.getId()),
                            Integer.parseInt(level3.getId()),
                            level3.getIdentifier()
                    ));
                    //[END] LEVEL 3
                }
            }
        }

        manager.setCategory(resultCategories);
        manager.store();
    }

    @Override
    public void fetchArguments(Bundle argument) {
        List<Breadcrumb> breadCrumb = Parcels.unwrap(argument.getParcelable(BREADCRUMB));
        currentCategory = argument.getString(CURR_CATEGORY, "");
        if (breadCrumb != null) {
            hadesV1Model = setupDataBreadcrumb(breadCrumb);
        }
    }

    private HadesV1Model setupDataBreadcrumb(List<Breadcrumb> breadCrumb) {
        HadesV1Model hadesV1Model = new HadesV1Model();
        HadesV1Model.Data data = new HadesV1Model.Data();
        List<HadesV1Model.Category> categories = new ArrayList<>();

        for (Breadcrumb breadcrumb : breadCrumb) {
            HadesV1Model.Category category = new HadesV1Model.Category();
            category.setId(breadcrumb.id);
            category.setIdentifier(breadcrumb.identifier);
            category.setName(breadcrumb.name);
            category.setParent(Integer.valueOf(breadcrumb.parentId));
            category.setTree(Integer.valueOf(breadcrumb.tree));
            if (breadcrumb.child != null) {
                List<HadesV1Model.Child> children = new ArrayList<>();
                for (Breadcrumb breadcrumb2 : breadcrumb.child) {
                    HadesV1Model.Child child = new HadesV1Model.Child();
                    child.setId(breadcrumb2.id);
                    child.setIdentifier(breadcrumb2.id);
                    child.setTree(Integer.valueOf(breadcrumb2.tree));
                    child.setParent(Integer.valueOf(breadcrumb2.parentId));
                    child.setName(breadcrumb2.name);
                    if (breadcrumb2.child != null) {
                        List<HadesV1Model.Child_> children_ = new ArrayList<>();
                        for (Breadcrumb breadcrumb3 : breadcrumb2.child) {
                            HadesV1Model.Child_ child_ = new HadesV1Model.Child_();
                            child_.setId(breadcrumb3.id);
                            child_.setIdentifier(breadcrumb3.id);
                            child_.setTree(Integer.valueOf(breadcrumb3.tree));
                            child_.setParent(Integer.valueOf(breadcrumb3.parentId));
                            child_.setName(breadcrumb3.name);
                            children_.add(child_);
                        }
                        child.setChild(children_);
                    }
                    children.add(child);
                }
                category.setChild(children);
            }
            categories.add(category);
        }

        data.setCategories(categories);
        hadesV1Model.setData(data);

        return hadesV1Model;

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
        if (!isAfterRotate) {

        }
    }


    private ArrayList<DynamicObject> setupCategoryData(Context context) {
        return setupCategoryData(context, null);
    }

    private ArrayList<DynamicObject> setupCategoryData(Context context, HadesV1Model hadesV1Model) {
        List<HadesV1Model.Category> categories;
        if (hadesV1Model != null) {
            categories = hadesV1Model.getData().getCategories();
        } else {
            ArrayList<DynamicObject> list = lookupCategoryFromDB(1, currentCategory);
            if (list.isEmpty()) {
                list = lookupCategoryFromDB(2, currentCategory);
                if (list.isEmpty()) {
                    list = lookupCategoryFromDB(3, currentCategory);
                    if (list.isEmpty()) {
                        list = lookupCategoryFromDB(currentCategory);
                    }
                }
            }
            return list;
        }

        ArrayList<DynamicObject> parentObjectList = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            ArrayList<DynamicObject> nestedParentList = new ArrayList<>();
            HadesV1Model.Category level1 = categories.get(i);
            List<HadesV1Model.Child> departmentChild = level1.getChild();
            for (int j = 0; j < departmentChild.size(); j++) {
                HadesV1Model.Child level2 = departmentChild.get(j);
                List<HadesV1Model.Child_> departmentChildLvl2 = level2.getChild();
                DynamicObject nestedParent = new DynamicObject(level2, departmentChildLvl2.size());
                nestedParentList.add(nestedParent);
                // Every nested parent gets some children
                ArrayList<DynamicObject> children = new ArrayList<>();
                for (int k = 0; k < departmentChildLvl2.size(); k++) {
                    HadesV1Model.Child_ level3 = departmentChildLvl2.get(k);
                    DynamicObject object = new DynamicObject(level3, departmentChildLvl2.size());
                    children.add(object);
                }
                nestedParent.addChild(children, 2);
            }

            DynamicObject dynamicObject = new DynamicObject(level1, departmentChild.size());
            dynamicObject.addChild(nestedParentList, 1);
            parentObjectList.add(dynamicObject);
        }
        return parentObjectList;
    }

    public ArrayList<DynamicObject> lookupCategoryFromDB(String currentCategory) {
        return lookupCategoryFromDB(0, currentCategory);
    }

    public ArrayList<DynamicObject> lookupCategoryFromDB(int level, String currentCategory) {
        Log.d(TAG, "lookupCategoryFromDB level " + level + " currentCategoryId " + currentCategory);
        manager = new CategoryDatabaseManager();
        List<CategoryDB> categoryDBList = new ArrayList<>();
        if (level == 0) {
            categoryDBList = manager.getDepartementChild(Integer.parseInt(currentCategory));
        } else {
            categoryDBList = manager.getDepartmentChild(level, Integer.parseInt(currentCategory));
        }
        return getDynamicObjects(currentCategory, categoryDBList);
    }

    public ArrayList<DynamicObject> getFromDB(String currentCategory) {
        Log.d(TAG, "getFromDB currentCategoryId " + currentCategory);
        List<CategoryDB> categoryDBList = manager.getDepartmentParent();
        return getDynamicObjects(currentCategory, categoryDBList);
    }

    @NonNull
    private ArrayList<DynamicObject> getDynamicObjects(String currentCategory, List<CategoryDB> departmentParent) {
        ArrayList<DynamicObject> parentObjectList = new ArrayList<>();
        Log.d(TAG, "List CategoryDB size " + departmentParent.size());
        for (int i = 0; i < departmentParent.size(); i++) {
            ArrayList<DynamicObject> nestedParentList = new ArrayList<>();
            CategoryDB level1 = departmentParent.get(i);
            Log.d(TAG, "CategoryDB dep_id " + level1.getDepartmentId() + " level id " + level1.getLevelId());
            if (level1.getDepartmentId() == Integer.parseInt(currentCategory)
                    // incase no current category
                    || currentCategory == null || currentCategory.equals("")) {
                int levelId_ONE = level1.getLevelId();
                List<CategoryDB> departmentChild = manager.getDepartmentChild(1 + levelId_ONE, level1.getDepartmentId());

                DynamicObject unnestedParent = DynamicObject.createOptionForAll(level1);
                nestedParentList.add(unnestedParent);

                // Every parent gets a few nested-parents
                // (we set their depth to 2 in order to facilitate the single expanded parent feature)
                for (int j = 0; j < departmentChild.size(); j++) {
                    CategoryDB level2 = departmentChild.get(j);
                    int levelId_TWO = level2.getLevelId();
                    List<CategoryDB> departmentChildLvl2 = manager.getDepartmentChild(1 + levelId_TWO, level2.getDepartmentId());

                    DynamicObject nestedParent = new DynamicObject(level2, departmentChildLvl2.size());
                    nestedParentList.add(nestedParent);

                    // Every nested parent gets some children
                    List<DynamicObject> children = new ArrayList<>();
                    unnestedParent = DynamicObject.createOptionForAll(level2);
                    children.add(unnestedParent);

                    for (int k = 0; k < departmentChildLvl2.size(); k++) {
                        CategoryDB level3 = departmentChildLvl2.get(k);
                        children.add(new DynamicObject(level3, departmentChildLvl2.size()));
                    }

                    nestedParent.addChild(children, 2);
                }

                DynamicObject customParentObject = new DynamicObject(level1, departmentChild.size());
                customParentObject.addChild(nestedParentList, 1);
                parentObjectList.add(customParentObject);
            }
        }
        Log.d(TAG, "parentObjectList size " + parentObjectList.size());
        return parentObjectList;
    }
}
