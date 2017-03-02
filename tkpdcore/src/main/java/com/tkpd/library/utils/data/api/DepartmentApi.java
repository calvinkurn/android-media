package com.tkpd.library.utils.data.api;


import com.tkpd.library.utils.data.model.Department;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by m.normansyah on 2/9/16.
 */
public interface DepartmentApi {
    @GET("/v1/categories?filter=type==tree")
    Observable<Department> getAllCategory();
}
